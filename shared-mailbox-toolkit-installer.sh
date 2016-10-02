#!/bin/bash

# Copyright (C) 2014-2016  Barry de Graaff
# 
# Bugs and feedback: https://github.com/Zimbra-Community/pgp-zimlet/issues
# 
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 2 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see http://www.gnu.org/licenses/.

set -e
# if you want to trace your script uncomment the following line
#set -x

echo "Shared Mailbox Toolkit installer"

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

if [ -z "$1"  ]
then
   one=$(whiptail --title "Shared Mailbox Toolkit Installer" --checklist "Choose components to install. CLI commands are always installed." 15 60 4 \
   "Client Zimlet" "" on \
   "Admin Zimlet and extension" "" on \
   "X-Authenticated-User header" "" on --clear 3>&1 1>&2 2>&3)
   if [ "$?" = "1" ]
   then
      echo "cancelled by user"
      exit 0
   fi   
   #Change bash script parameter, aka one cannot do something like 1=$(whiptail....) to set $1
   set -- "$one" "$2"
fi

echo "Check if git installed."
set +e
YUM_CMD=$(which yum)
APT_CMD=$(which apt-get)
GIT_CMD=$(which git)
set -e 

if [[ -z $GIT_CMD ]]; then
   if [[ ! -z $YUM_CMD ]]; then
      yum install -y git
   else
      apt-get install -y git
   fi
fi

TMPFOLDER="$(mktemp -d /tmp/shared-mailbox-toolkit-installer.XXXXXXXX)"
echo "Download Shared Mailbox Toolkit to $TMPFOLDER"
cd $TMPFOLDER
git clone https://github.com/Zimbra-Community/shared-mailbox-toolkit
cd shared-mailbox-toolkit

if [[ $1 == *"Client Zimlet"* ]]
then
   echo "Deploy client Zimlet"
   rm -Rf /opt/zimbra/zimlets-deployed/_dev/tk_barrydegraaff_sharetoolkit_client
   mkdir -p /opt/zimbra/zimlets-deployed/_dev/
   cp -rv tk_barrydegraaff_sharetoolkit_client /opt/zimbra/zimlets-deployed/_dev/tk_barrydegraaff_sharetoolkit_client
fi

if [[ $1 == *"Admin Zimlet and extension"* ]]
then
   echo "Deploy admin Zimlet"
   su - zimbra -c "zmzimletctl undeploy tk_barrydegraaff_sharetoolkit_admin"
   rm -f /tmp/tk_barrydegraaff_sharetoolkit_admin.zip
   cd tk_barrydegraaff_sharetoolkit_admin
   zip -r /tmp/tk_barrydegraaff_sharetoolkit_admin.zip *
   cd ..
   su - zimbra -c "zmzimletctl deploy /tmp/tk_barrydegraaff_sharetoolkit_admin.zip"

   echo "Deploy Java server extension"
   rm -Rf /opt/zimbra/lib/ext/ShareToolkit
   mkdir -p /opt/zimbra/lib/ext/ShareToolkit
   cp -v extension/ShareToolkit/out/artifacts/ShareToolkit/ShareToolkit.jar /opt/zimbra/lib/ext/ShareToolkit/
fi

if [[ $1 == *"X-Authenticated-User header"* ]]
then
   echo "Enable X-Authenticated-User header"
   su - zimbra -c "zmprov mcf zimbraSmtpSendAddAuthenticatedUser TRUE"
fi

echo "Deploy CLI tools"
cp -rv bin/* /usr/local/sbin/


echo "Flushing Zimlet Cache"
su - zimbra -c "zmprov fc all"

echo "--------------------------------------------------------------------------------------------------------------
Shared Mailbox Toolkit installed successful"

if [[ $1 == *"X-Authenticated-User header"* ]] || [[ $1 == *"Admin Zimlet and extension"* ]];
then
echo "You still need to restart some services to load the changes:"
fi

if [[ $1 == *"X-Authenticated-User header"* ]]
then
   echo "su - zimbra -c \"zmmtactl restart\""
fi

if [[ $1 == *"Admin Zimlet and extension"* ]]
then
   echo "su - zimbra -c \"zmmailboxdctl restart\""
fi


#!/bin/bash

echo "This is a provisional installer for development purpose only, do not use in production"

echo "Deploy client Zimlet"
rm -Rf /opt/zimbra/zimlets-deployed/_dev/tk_barrydegraaff_sharetoolkit_client
su - zimbra -c "zmzimletctl undeploy tk_barrydegraaff_sharetoolkit_admin"
cp -rv tk_barrydegraaff_sharetoolkit_client /opt/zimbra/zimlets-deployed/_dev/tk_barrydegraaff_sharetoolkit_client

echo "Deploy admin Zimlet"
rm -f /tmp/tk_barrydegraaff_sharetoolkit_admin.zip
cd tk_barrydegraaff_sharetoolkit_admin
zip -r /tmp/tk_barrydegraaff_sharetoolkit_admin.zip *
cd ..
su - zimbra -c "zmzimletctl deploy /tmp/tk_barrydegraaff_sharetoolkit_admin.zip"

echo "Deploy CLI tools"
cp -urv bin/* /usr/local/sbin/

echo "Deploy Java server extension"
rm -Rf /opt/zimbra/lib/ext/ShareToolkit
mkdir -p /opt/zimbra/lib/ext/ShareToolkit
cp extension/ShareToolkit/out/artifacts/ShareToolkit/ShareToolkit.jar /opt/zimbra/lib/ext/ShareToolkit/
su - zimbra -c "zmmailboxdctl restart"


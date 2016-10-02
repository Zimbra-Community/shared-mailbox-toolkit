Zimbra Shared Mailbox Toolkit (beta)
==========

If you find Zimbra Shared Mailbox Toolkit useful and want to support its continued development, you can make donations via:
- PayPal: info@barrydegraaff.tk
- Bank transfer: IBAN NL55ABNA0623226413 ; BIC ABNANL2A

Demo video: https://youtu.be/8dzVqPENaBk

Do you have generic mailboxes for you sales department, helpdesk or info@yourcompany.com, use Zimbra Shared Mailbox Toolkit to 
share those mailboxes with your staff. For long time Zimbra users: this toolkit is meant implement family mailboxes for 
Zimbra 8.

Designed for Zimbra version 8.7.

Bugs and feedback: https://github.com/Zimbra-Community/shared-mailbox-toolkit/issues

Report security issues to barrydg@zetalliance.org (PGP fingerprint: 9e0e165f06b365ee1e47683e20f37303c20703f8)

Stay up-to-date: new releases are announced on the users mailing list: http://lists.zetalliance.org/mailman/listinfo/users_lists.zetalliance.org

========================================================================

### Install prerequisites
  - No special requirements

### Installing
Use the automated installer:

    wget https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/shared-mailbox-toolkit-installer.sh -O /tmp/shared-mailbox-toolkit-installer.sh
    chmod +rx /tmp/shared-mailbox-toolkit-installer.sh
    /tmp/shared-mailbox-toolkit-installer.sh


========================================================================

### Screenshot of Admin Zimlet and extension
No need to have CLI access to create/revoke shares.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/admin-zimlet.png)

### Screenshot of Client Zimlet
The client Zimlet will automatically expand the shared mailbox, so the Inbox is displayed instead of `No results found`.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/client-zimlet.png)

### Screenshot of additional header
An additional header is added to the email to identify the user sending email from a shared mailbox. For example it can help finding out what sales person was sending out mail from sales@myzimbra.com. The header can be set via the installer or running `   su - zimbra -c "zmprov mcf zimbraSmtpSendAddAuthenticatedUser TRUE"` and `u - zimbra -c "zmmtactl restart"`.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/245ed7b8e4799004e81c4aa844bf6a0edf679f77/help/header.png)

### Screenshot of the installer
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/installer.png)

### License

Copyright (C) 2015-2016  Barry de Graaff [Zeta Alliance](http://www.zetalliance.org/)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.

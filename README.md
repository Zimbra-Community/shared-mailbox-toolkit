Zimbra Shared Mailbox Toolkit
==========

If you find Zimbra Shared Mailbox Toolkit useful and want to support its continued development, you can make donations via:
- PayPal: info@barrydegraaff.tk
- Bank transfer: IBAN NL55ABNA0623226413 ; BIC ABNANL2A

Demo video: https://youtu.be/8dzVqPENaBk

Do you have generic mailboxes for you sales department, helpdesk or info@yourcompany.com, use Zimbra Shared Mailbox Toolkit to 
share those mailboxes with your staff. For long time Zimbra users: this toolkit is meant to bring back family mailboxes to Zimbra 8+.

By default shares are created with SendAs rights and read/write access (rwixd). This allows the user to email on behalf of the shared account and sent mail will be go to the shared account sent folder. An outgoing mail filter and persona is set to achieve this.

Supported Zimbra versions: 8.8.11.

Bugs and feedback: https://github.com/Zimbra-Community/shared-mailbox-toolkit/issues

Report security issues to info@barrydegraaff.tk (PGP fingerprint: 97f4694a1d9aedad012533db725ddd156d36a2d0)

### Known issues

If you are using multiple email domains (example.com, example.nl), and the shared account is not in the same domain as the users account, there will be spf/dkim misalignment. See: https://github.com/Zimbra-Community/shared-mailbox-toolkit/issues/42

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
No need to have CLI access to create/revoke root shares.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/admin-zimlet.png)

### Screenshot of Client Zimlet
The client Zimlet will automatically expand the shared mailbox, so the Inbox is displayed instead of `No results found`.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/client-zimlet.png)
The client Zimlet will not show duplicated options when selecting FROM address.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/from.png)

### Screenshot of additional header
Optionally an additional header is added to the email to identify the user sending email from a shared mailbox. For example it can help finding out what sales person was sending out mail from sales@myzimbra.com. The header can be set via the installer or running `   su - zimbra -c "zmprov mcf zimbraSmtpSendAddAuthenticatedUser TRUE"` and `u - zimbra -c "zmmtactl restart"`.
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/245ed7b8e4799004e81c4aa844bf6a0edf679f77/help/header.png)

### Screenshot of the installer
![alt tag](https://raw.githubusercontent.com/Zimbra-Community/shared-mailbox-toolkit/master/help/installer.png)

### CLI Commands
Installed in /usr/local/sbin an can be run as user `zimbra`:
* `subzim`: Share an entire mailbox with another mailbox (root share).
* `unsubzim`: Revoke share created by subzim command (revoke root share).
* `personagen`: Generate personas/identities for all aliasses of an account.
* `removeshares`: Revoke all shares from an account. One can use this for managing people going into a different department, or in case the web UI fails to load due to sharing bugs.
* `submachinegun`: Use this in case you are missing folders in your shared account. Read in-script comments for more details.

### Enable the Admin Zimlet for delegated admins

    zmprov ma testadmin@example.com +zimbraAdminConsoleUIComponents zimbraClientUploadView
    zmprov grr global usr testadmin@example.com adminConsoleClientUploadRights
    zmprov fc all

### License

Copyright (C) 2015-2019  Barry de Graaff [Zeta Alliance](https://zetalliance.org/) and the _addSendAsOrSendOboAddresses patch by Michele [Olivo ZeXtras](https://www.zextras.com)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.

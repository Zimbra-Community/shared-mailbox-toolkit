/*

Copyright (C) 2016-2018  Barry de Graaff

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

*/
package tk.barrydegraaff.toolkit;

import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.AuthToken;
import com.zimbra.soap.DocumentHandler;
import com.zimbra.soap.ZimbraSoapContext;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShareToolkitSoapHandler extends DocumentHandler {
    public Element handle(Element request, Map<String, Object> context)
            throws ServiceException {
        try {

            ZimbraSoapContext zsc = getZimbraSoapContext(context);
            AuthToken authToken = zsc.getAuthToken();
            Account acct = authToken.getAccount();

            Element response = zsc.createElement(
                    "ShareToolkitResponse"
            );
            Element shareToolkitResult = response.addUniqueElement("shareToolkitResult");

            switch (request.getAttribute("action")) {
                case "getAccounts":
                    if (acct.isIsAdminAccount()) {
                        //isIsAdminAccount: is Global Admin
                        shareToolkitResult.setText(this.runCommand("/usr/local/sbin/acctalias", "", "", "", ""));
                    } else {
                        //give this admin only access to email address in the same domain as the admin account
                        shareToolkitResult.setText(this.runCommand("/usr/local/sbin/acctalias", extractDomain(acct.getName()), "", "", ""));
                    }
                    break;
                case "createShare":
                case "removeShare":
                    //In case a delegated admin account is used, only allow operation on accounts in the same domain.
                    if (!acct.isIsAdminAccount()) {
                        final String adminDomain = extractDomain(acct.getName());
                        final String accountADomain = extractDomain(request.getAttribute("accounta"));
                        final String accountBDomain = extractDomain(request.getAttribute("accountb"));
                        if (!areStringsEqual(adminDomain, accountADomain, accountBDomain)) {
                            shareToolkitResult.setText("You are not authorized for this domain.");
                            return response;
                        }
                    }
                    if ((this.validate(request.getAttribute("accountb"))) && (this.validate(request.getAttribute("accounta")))) {
                        final String skipPersonaCreation;
                        if ("true".equals(request.getAttribute("skipPersonaCreation"))) {
                            skipPersonaCreation = "N";
                        } else {
                            skipPersonaCreation = "Y";
                        }

                        final String permissions;
                        if (("r".equals(request.getAttribute("permissions"))) ||
                                ("rw".equals(request.getAttribute("permissions"))) ||
                                ("rwix".equals(request.getAttribute("permissions"))) ||
                                ("rwixd".equals(request.getAttribute("permissions"))) ||
                                ("rwixda".equals(request.getAttribute("permissions"))) ||
                                ("none".equals(request.getAttribute("permissions")))) {
                            permissions = request.getAttribute("permissions");
                        } else {
                            permissions = "rwixd";
                        }

                        if (request.getAttribute("action").equals("createShare")) {
                            this.runCommand("/usr/local/sbin/subzim", request.getAttribute("accountb"), request.getAttribute("accounta"), skipPersonaCreation, permissions);
                        } else if (request.getAttribute("action").equals("removeShare")) {
                            this.runCommand("/usr/local/sbin/unsubzim", request.getAttribute("accountb"), request.getAttribute("accounta"), skipPersonaCreation, permissions);
                        }

                        shareToolkitResult.setText("");
                    } else {
                        shareToolkitResult.setText("Invalid email address specified.");
                    }
                case "createPersonas":
                    //In case a delegated admin account is used, only allow operation on accounts in the same domain.
                    if (!acct.isIsAdminAccount()) {
                        final String adminDomain = extractDomain(acct.getName());
                        final String accountADomain = extractDomain(request.getAttribute("accounta"));
                        if (!areStringsEqual(adminDomain, accountADomain, accountADomain)) {
                            shareToolkitResult.setText("You are not authorized for this domain.");
                            return response;
                        }
                    }
                    if (this.validate(request.getAttribute("accounta"))) {
                        this.runCommand("/usr/local/sbin/personagen", request.getAttribute("accounta"), "", "", "");
                        shareToolkitResult.setText("");
                    } else {
                        shareToolkitResult.setText("Invalid email address specified.");
                    }
                    break;
            }
            return response;

        } catch (
                Exception e) {
            throw ServiceException.FAILURE("ShareToolkitSoapHandler ServiceException ", e);
        }

    }

    /*
    areStringsEqual has been written by ChatGPT
    * */
    public static boolean areStringsEqual(String str1, String str2, String str3) {
        // Using the equals method to compare strings for equality
        return str1.equals(str2) && str2.equals(str3);
    }

    /*
    extractDomain has been written by ChatGPT
    * */
    public static String extractDomain(String emailAddress) {
        // Split the email address into local part and domain part
        String[] parts = emailAddress.split("@");

        // Check if the email address is valid
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid email address format");
        }

        // Return the domain part
        return parts[1];
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private String runCommand(String cmd, String arg1, String arg2, String arg3, String arg4) throws ServiceException {
        try {
            ProcessBuilder pb = new ProcessBuilder()
                    .command(cmd, arg1, arg2, arg3, arg4)
                    .redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader cmdOutputBuffer = new BufferedReader(new InputStreamReader(p.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String aux = "";
            while ((aux = cmdOutputBuffer.readLine()) != null) {
                builder.append(aux);
                builder.append(';');
            }
            String cmdResult = builder.toString();
            return cmdResult;

        } catch (
                Exception e) {
            throw ServiceException.FAILURE("ShareToolkitSoapHandler runCommand exception", e);
        }
    }

}

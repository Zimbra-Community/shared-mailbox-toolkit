/*

Copyright (C) 2016  Barry de Graaff

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
            Element response = zsc.createElement(
                    "ShareToolkitResponse"
            );
            Element shareToolkitResult = response.addUniqueElement("shareToolkitResult");

            switch (request.getAttribute("action")) {
                case "getAccounts":
                    shareToolkitResult.setText(this.runCommand("/usr/local/sbin/acctalias", "", ""));
                    break;
                case "createShare":
                case "removeShare":
                    if ((this.validate(request.getAttribute("accountb"))) && (this.validate(request.getAttribute("accounta")))) {
                        if (request.getAttribute("action").equals("createShare")) {
                            this.runCommand("/usr/local/sbin/subzim", request.getAttribute("accountb"), request.getAttribute("accounta"));
                        } else if (request.getAttribute("action").equals("removeShare")) {
                            this.runCommand("/usr/local/sbin/unsubzim", request.getAttribute("accountb"), request.getAttribute("accounta"));
                        }

                        shareToolkitResult.setText("");
                    } else {
                        shareToolkitResult.setText("Invalid email address specified.");
                    }
                case "createPersonas":
                    if (this.validate(request.getAttribute("accounta"))) {
                        this.runCommand("/usr/local/sbin/personagen", request.getAttribute("accounta"), "");
                        shareToolkitResult.setText("");
                    } else {
                        shareToolkitResult.setText("Invalid email address specified.");
                    }
                    break;
            }
            return response;

        } catch (
                Exception e)

        {
            throw ServiceException.FAILURE("ShareToolkitSoapHandler ServiceException ", e);
        }

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private String runCommand(String cmd, String arg1, String arg2) throws ServiceException {
        try {
            ProcessBuilder pb = new ProcessBuilder()
                    .command(cmd, arg1, arg2)
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
                Exception e)

        {
            throw ServiceException.FAILURE("ShareToolkitSoapHandler runCommand exception", e);
        }
    }

}

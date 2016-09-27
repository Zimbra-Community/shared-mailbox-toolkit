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
import java.io.*;


public class ShareToolkitSoapHandler extends DocumentHandler {
    public Element handle(Element request, Map<String, Object> context)
            throws ServiceException {
        try {

            ZimbraSoapContext zsc = getZimbraSoapContext(context);
            Element response = zsc.createElement(
                    "ShareToolkitResponse"
            );

            switch (request.getAttribute("action"))
            {
                case "getAccounts":

                    Element users = response.addUniqueElement("users");
                    users.setText(this.runCommand("/opt/zimbra/bin/zmprov -l gaa"));
                    break;
                case "createShare":
                    Element createShareResult = response.addUniqueElement("createShareResult");
                    if((this.validate(request.getAttribute("accountb")))&&(this.validate(request.getAttribute("accounta"))))
                    {
                        this.runCommand("/usr/local/sbin/subzim " + request.getAttribute("accountb")  + " " + request.getAttribute("accounta"));
                        createShareResult.setText("");
                    }
                    else
                    {
                        createShareResult.setText("Invalid email address specified.");
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
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    private String runCommand(String cmd) throws ServiceException
    {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(cmd);
            BufferedReader cmdOutputBuffer = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String aux= "";
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

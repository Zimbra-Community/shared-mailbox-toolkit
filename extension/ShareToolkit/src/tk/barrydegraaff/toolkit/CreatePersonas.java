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

import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.Element;
import com.zimbra.common.soap.SoapParseException;
import com.zimbra.soap.DocumentHandler;
import com.zimbra.soap.ZimbraSoapContext;
import java.io.*;


public class CreatePersonas extends DocumentHandler {
    public Element handle(Element request, Map<String, Object> context)
            throws ServiceException {
        try {
            ZimbraSoapContext zsc = getZimbraSoapContext(context);

            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("/opt/zimbra/bin/zmprov -l gaa");

            BufferedReader cmdOutputBuffer = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String aux= "";
            while ((aux = cmdOutputBuffer.readLine()) != null) {
                builder.append(aux);
            }
            String cmdResult = builder.toString();

            Element response = zsc.createElement(
                    "CreatePersonasResponse"
            );
            Element elStart = response.addUniqueElement("start");
            Element elEnd = response.addUniqueElement("end");
            elStart.setText(request.getAttribute("user"));
            elEnd.setText(cmdResult);
            return response;

        } catch (
                Exception e)

        {
            throw ServiceException.FAILURE("exception occurred handling command", e);
        }

    }
}

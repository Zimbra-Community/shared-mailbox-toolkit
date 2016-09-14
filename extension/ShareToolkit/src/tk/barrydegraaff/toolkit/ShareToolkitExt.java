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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zimbra.common.localconfig.LC;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.StringUtil;
import com.zimbra.cs.account.Server;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.Entry;
import com.zimbra.cs.extension.ZimbraExtension;
import com.zimbra.soap.SoapServlet;


public class ShareToolkitExt implements ZimbraExtension {
    public void destroy() {
    }

    public String getName() {
        return "ShareToolkitExt";
    }

    public void init() throws ServiceException {
        SoapServlet.addService("AdminServlet", new ShareToolkitService());
    }

}



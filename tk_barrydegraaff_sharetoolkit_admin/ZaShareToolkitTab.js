/*
Copyright (C) 2014-2016  Barry de Graaff

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
ZaShareToolkitTab = function(parent, entry) {
    if (arguments.length == 0) return;
    ZaTabView.call(this, parent,"ZaShareToolkitTab");
    ZaTabView.call(this, {
        parent:parent,
        iKeyName:"ZaShareToolkitTab",
        contextId:"SHARE_TOOLKIT"
    });
    this.setScrollStyle(Dwt.SCROLL);

    document.getElementById('ztab__SHARE_TOOLKIT').innerHTML = 'Running zmprov -l gad';
    document.getElementById('ztab__SHARE_TOOLKIT').innerHTML = 'Running zmprov -l gad';

   var soapDoc = AjxSoapDoc.create("STCreatePersonas", "urn:ShareToolkit", null);
      soapDoc.getMethod().setAttribute("type", "all");
      soapDoc.getMethod().setAttribute("user", "test@domain.com");
      var csfeParams = new Object();
      csfeParams.soapDoc = soapDoc;
      csfeParams.asyncMode = false;
      try {
          var reqMgrParams = {} ;
          resp = ZaRequestMgr.invoke(csfeParams, reqMgrParams);
          document.getElementById('ztab__SHARE_TOOLKIT').innerHTML = "All accounts on server<br> <textarea style=\"width:600px; height:400px\">" + JSON.stringify(resp) + "</textarea>";  
     }catch (ex) {
        document.getElementById('ztab__SHARE_TOOLKIT').innerHTML = "Exception " + ex;  
     }    
       
    
}


ZaShareToolkitTab.prototype = new ZaTabView();
ZaShareToolkitTab.prototype.constructor = ZaShareToolkitTab;
//ZaTabView.XFormModifiers["ZaShareToolkitTab"] = new Array();

ZaShareToolkitTab.prototype.getTabIcon =
    function () {
        return "ClientUpload" ;
    }

ZaShareToolkitTab.prototype.getTabTitle =
    function () {
        return "Share Toolkit";
    }

ZaShareToolkitTab.prototype.getTitle =
    function () {
        return tk_barrydegraaff_sharetoolkit_admin.Client_upload_title;
    }

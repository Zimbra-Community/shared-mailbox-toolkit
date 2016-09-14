# Extension Template

Here is an example of an Admin extension it needs to be copied to : /opt/zimbra/lib/ext/ShareToolkit/ShareToolkit.jar

The jar file can be compiled with intelliJ and the front end request (copy paste in browser console) can be:


       var soapDoc = AjxSoapDoc.create("STCreatePersonas", "urn:ShareToolkit", null);
       soapDoc.getMethod().setAttribute("type", "all");
       soapDoc.getMethod().setAttribute("user", "test@domain.com");
       var csfeParams = new Object();
       csfeParams.soapDoc = soapDoc;
       csfeParams.asyncMode = false;
       try {
           var reqMgrParams = {} ;
           resp = ZaRequestMgr.invoke(csfeParams, reqMgrParams);
           console.log(resp);  
      }catch (ex) {
         console.log(ex);
      }


The extension will send a response with the user attribute from the request:
"Body":{"CreatePersonasResponse":{"start":{"_content":"test@domain.com"},"end":{}}},"_jsns":"urn:zimbraSoap"}

I am keeping a copy of this for reference.

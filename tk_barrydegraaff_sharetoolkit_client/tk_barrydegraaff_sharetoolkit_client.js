/**
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

//Constructor
function tk_barrydegraaff_sharetoolkit_client_HandlerObject() {
};


tk_barrydegraaff_sharetoolkit_client_HandlerObject.prototype = new ZmZimletBase();
tk_barrydegraaff_sharetoolkit_client_HandlerObject.prototype.constructor = tk_barrydegraaff_sharetoolkit_client_HandlerObject;

tk_barrydegraaff_sharetoolkit_client_HandlerObject.prototype.toString =
function() {
   return "tk_barrydegraaff_sharetoolkit_client_HandlerObject";
};

/** 
 * Creates the Zimlet, extends {@link https://files.zimbra.com/docs/zimlet/zcs/8.6.0/jsapi-zimbra-doc/symbols/ZmZimletBase.html ZmZimletBase}.
 * @class
 * @extends ZmZimletBase
 *  */
var ShareToolkitClient = tk_barrydegraaff_sharetoolkit_client_HandlerObject;

/** 
 * This method gets called when Zimbra Zimlet framework initializes.
 * ShareToolkitClient uses the init function to load openpgp.js and configure the user settings and runtime variables.
 */
ShareToolkitClient.prototype.init = function() {

    /* _addSendAsOrSendOboAddresses by Michele Olivo ZeXtras
     * If a user has configured a persona and has sendAs rights for mailbox@example.com, using
     * this patch, Zimbra will only display the persona in the FROM drop-down in the compose view.
     * If the user has no persona, Zimbra will display the value derived from sendAs.
     * The default behavior is to display all (and this clutters the FROM options list with dupes).
    * */
   ZmComposeView.prototype._addSendAsOrSendOboAddresses  =
   function(menu, emails, isObo, displayValueFunc) {
     var identities = appCtxt.getIdentityCollection().getIdentities(true),
       identityMap = {};
   
     for (var j = 0; j < identities.length; j += 1) {
       identityMap[identities[j].getField("SEND_FROM_ADDRESS")] = identities[j];
     }
   
     for (var i = 0; i < emails.length; i++) {
       var email = emails[i],
         addr = email.addr,
         displayValue = displayValueFunc(addr, email.displayName),
         extraData = {isDL: email.isDL, isObo: isObo},
         optData = new DwtSelectOptionData(addr, displayValue, null, null, null, null, extraData);
   
       if (identityMap.hasOwnProperty(addr)) {
         continue; // Skip identity already added.
       }
       menu.addOption(optData);
     }; 
   }
}


 /**
 * Called when a left click occurs (by the tree view listener). The folder that
 * was clicked may be a search, since those can appear in Trash within the folder tree. The
 * appropriate search will be performed.
 *
 * @param {ZmOrganizer}		folder		the folder or search that was clicked
 * 
 * @private
 */
ZmFolderTreeController.prototype._itemClicked = function(folder, openInTab) {

	// bug 41196 - turn off new mail notifier if inactive account folder clicked
	if (appCtxt.isOffline) {
		var acct = folder.getAccount();
		if (acct && acct.inNewMailMode) {
			acct.inNewMailMode = false;
			var allContainers = appCtxt.getOverviewController()._overviewContainer;
			for (var i in allContainers) {
				allContainers[i].updateAccountInfo(acct, true, true);
			}
		}
	}

	if (folder.type == ZmOrganizer.SEARCH) {
		// if the clicked item is a search (within the folder tree), hand
		// it off to the search tree controller
		var stc = this._opc.getTreeController(ZmOrganizer.SEARCH);
		stc._itemClicked(folder, openInTab);
	} else if (folder.id == ZmFolder.ID_ATTACHMENTS) {
		var attController = AjxDispatcher.run("GetAttachmentsController");
		attController.show();
	}
    else {
		var searchFor = ZmId.SEARCH_MAIL;
		if (folder.isInTrash()) {
			var app = appCtxt.getCurrentAppName();
			// if other apps add Trash to their folder tree, set appropriate type here:
			if (app == ZmApp.CONTACTS) {
				searchFor = ZmItem.CONTACT;
			}
		}
		var sc = appCtxt.getSearchController();
		var acct = folder.getAccount();

		var sortBy = appCtxt.get(ZmSetting.SORTING_PREF, folder.nId);
		if (!sortBy) {
			sortBy = (sc.currentSearch && folder.nId == sc.currentSearch.folderId) ? null : ZmSearch.DATE_DESC;
		}
		else {
			//user may have saved folder with From search then switched views; don't allow From sort in conversation mode
			var groupMode = appCtxt.getApp(ZmApp.MAIL).getGroupMailBy();
			if (groupMode == ZmItem.CONV && (sortBy == ZmSearch.NAME_ASC || sortBy == ZmSearch.NAME_DESC)) {
				sortBy = appCtxt.get(ZmSetting.SORTING_PREF, appCtxt.getCurrentViewId());  //default to view preference
				if (!sortBy) {
					sortBy = ZmSearch.DATE_DESC; //default
				}
				appCtxt.set(ZmSetting.SORTING_PREF, sortBy, folder.nId);
			}
		}
      if ((folder._isRemote == true) && (folder.oname == "USER_ROOT"))
      {
         console.log("ShareToolkitClient: shared mailbox detected, rewrite query to /Inbox");
         var query = folder.createQuery();
         query = query.substring(0,query.length -1);
         query = query + '/inbox"';
         
      }
      else
      {
         var query = folder.createQuery();
      }
		var params = {
			query:          query,
			searchFor:      searchFor,
			getHtml:        folder.nId == ZmFolder.ID_DRAFTS || appCtxt.get(ZmSetting.VIEW_AS_HTML),
			types:          folder.nId == ZmOrganizer.ID_SYNC_FAILURES ? [ZmItem.MSG] : null, // for Sync Failures folder, always show in traditional view
			sortBy:         sortBy,
			accountName:    acct && acct.name,
			userInitiated:  openInTab,
			origin:         ZmId.SEARCH
		};

		sc.resetSearchAllAccounts();

		if (appCtxt.multiAccounts) {
			// make sure we have permissions for this folder (in case an "external"
			// server was down during account load)
			if (folder.link && folder.perm == null) {
				var folderTree = appCtxt.getFolderTree(acct);
				if (folderTree) {
					var callback = new AjxCallback(this, this._getPermissionsResponse, [params]);
					folderTree.getPermissions({callback:callback, folderIds:[folder.id]});
				}
				return;
			}

			if (appCtxt.isOffline && acct.hasNotSynced() && !acct.__syncAsked) {
				acct.__syncAsked = true;

				var dialog = appCtxt.getYesNoMsgDialog();
				dialog.registerCallback(DwtDialog.YES_BUTTON, this._syncAccount, this, [dialog, acct]);
				dialog.setMessage(ZmMsg.neverSyncedAsk, DwtMessageDialog.INFO_STYLE);
				dialog.popup();
			}
		}

		sc.search(params);
	}
};




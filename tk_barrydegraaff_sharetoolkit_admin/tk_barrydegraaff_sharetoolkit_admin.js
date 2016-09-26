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
if(appNewUI && ZaSettings){
    if(window.console && window.console.log) console.log("Start loading tk_barrydegraaff_sharetoolkit_admin.js");
    function ZaShareToolkit() {
        ZaItem.call(this,"ZaShareToolkit");
        this._init();
        this.type = "ZaShareToolkit";
    };
    ZaShareToolkit.prototype = new ZaItem;
    ZaShareToolkit.prototype.constructor = ZaShareToolkit;

    ZaItem.loadMethods["ZaShareToolkit"] = new Array();
    ZaItem.modifyMethods["ZaShareToolkit"] = new Array();
    ZaItem.initMethods["ZaShareToolkit"] = new Array();

    ZaOperation.SHARE_TOOLKIT = ++ZA_OP_INDEX;
    ZaZimbraAdmin._SHARE_TOOLKIT_VIEW = ZaZimbraAdmin.VIEW_INDEX++;

    ZaApp.prototype.getShareToolkitViewController =
        function() {
            if (this._controllers[ZaZimbraAdmin._SHARE_TOOLKIT_VIEW] == null)
                this._controllers[ZaZimbraAdmin._SHARE_TOOLKIT_VIEW] = new ZaShareToolkitController(this._appCtxt, this._container);
            return this._controllers[ZaZimbraAdmin._SHARE_TOOLKIT_VIEW];
        }

    ZaShareToolkit.TreeListener = function (ev) {
        var clientUploader = new ZaShareToolkit();

        if(ZaApp.getInstance().getCurrentController()) {
            ZaApp.getInstance().getCurrentController().switchToNextView(ZaApp.getInstance().getShareToolkitViewController(),ZaShareToolkitController.prototype.show, [clientUploader]);
        } else {
            ZaApp.getInstance().getShareToolkitViewController().show(clientUploader);
        }
    }

    ZaShareToolkit.TreeModifier = function (tree) {
        var overviewPanelController = this ;
        if (!overviewPanelController) throw new Exception("ZaShareToolkit.TreeModifier: Overview Panel Controller is not set.");
        if(ZaSettings.ENABLED_UI_COMPONENTS[ZaSettings.Client_UPLOAD_VIEW] || ZaSettings.ENABLED_UI_COMPONENTS[ZaSettings.CARTE_BLANCHE_UI]) {
            var parentPath = ZaTree.getPathByArray([ZaMsg.OVP_home, ZaMsg.OVP_toolMig]);

            var ti = new ZaTreeItemData({
                parent:parentPath,
                id:ZaId.getTreeItemId(ZaId.PANEL_APP,"magHV",null, "ShareToolkitHV"),
                text: "Share Toolkit",
                mappingId: ZaZimbraAdmin._SHARE_TOOLKIT_VIEW});
            tree.addTreeItemData(ti);

            if(ZaOverviewPanelController.overviewTreeListeners) {
                ZaOverviewPanelController.overviewTreeListeners[ZaZimbraAdmin._SHARE_TOOLKIT_VIEW] = ZaShareToolkit.TreeListener;
            }
        }
    }

    if(ZaOverviewPanelController.treeModifiers)
        ZaOverviewPanelController.treeModifiers.push(ZaShareToolkit.TreeModifier);

}


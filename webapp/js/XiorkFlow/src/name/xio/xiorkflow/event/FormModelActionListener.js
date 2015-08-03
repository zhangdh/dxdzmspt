
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author xul
 */
function FormModelActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
FormModelActionListener.prototype.actionPerformed = function (obj) {
	
	var wrapper = this.xiorkFlow.getWrapper();
	var toolbar = this.xiorkFlow.getToolBar();
	var name = wrapper.getModel().getName();
	var model = wrapper.getModel();
	var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
	if (selectedMetaNodeModels == null || selectedMetaNodeModels == "") {
		alert("没有选择任何节点");
		return;
	}
	 var obj = new Object();
	 obj.xiorkFlow=this.xiorkFlow;
	var modelId=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"GetFormModelList", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
	if(modelId!=null){
	  wrapper.selectedModel=modelId;
	}
};


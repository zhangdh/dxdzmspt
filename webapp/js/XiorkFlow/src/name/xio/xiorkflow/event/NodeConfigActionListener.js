
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author xul
 */
function NodeConfigActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
NodeConfigActionListener.prototype.actionPerformed = function (obj) {
	
	var wrapper = this.xiorkFlow.getWrapper();
	var toolbar = this.xiorkFlow.getToolBar();
	var name = wrapper.getModel().getName();
	var model = wrapper.getModel();
	var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
	var obj = new Object();
	obj.xiorkFlow=this.xiorkFlow;
	if (selectedMetaNodeModels == null || selectedMetaNodeModels == "") {
		//显示流程属性窗口
		window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"oswf/graphicdesigner/default.do?method=showWkAttr", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:auto;status:no;resizable:yes;");
	}else{
	//显示节点配置窗口
		window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"oswf/graphicdesigner/default.do?method=showNodeConfig", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:auto;status:no;resizable:yes;");
	}
	//var modelId=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"GetFormModelList", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
	//if(modelId!=null){
	  //wrapper.selectedModel=modelId;
	//}
};


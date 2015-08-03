
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author renwy
 */
function FunctionActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
FunctionActionListener.prototype.actionPerformed = function (obj) {
	
	var wrapper = this.xiorkFlow.getWrapper();
	var toolbar = this.xiorkFlow.getToolBar();
	var name = wrapper.getModel().getName();
	var model = wrapper.getModel();
	//var doc = XiorkFlowModelConverter.convertModelToXML(model);
	var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
	if (selectedMetaNodeModels == null || selectedMetaNodeModels == "") {
		alert("没有选择任何节点");
		return;
	}
	//打开权限选择面板
	//window.showModalDialog
	var nodeID = selectedMetaNodeModels.get(0).getID();
	 var obj = new Object();
      obj.nodeId=nodeID;
      obj.xiorkFlow=this.xiorkFlow;
      if(selectedMetaNodeModels.get(0).type=="START_NODE"){
      obj.startNode="yes";
      }
	var functionConfig=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"/oswf/graphicdesigner/function/function_config.jsp", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
	 if(functionConfig!=null){
		 //是否已经设置
			var functionConfigModels=model.selectedFunctionConfigModels;
			var  doc=XMLDocument.newDomcument(); 
		    doc.async="false"; 
			doc.loadXML(functionConfig);
			var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
			for(var i=0;i<functionConfigModels.length;i++){
				 var xml=functionConfigModels[i];
				 var  xmlDoc=XMLDocument.newDomcument(); 
				 xmlDoc.async="false"; 
				 xmlDoc.loadXML(xml);
				 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				 if(stepId==currentStepId){
				    model.selectedFunctionConfigModels.removeById(i);
				    break;
				 }
			}
		model.selectedFunctionConfigModels.add(functionConfig);
	}
};



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author renwy
 */
function ConditionActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
ConditionActionListener.prototype.actionPerformed = function (obj) {
	
	var wrapper = this.xiorkFlow.getWrapper();
	var toolbar = this.xiorkFlow.getToolBar();
	var name = wrapper.getModel().getName();
	var model = wrapper.getModel();
	var selectedMetaNodeModels = wrapper.getModel().getSelectedMetaNodeModels().clone();
	if (selectedMetaNodeModels == null || selectedMetaNodeModels == "") {
		alert("没有选择任何节点");
		return;
	}
	if(selectedMetaNodeModels.get(0).type!="JOIN_NODE"){
		alert("请选择汇聚节点设置汇聚条件！");
		return;
	}
	//打开权限选择面板
	//window.showModalDialog
	 var nodeID = selectedMetaNodeModels.get(0).getID();
	 var obj = new Object();
     obj.nodeId=nodeID;
     obj.xiorkFlow=this.xiorkFlow ;
	 var joinConfig=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"/oswf/graphicdesigner/condition/set_condition.jsp", obj, "dialogWidth:300px;dialogHeight:200px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
	if(joinConfig!=null){
	   //是否已经设置
			var joinConfigModels=model.selectedJoinConfigModels;
			var  doc=XMLDocument.newDomcument(); 
		    doc.async="false"; 
			doc.loadXML(joinConfig);
			var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
			for(var i=0;i<joinConfigModels.length;i++){
				 var xml=joinConfigModels[i];
				 var  xmlDoc=XMLDocument.newDomcument(); 
				 xmlDoc.async="false"; 
				 xmlDoc.loadXML(xml);
				 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				 if(stepId==currentStepId){
				    model.selectedJoinConfigModels.removeById(i);
				    break;
				 }
			}
   		model.selectedJoinConfigModels.add(joinConfig);
   		
   	}
};


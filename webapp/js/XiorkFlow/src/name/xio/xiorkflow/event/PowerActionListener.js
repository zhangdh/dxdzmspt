
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author renwy
 */
function PowerActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
PowerActionListener.prototype.actionPerformed = function (obj) {
	
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
    //  alert(selectedMetaNodeModels.get(0).type);
      if(selectedMetaNodeModels.get(0).type=="START_NODE"){
      obj.startNode="yes";
      }
    obj.xiorkFlow=this.xiorkFlow;
    if(wrapper.selectedModel==null||wrapper.selectedModel==""){
	    alert("请先设置模板");
		return;
	}
    //nodeConfig.jsp
	var roleConfig=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"GetFormModel?formId="+wrapper.selectedModel+"&method=role", obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:yes;status:no;resizable:yes;");
	if(roleConfig!=null){
			//是否已经设置
			var roleConfigModels=model.selectedRoleConfigModels;
			var  doc=XMLDocument.newDomcument(); 
		    doc.async="false"; 
			doc.loadXML(roleConfig);
			var currentStepId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
			for(var i=0;i<roleConfigModels.length;i++){
				 var xml=roleConfigModels[i];
				 var  xmlDoc=XMLDocument.newDomcument(); 
				 xmlDoc.async="false"; 
				 xmlDoc.loadXML(xml);
				 var stepId=xmlDoc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				
				 if(stepId==currentStepId){
				    model.selectedRoleConfigModels.removeById(i);
				    break;
				 }
			}
	model.selectedRoleConfigModels.add(roleConfig);
  }
};


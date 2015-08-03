
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) renwy.com 2007</p>
 * @author renwy
 */
function FormActionListener(xiorkFlow) {
	this.xiorkFlow = xiorkFlow;
}
FormActionListener.prototype.actionPerformed = function (obj) {
	
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
      if(selectedMetaNodeModels.get(0).type=="START_NODE"){
      obj.startNode="yes";
      }
    //判断模板id
    if(wrapper.selectedModel!=null&&wrapper.selectedModel!=""){
        obj.formId=wrapper.selectedModel;
        obj.xiorkFlow=xiorkFlow;
		var formConfig=window.showModalDialog(XiorkFlowWorkSpace.BASE_PATH+"GetFormModel?formId="+wrapper.selectedModel, obj, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
	
		if(formConfig!=null){
			//是否已经设置
			var formConfigModels=model.selectedFormConfigModels;
			var  doc=XMLDocument.newDomcument(); 
		    doc.async="false"; 
			doc.loadXML(formConfig);
			var currentStepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
			for(var i=0;i<formConfigModels.length;i++){
				 var xml=formConfigModels[i];
				 var  xmlDoc=XMLDocument.newDomcument(); 
				 xmlDoc.async="false"; 
				 xmlDoc.loadXML(xml);
				 var stepId=xmlDoc.getElementsByTagName("step")[0].getAttribute("stepId");
				
				 if(stepId==currentStepId){
				    model.selectedFormConfigModels.removeById(i);
				    break;
				 }
			}
		    model.selectedFormConfigModels.add(formConfig);
		 }
	}else{
	    alert("请先设置模板");
		return;
	}
};


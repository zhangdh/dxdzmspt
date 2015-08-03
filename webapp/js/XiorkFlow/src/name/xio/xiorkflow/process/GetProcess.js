
/**
 * <p>Title:  </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) xio.name 2006</p>
 * @author xio
 */
function GetProcess(wrapper, tableViewer, toolbar) {
    this.base = Ajax;
    this.base();
    this.wrapper = wrapper;
    this.toolbar = toolbar;
    this.tableViewer = tableViewer;
}
GetProcess.prototype = new Ajax();
GetProcess.prototype.setButtonEnable = function (b) {
    if (this.toolbar) {
        this.toolbar.setButtonEnable(b);
    }
};

//
GetProcess.prototype.getProcess = function (name,id) {
    if (!name) {
        this.name = null;
    	//加载失败，工作流程图名字为空！
        alert("\u52a0\u8f7d\u5931\u8d25\uff0c\u5de5\u4f5c\u6d41\u7a0b\u56fe\u540d\u5b57\u4e3a\u7a7a\uff01");
        return false;
    }
    this.name = name;
    //
    this.setButtonEnable(false);
    
    //
    var url = XiorkFlowWorkSpace.URL_GET_PROCESS;
    var method = "POST";
    var params = "name=" + name+"&id="+id+"&workId="+workId;

    //
    this.loadXMLHttpRequest(url+"?method=showProcess", method, params);
};
GetProcess.prototype.onReadyStateChange = function (httpRequest) {
    if (httpRequest.readyState == 4) {
        if (httpRequest.status == 200) {
            this.processXMLHttpRequest(httpRequest);
        } else {
        	//处理过程出现错误！
            alert("\u5904\u7406\u8fc7\u7a0b\u51fa\u73b0\u9519\u8bef\uff01");
            this.setButtonEnable(true);
        }
    }
};
GetProcess.prototype.processXMLHttpRequest = function (httpRequest) {
    var doc = httpRequest.responseXML;
    if (!doc) {
    	//操作结束，未知服务器处理结果！
        alert("\u64cd\u4f5c\u7ed3\u675f\uff0c\u672a\u77e5\u670d\u52a1\u5668\u5904\u7406\u7ed3\u679c\uff01");
        this.setButtonEnable(true);
        return false;
    }

    //
    var responseNodes = doc.getElementsByTagName("Response");
 // alert(doc.xml);
    if ((responseNodes != null) && (responseNodes.length > 0)) {
        var responseNode = responseNodes[0];
        var statusValue = eval(responseNode.getAttribute("status"));
        var alertStr = "";
        switch (statusValue) {
          case XiorkFlowWorkSpace.STATUS_FAIL:
      		//获取失败。
            alertStr = "\u83b7\u53d6\u5931\u8d25\u3002";
            break;
          case XiorkFlowWorkSpace.STATUS_XML_PARSER_ERROR:
      		//获取失败，xml解析出错。
            alertStr = "\u83b7\u53d6\u5931\u8d25\uff0cxml\u89e3\u6790\u51fa\u9519\u3002";
            break;
          case XiorkFlowWorkSpace.STATUS_IO_ERROR:
      		//获取失败，IO错误。
            alertStr = "\u83b7\u53d6\u5931\u8d25\uff0cIO\u9519\u8bef\u3002";
            break;
          case XiorkFlowWorkSpace.STATUS_FILE_NOT_FOUND:
      		//获取失败，文件未找到。
            alertStr = "\u83b7\u53d6\u5931\u8d25\uff0c\u6587\u4ef6\u672a\u627e\u5230\u3002";
            break;
          default:
        	//获取失败，未知错误。
            alertStr = "\u83b7\u53d6\u5931\u8d25\uff0c\u672a\u77e5\u9519\u8bef\u3002";
            break;
        }
        this.setButtonEnable(true);
        alert(alertStr);
    } else {
        var editable = this.wrapper.getModel().isEditable();
        var model = XiorkFlowModelConverter.convertXMLToModel(doc);
        //得到当前步骤
        var currentStepNodes = doc.getElementsByTagName("currentStep");
	    for (var i = 0; i < currentStepNodes.length; i++) {
	        var stepNode = currentStepNodes[i];
	        var stepId=stepNode.getAttribute("stepId");
	        this.wrapper.currentSteps.add(stepId);
	    }
	    //得到历史步骤
	    var currentStepNodes = doc.getElementsByTagName("historyStep");
	    for (var i = 0; i < currentStepNodes.length; i++) {
	        var stepNode = currentStepNodes[i];
	        var stepId=stepNode.getAttribute("stepId");
	        this.wrapper.historySteps.add(stepId);
	    }
	    var workflowRole=doc.getElementsByTagName("workflowRole");
	    var role=new Array();
	    for(var i = 0; i <workflowRole.length; i++ ){
	       var t=workflowRole[i].xml;
	       var xml=t.replace("workflowRole","workflow");
	       xml=xml.replace("workflowRole","workflow");
	       var  doc2=XMLDocument.newDomcument(); 
		   doc2.async="false"; 
		   doc2.loadXML(xml);
		   this.wrapper.selectedRoleConfigModels.add(doc2.xml);
	    }
	     workflowRole=doc.getElementsByTagName("workflowJoin");
	     for(var i = 0; i <workflowRole.length; i++ ){
	       var t=workflowRole[i].xml;
	       var xml=t.replace("workflowJoin","workflow");
	       xml=xml.replace("workflowJoin","workflow");
	       var  doc2=XMLDocument.newDomcument(); 
		   doc2.async="false"; 
		   doc2.loadXML(xml);
		   this.wrapper.selectedJoinConfigModels.add(doc2.xml);
	    }
	     workflowRole=doc.getElementsByTagName("workflowFunc");
	     for(var i = 0; i <workflowRole.length; i++ ){
	       var t=workflowRole[i].xml;
	       var xml=t.replace("workflowFunc","workflow");
	       xml=xml.replace("workflowFunc","workflow");
	       var  doc2=XMLDocument.newDomcument(); 
		   doc2.async="false"; 
		   doc2.loadXML(xml);
		   this.wrapper.selectedFunctionConfigModels.add(doc2.xml);
	    }
	    
	    workflowRole=doc.getElementsByTagName("stepForm");
	     for(var i = 0; i <workflowRole.length; i++ ){
	       var t=workflowRole[i].xml;
	       var xml=t.replace("stepForm","step");
	       xml=xml.replace("stepForm","step");
	       var  doc2=XMLDocument.newDomcument(); 
		   doc2.async="false"; 
		   doc2.loadXML(xml);
		   this.wrapper.selectedFormConfigModels.add(doc2.xml);
	    }
	    var formModel =doc.getElementsByTagName("formModel");
	    if(formModel[0]!=null){
	        
	    	this.wrapper.selectedModel=formModel[0].getAttribute("formId");
	    }
	    //得到流程类别ID
	    var wkType=doc.getElementsByTagName("wkType");
	    if(wkType[0]!=null){
	    	this.wrapper.selectedWkType=wkType[0].getAttribute("wkTypeId");
	    }
        model.setName(this.name);
        model.setEditable(editable);
        this.wrapper.setModel(model);
        this.wrapper.setChanged(false);
        if (this.tableViewer) {
           // alert(this.tableViewer.base(Component));
            this.tableViewer.setModel(model);
        }
        this.setButtonEnable(true);
    }
};


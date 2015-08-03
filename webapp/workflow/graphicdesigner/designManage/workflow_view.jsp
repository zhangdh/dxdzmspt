<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<title>工单处理流程</title>
</HEAD>
<BODY style="bgcolor:white">
<%@ include file="oswfView.jsp"%>
</BODY>
<SCRIPT LANGUAGE="JavaScript">
<!--
_FLOW.eventFlag=false;
setEventFlag(false);
//得到name,workid
var searchStr = window.location.search;
var params = searchStr.substring(searchStr.lastIndexOf("?") + 1);
var paramsArray = params.split("&");
var name = null;
var workid=null;
var entryid=null;
for (var i = 0; i < paramsArray.length; i++) {
    var tempStr = paramsArray[i];
    var key = tempStr.substring(0, tempStr.indexOf("=")).toLowerCase();
    var value = tempStr.substring(tempStr.indexOf("=") + 1);
    if (key == "name") {
        name = value;
        continue;
    }
    if (key == "workid") {
        workid = value;
        continue;
    }
    if (key == "entryid") {
        entryid = value;
        continue;
    }
}
_Post("/flowdesign.coffice?method=viewProcess&workid="+workid+"&entryid="+entryid,'',function(json){
	var path="http://"+location.hostname+json.design_xmlpath+name+".xml";
 	_FLOW.loadFromXML(path);
 	if(json.steps!=""){
	 	var a=new Array();
	 	var steps=json.steps;
	 	a=steps.split(",");
	  	if(a.length>0){
		    Canvas.innerHTML = _FLOW.ProcString(a);
		    Canvas.innerHTML += _FLOW.StepString(a);
	  	}else{
		  	Canvas.innerHTML = _FLOW.ProcString();
		  	Canvas.innerHTML += _FLOW.StepString();
	  	}
 	}else{
 		Canvas.innerHTML = _FLOW.ProcString();
		Canvas.innerHTML += _FLOW.StepString();
 	}
	emptyLog();
});

//-->
</SCRIPT>
</HTML>

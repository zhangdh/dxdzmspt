<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<HEAD>
<TITLE>工作流修改</TITLE>
</HEAD>
<BODY style='background-image: url(${webcontext}/js/design/image/canvasbg.gif);'>
<%@ include file="oswfView.jsp"%>
</BODY>
<SCRIPT LANGUAGE="JavaScript">
<!--
//判断是否选中
alert('1234');
function ObjSelected(){
  if(_FOCUSTEDOBJ == null){
    alert("当前没有选中对象！--用鼠标单击流程图上的对象可以选中它");
    return false;  
  }
  return true;
}
//删除选中对象
function mnuDelObj(){
  if(ObjSelected()) deleteObj(_FOCUSTEDOBJ.id);
}
//保存流程图信息
function mnuSaveFlow(){ 
  
  //如有asp环境，要保存流程图的xml文件，请注释下面两行
 // alert('在asp环境下才能保存！');//--
  //return false;                  //--
 if(_FLOW.FileName==""){
    var name=prompt("请输入流程名称","");
    if(name==null){
			return;    
    }
    _FLOW.FileName=name;
 }
 var s = _FLOW.validate();
 if(s != ""){
 	alert("当前有效性检查有误\n\n" + s );return;
 }
  if(!confirm("确定要保存当前流程图[" + _FLOW.FileName + "]吗？")) return;
  
  try{
	var s = _FLOW.SaveToXML();
	if(s == "")
	  alert("保存成功！");
	//else
	  //alert("保存失败！"+s);
  }
  catch(e){
	alert(e);    
  }
}
function NavBrush_onClick(obj){
  //清除所有的ButtonClass
  var objs = document.all("tbxToolbox_btn");
  for(var i = 0; i < objs.length; i++)
    objs[i].className="bon2";
  obj.className = "bon1";
  _TOOLTYPE = obj.cType;
}

function DrawVML(){
  Canvas.innerHTML = _FLOW.ProcString();
  Canvas.innerHTML += _FLOW.StepString();
  _FLOW.getInnerObject();
  _FOCUSTEDOBJ = null;
  stuffProp();
}
function LoadFlow(AUrl){
  if(AUrl == "")
    _FLOW.createNew("");
  else
    _FLOW.loadFromXML(AUrl);
  DrawVML();
  emptyLog();
}
function DrawAll(){
  DrawVML();
}
//打开流程属性窗口
function openWkAttr(){
	var obj=window.showModalDialog("${ctx}/oswf/design/default.do?method=showWkAttr", _FLOW, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:auto;status:no;resizable:yes;");
	if(obj!=null){
		_FLOW.formId=obj.formId;
		_FLOW.wkType=obj.wkType;
	}
}

// 设置组件的事件，
_FLOW.eventFlag=true;
setEventFlag(true);
S = '<table width="100%" height="100%" cellspacing="0" cellpadding="0" border="0">' +
      '<tr>' +
        '<td bgcolor="#C4D6FC" valign="top" align="center">'+
          '<div id="tableContainer2" class="tableContainer">'+ 
          '<table id="propview" border="0" cellpadding="0" cellspacing="0" width="100%" class="gridstyle"> '+
          '<thead> '+
            '<tr height="25" class="hdr">'+
              '<td width="80">属性</td><td>值</td>'+
            '</tr>'+
          '</thead> '+
          '<tbody > '+
          '</tbody> '+
          '</table> '+
          '</div> '+
        '</td>'+
      '</tr>'+
    '</table>';
tbxPropbox = new TToolBox("tbxPropbox", "名称修改", screen.availWidth-330, 1, 270, 103, S, "down", true);
var S = '<table cellspacing="0" cellpadding="0" border="0">' +
    '<tr height="25"><td width="2"></td>' +

      '<td width="20" align="center" title="校验流程图"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuValidateFlow();"><img src="${webcontext}/workflow/design/js/image/validate.gif" border="0"></BUTTON></td>' +
     '<td width="20" align="center" title="保存流程图 Ctrl+S"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSaveFlow();"><img src="${webcontext}/js/design/image/save.gif" border="0"></BUTTON></td>' +
     
      '<td width="23" align="center" title="删除选中对象 Del"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuDelObj();"><img src="${webcontext}/workflow/design/js/image/delete.gif" border="0"></BUTTON></td>' +
      
      '<td width="4" background="image/split.gif"></td>'+

	  //工具箱
	  '<td align="center" title="选择对象\n\n1.单击本按钮\n2.在工作区(画布)上单击[任务]或[路径]的图形"><BUTTON cType="point" class="bon1" name="tbxToolbox_btn" HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${webcontext }/workflow/design/js/image/select.gif" border="0"></BUTTON></td>' +
      '<td align="center"></td>' +
	  '<td align="center" title="新增[任务](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"><BUTTON cType="rect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${webcontext}/workflow/design/js/image/element_new.png" border="0"></BUTTON></td>' +
   
       '<td align="center" title="新增[分支](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"> <BUTTON cType="splitrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${webcontext}/workflow/design/js/image/branch_element.png" border="0"></BUTTON></td>' +
	   '<td align="center" title="新增[汇聚](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"> <BUTTON cType="fillrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${webcontext}/workflow/design/js/image/element_into.png" border="0"></BUTTON></td>' +
	
	  '<td align="center" title="新增[路径](折角线)\n\n1.单击本按钮\n2.在[任务]图形(起点)上按住鼠标左键\n3.拉动鼠标至某[任务]图形(终点)中间位置\n4.松开鼠标左键"><BUTTON cType="polyline" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:PolyLine filled="false" Points="0,18 9,5 18,18" style="position:relative;" strokeweight="1"><v:stroke EndArrow="Classic"/></v:PolyLine></BUTTON></td>' +

	  '<td align="center" title="流程属性"> <BUTTON cType="splitrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="openWkAttr();"><img src="${webcontext }/workflow/design/js/image/config.gif" border="0"></BUTTON></td>' +
	  	
	  '<td width="4" background="${ctx }/js/design/image/split.gif"></td>'+
     // '<td width="23" align="center" title="关于"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuAbout();"><img src="image/qa.gif" border="0"></BUTTON></td>' +
    '<td width="2"></td></tr>' + 
    '</table>';
tbxToolbar = new TToolPanel("tbxToolbar", 1, 1, 600, 25, S);
//得到name,workid
var searchStr = window.location.search;
var params = searchStr.substring(searchStr.lastIndexOf("?") + 1);
var paramsArray = params.split("&");
var name = null;
var workid=null;
var issue=null;
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
    if (key == "issue") {
        issue = value;
        continue;
    }
}
var pworkId=workid;
sys_ajaxPostDirect("/oswf/design/default.do?method=showProcess&workid="+workid,'',function(json){
	var path="http://"+location.hostname+json.design_xmlpath+name+".xml";
	//加载流程图
	LoadFlow(path);
	var i=_FLOW.Procs.length;
	  var j=_FLOW.Steps.length;
	  //得到节点最大id
	  var procMax=_FLOW.Procs[0].ID;
	  for(var k=1;k<i;k++){
	  	var m=_FLOW.Procs[k].ID;
	  	if(parseInt(m)>parseInt(procMax))   
        	procMax=m;
	  }
	  //得到线最大id
	  var stepMax=_FLOW.Steps[0].ID;
	  for(var k=1;k<j;k++){
	  	var m=_FLOW.Steps[k].ID;
	  	if(parseInt(m)>parseInt(stepMax))   
        	stepMax=m;
	  }
	  if(parseInt(procMax)>parseInt(stepMax)){
	  	indexId=procMax;
	  }else{
	  	indexId=stepMax;
	  }
	  indexId++;
	//流程名称
	_FLOW.FileName=json.wkName;
	//模板ID		       
	_FLOW.formId=json.formModel;
	//公文类型
	_FLOW.wkType=json.wkType;
	//节点授权
	var nodeConfig=json.nodeConfig;
	var roles=nodeConfig.split(",");
	for(var i=0;i<roles.length;i++){
		 if(roles[i].indexOf("jionconfig")>0){//聚合节点
		   _FLOW.joinConfigs.push(roles[i]);
		 }else{
		   _FLOW.roleConfigs.push(roles[i]);
		 }
	}
	//表单设置
	var formConfig=json.formConfig;
	var roles=formConfig.split(",");
	var formId="";
	for(var i=0;i<roles.length;i++){
	  _FLOW.formConfigs.push(roles[i]);
	}
	//功能设置
	var funcConfig=json.funcConfig;
	var funcs=funcConfig.split(",");
	for(var i=0;i<funcs.length;i++){
		_FLOW.functionConfigs.push(funcs[i]);
	}
	//表单执行函数设置
	var formFuncConfig=json.formFuncConfig;
	var formfuncs=formFuncConfig.split(",");
	for(var i=0;i<formfuncs.length;i++){
		_FLOW.formFuncConfigs.push(formfuncs[i]);
	}
	//子流程设置
	var subConfig=json.subConfig;
	var subConfigs=subConfig.split(",");
	for(var i=0;i<subConfigs.length;i++){
		_FLOW.subConfigs.push(subConfigs[i]);
	}
});
//-->

</SCRIPT>
顶顶顶顶顶顶顶顶顶顶顶顶顶顶
<img src="${webcontext}/js/design/image/save.gif" border="0">
</HTML>

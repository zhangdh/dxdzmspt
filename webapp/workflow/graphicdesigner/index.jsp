<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<html>
<HEAD>
<TITLE>在线工作流设计系统</TITLE>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link href="${ctx}/js/design/css/style.css" type="text/css" rel="stylesheet">
<script charset="utf-8"  src="${ctx}/js/design/js/common.js"></script>
<script  charset="utf-8"  src="${ctx}/js/design/js/dtree.js"></script>
<script charset="utf-8"   src="${ctx}/js/design/js/shape.js"></script>
<script charset="utf-8"  src="${ctx}/js/design/js/toppanel.js"></script>
<script charset="utf-8" src="${ctx}/js/design/js/topflow.js"></script>
<script charset="utf-8" src="${ctx}/js/design/js/topflowevent.js"></script>
<script charset="utf-8"  src="${ctx}/js/design/js/contextmenu.js"></script>
<SCRIPT LANGUAGE="JScript">
var _TREE;
_FLOW.eventFlag=true;
setEventFlag(true);
//function
function DrawTree(){
  _TREE = new dTree('_TREE', 'image/');
  var num = 3, obj,i;
  var FocusColor = _FLOW.Config.ProcFocusedStrokeColor;
  _TREE.add(0,-1,' '+_FLOW.Text+'','javascript:','',_FLOW.ID);
  _TREE.add(1,0,'任务','javascript:','','Procs','','image/task.gif','image/task.gif');
  _TREE.add(2,0,'路径','javascript:','','Steps','','image/step.gif','image/step.gif');
  for (i = 0;i < _FLOW.Procs.length;i++){
    obj = _FLOW.Procs[i];
    _TREE.add(num++,1,obj.Text,'javascript:objFocusedOn("'+obj.ID+'");','',obj.ID,'','image/obj4.gif','image/obj4.gif');
  }

  for (i = 0;i < _FLOW.Steps.length;i++){
    obj = _FLOW.Steps[i];
    _TREE.add(num++,2,obj.Text,'javascript:objFocusedOn("'+obj.ID+'");','',obj.ID,'','image/obj4.gif','image/obj4.gif');
  }
  treeview.innerHTML = _TREE;
}
function DrawDataView(){
  var idTR, idTD, arr = _FLOW.DataView();
  for(var i = dataview.rows.length - 1; i > 0; i--)
    dataview.deleteRow(i);
  for(var i = 0, u = 1; i < arr.length; i++){
    idTR = dataview.insertRow();
  	idTR.onmouseover=new Function("this.className=\"focusLine\";");
	  idTR.onmouseout=new Function("this.className=\"normalLine\";");
    idTR.height = "22";
    idTD = idTR.insertCell();
	idTD.innerHTML = arr[i].ProcID + "(" + arr[i].ProcText + ")";
    idTD = idTR.insertCell();
    idTD.innerHTML = arr[i].Idx;
    idTD = idTR.insertCell();
	idTD.innerHTML = arr[i].PreProcID + "(" + arr[i].PreProcText + ")";
    idTD = idTR.insertCell();
    idTD.innerHTML = arr[i].Cond + "&nbsp;";
  }
  mergecell(dataview);
}

function DrawVML(){
  Canvas.innerHTML = _FLOW.ProcString();
  Canvas.innerHTML += _FLOW.StepString();
  _FLOW.getInnerObject();
  _FOCUSTEDOBJ = null;
  stuffProp();
}

function DrawAll(){
  //DrawTree();
  DrawVML();
  //DrawDataView();
}

function LoadFlow(AUrl){
  if(AUrl == "")
    _FLOW.createNew("");
  else
    _FLOW.loadFromXML(AUrl);
  DrawAll();
  emptyLog();
}

function ObjSelected(){
  if(_FOCUSTEDOBJ == null){
    alert("当前没有选中对象！--用鼠标单击流程图上的对象可以选中它");
    return false;  
  }
  return true;
}

//[流程图]菜单处理事件
function mnuNewFlow(){
  if(_FLOW.Modified)
    if(!confirm("当前流程图尚未保存，新建新文件将会放弃所有修改，继续新建吗？")) return;
  var flow = new TTopFlow("");
  flow.FileName = "untitled.xml";
  if(vmlOpenWin("flow.htm", flow, 350,200)){
    LoadFlow("");
    _FLOW.FileName = flow.FileName;
    _FLOW.Text = flow.Text;
    _FLOW.Password = flow.Password;
    DrawTree();
    delete flow;
  }
}

function mnuEditFlow(){
  if(vmlOpenWin("flow.htm", _FLOW, 350,200)){
    DrawTree();
  }
}

function mnuValidateFlow(){
  var s = _FLOW.validate();
  if(s == "")
    alert("校验完成，这是一个合法的流程图！");
  else
    alert(s);
}

function mnuOpenFlow(){
  if(_FLOW.Modified)
    if(!confirm("当前流程图尚未保存，打开新文件将会放弃所有修改，继续打开吗？")) return;
  if(vmlOpenWin("filelist.jsp", _FLOW, 450,400))
    LoadFlow(_FLOW.FileName);
}

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
  if(!confirm("确定要保存当前流程图[" + _FLOW.FileName + "]吗？")) return;
  var s = _FLOW.validate();
  if(s != "")
	if(!confirm("当前有效性检查有误\n\n" + s + "\n\n是否要继续保存？")) return;
  try{
	_FLOW.SaveToXML();
  }
  catch(e){
	alert(e);    
  }
}

function mnuReloadFlow(){
  if(_FLOW.Modified)
    if(!confirm("当前流程图尚未保存，重新载入将会放弃所有修改，继续重载吗？")) return;
  LoadFlow(_FLOW.FileName);
}

//[流程图对象]菜单处理事件
function mnuAddProc(){
  var Proc = new TProc(_FLOW);
  if(vmlOpenWin("proc.htm", Proc, 450,350)){
    _FLOW.addProc(Proc);
    pushLog("addproc", Proc);
    DrawAll();
  }
}

function mnuAddStep(){
  var Step = new TStep(_FLOW);
  if(vmlOpenWin("step.htm", Step, 500,350)){
    _FLOW.addStep(Step);
    pushLog("addstep", Step);
    DrawAll();
  }
}

function mnuCopyProc(){
  if(!ObjSelected()) return;
  if(_FOCUSTEDOBJ.typ != "Proc"){
    alert("只有任务可以复制！");
    return;
  }
  var curProc = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(!confirm("确定要复制任务[" + curProc.Text + "]吗？")) return;
  var Proc = new TProc(_FLOW);
  var iID = Proc.ID;
  Proc.clone(curProc);
  Proc.ID = iID;
  Proc.X = parseInt(curProc.X) + 10;
  Proc.Y = parseInt(curProc.Y) + 10;
  _FLOW.addProc(Proc);
  pushLog("addproc", Proc);
  DrawAll();
  objFocusedOn(iID);
  mnuSetZIndex("F");
}

function mnuEditObj(){
  if(!ObjSelected()) return;
  if(_FOCUSTEDOBJ.typ != "Proc" && _FOCUSTEDOBJ.typ != "Step") return;
  if(_FOCUSTEDOBJ.typ == "Proc")
    editProc(_FOCUSTEDOBJ.id);
  else
    editStep(_FOCUSTEDOBJ.id);
}

function mnuDelObj(){
  if(ObjSelected()) deleteObj(_FOCUSTEDOBJ.id);
}

function mnuSetZIndex(Act){
  if(!ObjSelected()) return;
  if(_FOCUSTEDOBJ.typ != "Proc" && _FOCUSTEDOBJ.typ != "Step") return;
  if(_FOCUSTEDOBJ.typ == "Proc")
    var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  else
    var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var oldValue = obj.zIndex;
  if(Act == "F")
    _FLOW.brintToFront(obj);
  else
    _FLOW.sendToBack(obj);
  _FOCUSTEDOBJ.style.zIndex = obj.zIndex;
  pushLog("editprop",{"obj":obj,"prop":"zIndex","_old":oldValue,"_new":obj.zIndex});
}

//[系统菜单]处理事件
function mnuOption(){
  if(vmlOpenWin("option.htm", _FLOW.Config, 510,510)){
    DrawAll();
  }
}

function mnuDemo(){
  var tmpwin = window.open("demo.htm");
  tmpwin.focus();
}

function mnuFileMgr(){
  vmlOpenWin("filemgr.jsp","",450,400);
}

function mnuAbout(){
  vmlOpenWin("about.htm", "", 480,400);
}

function mnuExit(){
  if(confirm("确定要退出本系统吗？")){
    window.opener = null;
    window.close();
  }
}

function mnuSetZoom(Act){
  var rate = Act == "in"?0.2:-0.2;
  var newzoom = _ZOOM + rate;
  if(newzoom > 2) return;
  if(newzoom < 0.2) return;
  changeZoom(newzoom);
  document.all("zoomshow").value = _ZOOM;
}

function changeZoom(v){
  _ZOOM = parseFloat(parseFloat(v).toFixed(2));
  Canvas.style.zoom = _ZOOM;
}

function mnuTurnToolbox(){
  tbxToolbox.VisibleEx = !tbxToolbox.VisibleEx;
  tbxToolbox.InnerObject.style.display = tbxToolbox.VisibleEx?"block":"none";
  document.all.mnu_win_toolbox.innerHTML = (tbxToolbox.VisibleEx?"隐藏":"显示") + "工具箱";
}

function mnuTurnPropbox(){
  tbxPropbox.VisibleEx = !tbxPropbox.VisibleEx;
  tbxPropbox.InnerObject.style.display = tbxPropbox.VisibleEx?"block":"none";
  document.all.mnu_win_propbox.innerHTML = (tbxPropbox.VisibleEx?"隐藏":"显示") + "属性框";
}

function mnuTurnObjView(){
  tbxObjView.VisibleEx = !tbxObjView.VisibleEx;
  tbxObjView.InnerObject.style.display = tbxObjView.VisibleEx?"block":"none";
  document.all.mnu_win_objview.innerHTML = (tbxObjView.VisibleEx?"隐藏":"显示") + "对象视图";
}

function mnuTurnDataView(){
  tbxDataView.VisibleEx = !tbxDataView.VisibleEx;
  tbxDataView.InnerObject.style.display = tbxDataView.VisibleEx?"block":"none";
  document.all.mnu_win_dataview.innerHTML = (tbxDataView.VisibleEx?"隐藏":"显示") + "数据视图";
}
//打开流程属性窗口
function openWkAttr(){
	var obj=window.showModalDialog("${ctx}/oswf/design/default.do?method=showWkAttr", _FLOW, "dialogWidth:600px;dialogHeight:400px;center:yes;help:no;scroll:auto;status:no;resizable:yes;");
	if(obj!=null){
		_FLOW.formId=obj.formId;
		_FLOW.wkType=obj.wkType;
	}
}

//-->
</SCRIPT>
</HEAD>
<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" style='background-image: url(${ctx }/js/design/image/canvasbg.gif);'>

<div style='left:0px;top:0px;width:100%;height:100%;position:absolute;z-index:-1000' id="Canvas"></div>
<v:rect class="toolui" style="display:none" id="_rectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:rect class="toolui" style="display:none" fillcolor="#CCCCCC" id="_fillrectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:rect class="toolui" style="display:none" fillcolor="#CCCCCC" id="_splitrectui">
<v:Stroke dashstyle="dashdot"/>
</v:rect>
<v:roundrect class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_roundrectui">
<v:Stroke dashstyle="dashdot"/>
</v:roundrect>
<v:shape type="#diamond" class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" strokeweight="1px" id="_diamondui">
<v:Stroke dashstyle="dashdot"/>
</v:shape>
<v:oval class="toolui" style="display:none;left:0px;top:0px;width:60px;height:50px;" id="_ovalui">
<v:Stroke dashstyle="dashdot"/>
</v:oval>
<v:line class="toolui" style="display:none" from="0,0" to="100,0" id="_lineui">
<v:Stroke dashstyle="dashdot" StartArrow="" EndArrow="Classic"/>
</v:line>


<SCRIPT LANGUAGE="JavaScript">
<!--
function NavBrush_onClick(obj){
  //清除所有的ButtonClass
  var objs = document.all("tbxToolbox_btn");
  for(var i = 0; i < objs.length; i++)
    objs[i].className="bon2";
  obj.className = "bon1";
  _TOOLTYPE = obj.cType;
}

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
tbxPropbox = new TToolBox("tbxPropbox", "名称修改", screen.availWidth-330, 1, 270, 100, S, "down", true);
var S = '<table cellspacing="0" cellpadding="0" border="0">' +
    '<tr height="25"><td width="2"></td>' +

      '<td width="20" align="center" title="校验流程图"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuValidateFlow();"><img src="${ctx }/js/design/image/validate.gif" border="0"></BUTTON></td>' +
     '<td width="20" align="center" title="保存流程图 Ctrl+S"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuSaveFlow();"><img src="${ctx }/js/design/image/save.gif" border="0"></BUTTON></td>' +
     
      '<td width="23" align="center" title="删除选中对象 Del"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuDelObj();"><img src="${ctx }/js/design/image/delete.gif" border="0"></BUTTON></td>' +
      
      '<td width="4" background="image/split.gif"></td>'+

	  //工具箱
	  '<td align="center" title="选择对象\n\n1.单击本按钮\n2.在工作区(画布)上单击[任务]或[路径]的图形"><BUTTON cType="point" class="bon1" name="tbxToolbox_btn" HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${ctx }/js/design/image/select.gif" border="0"></BUTTON></td>' +
      '<td align="center"></td>' +
	  '<td align="center" title="新增[任务](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"><BUTTON cType="rect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${ctx }/js/design/image/element_new.png" border="0"></BUTTON></td>' +
   
       '<td align="center" title="新增[分支](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"> <BUTTON cType="splitrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${ctx }/js/design/image/branch_element.png" border="0"></BUTTON></td>' +
	   '<td align="center" title="新增[汇聚](方形)\n\n1.单击本按钮\n2.在工作区(画布)上空白位置按住左键\n3.拉动鼠标\n4.松开鼠标左键"> <BUTTON cType="fillrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><img src="${ctx }/js/design/image/element_into.png" border="0"></BUTTON></td>' +
	
	  '<td align="center" title="新增[路径](折角线)\n\n1.单击本按钮\n2.在[任务]图形(起点)上按住鼠标左键\n3.拉动鼠标至某[任务]图形(终点)中间位置\n4.松开鼠标左键"><BUTTON cType="polyline" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="NavBrush_onClick(this);"><v:PolyLine filled="false" Points="0,18 9,5 18,18" style="position:relative;" strokeweight="1"><v:stroke EndArrow="Classic"/></v:PolyLine></BUTTON></td>' +

	  '<td align="center" title="流程属性"> <BUTTON cType="splitrect" class="bon2" name="tbxToolbox_btn"  HIDEFOCUS="true" onclick="openWkAttr();"><img src="${ctx }/js/design/image/config.gif" border="0"></BUTTON></td>' +
	  	
	  '<td width="4" background="${ctx }/js/design/image/split.gif"></td>'+
     // '<td width="23" align="center" title="关于"><BUTTON class="normalBtn" onmouseover="this.className=\'focusBtn\'" onmouseout=\"this.className=\'normalBtn\'" HIDEFOCUS="true" onclick="mnuAbout();"><img src="image/qa.gif" border="0"></BUTTON></td>' +
    '<td width="2"></td></tr>' + 
    '</table>';
tbxToolbar = new TToolPanel("tbxToolbar", 1, 1, 600, 25, S);
LoadFlow("");
//-->
</SCRIPT>
</BODY>
</HTML>

/*--------------------------------------------------|
| 本作品取得原作者授权修改自 support@tops.com.cn    |
| 的作品topflow                                     |
|                                                   |
| 本文件是TopFlow的核心文件，定义了各种类用到的     |
| 事件以及处理方法                                  |
|                                                   |
| 版权归上海雪线信息技术有限公司所有，              |
| 技术支持：sales@shcommit.com （仅对付费用户）     |
| 网    站：www.shcommit.com                        |
|                                                   |
| 请勿私自拷贝、修改或用于商业用途                  |
| 敬请保留本注释.                                   |
|                                                   |
| Updated: 20070613                                 |
|--------------------------------------------------*/

//global variable
var indexId=3;//修改20090327
var contentPath=webcontext;
var name="";
var pworkId="";
var _FLOW = new TTopFlow("");
var _TOOLTYPE = "point";
var _CURRENTX = _CURRENTY = 0;
var _FOCUSTEDOBJ = null;
var _ZOOM = 1;

var _MOVEOBJ = null;
var _MOVETYPE = "";
var _DOLOG = [];
var _DOLOGINDEX = -1;
var _strPt1 = "";
var _strPt2 = "";
var _strSltPt = "";
var _strLine1 = "";
var _strLine2 = "";
var _strSltLine = "";
var _PointOrLine;
var isSelectPoint = 0;
var isSelectLine=0;
var _clkPx = 0;
var _clkPy = 0;
var ptMoveType = "";
var oOval = null;
var _logMoveType = "";
var _MOVELINEOBJ = null;
//选中流程图元素对象，显示选中效果 

function objFocusedOn(objId)
{
	//清除原来选中的对象
	objFocusedOff();
	_FOCUSTEDOBJ = document.getElementById(objId);
	if(_FOCUSTEDOBJ != null) 
	{
		
		var x = (event.x + document.body.scrollLeft) / _ZOOM;
		var y = (event.y + document.body.scrollTop) / _ZOOM;
		_FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.fsc;
		
		_MOVEOBJ = document.getElementById(objId);
		_clkPx = x/4*3+"pt";
		_clkPy = y/4*3+"pt";
		if(_FOCUSTEDOBJ.tagName=="PolyLine")
		{
			_MOVELINEOBJ = new TStep(_FLOW);
			_MOVELINEOBJ.clone(_FLOW.getStepByID(objId));
			_MOVEOBJ = _FLOW.getStepByID(objId);
			var strPt =_MOVEOBJ.Points;
			var aryPt = strPt.split(',');
			var nPt=aryPt.length-1;
			_strPt2 = "";
			_strPt1 = "";
			_strLine2 = "";
			_strLine1 = "";
			for(i=0;i<=nPt;i=i+2)
			{
				var m = aryPt[i].substr(0,aryPt[i].length-2)*4/3;
				var n = aryPt[i+1].substr(0,aryPt[i+1].length-2)*4/3;
				var sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
				if(isSelectPoint==0&&sqrta<=10)
				{
					_PointOrLine = 0;
					isSelectPoint=1;
					_MOVETYPE="line_m";			  		
					_strSltPt = aryPt[i]+',' +aryPt[i+1];
					_clkPx = aryPt[i];
					_clkPy = aryPt[i+1];
					if (i==0)ptMoveType="from";
					if (i==nPt-1)ptMoveType="to";
				}
				else
				{
					if(isSelectPoint==1)
						_strPt2 = _strPt2+','+aryPt[i]+','+aryPt[i+1];
					else
						_strPt1 = _strPt1+','+aryPt[i]+',' +aryPt[i+1];
				}

				if(i<=nPt-3)
				{
					var r = aryPt[i+2].substr(0,aryPt[i+2].length-2)*4/3;
					var s = aryPt[i+3].substr(0,aryPt[i+3].length-2)*4/3;
					
					if((Math.abs(x*(n-s)+y*(r-m)+(m*s-n*r))/Math.sqrt((n-s)*(n-s)+(r-m)*(r-m))<=5)&&(isSelectLine==0)&&sqrta>10&&isSelectPoint==0)
					{
						_PointOrLine = 1;
						_MOVETYPE="line_m";
						isSelectLine = 1;
						_strSltLine = aryPt[i]+','+aryPt[i+1]+','+aryPt[i+2]+','+aryPt[i+3];
						_clkPx = x/4*3+"pt";
						_clkPy = y/4*3+"pt";
					}
					else
					{
						if(isSelectLine==1)
						{
							if (i <= nPt-3)
							{
								_strLine2 = _strLine2+','+aryPt[i+2]+','+aryPt[i+3];
							}
						}
						else
						{
							_strLine1 = _strLine1 + "," + aryPt[i]+',' +aryPt[i+1];
						}
					}
				}
			}
			if(_strPt1!= '')
			   _strPt1 = _strPt1.substr(1)+',';
			if(_strLine1!='')
			   _strLine1 = _strLine1.substr(1)+",";
		}
	}  
	//修改  20090310 属性窗口显示设置
	  if(_FLOW.eventFlag){
	  	stuffProp();
	  }
}
function createOval(x,y)
{
	oOval = document.createElement("v:oval");
	oOval.style.position = "absolute";
	oOval.style.width = "6px";
	oOval.style.height="6px"
	oOval.style.left = x;
	oOval.style.top = y;
	oOval.fillcolor = "red";
	oOval.strokecolor = "red";
	document.body.appendChild(oOval);
}
//将HTML对象转换成PolyLine实例
function toPolyLineObj(objTemp)
{
	var objLine = _FLOW.getStepByID(objTemp.id);
	return objLine;
}
//更新数据表
function updateFlow(htmlObj){

}

//放弃选择流程图元素对象，显示未选中效果
function objFocusedOff(){
  if(_FOCUSTEDOBJ != null) 
  {
	  _FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.sc; //恢复原来的颜色
  }
  _FOCUSTEDOBJ = null;
  isSelectPoint = 0;
  isSelectLine = 0;
  ptMoveType = "";
  oOval = null;
  return;
}

//删除流程图元素对象
function deleteObj(ObjId){
  var obj = document.getElementById(ObjId);
  if(obj == null) return false;
  if(obj.typ != "Proc" && obj.typ != "Step") return false;
  if(obj.typ == "Proc" && (obj.st == "BeginProc" || obj.st == "EndProc")){
    alert("不能删除！一个流程图中，开始与结束结点是必需的");
    return false;
  }
  if(confirm("确定要删除[" +obj.title + "]吗？")){
    objFocusedOff();
    if(obj.typ == "Proc"){
      var Proc = _FLOW.getProcByID(ObjId);
      _FLOW.deleteProcByID(ObjId);
      pushLog("delproc", Proc);
    }
    else{
      var Step = _FLOW.getStepByID(ObjId);
      _FLOW.deleteStepByID(ObjId);
      pushLog("delstep", Step);
    }
    _FLOW.Modified = true;
    DrawAll();
  }
}

//更改[任务]的ID值，将原路径重新指定到新ID
function changeProcID(OldID, NewID){
  var Step;
  for(var i = 0; i< _FLOW.Steps.length; i++){
    Step = _FLOW.Steps[i];
    if(Step.FromProc == OldID) Step.FromProc = NewID;
    if(Step.ToProc == OldID) Step.ToProc = NewID;
  }
}
function saveStepsToProc(obj)
{
	for(var i = 0; i < _FLOW.Steps.length; i++){
		var oStep = _FLOW.Steps[i];
		if(oStep.FromProc == obj.id || oStep.ToProc == obj.id){
			updateFlow(document.getElementById(oStep.ID));					
		}
	}
}
//更改Proc的位置
function changeProcPos(obj){
  for(var i = 0; i < _FLOW.Steps.length; i++){
    Step = _FLOW.Steps[i];
    if(Step.FromProc == obj.id || Step.ToProc == obj.id){
      objStepHTML = document.getElementById(Step.ID);
      if(Step.ShapeType == "Line"){
        Step.getPath();
        objStepHTML.from = Step.FromPoint;
        objStepHTML.to = Step.ToPoint;
      }
      else if(Step.ShapeType == "PolyLine"){
		var strPt="";
		var arrPt = Step.Points.split(",");
		var objWidth = obj.style.width;
		var objHeight = obj.style.height;
		var objX = obj.style.left;
		var objY = obj.style.top;
		var strMoveType = _MOVETYPE;
		
		objWidth = objWidth.substr(0,objWidth.length-2);
		objHeight = objHeight.substr(0,objHeight.length-2);
		objX = objX.substr(0,objX.length-2)*1;
		objY = objY.substr(0,objY.length-2)*1;
		
		if (_MOVETYPE=="")strMoveType = _logMoveType;
		switch(strMoveType){
			case "proc_sm":
			case "proc_m":
			case "proc_e":
			case "proc_n":
			case "proc_snw":
			case "proc_nw":
				if (Step.FromProc == obj.id)
				{
					strPt = (objX+objWidth*Step.fromRelX)*3/4+"pt,"+(objY+objHeight*Step.fromRelY)*3/4+"pt"
					for (var j=2; j<arrPt.length; j++)
					{
						strPt = strPt + "," + arrPt[j]
					}
				}
				if (Step.ToProc == obj.id)
				{
					for (var j=0; j<arrPt.length-2; j++)
					{
						strPt = strPt + arrPt[j] + ","
					}
					strPt = strPt + (objX+objWidth*Step.toRelX)*3/4+"pt,"+(objY+objHeight*Step.toRelY)*3/4+"pt"
				}
				Step.Points = strPt;
				break;
			default :
		}
		objStepHTML.outerHTML = Step.toString();
      }
    }
  }
  _logMoveType = "";
}

//修改[任务]
function editProc(objId){
//修改  20090310 浏览流程设置，去掉弹出窗口
   if(_FLOW.eventFlag){
	   //修改时
		  var oldobj = new TProc(_FLOW), newobj = new TProc(_FLOW);
		  var proc = _FLOW.getProcByID(objId);
		  proc._FLOW=_FLOW;
		  oldobj.clone(proc);
		  var oldID = proc.ID;
		  //在修改流程时读取配置信息 20090316
		  if(proc.ProcType=="SplitProc"){
		  	alert('分支节点不能设置！');
		  	return;
		  }
		  //修改20090411(尾节点)
		  if(proc.ProcType=="EndProc"){
		  	alert('尾节点不需要设置！');
		  	return;
		  }
		  //window.open(contentPath+"/oswf/graphicdesigner/designConfig/workflow_config.jsp");
		  var obj=vmlOpenWin(contentPath+"/workflow/graphicdesigner/designConfig/workflow_config.jsp", proc, 750,680);
		  if(obj!=null){
			  if(proc.ProcType=="JoinProc"){
			  		_FLOW.joinConfigs=addConfig(_FLOW.joinConfigs,proc.ID,obj.joinConfigs);
			  }else{
				  _FLOW.formConfigs=addFormConfig(_FLOW.formConfigs,proc.ID,obj.formConfigs);
				  _FLOW.functionConfigs=addConfig(_FLOW.functionConfigs,proc.ID,obj.functionConfigs);
				  //_FLOW.joinConfigs=addConfig(_FLOW.joinConfigs,proc.ID,obj.joinConfigs);
				  //if(proc.ProcType=="NormalProc"){
				  _FLOW.roleConfigs=addConfig(_FLOW.roleConfigs,proc.ID,obj.roleConfigs);
				  _FLOW.formFuncConfigs=addConfig(_FLOW.formFuncConfigs,proc.ID,obj.formFuncConfigs);
				  //子流程配置
				  _FLOW.subConfigs=addConfig(_FLOW.subConfigs,proc.ID,obj.subConfigs);
				  //}
				  _FLOW.formId=obj.formId;
				  //修改 20090323
				  //_FLOW.wkType=obj.wkType;
			  }
		 
		  
		// alert(_FLOW.roleConfigs);
		// alert(_FLOW.functionConfigs);
		// alert(_FLOW.joinConfigs);
		  //_FLOW.formConfigs.push(obj.formConfigs);
		  //_FLOW.functionConfigs.push(obj.functionConfigs);
		 // _FLOW.joinConfigs.push(obj.joinConfigs);
		 // _FLOW.roleConfigs.push(obj.roleConfigs);
		 //  _FLOW.formId=obj.formId;
		   
		   }
   }else{
      //浏览
   }
 /* var oldobj = new TProc(_FLOW), newobj = new TProc(_FLOW);
  var proc = _FLOW.getProcByID(objId);
  oldobj.clone(proc);
  var oldID = proc.ID;
  if(vmlOpenWin("proc.htm", proc, 450,350)){
    if(oldID != proc.ID) changeProcID(oldID, proc.ID);
    DrawAll();
    newobj.clone(proc);
    pushLog("editproc", {"_old":oldobj,"_new":newobj});
    _FLOW.Modified = true;
    objFocusedOn(proc.ID);
  }*/
}
/*
 增加判断属性重复设置，覆盖旧的设置
*/
function addFormConfig(obj,nodeId,content){
   var rt=false;
   var index=0;
  for(var i=0;i<obj.length;i++){
     var  doc2= new ActiveXObject('Microsoft.XMLDOM'); 
	 doc2.loadXML(obj[i]);
	 var id=doc2.documentElement.getAttribute("stepId");
	 if(id==nodeId){
	    rt=true;
	    index=i;
	    break;
	 }
  }
   if(rt){
     obj[index]=content;
   }else{
     obj.push(content);
   }
   return obj;
}
function addConfig(obj,nodeId,content){
   var rt=false;
    var index=0;
  for(var i=0;i<obj.length;i++){
     var  doc2= new ActiveXObject('Microsoft.XMLDOM');
	 doc2.loadXML(obj[i]);
	 if(obj[i]==undefined || obj[i]=='')continue;
	 var id=doc2.documentElement.getAttribute("nodeId");
	 if(id==nodeId){
	    rt=true;
	    index=i;
	    break;
	 }
  }
   if(rt){
     obj[index]=content;
   }else{
     obj.push(content);
   }
   return obj;
}
//修改[路径]
function editStep(objId){
  var oldobj = new TStep(_FLOW), newobj = new TStep(_FLOW);
  var step = _FLOW.getStepByID(objId);
  oldobj.clone(step);
  /*if(vmlOpenWin("step.htm", step, 450,350)){
    _FLOW.Modified = true;
    DrawAll();
    objFocusedOn(step.ID);
    newobj.clone(step);
    pushLog("editstep", {"_old":oldobj,"_new":newobj});
  }*/
  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  var strPt = step.Points;
  var aryPt = strPt.split(',');
  var nPt = aryPt.length-1;
  isSelectPoint = 0;
      _strPt2 = "";
      _strPt1 = "";
	  for(i=0;i<=nPt;i=i+2){
		var m = aryPt[i].substr(0,aryPt[i].length-2)*1.333;
	    var n = aryPt[i+1].substr(0,aryPt[i+1].length-2)*1.333;
	  	var sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
			if(isSelectPoint==0&&sqrta<=10){
			  _strSltPt = aryPt[i]+',' +aryPt[i+1];
			}
			else if(isSelectPoint==1){
				    _strPt2 = _strPt2+','+aryPt[i]+','+aryPt[i+1];
			}
			     else{
				    _strPt1 = _strPt1+','+aryPt[i]+',' +aryPt[i+1];
			}
      }    
      if(_strPt1!= ''){
	     _strPt1 = _strPt1.substr(1);
      }
  step.Points = _strPt1+_strPt2;
  updateFlow(document.getElementById(step.ID));
  document.getElementById(step.ID).outerHTML=step.toString();
   //修改  20090310 属性窗口显示设置
	  if(_FLOW.eventFlag){
	  	stuffProp();
	  }
  _FLOW.Modified = true;
}

//需要特点检查的属性
function beforePropChange(oItem){
  if("rule" in oItem){
    r = new RegExp(oItem.rule);
    if(!r.test(oItem.value)){
      alert(oItem.msg);
      oItem.focus();
      if(oItem.tagName.toLowerCase() == "input") oItem.select();
      return false;
    }
  }
  return true;
}

function setPropID(oItem){
  var obj = null,obj2 = null;
  obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var oldID = obj.ID;
  if(oldID == oItem.value) return;
  obj2 = _FLOW.getProcByID(oItem.value);
  if(obj2 == null) obj2 = _FLOW.getStepByID(oItem.value);
  if(obj2 != null){
    alert("编号[" + oItem.value +"-" + obj2.Text + "]已经存在！请重新输入！");
    oItem.focus();
    oItem.select();
    return;
  }
  document.all(_FOCUSTEDOBJ.id + "Text").id = oItem.value + "Text";
  obj.ID = oItem.value;
  _FOCUSTEDOBJ.id = oItem.value;
  if(_FOCUSTEDOBJ.typ == "Proc") changeProcID(oldID, obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"ID","_old":oldID,"_new":obj.ID});
}

//修改名称
function setPropText(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.Text == oItem.value) return;
  var oldValue = obj.Text;
  obj.Text = oItem.value;
  //if(obj.ObjType == "Proc")DrawAll();
  DrawAll();
  //else
  //  DrawTree();
  objFocusedOn(obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"Text","_old":oldValue,"_new":obj.Text});
}

//修改活动类型
function setActFlag(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.actFlag == oItem.value) return;
  var oldValue = obj.actFlag;
  if (oItem.value == "1013")
  {
	 var Porc
	  for(var i = 0; i< _FLOW.Procs.length; i ++){
		Proc = _FLOW.Procs[i];
		if (Proc.actFlag.substr(0,4)=="1013")
		{
			alert("一个流程里面不允许存在2个开始活动");
			return false;
		}
	  }
  }
  if (oItem.value == "2013")
  {
	 var Porc
	  for(var i = 0; i< _FLOW.Procs.length; i ++){
		Proc = _FLOW.Procs[i];
		if (Proc.actFlag.substr(0,4)=="2013")
		{
			alert("一个流程里面不允许存在2个结束活动");
			return false;
		}
	  }
  }
  obj.actFlag = oItem.value + '' + obj.actFlag.substr(4,4);
  if (oldValue.substr(0,4)=="1013" && oItem.value != "1013")
  {
	  _FLOW.deleteStepByID(_FLOW.ID);
	  DrawVML();
  }
  if (oItem.value == "2013")
  {
	  obj.ProcType = "EndProc";
	  obj.ShapeType = "Oval";
	  obj.Width = "40";
	  obj.Height = "40";
	  DrawVML();
  }
  else if (oItem.value == "1013")
  {
	  Step = new TStep(_FLOW);
	  Step.FromProc = "begin";
	  Step.Text = "流程开始";
	  Step.ID = _FLOW.ID;
	  Step.ToProc = obj.ID;
	  Step.zIndex = 2;
	  Step.ShapeType = "PolyLine";
	  _FLOW.addStep(Step);
	  DrawVML();
  }
  if (oldValue.substr(0,4)=="2013" && oItem.value != "2013")
  {
	  obj.ProcType = "NormalProc";
	  obj.ShapeType = "Rect";
	  obj.Width = "100";
	  obj.Height = "40";
	  DrawVML();
  }
  objFocusedOn(obj.ID);
}

//修改最大执行人数
function setWaittime(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.waittime == oItem.value) return;
  var oldValue = obj.waittime;
  obj.waittime = oItem.value;
}

//修改运行时联线
function setIsSltTrans(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var isSltTrans = oItem.checked ? 1 : 0;
  if(obj.isSltTrans == isSltTrans) return;
  obj.isSltTrans = oItem.checked ? 1 : 0;
}

//修改所有联线使用相同权限
function setIsSameCredit(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var isSameCredit = oItem.checked ? 1 : 0;
  if(obj.isSameCredit == isSameCredit) return;
  obj.isSameCredit = oItem.checked ? 1 : 0;
}

function setPropShapeType(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.ShapeType == oItem.value) return;
  var oldValue = obj.ShapeType;
  obj.ShapeType = oItem.value;
  DrawVML();
  objFocusedOn(obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"ShapeType","_old":oldValue,"_new":obj.ShapeType});
}

function setPropTextWeight(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.TextWeight == oItem.value) return;
  var oldValue = obj.TextWeight;
  obj.TextWeight = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Text").style.fontSize = oItem.value;
  pushLog("editprop",{"obj":obj,"prop":"TextWeight","_old":oldValue,"_new":obj.TextWeight});
}

function setPropStrokeWeight(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.StrokeWeight == oItem.value) return;
  var oldValue = obj.StrokeWeight;
  obj.StrokeWeight = oItem.value;
  _FOCUSTEDOBJ.strokeweight = obj.StrokeWeight;
  pushLog("editprop",{"obj":obj,"prop":"StrokeWeight","_old":oldValue,"_new":obj.StrokeWeight});
}

function setPropzIndex(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.zIndex == oItem.value) return;
  var oldValue = obj.zIndex;
  obj.zIndex = oItem.value;
  _FOCUSTEDOBJ.style.zIndex = obj.zIndex;
  pushLog("editprop",{"obj":obj,"prop":"zIndex","_old":oldValue,"_new":obj.zIndex});
}

function setPropX(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.X == oItem.value) return;
  var oldValue = obj.X;
  obj.X = oItem.value;
  _FOCUSTEDOBJ.style.left = obj.X;
  _MOVETYPE = "proc_sm"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"X","_old":oldValue,"_new":obj.X});
}

function setPropY(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Y == oItem.value) return;
  var oldValue = obj.Y;
  obj.Y = oItem.value;
  _FOCUSTEDOBJ.style.top = obj.Y;
  _MOVETYPE = "proc_sm"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Y","_old":oldValue,"_new":obj.Y});
}

function setPropW(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Width == oItem.value) return;
  var oldValue = obj.Width;
  obj.Width = oItem.value;
  _FOCUSTEDOBJ.style.width = obj.Width;
  _MOVETYPE = "proc_snw"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Width","_old":oldValue,"_new":obj.Width});
}

function setPropH(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Height == oItem.value) return;
  var oldValue = obj.Height;
  obj.Height = oItem.value;
  _FOCUSTEDOBJ.style.height = obj.Height;
  _MOVETYPE = "proc_snw"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Height","_old":oldValue,"_new":obj.Height});
}

function setPropCond(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.Cond == oItem.value) return;
  var oldValue = obj.Cond;
  obj.Cond = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Text").innerHTML = oItem.value;
  pushLog("editprop",{"obj":obj,"prop":"Cond","_old":oldValue,"_new":obj.Cond});
}

function setPropStartArrow(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.StartArrow == oItem.value) return;
  var oldValue = obj.StartArrow;
  obj.StartArrow = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Arrow").startarrow = obj.StartArrow;
  pushLog("editprop",{"obj":obj,"prop":"StartArrow","_old":oldValue,"_new":obj.StartArrow});
}

function setPropEndArrow(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.EndArrow == oItem.value) return;
  var oldValue = obj.EndArrow;
  obj.EndArrow = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Arrow").endarrow = obj.EndArrow;
  pushLog("editprop",{"obj":obj,"prop":"EndArrow","_old":oldValue,"_new":obj.EndArrow});
}

function setPropFromProc(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.FromProc == oItem.value) return;
  //判断是否已经有重复的Step
  if(_FLOW.StepPathExists(oItem.value, obj.ToProc)){
    alert("已经有一个路径从[" + oItem[oItem.selectedIndex].text + "]至[" + _FLOW.getProcByID(obj.ToProc).Text + "]，请更改！");
    oItem.focus();
    return false;
  }
  /*if(oItem.value == obj.ToProc && obj.ShapeType == "Line"){
    alert("起点与终点过程相同的[路径]只能使用折角线，请先恢复原起点并更改形状！");
    oItem.focus();
    return false;
  }*/
  if(oItem.value == obj.ToProc){
	  alert("不能指向本身！");
	  oItem.focus();
	  return false;
  }
  var oldValue = obj.FromProc;
  obj.FromProc = oItem.value;
  if(obj.ShapeType == "Line"){
    obj.getPath();
    _FOCUSTEDOBJ.from = obj.FromPoint;
    _FOCUSTEDOBJ.to = obj.ToPoint;
  }
  else if(obj.ShapeType == "PolyLine"){
    _FOCUSTEDOBJ.points.value = obj.reGetPath();
  }
  //DrawDataView();
  pushLog("editprop",{"obj":obj,"prop":"FromProc","_old":oldValue,"_new":obj.FromProc});
}

function setPropToProc(oItem){
	alert(oItem);
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.ToProc == oItem.value) return;
  //判断是否已经有重复的Step
  if(_FLOW.StepPathExists(obj.FromProc, oItem.value)){
    alert("已经有一个路径从[" + _FLOW.getProcByID(obj.FromProc).Text + "]至[" + oItem[oItem.selectedIndex].text + "]，请更改！");
    oItem.focus();
    return false;
  }
  /*if(oItem.value == obj.FromProc && obj.ShapeType == "Line"){
    alert("起点与终点过程相同的[路径]只能使用折角线，请先恢复原起点并更改形状！");
    oItem.focus();
    return false;
  }*/
  if(oItem.value == obj.FromProc){
	  alert("不能指向本身！");
	  oItem.focus();
	  return false;
  }
  var oldValue = obj.ToProc;
  obj.ToProc = oItem.value;
  if(obj.ShapeType == "Line"){
    obj.getPath();
    _FOCUSTEDOBJ.from = obj.FromPoint;
    _FOCUSTEDOBJ.to = obj.ToPoint;
  }
  else if(obj.ShapeType == "PolyLine"){
    _FOCUSTEDOBJ.points.value = obj.reGetPath();
  }
  //DrawDataView();
  pushLog("editprop",{"obj":obj,"prop":"ToProc","_old":oldValue,"_new":obj.ToProc});
}
//改变文本框
function onTextGetFocus(e,r,c)
{
  e.cols=c;
  e.rows=r;
}

function onTextBlur(e,r,c)
{
  e.cols=c;
  e.rows=r;
}
function stuffProp(){
  for(var i = propview.rows.length - 1; i > 0; i--)
    propview.deleteRow(i);
  if(_FOCUSTEDOBJ == null) return;
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var idTR, idTD, oSelect, oActSelect, oCoSelect, oOption;
 //Text
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "名称";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input id="pName" type="text" maxlength="20" rule="^\\S+$" msg="[名称]不能为空或包含空字符！" onblur="if(beforePropChange(this)) setPropText(this);" name="pText" maxlength="10" value="' + obj.Text + '" style="width:95%;">';
  
    

}

//CACHE
function emptyLog(){
  _DOLOG = [];
  _DOLOGINDEX = -1;
}

function pushLog(act, obj){
  var newLog = _DOLOG.slice(0, _DOLOGINDEX + 1);
  _DOLOG = newLog;
  _DOLOGINDEX = _DOLOG.push({"act" : act, "val": obj}) - 1;
}

function getLog(){
  return _DOLOG[_DOLOGINDEX];
}


function undoLog(){
  if(_DOLOGINDEX == -1){
    alert("没有操作记录可以撤消.");
    return;
  }
  if(doLog("undo")) _DOLOGINDEX --;
}

function redoLog(){
  if(_DOLOGINDEX == _DOLOG.length - 1){
    alert("没有操作记录可以恢复.");
    return;
  }
  _DOLOGINDEX ++;
  doLog("redo");
}

function doLog(act){
  var log = getLog();
/*TODO 是否要进行提醒
if(!confirm("确定要*"+(act == "undo"?"撤消":"恢复")+"*最后一次操作[" + log.act + "]吗？")){
    if(act == "redo") _DOLOGINDEX --;
    return false;
  }*/
  switch(log.act){
    case "addproc":
      act == "undo"?_FLOW.deleteProcByID(log.val.ID):_FLOW.addProc(log.val);
      DrawAll();
      break;
    case "addstep":
      act == "undo"?_FLOW.deleteStepByID(log.val.ID):_FLOW.addStep(log.val);
      DrawAll();
      break;
    case "delproc":
      act == "undo"?_FLOW.addProc(log.val):_FLOW.deleteProcByID(log.val.ID);
      DrawAll();
      break;
    case "delstep":
      act == "undo"?_FLOW.addStep(log.val):_FLOW.deleteStepByID(log.val.ID);
      DrawAll();
      break;
    case "editproc":
      if(act == "undo"){
        var Proc = _FLOW.getProcByID(log.val._new.ID);
        Proc.clone(log.val._old);
        if(log.val._new.ID != log.val._old.ID) changeProcID(log.val._new.ID, log.val._old.ID);
      }
      else{
        var Proc = _FLOW.getProcByID(log.val._old.ID);
        Proc.clone(log.val._new);
        if(log.val._new.ID != log.val._old.ID) changeProcID(log.val._old.ID, log.val._new.ID);
      }
      DrawAll();
      objFocusedOn(act == "undo"?log.val._old.ID:log.val._new.ID);
      break;
    case "editstep":
      if(act == "undo"){
        var Step = _FLOW.getStepByID(log.val._new.ID);
        Step.clone(log.val._old);
      }
      else{
        var Step = _FLOW.getStepByID(log.val._old.ID);
        Step.clone(log.val._new);
      }
      DrawVML();
      objFocusedOn(act == "undo"?log.val._old.ID:log.val._new.ID);
      break;
    case "moveproc":
      var obj = _FLOW.getProcByID(log.val.objID);
      if(act == "undo"){
        obj.setPropValue("X", log.val._old.X);
        obj.setPropValue("Y", log.val._old.Y);
        obj.setPropValue("Width", log.val._old.Width);
        obj.setPropValue("Height", log.val._old.Height);
      }
      else{
        obj.setPropValue("X", log.val._new.X);
        obj.setPropValue("Y", log.val._new.Y);
        obj.setPropValue("Width", log.val._new.Width);
        obj.setPropValue("Height", log.val._new.Height);
      }
	  _logMoveType = log.val.moveType;
      changeProcPos(obj.InnerObject);
      break;
    case "editprop":
      var CurrentProp = (act == "undo"?log.val._old:log.val._new);
      log.val.obj[log.val.prop] = CurrentProp;
      switch(log.val.prop){
        case "ID":
          if(log.val.obj.ObjType == "Proc") act == "undo"?changeProcID(log.val._new, log.val._old):changeProcID(log.val._old, log.val._new);
          DrawVML();
          objFocusedOn(log.val.obj.ID);
          break;
        case "Text":
          DrawAll();
          objFocusedOn(log.val.obj.ID);
          break;
        case "ShapeType":
          DrawVML();
          objFocusedOn(log.val.obj.ID);
          break;
        case "TextWeight":
          document.all(log.val.obj.ID + "Text").style.fontSize = CurrentProp;
          break;
        case "zIndex":
          log.val.obj.InnerObject.style.zIndex = CurrentProp;
          break;
        case "StrokeWeight":
          log.val.obj.InnerObject.strokeweight = CurrentProp;
          break;
        case "X":
          log.val.obj.InnerObject.style.left = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Y":
          log.val.obj.InnerObject.style.top = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Width":
          log.val.obj.InnerObject.style.width = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Height":
          log.val.obj.InnerObject.style.height = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Cond":
          document.all(log.val.obj.ID + "Text").innerHTML = CurrentProp;
          break;
        case "StartArrow":
          document.all(log.val.obj.ID + "Arrow").startarrow = CurrentProp;
          break;
        case "EndArrow":
          document.all(log.val.obj.ID + "Arrow").endarrow = CurrentProp;
          break;
        case "FromProc":
        case "ToProc":
          if(log.val.obj.ShapeType == "Line"){
            log.val.obj.getPath();
            log.val.obj.InnerObject.from = log.val.obj.FromPoint;
            log.val.obj.InnerObject.to = log.val.obj.ToPoint;
          }
          else if(log.val.ShapeType == "PolyLine"){
            log.val.obj.InnerObject.points.value = log.val.obj.reGetPath();
          }
          //DrawDataView();
          break;
      }      
      break;
  }
 //修改  20090310 属性窗口显示设置
  if(_FLOW.eventFlag){
  	stuffProp();
  }
  return true;
}

function doProcMouseDown(obj, x, y){
  //判断是否是画线
  if(_TOOLTYPE == "line" || _TOOLTYPE == "polyline"){
    _CURRENTX = x;
    _CURRENTY = y;
    _MOVEOBJ = document.all("_lineui");
    //_MOVEOBJ.from = _CURRENTX + "," + _CURRENTY;
	_MOVEOBJ.from = _CURRENTX + "," + (_CURRENTY - 0);//原代码
    _MOVEOBJ.to = _MOVEOBJ.from;
    _MOVEOBJ.style.display = "block";
    _MOVETYPE = _TOOLTYPE;
  }
  else{
    var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) -x <= 2);
    var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
    if(rightSide && bottomSide)
      _MOVETYPE = "proc_nw";
    else if(rightSide)
      _MOVETYPE = "proc_e";
    else if(bottomSide)
      _MOVETYPE = "proc_n";
    else{
      _MOVETYPE = "proc_m";
      _CURRENTX = x - obj.offsetLeft;
      _CURRENTY = y - obj.offsetTop;
    }
    _MOVEOBJ = obj;
  }
  window.event.cancelBubble = true;
}

// 这里的x,y是换算过的值(/ZOOM)
function fireProcMouseDown(x, y){
  var curProc = null;
  for(var i = 0; i< _FLOW.Procs.length; i ++){
    Proc = _FLOW.Procs[i];
    if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width))
        && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
	  if (curProc == null || Proc.zIndex >= curProc.zIndex) // 重叠的情况下取上面那个
	    curProc = Proc;
    }
  }

  if (curProc != null) {
    obj = document.getElementById(curProc.ID);
    objFocusedOn(obj.id);
    doProcMouseDown(obj, x, y);
    return true;
  }
  return false;
}

// 这里的x,y是换算过的值(/ZOOM)
function doProcMouseMove(obj, x, y){
  if(_TOOLTYPE == "line" || _TOOLTYPE == "polyline")
   
  document.all.Canvas.style.cursor = "crosshair";
  else{
    var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) -x <= 2);
    var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
    if(rightSide && bottomSide)
      document.all.Canvas.style.cursor = "NW-resize";
    else if(rightSide)
      document.all.Canvas.style.cursor = "E-resize";
    else if(bottomSide)
      document.all.Canvas.style.cursor = "N-resize";
    else
      document.all.Canvas.style.cursor = "hand";
  }
}

// 这里的x,y是换算过的值(/ZOOM)
function fireProcMouseMove(x, y){
  if (document.all.Canvas == null) return;
  for(var i = 0; i< _FLOW.Procs.length; i ++){
    Proc = _FLOW.Procs[i];
	  obj = document.getElementById(Proc.ID);
    if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width))
      && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
      doProcMouseMove(obj, x, y);	
	  return true;
    }
  }
  if (i >= _FLOW.Procs.length){
    document.all.Canvas.style.cursor = (_TOOLTYPE == "point"?"default":"crosshair");
  }
  return false;
}

function doDocMouseDown(){

  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  var oEvt = event.srcElement;
  if (oEvt.id == "tableContainer" || oEvt.id == "") return; 	// 过滤数据视图/对象视图上的事件
  if (oEvt.typ=="Step")
  {
	  document.all.Canvas.style.cursor = "default";
	  return;
  }
  if (fireProcMouseDown(x, y)) return;		// 过滤图元上的事件
  switch(_TOOLTYPE){
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":
      if(oEvt.tagName != "DIV") return;
      if(oEvt.id != "Canvas") return;
      var obj = document.all("_" + _TOOLTYPE + "ui");
      _CURRENTX = x;
      _CURRENTY = y;
      obj.style.left = _CURRENTX;
      obj.style.top = _CURRENTY;
      obj.style.width  = 0;
      obj.style.height = 0;
      obj.style.display = "block"
      _MOVETYPE = _TOOLTYPE;
      break;
  //修改
    case "splitrect":
      if(oEvt.tagName != "DIV") return;
      if(oEvt.id != "Canvas") return;
      var obj = document.all("_" + _TOOLTYPE + "ui");
      _CURRENTX = x;
      _CURRENTY = y;
      obj.style.left = _CURRENTX;
      obj.style.top = _CURRENTY;
      obj.style.width  = 0;
      obj.style.height = 0;
      obj.style.display = "block";
      _MOVETYPE = _TOOLTYPE;
      break;
  }
}

function doDocMouseMove(){
var x = (event.x + document.body.scrollLeft) / _ZOOM;
var y = (event.y + document.body.scrollTop) / _ZOOM;
var m,n,aryPt,_movePt,sqrta,_moveLine
  switch(_MOVETYPE){
    case "line":	
    case "polyline":
		//_MOVEOBJ.to = x + "," + y;
	    _MOVEOBJ.to = x + "," + (y - 0);//原代码
      break;
	case "line_m":
		var zx=x*_ZOOM
		var zy=y*_ZOOM
		if (oOval==null)createOval(zx,zy);
		if (_PointOrLine==0)
		{
			oOval.fillcolor = "blue";
			oOval.strokecolor = "blue";
		}
		m = _clkPx.substr(0,_clkPx.length-2)*4/3;
		n = _clkPy.substr(0,_clkPy.length-2)*4/3;
		sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
	    var _arySltLine = _strSltLine.split(',');
		if(sqrta>10)
	    {
			oOval.style.left = zx-3;
			oOval.style.top = zy-3;
			if(_PointOrLine==0){
			  _movePt = (x*3/4)+'pt,'+(y*3/4)+"pt";
			  _MOVEOBJ.Points = _strPt1+""+_movePt+""+_strPt2;
			}
			else{
			  _moveLine = _arySltLine[0]+','+_arySltLine[1]+','+x*3/4+'pt,'+y*3/4+'pt,'+_arySltLine[2]+','+_arySltLine[3];
			  _MOVEOBJ.Points = _strLine1+_moveLine+_strLine2;
			}
			document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
			_FLOW.Modified = true;
		}
	  break;
    case "proc_m":
		m = _clkPx.substr(0,_clkPx.length-2)*4/3;
		n = _clkPy.substr(0,_clkPy.length-2)*4/3;
		sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
		if(sqrta>10)
	    {
			var newX = x - _CURRENTX;
			var newY = y - _CURRENTY;
			if(newX < 0) newX = 0;
			if(newY < 30) newY = 30;
			_MOVEOBJ.style.left = newX + 2; //修正两个像素
			_MOVEOBJ.style.top = newY + 2;
			changeProcPos(_MOVEOBJ);
		}
		break;
    case "proc_n":
      var newH = y - parseInt(_MOVEOBJ.style.top);
      if(newH < 30) newH = 30;
      _MOVEOBJ.style.height = newH;
      changeProcPos(_MOVEOBJ);
      break;
    case "proc_e":
      var newW = x - parseInt(_MOVEOBJ.style.left);
      if(newW < 30) newW = 30;
      _MOVEOBJ.style.width = newW;
      changeProcPos(_MOVEOBJ);
      break;
    case "proc_nw":
      var newW = x - parseInt(_MOVEOBJ.style.left);
      var newH = y - parseInt(_MOVEOBJ.style.top);
      if(newW < 30) newW = 30;
      if(newH < 30) newH = 30;
      _MOVEOBJ.style.width = newW;
      _MOVEOBJ.style.height = newH;
      changeProcPos(_MOVEOBJ);
      break;
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":
      var newX = x;
      var newY = y;
      var obj = document.all("_" + _MOVETYPE + "ui");
      if(newX < _CURRENTX) obj.style.left = newX;
      obj.style.width = Math.abs(newX - _CURRENTX);
      if(newY < _CURRENTY) obj.style.top = newY;
      obj.style.height = Math.abs(newY - _CURRENTY);
      break;
    //修改
      case "splitrect":
      var newX = x;
      var newY = y;
      var obj = document.all("_" + _MOVETYPE + "ui");
      if(newX < _CURRENTX) obj.style.left = newX;
      obj.style.width = Math.abs(newX - _CURRENTX);
      if(newY < _CURRENTY) obj.style.top = newY;
      obj.style.height = Math.abs(newY - _CURRENTY);
      break;
    default: // 不是在移动状态下，将鼠标移动消息交给图元
      fireProcMouseMove(x, y); 
	 
  }
}
//获取当前点到当前对象的8个点中最近的点
function getNearPt(oProc,x,y)
{
	var objProc = document.getElementById(oProc.ID)
	var fromW = parseInt(objProc.style.width);
	var fromH = parseInt(objProc.style.height);
	var fromX = parseInt(objProc.style.left);
	var fromY = parseInt(objProc.style.top);
	var arrX = new Array();
	var arrY = new Array();
	var arrPos = new Array(0,0.25,0.5,0.75,1);
	var nX, nY, m,n , nearPt,poX,poY;
	arrX[0] = fromX;
	arrX[1] = fromX + fromW / 4;
	arrX[2] = fromX + fromW / 2;
	arrX[3] = fromX + fromW * 3 / 4;
	arrX[4] = fromX + fromW;

	arrY[0] = fromY;
	arrY[1] = fromY + fromH / 4;
	arrY[2] = fromY + fromH / 2;
	arrY[3] = fromY + fromH * 3 / 4;
	arrY[4] = fromY + fromH;
	m=0;
	n=0;
	for (var i=0; i<4; i++)
	{
		Math.abs(x-arrX[i]) < Math.abs(x-arrX[i+1]) ? m=m : m=i+1;
		Math.abs(y-arrY[i]) < Math.abs(y-arrY[i+1]) ? n=n : n=i+1;
	}
	if (m>0 && m<4 && n>0 && n<4)
	{
		if (m==3)
			m=4;
		else 
			m=0;
		if (n==3)
			n=4;
		else 
			n=0;
	}

	nX = arrX[m];
	nY = arrY[n];
	poX = arrPos[m];
	poY = arrPos[n];
	nearPt = nX*3/4+"pt,"+nY*3/4+"pt|~|"+poX+","+poY
	//nearPt = nX + "," + nY;
	return nearPt;
}
function doDocMouseUp(){
  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  switch(_MOVETYPE){
	 case "line_m":
	 //修改 选中bug问题
	      var zx=x*_ZOOM
	  	  var zy=y*_ZOOM
	      if (oOval==null)createOval(zx,zy);
	      
		 document.body.removeChild(oOval);
		 oOval = null;
		//var Step = _FLOW.getStepByID(_MOVEOBJ.ID);
		var ProcTo
		ProcTo = _FLOW.getProcAtXY(x, y);
		if (ProcTo==null)
		{
			if (ptMoveType=="from" || ptMoveType=="to")
			{
				_MOVEOBJ.Points = _strPt1+_strSltPt+_strPt2;
				document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
			}
		}
		else
	    {
			if (ptMoveType=="from" || ptMoveType=="to")
			{
				var Proc1,Proc2;
				Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
				Proc2 = _FLOW.getProcAtXY(x, y);
				
				var nearPt = getNearPt(ProcTo,x,y);
				var strPt = nearPt.split("|~|")[0];
				var arrPt = strPt.split(",");
				var strPos = nearPt.split("|~|")[1];
				var nX = arrPt[0].substr(0,arrPt[0].length-2);
				var nY = arrPt[1].substr(0,arrPt[1].length-2);
				var relX = strPos.split(",")[0];
				var relY = strPos.split(",")[1];
				if (ptMoveType=="from")
				{
					Proc1 = ProcTo;
					Proc2 = _FLOW.getProcByID(_MOVEOBJ.ToProc);
				}
				if (ptMoveType=="to")
				{
					Proc1 = _FLOW.getProcByID(_MOVEOBJ.FromProc);
					Proc2 = ProcTo;
				}
				var existProc = _FLOW.StepPathExists(Proc1.ID, Proc2.ID)
				if((Proc1.ID == Proc2.ID)||(existProc!=null && existProc.ID != _MOVEOBJ.ID)
					||(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc")
					||(_MOVEOBJ.FromProc=="begin" && ptMoveType=="from" && ProcTo.ID != _MOVEOBJ.FromProc)
					||(_MOVEOBJ.FromProc=="begin" && ptMoveType=="to" && ProcTo.ID != _MOVEOBJ.ToProc)
					||(_MOVEOBJ.FromProc!="begin" && ProcTo.ID=="begin")){
					if(existProc!=null && existProc.ID != _MOVEOBJ.ID)
						alert("已经有一个路径从[" + _FLOW.getProcByID(Proc1.ID).Text + "]至[" + _FLOW.getProcByID(Proc2.ID).Text + "]，操作不成功");
					if(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc")
						alert("路径必须符合“起点不能是结束结点，终点不能是开始结点”的规则！");
					if(Proc1.ID == Proc2.ID)
						alert("不能指向本身！");
					//FromProc是开始节点的联线
					//if (ptMoveType=="from"&&ProcTo.ID!=_MOVEOBJ.FromProc)alert(_MOVEOBJ.FromProc);
					//if (ptMoveType=="to"&&ProcTo.ID!=_MOVEOBJ.ToProc)alert(_MOVEOBJ.FromProc);
					_MOVEOBJ.Points = _strPt1+_strSltPt+_strPt2;
					document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
				}
				else
				{
					if (ptMoveType=="from")
					{
						_MOVEOBJ.setPropValue("FromProc",ProcTo.ID);
						_MOVEOBJ.fromRelX = relX;
						_MOVEOBJ.fromRelY = relY;
					}
					if (ptMoveType=="to")
					{
						_MOVEOBJ.setPropValue("ToProc",ProcTo.ID);
						_MOVEOBJ.toRelX = relX;
						_MOVEOBJ.toRelY = relY;
					}
					_MOVEOBJ.Points = _strPt1+strPt+_strPt2;
					document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
					//修改  20090310 属性窗口显示设置
					  if(_FLOW.eventFlag){
					  	stuffProp();
					  }
					_FLOW.Modified = true;
					var newobj = new TStep(_FLOW);
					newobj.clone(_MOVEOBJ)
					pushLog("editstep", {"_old":_MOVELINEOBJ,"_new":newobj});
				}
			}
			
		}
		updateFlow(document.getElementById(_MOVEOBJ.ID));
		break;
    case "proc_m":
    case "proc_n":
    case "proc_e":
    case "proc_nw":
      var Proc = _FLOW.getProcByID(_MOVEOBJ.id);
      var oldVal = {"X":Proc.X,"Y":Proc.Y,"Width":Proc.Width,"Height":Proc.Height};
      if(_MOVETYPE == "proc_m"){
        Proc.setPropValue("X",_MOVEOBJ.style.left);
        Proc.setPropValue("Y",_MOVEOBJ.style.top);
      }
      else{
        Proc.setPropValue("Width",_MOVEOBJ.style.width);
        Proc.setPropValue("Height",_MOVEOBJ.style.height);
      }
      //如果只是细小的调节，不记录历史操作
      if(Math.abs(parseInt(oldVal.X) - parseInt(Proc.X)) > 2 || Math.abs(parseInt(oldVal.Y) - parseInt(Proc.Y)) > 2
         || Math.abs(parseInt(oldVal.Width) - parseInt(Proc.Width)) > 2 || Math.abs(parseInt(oldVal.Height) - parseInt(Proc.Height)) > 2){
        pushLog("moveproc", {"objID":Proc.ID,"moveType":_MOVETYPE,"_old": oldVal, "_new":{"X":Proc.X,"Y":Proc.Y,"Width":Proc.Width,"Height":Proc.Height}});
      }
      //修改  20090310 属性窗口显示设置
					  if(_FLOW.eventFlag){
					  	stuffProp();
					  }
      _FLOW.Modified = true;
	  updateFlow(_MOVEOBJ);
	  saveStepsToProc(_MOVEOBJ);
      break;
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":
//修改
     case "splitrect":
    case "line":
    case "polyline":
      var obj = document.all("_" + (_MOVETYPE == "polyline"?"line":_MOVETYPE) + "ui");
      obj.style.display = "none";
      if(_MOVETYPE == "line" || _MOVETYPE == "polyline"){

		var Proc1,Proc2, Step;
        Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
        Proc2 = _FLOW.getProcAtXY(x, y);
		if(Proc1 == null || Proc2 == null){
          alert("起点或终点不是[任务]，请在[任务]图形上按住鼠标并拖动到某[任务]图形上松开.");
          break;;
        }
        if(_FLOW.StepPathExists(Proc1.ID, Proc2.ID)!=null){
          alert("已经有一个路径从[" + _FLOW.getProcByID(Proc1.ID).Text + "]至[" + _FLOW.getProcByID(Proc2.ID).Text + "]，请更改！");
          break;
        }
        if(Proc1.ID == Proc2.ID){
          alert("不能指向本身！");
          break;
        }
		if(Proc1.ProcType == "BeginProc")
		{
						//alert("开始节点不允许手动联线!");
			//break;
		}
		//修改
		if(Proc1.ProcType == "SplitProc"&&_FLOW.StepPathIsTo(Proc1.ID, Proc2.ID)!=null)
		{
			alert("分支节点不允许回退!");
			break;
		}
		//if(Proc1.ProcType == "NormalProc"&&_FLOW.StepPathIsTo(Proc1.ID, Proc2.ID)!=null)
		//{
			//alert("不允许回退!");
			//break;
		//}
		//修改 20090323
		if(Proc1.ProcType == "JoinProc"&&_FLOW.StepPathIsTo(Proc1.ID, Proc2.ID)!=null)
		{
			alert("汇聚节点不允许回退!");
			break;
		}
		if(Proc1.ProcType == "SplitProc"&&Proc2.ProcType == "JoinProc")
		{
			alert("分支节点不允许直接到汇聚节点!");
			break;
		}
		if(Proc1.ProcType == "SplitProc"&&Proc2.ProcType == "EndProc")
		{
			alert("汇聚节点不允许直接到结束节点!");
			break;
		}
		if(Proc1.ProcType == "JoinProc"&&Proc2.ProcType == "SplitProc")
		{
			alert("汇聚节点不允许直接到分支节点!");
			break;
		}
        if(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc"){
          alert("路径必须符合“起点不能是结束结点，终点不能是开始结点”的规则！");
          break;
        }
        Step = new TStep(_FLOW);
        Step.FromProc = Proc1.ID;
        Step.ToProc = Proc2.ID;
		//Step.Cond = Step.Text
		Step.zIndex = 2;
        Step.ShapeType = "PolyLine";

        _FLOW.addStep(Step);
        pushLog("addstep", Step);
        DrawAll();
		
      }
      else{
        var Proc = new TProc(_FLOW);
        //Proc.ShapeType = (_MOVETYPE == "rect"?"Rect":(_MOVETYPE == "roundrect"?"RoundRect":(_MOVETYPE == "oval"?"Oval":"Diamond")));
              // Proc.ShapeType = (_MOVETYPE == "rect"?"Rect":(_MOVETYPE == "roundrect"?"RoundRect":(_MOVETYPE=="fillrect"?"FillRect":(_MOVETYPE == "oval"?"Oval":"Diamond"))));
		       //修改
        Proc.ShapeType = (_MOVETYPE == "rect"?"Rect":(_MOVETYPE == "roundrect"?"RoundRect":(_MOVETYPE=="fillrect"?"FillRect":_MOVETYPE=="splitrect"?"splitrect":(_MOVETYPE == "oval"?"Oval":"Diamond"))));
		if(Proc.ShapeType=='splitrect'){
		  Proc.ProcType='SplitProc';
		}else if(Proc.ShapeType=='FillRect'){
		   Proc.ProcType='JoinProc';
		}
		Proc.X = parseInt(obj.style.left);
        Proc.Y = parseInt(obj.style.top);
        Proc.Width = parseInt(obj.style.width);
        Proc.Height = parseInt(obj.style.height);
        if(Proc.Width < 20 || Proc.Height < 20){
          //alert("图形长宽小于最小值，将采用默认大小");
          Proc.Width = 100;
          Proc.Height = 40;
        }
		_FLOW.addProc(Proc);
        pushLog("addproc", Proc);
        DrawAll();
      }
      break;
  }
  _MOVETYPE = "";
  _MOVELINEOBJ = null;
  _MOVEOBJ = null;
  return true;
}
function doDocSelectStart(){
  var oEvt = event.srcElement.tagName;
  if(oEvt != "INPUT") return false;
}

function doDocKeyDown(){
  switch(event.keyCode){
    case 46: //Del
      if(_FOCUSTEDOBJ != null && event.srcElement.tagName != "INPUT" && event.srcElement.tagName != "SELECT")
       // alert(2);
       //修改 20090320 删除的修改
        mnuDelObj();
       // alert(3);
      break;
   /* case 90: //z
      if(event.ctrlKey && event.shiftKey)
        redoLog();
      else if(event.ctrlKey)
        undoLog();
      break;
    case 89: //y
      if(event.ctrlKey) redoLog();
      break;
    case 112: //F1
      mnuAbout();
      break;
	case 187: //+
      if(event.ctrlKey) mnuSetZoom('in');
      break;
	case 189: //-
      if(event.ctrlKey) mnuSetZoom('out');
      break;*/
	case 83: //s
      if(event.ctrlKey) mnuSaveFlow();
      break;
	/*case 79: //o
      if(event.ctrlKey) mnuOpenFlow();
      break;
	case 78: //n
      if(event.ctrlKey) mnuNewFlow();
      break;
	case 67: //C
      if(event.ctrlKey) mnuCopyProc();
      break;*/
	case 13: //enter
      document.all.Canvas.focus();
      break;
  }
}
function showExcuter(id)
{

}
/*  设置是否为控件设置鼠标事件，在浏览 和修改流程用到*/
function setEventFlag(t){
  if(_FLOW.eventFlag){
    _FLOW.eventFlag=t;
    document.onselectstart = doDocSelectStart;
	document.onmousedown = doDocMouseDown;
	document.onmousemove = doDocMouseMove;
	document.onmouseup = doDocMouseUp;
	document.onkeydown = doDocKeyDown;
	}else{
  	_FLOW.eventFlag=t;
	}
}
document.onerror = new Function("return false;"); 

/*--------------------------------------------------|
| 本作品取得原作者授权修改自 support@tops.com.cn    |
| 的作品topflow                                     |
|                                                   |
| 本文件是CommitFlow的最核心文件，定义了设计器用到的|
| 各种对象类，以及各种类的方法、属性等，请勿更改此  |
| 文件！                                            |
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

//const
//[任务]类定义
function TProc(AFlow,id){
  this.ObjType = "Proc";
  this.Flow = AFlow;
  this.ID = id;
  if(this.ID == undefined) this.ID = this.Flow.getMaxProcID();
  this.Text = "新建" + this.ID;
  this.ShapeType = "RoundRect";
  this.ProcType = "NormalProc";
  this.Width = "80";
  this.Height = "50";
  this.X = "50";
  this.Y = "50";
  this.TextWeight = "9pt";
  this.StrokeWeight = "1";
  this.zIndex = 1;
  this.InnerObject = null;
  this.MoveType = "";
  
  //新增
  this.actFlag = "";
  this.waittime = "";
  this.isSltTrans = "1";
  this.isSameCredit = "1";
}

TProc.prototype.getInnerObject = function(){
  if(this.InnerObject == null) this.InnerObject = document.all(this.ID);
  return this.InnerObject;
}

TProc.prototype.setFocus = function(){
  this.getInnerObject.StrokeColor = this.Flow.Config.ProcFocusedStrokeColor;
}

TProc.prototype.lostFocus = function(){
  this.getInnerObject.StrokeColor = (this.ProcType == "NormalProc")?this.Flow.Config.ProcColor:this.Flow.Config._ProcColor;
}

TProc.prototype.doClick = function(){
  this.Flow.selectObject(this.ID, "Proc");
}

TProc.prototype.mouseDown = function(){
  var rightSide = (parseInt(this.X) + parseInt(this.Width) - event.x <= 2);
  var bottomSide = (parseInt(this.Y) + parseInt(this.Height) - event.y <= 2);
  if(rightSide && bottomSide)
    this.MoveType = "nw";
  else if(rightSide)
    this.MoveType = "e";
  else if(bottomSide)
    this.MoveType = "n";
  else
    this.MoveType = "m";
  this.getInnerObject.setCapture();
  switch(this.MoveType){
    case "m":
      this.CurrentX = event.x - this.InnerObject.offsetLeft;
      this.CurrentY = event.y - this.InnerObject.offsetTop;
	  break;
    case "front":
    case "back":
      if(_TOOLTYPE == "front")
        this.Flow.brintToFront(this);
      else
        this.Flow.sendToBack(this);
      this.getInnerObject.style.zIndex = this.zIndex;
      break;
  }
}

TProc.prototype.mouseMove = function(){

  switch(this.MoveType){
    case "m":
      this.X = event.x - this.CurrentX;
      this.Y = event.y - this.CurrentY;
	  if(this.X < 0) this.X = 0;
      if(this.Y < 30) this.Y = 30;
      this.InnerObject.style.left = this.X;
      this.InnerObject.style.top = this.Y;
      break;
    case "n":
      this.Height = event.y - this.Y;
      if(this.Height < 30) this.Height = 30;
      this.InnerObject.style.height = this.Height;
      break;
    case "e":
      this.Width = event.x - this.X;
      if(this.Width < 30) this.Width = 30;
      this.InnerObject.style.width = this.Width;
      break;
    case "nw":
      this.Width = event.x - this.X;
      this.Height = event.y - this.Y;
      if(this.Width < 30) this.Width = 30;
      if(this.Height < 30) this.Height = 30;
      this.InnerObject.style.width = this.Width;
      this.InnerObject.style.height = this.Height;
      break;
    default://没有任何按键的情况下，计算位置并显示相应的操作鼠标
      var rightSide = (parseInt(this.X) + parseInt(this.Width) - event.x <= 2);
      var bottomSide = (parseInt(this.Y) + parseInt(this.Height) - event.y <= 2);
      if(rightSide && bottomSide)
        this.getInnerObject.style.cursor = "NW-resize";
      else if(rightSide)
        this.getInnerObject.style.cursor = "E-resize";
      else if(bottomSide)
        this.getInnerObject.style.cursor = "N-resize";
      else
        this.getInnerObject.style.cursor = "hand";
      break;
  }
}

TProc.prototype.mouseUp = function(){
  if(this.MoveType != ""){
    this.getInnerObject.releaseCapture();
    if(this.MoveType == "nw"){
      if(parseInt(this.InnerObject.style.top)<-10){
        alert("对象上边界超出，将自动调整.");
        this.InnerObject.style.top=30;
      }
      if(parseInt(this.InnerObject.style.left)<-10){
        alert("对象左边界超出，将自动调整.");
        this.InnerObject.style.left=30;
      }
    }
  }
  this.MoveType = "";
}

TProc.prototype.clone = function(AProc){
  this.ID = AProc.ID;
  this.Text = AProc.Text;
  this.ShapeType = AProc.ShapeType
  this.ProcType = AProc.ProcType;
  this.Width = AProc.Width;
  this.Height = AProc.Height;
  this.X = AProc.X;
  this.Y = AProc.Y;
  this.TextWeight = AProc.TextWeight;
  this.StrokeWeight = AProc.StrokeWeight;
  this.zIndex = AProc.zIndex;
  this.InnerObject = null;
  this.MoveType = "";
}

TProc.prototype.setPropValue = function(AProp, AValue){
  switch(AProp){
    case "ID":
      var oldID = this.ID;
      if(oldID == AValue) return true;
      if(this.Flow.IDExists(AValue)){
        alert("编号[" + AValue + "]已经存在！");
        return false;
      }
      this.InnerObject.all(oldID + "Text").id = AValue + "Text";
      this.ID = AValue;
      this.InnerObject.id = AValue;
      this.Flow.changeProcID(oldID, AValue);
      break;
    case "X":
      this.X = AValue;
      this.InnerObject.style.left = AValue;
      break;
    case "Y":
      this.Y = AValue;
      this.InnerObject.style.top = AValue;
      break;
    case "Width":
      this.Width = AValue;
      this.InnerObject.style.width = AValue;
      break;
    case "Height":
      this.Height = AValue;
      this.InnerObject.style.height = AValue;
      break;
  }
}
//[任务]字符串化函数
TProc.prototype.toString = function(flag){
  var cl = this.Flow.Config;
  var nStockeColor,nTextColor;
  if(this.ProcType == 'BeginProc' || this.ProcType == 'EndProc'){
    nTextColor = cl._ProcTextColor;
    nStrokeColor = cl._ProcColor;
  }
  else{
    nTextColor = cl.ProcTextColor;
    nStrokeColor = cl.ProcColor;
  }
  var arrVal = new Array();
  arrVal["id"]              = this.ID;
  //arrVal["title"]           = this.ID + ':' + this.Text + "\n\nX-" + this.X + " Y-" + this.Y + " W-" + this.Width + " H-" + this.Height + " Z-" + this.zIndex;
  arrVal["title"]           = this.ProcType;
  arrVal["sc"]              = nStrokeColor;
  arrVal["st"]              = this.ProcType;
  arrVal["l"]               = this.X;
  arrVal["t"]               = this.Y;
  arrVal["w"]               = this.Width;
  arrVal["h"]               = this.Height;
  arrVal["z"]               = this.zIndex;
  arrVal["sw"]              = this.StrokeWeight;
  arrVal["fsc"]             = cl.ProcFocusedStrokeColor;
  arrVal["shadowenable"]    = cl.IsProcShadow;
  arrVal["shadowcolor"]     = cl.ProcShadowColor;
  arrVal["3denable"]        = cl.IsProc3D;
  arrVal["3ddepth"]         = cl.Proc3DDepth;
  arrVal["sc1"]             = cl.ProcColor1;
  arrVal["sc2"]             = cl.ProcColor2;
  arrVal["join"]            =cl.JoinProc;
  arrVal["tc"]              = nTextColor;
  arrVal["fs"]              = this.TextWeight;
  arrVal["text"]            = this.Text;

  //新增
  arrVal["af"]              = this.actFlag;
  arrVal["wt"]               = this.waittime;
  arrVal["ist"]               = this.isSltTrans;
  arrVal["isc"]               = this.isSameCredit;
  //修改  20090312
   if(flag!=""&&flag=="view"){
  		return stuffShape(getShapeView(this.ShapeType), arrVal);
   }
   //修改20090612
   else if(flag!=""&&flag=="deal"){
   		return stuffShape(getShapeDeal(this.ShapeType), arrVal);
   }	
   else{
        return stuffShape(getShapeVal(this.ShapeType), arrVal);
   }
  
}

//[路径]类定义
function TStep(AFlow,id){
  this.ObjType = "Step";
  this.Flow = AFlow;
  this.ID = id;
  if(this.ID == undefined) this.ID = this.Flow.getMaxStepID();
  this.Text = "新建" + this.ID;
  this.ShapeType = "Line";
  this.FromProc = "";
  this.ToProc = "";
  this.Points = "";
  this.Cond = "";
  this.StartArrow = "none";
  this.EndArrow = "Classic";
  this.TextWeight = "9pt";
  this.StrokeWeight = "1";
  this.zIndex = 0;
  this.InnerObject = null;
//新增
  this.fromRelX = 0;
  this.fromRelY = 0;
  this.toRelX = 0;
  this.toRelY = 0;
}

TStep.prototype.clone = function(AStep){
  this.ID = AStep.ID;
  this.Text = AStep.Text;
  this.ShapeType = AStep.ShapeType;
  this.FromProc = AStep.FromProc;
  this.ToProc = AStep.ToProc;
  this.Points = AStep.Points;  
  this.Cond = AStep.Cond;
  this.StartArrow = AStep.StartArrow;
  this.EndArrow = AStep.EndArrow;
  this.TextWeight = AStep.TextWeight;
  this.StrokeWeight = AStep.StrokeWeight;
  this.zIndex = AStep.zIndex;
  this.Points = AStep.Points;

  this.fromRelX = AStep.fromRelX;
  this.fromRelY = AStep.fromRelY;
  this.toRelX = AStep.toRelX;
  this.toRelY = AStep.toRelY;
}

TStep.prototype.setPropValue = function(AProp, AValue){
  switch(AProp){
    case "ID":
      var oldID = this.ID;
      if(oldID == AValue) return true;
      if(this.Flow.IDExists(AValue)){
        alert("编号[" + AValue + "]已经存在！");
        return false;
      }
      this.InnerObject.all(oldID + "Text").id = AValue + "Text";
      this.InnerObject.all(oldID + "Arrow").id = AValue + "Arrow";
      this.ID = AValue;
      this.InnerObject.id = AValue;
      break;
	case "Points":
	  this.Points = AValue;
	  break;
	case "FromProc":
	  this.FromProc = AValue;
	  break;
	case "ToProc":
	  this.ToProc = AValue;
	  break;
  }
}

//[路径]字符串化函数
TStep.prototype.toString = function(flag){
  var StepHTML = '';
  var cl = this.Flow.Config;
  var arrVal = new Array();
  arrVal["id"]              = this.ID;
  arrVal["title"]           = this.ID + ':' + this.Text;
  arrVal["sc"]              = cl.StepColor;
  arrVal["pt"]              = this.getPath();
  arrVal["z"]               = this.zIndex;
  arrVal["sw"]              = this.StrokeWeight;
  arrVal["fsc"]             = cl.StepFocusedStrokeColor;
  arrVal["sa"]              = this.StartArrow;
  arrVal["ea"]              = this.EndArrow;
  arrVal["cond"]            = this.Cond;
  arrVal["text"]            = this.Text;
  //修改 20090312
   if(flag!=""&&flag=="view"){
  		return stuffShape(getShapeView(this.ShapeType), arrVal);
   }
   //修改20090612
   else if(flag!=""&&flag=="deal"){
   		return stuffShape(getShapeDeal(this.ShapeType), arrVal);
   }	
   else{
        return stuffShape(getShapeVal(this.ShapeType), arrVal);
   }
}

TStep.prototype.getInnerObject = function(){
  if(this.InnerObject == null) this.InnerObject = document.all(this.ID);
  return this.InnerObject;
}

TStep.prototype.setFocus = function(){
  this.getInnerObject.StrokeColor = this.Flow.Config.StepFocusedStrokeColor;
}

TStep.prototype.lostFocus = function(){
  this.getInnerObject.StrokeColor = this.Flow.Config.StepColor;
}

TStep.prototype.doClick = function(){
  this.Flow.selectObject(this.ID, "Step");
}

//流程图类定义
function TTopFlow(AName){
  this.name = AName;
  this.ID = "";
  this.Text = "";
  this.FileName = "";
  this.FormID = "";
  this.Modified = false;
  this.Steps = [];
  this.Procs = [];
  //增加
  this.formConfigs = [];
  this.functionConfigs = [];
  this.joinConfigs = [];
  this.roleConfigs = [];
  this.formFuncConfigs=[];//增加表单执行函数
  this.subConfigs=[];//增加子流程配置
  this.formId;
  this.wkType;//流程类别(20090323)
  
  this.SelectedObject = null;
  this.Password = "";
 this.Config = {
	    _ProcColor                : "#FF0000",  //开始/结束
	    _ProcTextColor            : "#FF0000",  //开始/结束
	    ProcColor                 : "#0000FF",
	    ProcTextColor             : "#0000FF",
	    ProcFocusedStrokeColor    : "#FFFF00",
	    IsProcShadow              : "T",
	    ProcShadowColor           : "#B3B3B3",
	    ProcColor1                : "#FFFFFF",// : "#FFFFFF",
	    ProcColor2                : "#FFFFFF",//: "#FFFFFF",
	    IsProc3D                  : "F",
	    Proc3DDepth               : "20",
	    StepFocusedStrokeColor    : "#904E80",
	    StepColor                 : "#0000FF",
	    JoinProc                  : "#C4D6FC"
	  }
}

//
TTopFlow.prototype.getInnerObject = function(){
  for(var i = 0;i<this.Procs.length; i++)
    this.Procs[i].getInnerObject();
  for(i = 0;i<this.Steps.length; i++)
    this.Steps[i].getInnerObject();
}
//选中某个对象
TTopFlow.prototype.selectObject = function(aID, aType){
  this.unSelectObject();
  this.SelectedObject = (aType == "Proc")?this.getProcByID(aID):this.getStepByID(aID);
  this.SelectedObject.setFocus();
}

//取消选中某个对象
TTopFlow.prototype.unSelectObject = function(){
  if(this.SelectedObject != null) this.SelectedObject.lostFocus();
  this.SelectedObject = null;
}

//清除流程图的内容
TTopFlow.prototype.clear = function(){
  this.FileName = '';
  this.Steps.length = 0;
  this.Procs.length = 0;
}

//新建流程图
TTopFlow.prototype.createNew = function(AName){
  this.clear();
  //增加开始结点
 // alert();
 // this.getMaxProcID();
  Proc = new TProc(this, this.getMaxProcID());
  Proc.Text = "开始";
  Proc.ShapeType = "Oval";
  Proc.ProcType = "BeginProc";
  Proc.X = "0";
  Proc.Y = "30";
  this.addProc(Proc);
  //增加结束结点
  Proc = new TProc(this, this.getMaxProcID());
  Proc.Text = "结束";
  Proc.ShapeType = "Oval";
  Proc.ProcType = "EndProc";
  Proc.X = "700";
  Proc.Y = "350";
  this.addProc(Proc);
}

//添加流程图的[任务]元素对象
TTopFlow.prototype.addProc = function(AProc){
  if(this.Procs.length >= 20){
    alert("根据比赛要求，最多不允许超过20个任务!");
    return false;
  }
  this.Modified = true;
  this.Procs[this.Procs.length] = AProc;
}

//添加流程图的[路径]元素对象
TTopFlow.prototype.addStep = function(AStep){
  this.Steps[this.Steps.length] = AStep;
  this.Modified = true;
}

TTopFlow.prototype.changeProcID = function(OldID, NewID){alert("changeProcID");
  var Step;
  for(var i = 0; i< this.Steps.length; i++){
    Step = this.Steps[i];
    if(Step.FromProc == OldID) Step.FromProc = NewID;
    if(Step.ToProc == OldID) Step.ToProc = NewID;
  }
}
//获取一个[任务]的二维数据集视图
TTopFlow.prototype.getProcDataView = function(AProcID){
  var arr = [], Step;
  for(var i = 0; i < this.Steps.length; i++){
    Step = this.Steps[i];
    if(Step.ToProc == AProcID){
      S = this.getProcByID(Step.FromProc).Text;
      arr[arr.length] = new Array(Step.ID, S, Step.Cond);
    }
  }
  return arr;
}

//获取整个[流程图]的二维数据集视图
TTopFlow.prototype.DataView = function(){
  var Proc; arrDataView = [], arr = [];
  var i,j, u, k = 0;
  for(i = 0; i < this.Procs.length; i++){
    Proc = this.Procs[i];
    arr.length = 0;
    arr = this.getProcDataView(Proc.ID);
    u = arr.length;
    if(u != undefined && u != null && u > 0){
      for(j = 0; j < arr.length; j++){
        arrDataView[k++] = {
          "ProcID"      : Proc.ID,
          "ProcText"    : Proc.Text,
          "Idx"         : j + 1,
          "PreProcID"   : arr[j][0],
          "PreProcText" : arr[j][1],
          "Cond"        : arr[j][2]
        }
      }
    }
  }
  return arrDataView;
}

TTopFlow.prototype.hasPriorProc = function(AProcID){
  for(var i = 0; i < this.Steps.length; i++)
    if(this.Steps[i].ToProc == AProcID) return true;
  return false;
}

TTopFlow.prototype.hasNextProc = function(AProcID){
  for(var i = 0; i < this.Steps.length; i++)
    if(this.Steps[i].FromProc == AProcID) return true;
  return false;
}

TTopFlow.prototype.validate = function(){
  var ErrMsg = []; WarnMsg = [];
  var Proc, PType,ProcID;
  if(this.Procs.length==2){
  	return '流程定义不允许只存在首节点和尾节点！';
  }
  //if(this.Procs.length-1!=_FLOW.roleConfigs.length){
  	//return '节点权限设置不完整，请检查！';
  //}
  for(var i = 0; i < this.Procs.length; i++){
    Proc = this.Procs[i];
    PType = (Proc.ProcType == "NormalProc"?"中间任务":(Proc.ProcType == "BeginProc"?"开始任务":"结束任务"));
    if(Proc.ProcType == "NormalProc" || Proc.ProcType == "SplitProc" || Proc.ProcType == "JoinProc"  || Proc.ProcType == "EndProc")
      if(!this.hasPriorProc(Proc.ID)) ErrMsg.push("[" + Proc.Text + "] - " + PType + "必须有输入路径");
    if(Proc.ProcType == "NormalProc" || Proc.ProcType == "SplitProc" || Proc.ProcType == "JoinProc"  ||  Proc.ProcType == "BeginProc")
      if(!this.hasNextProc(Proc.ID)) ErrMsg.push("[" + Proc.Text + "] - " + PType + "必须有输出路径");
  }
  if(ErrMsg!=null && ErrMsg!=""){
  	return ErrMsg.join("\n") + WarnMsg.join("\n");
  }
  //校验表单
  if(_FLOW.formId==undefined){
 	return '无法保存当前流程，请设置流程属性,绑定表单模板！';
  }
  //校验节点配置
  for(var i=0;i<this.Procs.length;i++){
  	Proc = this.Procs[i];
  	if(Proc.ProcType!="SplitProc" && Proc.ProcType!="EndProc"){
  		ProcID=Proc.ID;
  		//首节点
  		if(Proc.ProcType=="BeginProc"){
  			//判断表单是否设置
  			if(_FLOW.formConfigs==null || _FLOW.formConfigs==""){
  				return '请双击首节点对节点表单项进行设置';
  			}else{
  				var arry=_FLOW.formConfigs;
  				var tFlag=true;
		        for(var j=0;j<arry.length;j++){
	            	var xml=arry[j];
	            	if(xml==""){
	              		continue;
	            	}
		          	var  doc=new ActiveXObject("Microsoft.XMLDOM"); 
			      	doc.async="false"; 
				  	doc.loadXML(xml);
				  	var stepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
				  	if(stepId==ProcID){
				  		tFlag=false;
				  		break;
				  	}
				}
				if(tFlag){
					return '请双击首节点对节点表单项进行设置';
				}
  			}
  			//判断节点是否授权
  			if(_FLOW.roleConfigs==null || _FLOW.roleConfigs==""){
  				return '请双击首节点对节点进行设置';
  			}else{
  			  	var arry=_FLOW.roleConfigs;
  				var tFlag=true;
		        for(var j=0;j<arry.length;j++){
	            	var xml=arry[j];
	            	if(xml==""){
	              		continue;
	            	}
		          	var  doc=new ActiveXObject("Microsoft.XMLDOM"); 
			      	doc.async="false"; 
				  	doc.loadXML(xml);
				  	var nId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				  	if(nId==ProcID){
				  		tFlag=false;
				  		break;
				  	}
				}
				if(tFlag){
					return '请双击首节点对节点进行设置';
				}
  			}
  		}
  		//普通节点
  		else if(Proc.ProcType=="NormalProc"){
  			//判断节点是否授权
  			if(_FLOW.roleConfigs==null || _FLOW.roleConfigs==""){
  				return '请双击节点：['+Proc.Text+'],对节点授权项进行设置';
  			}else{
  			  	var arry=_FLOW.roleConfigs;
  				var tFlag=true;
		        for(var j=0;j<arry.length;j++){
	            	var xml=arry[j];
	            	if(xml==""){
	              		continue;
	            	}
		          	var  doc=new ActiveXObject("Microsoft.XMLDOM"); 
			      	doc.async="false"; 
				  	doc.loadXML(xml);
				  	var nId=doc.getElementsByTagName("workflow")[0].getAttribute("nodeId");
				  	if(nId==ProcID){
				  		tFlag=false;
				  		break;
				  	}
				}
				if(tFlag){
					return '请双击节点：['+Proc.Text+'],对节点授权项进行设置';
				}
  			}
  		}
  	}
  }
  //检验各节点表单formId是否一致
  var formIds=_FLOW.formId;
  var info="";
  var tFlag=true;
  for(var i=0;i<this.Procs.length;i++){
    Proc = this.Procs[i];
  	if(Proc.ProcType!="SplitProc" && Proc.ProcType!="EndProc"){
  		ProcID=Proc.ID;
	  	var arry=_FLOW.formConfigs;
	  	var tFlag=true;
	  	for(var j=0;j<arry.length;j++){
	  		var xml=arry[j];
	  		if(xml==""){
	  			continue;
	  		}else{
  				var  doc=new ActiveXObject("Microsoft.XMLDOM"); 
		      	doc.async="false"; 
			  	doc.loadXML(xml);
			  	var stepId=doc.getElementsByTagName("step")[0].getAttribute("stepId");
			  	if(stepId==ProcID){
			  		var forms=doc.getElementsByTagName("step/form");
			  		var formId=forms[0].getAttribute("formId");
		  			if(formIds!=formId){
		  				info=info+'节点['+Proc.Text+']绑定表单id：'+formId+'\n';
		  				tFlag=false;
		  			}else{
		  				info=info+'节点['+Proc.Text+']绑定表单id：'+formId+'\n';
		  			}
			  	}	
	  		}
	  	}
	  	if(!tFlag){
	  		info=info+'\n以上节点绑定的表单不一致，请双击不一致的节点对表单进行设置!';
	  		return info;
	  	}
	}
  }
  return "";
}
//从XML文件中载入流程图
TTopFlow.prototype.loadFromXML = function(AFileName){
  this.clear();
  this.FileName = AFileName;
  var xmlDoc = new ActiveXObject("MSXML2.DOMDocument");
  xmlDoc.async = false;
  var flag = xmlDoc.load(AFileName);
  //var flag = xmlDoc.load('/_CommitSys/_CommitFlow/_createFlowXML.asp?defid='+AFileName);
  if (!flag) {
    alert('文件[' + AFileName + '载入失败！');
    this.createNew("");
    return false;
  }
  var xmlRoot = xmlDoc.documentElement;
  this.Text = xmlRoot.getAttribute("text");
  this.Password = xmlRoot.getAttribute("password");
  this.ID = xmlRoot.getAttribute("id");
  this.FormID = xmlRoot.getAttribute("formid");
  //Load Proc
  var Procs = xmlRoot.getElementsByTagName("Procs").item(0);       
  var id, oNode, Prop;
  for (i = 0;i < Procs.childNodes.length;i++) {
    var Proc = Procs.childNodes.item(i);
    Prop = Proc.getElementsByTagName("BaseProperties").item(0);
    id = Prop.getAttribute("id");
    oNode = new TProc(this,id);
    oNode.Text = Prop.getAttribute("text");
    oNode.ProcType = Prop.getAttribute("procType");

	//新增
	oNode.actFlag = Prop.getAttribute("actFlag");
	oNode.waittime = Prop.getAttribute("waittime");
	oNode.isSltTrans = Prop.getAttribute("isSltTrans");
	oNode.isSameCredit = Prop.getAttribute("isSameCredit");
    
    Prop = Proc.getElementsByTagName("VMLProperties").item(0);
    oNode.ShapeType = Prop.getAttribute("shapetype");
    oNode.Width = Prop.getAttribute("width");
    oNode.Height = Prop.getAttribute("height");
    oNode.X = Prop.getAttribute("x");
    oNode.Y = Prop.getAttribute("y");
    oNode.TextWeight = Prop.getAttribute("textWeight");
    oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
    oNode.zIndex = Prop.getAttribute("zIndex");
    if(oNode.zIndex =='') oNode.zIndex = this.getMinZIndex() - 1;
    this.addProc(oNode);
  }
  //Load Step
  var Steps = xmlRoot.getElementsByTagName("Steps").item(0);
  for (i = 0;i < Steps.childNodes.length;i++){
    var Step = Steps.childNodes.item(i);
    Prop = Step.getElementsByTagName("BaseProperties").item(0);
    id = Prop.getAttribute("id");
    oNode = new TStep(this,id);
    oNode.Text = Prop.getAttribute("text");
    oNode.FromProc = Prop.getAttribute("from");
    oNode.ToProc = Prop.getAttribute("to");
    oNode.Cond = Prop.getAttribute("cond");
	oNode.Cond = oNode.Cond.replace(/\'/g,'"')
	

    Prop = Step.getElementsByTagName("VMLProperties").item(0);
	oNode.Points = Prop.getAttribute("points"); 
	oNode.fromRelX = Prop.getAttribute("fromRelX");
	oNode.fromRelY = Prop.getAttribute("fromRelY");
	oNode.toRelX = Prop.getAttribute("toRelX");
	oNode.toRelY = Prop.getAttribute("toRelY");
    oNode.ShapeType = Prop.getAttribute("shapetype");
    oNode.StartArrow = Prop.getAttribute("startArrow");
    oNode.EndArrow = Prop.getAttribute("endArrow");
    oNode.StrokeWeight = Prop.getAttribute("strokeWeight");
    oNode.zIndex = Prop.getAttribute("zIndex");
    if(oNode.zIndex =='') oNode.zIndex = this.getMinZIndex() - 1;   
    this.addStep(oNode);
  }
  this.Modified = false;
  return true;
}

//将流程图保存至服务器上的XML文件中
TTopFlow.prototype.SaveToXML = function(AUrl){
  var xmlDoc = new ActiveXObject('MSXML2.DOMDocument');
  xmlDoc.async = false;
  xmlDoc.loadXML('<?xml version="1.0" encoding="GBK"?><TopFlow/>');
  var xmlRoot = xmlDoc.documentElement;
  var xmlNodeGrp, xmlNode, xmlNode2;
  xmlRoot.setAttribute("id", this.ID);
  xmlRoot.setAttribute("formid", this.FormID);//新增
  xmlRoot.setAttribute("filename", this.FileName);
  xmlRoot.setAttribute("text", this.Text);
  xmlRoot.setAttribute("password", this.Password);
  
  //Save Proc
  var xmlNodeGrp = xmlDoc.createNode(1,"Procs",""); 
  xmlRoot.appendChild(xmlNodeGrp);
  for(var i = 0; i < this.Procs.length; i++){
    Proc = this.Procs[i];
    xmlNode = xmlDoc.createNode(1, "Proc", "");
    xmlNode2 = xmlDoc.createNode(1, "BaseProperties", "");
    xmlNode2.setAttribute("id", Proc.ID);
    xmlNode2.setAttribute("text", Proc.Text);
    xmlNode2.setAttribute("procType", Proc.ProcType);

	xmlNode2.setAttribute("actFlag", Proc.actFlag);
	xmlNode2.setAttribute("waittime", Proc.waittime);
	xmlNode2.setAttribute("isSltTrans", Proc.isSltTrans);
	xmlNode2.setAttribute("isSameCredit", Proc.isSameCredit);

    xmlNode.appendChild(xmlNode2);

    xmlNode2 = xmlDoc.createNode(1, "VMLProperties", "");
    xmlNode2.setAttribute("shapetype", Proc.ShapeType);
    xmlNode2.setAttribute("width", Proc.Width);
    xmlNode2.setAttribute("height", Proc.Height);
    xmlNode2.setAttribute("x", Proc.X);
    xmlNode2.setAttribute("y", Proc.Y);
    xmlNode2.setAttribute("textWeight", Proc.TextWeight);
    xmlNode2.setAttribute("strokeWeight", Proc.StrokeWeight);
    xmlNode2.setAttribute("zIndex", Proc.zIndex);
    xmlNode.appendChild(xmlNode2);

    xmlNodeGrp.appendChild(xmlNode);
  }
  //Save Step
  xmlNodeGrp = xmlDoc.createNode(1,"Steps",""); 
  xmlRoot.appendChild(xmlNodeGrp);
  for(i = 0; i < this.Steps.length; i++){
    Step = this.Steps[i];
    xmlNode = xmlDoc.createNode(1, "Step", "");

    xmlNode2 = xmlDoc.createNode(1, "BaseProperties", "");
    xmlNode2.setAttribute("id", Step.ID);
    xmlNode2.setAttribute("text", Step.Text);
    xmlNode2.setAttribute("from", Step.FromProc);
    xmlNode2.setAttribute("to", Step.ToProc);
    xmlNode2.setAttribute("cond", Step.Cond);
	
    xmlNode.appendChild(xmlNode2);

    xmlNode2 = xmlDoc.createNode(1, "VMLProperties", "");
	xmlNode2.setAttribute("points", Step.Points);
	xmlNode2.setAttribute("fromRelX", Step.fromRelX);
	xmlNode2.setAttribute("fromRelY", Step.fromRelY);
	xmlNode2.setAttribute("toRelX", Step.toRelX);
	xmlNode2.setAttribute("toRelY", Step.toRelY);
    xmlNode2.setAttribute("shapetype", Step.ShapeType);
    xmlNode2.setAttribute("startArrow", Step.StartArrow);
    xmlNode2.setAttribute("endArrow", Step.EndArrow);
    xmlNode2.setAttribute("strokeWeight", Step.StrokeWeight);
    xmlNode2.setAttribute("zIndex", Step.zIndex);
    xmlNode.appendChild(xmlNode2);

    xmlNodeGrp.appendChild(xmlNode);
  }
    var x = xmlDoc.xml;
  	var s="";
	var params="";
	//alert(this.roleConfigs);
	//return;
	//alert(this.formConfigs.join(','));
	//return;
	params="&pworkId="+pworkId+"&name="+_FLOW.FileName+"&data="+x+"&wkTypeId="+this.wkType+"&join="+this.joinConfigs.join(',');
	params=params+"&functions="+this.functionConfigs.join(',')+"&formConfig="+this.formConfigs.join(',')+"&roleConfig="+this.roleConfigs.join(',');
	params=params+"&formFuncConfig="+this.formFuncConfigs.join(',');
	params=params+"&subConfig="+this.subConfigs.join(',');
	//alert(this.roleConfigs.join(','));
	//alert(this.formFuncConfigs.join(','));
	//return;
  //alert("34"+pworkId);
  //alert(this.functionConfigs.join(','));
 // return;
  if(pworkId!=null && pworkId!=""){
  	if(issue=="启用"){
	  	if(confirm("1、如果对流程图形外观和流程人员授权进行修改，不用产生新版本。2、如果对流程结构进行调整，请产生新版本，否则有可能会造成已有实例无法运行。是否产生新版本？")){
	  		params=params+"&ifNewWk=0";//0产生新流程
	  	}else{
	  		params=params+"&ifNewWk=1";//1不产生新流程
	  	}
	}
  }
  $.ajax({type:"POST", url:webcontext+"/oswf/design/default.do?method=saveProcess", data:params, dataType:"json", success:function (msg) {
		alert(msg.result);
		//window.close();
		parent.location.reload();
	}});
  //$.post(contentPath+"/js/AddProcess", { pworkId:pworkId,name:_FLOW.FileName,data: x,wkType:this.wkType,join:this.joinConfigs.join(','),functions:this.functionConfigs.join(','), formConfig:this.formConfigs.join(','), roleConfig:this.roleConfigs.join(',')},
		//function (data, textStatus){
			// data 可以是 xmlDoc, jsonObj, html, text, 等等.
			//this; // 这个Ajax请求的选项配置信息，请参考jQuery.get()说到的this
			//alert(data.result);
		//}, "json");
}

//根据[任务]的ID获取[任务]对象
TTopFlow.prototype.getProcByID = function(id){
  for(var i = 0; i<this.Procs.length; i++)
    if(this.Procs[i].ID == id) return this.Procs[i];
  return null;
}

//根据[路径]的ID获取[路径]对象
TTopFlow.prototype.getStepByID = function(id){
  for(var i = 0; i<this.Steps.length; i++)
    if(this.Steps[i].ID == id) return this.Steps[i];
  return null;
}

TTopFlow.prototype.getProcAtXY = function(x, y){
  var Proc;
  for(var i = 0; i < this.Procs.length; i++){
    Proc = this.Procs[i];
	if(x >= parseInt(Proc.X) && x <= parseInt(Proc.X) + parseInt(Proc.Width) && y >= parseInt(Proc.Y) && y <= parseInt(Proc.Y) + parseInt(Proc.Height)){
      return Proc;
    }
  }
  return null;
}

TTopFlow.prototype.IDExists = function(id){
  var obj = _FLOW.getProcByID(id);
  if(obj != null) return true;
  var obj = _FLOW.getStepByID(id);
  return (obj != null);
}

TTopFlow.prototype.StepPathExists = function(FromProc, ToProc){
  var Step;
  for(var i = 0; i< this.Steps.length; i++){
    Step = this.Steps[i];
    if(Step.FromProc == FromProc && Step.ToProc == ToProc) return Step;
  }
  return null;
}
//新增 判断
TTopFlow.prototype.StepPathIsTo = function(FromProc, ToProc){
  var Step;
  for(var i = 0; i< this.Steps.length; i++){
    Step = this.Steps[i];
    if(Step.FromProc == ToProc && Step.ToProc == FromProc) return Step;
  }
  return null;
}


//根据[任务]的ID删除[任务]对象
TTopFlow.prototype.deleteProcByID = function(id){
  this.Modified = true;
  for(var i = 0; i< this.Procs.length; i++)
    if(this.Procs[i].ID == id)
      this.Procs.splice(i,1);
  //删除与些Proc关联的Step
  for(i = this.Steps.length - 1; i >= 0 ; i--)
    if(this.Steps[i].FromProc == id || this.Steps[i].ToProc == id)
      this.Steps.splice(i,1);
}

//根据[路径]的ID删除[路径]对象
TTopFlow.prototype.deleteStepByID = function(id){
  this.Modified = true;
  for(var i = 0; i< this.Steps.length; i++)
    if(this.Steps[i].ID == id)
      this.Steps.splice(i,1);
}

//获取流程图最顶层的Z轴值
TTopFlow.prototype.getMaxZIndex = function(){
  var m = 0;
  for(var i = 0; i < this.Procs.length; i++)
    m = Math.max(m, this.Procs[i].zIndex);
  for(i = 0; i < this.Steps.length; i++)
    m = Math.max(m, this.Steps[i].zIndex);
  return m;
}

//获取流程图最底层的Z轴值
TTopFlow.prototype.getMinZIndex = function(){
  var m = 0;
  for(var i = 0; i < this.Procs.length; i++)
    m = Math.min(m, this.Procs[i].zIndex);
  for(i = 0; i < this.Steps.length; i++)
    m = Math.min(m, this.Steps[i].zIndex);
  return m;
}

//将一个流程图元素对象移至最上层
TTopFlow.prototype.brintToFront = function(obj){
  this.Modified = true;
  obj.zIndex = this.getMaxZIndex() + 1;
}

//将一个流程图元素对象移至最底层
TTopFlow.prototype.sendToBack = function(obj){
  this.Modified = true;
  obj.zIndex = this.getMinZIndex() - 1;
}

//获取流程图下一个[任务]的缺省ID
//修改20090327
TTopFlow.prototype.getMaxProcID = function(){
  var s = "";
  //var i, j, u = this.Procs.length+this.Steps.length+1;
  //for(i = 1; i<= u; i++){
    //s = i;//"proc"+i;
   /* for(j = 0; j < u-1; j++){
      if(this.Procs[j].ID == s) break;
    }
    if(j == u) break;*/
  //}
  return s=indexId++;

}

//获取流程图下一个[路径]的缺省ID
//修改20090327
TTopFlow.prototype.getMaxStepID = function(){
  var s = "";
  //var i, j, u = this.Steps.length+this.Procs.length+1;
  //for(i = 1; i<= u; i++){
    //s =i;// "step"+i;
  /*  for(j = 0; j < u-1; j++){
      if(this.Steps[j].ID == s) break;
    }
    if(j == u) break;*/
  //}
  return s=indexId++;
}

//流程图内全部[任务]的字符串化函数
TTopFlow.prototype.ProcString = function(obj){
  var S = "",i;
  //修改 20090312
  for(i = 0; i< this.Procs.length; i++){
        var proc=this.Procs[i];
      // alert(proc.ProcType);
        var flag="false";
        var objFlag="";
         //有参数  查看和浏览
        if(obj!=undefined){
         if(proc.ProcType!="SplitProc"&&proc.ProcType!="JoinProc"){
		        for(var j=0;j<obj.length;j++){
		        		var objList=obj[j].split(":");
		        		var objStep=objList[0];
		        		objFlag=objList[1];
			           if(proc.ID==objStep){
			             flag=true;
			             break;
			           }else{
			               flag=false;
			           }
		        }
		        if(flag){
		        	
		        	if(objFlag=='1'){
		        		S += this.Procs[i].toString("deal");
		        	}else{
		        		S += this.Procs[i].toString("view");
		        	}
		          	 
		        }else{
		           	 S += this.Procs[i];
		        }
		   }
	  /*
     	 分支和聚合节点 如果为分支查看所有分支动作的导向步骤是否已经执行，
     	 如果执行，把次分支置为执行
 		 聚合：查看他的汇聚节点如果汇聚节点的出发步骤执行过，汇聚置为执行     
      */
    		  flag=false;
		      if(proc.ProcType=="SplitProc"){
		         //遍历所有动作
				for(var j = 0; j< this.Steps.length; j++){
				   var  Step = this.Steps[j];
				    if(Step.FromProc == proc.ID && isIn(Step.ToProc,obj)=="true") {
				         flag=true;
			             break;
				    }else{
				        flag=false;
				    }
		  		}          
		  		//alert(flag);
		  		if(flag){
		          	 S += this.Procs[i].toString("view");
		        }else{
		           	 S += this.Procs[i];
		        }
      
   			  }
   			  flag=false;
   			  if(proc.ProcType=="JoinProc"){
		         //遍历所有动作
				for(var j = 0; j< this.Steps.length; j++){
				   var Step = this.Steps[j];
				   //修改20090403
				   //alert(Step.ToProc +"  "+ proc.ID +" osIN "+ isIn(Step.FromProc,obj));
				    if((Step.ToProc == proc.ID ) && isIn(Step.FromProc,obj)=="ture") {
				         
				         flag=true;
			             break;
				    }else{
				        flag=false;
				    }
		  		}   
		  		if(flag){
		          	 S += this.Procs[i].toString("view");
		        }else{
		           	 S += this.Procs[i];
		        }       
      
   			  }
        }else{
         //无参数 修改和新建
       		   S += this.Procs[i];
        }

    }
  return S;
}
function isIn(a,arry){
    var rt="false";
     for(var i = 0; i< arry.length; i++){
     		var objList=arry[i].split(":");
       		var objStep=objList[0];
       		var objFlag=objList[1];
             if(a==objStep){
                rt="true";
                break;
             }else{
                rt="false";
             }
  	} 
  	return rt;
}

//流程图内全部[路径]的字符串化函数
TTopFlow.prototype.StepString = function(obj){
 //修改 20090312
  var S = "",i;
  for(i = 0; i< this.Steps.length; i++){
        var step=this.Steps[i];
        var flag="false";
         //有参数  查看和浏览
        if(obj!=undefined){
		        for(var j=0;j<obj.length;j++){
		        		var objList=obj[j].split(":");
		        		var objStep=objList[0];
		        		var objFlag=objList[1];
		        	   if(step.ToProc==objStep){
			           //if(step.FromProc==obj[j]||step.ToProc==obj[j]){
			             flag=true;
			              break;
			           }else{
			               flag=false;
			           }
		        }
		        //if(stepID
		        if(flag){ 
		          	 S += this.Steps[i].toString("view");
		        }else{
		        	var tFlag=false;
		        	for(var j=0;j<obj.length;j++){
		        		//alert(obj[j]);
		        		var objList=obj[j].split(":");
		        		var objStep=objList[0];
		        		var objFlag=objList[1];
		        		//alert(step.ToProc);
		        		//alert(step.FromProc);
		        		//alert(objStep);
		        		//alert(objFlag);
		        		if(step.ToProc!=objStep && step.FromProc==objStep && objFlag=='0' ){
		        				tFlag=true;
		        				break;	        			
		        		}
		        	}
		        	if(tFlag){
		        		S += this.Steps[i].toString("view");
		        	}else{
		        		S += this.Steps[i];
		        	}
		           	 
		        }
        }else{
         //无参数 修改和新建
       		   S += this.Steps[i];
        }
    }
  return S;
}

//流程图字符串化函数
TTopFlow.prototype.toString = function(){
  return this.ProcString() + this.StepString();
}

//获取[路径]的划线结点路径
TStep.prototype.getPath = function(){
  if(this.Points != null && this.Points !="")return this.Points;
  var fromProc =document.getElementById(this.FromProc), toProc = document.getElementById(this.ToProc);
  
  if (fromProc==null || toProc==null) return '';
  var fromW = parseInt(fromProc.style.width);
  var fromH = parseInt(fromProc.style.height);
  var toW = parseInt(toProc.style.width);
  var toH = parseInt(toProc.style.height);
  var fromX = parseInt(fromProc.style.left);
  var fromY = parseInt(fromProc.style.top);
  var toX = parseInt(toProc.style.left);
  var toY = parseInt(toProc.style.top);
  if(this.FromProc == this.ToProc) 
    return this.getSelfPath(fromX, fromY, fromW, fromH);

  if (ifRepeatProc(fromX,fromY,fromW,fromH,toX,toY,toW,toH)){
    return "";
  }
  else if(this.ShapeType == "PolyLine")
	{
    return this.getLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);}
  else
    return this.Points;
//    return this.getPolyLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);
    
}

//重新获取[路径]的划线结点路径
TStep.prototype.reGetPath = function(){
  var fromProc =document.getElementById(this.FromProc), toProc = document.getElementById(this.ToProc);
  
  if (fromProc==null || toProc==null) return '';
  var fromW = parseInt(fromProc.style.width);
  var fromH = parseInt(fromProc.style.height);
  var toW = parseInt(toProc.style.width);
  var toH = parseInt(toProc.style.height);
  var fromX = parseInt(fromProc.style.left);
  var fromY = parseInt(fromProc.style.top);
  var toX = parseInt(toProc.style.left);
  var toY = parseInt(toProc.style.top);
  if(this.FromProc == this.ToProc) 
    return this.getSelfPath(fromX, fromY, fromW, fromH);

  if (ifRepeatProc(fromX,fromY,fromW,fromH,toX,toY,toW,toH)){
    return "";
  }
  else if(this.ShapeType == "PolyLine")
	{
    return this.getLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);}
  else
    return this.Points;
//    return this.getPolyLinePath(fromX, fromY, fromW, fromH, toX, toY, toW, toH);
    
}


//获取当[路径]指向自身时的划线结点路径
TStep.prototype.getSelfPath = function(ProcX, ProcY, ProcW, ProcH){
  var constLength = 10;
  point0X = ProcX + ProcW -constLength;
  point0Y = ProcY + ProcH;
  
  point1X = point0X;
  point1Y = point0Y + constLength;
  
  point2X = ProcX + ProcW + constLength;
  point2Y = point1Y;

  point3X = point2X;
  point3Y = point0Y-constLength;

  point4X = ProcX + ProcW;
  point4Y = point3Y;
  return point0X + ',' + point0Y + ' ' + point1X + ',' + point1Y + ' ' + point2X + ',' + point2Y + ' ' + point3X + ',' + point3Y + ' ' + point4X + ',' + point4Y;
}

//获取当[路径]线型为直线时的划线结点路径
TStep.prototype.getLinePath = function(fromProcX, fromProcY, fromProcW, fromProcH, toProcX, toProcY, toProcW, toProcH){

  var fromX, fromY, toX, toY, fromRelX, fromRelY, toRelX, toRelY;
  if(fromProcY + fromProcH < toProcY){;       //FromProc完全在ToProc上方
    if(fromProcX + fromProcW < toProcX){      //FromProc完全在ToProc左方
      fromX = fromProcX + fromProcW;          //取FromProc右下角
      fromY = fromProcY + fromProcH;
      toX = toProcX;                          //取ToProc左上角
      toY = toProcY;
	  fromRelX = 1;
	  fromRelY = 1;
	  toRelX = 0;
	  toRelY = 0;
    }
    else if(fromProcX > toProcX + toProcW){     //FromProc完全在ToProc右方
      fromX = fromProcX;          //取FromProc左下角
      fromY = fromProcY + fromProcH;
      toX = toProcX + toProcW;
      toY = toProcY;
	  fromRelX = 0;
	  fromRelY = 1;
	  toRelX = 1;
	  toRelY = 0;
    }
    else{                                     //取FromProc下中结点
      fromX = fromProcX + parseInt(fromProcW / 2);
      fromY = fromProcY + fromProcH;
      toX = toProcX + parseInt(toProcW / 2);
      toY = toProcY;
	  fromRelX = 0.5;
	  fromRelY = 1;
	  toRelX = 0.5;
	  toRelY = 0;
    }
  }
  else if(fromProcY > toProcY + toProcH){;       //FromProc完全在ToProc下方
    if(fromProcX + fromProcW < toProcX){      //FromProc完全在ToProc左方
      fromX = fromProcX + fromProcW;          //取FromProc右上角
      fromY = fromProcY;
      toX = toProcX;                          //取ToProc左下角
      toY = toProcY + toProcH;
	  fromRelX = 1;
	  fromRelY = 0;
	  toRelX = 0;
	  toRelY = 1;
    }
    else if(fromProcX > toProcX + toProcW){     //FromProc完全在ToProc右方
      fromX = fromProcX;          //取FromProc左上角
      fromY = fromProcY;
      toX = toProcX + toProcW;      //取ToProc右下角
      toY = toProcY + toProcH;
	  fromRelX = 0;
	  fromRelY = 0;
	  toRelX = 1;
	  toRelY = 1;
    }
    else{                                     //取FromProc下中结点
      fromX = fromProcX + parseInt(fromProcW / 2);
      fromY = fromProcY;
      toX = toProcX + parseInt(toProcW / 2);
      toY = toProcY + toProcH;
	  fromRelX = 0.5;
	  fromRelY = 0;
	  toRelX = 0.5;
	  toRelY = 1;
    }
  }
  else if(fromProcX + fromProcW < toProcX){ //FromProc在toProc左方
    fromX = fromProcX + fromProcW;
    fromY = fromProcY + parseInt(fromProcH / 2);
    toX = toProcX;
    toY = toProcY + parseInt(toProcH / 2);
	fromRelX = 1;
	fromRelY = 0.5;
	toRelX = 0;
	toRelY = 0.5;
  }
  else{ //在右方
    fromX = fromProcX;
    fromY = fromProcY + parseInt(fromProcH / 2);
    toX = toProcX + toProcW;
    toY = toProcY + parseInt(toProcH / 2);
	fromRelX = 0;
	fromRelY = 0.5;
	toRelX = 1;
	toRelY = 0.5;
  }
 
  this.fromRelX = fromRelX;
  this.fromRelY = fromRelY;
  this.toRelX = toRelX;
  this.toRelY = toRelY;
  this.Points = fromX/4*3 + 'pt,' + fromY/4*3 + 'pt,' + toX/4*3 + 'pt,' + toY/4*3 + 'pt';
  return this.Points;

}

//获取当[路径]线型为折线线时的划线结点路径
TStep.prototype.getPolyLinePath = function(fromProcX, fromProcY, fromProcW, fromProcH, toProcX, toProcY, toProcW, toProcH){
  //fromProc Center X,Y
  var fromCenterX = fromProcX + parseInt(fromProcW/2);
  var fromCenterY = fromProcY + parseInt(fromProcH/2);
  //toProc Center X,Y
  var toCenterX = toProcX + parseInt(toProcW/2);
  var toCenterY = toProcY + parseInt(toProcH/2);
  //
  point2X = fromCenterX;
  point2Y = toCenterY;
  if (toCenterX > fromCenterX) {		    //ToProc在FromProc的右边
    absY = toCenterY>=fromCenterY?toCenterY-fromCenterY:fromCenterY-toCenterY;  //计算两个对象中心点的距离
    if (parseInt(fromProcH/2)>=absY) {  //ToProc的顶部在FromProc的底部之上
      point1X = fromProcX + fromProcW;  
      point1Y = toCenterY;
      point2X = point1X;
      point2Y = point1Y;
    }
    else{
      point1X = fromCenterX;
      point1Y = fromCenterY<toCenterY?fromProcY+fromProcH:fromProcY;
    }
    absX = toCenterX-fromCenterX;
    if (parseInt(fromProcW/2)>=absX) {
      point3X = fromCenterX;
      point3Y = fromCenterY<toCenterY?toProcY:toProcY+toProcH;
      point2X = point3X;
      point2Y = point3Y;
    }
    else{
      point3X = toProcX;
      point3Y = toCenterY;
    }		   
  }
  if (toCenterX < fromCenterX) {
    absY = toCenterY>=fromCenterY?toCenterY-fromCenterY:fromCenterY-toCenterY;
    if (parseInt(fromProcH/2)>=absY) {
      point1X = fromProcX;
      point1Y = toCenterY;
      point2X = point1X;
      point2Y = point1Y;
    }else{
      point1X = fromCenterX;
      point1Y = fromCenterY<toCenterY?fromProcY+fromProcH:fromProcY;
    }
    absX = fromCenterX-toCenterX;
    if (parseInt(fromProcW/2)>=absX) {
      point3X = fromCenterX;
      point3Y = fromCenterY<toCenterY?toProcY:toProcY+toProcH;
      point2X = point3X;
      point2Y = point3Y;
    }
    else{
      point3X = toProcX + toProcW;
      point3Y = toCenterY;
    }		   
    }
    if (toCenterX == fromCenterX) {
      point1X = fromCenterX;
      point1Y = fromCenterY>toCenterY?fromProcY:fromProcY+fromProcH;
      point3X = fromCenterX;
      point3Y = fromCenterY>toCenterY?toProcY+toProcH:toProcY;
      point2X = point3X;point2Y = point3Y;
    }
    if (toCenterY == fromCenterY) {
      point1X = fromCenterX>toCenterX?fromProcX:fromProcX+fromProcW;
      point1Y = fromCenterY;
      point3Y = fromCenterY;
      point3X = fromCenterX>toCenterX?toProcX+toProcW:toProcX;
      point2X = point3X;point2Y = point3Y;
    }
  return point1X+','+point1Y+' '+point2X+','+point2Y+' '+point3X+','+point3Y;
}

//判断两个[任务]的位置是否有重叠
function ifRepeatProc(fromX,fromY,fromW,fromH,toX,toY,toW,toH){
  return (fromX + fromW >= toX) && (fromY + fromH >= toY) && (toX + toW >= fromX) && (toY + toH >= fromY);
}

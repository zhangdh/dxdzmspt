var lyywid=0;
var mk_dm = 0;
var form_id;
var id;
var stepId;
var fjid;
var params="";
var lclb_dm="";
var mk_dm="";
var mkurllb="";
var businessid="";
var dbFlag="";
var workid="";
var username;//用户名(yhid)
var xm;
var isExpand = 0;  
$(function() {
	//显示附件标签
	if($("#isFjid").val()!="0"){
		initFj();
		displayFj();
	}
	if($("#stepId").val() == "98"){
		$("#btn_play").show();
	}
	//显示留言标签
	if($("#isLy").val()!="0"){
		 lyywid = $("#id").val();
		 mk_dm = $("#mk_dm").val();
		 initMsg();
	}
	window.formFrame.location.href=webcontext+"/formuse.coffice?method=showData&businessId="+$("#id").val()+"&stepId="+$("#stepId").val()+"&workId="+$("#workid").val();
	$('#p').panel({ 
    	onExpand:function(){  
    	if(isExpand == 0){
        	$('#datalist').datagrid({
				method:'post',
				url:webcontext+'/wfuse.coffice?method=queryHis&id='+$("#id").val(),
				rownumbers:true,
				fitColumns: true,
				columns:[[
					{field:'clr',title:'处理人',width:$(this).width() * 0.1,align:'center'},
					{field:'dz',title:'动作',width:$(this).width() * 0.5,align:'center'},
					{field:'dzsj',title:'动作时间',width:$(this).width() * 0.1,align:'center'}
				]]
		 	});
		 isExpand=1;
		 }
    	}  
	});
});		
//保存表单
function saveForm(){
	var _id=$("#formFrame").contents().find("#_id").val();
	var url;
	if(_id==''){
		url=$("#formFrame").contents().find("#_formid").attr("action")+"&save=1";
	}else{
		url=$("#formFrame").contents().find("#_formid").attr("action");
	}
	var queryString=$("#formFrame").contents().find("#_formid").serialize();
	var undo_title=window.formFrame.document.all("undo_title");//标题
    if(undo_title!="undefined" && undo_title!=null){
     	title=undo_title.value;
     	if(title==""){
     		alert('标题不能为空!');
     		return;
     	}
    }else{
     	title="";
    }
	params="";
	queryString=queryString+"&businessId="+$("#id").val()+"&stepId="+$("#stepId").val();
	_Post(url,queryString,function(json){
		if(json.result=="success"){
			alert("保存成功");
		}else{
			alert("表单保存失败!");
			return;
		}
	});
}
//发送流程
function sendFlow(actionId,obj,stateName){
		var _id=$("#formFrame").contents().find("#_id").val();
		var url;
		if(_id==''){
			url=$("#formFrame").contents().find("#_formid").attr("action")+"&save=1";
		}else{
			url=$("#formFrame").contents().find("#_formid").attr("action");
		}
		var queryString=$("#formFrame").contents().find("#_formid").serialize();
		var undo_title = $("#formFrame").contents().find("input[name='undo_title']").val();
		if(!formFrame.window.valiateForm(actionId)){
			return ;
		} 
		params="";
		queryString=queryString+"&businessId="+$("#id").val()+"&stepId="+$("#stepId").val();
		_Post(url,queryString,function(json){
				if(json.result=="success"){
					form_id=json.formId;
					id=$("#id").val();
					stepId=$("#stepId").val();
					lclb_dm=$("#lclb_dm").val();
					mk_dm=$("#mk_dm").val();
					mkurllb=$("#mkurllb").val();
					businessid=$("#businessid").val();
					fjid=$("#fjid").val();
					workid = $("#workid").val();
					if(fjid!="" && fjid!=undefined){
						params=params+"&fjid="+fjid;
					}
					params=params+"&mk_dm="+mk_dm+"&mkurllb="+mkurllb+"&businessid="+businessid+"&workid="+workid+"&undo_title="+encodeURI(undo_title);
					if(obj.value=="回退"){
							if(confirm("确认要回退！")){
								url="/dfmethod.coffice?method=doFlow&id="+id+"&doId="+actionId+"&formId="+form_id+"&stepId="+stepId+"&lclb_dm="+lclb_dm+params;
							}else{
								return false;
							}
					}else{
							url="/dfmethod.coffice?method=doFlow&id="+id+"&doId="+actionId+"&formid="+form_id+"&stepid="+stepId+"&lclb_dm="+lclb_dm+params;
					}							
					_Post(url,"",function(json){
						if(json.flag=="0"){//直接处理
							if(json.result=="success"){
								alert("提交成功");
								$("#show_btns").hide();
								 opener.refreshUnDo();
							}else if(json.result=="rerror"){
								alert("调用表单执行函数时发生异常："+json.rturn);
								return false;
							}else{
								alert("提交失败，请重新提交");
								return false;
							}
						}else if(json.flag=="1"){//手工处理
							var arguemnts = new Object();
			         	 	arguemnts.window = window;
							if(json.result=="1"){
								if(stepId == 51){
									//市批办具有交予领导批示的权限
									var   rv  = window.showModalDialog(webcontext+"/workflow/use/userListLdps.jsp?stateName=已"+stateName+"&entryid="+$("#id").val()+"&workid="+workid, arguemnts, "dialogWidth:530px;dialogHeight:380px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
								}else{
									var   rv  = window.showModalDialog(webcontext+"/workflow/use/userList.jsp?stateName=已"+stateName+"&entryid="+$("#id").val()+"&workid="+workid, arguemnts, "dialogWidth:265px;dialogHeight:380px;center:yes;help:no;scroll:no;status:no;resizable:yes;");
								}
								if(rv=="sucess"){
									$("#show_btns").hide();
									try{
										opener.refreshUnDo();	
									}catch(exception){
										
									}finally{
										
									}
								
								}
							}else{
								alert("提交失败，请重新提交");
								return false;
							}
						}
					});
				}else{
					alert("表单保存失败!");
					return;
				}
		});		
}
function showFrame(){
		document.getElementById('formFrame').style.height=formFrame.document.body.scrollHeight;
		document.getElementById('formFrame').style.width=formFrame.document.body.scrollWidth;		
}
function displayFj(){
	_Post("/uploadfile.coffice?method=getFiles&mk_mc="+mk_mc+"&zid="+$("#id").val(),"",function(jsonData){
		var hostIp = "http://"+window.location.host;
		var fjStr = jsonData.fjStr;
		if(jsonData.ifFj == "1"){
			var form_dir = jsonData.mk_dir;
			var infolj = form_dir.split(":")[1];
			fjStr = eval("("+fjStr+")");
			$.each(fjStr,function(k,v){
				var wjmc = "";
				var wjlj = "";
				$("#files").append("<a href='"+hostIp+infolj+v.wjlj+"' target='_blank' style='text-decoration:none;'>"+v.wjmc+"</a>&nbsp;&nbsp;");
		    });
		}	
	});
}
function playLy(){
	var id = $("#_id",document.frames('formFrame').document).val();
	_Post("/thxx.doAction?method=queryLyPath","id="+id,function(returnData){
		var lyurl = returnData.lypath;
		if(lyurl == undefined){
			alert("此工单没有关联录音");
			return;
		}
    	window.open("play.jsp?url="+lyurl,"","height=100, width=500,toolbar=no, menubar=no, scrollbars=no, left=500,top=400,resizable=no,location=no, status=no,titlebar=no");
	})
}
function openFlow(){
	var stepId = $("#stepId").val();
	window.open(webcontext+"/workflow/view.html?stepId="+stepId,"流程展示","width=950,height=450,scrollbars=yes,location=no");
}
function openTimeLine(){
	window.open(webcontext+"/workflow/timeline.jsp?entryId="+$("#id").val(),"实例时间轴","width=950,height=300,scrollbars=yes,location=no");
}
function printForm(){
	window.open("./print.jsp");
}
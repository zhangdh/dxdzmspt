  sys_btn_auth = "btn_modi;";

var csz = "";
  
$(function() {
	sys_ajaxGet("/syspara/default.do?method=query","",function(json){
			Dumper.alert(json);
		bind(json);
	
	});
	//保存修改数据
	$("#btn_modi").click(function(){

	if($("#csdm").val()=='sms_isStart'||$("#csdm").val()=='sms_hzzt_isStart'){
		 if($("#old_csz").val()==0&&($("#csz").val()==2||$("#csz").val()==3)){
		   alert("尚未启用不能暂停或恢复！");return false;
		 }if($("#old_csz").val()==$("#csz").val()){
		   alert("操作成功！！！");return false;
		 }if($("#old_csz").val()==2&&$("#csz").val()==1){
		   alert("不能重复启用，请执行3恢复！");return false;
		 }if($("#old_csz").val()==3&&$("#csz").val()==1){
		   alert("已经恢复启用，不能重复启用！");return false;
		 }if($("#old_csz").val()==1&&$("#csz").val()==3){
		   alert("尚未暂停，无需恢复！");return false;
		 }			 
	}
	    var mk=$("#cx_mk").val();
		if(!Validate.CheckForm($('#form_show')[0]))return;
		var queryString=$("#form_show").serialize(); 
		
		csz = $("#csz").val();
		
		sys_ajaxPost("/syspara/default.do?method=modi",queryString,function(msg){
			ajaxAlert(msg);
		
			$("#cx_mk").val(mk);
			var querystr="cx_mk="+$("#cx_mk").val();
			sys_ajaxGet("/syspara/default.do?method=query",querystr);
			sys_showButton("btn_modi");
			
			if ($("#csdm").val() == "skin_globalid") {
				skinSetting(csz);
			}
		});
	});
	
	$("#btn_query").click(function(){
		callback_getPageData_table_list(1);
	});

});

//分页查询回调函数
function callback_getPageData_table_list(pagenum){
	var querystr=$("#form_query").serialize();
	querystr+="&page_goto="+pagenum;
	sys_ajaxPost("/syspara/default.do?method=query",querystr,function(json){
	bind(json);
	});
}
//点击列表显示明细数据回调函数
function callback_trclick_table_list(id){
	$('#form_show')[0].reset();
	sys_ajaxGet("/syspara/default.do?method=show",{id:id},function(json){
	Dumper.alert(json);
		bind(json);
	});
	sys_showButton("btn_modi");
}


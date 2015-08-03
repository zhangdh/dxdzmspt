sys_btn_auth="btn_save;btn_add;"//测试权限用，有权限的按钮id串，正式页面要删除
$(function() {
	var workNo = $("#workNo").val();
	sys_ajaxGet("/THXX/default.do?method=queryGhList&workNo="+workNo,function(json){
		bind(json);
	});
	sys_expandTable("table_list",30);
});
function callback_trclick_table_list(guid){}
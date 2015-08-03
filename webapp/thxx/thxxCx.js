sys_btn_auth="btn_save;btn_add;"//测试权限用，有权限的按钮id串，正式页面要删除
$(function() {
	sys_ajaxGet("/THXX/default.do?method=listTHXX",function(json){
		bind(json);
	});
	sys_expandTable("table_list",5);
});
//执行查询
function query(){
	callback_getPageData_table_list(1);
}
//分页查询回调函数，命名为"callback_getPageData_"加列表table的id
function callback_getPageData_table_list(pagenum){
	var querystr=$("#form_query").serialize();
	querystr+="&page_goto="+pagenum;
	sys_ajaxPostDirect("/THXX/default.do?method=queryTHXX",querystr,function(json){
		bind(json);
	});
}
function callback_trclick_table_list(guid){}
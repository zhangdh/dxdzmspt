$(function() {
    <#if cxfw??>cxfw_list("cxfw");//查询范围</#if>
    <#if tx??>remind_init(0);//提醒</#if>
    <#if attachment??>
	sys_attachment_span_show("${mk_dm}");//附件
	</#if>
	sys_expandTable("table_list",10);
	sys_ajaxGet("${url}?method=query");
	sys_showButton(sys_btn_auth);
});
//执行查询
function query(){
	callback_getPageData_table_list(1);
}
//分页查询回调函数，命名为"callback_getPageData_"加列表table的id
function callback_getPageData_table_list(pagenum){
	var querystr=$("#form_query").serialize();
	querystr+="&page_goto="+pagenum;
	sys_ajaxGet("${url}?method=query",querystr);
}

//从列表中删除记录
function list_del(){//删除后要重新加载数据，所以用ajaxget
	if ($("#table_list :checked").length==0){alert("请先选择要删除的数据");return false;}
	if (window.confirm("确定要删除选中的数据吗？")==false) return false;
	sys_ajaxGet("${url}?method=delete",$("#table_list :checkbox").filter(":gt(0)").serializeArray());
	sys_ajaxGet("${url}?method=delete",$("#table_list :checkbox").filter(":gt(0)").serializeArray(),function(msg){
	ajaxAlert(msg);
	var querystr=$("#form_query").serialize();
	querystr+="&page_goto=1";
	sys_ajaxGet("${url}?method=query",querystr);
	});
}
//点击列表显示明细数据
//命名为"callback_trclick"加列表table的id
function callback_trclick_table_list(guid){
	sys_ajaxGet("${url}?method=show",{guid:guid},function(json){
     bind(json);
	<#if KE??>
		sys_ke_clearEditor('content');				
		sys_ke_insertHtml('content',json.formData.content);
	</#if>
	});
	sys_showButton("btn_add");
}
//新增
function add(){
	$('#form_show')[0].reset();
	sys_showButton("btn_add,btn_save");
}
//保存数据
function save(guid){
	if (!Validate.CheckForm($("#form_show")[0])) {return;}//数据校验
	<#if KE??>
	KE.util.setData('content');
	</#if>
	var queryString = $("#form_show").serialize();
	sys_ajaxPost("${url}?method=save",queryString,function(msg){
		sys_showButton("btn_add");//ajax调用成功后才隐藏保存按钮，否则用户只能点击新增重新录入数据
		ajaxAlert(msg);
		sys_ajaxGet("${url}?method=query");
	});
}


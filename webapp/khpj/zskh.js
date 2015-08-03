var page_num  = 10;
var cur_page = 1;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/khpj.coffice?method=queryZS&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'mc',title:'部门名称',width:$(this).width() * 0.2,align:'center'},
			{field:'sjzs',title:'事件总数',width:$(this).width() * 0.1,align:'center'},
			{field:'smy',title:'双满意事件数',width:$(this).width() * 0.1,align:'center'},
			{field:'dmy',title:'单满意数',width:$(this).width() * 0.1,align:'center'},
			{field:'n1',title:'满意率',width:$(this).width() * 0.1,align:'center'},
			{field:'n2',title:'回复率',width:$(this).width() * 0.1,align:'center'},
			{field:'wbj',title:'未办结事件数',width:$(this).width() * 0.1,align:'center'},
			{field:'score',title:'分值',width:$(this).width() * 0.1,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		}    
	});
});
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
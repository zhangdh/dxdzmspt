var page_num  = 10;
var cur_page = 1;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/khpj.coffice?method=queryXQ&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'bmmc',title:'乡镇名称',width:$(this).width() * 0.2,align:'center'},
			{field:'asbj',title:'按时办结',width:$(this).width() * 0.1,align:'center'},
			{field:'csbj',title:'超时办结',width:$(this).width() * 0.1,align:'center'},
			{field:'smy',title:'双满意',width:$(this).width() * 0.1,align:'center'},
			{field:'dmy',title:'单满意',width:$(this).width() * 0.1,align:'center'},
			{field:'cswbj',title:'超时未办结',width:$(this).width() * 0.1,align:'center'},
			{field:'bmy',title:'不满意',width:$(this).width() * 0.1,align:'center'},
			{field:'n1',title:'双满意率',width:$(this).width() * 0.1,align:'center'},
			{field:'n2',title:'单满意率',width:$(this).width() * 0.1,align:'center'},
			{field:'n3',title:'按时办结率',width:$(this).width() * 0.1,align:'center'},
			{field:'n4',title:'办结率',width:$(this).width() * 0.1,align:'center'},
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
function test(){
	_Post("/app/login.app?method=login&yhm=sys&mm=1","",function(jsonData){
		Dumper.alert(jsonData);
		
	})
}
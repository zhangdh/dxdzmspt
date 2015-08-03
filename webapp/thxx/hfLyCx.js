var page_num  = 10;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/thxx.doAction?method=queryHfLy&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'called',title:'回访号码',width:$(this).width() * 0.25,align:'center'},
			{field:'cjsj',title:'回访时间',width:$(this).width() * 0.25,align:'center'},
			{field:'slbh_day',title:'工单编号',width:$(this).width() * 0.25,align:'center'},
			{field:'filename',title:'播放',width:$(this).width() * 0.1,align:'center',formatter:rowformater}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		}
	});
});
function clickData_datalist(num){
	query(num,page_num);
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function rowformater(value,row,index){
	return "<div onclick = play('"+value+"')><image  src='../css/play.png'/></div>"; 
}
function play(filename){
	alert(filename);
}
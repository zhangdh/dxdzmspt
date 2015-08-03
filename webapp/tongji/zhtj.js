var page_num  = 100;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/tjbb.coffice?method=zhtj&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'tjlx',title:'',width:$(this).width() * 0.1,align:'center'},
			{field:'qs',title:'全市',width:$(this).width() * 0.1,align:'center'},
			{field:'sbj',title:'市本级',width:$(this).width() * 0.1,align:'center'},
			{field:'adq',title:'安定区',width:$(this).width() * 0.1,align:'center'},
			{field:'ltx',title:'临洮县',width:$(this).width() * 0.1,align:'center'},
			{field:'mx',title:'岷县',width:$(this).width() * 0.1,align:'center'},
			{field:'wyx',title:'渭源县',width:$(this).width() * 0.1,align:'center'},
			{field:'zx',title:'漳县',width:$(this).width() * 0.1,align:'center'},
			{field:'lxx',title:'陇西县',width:$(this).width() * 0.1,align:'center'},
			{field:'twx',title:'通渭县',width:$(this).width() * 0.1,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			//alert('加载数据失败');
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function query(query_page){
	if($("#cx_sjq").val() == "" || $("#cx_sjz").val() == ""){
		alert("请正确选择时间条件");return;
	}
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function queryChart(){
	if($("#cx_sjq").val() == "" || $("#cx_sjz").val() == ""){
		alert("请正确选择时间条件");return;
	}
	var querystr = $("#form_search").serialize();
	window.open("zhchart.jsp?"+querystr,"_blank","menubar=no,toolbar=no,scrollbars=yes,location=no, resizable=yes,fullScreen=yes");
}
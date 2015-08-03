var page_num  = 10;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/fwqq.coffice?method=tjFwqq&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'qy',title:'符合条件',width:$(this).width() * 0.25,align:'center'},
			{field:'zs',title:'统计数量',width:$(this).width() * 0.25,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
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
function queryChart(){
	var querystr = $("#form_search").serialize();
	window.open("fwqqChart.jsp?"+querystr,"_blank","width:500px;height:600px");
}
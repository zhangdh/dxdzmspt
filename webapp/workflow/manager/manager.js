var page_num = 10;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/managerflow.coffice?method=listFlow&page_num='+page_num,
		frozenColumns:[[
	        {field:'id',checkbox:true}
		]],
		columns:[[
			{field:'mc',title:'流程名称',width:$(this).width() * 0.2,align:'center'},
			{field:'version',title:'版本',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'创建日期',width:$(this).width() * 0.2,align:'center'},
			{field:'zt',title:'状态',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openFlow(rowData.id,rowData.name);
		}	
	});
})
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/managerflow.coffice?method=delFlow","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function stop(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/managerflow.coffice?method=stopFlow","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function start(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	_Post("/managerflow.coffice?method=startFlow","id="+id,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result == true){
			$('#datalist').datagrid('reload');
		}
	});
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function clickData_datalist(num){
	query(num,page_num);
}
function openFlow(id,name){
	window.open(webcontext+"/workflow/view.html?stepId=0","流程展示","width=950,height=480,scrollbars=yes,location=no");
}
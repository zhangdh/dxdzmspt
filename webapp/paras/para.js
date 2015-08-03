var page_size = 10;
function go_nextpage(pageNumber, pageSize){
	query(pageNumber,pageSize);		
}
function edit(id){
	_Post("/syspara.coffice?method=show&id="+id,"",function(jsonData){			
		loadForm('form_show',jsonData);
			$('#dataedit').window("open");
		});
}
function save(){
	var formData = $("#form_show").serialize();
	_Post("/syspara.coffice?method=modi",formData,function(jsonData){			
		$('#datalist').datagrid('reload');
	});
}
function cancel(){
	$('#dataedit').window('close'); 
}	
		
function query(querypage,curpage){
	$('#datalist').datagrid('load', {
			page_goto:querypage,
			page_size:curpage,
			cx_mc:$("#cx_mc").val(),
			cx_csz:$("#cx_csz").val(),
			cx_sm:$("#cx_sm").val()			
	});
}
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		pageSize:10,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/syspara.coffice?method=query',
		frozenColumns:[[
	        {field:'id',checkbox:true}
		]],
		columns:[[
			{field:'csdm',title:'参数名称',width:$(this).width() * 0.2,align:'center'},
			{field:'cssm',title:'参数说明',width:$(this).width() * 0.2,align:'center'},
			{field:'csz',title:'参数值',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
					//alert('onBeforeLoad'+paras);
					//alert('onBeforeLoad');
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.id);
		}	
	});
});
function clickData_datalist(num){
	query(num,page_size);
}

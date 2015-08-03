var page_size = 10;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/category.do?method=queryDm&dm_lb=',
		rownumbers:true,
		pageSize:page_size,
		remoteSort:true,
		fitColumns: true,
		toolbar:[{
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				add();
			}
			
		},
		{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				 del();
			}
		}
		],
		frozenColumns:[[
	        {field:'dm',checkbox:true}
		]],
		columns:[[
			{field:'mc',title:'类别名称',width:$(this).width() * 0.2,align:'center'},
			{field:'sm',title:'类别说明',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'创建时间',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.dm);
		}	
	});
	_Post("/category.do?method=query","",function(jsonData){
		_loadSelect(jsonData);
	});
});
function edit(id){
	_Post("/category.do?method=show&dm="+id,"",function(jsonData){			
		loadForm('form_show',jsonData);
		$('#dataedit').window('open');
	});
}
function save(){
	var formData = $("#form_show").serialize();
	_Post("/category.do?method=save",formData,function(jsonData){		
		if(jsonData.result){
			$('#datalist').datagrid('reload');
		}else{
			alertMes(jsonData);
		}	
		
	});
}
function cancel(){
	$('#dataedit').window('close'); 
}	
		
function query(querypage,curpage){
	if(querypage == undefined){
		querypage = 1;
	}
	if(curpage == undefined){
		curpage = page_size;
	}
	$('#datalist').datagrid('load', {
			page_goto:querypage,
			page_size:curpage,
			dm_lb:$("#dm_lb").val()			
	});
}
function add(){
	$('#form_show')[0].reset();
	$("#lb_dm").val($("#dm_lb").val());
	$('#dataedit').window('open');
}
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var dm = selectrow.dm;
	_Post("/category.do?method=del&dm="+dm,"",function(jsonData){
		if(jsonData.result == false){
			alertMes(jsonData);
			return;
		}			
		$('#datalist').datagrid('reload');
	});
}
function clickData_datalist(num){
	query(num,page_size);
}

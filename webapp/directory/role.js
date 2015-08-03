var page_size = 10;
function edit(id){
	_Post("/directoryrole.coffice?method=showRoleInfo&jsid="+id,"",function(jsonData){	
		if(jsonData.result==false){
			alertMes(jsonData);
		}else{
			loadForm('form_show',jsonData);
			$('#dataedit').window('open');
		}			
	});
}
function save(){
	var formData = $("#form_show").serialize();
	_Post("/directoryrole.coffice?method=saveRole",formData,function(jsonData){			
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
			js_mc:$("#cx_mc").val()	
	});
}
function add(){
	$('#form_show')[0].reset();
	$('#dataedit').window('open');
}
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var jsid = selectrow.jsid;
	_Post("/directoryrole.coffice?method=deleteRole&jsid="+jsid,"",function(jsonData){
		if(jsonData.result == false){
			alertMes(jsonData);
			return;
		}			
		$('#datalist').datagrid('reload');
	});
}
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		pageSize:10,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/directoryrole.coffice?method=queryRole',
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
	        {field:'jsid',checkbox:true}
		]],
		columns:[[
			{field:'mc',title:'用户组名称',width:$(this).width() * 0.2,align:'center'},
			{field:'bz',title:'用户组说明',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.jsid);
		}	
	});
});
function clickData_datalist(num){
	query(num,page_size);
}

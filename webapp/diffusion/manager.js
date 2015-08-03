var page_num = 10;
$(function(){
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		url:webcontext+'/diffusion.coffice?method=manageConfig&page_num='+page_num,
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
	        {field:'id',checkbox:true},
	        {field:'lb_dm',checkbox:true,hidden:true}
		]],
		columns:[[
			{field:'glcdmc',title:'管理菜单',width:$(this).width() * 0.25,align:'center'},
			{field:'ckcdmc',title:'查看菜单',width:$(this).width() * 0.25,align:'center'},
			{field:'plqk',title:'是否允许评论',width:$(this).width() * 0.25,align:'center'},
			{field:'fbfw',title:'发布范围',width:$(this).width() * 0.25,align:'center'}
		]],
		onBeforeLoad:function(paras){
			
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.id);
		}	
	});
});
function edit(id){
	_Post("/diffusion.coffice?method=showManager","id="+id,function(jsonData){
		loadForm('form_show',jsonData);
		$("#dataedit").window("open");
	});
}
function add(){
	$('#form_show')[0].reset();
	$("#dataedit").window("open");
}
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var id = selectrow.id;
	var lb_dm = selectrow.lb_dm;
	_Post("/diffusion.coffice?method=delManager","id="+id+"&lb_dm="+lb_dm,function(jsonData){
		alertMes(jsonData);
		$('#datalist').datagrid('load');
	});
	
}
function save(){
	var formData = $("#form_show").serialize();
	alert(formData);
	_Post("/diffusion.coffice?method=saveManager",formData,function(jsonData){
		alertMes(jsonData);
		$('#datalist').datagrid('load');
		$("#dataedit").window("close");
	});
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var queryStr = "{'query_page':"+query_page+"}";
	
	var queryStr1 = eval("("+queryStr+")");
	$('#datalist').datagrid('load',queryStr1);
}
function clickData_datalist(num){
	query(num,page_num);
}
var page_num  = 10;
var curkid = "0";
var editor;
var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: zTreeOnClick
		}
		
};
$(function(){
	_Post("/knowledge.coffice?method=getSortTree","",function(jsonData){
		$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));
		 	
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/knowledge.coffice?method=queryKb&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		toolbar:[{
				text:'新增目录',
				iconCls:'icon-add',
				handler:function(){addKb();}
			},{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){delKb();}
			}
		],
		frozenColumns:[[
	        {field:'kid',checkbox:true}
		]],
		columns:[[
			{field:'kname',title:'目录标题',width:$(this).width() * 0.7,align:'center'},
			{field:'cjsj',title:'创建时间',width:$(this).width() * 0.3,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openKb(rowData.kid);
		}	
	});
});
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	$('#datalist').datagrid('reload',{
		'query_page':query_page,
		'ksjid':curkid
	});
}
function zTreeOnClick(event, treeId, treeNode){
	var kid = treeNode.id;
	var kico = treeNode.icon;
	if(kico == "close.png"){
		treeNode.icon = "open.png";
	}else{
		treeNode.icon = "close.png";
	}
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	treeObj.updateNode(treeNode);
	curkid = kid;
	$('#datalist').datagrid('reload',{
		ksjid:kid
	});
}
function addKb(){
	_Post("/knowledge.coffice?method=getTree","",function(jsonData){
	if(jsonData.result == true){
		_loadSelect(jsonData);
			$("#addkb").window('open');
		}else{
			alert("加载目录失败");	
		}
	});
}
function saveKb(){
	_Post("/knowledge.coffice?method=saveSortTree","curkid="+curkid+"&kname="+$("#kname").val(),function(jsonData){
		if(jsonData.result == true){
			$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));
			$("#kname").val("");
			var treeObj = $.fn.zTree.getZTreeObj("kbList");		
			var nodes = treeObj.getNodesByParam("id", curkid, null);
			treeObj.expandNode(nodes[0], true, true, true);
			$('#datalist').datagrid('reload',{
				ksjid:curkid
			});
		}	
	});
}
function updateKb(){
	_Post("/knowledge.coffice?method=updateSortTree","kid="+$("#kid").val()+"&kname="+$("#kname").val(),function(jsonData){
		if(jsonData.result == true){
			$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));
			var treeObj = $.fn.zTree.getZTreeObj("kbList");		
			var nodes = treeObj.getNodesByParam("id", curkid, null);
			treeObj.expandNode(nodes[0], true, true, true);
			$('#datalist').datagrid('reload',{
				ksjid:curkid
			});
		}	
	});
}
function clickData_datalist(num){
	query(num);
}
function delKb(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var kid = selectrow.kid;
	_Post("/knowledge.coffice?method=deleteTree&kid="+kid,"",function(jsonData){
		if(jsonData.result == false){
			alertMes(jsonData);
			return;
		}else{
			$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));
			var treeObj = $.fn.zTree.getZTreeObj("kbList");		
			var nodes = treeObj.getNodesByParam("id", curkid, null);
			treeObj.expandNode(nodes[0], true, true, true);
			$('#datalist').datagrid('reload',{
				ksjid:curkid
			});
		}			
		$('#datalist').datagrid('reload');
	});
}
function openKb(kid){
	_Post("/knowledge.coffice?method=openKb&kid="+kid,"",function(jsonData){	
		if(jsonData.result==false){
			alertMes(jsonData);
		}else{
			loadForm('form_show',jsonData);
			$('#addkb').window('open');
		}			
	});
}

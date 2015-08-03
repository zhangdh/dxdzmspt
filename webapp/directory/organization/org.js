var page_size = 10;
var tree_setting = {
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType:{"Y":"","N":""}
				
		},
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
	loadTree();
	$('#datalist').datagrid({
		method:'post',
		rownumbers:true,
		pageSize:page_size,
		remoteSort:true,
		fitColumns: true,
		singleSelect:true,
		url:webcontext+'/organization.coffice?method=getOrgList&lxid='+$("#lxid").val()+"&lx_dm="+$("#lx_dm").val()+'&orgid='+$("#orgid").val(),
		toolbar:[{
			text:'新增部门',
			iconCls:'icon-add',
			handler:function(){
				 addDep();
			}
		},
		{
			text:'新增岗位',
			iconCls:'icon-add',
			handler:function(){
				 addGw();
			}
		},
		{
			text:'新增用户',
			iconCls:'icon-add',
			handler:function(){
				 addYh();
			}
		},
		{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				 delOrg();
			}
		},
		{
			text:'导入组织目录',
			iconCls:'icon-help',
			handler:function(){
				 exportOrg();
			}
		}
		],
		frozenColumns:[[
	        {field:'guid',checkbox:true}
		]],
		columns:[[
			{field:'mc',title:'名称',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'创建时间',width:$(this).width() * 0.2,align:'center'},
			{field:'lx_mc',title:'类型',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
					//alert('onBeforeLoad'+paras);
					//alert('onBeforeLoad');
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.guid,rowData.lx_dm,rowData.lxid);
		}	
	});
});
function clickData_datalist(nums){
	query(nums, page_size,$("#lxid").val(),$("#lx_dm").val(),$("#orgid").val());
}
function query(querypage,curpage,lxid,lx_dm,orgid){
	if(querypage == undefined){
		querypage = 1;
	}
	if(curpage == undefined){
		curpage = page_size;
	}
	$('#datalist').datagrid('load', {
			page_goto:querypage,
			page_size:curpage,
			lxid:lxid,
			lx_dm:lx_dm,
			orgid:orgid
	});
}
function zTreeOnClick(){
	var treeObj = $.fn.zTree.getZTreeObj("orgtree");
	var nodes = treeObj.getSelectedNodes();
	$.each(nodes,function(k,v){
		$("#lxid").val(v.lxid);
		$("#lx_dm").val(v.lx_dm);
		$("#orgid").val(v.id);
		$("#mc").val(v.name);
	})
	if(nodes[0].lx_dm !="204"){
		$("#yh_div").window('close');
	}
	if(nodes[0].lx_dm=="204"){
		var lxid = nodes[0].lxid;
		_Post("/organization.coffice?method=showYh&lxid="+lxid,"",function(jsonData){
			if(jsonData.result){
				loadForm('yh_form',jsonData);
				$("#yh_div").window('open');
				$("#pwdtr").hide();
			}else{
				alertMes(jsonData);
			}
		_Post("/organization.coffice?method=selectRole","",function(jsonData){
			_loadSelect(jsonData);
			$("#roles").val($("#kz300").val());
		});	 
	});
	}
	query(1,page_size,nodes[0].lxid,nodes[0].lx_dm,nodes[0].id);
}
function delOrg(){
	var selectrow = $("#datalist").datagrid("getSelections");
	if(selectrow.length == 1){
		var lxid = selectrow[0].lxid;
		var lx_dm =  selectrow[0].lx_dm;
		var orgid =  selectrow[0].guid;
		_Post("/organization.coffice?method=delOrg","lxid="+lxid+"&lx_dm="+lx_dm+"&orgid="+orgid,function(jsonData){
			if(jsonData.result == false){
				alertMes(jsonData);
				return;
			}else{
				loadTree();
			}			
			
	});
	}else if (selectrow.length == 0){
		alert('未选中删除项');
	}else{
		alert('不可多选');
	}
	var dm = selectrow.dm;
}
function loadTree(){
	_Post("/organization.coffice?method=getOrgTree","",function(jsonData){ 
		$.fn.zTree.init($("#orgtree"), tree_setting,eval("("+jsonData.org_tree+")"));	
		var nodeId = $("#orgid").val();
		var treeObj = $.fn.zTree.getZTreeObj("orgtree");
		var curnode = treeObj.getNodeByParam("id",nodeId);
		treeObj.expandNode(curnode,true);
		$('#datalist').datagrid('reload');
	});
}
function edit(orgid,lx_dm,lxid){
	if(lx_dm == "201"){
		_Post("/organization.coffice?method=showBm&lxid="+lxid,"",function(jsonData){
			if(jsonData.result){
				loadForm('bm_form',jsonData);
				$("#bm_div").window('open');
			}else{
				alertMes(jsonData);
			}
			
		});
		
	}else if(lx_dm == "202"){
		_Post("/organization.coffice?method=showGw&lxid="+lxid,"",function(jsonData){
			if(jsonData.result){
				loadForm('gw_form',jsonData);
				$("#gw_div").window('open');
			}else{
				alertMes(jsonData);
			}
			
		});
	}else if(lx_dm == "204"){
		_Post("/organization.coffice?method=showYh&lxid="+lxid,"",function(jsonData){
			if(jsonData.result){
				loadForm('yh_form',jsonData);
				$("#yh_div").window('open');
				$("#pwdtr").hide();
			}else{
				alertMes(jsonData);
			}
		_Post("/organization.coffice?method=selectRole","",function(jsonData){
			_loadSelect(jsonData);
			$("#roles").val($("#kz300").val());
		});	
		});
	}
}
function addDep(){
	if($("#lx_dm").val() != "201"){
		alert("请在右侧选择部门");
		return;
	}
	$('#bm_form')[0].reset();
	$("#bm_div").window('open');	 
}
function addGw(){
	if($("#lx_dm").val() != "201" && $("#lx_dm").val() != ""){
		alert("请在右侧选择部门");
		return;
	}
	$('#gw_form')[0].reset();
	$("#gw_div").window('open');
}
function addYh(){
	if($("#lx_dm").val() != "202"){
		alert("请在右侧选择岗位");
		return;
	}
	$('#yh_form')[0].reset();
	$("#mc_gw").val($("#mc").val());
	//默认密码123456
	$("#dlmm").val('123456');
	$("#dlmm2").val('123456');
	$("#pwdtr").show();
	$("#yh_div").window('open');
	_Post("/organization.coffice?method=selectRole","",function(jsonData){
		_loadSelect(jsonData);
	});
}
function save(lx){
	if(lx == "bm"){
		var saveDepStr = $("#bm_form").serialize();
		saveDepStr = saveDepStr+"&curbmid="+$("#bmid").val();	
		saveDepStr = saveDepStr+"&orgid="+$("#orgid").val()+"&lxid="+$("#lxid").val()+"&lx_dm="+$("#lx_dm").val();
		_Post("/organization.coffice?method=saveBm",saveDepStr,function(jsonData){
			if(jsonData.result){
				$('#bm_div').window('close'); 
				loadTree();
			}else{
				alertMes(jsonData);
			}
		});
	} 
	if(lx == "gw"){		
		var saveDepStr = $("#gw_form").serialize();
		saveDepStr = saveDepStr+"&curgwid="+$("#gwid").val();
		saveDepStr = saveDepStr+"&orgid="+$("#orgid").val()+"&lxid="+$("#lxid").val()+"&lx_dm="+$("#lx_dm").val();
		_Post("/organization.coffice?method=saveGw",saveDepStr,function(jsonData){
			if(jsonData.result){
				$('#gw_div').window('close'); 
				loadTree();
			}else{
				alertMes(jsonData);
			}
		});
	}
	if(lx == "yh"){
		if(!($("#dlmm").val() == $("#dlmm2").val()))
		if(!Validate.CheckForm($('#yh_form')[0]))return;
		var saveYhStr = $("#yh_form").serialize();
		saveYhStr = saveYhStr+"&curyhid="+$("#yhid").val();
		saveYhStr = saveYhStr+"&orgid="+$("#orgid").val()+"&lxid="+$("#lxid").val()+"&lx_dm="+$("#lx_dm").val();
		_Post("/organization.coffice?method=saveYh",saveYhStr,function(jsonData){
			if(jsonData.result){
				$('#yh_div').window('close'); 
				loadTree();
			}else{
				alertMes(jsonData);
			}
		});
	}
}
function cancel(lx){
	if(lx == "bm"){
		$('#bm_div').window('close'); 
	} 
	if(lx == "gw"){
		$('#gw_div').window('close'); 
	}
	if(lx == "yh"){
		$('#yh_div').window('close'); 
	}
}
function selectRole(){
	$('#roleList').window('open');
}

function selected(){
	$('#roleList').window('close'); 
	$("#kz300").val($("#roles").val());
	$("#mc_js").val($("#roles   option:selected").text());
}
function redo(){
	var curyhid = $("#yhid").val();
	_Post("/organization.coffice?method=rePass&curyhid="+curyhid,"",function(jsonData){
				alertMes(jsonData);
	});
}
function exportOrg(){
 	_Post("/organization.coffice?method=exportOrg","",function(){
 		
 	});
}
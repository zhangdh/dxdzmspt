var editor;
var page_num  = 10;
var tree_setting = {
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType:{"Y":"ps","N":"s"}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
$(function(){
	editor = KindEditor.create('textarea[name="xxnr"]', {
					resizeType : 1,
					allowFileManager:true,
					uploadJson:webcontext+'/diffusion.coffice?method=uploadImage',
					fileManagerJson:webcontext+'/diffusion.coffice?method=uploadManagerImage',
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'hr','fullscreen','image','source'] 
					 
				});
	_Post("/diffusion.coffice?method=queryLb","",function(jsonData){
		_loadSelect(jsonData);
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/diffusion.coffice?method=queryFs&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		toolbar:[{
			text:'新建',
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
		},
		{
			text:'发送',
			iconCls:'icon-ok',
			handler:function(){
				 sendDiff();
			}
		}
		],
		frozenColumns:[[
	        {field:'guid',checkbox:true}
		]],
		columns:[[
			{field:'zt',title:'信息主题',width:$(this).width() * 0.6,align:'center'},
			{field:'xxlb',title:'信息类别',width:$(this).width() * 0.2,align:'center'},
			{field:'cjsj',title:'发送时间',width:$(this).width() * 0.2,align:'center'},
			{field:'yxqq',title:'有效时间起',width:$(this).width() * 0.2,align:'center'},
			{field:'yxqz',title:'有效时间止',width:$(this).width() * 0.2,align:'center'},
			{field:'xxzt',title:'信息状态',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){
					//alert('onBeforeLoad'+paras);
					//alert('onBeforeLoad');
		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			edit(rowData.guid);
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 	
	});
	loadTree();
});
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function loadTree(){
	_Post("/organization.coffice?method=getOrgTree","",function(jsonData){ 
		$.fn.zTree.init($("#orgtree"), tree_setting,eval("("+jsonData.org_tree+")"));	
	});
}
function clickData_datalist(num){
	query(num,page_num);
}
function add(){
	$('#form_show')[0].reset();
	$("#operdiv").show();
	$("#operdiv1").show();
	$("#operdiv2").hide();
	$("#fbfw").empty();
	editor.html("");
	$("#sys_attachment_wjsc").show();
	$("#files").html("");
	$("#attachments").empty();
	$("#dataedit").window("open");
}
function selectMan(){
	if($("#fsfwbz").attr("checked") == "checked"){
		alert("选择全部后无法再选择人员");
		return;
	}
	$("#selectYh").window('open');
}
function loadTree(){
	_Post("/organization.coffice?method=getOrgTree","",function(jsonData){ 
		$.fn.zTree.init($("#orgtree"), tree_setting,eval("("+jsonData.org_tree+")"));	
	});
}
function queding(){
	var selectedStr = "";
	var treeObj = $.fn.zTree.getZTreeObj("orgtree");
	var nodes = treeObj.getCheckedNodes(true);
	$("#fbfw").empty();
	$.each(nodes,function(k,v){
		var theoption = ""
		if(v.lx_dm == "204"){
			theoption="<option value='yh"+v.id+"'>"+v.name+"</option>";
			selectedStr=selectedStr+v.id+",";
		}
		$("#fbfw").append(theoption); 
	});
	$("#fbfw_value").val(selectedStr);
	$("#selectYh").window('close');	
}
function send(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	var re=eval("/{&nbsp;}/ig");
	saveStr = saveStr.replace(re," "); 

	_Post("/diffusion.coffice?method=send",saveStr,function(jsonData){
		alertMes(jsonData);
		$("#dataedit").window("close");
		query();
	});
}
function save(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	var re=eval("/{&nbsp;}/ig");
	saveStr = saveStr.replace(re," "); 
	alert(saveStr);
	_Post("/diffusion.coffice?method=save",saveStr,function(jsonData){
		alertMes(jsonData);
		$("#dataedit").window("close");
		query();
	});
}
function edit(guid){
	$('#form_show')[0].reset();
	$("#fbfw").empty();	
	$("#fbfw_value").empty();	
	editor.html("");	
	$("#files").html("");
	$("#attachments").empty();
	_Post("/diffusion.coffice?method=show","guid="+guid,function(jsonData){
		loadForm("form_show",jsonData);
		_loadSelect(jsonData);
		editor.html(jsonData.xxnr);
		var hostIp = "http://"+window.location.host;
		var fjStr = jsonData.fjStr;
		if(jsonData.ifFj == "1"){
			var diffusion_dir = jsonData.diffusion_dir;			
			var infolj = diffusion_dir.split(":")[1];
			fjStr = eval("("+fjStr+")");
			$.each(fjStr,function(k,v){
				var wjmc = "";
				var wjlj = "";
				$("#files").append("<a href='"+hostIp+infolj+v.wjlj+"' target='_blank' style='text-decoration:none;'>"+v.wjmc+"</a>&nbsp;&nbsp;");
		    });
		}	
		if(jsonData.xxzt == "8001"){
			$("#operdiv").show();
			$("#operdiv1").hide();
			$("#operdiv2").show();
			$("#sys_attachment_wjsc").show();
			$("#fbfw option").each(function() {  
   				$("#fbfw_value").val($("#fbfw_value").val()+$(this).val()+",");
    		}); 
		}else{
			$("#operdiv").hide();
			$("#sys_attachment_wjsc").hide();
		}
		$("#dataedit").window("open");
	});
}
function sendDiff(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var guid = selectrow.guid;
	_Post("/diffusion.coffice?method=sendDiff","guid="+guid,function(jsonData){
		alertMes(jsonData);
		query();
	});
}
function del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var guid = selectrow.guid;
	_Post("/diffusion.coffice?method=del","guid="+guid,function(jsonData){
		alertMes(jsonData);
		query();
	});
}
function send1(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	_Post("/diffusion.coffice?method=send1",saveStr,function(jsonData){
		alertMes(jsonData);
		$("#dataedit").window("close");
		query();
	});
}
function save1(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	var re=eval("/{&nbsp;}/ig");
	saveStr = saveStr.replace(re," "); 
	alert(saveStr);
	_Post("/diffusion.coffice?method=save1",saveStr,function(jsonData){
		alertMes(jsonData);
		$("#dataedit").window("close");
		query();
	});
}
function s(b){
	if(b){
		$("#selectId").hide();
		$("#fbfw_value").val();
		$("#fbfw").empty();
	}else{
		$("#selectId").show();
	}
}
function st(b){
	if(b){
		$("#yxqz").val('2099-12-31 23:59:59');
	}else{
		$("#yxqz").val('');
	}
}
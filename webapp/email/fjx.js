var page_num = 10;
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
var editor
$(function(){
	initFj();
	editor = KindEditor.create('textarea[name="yjnr"]', {
					resizeType : 1,
					allowFileManager:true,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|','hr','fullscreen']
				});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/email.coffice?method=fjxquery&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		toolbar:[{
			text:'写信',
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
				 sendyj();
			}
		}
		],
		frozenColumns:[[
	        {field:'guid',checkbox:true}
		]],
		columns:[[
			{field:'yjzt',title:'邮件主题',width:$(this).width() * 0.6,align:'center'},
			{field:'cjsj',title:'发送时间',width:$(this).width() * 0.2,align:'center'},
			{field:'zt',title:'当前状态',width:$(this).width() * 0.2,align:'center'}
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
		}	
	});
	$("#btn_sjr").click(function(){
		getYh();
		$("#selectYh").window('open');	
	});
	loadTree();
});
function getYh(){
	var orgObj = $.fn.zTree.getZTreeObj("orgtree");
	$("#sjr option").each(function() {  
   		var curNode = orgObj.getNodeByParam("id",$(this).val());
   		orgObj.checkNode(curNode,true,true );
    });    
}
function query(querypage,curpage){
	$('#datalist').datagrid('load', {
			query_page:querypage,
			cx_rq1:$("#cx_rq1").val(),
			cx_rq2:$("#cx_rq2").val()
	});
}
function  del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var yjid = selectrow.guid;
	_Post("/email.coffice?method=delFj","yjid="+yjid,function(jsonData){
		alertMes(jsonData);
		$('#datalist').datagrid("reload");
	});
}
function sendyj(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var yjid = selectrow.guid;
	_Post("/email.coffice?method=sendYj","yjid="+yjid,function(jsonData){
		alertMes(jsonData);
		$('#datalist').datagrid("reload");
	});
}
function add(){
	$('#form_show')[0].reset();
	$("#dataedit").window('open');
	$("#operdiv").show();
	$("#operbtns").show();
	$("#cgbtns").hide();
	$("#sjr").empty();
	editor.html("");
	$("#sys_attachment_wjsc").show();
	$("#btn_sjr").show();
	$("#files").html("");
	$("#attachments").empty();
}
function cancel(){
	$("#dataedit").window('close');	
}
function save(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	_Post("/email.coffice?method=save",saveStr,function(jsonData){
		$('#datalist').datagrid('reload');
		alertMes(jsonData);
		$("#dataedit").window('close');	
	});
	
}

function send(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	_Post("/email.coffice?method=send",saveStr,function(jsonData){
		$('#datalist').datagrid('reload');
		alertMes(jsonData);
		$("#dataedit").window('close');	
	});
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
	$("#sjr").empty();
	$.each(nodes,function(k,v){
		var theoption = ""
		if(v.lx_dm == "204"){
			theoption="<option value='yh"+v.id+"'>"+v.name+"</option>";
			selectedStr=selectedStr+v.id+",";
		}
		$("#sjr").append(theoption); 
	});
	$("#sjr_value").val(selectedStr);
	$("#selectYh").window('close');	
}

function edit(emailid){
	_Post("/email.coffice?method=show","emailid="+emailid,function(jsonData){
		if(jsonData.result == false){
			alertMsg(jsonData);
			return;
		}
		$("#files").html("");
		_loadSelect(jsonData);
		loadForm("form_show",jsonData);
		editor.html(jsonData.formData.yjnr);
		if (jsonData.iffj == 1){
			displayFj(jsonData);
		}
		if(jsonData.yjzt=="1201"){
			$("#sys_attachment_wjsc").hide();
			$("#operdiv").hide();
			$("#btn_sjr").hide();
		}else{
			$("#sys_attachment_wjsc").show();
			$("#operdiv").show();
			$("#operbtns").hide();
			$("#cgbtns").show();
			$("#btn_sjr").show();
			$("#sjr option").each(function() {  
   				$("#sjr_value").val($("#sjr_value").val()+$(this).val()+",");
    		});  
		}
		$("#dataedit").window('open');		
		
	});
}
function displayFj(jsonData){
	var fjs = jsonData.selectData.fjs;
	var mk_dir = jsonData.email_dir;
	var infolj = mk_dir.split(":")[1];
	var ablj = "";
	$.each(fjs,function(v,m){
		ablj = "http://"+window.location.host+infolj+m.wjlj;
     	$("#files").append("<a href='"+ablj+"' target='_blank' style='text-decoration:none;'>"+m.wjmc+"</a>&nbsp;&nbsp;");
	});
	$("#operdiv").hide();
}
function getFjs(ywid){
	$("#files").html("");
	_Post("/uploadfile.coffice?method=getFiles&mk_mc="+mk_mc+"&zid="+ywid,"",function(jsonData){
		var hostIp = "http://"+window.location.host;
		var fjStr = jsonData.fjStr;
		if(jsonData.ifFj == "1"){
			var form_dir = jsonData.mk_dir;
			var infolj = form_dir.split(":")[1];
			fjStr = eval("("+fjStr+")");
			$.each(fjStr,function(k,v){
				var wjmc = "";
				var wjlj = "";
				$("#files").append("<a href='"+hostIp+infolj+v.wjlj+"' target='_blank' style='text-decoration:none;'>"+v.wjmc+"</a>&nbsp;&nbsp;");
		    });
		}	
	});
}
function clickData_datalist(num){
	query(num,page_num);
}
function saveCg(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	_Post("/email.coffice?method=saveCg",saveStr,function(jsonData){
		$('#datalist').datagrid('reload');
		alertMes(jsonData);
		$("#dataedit").window('close');	
	});
}
function sendCg(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&nr="+editor.html();
	_Post("/email.coffice?method=sendCg",saveStr,function(jsonData){
		$('#datalist').datagrid('reload');
		alertMes(jsonData);
		$("#dataedit").window('close');	
	});
}
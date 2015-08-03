var page_size = 10;
var editor;
$(function(){
	editor = KindEditor.create('textarea[name="yjnr"]', {
					resizeType : 1,
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link']
				});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/email.coffice?method=sjxquery',
		rownumbers:true,
		pageSize:page_size,
		remoteSort:true,
		fitColumns: true,
		toolbar:[
		{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				 del();
			}
		}
		],
		frozenColumns:[[
	        {field:'guid',checkbox:true}
		]],
		columns:[[
			{field:'yjzt',title:'邮件主题',width:$(this).width() * 0.6,align:'center'},
			{field:'cjsj',title:'接收时间',width:$(this).width() * 0.2,align:'center'},
			{field:'fsr',title:'发送人',width:$(this).width() * 0.2,align:'center'},
			{field:'zt',title:'状态',width:$(this).width() * 0.1,align:'center'}
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
});
function query(querypage,curpage){
	$('#datalist').datagrid('load', {
			page_goto:querypage,
			page_size:curpage,
			cx_rq1:$("#cx_rq1").val(),
			cx_rq2:$("#cx_rq2").val(),
			ydzt:$("#ydzt").val()
	});
}
function  del(){
	var selectrow = $("#datalist").datagrid("getSelected");
	var yjid = selectrow.guid;
	_Post("/email.coffice?method=delSj","yjid="+yjid,function(jsonData){
		alertMes(jsonData);
		$('#datalist').datagrid('reload');
	});
}
function edit(emailid){
	_Post("/email.coffice?method=show","emailid="+emailid+"&showFlag=1",function(jsonData){
		if(jsonData.result == false){
			alertMes(jsonData);
			return;
		}
		$("#files").html("");
		loadForm("form_show",jsonData);
		_loadSelect(jsonData);
		editor.html(jsonData.formData.yjnr);
		if (jsonData.iffj == 1){
			displayFj(jsonData);
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
function clickData_datalist(num){
	query(num,page_size);
}
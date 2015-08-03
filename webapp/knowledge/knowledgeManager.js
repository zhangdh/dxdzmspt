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
	 KE.show({
		        id : 'knr',
		        resizeMode : 1,
		        skinType: 'tinymce',
		        items : [
		        'fullscreen','undo', 'redo',
		        'plainpaste', 'wordpaste', 'justifyleft', 'justifycenter', 'justifyright',
		        'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
		        'title', 'fontname', 'fontsize', 'textcolor', 'bgcolor', 'bold',
		        'italic', 'underline']	         
		    });
 
	_Post("/knowledge.coffice?method=getSortTree","",function(jsonData){
		$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));
		//alert(jsonData.kbList);	
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/knowledge.coffice?method=queryItem&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		toolbar:[
			{
				text:'新增条目',
				iconCls:'icon-add',
				handler:function(){addItem();}
			},{
				text:'删除',
				iconCls:'icon-remove',
				handler:function(){delItem();}
			}
		],
		frozenColumns:[[
	        {field:'kid',checkbox:true}
		]],
		columns:[[
			{field:'kbt',title:'知识标题',width:$(this).width() * 0.5,align:'center'},
			{field:'sskid',title:'所属知识目录',width:$(this).width() * 0.1,align:'center'},
			{field:'xm',title:'创建人员',width:$(this).width() * 0.1,align:'center'},
			{field:'cjsj',title:'创建时间',width:$(this).width() * 0.2,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openMx(rowData.kid);
		}	
	});
});
function openMx(kid){
	openwindow(webcontext+"/knowledge/knowledgeMx.jsp?kid="+kid,'',852,600);
}
function openwindow(url,name,iWidth,iHeight)
{
   var url;                                 
   var name;                            
   var iWidth;                          
   var iHeight;                        
   var iTop = (window.screen.availHeight-30-iHeight)/2;        
   var iLeft = (window.screen.availWidth-10-iWidth)/2;         
   window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes');
}
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	$('#datalist').datagrid('reload',{
		'query_page':query_page,
		'sskid':curkid
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
		sskid:kid
	});
}
function addItem(){	
	$('#form_item')[0].reset();  
	KE.util.setFullHtml("knr","");
	_Post("/knowledge.coffice?method=getAllTree","",function(jsonData){
	if(jsonData.result == true){
		_loadSelect(jsonData);
			$("#addItem").window('open');
		}else{
			alert("加载目录失败");	
		}
	});
}
function saveItem(){	
	if(!Validate.CheckForm($('#form_item')[0]))return;
	var saveStr  = $("#form_item").serialize();
	alert(saveStr);
	_Post("/knowledge.coffice?method=saveItem",saveStr,function(jsonData){
		alertMes(jsonData);	
		$("#addItem").window("close");
		query(1);
	});
}
function clickData_datalist(num){
	query(num);
}
function delItem(){
	var selectrow = $("#datalist").datagrid("getSelected");
	if(selectrow == null){
		alert("请选择需要删除的知识项");
		return;
	}
	var kid = selectrow.kid;
	_Post("/knowledge.coffice?method=deleteItem&kid="+kid,"",function(jsonData){
		if(jsonData.result == false){
			alertMes(jsonData);
			return;
		}			
		$('#datalist').datagrid('reload');
	});
}
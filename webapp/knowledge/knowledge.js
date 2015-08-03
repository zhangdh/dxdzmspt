var page_num  = 10;
var snode = "";
var setting = {
		check: {
				enable: true,
				chkStyle: "checkbox"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: treeOnclick
		}
};
$(function(){
	_Post("/knowledge.coffice?method=getSortTree","",function(jsonData){
		$.fn.zTree.init($("#kbList"),setting,eval("("+jsonData.kbList+")"));	
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/knowledge.coffice?method=queryKeyItem&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
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
function openwindow(url,name,iWidth,iHeight){
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
		'sskid':snode
	});
}
function querySolr(num){
	var treeObj = $.fn.zTree.getZTreeObj("kbList");
	var nodes = treeObj.getCheckedNodes(true);
	var snode = "";
	$.each(nodes,function(k,v){
		snode = snode+"'"+v.id+"',";
	});
	snode = snode+"''";
	_Post("/knowledge.coffice?method=querySolrItem&sskid="+snode,"keyword="+$("#cx_keyword").val()+"&page_num="+page_num+"&query_page="+num,function(jsonData){
		solrbind(jsonData);
		bindPageSolr(jsonData);
		$("#solrlistdiv").show();
		$("#datalistdiv").hide();
	});
}
function clickData_datalist(num){
	query(num);
}
function clickData_solrlist(num){
	querySolr(num);
}
function treeOnclick(event, treeId, treeNode) {
	snode = treeNode.id;
    $('#datalist').datagrid('reload',{
		'sskid':treeNode.id
	});
	$("#solrlistdiv").hide();
	$("#datalistdiv").show();
};
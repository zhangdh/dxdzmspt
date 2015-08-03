var page_num  = 10;
var curpage = 1;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/tjbb.coffice?method=queryDel&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'guid',checkbox:true},
	        {field:'flowid',hidden:true},
	        {field:'entryid',hidden:true}
		]],
		columns:[[
			{field:'slbh_day',title:'受理编号',width:$(this).width() * 0.1,align:'center',sortable:true},
			{field:'undo_title',title:'标题',width:$(this).width() * 0.2,align:'center'},
			{field:'ldrq',title:'受理日期',width:$(this).width() * 0.1,align:'center',sortable:true},
			{field:'bjxz',title:'办件类型',width:$(this).width() * 0.1,align:'center'},
			{field:'blhj',title:'删除时办理环节',width:$(this).width() * 0.1,align:'center'},
			{field:'clqx',title:'办结时限',width:$(this).width() * 0.1,align:'center',sortable:true}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openZb(rowData.flowid,rowData.entryid);	
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function openZb(flowid,entryid){
	//window.showModalDialog(webcontext+"/todo.coffice?method=show&id=0&entryid="+entryid+"&flowid="+flowid+"&stepId=0","","dialogWidth:"+(screen.width-120)+"px;dialogHeight:"+(screen.height)+"px;toolbar:no");
	window.open(webcontext+"/todo.coffice?method=show&id=0&entryid="+entryid+"&flowid="+flowid+"&stepId=0",entryid,"width="+(screen.width-120)+",height="+(screen.height-100)+",toolbar=no,resizable=no,scrollbars=yes,status=yes");
}
function  query(query_page){
	 
	var formData = $("#form_search").serialize();
	formData = decodeURIComponent(formData,true);
	//alert(formData);
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('reload',toData);
}
function clickData_datalist(num){
	query(num,page_num);
	curpage = num;
}
function rowformater(value,row,index){
	if(value == null){
		return "<image  src='../css/green.png'/>";
	}
	if(value>24*60){
		return "<image  src='../css/green.png'/>";
	}else if(value>0){
		return "<image  src='../css/yellow.png'/>";
	}else{
		return "<image  src='../css/red.png'/>";
	} 
}
 

function exportXls(){
	var formData = $("#form_search").serialize();
	document.location.href=sys_ctx+"/zbcx.coffice?method=exportXls&"+formData;
}
function exportKjXls(){
	var formData = "cx_bm="+$("#cx_bm1").val()+"&cx_blzt="+$("#cx_blzt1").val()+"&cx_blhj="+$("#cx_blhj1").val();
	document.location.href=sys_ctx+"/zbcx.coffice?method=exportXls&"+formData;
}
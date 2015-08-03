var page_num  = 10;
var querytype = 0 ;//综合查询
var curpage = 1;
var sortBy="";
var sortType = "";

$(function() {
	if(yhid == '62736' || yhid=='admin' || yhid=='sys' || yhid=='63333' || yhid == '63332' || yhid=='63321'){
		$("#cx_dw").show();$("#cx_dw1").show();
	}
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/zbcx.coffice?method=queryZb&page_num='+page_num+'&cx_bm='+$("#cx_bm").val(),
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'guid',checkbox:true},
	        {field:'flowid',hidden:true},
	        {field:'entryid',hidden:true}
		]],
		toolbar:[{
			text:'删除',
			iconCls:'icon-remove',
			handler:function(){
				 delZb();
			}
		}
		],
		columns:[[
			{field:'slbh_day',title:'受理编号',width:$(this).width() * 0.1,align:'center',sortable:true},
			{field:'undo_title',title:'标题',width:$(this).width() * 0.2,align:'center'},
			{field:'ldrq',title:'受理日期',width:$(this).width() * 0.1,align:'center',sortable:true},
			{field:'bjxz',title:'办件类型',width:$(this).width() * 0.1,align:'center'},
			{field:'blhj',title:'办理环节',width:$(this).width() * 0.1,align:'center'},
			{field:'blzt',title:'办理状态',width:$(this).width() * 0.1,align:'center',formatter:rowformater},
			{field:'qx',title:'区县',width:$(this).width() * 0.1,align:'center'},
			{field:'cbdw',title:'承办单位',width:$(this).width() * 0.1,align:'center'},
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
		},
		onSortColumn: function (sort, order) {
				sortBy = sort;
				sortType = order;
				clickData_datalist(curpage);
        }    
	});
});
function openZb(flowid,entryid){
	//window.showModalDialog(webcontext+"/todo.coffice?method=show&id=0&entryid="+entryid+"&flowid="+flowid+"&stepId=0","","dialogWidth:"+(screen.width-120)+"px;dialogHeight:"+(screen.height)+"px;toolbar:no");
	window.open(webcontext+"/todo.coffice?method=show&id=0&entryid="+entryid+"&flowid="+flowid+"&stepId=0",entryid,"width="+(screen.width-120)+",height="+(screen.height-100)+",toolbar=no,resizable=no,scrollbars=yes,status=yes");
}
function  query(query_page){
	querytype = 0;
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	formData = formData+"&sortBy="+sortBy+"&sortType="+sortType;
	formData = decodeURIComponent(formData,true);
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('reload',toData);
}
function clickData_datalist(num){
	//query(num,page_num);
	if(querytype  == 0){
		query(num,page_num);
	}else{
		queryKj(num,page_num);
	}
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
function delZb(){
	if(yhid != 'admin' && yhid != 'sys'){
		alert('只有超级管理才具有删除权限');
		return;
	}
	var selectrow = $("#datalist").datagrid("getSelected");
	var guid = selectrow.entryid;
	if(guid == 'null'){
		alert("请选择记录");
		return;
	}
	_Post("/zbcx.coffice?method=delZb","guid="+guid,function(jsonData){
		alertMes(jsonData);
		query();
	});
}
function rowformater(value,row,index){
	if(value == '办理中'){
		return '<font color=blue>'+value+'</font>'; 
	}else if(value=='已办结'){
		return value; 
	}else if(value=='已超期'){
		return '<font color=red>'+value+'</font>'; 
	}else if(value=='超时办结'){
		return '<font color=LightGrey>'+value+'</font>'; 
	}
}
function schange(){
	query(1);
}
function ztchange(){
	query(1);
}
function queryKj(query_page){
	//alert('12');
	querytype = 1;
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = "cx_bm="+$("#cx_bm1").val()+"&cx_blzt="+$("#cx_blzt1").val()+"&cx_blhj="+$("#cx_blhj1").val();
	formData = formData+"&sortBy="+sortBy+"&sortType="+sortType;
	//alert(formData);
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('reload',toData);
}
function exportXls(){
	var formData = $("#form_search").serialize();
	document.location.href=sys_ctx+"/zbcx.coffice?method=exportXls&"+formData;
}
function exportKjXls(){
	var formData = "cx_bm="+$("#cx_bm1").val()+"&cx_blzt="+$("#cx_blzt1").val()+"&cx_blhj="+$("#cx_blhj1").val();
	document.location.href=sys_ctx+"/zbcx.coffice?method=exportXls&"+formData;
}
var page_num  = 10;
var cur_page = 1;
var sortBy="";
var sortType = "";

$(function(){
	_Post("/undo.coffice?method=init","",function(jsonData){
		_loadSelect(jsonData);
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/undo.coffice?method=query&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'id',hidden:true},
	        {field:'tsgd',hidden:true},
	        {field:'bllx',hidden:true}
		]],
		columns:[[
			{field:'sfcq',title:'状态',width:$(this).width() * 0.05,align:'center',formatter:rowformater},
			{field:'bt',title:'标题',width:$(this).width() * 0.2,align:'center'},
			{field:'ldr',title:'来电人',width:$(this).width() * 0.1,align:'center'},
			{field:'lxdh',title:'联系电话',width:$(this).width() * 0.1,align:'center'},
			{field:'qx',title:'区县',width:$(this).width() * 0.1,align:'center'},
			{field:'cbdw',title:'承办单位',width:$(this).width() * 0.1,align:'center'},
			{field:'xxly',title:'信息来源',width:$(this).width() * 0.1,align:'center'},
			{field:'xzfl',title:'性质分类',width:$(this).width() * 0.1,align:'center'},
			{field:'blhj',title:'当前环节',width:$(this).width() * 0.1,align:'center'},
			{field:'sdsj',title:'收到时间',width:$(this).width() * 0.1,align:'center',sortable:true}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openUndo(rowData.id,rowData.tsgd,rowData.bllx);
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		},
		onSortColumn: function (sort, order) {
				sortBy = sort;
				sortType = order;
				clickData_datalist(cur_page);
        }    
	});
});
function query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	cur_page = query_page;
	var formData = $("#form_search").serialize();
	formData = formData+"&sortBy="+sortBy+"&sortType="+sortType;
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function clickData_datalist(num){
	query(num,page_num);
	cur_page = num;
}
function openUndo(id,tsgd,bllx){
	window.open(webcontext+'/formuse.coffice?method=showCurForm&id='+id+"&bllx="+bllx+"&tsgd="+tsgd,id,"width=900,height="+(screen.height)+",toolbar=no,resizable=no,scrollbars=yes,status=yes");
}
function refreshUnDo(){
	var formData = $("#form_search").serialize();
	formData = formData+"&sortBy="+sortBy+"&sortType="+sortType;
	var toData = getObj(formData,cur_page);
	$('#datalist').datagrid('load',toData);
}
function rowformater(value,row,index){
	 if(value >=12*60){
	 	return '<img src=../css/green.jpg>';
	 }else if(value>=0 && value <12*60){
	 	return '<img src=../css/yellow.jpg>';
	 }else if(value<0){
	 	return '<img src=../css/red.jpg>';
	 }
}
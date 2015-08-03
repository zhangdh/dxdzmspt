var page_num  = 10;
var querytype = 0 ;//综合查询
var curpage = 1;
var sortBy="";
var sortType = "";
var formid = "";
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/ldps.coffice?method=queryCsj&page_num='+page_num+'&cx_bm='+$("#cx_bm").val(),
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'formid',checkbox:true},
	        {field:'flowid',hidden:true},
	        {field:'entryid',hidden:true}
		]],
		columns:[[
			{field:'slbh_day',title:'受理编号',width:$(this).width() * 0.2,align:'center',sortable:true},
			{field:'undo_title',title:'标题',width:$(this).width() * 0.2,align:'center'},
			{field:'ldrq',title:'受理日期',width:$(this).width() * 0.15,align:'center',sortable:true},
			{field:'blhj',title:'办理环节',width:$(this).width() * 0.15,align:'center'},
			{field:'qx',title:'区县',width:$(this).width() * 0.15,align:'center'},
			{field:'cbdw',title:'承办单位',width:$(this).width() * 0.15,align:'center'},
			{field:'clqx',title:'处理期限',width:$(this).width() * 0.15,align:'center',sortable:true},
			{field:'czcl',title:'操作处理',width:$(this).width() * 0.1,align:'center',formatter:operColumn}
		]],
		onBeforeLoad:function(paras){},
		onLoadError:function(){
			alert('加载数据失败');
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
	window.open(webcontext+"/todo.coffice?method=show&id=0&entryid="+entryid+"&flowid="+flowid+"&stepId=0",entryid,"width=900,height="+(screen.height)+",toolbar=no,resizable=no,scrollbars=yes,status=yes");
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
	if(querytype  == 0){
		query(num,page_num);
	}else{
		queryKj(num,page_num);
	}
	curpage = num;
}
function operColumn(value,row,index){
	return "<a href='#' style='text-decoration:none;' onclick=openZb('"+row.flowid+"','"+row.entryid+"')>查看</a>&nbsp;"+"<a href='#' style='text-decoration:none;' onclick=ldps('"+row.formid+"')>批示</a>";
}
function ldps(id){
	formid = id;
	$("#ldyj").val("");
	$("#dataedit").window("open");
}
function save(){
	_Post("/ldps.coffice?method=save","ldyj="+$("#ldyj").val()+"&formid="+formid,function(jsonData){
		alertMes(jsonData);
		if(jsonData.result){
			$("#dataedit").window("close");
			query(curpage);
		}
	});
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


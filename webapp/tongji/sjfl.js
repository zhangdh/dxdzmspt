var page_num  = 100;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/tjbb.coffice?method=ldsj&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'lbmc',title:'类别名称',width:$(this).width() * 0.2,align:'center'},
			{field:'sll',title:'受理量',width:$(this).width() * 0.1,align:'center'},
			{field:'yxsl',title:'有效数量',width:$(this).width() * 0.1,align:'center'},
			{field:'bjsl',title:'当前办结数量',width:$(this).width() * 0.1,align:'center'},
			{field:'yxl',title:'有效率',width:$(this).width() * 0.15,align:'center',formatter:yxl},
			{field:'bjl',title:'当前办结率',width:$(this).width() * 0.15,align:'center',formatter:bjl}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			//alert('加载数据失败');
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function query(query_page){
	if($("#cx_sjq").val() == "" || $("#cx_sjz").val() == ""){
		alert("请正确选择时间条件");return;
	}
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('load',toData);
}
function yxl(value,row,index){
	var yxsl = row.yxsl;
	var sll  = row.sll;
	if(sll==0){
		return "100%";
	}else{
		return Math.round((yxsl*10000/sll))/100+"%";
	}
}
function bjl(value,row,index){
	var bjsl = row.bjsl;
	var sll  = row.sll;
	if(sll==0){
		return "100%";
	}else{
		return Math.round((bjsl*10000/sll))/100+"%";
	}
}
function queryChart(){
	if($("#cx_sjq").val() == "" || $("#cx_sjz").val() == ""){
		alert("请正确选择时间条件");return;
	}
	var querystr = $("#form_search").serialize();
	querystr = querystr+"&type=ldsj";
	window.open("chart.jsp?"+querystr,"_blank","width:600px;height:600px");
}
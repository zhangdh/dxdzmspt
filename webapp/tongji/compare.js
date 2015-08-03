var page_num  = 10;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/tjbb.coffice?method=compare&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'mc',title:'类别名称',width:$(this).width() * 0.5,align:'center'},
			{field:'sl1',title:'日期段1数据',width:$(this).width() * 0.25,align:'center'},
			{field:'sl2',title:'日期段2数据',width:$(this).width() * 0.25,align:'center'}
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
	if(!Validate.CheckForm($('#form_search')[0]))return;
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
		return (yxsl*100/sll)+"%";
	}
}
function bjl(value,row,index){
	var bjl = row.bjl;
	var sll  = row.sll;
	if(sll==0){
		return "100%";
	}else{
		return (bjl*100/sll)+"%";
	}
}
function queryChart(){
	if(!Validate.CheckForm($('#form_search')[0]))return;
	var querystr = $("#form_search").serialize();
	querystr = querystr+"&type=compare";
	window.open("chart.jsp?"+querystr,"_blank","width:500px;height:600px");
}
var page_num  = 10;
$(function() {
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/fwqq.coffice?method=queryGd&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		columns:[[
			{field:'ldrxm',title:'来电人',width:$(this).width() * 0.1,align:'center'},
			{field:'lxhm',title:'联系电话',width:$(this).width() * 0.1,align:'center'},
			{field:'bt',title:'标题',width:$(this).width() * 0.5,align:'center'},
			{field:'nrlb',title:'内容类别',width:$(this).width() * 0.1,align:'center'},
			{field:'cjsj',title:'受理时间',width:$(this).width() * 0.1,align:'center'},
			{field:'clr',title:'创建人员',width:$(this).width() * 0.1,align:'center'},
			{field:'zt',title:'是否办结',width:$(this).width() * 0.1,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			openGd(rowData.guid);	
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
	_Post("/fwqq.coffice?method=init","",function(jsonData){
		_loadSelect(jsonData);
	});
});
function  query(query_page){
	if(query_page == undefined){
		query_page = 1;
	}
	var formData = $("#form_search").serialize();
	var toData = getObj(formData,query_page);
	$('#datalist').datagrid('reload',toData);
}
function clickData_datalist(num){
	query(num,page_num);
}
function openGd(guid){
	_Post("/fwqq.coffice?method=showFwqq","guid="+guid,function(jsonData){
		loadForm("form_show",jsonData);
		if(jsonData.ifEnd==1){
			$("#modi").hide();
			$("#modiEnd").hide();
		}else{
			$("#modi").show();
			$("#modiEnd").show();
		}
		$("#dataedit").window("open");
		$("#btn_save").hide();
	});
}
function modiFwqq(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	_Post("/fwqq.coffice?method=modiFwqq&end=0",saveStr,function(jsonData){
		alertMes(jsonData);	
		query();
		$("#dataedit").window("close");
	});
}
function modiFwqqEnd(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	_Post("/fwqq.coffice?method=modiFwqq&end=1",saveStr,function(jsonData){
		alertMes(jsonData);	
		query();
		$("#dataedit").window("close");
	});
}
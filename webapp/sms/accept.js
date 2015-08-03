var page_num  = 10;
var ldhm="";
var lypath="";
$(function(){
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/sms.coffice?method=queryDx&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		frozenColumns:[[
	        {field:'id',hidden:true}
		]],
		columns:[[
			{field:'ldhm',title:'发送号码',width:$(this).width() * 0.1,align:'center'},
			{field:'content',title:'短信内容',width:$(this).width() * 0.5,align:'center'},
			{field:'create_time',title:'收到时间',width:$(this).width() * 0.1,align:'center'},
			{field:'state',title:'处理状态',width:$(this).width() * 0.1,align:'center'}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			//alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			showDx(rowData.id);
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function replay(){
	if($("#replay_content") == ""){
		alert('回复内容不可为空');
		return;
	}
	var saveStr  = $("#form_show").serialize();
	alert(saveStr);
	_Post("/sms.coffice?method=replayDx",saveStr,function(jsonData){
		alertMes(jsonData);	
		query();
		$("#dataedit").window("close");
	});
}
function saveEnd(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&sfT="+$("#sf").find("option:selected").text()+"&dsT="+$("#ds").find("option:selected").text()+"&xqT="+$("#xq").find("option:selected").text()+"&xzT="+$("#xz").find("option:selected").text();
	_Post("/fwqq.coffice?method=saveFwqq&end=1",saveStr,function(jsonData){
		alertMes(jsonData);	
		query();
		$("#dataedit").window("close");
	});
}
function query(query_page){
	$("#ldhm").val("");
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
function showDx(id){
	_Post("/sms.coffice?method=show","guid="+id,function(jsonData){
		loadForm("form_show",jsonData);
		$('#dataedit').window('open');
	});
}
function zb(){
	var saveStr  = "lxhm="+$("#dxhm").val()+"&nr="+$("#dxnr").val()+"&xxly=200603&id="+$("#id").val();
	_Post("/sms.coffice?method=saveFwqq&end=0",saveStr,function(jsonData){
		query();
		$("#dataedit").window("close");
		var fwqqid = jsonData.fwqqid;
		window.showModalDialog(webcontext+'/gdgl/default.do?method=createGd&fwqqid='+fwqqid,"","dialogWidth:"+(screen.width-120)+"px;dialogHeight:"+(screen.height)+"px;toolbar:no");
	});

}
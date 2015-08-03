var page_num  = 10;
var ldhm="";
var lypath="";
$(function(){
	var url = window.location.href;
	if(url.indexOf("lypath=")>-1 && url.indexOf("ldhm=")>-1){
		var para = url.split("?")[1];
		ldhm = (para.split("&")[1]).split("=")[1];
		lypath = (para.split("&")[0]).split("=")[1];
		$("#ldhm").val(ldhm);
		$("#lypath").val(lypath);
		$("#lxhm").attr('readonly','readonly');
		$("#cxldhm").val(ldhm);
	}
	_Post("/fwqq.coffice?method=init","",function(jsonData){
		_loadSelect(jsonData);
		get_xz();
	});
	$('#datalist').datagrid({
		method:'post',
		url:webcontext+'/fwqq.coffice?method=queryFwqq&page_num='+page_num,
		rownumbers:true,
		remoteSort:true,
		fitColumns: true,
		queryParams:{'ldhm':$("#ldhm").val()},
		toolbar:[{
			text:'新建',
			iconCls:'icon-add',
			handler:function(){
				add();
			}
		}
		],
		columns:[[
			{field:'bt',title:'标题',width:$(this).width() * 0.3,align:'center'},
			{field:'ldrxm',title:'来电人',width:$(this).width() * 0.1,align:'center'},
			{field:'lxhm',title:'联系电话',width:$(this).width() * 0.1,align:'center'},
			{field:'nrlb',title:'内容类别',width:$(this).width() * 0.1,align:'center'},
			{field:'cblx',title:'承办类型',width:$(this).width() * 0.1,align:'center'},
			{field:'clr',title:'受理人',width:$(this).width() * 0.1,align:'center'},
			{field:'cjsj',title:'受理时间',width:$(this).width() * 0.1,align:'center'},
			{field:'sfbj',title:'是否办结',width:$(this).width() * 0.1,align:'center'},
			{field:'lypath',title:'播放',width:$(this).width() * 0.05,align:'center',formatter:rowformater}
		]],
		onBeforeLoad:function(paras){

		},
		onLoadError:function(){
			alert('加载数据失败');
		},
		onClickRow:function(rowIndex, rowData){
			if(rowData.cblb_dm=="94063"){
				openZb(rowData.formid,rowData.entryid,rowData.tablename);
			}else{
				openFwqq(rowData.guid);
			}
		},
		rowStyler:function(index,row){ 
			if (index%2 == 1){               
				return 'background-color:#F2F2F2;';          
			 }       
		} 
	});
});
function rowformater(val,row,index){
	return "<a href='#' style='text-decoration:none;' onclick=play('"+val+"')>播放</a>";
}
function play(path){	
	if(path == ""){
		alert("此工单未关联到录音，可以为补录工单");
	}else{
		_Post("/thxx.doAction?method=queryLyPathById&id="+path,"",function(jsonData){
			alert(jsonData.lypath);
			window.open("../play.jsp?url="+jsonData.lypath,"","height=100, width=500,toolbar=no, menubar=no, scrollbars=no, left=500,top=400,resizable=no,location=no, status=no,titlebar=no");
		})
	}
}
function add(){
	$('#form_show')[0].reset();
	$("#dataedit").window("open");
	$("#btn_save").show();
	$("#btn_saveEnd").show();
	$("#btn_zb").hide();
	$("#lxhm").val(ldhm);
	$("#lypath").val(lypath);
	init();
}
function cblx(v){
	if(v=="94063"){
		$("#btn_zb").show();
		$("#btn_save").hide();
		$("#btn_end").hide();
	}else{
		$("#btn_zb").hide();
		$("#btn_save").show();
		$("#btn_end").show();
	}
}
function save(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&sfT="+$("#sf").find("option:selected").text()+"&dsT="+$("#ds").find("option:selected").text()+"&xqT="+$("#xq").find("option:selected").text()+"&xzT="+$("#xz").find("option:selected").text();
	_Post("/fwqq.coffice?method=saveFwqq&end=0",saveStr,function(jsonData){
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
function openFwqq(guid){
	_Post("/fwqq.coffice?method=showFwqq","guid="+guid,function(jsonData){
		loadForm("form_show",jsonData);
		$("#dataedit").window("open");
		$("#btn_save").hide();
		$("#btn_end").hide();
		if(jsonData.ifEnd == 1){
			$("#btn_modiend").hide();
			$("#btn_modi").hide();
		}else{
			$("#btn_modiend").show();
			$("#btn_modi").show();
		}
	});
}
function newFqq (){
	$('#form_show')[0].reset();
	$("#btn_save").show();
}
function zb(){
	if(!Validate.CheckForm($('#form_show')[0]))return;
	var saveStr  = $("#form_show").serialize();
	saveStr = saveStr+"&sfT="+$("#sf").find("option:selected").text()+"&dsT="+$("#ds").find("option:selected").text()+"&xqT="+$("#xq").find("option:selected").text()+"&xzT="+$("#xz").find("option:selected").text();
	_Post("/fwqq.coffice?method=saveFwqq&end=0",saveStr,function(jsonData){
		$("#dataedit").window("close");
		var fwqqid = jsonData.fwqqid;
		var reData = window.open(webcontext+'/gdgl/default.do?method=createGd&fwqqid='+fwqqid,"","width=900,height="+(screen.height)+",toolbar=no,location=no,resizable=no,scrollbars=yes,status=no");
	});
}
function openZb(formid,entryid,tablename){
	window.open(webcontext+"/fwqq.coffice?method=openZb&tablename="+tablename+"&entryid="+entryid+"&formid="+formid,entryid,"width=900,height="+(screen.height)+",toolbar=no,location=no,resizable=no,scrollbars=yes,status=no");
}
function init(){
	$("#lb_dm").val("401701");
	$("#cblb_dm").val("94062");
	$("#xzfl").val("200501");
	$("#xxly").val("200601");
	$("#sfbm").val("200101");
	$("#btn_save").show();
	$("#btn_end").show();
	$("#btn_modiend").hide();
	$("#btn_modi").hide();
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
function get_xz(){
	var xqDm = $("#xq").val();
	_Post("/fwqq.coffice?method=getQH&dm=xz&xqDm="+xqDm,"",function(jsonData){
		_loadSelect(jsonData);
	});
}
function vali(o){
	
	 var value=o.value;
	 alert(parseInt(value))
	 if(parseInt(value)>50){                     
	 	alert('到达限制长度');
	 	alert(value.substr(0,50));                     
	 } 
}
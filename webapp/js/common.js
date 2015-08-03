var sys_btn_auth="";//有权限的按钮列表
$.ajaxSetup ({
    cache: false //关闭AJAX相应的缓存
});
$(function() {
	//发起ajax请求时禁用按钮防止多次请求
    $("body").ajaxStart(function(){if(simplefect==3){$(":input").attr('disabled',true);}});
   	$("body").ajaxComplete(function(){
   	//$(":input").attr('disabled',false);
   	if(simplefect==3){
     	$(":input").attr('disabled',false);
   	}else if(simplefect==10){
   	   btn_disabled();
   	}else if(simplefect==1){	
      	//$(":input").attr('disabled',false);  
   	    //SetButtoninit();      	 

   	}else{ 	

   	}
   	try{
   	if(typeof(eval("afterAjax"))=="function"){
   	  afterAjax();
   	 } 
   	 }catch(e){
   	 }
   		});
});
var sys_g_formData;//抽取出json中的formData供select组件使用
function bind(bindValue){//json map结构
	for(var key in bindValue.formData){
		sys_g_formData = bindValue.formData;
		break;
	}
	//结束
	if (bindValue.result===false){
		window.alert(bindValue.msg);
		return;
	}
	$.each(bindValue,function(k,v){
		k=k.toLowerCase();
		if (/selectData.*/ig.test(k))//先显示下拉列表数据
			{bind(v);return;}
		if ((/formData.*/ig.test(k))||(/gridData.*/ig.test(k)))
			{bind(v);return;}
		var elem = document.getElementById(k);
		if (elem!==null){
			switch(elem.tagName) {
				case "INPUT":
					bindInput(k,v);
					break;
				case "TEXTAREA":
					bindText(k,v);
					break; 
				case "TABLE": 
					bindTable(k,v);
					break; 
				case "SELECT": 
					bindSelect(k,v);
					break; 
				case "DIV":
				case "SPAN":
					elem.innerHTML = v;
			}
		}
	});
	var hash=bindValue.tableHover;
	for(var key in hash){
		try{
			eval("callback_tableHover_"+hash[key]+"()");
		}catch(exception){
			var fn=eval("callback_trclick_"+hash[key]);
			$("#"+hash[key]).tableHover({getCell:0,trClick:fn});
		}
	}
	sys_attachment_auth();//判断附件是否有上传修改的权限
}
function bindInput(e,v) {
		v=filterTimestamp(v);
		//alert("e:"+$("[name='"+e+"']").attr("type")+":"+e);
		switch ($("[name='"+e+"']").attr("type")) {
			case "text":
			case "hidden":
			case "password":$("#"+e).val(v); break;
			case "radio" :
			case "checkbox" :
				$("[name="+e+"]").each(function(){
					if (String(v).indexOf(this.value)>-1) {this.checked=true;}
					else{this.checked=false;} 
				});
				break;
		}
}
function bindSelect(e, v) {
	if (v instanceof Array){//初始化select列表[{dm='',mc=''},{}]
                $("#"+e).empty();
		var objsel =document.getElementById(e);//alert(v);
		for(var i=0;i<v.length;i=i+1){
			if((i==0)&&(objsel.type!="select-multiple")){
				if ((v[i].dm!="-")&&(v[i].dm!="+")){//如果有删除标志则不显示请选择
					var oOption0=document.createElement("OPTION");
					oOption0.value="";
					oOption0.text="请选择";
					objsel.options.add(oOption0);
				}
				if ((v[i].dm=="-")||(v[i].dm=="+")){
					continue;
				}
			}
			var oOption=document.createElement("OPTION");
			if (v[i].dm=="+") {v[i].dm="";}
			oOption.value=v[i].dm;
			oOption.text=v[i].mc;
			objsel.options.add(oOption);
		}
		if (objsel.type!="select-multiple"){objsel.selectedIndex=0;}
	}else{
		var element =document.getElementById(e);
		for (var j = 0; j < element.options.length; j=j+1) {
			var temp = element.options[j];
			//可以绑定多值的情况
			temp.selected= (eval("/\\b"+temp.value+"\\b/.test(\""+v+"\")"));
		}
		
		/**
		*绑定select的时候，处理下级select select的cache!=false
		*  1. 如果页面缓存中没有，请求后台，并放入到缓存中
		*  2. 如果缓存中存在，从缓存中读取
		*
		*这样的目的，在当前页面，只需要第一次把相关列表加载，以后就不需要访问后台
		*cache=false的情况，是ajax，回显的时候，后台必须提供list，并且只包含一条记录
		
		**/
		$("select[sjid='"+e+"']").each(function(){
			var child = $(this);
			var childid = $(this).attr("id");
			var dm = $(this).attr("dm"); 
			var lb_dm = dm.substring(dm.lastIndexOf("=")+1);
			var selectid = $(this).attr("id");
			var sjid = $("#"+$(this).attr("sjid")).attr("id"); 
			var sjdm; 
			for(var key in sys_g_formData){
				if(key==sjid){
					sjdm = sys_g_formData[key];
					break;
				}
			}
			var flag = 0;
			for(var key in sys_g_formData){
				if(key==childid){
					flag = 1;
					break;
				}
			}
			if($(this).attr("cache")!="false" && flag==0){
				//1. 清理下拉列表
				$(this).empty();
				var option_list = $(this).next(".editable-select").next(".editable-select-options").children("ul");
				$(option_list).empty();
				$("#"+$(this).attr("id")+"_values").attr("value","");
				
				var result = eval(effective_selectData.stringify({lb_dm:lb_dm,selectid:selectid,sjdm:sjdm}));
				if(result.length==0){
					//alert("没有缓存，读取并放入页面缓存");
					$.ajax({
				  		type: "POST",
					    url:   sys_ctx+"/dmCache/default.do?method=getChild&dm="+dm+"&sjdm="+sjdm+"&selectid="+selectid ,
					    dataType: "json",
					    success:function(json){
							bind(json);
							//放入缓存
							var selectData;
							for(var key in json.selectData){
								selectData = json.selectData[key];
							}
							$("#"+selectid).editableSelect(selectData);
							for(var i=0;i<selectData.length;i++){
				    			var temp_selectData = {
			    					dm: selectData[i].dm,
			    					mc: selectData[i].mc,
			    					py: selectData[i].py,
			    					zt_dm: selectData[i].zt_dm,
			    					lb_dm: selectData[i].lb_dm,
			    					sjdm: selectData[i].sjdm,
			    					selectid: selectData[i].selectid
				    			};
				    			effective_selectData.insert(temp_selectData);
				    		}
					    }
				  	});
				//4. 如果缓存存在，从页面读取	
				}else{
					$(this).append("<option value=''>请选择</option>");
					for(var i=0;i<result.length;i++){
						var option = '<option value="'+result[i].dm+'">'+ result[i].mc +'</option>';
	        				$(this).append(option);
					}
					$(this).editableSelect(result);
				}
			}
		});
		
		//处理非一级、非ajax的select回显
		if($("#"+e).attr("sjid") &&　$("#"+e).attr("cache")!="false"){
			var select = $("#"+e);
			//1. 取得 DM 、SELECT ID 、上级SELECT ID 、上级SELECT值 
			var dm = $("#"+e).attr("dm"); 
			var lb_dm = dm.substring(dm.lastIndexOf("=")+1);
			var selectid = $("#"+e).attr("id");
			var sjid = $("#"+$("#"+e).attr("sjid")).attr("id"); 
			var sjdm; 
			for(var key in sys_g_formData){
				if(key==sjid){
					sjdm = sys_g_formData[key];
					break;
				}
			}
			//2. 清理下拉列表
			$(select).empty();
			var option_list = $(select).next(".editable-select").next(".editable-select-options").children("ul");
			$(option_list).empty();
			$("#"+$(this).attr("id")+"_values").attr("value","");
			//3. 如果页面缓存中没有，请求后台，并放入到缓存中
			var result = eval(effective_selectData.stringify({lb_dm:lb_dm,selectid:selectid,sjdm:sjdm}));
			if(result.length==0){
				//alert("没有缓存，读取并放入页面缓存");
				$.ajax({
			  		type: "POST",
				    url:   sys_ctx+"/dmCache/default.do?method=getChild&dm="+dm+"&sjdm="+sjdm+"&selectid="+selectid ,
				    dataType: "json",
				    success:function(json){
						bind(json);
						$("#"+e).val(v);
						//放入缓存
						var selectData;
						for(var key in json.selectData){
							selectData = json.selectData[key];
						}
						for(var i=0;i<selectData.length;i++){
			    				var temp_selectData = {
			    					dm: selectData[i].dm,
			    					mc: selectData[i].mc,
			    					py: selectData[i].py,
			    					zt_dm: selectData[i].zt_dm,
			    					lb_dm: selectData[i].lb_dm,
			    					sjdm: selectData[i].sjdm,
			    					selectid: selectData[i].selectid
			    				};
			    				effective_selectData.insert(temp_selectData);
			    		}
			    		$("#"+e).editableSelect(selectData);
				    }
			  	});
			//4. 如果缓存存在，从页面读取	
			}else{
				$(select).append("<option value=''>请选择</option>");
				for(var i=0;i<result.length;i++){
					var option = '<option value="'+result[i].dm+'">'+ result[i].mc +'</option>';
        			$(select).append(option);
				}
				$("#"+e).editableSelect(result);
				$("#"+e).val(v);
			}
		}
	}
}
function bindText(e, v) {
		v=filterTimestamp(v);
		$("#"+e).val(v);
}
function bindTable(tableId,tableJson,isDelRow){
	var tableData=tableJson.data;
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	if (tableId.indexOf("table_edit")!=-1) isDelRow=true;//约定:可增删行的表格id以table_edit开头
	if (isDelRow) {$("#"+tableId+" tbody tr:not(:first)").remove();}
	var keepRow=$("#"+tableId+" tbody tr").length-1;//页面中保留行数
	var i;
	for (i=0;i<tableData.length;i=i+1){
		var rowJson=tableData[i];
		var newTr=trInfo;
		for(var j in rowJson){
			var jValue=eval("rowJson."+j);
			jValue=filterTimestamp(jValue);
			var re=eval("/{"+j+"}/ig");
			newTr=newTr.replace(re,jValue);
		}
		newTr=newTr.replace(/{.*}/ig,"");//清空没有数据的单元格
		$("#"+tableId+" tbody tr:eq("+i+")").after("<tr>"+newTr+"</tr>");
		if (i<keepRow)	{$("#"+tableId+" tbody tr:last").remove();}
		//支持select的绑定,select的option值通过jjd.setSelect传入
		$("#"+tableId+" tbody tr:eq("+(i+1)+") select").each(
			function(i){$(this).val(eval("rowJson."+this.name));}
		);
	}
	//翻页时清空余下的行
	for (1==1;i<keepRow;i=i+1){
		$("#"+tableId+" tbody tr:eq("+(i+1)+") td").empty();
	}
	if (typeof(tableJson.page)!="undefined"){//如果存在导航条
		bindTableListNav(tableJson.page,tableId);
		//fixedThead(tableId);
	}
}
function sys_expandTable(tableId,rowNum){	
	$("#"+tableId+" tbody tr:not(:first)").remove();
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	newTr=trInfo.replace(/value=["'\w]*{\w*}["'\w]*/igm,"value=''");//防止出现“value=”的情况
	newTr=newTr.replace(/{.*}/igm,"");
	for(i=0;i<rowNum;i=i+1){
		$("#"+tableId+" tbody tr:eq("+i+")").after("<tr>"+newTr+"</tr>");
	}
	var fn=eval("callback_trclick_"+tableId);
	$("#"+tableId).tableHover({getCell:0,trClick:fn});
}
//过滤掉时间串的毫秒
function filterTimestamp(str){
	var v=new String(str);
	if (v.match(/(\d\d:\d\d:\d\d)\.\d{1,3}/)) {
		var v2=v.replace(/(\d\d:\d\d:\d\d)\.\d{1,3}/g, "$1");
		return v2;
	}
	return v;
}
function checkTrue(value) {
		var ret="";
		switch (typeof(value)) {
			case 'boolean': ret = value; break;
			case 'string': ret = (value === true || value == "1" || value == "true" || value == "yes"); break;
			case 'number': ret = (parseInt(value) == 1); break;
			default: ret = false;
		}
		return ret; 
	}
//级联下拉框
function selectJl(src,target,jsonSid){
	$("#"+src).change(function(){
		$("#"+target).empty();
		var selectedValue=$("#"+src).val();
		$.getJSON("../Service",$.extend(jsonSid,{selected:selectedValue}),function (msg){if (ajaxSucc(msg)){bindSelect(target,eval("msg."+target));}});
	});
}

function getValueFromUrl(name){
	var regexp=eval("/^\\?.*"+name+"=([^&]*).*$/");
	return (window.location.search).replace(regexp, "$1");
}

function getFilenameFromUrl(){
	var regexp=eval("/$/");
	return (window.location.href).replace(regexp, "$1");
}

//绑定列表导航
function bindTableListNav(pageInfo,tableId){
	var prefix="";//选择器前缀
	if (tableId===undefined){tableId="";}//页面中只有一个列表，不需要输入tableId
	else{prefix="#div_"+tableId+" ";tableId="_"+tableId;}
	$(prefix+"span[navid=page_cur]").text(pageInfo.page_cur);
	$(prefix+"span[navid=page_allPage]").text(pageInfo.page_allPage);
	$(prefix+"span[navid=page_allCount]").text(pageInfo.page_allCount);
	var pageNum=parseInt(pageInfo.page_cur);
	var page_pre=pageNum-1;
	var page_next=pageNum+1;	
	$(prefix+"span[navid=page_pre]").unbind();
	$(prefix+"span[navid=page_pre]").click(function(){
		if (page_pre===0){window.alert("已到达第一页");}		
		else{eval("callback_getPageData"+tableId+"("+page_pre+")");}
	});
	$(prefix+"span[navid=page_next]").unbind();
	$(prefix+"span[navid=page_next]").click(function(){
		if (page_next>parseInt(pageInfo.page_allPage)) {window.alert("已是最后一页");return false;}
		eval("callback_getPageData"+tableId+"("+page_next+")");return false;
	});
	$(prefix+"span[navid=page_enter]").unbind();
	$(prefix+"span[navid=page_enter]").click(function(){
		var _goto=$(prefix+"input[navid=page_goto]").val();
		if (isNaN(_goto)){window.alert("请输入有效的页码");return false;}
		if ((_goto>parseInt(pageInfo.page_allPage))||(_goto<1)) {window.alert("请输入有效的页码");return false;}
		eval("callback_getPageData"+tableId+"("+_goto+")");
	});
}
//增加ajax请求异常处理，可以获取500页面中的异常信息
(function($){
    /*var ajax=$.ajax;
    $.ajax=function(s){
        var old=s.error;
        var errHeader=s.errorHeader||"Error-Json";
        s.error=function(xhr,status,err){
          var errMsg = eval("(" + xhr.getResponseHeader(errHeader) + ")");
          old(xhr,status,errMsg);
        }
        ajax(s);
    }*/
 })(jQuery); 
//提示成功与否，一般在提交操作使用
function ajaxAlert(json){
 var message="";
  if(json.result===false||json.exception===true){
		if (json.exception===true){
			message=decodeURIComponent(json.msg).replace(/\+/g," ");
		}else{
			message=json.msg;
		}
		if($("#dialog-message").length <= 0){
		$("body").append("<div id='dialog-message'  title='信息提示'></div>");  
		}
		$("#dialog-message").append(message);
		$("#dialog-message").dialog({buttons: {确定: function() {$(this).dialog('close');$("#dialog-message").empty()}}});
		return false;
	}else{	    
        if(typeof(json)=='string'){
        message = json;
        }else{
        message="处理成功";
        }
		if($("#dialog-message").length <= 0){
		$("body").append("<div id='dialog-message' align='center' title='信息提示'></div>");  
		}
		$("#dialog-message").append(message);
		$("#dialog-message").dialog({height: 100,width:230});
		setTimeout(function(){$("#dialog-message").dialog('close');$("#dialog-message").empty()},1000);	
		return true;
	}
}
//信息提示
function sys_alert(msg,param){
   if(param=="ok"){//显示确认按钮
       	if($("#dialog-message").length <= 0){
		$("body").append("<div id='dialog-message'  title='信息提示'></div>");  
		}
		$("#dialog-message").append(msg);
		$("#dialog-message").dialog({height: 100,width:230,buttons: {确定: function() {$(this).dialog('close');$("#dialog-message").empty()}}});
   }else{//弹出框自动消失
		if($("#dialog-message").length <= 0){
		$("body").append("<div id='dialog-message' align='center' title='信息提示'></div>");  
		}
		$("#dialog-message").append(msg);
		$("#dialog-message").dialog({height: 100,width:230});
		setTimeout(function(){$("#dialog-message").dialog('close');$("#dialog-message").empty()},1000);	
   }
}
//异常时提示，成功不提示，一般查询数据操作使用
function ajaxSucc(json){
	if (json.result===false){
		ajaxAlert(json);
		return false;
	}else{
		return true;
	}
}
/*两参数时会自动调用bind，三参数需要自己调用*/
function sys_ajaxGet(url,data,callback){
	if (callback===undefined){
		if (data===undefined){
			$.getJSON(sys_ctx+url,function(json){bind(json);});
		}else{
			$.getJSON(sys_ctx+url,data,function(json){bind(json);});
		}
	}else{
		$.getJSON(sys_ctx+url, data,callback);
	}
}
function sys_ajaxPost(_url,_data,callback){
    var gw_sel; //存在多个岗位时，用户选择的岗位
    if(sys_yh_gw.split(";").length>1){//选择部门、岗位
		var obj = new Object();
		obj.sys_yh_gw=sys_yh_gw;
		obj.mk_dm=mk_dm;
		gw_sel=window.showModalDialog(sys_ctx+"/org/organize/sys_yh_gw.jsp?sys_yh_gw="+sys_yh_gw,obj);
    }else{
	    if(sys_yh_gw.indexOf("|")>0){
	    	gw_sel = sys_yh_gw.split("|")[1];
	    }else{
	    	gw_sel = sys_yh_gw;
	    }
    }
    var temp = gw_sel.split(":");
    _data =_data+"&bmid="+temp[0]+"&gwid="+temp[1];
	if (callback===undefined){
		$.ajax({type:"POST", url:sys_ctx+_url, data:_data, dataType:"json", 
		  success:function (msg) {
			ajaxAlert(msg);
		},error:function(xhr, ajaxOptions, thrownError){
            ajaxAlert(thrownError);
		}});
	}else{
		$.ajax({type:"POST", url:sys_ctx+_url, data:_data, dataType:"json", success:callback,
		  error:function(xhr, ajaxOptions, thrownError){
			ajaxAlert(thrownError);
		}});
	}
}
function sys_ajaxPostDirect(_url,_data,callback){
	if (callback===undefined){
		$.ajax({type:"POST", url:sys_ctx+_url, data:_data, dataType:"json", success:function (msg) {
			ajaxAlert(msg);
		},error:function(xhr, ajaxOptions, thrownError){
			ajaxAlert(thrownError);
		}});
	}else{
		$.ajax({type:"POST", url:sys_ctx+_url, data:_data, dataType:"json", success:callback,
			error:function(xhr, ajaxOptions, thrownError){
				ajaxAlert(thrownError);
		}});
	}
}
function tableBarClick(tableid){
	if (tableid===undefined){window.alert("tableBarClick参数错误");return;}
	$("#sys_td_"+tableid).slideToggle("fast");
	if ($("#sys_td_"+tableid+"_arrow").hasClass("c_table_bar_arrow_up")){
		$("#sys_td_"+tableid+"_arrow").removeClass("c_table_bar_arrow_up").addClass("c_table_bar_arrow_down");
	}else{
		$("#sys_td_"+tableid+"_arrow").removeClass("c_table_bar_arrow_down").addClass("c_table_bar_arrow_up");
	}
}

//根据权限显示按钮,传入的按钮id逗号分隔
function sys_showButton(btnidStr){
	$(".c_btn_auth").hide();//先将需要权限控制的全部隐藏
	$(".c_btn_hide").hide();//与权限无关但也要控制状态的
	if(btnidStr===undefined||btnidStr==''){
	return;
	}
	var btnArray=btnidStr.split(",");
	for(var key in btnArray){
		if (sys_btn_auth.indexOf(btnArray[key])>=0)
			{$("#"+btnArray[key]).show();}
	}
}
//固定数据列表表头
function fixedThead(tableId){
	//var tHeadHeight = $("#table_list_thead").height(); 
	var divBody=$(".c_div_table_list");
	var newDiv="<table class='c_table_list' border='1' id='"+tableId+"_new'><thead id='"+tableId+"_new'>";
	newDiv+=$("#"+tableId+"_thead").html()+"</thead></table>";
	divBody.before(newDiv);
	$("#"+tableId+"_new").width($("#"+tableId).width());
	$("#"+tableId).css("margin-top","-20px");
}
//kindEditor编辑器相关
function sys_ke_insertHtml(id, html) {
    KE.util.focus(id);
    KE.util.selection(id);
    var reg=new RegExp("http://.*?/");//匹配IP地址的正则表达式
    newhtml=html.replace(reg, "http://"+window.location.host+"/");
    KE.util.insertHtml(id, newhtml);
}
//kindEditor编辑器相关
function sys_ke_insertHtml_web(id, html) {
    KE.util.focus(id);
    KE.util.selection(id);
    var reg=new RegExp("<img src='http://.*?/");//匹配IP地址的正则表达式
    var key=window.location.host;
	eval('var mapjson='+"'"+sys_ke_ipmap+"'");
	fsHostTemp=mapjson.key;
	if((typeof(fsHostTemp)=="undefined")||fsHostTemp==""){
		fsHost="http://"+key;
	}else{
		fsHost="http://"+fsHost;
	}
    newhtml=html.replace(reg, "<img src='http://"+fsHost+":"+sys_fs_dk+"/");
    KE.util.insertHtml(id, newhtml);
}
function sys_ke_clearEditor(id) {
    KE.html(id, '');
}
//附件区域初始化
function sys_attachment_span_init(savename){
	if (pd_flag!=1){
		$(".sys_attachment_attachment").get(0).options.length = 0;
	}
	$(".fjid").val("");
	$(".fjid_Token").val("");
	$(".sys_attachment_xgbz").val("");
	$(".sys_attachment_xzck").show();
	if(savename!=undefined){
		if($("#"+savename).is(":visible")){
			$(".sys_attachment_wjsc").show();
		}else{
			$(".sys_attachment_wjsc").hide();
		}
	}
}
//附件区域文件上传按钮的权限控制，bind后由框架调用
function sys_attachment_auth(){
$(".sys_attachment_xgbz").each(function(){//用来获取数组
	nname=$(this).attr("name");
  	nname=nname.substring(19,nname.length);
  if($(this).val()=="1"){  
  	$("#sys_attachment_wjsc"+nname).show();  	
  }else if($(this).val()=="0"){  	
  	$("#sys_attachment_wjsc"+nname).hide();  
  }
});

}
//人员选择弹出窗口
function sys_selStaff_openWin(url){
   if(window.mk_dm&&mk_dm!=''&&url.indexOf("mk_dm=")<0){
    url=url+"&mk_dm="+mk_dm;
   }
   var iTop = (window.screen.availHeight-30-400)/2;        
   var iLeft = (window.screen.availWidth-10-340)/2; 
   window.open(url,'','height=400,innerHeight=400,width=340,innerWidth=340,top='+
   iTop+',left='+iLeft+',toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=no');

}
//弹出窗口
function sys_popup(url,width,height){
	if (width===undefined) {width=600;}
	if (height===undefined) {height=500;}
}
//删除元素所在的表格行,用于可增删行的表格
function sys_delTr(elem){
	//alert($(elem).parent().attr('id'));
	
	 var tr = elem.parentNode.parentNode;
	 var table = tr.parentNode;
	 var index = tr.rowIndex;
	 if($(table).find("tr").size()==2){
	 	alert("至少保留一条记录");
	 }else{
    	table.deleteRow(tr.rowIndex);
     }
    //alert(tr.innerHTML);
}
function sys_addTr(tableId){
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	trInfo=trInfo.replace(/{.*}/g,"_");//清空没有数据的单元格
	$("#"+tableId+" tbody tr:last").after("<tr>"+trInfo+"</tr>");
	$("#"+tableId+" tbody tr:last input[type='text']").each(function(){
		if($(this).val()=='_') $(this).val('');
	});
}
var sys_iframetab_info={};
$(function() {
	//保存tab页面初始信息
	$(".c_iframetab").each(function(){
		var _src={ict_src:$(this).attr("ict_src"),change:false};
		eval("sys_iframetab_info."+$(this).attr("id")+"=_src");		
	});
	//点击tab时
	$("#c_iframetab li a").click(function(){
		$(".c_current_iframetab").removeClass("c_current_iframetab");
		$(this).parent().addClass("c_current_iframetab");
		$(".c_iframetab").hide();
		$("#iframe_"+this.id).show();
		if (eval("sys_iframetab_info.iframe_"+this.id+".change")==true){
			eval("sys_iframetab_info.iframe_"+this.id+".change=false");
			$("#iframe_"+this.id).attr("src",eval("sys_iframetab_info.iframe_"+this.id+".src"));
			return false;
		}
	});
	//sys_showTab("tab_a,tab_b");
});
//需要更新iframe页面数据时调用,iframe.src中固定使用{guid}占位符
function sys_iframetab(guid){
	$.each(sys_iframetab_info,function(k,v){
		var _src=v.ict_src;
		var src2=_src.replace(/{guid}/,guid);
		if (_src!=src2) {v.change=true;v.src=src2;}
		else {v.change=false;}
	});
	//刷新当前页
	var _iframe="iframe_"+($(".c_current_iframetab a").attr("id"));
	$("#"+_iframe).attr("src",eval("sys_iframetab_info."+_iframe+".src"));
}
//需要显示哪些tab，格式，逗号分隔的字符串:tab_a,tab_c
function sys_showTab(tabidStr){
	$.each(sys_iframetab_info,function(k,v){
		if (tabidStr.indexOf(k.substring(7))>=0) $('#'+k.substring(7)).show();
		else $('#'+k.substring(7)).hide();
	});
}
//获取查询范围列表
function cxfw_list(tagname){
 sys_ajaxGet("/yhfw/default.do?method=getList_cxfw",{"tagname":tagname});
}
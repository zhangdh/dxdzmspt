var isExpand = 0;
$(function() {	 
	window.formFrame.location.href=webcontext+"/formuse.coffice?method=showFinalFormData&templateName="+$("#templateName").val()+"&formId="+$("#formId").val()+"&_time="+(new Date().getMilliseconds());
	$('#p').panel({ 
    	onExpand:function(){  
    	if(isExpand == 0){
        	$('#datalist').datagrid({
				method:'post',
				url:webcontext+'/wfuse.coffice?method=queryHis&id='+$("#id").val(),
				rownumbers:true,
				fitColumns: true,
				columns:[[
					{field:'clr',title:'处理人',width:$(this).width() * 0.1,align:'center'},
					{field:'dz',title:'动作',width:$(this).width() * 0.5,align:'center'},
					{field:'dzsj',title:'动作时间',width:$(this).width() * 0.1,align:'center'}
				]]
		 	});
		 isExpand=1;
		 }
    	}  
	});
	if($("#ifFj").val()=="1"){
		displayFj();
	}
});
function showFrame(){
		document.getElementById('formFrame').style.height=formFrame.document.body.scrollHeight;
		document.getElementById('formFrame').style.width=formFrame.document.body.scrollWidth;		
}	
function displayFj(){
		var hostIp = "http://"+window.location.host;
		var fjStr = $("#fjStr").val();
		var form_dir = $("#mk_dir").val();
		var infolj = form_dir.split(":")[1];
		fjStr = eval("("+fjStr+")");
		$.each(fjStr,function(k,v){
			var wjmc = "";
			var wjlj = "";
			$("#files").append("<a href='"+hostIp+infolj+v.wjlj+"' target='_blank' style='text-decoration:none;'>"+v.wjmc+"</a>&nbsp;&nbsp;");
	   });
}
function playLy(){
	var id = $("#_id",document.frames('formFrame').document).val();
	_Post("/thxx.doAction?method=queryLyPath","id="+id,function(returnData){
		var lyurl = returnData.lypath;
		if(lyurl == undefined){
			alert("此工单没有关联录音");
			return;
		}
    	window.open("play.jsp?url="+lyurl,"","height=100, width=500,toolbar=no, menubar=no, scrollbars=no, left=500,top=400,resizable=no,location=no, status=no,titlebar=no");
	})
}
function openFlow(){
	_Post("/todo.coffice?method=queryCurStep&entryId="+$("#id").val(),"",function(jsonData){
		var stepId = jsonData.stepId;
		window.open(webcontext+"/workflow/view.html?stepId="+stepId,"流程展示","width=950,height=450,scrollbars=yes,location=no");
	})
}
function openTimeLine(){
	window.open(webcontext+"/workflow/timeline.jsp?entryId="+$("#id").val(),"实例时间轴","width=950,height=300,scrollbars=yes,location=no");
}
function printForm(){
	window.open("./print.jsp");
}
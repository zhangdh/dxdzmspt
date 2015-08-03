var diffusion_num = 8;
var email_num = 8;
var undo_num = 8;
var unread_num = 8;
$(function(){
	initializeTable("diffusion_list",diffusion_num);
	initializeTable("undo_list",undo_num);
	initializeTable("unread_list",unread_num);
	initializeTable("email_list",email_num);
	initialize();
});
function initializeTable(tableId,rowNum){	
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	var i;
	for (i=0;i<rowNum;i=i+1){
		var newTr=trInfo;
		newTr=newTr.replace(/{.*?}/ig,"");
		newTr=newTr.replace(/【.*?】/ig,"");
		newTr=newTr.replace(/(.*?)/ig,"");
		$("#"+tableId+" tbody tr:eq("+i+")").after("<tr>"+newTr+"</tr>");
		
	}
}
function loadTable(tableId,jsonData){
	var tableData=jsonData.gridData.datalist.data;
	var trInfo=$("#"+tableId+" tbody tr:first").html();
	valiTable(tableId);
	var keepRow=$("#"+tableId+" tbody tr").length-1;//页面中保留行数
	var i;
	for (i=0;i<tableData.length;i=i+1){
		var rowJson=tableData[i];
		var newTr=trInfo;
		for(var j in rowJson){
			var jValue=eval("rowJson."+j);
			var re=eval("/{"+j+"}/ig");
			newTr=newTr.replace(re,jValue);
		}
		newTr=newTr.replace(/{.*}/ig,"");//清空没有数据的单元格
		$("#"+tableId+" tbody tr:eq("+i+")").after("<tr onclick=openMx('"+tableId+"',this)>"+newTr+"</tr>");
		if (i<keepRow)	{$("#"+tableId+" tbody tr:last").remove();}
	}
}
function valiTable(tableId){
	$("#"+tableId+" tbody tr:not(:first)").remove();
	if(tableId == "diffusion_list"){
		initializeTable(tableId,diffusion_num);
	}else if(tableId == "email_list"){
		initializeTable(tableId,email_num);
	}else if(tableId == "undo_list"){
		initializeTable(tableId,undo_num);
	}
}
function initialize(){
	_Post("/diffusion.coffice?method=listDesk&page_num="+diffusion_num,"",function(jsonData){
		loadTable("diffusion_list",jsonData);
	});
	_Post("/email.coffice?method=listDesk&page_num="+email_num,"",function(jsonData){
		loadTable("email_list",jsonData);
	});
	_Post("/undo.coffice?method=listDesk&page_num="+undo_num,"",function(jsonData){
		loadTable("undo_list",jsonData);
	});
	_Post("","",function(jsonData){
	
	});
}
function refresh(tableId){
	if(tableId == "diffusion"){
		_Post("/diffusion.coffice?method=listDesk&page_num="+diffusion_num,"",function(jsonData){
			loadTable("diffusion_list",jsonData);
		});
	}
	if(tableId == "undo"){
		_Post("/undo.coffice?method=listDesk&page_num="+undo_num,"",function(jsonData){
			loadTable("undo_list",jsonData);
		});
	}
	if(tableId == "email"){
		_Post("/email.coffice?method=listDesk&page_num="+email_num,"",function(jsonData){
			loadTable("email_list",jsonData);
		});
	}
}
function more(tableId){
	if(tableId == "diffusion"){
		window.open(webcontext+"/diffusion/List.jsp","_blank","height="+window.screen.height+",width="+window.screen.width+",title=信息发布,resizable=yes,scrollbars=yes");
	}else if(tableId == "undo"){
		window.open(webcontext+"/undo/undo.jsp","_blank","height="+window.screen.height+",width="+window.screen.width+",title=待办事宜,resizable=yes,scrollbars=yes");
	}else if(tableId == "email"){
		window.open(webcontext+"/email/sjx.jsp","_blank","height="+window.screen.height+",width="+window.screen.width+",title=站内邮件,resizable=yes,scrollbars=yes");
	}
}
function openMx(tableId,obj){
	 var mxId = $(obj).find("td:first").text();
	 if(tableId=="diffusion_list"){
	 	openwindow(webcontext+"/diffusion/DiffMx.jsp?guid="+mxId,'',852,600);
	 }else if(tableId=="email_list"){
	 	openwindow(webcontext+"/email/sjx.jsp",'',852,600);
	 }else if(tableId=="undo_list"){
	 	var arr = mxId.split(",");
	 	window.open(webcontext+'/formuse.coffice?method=showCurForm&id='+arr[0]+"&bllx="+arr[1]+"&tsgd="+arr[2],arr[0],"width=900,height="+(screen.height)+",toolbar=no,resizable=no,scrollbars=yes,status=yes");
	 	
	 	//var rv = window.showModalDialog(webcontext+'/formuse.coffice?method=showCurForm&id='+arr[0]+"&bjxz="+arr[1]+"&bllx="+arr[2]+"&ifcn="+arr[3],"","dialogWidth:"+(screen.width-120)+"px;dialogHeight:"+(screen.height)+"px;toolbar:no");
		//if(rv=="sucess"){
		//	refresh('undo');
		//}
	 }
	 
}
function refreshUnDo(){
	refresh('undo');
}
function openwindow(url,name,iWidth,iHeight)
{
   var url;                                 
   var name;                            
   var iWidth;                          
   var iHeight;                        
   var iTop = (window.screen.availHeight-30-iHeight)/2;        
   var iLeft = (window.screen.availWidth-10-iWidth)/2;         
   window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes');
}

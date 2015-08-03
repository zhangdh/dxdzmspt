var zNodes =  "";
var menStr = "";
$(function(){
	_Post("/menu.coffice?method=GetMenu","",function(jsonData){
		var  pmenu = new Array();
		zNodes = jsonData.menuStr;
		var obj = eval("("+zNodes+")");
		$.each(obj,function(k,v){
			if(v.pId == "0"){
				pmenu.push(k);
			}
		});
		for (var i = 0; i < pmenu.length; i++) {
			var pid = obj[pmenu[i]].id;
			var pname = obj[pmenu[i]].name;
			var pico = obj[pmenu[i]].qxico;
			menStr = menStr+"<div >"
	 						+"<div onclick=clickMenu(this,'"+pid+"') style='width:100%;border-bottom:1px solid #C0C0C0;height:24px;background:#CEE7F7'>"
	 						+"<img style='float:left;width:16px;height:16px;display:block;margin-top:4px;margin-left:15px;' src='images/"+pico+"'>"
	 						+"<span style='width:80%;height:100%;line-height:24px;font-family:Microsoft Yahei;font-size:12px;font-weight:800;color:#529ADE'>"
	 						+"&nbsp;&nbsp;"+pname+"</span><img id = 'im"+pid+"' src='images/arrow_down.png'>"
	 						+"</div><div id='"+pid+"' class='cmenu' display='none'>";
       		$.each(obj,function(k,v){
				if(v.pId == pid){
					menStr = menStr+"<div style='width:100%;height:25px;line-height:20px;' onMouseOver='mouseover(this)' onMouseOut='mouseout(this)' onclick=openUrl(this,'"+v.name+"','"+v.qxurl+"')>"
									+"<img style='float:left;width:16px;height:16px;display:block;margin-top:4px;margin-left:15px;' src='images/"+v.qxico+"'>"
									+"<span style='height:100%;line-height:25px;font-family:Microsoft Yahei;font-size:11px;color:#222222'>"
									+"&nbsp;&nbsp;"+v.name+"</span></div>";
									+"<span style='width:100%;height:2px;background:#000; background-image:url(images/dot.png);' />"
					
				}
			});
			menStr = menStr+"</div></div> ";
   		}
   		$("#menu").html(menStr);
	}); 
});
function mouseover(obj){
	 $(obj).css("background-color","#FFEC8B");
}
function mouseout(obj){
	$(obj).css("background-color","");
}
function openUrl(obj,name,qxurl){
	if(qxurl.indexOf("http") >-1){
		window.open(qxurl,'','menubar=no,toolbar=no,scrollbars=yes,location=no, resizable=yes,fullScreen=yes');
	}else{
		parent.frame_right.addTab(name,webcontext+qxurl);
	}
}
 function clickMenu(obj,pid){
  	$("#"+pid).toggle();
  	if($("#im"+pid).attr("src").indexOf("arrow_down.png")>-1){
  		$("#im"+pid).attr("src","images/arrow_up.png")
  	}else{
  		$("#im"+pid).attr("src","images/arrow_down.png")	
  	}
 }
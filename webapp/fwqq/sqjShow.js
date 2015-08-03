var cbjg="";
$(function() {
	sys_ajaxPost("/fwqq.coffice?method=queryAllLED","",function(json){
		$("#showContent").html(json.res);
	});
	
});
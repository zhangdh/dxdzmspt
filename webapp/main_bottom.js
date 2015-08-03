$(function() {
	getTitle();
});

function getTitle(){
	sys_ajaxPost("/publish/default.do?method=getTitle","",function(json){
		$("#showContent").html("<nobr>"+json.title+"</nobr>");
	});
}

function openWin(addr){
	var res=parent.window.showModalDialog(sys_ctx+"/"+addr,"_blank","dialogWidth:1000px;dialogHeight:650px;toolbar:no");
	if(res=="refresh"){
		getTitle();
	}
	
	
}
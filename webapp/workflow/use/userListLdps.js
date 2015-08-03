var yhsetting = {
	check: {
		enable: true,
		chkStyle: "checkbox",
		chkboxType: { "Y": "ps", "N": "s" }				
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
$(function(){
	_Post("/wfuse.coffice?method=showUserList","",function(jsonData){	
		loadForm("form_show",jsonData);
		getUserList();	
	});	
});
function getUserList(){
	var str = "sys_fsfw=userId&sys_fsfw_lb=userNames&show=yh&entryid="+$('#id').val()+"&stepid="+$('#nextstep').val()+"&formid="+$('#formid').val();
	str = str+"&workid="+workid;
	_Post("/wfuser.coffice?method=showUserTree",str,function(jsonData){		
		$.fn.zTree.init($("#usertree"),yhsetting,eval("("+jsonData.user_tree+")"));
	});
	//获取选择领导
	_Post("/ldps.coffice?method=getLd","",function(jsonData){		
		$.fn.zTree.init($("#ldtree"),yhsetting,eval("("+jsonData.ldtree+")"));
	});
}
function send(){
	var userId ="";
	var treeObj = $.fn.zTree.getZTreeObj("usertree");
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].lx_dm == "204"){
			userId = userId + nodes[i].id+",";
		}
	}
	var params="";	
	if(!userId){
		alert("请选择下一步处理人");
		return false;
	}				
	var selectUserCount = userId.split(",").length;
	if($("#workcondition").val()!="1"){//多人审核
		var amount = parseInt($("#amount").val());
		if(amount>selectUserCount){
			alert('选择人数要大于或等于'+$("#amount").val()+'人');
			return false;
		}
	}else{
		if(selectUserCount>2){
			alert('只能选择一个人');
			return false;
		}
	}
	params="&userIds="+userId.replace(/,/ig,"~");	
	var queryString = $("#form_show").serialize();
	queryString = queryString.replace("doid","doId");
	_Post("/wfuse.coffice?method=doWf",queryString+params,function(json){
		if(json.result=="success"){
			saveLd();
			alert("流程发送成功");
			window.returnValue   = "sucess";
			if($("#dbFlag").val()=="0"){
				$("#show_btns",window.dialogArguments.document).hide();
			}else{
				$("#show_btns",window.dialogArguments.document).hide();
			}
			self.close();
			
		}else{
			alert("流程发送失败");
		}
	});
}
function saveLd(){
	alert('12');
	var ldId ="";
	var treeObj = $.fn.zTree.getZTreeObj("ldtree");
	//svar treeObj = $.fn.zTree.getZTreeObj("usertree");
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].lx_dm == "204"){
			ldId = ldId + nodes[i].id+"~";
		}
	}
	if(ldId!=""){
		_Post("/ldps.coffice?method=saveLd","ldId="+ldId+"&formid="+$('#formid').val(),function(jsonData){		
			$.fn.zTree.init($("#ldtree"),yhsetting,eval("("+jsonData.ldtree+")"));
		});
	}
}
var selectedTab = 0;
var selectedYhid = 0;
var selectedJsid = 0;
var jssetting = {
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onCheck: JsOnCheck
		}
		};
var yhsetting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
				
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onCheck: YhOnCheck,
			beforeCheck: YhBeforeCheck
		}
	};
	var qxsetting = {
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "p", "N": "s" }
				
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
$(function(){
	_Post("/authority.coffice?method=getYhTree","",function(jsonData){
		$.fn.zTree.init($("#useresList"),yhsetting,eval("("+jsonData.org_tree+")"));	
	});
	_Post("/authority.coffice?method=getJsTree","",function(jsonData){
		$.fn.zTree.init($("#rolesList"),jssetting,eval("("+jsonData.org_tree+")"));	
	});
	_Post("/authority.coffice?method=getQxTree","",function(jsonData){
		$.fn.zTree.init($("#qxList"),qxsetting,eval("("+jsonData.org_tree+")"));	
	});
});

function JsOnCheck(event, treeId, treeNode) {
	selectedJsid = treeNode.id;
    _Post("/authority.coffice?method=getJsQx&jsid="+treeNode.id,"",function(jsonData){  		
			if(jsonData.result){
				qxTree = $.fn.zTree.getZTreeObj("qxList");
				qxTree.checkAllNodes(false);
				var qxArray = jsonData.qxStr.split(",");
				for(var i=0;i<qxArray.length-1;i++){
					var node = qxTree.getNodeByParam("id",qxArray[i], null);
					if(node != null){
							qxTree.checkNode(node, true, true);					
					}
				}
			}else{
				alertMsg(jsonData);
			}
	});
};
function YhOnCheck(event, treeId, treeNode) {
    //alert(treeNode.id + ", " + treeNode.name + "," + treeNode.checked);
    selectedYhid = treeNode.id;
    _Post("/authority.coffice?method=getYhQx&curyhid="+treeNode.id,"",function(jsonData){
			if(jsonData.result){
				qxTree = $.fn.zTree.getZTreeObj("qxList");
				qxTree.checkAllNodes(false);
				var qxArray = jsonData.qxStr.split(",");
				for(var i=0;i<qxArray.length-1;i++){
					var node = qxTree.getNodeByParam("id",qxArray[i], null);
					if(node != null){
							qxTree.checkNode(node, true, true);					
					}
				}
			}else{
				alertMsg(jsonData);
			}
	});
};
function YhBeforeCheck(treeId, treeNode, clickFlag){
	return (treeNode.lx_dm=="204");
}
function openTree(){
	var treeObj = $.fn.zTree.getZTreeObj("qxList");
	treeObj.expandAll(true);
}
function closeTree(){
	var treeObj = $.fn.zTree.getZTreeObj("qxList");
	treeObj.expandAll(false);
}
function saveQx(){
	var saveStr = "";
	var qxStr = "";
	var treeObj = $.fn.zTree.getZTreeObj("qxList");
	var nodes = treeObj.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		qxStr = qxStr + nodes[i].id+",";
	}
	saveStr = saveStr+"qxStr="+qxStr;
	if(selectedTab == 1){
		//用户授权
		saveStr = saveStr+"&selectedYhid="+selectedYhid;
		 _Post("/authority.coffice?method=saveYhQx",saveStr,function(jsonData){
		 	alertMes(jsonData);
		 });
	}else{
		//角色授权
		saveStr = saveStr+"&selectedJsid="+selectedJsid;
		 _Post("/authority.coffice?method=saveJsQx",saveStr,function(jsonData){
		 	alertMes(jsonData);
		 });
	}
}
function tabClick(){
	var curtab = $('#tabeId').tabs('getSelected');    
	if(selectedTab == $('#tabeId').tabs('getTabIndex',curtab)){
		return;
	}
	selectedTab = $('#tabeId').tabs('getTabIndex',curtab);
	qxTree = $.fn.zTree.getZTreeObj("qxList");
	qxTree.checkAllNodes(false);
}

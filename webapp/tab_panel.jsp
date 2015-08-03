<html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
	<head>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/js.jsp"%>
<script type="text/javascript" src="${ctx}/js/TabPanel/TabPanel.js"></script>
<script type="text/javascript" src="${ctx}/js/TabPanel/Fader.js"></script>
<script type="text/javascript" src="${ctx}/js/TabPanel/Math.uuid.js"></script>
<style>
html, body {
	width : 100%;
	height : 100%;
	padding : 0;
	margin : 0;
	overflow : hidden;
}
</style>
	</head>
<body>

<script type="text/javascript">

var tabpanel;
$(document).ready(function(){
	tabpanel = new TabPanel({
		renderTo:'tab',
		border:'none',
		active : 0,
		items : [{
			id:'desk',
			title:para['title'] != null ? para['title'] : '工具栏组件',
			html: '<iframe id="sxb" name="sxb" src="' + para['url'] + '" width="100%" height="100%"></iframe>',
			closable: true
		}]
	});
	//for(var i in para){
	//	alert(i + ":" + para[i]);
	//	addadd(i,para[i]);
	//}
});
var para ;
getArgs();
function addTab(title,_link){
	if(tabpanel.tabs.length == 1){
		if(tabpanel.tabpanel_tab_content.hasClass('display_none'))
			tabpanel.tabpanel_tab_content.removeClass('display_none');
		else
			tabpanel.setClosable(0,false);
	}
	tabpanel.addTab({id:title,title:title, html:'<iframe  id="addiframe" src="' + _link + '" width="100%" height="100%" frameborder="0"></iframe>'});
}
function getArgs(){
 para=new Array();
 search = self.location.href;
 search = search.split('?');
 if(search.length>1){
   var argList = search[1];
   argList = argList.split('&');
   for(var i=0; i<argList.length; i++){
     para[argList[i].split('=')[0]] = argList[i].split('=')[1];
   }
 }
}
</script>
<div id="tab" style="width:100%;height:100%;border:1px solid #C0C0C0;border-top:none;"></div>
</body>
</html>
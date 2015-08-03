<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ include file="/common/common.jsp" %>
<c:set var="webcontext" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<script src="authority.js" type="text/javascript"></script>
	<script>
		var webcontext = "${webcontext}"; 
	</script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
	<div region="west"  split="true" title="授权对象" style="width:300px;">
		<div id="tabeId" class="easyui-tabs" fit="true" plain="true" align=left style="width:100%;" onClick="tabClick()">
				<div title="角色列表" >
					<br/>
					<ul id="rolesList" name="rolesList" class="ztree"></ul>
				</div>
				<div title="人员列表" >
					<br/>
					<ul id="useresList" name="useresList" class="ztree"></ul>
				</div>
		</div>		
	</div>
	<div region="center" title="可授权对象" split="true" style="width:300px;" >
		<br/>&nbsp;
		<a href="javascript:void(0)" id="sb1" class="easyui-splitbutton"   onclick="javascript:openTree()">全部展开</a>
		<a href="javascript:void(0)" id="sb2" class="easyui-splitbutton"  onclick="javascript:closeTree()">全部折叠</a>
		<a href="javascript:void(0)" id="sb2" class="easyui-splitbutton"  onclick="javascript:saveQx()">保存授权</a>
		<ul id="qxList" name="qxList" class="ztree"></ul>
	</div>	
	
</div>
</body>
</html>
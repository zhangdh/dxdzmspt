<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ include file="/common/common.jsp" %>
<c:set var="webcontext" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="../js/kindeditor4/themes/default/default.css" />
	<link rel="stylesheet" type="text/css" href="../js/uploadify3.2/uploadify.css">
	<link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<script type="text/javascript" src="../js/uploadify3.2/jquery.uploadify.min.js" ></script>
	<script src="knowledgeManagerMl.js" type="text/javascript"></script>
	<script charset="utf-8" src="../js/kindeditor4/kindeditor-all.js"></script>
	<script charset="utf-8" src="../js/kindeditor4/lang/zh_CN.js"></script>
	<script type="text/javascript" src="../js/upload.js"></script>
	<script>
		var webcontext = "${webcontext}"; 
	</script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
	<div region="west"  split="true" title="知识目录" style="width:200px;">
		<ul id="kbList" name="kbList" class="ztree"></ul>
	</div>
	<div region="center"   class="easyui-panel" id="datalistdiv">
		<table id="datalist" style="height:auto"   title="知识目录" iconCls="icon-tip" singleSelect="true">		
			
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	</div>	
</div>
<div id="addkb" name="addkb" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="添加知识目录" style="top:20px;width:300px;height:140px;" iconCls="icon-tip">	
		<br/>
		<form id="form_show">
		<input type=hidden id="kid" name="kid"/>
		<table width="100%" class="table_show">
			<tr>
				<td>目录名称:</td>
				<td><input id="kname" name="kname" style="width:180px;height:20px"></td>
			</tr>
		</table>
		</form>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveKb()">保存</a>
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="updateKb()">修改</a>
		</div>
</div>
</body>
</html>
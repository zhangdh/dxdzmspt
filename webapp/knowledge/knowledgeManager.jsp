<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ include file="/common/common.jsp" %>
<c:set var="webcontext" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="../js/uploadify3.2/uploadify.css">
	<link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<script type="text/javascript" src="../js/uploadify3.2/jquery.uploadify.min.js" ></script> 
	<script type="text/javascript" src="../js/upload.js"></script>
	<script type="text/javascript" charset="utf-8" src="../js/kindeditor/kindeditor.js"></script>
	<script src="knowledgeManager.js" type="text/javascript"></script>
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
		<table id="datalist" style="height:auto"   title="知识条目" iconCls="icon-tip">		
			
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	</div>	
</div>
<div id="addkb" name="addkb" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="添加知识目录" style="top:20px;width:300px;height:160px;" iconCls="icon-tip">	
		<br/>
		<table width="100%" class="table_show">
			<tr>
				<td>目录名称:</td>
				<td><input id="kname" name="kname" style="width:180px;height:20px"></td>
			</tr>
			<tr>
				<td>上级目录:</td>
				<td><select id="sskid" name="sskid" style="width:180px;height:20px" required = true showName="上级目录"></select>
			</tr>
		</table>
		<br/>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveKb()">保存</a>
		</div>
</div>
<div id="addItem" name="addItem" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="添加知识条目" style="top:20px;width:850;height:600px;" iconCls="icon-tip">	
		<br/>
		<form id="form_item">
		<input type=hidden id="kid" name="kid"/>
		<table width="100%" class="table_show">
			<tr>
				<td style="width:80px;" align="right">所属知识条目:</td>
				<td style="width:85%"><select id="sskid1" name="sskid1" style="width:180px;height:20px" required = true showName="所属知识条目"></select>
			</tr>
			<tr>
				<td style="width:80px;" align="right">知识主题:</td>
				<td><input id="kbt" name="kbt" style="width:100%;height:20px"></textarea>
			</tr>
			<tr>
				<td style="width:80px;" align="right">知识内容:</td>
				<td>
				<textarea id="knr" name="knr" style="width:100%;height:400px;visibility:hidden;" ></textarea>
				</td>
			</tr>
			<tr>
				<td style="width:80px;" align="right">附件信息:</td>
				<td>
					<input type=hidden name="fjid" id="fjid">
			        <span id="files"></span>
			        <span id="attachment_span" name="attachment_span" >
			        	<a href='javascript:void(0)' id='sys_attachment_wjsc' class='easyui-linkbutton' onclick="openSc('')">文件上传</a>
			        </span>
				</td>
			</tr>
		</table>
		</form>
		<br/>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveItem()">保存</a>
		</div>
</div>
<!-- 上传附件div -->
	<div id="attachment_div" class="easyui-window" closed="true" title="上传文件" closed="true" collapsible="close" 
						minimizable="close" maximizable="close" modal="true"
						style="width:400px;height:250px;left:188px;top:5px" >
		<input type=hidden name="mk_dm" id = "mk_dm" value="181">
		<input type=hidden name="mk_mc" id = "mk_mc" value="knowledge">
		<input type=hidden name="mk_maxSize" id = "mk_maxSize">
		<input type=hidden name="mk_dir" id = "mk_dir">
		<div id="fileQueue"></div>		
		<div style="text-align:center;" id="test">
			<input type="file" name="uploadify" id="uploadify" style="display:none"/>
			<select size="5" id="attachments" name="attachments" style="width: 100%; font-family: 新宋体; font-size: 9pt; z-index:5;"></select>
			<br/>
			<a class="easyui-linkbutton" iconCls="icon-ok" id="scfj"  >开始上传</a>
			<a class="easyui-linkbutton" iconCls="icon-remove" id="delfj"" >删除上传</a>
		</div>
	</div>
</body>
</html>
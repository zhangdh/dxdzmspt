<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<html>
<head>
	<script src="manager.js" type="text/javascript"></script>
<title>信息发布列表</title>
</head>
<body>
<div id="datalistdiv" class="easyui-panel">
	<table id="datalist" style="height:auto"  title="数据列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
	</table>
	<div id="datadiv_page">
		 <%@ include file="/common/pagination.jsp" %>
	</div>
<div>
<div id="dataedit" class="easyui-window" closed="true" title="信息发布类别编辑" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close"
					style="top:120px;width:500px;height:160px;padding:10px;left:138px" iconCls="icon-tip">
	<form id="form_show">
		<input type=hidden id="xxlbdm" name="xxlbdm">
		<input type=hidden id="id" name="id">
		<table class="table_show">
			<tr>
				<td width="100px" align="right">管理菜单:</td>
				<td><input style="width:160px;" id="glcdmc" name="glcdmc"></td>
				<td width="80px" align="right">查看菜单:</td>
				<td><input style="width:140px;" id="ckcdmc" name="ckcdmc"></td>
			</tr>
			<tr>
				<td align="right">是否允许评论:</td>
				<td>
					<input type="radio" name="xxfb_yxpl" id="xxfb_yxpl" value="1" checked="checked">允许评论
           			<input type="radio" name="xxfb_yxpl" id="xxfb_yxpl" value="0">不允许评论
				</td>
				<td align="right">发布范围:</td>
				<td>
					<input type="radio" name="xxfb_fbfw" id="xxfb_fbfw" value="1" checked="checked">全体
           			<input type="radio" name="xxfb_fbfw" id="xxfb_fbfw" value="0">自定义
				</td>
			</tr>
		</table>
	</form>
	<div style="text-align:center;">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save()">保存</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
		</div>
</div>
</body>
</html>
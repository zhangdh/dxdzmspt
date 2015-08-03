<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<html>
  <head>
		<%@include file="/common/common.jsp"%>
		<script src="manager.js" type="text/javascript"></script>
		<title>流程管理</title>
  </head>
  <body>
  <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
	<form id="form_search">
		<label>流程名称:&nbsp;&nbsp;<input type="text" id="cx_mc" name="cx_mc" size="15" style="width:120px"></label>
		<label>状态:&nbsp;&nbsp;<select id="cx_zt" name="cx_zt" style="width:120px">
			<option value="">全部</option>
			<option value="0">启用</option>
			<option value="1">未启用</option>
		</select></label>
		<label>创建时间:&nbsp;&nbsp;<input id="cx_sjq" name="cx_sjq" size="15" onClick="WdatePicker()" style="width:120px"/></label>
		<label> -- <input id="cx_sjz" name="cx_sjz" size="15" onClick="WdatePicker()" style="width:120px"></label>
       	<label><input type= "button" class="btn_query" onclick="query()"  style="width:62px" value="搜索"/></label>
	</form>
   </div>
   <div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="流程列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	<div>
  </body>
</html>

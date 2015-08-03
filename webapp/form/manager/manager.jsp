<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<html>
  <head>
		<%@include file="/common/common.jsp"%>
		<%@ taglib uri="http://java.fckeditor.net" prefix="FCK"%>
		<script src="manager.js" type="text/javascript"></script>
		<title>流程管理</title>
  </head>
  <body>
  <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
	<form id="form_search">
		<label>名称:&nbsp;&nbsp;<input type="text" id="cx_mc" name="cx_mc" size="15" style="width:120px"></label>
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
	<div id="dataedit" class="easyui-window" closed="true" title="流程编辑" iconCls="icon-tip" style="left:20px;top:20px">
	<form id="form_show" name="form_show" action="${webcontext}/formmanager.coffice?method=saveForm" method="post">
        <input type="hidden" name="tableid" id="tableid"/>
        <FCK:editor instanceName="EditorDefault" basePath="/js/fckeditor">
        </FCK:editor>
    </form>
	</div>
  </body>
</html>

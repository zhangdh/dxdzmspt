<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="xzblqk.js" type="text/javascript"></script>
    <title>承办单位办理情况统计</title>
</head>
<body id="bodyid">
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>统计类别：
        		<select id="cx_lx" name="cx_lx" style="width:150px">
        			
        		</select>
			</label>
        	<label>转办日期：<input id="cx_sjq" class="WDate" name="cx_sjq" size="15" onClick="WdatePicker()"/></label>
          		  	&nbsp;至&nbsp;
			<label><input type="text" id="cx_sjz" class="WDate" name="cx_sjz" size="15" onClick="WdatePicker()"></label>
			<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="统计结果列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
	<div>
</body>
<!-- InstanceEnd --></html>

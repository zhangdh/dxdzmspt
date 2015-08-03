<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="compare.js" type="text/javascript"></script>
    <title>承办单位办理情况统计</title>
</head>
<body id="bodyid">
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>统计类型：
        		<input type=radio name="tjlx" value="nrfl" checked>内容类别
        		<input type=radio name="tjlx" value="xzfl">性质分类
        		<input type=radio name="tjlx" value="xxly">信息来源
			</label>
        		<label>来电日期段1：<input id="cx_sjq1" class="WDate" name="cx_sjq1" size="15" required = true onClick="WdatePicker()"/ showName="时间段一起"></label>
          		  	&nbsp;至&nbsp;
				<label><input type="text" id="cx_sjz1" class="WDate" name="cx_sjz1" size="15" required = true onClick="WdatePicker()" showName="时间段一止"></label>
				<label>来电日期段1：<input id="cx_sjq2" class="WDate" name="cx_sjq2" size="15" required = true onClick="WdatePicker()"/ showName="时间段二起"></label>
          		  	&nbsp;至&nbsp;
				<label><input type="text" id="cx_sjz2" class="WDate" name="cx_sjz2" size="15" required = true onClick="WdatePicker()" showName="时间段二止"></label>
				<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
				<label><input type= "button" class="btn_query" onclick="queryChart()" style="width:100px" value="图标显示"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="统计结果列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
	<div>
</body>
<!-- InstanceEnd --></html>

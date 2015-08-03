<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="fwqqTj.js" type="text/javascript"></script>
    <title>服务请求统计</title>
</head>
<body id="bodyid">
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>统计类型：
				<input type="radio"  name="tjtj"  value="tjzt" checked/>状态
				<input type="radio"  name="tjtj"  value="tjzx"/>坐席人员
				<input type="radio"  name="tjtj"  value="tjnrlb" />内容类别
				<input type="radio"  name="tjtj"  value="tjxxly" />信息来源
				<input type="radio"  name="tjtj"  value="tjxzfl" />性质分类
			</label>
        		<label>来电日期：<input id="cx_sjq" class="WDate" name="cx_sjq" size="15" onClick="WdatePicker()"/></label>
          		  	&nbsp;至&nbsp;
				<label><input type="text" id="cx_sjz" class="WDate" name="cx_sjz" size="15" onClick="WdatePicker()"></label>
				<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
				<label><input type= "button" class="btn_query" onclick="queryChart()" style="width:90px" value="图标展示"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="统计结果列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
	<div>
</body>
<!-- InstanceEnd --></html>

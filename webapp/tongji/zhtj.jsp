<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="zhtj.js" type="text/javascript"></script>
    <title>受理情况综合统计</title>
</head>
<body id="bodyid">
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>统计类型：
        		<input type=radio name="tjlx" value="blzt" checked>办理状态
        		<input type=radio name="tjlx" value="xxly">信息来源
        		<input type=radio name="tjlx" value="xzfl">性质分类
        		<input type=radio name="tjlx" value="nrfl">内容类别
			</label>
        		<label>来电日期：<input id="cx_sjq" class="WDate" name="cx_sjq" size="15" onClick="WdatePicker()"/></label>
          		  	&nbsp;至&nbsp;
				<label><input type="text" id="cx_sjz" class="WDate" name="cx_sjz" size="15" onClick="WdatePicker()"></label>
			<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
			<label><input type= "button" class="btn_query" onclick="queryChart()" style="width:90px" value="图标展现"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="统计结果列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
	<div>
</body>
<!-- InstanceEnd --></html>

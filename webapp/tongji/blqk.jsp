<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="blqk.js" type="text/javascript"></script>
    <title>承办单位办理情况统计</title>
</head>
<body id="bodyid">
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>统计单位：
        		<select id="cx_bm" name="cx_bm" style="width:150px">
        			<option value="">全部</option>
        			<option value="321_64650">市本级</option>
        			<option value="321_64651">安定区</option>
        			<option value="321_64652">临洮县</option>
        			<option value="321_64654">岷县</option>
        			<option value="321_64656">渭源县</option>
        			<option value="321_64657">漳县</option>
        			<option value="321_64653">陇西县</option>
        			<option value="321_64655">通渭县</option>
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

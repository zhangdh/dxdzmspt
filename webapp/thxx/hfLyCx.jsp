<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>
	<script src="hfLyCx.js" type="text/javascript"></script>
    <title>回访通话信息查询</title>
</head>

<body id="bodyid">
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>回访号码：<input type="text" id="cx_hm" name="cx_hm" size="15"></label>
        	<label>回访单号：<input type="text" id="cx_slbh" name="cx_slbh" size="15"></label>		
        	 回访时间：<input id="cx_StartDate" name="cx_StartDate" style="width: 15%" class="WDate" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				-
			<input id="cx_EndDate" name="cx_EndDate" style="width: 15%" class="WDate" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly">            
			<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="录音列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	<div>
</body>
<!-- InstanceEnd --></html>

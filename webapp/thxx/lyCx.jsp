<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<html> 
<head>

	<script src="lyCx.js" type="text/javascript"></script>
    <title>通话信息查询</title>
</head>

<body id="bodyid">
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>主叫号码：<input type="text" id="cx_caller" name="cx_caller" style="WIDTH: 120px; HEIGHT: 20px; "></label>
        	<label>被叫号码：<input type="text" id="cx_called" name="cx_called" style="WIDTH: 120px; HEIGHT: 20px; "></label>		
        	<label>通话类型：<select id="calltype" name="calltype"><option value="">请选择</option><option value="1">呼入</option><option value="0">呼出</option></select></label>
        	 通话时间：<input id="cx_StartDate" name="cx_StartDate" style="width: 15%" class="WDate" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
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
</html>

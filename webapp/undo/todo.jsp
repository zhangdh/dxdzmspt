<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html>
	<head>
		<script src="todo.js" type="text/javascript"></script>
		<title>待办列表</title>
	</head>
	<body id="bodyid">	
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
			<label>收到日期：
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_ldrqq" id="cx_ldrqq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
			--
			<label>
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_ldrqz" id="cx_ldrqz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
			<label>
				内容分类：<select id="cx_nrfl" name = "cx_nrfl" style="width:120px;height:20px"></select>
			</label>
			<label>
				性质分类：<select id="cx_xzfl" name = "cx_xzfl" style="width:120px;height:20px"></select>
			</label>
			<label>
				信息来源：<select id="cx_xxly" name = "cx_xxly" style="width:120px;height:20px"></select>
			</label>
			<label>
				工单编号：<input id="cx_slbh" name = "cx_slbh" style="width:160px;height:20px"></input>
			</label>
			<!-- 
			<label>
				工单标题：<input id="cx_bt" name = "cx_bt" style="width:160px;height:20px"></input>
			</label>
			 -->
			<label>
				来电号码：<input id="cx_ldhm" name = "cx_ldhm" style="width:160px;height:20px"></input>
			</label>
			<label>
				来电人姓名：<input id="cx_ldhxm" name = "cx_ldhxm" style="width:160px;height:20px"></input>
			</label>
			<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
		</form>
	</div>	
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="参数列表"  singleSelect="true"
				idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	<div>
	</body>
</html>

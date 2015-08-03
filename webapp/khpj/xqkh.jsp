<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.coffice.util.cache.Cache"%>  
<html>
 <head>
   	<%@ include file="/common/common.jsp" %>
	<script src="xqkh.js" type="text/javascript"></script>
	<title>转办查询</title>
  </head>
  <body id="bodyid">
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
			<label>来电日期日期：
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cxq" id="cxq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
				--
			<label>
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cxz" id="cxz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
			<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
	
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="查询结果"  singleSelect="true"
				idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 
		</div>
	<div>
		 
  </body>
</html>

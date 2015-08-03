<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<%@ include file="/common/common.jsp" %>
		<script src="List.js" type="text/javascript"></script>
		<title>信息发布查看</title>
	</head>
	<body>
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        <label>
        <SELECT name="cx_gq" id="cx_gq"  style="WIDTH: 120px; HEIGHT: 20px; ">
          <option value="">--信息状态--</option>
          <option value="0">过期信息</option>
          <option value="1">未过期信息</option>
          <option value="2">一直有效</option>
        </SELECT>
        <select name="lb_dm" id="lb_dm" style="width:120px;heigth:20px"></select>
       	</label>
        <label>主题：<input type="text" id="cx_xxzt" name="cx_xxzt" style="WIDTH: 120px; HEIGHT: 20px; "></label>		  
		<label>创建日期：
		<input type="text" id="cx_rq1" name="cx_rq1" style="WIDTH: 120px; HEIGHT: 20px; " onClick="WdatePicker()"></label>
		<label>&nbsp;至&nbsp;</label>
		<label><input  type="text" id="cx_rq2" name="cx_rq2" style="WIDTH: 120px; HEIGHT: 20px; " onClick="WdatePicker()" ></label>
       	<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="已发送列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	<div>
	</body>
</htm>
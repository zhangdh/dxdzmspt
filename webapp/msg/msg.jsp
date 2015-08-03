<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>留言反馈列表</title>

<% 
	String ywid = request.getParameter("ywid");
	
%>
<script src="msg.js" type="text/javascript"></script>
<script>
	var ywid = <%=ywid%>;	
</script>
</head>
<body>
<div id="datalistdiv" class="easyui-panel">
	<table id="datalist" style="height:auto"  title="数据列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
	</table>
	<div id="datadiv_page">
		 <%@ include file="/common/pagination.jsp" %>
	</div>
<div>
<div id="msg_div" class="easyui-window" closed="true" title="留言内容" closed="true" collapsible="close" 
						minimizable="close" maximizable="close" modal="true"
						style="width:450px;height:180px;left:188px;top:5px">
	<form id="form_show">
	<table>
		<tr>
			<td style="width:450px;">
				<textarea id="msgnr" name="msgnr" rows="5" style="width:100%"></textarea>
			</td>
		</tr>
	</table>
	</form>
	<div align="center" id="btns_div">
		<a href='javascript:void(0)' id='btn_save' class='easyui-linkbutton' onclick='save()'>保存留言</a>
	</div>
	</div>
</body>
</html>
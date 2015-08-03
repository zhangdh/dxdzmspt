<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script src="role.js" type="text/javascript"></script>
</head>
<body>
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
	<form id="form_search">
			<label>名称：<input type="text" name="cx_mc"  id="cx_mc"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	    <label><input type= "button" class="btn_query" onclick="query()"  style="width:62px" value="搜索"/></label>
	</form>
</div>
<div id="datalistdiv" class="easyui-panel">
	<table id="datalist" style="height:auto"  title="数据列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
	</table>
	<div id="datadiv_page">
		 <%@ include file="/common/pagination.jsp" %>
	</div>
<div>
<div id="dataedit" class="easyui-window" closed="true" title="用户组编辑" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close"
					style="top:120px;width:460px;height:190px;padding:10px;left:138px" iconCls="icon-tip">
		<form id="form_show">
			<input type=hidden id="js_id" name="js_id">
			<table class="table_show">
				<tr>
					<td width="80px" >用户组名称：</td>
					<td><input type="text" style="WIDTH: 300px; HEIGHT: 25px" name="mc" id="mc"></td>
				</tr>
				<tr>
					<td>用户组说明：</td>
					<td>
					<textarea id="bz" name="bz" style="width:300px;height:50px;"></textarea>
					</td>
				</tr>
			</table>
		</form>

		<div style="text-align:center;">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save()">保存</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
		</div>
</div>
</body>
</html>
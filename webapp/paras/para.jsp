<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script src="para.js" type="text/javascript"></script>
</head>
<body>
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件"  collapsible="true">
	<form id="form_search">
		<label>参数名称：<input type="text" name="cx_mc"  id="cx_mc"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	<label>参数说明：<input type="text" name="cx_sm"  id="cx_sm"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	<label>参数值：<input type="text" name="cx_csz"  id="cx_csz"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="查询"/></label>
	</form>
</div>
	
<div id="datalistdiv"  >
	<table id="datalist" style="height:auto"  title="参数列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
	</table>
	<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
	</div>
<div>
<div id="dataedit" class="easyui-window" closed="true" title="运行参数编辑" style="top:120px;width:460px;height:200px;padding:10px;" iconCls="icon-tip"
																collapsible="close" minimizable="close" maximizable="close">
		<form id="form_show">
			<input type=hidden id="id" name="id">
			<table class="table_show">
				<tr>
					<td width="80px" >参数名称：</td>
					<td><input type="text" style="WIDTH: 300px; HEIGHT: 25px" name="csdm" id="csdm"></td>
				</tr>
				<tr>
					<td>参数值： </td>
					<td><input type="text" style="WIDTH: 300px; HEIGHT: 25px" name="csz" id="csz" class="easyui-validatebox" missingMessage="xxx必须填写" ></td>
				</tr>
				<tr>
					<td>参数说明：</td>
					<td>
					<textarea id="cssm" name="cssm" style="width:300px;height:25px;" readonly="true"></textarea>
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
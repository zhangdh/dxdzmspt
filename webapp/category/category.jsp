<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script src="category.js" type="text/javascript"></script>
	<script>
		
	</script>
</head>
<body>
<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
	<form id="form_search">
	<label>维护类别：<select  class="c_select" name="dm_lb" id="dm_lb" style="width:120px;" ></select>
	</label>
    <label><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="query()">搜索</a></label>
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
<div id="dataedit"  style="top:120px;left:200px;width:460px;height:200px;padding:10px;" 
					class="easyui-window" closed="true" title="类别信息编辑" 
					collapsible="close" minimizable="close" maximizable="close" modal="true" iconCls="icon-tip">
		<form id="form_show">
			<input type=hidden id="dm" name="dm">
			<input type=hidden id="lb_dm" name="lb_dm">
			<table class="table_show">
				<tr>
					<td width="80px" >类别名称：</td>
					<td><input type="text" style="WIDTH: 300px; HEIGHT: 25px" name="mc" id="mc"></td>
				</tr>
				<tr>
					<td>类别说明：</td>
					<td>
					<textarea id="sm" name="sm" style="width:300px;height:60px;" ></textarea>
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
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<style type="text/css">
    .uploadify-button {
        background-color: transparent;
        border: none;
        padding: 0;
    }
    .uploadify:hover .uploadify-button {
        background-color: transparent;
    }
</style>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../js/kindeditor4/themes/default/default.css" />
    <link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
    <link rel="stylesheet" type="text/css" href="../js/uploadify3.2/uploadify.css">
    <script charset="utf-8" src="../js/kindeditor4/kindeditor-min.js"></script>
	<script charset="utf-8" src="../js/kindeditor4/lang/zh_CN.js"></script>
	<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="../js/uploadify3.2/jquery.uploadify.min.js" ></script>
	<script type="text/javascript" src="../js/upload.js"></script>
	<script src="sjx.js" type="text/javascript"></script>
</head>
<body>
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
			<label>接收时间：<input  name="cx_rq1" id="cx_rq1" style="width:120px" onClick="WdatePicker()"/></label><label>&nbsp;至&nbsp;<input  name="cx_rq2" id="cx_rq2" style="width:120px" onClick="WdatePicker()"/></label>
    		<label>
				阅读状态:
				<select style="width:120px" id="ydzt" name="ydzt">
					<option value="">----全部----</option>
					<option value="0">----未读----</option>
					<option value="1">----已读----</option>
				</select>
			</label>
    		<label><input type= "button" class="btn_query" onclick="query()"  style="width:62px" value="搜索"/></label>		
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="邮件列表"  
							 singleSelect="true" idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	</div>
	<div id="dataedit"  class="easyui-window"  closed="true" collapsible="close" minimizable="close" maximizable="close" 
						  title="邮件编辑" iconCls="icon-tip" style="width:700px;height:450px;padding:10px;left:138px;top:2px">
		<form id="form_show">
        <input type="hidden" class="ryxz_value"  name="sjr_value" id="sjr_value" check="notBlank" required="true" showName="收件人">
        <input type="hidden" id="guid" name="guid">
        <input type="hidden" id="yhxm" name="yhxm">
		<table width="800px" border="0" cellspacing="0" cellpadding="0" class="table_show">
	          
	            <tr style="height:30px">
	            	<td ><label>收 件 人&nbsp;</label></td>
	                <td colspan="3">
	               		<SELECT name="sjr"  class="ryxz"  id="sjr"  style="WIDTH: 150px; HEIGHT: 20px; "></SELECT>
	                		                         
	                </td>
	         	</tr>										
	            <tr style="height:30px">
	                  <td ><label>主题&nbsp;</label></td>
	                  <td colspan="3"><input class="biinput" maxlength="130" size="15"  check="notBlank" required="true" showName="报告主题" style="WIDTH: 600px; HEIGHT: 20px" name="yjzt" id="yjzt"/></td>
				</tr>
				<tr style="height:30px">
					  <td ><label>附件信息&nbsp;</label></td>
			          <td >
			          		<input type=hidden name="fjid" id="fjid">
			          		<span id="files"></span>
			          </td>
			          <td colspan="2"></td>
				</tr>
				<tr style="height:30px">
					  <td colspan="4"><textarea id="yjnr" name="yjnr" style="width:600px;height:230px;visibility:hidden;"></textarea></td>
				</tr>
            </table>
        <!-- InstanceEndEditable -->
        <!--数据明细区止-->
    </form>
    <div style="text-align:center;" id="operdiv">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save()">存为草稿</a>
				<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="send()">直接发送</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消写信</a>
	</div>
	</div>
</body>
</html>
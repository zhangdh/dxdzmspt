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
	<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
	<script type="text/javascript" src="../js/uploadify3.2/jquery.uploadify.min.js" ></script>
	<script type="text/javascript" src="../js/upload.js"></script>
	<script src="fjx.js" type="text/javascript"></script>
</head>
<body>
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
			<label>发送时间：<input  name="cx_rq1" id="cx_rq1" style="width:120px" onClick="WdatePicker()"/></label><label>&nbsp;至&nbsp;<input  name="cx_rq2" id="cx_rq2" style="width:120px" onClick="WdatePicker()"/></label>
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
						  title="邮件编辑" iconCls="icon-tip" style="width:960px;height:500px;padding:10px;left:138px;top:2px">
		<form id="form_show">
        <input type="hidden" class="ryxz_value"  name="sjr_value" id="sjr_value" check="notBlank" required="true" showName="收件人">
        <input type="hidden" id="guid" name="guid">
        <input type="hidden" id="yhxm" name="yhxm">
		<table width="900px" border="0" cellspacing="0" cellpadding="0" class="table_show">	          
	            <tr style="height:30px">
	            	<td style="width:80px"><label>收 件 人&nbsp;</label></td>
	                <td >
	               		<SELECT name="sjr"  class="ryxz"  id="sjr"  style="WIDTH: 150px; HEIGHT: 20px; "></SELECT>                
	                	<label><a class="easyui-linkbutton" iconCls="icon-help" href="javascript:void(0)" id="btn_sjr" >收件人</a></label>      
	                </td>
	         	</tr>										
	            <tr style="height:30px">
	                  <td ><label>主题&nbsp;</label></td>
	                  <td ><input class="biinput" maxlength="130" size="15"  check="notBlank" required="true" showName="邮件主题" style="WIDTH:100%; HEIGHT: 20px" name="yjzt" id="yjzt"/></td>
				</tr>
				<tr style="height:30px">
					  <td ><label>附件信息&nbsp;</label></td>
			          <td >
			          		<input type=hidden name="fjid" id="fjid">
			          		<span id="files"></span>
			          		<span id="attachment_span" name="attachment_span" >
			        			<a href='javascript:void(0)' id='sys_attachment_wjsc' class='easyui-linkbutton' onclick="openSc('')">文件上传</a>
			        		</span>
			          </td>			         
				</tr>
				<tr>
					<td></td>
					<td ><textarea id="yjnr" name="yjnr" style="WIDTH:82 0px;height:300px;visibility:hidden;"></textarea></td>
				</tr>
            </table>
        <!-- InstanceEndEditable -->
        <!--数据明细区止-->
    </form>
    <div style="text-align:center;" id="operdiv">
    		<div id="operbtns">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save()">存为草稿</a>
				<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="send()">直接发送</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消写信</a>
			</div>
			<div id="cgbtns">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="saveCg()">保存草稿</a>
				<a class="easyui-linkbutton" iconCls="icon-add" href="javascript:void(0)" onclick="sendCg()">发送草稿</a>
			</div>
	</div>
	</div>
	<!-- 选择用户 -->
	<div id="selectYh" class="easyui-window" closed="true" title="选择收件人" closed="true" collapsible="close" 
						minimizable="close" maximizable="close"  modal="true"
						style="width:265px;height:380px; left:158px;top:3px" inline="true">
		<!-- <ul id="orgtree" name="orgtree" class="ztree"></ul>
		<br/>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="queding()">确定</a>
		</div>
		 -->
		<div class="easyui-layout" style="width:100%;height:300px;">
				<div region="west"   style="width:250px;">
					<ul id="orgtree" name="orgtree" class="ztree"></ul>
				</div>
				<div region="center"  id="yxz">
					
				</div>
			</div>
			<br/>
			<div style="text-align:center;">
				<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="queding()">确定</a>
			</div>
	</div>
	<br/>
	
	</div>
	<!-- 上传附件div -->
	<div id="attachment_div" class="easyui-window" closed="true" title="上传文件" closed="true" collapsible="close" 
						minimizable="close" maximizable="close" modal="true"
						style="width:400px;height:250px;left:188px;top:5px" inline="true">
		<input type=hidden name="mk_dm" id = "mk_dm" value="120">
		<input type=hidden name="mk_mc" id = "mk_mc" value="email">
		<input type=hidden name="mk_maxSize" id = "mk_maxSize">
		<input type=hidden name="mk_dir" id = "mk_dir">
		<div id="fileQueue"></div>		
		<div style="text-align:center;" id="test">
			<input type="file" name="uploadify" id="uploadify" style="display:none"/>
			<select size="5" id="attachments" name="attachments" style="width: 100%; font-family: 新宋体; font-size: 9pt; z-index:5;"></select>
			<br/>
			<a class="easyui-linkbutton" iconCls="icon-ok" id="scfj"  >开始上传</a>
			<a class="easyui-linkbutton" iconCls="icon-remove" id="delfj"" >删除上传</a>
		</div>
	</div>
</body>
</html>
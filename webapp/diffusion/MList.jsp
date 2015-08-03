<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
	<head>
		<%@ include file="/common/common.jsp" %>
		<link rel="stylesheet" href="../js/kindeditor4/themes/default/default.css" />
    	<link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
    	<link rel="stylesheet" type="text/css" href="../js/uploadify3.2/uploadify.css">
    	<script charset="utf-8" src="../js/kindeditor4/kindeditor-all-min.js"></script>
		<script charset="utf-8" src="../js/kindeditor4/lang/zh_CN.js"></script>
		<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
		<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
		<script type="text/javascript" src="../js/uploadify3.2/jquery.uploadify.min.js" ></script>
		<script type="text/javascript" src="../js/upload.js"></script>
		<script src="MList.js" type="text/javascript"></script>
		<title>信息发布</title>
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
	<div id="dataedit" class="easyui-window" closed="true" title="信息编辑" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close" modal="true"
					style="top:80px;width:900px;height:550px;padding:10px;left:100px" iconCls="icon-tip">
		<form id="form_show">
			  <input type="hidden" id="guid" name="guid">
			<table class="table_show" style="width:100%">
				<tr>
					<td style="width:80px;align=right;">信息类别:</td>
					<td>
						<select id="gglb" name="gglb"  style="width:120px" required = "true"></select>
					</td>
				</tr>
				<tr>
					<td style="width:80px;align=right;">发布范围:</td>
					<td>
						<input type="hidden" id="fbfw_value" name="fbfw_value">
						<select id="fbfw" name="fbfw" style="width:120px">
						</select>
						<label id="selectId" style="display:none;"><a class="easyui-linkbutton" iconCls="icon-help" href="javascript:void(0)" onclick="selectMan();">收件人</a></label>
						<input type=checkbox name="fsfwbz" id="fsfwbz" value="1" onclick ="s(this.checked);" checked="true">全部人员
					</td>
				</tr>
				<tr>
					<td style="width:80px;align=right;">有效时间:</td>
					<td>
						<label><input  type="text" id="yxqq" name="yxqq" style="WIDTH: 140px; HEIGHT: 20px; " onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" required = "true"></label>
						--
						<label><input  type="text" id="yxqz" name="yxqz" style="WIDTH: 140px; HEIGHT: 20px; " onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="2099-12-31 23:59:59" required = "true"></label>
						<input type=checkbox name="yxsjbz" id="yxsjbz" value="1" checked="true" onclick ="st(this.checked);">一直有效
					</td>
				</tr>
				<tr>
					<td style="width:80px;align=right;">信息主题:</td>
					<td>
						<input id="xxzt" name="xxzt" maxlength=30 style="width:100%" required = "true">
					</td>
				</tr>
				<tr>
					<td style="width:80px;align=right;">附件信息:</td>
			        <td >
			          	<input type=hidden name="fjid" id="fjid">
			          	<span id="files"></span>
			          	<span id="attachment_span" name="attachment_span" >
			        		<a href='javascript:void(0)' id='sys_attachment_wjsc' class='easyui-linkbutton' onclick="openSc('')">文件上传</a>
			        	</span>
			         </td>
				</tr>
				<tr>
					<td>信息内容:</td>
					<td>
						<textarea id="xxnr" name="xxnr" style="width:100%;height:300px;visibility:hidden;" ></textarea>
					</td>
				</tr>
			</table>
		</form>

		<div style="text-align:center;" id="operdiv">
			<div id="operdiv1">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save()">暂时保存</a>
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="send()">发表</a>
				<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel()">取消</a>
			</div>
			<div id="operdiv2">
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save1()">暂时保存</a>
				<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="send1()">发表</a>
			</div>
		</div>
	</div>
	
	<!-- 选择用户 -->
	<div id="selectYh" class="easyui-window" closed="true" title="选择收件人" closed="true" collapsible="close" 
						minimizable="close" maximizable="close"  modal="true"
						style="width:265px;height:380px; left:158px;top:3px">
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
	<!-- 上传附件div -->
	<div id="attachment_div" class="easyui-window" closed="true" title="上传文件" closed="true" collapsible="close" 
						minimizable="close" maximizable="close" modal="true"
						style="width:400px;height:250px;left:188px;top:5px" >
		<input type=hidden name="mk_dm" id = "mk_dm" value="105">
		<input type=hidden name="mk_mc" id = "mk_mc" value="diffusion">
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
</htm>
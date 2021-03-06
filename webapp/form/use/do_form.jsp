<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${webcontext}/js/uploadify3.2/uploadify.css">
<script type="text/javascript" src="${webcontext}/js/upload.js"></script>
<script type="text/javascript" src="${webcontext}/js/msg.js"></script>
<script type="text/javascript" src="${webcontext}/js/uploadify3.2/jquery.uploadify.min.js" ></script>
<script src="${webcontext}/form/use/do_form.js" type="text/javascript"></script>
<title>事项处理</title>
</head>
<body id="bodyid">
	<input type="hidden" id="id" name="id" value="${id}" />
	<input type="hidden" id="stepId" name="stepId" value="${stepId}" />
	<input type="hidden" id="username"  name="username" value="${username}" />
	<input type="hidden" id="steptype" name="steptype" value="${steptype}" />
	<input type="hidden" id="unid" name="unid" value="${unid}" />
	<input type="hidden" id="operteName" name="operteName" value=""/>
	<input type="hidden" id="lclb_dm" name="lclb_dm" value="${lclb_dm}"/>
	<input type="hidden" id="mk_dm" name="mk_dm" value="${mk_dm}"/>
	<input type="hidden" id="mkurllb" name="mkurllb" value="${mkurllb}"/>
	<input type="hidden" id="businessid" name="businessid" value="${businessid}"/>
	<input type="hidden" id="workid" name="workid" value="${workid}"/>
	<input type="hidden" id="dbFlag" name="dbFlag" value="${dbFlag}"/>
	<input type="hidden" id="kjlx" name="kjlx" value="${kjlx}"/>
	<input type="hidden" id="xm" name="xm" value="${xm}"/>
	<input type="hidden" id="formfuncname" name="formfuncname" value="${formfuncname}"/>
	<input type="hidden" id="undo_title" name="undo_title" value="${undo_title}"/>
	<div id="div_show" class="c_table_bar_content">
		<div  id="show_btns" align="right" style="position:absolute;width:100%;height:30px;line-height:30px;background:#E7EEFE;padding-top:3px;padding-right:3px;">
		 	<c:forEach var="funcList" items="${funcList}">
				<c:if test="${funcList.func_id=='btn_fj'}">
					<c:set var="isFjid" value="1"></c:set>
					<c:set var="isFjQx1" value="${isFjQx}"></c:set>
					<input type=hidden name="fjid" id="fjid">
			        <span id="files"></span>
			        <span id="attachment_span" name="attachment_span" >
			        	<!--<a href='javascript:void(0)' id='sys_attachment_wjsc' class='easyui-linkbutton' onclick="openSc('form')">文件上传</a> -->
			        	<label><input type= "button" id='sys_attachment_wjsc' class="btn_query" onclick="openSc('form')"  style="width:80px" value="文件上传"/></label>
			        </span>
				</c:if>				
				<!-- 
				<c:if test="${funcList.func_id=='btn_ly'}">
					<c:set var="isLy" value="1"></c:set>
					<span id="msg_span" name="msg_span" >
						<a href='javascript:void(0)' id='btn_msg' class='easyui-linkbutton' onclick='openLy()'>留言反馈</a>
					</span>
				</c:if>		
				 -->		
			</c:forEach>
		 	<c:forEach var="_list" items="${actionList}">
				<!-- <input type="button" class="c_btn_auth" id="btn_send" value="${_list.actionname}" onClick="sendFlow('${_list.actionid}',this);"/> -->
				<!-- <a href="javascript:void(0)" id="btn_send" class="easyui-linkbutton" onclick="sendFlow('${_list.actionid}',this,'${_list.actionname}');">${_list.actionname}</a> -->
				<label><input type= "button" class="btn_query" onClick="sendFlow('${_list.actionid}',this,'${_list.actionname}');"    value="${_list.actionname}&nbsp;"/></label>
			</c:forEach>		
		 	<!--<a href="javascript:void(0)" id="btn_save" class="easyui-linkbutton" onClick="saveForm();">保存</a> -->
		 	<label><input type= "button" class="btn_query" onClick="saveForm();"  style="width:62px" value="保存"/></label>
		 	<!-- <a href="javascript:void(0)" id="btn_showwk" class="easyui-linkbutton" onclick="openFlow();">查看流程</a> -->
		 	<label><input type= "button" class="btn_query" onClick="openFlow();"  style="width:80px" value="查看流程"/></label>
		 	<!--<a href="javascript:void(0)" id="btn_play" style="display:none" class="easyui-linkbutton" onclick="playLy();">录音播放</a> -->
		 	<label><input type= "button" class="btn_query" onClick="playLy();"  style="width:80px" value="录音播放"/></label>
		 	<!--<a href='javascript:void(0)' id='btn_send' class='easyui-linkbutton' onclick="openTimeLine();">查看时间轴</a> -->
		 	<label><input type= "button" class="btn_query" onclick="openTimeLine();" style="width:90px" value="查看时间轴"/></label>
		 	<!--<a href='javascript:void(0)' id="btn_print" class="easyui-linkbutton" onclick="printForm()">打印事项信息</a> -->
		</div>
		<br><br>
		<div align="center">
			<iframe id='formFrame' onload="showFrame();"   frameborder='no' border='0' marginheight="0" marginwidth="0" scrolling="auto" > </iframe>
	    </div>
	    <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="流程处理过程" iconCls="icon-search" collapsible="true"  collapsed="true">
			<table id="datalist" style="height:auto"    singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
			</table>
		<div>
	</div>
	<!-- 上传附件div start-->
	<div id="attachment_div" class="easyui-window" closed="true" title="上传文件" closed="true" collapsible="close" 
						minimizable="close" maximizable="close" modal="true"
						style="width:400px;height:250px;left:188px;top:5px">
		<input type=hidden name="mk_dm" id = "mk_dm" value="104">
		<input type=hidden name="mk_mc" id = "mk_mc" value="form">
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
	<!-- end -->
</body>
</html>
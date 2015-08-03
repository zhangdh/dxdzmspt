<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${webcontext}/js/upload.js"></script>
<script type="text/javascript" src="${webcontext}/js/msg.js"></script>
<script src="${webcontext}/form/use/todo_form.js" type="text/javascript"></script>
<title>查看已办事项</title>
</head>
<script>
	function showFrame(){
		document.getElementById('formFrame').style.height=formFrame.document.body.scrollHeight;
		document.getElementById('formFrame').style.width=formFrame.document.body.scrollWidth;	
	}
</script>
<body id="bodyid">
	<input type="hidden" id="id" name="id" value="${id}" />
	<input type="hidden" id="stepId" name="stepId" value="${stepId}" />
	<input type="hidden" id="username"  name="username" value="${username}" />
	<input type="hidden" id="businessid" name="businessid" value="${businessid}"/>
	<input type="hidden" id="mk_dm" name="mk_dm" value="${mk_dm}"/>
	<input type="hidden" id="templateName" name="templateName" value="${templateName}"/>
	<input type="hidden" id="formId" name="formId" value="${formId}"/>	
	<input type="hidden" id="ifFj" name="ifFj" value="${ifFj}"/>	
	<input type="hidden" id="fjStr" name="fjStr" value="${fjStr}"/>	
	<input type="hidden" id="mk_dir" name="mk_dir" value="${mk_dir}"/>	
	<input type="hidden" id="fjStr" name="fjStr" value="${ifLy}"/>	
	<div id="div_show" class="c_table_bar_content">
		<div align="right" style="position:absolute;width:100%;height:30px;line-height:30px;background:#E7EEFE;padding-top:3px;padding-right:3px;">
			<c:if test="${ifFj=='1'}">
				<c:set var="isFjid" value="1"></c:set>
				<input type=hidden name="fjid" id="fjid">
		        <span id="files"></span>
			</c:if>	
			<!-- 		
			<c:if test="${ifLy=='1'}">
				<span id="msg_span" name="msg_span" >
					<a href='javascript:void(0)' id='btn_msg' class='easyui-linkbutton' onclick='openLy()'>留言反馈${Lys}条</a>
				</span>
			</c:if>	
			 -->
			<!-- <a href="javascript:void(0)" id="btn_showwk" class="easyui-linkbutton" onclick="playLy();">录音播放</a> -->
			<!--<a href='javascript:void(0)' id='btn_send' class='easyui-linkbutton' onclick="openFlow();">查看处理流程</a> -->
			<label><input type= "button" class="btn_query" onclick="openFlow();"  style="width:110px" value="查看处理流程"/></label>
			<!--<a href='javascript:void(0)' id='btn_send' class='easyui-linkbutton' onclick="openTimeLine();">查看时间轴</a>-->
			<label><input type= "button" class="btn_query" onclick="openFlow();"  style="width:110px" value="查看处理流程"/></label>
			<!--<a href='javascript:void(0)' id="btn_print" class="easyui-linkbutton" onclick="printForm()">打印事项信息</a> -->
		</div>
		<br/><br/>
		<div align="center">
			<iframe id='formFrame' onload="showFrame();" frameborder='no' border='0' scrolling="auto" marginheight="0" marginwidth="0"> </iframe>
	    </div>
	</div>
	<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="流程处理过程" iconCls="icon-search" collapsible="true"  collapsed="true">
		<table id="datalist" style="height:auto"    singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
	<div>
</body>
</html>
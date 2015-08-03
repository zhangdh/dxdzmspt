<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<html>
	<head>
		<% 
			String workid = request.getParameter("workid");
		%>
		<title>选择下一步处理人</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css"  href="${webcontext}/js/ztree/css/zTreeStyle/zTreeStyle.css">
		<script type="text/javascript" src="${webcontext}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
		<script type="text/javascript" src="${webcontext}/js/ztree/jquery.ztree.excheck-3.5.js"></script>

		<script>
		    var workid = '<%=workid%>';
		    $("#workid").val(workid);
		</script>
		<script type="text/javascript" src="userList.js"></script>
		<title>下一步处理选择列表</title>
	</head>
	<body>
	<div id="selectYh" class="easyui-window"  title="选择下一步处理人" closed="close" closable = "false" collapsible="close" 
				minimizable="close" maximizable="close"  modal="true" style="width:265px;height:380px; left:1px;top:1px">
		<div class="easyui-layout" style="width:100%;height:300px;">
			<div region="west"   style="width:250px;">
				<ul id="usertree" name="usertree" class="ztree"></ul>
			</div>
			<div region="center"  id="yxz"></div>
		</div>
		<br/>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:void(0)" onclick="send()">确定</a>
		</div>
	</div>
		<br/>
		<div id="alert_div" style="display:none">
			<table>
				<tr>
					<td>提醒内容</td>
					<td>
						<textarea row=2></textarea>
					</td>
				</tr>
			</table>
		</div>

		<form id="form_show">
					<input type="hidden"  name="id" id="id" value="${id}" />
					<input type="hidden" id="formid" name="formid" value="${formId}" />
					<input type="hidden" name="doid" id="doid" value="${doid}" />
					<input type="hidden" id="tostep" name="tostep" value="${toStep}" />
					<input type="hidden" id="nextstep" name="nextstep" value="${nextStep}" />
					<input type="hidden" name="username" id="username" value="${username}" />
					<input type="hidden" name="stepid" id="stepid" value="${stepId}" />
					<input type="hidden" name="sendtype" id="sendtype" value="1" />
					<input type="hidden" id="txflag" name="txflag" value="${remind}" />
					<input type="hidden" id="workid" name="workid" />
					<!-- 
					<input type="hidden" id="ldpsflag" name="ldpsflag" /> 
					<input type="hidden" id="cbFlag" name="cbFlag" value="${isCuib}" />
					<input type="hidden" id="lclb_dm" name="lclb_dm" value="${lclb_dm}" />
					<input type="hidden" id="mk_dm" name="mk_dm" value="${mk_dm}" />
					<input type="hidden" id="mkurllb" name="mkurllb" value="${mkurllb}" />
					 -->
					<input type="hidden" id="businessid" name="businessid"
						value="${businessid}" />
					<input type="hidden" id="isshowyh" name="isshowyh"
						value="${isShowYh}" />
					<input type="hidden" id="undo_title" name="undo_title"
						value="${undo_title}" />
					<input type="hidden" id="dbflag" name="dbFlag" value="${dbflag}" />
					<input type="hidden" id="workcondition" name="workcondition"
						value="${workcondition}" />
					<input type="hidden" name="userid" id="userid" value=""/>
					
					<input type="hidden" id="amount" name="amount" value="${amount}" />
		</form>
		
	</body>
</html>
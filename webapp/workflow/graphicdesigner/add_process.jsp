<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/js.jsp"%>
<%@ include file="/common/xiorkFlowBasePath.jsp"%>
<html xmlns:v="urn:schemas-microsoft-com:vml">
	<head>
		<title>流程定义</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<script charset="UTF-8" src="${ctx}/js/XiorkFlow/js/XiorkFlowWorkSpace.js" language="javascript"></script>
		<script charset="UTF-8" src="${ctx}/js/XiorkFlow/app/addprocess.js" language="javascript"></script>
	</head>
	<body onload="init();" onresize="oresize()">
		<div id="designer"
			style="width: 100%; height: 100%; border: #e0e0e0 1px solid;"></div>
		<div id="message"></div>
	</body>
</html>

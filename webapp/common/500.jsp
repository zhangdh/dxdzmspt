<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ page session="false"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>500 - 系统内部错误</title>
</head>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Exception) request.getAttribute("javax.servlet.error.exception");
	String msg=java.net.URLEncoder.encode(ex.getMessage(),"UTF8");
	response.addHeader("Error-Json","{exception:true,msg:'"+msg+"'}");
%>
<body>
	<div id="content">
		<h3>系统发生内部错误.<%=ex.getMessage()%></h3>
		<button onclick="history.back();">返回</button>
	</div>
</body>
</html>

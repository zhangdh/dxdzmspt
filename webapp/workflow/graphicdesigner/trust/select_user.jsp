<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type=request.getAttribute("type").toString();
String id=request.getAttribute("id").toString();
String stepId=request.getAttribute("stepId").toString();
String method=request.getAttribute("method").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>委托设置</title>
  </head>

  <body>
     <form action="TrustManager" method="post" name="form1">
             <input type="hidden" name="trustType" value="<%=type%>"></input>
             <input type="hidden" name="id" value="<%=id%>"></input>
             <input type="hidden" name="stepId" value="<%=stepId%>"></input>
             <input type="hidden" name="method" value="<%=method%>"></input>
	         <select size="6" id="configselect" name="user"
						style="width: 200" onChange="" >
						<option value="yu"> 
							yu 
						</option>
						<option value="li">
							li
						</option>
					</select>
   		      <div style=" float:left;width:100%;height:50px;">
   		      <input type="submit"  value="确定" /> 
   		      <input type="button" value="关闭" />
   		      </div>
   		      </form>
  </body>
   
</html>

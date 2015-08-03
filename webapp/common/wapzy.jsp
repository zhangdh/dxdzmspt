<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ page import="com.coffice.util.cache.Cache" %>
<%

String yhid1=(String)request.getAttribute("yhid");
String fontsize = "1";
if(yhid1!=null&&!yhid1.equals("")){
  fontsize =  (String)Cache.getUserInfo(yhid1,"fontsize");
}
%>
<link rel="stylesheet" type="text/css" href="${ctx}/wap_iphone/css/wap.css"/>

<%@ page import="com.coffice.util.SysPara"%>

<%
request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");
String pport=SysPara.getValue("pic_port");
%>
<script type="text/javascript">
function editor_Af(nr){
var re = "http://.*?/";	//匹配IP地址的正则表达式
var reg=new RegExp(re);
 return nr=nr.replace(reg, "http://"+window.location.hostname+":<%=pport%>/") ;
}
</script>
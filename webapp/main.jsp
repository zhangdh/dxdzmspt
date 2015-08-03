<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=com.coffice.util.SysPara.getValue("title") %></title>

<script>
<%if(request.getAttribute("prompt")!=null){%>
  alert('<%=request.getAttribute("prompt")%>');
<%}%>

function closeWin(event) {
   document.location.href="<%=request.getContextPath()%>/exit.doAction";
}
function max_win(){
	
}
</script>
</head>
<frameset id=frame_all onload="max_win();" border=0 onunload="closeWin(event);" framespacing=0 rows=95,*,26 topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<frame border=0 name=frame_top marginwidth=0 marginheight=0 src="top.jsp" frameborder=no scrolling=no topmargin="0" leftmargin="0" padding="0" style="width:100%">
	<frameset id=frame_main border=0 framespacing=0 frameborder=no cols=160,2,* topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
		<frame border=0 name=frame_left marginwidth=0 marginheight=0 src="menu/menu.jsp" frameborder=no scrolling=auto noresize="noresize">
		<frame border=0 name=frame_bar marginwidth=0 marginheight=0 src="" frameborder=no scrolling=auto noresize="noresize">
		<frame border=0 name=frame_right src="tab_panel.jsp?title=工作桌面&url=desk/desk.jsp" frameborder=no scrolling=auto noresize="noresize" >
	</frameset>
	<frame border=0 id=frame_bottom name=frame_bottom marginwidth=0 marginheight=0 src="main_bottom.jsp" frameborder=no scrolling=no topmargin="0" leftmargin="0" padding="0"  style="width:100%">
</frameset><noframes></noframes>
</html>
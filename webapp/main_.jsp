<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/meta.jsp" %>
<%@ include file="/common/js.jsp" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ page import="com.coffice.oa.company.*"%>
<%@ page import="com.coffice.util.SysPara"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=com.coffice.util.SysPara.getValue("title") %></title>
<%
  boolean is_tabpanel_desk = false;
  try{
	  is_tabpanel_desk = Boolean.parseBoolean(SysPara.getValue("sys_tabpanel"));
  }catch(Exception e){
  }
%>
<script>
<%if(request.getAttribute("prompt")!=null){%>
  alert('<%=request.getAttribute("prompt")%>');
<%}%>

function closeWin(event) {
// if(event.clientX<=0 && event.clientY<0) {
   document.location.href="<%=request.getContextPath()%>/login.do?method=exit";
  //}
}


function max_win(){
	
}

</script>
</head>
<frameset id=frame_all onload="max_win();" border=0 onunload="closeWin(event);" framespacing=0 rows=103,*,26 topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<frame border=0 name=frame_top marginwidth=0 marginheight=0 src="main_top.jsp" frameborder=no scrolling=no topmargin="0" leftmargin="0" padding="0" style="width:100%">
	<frameset id=frame_main border=0 framespacing=0 frameborder=no cols=160,10,* topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
		<frame border=0 name=frame_left marginwidth=0 marginheight=0 src="<%=request.getContextPath()%>/org/default.do?method=sys_menu&newskin=yes&fname=sys_menu_2103.jsp" frameborder=no topmargin="0" leftmargin="0" scrolling=auto noresize="noresize">
		<frame border=0 name=frame_bar marginwidth=0 marginheight=0 src="main_bar.jsp" frameborder=no scrolling=no topmargin="0" leftmargin="0" noresize="noresize">
        <%if(!is_tabpanel_desk){ %>
		<frame border=0 name=frame_right src="desk/desk_2103.jsp" frameborder=no scrolling=auto noresize="noresize">
	    <%}else{ %>
		<frame border=0 name=frame_right src="tab_panel.jsp?title=桌面&url=desk/desk_2103.jsp" frameborder=no scrolling=auto noresize="noresize">
	    <%}%>
	</frameset>
	<frame border=0 id=frame_bottom name=frame_bottom marginwidth=0 marginheight=0 src="main_bottom.jsp" frameborder=no scrolling=no topmargin="0" leftmargin="0" padding="0"  style="width:100%">
</frameset><noframes></noframes>
</html>
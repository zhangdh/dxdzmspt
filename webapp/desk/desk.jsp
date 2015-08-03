<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
	<script src="desk.js" type="text/javascript"></script>
</head>
<body>
    <div id="columns">
        <ul id="column1" class="column" style="width:55%">
            <li class="widget" divpos="diffusion" id="li_0_0" >  
                <div class="widget-head" id="div_head_0_0">
                    <div class="desk_title">&nbsp;&nbsp;信息发布</div>
                    <div class="desk_refresh"><a href="#" onclick=refresh('diffusion')>刷新</a></div>
                    <div class="desk_more"><a href="#" onclick=more('diffusion')>更多</a></div>                  
                </div>
                <div class="widget-content" id="div_content_0_0">
                	<jsp:include page="jsp/diffusion_desktop.jsp">
                		<jsp:param name="para" value="" />
                	</jsp:include>
                </div>
            </li>
            <li class="widget" divpos="undo" id="li_0_1">  
                <div class="widget-head" id="div_head_0_1">
                    <div class="desk_title">&nbsp;&nbsp;待办事宜</div>
                    <div class="desk_refresh"><a href="#" onclick=refresh('undo')>刷新</a></div>
                    <div class="desk_more"><a href="#" onclick=more('undo')>更多</a></div>                  
                </div>
                <div class="widget-content" id="div_content_0_0">
                	<jsp:include page="jsp/undo_desktop.jsp">
                		<jsp:param name="para" value="" />
                	</jsp:include>
                </div>
            </li>
        </ul>
        <ul id="column2" class="column" style="width:45%">
            <li class="widget" divpos="email" id="li_1_0">  
                <div class="widget-head" id="div_head_1_0">
                    <div class="desk_title">&nbsp;&nbsp;站内邮件</div>
                    <div class="desk_refresh"><a href="#" onclick=refresh('email')>刷新</a></div>
                    <div class="desk_more"><a href="#" onclick=more('email')>更多</a></div>                  
                </div>
                <div class="widget-content" id="div_content_1_0">
                	<jsp:include page="jsp/email_desktop.jsp">
                		<jsp:param name="para" value="" />
                	</jsp:include>
                </div>
            </li>
             <li class="widget" divpos="undo" id="li_1_1">  
                <div class="widget-head" id="div_head_1_1">
                    <div class="desk_title">&nbsp;&nbsp;待阅</div>
                    <div class="desk_refresh"><a href="#" onclick=refresh('unread')>刷新</a></div>
                    <div class="desk_more"><a href="#" onclick=more('unread')>更多</a></div>                  
                </div>
                <div class="widget-content" id="div_content_1_1">
                	<jsp:include page="jsp/unread_desktop.jsp">
                		<jsp:param name="para" value="" />
                	</jsp:include>
                </div>
            </li>
        </ul>
    </div>
</body>
</html>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/desk/jsfordesk.jsp"%>
		<script>
			function show_hide(){
				if(parent.document.all.frame_left.width=="172"){ //隐藏左侧
				  parent.frame_main.cols = "0,10,*";
				  $("#main_bar_arrow").removeClass("c_main_bar_arrow_left").addClass("c_main_bar_arrow_right");
				}else if(parent.document.all.frame_left.width=="0"){ //显示左侧
				  parent.frame_main.cols = "172,10,*";
				  $("#main_bar_arrow").removeClass("c_main_bar_arrow_right").addClass("c_main_bar_arrow_left");
				}
			}
		</script>
	</head>
	<body onClick="show_hide();">
		<table width="6" height="100%" border="0" cellpadding="0" cellspacing="0" class="c_main_bar">
        	<tr height="200px"><td></td></tr>            
			<tr height="41px" >
				<td id="main_bar_arrow" class="c_main_bar_arrow_left" onClick="show_hide();"></td>
			</tr>
            <tr><td></td></tr>
		</table>
</body>
</html>

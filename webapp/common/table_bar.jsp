<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%
String table_id=request.getParameter("table_id")!=null?request.getParameter("table_id"):"";
String is_hidden=request.getParameter("hFlag")!=null?request.getParameter("hFlag"):"";
if(!"".equals(table_id)){
	table_id="id="+table_id;
}
if(!"".equals(is_hidden)){
	if(is_hidden.equals("1")){//显示
		is_hidden="style=display:";
	}else{//隐藏
		is_hidden="style=display:none";
	}
}
String div_id = request.getParameter("div_id");
if (div_id==null) {out.print("<script>alert('table_bar.jsp:请指定div_id');</script>");}
String bar_title = request.getParameter("bar_title");
if (bar_title==null) bar_title="";
%>
<table <%=table_id %> width="100%" border="0" cellspacing="0" cellpadding="0" <%=is_hidden %>>
	<tr onClick="tableBarClick('<%=div_id%>')">
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="c_table_bar">
				<tr>
					<td width="50%"><%=bar_title %></td>
					<td width="47%"></td>
					<td width="3%" id="sys_td_<%=div_id%>_arrow" class="c_table_bar_arrow_up">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td id="sys_td_<%=div_id%>">
			<script>
				$('#sys_td_<%=div_id%>').html($('#<%=div_id%>').html());
				$('#<%=div_id%>').empty();
			</script>
		</td>
	</tr>
</table>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<% 
    String workNo = request.getParameter("workNo");
%>
<html><!-- InstanceBegin template="/Templates/3tableBar.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/js.jsp" %>
    <!--页面私有js区域起-->
    <!-- InstanceBeginEditable name="head" -->
    <script src="queryGh.js" type="text/javascript"></script>
    <script src="js/datadumper.js" type="text/javascript"></script>
    <link type="text/css" href="../css/default.css" rel="stylesheet" />
    <link type="text/css" href="../css/3/style.css" id="css_id" rel="stylesheet">
    <title>工号通话信息查询</title>
    <!-- InstanceEndEditable -->
    <!--页面私有js区止起-->

</head>
<body id="bodyid">
<div id="div_list" class="c_table_bar_content">
    <div id="div_table_list">
        <div >
            <!--列表区域起，列表要显示5条记录-->
            <!-- InstanceBeginEditable name="table_list" -->
            <table id="table_list" class="c_table_list" width="100%" border="1">
            <input type="hidden" name="workNo" id="workNo" value="<%=workNo%>"/>
                <thead id="table_list_thead">
                <tr>
                    <td width="4%" style="display:none">guid</td>					 
                    <td width="24%" height="22" align="center"  class="lieb">主叫</td>
					<td width="24%" height="22" align="center"  class="lieb">被叫</td>
					<td width="24%" height="22" align="center"  class="lieb">呼入/呼出</td>
					<td width="24%" height="22" align="center"  class="lieb">通话开始时间</td>		
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none">{guid}</td>
					<td align="center" style="cursor:hand;">{caller}</td>
					<td align="center">{called}</td>
					<td align="center">{mc}</td>
					<td align="center">{calldate}</td>
                </tr>
                </tbody>
            </table>
            <!-- InstanceEndEditable -->
            <!--列表区域止-->
        </div>
</div>

</body>
<!-- InstanceEnd --></html>

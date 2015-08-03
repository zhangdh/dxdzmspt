<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<html><!-- InstanceBegin template="/Templates/3tableBar.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
    <%@ include file="/common/meta.jsp"%>
    <%@ include file="/common/js.jsp"%>
    <title>通话信息查询</title>
	<script src="${ctx}/js/attachment.js" type="text/javascript"></script>
	<script src="thxxCx.js" type="text/javascript"></script>
</head>

<body id="bodyid">
<div id="div_query" class="c_table_bar_content">
    <form id="form_query">
        <!-- 查询区域起-->
        <!-- InstanceBeginEditable name="form_query" -->
        <label>主叫：<input type="text" id="cx_zj" name="cx_zj" size="15"></label>
        <label>被叫：<input type="text" id="cx_bj" name="cx_bj" size="15"></label>
        <label>呼入/呼出：<select  id="cxthlb" name="cxthlb" style="WIDTH: 100px; HEIGHT: 20px; " showName="呼入/呼出"></select></label>
        &nbsp;<input type="button" id="btn_query" value="搜索" onClick="query();">
        &nbsp;<input type="reset" value="重置"/>
        <!-- InstanceEndEditable -->
        <!-- 查询区止起-->
    </form>
</div>
<div id="div_list" class="c_table_bar_content">
    <div id="div_table_list">
        <div class="c_div_table_list">
            <!--列表区域起，列表要显示10条记录-->
            <!-- InstanceBeginEditable name="table_list" -->
            <table id="table_list" class="c_table_list" width="100%" border="1">
                <thead id="table_list_thead">
                <tr>
                    <td width="11%" style="display:none">guid</td>					 
                    <td width="16%" height="22" align="center"  class="lieb">主叫</td>
					<td width="16%" height="22" align="center"  class="lieb">被叫</td>
					<td width="16%" height="22" align="center"  class="lieb">呼入/呼出</td>
					<td width="16%" height="22" align="center"  class="lieb">工号</td>
					<td width="16%" height="22" align="center"  class="lieb">通话开始时间</td>					
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none">{guid}</td>
					<td align="center" style="cursor:hand;">{caller}</td>
					<td align="center">{called}</td>
					<td align="center">{mc}</td>
					<td align="center">{workno}</td>
					<td align="center">{calldate}</td>
                </tr>
                </tbody>
            </table>
            <!-- InstanceEndEditable -->
            <!--列表区域止-->
        </div>
        <div class="c_div_table_list_nav">
            <%@ include file="/common/table_list_nav.jsp" %>
        </div>
    </div>
</div>
<jsp:include page="/common/table_bar.jsp">
	<jsp:param name="div_id" value="div_query" />
	<jsp:param name="bar_title" value="查询" />
</jsp:include>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_list"/>
    <jsp:param name="bar_title" value="结果列表"/>
</jsp:include>
</body>
<!-- InstanceEnd --></html>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/js.jsp" %>
    <!--页面私有js区域起-->
    <!-- TemplateBeginEditable name="head" -->
    <script src="default.js" type="text/javascript"></script>
    <!-- TemplateEndEditable -->
    <!--页面私有js区止起-->
</head>

<body id="bodyid">
<div id="div_list" class="c_table_bar_content">
<!-- TemplateBeginEditable name="div_list" -->
    <div id="div_table_list">
        <div class="c_div_table_list">
            <!--列表区域起，列表要显示10条记录-->            
            <table id="table_list" class="c_table_list" width="100%" border="1">
                <thead id="table_list_thead">
                <tr>
                    <td width="100%">表头</td>
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none"><!--根据需要可以隐藏某些行，如guid--></td>
                </tr>
                </tbody>
            </table>
            <!--列表区域止-->
        </div>
        <div class="c_div_table_list_nav">
            <div class="c_table_list_nav_btn">
                <!--列表按钮区起-->

                <!--列表按钮区止-->
            </div>
            <%@ include file="/common/table_list_nav.jsp" %>
        </div>
    </div>
    <div id="div_table_list2">
        <div class="c_div_table_list">
            <!--列表区域起，列表要显示10条记录-->
            <table id="table_list2" class="c_table_list" width="100%" border="1">
                <thead id="table_list_thead2">
                <tr>
                    <td width="100%">表头</td>
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none"><!--根据需要可以隐藏某些行，如guid--></td>
                </tr>
                </tbody>
            </table>
            <!--列表区域止-->
        </div>
        <div class="c_div_table_list_nav">
            <div class="c_table_list_nav_btn">
                <!--列表按钮区起-->

                <!--列表按钮区止-->
            </div>
            <%@ include file="/common/table_list_nav.jsp" %>
        </div>
    </div>
<!-- TemplateEndEditable -->
</div>
<div id="div_show" class="c_table_bar_content">
    <div class="c_table_show_btn">
        <!-- 数据按钮区起 -->
        <!-- TemplateBeginEditable name="form_show_button" -->
        <input type="button" class="c_btn_auth" id="btn_add" value="新增" onClick="add();"/>
        <input type="button" class="c_btn_auth" id="btn_save" value="保存" onClick="save();"/>
        <!-- TemplateEndEditable -->
        <!-- 数据按钮区止 -->
    </div>
    <form id="form_show">
        <!--数据明细区起-->
        <!-- TemplateBeginEditable name="form_show" -->

        <!-- TemplateEndEditable -->
        <!--数据明细区止-->
    </form>
</div>
<!-- TemplateBeginEditable name="jsp_include" -->
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_list"/>
    <jsp:param name="bar_title" value="查询结果"/>
</jsp:include>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_show"/>
    <jsp:param name="bar_title" value="数据显示"/>
</jsp:include>
<!-- TemplateEndEditable -->
</body>
</html>

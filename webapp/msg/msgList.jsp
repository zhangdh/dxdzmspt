<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/3tableBar.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/js.jsp" %>
    <!--页面私有js区域起-->
    <!-- InstanceBeginEditable name="head" -->
    <title>留言反馈</title>
    <script type="text/javascript" charset="utf-8" src="${ctx}/msg/msg.js"></script>
	<script type="text/javascript" src="${ctx}/js/datePicker/WdatePicker.js"></script>
    <!-- InstanceEndEditable -->
    <!--页面私有js区止起-->
</head>

<body id="bodyid">
<div id="div_query" class="c_table_bar_content">
    <form id="form_query">
        <!-- 查询区域起-->
        <!-- InstanceBeginEditable name="form_query" -->
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
                    <td width="4%"  style="display:none">&nbsp;</td>					 
                    <td width="25%" height="22" align="center"  class="lieb">留言人</td>
                    <td width="30%" height="22" align="center"  class="lieb">留言内容</td>
					<td width="30%" height="22" align="center"  class="lieb">留言时间</td>
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none">{guid}</td>
					<td align="center" style="cursor:hand;display:none">{guid}</td>
					<td align="center" style="cursor:hand;">{yhid}</td>
					<td align="center" style="cursor:hand;">{msgnr}</td>
					<td align="center">{cjsj}</td>
                </tr>
                </tbody>
            </table>
            <!-- InstanceEndEditable -->
            <!--列表区域止-->
        </div>
        <div class="c_div_table_list_nav">
            <div class="c_table_list_nav_btn">
                <!--列表按钮区起-->
                <!-- InstanceBeginEditable name="table_list_button" -->
                <!-- InstanceEndEditable -->
                <!--列表按钮区止-->
            </div>
            <%@ include file="/common/table_list_nav.jsp" %>
        </div>
    </div>
</div>
<div id="div_show" class="c_table_bar_content">
    <div class="c_table_show_btn">
        <!-- 数据按钮区起 -->
        <!-- InstanceBeginEditable name="form_show_button" -->
        <input type="button" class="c_btn_hide" id="btn_insert" name="btn_insert" value="新建"/>
		<input type="button" class="c_btn_hide" id="btn_save" name="btn_save" value="保存"/>
		<input type="button" class="c_btn_hide" id="btn_del" name="btn_del" value="删除"/>

        <!-- InstanceEndEditable -->
        <!-- 数据按钮区止 -->
    </div>
    <form id="form_show">
        <!--数据明细区起-->
        <!-- InstanceBeginEditable name="form_show" -->
        <input type="hidden" name="guid" id="guid">
        <input type="hidden" name="yhid" id="yhid">
        <input type="hidden" name="yh_id" id="yh_id">
        <input type="hidden" name="ywid" id="ywid" >
        <input type="hidden" name="mk_dm" id="mk_dm" >
        <input type="hidden" name="mk_dm_mc" id="mk_dm_mc" >
        <input type="hidden" name="wr_flag" id="wr_flag" >
        <input type="hidden" name="cjsj" id="cjsj">
        <input type="hidden" name="count" id="count">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">	
		<tr>
			<td align="center" >请录入留言反馈内容：</td>
	    </tr>								
		<tr>
			<td align="center"><textarea check="notBlank" required="true" showName="留言内容" id="msgnr" name="msgnr" style="width:680px;" rows="4"></textarea></td>
		</tr>
            </table>
        <!-- InstanceEndEditable -->
        <!--数据明细区止-->
    </form>
</div>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_list"/>
    <jsp:param name="bar_title" value="留言列表"/>
</jsp:include>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_show"/>
    <jsp:param name="bar_title" value="数据显示"/>
</jsp:include>
</body>
<!-- InstanceEnd --></html>

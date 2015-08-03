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
    <title>系统参数配置</title>
    <script src="syspara.js" type="text/javascript"></script>
    <!-- InstanceEndEditable -->
	<script type="text/javascript">
	function skinSetting(sys_skin){
	 	var skin_mc;
	 	var skin_mc_menu; 
	 		
		if(sys_skin == null || "" == sys_skin){
			skin_mc="${ctx}/css/3/style.css";
			skin_mc_menu="${ctx}/menu/css/3/style.css";
		}else if("2103"==(sys_skin)){
			skin_mc="${ctx}/css/3/style.css";
			skin_mc_menu="${ctx}/menu/css/3/style.css";
		}else if("2105"==(sys_skin)){
			skin_mc="${ctx}/css/5/style.css";
			skin_mc_menu="${ctx}/menu/css/5/style.css";
		}else if("2106"==(sys_skin)){
			skin_mc="${ctx}/css/6/style.css";
			skin_mc_menu="${ctx}/menu/css/6/style.css";
		}
	 		
		Ceshi(skin_mc);
		top.frame_top.Ceshi(skin_mc);
		top.frame_bar.Ceshi(skin_mc);
		top.frame_left.Ceshi(skin_mc_menu);
		top.frame_bottom.Ceshi(skin_mc);
	}
	</script>
    <!--页面私有js区止起-->
</head>
<body id="bodyid">
<div id="div_query" class="c_table_bar_content">
    <form id="form_query">
        <!-- 查询区域起-->
        <!-- InstanceBeginEditable name="form_query" -->
       	    <label>按模块进行查询：<SELECT name="cx_mk"  id="cx_mk"  style="WIDTH: 200px; HEIGHT: 20px; "></SELECT></label>
       	    <label>参数名称：<input type="text" name="cx_mc"  id="cx_mc"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	    <label>参数说明：<input type="text" name="cx_sm"  id="cx_sm"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	    <label>参数值：<input type="text" name="cx_csz"  id="cx_csz"  style="WIDTH: 120px; HEIGHT: 20px; "/></label>
       	    <input type="button" id="btn_query" value="搜索" >
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
                    <td width="11%" style="display:none">id</td>
                    <td width="4%">&nbsp;</td>					 
                    <td width="15%" height="22" align="center"  class="lieb">参数名称</td>
                    <td width="30%" height="22" align="center"  class="lieb">参数说明</td>
					<td width="40%" height="22" align="center"  class="lieb">参数值</td>
					<td width="40%" height="22" align="center"  class="lieb">模块</td>
                </tr>
                </thead>
                <tbody>
                <tr style="display:none"><!--模板行-->
                    <td style="display:none">{id}</td>
					<td align="center"><input type="checkbox" name="table_list_checkbox" value="{id}"></td>
					<td align="center" style="cursor:hand;">{csdm}</td>
					<td align="center" style="cursor:hand;">{cssm}</td>
					<td align="center" style="cursor:hand;">{csz}</td>
					<td align="center" style="cursor:hand;">{mk_dm}</td>
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
		<input type="button" class="c_btn_auth" id="btn_modi" name="btn_modi" value="保存修改"/>
        <!-- InstanceEndEditable -->
        <!-- 数据按钮区止 -->
    </div>
    <form id="form_show">
        <!--数据明细区起-->
        <!-- InstanceBeginEditable name="form_show" -->        
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<input type="hidden" id="id" name="id" >	 
		<input type="hidden" id="old_csz" name="old_csz" >           
	            <tr>
	            	<td width="18%" align="right" >参数名称：</td>
	                <td width="82%"><input class="biinput" maxlength="130" size="15"  style="WIDTH: 400px; HEIGHT: 20px" name="csdm" id="csdm" readonly="true" /></td>
	         	</tr>				
				<tr>
					  <td width="18%" align="right">参数说明：</td>
					  <td width="82%"><textarea id="cssm" name="cssm" style="width:400px;height:40px;" readonly="true"></textarea></td>
				</tr>
				<tr>
	            	<td width="18%" align="right" >参 数 值：</td>
	                <td width="82%"><input class="biinput" maxlength="500" size="15"  style="WIDTH: 400px; HEIGHT: 20px" name="csz" id="csz" required="true" showName="参数值"/></td>
	         	</tr>	
            </table>
        <!-- InstanceEndEditable -->
        <!--数据明细区止-->
    </form>
</div>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_query"/>
    <jsp:param name="bar_title" value="查询"/>
</jsp:include>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_list"/>
    <jsp:param name="bar_title" value="参数列表"/>
</jsp:include>
<jsp:include page="/common/table_bar.jsp">
    <jsp:param name="div_id" value="div_show"/>
    <jsp:param name="bar_title" value="数据显示"/>
</jsp:include>
</body>
<!-- InstanceEnd --></html>

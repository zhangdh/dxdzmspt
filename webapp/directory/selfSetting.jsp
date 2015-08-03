<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@page import="com.coffice.util.cache.Cache"%>
<%@ include file="/common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
    String gh=(String)Cache.getUserInfo((String)request.getAttribute("yhid"),"gonghao");
	String fjh=(String)Cache.getUserInfo((String)request.getAttribute("yhid"),"fenjihao");
	if(gh==null)gh="未设置";
	if(fjh==null)fjh="未设置";
%>
<html><!-- InstanceBegin template="/Templates/3tableBar0_~_1.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
    <script src="selfSetting.js" type="text/javascript"></script>
</head>
<body id="bodyid">
<div id="modiPs" class="easyui-panel" title="修改密码" iconCls="icon-tip">
	<form id="PsInfo">
	<table style="width:100%" class="c_table"  border="1px">
	  <tr  id="pwdtr">
            <td width="80px" height="25"  align="right" class="c_text"><div align="right" >登录密码：</div></td>
            <td width="150px"><input type="password" name="dlmm" id="dlmm" style="width:100%;ime-mode:disabled" showName="密码" value="${dlmm}"/>
            <td width="80px" class="c_text"><div align="right">密码确认：</div></td>
            <td width="150px"><input type="password" name="dlmm2" id="dlmm2" style="width:100%;ime-mode:disabled" showName="密码确认" value="${dlmm}"/>      
            <td>
            	&nbsp;<input type= "button" class="btn_query" style="width:100px" onclick="save('mm')" value="保存密码"/>
            </td>      
      </tr>
    </table>
    </form>
<div>
<div id="modiSelfInfo" class="easyui-panel" width="100px" title="个人信息维护" iconCls="icon-tip">
	<form id="selfInfo">
        <!--数据明细区起-->
        <!-- InstanceBeginEditable name="form_show" -->
      <table  class="c_table"   cellpadding="0">
      <tr>
        <td width="80px" height="25" class="c_text"><div align="right">工号：</div></td>
        <td width="150px">
        	<div><%=gh%></div>
        	<input name="kz321" id="kz321" type="hidden" value="<%=gh%>" />
        	</td>
        <td width="80px" class="c_text"><div align="right">分机号：</div></td>
        <td width="150px">
        	<div><%=fjh%></div>
        	<input type="hidden" id ="kz322" name="kz322" value="<%=fjh%>" />
        	<!--<input type="text" name="kz322" id ="kz322" style="width:60%" maxlength="50" value="${kz322}"/>-->
        </td>
        <td rowspan="6"  >
      		&nbsp;<input type= "button" class="btn_query" style="width:100px" onclick="save('yh')" value="保存信息"/>
      	</td>
      </tr>
      
      <tr>
        <td  height="25" class="c_text"><div align="right">中文名称：</div></td>
        <td  colspan="3"><input type="text" name="xm" required="true" readonly="readonly" showName="中文名称" id ="xm" maxlength="50" style="width:100%" value="${xm}"/></td>
      </tr>
      
      <tr>
        <td height="25" class="c_text"><div align="right">家庭地址：</div></td>
        <td  colspan="3"><input type="text" name="jtdz" id ="jtdz" maxlength="50" style="width:100%" value="${jtdz}"/></td>
      </tr>
      <tr>
        <td height="25" class="c_text"><div align="right">家庭邮编：</div></td>
        <td ><input type="textt" name="yb" id ="yb" style="width:100%" maxlength="yb" check="isZip" value="${yb}"/></td>
        <td  class="c_text"><div align="right">办公电话：</div></td>
        <td ><input type="text" name="jtdh" id="jtdh" maxlength="30" style="width:100%" check="isPhone" value="${jtdh}"/></td>
      </tr>
      <tr>
        <td  height="25" class="c_text"><div align="right">移动电话：</div></td>
        <td  colspan="3"><input type="text" name="sj" id="sj" style="width:100%" maxlength="50" check="isMobile" value="${sj}"/></td>
      </tr>
      <tr>
        <td  height="25" class="c_text"><div align="right">电子邮箱：</div></td>
        <td  colspan="3"><input type="text" name="email" id ="email" style="width:100%" maxlength="50" check="isEmail" value="${email}"/></td>
      </tr>
      <tr>
      	
      </tr>
      </table>
    </form>
<div>
<div id="downloads" class="easyui-panel" title="常用下载" iconCls="icon-tip">
	<br/><br/><br/>
</div>
</body>
<!-- InstanceEnd --></html>

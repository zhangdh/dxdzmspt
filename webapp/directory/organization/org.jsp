<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<c:set var="webcontext" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../../js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css"  href="../../js/ztree/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="../../css/common.css">
	<script type="text/javascript" src="../../js/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="../../js/validate.js"></script>
	<script type="text/javascript" src="../../js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script src="../../js/datePicker/WdatePicker.js?t=2123580090111" type="text/javascript"></script>
	<script src="../../js/coffice.js" type="text/javascript"></script>
	<script>
		var webcontext = "${webcontext}"; 
	</script>
	<script src="org.js" type="text/javascript"></script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
	<input type=hidden id = "orgid" name ="orgid">
	<input type=hidden id = "lxid" name ="lxid">
	<input type=hidden id = "mc" name ="mc">
	<input type=hidden id = "lx_dm" name ="lx_dm">
	<div region="west"  split="true" title="人员结构图" style="width:200px;">
		<ul id="orgtree" name="orgtree" class="ztree"></ul>
	</div>
	<div region="center" id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="下属列表"  iconCls="icon-tip">
					
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	</div>
	<div id="bm_div" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="部门编辑" style="top:120px;width:400px;height:280px;padding:10px;" iconCls="icon-tip">
		<form id="bm_form">
			<input type=hidden id='bmid' name='bmid'>
			<table class="table_show" align="center">
			<tr>
        	<td width="18%" height="25">
          		<div align="right">部门名称：</div>
        	</td>
        	<td width="82%">
          		<input type ="text" id="bm_mc" name="bm_mc" style="width:100%" maxlength="20" required="true" showName="部门名称"/>
        	</td>
      		</tr>
      		<tr>
        	<td width="18%" height="25">
          		<div align="right">部门电话：</div>
        	</td>
        	<td width="82%">
          		<input type ="text" id="bm_dh" name="bm_dh" style="width:100%" maxlength="30" check="isPhone"/>
        	</td>
      		</tr>
      		<tr>
        	<td width="18%" height="25">
        	  <div align="right">部门传真：</div>
        	</td>
        	<td width="82%">
        	  <input type ="text" id="bm_cz" name="bm_cz" style="width:100%" maxlength="30" check="isPhone"/>
        	</td>
     		 </tr>
      		<tr>
        	<td width="18%" height="25">
         		 <div align="right">部门邮箱：</div>
        	</td>
        	<td width="82%">
          		<input type ="text" id = "bm_email" name="bm_email" style="width:100%" maxlength="30" check="isEmail"/>
        	</td>
      		</tr>
     		 <tr>
      		  <td width="18%" height="25">
        		  <div align="right">部门描述：</div>
      		  </td>
        	<td width="82%">
          		<input type="text" id="bm_bz" name="bm_bz" rows="6" style="width:100%"/>
        	</td>
     		 </tr>
     		 <tr>
     		  <td width="18%" height="25">
        		  <div align="right">市级部门：</div>
      		  </td>
        	  <td width="82%">
          		<input type="radio" name="kz328" value="1"/>是
          		<input type="radio" name="kz328" value="0"/>否
        	  </td>
     		 </tr>
		</table>
		</form>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save('bm')">保存</a>
			<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel('bm')">取消</a>
		</div>
	</div>
	<div id="gw_div" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="岗位编辑" style="top:120px;width:400px;height:160px;padding:10px;" iconCls="icon-tip">
	  <form id="gw_form">
	  <input type=hidden id='gwid' name='gwid'>
	  <table class="table_show" align="center">
      <tr id="gwmc">
        <td width="18%" height="25" >
          <div align="right">岗位名称：</div>
        </td>
        <td width="82%"><input type="text" name="gw_mc"  id="gw_mc"  required="true" style="width:80%"/></td>
      </tr>
      <tr class="tb_cl6" >
        <td width="18%" height="25">
          <div align="right">岗位描述：</div>
        </td>
        <td width="82%"><input type="text" name="gw_bz" id="gw_bz" rows="12" style="width:80%"/></td>
      </tr>
	</table>
	</form>
	<div style="text-align:center;">
		<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save('gw')">保存</a>
		<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel('gw')">取消</a>
	</div>
	</div>
	<div id="yh_div" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="用户编辑" style="top:1px;width:600px;height:320px;" iconCls="icon-tip">
	<form id="yh_form">
	<input type=hidden id='yhid' name='yhid'>
	<table class="c_table" width="100%" border="1px">
          <tr>
            <td width="20%" height="25" class="c_text"><div align="right">岗位名称：</div></td>
            <td width="30%"><input type="text" name="mc_gw" id="mc_gw" style="width:100%" value="${gw_mc}"  disabled="disabled" >
            </td>
            <td width="20%" class="c_text"><div align="right">所属角色：</div></td>
            <td width="30%">
            	<input type=hidden id="kz300" name="kz300">
            	<input type="text" name="mc_js" id="mc_js" onClick="selectRole();" style="width:100%" readonly=true value="${js_mc}" >
             </td>
          </tr>
          <tr >
            <td width="20%" height="25" class="c_text"><div align="right">登录名称：</div></td>
            <td width="30%"><input type="text" id="dlmc" name="dlmc" style="width:100%;ime-mode:disabled;" maxlength="15" required="true" showName="登陆名称" value="${dlmc}"/>
            <td width="20%" class="c_text"><div align="right">中文名称：</div></td>
            <td width="30%">
            	<input type="text" name="xm" id = "xm" style="width:100%" maxlength="20" required="true" showName="中文名称" value="${xm}"/>
               
          </tr>
          <tr  id="pwdtr" style="display:none">
            <td width="20%" height="25" class="c_text"><div align="right" >登录密码：</div></td>
            <td width="30%"><input type="password" name="dlmm" id="dlmm" style="width:100%;ime-mode:disabled" showName="密码" value="${dlmm}"/>
            <td width="20%" class="c_text"><div align="right">密码确认：</div></td>
            <td width="30%"><input type="password" name="dlmm2" id="dlmm2" style="width:100%;ime-mode:disabled" showName="密码确认" value="${dlmm}"/>
             
          </tr>
         <tr>
            <td width="20%" height="25" class="c_text"><div align="right">工号：</div></td>
            <td width="30%" ><input type="text" name="kz321" id="kz321" style="width:100%"  value="${kz321}"/></td>
            <td width="20%" class="c_text"><div align="right">分机号：</div></td>
            <td width="30%"><input type="text" name="kz322" id="kz322"  style="width:100%"  value="${kz322}"/></td>
          </tr>
          <tr>
          	<td width="20%" height="25" class="c_text"><div align="right">单位性质：</div></td>
            <td width="30%" >
            	<select id="kz400" name="kz400" style="width:100%">
            		<option value=""></option>
            		<option value="szbm">市直部门</option>
            		<option value="xzbm">县直部门</option>
            		<option value="xz">乡镇</option>
            	</select>
            </td>
          </tr>
          <tr>
            <td width="20%" height="25" class="c_text"><div align="right">出生日期：</div></td>
            <td width="30%"><input type="text" name="birthday" id="birthday" style="width:100%" onClick="WdatePicker()" value="${birthday}"/>
            <td width="20%" class="c_text"><div align="right">性别：</div></td>
            <td width="30%"><input type="radio"  name="sex" value="1" checked/>
              男&nbsp;&nbsp;
              <input type="radio"  name="sex" value="0"/>
              女</td>
          </tr>
          <tr style="display:none">
            <td width="20%" height="25" class="c_text"><div align="right">家庭地址：</div></td>
            <td width="80%" colspan="3"><input type="text" name="jtdz" id ="jtdz" maxlength="50" style="width:82%" value="${jtdz}"/></td>
          </tr>
          <tr>
            <td width="20%" height="25" class="c_text"><div align="right">家庭邮编：</div></td>
            <td width="30%"><input type="textt" name="yb" id ="yb" style="width:100%" maxlength="6" check="isZip" value="${yb}"/></td>
              <td width="20%" class="c_text"><div align="right">电子邮箱：</div></td>
            <td width="30%"><input type="text" name="email" id ="email" style="width:100%" maxlength="50" check="isEmail" value="${email}"/></td>
          </tr>
          <tr>
            <td width="20%" height="25" class="c_text"><div align="right">移动电话：</div></td>
            <td width="30%"><input type="text" name="sj" id="sj" style="width:100%" maxlength="50"  value="${sj}"/></td>
            <td width="20%" class="c_text"><div align="right">办公电话：</div></td>
            <td width="30%"><input type="text" name="jtdh" id="jtdh" maxlength="30" style="width:100%" check="isPhone" value="${jtdh}"/></td>
          </tr>
          <tr>
            <td width="20%" height="25" class="c_text"><div align="right">备注信息：</div></td>
            <td width="35%" colspan="3">
            <textarea  name="bz" id="bz" style="width:100%" rows="3"><c:out value="${bz}"/></textarea>
        </td>
        </tr>
	</table>
	</form>
	<div style="text-align:center;">
		<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="save('yh')">保存</a>
		<a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)" onclick="cancel('yh')">取消</a>
		<a class="easyui-linkbutton" iconCls="icon-redo" href="javascript:void(0)" onclick="redo()">重置密码</a>
	</div>
	<div id="roleList" name="roleList" class="easyui-window" closed="true" inline="true" minimizable="false"
				maximizable="false" title="角色选择" style="top:20px;width:240px;height:130px;" iconCls="icon-tip">	
		<table width="100%" class="table_show">
			<tr>
				<td>角色列表</td>
				<td>
				<select id="roles" name="roles" style="width:120px">
				</select></td>
			</tr>
			<!-- 
			<tr>
				<td>授权部门</td>
				<td>&nbsp;&nbsp;
					<input type=radio name="qxbz">是
					&nbsp;
					<input type=radio name="qxbz" selected>否
				</td>
			</tr>
			<tr id="select_dep" name="select_dep">
				<td>部门列表</td>
				<td>
				<select id="deplist" name="deplist" style="width:120px">
				</select></td>
			</tr>
			 -->
		</table>
		<br/>
		<div style="text-align:center;">
			<a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0)" onclick="selected()">保存</a>
		</div>
	</div>
	</div>
</div>
</body>
</html>
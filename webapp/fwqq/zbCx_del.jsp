<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.coffice.util.cache.Cache"%>  
<html>
 <head>
   	<%@ include file="/common/common.jsp" %>
   	<% 
   		String yhid=(String)request.getAttribute("yhid");
   		String bmid=(String)Cache.getUserInfo(yhid,"bmid");
   	%>
   	<script>
   		var yhid = '<%=yhid%>';
		var curbmid = '<%=bmid%>';
	</script>
	<script src="zbCx_del.js" type="text/javascript"></script>
	<title>转办查询</title>
  </head>
  <body id="bodyid">
   <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="综合查询" iconCls="icon-search"  collapsed = "true" collapsible="true">
		<form id="form_search">   
				<label>来电日期：
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjq" id="cx_sjq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				--
				<label>
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjz" id="cx_sjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				<label id="cx_dw" style="display:none">
        			<select id="cx_bm" name="cx_bm" style="width:140px">
        				<option value="">---区县---</option>
        				<option value="321_61221">市级直属</option>
        				<option value="321_61198">崆峒区</option>
        				<option value="321_61199">泾川县</option>
        				<option value="321_61200">灵台县</option>
        				<option value="321_61202">华亭县</option>
        				<option value="321_61203">庄浪县</option>
        				<option value="321_61204">静宁县</option>
        			</select>
				</label>
				<label>
					<select id="cx_blhj" name="cx_blhj" style="width:140px">
        				<option value="">--办理环节--</option>
        				<option value="3">坐席受理</option>
        				<option value="51">市批办中心批办</option>
        				<option value="52">县批办中心批办</option>
        				<option value="57">市级部门办理</option>
        				<option value="93">县级部门办理</option>
        				<option value="180">县批办审核承诺</option>
        				<option value="182">市批办审核承诺</option>
        				<option value="115">县批办中心审核上报</option>
        				<option value="98">市批办中心审核办结</option>
        				<option value="75">市级部门重办</option>
        				<option value="53">县批办中心重办</option>
        				<option value="94">县级部门重办</option>
        				<option value="178">领导批示</option>
        				<option value="179">县领导批示</option>
        				<option value="59">回访坐席回访</option>
        				<option value="4">办结归档</option>
        			</select>
				</label>  
				<label>
					<select id="cx_blzt" name="cx_blzt" style="width:120px">
        				<option value="">--办理状态--</option>
        				<option value="0">办理中</option>
        				<option value="1">已办结</option>
        				<option value="2">已超期</option>
        				<option value="3">超时办结</option>
        			</select>
				</label>
				<label>
					<select id="cx_bllx" name="cx_bllx" style="width:120px">
        				<option value="">--办件类型--</option>
        				<option value="402101">限时件</option>
        				<option value="402102">承诺件</option>
        				<option value="402103">联办件</option>
        				<option value="402104">督办件</option>
        			</select>
				</label>  
				<label>
					<select id="cx_xzfl" name="cx_xzfl" style="width:120px">
        				<option value="">--性质分类--</option>
        				<option value="200501">咨询</option>
        				<option value="200503">投诉</option>
        				<option value="200504">求助</option>
        				<option value="200507">建议</option>
        			</select>
				</label> 
				<label>
					<select id="cx_xxly" name="cx_xxly" style="width:120px">
        				<option value="">--信息来源--</option>
        				<option value="200601">电话</option>
        				<option value="200603">短信</option>
        			</select>
				</label> 
				<br/>
				<label>工单编号：<input type="text" id="slbh_day" name="slbh_day" style="width:140px"/></label>
				<label>来电号码：<input type="text" id="ldhm" name="ldhm" style="width:140px"/></label>
				<label>来电人姓名：<input type="text" id="ldr" name="ldr" style="width:140px"/></label>
				<label><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="query()">搜索</a></label>
				<label><a class="easyui-linkbutton" iconCls="icon-sum" href="javascript:void(0)" onclick="exportXls()">导出</a></label>
			</form>
		</div>
 		<div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="快捷查询" iconCls="icon-search"   collapsible="true">
 			<label id="cx_dw1" style="display:none">统计单位：
        			<select id="cx_bm1" name="cx_bm1" style="width:140px" onchange="queryKj(1)" >
        				<option value="">---县区---</option>
        				<option value="321_61221">市级直属</option>
        				<option value="321_61198">崆峒区</option>
        				<option value="321_61199">泾川县</option>
        				<option value="321_61200">灵台县</option>
        				<option value="321_61202">华亭县</option>
        				<option value="321_61203">庄浪县</option>
        				<option value="321_61204">静宁县</option>
        			</select>
				</label>
				<label>
					<select id="cx_blhj1" name="cx_blhj1" style="width:140px" onchange="queryKj(1)">
        				<option value="">--办理环节--</option>
        				<option value="3">坐席受理</option>
        				<option value="51">市批办中心批办</option>
        				<option value="52">县批办中心批办</option>
        				<option value="57">市级部门办理</option>
        				<option value="93">县级部门办理</option>
        				<option value="180">县批办审核承诺</option>
        				<option value="182">市批办审核承诺</option>
        				<option value="115">县批办中心审核上报</option>
        				<option value="98">市批办中心审核办结</option>
        				<option value="75">市级部门重办</option>
        				<option value="53">县批办中心重办</option>
        				<option value="93">县级部门办理</option>
        				<option value="178">领导批示</option>
        				<option value="179">县领导批示</option>
        				<option value="59">回访坐席回访</option>
        				<option value="4">办结归档</option>
        			</select>
				</label>   
				<label>
					<select id="cx_blzt1" name="cx_blzt1" style="width:140px" onchange="queryKj(1)">
        				<option value="">--办理状态--</option>
        				<option value="0">办理中</option>
        				<option value="1">已办结</option>
        				<option value="2">已超期</option>
        				<option value="3">超时办结</option>
        			</select>
				</label>  
				<label><a class="easyui-linkbutton" iconCls="icon-sum" href="javascript:void(0)" onclick="exportKjXls()">导出</a></label> 	
 		</div>
		<div id="datalistdiv" class="easyui-panel">
			<table id="datalist" style="height:auto"  title="承办单列表"  singleSelect="true"
				idField="itemid" iconCls="icon-tip">		
			</table>
			<div id="datadiv_page">
				 <%@ include file="/common/pagination.jsp" %>
			</div>
		<div>
	</body>
</html>

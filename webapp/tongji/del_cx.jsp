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
	<script src="del_cx.js" type="text/javascript"></script>
	<title>转办查询</title>
  </head>
  <body id="bodyid">
   <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="综合查询" iconCls="icon-search"   >
		<form id="form_search">   
				<label>来电日期：
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjq" id="cx_sjq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				--
				<label>
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjz" id="cx_sjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
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

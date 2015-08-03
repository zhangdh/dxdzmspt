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
	<script src="cdps.js" type="text/javascript"></script>
	<title>领导查单批示</title>
  </head>
  <body id="bodyid">
   <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="综合查询" iconCls="icon-search"   collapsible="true">
		<form id="form_search">   
				<label>来电日期：
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjq" id="cx_sjq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				--
				<label>
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjz" id="cx_sjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				<label id="cx_dw">
        			<select id="cx_bm" name="cx_bm" style="width:140px">
        				<option value="">---区县---</option>
        				<option value="321_64650">市本级</option>
        				<option value="321_64651">安定区</option>
        				<option value="321_64652">临洮县</option>
        				<option value="321_64654">岷县</option>
        				<option value="321_64656">渭源县</option>
        				<option value="321_64657">漳县</option>
        				<option value="321_64653">陇西县</option>
        				<option value="321_64655">通渭县</option>
        			</select>
				</label>
				<label>
					<select id="cx_blhj" name="cx_blhj" style="width:140px">
        				<option value="">--办理环节--</option>
        				<option value="3">坐席受理</option>
        				<option value="51">市批办中心批办</option>
        				<option value="52">县批办中心批办</option>
        				<option value="57">市直部门办理</option>
        				<option value="75">市直部门重办</option>
        				<option value="93">县级部门办理</option>
        				<option value="94">县级部门重办</option>
        				<option value="182">市批办审核延期</option>
        				<option value="183">市批办审核</option>
        				<option value="115">县批办中心审核</option>
        				<option value="59">回访坐席回访</option>
        				<option value="98">市批办中心审核办结</option>
        				<option value="4">办结归档</option>
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
		 
				<label>工单编号：<input type="text" id="slbh_day" name="slbh_day" style="width:140px"/></label>
				<label>来电号码：<input type="text" id="ldhm" name="ldhm" style="width:140px"/></label>
				<label>来电人姓名：<input type="text" id="ldr" name="ldr" style="width:140px"/></label>
				<input type="checkbox" id ="yps" name="yps">已批示
				<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label> 
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
		<div id="dataedit" class="easyui-window" closed="true" title="领导意见" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close" 
					style="top:80px;width:700px;height:250px;left:100px" iconCls="icon-tip">
		<div class="div_oper">
			<a href="javascript:void(0)" id="btn_save" class="easyui-linkbutton" onclick="save()">保存</a>
		</div>
		<table class="table_show">
			<tr>
				<td align="right" style="width:80px;height:150px">领导意见：</td>
				<td style="width:620px">
					<textarea style="WIDTH: 100%;height:150px"   name="ldyj" id="ldyj" required = true  showName="领导意见">
					</textarea>
				</td>
	       	</tr>
		</table>
	</div>	
	</body>
</html>

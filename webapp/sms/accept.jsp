<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
 <head>
   	<%@ include file="/common/common.jsp" %>
	<script src="accept.js" type="text/javascript"></script>
	<title>服务请求录入</title>
  </head>
  <body>
    <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>来电号码：<input type="text" id="cxhm" name="cxhm" style="WIDTH: 120px; HEIGHT: 20px; "></label>		  
			<label>收到日期：
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cjsjq" id=""cjsjq"" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
			--
			<label>
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cjsjz" id="cjsjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
       		<label><a class="easyui-linkbutton" iconCls="icon-search" href="javascript:void(0)" onclick="query()">搜索</a></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="短信列表"  singleSelect="true"
			idField="itemid" iconCls="icon-tip">		
		</table>
		<div id="datadiv_page">
			 <%@ include file="/common/pagination.jsp" %>
		</div>
	<div>
	<div id="dataedit" class="easyui-window" closed="true" title="信息详情" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close" 
					style="top:80px;width:690px;height:360px;left:100px" iconCls="icon-tip">
		<div class="div_oper">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="replay()">回复办结</a>
			<a href="javascript:void(0)" id="btn_save" class="easyui-linkbutton" onclick="zb()">交办</a>
		</div>
		<form id="form_show">
			<table class="table_show">
				<input type=hidden id="id" name="id">
				<tr>
					<td align="right" style="width:70px">短信号码：</td>
					<td style="width:620px;hight:300px">
						<input class="biinput" maxlength="50" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="dxhm" id="dxhm" />
					</td>
	         	</tr>
	         	<tr>
	         		<td align="right" style="width:70px">短信内容：</td>
	         		<td style="width:620px;hight:300px">
	         			<textarea id="dxnr" name="dxnr" rows="6" style="WIDTH: 100%;"></textarea>
	         		</td>
	         	</tr>	
	         	<tr>
	         		<td align="right" style="width:70px">回复内容：</td>
	         		<td style="width:620px;hight:300px">
	         			<textarea id="replay_content" name="replay_content" rows="6" style="WIDTH: 100%;"></textarea>
	         		</td>
	         	</tr>	
			</table>
		</form>
	</div>	
  </body>
</html>

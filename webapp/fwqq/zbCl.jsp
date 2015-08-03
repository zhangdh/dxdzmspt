<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
 <head>
   	<%@ include file="/common/common.jsp" %>
	<script src="zbCl.js" type="text/javascript"></script>
	<title></title>
  </head>
  <body id="bodyid">
   <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">      	
				<label>来电人：<input type="text" id="cxlxrxm" name="cxlxrxm" style="width:100px"/></label>
				<label>来电号码：<input type="text" id="cxldhm" name="cxldhm" style="width:100px"/></label>
				<label>工单标题：<input type="text" id="title" name="title" style="width:100px"/></label>
				<label>来电时间：
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjq" id="cx_sjq" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label>
				--
				<label>
				<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cx_sjz" id="cx_sjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
				</label> 
				<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
			</form>
		</div>

		<div id="datalistdiv" class="easyui-panel">
			<table id="datalist" style="height:auto"  title="工单列表"  singleSelect="true"
				idField="itemid" iconCls="icon-tip">		
			</table>
			<div id="datadiv_page">
				 <%@ include file="/common/pagination.jsp" %>
			</div>
		<div>
		<div id="dataedit" class="easyui-window" closed="true" title="信息编辑" iconCls="icon-tip" 
					collapsible="close" minimizable="close" maximizable="close" 
					style="top:80px;width:800px;height:520px;left:100px" iconCls="icon-tip">
		<div class="div_oper">
			<a href="javascript:void(0)" class="easyui-linkbutton" id="modi" onclick="modiFwqq()">保存修改</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" id="modiEnd" onclick="modiFwqqEnd()">办结归档</a>
			<a href="javascript:void(0)" id="btn_zb" class="easyui-linkbutton" style="display:none" onclick="zb()">转办申请</a>
		</div>
		<form id="form_show">
			<input type=hidden id="guid" name="guid">
			<input type=hidden id="lypath" name="lypath">
			<input type=hidden id="ldhm" name="ldhm">
			<table class="table_show">
				<tr>
					<td align="right" style="width:80px">来电人姓名：</td>
					<td style="width:300px">
						<input class="biinput" maxlength="20" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="ldrxm" id="ldrxm" showName="姓名"/>
					</td>
					<td align="right" style="width:80px">联系电话：</td>
					<td style="width:300px">
						<input class="biinput" maxlength="20" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="lxhm" id="lxhm" showName="联系电话"/>
					</td>
					
	         	</tr>
	         	<tr>
					<td align="right">联系地址：</td>
		     	    <td ><input class="biinput" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="ldrdz" id="ldrdz" showName="地址"/></td>
		     	    <td align="right" >事发地址：</td>
		     	    <td ><input class="biinput" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="sfdz" id="sfdz" showName="事发地址"/></td>
		     	</tr>
		     	<tr>
		     		<td align="right">内容类别：</td>
					<td>
						<select style="WIDTH:100%; HEIGHT: 20px" name="lb_dm" id="lb_dm" showName="内容类别">
						</select>
					</td>
					<td align="right">承办类别：</td>
					<td>
						<select  style="WIDTH:100%; HEIGHT: 20px" name="cblb_dm" id="cblb_dm" showName="承办类别" onchange=cblx(this.value)>
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">性质分类：</td>
					<td>
						<select style="WIDTH: 100%; HEIGHT: 20px" name="xzfl" id="xzfl" showName="性质分类">
						</select>
					</td>
					<td align="right">信息来源：</td>
					<td>
						<select   style="WIDTH: 100%; HEIGHT: 20px" name="xxly" id="xxly" showName="信息来源">
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">是否保密：</td>
					<td>
						<select style="WIDTH: 100%; HEIGHT: 20px" name="sfbm" id="sfbm" showName="是否保密">
						</select>
					</td>
					<td align="right">通话类别：</td>
					<td>
						<select   style="WIDTH: 100%; HEIGHT: 20px" name="thlb_dm" id="thlb_dm" showName="通话类别">
							<option value="8400">呼入</option>
							<option value="8401">呼出</option>
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">无效类型：</td>
					<td>
						<select style="WIDTH: 300px; HEIGHT: 20px" name="wxlx" id="wxlx"   showName="无效类型">
						</select>
					</td>
		     	</tr>
	         	<tr>
					<td align="right" >标题：</td>
		     	    <td colspan="3">
		     	    	<input style=" width:100%; HEIGHT: 20px" name="bt" id="bt" showName="标题"/>
		     	    </td>
		     	</tr>
		     	<tr>
		     	    <td align="right" >内容：</td>
		     	    <td colspan="3">
		     	    	<textarea style="WIDTH: 100%;" rows="6" name="nr" id="nr" showName="内容"></textarea>
		     	    </td>
		     	</tr>	
		     	
	         	<tr>
	         		<td align="right">处理结果：</td>
	         		<td colspan="3">
	         			<textarea id="cljg" name="cljg" rows="6" style="WIDTH: 100%;"></textarea>
	         		</td>
	         	</tr>	
			</table>
		</form>
	</div>	
	</body>
</html>

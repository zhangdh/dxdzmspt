<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
 <head>
   	<%@ include file="/common/common.jsp" %>
	<script src="fwqq.js" type="text/javascript"></script>
	<title>服务请求录入</title>
  </head>
  <body>
    <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
		<form id="form_search">
        	<label>来电号码：<input type="text" id="cxldhm" name="cxldhm" style="WIDTH: 120px; HEIGHT: 20px; "></label>		  
			<td width="18%" align="left" ><label>来电人：</label></td>
			<td width="32%"><input class="biinput" maxlength="130" size="15"  style="WIDTH: 120px; HEIGHT: 20px" name="cxlxrxm" id="cxlxrxm" showName="来电人"/></td>
			<label>创建日期：
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cjsjq" id=""cjsjq"" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
			--
			<label>
			<input class="Wdate" style="WIDTH: 120px; HEIGHT: 20px" type="text" name="cjsjz" id="cjsjz" onFocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true})" readonly="readonly"/>
			</label>
       		<label><input type= "button" class="btn_query" onclick="query()" style="width:62px" value="搜索"/></label>
		</form>
	</div>
	<div id="datalistdiv" class="easyui-panel">
		<table id="datalist" style="height:auto"  title="来电记录"  singleSelect="true"
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
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="newFqq()">新增</a>
			<a href="javascript:void(0)" id="btn_save" class="easyui-linkbutton" onclick="save()">保存</a>
			<a href="javascript:void(0)" id="btn_modi" class="easyui-linkbutton" onclick="modiFwqq()">保存修改</a>
			<a href="javascript:void(0)" id="btn_end" class="easyui-linkbutton" onclick="saveEnd()">保存办结</a>
			<a href="javascript:void(0)" id="btn_modiend" class="easyui-linkbutton" onclick="modiFwqqEnd()">修改办结</a>
			<a href="javascript:void(0)" id="btn_zb" class="easyui-linkbutton" style="display:none" onclick="zb()">转办申请</a>
		</div>
		<form id="form_show">
			<table class="table_show">
			<input type=hidden id="guid" name="guid">
			<input type=hidden id="lypath" name="lypath">
			<input type=hidden id="ldhm" name="ldhm">
				<tr>
					<td align="right" style="width:80px">来电人姓名：</td>
					<td style="width:300px">
						<input class="biinput" maxlength="50" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="ldrxm" required = true id="ldrxm" showName="姓名"/>
					</td>
					<td align="right" style="width:80px">联系电话：</td>
					<td style="width:300px">
						<input class="biinput" maxlength="50" size="15"  style="WIDTH:100%; HEIGHT: 20px" name="lxhm" required = true id="lxhm" showName="联系电话"/>
					</td>
	         	</tr>
		     	<tr>
		     		<td align="right">联系地址：</td>
		     	    <td colspan="3">
		     	    	<select  id="sf" name="sf"  style="WIDTH: 100px; HEIGHT: 20px; "    showName="省份">
		     	    		<option value="620000" selected>甘肃省</option>
		     	    	</select>
	         			<select  id="ds" name="ds" style="WIDTH: 100px; HEIGHT: 20px; "    showName="地区">
	         				<option value="621100" selected>定西市</option>
	         			</select>
	         			<select  id="xq" name="xq" style="WIDTH: 100px; HEIGHT: 20px; " onchange="get_xz()"  showName="县区"></select>
	         			<select  id="xz" name="xz" style="WIDTH: 100px; HEIGHT: 20px; " showName="乡镇"  ></select>
		     	    	<input class="biinput"   style="WIDTH:255px; HEIGHT: 20px" name="xxdz" required = true  id="xxdz" showName="详细地址"/>
		     	    </td>
		     	</tr>
		     	<tr>
		     		<td align="right">内容类别：</td>
					<td>
						<select style="WIDTH:300px; HEIGHT: 20px" name="lb_dm" id="lb_dm" required = true  showName="内容类别">
						</select>
					</td>
					<td align="right">承办类型：</td>
					<td>
						<select  style="WIDTH:300px; HEIGHT: 20px" name="cblb_dm" id="cblb_dm" required = true  showName="承办类别" onchange=cblx(this.value)>
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">性质分类：</td>
					<td>
						<select style="WIDTH:300px; HEIGHT: 20px" name="xzfl" id="xzfl" required = true  showName="性质分类">
						</select>
					</td>
					<td align="right">信息来源：</td>
					<td>
						<select   style="WIDTH:300px; HEIGHT: 20px" name="xxly" id="xxly" required = true  showName="信息来源">
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">是否匿名：</td>
					<td>
						<select style="WIDTH:300px; HEIGHT: 20px" name="sfbm" id="sfbm" required = true  showName="是否保密">
						</select>
					</td>
					<td align="right">通话类型：</td>
					<td>
						<select   style="WIDTH:300px; HEIGHT: 20px" name="thlb_dm" id="thlb_dm" showName="通话类别">
							<option value="8400">呼入</option>
							<option value="8401">呼出</option>
						</select>
					</td>
		     	</tr>
		     	<tr>
		     		<td align="right">无效类型：</td>
					<td>
						<select style="WIDTH: 300px; HEIGHT: 20px" name="wxlx" id="wxlx"  showName="无效类型">
						</select>
					</td>
		     	</tr>
	         	<tr>
					<td align="right" >标题：</td>
		     	    <td colspan="3">
		     	    	<input style=" width:100%; HEIGHT: 20px" name="bt" id="bt" required = true  maxlength = '25' showName="标题"/>
		     	    </td>
		     	</tr>
		     	<tr>
		     	    <td align="right" >内容：</td>
		     	    <td colspan="3">
		     	    	<textarea style="WIDTH: 100%;" rows="6" name="nr" id="nr" required = true  showName="内容"></textarea>
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

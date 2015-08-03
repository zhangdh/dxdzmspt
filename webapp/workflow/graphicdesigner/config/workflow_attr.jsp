<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<html>
  <head>
    <title>流程属性</title>
  </head>
  <script charset="UTF-8" src="${ctx}/js/XiorkFlow/js/XiorkFlowWorkSpace.js" language="javascript"></script>
  <script>
  		var obj = window.dialogArguments;
  		var wrapper = obj.xiorkFlow.getWrapper();
		var toolbar = obj.xiorkFlow.getToolBar();
		var model = wrapper.getModel();
		//页面加载
		window.onload=function(){
			 //匹配模板
		     var modelId=wrapper.selectedModel;
	         var radio=document.getElementsByName("roleIds");
	         for(var i=0;i<radio.length;i++){
	           var r=radio[i];
	           if(r.value==modelId){
	             r.checked=true;
	           } 
	         }
	         //匹配流程类别
	         var wkTypeId=wrapper.selectedWkType;
	         var wkTypeRadio=document.getElementsByName("wkTypes");
	         for(var i=0;i<wkTypeRadio.length;i++){
	           var r=wkTypeRadio[i];
	           if(r.value==wkTypeId){
	             r.checked=true;
	           } 
	         }
		}
		
  		//tab页切换
  		function s_chg(td_now, mCols){
		  //模板
		  if(td_now.id == "s_td_1"){
		    document.all.s_td_1.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    
		    document.all.tabModel.style.display="";
		    document.all.tabWkType.style.display="none";

		  }
		  //流程类别
		  if(td_now.id == "s_td_2"){
		    document.all.s_td_1.background='${ctx}/oswf/resources/images/bg_td_hd100_2.gif';
		    document.all.s_td_2.background='${ctx}/oswf/resources/images/bg_td_hd100.gif';
		    
		    document.all.tabModel.style.display="none";
		    document.all.tabWkType.style.display="";
		  }
		}
		//设置表单ID
  		function setFormModel(){
  			wrapper.selectedModel=$('input[name=roleIds]:checked').val();
  		}
  		//设置流程类别ID
  		function setWkType(){
  			wrapper.selectedWkType=$('input[name=wkTypes]:checked').val();
  		}	
		//确定
  		function getSelect(){
  			//设置流程类别
  			if(wrapper.selectedWkType==""){
  				alert("请设置流程类别");
  				return false;
  			}
  			window.close();
  		}	
  </script>
  <body>
     <table width="100%"  border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100.gif" id="s_td_1" class="s_on" onClick="s_chg(this,1);" style="cursor:hand"><span class="style1">模板</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="100" valign="bottom" align="left">
			<table border="0" cellspacing="0" cellpadding="0" >
			 <tr valign="middle" align="center">
			   <td width="100" height="25" background="${ctx}/oswf/resources/images/bg_td_hd100_2.gif" id="s_td_2" class="s_on" onClick="s_chg(this,3);" style="cursor:hand"><span class="style1">流程类别</span></td>
			 </tr>
			</table>
	    </td>
	    <td width="90%" valign="bottom" align="left">&nbsp;</td>
	  </tr>
	</table>
	<div align="center">
		<table id="tabModel" class="c_table_list" width="100%" align="right" border=1>
			<tr align="center">
				<td colspan=2>选用模板列表</td>
			</tr>
			<c:if test="${empty  modelList}">
			<tr align="center">
				<td colspan=2><font color=red>无模板信息</font></td>
			</tr>
			</c:if>
			<c:if test="${!empty  modelList}">
				<c:forEach var="modelList" items="${modelList}">
					<tr align="center">
						<td><input type="radio" id="roleIds" name="roleIds" value="<c:out value="${modelList.id}" />" onClick="setFormModel();"/></td>
						<td><c:out value="${modelList.formName}" /></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<table id="tabWkType" class="c_table_list" width="100%" align="center" border=1 style="display:none">
			<tr align="center">
				<td colspan=2>选用流程类别列表</td>
			</tr>
			<c:if test="${empty  wkTypeList}">
			<tr align="center">
				<td colspan=2><font color=red>无流程类别信息</font></td>
			</tr>
			</c:if>
			<c:if test="${!empty  wkTypeList}">
				<c:forEach var="wkTypeList" items="${wkTypeList}">
					<tr align="center">
						<td><input type="radio" id="wkTypes" name="wkTypes" value="<c:out value="${wkTypeList.dm}" />" onClick="setWkType();"/></td>
						<td><c:out value="${wkTypeList.mc}" /></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		<div style=" float:left;width:100%;height:50px;margin-top:20px">
   		     	 <input type="button" onclick="getSelect();" value="确定" />
   		</div>
	</div>
  </body>
</html>

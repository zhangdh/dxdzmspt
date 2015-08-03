<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<html>
  <head>
    <title>流程属性</title>
  </head>
  <script>
  		var obj = window.dialogArguments;
  		var formId;
  		var wkType;
		//页面加载
		window.onload=function(){
			 //匹配模板
			 //设置选中表单
			if(obj.formId!="undefined"){
  		  		formId=obj.formId;
			    $("#tabModel").find("input").each(function(){
			       if(this.value==formId)
			       		this.checked="checked";
			    });
  			}
	        //匹配流程类别
	        //if(obj.wkType!="undefined"){
		        //wkType=obj.wkType;
		        //$("#tabWkType").find("input").each(function(){
			       //if(this.value==wkType)
			       		//this.checked="checked";
			    //});
	        //}
	        
		}
		
  		//tab页切换
  		function s_chg(td_now){
  		  $(".c_current_tab").removeClass("c_current_tab");
		  $("#"+td_now).parent().addClass("c_current_tab");
		  //模板
		  if(td_now== "s_td_1"){
		    document.all.tabModel.style.display="";
		    //document.all.tabWkType.style.display="none";

		  }
		  //流程类别
		  //if(td_now== "s_td_2"){
		    //document.all.tabModel.style.display="none";
		    //document.all.tabWkType.style.display="";
		  //}
		}
		//设置表单ID
  		function setFormModel(){
  			obj.formId=$('input[name=roleIds]:checked').val();
  		}
  		//设置流程类别ID
  		function setWkType(){
  			obj.wkType=$('input[name=wkTypes]:checked').val();
  		}	
		//确定
  		function getSelect(){
  			//设置流程类别
  			//if(obj.wkType==undefined){
  				//alert("请设置流程类别");
  				//return false;
  			//}
  			window.returnValue=obj;
  			window.close();
  		}	
  </script>
  <body>
  	  <div id="c_tab">
		  <ul>
		    <li class="c_current_tab" ><a id="s_td_1" href="#" onClick="s_chg('s_td_1');"><span>模板</span></a></li>
		    <!--  
		    <li><a id="s_td_2" onClick="s_chg('s_td_2');" href="#"><span>流程类别</span></a></li>
		    -->
		  </ul>
	 </div>
	<div align="center">
		<table id="tabModel" class="c_table_list" width="90%" bordercolor="black" align="center" cellspacing="0" cellpadding="1" border=1>
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
		<!--  
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
		-->
		<div style=" float:left;width:100%;height:50px;margin-top:20px">
   		     	 <input type="button" onclick="getSelect();" value="确定" />
   		</div>
	</div>
  </body>
</html>

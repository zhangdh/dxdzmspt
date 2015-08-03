<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ include file="/common/common.jsp" %>
<c:set var="webcontext" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css"  href="../js/ztree/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="../js/ztree/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="../js/ztree/jquery.ztree.excheck-3.5.js"></script>
	<script src="knowledge.js" type="text/javascript"></script>
	<script>
		var webcontext = "${webcontext}"; 
	</script>
</head>
<body>
<div class="easyui-layout" style="width:100%;height:100%;">
	<div region="west"  split="true" title="知识目录" style="width:200px;">
		<ul id="kbList" name="kbList" class="ztree"></ul>
	</div>
	<div region="center"   class="easyui-panel">
	    <div id="p" class="easyui-panel" style="height:auto;padding:5px;" title="查询条件" iconCls="icon-search" collapsible="true">
			<form id="form_search">
	        	<label>关键字：<input type="text" id="cx_keyword" name="cx_keyword" style="WIDTH: 180px; HEIGHT: 20px; "></label>		  
	       		<label><input type= "button" class="btn_query" onclick="querySolr(1)"  style="width:62px" value="搜索"/></label>
			</form>
		</div>
		<div class="easyui-panel" id="datalistdiv">
			<table id="datalist" style="height:auto"   title="知识条目" iconCls="icon-tip"></table>
			<div id="datadiv_page">
				 <%@ include file="/common/pagination.jsp" %>
			</div>			
		</div>
		<div class="easyui-panel" id="solrlistdiv" style="display:none">
			<table id="solrlist" width="100%" cellpadding="4" cellspacing="4">
               <tbody>
               <tr style="display:none;"><!--模板行-->
					<td  style="cursor:hand;border-bottom: solid 1px gray;"><br>
						<div style="font-size: medium;font-weight: normal;font-family: arial">
						<span style="width: 50%;text-align: left;">问题:&nbsp;<a href="javaScript:void(0);" onclick="openMx('{kid}');" style="text-decoration: underline;font-size: 15;">{kbt}</a></span>				
						<div style="line-height: 25px;height: 25px;font-size: 13px;color: gray;">回答:&nbsp;{knr}&nbsp;&nbsp </div></div>
					</td>
                </tr>
                </tbody>
            </table>
			<div id="datadiv_page">
				 <%@ include file="/common/pagination.jsp" %>
			</div>			
		</div>
	</div>
</div>
</body>
</html>
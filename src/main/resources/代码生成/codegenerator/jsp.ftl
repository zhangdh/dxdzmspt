<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/js.jsp"%>
		<script src="default.js" type="text/javascript"></script>
		<#if attachment??>
		<script type="text/javascript"  src=${r"${ctx}/js/attachment.js"}></script>
		</#if>
		<#if KE??>
	    <script type="text/javascript"  src=${r"${ctx}/js/kindeditor/kindeditor.js"}></script>
	    </#if>
	    <#if tx??>
	    <script type="text/javascript"  src=${r"${ctx}/remind/js/tx.js"}></script> 
	    </#if>
	    <#if wdatepicker??>
	    <script type="text/javascript"  src=${r"${ctx}/js/datePicker/WdatePicker.js"}></script>
	    </#if>
	    <#if KE??>
	    <script type="text/javascript">
		    KE.show({
		        id : 'content',
		        resizeMode : 1,
		        skinType: 'tinymce',
		        items : [
		        'fullscreen','undo', 'redo','cut', 'copy', 'paste',
		        'plainpaste', 'wordpaste', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
		        'emoticons',  'title', 'fontname', 'fontsize', '-',
		        'textcolor', 'bgcolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'removeformat', 'selectall', 'image',
		        'flash', 'media', 'hr',
    			]
		        
		    });
	     </script>
	     </#if>
	</head>
	<body id="bodyid">
		<!--每个收缩单元中的内容放在不同的div中且指定class，并在页面底部用include包含 -->
		<div id="div_query" class="c_table_bar_content"><!-- 注意如有需要id可自行命名但一般用默认值即可，和class命名规则 -->
			<form id="form_query"><!-- 注意form id命名规则 -->
				<!-- 注意加label -->
				<#list cxtj as t>
				${t.cxzd}
				</#list>
				<input type="button" id="btn_query" value="搜索" onClick="query();">
			</form>
		</div>

		<div id="div_list" class="c_table_bar_content">
		<div id="div_table_list"><!-- 此id命名必须为div_+table的id -->
			<div class="c_div_table_list">
				<table id="table_list" class="c_table_list" width="100%" border="1">
					<thead id="table_list_thead">
						<tr>
						  <td width="11%" style="display:none">guid</td>
                            <td width="4%">&nbsp;</td>
                            <#list cxjgzd_zw as mc>
                            <td>${mc}</td>
                            </#list>
                        </tr>
					</thead>
					<tbody>
						<tr style="display:none">
						  <td style="display:none">{guid}</td>
							<td><input type="checkbox" name="checkbox_guid" value="{guid}"></td>
							<#list cxjgzd as zd>
                            <td>{${zd}}</td>
                            </#list>
						</tr>																																				
					</tbody>
				</table>
		  </div>
			<div class="c_div_table_list_nav">
				<div class="c_table_list_nav_btn">
				<input type=button value="反选" onClick="$('#table_list').toggleCheckboxes();">
				<input type=button value="删除" onClick="list_del()">
				</div>
				<%@ include file="/common/table_list_nav.jsp"%>
			</div>
		  </div>
		</div>
		<#if div_show??>
		<div id="div_show" class="c_table_bar_content">
			<div class="c_table_show_btn">
				<!-- 需要权限控制的按钮都要加入名为c_btn_auth的class -->
			   <#list btn as b>
			  <input type="button" <#if b.c_btn_auth??>class="c_btn_auth"</#if> id="${b.id}" value="${b.value}" onClick="${b.onClick}"/>
			   </#list>
			</div>
			<form id="form_show"><!-- 用固定宽度的table定位各元素位置 -->
			   <input type="hidden" name="guid" id="guid">
				<table width="800px" class="c_table_show" border="0" cellpadding="0">
				<#list form_zd as zd>
					<tr>
						<td><label><#if zd.lable??>${zd.lable}</#if></label></td>
						<td>${zd.zd}</td>
					</tr>
				</#list>
				</table>
			</form>

		</div>
		</#if>
		<!-- includes要在div的下面 -->
		<jsp:include page="/common/table_bar.jsp">
			<jsp:param name="div_id" value="div_query" />
			<jsp:param name="bar_title" value="查询" />
		</jsp:include>
		<jsp:include page="/common/table_bar.jsp">
			<jsp:param name="div_id" value="div_list" />
			<jsp:param name="bar_title" value="查询结果" />
		</jsp:include>
		<#if div_show??>
		<jsp:include page="/common/table_bar.jsp">
			<jsp:param name="div_id" value="div_show" />
			<jsp:param name="bar_title" value="数据显示" />
		</jsp:include>
		</#if>
	</body>
</html>

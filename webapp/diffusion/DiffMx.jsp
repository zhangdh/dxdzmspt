<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/common.jsp" %>
<%
String guid=request.getParameter("guid")!=null?(String)request.getParameter("guid"):""; 
%>
<html>
<head>
  <title>信息发布查看</title>
  <script>
 	$(function(){
 		_Post("/diffusion.coffice?method=showJs","guid=<%=guid%>",function(jsonData){
 	   	 	$("#ggzt").text(jsonData.zt);
 	    	$("#ggnr").html(jsonData.nr);
 	    	$("#yhid").text(jsonData.fbr);
 	    	$("#cx_yxqq").text(jsonData.yxqq);
 	    	$("#cx_yxqz").text(jsonData.yxqz);
 	    	displayFj(jsonData);
		});
	});
	function displayFj(jsonData){
		var hostIp = "http://"+window.location.host;
		var fjStr = jsonData.fjStr;
		if(jsonData.ifFj == "1"){
			var diffusion_dir = jsonData.diffusion_dir;
			var infolj = diffusion_dir.split(":")[1];
			fjStr = eval("("+fjStr+")");
			$.each(fjStr,function(k,v){
				var wjmc = "";
				var wjlj = "";
				$("#files").append("<a href='"+hostIp+infolj+v.wjlj+"' target='_blank' style='text-decoration:none;'>"+v.wjmc+"</a>&nbsp;&nbsp;");
		    });
		}	
	}
  </script>
<style type="text/css">
<!--
.style2 {	FONT-WEIGHT: bold; FONT-SIZE: 16px;
}
.style3 {	FONT-WEIGHT: bold; FONT-SIZE: 12px;
}
.style4 {	 FONT-SIZE: 12px; color:#C0BFBF;
}
.style5 {	 FONT-SIZE: 12px; color:#000000; 
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<script language="JavaScript">
//双击鼠标滚动屏幕的代码
var currentpos,timer;
function initialize()
{
timer=setInterval ("scrollwindow ()",30);
}
function sc()
{
clearInterval(timer);
}
function scrollwindow()
{
currentpos=document.body.scrollTop;
window.scroll(0,++currentpos);
if (currentpos !=document.body.scrollTop)
sc();
}
document.onmousedown=sc
document.ondblclick=initialize

</script>
</head>

<body>
<div id="div_show" class="c_table_bar_content">
<div class="c_table_show_btn">
        <!-- 数据按钮区起 -->
        <!-- InstanceBeginEditable name="form_show_button" -->
        <!-- InstanceEndEditable -->
        <!-- 数据按钮区止 -->
</div>
    <form id="form_show" >
        <!--数据明细区起-->
        <!-- InstanceBeginEditable name="form_show" -->
        <input type="hidden" name="guid" id="guid">
        <input type="hidden" name="zt_dm" id="zt_dm">
<table width="802" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td height="262" valign="top"><div align="center">
	<table width="656" border="0" cellspacing="0" cellpadding="0">
	  <tr>
        <td height="30"></td>
      </tr>
      <tr>
        <td><div align="center"><span class="style2"><span id="ggzt"></span></span></div></td>
      </tr>
      <tr>
        <td height="10"></td>
      </tr>
      <tr>
        <td><div align="center" class="style4">发布人：<span id="yhid" ></span>&nbsp;有效时间：<span  id="cx_yxqq"></span>至<span  id="cx_yxqz"></span>&nbsp;&nbsp;</div></td>
      </tr>
      <tr>
        <td height="16"></td>
      </tr>
      <tr>
        <td height="1" bgcolor="#C0BFBF"></td>
      </tr>
      <tr>
        <td height="16"></td>
      </tr>
    </table>
      <table width="656" border="0" cellpadding="0" cellspacing="0" class="style5">
        <tr>
          <td height="300" valign="top"><div><span id="ggnr" ></span></div></td>
        </tr>
      </table>
      <table width="656" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="16"></td>
        </tr>
      </table>
      <table width="656" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="391" class="style5"><div align="right"></div></td>
          <td width="56"></td>
        </tr>
      </table>
      <table width="656" border="0" cellspacing="0" cellpadding="0">
	  <tr>
        <td height="30"><a name="webtop"></a>&nbsp;</td>
      </tr>
      <tr>
        <td height="1" bgcolor="#C0BFBF"></td>
      </tr>
      <tr>
        <td height="16"></td>
      </tr>
    </table>
      <table width="656" border="0" cellpadding="0" cellspacing="0" class="style4">
        <tr>
          <td width="629" class="style5"><div align="right">附件信息：<span id="files" name="files" ></span></div></td>
        </tr>
      </table>
      <table width="656" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="16"></td>
        </tr>
      </table>
      
      </div></td>
  </tr>
</table>
        <!-- InstanceEndEditable -->
        <!--数据明细区止-->
        </form>
</div>
	</body>
</html>

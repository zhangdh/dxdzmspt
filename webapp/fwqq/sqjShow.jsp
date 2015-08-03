<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ page import="javax.servlet.http.HttpServletRequest"%>
<%@ page import="com.coffice.util.cache.Cache"%>  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/3tableBar.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/js.jsp" %>
    <!--页面私有js区域起-->
    <!-- InstanceBeginEditable name="head" -->
    <title>大屏幕显示</title>
    <script src="sqjShow.js" type="text/javascript"></script>
    <script src="js/datadumper.js" type="text/javascript"></script>
    <!--页面私有js区止起-->
   <style type="text/css">
	 #demo{overflow:hidden; width: 100%;height: 800px; background:#FFF;float: left;display: inline;}
	</style>
</head>
<body id="bodyid">
<table class="c_table_list" width="100%"><tr><td width='8%' class="lieb" align="center">受理人</td><td class="lieb" width='15%' align="center">受理时间</td><td class="lieb" width='10%' align="center">来电号码</td><td class="lieb" width='10%' align="center">标题</td><td class="lieb" width='8%' align="center">承办类型</td><td class="lieb" width='33%' align="center">内容</td><td class="lieb" width='8%' align="center">性质分类</td><td class="lieb" width='8%' align="center">信息来源</td></tr></table><!--
&nbsp;
<span id="marquees">
<table>
	<tr><td id="showContent"></td></tr>
</table>

</span>
-->
<div id="demo">
<div id="demo1">

</div>
<div id="demo2"> </div>
<script type="text/javascript">
 var speed=40;
 var FGDemo=document.getElementById('demo');
 var FGDemo1=document.getElementById('demo1');
 var FGDemo2=document.getElementById('demo2');
 FGDemo2.innerHTML=FGDemo1.innerHTML
 function Marquee1(){
 if(FGDemo1.offsetHeight-FGDemo.scrollTop<=800){
 	FGDemo.scrollTop= 0 ;
 }
 else{
 	FGDemo.scrollTop++
 	}
 }
 var MyMar1=setInterval(Marquee1,speed)
 FGDemo.onmouseover=function() {clearInterval(MyMar1)}
 FGDemo.onmouseout=function() {MyMar1=setInterval(Marquee1,speed)}
</script> </div>
<div style="font-size:16px;" align="center">
	&nbsp;&nbsp;&nbsp;&nbsp;当前办件信息&nbsp;&nbsp;&nbsp;&nbsp;
	 受理数:<span id = "sls" name="sls"></span>&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;
	 转办数：<span id = "zbs" name="zbs"></span>&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;
	 办结数：<span id = "bjs" name="bjs"></span>&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;
	 回访数：<span id = "hfs" name="hfs"></span>&nbsp;件&nbsp;&nbsp;&nbsp;&nbsp;
</div>
</body>
</html>

<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/js.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/jquery-1.3.1.js?t=20090219" type="text/javascript"></script>
</head>
<body>

 <div id="demo" style="overflow:hidden;height:100px;width:1500px;">
 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="26"  id="demo1" class="c_main_bottom_td">
	    <table width="1500" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td id="showContent" style="width:1500px;"  align="right" colspan="3">
	        	
	        </td>
	      </tr>
	    </table>
    </td>
    <td id="demo2" class="c_main_bottom_td"></td>
  </tr>
</table> 
</div>

<script Language=JavaScript>
//window.setTimeout('this.location.reload();',45000);
//setInterval("Test();",3000000);//30秒
//定时检测用户是否在别处登陆
function Test(){
	$.getJSON("<%=request.getContextPath()%>/login.do?method=isRepeatLogin"+"&_time="+(new Date().getMilliseconds()), "",function(json){
	if(json.result===true){
	 if(getCookie()==json.token){
	  alert(json.msg);
	  //关闭窗口
	  top.close();
     //document.cookie="sdcncsi_ict_token=;expires="+(new Date().toUTCString()-1);
      }
	}
	});
}
function getCookie(){
//获取cookie字符串
var strCookie=document.cookie;
//将多cookie切割为多个名/值对
var arrCookie=strCookie.split(";");
var token="";
//遍历cookie数组，处理每个cookie对
for(var i=0;i<arrCookie.length;i++){
      var arr=arrCookie[i].split("=");
      if("sdcncsi_ict_token"==arr[0]){
             token=arr[1];
             break;
      }
}
return token;
}
</script>


<script>
var speed=1
var MyMar=setInterval(Marquee,speed)
demo2.innerHTML=demo1.innerHTML
demo.onmouseover=function() {clearInterval(MyMar)}
demo.onmouseout=function() {MyMar=setInterval(Marquee,speed)}
function Marquee(){
if(demo2.offsetWidth-demo.scrollLeft<=0)
   demo.scrollLeft-=demo1.offsetWidth
else{
   demo.scrollLeft++
}
}
</script>

<body>l</body>
</html>
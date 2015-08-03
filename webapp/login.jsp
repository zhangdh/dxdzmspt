<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false"%>
<html>
<%@ include file="/common/common.jsp"%>
<head>
<title>用户登录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script>
$(function(){
  	var w = $(document).width();
  	var h = $(document).height();
  	var top = (h-450)/2;
  	var left = (w-658)/2;
  

  	$("#div3").css("left",(w-140)/2);
  	$("#div3").css("top",(h+286)/2);
  	if("${warning}"!=""){
   		alert("${warning}");
   		$("#dd").focus();
 	}
  	if($("#dd").val()!=""){
   		 $("#mm").focus();
  	}else{
    	 $("#dd").focus();
  	}
});
function login(){
	var iname = $("#dd").val();
	var ipassword = $("#mm").val();
	if (iname == "") {
		alert("请输入用户名");
		$("#dd").focus();
		return false;
	}
	if (ipassword == "") {
		alert("请输入密码");
		$("#mm").focus();
		return false;
	}
    document.formid.submit();
}
function reset1(){
	$("#dd").attr("value","").focus();
	$("#mm").attr("value","");
}

</script>
<script>
 if('${ifdl}'!='true'){
  document.location.href="${webcontext}/login.doAction";
}
function init(){
	var w = document.body.scrollWidth;
	var h = document.body.scrollHeight;
	if(w>900){
		document.getElementById("d1").style.left=(w-900)/2;
	}
	if(h>591){
		document.getElementById("d1").style.top=(h-591)/2;
	}
}
</script>
</head>
<body onload="init();" >
	<div id="d1" style="width:900px;height:591px;position:relative;background-image:url('css/login/bg.gif');">
		<form id="formid" name="formid" action="${webcontext}/login.doAction" method="post">	
		<div id="div2" style="position:relative;top:178px;left:280px;">
			<div style="width:130px;height:20px;top:108px;left:380px;position:relative;">
				<input type="text" name="dlmc" id="dlmc" size="25" maxlength="25" required="true"  style="width:140px;height:22px" onKeyDown="if(event.keyCode == 13) login();" value="${dlmc}">
			</div>
			<div style="left:380px;height:20px;position:relative;top:120px;" >
				<input type="password" name="mm" id="mm"  size="25" maxlength="25" required="true" style="width:140px;height:22px" onKeyDown="if(event.keyCode == 13) login();" >
			</div>
			<div style="left:370px;height:20px;position:relative;top:160px;">
				<input type= "button" class="btn_query" onclick="login()"  style="width:68px" value="登录"/>
				    
				<input type= "button" class="btn_query" onclick="reset1()"  style="width:68px" value="重置"/>
			</div>
		</div>
		</form>
	</div>
	<div id="div3" style="width:140px;align:center;position:relative;font-size:12px;font-style:oblique;color:#6E6E6E"></div>
</body>
</html>
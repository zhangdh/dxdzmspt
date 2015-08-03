<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page session="false"%>
<html>
<head>
<script> 
function openNewWindow(){
	var ScreenWidth = screen.availWidth; 
	var ScreenHeight = screen.availHeight; 
	var Win = window.open('login.jsp', '','width=' + ScreenWidth +', height=' + ScreenHeight + ', scrollbars=yes, status=no,toolbar=no,menubar=no,location=no,left=0,top=0');  
   	//window.open(Win,'_self',"");
    window.close();
	
}
</script>
</head> 
<body onload="openNewWindow();"> 
<script>
	window.opener=null;
    window.open("",'_self',"");
	window.close();
</script>
</body> 
</html>

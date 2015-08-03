<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>
<% 
	String yhid = request.getAttribute("yhid").toString();
%>
<html>
  <head>
  	<script type="text/javascript" src="menu.js"></script>
  	<style>
  		body {
			
		}
		.menu{
			border-color:#37A8C8;
			border-width:1px;
		}
		.pmenu{
			position:relative;
			width:160px;
			height:25px;
			line-height:26px;
			background-image:url('../css/menu/menu_bg.gif');	
			color:#000079;
			font-weight:bolder;			
		}
		.cmenu{
			font-color:#000079;
			display:none;
			font-size:13px;
			font-family:'方正小标宋';
			width:160px;
			height:20px;
		}
		html{overflow:hidden;}
		
		a:visited{color:#000079;}
		a:link{color:#000079;} 
		a{text-decoration: none;}
		a { blr:expression(this.onFocus=this.blur()) }
  	</style>
  	<script>

  	</script>
  </head>
  <body scroll="no">
	 <div id="menu"></div>
  </body>
</html>

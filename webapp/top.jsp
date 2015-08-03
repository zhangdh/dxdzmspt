<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ page import="com.coffice.util.cache.Cache"%>  
<%@ page import="java.text.SimpleDateFormat"%> 
<%@ page import="java.util.Date"%> 
<%@ include file="/common/common.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="css/phone/phone.css" rel="stylesheet" type="text/css"> 
<style>
.top_right{
	position:absolute;
	right:5px;
	width:240px;
	height:65px;
	top:30px;
	color:#000079;
	font-weight:bolder;
}
.top_phone{
	position:absolute;
	right:0px;
	float:right;
	width:550px;
	align:center;
}
.userInfo{
	position:absolute;
	left:5px;
	float:left;
	width:500px;
	height:26px;
	align:center;
}
.logo{
	position:absolute;
	background: url(css/top/logo.png) no-repeat;
	margin-top:2px;
	left:10px;
	width:130px;
	height:58px;
}
.logo_text{
	position:absolute;
	background: url(css/top/logo_text.png) no-repeat;
	top:0px;
	margin-top:0px;
	left:130px;
	width:250px;
	height:58px;
}
.top_left{
	width:650px;
	height:65px;
	position: absolute;
	background: url('css/top/top-1.jpg');
	padding:0px;
	top:0;
	left:0;
	float:left;
	z-index:-1;
}
.top_right{
	background: url('css/top/top-3.jpg');
	position: absolute;
	float:right;
	height:65px;
	width:434px;
	top: 0px;
	right: 0px;
	z-index:-1;
	 
}
.top_center{
	background-color:#0EABF4;	 
	height: 65px;
	padding:0px;
	margin-right: 434px;
	margin-left: 650px;
}
a{text-decoration: none;}
a:visited{color:#000079;}
a:link{color:#000079;} 
a { blr:expression(this.onFocus=this.blur()) }
</style>
<%
	String yhid=(String)request.getAttribute("yhid");
    String WorkNo=(String)Cache.getUserInfo(yhid,"gonghao");
	String WebVoipAccount=(String)Cache.getUserInfo(yhid,"fenjihao");
    if(WorkNo==null){
    	WorkNo="null";
    }
%>
<script language="javascript">
	function exit(){
   		if(confirm("确认要退出系统吗？")){
   			parent.document.location.href="<%=request.getContextPath()%>/exit.doAction";
	 	}
   	}
</script>
<script>
	function btnclick(name){
		SoftPhone.BtnClick(name);
	}
	function inBound(called,filename){
		parent.frame_right.addTab(called+"来电","fwqq/fwqq.jsp?lypath="+filename+"&ldhm="+called);
	}
	function call(){
		var agentlist = SoftPhone.GetIdleAgent();
		JsCall(agentlist);
	}
	function SaveRec(caller,called,flowstr,filename,agentnum,calltype){
	    _Post("/thxx.doAction?method=saveReco&caller="+caller+"&called="+called+"&flowstr="+flowstr+"&filename="+filename+"&agentnum="+agentnum+"&calltype="+calltype,"",function(json){
	    	if (calltype == "0" && $("#hfGuid").val()!=""){
	    		 var saveSql="caller="+caller+"&called="+called+"&flowstr="+flowstr+"&filename="+filename+"&agentnum="+agentnum+"&calltype="+calltype+"&ywid="+$("#hfGuid").val();
	    		 _Post("/thxx.doAction?method=saveHfReco",saveSql,function(){});
	    		 	$("#hfGuid").val("");
	    	}   	
	    });
	}
	function onLoad(){
		var now=new Date()
		var week= "";
		if(now.getDay()==0){
			week = "星期日";
		}else if(now.getDay()==1){
			week = "星期一";
		}else if(now.getDay()==2){
			week = "星期二";
		}else if(now.getDay()==3){
			week = "星期三";
		}else if(now.getDay()==4){
			week = "星期四";
		}else if(now.getDay()==5){
			week = "星期五";
		}else if(now.getDay()==6){
			week = "星期五";
		}
   		var t = now.getFullYear()+"年"+(now.getMonth()+1)+"月"+now.getDate()+"日 "+week;
   		$("#timeInfo").html(t); 
	}
	function test(){
		inBound("1234567890","A3EDHEEER");
		SaveRec("1234567890","12345","A3EDHEEER","rec/2/voice/20121017/093212345/2003/20121017004948125.mp3","120",0);
	}
</script>
</head>
<body onload="onLoad();">
<div style="height:65px;width:100%">
	<div class="top_left">
		<div id="logo" class="logo"></div>
		<div id="logo" class="logo_text"></div>
	</div>
	<div class="top_center"></div>
	<div class="top_right">
		<div style="width:60px; margin-top:15px;float:right;font-family:Microsoft Yahei;font-size:14px;font-weight:800;color:#529ADE'" onclick="exit()">
			<img src="css/top/exit.png"><span >&nbsp;<a href='#'>退出</a></span>
		</div>
	</div>
</div>
<div style="height:26px;background-color:'#F7F7F7';overflow:hidden;line-height:26px;">
	 <div id="userInfo" class="userInfo">
		<img src="css/top/user.png" style="margin-top:4px;margin-left:10px;"></img>
		<span style='height:100%;line-height:25px;font-family:Microsoft Yahei;font-size:14px;font-weight:800;color:#529ADE'>当前用户： <%=Cache.getUserInfo(yhid,"xm") %></span>
		<span id="timeInfo" style='height:100%;line-height:25px;font-family:Microsoft Yahei;font-size:12px;font-weight:700;color:#529ADE'></span>
	 </div>
	 <div id="softphoneID" class="top_phone">
	 	<input type=button class="BtnSignInOut" id="BtnSignInOut"  name="BtnSignInOut" value="  签入"  onclick="btnclick('BtnSignInOut')"/>
	 	<input type=button class="BtnIdleWork" id="BtnIdleWork"  name="BtnIdleWork" value="  就绪" disabled onclick="btnclick('BtnIdleWork')"/>
	 	<input type=button class="BtnCall" id="BtnCall"  name="BtnCall" value="  拨号" disabled onclick="btnclick('BtnIdleWork')"/> 
		<input type=button class="BtnAnswerRefuse" id="BtnAnswerRefuse"  name="BtnAnswerRefuse" value="  摘机" disabled onclick="btnclick('BtnIdleWork')"/>
		<input type=button class="BtnTrance" id="BtnTrance"  name="BtnTrance" value="  转接" disabled onclick="btnclick('BtnTrance')"/>
		<input type=button class="BtnTranAgent"  id=BtnTranAgent  name=BtnTranAgent  value="  转坐席"  disabled onclick="btnclick('BtnTranAgent')">
	 	<input  type=button class="BtnMeet"  value="  会议"  id=BtnMeet  name=BtnMeet   disabled onclick="btnclick('BtnMeet')">
	 	<INPUT  type=button class="BtnHold"  value="  保持"  id=BtnHold  name=BtnHold   disabled onclick="btnclick('BtnHold')">
	 	<INPUT  type=button class="Btntest"  value="测试"  id=BtnHold  name=Btntest    onclick="test()">
	</div>
</div>
<object id="SoftPhone" classid="clsid:02CAB47C-C4F6-4CA8-9867-25EE8328640F" width="0" height="0" title=""></object>
<input type="hidden" id="hfGuid" name="hfGuid" />
<script language="javascript">	
	WorkNo = '<%=WorkNo%>';
	Password = '89510F6E056E0FE82D6E51D585361814'; 
	WebVoipAccount='';
	WebVoipPassword='';
	try{
		SoftPhone.StaffMsg(WorkNo,Password,WebVoipAccount,WebVoipPassword);
	}catch(Exception){}
</script> 
<script language="javascript" event="OnInBound(caller,called,tmpfile,tmpfile1,tmpfile2,tmpfile3)" for="SoftPhone">
	//来电事件
	inBound(caller,tmpfile1);
</script>
<script language="javascript" event="OnRecSucc(caller,called,flowstr,filename,calltype,agentnum)" for="SoftPhone">
		SaveRec(caller,called,flowstr,filename,agentnum,calltype);
</script>
<script language="javascript" event="OnStatChang(a01,a02,a03,a04,a05,a06,a07,a08,a09,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20)" for="SoftPhone">
     if(a01==true) {
          document.getElementById('BtnSignInOut').disabled = false;   
     }else{
          document.getElementById('BtnSignInOut').disabled=true;          
     }  
	 document.getElementById('BtnSignInOut').value = "  "+a02;
	 if (a02=='签出'){
		  document.getElementById("BtnSignInOut").className="BtnSignInOut1";
     }
     if (a02=='签入'){		
		  document.getElementById("BtnSignInOut").className="BtnSignInOut";
     }
	 if (a03==true) {
          document.getElementById('BtnIdleWork').disabled = false;
          if (a04=='未就绪'){
		   	document.getElementById("BtnIdleWork").className="BtnIdleWork1";
          }else{		
		 	document.getElementById("BtnIdleWork").className="BtnIdleWork2"; 
          }         
      }else{
          document.getElementById('BtnIdleWork').disabled = true;
          document.getElementById("BtnIdleWork").className="BtnIdleWork"; 
      }
      document.getElementById('BtnIdleWork').value = "  "+a04;  
      if (a05==true) {
          document.getElementById('BtnCall').disabled = false;  
          document.getElementById('BtnCall').className = "BtnCall1";
      }else{
		  document.getElementById('BtnCall').disabled = true;
          document.getElementById('BtnCall').className = "BtnCall";
      }
	  document.getElementById('BtnCall').value="  "+a06; 
	    
	  if (a07==true){
    	  document.getElementById('BtnAnswerRefuse').disabled = false;
    	  if (a08=="摘机"){
			 document.getElementById('BtnAnswerRefuse').className = "BtnAnswerRefuse1";   
          }else{
 			 document.getElementById('BtnAnswerRefuse').className = "BtnAnswerRefuse2";   
		  }      
       }else{
		  document.getElementById('BtnAnswerRefuse').disabled = true;
		  document.getElementById('BtnAnswerRefuse').className = "BtnAnswerRefuse"; 
       }
	   document.getElementById('BtnAnswerRefuse').value = "  "+a08; 
	   if (a09==true) {
          document.getElementById('BtnTrance').disabled = false;   
          document.getElementById('BtnTrance').className = "BtnTrance1";
       }else{
          document.getElementById('BtnTrance').disabled = true;
          document.getElementById('BtnTrance').className = "BtnTrance";
       }
	   document.getElementById('BtnTrance').value="  "+a10;
	   
	   if (a11==true) {
		  document.getElementById('BtnMeet').disabled = false; 
		  document.getElementById('BtnMeet').className = "BtnMeet1"; 
       }else{
          document.getElementById('BtnMeet').disabled = true;
          document.getElementById('BtnMeet').className = "BtnMeet";
       }
       document.getElementById('BtnMeet').value="  "+a12;
       
	   if (a13==true){
          document.getElementById('BtnHold').disabled = false;
          if (a14=="保持"){
              document.getElementById("BtnHold").className="BtnHold1"; 
          }else{
           	  document.getElementById("BtnHold").className="BtnHold2"; 
          }  
       }else{
          document.getElementById('BtnHold').disabled = true;
          document.getElementById("BtnHold").className="BtnHold";
       }
       document.getElementById("BtnHold").value="  "+a14; 
       
	   if (a15==true){
           document.getElementById('BtnIvr').disabled = false;
           document.getElementById('BtnIvr').className="BtnIvr2" ;
        }else{
		   document.getElementById('BtnIvr').disabled = true;
           document.getElementById('BtnIvr').className="BtnIvr1";
        }
       document.getElementById('BtnIvr').value=a16;  
       if (a17==true){
     	   document.getElementById('BtnQI').disabled = false;
     	   document.getElementById('BtnQI').className="BtnQI2" ;
    	}else{
     	   document.getElementById('BtnQI').disabled = true;
     	   document.getElementById('BtnQI').className="BtnQI1" ;
        }
        document.getElementById('BtnQI').value = a18;
        if (a19==true) {
            document.getElementById('BtnTranAgent').disabled = false;   
            document.getElementById('BtnTranAgent').className = "BtnTranAgent1";
        }else{
            document.getElementById('BtnTranAgent').disabled = true;
            document.getElementById('BtnTranAgent').className = "BtnTranAgent";
        }
		document.getElementById('BtnTranAgent').value="  "+a20;
 </script>
 <script>
 	 
 </script>
</body>
</html>

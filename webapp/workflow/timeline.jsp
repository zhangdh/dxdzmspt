<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="${webcontext}/js/timelinr/jquery.timelinr-0.9.53.js"></script>
<html>
	<head>
		<title>实例时间轴</title>
		<script>
			$(function(){ 
				var str1 = "";
				var str2 = "";
				var uri = window.location.search;
       	    	var entryId = uri.split("entryId=")[1];
       	    	_Post("/wfuse.coffice?method=queryTimeLine&id="+entryId,"",function(jsonData){
       	    		var data = jsonData.list;
       	    		$.each(data,function(k,v){
       	    			str1 = str1+"<li><a href='#"+v.stepid+"'>"+v.stepname+"</a></li>";
       	    			str2 = str2+"<li id='"+v.stepid+"'>"
       	    					   +"处理人:"+v.clr+"<br/>"
       	    					   +"开始时间:"+v.starttime+"<br/>"
       	    					   +"结束时间:"+v.endtime+"<br/>"
       	    					   +"处理时长:"+v.sjjg+"<br/>"
       	    				   	   +"</p></li>";
       	    			$("#dates").html(str1);
       	    			$("#issues").html(str2);
       	    		});
       	    		$().timelinr({ 
           				orientation:'horizontal' 
   					}); 
       	    	});
			}); 
		</script>
		<style type="text/css">
			#timeline {width:"900px";height:"200px";overflow:"hidden";margin:"50px auto";position: "relative";background: "url('../js/timelinr/dot.gif') left 45px repeat-x";}
			#dates {width: 900px;height: 50px;overflow: hidden;}
			#dates li {list-style: "none";float: "left";width: "100px";height: "50px";font-size: "10px";text-align: "center";background: "url('../js/timelinr/biggerdot.png') center bottom no-repeat";}
			#dates a {line-height: "16px";padding-bottom: "10px";text-decoration:"none";}
			#dates .selected {font-size: "16px";}
			#issues {width: "760px";height: "300px";overflow: "hidden";}	
			#issues li {width: "760px";height: "300px";list-style: "none";float: "left";}
			#issues li h1 {color: "#ffcc00";font-size: "42px";margin: "100px 0";text-shadow: "#000 1px 1px 2px";}
			#issues li p {font-size: "14px";margin-right: "70px"; margin:"10px"; font-weight: "normal";line-height: "22px";}
		</style>
	</head>
	<body>	
	<div id="timeline"> 
   	<ul id="dates"> 
       
   	</ul> 
   	<ul id="issues"> 
       
   	</ul> 
	</div> 
	</body>
</html>
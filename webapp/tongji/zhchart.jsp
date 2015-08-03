<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <title>图表统计查询</title>
      <%@ include file="/common/common.jsp" %>
      <%
      	String type = request.getParameter("tjlx");
       %>
    <!-- InstanceEndEditable -->
    <!--页面私有js区止起-->
    <script type="text/javascript" src="../js/coffice.js"></script>
    <script type="text/javascript" src="../js/fusionChart/FusionCharts.js"></script>
    <script type="text/javascript" src="zhchart.js"></script>
    <script>
    	var type = '<%=type%>';
    	var searchStr = window.location.search;
	    var params = searchStr.substring(searchStr.lastIndexOf("?") + 1);
    </script>
</head>
<body id="bodyid">
<div id="chart0" style="height:300;width:400;display:inline"></div>
<div id="chart1" style="height:300;width:400;display:inline"></div>
<div id="chart2" style="height:300;width:400;display:inline"></div>
<div id="chart3" style="height:300;width:400;display:inline"></div>
<div id="chart4" style="height:300;width:400;display:inline"></div>
<div id="chart5" style="height:300;width:400;display:inline"></div>
<div id="chart6" style="height:300;width:400;display:inline"></div>
<div id="chart7" style="height:300;width:400;display:inline"></div>
<div id="chart8" style="height:300;width:400;display:inline"></div>
<div id="chart9" style="height:300;width:400;display:inline"></div>
<div id="chart10" style="height:300;width:400;display:inline"></div>
<div id="chart11" style="height:300;width:400;display:inline"></div>
<div id="chart12" style="height:300;width:400;display:inline"></div>
<div id="chart13" style="height:300;width:400;display:inline"></div>
<div id="chart14" style="height:300;width:400;display:inline"></div>
<div id="chart15" style="height:300;width:400;display:inline"></div>
<div id="chart16" style="height:300;width:400;display:inline"></div>
<div id="chart17" style="height:300;width:400;display:inline"></div>
</body>
</html>

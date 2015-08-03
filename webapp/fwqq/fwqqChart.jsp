<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
    <title>图表统计查询</title>
      <%@ include file="/common/common.jsp" %>
    <!-- InstanceEndEditable -->
    <!--页面私有js区止起-->
    <script type="text/javascript" src="../js/coffice.js"></script>
    <script type="text/javascript" src="../js/fusionChart/FusionCharts.js"></script>
    <script type="text/javascript" src="fwqqChart.js"></script>
    <script>
    	var searchStr = window.location.search;
	    var params = searchStr.substring(searchStr.lastIndexOf("?") + 1);
    </script>
</head>
<body id="bodyid">
<div id="chart1_div" style="z-index:-100"></div>
</body>
</html>

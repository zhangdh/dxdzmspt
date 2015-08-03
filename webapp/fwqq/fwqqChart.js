$(function(){	
	_Post("/fwqq.coffice?method=tjFwqqChart",params,function(jsonData){
			var chart1 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", document.documentElement.offsetWidth-18, document.documentElement.offsetHeight-5,"0","0");
			chart1.setDataXML(jsonData.xmlStr);
	  		chart1.render("chart1_div");
	});
})
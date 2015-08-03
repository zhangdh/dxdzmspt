$(function(){	
	_Post("/txzx.coffice?method=query","",function(jsonData){
			var chart1 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart1.setDataXML(jsonData.chart1);
	  		chart1.render("chart1_div");
	  		
	  		var chart2 = new FusionCharts("../js/fusionChart/Pie3D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart2.setDataXML(jsonData.chart2);
	  		chart2.render("chart2_div");

	  		var chart3 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart3.setDataXML(jsonData.chart3);
	  		chart3.render("chart3_div");

	  		var chart4 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart4.setDataXML(jsonData.chart4);
	  		chart4.render("chart4_div");
	  		
	  		var chart5 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart5.setDataXML(jsonData.chart5);
	  		chart5.render("chart5_div");
	  		
	  		var chart6 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart6.setDataXML(jsonData.chart6);
	  		chart6.render("chart6_div");
	  		
	  		
	  		var chart7 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart7.setDataXML(jsonData.chart7);
	  		chart7.render("chart7_div");
	  		
	  		var chart8 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart8.setDataXML(jsonData.chart8);
	  		chart8.render("chart8_div");
	  		
	  		var chart9 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart9.setDataXML(jsonData.chart5);
	  		chart9.render("chart9_div");
	  		
	  		var chart10 = new FusionCharts("../js/fusionChart/Column2D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
			chart10.setDataXML(jsonData.chart10);
	  		chart10.render("chart10_div");
	  		 
	});
})
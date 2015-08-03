$(function(){	
	if(type=="ldsj"){
		document.title = "来电数据图表显示";
		_Post("/tjbb.coffice?method=ldsjChart",params,function(jsonData){
			var chart1 = new FusionCharts("../js/fusionChart/MSColumn2D.swf", "myChartId", document.documentElement.offsetWidth-18, document.documentElement.offsetHeight-5,"0","0");
			chart1.setDataXML(jsonData.xmlStr);
	  		chart1.render("chart1_div");
		});
	}else if(type=="compare"){
		document.title = "来电数据比较数据图表显示";
		_Post("/tjbb.coffice?method=compareChart",params,function(jsonData){
			var chart1 = new FusionCharts("../js/fusionChart/MSColumn2D.swf", "myChartId", document.documentElement.offsetWidth-18, document.documentElement.offsetHeight-5,"0","0");
			chart1.setDataXML(jsonData.xmlStr);
	  		chart1.render("chart1_div");
		});
	}
	
})
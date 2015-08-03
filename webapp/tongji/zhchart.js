$(function(){	
	//alert(params);
	var cxq =(params.split("cx_sjq=")[1]).split("&cx_sjz")[0];
	var cxz =params.split("&cx_sjz=")[1];
	if(type=="blzt"){
		document.title = cxq+"至"+cxz+"办理状态数据图表显示";
	}else if(type=="bjlx"){
		document.title = cxq+"至"+cxz+"办件类型数据图表显示";
	}else if(type=="xxly"){
		document.title = cxq+"至"+cxz+"信息来源数据图表显示";
	}else if(type=="xzfl"){
		document.title = cxq+"至"+cxz+"性质分类数据图表显示";
	}else if(type == "nrfl"){
		document.title = cxq+"至"+cxz+"内容类别数据图表显示";
	}
	_Post("/tjbb.coffice?method=zhtjChart",params,function(jsonData){
		$.each(jsonData,function(k,v){
			if(k.indexOf("chart")>-1){
				var chart = new FusionCharts("../js/fusionChart/Pie3D.swf", "myChartId", (document.documentElement.offsetWidth-22)/2, 300,"0","0");
				chart.setDataXML(v);
	  			chart.render(k);				
			}
		})
 		
	});
})
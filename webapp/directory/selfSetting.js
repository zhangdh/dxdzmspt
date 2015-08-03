$(function(){
	_Post("/setting.coffice?method=getYhInfo","",function(jsonData){
			loadForm("selfInfo",jsonData)
	});
});

function save(lb){
	if(lb=="mm"){
		if($("#dlmm").val() != $("#dlmm2").val()){
			alert("2次密码输入不一致，请重新输入");
		}else{
			_Post("/setting.coffice?method=modiMM","dlmm="+$("#dlmm").val(),function(jsonData){
				alertMes(jsonData);
			});
		}
	}
	if(lb=="yh"){
		var saveInfo = $("#selfInfo").serialize();
		_Post("/setting.coffice?method=modiInfo",saveInfo,function(jsonData){
					alertMes(jsonData);
		});
	}
}
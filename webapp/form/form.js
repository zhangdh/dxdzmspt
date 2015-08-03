function getWh(id,customid){
sys_ajaxGet("/form/default.do?method=getWh","id="+id+"&customid="+customid,function(json){
$("#"+id).attr("value",json.sdcncsi_ict_wh);
}); 
}

function getWenHao(){
var customid=$("#sdcncsi_ict_wh").attr("datatype");
sys_ajaxGet("/form/default.do?method=getWh","customid="+customid,function(json){
$("#sdcncsi_ict_wh").attr("value",json.sdcncsi_ict_wh);
});
}
function showForm(){
	$("form :input:not(:hidden)").each(function(){
		var textValue = "";
		if($(this).attr('tagName')=="SELECT"){
		if($(this).attr('pickertype')){
		  $(this).find("option").each(function(){
		  textValue = textValue+","+$(this).text();
		  });
		  textValue = textValue.replace(/,/,'');
		  $(this).next().remove();
		}else{
			textValue = $(this).find("option:selected").text();
		 }
		// $(this).parent().append(textValue);
		$(this).after(textValue);
		$(this).hide();
		}
		else{
		textValue = $(this).val();
		if($(this).attr('tagName')=="TEXTAREA"){
		//$(this).parent().append(textValue);
		$(this).after(textValue);
		$(this).hide();
		}else{
		$(this).css("border-left-width","0");
		$(this).css("border-right-width","0");
		$(this).css("border-top-width","0");
		$(this).css("border-bottom-width","0");
		$(this).attr("readOnly","readOnly");
		$(this).attr("onfocus","");//取消onfocus事件
		$(this).click(function(){return false;});
		}
		}

	});
	//取消日期控件的样式
	$(".Wdate").removeClass("Wdate");
}
Date.prototype.format =function(format)
{
var o = {
"M+" : this.getMonth()+1, //month
"d+" : this.getDate(), //day
"h+" : this.getHours(), //hour
"m+" : this.getMinutes(), //minute
"s+" : this.getSeconds(), //second
"q+" : Math.floor((this.getMonth()+3)/3), //quarter
"S" : this.getMilliseconds() //millisecond
}
if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
(this.getFullYear()+"").substr(4- RegExp.$1.length));
for(var k in o)if(new RegExp("("+ k +")").test(format))
format = format.replace(RegExp.$1,
RegExp.$1.length==1? o[k] :
("00"+ o[k]).substr((""+ o[k]).length));
return format;
}
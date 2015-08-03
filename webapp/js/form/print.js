var doPrint = function(){
	//beforePrint();
	window.print();
};
var clickHandler = function(event){
	$(this).hide();
	window.print();
	$(this).show();
};
var beforePrint = function(){
	$(".read_only").each(function(){
	$(this).children().each(function(){
	$(this).attr("disabled",false);
	 });
	 });
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
		$(this).parent().append(textValue);	
		$(this).hide();
		}
		else{
		textValue = $(this).val();
		if($(this).attr('tagName')=="TEXTAREA"){
		$(this).css("border-left-width","0");
		$(this).css("border-right-width","0");
		$(this).css("border-top-width","0");
		$(this).css("border-bottom-width","0");
		$(this).css("overflow-y","hidden");
		}else{
		$(this).css("border-left-width","0");
		$(this).css("border-right-width","0");
		$(this).css("border-top-width","0");
		$(this).css("border-bottom-width","0");
		 }
		}

	});
};

var beforeTPrint = function(){
	$("body :input:not(:hidden)").each(function(){
		var textValue = "";
		if($(this).attr('tagName')=="SELECT"){
			textValue = $(this).find("option:selected").text();
		}
		else{
			textValue = $(this).val();
		}
		$(this).parent().append(textValue);
		$(this).hide();
	});
	$('<input type="button" value="Print"/>').appendTo('body').bind('click', clickHandler);
};
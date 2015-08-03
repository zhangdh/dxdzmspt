window.onload = function() {
	var bodyChildren = document.forms[0].childNodes;
	for ( var i = 0; i < bodyChildren.length; i++) {
		if (bodyChildren[i].tagName
				&& bodyChildren[i].tagName.toUpperCase() === 'TABLE') {
			var cells = bodyChildren[i].cells;
			for ( var j = 0; j < cells.length; j++) {
				if (cells[j].className
						&& cells[j].className.toUpperCase() === 'READONLY') {
					var components = cells[j].childNodes;
					for ( var k = 0; k < components.length; k++) {
						if (components[k].tagName) {
							var c = components[k];
							var v = c.value;
							if (c.tagName.toUpperCase() === 'SELECT') {
								v = c.options[c.selectedIndex].text;
							}
							if (c.tagName.toUpperCase() === 'SPAN')
							    continue;
							c.style.setAttribute("display", "none");
							if(v)
							cells[j].innerHTML += v;
						}
					}
				} else {
					continue;
				}
			}
		}
	}
}

function unread(){
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

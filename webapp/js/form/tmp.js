window.onload = function() {
	var bodyChildren = document.forms[0].childNodes;
	for ( var i = 0; i < bodyChildren.length; i++) {
		if (bodyChildren[i].tagName && bodyChildren[i].tagName.toUpperCase() === 'TABLE') {
			var cells = bodyChildren[i].cells;
			for ( var j = 0; j < cells.length; j++) {
				if (cells[j].className && cells[j].className.toUpperCase() === 'READONLY') {
					//alert('j'+j);
					var components = cells[j].childNodes;
					//alert(components.length);
					for ( var k = 0; k < components.length; k++) {
						//alert(components[k].tagName);
						if (components[k].tagName) {
							var c = components[k];
							var v = c.value;
							//alert(c.tagName+':'+v);
							if (c.tagName.toUpperCase() === 'SELECT') {
								v = c.options[c.selectedIndex].text;
							}
							if (c.tagName.toUpperCase() === 'SPAN')
							    continue;
							c.style.setAttribute("display", "none");
							if(v){
								var vR = v.split(".")[0];
								var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/; 
        						if(reg.test(vR)){
        							cells[j].innerHTML += v.substring(0,16);	
        						}else{
        							cells[j].innerHTML += v;	
        						}
							}	
						}
					}
				} else {
					continue;
				}
			}
		}
	}
}
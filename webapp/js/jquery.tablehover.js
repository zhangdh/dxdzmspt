jQuery.fn.tableHover = function(options){
	var settings = $.extend({
		hoverClass:'c_trHover',
		clickClass:'c_trClick',
		oddClass:'c_trOdd',
		evenClass:'c_trEven',
		//trClick:fn
		getCell:0
	},options);
    var css, oj;
    this.each(function(){//选中了多个表格
    	//if ((settings.oddClass=='')&&(settings.evenClass=='')) return;
        oj = jQuery("tbody tr",jQuery(this));
        oj.each(function(i){ // 选中表格中的 TR 加 CSS
        	jQuery(this).removeClass();
            css = i % 2 == 0 ? settings.evenClass : settings.oddClass;
            jQuery(this).addClass(css);
        });
        
        if(settings.hoverClass!=''){
            oj.hover(function(){
                jQuery(this).addClass(settings.hoverClass);
            },
            function(){
                jQuery(this).removeClass(settings.hoverClass);
            });
        }
        var fn=settings.trClick;
        if(fn && "function" == typeof fn){ //用来处理点击事件的回调函数
            oj.each(function(i){//取消原来绑定，以免翻页等多次绑定时调用多次fn
				jQuery(this).unbind("click");
			});
		}
        if(settings.clickClass!=''){
            oj.click(function(){
           		jQuery(this).siblings().removeClass(settings.clickClass);
                jQuery(this).addClass(settings.clickClass);
            });
        }

        if(fn && "function" == typeof fn){ //用来处理点击事件的回调函数
            if (isNaN(parseInt(settings.getCell))) return;   
            oj.click(function(event){
                event = jQuery.event.fix( event || window.event || {} ); 
                var cn = event.target;
                if( cn && cn.nodeName.toLowerCase() == "td"){
                	var cells=settings.getCell.toString();
                    if(cells.length==1){
                    	var cellValue=jQuery(cn).parent().children().eq(cells).html();
                    	if (jQuery.trim(cellValue)=="") return;
                    	fn.call(this,cellValue);
                    }else{
	                	var returnStr='';
                		for(i=0;i<cells.length;i++){
	                    	returnStr = returnStr + jQuery.trim(jQuery(cn).parent().children().eq(cells.charAt(i)).html()) + ",";
	                    }
	                    fn.call(this,returnStr.split(","));
                    }
                    //executeEventHandler(cellValue,settings.on_click);
                }
            });
        }
    });
} 
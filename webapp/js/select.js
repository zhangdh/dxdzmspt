/**
*代码缓存处理
*2010-07-26
**/
//0. 定义页面缓存
var effective_selectData = new TAFFY(
   		[{dm:"",
		 mc:"",
		 py:"",
		 zt_dm:"",
		 lb_dm:"",
		 sjdm:"",
		 selectid:""
		}]);
$(function() {
   //1. 检查属性绑定情况
   $("select").each(function(){
   		var select = $(this);
   		//默认cache为true
   		if(!$(this).attr("cache") &&　!$(this).attr("sjid")){ $(this).attr("cache","true"); }
   		
   		//检查被转换的select是否具有dm属性，如果没有，移除这个select的所有相关属性
   		if($(this).attr("class")=="editableSelect" && !$(this).attr("dm")){
   			sys_alert($(this).attr("id")+"没有绑定dm属性");
   			$(this).removeClass("editableSelect");return;
   		}
   		//检查绑定的上级select是否存在，如果不存在，移除这个select的所有相关属性
   		if($(this).attr("sjid")){
   			var sjid = $(this).attr("sjid");
   			if(!$("#"+sjid).attr("id")){
   				sys_alert("错误：没有定义为"+sjid+"的select");
   				$(select).removeAttr("dm").removeAttr("sjid").removeClass("editableSelect");return;
   			}
   		}
   	
   });
  
  //2. select转换成输入框 
  $("select[class='editableSelect']").each(function(){
		var select = $(this);
		var iwidth = $(this).width()+3;//输入框宽度
		var selectID = $(this).attr("id")+"_mc";//select之后的input ID. 格式为'selectID_mc'
		//隐藏select IE6及其他
		if ($.browser.msie && $.browser.version < 7) { 
			$(this).get(0).style.width = 0;
	        $(this).get(0).style.visibility= 'hidden';  
	    }else{
	    	$(this).hide();
	    }  
		var html = "<input type='text' class='editable-select' style='width:"+iwidth+"px;' id='"+selectID+"' name='"+selectID+"'/>";
		html += "<div class='editable-select-options' style=' white-space:nowrap;display:none;position: absolute;overflow-y:auto;margin:0;padding:0;border: 1px solid #CCC;z-index:10;background:#FFFFFF;'><ul style='background:#FFFFFF;'></ul></div>";
		html += "<div class='divframe' style='display:none;position: absolute;overflow:no;margin:0;padding:0;border: 1px solid #CCC;z-index:9;background:#FFFFFF;'></div>";
		if($(this).attr("multiSelect")=="true"){
       		html += "<input type='text' style='display:none' id='"+$(this).attr("id")+"_values"+"'>";
       	}
		$(this).after(html);
  });
  
   //3.加载数据
   var dm = "";
   $("select[dm][cache='true']").each(function(){//cache=true 所有select
   		var temp_dm = $(this).attr("dm")+"~"+$(this).attr("id");
   		dm += dm==""?temp_dm:","+temp_dm;
   });
   if(dm != ""){
   		$.ajax({
	  		type: "POST",
		    url:   sys_ctx+"/dmCache/default.do?method=getDm&dm="+dm,
		    dataType: "json",
		    success:function(json){
		    	//如果有错误或者警告信息，弹出对话框
		    	if(json.msg!=""){
		    		sys_alert(json.msg); return;
		    	}
		    	bind(json);
		    	$.each(json.selectData,function(){
		    		var v = $(this);
		    		for(var i=0;i<v.length;i++){
		    			var temp_selectData = {
		    					dm: v[i].dm,
		    					mc: v[i].mc,
		    					py: v[i].py,
		    					zt_dm: v[i].zt_dm,
		    					lb_dm: v[i].lb_dm,
		    					sjdm: v[i].sjdm,
		    					selectid: v[i].selectid
		    				};
		    			effective_selectData.insert(temp_selectData);
		    		}
		    		//生成列表
		    		var selectid;
		    		if(v.length>0){
		    			selectid = v[0].selectid;
		    			if(!$("#"+selectid).attr("sjid")){
		    				$("#"+selectid).editableSelect(v);
		    			}
		    		}
		    	});
		    }
	  	});	
   }
   
   //3. 回显文本到input cache=true 一级select 二级和ajax必须提供selectID_MC字段
   $("body").ajaxComplete(function(){
	    $("select[class='editableSelect']").each(function(){
	    	 if($(this).find("option:selected").text()!="请选择" && $(this).find("option:selected").text()!=""){
				$(this).next(".editable-select").attr("value",$(this).find("option:selected").text());
			 }
	    });
   });
   
   
   //4. 列表显示隐藏
   $("input.editable-select").live("click",function(){
   		var input = $(this);
        var select = $(this).attr("id").substring(0,$(this).attr("id").lastIndexOf("_"));
   		$(this)[0].select();
		if ($(this).next(".editable-select-options").is(':visible')) {
             $(this).multiSelectOptionsHide();
             if($("#"+select).attr("multiSelect")=="true"){
        		var select_values = $("#"+$("#"+select).attr("id")+"_values").val();
				 var checked_count = select_values.split("~").length - 1;
				 if(checked_count==1){
					var oldval = $("#"+select).val();
					$("#"+select).val(select_values.split("~")[0]);
					$("#"+select).changev(oldval);
				 }
        	 }
         }else{
         	if($("#"+select).text().indexOf($(this).val())!=-1){
				$(this).next(".editable-select-options").children("ul").children("li").each(function(){
					$(this).show();
				});
			}
        	$(this).multiSelectOptionsShow();
        	$(this).next(".editable-select-options").children("ul").children("li").css({width:$(this).next(".editable-select-options").children("ul").width()});
         }
	});
	
	//拼音查找
	$("select[class='editableSelect']").each(function(){
		var select = $(this);
		var selectid = $(this).attr("id");
		var sjdm = "";
		var lb_dm = $(this).attr("dm").substring($(this).attr("dm").indexOf("=")+1);
		// 页面缓存查找
		if($(this).attr("cache")!="false" || $(this).attr("cache")=="null"){
    		$(this).next(".editable-select")[0].onpropertychange = function(){
    			$(select).next(".editable-select").next(".editable-select-options").children("ul").children("li").css({width:'100%'});
    			if($(this).val()!="" && $(this).val().indexOf("个已选择")==-1){
    				if($(select).attr("sjid")){
						sjdm = $("#"+$(select).attr("sjid")).val();
						if($("#"+$(select).attr("sjid")).val()==""){
							sys_alert("请先选择"+$("#"+$(select).attr("sjid")).attr("showName"));
							return;
						}
					}
    				$(select).next(".editable-select").next(".editable-select-options").children("ul").children("li").each(function(){
						$(this).hide();
					});
					var result = eval(effective_selectData.stringify({py:{like:$(this).val()},selectid:selectid,sjdm:sjdm,zt_dm:1,lb_dm:lb_dm}));
					if(result.length>0){
						for(var i=0;i<result.length;i++){
							$(select).next(".editable-select").next(".editable-select-options").children("ul").children("li").each(function(){
								if($(this).attr("id")==result[i].dm){$(this).show();}
							});
						}
					}
    			}else{
    				$(select).next(".editable-select").next(".editable-select-options").children("ul").children("li").each(function(){
						$(this).show();
					});
					$(select).get(0).selectedIndex = 0;
    			}
    			var iwidth = $(select).next(".editable-select").next(".editable-select-options").children("ul").width();
    			if(iwidth!=0){
    				$(select).next(".editable-select").next(".editable-select-options").children("ul").children("li").css({width:iwidth});
    			}
    		}
    	}
    	
    	
    		
	});
	
   //5. 文本框 ajax请求
    $("input.editable-select").live("keyup",function(){
		var input = $(this);
        var selectid = $(this).attr("id").substring(0,$(this).attr("id").lastIndexOf("_"));
        var dm = $("#"+selectid).attr("dm");
        //有sjid的select的ajax查询
        if($("#"+selectid).attr("cache")=="false" && $("#"+selectid).attr("sjid") && event.keyCode!=40 && event.keyCode!=38 && event.keyCode!=13){
       		if($(input).val()!="" && $(this).val().indexOf("个已选择")==-1){
				var ss = $(input).val();
				var sjid = $("#"+selectid).attr("sjid");
				if($("#"+sjid).val()==""){
					sys_alert("请先选择"+$("#"+sjid).attr("showName"));return;
				}
				var sjdm = $("#"+sjid).val();
				var search = $(input).val();
		        setTimeout(function(){
					var temp_s = $(input).val(); 
					if(search==temp_s){
						$(input).next(".editable-select-options").next(".divframe").html("");
						//alert("ajax查询");
						var ajax_url = sys_ctx+"/dmCache/default.do?method=ajax&dm="+dm+"&py="+ss+"&selectid="+selectid+"&sjdm="+sjdm;
						ajax_url = encodeURI(ajax_url);
						$.ajax({
					  		type: "POST",
						    url:   ajax_url,
						    dataType: "json",
						    beforeSend: function(){$(":input").removeAttr('disabled');},
						    success:function(json){
						    	$("#"+selectid).empty();
						    	$("#"+selectid).next(".editable-select").next(".editable-select-options").children("ul").empty();
								bind(json);
								$("#"+selectid).editableSelect();
						    },
						    complete:function(){
						    	setTimeout(function(){
						    		$(input).focus();
						    		$(input).multiSelectOptionsShow();
						    		$(input).next(".editable-select-options").children("ul").children("li").css({width:$(input).next(".editable-select-options").children("ul").width()});
						    	},200);
						    }
					  	});	
					}
		        },200);
			}else{
				$("#"+selectid).get(0).selectedIndex = 0;
			}
       	}
       	//一级select的ajax查询
		if($("#"+selectid).attr("cache")=="false" && !$("#"+selectid).attr("sjid")){
       		if($(input).val()!="" && $(this).val().indexOf("个已选择")==-1){
				var ss = $(input).val();
				var ajax_url = sys_ctx+"/dmCache/default.do?method=ajax&dm="+dm+"&py="+ss+"&selectid="+selectid;
				ajax_url = encodeURI(ajax_url);
				$.ajax({
			  		type: "POST",
				    url:  ajax_url,
				    dataType: "json",
				    beforeSend: function(){$(":input").removeAttr('disabled');},
				    success:function(json){
						bind(json);
						$("#"+selectid).editableSelect();
				    },
				    complete:function(){
				    	setTimeout(function(){
				    		$(input).focus();
				    		$(input).multiSelectOptionsShow();
				    		$(input).next(".editable-select-options").children("ul").children("li").css({width:$(input).next(".editable-select-options").children("ul").width()});
				    	},200);
				    	
				    }
			  	});	
			}
       	}
        
	     //光标在文本框时，键盘事件 向下
	     if(event.keyCode==40){
	     	if (!$(this).next(".editable-select-options").is(':visible')) {
	     		 if($("#"+selectid).text().indexOf($(this).val())!=-1){
					$(this).next(".editable-select-options").children("ul").children("li").each(function(){
						$(this).show();
					});
				 }
	             $(input).multiSelectOptionsShow();
	         }
	     	if(!$(this).next(".editable-select-options").children("ul").find("li:visible").hasClass('hover')){
	     		$(this).next(".editable-select-options").children("ul").find("li:visible:first").addClass('hover').css({'background-color': '#C0C0C0'});
	     		$(this).next(".editable-select-options").children("ul").find("li:hidden").removeClass('hover').css({'background-color': '#FFFFFF'});
	     	}else{
	     		var lihovervsible = $(this).next('.editable-select-options').children("ul").find('li.hover');
	     		$(lihovervsible).removeClass('hover').css({'background-color': '#FFFFFF'});
	            $(lihovervsible).nextAll('li:visible:first').addClass('hover').css({'background-color': '#C0C0C0'})
	     	}
	     }
	     //光标在文本框时，键盘事件 向上
	     if (event.keyCode == 38) {
	     	 if (!$(this).next(".editable-select-options").is(':visible')) {
	     	 	 if($("#"+selectid).text().indexOf($(this).val())!=-1){
					$(this).next(".editable-select-options").children("ul").children("li").each(function(){
						$(this).show();
					});
				 }
	             $(input).multiSelectOptionsShow();
	         }
	         if (!$(this).next('.editable-select-options').children("ul").find('li:visible').hasClass('hover')) {
	             $(this).next('.editable-select-options').children("ul").find('li:visible:last').addClass('hover').css({'background-color': '#C0C0C0'});
	             $(this).next(".editable-select-options").children("ul").find("li:hidden").removeClass('hover').css({'background-color': '#FFFFFF'});
	         } else {
	         	 var lihovervsible = $(this).next('.editable-select-options').children("ul").find('li.hover');
	     		$(lihovervsible).removeClass('hover').css({'background-color': '#FFFFFF'});
	            $(lihovervsible).prevAll('li:visible:first').addClass('hover').css({'background-color': '#C0C0C0'})
	         }
	     }
	     //回车 选中高亮的选项
	     if(event.keyCode == 13){
	     	$(this).next('.editable-select-options').children("ul").find("li[class='hover']").click();
	     }
	});
  		
});//jquery结束 

//转换列表插件
if (jQuery) (function($) {
    $.extend($.fn, {
    	//转换有效列表
        editableSelect: function(selectData) {
        	var select = $(this);
        	var option_list = $(this).next(".editable-select").next(".editable-select-options").children("ul");
        	if($(this).attr("cache")!="false"){
	       		for(var i=0;i<selectData.length;i++){
	    			if(selectData[i].zt_dm==1){
						var li = '<li id="'+selectData[i].dm+'" style="width:100%">'+ selectData[i].mc +'</li>';
	      				option_list.append(li);
				    }
	    		}
        	}else{
        		$(select).find("option").each(function() {
        			if($(this).text()!="请选择"){
        				var li = '<li id="'+$(this).val()+'" style="width:100%">'+ $(this).text() +'</li>';
			        	option_list.append(li);
        			}
		        });
        	}
        	//添加多选
        	if($(this).attr("multiSelect")=="true"){
        		$(this).next(".editable-select").next(".editable-select-options").children("ul").find("li").addClass("check");
        	}
        	
        	//列表鼠标划过颜色改变
			//点击列表回显
			var input = $(this).next(".editable-select");
			$(input).next(".editable-select-options").children("ul").children("li")
			.mouseover(function(){
				if($(select).attr("multiSelect")=="true"){
					if(!$(this).hasClass("checked")){
						$(this).addClass('hover');
						$(this).css({'background-color': '#C0C0C0'});
						$(this).siblings(".hover").removeClass("hover").css({'background-color': '#FFFFFF'});
					}
				}else{
					$(this).css({'background': '#C0C0C0'});
					$(this).addClass('hover');
					$(this).siblings(".hover").removeClass("hover").css({background: '#FFFFFF'});
				}
			}).mouseout(function(){
				if($(select).attr("multiSelect")=="true"){
					if(!$(this).hasClass("checked")){
						$(this).css({'background-color': '#FFFFFF'});
						$(this).removeClass('hover');
					}
				}else{
					$(this).css({'background': '#FFFFFF'});
					$(this).removeClass('hover');
				}
				
			}).click(function(){
				if($(select).attr("multiSelect")=="true"){
					if(!$(this).hasClass("checked")){
						$(this).css({'background-color': '#C0C0C0'});
						$(this).addClass('checked').removeClass('hover');
						var select_values = $("#"+$(select).attr("id")+"_values").val();
						select_values += $(this).attr("id")+"~";
						$("#"+$(select).attr("id")+"_values").attr("value",select_values);
						var checked_count = select_values.split("~").length - 1;
						$(input).attr("value",checked_count+"个已选择");
					}else{
						$(this).css({'background-color': '#FFFFFF'});
						$(this).removeClass('checked').addClass('check').removeClass('hover');
						var select_values = $("#"+$(select).attr("id")+"_values").val();
						select_values = select_values.replace($(this).attr("id")+"~","");
						$("#"+$(select).attr("id")+"_values").attr("value",select_values);
						var checked_count = select_values.split("~").length - 1;
						$(input).attr("value",checked_count+"个已选择");
					}
				}else{
					var oldval = $(select).val();
					$(input).attr("value",$(this).html());
					$(input).multiSelectOptionsHide();
					$(select).val($(this).attr("id"));
					$(select).changev(oldval);
				}
			});
			//隐藏列表框
			$('*').click(function(){
				if($(input).next(".editable-select-options").is(':visible') && !$(this).hasClass("editable-select-options")){
					multiSelectCurrent = $(input);
            		timer = setTimeout('jQuery(multiSelectCurrent).multiSelectOptionsHide();', 250);
				}
			});
        },
        
        //隐藏列表
        multiSelectOptionsHide: function() {
        	$(this).next(".editable-select-options").next(".divframe").empty();
        	$(this).next(".editable-select-options").next(".divframe").hide();
	    	$(this).next(".editable-select-options").hide();
        },
        
        //显示列表
        multiSelectOptionsShow: function() {
        	var input = $(this);
        	var select = $(this).attr("id").substring(0,$(this).attr("id").lastIndexOf("_"));
        	var offset = $(this).position();
        	//删除iframe
        	$(this).next(".editable-select-options").next(".divframe").html("");
        	//定义div iframe的高度和宽度
        	$(this).next(".editable-select-options").css({ width: $(input).width()+17 + 'px' });
			var iheight = $(this).next(".editable-select-options").height();
			if(iheight>200 || iheight==0){
				$(this).next(".editable-select-options").css({ height: '200px' });
				$(this).next(".editable-select-options").next(".divframe").css({ height: 200 + 'px' });
			}
        	//处理并显示iframe的div
        	$(this).next(".editable-select-options").next(".divframe").css({ top: offset.top + $(input).outerHeight() + 'px' });
			$(this).next(".editable-select-options").next(".divframe").css({ left: offset.left + 'px' });
			$(this).next(".editable-select-options").next(".divframe").css({ width: $(input).width()+17 + 'px' });
			$(this).next(".editable-select-options").next(".divframe").show();
			//处理并显示列表
			$(this).next(".editable-select-options").css({ top: offset.top + $(this).outerHeight() + 'px' });
			$(this).next(".editable-select-options").css({ left: offset.left + 'px' });
			$(this).next(".editable-select-options").show();
			
			//增加iframe 
			var L=document.createElement('IFRAME');  
	        L.name='completionFrame';  
	        L.width=$(this).next(".editable-select-options").next(".divframe").width();  
	        L.height=$(this).next(".editable-select-options").next(".divframe").outerHeight();  
	        $(this).next(".editable-select-options").next(".divframe").append(L);  
			//隐藏其他列表			
			$("input.editable-select").each(function(){
				if($(this).attr("id")!=$(input).attr("id")){
					$(this).multiSelectOptionsHide();
				}
			});
			
        },
        changev: function(oldval){
        	var parent_id = $(this).attr("id");
        	var parent_val = $(this).val();
        	if(parent_val==oldval){return;}
        	$("select[dm][sjid='"+$(this).attr("id")+"'][cache='false']").each(function(){
        		$(this).empty();
        		$(this).next(".editable-select").attr("value","");
        		$(this).next(".editable-select").next(".editable-select-options").children("ul").empty();
        		$("#"+$(this).attr("id")+"_values").attr("value","");
        	});
        	$("select[dm][sjid='"+$(this).attr("id")+"'][cache!='false']").each(function(){
        		$(this).empty();
        		var option_list = $(this).next(".editable-select").next(".editable-select-options").children("ul");
               	$(option_list).empty();
        		$(this).next(".editable-select").attr("value","请选择");
        		
        		var lb_dm = $(this).attr("dm").substring($(this).attr("dm").indexOf("=")+1);
               	var selectid = $(this).attr("id");
               	var sjdm = $("#"+$(this).attr("sjid")).val();
               
               	var result = eval(effective_selectData.stringify({lb_dm:lb_dm,zt_dm:1,selectid:selectid,sjdm:sjdm}));
               	if(result.length>0){
		   			$(this).append("<option id=''>请选择</option>");
		   			for(var i=0;i<result.length;i++){
		   				var option = '<option value="'+result[i].dm+'">'+ result[i].mc +'</option>';
		        		$(this).append(option);
		   			}
		   			$(this).editableSelect(result);
               	}else{
               		//cache=null情况下，普通联动加载
               		var child_id = $(this).attr("id");
	        		var child_dm = $(this).attr("dm");
					if($(this).attr("cache")==null){
	        			$.ajax({
					  		type: "POST",
						    url:   sys_ctx+"/dmCache/default.do?method=getChild&dm="+child_dm+"&sjdm="+parent_val+"&selectid="+child_id ,
						    dataType: "json",
						    success:function(json){
								bind(json);
								var selectData;
								for(var key in json.selectData){
									selectData = json.selectData[key];
								}
								for(var i=0;i<selectData.length;i++){
					    			var temp_selectData = {
					    					dm: selectData[i].dm,
					    					mc: selectData[i].mc,
					    					py: selectData[i].py,
					    					zt_dm: selectData[i].zt_dm,
					    					lb_dm: selectData[i].lb_dm,
					    					sjdm: selectData[i].sjdm,
					    					selectid: selectData[i].selectid
					    				};
					    			effective_selectData.insert(temp_selectData);
					    		}
					    		$("#"+selectid).editableSelect(selectData);
						    }
					  	});
	        		}
               	}
        	});
        }
    });

})(jQuery);
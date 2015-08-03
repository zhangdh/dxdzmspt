var fbl;
var leftgb="";
var leftscroll="";
var screenWidth=screen.width;
$(function(){
    screenwidth();
  // Very basic usage

  $.fn.EasyWidgets({
      behaviour : {
      useCookies : true
    },
    i18n : { 
      closeText : "<span style='position:relative;left:"+leftgb+";'><img src = "+sys_ctx+"/css/1/desk_si_03.gif border=0></span>",
      extendText : "<span style='position:relative;left:"+leftscroll+";'><img src = "+sys_ctx+"/css/1/desk_si_01.gif border=0></span>",
      collapseText : "<span style='position:relative;left:"+leftscroll+";'><img src = "+sys_ctx+"/css/1/desk_si_02.gif border=0></span>",
     
      confirmMsg:'确定要关闭?'      
    }
  });
  
});

function screenwidth(){
	if ((screen.width == 1280)){
	   leftgb="10%";//65
	   leftscroll="9%";//63
	}else if (screen.width >= 1440){
	   leftgb="13%";
	   leftscroll="12%";
	}else if (screen.width >= 1024){
	   leftgb="9%";
	   leftscroll="8%";
	}else if (screen.width >= 800){
	   leftgb="5%";
	   leftscroll="4%";
	}
}
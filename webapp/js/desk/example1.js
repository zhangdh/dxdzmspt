
// DOM ready!

$(function(){

  // Use the cookie plugin
  
  $.fn.EasyWidgets({

    behaviour : {
      useCookies : true
    },
    i18n : {
      editText : 'Edit',
      closeText : 'fffff',
      extendText : 'Extend',
      collapseText : 'Collapse',
      cancelEditText : 'Cancel',
      editTitle : 'Edit this widget',
      closeTitle : 'Close this widget',
      confirmMsg : 'Remove this widget?',
      cancelEditTitle : 'Cancel edition',
      extendTitle : 'Extend this widget',
      collapseTitle : 'Collapse this widget'
    },

    callbacks : {

      onAdd : function(){
        Log('onAdd');
      },

      onEdit : function(){
        Log('onEdit');
      },

      onShow : function(){
        Log('onShow');
      },

      onHide : function(){
        Log('onHide');
      },

      onClose : function(){
        Log('onClose');
      },

      onEnable : function(){
        Log('onEnable');
      },

      onExtend : function(){
        Log('onExtend');
      },

      onDisable : function(){
        Log('onDisable');
      },

      onDragStop : function(){
        Log('onDragStop');
      },

      onCollapse : function(){
        Log('onCollapse');
      },

      onAddQuery : function(){
        Log('onAddQuery');
        return true;
      },

      onEditQuery : function(){
        Log('onEditQuery');
        return true;
      },

      onShowQuery : function(){
        Log('onShowQuery');
        return true;
      },

      onHideQuery : function(){
        Log('onHideQuery');
        return true;
      },

      onCloseQuery : function(){
        Log('onCloseQuery');
        return true;
      },

      onCancelEdit : function(){
        Log('onCancelEdit');
      },

      onEnableQuery : function(){
        Log('onEnableQuery');
        return true;
      },

      onExtendQuery : function(){
        Log('onExtendQuery');
        return true;
      },

      onDisableQuery : function(){
        Log('onDisableQuery');
        return true;
      },

      onCollapseQuery : function(){
        Log('onCollapseQuery');
        return true;
      },

      onCancelEditQuery : function(){
        Log('onCancelEditQuery');
        return true;
      },

      onChangePositions : function(){
        Log('onChangePositions');
      },

      onRefreshPositions : function(){
        Log('onRefreshPositions');
      }
    }

  });

  // Some Ajax progress stuff for Widgets on demand
  
  $('#ajax-progress').ajaxStart(function(){
    $(this).show();
  }).ajaxStop(function(){
    $(this).hide();
  });
  
});

var callbackNum = 0;

function Log(msg){
  callbackNum++;
  var console = $('#callbacks-console');
  console.append('<div>'+callbackNum+' :: '+msg+'</div>');
  // A barbarity, but ok for this time
  console.scrollTop(99999);
  return true;
}

function AddWidget(url, placeId){
  $.get(url, function(html){
    $.fn.AddEasyWidget(html, placeId);
  });
}

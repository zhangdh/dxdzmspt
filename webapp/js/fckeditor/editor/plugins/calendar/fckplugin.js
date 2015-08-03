// Register the related commands.
FCKCommands.RegisterCommand( 'My_Calendar' , new FCKDialogCommand( 'My_Calendar' , FCKLang['DlgMyCalendarTitle'] , FCKConfig.PluginsPath + 'calendar/calendar.html' , 340, 220 ) ) ;

// Create the "My_Calendar" toolbar button.
var oFindItem = new FCKToolbarButton( 'My_Calendar', FCKLang['DlgMyCalendarTitle'] ) ;
oFindItem.IconPath = FCKConfig.PluginsPath + 'calendar/calendar.gif' ;

FCKToolbarItems.RegisterItem( 'My_Calendar', oFindItem ) ; // 'My_Button' is the name used in the Toolbar config.

FCKConfig.Plugins.Add( 'My_Calendar' ) ;

FCK.ContextMenu.RegisterListener( {
    AddItems : function( menu, tag, tagName )
    {		
            // under what circumstances do we display this option
            if ( tagName == 'INPUT' && (tag.onfocus || tag.className == 'Wdate') )
            {
                    // when the option is displayed, show a separator  the command
                    menu.AddSeparator() ;
                    // the command needs the registered command name, the title for the context menu, and the icon path
                    oFindItem.IconPath = FCKConfig.PluginsPath + 'calendar/calendar.gif' ;
                    menu.AddItem( 'My_Calendar', FCKLang.DlgMyCalendarTitle, oFindItem.IconPath ) ;
            }
    }}
);

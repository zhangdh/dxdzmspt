// Register the related commands.
FCKCommands.RegisterCommand( 'UserPicker' , new FCKDialogCommand( 'UserPicker' , FCKLang['DlgUserPickerTitle'] , FCKConfig.PluginsPath + 'userpicker/userpicker.html' , 340, 250) ) ;

var oFindItem = new FCKToolbarButton( 'UserPicker', FCKLang['DlgUserPickerTitle'] ) ;
oFindItem.IconPath = FCKConfig.PluginsPath + 'userpicker/userpicker.gif' ;

FCKToolbarItems.RegisterItem( 'UserPicker', oFindItem ) ; 

FCKConfig.Plugins.Add( 'UserPicker' ) ;

FCK.ContextMenu.RegisterListener( {
    AddItems : function( menu, tag, tagName )
    {		
            if ( tagName == 'INPUT' && tag.pickertype!='' )
            {
                    // when the option is displayed, show a separator  the command
                    menu.AddSeparator() ;
                    // the command needs the registered command name, the title for the context menu, and the icon path
                    oFindItem.IconPath = FCKConfig.PluginsPath + 'userpicker/userpicker.gif' ;
                    menu.AddItem( 'UserPicker', FCKLang.DlgUserPickerTitle, oFindItem.IconPath ) ;
            }
    }}
);

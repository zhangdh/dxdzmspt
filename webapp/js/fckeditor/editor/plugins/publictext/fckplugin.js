// Register the related commands.
FCKCommands.RegisterCommand( 'PublicText' , new FCKDialogCommand( 'PublicText' , FCKLang['DlgPublicTextTitle'] , FCKConfig.PluginsPath + 'publictext/publictext.html' , 340, 220 ) ) ;

var oFindItem = new FCKToolbarButton( 'PublicText', FCKLang['DlgPublicTextTitle'] ) ;
oFindItem.IconPath = FCKConfig.PluginsPath + 'publictext/textfield.gif' ;

FCKToolbarItems.RegisterItem( 'PublicText', oFindItem ) ;

FCKConfig.Plugins.Add( 'PublicText' ) ;

FCK.ContextMenu.RegisterListener( {
    AddItems : function( menu, tag, tagName )
    {		
            if ( tagName == 'INPUT' && (tag.name=='_public_text_field') )
            {
                    // when the option is displayed, show a separator  the command
                    menu.AddSeparator() ;
                    // the command needs the registered command name, the title for the context menu, and the icon path
                    oFindItem.IconPath = FCKConfig.PluginsPath + 'publictext/textfield.gif' ;
                    menu.AddItem( 'PublicText', FCKLang.DlgPublicTextTitle, oFindItem.IconPath ) ;
            }
    }}
);

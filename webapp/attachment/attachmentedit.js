sys_btn_auth = "btn_dg;btn_save;btn_qz";
var mFileType=".doc";
var mTemplate="";
var mRecordID="";
var mEditType="1,1";
var mShowType="1";
var mUserName=null;
var mDescript="";
var mFileName="";
var mDisabled="";
var mDisabledSave="";
var mWord="";
var mExcel="";
var mHTMLPath="";
var mFileDate="";
var entryid="";
var dgbj="0";//0、正文 1、定稿
var modleid="";
var showbutton="0";
var fjid="";//附件id
var mk_dm="";
var fjpath="";
var showtype="0";//0、附件　1、正文
var rpath="";//正文相对路径
var bType="1";//0_不显示保存,1_显示保存
var npath="";
var tflag="0";
$(function() {
	var searchStr = window.location.search;
	var params = searchStr.substring(searchStr.lastIndexOf("?") + 1);
	var paramsArray = params.split("&");
	for (var i = 0; i < paramsArray.length; i++) {
	    var tempStr = paramsArray[i];
	    var key = tempStr.substring(0, tempStr.indexOf("="))
	    var value = tempStr.substring(tempStr.indexOf("=") + 1);
	    if (key == "fjid") {
	        fjid = value;
	        continue;
	    }
	    if (key == "mk_dm") {
	        mk_dm = value;
	        continue;
	    }
	   	if (key == "showtype") {
	        showtype = value;
	        continue;
	    }
	    if (key == "rpath") {
	        rpath = value;
	        continue;
	    }
	    if (key == "FileType") {
	        mFileType = value;
	        continue;
	    }
	    if (key == "bType") {
	        bType = value;
	        continue;
	    }
	    if (key == "npath") {
	        npath = value;
	        continue;
	    }	  	    	    
	}
	//取得用户名
    mUserName=uname;
    if(showtype=="0"){//附件
		sys_ajaxPost('/document/default.do?method=queryPathByAttachid','fjid='+fjid+'&mk_dm='+mk_dm,function(json){
					fjpath=json.fjpath;
					mFileType=json.filetype;
					//fjpath="D:/oainfo_temp/docc/20100302/1.doc";
					try{
					//以下属性必须设置，实始化iWebOffice
				    webform.WebOffice.WebUrl=mServerUrl;             //WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档，重要文件
				    webform.WebOffice.RecordID=mRecordID;            //RecordID:本文档记录编号
				    webform.WebOffice.Template=mTemplate;            //Template:模板编号
				    webform.WebOffice.FileName=fjpath;            	//FileName:文档名称
				    webform.WebOffice.FileType=mFileType;            //FileType:文档类型  .doc  .xls  .wps
				    webform.WebOffice.UserName=mUserName;            //UserName:操作用户名，痕迹保留需要
				    webform.WebOffice.EditType=mEditType;            //EditType:编辑类型  方式一、方式二  <参考技术文档>
				                                                            //第一位可以为0,1,2,3 其中:0不可编辑;1可以编辑,无痕迹;2可以编辑,有痕迹,不能修订;3可以编辑,有痕迹,能修订；
				                                                            //第二位可以为0,1 其中:0不可批注,1可以批注。可以参考iWebOffice2006的EditType属性，详细参考技术白皮书
				    //webform.WebOffice.Language="CH";					    //Language:多语言支持显示选择   CH 简体 TW繁体 EN英文
				
				    //Start  iWebOffice2006属性  以下属性可以不要
				    webform.WebOffice.PenColor="#FF0000";                   //PenColor:默认批注颜色
				    webform.WebOffice.PenWidth="1";                         //PenWidth:默认批注笔宽
				    webform.WebOffice.ShowToolBar="0";                      //ShowToolBar:是否显示工具栏:1显示,0不显示
				    //End
					webform.WebOffice.ShowMenu="0";
				    //WebSetRibbonUIXML();                                  //控制OFFICE2007的选项卡显示
				    webform.WebOffice.WebSetMsgByName("fjpath",fjpath);//磁盘文件全路径
					webform.WebOffice.WebSetMsgByName("opentype","1");		//0、数据库方式打开 1、服务器文件方式打开
						
				    webform.WebOffice.WebOpen();                            //打开该文档    交互OfficeServer  调出文档OPTION="LOADFILE"    调出模板OPTION="LOADTEMPLATE"     <参考技术文档>
				    webform.WebOffice.ShowType=mShowType;              //文档显示方式  1:表示文字批注  2:表示手写批注  0:表示文档核稿
				
				    //StatusMsg(webform.WebOffice.Status);                    //状态信息
				  }catch(e){
				    alert(e.description);                                   //显示出错误信息
				  }
		});
	}else if(showtype=="1"){//正文
//		sys_ajaxPost('/document/default.do?method=queryPathByAttachid','mk_dm='+mk_dm,function(json){
					fjpath=rpath;
					try{
					//以下属性必须设置，实始化iWebOffice
				    webform.WebOffice.WebUrl=mServerUrl;             //WebUrl:系统服务器路径，与服务器文件交互操作，如保存、打开文档，重要文件
				    webform.WebOffice.RecordID=mRecordID;            //RecordID:本文档记录编号
				    webform.WebOffice.Template=mTemplate;            //Template:模板编号
				    webform.WebOffice.FileName=fjpath;            	//FileName:文档名称
				    webform.WebOffice.FileType=mFileType;            //FileType:文档类型  .doc  .xls  .wps
				    webform.WebOffice.UserName=mUserName;            //UserName:操作用户名，痕迹保留需要
				    webform.WebOffice.EditType=mEditType;            //EditType:编辑类型  方式一、方式二  <参考技术文档>
				                                                            //第一位可以为0,1,2,3 其中:0不可编辑;1可以编辑,无痕迹;2可以编辑,有痕迹,不能修订;3可以编辑,有痕迹,能修订；
				                                                            //第二位可以为0,1 其中:0不可批注,1可以批注。可以参考iWebOffice2006的EditType属性，详细参考技术白皮书
				    webform.WebOffice.Language="CH";					    //Language:多语言支持显示选择   CH 简体 TW繁体 EN英文
				
				    //Start  iWebOffice2006属性  以下属性可以不要
				    webform.WebOffice.PenColor="#FF0000";                   //PenColor:默认批注颜色
				    webform.WebOffice.PenWidth="1";                         //PenWidth:默认批注笔宽
				    webform.WebOffice.ShowToolBar="0";                      //ShowToolBar:是否显示工具栏:1显示,0不显示
				    //End
					webform.WebOffice.ShowMenu="0";
				    //WebSetRibbonUIXML();                                  //控制OFFICE2007的选项卡显示
				    webform.WebOffice.WebSetMsgByName("fjpath",fjpath);//磁盘文件全路径
					webform.WebOffice.WebSetMsgByName("opentype","1");		//0、数据库方式打开 1、服务器文件方式打开
					
					
				    webform.WebOffice.WebOpen();                            //打开该文档    交互OfficeServer  调出文档OPTION="LOADFILE"    调出模板OPTION="LOADTEMPLATE"     <参考技术文档>
				    webform.WebOffice.ShowType=mShowType;              //文档显示方式  1:表示文字批注  2:表示手写批注  0:表示文档核稿
					SaveDocument();
				    //StatusMsg(webform.WebOffice.Status);                    //状态信息
				  }catch(e){
				    alert(e.description);                                   //显示出错误信息
				  }
//		});
		if(bType=="0"){
			sys_showButton("");
		}
	}
	//保存修改数据
	$("#btn_save").click(function(){
		tflag="1";
		SaveDocument();
	});
});
//分页查询回调函数
function callback_getPageData_table_list(pagenum){
	var querystr=$("#form_query").serialize();
	querystr+="&page_goto="+pagenum;
	sys_ajaxGet("/syspara/default.do?method=query",querystr);
}
//点击列表显示明细数据回调函数
function callback_trclick_table_list(id){
	$('#form_show')[0].reset();
	sys_ajaxGet("/syspara/default.do?method=show",{id:id},function(json){
		bind(json);
	});
	sys_showButton("btn_modi");
}


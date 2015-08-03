var fjid;
var mk_maxSize = 5;
var mk_dir;
var mk_dm ;
var mk_mc;
var bz = 0;
$(function(){
	try {
		var  swf  =  new  ActiveXObject('ShockwaveFlash.ShockwaveFlash');  
	} catch(err) {
		alert('请安装flash10插件,下载地址【个人设置-控件下载】');
	}
	//默认不初始化附件
	// mk_dm = $("#mk_dm").val();
	// mk_mc = $("#mk_mc").val();
	//$("#attachment_span").append("<span spanID='att_upload_new' id='att_upload_new' name='att_upload_new' ></span><input type='button' name='sys_attachment_wjsc' id='sys_attachment_wjsc'  onclick='openSc()' value='文件上传' >&nbsp;<input type='hidden' class='fjid' name='fjid' id='fjid' value=''/>&nbsp;<input type='hidden' class='sys_attachment_xgbz' id='sys_attachment_xgbz' name='sys_attachment_xgbz'  value='' />&nbsp;<input type='hidden' class='fjid_Token' name='fjid_Token' id='fjid_Token' value=''/>");
	$("#scfj").click(function(){
		$('#uploadify').uploadify("upload","*");
	});
	$("#qxfj").click(function(){
		$('#uploadify').uploadify();
		
	});
	$("#delfj").click(function(){
		var wjlj = $("#attachments").val()
		if(wjlj == "")return;
		_Post("/uploadfile.coffice?method=delFj","wjlj="+wjlj,function(jsonData){
			if(jsonData.result == true){
				$("#attachments option[value='"+wjlj+"']").remove();
			}else{
				alertMsg(jsonData);
			}		  
		})
	});
});
function openSc(v){
	fjid = $("#fjid").val();
	initFj();
	if(fjid == ""){
		_Post("/uploadfile.coffice?method=getFileInfo","mk_dm="+mk_dm+"&mk_mc="+mk_mc,function(jsonData){
			if(v=="form"){
				fjid = $("#id").val();
			}else{
				fjid = jsonData.fjid;
			}
			$("#fjid").val(fjid);
		 	mk_maxSize = jsonData.mk_maxSize;
		 	mk_dir = jsonData.mk_dir;			
			selectFile();
		 });
		 return;
	}
	selectFile();
}
function selectFile(){
	 if(bz == 1){
	 	$('#uploadify').uploadify('destroy');
	 }
	 _Post("/uploadfile.coffice?method=getFileList","zid="+$("#fjid").val(),function(jsonData){
	 	_loadSelect1(jsonData);
	 });	 
	 $("#files").html("");
	 $('#attachment_div').window({
       	onBeforeClose:function(){ 
     		$("#attachments option").each(function(){
     			var wjlj = $(this).val();
     			var wjmc = $(this).text();
     			var infolj = mk_dir.split(":")[1];
     			var ablj = "http://"+window.location.host+infolj+"/"+wjlj;
     			 $("#files").append("<a href='"+ablj+"' target='_blank' style='text-decoration:none;'>"+wjmc+"</a>&nbsp;&nbsp;");
			});
       	}
     });
	 $("#attachment_div").window("open");
	 $("#uploadify").uploadify({
             'auto':false,
             'height': 25,
             'buttonText':'选择文件',
             // 'formData':{'mk_dm':mk_dm,'mk_mc':mk_mc,'mk_maxSize':mk_maxSize,'mk_dir':mk_dir,'fjid':fjid},
             //'cancelImg' : webcontext+'/js/uploadify3.2/cancel.png', // 取消按钮的图片  
             'swf':webcontext+'/js/uploadify3.2/uploadify.swf?ver='+ Math.random(), 
             'progressData':'per',
             'queueID':'fileQueue',
             'fileSizeLimit':mk_maxSize*1024+"k",
             'uploader':webcontext+'/attachment/UploadFile.jsp?mk_dm='+mk_dm+'&mk_mc='+mk_mc+"&mk_maxSize="+mk_maxSize+"&mk_dir="+mk_dir+"&fjid="+fjid,
             'onFallback':function(){
             	alert('flash不兼容');
             },
             'onUploadComplete':function(file){
              	
             },
             'onUploadError':function(file,errorCode,errorMsg,errorString){
              	
             },
             'onQueueComplete':function(queueData){
              
             },
             'onUploadSuccess':function(file,data,response){
             	var a = "<option value='"+data+"'>"+file.name+"</option>";
             	$("#attachments").append(a);
             },
             'onSelectError':function(file,errorCode,errorMsg){
             	
             }
    });
    bz = 1;
}
function initFj(){
	 mk_dm = $("#mk_dm").val();
	 mk_mc = $("#mk_mc").val();
	//$("#attachment_span").append("<span spanID='att_upload_new' id='att_upload_new' name='att_upload_new' ></span><input type='button' name='sys_attachment_wjsc' id='sys_attachment_wjsc'  onclick='openSc()' value='文件上传' >&nbsp;<input type='hidden' class='fjid' name='fjid' id='fjid' value=''/>&nbsp;<input type='hidden' class='sys_attachment_xgbz' id='sys_attachment_xgbz' name='sys_attachment_xgbz'  value='' />&nbsp;<input type='hidden' class='fjid_Token' name='fjid_Token' id='fjid_Token' value=''/>");
	//$("#attachment_span").append("<span spanID='att_upload_new' id='att_upload_new' name='att_upload_new' ><a href='javascript:void(0)' id='sys_attachment_wjsc' class='easyui-linkbutton' onclick=openSc()>文件上传</a>&nbsp;<input type='hidden' class='fjid' name='fjid' id='fjid' value=''/>&nbsp;<input type='hidden' class='sys_attachment_xgbz' id='sys_attachment_xgbz' name='sys_attachment_xgbz'  value='' />&nbsp;<input type='hidden' class='fjid_Token' name='fjid_Token' id='fjid_Token' value=''/>");
}
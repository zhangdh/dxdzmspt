<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.coffice.base.store.*" %>
<%@ include file="/common/taglibs.jsp" %>
<%
	String filememoStr="";
	String yhid = (String)request.getAttribute("yhid");
	String mk_dm = request.getParameter("mk_dm")==null?"":request.getParameter("mk_dm");
	String att_lj=SysPara.getValue(mk_dm+"_lj")==null?"":SysPara.getValue(mk_dm+"_lj");
	String att_size=SysPara.getValue(mk_dm+"_size")==null?"":SysPara.getValue(mk_dm+"_size");
	
	//自定义路径
	String att_wjlj = request.getParameter("att_wjlj")==null?"":request.getParameter("att_wjlj");
	att_wjlj=java.net.URLDecoder.decode(att_wjlj,"utf-8");
	//System.out.println(att_wjlj);
	if(att_wjlj!=null&&!"".equals(att_wjlj)) {
		att_lj=att_lj+"/"+att_wjlj+"-flag";
	}
	//request.getSession().setAttribute("att_lj",att_lj);
	Cache.setUserInfo(yhid,"att_lj",att_lj);
	int att_size_num=0;
	if(!"".equals(att_size)) {
		att_size_num = Integer.parseInt(att_size)*1024*1024;
	} else {
		att_size_num = 0;
	}
	
	String att_limitname=request.getParameter("att_limitname")!=null?(String)request.getParameter("att_limitname"):"";
	String selname=request.getParameter("selname")!=null?(String)request.getParameter("selname"):"";
	String _fjid=request.getParameter("_fjid")!=null?(String)request.getParameter("_fjid"):"";
	if("undefined".equals(_fjid)) {
		_fjid="";
	}
	String att_num="";
	String att_type=SysPara.getValue(mk_dm+"_type")==null?"":SysPara.getValue(mk_dm+"_type");
	filememoStr=att_type.replace("\"","");
	att_type=att_type.substring(0,att_type.lastIndexOf("\""));
	att_type=att_type.replaceAll("\",\"",";*");
	att_type=att_type.replace("\"","*");
	if(!att_limitname.equals("")){
		att_num=SysPara.getValue(mk_dm+"_num_"+att_limitname);
	}else{
		try{
			att_num=SysPara.getValue(mk_dm+"_num");}
		catch (Exception e){
			att_num="";
		}
	}
	//限制上传个数变量
	String att_size_flag="",numStr="";
	if(!"".equals(att_num)) {
		att_size_flag=att_num;
		numStr="允许上传个数为"+att_num+"个";
	} else {
		att_size_flag="-1";
	}
	
	String att_uploadify_size="";
	if(!att_num.equals("")){
		att_uploadify_size=att_num;
	} else {
		att_uploadify_size=att_size;
	}
	
	//int upload_size = 0;
	//if(!"".equals(att_num)) {
		//upload_size=Integer.parseInt(att_num);
	//} else {
		//upload_size=Integer.parseInt(att_size);
	//}
	
	//获得当前用户的当前模块还有多少空余上传空间
	Store store = new Store();
	int availableSize = store.getAvailableSize(yhid,mk_dm);
%>

<html>
	<head>
		<title>附件</title> 
		<%@ include file="/common/meta.jsp"%>
		<%@ include file="/common/js.jsp" %>
		<link href="${ctx}/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${ctx}/js/uploadify/swfobject.js"></script>
		<script type="text/javascript" src="${ctx}/js/uploadify/jquery.uploadify.v2.0.3.min.js"></script>
			
		<script type="text/javascript">
			try {
				var  swf  =  new  ActiveXObject('ShockwaveFlash.ShockwaveFlash');  
			} catch(err) {
				alert('请安装flash10插件,下载地址【个人设置-控件下载】');
			}
		
		//父页面guid
		var guid='';
		var fjid='';
		//alert(window.dialogArguments.document.getElementById("doc_body"));
		//if(window.dialogArguments.document.getElementById("guid")!=null) {
		if('<%=mk_dm%>'=='document_model'||'<%=mk_dm%>'=='document_fj') {
			if(window.dialogArguments.document.getElementById("id")!=null) {
				guid = window.dialogArguments.document.getElementById("id").value;
			} 
		} else {
			//alert(window.dialogArguments.document.getElementById("id").value);
			if(window.dialogArguments.document.getElementById("guid")!=null) {
				guid = window.dialogArguments.document.getElementById("guid").value;
			} 
		}
		
		//alert(window.dialogArguments.document.getElementById("fjid"));
		//流程类型特殊处理
		if('<%=mk_dm%>'=='document_model') {
			//alert(window.dialogArguments.document.getElementById("doc_body"));
			if(window.dialogArguments.document.getElementById("fjid")==null) {
				//如果fjid为空，则向页面中添加
				var fjid_num='<%=_fjid%>';//定义存取随机数变量
				var oInput=window.dialogArguments.document.createElement("input");
				oInput.type='hidden';
				oInput.id='fjid';
				oInput.value=fjid_num;
				var formshow=window.dialogArguments.document.getElementById("form_show");
				formshow.appendChild(oInput);
				window.dialogArguments.document.getElementById("doc_body").value=fjid_num;
			}
			window.dialogArguments.document.getElementById("fjid").value='<%=_fjid%>';
			window.dialogArguments.document.getElementById("doc_body").value='<%=_fjid%>';
		} else if('<%=mk_dm%>'=='document_fj') {
			if(window.dialogArguments.document.getElementById("_fjid")==null) {
				//如果fjid为空，则向页面中添加
				var fjid_num='<%=_fjid%>';//定义存取随机数变量
				var oInput=window.dialogArguments.document.createElement("input");
				oInput.type='hidden';
				oInput.id='fjid';
				oInput.value=fjid_num;
				var formshow=window.dialogArguments.document.getElementById("form_show");
				formshow.appendChild(oInput);
				window.dialogArguments.document.getElementById("doc_body").value=fjid_num;
			} else {
				window.dialogArguments.document.getElementById("_fjid").value='<%=_fjid%>';
			}
		} else {
			if(window.dialogArguments.document.getElementById("fjid")==null) {
				//如果fjid为空，则向页面中添加
				var fjid_num='';//定义存取随机数变量
				var oInput=window.dialogArguments.document.createElement("input");
				oInput.type='hidden';
				oInput.id='fjid';
				oInput.value=fjid_num;
				var formshow=window.dialogArguments.document.getElementById("form_show");
				formshow.appendChild(oInput);
			}
		}
		
		if('<%=mk_dm%>'=='document_fj') {
			fjid = window.dialogArguments.document.getElementById("_fjid").value;
		} else {
			fjid = window.dialogArguments.document.getElementById("fjid").value;
		}
		$(function(){
			//document.getElementById("shangchuanButton").style.display="none";
			sys_ajaxGet("/attachment/default.do?method=listUploadify&mk_dm=<%=mk_dm%>&zid="+fjid+"&selectName=<%=selname%>","",function(json){
				bind(json);
				//alert($("#sys_attachment_uplodify").get(0).length-1);
				//var uploadedNum = $("#sys_attachment_uplodify").get(0).length-1;
				//if(uploadedNum>5) {
					//document.getElementById("shangchuanButton").style.display="none";
				//} else {
					//document.getElementById("shangchuanButton").style.display="";
				//}
			});
			uploadify_onload();
		});
		
		function uploadify_onload() {
			var randomNUM = Math.random(); // 随机数
			var random_num;//定义存取随机数变量
			var fjidZym;//主页面的fjid
			var mkdm;//返回数据中的模块代码
			var selectName;
			fjid=fjid+'_';
			guid=guid+'_';
			//随机数,辨别是否为新建页面
			var random_obj = window.dialogArguments.document.getElementById("random_num");
			if(random_obj==null||random_obj.value=='') {
				//如果random_obj为空，则为新建页面
				random_num=randomNUM;
				var oInput=window.dialogArguments.document.createElement("input");
				oInput.type='hidden';
				oInput.id='random_num';
				oInput.value=random_num;
				var formshow=window.dialogArguments.document.getElementById("form_show");
				if(formshow==null) {
					formshow=window.dialogArguments.document.createElement("form_show");
					formshow.appendChild(oInput);
				} else {
					formshow.appendChild(oInput);
				}
			} else {   
				//如果不为空，则不为新建页面
				random_num = random_obj.value;
			}
			
			$("#uploadify").uploadify({
                'uploader'       : '${ctx}/js/uploadify/uploadify.swf',
                //'script'         : '${ctx}/servlet/fileUpload?random_num='+random_num+','+guid+';'+sys_yhid+':'+fjid+'~<%=mk_dm%>'+'!<%=att_lj%>'+'^<%=att_size%>',
                'script'         : '${ctx}/attachment/uploadify/uploadify_do.jsp?random_num='+random_num+','+guid+':'+fjid+'~<%=mk_dm%>'+'!<%=att_lj%>'+'^<%=att_size%>'+'<><%=selname%>',
                'cancelImg'      : '${ctx}/js/uploadify/cancel.png',
                'queueID'        : 'fileQueue',
                'auto'           : false,
                'multi'          : true,
                //'queueSizeLimit' : '<%=att_uploadify_size%>',
                //'buttonText'     : 'upload',
                'buttonImg'      : '${ctx}/css/xuanzewenjian.jpg',
                //'buttonImg'      : '${ctx}/css/quxiao.jpg',
                'width'          : '65',
                'height'         : '24',
                'wmode'          : 'transparent',
                'fileExt'        : '<%=att_type%>',
                'fileDesc'       : '请选择<%=att_type%>格式文件',
                'sizeLimit'      : '<%=att_size_num%>',
	            onComplete: function (evt, queueID, fileObj,response, data) {
	            	mkdm=response.substring(response.indexOf(",")+1,response.indexOf(";"));
	            	fjidZym=response.substring(0,response.indexOf(","));
	            	selectName=response.substring(response.indexOf(";")+1,response.length);
	            	//alert(mkdm+'----'+selectName);
	            	//alert(selectName);
	            	//alert(fjidZym);
	            	//将fjid返回给父页面
	            	var fjidObj = window.dialogArguments.document.getElementById("fjid");
	            	if(fjidObj!=null) {
	            		window.dialogArguments.document.getElementById("fjid").value=fjidZym;
	            		if(mkdm=='document_model') {
	            			window.dialogArguments.document.getElementById("doc_body").value=fjidZym;
	            		}
	            		if(mkdm=='document_fj') {
	            			window.dialogArguments.document.getElementById("_fjid").value=fjidZym;
	            		}
	            	}
	            	sys_ajaxGet("/attachment/default.do?method=listUploadify&mk_dm="+mkdm+"&zid="+fjidZym+"&selectName="+selectName,"",function(json){
						bind(json);
					});
					//$('<li></li>').appendTo('.files').text(response);
				},
				onError: function(a, b, c, d) {
					 //alert(d.type);
					 if(d.type='File Size') {
					 	alert("文件大小超出限制！");
					 } else {
					 	alert("文件上传失败！");
					 }
       			},
       			onSelectOnce: function(event,data) {
       				var uploadedNum = $("#sys_attachment_uplodify").get(0).length-1;
       				var totalCount = data.fileCount+uploadedNum;
       				var att_size_pd = '<%=att_size_flag%>';
       				//alert(att_size_pd);
       				if(att_size_pd != '-1') {
       					if(data.fileCount > '<%=att_size_flag%>' || uploadedNum >= <%=att_size_flag%> || totalCount > <%=att_size_flag%>) {
	       					alert('最多上传<%=att_size_flag%>个文件!');
	       					jQuery('#uploadify').uploadifyClearQueue()
       					}
       				}
       				var totalBytes = data.allBytesTotal;
       				//alert(data.allBytesTotal);
       				if(totalBytes > <%=availableSize%>) {
       					alert('已无附件上传空间！');
       					jQuery('#uploadify').uploadifyClearQueue()
       				}
       			}
            });
		}
        
        function uploadifyUpload(){
        	//var category = $('#category').val();
			//$('#uploadify').uploadifySettings('scriptData', {'category' : category});
  			$('#uploadify').uploadifyUpload();
	    }
	   
		function del() {
			//sys_ajaxPost("/attachment/default.do?method=del&mk_dm=<%=mk_dm%>&guid="+$("#sys_attachment_uplodify").val(),"");
			var _url = '${ctx}/attachment/default.do?method=del';
			var querystr = '&mk_dm=<%=mk_dm%>&guid='+$("#sys_attachment_uplodify").val();
			jQuery.ajax({ 
				async : 'false',
		        type : 'GET',   
		        contentType : 'application/json',   
		        url : _url,    
		        data : querystr,   
		        dataType : 'json'   
		   	}); 
			
			if('<%=mk_dm%>'=='document_model'||'<%=mk_dm%>'=='document_fj') {
				sys_ajaxGet("/attachment/default.do?method=listUploadify&mk_dm=<%=mk_dm%>&zid=<%=_fjid%>&_fjid=<%=_fjid%>&selectName=<%=selname%>");
			} else {
				reload.click();	
			}
		}
		
		window.returnValue='True';
        </script>
        <base   target="_self">
	</head>
	<body >
	<table width="700" align="center">
		<tr>
		 <td><a id="reload" href='${ctx}/attachment/uploadify/uploadify.jsp?mk_dm=<%=mk_dm %>' style="display:none"></a></td>
		</tr>
		<tr>
			<td>
				<span class="mainContainer">
				<span class="mainInfo">
				<span id="fileQueue"></span>
					<select name="category" id="category" style="display:none"></select>
					<input type="file" name="uploadify" id="uploadify"/>
					<input type='button' id="shangchuanButton" name='shangchuanButton' onClick="javascript:uploadifyUpload()" style="background :url('${ctx}/css/shangchuan.jpg');height:28px;width:32px;border:0px;cursor:hand" />
					<input type='button' id="quxiaoButton" name='quxiaoButton' onClick="javascript:jQuery('#uploadify').uploadifyClearQueue()" style="background :url('${ctx}/css/quxiao.jpg');height:28px;width:32px;border:0px;cursor:hand"/>
					<span class=files ></span>
				</span> 
				</span>
			</td>
		</tr>
		
		<tr>
			<td>
				<div align="left"><font color="#ff0000">说明：允许附件格式为<%=filememoStr%>文件 大小:<%=att_size%>MB <%=numStr%></font></div>
				<br>
				<select size="5" id="sys_attachment_uplodify" name="sys_attachment_uplodify" style="width: 80%; font-family: 新宋体; font-size: 9pt; z-index:5;"></select>
				<input type="button" id="del" name="del" value="删除" onclick="del();" style="width=62;height=21;"/>
			</td>
		</tr>
	</table>
	</body>
</html>
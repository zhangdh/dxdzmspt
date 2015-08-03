<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/common.jsp"%>
<%@ page import="java.io.*,java.util.*,com.coffice.attachment.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.coffice.util.SysPara"%>
<%@ page import="com.coffice.util.RequestUtil"%>
<%@page import="com.oreilly.servlet.MultipartRequest, net.sf.json.JSONArray, net.sf.json.JSONObject; "%>
<%
request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");

String mk_dm=request.getParameter("mk_dm")!=null?(String)request.getParameter("mk_dm"):"";//模块代码
String zid=request.getParameter("zid")!=null?(String)request.getParameter("zid"):"";//组ID
//获取配置参数,文件类型,文件大小
String att_lj=SysPara.getValue(mk_dm+"_lj");
String att_size=SysPara.getValue(mk_dm+"_size");

String FileUrl = "";
String id="";
String wjmc="",wjlj="",ext="",guid="";
String[] wjljTemp=null;
int wjkj=0;

String Msg;
String Msg1 = "上传文件大小超过限制!"; 
String Msg2 = "文件上传失败!"; 
String Msg3 = "文件上传成功!"; 

String FileWidth = null; 
String FileHeight = null; 
String FileBorder = null; 
String FileTitle = null; 

String  date="";
SimpleDateFormat gs = new SimpleDateFormat("yyyyMMdd");
Date now = new Date();
date = gs.format(now);

try{
  int maxPostSize =Integer.parseInt(att_size)*1024*1024;
  try{        
  String saveDirectory =att_lj+"/"+date+"/";
   	try{

        UploadFileRenamePolicy frp=new UploadFileRenamePolicy();
       	File dirname = new File(saveDirectory); 
		if (!dirname.isDirectory()) 
		{ //目录不存在 
		     dirname.mkdir(); //创建目录
		} 
  		MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"utf-8",frp);
  		Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()){
		String name = (String)files.nextElement();
		File f = multi.getFile(name);
		if(f!=null){
  		String fileName = multi.getFilesystemName(name);
  		String lastFileName= saveDirectory+"/" + fileName;	
  		
  		wjmc=multi.getOriginalFileName(name);
		wjlj="/"+date+"/"+fileName;
	    } } 
  		id=multi.getParameter("id"); 
  		FileWidth=multi.getParameter("imgWidth");
  		FileHeight=multi.getParameter("imgHeight");
  		FileBorder=multi.getParameter("imgBorder");
        FileTitle=multi.getParameter("imgTitle");
        FileUrl=wjlj;       
        Msg=Msg3;
        FileUrl=SysPara.getValue("pic_host")+wjlj;
        out.println( "<html>"); 
    	out.println( "<head>"); 
    	out.println( "<title>error</title>"); 
   		out.println( "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"); 
   		out.println( "</head>"); 
    	out.println( "<body>"); 
    	//out.println( "<script type=\"text/javascript\">parent.KE.plugin[\"image\"].insert(\""+id+"\", \"http://\"+window.location.hostname+\":"+FileUrl+"\","+FileTitle+"\","+FileWidth+"\",\""+FileHeight+"\",\""+FileBorder+"\");</script>"); 
    	out.println( "<script type=\"text/javascript\">parent.editor.insertImage(\""+id+"\",\""+FileUrl+"\",\""+FileTitle+"\",\""+FileWidth+"\",\""+FileHeight+"\",\""+FileBorder+"\");</script>");
   	 	out.println( "</body>"); 
   		out.println( "</html>");  
     }catch(Exception e){
     	e.printStackTrace();
     	Msg=Msg2;
     	out.println( "<html>"); 
    	out.println( "<head>"); 
    	out.println( "<title>error</title>"); 
   		out.println( "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"); 
   		out.println( "</head>"); 
    	out.println( "<body>"); 
    	out.println( "<script type=\"text/javascript\">alert(\""+Msg+"\");history.back();</script>"); 
   	 	out.println( "</body>"); 
   		out.println( "</html>"); 
     } 
   } catch(Exception e){ 
   e.printStackTrace();
  		 Msg=Msg1;
     	out.println( "<html>"); 
    	out.println( "<head>"); 
    	out.println( "<title>message</title>"); 
   		out.println( "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">"); 
   		out.println( "</head>"); 
    	out.println( "<body>"); 
    	out.println( "<script type=\"text/javascript\">alert(\""+Msg+"\");history.back();</script>"); 
   	 	out.println( "</body>"); 
   		out.println( "</html>"); 
   }		
}catch(Exception e){
		e.printStackTrace();
}

    %>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.coffice.util.*" %>
<%@ page import="java.io.File" %>
<%@ page import="com.coffice.attachment.*" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="java.text.SimpleDateFormat" %>
<% 
try{
	String mk_dm = request.getParameter("mk_dm");
	String mk_mc= request.getParameter("mk_mc");
	String mk_dir = request.getParameter("mk_dir"); 
	String mk_maxSize = request.getParameter("mk_maxSize");
	String fjId = request.getParameter("fjid");
	String yhid = (String)request.getAttribute("yhid");
	String date="";//日期
	SimpleDateFormat gs = new SimpleDateFormat("yyyyMMdd");
	Date now = new Date();
	date = gs.format(now);
	String directory = mk_dir+"/"+date+"/";
	UploadFile uploadfile= new UploadFile(yhid);
	uploadfile.vali(directory);
	UploadFileRenamePolicy frp=new UploadFileRenamePolicy();
	int maxSize = Integer.parseInt(mk_maxSize)*1024*1024;
	MultipartRequest multi = new MultipartRequest(request, directory, maxSize,"utf-8",frp);
	Enumeration files = multi.getFileNames();
	String guidFJ = "";
	String wjmc = "";
	String wjDir = "";
	int wjSize = 0;
	String fileName ="";
	while (files.hasMoreElements()){
		String  name = (String)files.nextElement();
		File f = multi.getFile(name);
		if(f!=null){
			fileName = multi.getFilesystemName(name);
			guidFJ=fileName.substring(0,22);
			wjmc=multi.getOriginalFileName(name);
			wjDir = directory+fileName;
			wjSize=uploadfile.getFileSize(wjDir);
			uploadfile.saveFjInfo(guidFJ,mk_dm,fjId,wjmc,"/"+date+"/"+fileName,wjSize);
		}
	}
	response.getWriter().print("/"+date+"/"+fileName);
}catch(Exception e){
	e.printStackTrace();
}
%>
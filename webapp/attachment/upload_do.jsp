<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/js.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.coffice.util.SysPara"%>
<%@page import="java.io.*,java.util.*,com.coffice.base.attachment.*"%>
<%@ page import="com.coffice.util.RequestUtil"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="com.coffice.util.SmartPrinter"%>
<%
request.setCharacterEncoding("UTF-8");
response.setContentType("text/html; charset=UTF-8");

String zdylj = "";//自定义路径
String zdylj_att="";//保存到数据库中的路径
String att_mk_dm=request.getParameter("mk_dm")!=null?(String)request.getParameter("mk_dm"):"";//模块代码
String zid=request.getParameter("zid")!=null?(String)request.getParameter("zid"):"0";//组ID
//自定义文件路径
String att_wjlj=request.getParameter("att_wjlj")!=null?(String)request.getParameter("att_wjlj"):"";
if(att_wjlj!=null&!"".equals(att_wjlj)) {
	zdylj=att_wjlj;
	zdylj_att=new String(zdylj.getBytes("UTF-8"),"GBK");
	att_wjlj=new String(zdylj.getBytes("UTF-8"),"GBK")+"-flag"; 
}

String slt=request.getParameter("slt")!=null?(String)request.getParameter("slt"):"0";//缩略图ID,1表示为缩略图

String zid_Token=request.getParameter("zid_Token")!=null?(String)request.getParameter("zid_Token"):"0";//组ID令牌
String selname=request.getParameter("selname")!=null?(String)request.getParameter("selname"):"";
String att_limitname=request.getParameter("att_limitname")!=null?(String)request.getParameter("att_limitname"):"";
String yhid = (String)request.getParameter("yhid");

String slt_flag=request.getParameter("slt_flag")!=null?(String)request.getParameter("slt_flag"):"0";//缩略图标志，1表示缩略图

/*System.out.println("==================================");
System.out.println(request.getQueryString());
System.out.println(yhid);*/

String att_size=SysPara.getValue(att_mk_dm+"_size");

//控制缩略图大小
int slt_size=1*1024*1024;
if("1".equals(slt_flag)) {
	try {
		slt_size=Integer.parseInt(SysPara.getValue(att_mk_dm+"_slt_size"))*1024*1024;
	} catch(Exception e) {
		slt_size=1*1024*1024;
	}
}

String  date="";
SimpleDateFormat gs = new SimpleDateFormat("yyyyMMdd");
Date now = new Date();
date = gs.format(now);

String wjmc="",wjlj="",ext="",guid="",cs="";
String[] wjljTemp=null;
int wjkj=0;
FileFuns fileFuns=new FileFuns();

try{
	//com.jspsmart.upload.File file = su.getFiles().getFile(0);
	//String con=file.getContentType();
	//wjmc=file.getFileName();
	//wjkj=file.getSize();
	//ext=file.getFileExt();
	Attachment att = new Attachment(yhid); 
	
	//获取配置参数,文件类型,文件大小
	String att_lj=SysPara.getValue(att_mk_dm+"_lj");
	if(att_lj.indexOf("{") > 0){
		cs = att_lj.substring(att_lj.indexOf("{")+1,att_lj.indexOf("}"));
		cs = (String)Cache.getUserInfo(yhid,cs);
		att_lj = att_lj.substring(0,att_lj.indexOf("{")-1);
	}
	if(cs!=null&&!"".equals(cs)){
		att_lj = att_lj + "/" +cs;
	}
	if(att_wjlj!=null&&!"".equals(att_wjlj)) {
		att_lj=att_lj+"/"+att_wjlj;
	}
	////建立文件夹
	//String slt_lj = request.getRealPath("/")+"publishupload";
	String slt_lj = "";
	try {
		slt_lj = SysPara.getValue(att_mk_dm+"_slt_lj");
		if(slt_lj!=null&&"".equals(slt_lj)) {
			slt_lj="D:/oainfo/publish";
		}
	} catch(Exception e) {
		slt_lj="D:/oainfo/publish";
	}
	//String slt_lj = "D:/oainfo/publish/sltupload/";
	if("1".equals(slt_flag)) {//缩略图
		//att.dirReday(slt_lj); 
		att.dirReday(slt_lj,date);
	} else {
		att.dirReday(att_lj,date); 
	}
	
	if(att_wjlj!=null&&!"".equals(att_wjlj)) {
		att_lj=att_lj.substring(0,att_lj.indexOf("-flag"));
	}
	//重命名文件
	//wjljTemp=att.nameReday(wjmc,date,ext); 
	//wjlj=wjljTemp[0];
	//guid=wjljTemp[1];   
	//并上传
	//file.saveAs();   
	//文件上传后，保存在saveDirectory
	String saveDirectory ="";
	if(att_wjlj!=null&&!"".equals(att_wjlj)) {
		saveDirectory =att_lj+"/";
	} else {
		saveDirectory =att_lj+"/"+date+"/";
	}
	
	//
	int maxPostSize =Integer.parseInt(att_size)*1024*1024;
	//response的编码为"utf-8",同时采用相应的命名策略（我用了自己的实现方法）冲突解决策略,实现上传
	RandomFileRenamePolicy rfrp=null;
	RandomFileRenamePolicySlt rfrp_slt=null; 
	if("1".equals(slt_flag)) {//如果是缩略图,则处理中文名称问题
		rfrp_slt=new RandomFileRenamePolicySlt();
	} else {
		rfrp=new RandomFileRenamePolicy();
	}
	
	String contextPath = request.getContextPath();
	String sltDirectory = slt_lj+"/"+date+"/";//缩略图保存路径
	MultipartRequest multi = null;
	
	if("1".equals(slt_flag)) {//如果是缩略图,则上传到项目路径
		multi = new MultipartRequest(request, sltDirectory, slt_size,"utf-8",rfrp_slt);
	} else {
		multi = new MultipartRequest(request, saveDirectory, maxPostSize,"utf-8",rfrp);
	}
	
	//输出反馈信息
	Enumeration files = multi.getFileNames();
	while (files.hasMoreElements()){
	String name = (String)files.nextElement();
	File f = multi.getFile(name);
	
	if(f!=null){
	String fileName = multi.getFilesystemName(name);
	//外包呼叫中心模块传真文件格式转换成tif
	if("fax".equals(att_mk_dm)){
		String tifSource = saveDirectory+fileName;
		SmartPrinter smartP = new SmartPrinter();
		smartP.convertFile(tifSource);
	}

	
	//保存数据库
	//mk_dm 模块代码//zid附件组唯一ID//wjmc文件名称/wjlj文件路径//wjkj文件空间
	wjmc=multi.getOriginalFileName(name);
	if("1".equals(slt_flag)) {
		wjlj="/"+date+"/"+fileName;	
	} else {
		if(cs!=null&&!"".equals(cs)){
			if(!"".equals(zdylj_att)) {
				wjlj="/"+cs+"/"+zdylj_att+"/"+fileName;	
			} else {
				wjlj="/"+cs+"/"+date+"/"+fileName;	
			}
		}else{
			if(!"".equals(zdylj_att)) {
				wjlj="/"+zdylj_att+"/"+fileName;	
			} else {
				wjlj="/"+date+"/"+fileName;	
			}
		}
	}
	
	guid=fileName.substring(0,22);
	String lastFileName="";
	if("1".equals(slt_flag)) {
		//lastFileName= slt_lj+"/" + fileName;
		lastFileName= sltDirectory+"/" + fileName;
	} else {
		lastFileName= saveDirectory + fileName;
	}
	wjkj=fileFuns.queryFileSize(lastFileName);
	
	if("1".equals(slt_flag)) {
		slt=att.insertAttachment(guid,att_mk_dm,slt,wjmc,wjlj,wjkj,slt_flag);
	} else {
		zid=att.insertAttachment(guid,att_mk_dm,zid,wjmc,wjlj,wjkj,slt_flag);
	}
	out.println("<script>");
	out.println("alert('成功：文件上传成功！');");  
	out.println("</script>");  
	}
}                   
}catch(Exception eu){
	eu.printStackTrace();
   				out.println("<script>");
		        out.println("alert('错误：文件超出大小限制！');");
		        out.println("</script>"); 
}
    %>
<head>
 <title>协同办公管理系统</title>
<base   target="_self">
</head>
<body  >
<a id="reload" href='${ctx}/attachment/upload.jsp?slt_flag=<%=slt_flag %>&mk_dm=<%=att_mk_dm%>&slt=<%=slt%>&zid=<%=zid%>&zid_Token=<%=zid_Token%>&selname=<%=selname%>&att_wjlj=<%=zdylj%>&att_limitname=<%=att_limitname%>' style="display:none"></a>
</body>
<script>
reload.click();
</script>


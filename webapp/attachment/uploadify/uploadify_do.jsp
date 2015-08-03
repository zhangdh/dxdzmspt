<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.coffice.util.cache.Cache"%>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="javax.servlet.ServletException" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate" %>
<%@ page import="com.coffice.base.attachment.Attachment" %>
<%@ page import="com.coffice.base.attachment.FileFuns" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.coffice.base.attachment.uploadify.*" %>

<%
String zid = "";
 //接收页面传来参数
 //根据逗号分隔
 //如果guid为空,则为新建,要创建zid
String yhid = (String)request.getAttribute("yhid");
String random_num = request.getParameter("random_num");
String guid = random_num.substring(random_num.indexOf(",")+1, random_num.indexOf(":"));
String randomNum = random_num.substring(0, random_num.indexOf(","));
String fjid = random_num.substring(random_num.indexOf(":")+1, random_num.indexOf("~"));//附件id
String mk_dm = random_num.substring(random_num.indexOf("~")+1,random_num.indexOf("!"));
String att_lj = random_num.substring(random_num.indexOf("!")+1,random_num.indexOf("^"));
String att_size = random_num.substring(random_num.indexOf("^")+1,random_num.indexOf("<>"));
String selname = random_num.substring(random_num.indexOf("<>")+2,random_num.length());
if("imail".equals(mk_dm)||"form".equals(mk_dm)) {
	guid="teshuchuli";
}
try {
response.setCharacterEncoding("utf-8");
	
	String guidFJ="";//附件表中的guid
	String wjmc="";//文件名称
	String date="";//日期
	String wjlj="";//文件路径
	String lastFileName="";
	int wjkj=0;
	SimpleDateFormat gs = new SimpleDateFormat("yyyyMMdd");
	Date now = new Date();
	date = gs.format(now);
	Attachment att = new Attachment(yhid);
	att.dirReday(att_lj,date);
	
	String saveDirectory ="";

	if(att_lj.contains("-flag")) {
		//自定义上传路径的情况
		//att_lj=request.getSession().getAttribute("att_lj").toString();
		att_lj=(String)Cache.getUserInfo(yhid,"att_lj");
		String att_lj_pd = att_lj;
		att_lj_pd=att_lj_pd.substring(0,att_lj_pd.indexOf("-flag"));
		saveDirectory =att_lj_pd+"/";
	} else {
		saveDirectory =att_lj+"/"+date+"/";
	}
	
	int maxPostSize =Integer.parseInt(att_size)*1024*1024;
	//int maxPostSize =10000*1024*1024;
	UploadifyRandomFileRenamePolicy rfrp=new UploadifyRandomFileRenamePolicy();
	MultipartRequest multi = new MultipartRequest(request, saveDirectory, maxPostSize,"utf-8",rfrp);
	FileFuns fileFuns=new FileFuns();
	
	//反馈信息
	Enumeration files = multi.getFileNames();
	while (files.hasMoreElements()){
		String name = (String)files.nextElement();
		File f = multi.getFile(name);
		if(f!=null){
			String fileName = multi.getFilesystemName(name);
			guidFJ=fileName.substring(0,22);
			wjmc=multi.getOriginalFileName(name);
			System.out.println(fileName);
			if(att_lj.contains("-flag")) {
				String temp_lj = att_lj.substring(att_lj.lastIndexOf("/")+1,att_lj.length()-5);
				if(att_lj.indexOf("kb")>-1){
					temp_lj = att_lj.split("tree/")[1].substring(0,att_lj.split("tree/")[1].indexOf("-flag"));
				}
				wjlj="/"+temp_lj+"/"+fileName;
			} else {
				wjlj="/"+date+"/"+fileName;
			}
			lastFileName= saveDirectory+"/" + fileName;
			wjkj=fileFuns.queryFileSize(lastFileName);
			
			if("_".equals(guid)) {//新建
				Map randomMap = (Map)request.getSession().getAttribute(yhid+"_uploadify");
				//System.out.println(randomMap);
				if(randomMap==null) {
					zid=att.insertUploadifyAttachment(guidFJ,mk_dm,zid,wjmc,wjlj,wjkj);
					Map uploadifyMap = new HashMap();
					uploadifyMap.put(randomNum, zid);
					request.getSession().setAttribute(yhid+"_uploadify", uploadifyMap);
				} else {
					if(randomMap.containsKey(randomNum)) {
						String randomZid = (String)randomMap.get(randomNum);
						zid=att.insertUploadifyAttachment(guidFJ,mk_dm,randomZid,wjmc,wjlj,wjkj);
					} else {
						randomMap.clear();
						zid=att.insertUploadifyAttachment(guidFJ,mk_dm,"",wjmc,wjlj,wjkj);
						randomMap.put(randomNum, zid);
					}
				}
			} else {//修改
				if("_".equals(fjid)&&!"_".equals(guid)) {
					Map randomMap = (Map)request.getSession().getAttribute(yhid+"_uploadify");
					if(randomMap==null) {
						zid=att.insertUploadifyAttachment(guidFJ,mk_dm,zid,wjmc,wjlj,wjkj);
						Map uploadifyMap = new HashMap();
						uploadifyMap.put(randomNum, zid);
						request.getSession().setAttribute(yhid+"_uploadify", uploadifyMap);
					} else {
						if(randomMap.containsKey(randomNum)) {
							String randomZid = (String)randomMap.get(randomNum);
							zid=att.insertUploadifyAttachment(guidFJ,mk_dm,randomZid,wjmc,wjlj,wjkj);
						} else {
							randomMap.clear();
							zid=att.insertUploadifyAttachment(guidFJ,mk_dm,"",wjmc,wjlj,wjkj);
							randomMap.put(randomNum, zid);
						}
					}
				} else if(fjid.contains("_")) {
					zid=att.insertUploadifyAttachment(guidFJ,mk_dm,fjid.substring(0, fjid.indexOf("_")),wjmc,wjlj,wjkj);
				} else {
					zid=att.insertUploadifyAttachment(guidFJ,mk_dm,fjid,wjmc,wjlj,wjkj);
				}
			}
		}
	}
	response.getWriter().print(zid+","+mk_dm+";"+selname);
} catch(Exception e) {
	e.printStackTrace();
}
%>
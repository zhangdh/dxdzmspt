package com.coffice.diffusion;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.attachment.UploadFile;
import com.coffice.attachment.UploadFileRenamePolicy;
import com.coffice.util.Log;
import com.coffice.util.RequestUtil;
import com.coffice.util.SysPara;
import com.oreilly.servlet.MultipartRequest;



@Controller
@RequestMapping("/diffusion.coffice")
public class DiffusionController {
	
	@RequestMapping(params = "method=manageConfig")
	public ModelAndView manageConfig(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.manageConfig());
	}
	
	@RequestMapping(params = "method=saveManager")
	public ModelAndView saveManager(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.saveManager());
	}
	@RequestMapping(params = "method=delManager")
	public ModelAndView delManager(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.delManager());
	}
	@RequestMapping(params = "method=showManager")
	public ModelAndView showManager(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.showManager());
	}
	@RequestMapping(params = "method=queryLb")
	public ModelAndView queryLb(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.queryLb());
	}
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.save());
	}
	@RequestMapping(params = "method=send")
	public ModelAndView send(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.send());
	}
	@RequestMapping(params = "method=save1")
	public ModelAndView save1(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.save1());
	}
	@RequestMapping(params = "method=send1")
	public ModelAndView send1(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.send1());
	}
	@RequestMapping(params = "method=del")
	public ModelAndView del(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.del());
	}
	@RequestMapping(params = "method=queryFs")
	public ModelAndView queryFs(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.queryFs());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView show(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.show());
	}
	@RequestMapping(params = "method=sendDiff")
	public ModelAndView sendDiff(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.sendDiff());
	}
	
	@RequestMapping(params = "method=delJs")
	public ModelAndView delJs(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.delJs());
	}
	@RequestMapping(params = "method=queryJs")
	public ModelAndView queryJs(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.queryJs());
	}
	@RequestMapping(params = "method=showJs")
	public ModelAndView showJs(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.showJs());
	}
	@RequestMapping(params = "method=listDesk")
	public ModelAndView listDesk(HttpServletRequest request) throws Exception {
		Diffusion diffusion = new Diffusion(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", diffusion.listDesk());
	}
	@RequestMapping(params = "method=uploadImage")
	public void uploadImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Map reMap = new HashMap();
		PrintWriter out = response.getWriter();

 
		try{
			String yhid = (String)request.getAttribute("yhid");
			String date="";//日期
			SimpleDateFormat gs = new SimpleDateFormat("yyyyMMdd");
			Date now = new Date();
			date = gs.format(now);
			String directory = SysPara.getValue("kindeditor_lj")+"/"+date+"/";
			UploadFile uploadfile= new UploadFile(yhid);
			uploadfile.vali(directory);
			UploadFileRenamePolicy frp=new UploadFileRenamePolicy();
			MultipartRequest multi = new MultipartRequest(request, directory, 100*1024*1024,"utf-8",frp);
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
					uploadfile.saveFjInfo(guidFJ,"105","",wjmc,"/"+date+"/"+fileName,wjSize);
				}
			}
			String contextPath =request.getRequestURL().toString();
			contextPath = contextPath.split("/")[0]+"//"+contextPath.split("/")[2]+directory.split(":")[1];
			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url", contextPath+fileName);
			out.println(obj.toJSONString());
		}catch(Exception e){
			Log.writeLog("error","信息发布上传图片时异常:"+e.toString());
			e.printStackTrace();
			out.println("{'error':1,'message':'信息发布上传图片时异常'}" );
		}finally{
			out.close();
		}
		 
	}
	@RequestMapping(params = "method=uploadManagerImage")
	public void uploadManagerImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String rootPath = request.getServletPath()+ "attached/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  = request.getContextPath() + "/attached/";
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

		String dirName = request.getParameter("dir");
		if (dirName != null) {
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}
		//根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		//排序形式，name or size or type
		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

		//不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			out.println("Access is not allowed.");
			return;
		}
		//最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			out.println("Parameter is not valid.");
			return;
		}
		//目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			out.println("Directory does not exist.");
			return;
		}

		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		JSONObject result = new JSONObject();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);

		response.setContentType("application/json; charset=UTF-8");
		out.println(result.toJSONString());
		out.close();
		 
	}
}

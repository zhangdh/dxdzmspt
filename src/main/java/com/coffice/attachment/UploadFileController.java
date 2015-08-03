package com.coffice.attachment;
import javax.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.*;

@Controller
@RequestMapping("/uploadfile.coffice")
public class UploadFileController extends BaseUtil{
	//查询query
	@RequestMapping(params = "method=getFileInfo")
	public ModelAndView getFileInfo(HttpServletRequest request) {
		JspJsonData jjd  = new JspJsonData();
		try{
			String mk_dm = request.getParameter("mk_dm");
			String mk_mc = request.getParameter("mk_mc");
			String fjid = Guid.get();
			String mk_dir = SysPara.getValue(mk_mc+"_dir");
			String mk_maxSize = SysPara.getValue(mk_mc+"_maxSize");
			jjd.setExtend("fjid", fjid);
			jjd.setExtend("mk_dir", mk_dir);
			jjd.setExtend("mk_maxSize", mk_maxSize);
		}catch(Exception e){
			
		}
		return new ModelAndView("jsonView",jjd.getData());
	}
	
	@RequestMapping(params = "method=delFj")
	public ModelAndView delFj(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.delFj());
	}
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.save());
	}
	@RequestMapping(params = "method=send")
	public ModelAndView send(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.send());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView show(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.send());
	}
	@RequestMapping(params = "method=getFiles")
	public ModelAndView getFiles(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.getFiles());
	}
	@RequestMapping(params = "method=getFileList")
	public ModelAndView getFileList(HttpServletRequest request) {
		UploadFile uploadfile = new UploadFile(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",uploadfile.getFileList());
	}
}

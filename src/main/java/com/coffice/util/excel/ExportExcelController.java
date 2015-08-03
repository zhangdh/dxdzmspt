package com.coffice.util.excel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;
import com.coffice.util.cache.Cache;


@Controller
@RequestMapping("/exportexcel/default.do")
public class ExportExcelController {
	@RequestMapping(params = "method=exportExcelIsok")
	public ModelAndView is(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 Map map = RequestUtil.getMap(request);
		 String key = (String)request.getParameter("key");
		// System.out.println("接受到的key:"+key+" 用户id:"+(String)map.get("yhid"));
		 String value=(String)Cache.getUserInfo((String)map.get("yhid"),key);
		 JspJsonData jjd = new JspJsonData();
		 if(value==null){
			 jjd.setExtend("isok", "0");
			 return new ModelAndView("jsonView",jjd.getData());
		 }else{
			 jjd.setExtend("isok", "1");
			 jjd.setExtend("file", value);
			 return new ModelAndView("jsonView",jjd.getData());
		 }
	}	
	@RequestMapping(params = "method=showFile")
	public ModelAndView showFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map map = RequestUtil.getMap(request);
		System.out.println("url: http://localhost:"+request.getLocalPort()+(String)map.get("file"));
		response.sendRedirect((String)map.get("file"));
		//request.getRequestDispatcher("http://localhost:"+request.getLocalPort()+(String)map.get("file")).
		return null;//new ModelAndView("http://localhost:"+request.getLocalPort()+(String)map.get("file"), map);
	}		
	

}

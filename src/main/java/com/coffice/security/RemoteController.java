package com.coffice.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/remote/file/default.do")
public class RemoteController extends BaseUtil {
	//初始化
	@RequestMapping(params = "method=init")
	public ModelAndView init(HttpServletRequest request) {
		Remote remote = new Remote(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", remote.init());
	}
	//级联调用名称
	@RequestMapping(params = "method=listMc")
	public ModelAndView listMc(HttpServletRequest request) {
		Remote remote = new Remote(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", remote.listMc());
	}
	//加密、签名
	@RequestMapping(params = "method=testSign")
	public ModelAndView testSign(HttpServletRequest request) {
		Enumeration enu = request.getParameterNames();
		StringBuffer queryBuff = new StringBuffer();
		while (enu.hasMoreElements()) {
			String paramName = (String) enu.nextElement();
			String[] values = request.getParameterValues(paramName);
			String value = "";
			for (int i = 0; i < values.length; i++) {
				value = value + (value.equals("") ? "" : "~") + values[i];
			}
			if(!paramName.equals("apikey") && !paramName.equals("api_key") && !paramName.equals("method")){
				queryBuff.append(paramName).append("=").append(value).append("&");
			}
		}
		String querystr = queryBuff.toString();
		if(querystr.length()>0){
			querystr = querystr.substring(0,querystr.length()-1);
		}
		Remote remote = new Remote(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", remote.testSign(querystr));
	}

}

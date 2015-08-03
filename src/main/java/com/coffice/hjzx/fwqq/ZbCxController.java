package com.coffice.hjzx.fwqq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/zbcx.coffice")
public class ZbCxController extends BaseUtil {	
	@RequestMapping(params = "method=queryZb")
	public ModelAndView queryZb(HttpServletRequest request) {
		Zbcx zbcx = new Zbcx(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", zbcx.queryZb());
	}
	
	@RequestMapping(params = "method=delZb")
	public ModelAndView delZb(HttpServletRequest request) {
		Zbcx zbcx = new Zbcx(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", zbcx.delZb());
	}
	@RequestMapping(params = "method=exportXls")
	public ModelAndView exportXls(HttpServletResponse response,HttpServletRequest request) {
		Zbcx zbcx = new Zbcx(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", zbcx.exportXls(response,request));
	}
}

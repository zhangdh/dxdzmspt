package com.coffice.hjzx.ldps;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.hjzx.fwqq.Zbcx;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/ldps.coffice")
public class LdpsController {
	@RequestMapping(params = "method=queryZb")
	public ModelAndView queryZb(HttpServletRequest request) {
		Ldps ldps = new Ldps(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ldps.queryZb());
	}
	@RequestMapping(params = "method=queryCsj")
	public ModelAndView queryCsj(HttpServletRequest request) {
		Ldps ldps = new Ldps(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ldps.queryCsj());
	}
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
		Ldps ldps = new Ldps(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ldps.save());
	}
	@RequestMapping(params = "method=getLd")
	public ModelAndView getLd(HttpServletRequest request) {
		Ldps ldps = new Ldps(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ldps.getLd());
	}
	@RequestMapping(params = "method=saveLd")
	public ModelAndView saveLd(HttpServletRequest request) {
		Ldps ldps = new Ldps(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ldps.saveLd());
	}
}

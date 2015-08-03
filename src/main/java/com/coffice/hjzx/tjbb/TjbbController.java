package com.coffice.hjzx.tjbb;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.hjzx.fwqq.Fwqq;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/tjbb.coffice")
public class TjbbController {
	@RequestMapping(params = "method=initXzBlqk")
	public ModelAndView initXzBlqk(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.initXzBlqk());
	}
	@RequestMapping(params = "method=xzBlqk")
	public ModelAndView xzBlqk(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.xzBlqk());
	}
	
	@RequestMapping(params = "method=blqk")
	public ModelAndView blqk(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.blqk());
	}
	
	@RequestMapping(params = "method=ldsj")
	public ModelAndView ldsj(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.ldsj());
	}
	@RequestMapping(params = "method=ldsjChart")
	public ModelAndView ldsjChart(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.ldsjChart());
	}
	@RequestMapping(params = "method=compare")
	public ModelAndView compare(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.compare());
	}
	@RequestMapping(params = "method=compareChart")
	public ModelAndView compareChart(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.compareChart());
	}
	@RequestMapping(params = "method=sltj")
	public ModelAndView sltj(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.sltj());
	}
	@RequestMapping(params = "method=zhtj")
	public ModelAndView zhtj(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.zhtj());
	}
	@RequestMapping(params = "method=zhtjChart")
	public ModelAndView zhtjChart(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.zhtjChart());
	}
	@RequestMapping(params = "method=queryDel")
	public ModelAndView queryDel(HttpServletRequest request) {
		Tjbb tjbb = new Tjbb(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.queryDel());
	}
}

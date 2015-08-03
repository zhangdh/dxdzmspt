package com.coffice.hjzx.tjbb;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/khpj.coffice")
public class KhpjController {
	//考核数据县区
	@RequestMapping(params = "method=queryXQ")
	public ModelAndView queryXQ(HttpServletRequest request) {
		Khpj khpj = new Khpj(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", khpj.queryXQ());
	}
	//考核数据乡镇
	@RequestMapping(params = "method=queryXZ")
	public ModelAndView queryXZ(HttpServletRequest request) {
		Khpj khpj = new Khpj(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", khpj.queryXZ());
	}
	//考核数据直属部门
	@RequestMapping(params = "method=queryZS")
	public ModelAndView querySZ(HttpServletRequest request) {
		Khpj khpj = new Khpj(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", khpj.queryZS());
	}

}

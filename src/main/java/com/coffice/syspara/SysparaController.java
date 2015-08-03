package com.coffice.syspara;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/syspara.coffice")
public class SysparaController extends BaseUtil {	

	//查询query
	@RequestMapping(params = "method=query")
	public ModelAndView query(HttpServletRequest request) {
		Syspara syspara = new Syspara(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", syspara.query());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Syspara syspara = new Syspara(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", syspara.show());
	}	
	//	修改
	@RequestMapping(params = "method=modi")
	public ModelAndView modi(HttpServletRequest request) {
		Map map =RequestUtil.getMap(request);
		Syspara syspara = new Syspara(map);
		return new ModelAndView("jsonView", syspara.modi());
	}

}

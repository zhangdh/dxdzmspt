package com.coffice.msg;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.RequestUtil;
@Controller
@RequestMapping("/msg.coffice")
public class MesgController {
	/**
	 * 显示留言
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=showLy")
	public ModelAndView showLy(HttpServletRequest request) {
		Mesg msg=new Mesg(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", msg.showLy());
	}
	
	@RequestMapping(params = "method=queryLy")
	public ModelAndView queryLy(HttpServletRequest request) {
		Mesg msg=new Mesg(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", msg.queryLy());
	}
	@RequestMapping(params = "method=saveLy")
	public ModelAndView saveLy(HttpServletRequest request) {
		Mesg msg=new Mesg(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", msg.saveLy());
	}
	@RequestMapping(params = "method=showMx")
	public ModelAndView showMx(HttpServletRequest request) {
		Mesg msg=new Mesg(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", msg.showMx());
	}
}

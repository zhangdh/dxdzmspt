package com.coffice.menu;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/menu.coffice")
public class MenuController {	
	@RequestMapping(params = "method=GetMenu")
	public ModelAndView GetMenu(HttpServletRequest request) {
		Menu menu = new Menu(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", menu.GetMenu());
	}
}

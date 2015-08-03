package com.coffice.workflow.util;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.workflow.use.WKUse;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/dfmethod.coffice"})
public class DoFlowController
{
	@RequestMapping(params = "method=doFlow")
	public ModelAndView doFlow(HttpServletRequest request) {
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
		DoFlow doflow=new DoFlow(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", doflow.doFlow(wkuse));
	}
 
}
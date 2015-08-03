package com.coffice.authority;


import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.RequestUtil;



@Controller
@RequestMapping({"/authority.coffice"})
public class AuthorityController {
	
	  @RequestMapping(params={"method=getJsTree"})
	  public ModelAndView getJsTree(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.getJsTree());
	  }
	  @RequestMapping(params={"method=getYhTree"})
	  public ModelAndView getYhTree(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.getYhTree());
	  }
	  @RequestMapping(params={"method=saveJsQx"})
	  public ModelAndView saveJsQx(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.saveJsQx());
	  }
	  @RequestMapping(params={"method=saveYhQx"})
	  public ModelAndView saveYhQx(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.saveYhQx());
	  }
	  @RequestMapping(params={"method=getYhQx"})
	  public ModelAndView getYhQx(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.getYhQx());
	  }
	  @RequestMapping(params={"method=getJsQx"})
	  public ModelAndView getJsQx(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.getJsQx());
	  }
	  @RequestMapping(params={"method=getQxTree"})
	  public ModelAndView getQxTree(HttpServletRequest request) {
		 Authority auth = new Authority(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", auth.getQxTree());
	  }
}

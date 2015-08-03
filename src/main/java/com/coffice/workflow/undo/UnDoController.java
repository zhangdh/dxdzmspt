package com.coffice.workflow.undo;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/undo.coffice"})
public class UnDoController {
	 
	  @RequestMapping(params={"method=init"})
	  public ModelAndView init(HttpServletRequest request)
	  {
		UnDo undo = new UnDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", undo.init());
	  }
	  @RequestMapping(params={"method=query"})
	  public ModelAndView query(HttpServletRequest request)
	  {
		UnDo undo = new UnDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", undo.query());
	  }
	  
	  @RequestMapping(params={"method=listDesk"})
	  public ModelAndView listDesk(HttpServletRequest request)
	  {
		UnDo undo = new UnDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", undo.listDesk());
	  }
	  
}

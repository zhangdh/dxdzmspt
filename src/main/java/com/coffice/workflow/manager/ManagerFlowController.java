package com.coffice.workflow.manager;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/managerflow.coffice"})
public class ManagerFlowController {
	 
	  @RequestMapping(params={"method=listFlow"})
	  public ModelAndView listFlow(HttpServletRequest request)
	  {
		ManagerFlow managerflow = new ManagerFlow(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", managerflow.listFlow());
	  }
	  @RequestMapping(params={"method=stopFlow"})
	  public ModelAndView stopFlow(HttpServletRequest request)
	  {
		ManagerFlow managerflow = new ManagerFlow(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", managerflow.stopFlow());
	  }
	  @RequestMapping(params={"method=startFlow"})
	  public ModelAndView startFlow(HttpServletRequest request)
	  {
		ManagerFlow managerflow = new ManagerFlow(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", managerflow.startFlow());
	  }
	  @RequestMapping(params={"method=delFlow"})
	  public ModelAndView delFlow(HttpServletRequest request)
	  {
		ManagerFlow managerflow = new ManagerFlow(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", managerflow.delFlow());
	  }
}

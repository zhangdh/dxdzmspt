package com.coffice.workflow.use;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/wfuse.coffice"})
public class WKUseController
{
	@RequestMapping(params={"method=showUserList"})
	public ModelAndView showUserList(HttpServletRequest request)
	  {
	    WKUse wkuse = new WKUse(RequestUtil.getMap(request));
	    Map map = wkuse.showUserList();
	    JspJsonData	jjd = new JspJsonData();
	    jjd.setForm(map);
	    return new ModelAndView("jsonView", jjd.getData());
	     
	  }
	  @RequestMapping(params={"method=doWf"})
	  public ModelAndView doWfInstanceAction(HttpServletRequest request)
	  {
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", wkuse.doWfInstanceAction());
	  }
	  @RequestMapping(params={"method=queryHis"})
	  public ModelAndView queryHis(HttpServletRequest request)
	  {
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", wkuse.queryHis());
	  }
	  @RequestMapping(params={"method=queryTimeLine"})
	  public ModelAndView queryTimeLine(HttpServletRequest request)
	  {
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", wkuse.queryTimeLine());
	  }
}
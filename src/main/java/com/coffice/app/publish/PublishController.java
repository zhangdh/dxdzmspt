package com.coffice.app.publish;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.app.gd.Gd;
import com.coffice.form.service.FormService;
import com.coffice.util.Db;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;
import com.coffice.workflow.use.WKUse;
import com.coffice.workflow.util.DoFlow;
import com.coffice.workflow.util.WorkFlowMethod;
@Controller
@RequestMapping({"/app/publish.app"})
public class PublishController {
	
	  @RequestMapping(params={"method=queryInfo"})
	  public ModelAndView queryInfo(HttpServletRequest request){
		Publish publish = new Publish(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", publish.queryInfo());
	  }
	  
	  @RequestMapping(params={"method=showInfo"})
	  public ModelAndView showInfo(HttpServletRequest request){
		Publish publish = new Publish(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", publish.showInfo());
	  }
}

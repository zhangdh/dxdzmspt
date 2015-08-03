package com.coffice.directory.organization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;



@Controller
@RequestMapping({"/setting.coffice"})
public class SettingController {
	
	  @Transactional
	  @RequestMapping(params={"method=modiMM"})
	  public ModelAndView getOrgTree(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Setting set = new Setting(map);
	    return new ModelAndView("jsonView", set.modiMM());
	  }
	  
	  @RequestMapping(params={"method=modiInfo"})
	  public ModelAndView getOrgList(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Setting set = new Setting(map);
	    return new ModelAndView("jsonView", set.modiInfo());
	  }
	  @RequestMapping(params={"method=getYhInfo"})
	  public ModelAndView getYhInfo(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Setting set = new Setting(map);
	    return new ModelAndView("jsonView", set.getYhInfo());
	  }
	
}

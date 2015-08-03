package com.coffice.directory.role;

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
@RequestMapping({"/directoryrole.coffice"})
public class DirectoryRoleController {
	
	 @Transactional
	  @RequestMapping(params={"method=saveRole"})
	  public ModelAndView saveRole(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    DirectoryRole role = new DirectoryRole();
	    return new ModelAndView("jsonView", role.saveRole(map));
	  }

	  @RequestMapping(params={"method=queryRole"})
	  public ModelAndView queryRole(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    DirectoryRole role = new DirectoryRole();

	    return new ModelAndView("jsonView", role.queryRole(map));
	  }

	  @RequestMapping(params={"method=showRoleInfo"})
	  public ModelAndView showRoleInfo(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    DirectoryRole role = new DirectoryRole();
	    return new ModelAndView("jsonView", role.showRoleInfo(map));
	  }
	  @Transactional
	  @RequestMapping(params={"method=deleteRole"})
	  public ModelAndView deleteRole(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    DirectoryRole role = new DirectoryRole();

	    return new ModelAndView("jsonView", role.deleteRole(map));
	  }
}

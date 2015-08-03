package com.coffice.directory.organization;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/organization.coffice"})
public class OrganizationController {
	
	  @Transactional
	  @RequestMapping(params={"method=getOrgTree"})
	  public ModelAndView getOrgTree(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.getOrgTree());
	  }
	  
	  @RequestMapping(params={"method=getOrgList"})
	  public ModelAndView getOrgList(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.getOrgList());
	  }
	  
	  @RequestMapping(params={"method=saveBm"})
	  public ModelAndView saveBm(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.saveBm());
	  }
	  @RequestMapping(params={"method=modiBm"})
	  public ModelAndView modiBm(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.modiBm());
	  }
	  
	  @RequestMapping(params={"method=saveGw"})
	  public ModelAndView saveGw(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.saveGw());
	  }
	  @RequestMapping(params={"method=modiGw"})
	  public ModelAndView modiGw(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.modiGw());
	  }
	  
	  @RequestMapping(params={"method=saveYh"})
	  public ModelAndView saveYh(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.saveYh());
	  }
	  
	  @RequestMapping(params={"method=modiYh"})
	  public ModelAndView modiYh(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.modiYh());
	  }
	  
	  @RequestMapping(params={"method=delOrg"})
	  public ModelAndView delOrg(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.delOrg());
	  }
	  
	  @RequestMapping(params={"method=selectRole"})
	  public ModelAndView selectRole(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.selectRole());
	  }
	  @RequestMapping(params={"method=showBm"})
	  public ModelAndView showBm(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.showBm());
	  }
	  @RequestMapping(params={"method=showGw"})
	  public ModelAndView showGw(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.showGw());
	  }
	  @RequestMapping(params={"method=showYh"})
	  public ModelAndView showYh(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.showYh());
	  }
	  @RequestMapping(params={"method=rePass"})
	  public ModelAndView rePass(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.rePass());
	  }
	  @RequestMapping(params={"method=exportOrg"})
	  public ModelAndView exportOrg(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
	    Organization org = new Organization(map);
	    return new ModelAndView("jsonView", org.exportOrg());
	  }
}

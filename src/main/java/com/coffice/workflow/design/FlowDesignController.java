package com.coffice.workflow.design;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.xio.xiorkflow.web.ProcessBusiBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/flowdesign.coffice"})
public class FlowDesignController
{
  @Transactional
  @RequestMapping(params={"method=saveProcess"})
  public ModelAndView saveProcess(HttpServletRequest request)
  {
    FlowDesign design = new FlowDesign(RequestUtil.getMap(request));
    return new ModelAndView("jsonView", design.saveProcess(request));
  }

  @RequestMapping(params={"method=showProcess"})
  public ModelAndView showProcess(HttpServletRequest request, HttpServletResponse response)
  {
    FlowDesign design = new FlowDesign(RequestUtil.getMap(request));
    return new ModelAndView("jsonView", design.showProcess());
  }

  @RequestMapping(params={"method=viewProcess"})
  public ModelAndView viewProcess(HttpServletRequest request, HttpServletResponse response)
  {
    FlowDesign design = new FlowDesign(RequestUtil.getMap(request));
    return new ModelAndView("jsonView", design.viewProcess());
  }

  @RequestMapping(params={"method=showWkAttr"})
  public ModelAndView showWkAttr(HttpServletRequest request)
  {
    FlowDesign design = new FlowDesign(RequestUtil.getMap(request));
    return new ModelAndView("/oswf/graphicdesigner/designConfig/workflow_attr.jsp", design.showWkAttr());
  }

  @RequestMapping(params={"method=showFunctionConfig"})
  public ModelAndView showFunctionConfig(HttpServletRequest request)
  {
    ProcessBusiBean processBusi = new ProcessBusiBean(RequestUtil.getMap(request));
    return new ModelAndView("jsonView", processBusi.showFunctionConfig());
  }
}
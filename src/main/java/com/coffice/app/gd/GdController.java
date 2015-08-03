package com.coffice.app.gd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.form.service.FormService;
import com.coffice.util.Db;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;
import com.coffice.workflow.use.WKUse;
import com.coffice.workflow.util.DoFlow;
import com.coffice.workflow.util.WorkFlowMethod;

@Controller
@RequestMapping({"/app/gd.app"})
public class GdController {
	  @RequestMapping(params={"method=queryUnDo"})
	  public ModelAndView queryUnDo(HttpServletRequest request){
		Gd gd = new Gd(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", gd.queryUnDo());
	  }
	  @RequestMapping(params={"method=queryToDo"})
	  public ModelAndView queryToDo(HttpServletRequest request){
		Gd gd = new Gd(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", gd.queryToDo());
	  }
	  @RequestMapping(params={"method=showToDoForm"})
	  public ModelAndView showToDoForm(HttpServletRequest request){
		Gd gd = new Gd(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", gd.showToDoForm());
	  }
	  
	  @RequestMapping(params={"method=showForm"})
	  public ModelAndView showForm(HttpServletRequest request){
		Gd gd = new Gd(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", gd.showForm());
	  }
	  @RequestMapping(params={"method=saveForm"})
	  public ModelAndView saveForm(HttpServletRequest request){
	    JspJsonData jjd = new JspJsonData();
	    String loginCode = "";
	    Map _map = RequestUtil.getMap(request);
	    loginCode = _map.get("yhid").toString();
	    try {
	      FormService formService = new FormService();
	      //String tableName = request.getParameter("tableName");
	      String tableName = "t_form1305";
	      Map authority = null;
	      String businessId = request.getParameter("enid");
	      String stepId = request.getParameter("stepid");
	      String undo_title = request.getParameter("undo_title");
	      if(!"".equals(undo_title)){
	    	  Db.getJtA().update("update t_oswf_work_item set formname='"+undo_title+"' where Entry_ID="+businessId);
	      }
	      WorkFlowMethod wfMethod = new WorkFlowMethod();
	      String  id = request.getParameter("id");

	      authority = wfMethod.getFormRole2(tableName, stepId);
	      formService.updateFormData1(tableName, _map, id, authority, loginCode, businessId,stepId);
	      //request.setAttribute("modelName", tableName);
	      
	      jjd.setResult(true,"保存成功");
	    } catch (Exception e) {
	      jjd.setResult(false,"保存失败:"+e.toString());
	      e.printStackTrace();
	    }
	    return new ModelAndView("jsonView", jjd.getData());
	  }
	  @RequestMapping(params={"method=queryHis"})
	  public ModelAndView queryHis(HttpServletRequest request){
		Gd gd = new Gd(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", gd.queryHis());
	  }
	  @RequestMapping(params={"method=doFlow"})
	  public ModelAndView doFlow(HttpServletRequest request){
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
		DoFlow doflow=new DoFlow(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", doflow.doFlow(wkuse));
	  }
	  @RequestMapping(params={"method=executeFlow"})
	  public ModelAndView executeFlow(HttpServletRequest request){
		WKUse wkuse = new WKUse(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", wkuse.doWfInstanceAction());
	  }
	  
	  @RequestMapping(params={"method=showUsers"})
	  public ModelAndView showUsers(HttpServletRequest request){
		  Gd gd = new Gd(RequestUtil.getMap(request));
		  return new ModelAndView("jsonView", gd.showUsers());
	  }
}

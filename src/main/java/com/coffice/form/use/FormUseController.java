package com.coffice.form.use;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.form.service.FormService;
import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.form.bean.FormInfo;
import com.coffice.form.bean.Page;
import com.coffice.form.dao.imp.MySqlFormDaoImpl;
import com.coffice.form.service.FormData;
import com.coffice.form.service.TemplateService;
import com.coffice.util.Db;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;
import com.coffice.workflow.use.WKUse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Controller
@RequestMapping("/formuse.coffice")
public class FormUseController {
	
	@RequestMapping(params = "method=showCurForm")
	public ModelAndView showCurForm(HttpServletRequest request,HttpServletResponse response) {
		WKUse app = new WKUse(RequestUtil.getMap(request));
		FormUse formuse=new FormUse(RequestUtil.getMap(request));
		return new ModelAndView("/form/use/do_form.jsp", formuse.showDocumentItem(app));
	}
	@RequestMapping(params={"method=showData"})
	  public void showData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {

		    TemplateService ts = new TemplateService();
		    String businessId = request.getParameter("businessId");
		    String stepId = request.getParameter("stepId");
		    String workId = request.getParameter("workId");

		    WorkFlowMethod wfMethod = new WorkFlowMethod();
		    //所有表单字段的tID 和 是否可编辑
		    //Map _map = wfMethod.getFormRole(businessId, stepId);
		    Map _map = wfMethod.getFormRole3(workId, stepId);
		    //可编辑字段的字段id，数据类型 是否必填 标题
		    //List validateList = wfMethod.getFormValidate(businessId, stepId);
		    String templatename = wfMethod.getFormModel1(workId, stepId);
		    MySqlFormDaoImpl formDao = new MySqlFormDaoImpl();
		    FormInfo formInfo = formDao.getFormByTemplateName(templatename);
		    String formId = wfMethod.getFormId(businessId, stepId);
		    if (("".equals(formId)) || (formId == null))
		      formId = null;

		    Configuration cfg = new Configuration();
		    String path = "";
		    path = formDao.getTemplatePath(formInfo.getId());
		    if ("".equals(path))
		      try {
		        path = SysPara.getValue("templatepath");
		        cfg.setDirectoryForTemplateLoading(new File(path));
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    else
		      cfg.setServletContextForTemplateLoading(request.getSession().getServletContext(), path);

		    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		    cfg.setOutputEncoding("UTF-8");
		    cfg.setLocale(Locale.CHINA);

		    String yhid = (String)request.getAttribute("yhid");
		    Page page = ts.getPage(formInfo.getTableName(), formId, _map, yhid, 1);

		    //page.put("template_validate_Javascript", ts.generateScript(validateList));
		    page.setTemplate(formInfo.getTemplateName());
		    if (page.getTemplate() != null)
		    {
		      Template t = cfg.getTemplate(page.getTemplate());

		      response.setContentType("text/html; charset=" + cfg.getOutputEncoding());

		      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
		      response.setHeader("Pragma", "no-cache");
		      response.setHeader("Expires", "0");
		      Writer out = response.getWriter();
		      Map map = page.getRoot();
		      String impClass = request.getParameter("impClass");
		      if ((!("".equals(impClass))) && (impClass != null))
		        try
		        {
		          Class c = Class.forName(impClass);
		          FormData formData = (FormData)c.newInstance();
		          Map mapData = formData.getFormData(formId);
		          map.putAll(mapData);
		        } catch (IllegalAccessException e) {
		          e.printStackTrace();
		        } catch (InstantiationException e) {
		          e.printStackTrace();
		        } catch (ClassNotFoundException e1) {
		          e1.printStackTrace();
		        }

		      Iterator it = map.entrySet().iterator();
		      while (it.hasNext()) {
		        Map.Entry element = (Map.Entry)it.next();
		        String key = (String)element.getKey();
		        Object au = map.get(key);
		        if (au == null)
		          map.put(key, "");
		      }
		      map.put("context", request.getContextPath());
		      map.put("id", (formId == null) ? "" : formId);
		      map.put("viewflow", "0");
		      map.put("current_logged_on_user", (String)Cache.getUserInfo(yhid, "xm"));

		      String signature_enabled = "";
		      try {
		        signature_enabled = SysPara.getValue("signature_enabled");
		      } catch (Exception ex) {
		        signature_enabled = "false";
		      }
		      map.put("signature_enabled", signature_enabled);
		      try {
		        t.process(map, out);
		      } catch (TemplateException e) {
		        throw new ServletException("Error while processing FreeMarker template", e);
		      }
		    } else {
		      throw new ServletException("The action didn't specified a command.");
		    }
	  }
	 @RequestMapping(params={"method=showFinalFormData"})
	  public void showFinalFormData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	  {
	    TemplateService ts = new TemplateService();
	    String templateName = request.getParameter("templateName");
	    MySqlFormDaoImpl formDao = new MySqlFormDaoImpl();
	    FormInfo formInfo = formDao.getFormByTemplateName(templateName);
	    String formId = request.getParameter("formId");
	    if (("".equals(formId)) || (formId == null) || ("null".equals(formId)))
	      formId = null;

	    Configuration cfg = new Configuration();
	    String path = "";
	    path = formDao.getTemplatePath(formInfo.getId());
	    if ("".equals(path))
	      try {
	        path = SysPara.getValue("templatepath");
	        cfg.setDirectoryForTemplateLoading(new File(path));
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    else
	      cfg.setServletContextForTemplateLoading(request.getSession().getServletContext(), path);

	    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	    cfg.setOutputEncoding("UTF-8");
	    cfg.setLocale(Locale.CHINA);
	    Map _map = new HashMap();

	    Page page = ts.getPage(formInfo.getTableName(), formId, _map, "", 0);
	    List validateList = new ArrayList();
	    page.put("template_validate_Javascript", ts.generateScript(validateList));
	    page.setTemplate(templateName);
	    if (page.getTemplate() != null)
	    {
	      Template t = cfg.getTemplate(page.getTemplate());
	      response.setContentType("text/html; charset=" + cfg.getOutputEncoding());
	      response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
	      response.setHeader("Pragma", "no-cache");
	      response.setHeader("Expires", "0");
	      Writer out = response.getWriter();
	      Map map = page.getRoot();
	      Iterator it = map.entrySet().iterator();
	      while (it.hasNext()) {
	        Map.Entry element = (Map.Entry)it.next();
	        String key = (String)element.getKey();
	        Object au = map.get(key);
	        if (au == null)
	          map.put(key, "");
	      }
	      map.put("context", request.getContextPath());
	      map.put("viewflow", "1");

	      String signature_enabled = "";
	      try {
	        signature_enabled = SysPara.getValue("signature_enabled");
	      } catch (Exception ex) {
	        signature_enabled = "false";
	      }
	      map.put("signature_enabled", signature_enabled);

	      String tableid = formInfo.getId();
	      List _list = Db.getJtA().queryForList("select tdid from t_form_authority where tableid=?", new Object[] { tableid });
	      for (Iterator localIterator1 = _list.iterator(); localIterator1.hasNext(); ) { Map __map = (Map)localIterator1.next();
	        map.put(((String)__map.get("tdid")) + "_tdClass", "READONLY");
	      }
	      String impClass = request.getParameter("impClass");
	      if ((!("".equals(impClass))) && (impClass != null))
	        try
	        {
	          Class c = Class.forName(request.getParameter("impClass"));
	          FormData formData = (FormData)c.newInstance();
	          Map mapData = formData.getFormData(formId);
	          map.putAll(mapData);
	        } catch (IllegalAccessException e) {
	          e.printStackTrace();
	        } catch (InstantiationException e) {
	          e.printStackTrace();
	        } catch (ClassNotFoundException e1) {
	          e1.printStackTrace();
	        }
	      try
	      {
	        t.process(map, out);
	      } catch (TemplateException e) {
	        throw new ServletException("Error while processing FreeMarker template", e);
	      }
	    } else {
	      throw new ServletException("The action didn't specified a command.");
	    }
	  }
	@RequestMapping(params = "method=showDocumentItem")
	public ModelAndView showDocumentItem(HttpServletRequest request,HttpServletResponse response) {
		WKUse app = new WKUse(RequestUtil.getMap(request));
		FormUse doc=new FormUse(RequestUtil.getMap(request));
		return new ModelAndView("/form/use/do_form.jsp", doc.showDocumentItem(app));
	}
	@RequestMapping(params={"method=saveData"})
	  public ModelAndView saveData(HttpServletRequest request, ModelMap map) {
		JspJsonData jjd = new JspJsonData();
	    String loginCode = "";
	    Map _map = RequestUtil.getMap(request);
	    loginCode = _map.get("yhid").toString();
	    try {
	      FormService formService = new FormService();
	      String tableName = request.getParameter("tableName");
	      Map authority = null;
	      String businessId = request.getParameter("businessId");
	      String stepId = request.getParameter("stepId");
	      String undo_title = request.getParameter("undo_title");
	      if(!"".equals(undo_title)){
	    	  Db.getJtA().update("update t_oswf_work_item set formname='"+undo_title+"' where Entry_ID="+businessId);
	      }
	      WorkFlowMethod wfMethod = new WorkFlowMethod();
	      String  id = request.getParameter("id");

	      authority = wfMethod.getFormRole2(tableName, stepId);
	      formService.updateFormData1(tableName, _map, id, authority, loginCode, businessId,stepId);

	      request.setAttribute("modelName", tableName);

	      jjd.setExtend("formId", id);
	      jjd.setExtend("result", "success");
	    } catch (Exception e) {
	      jjd.setExtend("result", "error");
	      e.printStackTrace();
	    }

	    return new ModelAndView("jsonView", jjd.getData());
	  }
}

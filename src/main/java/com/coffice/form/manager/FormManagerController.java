package com.coffice.form.manager;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.form.bean.FormInfo;
import com.coffice.form.bean.Page;
import com.coffice.form.dao.imp.MySqlFormDaoImpl;
import com.coffice.form.service.TemplateService;
import com.coffice.util.Db;
import com.coffice.util.RequestUtil;
import com.coffice.util.SysPara;

@Controller
@RequestMapping({"/formmanager.coffice"})
public class FormManagerController {
	@RequestMapping(params={"method=listForm"})
	public ModelAndView listForm(HttpServletRequest request){
		FormManager formmanager = new FormManager(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", formmanager.listForm());
	}
	@RequestMapping(params={"method=startForm"})
	public ModelAndView startForm(HttpServletRequest request){
		FormManager formmanager = new FormManager(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", formmanager.startForm());
	}
	@RequestMapping(params={"method=delForm"})
	public ModelAndView delForm(HttpServletRequest request){
		FormManager formmanager = new FormManager(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", formmanager.delForm());
	}
	 @RequestMapping(params={"method=showForm"})
	  public void showForm(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException
	  {
	    TemplateService ts = new TemplateService();

	    String id = request.getParameter("id");
	    MySqlFormDaoImpl formDao = new MySqlFormDaoImpl();
	    FormInfo formInfo = formDao.getFormById(id);
	    String templateName = formInfo.getTemplateName();

	    Configuration cfg = new Configuration();
	    String path = "";
	    path = formDao.getTemplatePath(id);
	    try {
	      if (!("".equals(path))) {
	    	  cfg.setServletContextForTemplateLoading(request.getSession().getServletContext(), path);
	      }
	      path = SysPara.getValue("templatepath");
	      cfg.setDirectoryForTemplateLoading(new File(path));
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	    }

	    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	    cfg.setOutputEncoding("UTF-8");
	    cfg.setLocale(Locale.CHINA);
	    Map authority = new HashMap();
	    Page page = ts.getPage(formInfo.getTableName(), "", authority, "", 0);

	    List list = new ArrayList();
	    page.put("template_validate_Javascript", ts.generateScript(list));
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
	      map.put("context", request.getContextPath());

	      List _list = Db.getJtA().queryForList("select tdid from t_form_authority where tableid=?", new Object[] { id });
	      for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { Map _map = (Map)localIterator.next();
	        map.put(_map.get("tdid").toString() + "_tdClass", "temp");
	      }
	      map.put("id", "");
	      map.put("viewflow", "0");
	      try {
	        t.process(map, out);
	      } catch (TemplateException e) {
	        throw new ServletException("Error while processing FreeMarker template", e);
	      }
	    } else {
	      throw new ServletException("The action didn't specified a command.");
	    }
	  }
	 @RequestMapping(params={"method=loadTemplateData"})
	  public void loadTemplateData(HttpServletRequest request, HttpServletResponse response)
	    throws IOException
	  {
	    response.setContentType("text/html");
	    response.setCharacterEncoding("gbk");
	    String id = request.getParameter("id");
	    String tablename = (String)Db.getJtN().queryForObject("select tablename from t_form_template_info where id='" + id + "'", String.class);
	    request.setAttribute("id", id);
	    String path = "";
	    try {
	      path = SysPara.getValue("templatepath");
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    FileInputStream infile = new FileInputStream(path + tablename + "_source.txt");
	    int size = 1024;
	    byte[] buffer = new byte[size];
	    OutputStream fos = response.getOutputStream();
	    while (true) {
	      int i = infile.read(buffer, 0, size);
	      if (i == -1)
	        break;
	      fos.write(buffer, 0, i);
	    }

	    infile.close();
	    fos.close(); 
	  }
	 @RequestMapping(params={"method=saveForm"})
	  public String saveForm(HttpServletRequest request, HttpServletResponse response)
	  {
	    Map map = RequestUtil.getMap(request);
	    TemplateService ts = new TemplateService();
	    String content = request.getParameter("EditorDefault");
	    String formName = "";
	    formName = request.getParameter("fn");
	    FormManager form = new FormManager();
	    if ("true".equals(form.getSys_para("8859ToUtf8")))
	      try {
	        formName = new String(formName.getBytes("iso8859-1"), "utf-8");
	      } catch (UnsupportedEncodingException e1) {
	        e1.printStackTrace();
	      }

	    String path = "";
	    try {
	      path = SysPara.getValue("templatepath");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    String sourceName = "index.ftl";
	    ts.createTemplate(content, path, sourceName, formName, map);
	    return "/form/manager/manager.jsp";
	  }

}

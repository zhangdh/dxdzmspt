package com.coffice.workflow.todo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/todo.coffice"})
public class ToDoController {
	 
	  @RequestMapping(params={"method=init"})
	  public ModelAndView init(HttpServletRequest request)
	  {
		ToDo todo = new ToDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", todo.init());
	  }
	  @RequestMapping(params={"method=query"})
	  public ModelAndView query(HttpServletRequest request)
	  {
		ToDo todo = new ToDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", todo.query());
	  }
	  @RequestMapping(params={"method=queryCurStep"})
	  public ModelAndView queryCurStep(HttpServletRequest request)
	  {
		ToDo todo = new ToDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", todo.queryCurStep());
	  }
	  @RequestMapping(params={"method=listDesk"})
	  public ModelAndView listDesk(HttpServletRequest request)
	  {
		ToDo todo = new ToDo(RequestUtil.getMap(request));
	    return new ModelAndView("jsonView", todo.listDesk());
	  }
	  @RequestMapping(params = "method=show")
	  public ModelAndView show(HttpServletRequest request,HttpServletResponse response) {
		ToDo doc=new ToDo(RequestUtil.getMap(request));
		return new ModelAndView("/form/use/todo_form.jsp", doc.showDocumentInfo());
	}
}

package com.coffice.hjzx.fwqq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;
import com.coffice.workflow.todo.ToDo;


@Controller
@RequestMapping("/fwqq.coffice")
public class FwqqController extends BaseUtil {	
	

	@RequestMapping(params = "method=queryFwqq")
	public ModelAndView queryFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.queryFwqq());
	}

	@RequestMapping(params = "method=saveFwqq")
	public ModelAndView saveFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.saveFwqq());
	}
	
	@RequestMapping(params = "method=modiFwqq")
	public ModelAndView modiFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.modiFwqq());
	}

	@RequestMapping(params = "method=init")
	public ModelAndView init(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.init());
	}
	@RequestMapping(params = "method=getQH")
	public ModelAndView getQH(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.getQH());
	}
	@RequestMapping(params = "method=showFwqq")
	public ModelAndView showFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.showFwqq());
	}
	//执行服务请求统计tjFwqq
	@RequestMapping(params = "method=tjFwqq")
	public ModelAndView tjFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.tjFwqq());
	}
	//执行服务请求统计tjFwqqChart
	@RequestMapping(params = "method=tjFwqqChart")
	public ModelAndView tjFwqqChart(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.tjFwqqChart());
	}
	@RequestMapping(params = "method=openZb")
	public ModelAndView openZb(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request)); 
		return new ModelAndView("/form/use/todo_form.jsp", fwqq.openZb());
	}
	@RequestMapping(params = "method=queryAllLED")
	public ModelAndView queryAllLED(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.queryAllLED());
	}
	@RequestMapping(params = "method=queryGd")
	public ModelAndView queryGd(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.queryGd());
	}
	@RequestMapping(params = "method=queryFwqqCl")
	public ModelAndView queryFwqqCl(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.queryFwqqCl());
	}
	@RequestMapping(params = "method=zbFwqq")
	public ModelAndView zbFwqq(HttpServletRequest request) {
		Fwqq fwqq = new Fwqq(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", fwqq.zbFwqq());
	}
}

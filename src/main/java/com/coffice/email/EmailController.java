package com.coffice.email;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/email.coffice")
public class EmailController extends BaseUtil{
//1201已发送----1202已删除----1203草稿箱----1204收件箱----1205彻底删除
	
	@RequestMapping(params = "method=fjxquery")
	public ModelAndView fjxquery(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.fjxQuery());//1201
	}
	// 事务声明
	@Transactional
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.save());
	}
	@RequestMapping(params = "method=send")
	public ModelAndView send(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.send());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView json(HttpServletRequest request){
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.show());
	}
	@RequestMapping(params = "method=delFj")
	public ModelAndView delFj(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.delFj());
	}
	@RequestMapping(params = "method=sendYj")
	public ModelAndView sendYj(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.sendYj());
	}
	@RequestMapping(params = "method=sjxquery")
	public ModelAndView sjxquery(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.sjxQuery());
	}
	@RequestMapping(params = "method=delSj")
	public ModelAndView delSj(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.delSj());
	}
	
	@RequestMapping(params = "method=mainList")
	public ModelAndView mainList(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.mainList());
	}
	@RequestMapping(params = "method=listDesk")
	public ModelAndView listDesk(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.listDesk());
	}
	@RequestMapping(params = "method=saveCg")
	public ModelAndView saveCg(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.saveCg());
	}
	@RequestMapping(params = "method=sendCg")
	public ModelAndView sendCg(HttpServletRequest request) {
		Email email = new Email(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", email.sendCg());
	}
}

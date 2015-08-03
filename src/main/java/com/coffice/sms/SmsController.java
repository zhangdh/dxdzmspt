package com.coffice.sms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/sms.coffice")
public class SmsController extends BaseUtil {	
	@RequestMapping(params = "method=queryDx")
	public ModelAndView queryDx(HttpServletRequest request) {
		Sms sms = new Sms(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", sms.queryDx());
	}
	@RequestMapping(params = "method=replayDx")
	public ModelAndView delZb(HttpServletRequest request) {
		Sms sms = new Sms(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", sms.replayDx());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView show(HttpServletRequest request) {
		Sms sms = new Sms(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", sms.show());
	}
	@RequestMapping(params = "method=saveFwqq")
	public ModelAndView saveFwqq(HttpServletRequest request) {
		Sms sms = new Sms(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", sms.saveFwqq());
	}
}

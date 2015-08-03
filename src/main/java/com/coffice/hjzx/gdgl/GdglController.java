package com.coffice.hjzx.gdgl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.workflow.use.WKUse;
import com.coffice.hjzx.fwqq.Fwqq;
import com.coffice.util.RequestUtil;

/**
 * 工单管理控件器
 * @author Administrator 
 *
 */
@Controller
@RequestMapping("/gdgl/default.do")
public class GdglController {
	/**
	 * 创建工单(手工)
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=createGd")
	public synchronized ModelAndView  createGd(HttpServletRequest request) {
		WKUse app = new WKUse(RequestUtil.getMap(request));
		Gdgl gdgl=new Gdgl(RequestUtil.getMap(request));
		return new ModelAndView("/gdgl/default.jsp", gdgl.createGd(app));
	}
	@RequestMapping(params = "method=getFormnameWithTx")
	public ModelAndView getFormnameWithTx(HttpServletRequest request) {
		Gdgl gdgl=new Gdgl(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", gdgl.getFormnameWithTx());	
	}
	
}

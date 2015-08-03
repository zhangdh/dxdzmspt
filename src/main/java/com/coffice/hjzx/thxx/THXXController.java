package com.coffice.hjzx.thxx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/thxx.doAction")
public class THXXController {
	/**
	 * 显示通话列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=listTHXX")
	public ModelAndView listIVRRk(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.listTHXX());
	}
	/**
	 * 保存通话信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=saveTHXX")
	public ModelAndView saveIVRCalendar(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",thxx.saveTHXX());
	}
	/**
	 * 查询通话信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryTHXX")
	public ModelAndView queryIVRCalendar(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryTHXX());
	}
	/**
	 * 查询此工号最近50条通话信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryGhList")
	public ModelAndView queryGhList(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryGhList());
	}
	
	/**
	 * 保存通话录音信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=saveReco")
	public ModelAndView saveReco(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.saveReco());
	}
	
	/**
	 * 保存回访通话录音信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=saveHfReco")
	public ModelAndView saveHfReco(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.saveHfReco());
	}
	
	/**
	 * 查询回访录音信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryHfLy")
	public ModelAndView queryHfLy(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryHfLy());
	}
	
	/**
	 * 查询录音信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryLy")
	public ModelAndView queryLy(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryLy());
	}
	
	/**
	 * 查询录音路径
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryLyPath")
	public ModelAndView queryLyPath(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryLyPath());
	}
	
	/**
	 * 查询录音路径
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=queryLyPathById")
	public ModelAndView queryLyPathById(HttpServletRequest request) {
		THXX thxx = new THXX(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", thxx.queryLyPathById());
	}
}

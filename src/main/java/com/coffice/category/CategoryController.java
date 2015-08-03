package com.coffice.category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/category.do")
public class CategoryController extends BaseUtil {
	
	//查询query
	@RequestMapping(params = "method=query")
	public ModelAndView query(HttpServletRequest request) {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.query());
	}
	@RequestMapping(params = "method=queryDm")
	public ModelAndView queryCategory(HttpServletRequest request) {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.queryDm());
	}
	@RequestMapping(params = "method=show")
	public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.show());
	}	
	//删除del
	@RequestMapping(params = "method=del")
	public ModelAndView del(HttpServletRequest request) {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.del());
	}
	//	修改
	@RequestMapping(params = "method=modi")
	public ModelAndView modi(HttpServletRequest request) {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.modi());
	}
	// 事务声明
	@Transactional
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
		Category category = new Category(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", category.save());
	}

}

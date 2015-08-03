package com.coffice.knowledge;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/knowledge.coffice")
public class KnowledgeController {
	// 转到信息维护树页面
	@RequestMapping(params = "method=saveSortTree")
	public ModelAndView saveSortTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.saveSortTree());
	}
	@RequestMapping(params = "method=getSortTree")
	public ModelAndView getSortTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.getSortTree());
	}
	@RequestMapping(params = "method=delSortTree")
	public ModelAndView delSortTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.delSortTree());
	}

	@Transactional
	@RequestMapping(params = "method=saveItem")
	public ModelAndView saveSort(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.saveItem());
	}
	

	@RequestMapping(params = "method=delItem")
	public ModelAndView delItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.delItem());
	}

	@Transactional
	@RequestMapping(params = "method=queryItem")
	public ModelAndView queryItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.queryItem());
	}
	@Transactional
	@RequestMapping(params = "method=queryKeyItem")
	public ModelAndView queryKeyItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.queryKeyItem());
	}
	@RequestMapping(params = "method=querySolrItem")
	public ModelAndView querySolrItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.querySolrItem());
	}
	@Transactional
	@RequestMapping(params = "method=showItem")
	public ModelAndView showItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.showItem());
	}
	@Transactional
	@RequestMapping(params = "method=getTree")
	public ModelAndView getTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.getTree());
	}
	@Transactional
	@RequestMapping(params = "method=getAllTree")
	public ModelAndView getAllTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.getAllTree());
	}
	@RequestMapping(params = "method=queryKb")
	public ModelAndView queryKb(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.queryKb());
	}
	@RequestMapping(params = "method=openKb")
	public ModelAndView openKb(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.openKb());
	}
	@RequestMapping(params = "method=updateSortTree")
	public ModelAndView updateSortTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.updateSortTree());
	}
	@RequestMapping(params = "method=deleteTree")
	public ModelAndView deleteTree(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.deleteTree());
	}
	@RequestMapping(params = "method=deleteItem")
	public ModelAndView deleteItem(HttpServletRequest request) {
		Knowledge kb = new Knowledge(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", kb.deleteItem());
	}
}

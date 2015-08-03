package com.coffice.unread;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.syspara.Syspara;
import com.coffice.util.BaseUtil;
import com.coffice.util.RequestUtil;


@Controller
@RequestMapping("/unread.coffice")
public class UnReadController {
	//查询query
	@RequestMapping(params = "method=listDesk")
	public ModelAndView listDesk(HttpServletRequest request) {
		UnRead unread = new UnRead(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", unread.listDesk());
	}
}

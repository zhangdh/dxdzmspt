package com.coffice.hjzx.txzx;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.hjzx.fwqq.Fwqq;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("/txzx.coffice")
public class TxzxController {
	@RequestMapping(params = "method=query")
	public ModelAndView blqk(HttpServletRequest request) {
		Txzx tjbb = new Txzx(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", tjbb.query());
	}
	
	
}

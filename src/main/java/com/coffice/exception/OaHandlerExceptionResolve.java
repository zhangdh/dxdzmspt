// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OaHandlerExceptionResolve.java

package com.coffice.exception;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

// Referenced classes of package com.ashburz.exception:
//			NeedLoginException

public class OaHandlerExceptionResolve
	implements HandlerExceptionResolver
{

	private Log log;

	public OaHandlerExceptionResolve()
	{
		log = LogFactory.getLog(getClass());
	}

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	{
		log.warn((new StringBuilder("Handle exception: ")).append(ex.getClass().getName()).toString());
		if (ex instanceof NeedLoginException)
		{
			return new ModelAndView("redirect:login.do");
		} else
		{
			Map model = new HashMap();
			model.put("ex", ex.getClass().getSimpleName());
			model.put("message", ex.getMessage());
			return new ModelAndView("error", model);
		}
	}
}

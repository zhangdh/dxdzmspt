// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AclFilter.java

package com.coffice.security;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coffice.login.Login;
 
import com.coffice.exception.MethodNotPermitException;
import com.coffice.exception.UrlNotPermitException;
import com.coffice.util.cache.Cache;


// Referenced classes of package com.ashburz.security:
//			AclVerify

public class AclFilter extends HttpServlet implements Filter{

	private String baseUrl;

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
		throws IOException, ServletException{
		Logger log = LoggerFactory.getLogger(com.coffice.security.AclFilter.class);
		FilterChain chain = arg2;
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		String uri = request.getServletPath();
		if (uri == null)uri = "";
		if (baseUrl(uri) || uri.indexOf("/wap/") >= 0 || uri.indexOf("/web/") >= 0){
			chain.doFilter(request, response);
			return;
		}
		String method = request.getParameter("method");
		String yhid = (String)request.getAttribute("yhid");
		if (yhid == null || yhid.equals("")){
			PrintWriter writer = response.getWriter();
			Cookie cookies[] = request.getCookies();
			if (cookies != null){
				for (int i = 0; i < cookies.length; i++){
					cookies[i].setValue(null);
					cookies[i].setMaxAge(0);
					response.addCookie(cookies[i]);
				}
			}
			writer.write("<script>");
			writer.write((new StringBuilder("top.document.location.href='")).append(request.getContextPath()).append("/login.jsp").append("'").toString());
			writer.write("</script>");
		} else{
			if (Cache.getGlobalInfo(yhid, "xm") == null){
				Login login = new Login();
				login.cacheInfo(yhid);
			}
			if ("admin".equals(yhid)||"sys".equals(yhid)){
				chain.doFilter(request, response);
				return;
			}
			AclVerify acl = new AclVerify();
			try{
				acl.verify(yhid, uri, method);
			}catch (UrlNotPermitException e){
				log.warn((new StringBuilder(String.valueOf(yhid))).append(" ").append(uri).append(" ").toString());
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().println((new StringBuilder("{alert:' uri:")).append(uri).append(" '}").toString());
				response.getWriter().flush();
				response.getWriter().close();
				return;
			}
			catch (MethodNotPermitException e){
				log.warn((new StringBuilder(String.valueOf(yhid))).append(" ").append(uri).append(" ").append(" ").append(method).toString());
				response.setContentType("text/html;charset=UTF-8");
				response.getWriter().println((new StringBuilder("{alert:' method:")).append(method).append(" '}").toString());
				response.getWriter().flush();
				response.getWriter().close();
				return;
			}
			chain.doFilter(request, response);
		}
	}

	private boolean baseUrl(String url){
		return baseUrl.indexOf(url) >= 0;
	}

	public void init(FilterConfig arg0)throws ServletException{
		baseUrl = arg0.getInitParameter("baseUrl");
	}

	public AclFilter(){
	}

	public void destroy(){
		super.destroy();
	}

	public void init()throws ServletException{
	}
}

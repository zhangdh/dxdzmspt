package com.coffice.security;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.util.SysPara;

public class RemoteFilter implements Filter {

	private String baseUrl;
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String uri = request.getServletPath();
		String queryString = request.getQueryString();
		req.setCharacterEncoding("UTF-8");
		if(queryString.indexOf("&api_paras=")>=0){//判断请求路径是否是.rs并且包含api_paras
			Remote remote = new Remote();
			if(remote.verify(queryString)){
				//验证通过
			}else{
				response.setCharacterEncoding("utf-8");
				PrintWriter writer = response.getWriter();
				String login_redirect="";//登录验证失败重定向的页面url
				try {
					login_redirect = SysPara.getValue("login_redirect");
				} catch (Exception e) {
					login_redirect="/login.jsp";
				}
				writer.write("<script>");
				writer.write("top.document.location.href='"+request.getContextPath()+login_redirect+"'");
				writer.write("</script>");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		this.baseUrl = arg0.getInitParameter("baseUrl");
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
}

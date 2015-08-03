package com.coffice.app.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;


public class RequestFilter implements Filter{
  private static final long serialVersionUID = -1148112408610278452L;
  private static final String DEFAULT_CONTENT_TYPE = "application/javascript;charset=utf-8";
  private String baseUrl;
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException{
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8"); 
    String uri = request.getServletPath();
    PrintWriter writer = response.getWriter();
    try{
       if ((this.baseUrl.indexOf(uri) >= 0)){
    	   if ("GET".equals(request.getMethod().toUpperCase())) {  
	            Map<String, String[]> params = request.getParameterMap();  
	            if (params.containsKey("callback")) { 
	            	response.setContentType(DEFAULT_CONTENT_TYPE);  
	                response.setCharacterEncoding("UTF-8"); 
	                response.getWriter().print(new String(params.get("callback")[0] + "("));  
	                chain.doFilter(request, response);
	                response.getWriter().print(");");  
	            }else{
	            	chain.doFilter(request, response);
	            }
		   }else{
		        	chain.doFilter(request, response);
	       }
	            	
           return;
       }else{
    	   String yhid = request.getParameter("yhid")==null?"":String.valueOf(request.getParameter("yhid"));
    	   if("".equals(yhid) || "null".equals(yhid)){
    		   Map _map = new HashMap();
    		   writer.write("{result:0,errorinfo:'验证失败 未获取到用户id'}");
    		   return;
    	   }else{
    		   request.setAttribute("yhid", yhid);
    		   if ("GET".equals(request.getMethod().toUpperCase())) {  
    	            Map<String, String[]> params = request.getParameterMap();  
    	            if (params.containsKey("callback")) { 
    	            	response.setContentType(DEFAULT_CONTENT_TYPE);  
    	                response.setCharacterEncoding("UTF-8"); 
    	                response.getWriter().print(new String(params.get("callback")[0] + "("));  
    	                chain.doFilter(request, response);
    	                response.getWriter().print(");");  
    	            }else{
    	            	chain.doFilter(request, response);
    	            }
    		   }else{
    		        	chain.doFilter(request, response);
		        }
    	   }
       }
    }finally{
    	 writer.close();
    }
    chain.doFilter(request, response);
  }

  public void init(FilterConfig arg0) throws ServletException{
	  this.baseUrl = arg0.getInitParameter("baseUrl");
  }

  public void destroy(){
  }
}
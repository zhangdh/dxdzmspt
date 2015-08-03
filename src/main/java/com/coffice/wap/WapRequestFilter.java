package com.coffice.wap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.login.Login;
import com.coffice.util.RequestUtil;
import com.coffice.util.cache.Cache;


public class WapRequestFilter implements Filter{
	
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
		ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		req.setCharacterEncoding("UTF-8");
		//请求URL
		String   path   =   (String)   request.getRequestURI();
		if(path.indexOf("/wap")>=0){
			Cookie cookies[]=request.getCookies(); 
			String token="";
			if(cookies!=null){
				for(int i=0;i<cookies.length;i++){
		            if(cookies[i].getName().equals("sdcncsi_ict_token")){
		            	token = cookies[i].getValue();
		            	break;
		            }
				}
			}
			Login login = new Login();
			String yhid = login.getYhidByToken(token);
			if(!yhid.equals("")){
				Map reqMap=RequestUtil.getMap(request);
				Map oldMap=(Map)Cache.getUserInfo(yhid, "wap");
				if(oldMap!=null){
					oldMap.putAll(reqMap);
					Cache.setUserInfo(yhid, "wap", oldMap);
				}else{
					Cache.setUserInfo(yhid, "wap", reqMap);
				}
			}else{
				Cache.setUserInfo(yhid, "wap", new HashMap());
			}

			if(path.endsWith(".jsp")){
				String newPath = path;
				int face  = 0;
				if(yhid!=null&&!"".equals(yhid)){
					if(Cache.getUserInfo(yhid, "wapface")==null){
						
					}else{
						face = (Integer)Cache.getUserInfo(yhid, "wapface");
					}
					
				}
				if(face==1){
					 newPath = path.replaceFirst("/wap/", "/wap_iphone/");
					 response.sendRedirect(newPath);
					 chain.doFilter(request, response);
					 return;
				}

			}
		}
		chain.doFilter(request, response);
	}
	
	public void destroy() {
		// TODO Auto-generated method stub

	}
}

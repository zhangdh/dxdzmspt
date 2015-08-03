package com.coffice.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import com.coffice.login.Login;
import com.coffice.util.SysPara;


public class RequestFilter	implements Filter{
  private static final long serialVersionUID = -1148112408610278452L;
  private String baseUrl;
  private final String USER_KEY = "username";
  private final String LOGINIP_KEY = "login_ip";

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException{
    HttpServletRequest request = (HttpServletRequest)req;
    HttpServletResponse response = (HttpServletResponse)res;
    String uri = request.getServletPath();
    String queryString = request.getQueryString();
    boolean isSuccSetMDC = false;
    req.setCharacterEncoding("UTF-8");
    if ((this.baseUrl.indexOf(uri) >= 0)){
      chain.doFilter(request, response);
      return;
    }
    String token = getCookieValue(request, "com.ashburz_token");
    Login login = new Login();
    String yhid = login.getYhidByToken(token);
    if (!yhid.equals("")){
      if ((!SysPara.compareValue("session_timeout", "0", "30")) && (((queryString == null) || (queryString.indexOf("method=isRepeatLogin") < 0)))) {
        login.updateActive_time(token);
      }
      request.setAttribute("yhid", yhid);
      String dlip = request.getRemoteAddr();
      isSuccSetMDC = setMDC(yhid, dlip);
    }else {
      response.setCharacterEncoding("utf-8");
      PrintWriter writer = response.getWriter();
      Cookie[] cookie = request.getCookies();
      if (cookie != null) {
        for (int i = 0; i < cookie.length; ++i) {
          cookie[i].setValue(null);
          cookie[i].setMaxAge(0);
          response.addCookie(cookie[i]);
        }
      }
      String login_redirect = "";
      try {
        login_redirect = SysPara.getValue("login_redirect");
      } catch (Exception e) {
        login_redirect = "/login.jsp";
      }
      String conpath = request.getContextPath();
      StringBuffer writeStr = new StringBuffer();
      writeStr.append("<script>try{alert('系统此账号重复登陆,登陆信息失效,请退出重新登陆');window.close();window.dialogArguments.top.document.location.href='").append(conpath).append(login_redirect).append("';}catch(e){try{top.document.location.href='").append(conpath).append(login_redirect)
              .append("';}catch(e){}finally{").append("}}finally{}")
      		  .append("</script>");
      writer.write(writeStr.toString());
      writer.close();
      return;
    }
    try{
      chain.doFilter(request, response);
    }finally {
      if (isSuccSetMDC) {
        MDC.remove("username");
        MDC.remove("login_ip");
      }
    }
  }

  public void init(FilterConfig arg0) throws ServletException{
    this.baseUrl = arg0.getInitParameter("baseUrl");
  }

  public void destroy(){
  }

  private boolean setMDC(String username, String dlip){
    if ((username != null) && (username.trim().length() > 0)) {
      MDC.put("username", username);
      MDC.put("login_ip", dlip);
      return true;
    }
    return false;
  }
  private String getCookieValue(HttpServletRequest request, String name) {
    String cookie_value = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (int i = 0; i < cookies.length; ++i) {
        if (cookies[i].getName().equals(name)) {
          cookie_value = cookies[i].getValue();
          break;
        }
      }
    }
    return cookie_value;
  }
}
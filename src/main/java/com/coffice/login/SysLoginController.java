package com.coffice.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coffice.util.Des;
import com.coffice.util.Guid;
import com.coffice.util.Mac;
import com.coffice.util.Md5;
import com.coffice.util.SysPara;
@Controller
public class SysLoginController {

	@RequestMapping("/login.doAction")
	public String login(HttpServletRequest request, HttpServletResponse response)
	{
		String dlmc = request.getParameter("dlmc");
		String password = request.getParameter("mm");
		Login login = new Login();
		/*if(!login.valiNum()){
			request.setAttribute("ifdl", "true");
			request.setAttribute("warning", "系统注册未成功");
			return "/login.jsp";
		}*/
		String login_redirect = "";
		boolean login_cookie = false;
		try
		{
			login_redirect = SysPara.getValue("login_redirect");
		}
		catch (Exception e)
		{
			login_redirect = "/login.jsp";
		}
		request.setAttribute("ifdl", "true");
		if ("".equals(dlmc) || dlmc == null)
		{
			dlmc = login.getCookieValue(request, "com.ashburz_username");
			password = login.getCookieValue(request, "com.ashburz_password");
			if ("".equals(password) || password == null)
			{
				request.setAttribute("dlmc", dlmc);
				return login_redirect;
			}
			Des des = new Des();
			password = des.getDesString(password);
			login_cookie = true;
		}
		String mainpage_url = (new StringBuilder("main.jsp?guid=")).append(Guid.get()).toString();
		String yhid = "";
		String isUSBKey = request.getParameter("isUSBKey");
		String plain_pass = password;
		password = Md5.getMd5(password);

		if (login.validate(dlmc, password))
		{
			yhid = login.getYhid(dlmc);	
			if (!login_cookie)
			{
				Cookie cookie_username = new Cookie("com.ashburz_username", dlmc);
				cookie_username.setMaxAge(0x1e13380);
				response.addCookie(cookie_username);
				if ("1".equals(request.getParameter("mem_pass")))
				{
					Cookie cookie_pass = new Cookie("com.ashburz_password", (new Des()).getEncString(plain_pass));
					cookie_pass.setMaxAge(0x1e13380);
					response.addCookie(cookie_pass);
				}
			}
			if (login_cookie)
				login.resetLoginInfo(response, dlmc, yhid);
			else
				login.setLoginInfo(request, response, login, yhid, dlmc);
			return mainpage_url;
		} else
		{
			request.setAttribute("warning", "用户名或密码错误！！");
			return login_redirect;
		}
	}
	 @RequestMapping("/exit.doAction")
	 public String exit(HttpServletRequest request, HttpServletResponse response){
			Login login = new Login();
			String yhid = login.getYhid(request);
			String login_redirect = "/login.jsp";
			/*try
			{
				login_redirect = SysPara.getValue("login_redirect");
			}
			catch (Exception e)
			{
				login_redirect = "/login.jsp";
			}*/
			Cookie cookies[] = request.getCookies();
			if (cookies != null)
			{
				for (int i = 0; i < cookies.length; i++)
					if (!cookies[i].getName().equals("com.ashburz_username"))
					{
						cookies[i].setValue(null);
						cookies[i].setMaxAge(0);
						response.addCookie(cookies[i]);
					}

			}
			if (!"".equals(yhid))
			{
				login.removeCache(yhid);
				login.delSessionInfo(yhid);
			}
			return login_redirect;
		}
}

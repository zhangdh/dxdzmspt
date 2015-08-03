package com.coffice.app.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.bean.UserBean;
import com.coffice.login.Login;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.Md5;
import com.coffice.util.SysPara;

@Controller
@RequestMapping("/app/login.app")
public class LoginController {
	
	@RequestMapping(params = "method=login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
		String dlmc = "";
		String password ="";
		String device_type ="";
		String app_version ="";
		String version = "";
		String ip = "";
		JspJsonData jjd = new JspJsonData();
		Map _map = new HashMap();
		try{
			dlmc = request.getParameter("yhm")==null?"":String.valueOf(request.getParameter("yhm"));
			password =  request.getParameter("mm")==null?"":String.valueOf(request.getParameter("mm"));
			ip =  request.getRemoteAddr();
			password = Md5.getMd5(password);
			Login login = new Login();
			if("".equals(dlmc) || "".equals(password)){
				_map.put("result", "0");
				_map.put("errorinfo","没有获取到用户名或密码数据");
			}
			if(login.validate(dlmc, password)){
				String yhid = login.getYhid(dlmc);	
				UserBean user  = UserBean.get(yhid);
				_map.put("result", "1");
				_map.put("yhid", yhid);
				_map.put("yhmc", user.getXm());
				device_type = request.getParameter("device_type")==null?"":String.valueOf(request.getParameter("device_type"));
				app_version = request.getParameter("appversion")==null?"":String.valueOf(request.getParameter("appversion"));
				if("anroid".equals(device_type)){
					version =SysPara.getValue("anroid_version");
				}else if("ios".equals(device_type)){
					version =SysPara.getValue("iphone_version");
				}
				_map.put("appversion",version);
				//保存登录信息
				login.insertLogin(yhid,ip,device_type,app_version);
				login.cacheInfo(yhid);
			}else{
				_map.put("result", "0");
				_map.put("errorinfo","用户名或密码错误");
			}
		}catch(Exception e){
			Log.writeLog("error","用户:"+dlmc+" 登录验证异常,"+e.toString());
		}
		return new ModelAndView("jsonView", _map);
	}
}

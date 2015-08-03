package com.coffice.login;

import java.util.*;
import javax.servlet.http.*;
import com.coffice.util.Wh;
import com.coffice.bean.UserBean;
import com.coffice.exception.ServiceException;
import com.coffice.security.Token;
import com.coffice.util.*;
import com.coffice.util.cache.Cache;

public class Login extends BaseUtil{
	protected String getCookieValue(HttpServletRequest request, String key)
	{
		Cookie cookies[] = request.getCookies();
		String token = "";
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (!cookies[i].getName().equals(key))
					continue;
				token = cookies[i].getValue();
				break;
			}

		}
		return token;
	}
	public boolean validate(String username, String password){
		int count = getJtA().queryForInt("select count(*) from t_org_yh where dlmc=? and dlmm=? and zt_dm=1", new Object[] {
			username, password
		});
		return count == 1;
	}
	public int validate(String username, String password, String ip){
		Wh wh = new Wh();
		Map ipmap = new HashMap();
		ipmap.put("phoneip", ip);
		Map flthm_map = wh.checkSysWh("wap_flthm", ipmap);
		if ("1".equals((String)flthm_map.get("tflag")))
		{
			LogItem logItem = new LogItem();
			logItem.setMethod("validate");
			String guid = Guid.get();
			logItem.setLogid(guid);
			logItem.setLevel("debug");
			logItem.setDesc("手机登录信息");
			logItem.setContent((new StringBuilder("手机正常访问，登录用户：")).append(username).append(";ip地址：").append(ip).toString());
			Log.write(logItem);
			return !validate(username, password) ? 0 : 200;
		} else
		{
			LogItem logItem = new LogItem();
			logItem.setMethod("validate");
			String guid = Guid.get();
			logItem.setLogid(guid);
			logItem.setLevel("debug");
			logItem.setDesc("手机登录信息");
			logItem.setContent((new StringBuilder("手机访问受限，登录用户：")).append(username).append(";ip地址：").append(ip).toString());
			Log.write(logItem);
			return -200;
		}
	}
	public String getYhid(String dlmc){
		String yhid = (String)getJtA().queryForObject("select yhid from t_org_yh where dlmc=? and zt_dm=1", new Object[] {
			dlmc
		}, java.lang.String.class);
		return yhid;
	}
	public void resetLoginInfo(HttpServletResponse response, String dlmc, String yhid){
		String token = "";
		List list = getJtN().queryForList("select token from t_session where yhid=? and zt_dm=1", new Object[] {
			yhid
		});
		if (list.size() > 0)
			token = (String)((Map)list.get(0)).get("token");
		Cache.setUserInfo(yhid, "dlmc", dlmc);
		if (Cache.getUserInfo(yhid, "xm") == null)
			cacheInfo(yhid);
		Cookie cookie = new Cookie("com.ashburz_token", token);
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		updateDlcs(yhid);
		(new Wh()).updateSysLog("login", 1);
	}
	public void cacheInfo(String yhid){
		UserBean userbean = UserBean.get(yhid);
		Cache.setUserInfo(yhid, "userbean", userbean);
		Cache.setUserInfo(yhid, "zzid", userbean.getZzid());
		Cache.setUserInfo(yhid, "xm", userbean.getXm());
		Cache.setUserInfo(yhid, "js", userbean.getJsid_list());
		Cache.setUserInfo(yhid, "bmid", userbean.getBmid());
		Cache.setUserInfo(yhid, "bmmc", userbean.getBmmc());
		Cache.setUserInfo(yhid, "org", getOrgInfo(yhid));
		Cache.setUserInfo(yhid, "gw_sel", getGw_Selected(yhid));
		Cache.setUserInfo(yhid, "url", getUrlByYhid(yhid));
		Cache.setUserInfo(yhid, "method", getMethodByYhid(yhid));
		if (Cache.getGlobalInfo("", "method") == null)
			Cache.setGlobalInfo("", "method", getAllMethod());
		if (Cache.getGlobalInfo("", "url") == null)
			Cache.setGlobalInfo("", "url", getAllUrl());
		Cache.setUserInfo(yhid, "isManager", Boolean.valueOf(isManager(yhid)));
		//Cache.setUserInfo(yhid, "sys_btn_auth", (new Org()).Get_QxBtn_List(yhid));
		//缓存cache_yhxx_kz参数指定的扩展信息
		try {
			String cache_yhxx_kz_z = SysPara.getValue("cache_yhxx_kz");
			String kzx[] = cache_yhxx_kz_z.split("~");
			for(int i=0;i<kzx.length;i++){
				List _list  = this.getJtN().queryForList("select kzz from t_org_yh_kz where yhid='"+yhid+"' and kz_dm ='"+(kzx[i].split(":"))[1]+"'");
				if(_list.size()>0){
					Map kzz_map = (Map)_list.get(0);
					if(kzz_map.size()>0){
						Cache.setUserInfo(yhid, kzx[i].split(":")[0], kzz_map.get("kzz"));
					}else{
						Cache.setUserInfo(yhid, kzx[i].split(":")[0],"" );		
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public void setLoginInfo(HttpServletRequest request, HttpServletResponse response, Login login, String yhid, String dlmc){
		String token = "";
		try{
			token = Token.getToken(Guid.get());
		}catch (Exception e){
			e.printStackTrace();
		}
		String ip = request.getRemoteAddr();
		String info[] = login.insertSessionInfo(yhid, ip, token);
		if (!"".equals(info[0]))
			if (ip.equals(info[1])){
				getJtN().update("delete from t_session where yhid=? and zt_dm=0", new Object[] {yhid});
			}else{
				String prompt = (new StringBuilder("在")).append(info[1]).append("上登录的用户").append(dlmc).append("将自动退出").toString();
				request.setAttribute("ip", info[1]);
				request.setAttribute("prompt", prompt);
			}
		Cache.setUserInfo(yhid, "dlmc", dlmc);
		login.cacheInfo(yhid);
		Cookie cookie = new Cookie("com.ashburz_token", token);
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
		login.updateDlcs(yhid);
		(new Wh()).updateSysLog("login", 1);
	}
	public String[] insertSessionInfo(String yhid, String ip, String token){
		LogItem logItem = new LogItem();
		String info[] = {"", ""};
		int yhs = 0;
		try{
			if (SysPara.compareValue("session_storage", "1", "1")){
				List list = getJtA().queryForList("select * from t_session where yhid=? order by login_time desc", new Object[] {
					yhid
				});
				if (list.size() > 0){
					getJtA().update("update t_session set zt_dm=0 where yhid=?", new Object[] {
						yhid
					});
					Map map = (Map)list.get(0);
					info[0] = (String)map.get("token");
					info[1] = (String)map.get("ip");
				}
				getJtA().update("insert into t_session(yhid,ip,token,login_time,active_time)values(?,?,?,now(),now())", new Object[] {
					yhid, ip, token
				});
				yhs = getJtA().queryForInt("select count(*) from t_session where zt_dm=1");
			}else{
				String _tokens = (String)Cache.getGlobalInfo("tokens", yhid);
				if (_tokens != null){
					String _token = _tokens.split("~")[0];
					info[0] = _token;
					info[1] = (String)Cache.getGlobalInfo("ip", _token);
					Cache.setGlobalInfo("zt_dm", _token, Integer.valueOf(0));
					removeCache(yhid);
				}
				Cache.setGlobalInfo("yhid", token, yhid);
				Cache.setGlobalInfo("active_time", token, Long.valueOf((new Date()).getTime()));
				Cache.setGlobalInfo("zt_dm", token, Integer.valueOf(1));
				Cache.setGlobalInfo("ip", token, ip);
				String tokens = (String)Cache.getGlobalInfo("tokens", yhid);
				if (tokens != null)
					Cache.setGlobalInfo("tokens", yhid, (new StringBuilder(String.valueOf(token))).append("~").append(tokens).toString());
				else
					Cache.setGlobalInfo("tokens", yhid, token);
				String users_online = (String)Cache.getGlobalInfo("users", "online");
				yhs = 0;
				if (users_online != null && !"".equals(users_online)){
					yhs = users_online.split("~").length;
					if (users_online.indexOf(yhid) < 0){
						Cache.setGlobalInfo("users", "online", (new StringBuilder(String.valueOf(users_online))).append("~").append(yhid).toString());
						yhs++;
					}
				}else{
					Cache.setGlobalInfo("users", "online", yhid);
					yhs = 1;
				}
			}
			String guid = Guid.get();
			logItem.setMethod("[com.ashburz].insertSessionInfo");
			logItem.setLogid(guid);
			logItem.setLevel("info");
			logItem.setDesc("当前用户数");
			logItem.setContent((new StringBuilder("当前登录用户：")).append(yhid).append(";当前登录用户数：").append(yhs).toString());
			Log.write(logItem);
		}catch (Exception ex){
			String guid = Guid.get();
			logItem.setMethod("[com.ashburz.login].insertSessionInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存数据时出现异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("保存数据时异常");
		}
		return info;
	}
	public void removeCache(String yhid){
		Cache.removeInfo(yhid, "userbean");
		Cache.removeInfo(yhid, "zzid");
		Cache.removeInfo(yhid, "xm");
		Cache.removeInfo(yhid, "dlmc");
		Cache.removeInfo(yhid, "js");
		Cache.removeInfo(yhid, "org");
		Cache.removeInfo(yhid, "gw_sel");
		Cache.removeInfo(yhid, "url");
		Cache.removeInfo(yhid, "method");
	}
	private boolean isManager(String yhid){
		if ("admin".equals(yhid) || "sys".equals(yhid))
			return true;
		List _list = getJtN().queryForList("select kzz from t_org_yh_kz where yhid=? and kz_dm=303", new Object[] {
			yhid
		});
		if (_list.size() > 0){
			String bmid = (String)((Map)_list.get(0)).get("kzz");
			if ("0".equals(bmid))
				return true;
			int count_bm = getJtN().queryForInt("select count(*) from t_org_bm where zt_dm=1");
			int count_cbm = getJtN().queryForInt((new StringBuilder("select count(*) from t_org_bm where bmid like '")).append(bmid).append("%'").toString());
			return count_bm == count_cbm;
		} else
		{
			return false;
		}
	}
	public List getAllMethod(){
		List list = getJtA().queryForList("select concat(url,method) as method from t_qx_mx where qxlx=2 and zt_dm=1");
		return list;
	}

	public List getAllUrl(){
		List list = getJtA().queryForList("select url,mk_dm  from t_qx_mx where qxlx=1 and zt_dm=1");
		return list;
	}
	public List getMethodByYhid(String yhid){
		StringBuffer sb = new StringBuffer();
		sb.append("select concat(url,method) as method  from t_qx_mx a,t_qx_js b ");
		sb.append("where a.qxid = b.qxid and a.qxlx='2' and jsid in(");
		sb.append("select kzz from t_org_yh_kz  where yhid='").append(yhid).append("' and kz_dm=300) and b.zt_dm=1 ");
		sb.append(" union ");
		sb.append("select concat(url,method) from t_qx_mx a,t_qx_js b where a.qxid = b.qxid ");
		sb.append("and jsid='").append(yhid).append("' and a.qxlx='2' and b.zt_dm=1");
		List list = getJtA().queryForList(sb.toString());
		return list;
	}
	public List getUrlByYhid(String yhid){
		StringBuffer sb = new StringBuffer();
		sb.append("select url  from t_qx_mx a,t_qx_js b ");
		sb.append("where a.qxid = b.qxid and a.qxlx='1' and jsid in(");
		sb.append("select kzz from t_org_yh_kz  where yhid='").append(yhid).append("' and kz_dm=300) and b.zt_dm=1");
		sb.append(" union ");
		sb.append("select url from t_qx_mx a,t_qx_js b where a.qxid = b.qxid ");
		sb.append("and jsid='").append(yhid).append("' and a.qxlx='1' and b.zt_dm=1");
		List list = getJtA().queryForList(sb.toString());
		return list;
	}
	public String getOrgInfo(String yhid){
		StringBuffer sb = new StringBuffer();
		List list = getJtA().queryForList("select b.mc as bm_mc,c.mc as gw_mc,b.lxid as bmid,c.lxid as gwid from t_org_tree a,t_org_tree b,t_org_tree c where a.sjbmid=b.lxid and a.sjid=c.guid and c.sjbmid=b.lxid and a.lxid=? and a.zt_dm=1 and b.zt_dm=1 and c.zt_dm=1", new Object[] {
			yhid
		});
		Map map;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); sb.append(";").append(map.get("bm_mc")).append(":").append(map.get("gw_mc")).append("|").append(map.get("bmid")).append(":").append(map.get("gwid")))
			map = (Map)iterator.next();

		sb.replace(0, 1, "");
		return sb.toString();
	}

	public List getGw_Selected(String yhid){
		List list = getJtA().queryForList("select * from t_yh_gw where yhid=?", new Object[] {
			yhid
		});
		return list;
	}
	public void updateDlcs(String yhid){
		LogItem logItem = new LogItem();
		try
		{
			getJtA().update("update t_org_yh set dlcs=dlcs+1 where yhid=?", new Object[] {
				yhid
			});
			logItem.setMethod("updateDlcs");
			logItem.setLevel("info");
			logItem.setDesc("保存成功");
			Log.write(logItem);
		}catch (Exception e){
			String guid = Guid.get();
			logItem.setMethod("updateDlcs");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("");
			logItem.setContent(e.toString());
			Log.write(logItem);
			throw new ServiceException("");
		}
	}
	public String getYhid(HttpServletRequest request){
		Cookie cookies[] = request.getCookies();
		String token = "";
		if (cookies != null){
			for (int i = 0; i < cookies.length; i++){
				if (!cookies[i].getName().equals("com.ashburz_token")) continue;
				token = cookies[i].getValue();
				break;
			}
		}
		String yhid = getYhidByToken(token);
		return yhid;
	}

	public String getYhidByToken(String token){
		String yhid = "";
		if (SysPara.compareValue("session_storage", "1", "1")){
			List list = getJtA().queryForList("select yhid from t_session where token=? and zt_dm=1", new Object[] {
				token
			});
			if (list.size() > 0)
				yhid = (String)((Map)list.get(0)).get("yhid");
		} else{
			int zt_dm = 0;
			yhid = (String)Cache.getGlobalInfo("yhid", token);
			Object obj = Cache.getGlobalInfo("zt_dm", token);
			if (obj != null)
				zt_dm = ((Integer)obj).intValue();
			if (yhid == null || zt_dm == 0)
				yhid = "";
		}
		return yhid;
	}

	public void delSessionInfo(String yhid){
		if (SysPara.compareValue("session_storage", "1", "1")){
			LogItem logItem = new LogItem();
			try{
				getJtA().update("delete from t_session where yhid = ?", new Object[] {
					yhid
				});
				logItem.setMethod("delSessionInfo");
				logItem.setLevel("info");
				logItem.setDesc("更新成功");
				Log.write(logItem);
			}catch (Exception ex){
				String guid = Guid.get();
				logItem.setMethod("delSessionInfo");
				logItem.setLogid(guid);
				logItem.setLevel("error");
				logItem.setDesc("删除数据时出现异常");
				logItem.setContent(ex.toString());
				Log.write(logItem);
				throw new ServiceException("删除t_session数据时异常");
			}
		}else{
			String online_users = (String)Cache.getGlobalInfo("users", "online");
			online_users = online_users.replaceFirst((new StringBuilder("~?")).append(yhid).toString(), "");
			Cache.setGlobalInfo("users", "online", online_users);
			String token[] = String.valueOf(Cache.getGlobalInfo("tokens", yhid)).split("~");
			String as[] = token;
			int i = 0;
			for (int j = as.length; i < j; i++){
				String t = as[i];
				Cache.removeInfo("yhid", t);
				Cache.removeInfo("ip", t);
				Cache.removeInfo("active_time", t);
				Cache.removeInfo("zt_dm", t);
			}
			Cache.removeInfo("tokens", yhid);
		}
	}
	public void updateActive_time(String token){
		LogItem logItem = new LogItem();
		try{
			if (SysPara.compareValue("session_storage", "1", "1")){
				getJtA().update("update t_session set active_time =now() where token = ? and zt_dm=1", new Object[] {
						token
					});
			}else{
				Cache.setGlobalInfo("active_time", token, Long.valueOf((new Date()).getTime()));
			}
			logItem.setMethod("updateActive_time");
			logItem.setLevel("debug");
			logItem.setDesc("更新成功");
			Log.write(logItem);
		}catch (Exception ex){
			String guid = Guid.get();
			logItem.setMethod("updateActive_time");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("更新active_time时出现异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("更新t_session数据时异常");
		}
	}
 public boolean valiNum(){
	 String codeNum = Code.codeNum;
	 String MAC = Mac.getMac();
	 String Md5Mac = Md5.getMd5(MAC+"MD5");
	 if(Md5Mac.equals(codeNum)){
		 return true;
	 }else{
		 return false;
	 }
 }
 public void insertLogin(String yhid,String ip,String device_type,String app_version){
	 Db.getJtN().update("delete from t_session_app where  yhid = '"+yhid+"'");
	 Db.getJtN().update("insert into t_session_app values('"+yhid+"','"+ip+"','"+Db.getStr()+"','"+device_type+"','"+app_version+"')");
 }
}

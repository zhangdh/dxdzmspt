package com.coffice.security;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Md5;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Remote extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;
	
	public Remote(){
		
	}
	public Remote(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	
	public Map init() {
		try {		
			List list = this.getNpjtN().getJdbcOperations().queryForList("select distinct apikey as dm,apikey as mc from t_sec_apiurl where zt_dm=1");
			jjd.setSelect("apikey", list);
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "初始化远程调用地址时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("init");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("初始化远程调用地址时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	//级联远程地址调用名称
	@SuppressWarnings("unchecked")
	public Map listMc(){
		try {
			Map map1 = new HashMap();
			map1.put("dm", "-");
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
  			List list = npjt.queryForList("select apikey as dm,apikey as mc from t_sec_apiurl where zt_dm=1 and apiurl=:api_url",map);
  			list.add(0,map1);//去掉"请选择"
  			jjd.setSelect("apikey", list);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("listMc");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("级联调用名称时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	 /**
	 * 客户端按顺序加密所有请求参数
	 * @param api_key 调用远程服务名称
	 * @param querystr 其他参数，如果没有，可以为空
	 * @return
	 * @author fanglm created on Jul 26, 2010 1:43:02 PM
	 */
	public String afterSign(String api_key,String querystr) {
		StringBuffer beforeSign = new StringBuffer();
		StringBuffer afterSign = new StringBuffer();
		try {		
			//String api_key = (String)map.get("api_key");
			String api_ver = SysPara.getValue("api_ver");//从系统参数表中取出版本号
			String api_secret = SysPara.getValue("api_secret");//从系统参数表中取出密钥
			String api_zzid = SysPara.getValue("api_zzid");//从参数表中取出组织标示ID
			if(api_zzid==null){
				api_zzid = "";
			}
			String api_yhid = (String)Cache.getUserInfo(yhid, "dlmc");//获取登录用户ID
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			String api_ts = sf.format(new Date());
			String api_paras = "",api_url = "";
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			List _list = npjt.getJdbcOperations().queryForList("select apiurl from t_sec_apiurl where zt_dm=1 and apikey=?", new Object[]{api_key});
			Map _map = (Map)_list.get(0);
			api_url = (String)_map.get("apiurl");
			if(api_url!=null && !"".equals(api_url)){
				//按字母顺序拼接字符串
				beforeSign.append("api_key=").append(api_key).append("&api_ts=").append(api_ts).append("&api_ver=").append(api_ver)
				.append("&api_yhid=").append(api_yhid).append("&api_zzid=").append(api_zzid);
				
				//用Base64加密拼接后的字符串
				afterSign.append(api_url);
				//判断请求路径是否包含参数
				if(api_url.lastIndexOf("?")>=0){
					afterSign.append("&");
				}else{
					afterSign.append("?");
				}
				if(!"".equals(querystr)){
					beforeSign.append("&").append(querystr);
				}
				String[] sorts = beforeSign.toString().split("&");
				beforeSign.delete(0, beforeSign.length());
				Arrays.sort(sorts);//按字母顺序排列参数 
				for(int i=0;i<sorts.length;i++){
					beforeSign.append(sorts[i]).append("&");//重新拼装参数
				}
				if(beforeSign.length()>0){
					api_paras = beforeSign.toString().substring(0,beforeSign.toString().length()-1);//去掉最后一个&
					api_paras = new String(Base64.encode(api_paras.getBytes()));
				}
				//将密钥加到参数后并用Md5加密生成签名
				String api_sign = Md5.getMd5(api_paras+api_secret);
				
				afterSign.append("api_ver=").append(api_ver).append("&api_key=").append(api_key).append("&api_zzid=").append(api_zzid)
				.append("&api_yhid=").append(api_yhid).append("&api_paras=").append(api_paras).append("&api_ts=").append(api_ts).append("&api_sign=").append(api_sign);
				//jjd.setResult(true, "处理成功");
				//jjd.setExtend("url", afterSign.toString());
			}else{
				//jjd.setResult(false, "得到的远程地址为空");
			}
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "获取远程地址时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("afterSign");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取远程地址时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return afterSign.toString();
	}
	/**
	 * 服务端验证签名和时间戳
	 * @param queryString
	 * @return boolean
	 * @author fanglm created on Jul 15, 2010 2:00:12 PM
	 */
	public boolean verify(String queryString){
		String key = "",value = "",api_paras = "",api_sign = "",ser_sign = "",api_ts = "",api_zzid = "",ser_secret = "";
		String[] str = queryString.split("&");
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String ser_ts2 = sf.format(new Date()); //获取服务器时间
		for(int i=0;i<str.length;i++){
			String tempStr = str[i];
			key = tempStr.substring(0,tempStr.indexOf("="));
			value = tempStr.substring(tempStr.indexOf("=")+1);
			if(key.equals("api_paras")){
				api_paras = value;
			}
			if(key.equals("api_sign")){
				api_sign = value;
			}
		}
		String afterDecode = new String(Base64.decode(api_paras));//解密字符串 获取加密前的api_zzid和api_ts
		String[] arrDecode = afterDecode.split("&");
		for(int j =0;j<arrDecode.length;j++){
			String tempDecode = arrDecode[j];
			key = tempDecode.substring(0,tempDecode.indexOf("="));
			value = tempDecode.substring(tempDecode.indexOf("=")+1);
			if(key.equals("api_zzid")){
				api_zzid = value;
			}
			if(key.equals("api_ts")){
				api_ts = value;
			}
		}
		ser_secret = this.getSecretKey(api_zzid);//根据api_zzid获取服务器端密钥
		ser_sign = Md5.getMd5(api_paras+ser_secret);//md5加密 生成服务器端签名
		int api_timeout = 0;
		try {//获取服务端时间戳
			api_timeout = Integer.parseInt(SysPara.getValue("api_timeout"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long interval = Long.parseLong(ser_ts2)-Long.parseLong(api_ts);
		//判断签名是否一致 并且时间差在允许的时间范围内
		if(ser_sign.equals(api_sign) && interval<=api_timeout){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 根据zzid查询对应的密钥并返回该密钥
	 * @param zzid
	 * @return 密钥
	 */
	public String getSecretKey(String zzid){
		String api_secret = "";
		try {
			if(SysPara.getValue("db_type").equals("oracle") && zzid.equals("")){
				api_secret = (String)this.getNpjtN().getJdbcOperations().queryForMap("select apisecret from t_sec_secret where zzid is null").get("apisecret");
			}else{
				api_secret = (String)this.getNpjtN().getJdbcOperations().queryForMap("select apisecret from t_sec_secret where zzid=?",new Object[]{zzid}).get("apisecret");
			}
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "获取密钥时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("getSecretKey");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取密钥时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return api_secret;
	}
	
	public Map testSign(String querystr) {
		try {		
			String api_key = (String)map.get("api_key");
			String url = this.afterSign(api_key, querystr);
			jjd.setExtend("url", url);
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "获取加密后的字符串时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("testSign");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取加密后的字符串时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
}

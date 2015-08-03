package com.coffice.msg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class Mesg extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;
	String bmid;
	String gwid;
	String zzid;

	Map map;
	String db_type="";
	public Mesg(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
		try{
			db_type=SysPara.getValue("db_type");
		}catch(Exception ex){
			
		}
		
	}
	public Mesg() {
	}
	
	public Map showLy(){
		String ywid="";
		int num=0;
		try {
			ywid=String.valueOf(map.get("ywid"));
			Map _map=new HashMap();
			_map.put("ywid", ywid);
  			num= this.getNpjtA().queryForInt("select count(*) from t_msg where ywid='"+ywid+"'",_map);
			jjd.setExtend("msg_num", num);
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("showLy");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示留言时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map queryLy(){
		String ywid="";
		int num=0;
		try {
			ywid=String.valueOf(map.get("ywid"));
			PageBean page = new PageBean();
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageSize("10");
			}
			page.setPageGoto((String)map.get("page_goto"));
			page.setSql("select zzid, bmid, gwid, (select xm from t_org_yh where yhid=t_msg.yhid) as yhid, guid, mk_dm, ywid, msgnr, cjsj from t_msg where ywid='"+map.get("ywid")+"'");
	        page.setCountSql("select count(*)  from t_msg where ywid='"+map.get("ywid")+"'");
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
			
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("showLy");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示留言时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map saveLy(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			String mk_dm = map.get("mk_dm")==null?"":String.valueOf(map.get("mk_dm"));
			String msgnr = map.get("msgnr")==null?"":String.valueOf(map.get("msgnr"));
			String ywid = map.get("ywid")==null?"":String.valueOf(map.get("ywid"));
			sqlStr.append("insert into t_msg values('").append(zzid).append("','").append(bmid)
				  .append("','").append(gwid).append("','").append(yhid).append("','")
				  .append(Guid.get()).append("','").append(mk_dm).append("','").append(ywid)
				  .append("','").append(msgnr).append("','").append(this.DateConvertStr()).append("')");
			this.getJtN().update(sqlStr.toString());
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("saveLy");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存留言时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map showMx(){
		try{
			String guid  = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			Map _map = this.getJtN().queryForMap("select (select xm from t_org_yh where yhid = a.yhid)xm,a.* from t_msg a where guid='"+guid+"'");
			jjd.setForm(_map);
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("showMx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示留言时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
}

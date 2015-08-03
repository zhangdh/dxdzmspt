package com.coffice.category;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.bean.PageBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;


public class Category extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;

	public Category(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	
	public Map save() {
		try {
			if(map.get("dm")!=null && !"".equals(map.get("dm"))){
				//修改
				NamedParameterJdbcTemplate npjt = this.getNpjtA();
				npjt.update("update t_dm  set mc=:mc,sm=:sm where dm =:dm",map);
				jjd.setResult(true, "修改成功");
			}else{
				String lb_dm = map.get("lb_dm").toString();
				map.put("dm", getNextDm(lb_dm));
				map.put("zt_dm", 1);
				map.put("cjsj",new Date(System.currentTimeMillis()));
				map.put("xh", 0);
				this.getSji().withTableName("t_dm").execute(map);
				jjd.setResult(true, "保存成功");
			}
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "保存数据时出现错误，错误代码:" + guid);
			throw new ServiceException("保存t_dm数据时异常");// 抛出此异常以触发回滚
		}
		return jjd.getData();
	}
	
	public Map query() {
		try {
/*			NamedParameterJdbcTemplate npjt = this.getNpjtA();
  			List _list = npjt.queryForList("select dm,zwmc as mc from t_dm_lb where dm>=1000 and dm<10000",map);*/
			List _list = null;
			StringBuffer sb = new StringBuffer();
			//if("admin".equals(map.get("yhid"))){
                    sb.append("select dm ,zwmc  as mc from t_dm_lb a ");
                     _list = this.getJtN().queryForList(sb.toString());	
			/*//}else{
               sb.append("select dm,zwmc as mc from t_dm_lb a ,t_dm_lb_kz b");
			   sb.append(" where a.dm =b.lb_dm and (b.zzid='");
			   sb.append(map.get("zzid"));
			   sb.append("' or b.zzid='0') and b.zybz='1' and b.zt_dm=1");
			   sb.append(" and a.dm>=1000  order by dm");
			   
			 _list = this.getJtN().queryForList(sb.toString());
			}*/
			
			jjd.setSelect("dm_lb", _list);
			jjd.setGrid("datalist", new ArrayList(), null);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "dm.query异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map queryDm() {
		try {
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			if(map.get("page_size")!=null){
				page.setPageSize(String.valueOf(map.get("page_size")));
			}else{
				page.setPageSize("10");
			}
			NamedParameterJdbcTemplate npjt = this.getNpjtA();
			page.setSql("select * from t_dm where zt_dm=1 and lb_dm='"+map.get("dm_lb")+"'");
			page.setCountSql("select count(*)  from t_dm where zt_dm=1 and lb_dm='"+map.get("dm_lb")+"'");
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "dm.query异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map show() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtA();
			Map _map = npjt.queryForMap("select * from t_dm where dm=:dm", map);
			jjd.setForm(_map);	
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "查找明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("show");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查找明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

  	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	private String getCurrentTime() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		return curTime;

	}
	/**
	 * 获取最大代码Dm
	 * 
	 * @return
	 */
	private int getNextDm(String lb_dm) {
		String maxsql="";
		int MaxDm=0;
		try{
			maxsql="select max(dm)+1 from t_dm where lb_dm = '"+lb_dm+"'";
			MaxDm =this.getJtA().queryForInt(maxsql);
			if(MaxDm == 1){
				MaxDm=Integer.parseInt(lb_dm+"01");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取最大代码时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Dm.getNextDm异常:" + e.toString());
		}
		return MaxDm;

	}
	/**
	 * 获取最大代码Dm
	 * 
	 * @return
	 */
	private boolean checkIsExists(String lb_dm,String mc) {
		String sql="";
		int num=0;
		boolean flag=false;
		try{
			sql="select  count(*) from t_dm where lb_dm="+lb_dm+" and mc='"+mc+"'";
			num =this.getJtA().queryForInt(sql);
			if(num>0){flag=true;}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取最大代码时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Dm.getNextDm异常:" + e.toString());
		}
		return flag;

	}	
	public Map del() {
		String dm = map.get("dm")==null?"":String.valueOf(map.get("dm"));
		
		try {			
			this.getJtA().update("update t_dm  set zt_dm=0 where dm = '"+dm+"'");
			jjd.setResult(true, "删除成功");
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "查找明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("del_update");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查找明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map modi() {
		try {			
			NamedParameterJdbcTemplate npjt = this.getNpjtA();
			npjt.update("update t_dm  set mc=:mc,sm=:sm where dm =:dm",map);
			jjd.setResult(true, "修改成功");
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "查找明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("del_update");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查找明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
}

package com.coffice.syspara;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;



public class Syspara extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;

	public Syspara(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}

	
	public Map query() {
		StringBuffer sqlTemp=null;
		String sqlwhere="";
		try {
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			if(map.get("page_size")!=null){
				page.setPageSize(String.valueOf(map.get("page_size")));
			}else{
				page.setPageSize("10");
			}
			
			sqlTemp=new StringBuffer();
			if(map.get("cx_mk")!=null&&!map.get("cx_mk").toString().equals("")){
				sqlTemp.append(" and mk_dm='").append(map.get("cx_mk")).append("'");
		    }
			if(map.get("cx_mc")!=null&&!map.get("cx_mc").toString().equals("")){
				sqlTemp.append(" and csdm like '%").append(map.get("cx_mc")).append("%'");
		    }
			if(map.get("cx_sm")!=null&&!map.get("cx_sm").toString().equals("")){
				sqlTemp.append(" and cssm like '%").append(map.get("cx_sm")).append("%'");
		    }
			if(map.get("cx_csz")!=null&&!map.get("cx_csz").toString().equals("")){
				sqlTemp.append(" and csz like '%").append(map.get("cx_csz")).append("%'");
		    }
			sqlwhere=sqlTemp.toString();
			page.setSql("select * from t_sys_para where yxbz=1 "+sqlwhere);
			page.setCountSql("select count(*)  from t_sys_para where yxbz=1 "+sqlwhere);
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
			if(map.get("cx_mk")==null||map.get("cx_mk").toString().equals("")){
				NamedParameterJdbcTemplate npjt = this.getNpjtA();
	  			List _selectlist = npjt.queryForList("select dm,sm as mc from t_dm where lb_dm=1 and zt_dm=1",map);
	  			//jjd.setSelect("cx_mk", _selectlist);
			}
			} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "syspara.query异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map show() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtA();
			Map _map = npjt.queryForMap("select * from t_sys_para where id=:id", map);
			_map.put("old_csz", _map.get("csz"));
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
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		return curTime;

	}


	private boolean checkIsExists(String lb_syspara,String mc) {
		String sql="";
		int num=0;
		boolean flag=false;
		try{
			sql="select  count(*) from t_sys_para where lb_syspara="+lb_syspara+" and mc='"+mc+"'";
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
			jjd.setResult(false, "Syspara.getNextSyspara异常:" + e.toString());
		}
		return flag;

	}	

	public Map modi() {
		try {			
			NamedParameterJdbcTemplate npjt = this.getNpjtA();			
			npjt.update("update t_sys_para  set csz=:csz where csdm =:csdm",map);
			SysPara.updateSysPara(map.get("csdm").toString(),map.get("csz").toString());
			jjd.setResult(true, "修改成功");
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "修改参数值出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
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

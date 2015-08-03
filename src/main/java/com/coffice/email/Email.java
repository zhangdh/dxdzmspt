package com.coffice.email;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import com.coffice.jsry.Jsry;
import com.coffice.bean.PageBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Sql;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;


public class Email extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;
	public Email(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}

	public Email() {
	}

	/**
	 * 存草稿
	 * 
	 * @return
	 */
	public Map save() {
		try {
			map.put("guid", Guid.get());
			if ("mysql".equals(SysPara.getValue("db_type"))
					|| "sqlserver".equals(SysPara.getValue("db_type"))) {
				map.put("cjsj", getCurrentTime());
			} else if ("oracle".equals(SysPara.getValue("db_type"))) {
				map.put("cjsj", Sql.stringToTimestamp(getCurrentTime()));
			}
			map.put("yjzt_dm", 1203);// 1203草稿箱
			map.put("zt_dm", 1300);
			map.put("ly", 1205);
			map.put("yjnr",map.get("nr").toString());
			map.put("fjid",map.get("fjid").toString().replace("~",""));
			int ydhz = 0;
			this.getSji().withTableName("t_imail_mx").execute(map);
			//120 邮件模块代码
			Jsry.save( map.get("guid").toString(), "120", map.get(
					"sjr_value").toString());
			jjd.setResult(true, "保存成功");
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存邮件明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "保存邮件明细数据时出现错误，错误代码:" + guid);
		}
		return jjd.getData();
	}

	/**
	 * 发送
	 * 
	 * @return
	 */
	public Map send() {
		try {
			map.put("guid", Guid.get());
			if ("mysql".equals(SysPara.getValue("db_type"))
					|| "sqlserver".equals(SysPara.getValue("db_type"))) {
				map.put("cjsj", getCurrentTime());
			} else if ("oracle".equals(SysPara.getValue("db_type"))) {
				map.put("cjsj", Sql.stringToTimestamp(getCurrentTime()));
			}
			map.put("yjzt_dm", 1201);// 1203草稿箱
			map.put("zt_dm", 1300);
			map.put("ly", 1205);
			map.put("yjnr",map.get("nr").toString());
			int ydhz = 0;
			this.getSji().withTableName("t_imail_mx").execute(map);
			//120 邮件模块代码
			Jsry.save( map.get("guid").toString(), "120", map.get(
					"sjr_value").toString());
			jjd.setResult(true, "发送邮件成功");
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("send");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存发送数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "保存发送数据时出现错误，错误代码:" + guid);
			throw new ServiceException("保存imail_mx数据时异常");// 抛出此异常以触发回滚
		}
		return jjd.getData();
	}
	//删除收件箱
	public Map delFj() {
		try {
			String yjid = map.get("yjid")==null?"":String.valueOf(map.get("yjid"));
			if("".equals(yjid)){
				jjd.setResult(false,"邮件id为空");
			}else{
				this.getJtN().update("update t_imail_mx set yjzt_dm = '1202' where guid='"+yjid+"'");
			}
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("delFj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存发送数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "删除收件箱时出现错误，错误代码:" + guid);
		}
		return jjd.getData();
	}
	//删除收件箱
	public Map sendYj() {
		try {
			String yjid = map.get("yjid")==null?"":String.valueOf(map.get("yjid"));
			if("".equals(yjid)){
				jjd.setResult(false,"邮件id为空");
			}else{
				this.getJtN().update("update t_imail_mx set yjzt_dm = '1201' where guid='"+yjid+"'");
			}
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("sendYj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("草稿箱发件时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "发送邮件时出现错误，错误代码:" + guid);
		}
		return jjd.getData();
	}


	
	

	/**
	 * 发件箱显示
	 * 
	 * @return
	 */
	public Map fjxQuery() {
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlWhere = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		String sqlwhere = "", CountSQL = "";
		String yh = "";
		try {
			String dbtype = SysPara.getValue("db_type");
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
			if (map.get("cx_rq1") != null
					&& !map.get("cx_rq1").toString().equals("")) {
				if (dbtype.equals("oracle")) {
					sqlWhere.append(" and  a.cjsj>=to_date('").append(
							map.get("cx_rq1").toString()).append(
							" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sqlWhere.append(" and  a.cjsj>='").append(
							map.get("cx_rq1").toString()).append(" 00:00:00")
							.append("' ");
				}
			}
			if (map.get("cx_rq2") != null
					&& !map.get("cx_rq2").toString().equals("")) {
				if (dbtype.equals("oracle")) {
					sqlWhere.append(" and  a.cjsj<=to_date('").append(
							map.get("cx_rq2").toString()).append(
							" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sqlWhere.append(" and  a.cjsj<='").append(
							map.get("cx_rq2").toString()).append(" 23:59:59")
							.append("' ");
				}
			}

			sqlWhere.append(" and a.yhid ='").append(yhid).append("'");
			sqlStr.append("select guid,yjzt,cjsj,(case when yjzt_dm = '1201' then '已发送' when yjzt_dm = '1203' then '草稿箱' end ) zt from t_imail_mx a where yjzt_dm in ('1201','1203') ")
				  .append(sqlWhere.toString());
			sqlCount.append("select count(*) from t_imail_mx a where yjzt_dm in ('1201','1203') ").append(sqlWhere.toString());
			page.setSql(sqlStr.toString()+" order by a.cjsj desc");
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List<Map> _list = Db.getPageData(page);			
			jjd.setGrid("datalist", _list, page);
			jjd.setExtend("fjr", Cache.getUserInfo(yhid, "xm"));
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常6");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Imail.query异常:" + e.toString());
		}
		return jjd.getData();
	}

	/**
	 * 显示内容
	 * 
	 * @return
	 */

	public Map show() {
		try {
			String emailid = map.get("emailid")==null?"":String.valueOf( map.get("emailid"));
			if("".equals(emailid)){
				jjd.setResult(false,"邮件id为空");
			}
			Map _map = this.getJtN().queryForMap("select * from t_imail_mx where guid='"+emailid+"'");
			jjd.setForm(_map);
			List _list = this.getJtN().queryForList("select (select distinct(xm) from t_org_yh where yhid = a.fsfw_ry) mc,fsfw_ry dm from t_email_fsfw a where emailid='"+emailid+"'");
			jjd.setSelect("sjr", _list);
			if(_map.size() >0){
				jjd.setExtend("yjzt",_map.get("yjzt_dm").toString());
				String email_dir = SysPara.getValue("email_dir");
				String fjId = _map.get("fjid")==null?"":String.valueOf(_map.get("fjid"));
				if("".equals(fjId)){
					jjd.setExtend("iffj","0");
				}else{
					jjd.setExtend("iffj","1");
					_list = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where mk_dm='120' and zid='"+fjId+"' ");
					jjd.setSelect("fjs", _list);
					jjd.setExtend("email_dir", email_dir);
				}
			}
			String showFlag = map.get("showFlag")==null?"0":String.valueOf(map.get("showFlag"));
			if("1".equals(showFlag)){
				this.getJtN().update("update t_email_fsfw set ydcs = ydcs+1 where fsfw_ry ='"+yhid+"' and emailid='"+emailid+"'");
			}
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
	//删除收件箱
	public Map delSj() {
		try {
			String yjid = map.get("yjid")==null?"":String.valueOf(map.get("yjid"));
			if("".equals(yjid)){
				jjd.setResult(false,"邮件id为空");
			}else{
				this.getJtN().update("update t_email_fsfw set zt_dm = 0 where emailid='"+yjid+"' and fsfw_ry = '"+yhid+"'");
			}
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("delFj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除收件箱数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "删除收件箱时出现错误，错误代码:" + guid);
		}
		return jjd.getData();
	}

	public Map sjxQuery() {
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlWhere = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		try {
			String dbtype = SysPara.getValue("db_type");
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			if(map.get("page_size") != null){
				page.setPageSize(map.get("page_size").toString());
			}else{
				page.setPageSize("10");
			}
			if (map.get("cx_rq1") != null
					&& !map.get("cx_rq1").toString().equals("")) {
				if (dbtype.equals("oracle")) {
					sqlWhere.append(" and  a.cjsj>=to_date('").append(
							map.get("cx_rq1").toString()).append(
							" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sqlWhere.append(" and  a.cjsj>='").append(
							map.get("cx_rq1").toString()).append(" 00:00:00")
							.append("' ");
				}
			}
			if (map.get("cx_rq2") != null
					&& !map.get("cx_rq2").toString().equals("")) {
				if (dbtype.equals("oracle")) {
					sqlWhere.append(" and  a.cjsj<=to_date('").append(
							map.get("cx_rq2").toString()).append(
							" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				} else {
					sqlWhere.append(" and  a.cjsj<='").append(
							map.get("cx_rq2").toString()).append(" 23:59:59")
							.append("' ");
				}
			}
			if(map.get("ydzt") !=null && !map.get("ydzt").toString().equals("")){
				if(map.get("ydzt").toString().equals("0")){
					sqlWhere.append(" and b.ydcs =0 ");
				}else{
					sqlWhere.append(" and b.ydcs >0 ");
				}			
			}
			sqlWhere.append(" and b.fsfw_ry ='").append(yhid).append("' ");
			//sqlWhere.append(" order by a.cjsj desc");
			sqlStr.append("select a.guid,yjzt,a.cjsj,(select xm from t_org_yh where yhid = a.yhid) fsr,(case when b.ydcs=0 then '未读' else '已读' end )zt from t_imail_mx a,t_email_fsfw b where b.zt_dm =1 and a.guid = b.emailid and yjzt_dm = '1201' ")
				  .append(sqlWhere.toString());
			sqlCount.append("select count(*) from t_imail_mx a,t_email_fsfw b where b.zt_dm =1 and yjzt_dm='1201' ").append(sqlWhere.toString());
			page.setSql(sqlStr.toString()+" order by a.cjsj desc");
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List<Map> _list = Db.getPageData(page);			
			jjd.setGrid("datalist", _list, page);
			jjd.setExtend("fjr", Cache.getUserInfo(yhid, "xm"));
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询收件箱时出现异常6");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Imail.query异常:" + e.toString());
		}
		return jjd.getData();
	}
	//主页面显示未读邮件方法
	public Map mainList() {
		try {
			String page_size = map.get("page_size")==null?"10":String.valueOf(map.get("page_size"));
			String page_goto = map.get("page_goto")==null?"1":String.valueOf(map.get("page_goto"));
			PageBean page = new PageBean();
			page.setPageGoto(page_size);
			page.setPageGoto(page_goto);
			page.setSql(new StringBuffer().append("select a.guid,a.yjzt,b.cjsj from t_imail_mx a,t_email_fsfw b where a.guid = b.emailid and b.ydcs = 0 and b.fsfw_ry = '")
				                          .append(yhid).append("' order by cjsj desc").toString() );

			page.setCountSql(new StringBuffer().append("select count(*) from t_imail_mx a,t_email_fsfw b where a.guid = b.emailid and b.ydcs = 0 and b.fsfw_ry = '")
				                          .append(yhid).append("'").toString() );
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("desk_5_list", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("mainList");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("主页查询未读邮件时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Email.mainList:" + e.toString());
		}
		return jjd.getData();
	}
	public Map listDesk() {
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		try {
			PageBean page = new PageBean();
			if(map.get("page_num") != null){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			if(map.get("page_goto") != null){
				page.setPageGoto(map.get("page_goto").toString());
			}else{
				page.setPageGoto("1");
			}
			sqlStr.append("select a.guid,yjzt,a.cjsj,(select xm from t_org_yh where yhid = a.yhid) fsr from t_imail_mx a,t_email_fsfw b where b.zt_dm =1 and a.guid = b.emailid and yjzt_dm = '1201' ")
				  .append(" and b.fsfw_ry ='").append(yhid).append("'  ").append(" order by a.cjsj desc");;
			sqlCount.append("select count(*) from t_imail_mx a,t_email_fsfw b where b.zt_dm =1 and yjzt_dm='1201' ").append(" and b.fsfw_ry ='").append(yhid).append("'  ");
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List<Map> _list = Db.getPageData(page);	
			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("listDesk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("桌面查询收件箱时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Email.listDesk异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map sendCg() {
		try {
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("delete from t_imail_mx where guid = '"+guid+"'");
				this.getJtN().update("delete from t_email_fsfw where emailid = '"+guid+"'");
				send();
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("listDesk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("桌面查询收件箱时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Email.listDesk异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map saveCg() {
		try{
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
		if(!"".equals(guid)){
			this.getJtN().update("delete from t_imail_mx where guid = '"+guid+"'");
			this.getJtN().update("delete from t_email_fsfw where emailid = '"+guid+"'");
			save();
		}
		
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("saveCg");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存草稿件时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Email.saveCg异常:" + e.toString());
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

	private java.sql.Timestamp getCurTime(String sTime, String formatType) {
		java.sql.Timestamp dateTime = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatType,
					Locale.ENGLISH);
			format.setLenient(false);
			java.util.Date timeDate = format.parse(sTime);
			dateTime = new java.sql.Timestamp(timeDate.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateTime;
	}
}

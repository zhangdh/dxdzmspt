package com.coffice.hjzx.thxx;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.coffice.util.cache.Cache;


public class THXX extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String yhid;
	Map map;
	String sql;
	private Object request;
	private static String strOralceTime = "to_date({0},'yyyy-mm-dd hh24:mi:ss')";// oracle数据库格式化日期函数(精确到秒)
	private static String strOralceDate = "to_date({0},'yyyy-mm')";// oracle数据库格式化日期函数(精确到月)
	private static int database = 0;// 0:mysql 1:oracle 2:sqlserver
	String VdnId ;
	// 类加载时判断当前数据库版本
	static {
		try {
			if (SysPara.getValue("db_type").equals("mysql"))
				database = 0;
			else if (SysPara.getValue("db_type").equals("oracle"))
				database = 1;
			else if (SysPara.getValue("db_type").equals("sqlserver"))
				database = 2;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public THXX(){
		
	}
	public THXX(Map mapIn){
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		VdnId = (String)Cache.getUserInfo(yhid,"vdn");
		this.map = mapIn;
	}
	/**
	 * 通话信息列表
	 * @return
	 */
	public Map listTHXX(){
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer sqlCount=new StringBuffer();
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			List list = npjt.queryForList("select dm,mc from t_dm where dm>=8400 and dm<=8401 ",map);
			jjd.setSelect("cxthlb", list);
			
			sqlStr.append(" select a.*,b.mc as mc from t_hjzx_thxx a,t_dm b where a.thlb = b.dm order by a.calldate desc ");
			sqlCount.append("select count(*) from t_hjzx_thxx a ");
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("listTHXX");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示通话信息列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	/**
	 * 保存通话信息
	 * @return
	 */
	public Map saveTHXX(){
		NamedParameterJdbcTemplate npjt = this.getNpjtN();
		StringBuffer strSql=new StringBuffer();
		StringBuffer strSqlQuery=new StringBuffer();
		String caller=String.valueOf(map.get("caller"))== null?"":String.valueOf(map.get("caller"));
		String called=String.valueOf(map.get("called"))== null?"":String.valueOf(map.get("called"));
		String tempfilename=String.valueOf(map.get("tempfilename"))== null?"":String.valueOf(map.get("tempfilename"));
		String workNo=String.valueOf(map.get("workNo"))== null?"":String.valueOf(map.get("workNo"));
		String thlb=String.valueOf(map.get("thlb"))== null?"":String.valueOf(map.get("thlb"));
		String username=yhid;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		try {
					strSql.delete(0, strSql.length());//清空
					strSql.append("insert into t_hjzx_thxx(GUID,CALLER,CALLED,TEMPFILENAME,WORKNO,CALLDATE,THLB) ");
					strSql.append("values(").append("SEQ_CALLID.nextval").append(",'");
					strSql.append(caller).append("','").append(called).append("','");
					strSql.append(tempfilename).append("','").append(workNo).append("','");
					strSql.append(curTime).append("','");
					strSql.append(thlb).append("')");
					
					this.getJtN().update(strSql.toString());
					jjd.setExtend("result", "1");
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("saveTHXX");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存通话信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setExtend("result", "2");
		}
		return jjd.getData();
	}
	/**
	 * 查询通话信息
	 * @return
	 */
	public Map queryTHXX(){
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer sqlCount=new StringBuffer();
		StringBuffer sqlWhere=new StringBuffer();
		try {
			String tmp=null;
			tmp = (String)map.get("cx_zj");
			if(tmp!=null && !"".equals(tmp)){
				sqlWhere.append(" and a.caller like '%").append(tmp).append("%'");
			}
			tmp = (String)map.get("cx_bj");
			if(tmp!=null && !"".equals(tmp)){
				sqlWhere.append(" and a.called like '%").append(tmp).append("%'");
			}
			tmp = (String)map.get("cxthlb");
			if(tmp!=null && !"".equals(tmp)){
				sqlWhere.append(" and a.thlb like '%").append(tmp).append("%'");
			}
			sqlStr.append(" select a.*,b.mc as mc from t_hjzx_thxx a,t_dm b where a.thlb = b.dm ");
			sqlStr.append(sqlWhere.toString()).append(" order by a.calldate desc ");
			sqlCount.append(" select count(*) ");
			sqlCount.append(" from t_hjzx_thxx a where 1=1 ").append(sqlWhere.toString());
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryTHXX");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询通话信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	/**
	 * 查询此工号最近50条通话信息
	 * @return
	 */
	public Map queryGhList(){
		StringBuffer sqlStr=new StringBuffer();
		StringBuffer sqlCount=new StringBuffer();
		StringBuffer sqlWhere=new StringBuffer();
		try {
			String tmp=null;
			tmp = (String)map.get("workNo");
			if(tmp!=null && !"".equals(tmp)){
				sqlWhere.append(" and a.workno like '%").append(tmp).append("%'");
			}
			sqlStr.append(" select a.*,b.mc as mc from t_hjzx_thxx a,t_dm b where a.thlb = b.dm and rownum <=30 ");
			sqlStr.append(sqlWhere.toString()).append(" order by a.calldate desc ");
			sqlCount.append(" select count(*) ");
			sqlCount.append(" from t_hjzx_thxx a where 1=1 ").append(sqlWhere.toString());
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryTHXX");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询通话信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	/**
	 * 保存通话录音信息
	 * @return
	 */
	public Map saveReco(){
		StringBuffer sqlStr=new StringBuffer();
		try {
			String caller = map.get("caller")== null?"":String.valueOf(map.get("caller"));
			String called = map.get("called")== null?"":String.valueOf(map.get("called"));
			String flowstr = map.get("flowstr")== null?"":String.valueOf(map.get("flowstr"));
			String filename = map.get("filename")== null?"":String.valueOf(map.get("filename"));
			String agentnum = map.get("agentnum")== null?"":String.valueOf(map.get("agentnum"));
			String calltype = map.get("calltype")== null?"":String.valueOf(map.get("calltype"));
			sqlStr.append(" insert into t_reco (caller,called,flowstr,filename,agentnum,calltype,cjsj) values('");
			sqlStr.append(caller).append("','").append(called).append("','").append(flowstr).append("','");
			sqlStr.append(filename).append("','").append(agentnum).append("','").append(calltype).append("',GETDATE())");
			this.getJtN().update(sqlStr.toString());
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("saveReco");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存通话录音信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	/**
	 * 保存回访通话录音信息
	 * @return
	 */
	public Map saveHfReco(){
		System.out.println("------------------保存回访录音--------------");
		StringBuffer sqlStr=new StringBuffer();
		try {
			String caller = map.get("caller")== null?"":String.valueOf(map.get("caller"));
			String called = map.get("called")== null?"":String.valueOf(map.get("called"));
			String flowstr = map.get("flowstr")== null?"":String.valueOf(map.get("flowstr"));
			String filename = map.get("filename")== null?"":String.valueOf(map.get("filename"));
			String agentnum = map.get("agentnum")== null?"":String.valueOf(map.get("agentnum"));
			String calltype = map.get("calltype")== null?"":String.valueOf(map.get("calltype"));
			String ywid = map.get("ywid")== null?"":String.valueOf(map.get("ywid"));
			Map _map = this.getJtN().queryForMap("select entry_id from t_oswf_work_item where id= '"+ywid+"'");
			String entry_id = "";
			if(_map.size()>0){
				entry_id  = _map.get("entry_id")==null?"":String.valueOf(_map.get("entry_id"));
			}
			
			String slbh_day = "";
			if(!"".equals(entry_id)){
				_map = this.getJtN().queryForMap("select slbh_day,ldrq from t_form1305 where slbh='"+entry_id+"'");
				if(_map.size()>=0){
					slbh_day = _map.get("slbh_day")==null?"":String.valueOf(_map.get("slbh_day"));
				}				
			}
			
			sqlStr.append(" insert into t_hf_reco (caller,called,flowstr,filename,agentnum,calltype,cjsj,slbh,slbh_day,ywid) values('");
			sqlStr.append(caller).append("','").append(called).append("','").append(flowstr).append("','");
			sqlStr.append(filename).append("','").append(agentnum).append("','").append(calltype).append("',GETDATE(),'").append(entry_id);
			sqlStr.append("','").append(slbh_day).append("','").append(ywid).append("')");
			System.out.println("插入回访录音"+sqlStr.toString());
			this.getJtN().update(sqlStr.toString());
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("saveHfReco");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存回访通话录音信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	
	/**
	 * 查询回访通话录音信息
	 * @return
	 */
	public Map queryHfLy(){
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlWhere = new StringBuffer();
			String temp = "";
			sqlStr.append("select called,slbh_day,filename,cjsj from t_hf_reco where 1=1 ");
			temp = map.get("cx_hm")== null?"":String.valueOf(map.get("cx_hm"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and called like '%").append(temp).append("%' ");
			}
			temp =  map.get("cx_slbh")== null?"":String.valueOf(map.get("cx_slbh"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and slbh_day like '%").append(temp).append("%' ");
			}
			temp = map.get("cx_StartDate")== null?"":String.valueOf(map.get("cx_StartDate"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and cjsj >= '").append(temp).append("' ");
			}
			temp = map.get("cx_EndDate")== null?"":String.valueOf(map.get("cx_EndDate"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and cjsj <= '").append(temp).append(" 23:59:59' ");
			}
			sqlStr.append(sqlWhere);
			sqlCount.append("select count(*) from t_hf_reco where 1=1 ").append(sqlWhere);
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
			page.setSql(sqlStr.toString()+" order by cjsj desc");
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryHfLy");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询回访通话录音信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	
	/**
	 * 查询通话录音信息
	 * @return
	 */
	public Map queryLy(){
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlWhere = new StringBuffer();
			String temp = "";
			temp = map.get("cx_caller")== null?"":String.valueOf(map.get("cx_caller"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and caller like '%").append(temp).append("%' ");
			}
			temp = map.get("cx_called")== null?"":String.valueOf(map.get("cx_called"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and called like '%").append(temp).append("%' ");
			}
			temp = map.get("calltype")== null?"":String.valueOf(map.get("calltype"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and calltype = '").append(temp).append("' ");
			}
			temp = map.get("cx_StartDate")== null?"":String.valueOf(map.get("cx_StartDate"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and cjsj >='").append(temp).append("' ");
			}
			temp = map.get("cx_EndDate")== null?"":String.valueOf(map.get("cx_EndDate"));
			if(temp!=null && !"".equals(temp)){
				sqlWhere.append(" and cjsj <= '").append(temp).append("  23:59:59' ");
			}
			sqlStr.append("select caller,called, convert(varchar(120),cjsj,120) cjsj ,filename,(case when calltype=1 then '呼入' else '呼出' end) calltype from t_reco where 1=1 ").append(sqlWhere);
			sqlCount.append("select count(*) from t_reco where 1=1 ").append(sqlWhere);
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
			page.setSql(sqlStr.toString()+" order by cjsj desc");
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryHfLy");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询通话录音信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	/**
	 * 查询录音路径
	 * @return
	 */
	public Map queryLyPath(){
		try {
			String id = map.get("id")==null?"":String.valueOf(map.get("id"));
			if(!"".equals(id)){
				Map _map = this.getJtN().queryForMap("select lypath from t_form1305 where id='"+id+"'");
				_map = this.getJtN().queryForMap("select filename from t_reco where flowstr='"+_map.get("lypath")+"'");
				jjd.setExtend("lypath",_map.get("filename"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryLyPath");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询录音路径时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map queryLyPathById(){
		try {
			String id = map.get("id")==null?"":String.valueOf(map.get("id"));
			if(!"".equals(id)){
				Map _map = this.getJtN().queryForMap("select lypath from t_form1305 where id='"+id+"'");
				_map = this.getJtN().queryForMap("select filename from t_reco where flowstr='"+id+"'");
				jjd.setExtend("lypath",_map.get("filename"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryLyPath");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询录音路径时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
}

package com.coffice.hjzx.fwqq;

import java.util.ArrayList;
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
import com.coffice.util.fusionchart.DataSet;
import com.coffice.util.fusionchart.single.ColumnChart;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;


public class Fwqq extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Fwqq(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		zzid = (String) mapIn.get("zzid");
		bmid = (String) mapIn.get("bmid");
		gwid = (String) mapIn.get("gwid");
		yhid = (String) mapIn.get("yhid");
		this.map = mapIn;
	}	
  	/**
	 * 查询服务请求列表
	 * 
	 * @return
	 */
	public Map queryFwqq() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String dhType = SysPara.getDbType();
			String temp = "";
			temp = map.get("ldhm")==null?"":String.valueOf(map.get("ldhm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and lxhm like '%").append(temp).append("%'");
			}
			temp = map.get("cxldhm")==null?"":String.valueOf(map.get("cxldhm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and lxhm like '%").append(temp).append("%'");
			}
			temp = map.get("cxlxrxm")==null?"":String.valueOf(map.get("cxlxrxm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrxm like '%").append(temp).append("%'");
			}
			temp = map.get("cjsjq")==null?"":String.valueOf(map.get("cjsjq"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj > to_date('").append(temp).append("','yyyy-mm-dd') ");
				}else{
					sqlWhere.append(" and cjsj > '").append(temp).append("' ");
				}
			}
			temp = map.get("cjsjz")==null?"":String.valueOf(map.get("cjsjz"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj < to_date('").append(temp).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				}else{
					sqlWhere.append(" and cjsj < '").append(temp).append("' ");
				}
				
			}
			sqlStr.append("select guid,bt,ldrxm,lxhm,(select mc from t_dm where dm = a.lb_dm) nrlb,cblb_dm,formid,entryid,tablename,(select mc from t_dm where dm = a.cblb_dm) cblx,")
				  .append("(select xm from t_org_yh where yhid = a.yhid) clr, cjsj,lypath,(case when ifEnd=1 then '已完成' else '未完成' end) sfbj,ifEnd from t_hjzx_fwqq a where yxbz = 1  ")
				  .append(sqlWhere);
			sqlCount.append("select count(*) from t_hjzx_fwqq where yxbz = 1  ").append(sqlWhere);
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
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
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.queryFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map queryGd() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String dhType = SysPara.getDbType();
			String temp = "";
			temp = map.get("cxldhm")==null?"":String.valueOf(map.get("cxldhm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and lxhm like '%").append(temp).append("%'");
			}
			temp = map.get("zt")==null?"":String.valueOf(map.get("zt"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ifEnd = '").append(temp).append("'");
			}
			temp = map.get("cxlxrxm")==null?"":String.valueOf(map.get("cxlxrxm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrxm like '%").append(temp).append("%'");
			}
			temp = map.get("cj_sjq")==null?"":String.valueOf(map.get("cj_sjq"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj > to_date('").append(temp).append("','yyyy-mm-dd') ");
				}else{
					sqlWhere.append(" and cjsj > '").append(temp).append("' ");
				}
				
			}
			temp = map.get("cj_sjz")==null?"":String.valueOf(map.get("cj_sjz"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj < to_date('").append(temp).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				}else{
					sqlWhere.append(" and cjsj < '").append(temp).append("' ");
				}
				
			}			
			sqlStr.append("select guid,bt,ldrxm,lxhm,(select mc from t_dm where dm = a.lb_dm) nrlb,(case when ifEnd=1 then '已办结' else '未办结' end ) zt ")
				  .append(",(select xm from t_org_yh where yhid = a.yhid) clr,SUBSTRING(convert(varchar(100),cjsj,120) ,6,11) cjsj from t_hjzx_fwqq a where yxbz = 1  and cblb_dm <> '94063' ")
				  .append(sqlWhere);
			sqlCount.append("select count(*) from t_hjzx_fwqq where yxbz = 1 and cblb_dm <> '94063' ").append(sqlWhere);
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
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
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.queryFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map queryFwqqCl() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String dhType = SysPara.getDbType();
			String temp = "";
			temp = map.get("cxldhm")==null?"":String.valueOf(map.get("cxldhm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and lxhm like '%").append(temp).append("%'");
			}
			temp = map.get("zt")==null?"":String.valueOf(map.get("zt"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ifEnd = '").append(temp).append("'");
			}
			temp = map.get("cxlxrxm")==null?"":String.valueOf(map.get("cxlxrxm"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrxm like '%").append(temp).append("%'");
			}
			temp = map.get("cj_sjq")==null?"":String.valueOf(map.get("cj_sjq"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj > to_date('").append(temp).append("','yyyy-mm-dd') ");
				}else{
					sqlWhere.append(" and cjsj > '").append(temp).append("' ");
				}
				
			}
			temp = map.get("cj_sjz")==null?"":String.valueOf(map.get("cj_sjz"));
			if(!"".equals(temp)){
				if("oracle".equals(dhType)){
					sqlWhere.append(" and cjsj < to_date('").append(temp).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
				}else{
					sqlWhere.append(" and cjsj < '").append(temp).append("' ");
				}
				
			}			
			sqlStr.append("select guid,bt,ldrxm,lxhm,(select mc from t_dm where dm = a.lb_dm) nrlb,(case when ifEnd=1 then '已办结' else '未办结' end ) zt ")
				  .append(",(select xm from t_org_yh where yhid = a.yhid) clr,SUBSTRING(convert(varchar(100),cjsj,120) ,6,11) cjsj from t_hjzx_fwqq a where yxbz = 1  and cblb_dm <> '94063' ")
				  .append(" and ifEnd = 0 and yhid = '").append(yhid).append("' ")
				  .append(sqlWhere);
			sqlCount.append("select count(*) from t_hjzx_fwqq where yxbz = 1 and cblb_dm <> '94063' ").append(" and ifEnd = 0 and yhid = '").append(yhid).append("' ").append(sqlWhere);
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
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
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询服务请求待办数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.queryFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map saveFwqq(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			int yxbz = 1;
			int ifEnd = 1;
			if(((String)map.get("cblb_dm")).equals("94063")){
					yxbz = 0;
			}
			if(((String)map.get("end")).equals("0")){
				ifEnd = 0;
			}
			String guid = Guid.get();
			String ldrdz = new StringBuffer().append((String)map.get("sfT")).append((String)map.get("dsT"))
											 .append((String)map.get("xqT")).append((String)map.get("xzT"))
											 .append((String)map.get("xxdz")).toString();
			sqlStr.append("insert into t_hjzx_fwqq(zzid,bmid,gwid,yhid,guid,ldrxm,ldrdz,sfdz,ldhm,lxhm,bt,nr,clr,cljg,cblb_dm,lb_dm,sfbm,xzfl,xxly,lypath,cjsj,yxbz,ifEnd,sf,ds,xq,xz,xxdz,wxlx)values('")
				  .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','").append(yhid)
				  .append("','").append(guid).append("','").append((String)map.get("ldrxm")).append("','")
				  .append(ldrdz).append("','").append((String)map.get("sfdz")).append("','")
				  .append((String)map.get("ldhm")).append("','").append((String)map.get("lxhm")).append("','")
				  .append((String)map.get("bt")).append("','").append((String)map.get("nr")).append("','")
				  .append(yhid).append("','").append((String)map.get("cljg")).append("','")
				  .append((String)map.get("cblb_dm")).append("','").append((String)map.get("lb_dm")).append("','")
				  .append((String)map.get("sfbm")).append("','").append((String)map.get("xzfl")).append("','")
				  .append((String)map.get("xxly")).append("','").append((String)map.get("lypath")).append("',")
				  .append(getDateStr()).append(",").append(yxbz).append(",").append(ifEnd).append(",'")
				  .append((String)map.get("sf")).append("','").append((String)map.get("ds")).append("','")
				  .append((String)map.get("xq")).append("','").append((String)map.get("xz")).append("','")
				  .append((String)map.get("xxdz")).append("','").append((String)map.get("wxlx")).append("')");
			this.getJtN().update(sqlStr.toString());
			jjd.setExtend("fwqqid",guid);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.saveFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map zbFwqq(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			String fwqqid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			this.getJtN().update("update t_hjzx_fwqq set yxbz = 0 where guid = '"+fwqqid+"'");
			int yxbz = 1;
			int ifEnd = 0;
			if(((String)map.get("cblb_dm")).equals("94063")){
					yxbz = 0;
			}
			
			String guid = Guid.get();
			sqlStr.append("insert into t_hjzx_fwqq(zzid,bmid,gwid,yhid,guid,ldrxm,ldrdz,sfdz,ldhm,lxhm,bt,nr,clr,cljg,cblb_dm,lb_dm,sfbm,xzfl,xxly,lypath,cjsj,yxbz,ifEnd,sf,ds,xq,xz,xxdz,wxlx)values('")
				  .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','").append(yhid)
				  .append("','").append(guid).append("','").append((String)map.get("ldrxm")).append("','")
				  .append((String)map.get("ldrdz")).append("','").append((String)map.get("sfdz")).append("','")
				  .append((String)map.get("ldhm")).append("','").append((String)map.get("lxhm")).append("','")
				  .append((String)map.get("bt")).append("','").append((String)map.get("nr")).append("','")
				  .append(yhid).append("','").append((String)map.get("cljg")).append("','")
				  .append((String)map.get("cblb_dm")).append("','").append((String)map.get("lb_dm")).append("','")
				  .append((String)map.get("sfbm")).append("','").append((String)map.get("xzfl")).append("','")
				  .append((String)map.get("xxly")).append("','").append((String)map.get("lypath")).append("',")
				  .append(getDateStr()).append(",").append(yxbz).append(",").append(ifEnd).append(",'")
				  .append((String)map.get("sf")).append("','").append((String)map.get("ds")).append("','")
				  .append((String)map.get("xq")).append("','").append((String)map.get("xz")).append("','")
				  .append((String)map.get("xxdz")).append("','").append((String)map.get("wxlx")).append("')");
			this.getJtN().update(sqlStr.toString());
			jjd.setExtend("fwqqid",guid);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("转办服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.saveFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map modiFwqq(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			int ifEnd = 1;
			if(((String)map.get("end")).equals("0")){
				ifEnd = 0;
			}
			sqlStr.append("update t_hjzx_fwqq set bt='").append((String)map.get("bt")).append("',nr='")
				  .append((String)map.get("nr")).append("',cljg='").append((String)map.get("cljg")).append("',ifEnd=").append(ifEnd).append(" where guid='").append((String)map.get("guid"))
				  .append("'");
			this.getJtN().update(sqlStr.toString());
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("modiFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("修改服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.modiFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map showFwqq(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				Map _map = this.getJtN().queryForMap("select * from t_hjzx_fwqq where guid = '"+guid+"'");
				jjd.setExtend("ifEnd",_map.get("ifEnd"));
				jjd.setForm(_map);
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.showFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map init(){
		try{
			List nrlbList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='4017' order by xh desc ");
			jjd.setSelect("lb_dm",nrlbList);
			List cblxList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='4016' order by xh desc ");
			jjd.setSelect("cblb_dm",cblxList);
			List xzflList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2005' order by xh desc ");
			jjd.setSelect("xzfl",xzflList);
			List xxlyList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2006' order by xh desc ");
			jjd.setSelect("xxly",xxlyList);
			List sfbmList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2001' order by xh desc ");
			jjd.setSelect("sfbm",sfbmList);
			List wxlxList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='6004' order by xh desc ");
			jjd.setSelect("wxlx",wxlxList);
			List xqList = this.getJtN().queryForList("select xzqh_dm dm,xzqh_mc mc from t_dm_xzqh where sj_xzqh_dm = '620800' order by xzqh_dm  ");
			jjd.setSelect("xq",xqList);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("init");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("初始化服务请求页面时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.init异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map getQH(){
		try{
			String dm = map.get("dm")==null?"":String.valueOf(map.get("dm"));
			if("xz".equals(dm)){
				String xqDm = map.get("xqDm")==null?"":String.valueOf(map.get("xqDm"));
				if(!"".equals(xqDm)){
					List xzList = this.getJtN().queryForList("select xzqh_dm dm,xzqh_mc mc from t_dm_xzqh where sj_xzqh_dm = '"+xqDm+"' order by xzqh_dm  ");
					jjd.setSelect("xz",xzList);
				}
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("init");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("初始化服务请求页面时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.init异常:" + e.toString());
		}
		return jjd.getData();
	}
	/**
	 * 服务请求统计结果
	 * 
	 * @return
	 */
	public Map tjFwqq() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String tmp="";
			tmp = String.valueOf(map.get("cx_sjq"))!=null?String.valueOf(map.get("cx_sjq")):"";
			if(!"".equals(tmp)&& tmp!=null & !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(cjsj,'YYYY-MM-DD') >='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and cjsj >='").append(tmp).append("'");
				}
			}
			tmp = String.valueOf(map.get("cx_sjz"))!=null?String.valueOf(map.get("cx_sjz")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(cjsj,'YYYY-MM-DD') <='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and cjsj<='").append(tmp).append(" 23:59:59'");
				}
			}
			tmp = String.valueOf(map.get("tjtj"))!=null?String.valueOf(map.get("tjtj")):"";
			if("tjzx".equals(tmp)){
				sqlStr.append("select (select xm from t_org_yh where yhid=a.yhid) as qy,count(yhid) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by yhid ");
			}else if("tjnrlb".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.lb_dm) as qy,count(lb_dm) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by lb_dm ");
			}else if("tjxxly".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.xxly) as qy,count(xxly) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by xxly ");
			}else if("tjxzfl".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.xzfl) as qy,count(xzfl) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by xzfl ");
			}else{
				sqlStr.append("select (select mc from t_dm where dm=a.cblb_dm) as qy,count(cblb_dm) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by cblb_dm ");
			}
			List _list = npjt.queryForList(sqlStr.toString(),map);
			PageBean page = new PageBean();
			page.setPage_allCount(1000);
			page.setPageSize("1000");
			page.setPage_allPage(100);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("tjFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.tjFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	/**
	 * 服务请求统计结果
	 * 
	 * @return
	 */
	public Map tjFwqqChart() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String tmp="";
			tmp = String.valueOf(map.get("cx_sjq"))!=null?String.valueOf(map.get("cx_sjq")):"";
			if(!"".equals(tmp)&& tmp!=null & !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(cjsj,'YYYY-MM-DD') >='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and cjsj >='").append(tmp).append("'");
				}
			}
			tmp = String.valueOf(map.get("cx_sjz"))!=null?String.valueOf(map.get("cx_sjz")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(cjsj,'YYYY-MM-DD') <='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and cjsj<='").append(tmp).append(" 23:59:59'");
				}
			}
			tmp = String.valueOf(map.get("tjtj"))!=null?String.valueOf(map.get("tjtj")):"";
			if("tjzx".equals(tmp)){
				sqlStr.append("select (select xm from t_org_yh where yhid=a.yhid) as qy,count(yhid) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by yhid ");
			}else if("tjnrlb".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.lb_dm) as qy,count(lb_dm) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by lb_dm ");
			}else if("tjxxly".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.xxly) as qy,count(xxly) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by xxly ");
			}else if("tjxzfl".equals(tmp)){
				sqlStr.append("select (select mc from t_dm where dm=a.xzfl) as qy,count(xzfl) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by xzfl ");
			}else{
				sqlStr.append("select (select mc from t_dm where dm=a.cblb_dm) as qy,count(cblb_dm) as zs from t_hjzx_fwqq a where yxbz=1 ");
				sqlStr.append(sqlWhere.toString()).append(" group by cblb_dm ");
			}
			List _list = npjt.queryForList(sqlStr.toString(),map);
			ColumnChart columnChart = new ColumnChart("来电服务请求统计柱形显示表","","");	
			List xmlDataList = new ArrayList();
		    for(int i=0;i<_list.size();i++){
		    	Map m = (Map) _list.get(i);
		    	DataSet dataSet = new DataSet(m.get("qy").toString(),m.get("zs").toString());
		    	xmlDataList.add(dataSet);
		    }
		    columnChart.setDataList(xmlDataList);
		    System.out.println(columnChart.dataXml());
		    jjd.setExtend("xmlStr", columnChart.dataXml());//初始化柱状图
		    
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("tjFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.tjFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map openZb() {
		String username=yhid;
		String wkName="";
		String mk_dm="";
		String stepId="";
		try {
			String businessid=String.valueOf(map.get("id"));
			int entryId=Integer.parseInt(String.valueOf(map.get("entryid")));
			String formId = map.get("formid")==null?"":String.valueOf(map.get("formid"));
			String tablename = map.get("tablename")==null?"":String.valueOf(map.get("tablename"));
			String templateName = "";
			if(!"".equals(tablename)){
				templateName = tablename+".ftl";
			}
			Workflow wf = new BasicWorkflow(username);
			wkName=wf.getWorkflowName(entryId);
			WorkFlowMethod wfMethod=new WorkFlowMethod();
		    //表单id
		    //功能列表
		    map.put("id", entryId);
		    map.put("templateName", templateName);//模板名称
		    map.put("formId", formId);//表单id
		    map.put("stepId", stepId);
		    map.put("wkName", wkName);
			map.put("mk_dm", mk_dm);//模块代码
			map.put("businessid", businessid);//业务id
			List _list = this.getJtN().queryForList("select * from t_msg where ywid='"+String.valueOf(entryId)+"'");
			if(_list.size()>0){
				map.put("ifLy","1");
				map.put("Lys", _list.size());
			}
			map.putAll(getFiles(entryId));
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("showDocumentInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示待阅时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}
	public Map getFiles(long id){
		Map _map = new HashMap();
		String zid = String.valueOf(id);
		try{
			String mk_mc = "form";			
			if(!"".equals(zid)){
				String mk_dir = SysPara.getValue(mk_mc+"_dir");
				jjd.setExtend("mk_dir", mk_dir);
				String fjStr = "[";
				List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+zid+"'");
				for(int i=0;i<fjList.size();i++){
					Map fjMap = (Map)fjList.get(i);
					fjStr = fjStr+"{wjmc:'"+fjMap.get("wjmc")+"',wjlj:'"+fjMap.get("wjlj")+"'},";
				}
				fjStr = fjStr.substring(0,fjStr.length()-1);
				fjStr +="]";
				_map.put("fjStr",fjStr);
				if(fjList.size()>0){
					_map.put("ifFj","1");
					_map.put("mk_dir",SysPara.getValue("form_dir"));
				}else{
					_map.put("ifFj","0");
				}
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"查询附件信息");
		}
			return _map;
		}

	public Map queryAllLED(){
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select top 100 substring(convert(varchar(100),cjsj,120),1,16) cjsj, (select xm from t_org_yh where yhid=fwqq.yhid)slr,lxhm,bt,(select mc from t_dm where dm=fwqq.cblb_dm)zt_dm,SUBSTRING(nr,0,30) nr,(select mc from t_dm where dm=fwqq.xzfl) xzfl,(select mc from t_dm where dm=fwqq.xxly) xxly from (");
		sqlStr.append("select * from t_hjzx_fwqq where entryid is null and yxbz=1 ");
		sqlStr.append(" union all ");
		sqlStr.append("select * from t_hjzx_fwqq a where entryid is not null and yxbz=1 and exists  (select *  from t_oswf_work_item b where a.entryid = b.Entry_ID) ");
		sqlStr.append(") fwqq order by cjsj desc ");
		//List resList=this.getJtN().queryForList("select cjsj, (select xm from t_org_yh where yhid=fwqq.yhid)slr,hfhm,title,(select mc from t_dm where dm=fwqq.zt_dm)zt_dm,SUBSTRING(nr,0,30) nr,(select mc from t_dm where dm=fwqq.xzfl) xzfl,(select mc from t_dm where dm=fwqq.xxly) xxly from t_hjzx_fwqq fwqq where yxbz=1");
		List resList=this.getJtN().queryForList(sqlStr.toString());
		String tableStr="";
		if(resList.size()>0){
			tableStr="<table  class='c_table_list' >";
			String tempStr="";
			for(int s=0;s<resList.size();s++){
				Map dataMap=(Map)resList.get(s);
				if (s%2==0){
					tempStr+="<tr><td width='8%' align='center'>"+dataMap.get("slr")+"</td><td width='15%' nowrap='nowrap' align='center'>"+dataMap.get("cjsj")+"</td><td width='10%' align='center'>"+dataMap.get("hfhm")+"</td><td width='10%' align='center'>"+dataMap.get("bt")+"</td><td width='8%' align='center'>"+dataMap.get("zt_dm")+"</td><td width='33%' align='center' >"+dataMap.get("nr")+"</td><td width='8%' align='center'>"+dataMap.get("xzfl")+"</td><td width='8%' align='center'>"+dataMap.get("xxly")+"</td></tr>";
				}else{
					tempStr+="<tr style='background-color:#BFD6F8'><td width='8%' align='center'>"+dataMap.get("slr")+"</td><td width='15%' nowrap='nowrap' align='center'>"+dataMap.get("cjsj")+"</td><td width='10%' align='center'>"+dataMap.get("lxhm")+"</td><td width='10%' align='center'>"+dataMap.get("title")+"</td><td width='8%' align='center'>"+dataMap.get("zt_dm")+"</td><td width='33%' align='center' >"+dataMap.get("nr")+"</td><td width='8%' align='center'>"+dataMap.get("xzfl")+"</td><td width='8%' align='center'>"+dataMap.get("xxly")+"</td></tr>";
				}
			}
			tableStr+=tempStr+"</table>";
		}
		jjd.setExtend("res", tableStr);
		return jjd.getData();
	}
}

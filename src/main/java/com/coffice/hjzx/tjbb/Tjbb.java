package com.coffice.hjzx.tjbb;

import java.util.ArrayList;
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
import com.coffice.util.fusionchart.multiseries.ColumnChartM;

public class Tjbb extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Tjbb(Map mapIn) {
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
	public Map blqk() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String cxfw = map.get("cx_bm")==null?"":String.valueOf(map.get("cx_bm"));
			if(!"".equals(cxfw)){
				sqlWhere.append(" and bmid='").append(cxfw).append("'");
			}
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			if(!"".equals(cx_sjq) && !"".equals(cx_sjz)){
				sqlWhere.append(" and zbsj is not null and zbsj >'").append(cx_sjq).append("' and zbsj <'").append(cx_sjz).append("' ");
			}
			sqlStr.append(" select (select mc from t_org_bm where bmid = a.bmid) bmmc,")
				  .append("COUNT(*) sjzs,SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()+1000) then 1 else 0 end ) asbj,")
				  .append("SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()-1000) then 1 else 0 end ) csbj,")
				  .append("SUM(case when bjbz = 0 then 1 else 0 end ) wbj,SUM(case when bjbz = 0 and ISNULL(clqx,getDate()+1) < getDate() then 1 else 0 end ) cswbj,")
				  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
				  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
				  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
				  .append("SUM(case when a.dfldr = '200201' then 1 else 0 end ) hf ,")
				  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf,")
				  .append("SUM(case when wfhfqk is not null then 1 else 0 end )wfhf,")
				  .append("SUM(case when zxhf_blzt IS NULL and zxhf_myd IS  NULL and zxhf_fwtd IS  NULL and sfgzdsr IS  NULL then 1 else 0 end)whf,")
				  .append("SUM(case when zxhf_blzt <> blzt then 1 else 0 end ) yzy,SUM(case when curstepid = 59 then 1 else 0 end ) dhf,")
				  .append("SUM(case when curstepid = 4 then 1 else 0 end ) sj,SUM(case when iscb = 1 then 1 else 0 end ) cbgd,SUM(case when issqyq = 1 then 1 else 0 end ) sqyq ")
				  .append(" from t_cbd_sync a where bmid is not null ").append(sqlWhere.toString()).append(" group by bmid  ");
			sqlStr.append(" union all ");
			sqlStr.append(" select (select xm from t_org_yh where yhid = a.clrid) bmmc,")
			  .append("COUNT(*) sjzs,SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()+1000) then 1 else 0 end ) asbj,")
			  .append("SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()-1000) then 1 else 0 end ) csbj,")
			  .append("SUM(case when bjbz = 0 then 1 else 0 end ) wbj,SUM(case when bjbz = 0 and ISNULL(clqx,getDate()+1) < getDate() then 1 else 0 end ) cswbj,")
			  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
			  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
			  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
			  .append("SUM(case when a.dfldr = '200201' then 1 else 0 end ) hf ,")
			  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf,")
			  .append("SUM(case when wfhfqk is not null then 1 else 0 end )wfhf,")
			  .append("SUM(case when zxhf_blzt IS NULL and zxhf_myd IS  NULL and zxhf_fwtd IS  NULL and sfgzdsr IS  NULL then 1 else 0 end)whf,")
			  .append("SUM(case when zxhf_blzt <> blzt then 1 else 0 end ) yzy,SUM(case when curstepid = 59 then 1 else 0 end ) dhf,")
			  .append("SUM(case when curstepid = 4 then 1 else 0 end ) sj,SUM(case when iscb = 1 then 1 else 0 end ) cbgd,SUM(case when issqyq = 1 then 1 else 0 end ) sqyq ")
			  .append(" from t_cbd_sync a where bmid is not null ").append(sqlWhere.toString()).append(" group by clrid    ");
			PageBean page = new PageBean();
			page.setCountSql("select count(*) from ("+sqlStr+" )");
			page.setSql(sqlStr.toString());
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
			logItem.setMethod("blqk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询承办单位办理情况统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.blqk异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map xzBlqk() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			bmid = (String)Cache.getUserInfo(yhid,"bmid");
			sqlWhere.append(" and bmid = '").append(bmid).append("'");
			String cxfw = map.get("cx_lx")==null?"":String.valueOf(map.get("cx_lx"));
			if(!"".equals(cxfw)){
				sqlWhere.append(" and clrid in (select lxid from t_org_tree where lx_dm = '204' and sjid ='").append(cxfw).append("') ");
			}
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			if(!"".equals(cx_sjq) && !"".equals(cx_sjz)){
				sqlWhere.append(" and zbsj is not null and zbsj >'").append(cx_sjq).append("' and zbsj <'").append(cx_sjz).append("' ");
			}
			sqlStr.append(" select '合计' bmmc,")
				  .append("COUNT(*) sjzs,SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()+1000) then 1 else 0 end ) asbj,")
				  .append("SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()-1000) then 1 else 0 end ) csbj,")
				  .append("SUM(case when bjbz = 0 then 1 else 0 end ) wbj,SUM(case when bjbz = 0 and ISNULL(clqx,getDate()+1) < getDate() then 1 else 0 end ) cswbj,")
				  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
				  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
				  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
				  .append("SUM(case when a.dfldr = '200201' then 1 else 0 end ) hf ,")
				  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf,")
				  .append("SUM(case when wfhfqk is not null then 1 else 0 end )wfhf,")
				  .append("SUM(case when zxhf_blzt IS NULL and zxhf_myd IS  NULL and zxhf_fwtd IS  NULL and sfgzdsr IS  NULL then 1 else 0 end)whf,")
				  .append("SUM(case when zxhf_blzt <> blzt then 1 else 0 end ) yzy,SUM(case when curstepid = 59 then 1 else 0 end ) dhf,")
				  .append("SUM(case when curstepid = 4 then 1 else 0 end ) sj,SUM(case when iscb = 1 then 1 else 0 end ) cbgd,SUM(case when issqyq = 1 then 1 else 0 end ) sqyq ")
				  .append(" from t_cbd_sync a where 1=1 ").append(sqlWhere.toString());
			sqlStr.append(" union all ");
			sqlStr.append(" select (select xm from t_org_yh where yhid = a.clrid) bmmc,")
			  .append("COUNT(*) sjzs,SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()+1000) then 1 else 0 end ) asbj,")
			  .append("SUM(case when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()-1000) then 1 else 0 end ) csbj,")
			  .append("SUM(case when bjbz = 0 then 1 else 0 end ) wbj,SUM(case when bjbz = 0 and ISNULL(clqx,getDate()+1) < getDate() then 1 else 0 end ) cswbj,")
			  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
			  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
			  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
			  .append("SUM(case when a.dfldr = '200201' then 1 else 0 end ) hf ,")
			  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf,")
			  .append("SUM(case when wfhfqk is not null then 1 else 0 end )wfhf,")
			  .append("SUM(case when zxhf_blzt IS NULL and zxhf_myd IS  NULL and zxhf_fwtd IS  NULL and sfgzdsr IS  NULL then 1 else 0 end)whf,")
			  .append("SUM(case when zxhf_blzt <> blzt then 1 else 0 end ) yzy,SUM(case when curstepid = 59 then 1 else 0 end ) dhf,")
			  .append("SUM(case when curstepid = 4 then 1 else 0 end ) sj,SUM(case when iscb = 1 then 1 else 0 end ) cbgd,SUM(case when issqyq = 1 then 1 else 0 end ) sqyq ")
			  .append(" from t_cbd_sync a where bmid is not null ").append(sqlWhere.toString()).append(" group by clrid    ");
			PageBean page = new PageBean();
			page.setCountSql("select count(*) from ("+sqlStr+" )");
			page.setSql(sqlStr.toString());
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
			logItem.setMethod("XzBlqk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询乡镇承办单位办理情况统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.XzBlqk异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map initXzBlqk() {
		try {
			bmid = (String) Cache.getUserInfo(yhid,"bmid");
			List _list = Db.getJtN().queryForList("select mc,guid dm from t_org_tree where  lx_dm = '202' and sjbmid ='"+bmid+"'");
			jjd.setSelect("cx_lx",_list);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("initXzBlqk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询乡镇承办单位办理情况初始化时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "tjbb.initXzBlqk异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map ldsj() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			sqlWhere.append(" and cjsj >'").append(cx_sjq).append("' and cjsj <'").append(cx_sjz).append(" 23:59:59'");
			if("nrfl".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sll,")
					  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
					  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq a ")
					  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by lb_dm ");
				sqlCount.append("select count(distinct(lb_dm)) from t_hjzx_fwqq where 1=1 ")
						.append(sqlWhere.toString());
			}else if("xzfl".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.xzfl) lbmc,count(*) sll,")
				  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq  a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by xzfl ");
				sqlCount.append("select count(distinct(xzfl)) from t_hjzx_fwqq where 1=1 ")
					.append(sqlWhere.toString());
			}else if("xxly".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.xxly) lbmc,count(*) sll,")
				  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by xxly ");
				sqlCount.append("select count(distinct(xxly)) from t_hjzx_fwqq where 1=1 ")
					.append(sqlWhere.toString());
			}else{
				PageBean page = new PageBean();
				List _list = new ArrayList();
	  			jjd.setGrid("datalist", _list, page);
	  			return jjd.getData();
			}
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString());
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
  			return jjd.getData();
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("ldsj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询来电数据统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.ldsj异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map ldsjChart() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			sqlWhere.append(" and cjsj >'").append(cx_sjq).append("' and cjsj <'").append(cx_sjz).append(" 23:59:59'");
			if("nrfl".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sll,")
					  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
					  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq a ")
					  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by lb_dm ");
			}else if("xzfl".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.xzfl) lbmc,count(*) sll,")
				  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq  a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by xzfl ");
			}else if("xxly".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = a.xxly) lbmc,count(*) sll,")
				  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end )bjsl,0 yxl,0 bjl from t_hjzx_fwqq a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by xxly ");
			}
			List _list = Db.getJtN().queryForList(sqlStr.toString());
			ColumnChartM column = new ColumnChartM(_list,"lbmc");
			column.dataSet(_list,"sll","受理量");
			column.dataSet(_list,"yxsl","有效数量");
			column.dataSet(_list,"bjsl","当前办结数量");
			jjd.setExtend("xmlStr",column.getXml());
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("ldsjChart");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询来电数据图表显示时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.ldsjChart异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map compare() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere1=new StringBuffer();
			StringBuffer sqlWhere2=new StringBuffer();
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			String cx_sjq1 = map.get("cx_sjq1")==null?"":String.valueOf(map.get("cx_sjq1"));
			String cx_sjz1 = map.get("cx_sjz1")==null?"":String.valueOf(map.get("cx_sjz1"));
			String cx_sjq2 = map.get("cx_sjq2")==null?"":String.valueOf(map.get("cx_sjq2"));
			String cx_sjz2 = map.get("cx_sjz2")==null?"":String.valueOf(map.get("cx_sjz2"));
			sqlWhere1.append(" and cjsj >'").append(cx_sjq1).append("' and cjsj <'").append(cx_sjz1).append(" 23:59:59'");
			sqlWhere2.append(" and cjsj >'").append(cx_sjq2).append("' and cjsj <'").append(cx_sjz2).append(" 23:59:59'");
			List _list1 = null;
			List _list2 = null;
			List _list = null;
			List Resultlist = new ArrayList();
			if("nrfl".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select lb_dm dm,(select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by lb_dm ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select lb_dm dm,(select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by lb_dm ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '4017' order by xh ");
				
			
			}else if("xzfl".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select xzfl dm,(select mc from t_dm where dm = a.xzfl) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by xzfl ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select xzfl dm,(select mc from t_dm where dm = a.xzfl) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by xzfl ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '2005' order by xh ");
			}else if("xxly".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select xxly dm,(select mc from t_dm where dm = a.xxly) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by xxly ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select xxly dm,(select mc from t_dm where dm = a.xxly) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by xxly ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '2006' order by xh ");
			}else{
				PageBean page = new PageBean();
				List _rlist = new ArrayList();
	  			jjd.setGrid("datalist", _rlist, page);
			}
			for(int i=0;i<_list.size();i++){
				Map _map =(Map)_list.get(i);
				_map.put("sl1","0");
				_map.put("sl2","0");
				for(int j =0;j<_list1.size();j++){
					if(_map.get("dm").toString().equals(((Map)_list1.get(j)).get("dm").toString())){
						_map.put("sl1",((Map)_list1.get(j)).get("sl").toString());
						continue;
					}
				}
				for(int j =0;j<_list2.size();j++){
					if(_map.get("dm").toString().equals(((Map)_list2.get(j)).get("dm").toString())){
						_map.put("sl2",((Map)_list2.get(j)).get("sl").toString());
						continue;
					}
				}
				Resultlist.add(_map);
			}
			
  			jjd.setGrid("datalist", Resultlist, new PageBean());
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("ldsj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询来电数据统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.ldsj异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map compareChart() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere1=new StringBuffer();
			StringBuffer sqlWhere2=new StringBuffer();
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			String cx_sjq1 = map.get("cx_sjq1")==null?"":String.valueOf(map.get("cx_sjq1"));
			String cx_sjz1 = map.get("cx_sjz1")==null?"":String.valueOf(map.get("cx_sjz1"));
			String cx_sjq2 = map.get("cx_sjq2")==null?"":String.valueOf(map.get("cx_sjq2"));
			String cx_sjz2 = map.get("cx_sjz2")==null?"":String.valueOf(map.get("cx_sjz2"));
			sqlWhere1.append(" and cjsj >'").append(cx_sjq1).append("' and cjsj <'").append(cx_sjz1).append(" 23:59:59'");
			sqlWhere2.append(" and cjsj >'").append(cx_sjq2).append("' and cjsj <'").append(cx_sjz2).append(" 23:59:59'");
			List _list1 = null;
			List _list2 = null;
			List _list = null;
			List Resultlist = new ArrayList();
			if("nrfl".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select lb_dm dm,(select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by lb_dm ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select lb_dm dm,(select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by lb_dm ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '4017' order by xh ");
				
			
			}else if("xzfl".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select xzfl dm,(select mc from t_dm where dm = a.xzfl) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by xzfl ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select xzfl dm,(select mc from t_dm where dm = a.xzfl) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by xzfl ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '2005' order by xh ");
			}else if("xxly".equals(tjlx)){
				_list1 = this.getJtN().queryForList(new StringBuffer().append("select xxly dm,(select mc from t_dm where dm = a.xxly) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere1).append(" group by xxly ").toString());
				_list2 = this.getJtN().queryForList(new StringBuffer().append("select xxly dm,(select mc from t_dm where dm = a.xxly) lbmc,count(*) sl from t_hjzx_fwqq a where yxbz = 1 ").append(sqlWhere2).append(" group by xxly ").toString());
				_list  = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm =1 and lb_dm = '2006' order by xh ");
			}else{
				PageBean page = new PageBean();
				List _rlist = new ArrayList();
	  			jjd.setGrid("datalist", _rlist, page);
			}
			for(int i=0;i<_list.size();i++){
				Map _map =(Map)_list.get(i);
				_map.put("sl1","0");
				_map.put("sl2","0");
				for(int j =0;j<_list1.size();j++){
					if(_map.get("dm").toString().equals(((Map)_list1.get(j)).get("dm").toString())){
						_map.put("sl1",((Map)_list1.get(j)).get("sl").toString());
						continue;
					}
				}
				for(int j =0;j<_list2.size();j++){
					if(_map.get("dm").toString().equals(((Map)_list2.get(j)).get("dm").toString())){
						_map.put("sl2",((Map)_list2.get(j)).get("sl").toString());
						continue;
					}
				}
				Resultlist.add(_map);
			}
			ColumnChartM column = new ColumnChartM(_list,"mc");
			column.dataSet(_list,"sl1","时间段一");
			column.dataSet(_list,"sl2","时间段二");
			jjd.setExtend("xmlStr",column.getXml());
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("ldsj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询来电数据统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.ldsj异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map sltj() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			//StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			sqlWhere.append(" and cjsj >'").append(cx_sjq).append("' and cjsj <'").append(cx_sjz).append(" 23:59:59'");
			sqlStr.append("select (select mc from t_dm where dm = a.lb_dm) lbmc,count(*) sll,")
				  .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when cblb_dm = '94063' then 1 else 0 end ) jbl,")
				  .append("SUM(case when cblb_dm = '94062' then 1 else 0 end ) zjdfl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end ) dqgdl ")
				  .append(",0 yxl,0 bjl from t_hjzx_fwqq a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString()).append(" group by lb_dm ");
			sqlStr.append(" union all ")
			      .append(" select '合计' lbmc ,count(*) sll,")
			      .append("SUM(case when cblb_dm <> '94064' then 1 else 0 end )yxsl,")
				  .append("SUM(case when cblb_dm = '94063' then 1 else 0 end ) jbl,")
				  .append("SUM(case when cblb_dm = '94062' then 1 else 0 end ) zjdfl,")
				  .append("SUM(case when ifEnd = 1 then 1 else 0 end ) dqbjl ")
				  .append(",0 yxl,0 bjl from t_hjzx_fwqq a ")
				  .append(" where yxbz = 1 ").append(sqlWhere.toString());
			PageBean page = new PageBean();
			page.setPage_allCount(50);
			page.setSql(sqlStr.toString());
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
  			return jjd.getData();
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("ldsj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询来电数据统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.ldsj异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map zhtj() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			if(tjlx.equals("")){
				tjlx = "blzt";
			}
			sqlWhere.append("  ldrq >'").append(cx_sjq).append("' and ldrq <'").append(cx_sjz).append(" 23:59:59'");
			if("blzt".equals(tjlx)){
				  sqlStr.append("select blzt tjlx,count(*) qs,SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx from (") //通渭县
				        .append("select bmid,( case when bjbz = 0 and  getDate() <= ISNULL(clqx,getDate()+1) then '办理中' ")
				        .append(" when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()) then '已办结' ")
				        .append(" when bjbz = 0 and getDate() > ISNULL(clqx,getDate()-1) then '已超期' ")
				        .append(" when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()) then '超时办结' else '' end) blzt ")
				        .append(" from t_cbd_sync where ").append(sqlWhere)
				        .append(") a group by blzt");
				  sqlStr.append(" union all ")
				  		.append("select '合计' tjlx,count(*) qs,SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
				  		.append(" from t_cbd_sync where ").append(sqlWhere);
			}else if("xxly".equals(tjlx)){
				 sqlStr.append("select (select mc from t_dm where dm = xxly) tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			        .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by xxly ");
				 sqlStr.append("union all ")
				 	.append("select '合计' tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			        .append(" from t_cbd_sync where ").append(sqlWhere);
			}else if("xzfl".equals(tjlx)){
				 sqlStr.append("select (select mc from t_dm where dm = xzfl) tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			        .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by xzfl ");
				 sqlStr.append("union all ")
				 	.append("select '合计' tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx  ") //通渭县
			        .append(" from t_cbd_sync where ").append(sqlWhere);
			}else if("nrfl".equals(tjlx)){
				 sqlStr.append("select (select mc from t_dm where dm = nrfl) tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
				        .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by nrfl ");
				 sqlStr.append("union all ")
				 		.append("select '合计' tjlx,count(*) qs,")
				    	.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
				        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
				        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
				        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
				        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
				        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
				        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
				        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx  ") //通渭县
			        .append(" from t_cbd_sync where ").append(sqlWhere);
			}else{
				//sqlStr.append("select '',0,0,0,0,0,0,0,0,0 from ")
			}
			
			PageBean page = new PageBean();
			page.setSql(sqlStr.toString());
			page.setPageGoto("1");
			page.setPage_allCount(100);
			page.setPageSize("1000");
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
  			return jjd.getData();
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("zhtj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询综合数据统计时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.zhtj异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map zhtjChart() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			List reList = null;
			String cx_sjq = map.get("cx_sjq")==null?"":String.valueOf(map.get("cx_sjq"));
			String cx_sjz = map.get("cx_sjz")==null?"":String.valueOf(map.get("cx_sjz"));
			String tjlx = map.get("tjlx")==null?"":String.valueOf(map.get("tjlx"));
			if(tjlx.equals("")){
				tjlx = "bjlx";
			}
			sqlWhere.append("  ldrq >'").append(cx_sjq).append("' and ldrq <'").append(cx_sjz).append(" 23:59:59'");
			if("blzt".equals(tjlx)){
				 sqlStr.append("select blzt tjlx,count(*) qs,SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
			        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
			        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
			        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
			        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
			        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
			        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
			        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx from (") //通渭县
			        .append("select bmid,( case when bjbz = 0 and  getDate() <= ISNULL(clqx,getDate()+1) then '办理中' ")
			        .append(" when bjbz = 1 and ISNULL(bjsj,getDate()) <= ISNULL(clqx,getDate()) then '已办结' ")
			        .append(" when bjbz = 0 and getDate() > ISNULL(clqx,getDate()-1) then '已超期' ")
			        .append(" when bjbz = 1 and ISNULL(bjsj,getDate()) > ISNULL(clqx,getDate()) then '超时办结' else '' end) blzt ")
			        .append(" from t_cbd_sync where ").append(sqlWhere)
			        .append(") a group by blzt");
			  sqlStr.append(" union all ")
			  		.append("select '合计' tjlx,count(*) qs,SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
			        .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
			        .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
			        .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
			        .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
			        .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
			        .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
			        .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx  ") //通渭县
			  		.append(" from t_cbd_sync where ").append(sqlWhere);
				reList = this.getJtN().queryForList(sqlStr.toString());
				StringBuffer chartStr = new StringBuffer(); 
				for(int i=0;i<reList.size();i++){
					Map _map = (Map)reList.get(i);
					chartStr.delete(0,chartStr.length());
					chartStr.append("<chart caption='办理状态(").append(_map.get("tjlx")==null?"":String.valueOf(_map.get("tjlx"))).append(")各区县数量' showPercentInToolTip= '1'  baseFontSize='12' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1'>")
							.append("<set label='市本级' value='").append(_map.get("sbj")).append("'/>")
							.append("<set label='安定区' value='").append(_map.get("adq")).append("'/>")
							.append("<set label='临洮县' value='").append(_map.get("ltx")).append("'/>")
							.append("<set label='岷县' value='").append(_map.get("mx")).append("'/>")
							.append("<set label='渭源县' value='").append(_map.get("wyx")).append("'/>")
							.append("<set label='漳县' value='").append(_map.get("zx")).append("'/>")
							.append("<set label='陇西县' value='").append(_map.get("lxx")).append("'/>")
							.append("<set label='通渭县' value='").append(_map.get("twx")).append("'/>")
							.append("</chart>");
					jjd.setExtend("chart"+i,chartStr.toString());							
				}
			}else if("xxly".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = xxly) tjlx,count(*) qs,")
		    		  .append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
		    		  .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
		    		  .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
		    		  .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
		    		  .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
		    		  .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
		    		  .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
		    		  .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
		    		  .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by xxly ");
			   sqlStr.append("union all ")
			   		 .append("select '合计' tjlx,count(*) qs,")
			   		 .append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
			   		 .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
			   		 .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
			   		 .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
			   		 .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
			   		 .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
			   		 .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
			   		 .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			   		 .append(" from t_cbd_sync where ").append(sqlWhere);
				reList = this.getJtN().queryForList(sqlStr.toString());
				StringBuffer chartStr = new StringBuffer(); 
				for(int i=0;i<reList.size();i++){
					Map _map = (Map)reList.get(i);
					chartStr.delete(0,chartStr.length());
					chartStr.append("<chart caption='信息来源(").append(_map.get("tjlx")==null?"":String.valueOf(_map.get("tjlx"))).append(")各区县数量' showPercentInToolTip= '1'  baseFontSize='12' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1'> ")
							.append("<set label='全市' value='").append(_map.get("qs")).append("'/>")
							.append("<set label='市本级' value='").append(_map.get("sbj")).append("'/>")
							.append("<set label='安定区' value='").append(_map.get("adq")).append("'/>")
							.append("<set label='临洮县' value='").append(_map.get("ltx")).append("'/>")
							.append("<set label='岷县' value='").append(_map.get("mx")).append("'/>")
							.append("<set label='渭源县' value='").append(_map.get("wyx")).append("'/>")
							.append("<set label='漳县' value='").append(_map.get("zx")).append("'/>")
							.append("<set label='陇西县' value='").append(_map.get("lxx")).append("'/>")
							.append("<set label='通渭县' value='").append(_map.get("twx")).append("'/>")
							.append("</chart>");
					jjd.setExtend("chart"+i,chartStr.toString());							
				}
			}else if("xzfl".equals(tjlx)){
				sqlStr.append("select (select mc from t_dm where dm = xzfl) tjlx,count(*) qs,")
		    		  .append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
		    		  .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
		    		  .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
		    		  .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
		    		  .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
		    		  .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
		    		  .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
		    		  .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
		    		  .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by xzfl ");
				sqlStr.append("union all ")
					  .append("select '合计' tjlx,count(*) qs,")
					  .append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
					  .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
					  .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
					  .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
					  .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
					  .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
					  .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
					  .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
					  .append(" from t_cbd_sync where ").append(sqlWhere);
				 reList = this.getJtN().queryForList(sqlStr.toString());
					StringBuffer chartStr = new StringBuffer(); 
					for(int i=0;i<reList.size();i++){
						Map _map = (Map)reList.get(i);
						chartStr.delete(0,chartStr.length());
						chartStr.delete(0,chartStr.length());
						chartStr.append("<chart caption='性质分类(").append(_map.get("tjlx")==null?"":String.valueOf(_map.get("tjlx"))).append(")各区县数量' showPercentInToolTip= '1'  baseFontSize='12' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1'> ")
								.append("<set label='全市' value='").append(_map.get("qs")).append("'/>")
								.append("<set label='市本级' value='").append(_map.get("sbj")).append("'/>")
								.append("<set label='安定区' value='").append(_map.get("adq")).append("'/>")
								.append("<set label='临洮县' value='").append(_map.get("ltx")).append("'/>")
								.append("<set label='岷县' value='").append(_map.get("mx")).append("'/>")
								.append("<set label='渭源县' value='").append(_map.get("wyx")).append("'/>")
								.append("<set label='漳县' value='").append(_map.get("zx")).append("'/>")
								.append("<set label='陇西县' value='").append(_map.get("lxx")).append("'/>")
								.append("<set label='通渭县' value='").append(_map.get("twx")).append("'/>")
								.append("</chart>");
						jjd.setExtend("chart"+i,chartStr.toString());							
					}
			}else if("nrfl".equals(tjlx)){
				 sqlStr.append("select (select mc from t_dm where dm = nrfl) tjlx,count(*) qs,")
			    	   .append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
			    	   .append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
			    	   .append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
			    	   .append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
			    	   .append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
			    	   .append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
			    	   .append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
			    	   .append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			    	   .append(" from t_cbd_sync where ").append(sqlWhere).append(" group by nrfl ");
				 sqlStr.append("union all ")
			 			.append("select '合计' tjlx,count(*) qs,")
			 			.append("SUM(case when bmid = '321_64650' then 1 else 0 end) sbj,")
			 			.append("SUM(case when bmid = '321_64651' then 1 else 0 end) adq,") //安定区
			 			.append("SUM(case when bmid = '321_64652' then 1 else 0 end) ltx,") //临洮县
			 			.append("SUM(case when bmid = '321_64654' then 1 else 0 end) mx,")  //岷县
			 			.append("SUM(case when bmid = '321_64656' then 1 else 0 end) wyx,") //渭源县
			 			.append("SUM(case when bmid = '321_64657' then 1 else 0 end) zx,")  //漳县
			 			.append("SUM(case when bmid = '321_64653' then 1 else 0 end) lxx,") //；陇西县
			 			.append("SUM(case when bmid = '321_64655' then 1 else 0 end) twx ") //通渭县
			 			.append(" from t_cbd_sync where ").append(sqlWhere);
			 reList = this.getJtN().queryForList(sqlStr.toString());
				StringBuffer chartStr = new StringBuffer(); 
				for(int i=0;i<reList.size();i++){
					Map _map = (Map)reList.get(i);
					chartStr.delete(0,chartStr.length());
					chartStr.delete(0,chartStr.length());
					chartStr.append("<chart caption='性质分类(").append(_map.get("tjlx")==null?"":String.valueOf(_map.get("tjlx"))).append(")各区县数量' showPercentInToolTip= '1'  baseFontSize='12' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1'> ")
							.append("<set label='全市' value='").append(_map.get("qs")).append("'/>")
							.append("<set label='市本级' value='").append(_map.get("sbj")).append("'/>")
							.append("<set label='安定区' value='").append(_map.get("adq")).append("'/>")
							.append("<set label='临洮县' value='").append(_map.get("ltx")).append("'/>")
							.append("<set label='岷县' value='").append(_map.get("mx")).append("'/>")
							.append("<set label='渭源县' value='").append(_map.get("wyx")).append("'/>")
							.append("<set label='漳县' value='").append(_map.get("zx")).append("'/>")
							.append("<set label='陇西县' value='").append(_map.get("lxx")).append("'/>")
							.append("<set label='通渭县' value='").append(_map.get("twx")).append("'/>")
							.append("</chart>");
					jjd.setExtend("chart"+i,chartStr.toString());							
				}
			}else{
				//sqlStr.append("select '',0,0,0,0,0,0,0,0,0 from ")
			}
  			return jjd.getData();
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("zhtjChart");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询综合数据统计饼状图时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.zhtjChart异常:" + e.toString());
		}
		return jjd.getData();
	}
	/**
	 * 查询删掉的办件
	 * 
	 * @return
	 */
	public Map queryDel() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			String jsId = "";
			
			String tmp="";
			tmp = String.valueOf(map.get("cx_sjq"))!=null?String.valueOf(map.get("cx_sjq")):"";
			if(!"".equals(tmp)&& tmp!=null & !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(ldrq,'YYYY-MM-DD') >='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and ldrq >='").append(tmp).append("'");
				}
			}
			tmp = String.valueOf(map.get("cx_sjz"))!=null?String.valueOf(map.get("cx_sjz")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(ldrq,'YYYY-MM-DD') <='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and ldrq<='").append(tmp).append(" 23:59:59'");
				}
			}
			tmp = String.valueOf(map.get("cx_blhj"))!=null?String.valueOf(map.get("cx_blhj")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and curstepid = ").append(tmp);
			}
			tmp = String.valueOf(map.get("cx_bllx"))!=null?String.valueOf(map.get("cx_bllx")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and bjxz = '").append(tmp).append("'");
			}
			tmp = String.valueOf(map.get("cx_xzfl"))!=null?String.valueOf(map.get("cx_xzfl")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and xzfl = '").append(tmp).append("'");
			}
			tmp = String.valueOf(map.get("cx_xxly"))!=null?String.valueOf(map.get("cx_xxly")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and xxly = '").append(tmp).append("'");
			}
			tmp = String.valueOf(map.get("slbh_day"))!=null?String.valueOf(map.get("slbh_day")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and slbh_day = '").append(tmp).append("'");
			}
			tmp = String.valueOf(map.get("ldr"))!=null?String.valueOf(map.get("ldr")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and ldr like '%").append(tmp).append("%'");
			}
			tmp = String.valueOf(map.get("ldhm"))!=null?String.valueOf(map.get("ldhm")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and lxdh like '%").append(tmp).append("%'");
			}
			tmp = String.valueOf(map.get("title"))!=null?String.valueOf(map.get("title")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and undo_title like '%").append(tmp).append("%'");
			}
			sqlStr.append("select (select stepname from t_oswf_node_info where stepid=a.curstepid) blhj,workid flowid,entryid,slbh_day,left(undo_title,25) undo_title,SUBSTRING(convert(varchar(50),ldrq,120),6,11) ldrq,ldrq ldrq1,(case when bjxz='402101' then '限时件' when bjxz='402102' then '承诺件' when bjxz='402103' then '联办件' when bjxz='402104' then '督办件' else '' end ) bjxz,")
				  .append("( case when isend = 0 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) then '办理中' ")
				  .append(" when isdirend =1 then '已办结' when isend = 1 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) then '已办结' ")
				  .append(" when isend = 0 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) then '已超期' ")
				  .append(" when isend = 1 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) then '超时办结' else '' end) blzt,")
				  .append(" (select mc from t_org_bm where bmid =a.bmid) qx, ")
				  .append("(select xm from t_org_yh where yhid =a.bmclry) cbdw,clqx ")
				  .append(" from t_cbd_sync_del a where 1=1 ").append(sqlWhere);
			sqlCount.append("select count(*) from t_cbd_sync_del a where 1=1 ")
				  .append(sqlWhere);
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
			sqlStr.append(" order by ldrq1 desc");
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryZb");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询转办删除数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Tjbb.queryDel异常:" + e.toString());
		}
		return jjd.getData();
	}
}

package com.coffice.hjzx.fwqq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.util.excel.ExcelTool;
import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;


public class Zbcx extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Zbcx(Map mapIn) {
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
	 * 转办查询
	 * 
	 * @return
	 */
	public Map queryZb() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			String jsId = "";
			List jsList = (List)Cache.getUserInfo(yhid,"js");
			//System.out.println("jsList:"+jsList);
			String curbmid = "";
			if(!"admin".equals(yhid) && !"sys".equals(yhid)){
				curbmid = Cache.getUserInfo(yhid,"bmid").toString();
			}
			//System.out.println("curbmid:"+curbmid);
			if(jsList.size()>0){
				jsId = ((Map)jsList.get(0)).get("dm").toString();
			}
			if("2014081220584987587670".equals(jsId) || "2014081220585718797276".equals(jsId)){
				//办理单位查询，只查询本人的。
				sqlWhere.append(" and clrid='").append(yhid).append("'");
			}
			if("2014081220590317128204".equals(jsId) || "2014091616413357856840".equals(jsId)){
				//如果是县批办或者县领导，则限定只查询自己县区的
				if(!"".equals(bmid)){
					sqlWhere.append(" and bmid='").append(curbmid).append("'");
				}else{
					//异常处理，当部门id为空时，不查询出数据
					sqlWhere.append(" and 1=0 ");
				}
			}
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
			
			tmp = String.valueOf(map.get("cx_bm"))!=null?String.valueOf(map.get("cx_bm")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and bmid = '").append(tmp).append("'");
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
			
			sqlStr.append("select (select stepname from t_oswf_node_info where stepid=a.curstepid) blhj,workid flowid,entryid,slbh_day,left(undo_title,25) undo_title,ldrq,")
				  .append(" (select mc from t_org_bm where bmid =a.bmid) qx, ")
				  .append("(select xm from t_org_yh where yhid =a.clrid) cbdw,clqx ")
				  .append(" from t_cbd_sync a where 1=1 ").append(sqlWhere);
			sqlCount.append("select count(*) from t_cbd_sync a where 1=1 ")
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
			String sortBy = String.valueOf(map.get("sortBy"))!=null?String.valueOf(map.get("sortBy")):"";
			String sortType = String.valueOf(map.get("sortType"))!=null?String.valueOf(map.get("sortType")):"";
			if(!"".equals(sortBy) && !"null".equals(sortBy)){
				sqlStr.append(" order by ").append(sortBy).append(" ");
			}else{
				sqlStr.append(" order by ldrq ");
			}
			if(!"".equals(sortType) && !"null".equals(sortType)){
				sqlStr.append(sortType);
			}else{
				sqlStr.append(" desc ");
			}
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
			logItem.setDesc("查询转办数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "zbcx.queryZb异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	/**
	 * 删除转办
	 * 
	 * @return
	 */
	public Map delZb() {
		try {
			String zbguid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(zbguid)){
				this.getJtN().update("insert into t_oswf_work_item_del select * from t_oswf_work_item where Entry_ID="+zbguid);
				this.getJtN().update("insert into t_oswf_work_item_his_del select * from t_oswf_work_item_his where Entry_ID="+zbguid);
				this.getJtN().update("insert into t_cbd_sync_del select * from t_cbd_sync where entryid="+zbguid);
				this.getJtN().update("delete from t_oswf_work_item where Entry_ID="+zbguid);
				this.getJtN().update("delete from t_oswf_work_item_his where Entry_ID="+zbguid);
				this.getJtN().update("delete from t_cbd_sync where entryid="+zbguid);
				this.getJtN().update("update t_hjzx_fwqq set yxbz = 0 where entryid="+zbguid+" and cblb_dm = '94063'");
			}
		} catch (Exception e) {
			logItem.setMethod("delZb");
			logItem.setLevel("error");
			logItem.setDesc("删除转办数据");
			logItem.setContent(e.toString());
			logItem.setYhid(yhid);
			Log.write(logItem);
			jjd.setResult(false, "删除发生异常");
		}
		return jjd.getData();
	}
	/**
	 * 承办单位回退率导出
	 * @param response
	 * @param request
	 * @return
	 */
	public Map exportXls(HttpServletResponse response,HttpServletRequest request){
		try{
			String templateFileName = "fwqq/zbExport.xls";
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			String jsId = "";
			List jsList = (List)Cache.getUserInfo(yhid,"js");
			//System.out.println("jsList:"+jsList);
			String curbmid = "";
			if(!"admin".equals(yhid) && !"sys".equals(yhid)){
				curbmid = Cache.getUserInfo(yhid,"bmid").toString();
			}
			//System.out.println("curbmid:"+curbmid);
			if(jsList.size()>0){
				jsId = ((Map)jsList.get(0)).get("dm").toString();
			}
			if("2014032922022988525704".equals(jsId) || "2014032922023467911435".equals(jsId)){
				//办理单位查询，只查询本人的。
				sqlWhere.append(" and bmclry='").append(yhid).append("'");
			}
			if("2014032922313831445746".equals(jsId) || "2014032922022298082563".equals(jsId)){
				//如果是县批办或者县领导，则限定只查询自己县区的
				if(!"".equals(bmid)){
					sqlWhere.append(" and bmid='").append(curbmid).append("'");
				}else{
					//异常处理，当部门id为空时，不查询出数据
					sqlWhere.append(" and 1=0 ");
				}
			}
			String cx_iStartDate = String.valueOf(map.get("cx_sjq"))!=null?String.valueOf(map.get("cx_sjq")):"";
			if(!"".equals(cx_iStartDate)&& cx_iStartDate!=null & !"null".equals(cx_iStartDate)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(ldrq,'YYYY-MM-DD') >='").append(cx_iStartDate).append("'");
				}else{
					sqlWhere.append(" and ldrq >='").append(cx_iStartDate).append("'");
				}
			}
			String cx_iEndDate = String.valueOf(map.get("cx_sjz"))!=null?String.valueOf(map.get("cx_sjz")):"";
			if(!"".equals(cx_iEndDate) && cx_iEndDate!=null && !"null".equals(cx_iEndDate)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(ldrq,'YYYY-MM-DD') <='").append(cx_iEndDate).append("'");
				}else{
					sqlWhere.append(" and ldrq<='").append(cx_iEndDate).append(" 23:59:59'");
				}
			}
			String tmp="";
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
			
			tmp = String.valueOf(map.get("cx_bm"))!=null?String.valueOf(map.get("cx_bm")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and bmid = '").append(tmp).append("'");
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
			tmp = String.valueOf(map.get("cx_blzt"))!=null?String.valueOf(map.get("cx_blzt")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				if("0".equals(tmp)){
					//sqlWhere.append(" and bmclsj is NULL and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) ");
					sqlWhere.append(" and isend = 0 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) ");
				}else if("1".equals(tmp)){
					//sqlWhere.append(" and bmclsj is not NULL ");
					sqlWhere.append(" and ((isend = 1 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate())) or isdirend = 1 )");
				}else if("2".equals(tmp)){
					//sqlWhere.append(" and bmclsj is NULL and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) ");
					sqlWhere.append(" and isend = 0 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate())");
				}else if("3".equals(tmp)){
					//sqlWhere.append(" and bmclsj is not NULL and bmclsj > ISNULL(clqx,getDate()) ");
					sqlWhere.append(" and isend = 1 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) ");
				}
			}
			sqlStr.append("select row_number()over(order by formid )as rownum, undo_title ld,ldr,lxdh,(select fywt from t_form1305 where id = a.formid) nr,")
				  .append("(select mc from t_dm where dm = a.nrfl) nrfl,(select mc from t_org_bm where bmid = a.bmid) qx,")
				  .append("(select mc from t_dm where dm = a.xxly) xxly,(select mc from t_dm where dm = a.xzfl) xzfl,")
				  .append("(select stepname from t_oswf_node_info where stepid = a.curstepid) blhj,")
				  .append("(select mc from t_dm where dm = a.bjxz) bjxz,")
				   .append("( case when isend = 0 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) then '办理中' ")
				  .append(" when isdirend =1 then '已办结' when isend = 1 and ISNULL(bmclsj,getDate()) <= ISNULL(clqx,getDate()) then '已办结' ")
				  .append(" when isend = 0 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) then '已超期' ")
				  .append(" when isend = 1 and ISNULL(bmclsj,getDate()) > ISNULL(clqx,getDate()) then '超时办结' else '' end) blz,")
				  .append("(select xm from t_org_yh where yhid = a.bmclry) cbdw,(select slzx from t_form1305 where id = a.formid) slr,")
				  .append("CONVERT(varchar(100), ldrq, 23) ldrq,CONVERT(varchar(100), clqx, 23) clqx from t_cbd_sync a where 1=1 ").append(sqlWhere)
				  .append(" order by ldrq ");
			List resultList = Db.getJtN().queryForList(sqlStr.toString());
			ExcelTool tool = new ExcelTool(templateFileName,"办件列表");
			tool.addLists("t", resultList);
			tool.exportExcel(response, request);
			oplog.warn("导出数据成功");
		}catch(Exception e){
			jjd.setResult(false, "Zbcx.exportXls:" + e.toString());
			//throw new ServiceException("导出数据出现异常");
		}
		return jjd.getData();
	}
}

package com.coffice.workflow.manager;

import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Convert;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class ManagerFlow extends BaseUtil{
	JspJsonData jjd;
	LogItem logItem;
	String zzid;
	String bmid;
	String gwid;
	String jsid;
	String yhid;
	Map map;
	public ManagerFlow(Map mapIn) {
		this.jjd = new JspJsonData();
		this.logItem = new LogItem();
		this.zzid = ((String) mapIn.get("zzid"));
		this.bmid = ((String) mapIn.get("bmid"));
		this.gwid = ((String) mapIn.get("gwid"));
		this.jsid = ((String) mapIn.get("jsid"));	
		this.yhid = ((String) mapIn.get("yhid"));		
		this.logItem.setYhid(this.yhid);
		this.logItem.setClassName(super.getClass().getName());
		this.map = mapIn;
	}
	
	public Map listFlow(){
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlWhere = new StringBuffer();
		try{
			String dbtype = SysPara.getValue("db_type");
			String wfName = (String) this.map.get("cx_mc");
			String zt = (String) this.map.get("cx_zt");
			String cx_sjq = (String) this.map.get("cx_sjq");
			String cx_sjz = (String) this.map.get("cx_sjz");
			if ((wfName != null) && (!"".equals(wfName))) {
				sqlWhere.append(" and remark like '%").append(wfName).append("%' ");
			}
			if ((zt != null) && (!"".equals(zt))) {
				sqlWhere.append(" and isuse='").append(zt).append("' ");
			}
			if ((cx_sjq != null) && (!"".equals(cx_sjq))) {
				if(("oracle").equals(dbtype)){
					sqlWhere.append(" and op_date>=to_date('").append(cx_sjq)
						    .append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				}else{
					sqlWhere.append(" and op_date>='").append(cx_sjq)
				    				.append(" 00:00:00'");
				}
			}
			if ((cx_sjz != null) && (!"".equals(cx_sjz))) {
				if(("oracle").equals(dbtype)){
					sqlWhere.append(" and op_date<=to_date('").append(cx_sjz)
						    .append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
				}else{
					sqlWhere.append(" and op_date<='").append(cx_sjz)
				    				.append(" 00:00:00'");
				}
			}
			sqlStr.append("select  name,id,remark mc,version,OP_DATE cjsj,(case when ISUSE ='1' then '未启用' when ISUSE ='0' then '启用' end ) zt from t_oswf_def a where isuse<>'2' ").append(
					sqlWhere.toString()).append(" order by op_date desc");
			sqlCount
					.append("select count(*) from t_oswf_def where isuse<>'2' ")
					.append(sqlWhere.toString());
			PageBean page = new PageBean();
			if(map.get("query_page") !=null && map.get("query_page")!=""){
				page.setPageGoto(map.get("query_page").toString());
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num") !=null && map.get("page_num")!=""){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(this.map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "查询流程列表时出现错误，错误代码：" + guid);
			this.logItem.setMethod("listFlow");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("查询流程列表时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map stopFlow(){
		try{
			String wfId = map.get("id")==null?"":String.valueOf(map.get("id"));
			if(!"".equals(wfId)){
				getJtN().update("update t_oswf_def set isuse='1' where id='" + wfId + "'");
				this.jjd.setExtend("prompt", "停用流程成功");
			}
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "停用流程时出现错误，错误代码：" + guid);
			this.logItem.setMethod("stopFlow");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("停用流程时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map startFlow(){
		try{
			String wfId = map.get("id")==null?"":String.valueOf(map.get("id"));
			if(!"".equals(wfId)){
				getJtN().update("update t_oswf_def set isuse='0' where id='" + wfId + "'");
				this.jjd.setExtend("prompt", "启用流程成功");
			}
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "启用流程时出现错误，错误代码：" + guid);
			this.logItem.setMethod("startFlow");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("启用流程时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map delFlow(){
		try{
			String wfId = map.get("id")==null?"":String.valueOf(map.get("id"));
			if(!"".equals(wfId)){
				getJtN().update("update t_oswf_def set isuse='2' where id='" + wfId + "'");
				this.jjd.setExtend("prompt", "删除流程成功");
			}
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "删除流程时出现错误，错误代码：" + guid);
			this.logItem.setMethod("delFlow");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("删除流程时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
}

package com.coffice.workflow.undo;

import java.util.Iterator;
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

public class UnDo extends BaseUtil{
	JspJsonData jjd;
	LogItem logItem;
	String zzid;
	String bmid;
	String gwid;
	String jsid;
	String yhid;
	Map map;
	public UnDo(Map mapIn) {
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
	
	public Map listDesk(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
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
			sqlStr.append("select left(formname,25) bt,b.id,tsgd,")
				  .append("b.cjsj sdsj,bllx from t_form1305 a,t_oswf_work_item b where a.slbh= b.Entry_ID and b.value='").append(yhid).append("' ");
			sqlCount.append("select count(*) from t_form1305 a,t_oswf_work_item b where a.slbh= b.Entry_ID and b.value='").append(yhid).append("' ");
			page.setSql(sqlStr.toString()+" order by cjsj desc");
			page.setCountSql(sqlCount.toString());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "桌面查询流程待办时：" + guid);
			this.logItem.setMethod("listDesk");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("桌面查询流程待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map init(){
		try{
			List nrlbList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='4017' order by xh desc ");
			jjd.setSelect("cx_nrfl",nrlbList);
			List xzflList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2005' order by xh desc ");
			jjd.setSelect("cx_xzfl",xzflList);
			List xxlyList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2006' order by xh desc ");
			jjd.setSelect("cx_xxly",xxlyList);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "初始化待办页面：" + guid);
			this.logItem.setMethod("init");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("初始化待办页面时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map query(){
		try{
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer sqlWhere = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				String dbtype = SysPara.getValue("db_type");
				String temp = "";
				temp = map.get("cx_ldrqq")==null?"":String.valueOf(map.get("cx_ldrqq"));
				if(!"".equals(temp)){
					if(dbtype.equals("oracle")){
						sqlWhere.append(" and b.cjsj>=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sqlWhere.append(" and b.cjsj>='").append(temp).append(" 00:00:00'");
					}
				}
				temp = map.get("cx_ldrqz")==null?"":String.valueOf(map.get("cx_ldrqz"));
				if(!"".equals(temp)){
					if(dbtype.equals("oracle")){
						sqlWhere.append(" and b.cjsj<=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sqlWhere.append(" and b.cjsj<='").append(temp).append(" 23:59:59'");
					}
				}
				temp = map.get("cx_nrfl")==null?"":String.valueOf(map.get("cx_nrfl"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.nrfl = '").append(temp).append("'");
				}
				temp = map.get("cx_xzfl")==null?"":String.valueOf(map.get("cx_xzfl"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.xzfl = '").append(temp).append("'");
				}
				temp = map.get("cx_xxly")==null?"":String.valueOf(map.get("cx_xxly"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.xxly = '").append(temp).append("'");
				}
				temp = map.get("cx_slbh")==null?"":String.valueOf(map.get("cx_slbh"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.slbh_day = '").append(temp).append("'");
				}
				temp = map.get("cx_ldhm")==null?"":String.valueOf(map.get("cx_ldhm"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.lxdh = '").append(temp).append("'");
				}
				temp = map.get("cx_ldhxm")==null?"":String.valueOf(map.get("cx_ldhxm"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.ldr = '").append(temp).append("'");
				}

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
				//String sfcq = "(case when zbsj IS null  or sc>=12  then '<img src=green.jpg>' when sc<0 then '<img src=red.jpg>' else '<img src=yellow.jpg>' end) imgState";
				sqlStr.append("select ldr,lxdh,b.cjsj sdsj,b.id,b.Entry_ID enid,b.step_id curid,left(formname,25) bt,ldrq ,tsgd,")
					  .append("DATEDIFF(mi,ISNULL(bjsj,getDate()),ISNULL(clqx,'2099-01-01 00:00:00')) sfcq,")
				      .append("(select mc from t_dm where dm = xxly )xxly,(select mc from t_dm where dm = xzfl )xzfl,")
				      .append("(select stepname from t_oswf_node_info where stepid=b.Step_ID) blhj,")
				      .append(" (select mc from t_org_bm where bmid =a.bmid) qx, ")
				      .append("(select xm from t_org_yh where yhid =a.bmclry) cbdw,clqx,bllx  from t_form1305 a,t_oswf_work_item b ")
				      .append(" where a.slbh= b.entry_id and b.value='").append(yhid).append("'").append(sqlWhere);
				sqlCount.append("select count(*) from t_form1305 a,t_oswf_work_item b where a.slbh= b.entry_id and value='")
						.append(yhid).append("'").append(sqlWhere);
				
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
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "查询待办时：" + guid);
			this.logItem.setMethod("query");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("查询待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
}

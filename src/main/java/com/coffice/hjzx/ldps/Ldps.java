package com.coffice.hjzx.ldps;

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

public class Ldps extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Ldps(Map mapIn) {
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
			tmp = String.valueOf(map.get("yps"))!=null?String.valueOf(map.get("yps")):"";
			if("on".equals(tmp)){
				sqlWhere.append(" and formid in (").append("select formid from t_hjzx_form_ldps where yhid='").append(yhid).append("') ");
			}
			sqlStr.append("select formid,(select stepname from t_oswf_node_info where stepid=a.curstepid) blhj,workid flowid,entryid,slbh_day,left(undo_title,25) undo_title,ldrq,")
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
			jjd.setResult(false, "ldps.queryZb异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map save(){
		try{
			String formid = map.get("formid")==null?"":String.valueOf(map.get("formid"));
			String ldyj = map.get("ldyj")==null?"":String.valueOf(map.get("ldyj"));
			Db.getJtN().update("insert into t_form_attitude values('"+Guid.get()+"','1305','"+formid+"','201408112024113511832','"+ldyj+"','"+yhid+"','"+Db.getStr()+"',NULL)");
			Db.getJtN().update("insert into t_hjzx_form_ldps values('"+formid+"','1900102810411026561973','"+ldyj+"','"+yhid+"','"+Db.getStr()+"')");
			Db.getJtN().update("delete from t_hjzx_form_csj where formid = '"+formid+"' and yhid ='"+yhid+"'");
		}catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存领导意见时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ldps.save异常:" + e.toString());
		}
		return jjd.getData();
	}
	/**
	 * 呈送件 查询
	 * 
	 * @return
	 */
	public Map queryCsj() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
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
			sqlWhere.append(" and formid in (select formid from t_hjzx_form_csj where yhid='").append(yhid).append("')");
			sqlStr.append("select formid,(select stepname from t_oswf_node_info where stepid=a.curstepid) blhj,workid flowid,entryid,slbh_day,left(undo_title,25) undo_title,ldrq,")
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
			logItem.setMethod("queryCsj");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询呈送件数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ldps.queryCsj异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map getLd(){
		try{
			List ldList = Db.getJtN().queryForList("select * from t_org_tree where sjid = 0 or  sjid = '67408' or guid = '67408' or guid = '64650'");
			StringBuffer ldStr = new StringBuffer();
			ldStr.append("[");
			for(int i = 0;i < ldList.size(); i ++){
				Map _map = (Map)ldList.get(i);
				if("0".equals(_map.get("sjid"))){
					ldStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/1_close.png'},");
					
				}else if(String.valueOf(_map.get("lx_dm")).equals("201")){
					//部门
					ldStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/folder.png'},");
				}else if(String.valueOf(_map.get("lx_dm")).equals("202")){
					//岗位
					ldStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/7.png'},");
				}else if(String.valueOf(_map.get("lx_dm")).equals("204")){
					//人员
					ldStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/man.png'},");
				}else{
					//其它
					ldStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/9.png'},");
				}
			}
			String treeString = ldStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("ldtree", treeString);
		}catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getLd");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取领导列表");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ldps.getLd异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map saveLd(){
		try{
			String ldId = map.get("ldId")==null?"":String.valueOf(map.get("ldId"));
			String formid = map.get("formid")==null?"":String.valueOf(map.get("formid"));
			String[] ld = ldId.split("~");
			for(int i=0;i<ld.length-1;i++){
				Db.getJtN().update("insert into t_hjzx_form_csj values('"+formid+"','"+ld[i]+"',getDate())");
			}
		}catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("saveLd");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存选择的领导");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ldps.saveLd异常:" + e.toString());
		}
		return jjd.getData();
	}
}

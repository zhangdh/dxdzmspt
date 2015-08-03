package com.coffice.directory.organization;


import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Md5;
import com.coffice.util.Str;
import com.coffice.util.SysPara;
public class Organization extends BaseUtil{

	JspJsonData jjd;
	String yhid;
	Map gmap;
	String str;
	ArrayList list;
	LogItem logItem;// 日志项
	String bmid;//部门ID	
	String gwid;//岗位ID	
	public Organization()
	{
		str = "";
		list = new ArrayList();
	}

	public Organization(Map mapIn)
	{
		str = "";
		list = new ArrayList();
		jjd = new JspJsonData();
		yhid = (String)mapIn.get("yhid");
		bmid = (String) mapIn.get("bmid");
		gwid = (String) mapIn.get("gwid");
		logItem = new LogItem();
		gmap = mapIn;
	}

	public Map getOrgTree()
	{		
		String sql_px = "select a.guid,a.sjid,a.lx_dm,a.sjbmid,a.mc,a.lxid,a.py,a.xh, pxxh  from t_org_tree a left outer join t_org_yh_px b on a.lxid=b.yhid where a.zt_dm=1 order by sjid,pxxh,xh,guid";
		List _list = this.getJtN().queryForList(sql_px);
		StringBuffer treeStr = new StringBuffer();
		treeStr.append("[");
		for(int i = 0;i < _list.size(); i ++){
			Map _map = (Map)_list.get(i);
			//根节点
		//	System.out.println("lx_dm:"+_map.get("lx_dm"));
			if("0".equals(_map.get("sjid"))){
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../../js/ztree/css/zTreeStyle/img/diy/1_close.png'},");
				
			}else if(String.valueOf(_map.get("lx_dm")).equals("201")){
				//部门
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../../js/ztree/css/zTreeStyle/img/diy/folder.png'},");
			}else if(String.valueOf(_map.get("lx_dm")).equals("202")){
				//岗位
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../../js/ztree/css/zTreeStyle/img/diy/7.png'},");
			}else if(String.valueOf(_map.get("lx_dm")).equals("204")){
				//人员
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../../js/ztree/css/zTreeStyle/img/diy/man.png'},");
			}else{
				//其它
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../../js/ztree/css/zTreeStyle/img/diy/9.png'},");
			}
			
		}
		String treeString = treeStr.toString();
		if(treeString.length()>1){
			treeString = treeString.substring(0,treeString.length()-1);
			treeString = treeString+"]";
		}
		jjd.setExtend("org_tree",treeString);
		return jjd.getData();
	}

	public Map getOrgList(){
		String orgid = gmap.get("orgid")==null?"":String.valueOf(gmap.get("orgid"));
		String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
		String lx_dm = gmap.get("lx_dm")==null?"":String.valueOf(gmap.get("lx_dm"));
		PageBean page = new PageBean();
		page.setPageGoto((String) (gmap.get("page_goto")));
		if(gmap.get("page_size")!=null){
			page.setPageSize(String.valueOf(gmap.get("page_size")));
		}else{
			page.setPageSize("10");
		}
		try {
			if(!"".equals(orgid)){
				page.setSql("select guid,lxid,mc,cjsj,lx_dm,(case when lx_dm='201' then '部门' when lx_dm='202' then '岗位' when lx_dm ='204' then '用户' else '' end ) lx_mc from t_org_tree where zt_dm=1 and sjid='"+orgid+"'");
				page.setCountSql("select count(*) from t_org_tree where zt_dm=1 and sjid='"+orgid+"'");
				page.setNamedParameters(gmap);
				List _list = Db.getPageData(page);
				jjd.setGrid("datalist", _list, page);
			}else{
				page.setSql("select  guid,lxid,mc,cjsj,lx_dm,(case when lx_dm='201' then '部门' when lx_dm='202' then '岗位' when lx_dm ='204' then '用户' else '' end ) lx_mc from t_org_tree where zt_dm=1 and sjid='0'");
				page.setCountSql("select count(*) from t_org_tree where zt_dm=1 and sjid='0'");
				page.setNamedParameters(gmap);
				List _list = Db.getPageData(page);
				jjd.setGrid("datalist", _list, page);
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getOrgList");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到查询结果失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getOrgList异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map saveBm(){
		try{
			String curbmid = gmap.get("curbmid")==null?"":String.valueOf(gmap.get("curbmid"));
			String dbtype = SysPara.getValue("db_type");
			if(!"".equals(curbmid)){
				//update
				String mc = gmap.get("bm_mc")==null?"":String.valueOf(gmap.get("bm_mc"));
				String dh = gmap.get("bm_dh")==null?"":String.valueOf(gmap.get("bm_dh"));
				String cz = gmap.get("bm_cz")==null?"":String.valueOf(gmap.get("bm_cz"));
				String email = gmap.get("bm_email")==null?"":String.valueOf(gmap.get("bm_email"));
				String bz = gmap.get("bm_bz")==null?"":String.valueOf(gmap.get("bm_bz"));
				getJtN().update("update t_org_bm set mc='"+mc+"',dh='"+dh+"',cz='"+cz+"',email='"+email+"',bz='"+bz+"' where bmid = '"+curbmid+"'");
				getJtN().update("update t_org_tree set mc='"+mc+"' where lxid = '"+curbmid+"'");
				getJtN().update("delete t_org_bm_kz where bmid = '"+curbmid+"'");	
				for (Iterator iterator = gmap.keySet().iterator(); iterator.hasNext();)
				{
					Object obj = iterator.next();
					if (String.valueOf(obj).startsWith("kz") && !"".equals(Str.filterNull(String.valueOf(gmap.get(obj)))))
						getJtN().update("insert into t_org_bm_kz(bmid,kz_dm,kzz)values(?,?,?)", new Object[] {
								curbmid, String.valueOf(obj).replaceFirst("kz", ""), gmap.get(obj)
						});
				}
				jjd.setResult(true,"");
			}else{
				//add
				String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
				String orgid = gmap.get("orgid")==null?"":String.valueOf(gmap.get("orgid"));
				String lx_dm = gmap.get("lx_dm")==null?"":String.valueOf(gmap.get("lx_dm"));
				String mc = gmap.get("bm_mc")==null?"":String.valueOf(gmap.get("bm_mc"));
				String dh = gmap.get("bm_dh")==null?"":String.valueOf(gmap.get("bm_dh"));
				String cz = gmap.get("bm_cz")==null?"":String.valueOf(gmap.get("bm_cz"));
				String email = gmap.get("bm_email")==null?"":String.valueOf(gmap.get("bm_email"));
				String bz = gmap.get("bm_bz")==null?"":String.valueOf(gmap.get("bm_bz"));				
				List sjLst  = this.getJtN().queryForList("select zzid from t_org_bm where bmid = '"+lxid+"'");
				String sjZzid = "0";
				if(sjLst.size()>0){
					sjZzid = ((Map)sjLst.get(0)).get("zzid").toString();
				}
				String nextId = String.valueOf(getNextId());
				String org_bmid  = lxid+"_"+nextId;
				if ("mysql".equals(dbtype) || "sqlserver".equals(dbtype)){
					getJtN().update("insert into t_org_bm(zzid,bmid,mc,dh,cz,email,bz,sjid,xh,cjsj,lrr_bmid,lrr_gwid,lrr_yhid)values(?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
							sjZzid, org_bmid, mc,dh, cz, email, bz, lxid, "0", getCurrentTime(), bmid,gwid, yhid
					});
					getJtN().update("insert into t_org_tree(guid,sjid,lx_dm,sjbmid,lxid,mc,py,xh,zt_dm,cjsj)values(?,?,?,?,?,?,?,?,?,?)", new Object[] {
							nextId, orgid, "201",orgid,org_bmid,mc,"","0","1",getCurrentTime()});
					
				}else if ("oracle".equals(dbtype)){
					getJtN().update("insert into t_org_bm(zzid,bmid,mc,dh,cz,email,bz,sjid,xh,cjsj,lrr_bmid,lrr_gwid,lrr_yhid)values(?,?,?,?,?,?,?,?,?,sysdate,?,?,?)", new Object[] {
							sjZzid, org_bmid, mc,dh, cz, email, bz, lxid, "0", bmid,gwid, yhid});
					getJtN().update("insert into t_org_tree(guid,sjid,lx_dm,sjbmid,lxid,mc,py,xh,zt_dm,cjsj)values(?,?,?,?,?,?,?,?,?,sysdate)", new Object[] {
							nextId, orgid, "201",lxid,mc,"","0","1"});
				}
				
				for (Iterator iterator = gmap.keySet().iterator(); iterator.hasNext();)
				{
					Object obj = iterator.next();
					if (String.valueOf(obj).startsWith("kz") && !"".equals(Str.filterNull(String.valueOf(gmap.get(obj)))))
						getJtN().update("insert into t_org_bm_kz(bmid,kz_dm,kzz)values(?,?,?)", new Object[] {
								org_bmid, String.valueOf(obj).replaceFirst("kz", ""), gmap.get(obj)
						});
				}
				jjd.setResult(true,"");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveBm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存部门信息失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "saveBm:" + e.toString());
		}
		return jjd.getData();
	}
	public Map modiBm(){
		
		return jjd.getData();
	}
	public Map saveGw(){
		try{
			String curgwid = gmap.get("curgwid")==null?"":String.valueOf(gmap.get("curgwid"));
			String dbtype = SysPara.getValue("db_type");
			String mc = gmap.get("gw_mc")==null?"":String.valueOf(gmap.get("gw_mc"));
			String bz = gmap.get("gw_bz")==null?"":String.valueOf(gmap.get("gw_bz"));				
			if(!"".equals(curgwid)){
				this.getJtN().update(new StringBuffer().append("update t_org_gw set mc ='").append(mc).append("',bz='").append(bz).append("' where gwid = '").append(curgwid).append("'").toString());
				this.getJtN().update(new StringBuffer().append("update t_org_tree set mc ='").append(mc).append("' where lxid = '").append(curgwid).append("'").toString());
			}else{
				String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
				String orgid = gmap.get("orgid")==null?"":String.valueOf(gmap.get("orgid"));
				String lx_dm = gmap.get("lx_dm")==null?"":String.valueOf(gmap.get("lx_dm"));
				String nextId = String.valueOf(getNextId());
				String org_gwid  = lxid+"_"+nextId;
				List sjLst  = this.getJtN().queryForList("select zzid from t_org_bm where bmid = '"+lxid+"'");
				String sjZzid = "0";
				if(sjLst.size()>0){
					sjZzid = ((Map)sjLst.get(0)).get("zzid").toString();
				}
				if ("mysql".equals(dbtype) || "sqlserver".equals(dbtype)){
					getJtN().update("insert into t_org_gw(zzid,gwid,mc,bz,sjgwid,cjsj,jb,xh,lrr_bmid,lrr_gwid,lrr_yhid,zt_dm)values(?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
							sjZzid, org_gwid, mc,"", "",getCurrentTime(), "1","0",bmid,gwid, yhid,"1"});
					getJtN().update("insert into t_org_tree(guid,sjid,lx_dm,sjbmid,lxid,mc,py,xh,zt_dm,cjsj)values(?,?,?,?,?,?,?,?,?,?)", new Object[] {
							nextId, orgid, "202",lxid,org_gwid,mc,"","0","1",getCurrentTime()});
					
				}else if ("oracle".equals(dbtype)){
					getJtN().update("insert into t_org_gw(zzid,gwid,mc,bz,sjgwid,cjsj,jb,xh,lrr_bmid,lrr_gwid,lrr_yhid,zt_dm)values(?,?,?,?,?,sysdate,?,?,?,?,?,?)", new Object[] {
							sjZzid, org_gwid, mc,"", "","1","0",bmid,gwid, yhid,"1"});
					getJtN().update("insert into t_org_tree(guid,sjid,lx_dm,sjbmid,lxid,mc,py,xh,zt_dm,cjsj)values(?,?,?,?,?,?,?,?,?,sysdate)", new Object[] {
							nextId, orgid, "202",lxid,org_gwid,mc,"","0","1"});
				}
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveGw");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存岗位信息失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "saveGw:" + e.toString());
		}
		return jjd.getData();
	}
	public Map modiGw(){
		
		return jjd.getData();
	}
	public Map saveYh(){
		try{

			String dateStr = getDateStr();
			String curyhid = gmap.get("curyhid")==null?"":String.valueOf(gmap.get("curyhid"));
			if(!"".equals(curyhid)){
				StringBuffer yhStr = new StringBuffer();
				yhStr.append("update t_org_yh set xm='").append(gmap.get("xm")).append("',birthday='").append(gmap.get("birthday"))
														  .append("',sex='").append(gmap.get("sex")).append("',jtdz='")
														  .append(gmap.get("jtdz")).append("',yb='").append(gmap.get("yb"))
														  .append("',jtdh='").append(gmap.get("jtdh")).append("',sj='")
														  .append(gmap.get("sj")).append("',email='").append(gmap.get("email"))
														  .append("',bz='").append( gmap.get("bz")).append("' where yhid = '")
														  .append(curyhid).append("'");
				this.getJtN().update(yhStr.toString());
				yhStr.delete(0, yhStr.length());
				yhStr.append("update t_org_tree set mc='").append(gmap.get("xm")).append("' where lxid = '").append(curyhid).append("'");
				this.getJtN().update(yhStr.toString());
				this.getJtN().update("delete t_org_yh_kz where yhid = '"+curyhid+"'");
				for (Iterator iterator = gmap.keySet().iterator(); iterator.hasNext();)
				{
					Object obj = iterator.next();
					if (String.valueOf(obj).startsWith("kz") && !"".equals(Str.filterNull(String.valueOf(gmap.get(obj)))))
						getJtN().update(new StringBuffer().append("insert into t_org_yh_kz(yhid,kz_dm,kzz,yxsjq)values(?,?,?,").append(dateStr).append(")").toString(), new Object[] {
							curyhid, String.valueOf(obj).replaceFirst("kz", ""), gmap.get(obj),
						});
				}
			}else{
				List yhList = this.getJtN().queryForList("select * from t_org_yh where dlmc = '"+gmap.get("dlmc")+"'");
				if(yhList.size() > 0){
					jjd.setResult(false, "已存在相同登陆名称用户");
					return jjd.getData();
				}
				String nextYhId = String.valueOf(getNextId());
				String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
				String orgid = gmap.get("orgid")==null?"":String.valueOf(gmap.get("orgid"));
				String lx_dm = gmap.get("lx_dm")==null?"":String.valueOf(gmap.get("lx_dm"));
				String sjZzid = "0";
				String sjBmid = "0";
			
				List sjList = this.getJtN().queryForList("select sjbmid from t_org_tree where lxid = '"+lxid+"'");
				if(sjList.size()>0){
					sjBmid = ((Map)sjList.get(0)).get("sjbmid").toString();
				}
				sjList = this.getJtN().queryForList("select zzid from t_org_gw where gwid = '"+lxid+"'");
				if(sjList.size()>0){
					sjZzid = ((Map)sjList.get(0)).get("zzid").toString();
				}
				
				getJtN().update((new StringBuilder("insert into t_org_yh(zzid,yhid,dlmc,dlmm,xm,py,qxbz,xh,birthday,sex,jtdz,yb,jtdh, sj, email,qq,msn,dlcs,cjsj,qz,tx,rzsj,gl,xl,byyx, yhzt_dm, sfzx, ip, bz, zt_dm,lrr_bmid,lrr_gwid,lrr_yhid,grtx)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,")).append(dateStr).append(",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString(), new Object[] {
						sjZzid, nextYhId, gmap.get("dlmc"), Md5.getMd5((String)gmap.get("dlmc")), gmap.get("xm"), gmap.get("py"), "0", "1000000", !"".equals(gmap.get("birthday")) && gmap.get("birthday") != null ? Date.valueOf((String)gmap.get("birthday")) : null, gmap.get("sex"), 
						gmap.get("jtdz"), gmap.get("yb"), gmap.get("jtdh"), gmap.get("sj"), gmap.get("email"), gmap.get("qq"), gmap.get("msn"), Integer.valueOf(0), gmap.get("qz"), gmap.get("tx"), 
						!"".equals(gmap.get("rzsj")) && gmap.get("rzsj") != null ? Date.valueOf((String)gmap.get("rzsj")) : null, !"".equals(gmap.get("gl")) && gmap.get("gl") != null ? gmap.get("gl") : null, gmap.get("xl"), gmap.get("byyx"), "400", "0", gmap.get("ip"), gmap.get("bz"), Integer.valueOf(1), bmid, 
						gwid, yhid, nextYhId});
				StringBuffer treeSql = new StringBuffer();
				treeSql.append("insert into t_org_tree(guid,sjid,lx_dm,sjbmid,lxid,mc,py,xh,zt_dm,cjsj)values('")
						.append(nextYhId).append("','").append(orgid).append("','204','").append(sjBmid).append("','")
						.append(nextYhId).append("','").append(gmap.get("xm")).append("','','0','1',").append(dateStr).append(")");
				this.getJtN().update(treeSql.toString());
				for (Iterator iterator = gmap.keySet().iterator(); iterator.hasNext();)
				{
					Object obj = iterator.next();
					if (String.valueOf(obj).startsWith("kz") && !"".equals(Str.filterNull(String.valueOf(gmap.get(obj)))))
						getJtN().update(new StringBuffer().append("insert into t_org_yh_kz(yhid,kz_dm,kzz,yxsjq)values(?,?,?,").append(dateStr).append(")").toString(), new Object[] {
								nextYhId, String.valueOf(obj).replaceFirst("kz", ""), gmap.get(obj),
						});
				}
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveYh");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存用户信息失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "saveYh:异常");
		}
		return jjd.getData();
	}
	public Map modiYh(){
		
		return jjd.getData();
	}
	public Map delOrg(){
		try{
		String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
		String orgid = gmap.get("orgid")==null?"":String.valueOf(gmap.get("orgid"));
		String lx_dm = gmap.get("lx_dm")==null?"":String.valueOf(gmap.get("lx_dm"));
		if(!"".equals(lxid)){
			int rowCount = this.getJtN().queryForInt("select count(*) from t_org_tree where zt_dm=1 and sjid='"+orgid+"'");
			if(rowCount > 0 ){
				jjd.setResult(false,"选项有下属项，无法删除");
				return jjd.getData();
			}
			this.getJtN().update("update t_org_tree set zt_dm = 0 where guid=?", new Object[]{orgid});
			if("201".equals(lx_dm)){
				//部门
				this.getJtN().update("update t_org_bm set zt_dm =0 where bmid = ?", new Object[]{lxid});
				this.getJtN().update("delete t_org_bm_kz where bmid = ?", new Object[]{lxid});
			}else if("202".equals(lx_dm)){
				//岗位
				this.getJtN().update("update t_org_gw set zt_dm =0 where gwid = ?", new Object[]{lxid});
			}else if ("204".equals(lx_dm)){
				//用户
				this.getJtN().update("update t_org_yh set zt_dm =0 where yhid = ?", new Object[]{lxid});
				this.getJtN().update("delete t_org_yh_kz where yhid = ?", new Object[]{lxid});
			}
		}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("delOrg");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除org异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "delOrg异常:" + e.toString());
		}
		return jjd.getData();
	}

	private String getCurrentTime()
	{
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		return curTime;
	}

	public Map selectRole(){
		try{
			List _list = getJtN().queryForList("select jsid dm ,mc from t_org_js order by pxxh ");
			jjd.setSelect("roles", _list);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("selectRole");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除org异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "selectRole异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map showBm(){
		try{
			String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
			if(!"".equals(lxid)){
				List bmList = getJtN().queryForList("select  bmid,mc bm_mc,dh bm_dh,cz bm_cz,email bm_email,bz bm_bz from t_org_bm where bmid = '"+lxid+"'");
				Map _map = (Map)bmList.get(0);
				List kzList = getJtN().queryForList("select  * from t_org_bm_kz where bmid = '"+lxid+"'");
				Map map_;
				for (Iterator iterator = kzList.iterator(); iterator.hasNext(); _map.put((new StringBuilder("kz")).append(map_.get("kz_dm")).toString(), map_.get("kzz")))
					map_ = (Map)iterator.next();
				jjd.setForm(_map);
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showBm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除org异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "showBm:" + e.toString());
		}
		return jjd.getData();
	}
	public Map showGw(){
		try{
			String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
			if(!"".equals(lxid)){
				Map _map = getJtN().queryForMap("select  gwid,mc gw_mc,bz gw_bz from t_org_gw where gwid = '"+lxid+"'");
				jjd.setForm(_map);
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showGw");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除org异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "showGw:" + e.toString());
		}
		return jjd.getData();
	}
	public Map showYh(){
		try{
			String lxid = gmap.get("lxid")==null?"":String.valueOf(gmap.get("lxid"));
			if(!"".equals(lxid)){
				List bmList = getJtN().queryForList("select  * from t_org_yh where yhid = '"+lxid+"'");
				Map _map = (Map)bmList.get(0);
				List kzList = getJtN().queryForList("select  kz_dm,kzz from t_org_yh_kz where yhid = '"+lxid+"'");
				Map map_ ;
				for(int i=0;i<kzList.size();i++){
					map_ = (Map)kzList.get(i);
					_map.put(new StringBuilder("kz").append(map_.get("kz_dm")).toString(), String.valueOf(map_.get("kzz")));
				}
				
				kzList = getJtN().queryForList((new StringBuilder("select mc as gwmc from t_org_tree a where exists (select * from t_org_tree where a.guid=sjid and lxid='")).append(lxid).append("' and zt_dm=1)").toString());
				for (int  j=0;j<kzList.size();j++)
				{	
					map_ = (Map)kzList.get(j);
					_map.put("mc_gw", String.valueOf(map_.get("gwmc")));
				}
				
				kzList = getJtN().queryForList((new StringBuilder("select mc as jsmc from t_org_js where jsid='")).append(String.valueOf(_map.get("kz300"))).append("'").toString());
				for (int  j=0;j<kzList.size();j++)
				{	
					map_ = (Map)kzList.get(j);
					_map.put("mc_js", String.valueOf(map_.get("jsmc")));
				}
				jjd.setForm(_map);
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showYh");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("showYh异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "showYh:" + e.toString());
		}
		return jjd.getData();
	}
	public Map rePass(){
		try{
			String curyhid = gmap.get("curyhid")==null?"":String.valueOf(gmap.get("curyhid"));
			getJtN().update("update t_org_yh set dlmm='"+Md5.getMd5("123456")+"' where yhid = '"+curyhid+"'");
			jjd.setResult(true,"重置成功");
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("rePass");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("重置密码异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "重置密码异常");
		}
		return jjd.getData();
	}
	public Map exportOrg(){
		try{
			  File file = new File("C:/ashburz/org.xls");
			  Workbook wb = Workbook.getWorkbook(file); 
			  Sheet[] sheet = wb.getSheets();   
			  StringBuffer s1 = new StringBuffer();
			  StringBuffer s2 = new StringBuffer();
			  StringBuffer s3 = new StringBuffer();
			  List rList = new ArrayList();
			  if(sheet!=null&&sheet.length>0){   
			        //对每个工作表进行循环   
			        for(int i=0;i<sheet.length;i++){
			        	 int rowNum = sheet[i].getRows();   
			        	 for(int j =2;j<rowNum;j++){
			        		 s1.delete(0, s1.length());
			        		 s2.delete(0, s2.length());
			        		 s3.delete(0, s3.length());
			        		 Cell[] cells = sheet[i].getRow(j); 
			        		 String xqmc = cells[0].getContents();
			        		 String xqid = this.getJtN().queryForMap("select bmid from t_org_bm where mc ='"+xqmc+"'").get("bmid").toString();
			        		 String gwmc = cells[1].getContents();
			        		 rList = this.getJtN().queryForList("select guid,lxid from t_org_tree where lx_dm = '202' and  mc ='"+gwmc+"'"+" and sjbmid='"+xqid+"'");
			        		 String gwid = "";
			        		 if(rList.size()>0){
			        			 gwid = ((Map)rList.get(0)).get("lxid").toString();
			        		 }else{
			        			 int nextId = this.getNextId();
			        			 gwid = xqid+"_"+nextId;
			        			 String sjid = this.getJtN().queryForMap("select guid from t_org_tree where lxid = '"+xqid+"' and lx_dm = '201'").get("guid").toString();
			        			 this.getJtN().update("insert into t_org_gw values(0,'"+gwid+"','"+gwmc+"','','',getDate(),1,0,0,0,'',1)");
			        			 this.getJtN().update("insert into t_org_tree values('"+nextId+"','"+sjid+"','202','"+xqid+"','"+gwid+"','"+gwmc+"','',0,1,getDate())");
			        		 }
			        		 //String gwid = this.getJtN().queryForMap("select guid,lxid from t_org_tree where lx_dm = '202' and  mc ='"+gwmc+"'"+" and sjbmid='"+xqid+"'").get("lxid").toString();
 
			        		 String sjid = this.getJtN().queryForMap("select guid,lxid from t_org_tree where lx_dm = '202' and  mc ='"+gwmc+"'"+" and sjbmid='"+xqid+"'").get("guid").toString();
 
			        		 String bmmc_yhmc = cells[2].getContents();
 
			        		 String nextYhId = String.valueOf(getNextId());
			        		 String dlmc = cells[3].getContents();
			        		 String jsmc = cells[4].getContents();
			        		 rList = this.getJtN().queryForList("select jsid from t_org_js where mc ='"+jsmc+"'");
			        		 String jsid = "";
			        		 if(rList.size()>0){
			        			 jsid = ((Map)rList.get(0)).get("jsid").toString();
			        		 }else{
			        			 jsid = Guid.get();
			        			 this.getJtN().update("insert into t_org_js values(0,'"+jsid+"','"+jsmc+"','',getDate(),0,0,'','10000')");
			        		 }
			        		 //String jsid = this.getJtN().queryForMap("select jsid from t_org_js where mc ='"+jsmc+"'").get("jsid").toString();
			        		 
			        		 s1.append("insert into t_org_yh(zzid,yhid,dlmc,dlmm,xm,qxbz,cjsj,yhzt_dm,zt_dm,grtx)values('321','")
			        		 	.append(nextYhId).append("','").append(dlmc).append("','").append(Md5.getMd5("12345"))
			        		 	.append("','").append(bmmc_yhmc).append("',0,getDate(),'400',1,'").append(nextYhId).append("')");
			        		 s2.append("insert into t_org_tree values('").append(nextYhId).append("','")
			        		   .append(sjid).append("','204','").append(xqid).append("','").append(nextYhId)
			        		   .append("','").append(bmmc_yhmc).append("','','0',1,getDate())");
			        		 s3.append("insert into t_org_yh_kz values('").append(nextYhId).append("','300','")
			        		   .append(jsid).append("',getDate(),NULL)");
			        		 this.getJtN().update(s1.toString());
			        		 this.getJtN().update(s2.toString());
			        		 this.getJtN().update(s3.toString());
			        	 }
			        }
			  }
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("exportOrg");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("导入组织目录异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "导入组织目录异常");
		}
		return jjd.getData();
	}
}

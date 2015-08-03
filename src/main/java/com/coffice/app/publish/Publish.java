package com.coffice.app.publish;

import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class Publish extends BaseUtil{
	JspJsonData jjd;
	LogItem logItem;
	String yhid;
	Map map;
	public Publish(Map mapIn) {
		this.jjd = new JspJsonData();
		this.logItem = new LogItem();	
		this.yhid = ((String) mapIn.get("yhid"));		
		this.logItem.setYhid(this.yhid);
		this.logItem.setClassName(super.getClass().getName());
		this.map = mapIn;
	}
	public Map queryInfo(){
		try{
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer sqlWhere = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				PageBean page = new PageBean();
				sqlStr.append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(select xm from t_org_yh where yhid =a.yhid) fbr,cjsj from t_publish_mx a where zt_dm = '8002' and fbfwbz = 1 ").append(sqlWhere.toString());
				sqlStr.append(" union  ");
				sqlStr.append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(select xm from t_org_yh where yhid =a.yhid) fbr,cjsj from t_publish_mx a where zt_dm = '8002' and fbfwbz = 0 ")
					  .append(" and guid in (").append("select ywid from t_diffusion_yhfw where zt_dm =1 and fsfw_ry='").append(yhid).append("')").append(sqlWhere.toString());
				sqlCount.append("select count(*) from (").append(sqlStr.toString()).append(") a");
				page.setSql(sqlStr.toString());
				page.setCountSql(sqlCount.toString());
				page.setOrderBy(" cjsj desc ");
				if(map.get("query_page")!=null){
					page.setPageGoto(map.get("query_page").toString());
				}else{
					page.setPageGoto("1");
				}
				if(map.get("page_num")!=null){
					page.setPageSize(map.get("page_num").toString());
				}else{
					page.setPageSize("10");
				}
				page.setNamedParameters(map);
				List _list = Db.getPageData(page);
				jjd.setExtend("reList", _list);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app查询信息发布时：" + guid);
			this.logItem.setMethod("app.queryInfo");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app查询信息发布时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	
	public Map showInfo(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				StringBuffer sqlStr = new StringBuffer();
				sqlStr.append("select (select xm from t_org_yh where yhid =a.yhid) yhid,fjid,cjsj,zt,nr from t_publish_mx a where guid ='"+guid+"'");
				Map _map = this.getJtN().queryForMap(sqlStr.toString());
				jjd.setExtend("nr",_map.get("nr")==null?"":String.valueOf(_map.get("nr")));
				jjd.setExtend("zt",_map.get("zt")==null?"":String.valueOf(_map.get("zt")));
				jjd.setExtend("fbr",_map.get("yhid")==null?"":String.valueOf(_map.get("yhid")));
				jjd.setExtend("cjsj",_map.get("cjsj")==null?"":String.valueOf(_map.get("cjsj")));
				String fjId = _map.get("fjid")==null?"":String.valueOf(_map.get("fjid"));
				jjd.setExtend("ifFj","0");
				if(!"".equals(fjId)){
					String diffusion_dir = SysPara.getValue("diffusion_dir");
					diffusion_dir = diffusion_dir.split(":")[1];
					jjd.setExtend("dir",diffusion_dir);
					List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+_map.get("fjid").toString()+"'");
					jjd.setExtend("fjStr",fjList);
					jjd.setExtend("ifFj","1");
				}
				this.getJtN().update("update t_diffusion_yhfw set ydcs = ydcs+1 where ywid='"+guid+"' and fsfw_ry='"+yhid+"'");
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app查询信息发布信息时：" + guid);
			this.logItem.setMethod("app.showInfo");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app查询信息发布信息时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
}

package com.coffice.hjzx.tjbb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class Khpj {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Khpj(Map mapIn) {
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
	//考核数据县区
	public Map queryXQ() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String temp = "";
			temp = map.get("cxq")==null?"":String.valueOf(map.get("cxq"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq >'").append(temp).append("'");
			}
			temp = map.get("cxz")==null?"":String.valueOf(map.get("cxz"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq <'").append(temp).append("'");
			}
			sqlStr.append("select (select mc from t_org_bm where bmid = a.bmid) bmmc,count(*) zl,")
				  .append("SUM(case when a.bjbz = 1 and a.bjsj<=clqx then 1 else 0 end ) asbj,")
				  .append("SUM(case when a.bjbz = 1 and a.bjsj>clqx then 1 else 0 end ) csbj,")
				  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
				  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
				  .append("SUM(case when a.bjbz=0 and ISNULL(clqx,getDate())<GETDATE() then 1 else 0 end )cswbj,")
				  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
				  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf ")
				  .append(" from t_cbd_sync a  where bmid IS NOT NULL ").append(sqlWhere).append(" group by bmid ");
			List _list = Db.getJtN().queryForList(sqlStr.toString());
			for(int i=0;i<_list.size();i++){
				Map _map = (Map)_list.get(i);
				int zl = Integer.parseInt(_map.get("zl").toString()); 	//按时办结
				int A0 = Integer.parseInt(_map.get("asbj").toString());//按时办结
				int A1 = Integer.parseInt(_map.get("csbj").toString());//超时办结
				int A2 = Integer.parseInt(_map.get("smy").toString());//双满意
				int A3 = Integer.parseInt(_map.get("dmy").toString());//单满意
				int A4 = Integer.parseInt(_map.get("cswbj").toString());//超时未办结
				int A5 = Integer.parseInt(_map.get("bmy").toString()); //不满意
				int cghf = Integer.parseInt(_map.get("cghf").toString());//成功回访
				double n1 = (float) 0.00;//双满意率
				double n2 = (float) 0.0;//单满意率
				double n3 = (float) 0.0; //按时办结率
				double n4 = (float) 0.0; //办结率
				if(cghf!=0){
					n1 = (double)(Math.round(A2*10000/cghf)/10000.0);
					n2 = (double)(Math.round(A3*10000/cghf)/10000.0);
				}
				if((A0+A1)!=0){
					n3 = (double)(Math.round(A0*10000/(A0+A1))/10000.0);
					n4 = (double)(Math.round((A0+A1)*10000/zl)/10000.0); 
				}
				double A = (double) (0.0025*A0+0.0015*A1+0.025*A2+0.0125*A3-0.05*A4-0.0125*A5+175*n1+125*n2+2.5*n3+n4);
				_map.put("n1",n1*100+"%");
				_map.put("n2",n2*100+"%");
				_map.put("n3",n3*100+"%");
				_map.put("n4",n4*100+"%");
				_map.put("score",A);
			}
			PageBean page = new PageBean();
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryXQ");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("考核评价查询区县数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Khpj.queryXQ异常:" + e.toString());
		}
		return jjd.getData();
	}
	//考核数据乡镇
	public Map queryXZ() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String temp = "";
			temp = map.get("cxq")==null?"":String.valueOf(map.get("cxq"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq >'").append(temp).append("'");
			}
			temp = map.get("cxz")==null?"":String.valueOf(map.get("cxz"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq <'").append(temp).append("'");
			}
			temp = map.get("cxbmid")==null?"":String.valueOf(map.get("cxbmid"));
			if(!"".equals(temp)){
				sqlWhere.append(" and bmid = '").append(temp).append("'");
			}
			sqlStr.append("select max(bmid)bmid,(select xm from t_org_yh where yhid = a.clrid) mc,count(*) zl,")
			  .append("SUM(case when a.bjbz = 1 and a.bjsj<=clqx then 1 else 0 end ) asbj,")
			  .append("SUM(case when a.bjbz = 1 and a.bjsj>clqx then 1 else 0 end ) csbj,")
			  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
			  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
			  .append("SUM(case when a.bjbz=0 and ISNULL(clqx,getDate())<GETDATE() then 1 else 0 end )cswbj,")
			  .append("SUM(case when a.zxhf_myd='600303' OR a.zxhf_fwtd='600303' then 1 else 0 end )bmy,")
			  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf ")
				  .append(" from t_cbd_sync a  where bmid IS NOT NULL  and clrid IS NOT NULL and clrid in (select yhid from t_org_yh_kz where kz_dm='400' and kzz='xz')")
				  .append(sqlWhere).append(" group by clrid order by bmid ");
			List _list = Db.getJtN().queryForList(sqlStr.toString());
			for(int i=0;i<_list.size();i++){
				Map _map = (Map)_list.get(i);
				int zl = Integer.parseInt(_map.get("zl").toString()); //按时办结
				int A0 = Integer.parseInt(_map.get("asbj").toString()); //按时办结
				int A1 = Integer.parseInt(_map.get("csbj").toString()); //超时办结
				int A2 = Integer.parseInt(_map.get("smy").toString());// 双满意
				int A3 = Integer.parseInt(_map.get("dmy").toString());//单满意
				int A4 = Integer.parseInt(_map.get("cswbj").toString());//超时未办结
				int A5 = Integer.parseInt(_map.get("bmy").toString());//不满意
				int cghf = Integer.parseInt(_map.get("cghf").toString());//成功回访
				double n1 = (float) 0.00;//双满意率
				double n2 = (float) 0.0;//单满意率
				double n3 = (float) 0.0; //按时办结率
				double n4 = (float) 0.0; //办结率
				if(cghf!=0){
					n1 = (double)(Math.round(A2*10000/cghf)/10000.0);
					n2 = (double)(Math.round(A3*10000/cghf)/10000.0);
				}
				if((A0+A1)!=0){
					n3 = (double)(Math.round(A0*10000/(A0+A1))/10000.0);
					n4 = (double)(Math.round((A0+A1)*10000/zl)/10000.0); 
				}
				double A = (double) (0.0025*A0+0.0015*A1+0.025*A2+0.0125*A3-0.05*A4-0.0125*A5+175*n1+125*n2+2.5*n3+n4);
				_map.put("n1",n1*100+"%");
				_map.put("n2",n2*100+"%");
				_map.put("n3",n3*100+"%");
				_map.put("n4",n4*100+"%");
				_map.put("score",A);
			}
			PageBean page = new PageBean();
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryXZ");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("考核评价查询乡镇数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Khpj.queryXZ异常:" + e.toString());
		}
		return jjd.getData();
	}
	//考核数据 直属部门
	public Map queryZS() {
		try {
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			String temp = "";
			temp = map.get("cxq")==null?"":String.valueOf(map.get("cxq"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq >'").append(temp).append("'");
			}
			temp = map.get("cxz")==null?"":String.valueOf(map.get("cxz"));
			if(!"".equals(temp)){
				sqlWhere.append(" and ldrq <'").append(temp).append("'");
			}
			temp = map.get("cxbmid")==null?"":String.valueOf(map.get("cxbmid"));
			if(!"".equals(temp)){
				sqlWhere.append(" and bmid = '").append(temp).append("'");
			}
			sqlStr.append("select max(bmid) bmid,(select xm from t_org_yh where yhid = a.clrid) mc,count(*) sjzs,")
			  	  .append("SUM(case when a.zxhf_myd='600301' and a.zxhf_fwtd='600301' then 1 else 0 end) smy,")
			  	  .append("SUM(case when (a.zxhf_myd='600301' and a.zxhf_fwtd <>'600301') or (a.zxhf_myd <> '600301' and a.zxhf_fwtd ='600301') then 1 else 0 end) dmy,")
			  	  .append("SUM(case when bjbz = 0 then 1 else 0 end ) wbj,SUM(case when sfgzdsr='600501' then 1 else 0 end ) hfsl,")
			  	  .append("SUM(case when zxhf_blzt IS NOT NULL and zxhf_myd IS NOT NULL and zxhf_fwtd IS NOT NULL and sfgzdsr IS NOT NULL then 1 else 0 end)cghf ")
				  .append(" from t_cbd_sync a  where bmid ='321_64650'  and clrid IS NOT NULL ").append(sqlWhere).append(" group by clrid ");
			List _list = Db.getJtN().queryForList(sqlStr.toString());
			for(int i=0;i<_list.size();i++){
				Map _map = (Map)_list.get(i);
				int A0 = Integer.parseInt(_map.get("sjzs").toString()); //事件总数
				int A1 = Integer.parseInt(_map.get("smy").toString()); //双满意数
				int A2 = Integer.parseInt(_map.get("dmy").toString());// 单满意数
				int B1 = Integer.parseInt(_map.get("wbj").toString());// 未办结数
				int cghf = Integer.parseInt(_map.get("cghf").toString());//成功回访
				int hfsl = Integer.parseInt(_map.get("hfsl").toString());//回复数量
				double n1 = (double) 0.00;//满意率
				double n2 = (double) 0.0;//回复率
				
				if(cghf!=0){
					n1 = (double)(Math.round((A1+A2)*10000/cghf)/10000.0);
					n1 = (double)(Math.round(hfsl*10000/cghf)/10000.0);
				}
				double A=(double)(Math.round(((0.06*A0+1.5*A1+0.5*A2-B1/15+10*n1+10*n2)*0.8)*10000)/10000.0);
				_map.put("n1",n1*100+"%");
				_map.put("n2",n2*100+"%");
				_map.put("score",A);
			}
			PageBean page = new PageBean();
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("querySZ");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("考核评价查询直属部门数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Khpj.queryZS:" + e.toString());
		}
		return jjd.getData();
	}
}

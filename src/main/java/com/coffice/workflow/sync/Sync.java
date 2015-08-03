package com.coffice.workflow.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coffice.bean.UserBean;
import com.coffice.sms.SysData;
import com.coffice.util.Db;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.cache.Cache;

public class Sync extends Thread{
	Map _map = new HashMap();
	 LogItem logItem;
	public  Sync(Map _map){
		logItem = new  LogItem();
		this._map = _map;
	}
	public void run(){
	try{
		logItem.setMethod("Sync.run");
		logItem.setLevel("info");
		String isBack = _map.get("isBack")==null?"":String.valueOf(_map.get("isBack"));
		String isEnd = _map.get("isEnd")==null?"":String.valueOf(_map.get("isEnd"));
		String workid = _map.get("workid")==null?"":String.valueOf(_map.get("workid"));
		String entryid = _map.get("entryid")==null?"":String.valueOf(_map.get("entryid"));
		String stepid = _map.get("stepid")==null?"":String.valueOf(_map.get("stepid"));
		String curstepid = _map.get("curstepid")==null?"":String.valueOf(_map.get("curstepid"));
		String yhid = _map.get("yhid")==null?"":String.valueOf(_map.get("yhid"));
		String xm = Cache.getUserInfo(yhid,"xm").toString();
		String formid = _map.get("formid")==null?"":String.valueOf(_map.get("formid"));
		List userList = (List)_map.get("userList");
		int nextCount = userList.size();
		String nextYhid = "";
		logItem.setContent("Sync流程同步,entryid:"+entryid+",stepid:"+stepid+",curstepid:"+curstepid);
		if("3".equals(curstepid)){
			Thread.sleep(5000);
			Map formMap  = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
			logItem.setDesc("Sync:数据同步-流程发起,yhid:"+yhid+",formMap:"+formMap);
			StringBuffer sql = new StringBuffer();
			sql.append("insert into t_cbd_sync(workid,entryid,formid,ldrq,slbh_day,ldr,lxdh,lxdz,xxly,xzfl,bjyt,sfbm,jjcd,nrfl,undo_title,curstepid)")
				.append(" values('").append(workid).append("',").append(entryid).append(",'").append(formid).append("','")
				.append(formMap.get("ldrq")).append("','").append(formMap.get("slbh_day")).append("','").append(formMap.get("ldr"))
				.append("','").append(formMap.get("lxdh")).append("','").append(formMap.get("lxdz")).append("','")
				.append(formMap.get("xxly")).append("','").append(formMap.get("xzfl")).append("','")
				.append(formMap.get("bjyt")).append("','").append(formMap.get("sfbm")).append("','").append(formMap.get("jjcd"))
				.append("','").append(formMap.get("nrfl")).append("','").append(formMap.get("undo_title")).append("',3)");
			Db.getJtN().update(sql.toString());
		}else if("51".equals(curstepid)){
			Map formMap  = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
			logItem.setDesc("Sync:数据同步-坐席呈交,yhid:"+yhid+",formMap:"+formMap);
			StringBuffer sql = new StringBuffer();
			sql.append("update t_cbd_sync set ldr= '").append(formMap.get("ldr")).append("',lxdh='").append(formMap.get("lxdh"))
				.append("',lxdz='").append(formMap.get("lxdz")).append("',xxly='").append(formMap.get("xxly"))
				.append("',xzfl='").append(formMap.get("xzfl")).append("',bjyt='").append(formMap.get("bjyt")).append("',")
				.append("nrfl='").append(formMap.get("nrfl")).append("',sfbm='").append(formMap.get("sfbm")).append("',")
				.append("jjcd='").append(formMap.get("jjcd")).append("',undo_title='").append(formMap.get("undo_title"))
				.append("',zpbsj='").append(Db.getStr()).append("',curstepid=51 where formid='").append(formid).append("'");
			Db.getJtN().update(sql.toString());
		}else if("52".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-转县批办,yhid:"+yhid+",formid:"+formid);
			if(nextCount > 0){
				nextYhid = ((Map)userList.get(0)).get("userId").toString();
			}
			UserBean user = UserBean.get(nextYhid);
			String nextBmid = user.getBmid();
			Db.getJtN().update("update t_form1305 set zbsj = getDate(),bllx=0 where id='"+formid+"'");
			Map _map = Db.getJtN().queryForMap("select * from t_form1305 where id='"+formid+"'");
			Db.getJtN().update("update t_cbd_sync set zbsj = '"+Db.getStr()+"',bmid='"+nextBmid+"',curstepid=52,clqx='"+_map.get("clqx")+"',bllx=0 where formid = '"+formid+"'");
		}else if("57".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-转市级部门,yhid:"+yhid+",formid:"+formid);
			if(nextCount > 0){
				nextYhid = ((Map)userList.get(0)).get("userId").toString();
			}
			UserBean user = UserBean.get(nextYhid);
			String nextBmid = user.getBmid();
			Db.getJtN().update("update t_form1305 set zbsj = getDate(),bllx=1 where id='"+formid+"'");
			Map _map = Db.getJtN().queryForMap("select * from t_form1305 where id='"+formid+"'");
			Db.getJtN().update("update t_cbd_sync set zbsj = '"+Db.getStr()+"',bmid='"+nextBmid+"',clrid='"+nextYhid+"',curstepid=57,clqx='"+_map.get("clqx")+"',bllx=1 where formid = '"+formid+"'");

		}else if("75".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-转市级部门重办,yhid:"+yhid+",formid:"+formid);
			Db.getJtN().update("update t_cbd_sync set clsj=NULL,curstepid=75,bjsj=NULL,bjbz=0,iscb=1  where formid = '"+formid+"'");
			Db.getJtN().update("update t_form1305 set clsj = NULL,bjsj =NULL where id='"+formid+"'");
		}else if("93".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-转县部门,yhid:"+yhid+",formid:"+formid);
			if(nextCount > 0){
				nextYhid = ((Map)userList.get(0)).get("userId").toString();
			}
			Db.getJtN().update("update t_cbd_sync set clrid='"+nextYhid+"',curstepid=93 where formid = '"+formid+"'");
		}else if("94".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-转县部门重办,yhid:"+yhid+",formid:"+formid);
			Db.getJtN().update("update t_cbd_sync set clsj=NULL,curstepid=94,bjsj=NULL,bjbz=0,iscb=1 where formid = '"+formid+"'");
			Db.getJtN().update("update t_form1305 set clsj = NULL,bjsj =NULL where id='"+formid+"'");
		}else if("182".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-市批办审核延期,yhid:"+yhid+",formid:"+formid);
			Db.getJtN().update("update t_cbd_sync set curstepid=182,issqyq=1 where formid = '"+formid+"'");
		}else if("183".equals(curstepid)){
			Map formMap  = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
			logItem.setDesc("Sync:数据同步-市批办审核回访,yhid:"+yhid+",formMap:"+formMap);
			Db.getJtN().update("update t_cbd_sync set clsj= '"+Db.getStr()+"',dfldr='"+formMap.get("dfldr")+"',blzt='"+formMap.get("blzt")+"',curstepid=183 where formid = '"+formid+"'");
			Db.getJtN().update("update t_form1305 set clsj = '"+Db.getJtN()+"' where id = '"+formid+"'");
		}else if("115".equals(curstepid)){
			Map formMap  = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
			logItem.setDesc("Sync:数据同步-县批办审核回访,yhid:"+yhid+",formMap:"+formMap);
			Db.getJtN().update("update t_cbd_sync set clsj= '"+Db.getStr()+"',dfldr='"+formMap.get("dfldr")+"',blzt='"+formMap.get("blzt")+"',curstepid=115 where formid = '"+formid+"'");
			Db.getJtN().update("update t_form1305 set clsj = '"+Db.getJtN()+"' where id = '"+formid+"'");
		}else if("59".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-坐席回访,yhid:"+yhid+",formid:"+formid);
			Db.getJtN().update("update t_cbd_sync set curstepid=59,bjsj='"+Db.getStr()+"',bjbz=1 where formid = '"+formid+"'");
			Db.getJtN().update("update t_form1305 set bjsj = '"+Db.getJtN()+"' where id = '"+formid+"'");
		}else if("98".equals(curstepid)){
			StringBuffer sql = new StringBuffer();
			Map formMap  = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
			logItem.setDesc("Sync:数据同步-市批办审核办结,yhid:"+yhid+",formMap:"+formMap);
			sql.append("update t_cbd_sync set zxhf_blzt='").append(formMap.get("zxhf_blzt")).append("',")
				.append("zxhf_myd='").append(formMap.get("zxhf_myd")).append("',zxhf_fwtd='")
				.append(formMap.get("zxhf_fwtd")).append("',sfgzdsr='").append(formMap.get("sfgzdsr"))
				.append("',wfhfqk='").append(formMap.get("wfhfqk")).append("',curstepid=98 where formid = '")
				.append(formid).append("'");
			Db.getJtN().update(sql.toString());
		}else if("4".equals(curstepid)){
			logItem.setDesc("Sync:数据同步-办结,yhid:"+yhid+",formid:"+formid);
			if("51".equals(stepid)){
				Db.getJtN().update("update t_cbd_sync set isend = 1,isdirend=1,curstepid=4 where formid ='"+formid+"'");
			}else if("57".equals(stepid) || "93".equals(stepid)){
				//由办理单位直接结束的，特殊工单
				Db.getJtN().update("update t_cbd_sync set isend = 1,curstepid=4,bjbz=1,ifts=1, dfldr='200201',blzt='200701',zxhf_blzt='600101',zxhf_myd='600301',zxhf_fwtd='600301',sfgzdsr='600501',clsj='"+Db.getStr()+"',bjsj='"+Db.getStr()+"' where formid ='"+formid+"'");
			}else{
				Db.getJtN().update("update t_cbd_sync set isend = 1,curstepid=4 where formid ='"+formid+"'");
			}
			Db.getJtN().update("update t_cbd_sync set isend= 1 where formid='"+formid+"'");
		}else{
			logItem.setDesc("Sync:数据同步-未匹配,yhid:"+yhid+",formid:"+formid);
		}
	}catch(Exception e){
		logItem.setLevel("error");
		logItem.setDesc("Sync.run:"+e.getMessage());
		Log.write(logItem);
	}
	}
}

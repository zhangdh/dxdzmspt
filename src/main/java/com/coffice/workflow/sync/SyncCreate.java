package com.coffice.workflow.sync;

import java.util.HashMap;
import java.util.Map;

import com.coffice.util.Db;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class SyncCreate extends Thread{
	Map _map = new HashMap();
	 LogItem logItem;
	public  SyncCreate(Map _map){
		logItem = new  LogItem();
		this._map = _map;
	}
	public void run(){
		logItem.setMethod("SyncCreate.run");
		logItem.setLevel("info");
		logItem.setDesc("流程发起--同步数据");
		Log.write(this.logItem);
		String workid = _map.get("workid")==null?"":String.valueOf(_map.get("workid"));
		String entryid = _map.get("entryid")==null?"":String.valueOf(_map.get("entryid"));
		String caller = _map.get("caller")==null?"":String.valueOf(_map.get("caller"));
		Db.getJtN().update("insert into t_cbd_sync(workid,entryid,isdirend,isend,curstepid)values(?,?,0,0,3)",new Object[]{workid,entryid});
	}
}

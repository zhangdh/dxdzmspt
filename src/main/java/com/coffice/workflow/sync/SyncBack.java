package com.coffice.workflow.sync;

import java.util.HashMap;
import java.util.Map;

import com.coffice.util.LogItem;

public class SyncBack extends Thread{
	Map _map = new HashMap();
	LogItem logItem;
	public  SyncBack(Map _map){
		logItem = new  LogItem();
		this._map = _map;
	}
	public void run(){
		try{
			
		}catch(Exception e){
			
		}
		
	}
}

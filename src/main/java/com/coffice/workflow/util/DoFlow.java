package com.coffice.workflow.util;

import java.util.List;
import java.util.Map;

import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.workflow.use.WKUse;

public class DoFlow extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	Map map;
	public DoFlow(){
		jjd = new JspJsonData();
		logItem = new LogItem();
	}
	public DoFlow(Map mapIn){
		jjd = new JspJsonData();
		logItem = new LogItem();
		zzid = (String) mapIn.get("zzid");
		bmid = (String) mapIn.get("bmid");
		gwid = (String) mapIn.get("gwid");
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	public Map doFlow(WKUse wkuse){
		Map _map=null;
		try {
			_map=wkuse.doWfInstanceAction();
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("WorkFlowMethod.doFlow");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"发送时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setExtend("result", "error");
			_map.putAll(jjd.getData());
			throw new ServiceException("保存或发送时异常");
		}
		logItem.setMethod("WorkFlowMethod.doFlow");
		logItem.setLevel("info");
		logItem.setDesc("用户："+yhid+"发送结束");
		Log.write(logItem);
		return _map;
	}
}

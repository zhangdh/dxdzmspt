package com.coffice.form.use;

import java.io.File;
import java.util.List;
import java.util.Map;
import com.coffice.workflow.use.WKUse;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;
public class FormUse extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	Map map;
	/*String sql;
	//公文模板路径
	String modelpath="";
	//公文处理临时路径
	String formPath="";
	//公文临时文件下载路径
	String downfilepath = "";
	//公文临时文件上传路径
	String upfilepath="";
	//解压和压缩工具
	//CabFile cabfile = null;
	//临时模板路径
	String tempPath="";
	//dll路径
	String dllpath="";*/
	
	
	public FormUse(Map mapIn){
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

	public Map showDocumentItem(WKUse app) {
		try {
			//显示流程实例项目
			map=app.showWfInstanceItem();
			map.put("xm", Cache.getUserInfo(yhid, "xm"));
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("showDocumentItem");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示公文处理时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}	
}

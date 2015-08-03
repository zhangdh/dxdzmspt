package com.coffice.unread;

import java.util.List;
import java.util.Map;
import com.coffice.bean.PageBean;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class UnRead {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;

	public UnRead(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	public Map listDesk() {
		try {
			PageBean page = new PageBean();
			if(map.get("page_num") != null){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			if(map.get("page_goto") != null){
				page.setPageGoto(map.get("page_goto").toString());
			}else{
				page.setPageGoto("1");
			}
			page.setSql(new StringBuffer().append("select guid,zt,cjsj from t_unread_mx a,t_unread_fw b where a.guid=b.ywid ")
										  .append(" and b.ydcs = 0 and b.fsfw_ry = '").append(yhid).append("' order by a.cjsj desc ").toString());
			page.setCountSql(new StringBuffer().append("select count(*) from t_unread_mx a,t_unread_fw b where a.guid=b.ywid ")
										  .append(" and b.ydcs = 0 and b.fsfw_ry = '").append(yhid).append("' order by a.cjsj desc ").toString());
			page.setNamedParameters(map);
			List<Map> _list = Db.getPageData(page);	
			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("listDesk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("桌面查询未读信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "桌面查询未读信息时时出现错误，错误代码:" + guid);
		}
		return jjd.getData();
	}
}

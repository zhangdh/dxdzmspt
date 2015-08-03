package com.coffice.directory.organization;

import java.util.ArrayList;

import java.util.Map;
//import org.apache.http.client.ClientProtocolException;
import com.coffice.util.BaseUtil;

import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Md5;

public class Setting extends BaseUtil{

	JspJsonData jjd;
	String yhid;
	Map gmap;
	String str;
	ArrayList list;
	LogItem logItem;// 日志项
	String bmid;//部门ID	
	String gwid;//岗位ID	
	public Setting()
	{
		str = "";
		list = new ArrayList();
	}

	public Setting(Map mapIn)
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

	public Map modiMM(){
			try {
				String dlmm = gmap.get("dlmm")==null?"":String.valueOf(gmap.get("dlmm"));
				if(!"".equals(dlmm)){
					this.getJtN().update("update t_org_yh set dlmm='"+Md5.getMd5(dlmm)+"' where yhid='"+yhid+"'" );
					jjd.setResult(true,"修改密码成功");
				}
			}catch(Exception e){
				String guid = Guid.get();
				logItem.setMethod("modiMM");
				logItem.setLogid(guid);
				logItem.setLevel("error");
				logItem.setDesc("修改密码成功");
				logItem.setContent(e.toString());
				Log.write(logItem);
				jjd.setResult(false, "modiMM异常:" + e.toString());
			}
			return jjd.getData();
		}

	public Map modiInfo(){
		try {
			String xm = gmap.get("xm")==null?"":String.valueOf(gmap.get("xm"));
			String jtdz = gmap.get("jtdz")==null?"":String.valueOf(gmap.get("jtdz"));
			String yb = gmap.get("yb")==null?"":String.valueOf(gmap.get("yb"));
			String jtdh = gmap.get("jtdh")==null?"":String.valueOf(gmap.get("jtdh"));
			String sj = gmap.get("sj")==null?"":String.valueOf(gmap.get("sj"));
			String email = gmap.get("email")==null?"":String.valueOf(gmap.get("email"));
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("update t_org_yh set xm='").append(xm).append("',jtdz='").append(jtdz)
			      .append("',yb='").append(yb).append("',jtdh='").append(jtdh).append("',sj='")
			      .append(sj).append("',email='").append(email).append("' where yhid='")
			      .append(yhid).append("'");
			this.getJtN().update(sqlStr.toString());
			jjd.setResult(true,"更新个人信息成功");
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("modiInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("修改个人信息失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "modiInfo:" + e.toString());
		}
		return jjd.getData();
	}
	public Map getYhInfo(){
		try {
			Map _map = this.getJtN().queryForMap("select xm,jtdz,yb,jtdh,sj,email from t_org_yh where yhid ='"+yhid+"'");
			jjd.setForm(_map);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("modiInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("修改个人信息失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "modiInfo:" + e.toString());
		}
		return jjd.getData();
	}
}

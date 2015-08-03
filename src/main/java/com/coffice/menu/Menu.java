package com.coffice.menu;
import java.util.List;
import java.util.Map;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;


public class Menu extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;//组织ID
	String bmid;//部门ID
	String gwid;//岗位ID	
	String yhid;//用户ID
	Map map;	
	public Menu(){
		logItem = new LogItem();
	}
	public Menu(String yhid){
		logItem = new LogItem();
		yhid = this.yhid;
	}	
	public Menu(Map mapIn){
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
	
	public Map GetMenu(){
		List _list = null;
		try{
			if("admin".equals(yhid) || "sys".equals(yhid)){
				_list = this.getJtN().queryForList("select * from t_qx_mx where qxlx='1' and zt_dm=1 order by xh ");
			}else{
				List _jsList = this.getJtN().queryForList("select kzz from t_org_yh_kz where kz_dm = '300' and yhid ='"+yhid+"'");
				String jsId ="";
				if(_jsList.size() >0 ){
					jsId = ((Map)_jsList.get(0)).get("kzz").toString();
				}
				StringBuffer sqlStr = new StringBuffer();
				sqlStr.append("select * from  t_qx_mx a ,t_qx_js b ")
				 	  .append(" where a.qxid = b.qxid and a.qxlx='1' and a.zt_dm = 1 and jsid in ('").append(yhid)
				      .append("','").append(  jsId).append("') order by xh ");
				_list = this.getJtN().queryForList(sqlStr.toString());
			}	
			if(_list.size()==0){
				jjd.setExtend("menuStr","");
				return jjd.getData();
			}
			StringBuffer menuStr = new StringBuffer();
			menuStr.append("[");
			for(int i=0;i<_list.size();i++){
				Map _map = (Map)_list.get(i);
				menuStr.append("{id:").append(_map.get("qxid")).append(",pId:").append(_map.get("sjid"))
				       .append(",name:'").append(_map.get("mc")).append("',qxurl:'").append(_map.get("url"))
				       .append("',qxico:'").append(_map.get("iconame")).append("'},");
			}
			String menuString = menuStr.toString();
			menuString = menuString.substring(0,menuString.length()-1);
			menuString = menuString+"]";
			jjd.setExtend("menuStr",menuString);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("GetMenu");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询菜单项时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
}

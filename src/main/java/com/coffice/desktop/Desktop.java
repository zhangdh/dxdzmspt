package com.coffice.desktop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class Desktop extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String yhid;
	String dbType="";
	Map map;
	String moRenLayOut="";
	public Desktop(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	
	public Map getNameMap(){
		Map nameMap=new HashMap();
		
		List _list=this.getNpjtN().queryForList("select id,mc from t_desk_mkxx",map);
		for(int i=0;i<_list.size();i++){
			Map map=(Map)_list.get(i);
			nameMap.put(map.get("id"),map.get("mc"));
		}
		return nameMap;
	}
	public Map getDivIdMap(){
		Map idMap=new HashMap();
		List _list=this.getNpjtA().queryForList("select id,id_sub from t_desk_mkxx",map);
		for(int i=0;i<_list.size();i++){
			Map map=(Map)_list.get(i);
			idMap.put(map.get("id"),map.get("id_sub"));
		}
		return idMap;
	}
	public Map getUrlMap(){
		Map urlMap=new HashMap();
		List _list=this.getNpjtA().queryForList("select id,url from t_desk_mkxx",map);
		for(int i=0;i<_list.size();i++){
			Map map=(Map)_list.get(i);
			urlMap.put(map.get("id"),map.get("url"));
		}
		return urlMap;
	}
	public Map getIncludeJspMap(){
		Map includeJspMap=new HashMap();
		List _list=this.getNpjtA().queryForList("select id,jsp from t_desk_mkxx",map);
		for(int i=0;i<_list.size();i++){
			Map map=(Map)_list.get(i);
			includeJspMap.put(map.get("id"),map.get("jsp"));
		}
		return includeJspMap;
	}
	public String getPageVal(){
		NamedParameterJdbcTemplate npjt =this.getNpjtA();
		String yhid=(String)map.get("yhid");
		List _list=npjt.queryForList("select * from t_org_yh_kz where yhid='"+yhid+"' and kz_dm=8501",map);
		String buju="";
		if(yhid.equals("admin") || _list.size()==0){
			Map _map1=npjt.queryForMap("select csz from t_sys_para where mk_dm=166 and csdm='desk_layout_val'",map);
			buju=(String)_map1.get("csz");
		}else{
			Map _map=(Map)_list.get(0);
			String kzz=(String)_map.get("kzz");
			buju=kzz;//布局
		}
		return buju;
	}
	
	
}

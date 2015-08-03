package com.coffice.authority;


import rtx.AddDeptAndUserInfo;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class Authority extends BaseUtil{

	JspJsonData jjd;
	String bmid;
	String gwid;
	String yhid;
	Map gmap;
	String str;
	ArrayList list;
	LogItem logItem;// 日志项

	public Authority()
	{
		str = "";
		list = new ArrayList();
	}

	public Authority(Map mapIn)
	{
		str = "";
		list = new ArrayList();
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String)mapIn.get("yhid");
		bmid = (String)mapIn.get("bmid");
		gwid = (String)mapIn.get("gwid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		gmap = mapIn;
	}

	public Map getJsTree(){	
		try{
			StringBuffer treeStr = new StringBuffer();
			treeStr.append("[");
			List _list = this.getJtN().queryForList("select * from t_org_js ");
			for(int i = 0;i < _list.size(); i ++){
				Map _map = (Map)_list.get(i);
				treeStr.append("{id:'").append(_map.get("jsid")).append("',pId:'0',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/1_close.png'},");
			}
			String treeString = treeStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("org_tree",treeString);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getJsTree");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取角色列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getJsTree时出现错误，错误代码:" + guid);
			throw new ServiceException("getJsTree时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}
	public Map getYhTree(){	
		try{
			String sql_px = "select a.guid,a.sjid,a.lx_dm,a.sjbmid,a.mc,a.lxid,a.py,a.xh, pxxh  from t_org_tree a left outer join t_org_yh_px b on a.lxid=b.yhid where a.zt_dm=1 order by sjid,pxxh,xh,guid";
			List _list = this.getJtN().queryForList(sql_px);
			StringBuffer treeStr = new StringBuffer();
			treeStr.append("[");
			for(int i = 0;i < _list.size(); i ++){
				Map _map = (Map)_list.get(i);
				//根节点
			//	System.out.println("lx_dm:"+_map.get("lx_dm"));
				if("0".equals(_map.get("sjid"))){
					treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/1_close.png'},");
					
				}else if(String.valueOf(_map.get("lx_dm")).equals("201")){
					//部门
					treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/folder.png'},");
				}else if(String.valueOf(_map.get("lx_dm")).equals("202")){
					//岗位
					treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/7.png'},");
				}else if(String.valueOf(_map.get("lx_dm")).equals("204")){
					//人员
					treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/man.png'},");
				}else{
					//其它
					treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/9.png'},");
				}
				
			}
			String treeString = treeStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("org_tree",treeString);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getYhTree");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("获取用户列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getYhTree时出现错误，错误代码:" + guid);
			throw new ServiceException("getYhTree时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}

	public Map saveJsQx(){	
		try{
			String selectedJsid = gmap.get("selectedJsid")==null?"":String.valueOf(gmap.get("selectedJsid"));
			if("".equals(selectedJsid)){
				jjd.setResult(false,"保存失败，角色id为空");
				return jjd.getData();
			}
			String qxStr = gmap.get("qxStr")==null?"":String.valueOf(gmap.get("qxStr"));
			String[] qxArray = qxStr.split(",");
			this.getJtN().update("delete t_qx_js where jsid = '"+selectedJsid+"'");
			for(int i=0;i<qxArray.length;i++){
				this.getJtN().update("insert into t_qx_js values('"+selectedJsid+"','"+qxArray[i]+"',1)");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveJsQx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存角色权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "saveJsQx时出现错误，错误代码:" + guid);
			throw new ServiceException("saveJsQx时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}

	public Map saveYhQx(){	
		try{
			String selectedYhid = gmap.get("selectedYhid")==null?"":String.valueOf(gmap.get("selectedYhid"));
			String qxStr = gmap.get("qxStr")==null?"":String.valueOf(gmap.get("qxStr"));
			String[] qxArray = qxStr.split(",");
			this.getJtN().update("delete t_qx_js where jsid = '"+selectedYhid+"'");
			for(int i=0;i<qxArray.length;i++){
				this.getJtN().update("insert into t_qx_js values('"+selectedYhid+"','"+qxArray[i]+"',1)");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveYhQx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存用户权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "saveYhQx时出现错误，错误代码:" + guid);
			throw new ServiceException("saveYhQx时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}

	public Map getYhQx(){	
		try{
			String curyhid = gmap.get("curyhid")==null?"":String.valueOf(gmap.get("curyhid"));
			StringBuffer qxStr =  new StringBuffer();
			if(!"".equals(curyhid)){
				List qxList = this.getJtN().queryForList("select qxid from t_qx_js where zt_dm=1 and jsid = '"+curyhid+"'");
				for(int i=0;i<qxList.size();i++){
					qxStr.append(((Map)qxList.get(i)).get("qxid")).append(",");
				}
				jjd.setExtend("qxStr",qxStr.toString());
			}else{
				jjd.setResult(false,"角色id为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getYhQx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到用户权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getYhQx时出现错误，错误代码:" + guid);
			throw new ServiceException("getYhQx时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}

	public Map getJsQx(){	
		try{
			String jsid = gmap.get("jsid")==null?"":String.valueOf(gmap.get("jsid"));
			StringBuffer qxStr =  new StringBuffer();
			if(!"".equals(jsid)){
				List qxList = this.getJtN().queryForList("select qxid from t_qx_js where zt_dm=1 and jsid = '"+jsid+"'");
				for(int i=0;i<qxList.size();i++){
					qxStr.append(((Map)qxList.get(i)).get("qxid")).append(",");
				}
				jjd.setExtend("qxStr",qxStr.toString());
			}else{
				jjd.setResult(false,"角色id为空");
			}
			
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getJsQx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到角色权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getJsQx时出现错误，错误代码:" + guid);
			throw new ServiceException("getJsQx时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}

	public Map getQxTree(){	
		try{
			StringBuffer QxStr = new StringBuffer();
			QxStr.append("select * from (")
				  .append("select qxid,mk_dm,qxlx,mc,sjid,xh from t_qx_mx where zt_dm = 1 and  sjid = 0 ")
				  .append(" union all ").append(" select qxid,mk_dm,qxlx,mc,sjid,xh from t_qx_mx where zt_dm = 1 and sjid in (")
				  .append(" select qxid from t_qx_mx where zt_dm=1 and sjid =0 ) ")
				  .append(") a order by qxlx,xh");
			List qxList = this.getJtN().queryForList(QxStr.toString());
			StringBuffer treeStr = new StringBuffer();
			treeStr.append("[");
			for(int i=0;i<qxList.size();i++){
				Map _map = (Map)qxList.get(i);
				if("1".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if ("2".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if("3".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if("4".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if ("5".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if ("6".equals(String.valueOf(_map.get("qxlx")))){
					
				}else if ("7".equals(String.valueOf(_map.get("qxlx")))){
					
				}else{
					
				}
				treeStr.append("{id:'").append(_map.get("qxid")).append("',pId:'").append(_map.get("sjid")).append("',name:'").append(_map.get("mc")).append("',icon:'../js/ztree/css/zTreeStyle/img/diy/9.png'},");
			}
			String treeString = treeStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("org_tree",treeString);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("getQxTree");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到权限列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "getQxTree时出现错误，错误代码:" + guid);
			throw new ServiceException("getQxTree时异常");// 抛出此异常以触发回滚
		}
	
		return jjd.getData();
	}


	
}

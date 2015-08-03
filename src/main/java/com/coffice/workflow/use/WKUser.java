package com.coffice.workflow.use;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coffice.directory.organization.Org_pub;
import com.coffice.util.BaseUtil;
import com.coffice.util.JspJsonData;
import com.coffice.util.LogItem;
import com.coffice.util.cache.Cache;
import com.coffice.workflow.condition.table.date.NodepropertyuserData;
import com.coffice.workflow.util.WorkFlowMethod;

public class WKUser extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;//组织ID
	String bmid;//部门ID
	String gwid;//岗位ID
	String yhid;
	Map map;
	String sql;
	WorkFlowMethod wfMethod=null;
	String ywid="";	
	public WKUser(){
		logItem = new LogItem();
		
	}	
	public WKUser(Map mapIn){
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
	/**
	 * 得到流程处理人配置信息
	 * @param entryid
	 * @param stepid
	 * @return
	 */
	public String[] getOswfClrInfo(String entryid,String stepid){
		WorkFlowMethod  wfMethod=new WorkFlowMethod();
		String[] s=new String[3];
		try {
			String roleType="";//权限类别
			String roleValue="";//权限id
			int condm=0;//1(发起者上级岗位)2(上一步执行者上级岗位)3(发起者)
			ArrayList<NodepropertyuserData> nodeUsers=wfMethod.findNodeUser(Integer.parseInt(stepid), Integer.parseInt(entryid));
			for(int j=0;j<nodeUsers.size();j++){
				NodepropertyuserData data=nodeUsers.get(j);
				roleType=data.doType;
				if(j==nodeUsers.size()-1){
					roleValue=roleValue+data.userId;
				}else{
					roleValue=roleValue+data.userId+",";
				}
				condm=data.conDm;
			}
			s[0]=roleType;
			s[1]=roleValue;
			s[2]=String.valueOf(condm);
		} catch (Exception e) {
			String msg=new StringBuffer("得到流程处理人配置信息：[").append(entryid).append("]时出现异常:").append(e.toString()).toString();
			log.error(msg);
		}
		return s;
	}
	
	/**
	 * 得到流程处理人配置信息 优化新增
	 * @param entryid
	 * @param stepid
	 * @return
	 */
	public String[] getOswfClrInfoKz(String workid,String stepid){
		WorkFlowMethod  wfMethod=new WorkFlowMethod();
		String[] s=new String[3];
		try {
			String roleType="";//权限类别
			String roleValue="";//权限id
			int condm=0;//1(发起者上级岗位)2(上一步执行者上级岗位)3(发起者)
			ArrayList<NodepropertyuserData> nodeUsers=wfMethod.findNodeUserKz(Integer.parseInt(stepid), workid);
			for(int j=0;j<nodeUsers.size();j++){
				NodepropertyuserData data=nodeUsers.get(j);
				roleType=data.doType;
				if(j==nodeUsers.size()-1){
					roleValue=roleValue+data.userId;
				}else{
					roleValue=roleValue+data.userId+",";
				}
				condm=data.conDm;
			}
			s[0]=roleType;
			s[1]=roleValue;
			s[2]=String.valueOf(condm);
		} catch (Exception e) {
			String msg=new StringBuffer("得到流程处理人配置信息：[").append(workid).append("]时出现异常:").append(e.toString()).toString();
			log.error(msg);
		}
		return s;
	}
	/**
	    * 根据用户列表返回用户树
	    * @param lxz
	    * @param bm_set
	    * @return
	    */
	   private List<Map> getYhTreeByYhList(String lxz, HashSet<String> bm_set) {
	   	List<Map> list;
	   	List<Map> bm_list = this.getJtN().queryForList("select sjbmid from t_org_tree where lxid in("+lxz+")");
	   	  String bmlist = getParentBmListByBmList(bm_set, bm_list);
	   		//获取用户对应的sjid列表
	   		List<Map> sjid_list = this.getJtN().queryForList("select sjid from t_org_tree  where lxid in("+lxz+")");
	   		String sjid = "";
	   		for(Map map:sjid_list){
	   			sjid = sjid+",'"+map.get("sjid")+"'";
	   		}
	   		sjid = sjid.replaceFirst(",", "");
	   		list = this.getJtN().queryForList("select * from t_org_tree where (lxid in("+lxz+") or guid in("+sjid+") or lxid in("+bmlist+
	   				")) and zt_dm=1 order by sjid,xh,guid+1");
	   	return list;
	   }
	   /**
	    * 获取定制部门的所有父部门列表
	    * @param bm_set
	    * @param bm_list
	    * @return
	    */
	   private String getParentBmListByBmList(HashSet<String> bm_set, List<Map> bm_list) {
	   	for(Map map:bm_list){
	   			getAllBmid(bm_set, String.valueOf(map.get("sjbmid")));
	   	  }
	   		Iterator<String> iter = bm_set.iterator();
	   		String bmlist = "";
	   		while (iter.hasNext()) {
	   			String temp = iter.next();
	   			bmlist = bmlist+",'"+temp+"'";
	   		}
	   		bmlist = bmlist.replaceFirst(",", "");
	   	return bmlist;
	   }
	   private void getAllBmid(HashSet<String> bm_set, String fsfw) {
		   	if(fsfw.indexOf("_")>0){
		   		bm_set.add(fsfw);
		   		String bmid = fsfw.substring(0,fsfw.lastIndexOf("_"));
		   		getAllBmid(bm_set,bmid);
		   	}else{
		   		bm_set.add(fsfw);
		   	}
	   }
	   /**
	    * 根据部门、岗位、人员、角色获取用户树--应用于工作流下一步处理
	    * 人员的选择
	    * @param lxz
	    * @param lxbz
	    * @param condm 
	    * @param yhid 
	    * @return
	    */
	   public List getYhTree(String lxz,String lxbz,String condm,String yhid,String entryid){
		   	 StringBuffer sb = new StringBuffer();
		   	  //新增处理人员筛选方式 condm = 4  //yhid的同部门人员(不包括下级部门) 8 代表第一次执行某节点是
		   	  if("4".equals(condm) || "8".equals(condm)){
		   		 String curBmid = Cache.getUserInfo(yhid,"bmid").toString();
		   		 List _list = this.getJtN().queryForList("select * from t_org_tree where sjbmid ='0' or lxid='"+curBmid+"' or sjbmid = '"+curBmid+"' and lxid <> '"+yhid+"' order by xh ");
		   		 return _list;
		   	  } 
			  List<Map> list = null;
			  Org_pub org_pub = new Org_pub();
			  String[] sys_fsfw_list = lxz.split(",");
			  HashSet<String> bm_set = new HashSet<String>();
			  if(lxz.indexOf("'")<0){
					  lxz = lxz.replaceAll(",", "','");
					  lxz="'"+lxz+"'";
			  }
			  if("0".equals(lxbz)){//部门
					for (String fsfw : sys_fsfw_list) {
						getAllBmid(bm_set, fsfw);
					}
					Iterator<String> iter = bm_set.iterator();
					String bmlist = "";
					while (iter.hasNext()) {
						String temp = iter.next();
						bmlist = bmlist+",'"+temp+"'";
					}
					bmlist = bmlist.replaceFirst(",", "");
					list = this.getJtN().queryForList("select * from t_org_tree where (sjbmid in("+lxz+") or lxid in("+bmlist+
							")) and zt_dm=1 order by sjid,xh,guid+1");
			   }else if("2".equals(lxbz)){//岗位
				  if("0".equals(condm)){//直接选择岗位
	           List<Map> bm_list = this.getJtN().queryForList("select sjbmid from t_org_tree where lxid in("+lxz+")");
	           String bmlist = getParentBmListByBmList(bm_set, bm_list);
	             sb.setLength(0);
	             sb.append("select * from t_org_tree where guid in(");
	             sb.append("select guid from t_org_tree where sjid in(select guid from t_org_tree where lxid in(").append(lxz).append(")))");
	             sb.append(" or lxid in(").append(lxz).append(")");
	             sb.append(" or lxid in("+bmlist+" ) and zt_dm=1 order by sjid,xh,guid+1");
	             list = this.getJtN().queryForList(sb.toString());
				   }else if("1".equals(condm)){//岗位发起者直接上级
					   String sql="select * from (select value,cjsj from t_oswf_work_item where entry_id='"+entryid+"' union all select value,cjsj from t_oswf_work_item_his where entry_id='"+entryid+"') l order by cjsj asc ";
					   List<Map> _list=this.getJtN().queryForList(sql);
	                List<Map> ygList=this.getSjyh(String.valueOf(_list.get(0).get("value")));
	                lxz="";
					   for(Map map:ygList){
						lxz = lxz+",'"+map.get("yhid")+"'";
					   }
					  lxz = lxz.replaceFirst(",", "");
					  list = this.getYhTreeByYhList(lxz, bm_set);
				   }else if("2".equals(condm)){//岗位执行者直接上级
					  List<Map> ygList=this.getSjyh(yhid);
					  lxz="";
					  for(Map map:ygList){
						lxz = lxz+",'"+map.get("yhid")+"'";
					  }
					  lxz = lxz.replaceFirst(",", "");
					  list = this.getYhTreeByYhList(lxz, bm_set);
				   }else if("4".equals(condm)){//上一步执行者下级岗位
					  List<Map> ygList=this.getXjyh(yhid);
					  lxz="";
					  for(Map map:ygList){
						lxz = lxz+",'"+map.get("yhid")+"'";
					  }
					  lxz = lxz.replaceFirst(",", "");
					  list =  this.getYhTreeByYhList(lxz, bm_set);
				   }else if("5".equals(condm)){//上一步执行者同部门
					  List<Map> ygList=this.getBmYhList(yhid);
					  lxz="";
					  for(Map map:ygList){
						lxz = lxz+",'"+map.get("yhid")+"'";
					  }
					  lxz = lxz.replaceFirst(",", "");
					 list =  this.getYhTreeByYhList(lxz, bm_set);
				   }
				}else if("1".equals(lxbz)){//用户
		              list = getYhTreeByYhList(lxz, bm_set);
				}else if("3".equals(lxbz)){//角色
	           List<Map> bm_list = this.getJtN().queryForList("select distinct sjbmid from t_org_yh_kz a,t_org_tree b where a.yhid=b.lxid " +
	           		" and kzz in("+lxz+") and kz_dm=300 and zt_dm=1");
	           String bmlist = getParentBmListByBmList(bm_set, bm_list);
	             sb.setLength(0);
	             sb.append("select * from t_org_tree where (");
	             sb.append("lxid in(select a.yhid from t_org_yh a,t_org_yh_kz b ");
	             sb.append("where a.yhid = b.yhid and b.kz_dm=300 and b.kzz in(").append(lxz).append(") and a.zt_dm=1) ");
	             sb.append("or guid in(select sjid from t_org_tree where lxid in(select a.yhid from t_org_yh a,t_org_yh_kz b");
	             sb.append(" where a.yhid = b.yhid and b.kz_dm=300 and b.kzz in(").append(lxz).append(") and a.zt_dm=1) and zt_dm=1) or ");
	             sb.append("lxid in(").append(bmlist).append(")) and zt_dm=1 order by sjid,xh,guid+1");
					list = this.getJtN().queryForList(sb.toString());
				}

		        return list;
		}
	   public List getSjyh(String yhid){
			StringBuffer sb = new StringBuffer();
			/*sb.append("select lxid as yhid,mc from t_org_tree a where sjid in(select guid from t_org_tree where lxid in ");
			sb.append(" (select sjgwid from t_org_gw where gwid in(select a.lxid from t_org_tree a,t_org_tree b where a.guid =b.sjid and b.lxid='");
			sb.append(yhid).append("') and zt_dm=1)");
			sb.append(" and sjbmid in(select sjbmid from t_org_tree where lxid='").append(yhid).append("')");
		    sb.append(" and zt_dm=1) and zt_dm=1"); */
			sb.append("select lxid as yhid,mc from t_org_tree where sjid in (select guid from t_org_tree where lx_dm='202' and lxid in (select sjgwid from t_org_gw where gwid in (select lxid as gwid from t_org_tree where guid in (select sjid from t_org_tree where lxid='"+yhid+"' and lx_dm='204'))))");
			List list = this.getJtA().queryForList(sb.toString());
			return list;
		}
	   /**
		 * 获取指定用户所在部门的下级岗位的所有用户
		 * @param yhid
		 * @return list
		 */
		public List getXjyh(String yhid){
			StringBuffer sb = new StringBuffer();
			/*sb.append("select lxid as yhid,mc from t_org_tree ");
			sb.append("where sjid in(select guid from t_org_tree where lxid in(");
			sb.append("select gwid from t_org_gw where sjgwid in(select lxid from t_org_tree where guid in(");
			sb.append("select sjid from t_org_tree where  lxid='").append(yhid).append("'))))");
			sb.append(" and sjbmid in(select sjbmid from t_org_tree where  lxid='").append(yhid).append("') and zt_dm=1");
			*/
			sb.append("select lxid as yhid,mc from t_org_tree where sjid in (select  d.guid as dguid from t_org_tree a left join t_org_tree b on  a.sjid=b.guid left join t_org_gw c on c.sjgwid=b.lxid left join t_org_tree d on d.lxid=c.gwid and d.lx_dm=202 where a.lxid='"+yhid+"' and a.lx_dm=204 )	and lx_dm=204");
			 
			
			
			List list = this.getJtA().queryForList(sb.toString());
			return list;
		}
		 /**
		   * 获取用户id所在部门的人员列表
		   * @param yhid
		   * @return
		   */
		  public List getBmYhList(String yhid){
			  List<Map> list = this.getJtN().queryForList("select distinct a.lxid as yhid,a.mc from t_org_tree a,t_org_tree b where a.sjbmid = b.sjbmid and b.lxid=? and a.lx_dm=204 and a.zt_dm=1 order by yhid",
					  new Object[] {yhid});
			  return list;
		  }
}

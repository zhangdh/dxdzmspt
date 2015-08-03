package com.coffice.app.gd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Convert;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;
import com.coffice.workflow.use.WKUser;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

public class Gd extends BaseUtil{
	JspJsonData jjd;
	LogItem logItem;
	String yhid;
	Map map;
	public Gd(Map mapIn) {
		this.jjd = new JspJsonData();
		this.logItem = new LogItem();	
		this.yhid = ((String) mapIn.get("yhid"));		
		this.logItem.setYhid(this.yhid);
		this.logItem.setClassName(super.getClass().getName());
		this.map = mapIn;
	}

	public Map queryUnDo(){
		try{
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer sqlWhere = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				PageBean page = new PageBean();
				if(map.get("query_page")!=null){
					page.setPageGoto((String) (map.get("query_page")));
				}else{
					page.setPageGoto("1");
				}
				if(map.get("page_num")!=null){
					page.setPageSize((String) (map.get("page_num")));
				}else{
					page.setPageSize("10");
				}
				String title  = map.get("title")==null?"":String.valueOf(map.get("title"));
				if(!"".equals(title)){
					sqlWhere.append(" and formname like '%").append(title).append("%' ");
				}
				sqlStr.append("select slbh_day,b.id,b.Entry_ID enid,b.step_id curid,formname undo_title,ldrq ")
				  .append(" from t_form1305 a,t_oswf_work_item b ")
				  .append(" where a.slbh= b.entry_id and b.value='").append(yhid).append("'").append(sqlWhere);
				sqlCount.append("select count(*) from t_form1305 a,t_oswf_work_item b where a.slbh= b.entry_id and value='")
						.append(yhid).append("'").append(sqlWhere);
				sqlStr.append(" order by ldrq desc");
				page.setSql(sqlStr.toString());				
				page.setCountSql(sqlCount.toString());
				page.setNamedParameters(map);
				List _list = Db.getPageData(page);
				jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app查询待办时：" + guid);
			this.logItem.setMethod("app.query");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app查询待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	
	public Map queryToDo(){
		try{
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer sqlWhere = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				PageBean page = new PageBean();
				if(map.get("query_page")!=null){
					page.setPageGoto((String) (map.get("query_page")));
				}else{
					page.setPageGoto("1");
				}
				if(map.get("page_num")!=null){
					page.setPageSize((String) (map.get("page_num")));
				}else{
					page.setPageSize("10");
				}
				String title  = map.get("title")==null?"":String.valueOf(map.get("title"));
				if(!"".equals(title)){
					sqlWhere.append(" and formname like '%").append(title).append("%' ");
				}
				sqlStr.append("select a.id ,slbh_day,b.Entry_ID enid,b.step_id curid,formname undo_title,ldrq ")
				  .append(" from t_form1305 a,t_oswf_work_item_his b ")
				  .append(" where a.slbh= b.entry_id and b.value='").append(yhid).append("'").append(sqlWhere);
				sqlCount.append("select count(*) from t_form1305 a,t_oswf_work_item_his b where a.slbh= b.entry_id and value='")
						.append(yhid).append("'").append(sqlWhere);
				sqlStr.append(" order by ldrq desc");
				page.setSql(sqlStr.toString());				
				page.setCountSql(sqlCount.toString());
				page.setNamedParameters(map);
				List _list = Db.getPageData(page);
				jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app查询待办时：" + guid);
			this.logItem.setMethod("app.queryToDo");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app查询已办办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	
	public Map showForm(){
		try{
			String username=yhid;
			String dbid = map.get("id")==null?"":String.valueOf(map.get("id"));
			long entryId=0;
			int stepId=0;
			String workid="";
			String stepName="";
			String wkName="";
			Map _map=null;
			ArrayList _list=new ArrayList();
			List<Map> dbList = Db.getJtN().queryForList("select Entry_ID ,Step_ID,workid from t_oswf_work_item where id = '"+dbid+"'");
			for(Map map:dbList){
				workid=String.valueOf(map.get("workid"));
				entryId=Long.parseLong(String.valueOf(map.get("entry_id")));
				stepId=Integer.parseInt(String.valueOf(map.get("step_id")));
			}
			Workflow wf = new BasicWorkflow(username);
			wkName=wf.getWorkflowName(entryId);
		    WorkflowDescriptor wd = wf.getWorkflowDescriptor(wkName);
			StepDescriptor stepDe = (StepDescriptor) wd.getStep(stepId);
			stepName=stepDe.getName();//步骤名称
			List list=stepDe.getActions();/*本步骤的所有动作*/
			int[] actions = wf.getAvailableActions(entryId, null);
			String bllx = map.get("bllx")==null?"-1":String.valueOf(map.get("bllx"));
			 /*查找可以执行的动作*/
		    for (int i = 0; i < actions.length; i++) {
		    	 for (int j = 0; j < list.size(); j++) {
		    		 ActionDescriptor action = (ActionDescriptor) list.get(j);
		    		 int id = action.getId();
		    		 if(actions[i]==action.getId()){
		    			 if("1".equals(bllx) && stepId == 98){
		    				 //重办时如办理类型是市级，则隐藏县级重办
		    				 if(action.getId() == 9803){
		    					 break;
		    				 }
		    			 }
		    			 if("0".equals(bllx) && stepId == 98){
		    				 //重办时如办理类型是县级，则隐藏市级重办
		    				 if(action.getId() == 9802){
		    					 break;
		    				 }
		    			 }
		    			 _map=new HashMap();
		    			 _map.put("actionid", action.getId());
		    			 _map.put("actionname", action.getName());
		    			 _list.add(_map);
		    			 break;
		    		 }
		    	 }
		    }
		    WorkFlowMethod wfMethod=new WorkFlowMethod();
		    String formId=wfMethod.findFormIdByBusiness((int)entryId,stepId);
		    Map formMap = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formId+"'");
		    List<Map> formList = Db.getJtN().queryForList("select a.id ,a.lb fieldlb,componentname fieldname,componenttype fieldtype,stypeid,datatype,a.remark field,role fieldauth from t_form_columntype a,t_oswf_nodeform_config b where a.tdid = b.tdid and b.stepID = "+stepId);
		    for(Map map_:formList){
		    	if("TEXTAREA".equals(String.valueOf(map_.get("fieldtype"))) && "1801".equals(String.valueOf(map_.get("fieldlb")))){
		    		//会签
		    		String columnid = String.valueOf(map_.get("id"));
		    		List attitudeList = Db.getJtN().queryForList("select content,insertdate,(select xm from t_org_yh where yhid = logincode) xm from t_form_attitude where formid='"+formId+"' and columnid='"+columnid+"'");
		    		map_.put("fieldvalue", attitudeList);
		    	}else{
		    		map_.put("fieldvalue", formMap.get(map_.get("fieldname").toString()));
		    	}
		    	if("SELECT".equals(map_.get("fieldtype"))){
		    		String stypeid = String.valueOf(map_.get("stypeid"));
		    		List sList = Db.getJtN().queryForList("select dm,mc from t_dm where lb_dm = '"+stypeid+"' and zt_dm=1 order by dm");
		    		map_.put("fieldvalues", sList);
		    	}
		    }
		    jjd.setExtend("form", formList.toArray());
		    jjd.setExtend("entryId", entryId);
		    jjd.setExtend("stepId", stepId);
		    jjd.setExtend("formid", formId);
		    jjd.setExtend("actions", _list.toArray());
		    jjd.setExtend("workid",workid);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app查询待办时：" + guid);
			this.logItem.setMethod("gd.showForm");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app查询待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	
	public Map showToDoForm(){
		try{
			String username=yhid;
			String formid = map.get("id")==null?"":String.valueOf(map.get("id"));
			String  entryId="0";
		    Map formMap = Db.getJtN().queryForMap("select * from t_form1305 where id = '"+formid+"'");
		    entryId = String.valueOf(formMap.get("slbh"));
		    List<Map> formList = Db.getJtN().queryForList("select a.id ,a.lb fieldlb,componentname fieldname,componenttype fieldtype,stypeid,datatype,a.remark field from t_form_columntype a where tableid='1305' ");
		    for(Map map_:formList){
		    	if("TEXTAREA".equals(String.valueOf(map_.get("fieldtype"))) && "1801".equals(String.valueOf(map_.get("fieldlb")))){
		    		//会签
		    		String columnid = String.valueOf(map_.get("id"));
		    		List attitudeList = Db.getJtN().queryForList("select content,insertdate,(select xm from t_org_yh where yhid = logincode) xm from t_form_attitude where formid='"+formid+"' and columnid='"+columnid+"'");
		    		map_.put("fieldvalue", attitudeList);
		    	}else{
		    		map_.put("fieldvalue", formMap.get(map_.get("fieldname").toString()));
		    	}
		    	if("SELECT".equals(map_.get("fieldtype"))){
		    		String stypeid = String.valueOf(map_.get("stypeid"));
		    		List sList = Db.getJtN().queryForList("select dm,mc from t_dm where lb_dm = '"+stypeid+"' and zt_dm=1 order by dm");
		    		map_.put("fieldvalues", sList);
		    	}
		    }
		    jjd.setExtend("form", formList.toArray());
		    jjd.setExtend("entryId", entryId);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "app显示已办表单数据时：" + guid);
			this.logItem.setMethod("gd.showToDoForm");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("app显示已办表单数据异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	
	public Map queryHis() {
		String id="";
		StringBuffer sqlStr = new StringBuffer();
		try{
			id = map.get("enid")==null?"-1":String.valueOf(map.get("enid"));
			sqlStr.append("select clr,stepname,actionname,zt,dzsj from(");
			sqlStr.append("select xm clr,stepname,actionname, cjsj dzsj,0 xh,'完成' zt from t_oswf_historystep where entryid=").append(id);
			sqlStr.append(" union all ");
			sqlStr.append("select (select xm from t_org_yh where yhid = a.value) clr,(select top 1 stepname from t_oswf_node_info where stepid= a.Step_ID) stepname,'' actionname,cjsj dzsj,1 xh,'待处理' zt from t_oswf_work_item a where Entry_ID=").append(id);
			sqlStr.append(") a order by xh,dzsj asc");
			List list = getJtN().queryForList(sqlStr.toString());
			jjd.setExtend("hislist", list);
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("查看流程实例：[").append(id).append("]历史时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}	
		return jjd.getData();
	}
	public Map showUsers() {
		String id="";
		StringBuffer sqlStr = new StringBuffer();
		try{
			/*String nextStep = (String) Cache.getUserInfo(yhid, "nextStep");//下一步骤id
			String  workcondition = (String)Cache.getUserInfo(yhid, "workcondition"); // 0(多人)1(单人)
			String  amount = (String)Cache.getUserInfo(yhid, "amount"); // 允许的人数
*/			String nextStep =  (String) map.get("nextStep");
			String workcondition =  (String) map.get("workcondition");
			String amount =  (String) map.get("amount");
			
			String entryid =  (String) map.get("enid");
			//String stepid =sh (String) map.get("stepid");
			String workid = (String) map.get("workid");
			WKUser su = new WKUser();
			String[] spr=su.getOswfClrInfoKz(workid,nextStep);
			List list = su.getYhTree(spr[1],spr[0],spr[2], yhid,nextStep);
			jjd.setExtend("workcondition", workcondition);
			jjd.setExtend("amount", amount);
			jjd.setExtend("users", list);
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("查看流程实例：[").append(id).append("]历史时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}	
		return jjd.getData();
	}
}

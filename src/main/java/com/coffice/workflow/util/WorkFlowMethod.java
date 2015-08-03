package com.coffice.workflow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.coffice.workflow.condition.table.date.NodepropertconditionData;
import com.coffice.workflow.condition.table.date.NodepropertyuserData;
import com.coffice.workflow.condition.table.date.WorkitemData;
import com.coffice.workflow.condition.table.date.config_nodeformData;
import com.coffice.form.bean.ValidateInfo;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.TransSql;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;

public class WorkFlowMethod extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	Map map;
	public WorkFlowMethod(){
		jjd = new JspJsonData();
		logItem = new LogItem();
	}
	public WorkFlowMethod(Map mapIn){
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
	 /** 保存业务和表单的关系*/
	public ArrayList saveBusinessForm(String businessId,String stepId,String formId,String version){
		ArrayList stepIds=new ArrayList();
		String sql="insert into t_oswf_busi_form_relation(Id,businessId,stepID,formId,ver) values('"+Guid.get()+"',"+businessId+","+stepId+",'"+formId+"',"+version+")";
		try {
			this.getJtN().update(sql);
			logItem.setMethod("saveBusinessForm");
			logItem.setLevel("info");
			logItem.setContent("保存业务和表单的关系成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("saveBusinessForm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存业务和表单的关系时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return stepIds;
	}
	/** 根据业务id查询表单id*/
	public String  findFormIdByBusiness(int businessId){
		String formId="";
		String sql="select distinct formId from  t_oswf_busi_form_relation where businessId="+businessId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				formId=String.valueOf(map.get("formId"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findFormIdByBusiness");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 根据业务id查询表单id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return formId;
	}
	
	/** 根据业务id查询表单id*/
	public String  findFormIdByBusiness(int businessId,int stepId){
		ArrayList stepIds=new ArrayList();
		String formId="";
		//因为不知道什么原因造成t_oswf_busi_form_relation表中某些step的formid 不正常，所以查询时直接查询得到首节点的formid值
		//String sql="select formId from  t_oswf_busi_form_relation where businessId="+businessId +" and stepID="+stepId;
		String sql="select formId from  t_oswf_busi_form_relation where businessId="+businessId +" and stepID='3' ";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				formId=String.valueOf(map.get("formId"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findFormIdByBusiness");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 根据业务id查询表单id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return formId;
	}
	/**
	 * 根据流程id、步骤id得到功能列表
	 */
	public Map getFuncConfig(String workid,int stepid,String entryid){
		String sql="select funcConfig,buttonType from t_oswf_nodecondition_config where workid='"+workid+"' and stepid='"+stepid+"'";
		Map _map=new HashMap();
		List<Map> firstList=null;
		try {	    
			List<Map> list=this.getJtA().queryForList(sql);
			for(Map map:list){
				firstList=getFuncList(String.valueOf(map.get("buttonType")));
			}
			_map.put("funcList", firstList);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFuncConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("根据流程id、步骤id得到功能列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return _map;
	}
	/**
	 * 得到功能列表
	 * @param funcid
	 * @return
	 */
	public List getFuncList(String funcid){
		String sql="select * from t_oswf_node_function_dm where id in("+funcid+")";
		List<Map> list=null;
		try {
			list=this.getJtA().queryForList(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 根据实例id得到流程id
	 * @param entryid
	 * @return
	 */
	public String queryWorkidByEntryid(String entryid){
		String workid="";
		try {
			StringBuffer sqlStr=new StringBuffer();
			sqlStr.append("select workid from t_oswf_busi_relation where businessId=?");
			List<Map> list=this.getJtN().queryForList(sqlStr.toString(),new Object[]{entryid});
			for(Map _map:list){
				workid=(String)_map.get("workid");
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryWorkidByEntryid");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("根据实例id得到流程id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return workid;
	}
	/** 根据流程id查询模板名称*/
	public String  findTableNameById(String  workid){
		String templateName="";
		String sql="select tableName from t_form_template_info  where id=(select distinct formId from  t_oswf_nodeform_config where workid='"+workid+"')";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				templateName=String.valueOf(map.get("tableName"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findTemplateById");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 根据流程id查询模板id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return templateName;
	}
	/*查询节点任务分配设置*/
	public Map findNodeTaskAllocation(int stepId,int businessId){
		Map _map=new HashMap();
		String sql="select taskAllocation,workcondition,amount,remind,isCuib from t_oswf_nodecondition_config n ,t_oswf_busi_relation b " +
		"where n.stepID='"+stepId+"' and b.businessId='"+businessId+"' and b.workid=n.workid ";
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				_map.put("taskAlltocation",map.get("taskAllocation"));
				_map.put("workcondition",map.get("workcondition"));
				_map.put("amount",map.get("amount"));
				_map.put("remind",map.get("remind"));
				_map.put("isCuib",map.get("isCuib"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeTaskAllocation(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点任务分配设置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return _map;
	}
	/*查询节点任务分配设置*/
	public Map findNodeTaskAllocation2(int stepId,String workId){
		Map _map=new HashMap();
		String sql="select taskAllocation,workcondition,amount,remind,isCuib from t_oswf_nodecondition_config where stepID='"+stepId+"' and workId='"+workId+"'";
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				_map.put("taskAlltocation",map.get("taskAllocation"));
				_map.put("workcondition",map.get("workcondition"));
				_map.put("amount",map.get("amount"));
				_map.put("remind",map.get("remind"));
				/*_map.put("isCuib",map.get("isCuib"));*/
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeTaskAllocation2(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点任务分配设置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return _map;
	}
	
	/*查询节点配置*/
	public ArrayList findNodeUser(int stepId,int businessId){

		String sql = "select n.id,n.workId,n.stepID,n.userId,n.status,n.doType,n.con_dm from t_oswf_noderole_config n ," +
				"t_oswf_busi_relation b where b.businessId="+businessId+" and stepID="+stepId+" and b.workId=n.workId ";
		ArrayList<NodepropertyuserData> dateList=new ArrayList<NodepropertyuserData>();
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				NodepropertyuserData data=new NodepropertyuserData();
				data.id=String.valueOf(map.get("Id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.userId=String.valueOf(map.get("userId"));
				data.status=String.valueOf(map.get("status"));
				data.doType=String.valueOf(map.get("doType"));
				data.conDm=Integer.parseInt(String.valueOf(map.get("con_dm")));
				dateList.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeUser(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return dateList;
	}
	/*查询节点配置优化新增*/
	public ArrayList findNodeUserKz(int stepId,String workId){

		String sql = "select n.id,n.workId,n.stepID,n.userId,n.status,n.doType,n.con_dm from t_oswf_noderole_config n " +
				" where n.workId='"+workId+"' and stepID="+stepId;
		ArrayList<NodepropertyuserData> dateList=new ArrayList<NodepropertyuserData>();
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				NodepropertyuserData data=new NodepropertyuserData();
				data.id=String.valueOf(map.get("Id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.userId=String.valueOf(map.get("userId"));
				data.status=String.valueOf(map.get("status"));
				data.doType=String.valueOf(map.get("doType"));
				data.conDm=Integer.parseInt(String.valueOf(map.get("con_dm")));
				dateList.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeUser(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return dateList;
	}
	
	/*查询节点配置另外*/
	public List findNodeUser1(int stepId,int businessId){

		String sql = "select n.id,n.workId,n.stepID,n.userId,n.status,n.doType,n.con_dm from t_oswf_noderole_config n ," +
				"t_oswf_busi_relation b where b.businessId="+businessId+" and stepID="+stepId+" and b.workId=n.workId ";
		ArrayList<NodepropertyuserData> dateList=new ArrayList<NodepropertyuserData>();
		List<Map> list = new ArrayList();
		try {
			list=this.getJtN().queryForList(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeUser(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/*查询节点配置另外*/
	public List findNodeUser2(int stepId,String workid){

		String sql = "select id,workId,stepID,userId,status,doType,con_dm from t_oswf_noderole_config where stepID="+stepId+" and workId='"+workid+"'";
		ArrayList<NodepropertyuserData> dateList=new ArrayList<NodepropertyuserData>();
		List<Map> list = new ArrayList();
		try {
			list=this.getJtN().queryForList(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodeUser(int stepId,int businessId)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	
	/*指定节点处理用户的插入*/
	public void insertNodeUser(String workid,int stepId,int businessId,String userId,String ywid_tx,String ywid_cb){
		StringBuffer strSql=new StringBuffer();
		String fqz="";
		String sql = "insert into t_oswf_work_item_role(id,workid,businessId,stepID,userId,status,remind_id,cuib_id)values('"+Guid.get()+"','"+workid+"',"+businessId
		+","+stepId+",'"+userId+"','0','"+ywid_tx+"','"+ywid_cb+"') ";
		try {
			//待办是否显示发启者
			/*if(SysPara.compareValue("oswf_work_show_fqr","1")){
				if("-1".equals(ywid_cb)){//首节点
					strSql.delete(0, strSql.length());
					strSql.append("select xm from t_org_yh where yhid=?");
					Map map1=this.getJtN().queryForMap(strSql.toString(),new Object[]{userId});
					fqz=(String)map1.get("xm");
					sql = "insert into t_oswf_work_item_role(id,workid,businessId,stepID,fqz,userId,status,remind_id,cuib_id)values('"+Guid.get()+"','"+workid+"',"+businessId
					+","+stepId+",'"+fqz+"','"+userId+"','0','"+ywid_tx+"','"+ywid_cb+"') ";
				}else{
					strSql.delete(0, strSql.length());
					strSql.append("select distinct fqz from t_oswf_work_item where entry_id=?");
					List<Map> list=this.getJtN().queryForList(strSql.toString(),new Object[]{businessId});
					if(list.size()!=0){
						for(Map _map:list){
							fqz=(String)_map.get("fqz");
						}
					}
					sql = "insert into t_oswf_work_item_role(id,workid,businessId,stepID,fqz,userId,status,remind_id,cuib_id)values('"+Guid.get()+"','"+workid+"',"+businessId
					+","+stepId+",'"+fqz+"','"+userId+"','0','"+ywid_tx+"','"+ywid_cb+"') ";
				}
			}*/
			int i=this.getJtN().update(sql);
			logItem.setMethod("insertNodeUser");
			logItem.setLevel("info");
			logItem.setContent("保存指定节点处理用户信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertNodeUser(String workid,int stepId,int businessId,String userId,String ywid_tx,String ywid_cb)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存指定节点处理用户信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	/**
	 * 判断当前节点是否可以回退，分支节点有一人审核通过则不能回退
	 * @param entry_id
	 * @param stepIdVal
	 * @return
	 */
	public boolean isAbleHuiTui(String entry_id,String stepIdVal){
		List<Map> list = this.getJtN().queryForList("select previous_id from os_currentstep_prev where id =(select id from os_currentstep where entry_id="+entry_id+" and step_id="+stepIdVal+")");
		String previous_ids = "";
		for(Map _map:list){
			previous_ids = previous_ids+","+_map.get("previous_id");
		}
		previous_ids = previous_ids.replaceFirst(",", "");
		list = this.getJtN().queryForList("select * from os_historystep_prev where previous_id in("+previous_ids+")");
		if(list.size()>0){//不能回退
			return false;
		}
		return true;
	}
	/*查询流程的当前步骤历史表，找出回退时的待办人*/
	/**
	 * &id 
	 * 
	 * */
	public String  findos_historystep(String entry_id, String stepIdVal,JDBCWorkflowStore store,String yhid){
		String user="";
		StringBuffer sqlStr=new StringBuffer();
		ArrayList previous=new ArrayList();
		long currentId=0;//当前id值
		ArrayList previousId=new ArrayList();//上一步骤id值
		String previousIds="";
		String stepids="";
		String tempsql="";
		String oswf_work_show_fqr="0";
		try{
			//根据实例id和步骤id得到当前记录id值
			sqlStr.append("select ID from os_currentstep where ENTRY_ID='").append(entry_id).append("' and STEP_ID='").append(stepIdVal).append("'");
			List<Map> list=this.getJtN().queryForList(sqlStr.toString());
			for(Map map:list){
				currentId=Long.parseLong(String.valueOf(map.get("ID")));;
			}
			//根据当前记录id值得到上一步骤id值
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("select distinct PREVIOUS_ID from os_currentstep_prev where ID='").append(currentId).append("'");
			List<Map> list1=this.getJtN().queryForList(sqlStr.toString());
			for(Map map:list1){
				previousId.add(Long.parseLong(String.valueOf(map.get("PREVIOUS_ID"))));
				previousIds = previousIds + "," + map.get("PREVIOUS_ID");
			}
			previousIds = previousIds.replaceFirst(",", "");
			Configuration config = new DefaultConfiguration();
			
			config.load(getClass().getResource("/osworkflow.xml")); 
			JDBCWorkflowStore workflowStore=(JDBCWorkflowStore)config.getWorkflowStore();
			
			//获取要删除的当前id
			String delIds = "";
			sqlStr.setLength(0);
			sqlStr.append("select id  from os_currentstep_prev where PREVIOUS_ID in(").append(previousIds).append(")");
			list = this.getJtN().queryForList(sqlStr.toString());
			for (Map _map : list) {
				delIds = delIds + "," + _map.get("id");
			}
			//获取要删除待办的stepid
			String delStepIds = "";
			sqlStr.setLength(0);
			sqlStr.append("select STEP_ID  from os_currentstep where id in(").append(delIds.replaceFirst(",", "")).append(")");
			list = this.getJtN().queryForList(sqlStr.toString());
			for (Map _map : list) {
				delStepIds = delStepIds + "," + _map.get("STEP_ID");
			}
			
			sqlStr.setLength(0);
			sqlStr.append("delete from os_currentstep_prev where PREVIOUS_ID in(").append(previousIds).append(")");
			this.getJtN().update(sqlStr.toString());
			
			sqlStr.setLength(0);
			sqlStr.append("delete from os_currentstep where id in (").append(delIds.replaceFirst(",", "")).append(")");
			this.getJtN().update(sqlStr.toString());
			
			
			String os_currentstep = "";//回退的当前节点的id
			for(int i=0;i<previousId.size();i++){
				long huitid=workflowStore.getNextStepSequence(Db.getDs().getConnection());
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("insert into os_currentstep(ID,ENTRY_ID,STEP_ID,ACTION_ID,OWNER,START_DATE,FINISH_DATE,DUE_DATE,STATUS,CALLER) ");
				sqlStr.append("select '").append(huitid).append("',ENTRY_ID,STEP_ID,ACTION_ID,OWNER,now(),FINISH_DATE,DUE_DATE,'Underway',CALLER ");
				sqlStr.append("from os_historystep ");
				sqlStr.append("where id=").append(previousId.get(i));
				tempsql=TransSql.trans(sqlStr.toString());
				this.getJtN().update(tempsql);
				
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("insert into os_currentstep_prev(ID,PREVIOUS_ID) ");
				sqlStr.append("select '").append(huitid).append("',PREVIOUS_ID ");
				sqlStr.append("from os_historystep_prev ");
				sqlStr.append("where ID=").append(previousId.get(i));
				this.getJtN().update(sqlStr.toString());
				
				this.getJtN().update("delete from os_historystep_prev where id="+previousId.get(i));
				this.getJtN().update("delete from os_historystep where id="+previousId.get(i));
				
				os_currentstep = os_currentstep +","+huitid;
			}
			//删除t_oswf_work_item
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("delete from t_oswf_work_item where Entry_ID=").append(entry_id).append(" and Step_ID in (").append(delStepIds.replaceFirst(",", "")).append(")") ;
			this.getJtN().update(sqlStr.toString());
			
			//插入t_oswf_work_item
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("select Step_ID from os_currentstep where id in(").append(os_currentstep.replaceFirst(",", "")).append(")");
			List<Map> list11=this.getJtN().queryForList(sqlStr.toString());
			if(SysPara.compareValue("oswf_work_show_fqr","1")){
				oswf_work_show_fqr="1";
			}
			String oswf_work_notable_ywid = "0";
			if(SysPara.compareValue("oswf_work_notable_ywid","1")){
				oswf_work_notable_ywid="1";
			}
			for(Map _map:list11){
				sqlStr.delete(0, sqlStr.length());
				String workItemId=Guid.get();
				if(oswf_work_show_fqr.equals("1")){
					if(oswf_work_notable_ywid.equals("1")){
						sqlStr.append("insert into t_oswf_work_item(Id,zzid,bmid,gwid,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj,ywid) ");
						sqlStr.append("select distinct '").append(workItemId).append("',zzid,bmid,gwid,workid,Entry_ID,Step_ID,type,fqz,Value,'1',trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,now(),ywid ");
						sqlStr.append("from t_oswf_work_item_his ");
						sqlStr.append("where Entry_ID='").append(entry_id).append("' and Step_ID ='").append(String.valueOf(_map.get("Step_ID"))).append("' ");
				
					}else{
						sqlStr.append("insert into t_oswf_work_item(Id,zzid,bmid,gwid,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
						sqlStr.append("select distinct '").append(workItemId).append("',zzid,bmid,gwid,workid,Entry_ID,Step_ID,type,fqz,Value,'1',trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,now() ");
						sqlStr.append("from t_oswf_work_item_his ");
						sqlStr.append("where Entry_ID='").append(entry_id).append("' and Step_ID ='").append(String.valueOf(_map.get("Step_ID"))).append("' ");
					}
				}else{
					if(oswf_work_notable_ywid.equals("1")){
						sqlStr.append("insert into t_oswf_work_item(Id,zzid,bmid,gwid,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj,ywid) ");
						sqlStr.append("select distinct '").append(workItemId).append("',zzid,bmid,gwid,workid,Entry_ID,Step_ID,type,Value,'1',trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,now(),ywid ");
						sqlStr.append("from t_oswf_work_item_his ");
						sqlStr.append("where Entry_ID='").append(entry_id).append("' and Step_ID ='").append(String.valueOf(_map.get("Step_ID"))).append("' ");
				  }else{
						sqlStr.append("insert into t_oswf_work_item(Id,zzid,bmid,gwid,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
						sqlStr.append("select distinct '").append(workItemId).append("',zzid,bmid,gwid,workid,Entry_ID,Step_ID,type,Value,'1',trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,now() ");
						sqlStr.append("from t_oswf_work_item_his ");
						sqlStr.append("where Entry_ID='").append(entry_id).append("' and Step_ID ='").append(String.valueOf(_map.get("Step_ID"))).append("' ");
						
					}
			  }
				tempsql=TransSql.trans(sqlStr.toString());
				this.getJtN().update(tempsql);
				stepids=stepids+","+String.valueOf(_map.get("Step_ID"));

			}
			
		
			//删除t_oswf_work_item_his
			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("delete from t_oswf_work_item_his ");
			sqlStr.append("where Entry_ID=").append(entry_id).append(" and Step_ID in (").append(stepids.replaceFirst(",", "")).append(")");
			this.getJtN().update(sqlStr.toString());
			
			if(!"".equals(stepids)){
				stepids=stepids.replaceFirst(",", "");
			}
			StringBuffer sb=new StringBuffer();
			sb.append("查询流程的当前步骤历史表，找出回退时的待办人成功");
			log.warn(sb.toString());

		}catch(Exception e){
			String msg=new StringBuffer("查询流程的当前步骤历史表，找出回退时的待办人时出现异常:").append(e.toString()).toString();
			log.error(msg);
		}
		 return stepids;
		
	}
	/** 根据流程id查询模板名称*/
	public String  findTemplateById(String  workid){
		String templateName="";
		String sql="select templateName from t_form_template_info  where id=(select distinct formId from  t_oswf_nodeform_config where workid='"+workid+"')";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				templateName=String.valueOf(map.get("templateName"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findTemplateById");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 根据流程id查询模板id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return templateName;
	}
	/* 插入业务和流程关联的表 */
	public boolean insertBusinessShip( long businessId,String workId) {
		boolean rt=false;
		String sql = "insert into t_oswf_busi_relation (id,businessId,workId)VALUES('"+Guid.get()+"',"
				+ businessId + ",'" + workId + "')";
		try {
			int i=this.getJtN().update(sql);
			if(i>0){
				rt=true;
			}
			logItem.setMethod("insertBusinessShip");
			logItem.setLevel("info");
			logItem.setContent("保存业务流程信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertBusinessShip");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存业务流程信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return rt;
	}
	/*查询待办任务*/
	public ArrayList findWorkitem(String user){
		WorkflowDescriptor wd=null;
		StepDescriptor step =null;
		String sql = "select w.Id,w.Entry_ID,w.Step_ID,w.type,w.value,w.trust,d.remark " +
		"from t_oswf_work_item w,t_oswf_busi_relation b, t_oswf_def d " +
		" where value='"+user+"' and trust<>'0' and w.Entry_ID=b.businessId and d.id=b.workId";
		ArrayList dataList=new ArrayList();
		try {
			Workflow wf = new BasicWorkflow(user);
			List<Map> list=this.getJtA().queryForList(sql);
			for(Map map:list){
				WorkitemData data=new WorkitemData();
				data.id=String.valueOf(map.get("Id"));
				data.entry_ID=Integer.parseInt(String.valueOf(map.get("Entry_ID")));
				data.step_ID=Integer.parseInt(String.valueOf(map.get("Step_ID")));
				wd = wf.getWorkflowDescriptor(wf.getWorkflowName(data.entry_ID));
				step = (StepDescriptor) wd.getStep(data.step_ID);
				data.name=step.getName();
				data.type=Integer.parseInt(String.valueOf(map.get("type")));
				data.value=(String)map.get("value");
				data.trust=(String)map.get("trust");
				data.remark=(String)map.get("remark");
				dataList.add(data);
			}
			//委托的
			sql="select w.Id,w.Entry_ID,w.Step_ID,w.type,w.value,w.trust,d.remark " +
			"from t_oswf_work_item  w ,t_oswf_trust_flow_record t,t_oswf_busi_relation b ,t_oswf_def d where t.mandatary='"
			+user+"' and w.trust='0' and  t.trustType='0' and t.businessId=w.Entry_ID " +
				"and w.Step_ID=t.stepId and w.Entry_ID=b.businessId and b.workId=d.id " +
				"and t.takeBackDate is null ";
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				WorkitemData data=new WorkitemData();
				data.id=String.valueOf(map.get("Id"));
				data.entry_ID=Integer.parseInt(String.valueOf(map.get("Entry_ID")));
				data.step_ID=Integer.parseInt(String.valueOf(map.get("Step_ID")));
				wd = wf.getWorkflowDescriptor(wf.getWorkflowName(data.entry_ID));
				step = (StepDescriptor) wd.getStep(data.step_ID);
				data.name=step.getName();
				data.type=Integer.parseInt(String.valueOf(map.get("type")));
				data.value=(String)map.get("value");
				data.trust=(String)map.get("trust");
				data.remark=(String)map.get("remark");
				dataList.add(data);
			}
			//流程委托
			sql="select w.Id,w.Entry_ID,w.Step_ID,w.type,w.value,t.trustType,d.remark " +
			"from t_oswf_work_item  w ,t_oswf_trust_flow_record t,t_oswf_busi_relation b ,t_oswf_def d where t.mandatary='"
			+user+"' and w.trust='0' and  t.trustType='1'  and w.Step_ID=t.stepId  " +
				"and w.Entry_ID in(select businessId from t_oswf_busi_relation where workId=t.businessId) " +
				"and w.Entry_ID=b.businessId and b.workId=d.id and t.takeBackDate is null";
			List<Map> list2=this.getJtA().queryForList(sql);
			for(Map map:list2){
				WorkitemData data=new WorkitemData();
				data.id=String.valueOf(map.get("Id"));
				data.entry_ID=Integer.parseInt(String.valueOf(map.get("Entry_ID")));
				data.step_ID=Integer.parseInt(String.valueOf(map.get("Step_ID")));
				wd = wf.getWorkflowDescriptor(wf.getWorkflowName(data.entry_ID));
				step = (StepDescriptor) wd.getStep(data.step_ID);
				data.name=step.getName();
				data.type=Integer.parseInt(String.valueOf(map.get("type")));
				data.value=(String)map.get("value");
				data.trust=(String)map.get("trust");
				data.remark=(String)map.get("remark");
				dataList.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findWorkitem(String user)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询待办任务时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return dataList;
	}
	/**
	 * 更新待办标题
	 * @param businessid
	 * @return
	 */
	public boolean updateWorkitemTitle(String businessid,String bt){
		boolean rt=false;
		StringBuffer strSql=new StringBuffer();
		try {
			strSql.append("update t_oswf_work_item set formname='").append(bt).append("' where id='").append(businessid).append("'");
			int i=this.getJtN().update(strSql.toString());
			if(i!=0){
				rt=true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("updateWorkitemTitle");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("更新待办标题时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return rt;
	}
	/*查询待办任务*/
	public ArrayList findWorkitem(long Entry_ID,int Step_ID){
		
		String sql = "select * from t_oswf_work_item where Entry_ID='"+Entry_ID+"' and Step_ID="+Step_ID+"";
		ArrayList dateList=new ArrayList();
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				WorkitemData data=new WorkitemData();
				data.id=String.valueOf(map.get("Id"));
				data.entry_ID=Integer.parseInt(String.valueOf(map.get("Entry_ID")));
				data.step_ID=Integer.parseInt(String.valueOf(map.get("Step_ID")));
				data.type=Integer.parseInt(String.valueOf(map.get("type")));
				data.value=String.valueOf(map.get("value"));
				data.trust=String.valueOf(map.get("trust"));
				data.status=String.valueOf(map.get("status"));
				dateList.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findWorkitem(long Entry_ID,int Step_ID)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询待办任务时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return dateList;
	}
	/*查询待办任务*/
	public ArrayList findWorkitem(long Entry_ID,int Step_ID,String name){
		
		String sql = "select * from t_oswf_work_item where Entry_ID='"+Entry_ID+"' and Step_ID="+Step_ID+" and value='"+name+"'";
		ArrayList dateList=new ArrayList();
		try {
			List<Map> list=this.getJtN().queryForList(sql);
			for(Map map:list){
				WorkitemData data=new WorkitemData();
				data.id=String.valueOf(map.get("Id"));
				data.entry_ID=Integer.parseInt(String.valueOf(map.get("Entry_ID")));
				data.step_ID=Integer.parseInt(String.valueOf(map.get("Step_ID")));
				data.type=Integer.parseInt(String.valueOf(map.get("type")));
				data.value=(String)map.get("value");
				data.trust=(String)map.get("trust");
				dateList.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findWorkitem(long Entry_ID,int Step_ID,String name)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询待办任务时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return dateList;
	}
	/**
	 * 读取工作流文件名
	 * @param workId
	 * @return
	 */
	public String getWkName(String workId){
		String name="";
		try {
			String sql="select name from t_oswf_def where id='"+workId+"' ";
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				name=String.valueOf(map.get("name"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getWkName");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取工作流文件名时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return name;
	}
	/**插入流程定义*/
	public String  insertWorkdef(String zzid,String bmid,String gwid,String name,String remark ,String user,String wkTypeId){
		String workId="";
		logItem = new LogItem();
		try {
		    String sql = "";
		    long versionId=1;//this.getNextVersion();
		    workId=Guid.get();
		    sql="insert into t_oswf_def(zzid,bmid,gwid,yhid,id,name,remark,version,versionId,op_user,op_date,isuse,lclb_dm)" +
			"VALUES('"+zzid+"','"+bmid+"','"+gwid+"','"+user+"','"+workId+"','"+ name + "','" + remark +"','"+1.0+"',"+versionId+",'"+user+"',now(),1,"+wkTypeId+")";
			this.getJtA().update(sql);
			logItem.setMethod("insertWorkdef");
			logItem.setLevel("info");
			logItem.setDesc("保存流程成功");
			Log.write(logItem);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertWorkdef(String,String,String)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存流程时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return workId;
		
	}
	/**插入流程定义 标记版本号*/
	public String  insertWorkdef(String zzid,String bmid,String gwid,String pworkId,String name,String remark ,String user,String wkTypeId,String ifNewWk){
		String workId="";
		logItem = new LogItem();
		try {
		    String sql = "";
		    String use="";
		    String version="1.0";
		    String versionId="";
	    	sql="select isuse,version,versionId,remark from t_oswf_def where id='"+pworkId+"'";
			List<Map> list=this.getJtA().queryForList(sql);
			for(Map map:list){
				use=String.valueOf(map.get("isuse"));
				versionId=String.valueOf(map.get("versionId"));
				remark=String.valueOf(map.get("remark"));
				//version=getNextVersion(remark)+"";
				version=String.valueOf(map.get("version"));
			}
			if(use.equals("1")){//未启用流程修改
				sql="delete from t_oswf_node_info where workid='"+pworkId+"'";
				this.getJtA().update(sql);
				sql="delete from t_oswf_nodeform_config where workId='"+pworkId+"'";
				this.getJtA().update(sql);
				sql="delete from t_oswf_nodecondition_config where workId='"+pworkId+"'";
				this.getJtA().update(sql);
				sql="delete from  t_oswf_noderole_config where workId='"+pworkId+"'";
				this.getJtA().update(sql);
				sql="delete from t_oswf_def where id='"+pworkId+"'";
				this.getJtA().update(sql);
				sql="delete from t_oswf_nodeformfunc_config where workid='"+pworkId+"'";
				this.getJtA().update(sql);
				//判断是否可用子流程
				if(SysPara.compareValue("oswf_usesubwf","1")){
					sql="delete from t_oswf_node_sub_config where workid='"+pworkId+"'";
					this.getJtA().update(sql);
				}
				workId=Guid.get();
			}else{
				if(ifNewWk.equals("1")){//启用流程不产生新版本
					sql="delete from t_oswf_node_info where workid='"+pworkId+"'";
					this.getJtA().update(sql);
					sql="delete from t_oswf_nodeform_config where workId='"+pworkId+"'";
					this.getJtA().update(sql);
					sql="delete from t_oswf_nodecondition_config where workId='"+pworkId+"'";
					this.getJtA().update(sql);
					sql="delete from  t_oswf_noderole_config where workId='"+pworkId+"'";
					this.getJtA().update(sql);
					sql="delete from t_oswf_def where id='"+pworkId+"'";
					this.getJtA().update(sql);
					sql="delete from t_oswf_nodeformfunc_config where workid='"+pworkId+"'";
					this.getJtA().update(sql);
					//判断是否可用子流程
					if(SysPara.compareValue("oswf_usesubwf","1")){
						sql="delete from t_oswf_node_sub_config where workid='"+pworkId+"'";
						this.getJtA().update(sql);
					}
					workId=pworkId;
				}else if(ifNewWk.equals("0")){
					version=String.valueOf(getNextVersion(remark,zzid))+".0";
			    	workId=Guid.get();
			    	use="1";
			    }
			}
			sql="insert into t_oswf_def(zzid,bmid,gwid,yhid,id,name,remark,version,versionId,op_user,op_date,isuse,lclb_dm)" +
			" VALUES('"+zzid+"','"+bmid+"','"+gwid+"','"+user+"','"+workId+"','"+ name + "','" + remark +"','"+version+"','"+versionId+"','"+user+"',now(),"+use+","+wkTypeId+")";
			this.getJtA().update(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertWorkdef(String,String,String,String)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存流程时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return workId;
	}
	/**
	 * 得到下一个版本号
	 * @return
	 */
	public long getNextVersion(String remark,String zzid){
		long version=0;
		logItem = new LogItem();
		String versionStr="";
		try {
			String sql="";
			String dbtype = SysPara.getValue("db_type");
			if(dbtype.equals("mysql")){
				sql="select max(version*1) version from t_oswf_def where zzid='"+zzid+"' and remark='"+remark+"' ";
			}else if(dbtype.equals("sqlserver")){
				sql="select max(convert(int,substring(version,0,charindex('.',version)))) version from t_oswf_def where zzid='"+zzid+"' and remark='"+remark+"' ";
			}else if(dbtype.equals("oracle")){
				sql="select max(to_number(version)) version from t_oswf_def where zzid='"+zzid+"' and remark='"+remark+"' ";
			}
//			String dbtype = SysPara.getValue("db_type");
//			if(dbtype.equals("mysql")){
//				sql="select version from t_oswf_def where remark='"+remark+"' order by op_date desc limit 1 ";
//			}else if(dbtype.equals("sqlserver")){
//				sql="select top 1 version from t_oswf_def where remark='"+remark+"' order by op_date desc ";
//			}else if(dbtype.equals("oracle")){
//				sql="select version from t_oswf_def where remark='"+remark+"' and rownum=1 order by op_date desc ";
//			}
			List<Map> list=this.getJtN().queryForList(sql);
			if(list.size()!=0){
				for(Map map:list){
					versionStr=String.valueOf(map.get("version"));
					if(versionStr.indexOf(".")!=-1){
						versionStr=versionStr.substring(0,versionStr.indexOf("."));
					}
					version=Long.parseLong(versionStr);
				}
			}
			version=version+1;
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getNextVersion(remark)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到下一个版本号时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return version;
	}
	/**
	 * 得到下一个版本号
	 * @return
	 */
	public long getNextVersion(){
		long version=0;
		logItem = new LogItem();
		try {
			String sql="";
			sql="INSERT INTO t_oswf_ver (id) select max(id)+1 from t_oswf_ver ";
			this.getJtA().update(sql);
			sql="SELECT max(id) as version FROM t_oswf_ver";
			List<Map> list=this.getJtA().queryForList(sql);
			if(list.size()!=0){
				for(Map map:list){
					version=Long.parseLong(String.valueOf(map.get("version")));
				}
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getNextVersion");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到下一个版本号时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return version;
	}
	/**
	 * 读取工作流类别ID
	 * @param workId
	 * @return
	 */
	public String getWkTypeId(String workId){
		String wkTypeId="";
		try {
			String sql="select lclb_dm from t_oswf_def where id='"+workId+"' ";
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				wkTypeId=String.valueOf(map.get("lclb_dm"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("getWkTypeId");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取工作流类别ID时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return wkTypeId;
	}
	
	/**
	 * 读取工作流名称
	 * @param workId
	 * @return
	 */
	public String getWkRemark(String workId){
		String remark="";
		try {
			String sql="select remark from t_oswf_def where id='"+workId+"' ";
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				remark=String.valueOf(map.get("remark"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("getWkRemark");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取工作流名称时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return remark;
	}
	 /** 读取流程节点配置*/
	public ArrayList getStepConfig(String workId){
		ArrayList list=new ArrayList();
		String sql="select id,workId,stepID,workcondition,amount,taskAllocation,formula," +
				"autoStep,isBack,remind,isCuib,cuibTime,funcConfig,buttonType " +
				" from t_oswf_nodecondition_config where workId='"+workId+"'";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				NodepropertconditionData data=new NodepropertconditionData();
				data.id=String.valueOf(map.get("id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.workcondition=String.valueOf(map.get("workcondition"));
				data.amount=String.valueOf(map.get("amount"));
				data.taskAllocation=String.valueOf(map.get("taskAllocation"));
				data.formula=String.valueOf(map.get("formula"));
				data.autoStep=String.valueOf(map.get("autoStep"));
				data.isBack=String.valueOf(map.get("isBack"));
				data.remind=String.valueOf(map.get("remind"));
				data.isCuib=String.valueOf(map.get("isCuib"));
				data.remindTime=String.valueOf(map.get("cuibTime"));
				data.funcconfig=String.valueOf(map.get("funcConfig"));
				data.buttonType=String.valueOf(map.get("buttonType"));
				list.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getStepConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 读取流程节点配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/**
	 * 得到节点名称
	 * @param workId
	 * @param stepId
	 * @return
	 */
	public String getNodeName(String workId ,String stepId){
		String nodeName="";
		try {
			String sql="select stepname from t_oswf_node_info where workid='"+workId+"' and stepid='"+stepId+"'";
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				nodeName=String.valueOf(map.get("stepname"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("getNodeName");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 得到节点名称时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return nodeName;
	}
	/**
	 * 得到节点类型
	 * @param workId
	 * @param stepId
	 * @return
	 */
	public String getNodeType(String workId ,String stepId){
		String nodeType="";
		try {
			String sql="select steptype from t_oswf_noderole_config where workid='"+workId+"' and stepid='"+stepId+"'";
			List<Map> _list=this.getJtA().queryForList(sql);
			if(_list.size()!=0){
				for(Map map:_list){
					nodeType=String.valueOf(map.get("steptype"));
				}
				if(nodeType.equals("0")){
					nodeType="START_NODE";
				}
			}else{
				nodeType="START_NODE";
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("getNodeType");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到节点类型时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return nodeType;
	}
	 /** 读取流程节点角色和用户配置*/
	public ArrayList getStepUserConfig(String workId ,String stepId){
		ArrayList list=new ArrayList();
		String sql="select id,workId,stepID,userId,status,doType,con_dm " +
				" from t_oswf_noderole_config where workId='"+workId+"' and stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				NodepropertyuserData data=new NodepropertyuserData();
				data.id=String.valueOf(map.get("id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.userId=String.valueOf(map.get("userId"));
				data.status=String.valueOf(map.get("status"));
				data.doType=String.valueOf(map.get("doType"));
				data.conDm=Integer.parseInt(String.valueOf(map.get("con_dm")));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String guid = Guid.get();
			logItem.setMethod("getStepUserConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 读取流程节点角色和用户配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	 /** 读取流程节点角色和用户配置*/
	public String getworkId(String workName){
		String workId="";
		String sql="select id,name,remark " +
				" from t_oswf_def where name='"+workName+"'";
		try {
			List<Map> list=this.getJtA().queryForList(sql);
			for(Map map:list){
				NodepropertyuserData data=new NodepropertyuserData();
				workId=String.valueOf(map.get("id"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getworkId");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取流程节点角色和用户配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return workId;
	}
	/**
	 * 得到流程的步骤列表
	 * @param workId
	 * @return
	 */
	public ArrayList getConfigStep(String workId){
		ArrayList list=new ArrayList();
		String sql="select stepID,formId" +
				" from t_oswf_nodeform_config where workId='"+workId+"' group by stepID,formId ";
		try {
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				String[] data=new String[2];
				data[0]=String.valueOf(map.get("stepID"));
				data[1]=String.valueOf(map.get("formId"));
				list.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getConfigStep");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到流程的步骤列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/**
	 * 得到流程表单执行函数
	 * @param workid
	 * @return
	 */
	public List<Map> getConfigFormFunc(String workid){
		List<Map> list=null;
		StringBuffer sqlStr=new StringBuffer();
		try {
			sqlStr.append(" select id,workid,stepid,funcname from t_oswf_nodeformfunc_config where workid=?");
			list=this.getJtN().queryForList(sqlStr.toString(),new Object[]{workid});
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getConfigFormFunc");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到流程表单执行函数时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	 /** 读取流程节点表单配置*/
	public ArrayList getStepFormConfig(String workId ,String stepId){
		ArrayList list=new ArrayList();
		String sql="select id,workId,stepID,formId,tdID,role," +
				"ischeck,remark" +
				" from t_oswf_nodeform_config where workId='"+workId+"' and stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				config_nodeformData data=new config_nodeformData();
				data.id=String.valueOf(map.get("id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.formId=String.valueOf(map.get("formId"));
				data.tdID=String.valueOf(map.get("tdID"));
				data.role=String.valueOf(map.get("role"));
				data.ischeck=String.valueOf(map.get("ischeck"));
				data.remark=String.valueOf(map.get("remark"));
				list.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getStepFormConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc(" 读取流程节点表单配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/**
	 * 得到子流程配置信息
	 * @param workid
	 * @return
	 */
	public List<Map> getConfigSub(String workid){
		List<Map> list=null;
		StringBuffer sqlStr=new StringBuffer();
		try {
			sqlStr.append(" select id,workid,stepid,subbz_dm,doc_id,formbz_dm,zwbz_dm,fjbz_dm,lybz_dm,lcjsbz_dm from t_oswf_node_sub_config where workid=?");
			list=this.getJtN().queryForList(sqlStr.toString(),new Object[]{workid});
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getConfigSub");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到子流程配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	} 

	 /** 读取执行过的所有步骤的id*/
	public ArrayList getSteps(String businessId){
		ArrayList stepIds=new ArrayList();
		StringBuffer sqlStr=new StringBuffer();
		//历史表
		sqlStr.append("select STEP_ID,'1' as flag from os_currentstep where ENTRY_ID=").append(businessId);
		sqlStr.append(" union ");
		sqlStr.append("select STEP_ID,'0' as flag from os_historystep where ENTRY_ID=").append(businessId);
		try {
			List<Map> _list=this.getJtA().queryForList(sqlStr.toString());
			for(Map map:_list){
				stepIds.add(String.valueOf(map.get("STEP_ID"))+":"+String.valueOf(map.get("flag")));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getSteps");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取执行过的所有步骤的id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return stepIds;
	}

	 /** 读取所有表单模板*/
	public List getFormModelList(){
		List list=null;
		String sql="select id,tableName,formName,templateName,inuse from t_form_template_info where inuse='1' and zt_dm='1'";
		try {
			list = this.getJtA().queryForList(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModelList");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取所有表单模板时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
		
	}
	
	 /** 读取所有表单模板*/
	public List getFormModelList(String zzid){
		List list=null;
		String sql="SELECT id,tableName,formName,templateName,inuse FROM t_form_template_info WHERE inuse ='1' and  zzid='"+zzid+"' and zt_dm='1'";
		try {
			list = this.getJtA().queryForList(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModelList");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取所有表单模板时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/**
	 * 读取所有流程类别列表
	 * @return
	 */
	public List getWkTypeList(){
		List list=null;
		String sql="select dm,mc from t_dm where lb_dm=16 and zt_dm=1";
		try {
			list = this.getJtA().queryForList(sql);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getWkTypeList");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取所有流程类别列表时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	 /** 读取表单的权限*/
	public Map<String, Integer> getFormRole(String businessId,String stepId){
		Map<String, Integer> map=new HashMap<String, Integer>();
		String sql="select c.id,c.workId,c.stepID,c.formId,c.tdID,c.role,c.ischeck,c.remark from t_oswf_nodeform_config c,t_oswf_busi_relation b" +
				" where c.workId=b.workId and b.businessId="+businessId+" and c.stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map1:list1){
				map.put(String.valueOf(map1.get("tdID")), Integer.parseInt(String.valueOf(map1.get("role"))));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormRole");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取表单的权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
		
	}
	 /** 读取表单的权限2*/
	public Map<String, Integer> getFormRole2(String tableName,String stepId){
		Map<String, Integer> map=new HashMap<String, Integer>();
		String sql="select tdID,role from t_oswf_nodeform_config a,t_form_template_info b where a.formId=b.id and b.tableName='"+tableName+"' and stepID='"+stepId+"'"; 
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map1:list1){
				map.put(String.valueOf(map1.get("tdID")), Integer.parseInt(String.valueOf(map1.get("role"))));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormRole");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取表单的权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
		
	}
	 /** 读取表单的权限3*/
	public Map<String, Integer> getFormRole3(String workId,String stepId){
		Map<String, Integer> map=new HashMap<String, Integer>();
		String sql="select c.id,c.workId,c.stepID,c.formId,c.tdID,c.role,c.ischeck,c.remark from t_oswf_nodeform_config c where workid='"+workId+"' and c.stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map1:list1){
				map.put(String.valueOf(map1.get("tdID")), Integer.parseInt(String.valueOf(map1.get("role"))));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormRole");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取表单的权限时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
		
	}
	 /** 读取表单验证*/
	public List getFormValidate(String businessId,String stepId){
		ArrayList list=new ArrayList();
		String sql="  select t.componentname,t.datatype,c.isCheck,t.remark"+
		" from t_oswf_nodeform_config  c inner join t_form_columntype  t on t.tdid = c.tdId and t.tableid = c.formId  inner join t_oswf_busi_relation b on c.workId=b.workId"+
		" where  b.businessId="+businessId+" and c.stepID="+stepId+" and c.role='0' ";
		try {
			ValidateInfo validate=null;
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				validate=new ValidateInfo();
				validate.setComponentName(String.valueOf(map.get("componentname")));
				validate.setDataType(String.valueOf(map.get("datatype")));
				validate.setValidateType(Integer.parseInt(String.valueOf(map.get("isCheck"))));
				validate.setRemark(String.valueOf(map.get("remark")));
				list.add(validate);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormValidate");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取表单验证时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
		
	}
	 /** 读取流程所用模板名称*/
	public String getFormModel(String businessId,String stepId){
		String modelId="";
		String sql="select distinct c.formId  from t_oswf_nodeform_config c,t_oswf_busi_relation b" +
				" where c.workId=b.workId and b.businessId="+businessId+" and c.stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				modelId=String.valueOf(map.get("formId"));
			}
			sql="select templateName from t_form_template_info where id='"+modelId+"'";
			List<Map> list2=this.getJtA().queryForList(sql);
			for(Map map:list2){
				modelId=String.valueOf(map.get("templateName"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModel");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取流程所用模板名称时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return modelId;
		
	}
	 /** 读取流程所用模板名称1*/
	public String getFormModel1(String workId,String stepId){
		String modelId="";
		String sql="select distinct formId  from t_oswf_nodeform_config where workId="+workId+" and stepID="+stepId;
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				modelId=String.valueOf(map.get("formId"));
			}
			sql="select templateName from t_form_template_info where id='"+modelId+"'";
			List<Map> list2=this.getJtA().queryForList(sql);
			for(Map map:list2){
				modelId=String.valueOf(map.get("templateName"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModel");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取流程所用模板名称时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return modelId;
		
	}
	 /** 读取流程所用表单id*/
	public String getFormId(String businessId,String stepId){
		String formId="";
		String sql="select formId from t_oswf_busi_form_relation where businessId="+businessId+" and stepID="+stepId+"";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				formId=String.valueOf(map.get("formId"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormId");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取流程所用表单id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return formId;
		
	}
	/*查询节点设置*/
	public String findNodepropertcondition(int stepId,int businessId){
		String nodeCon="";
		String sql="select formula from t_oswf_nodecondition_config n ,t_oswf_busi_relation b "+
		"where stepID='"+stepId+"' and b.businessId='"+businessId+"' and n.workId=b.workId";
		try {
			List<Map> list1=this.getJtN().queryForList(sql);
			for(Map map:list1){
				nodeCon=String.valueOf(map.get("formula"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findNodepropertcondition");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询节点设置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return nodeCon;
	}
	/*根据workId查询流程节点的所以配置*/
	public ArrayList findAllNodepropertcondition(String workId){
		ArrayList list=new ArrayList();
		String sql="select n.id, n.workId,n.stepID,n.workcondition,n.taskAllocation,n.formula,n.autoStep,n.isBack" +
				" from t_oswf_nodecondition_config n "+
		"where  n.workId='"+workId+"' ";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				NodepropertconditionData data=new NodepropertconditionData();
				data.id=String.valueOf(map.get("id"));
				data.workId=String.valueOf(map.get("workId"));
				data.stepID=Integer.parseInt(String.valueOf(map.get("stepID")));
				data.workcondition=String.valueOf(map.get("workcondition"));
				data.taskAllocation=String.valueOf(map.get("taskAllocation"));
				data.formula=String.valueOf(map.get("formula"));
				data.autoStep=String.valueOf(map.get("autoStep"));
				data.isBack=String.valueOf(map.get("isBack"));
				list.add(data);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findAllNodepropertcondition");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("根据workId查询流程节点的所以配置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
	}
	/*查询自动执行公式字段*/
	public String  findFieldValue(String fieldId,String businessId,String preStepId){
		String sql="select componentname,tableid from t_form_columntype  where id="+fieldId;
		String fieldName="";
		String tableId="";
		String tableName="";
		String pId="";
		String ver="";
		String fileValue="";
		String stepId="";
		try {
			List<Map> _list=this.getJtA().queryForList(sql);
			for(Map map:_list){
				fieldName=String.valueOf(map.get("componentname"));
				tableId=String.valueOf(map.get("tableid"));
			}
			
			sql="select tableName from t_form_template_info where id="+tableId;
			List<Map> _list1=this.getJtA().queryForList(sql);
			for(Map map:_list1){
				tableName=String.valueOf(map.get("tableName"));
			}
			sql="select  STEP_ID from os_historystep where ID="+preStepId;
			List<Map> _list2=this.getJtA().queryForList(sql);
			for(Map map:_list2){
				stepId=String.valueOf(map.get("STEP_ID"));
			}
			sql="select formId,ver from t_oswf_busi_form_relation where businessId="+businessId+" and  stepID="+stepId+"";
			List<Map> _list3=this.getJtA().queryForList(sql);
			for(Map map:_list3){
				pId=String.valueOf(map.get("formId"));
				ver=String.valueOf(map.get("ver"));
			}
			sql="select "+fieldName+" as filevalue from `_"+tableName+"` where pid="+pId+" and ver="+ver+"";
			List<Map> _list4=this.getJtA().queryForList(sql);
			for(Map map:_list4){
				fileValue=String.valueOf(map.get("filevalue"));
			}
			
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("findFieldValue");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询自动执行公式字段时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return fileValue;
	}
	/**
	 * 添加节点基本信息
	 * @param workId
	 * @param stepId
	 * @param stepName
	 */
	public void insertNodeInfo(String workId,String stepId,String stepName){
		StringBuffer strSql=new StringBuffer();
		try {
			strSql.append("insert into t_oswf_node_info(id,workid,stepid,stepname)");
			strSql.append(" values('").append(Guid.get()).append("','").append(workId).append("','").append(stepId).append("','").append(stepName).append("')");
			this.getJtA().update(strSql.toString());
			logItem.setMethod("insertNodeInfo");
			logItem.setLevel("info");
			logItem.setContent("添加节点基本信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertNodeInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("添加节点基本信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	 /** 添加节点配置信息*/
	public void insertNodepropertcondition(String workId,String stepID,String condition,String amount,String taskAllocation,String formula,String autoStep,String isBack ,String remind,String isCuibNode,String cuibTime){
		try {
			String sql = "insert into t_oswf_nodecondition_config (id,workId,stepID,workcondition,amount,taskAllocation," +
				"formula,autoStep,isBack,remind,isCuib,cuibTime)VALUES('"+Guid.get()+"','"+ workId + "'," + stepID + ","
				+condition+",'"+amount+"','"+taskAllocation+"','"+formula+"','"+autoStep+"','"+isBack+"','"
				+remind+"','"+isCuibNode+"','"+cuibTime+"')";
			this.getJtA().update(sql);
			logItem.setMethod("insertNodepropertcondition");
			logItem.setLevel("info");
			logItem.setContent("添加节点配置信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertNodepropertcondition");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存节点配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}	
	}
	/**
	 * 功能：添加节点授权信息
	 * @param workId
	 * @param stepID
	 * @param userId
	 * @param doType
	 */
	public void insertNodepropertyuser(String workId,String stepID,String userId,String doType,String gwrole,String stepType ){
		String sql = "insert into t_oswf_noderole_config(id,workId,stepID,userId,status,doType,con_dm,stepType)VALUES('"+Guid.get()+"','"
				+ workId + "','" + stepID + "','"+userId+"',1,'"+doType+"',"+gwrole+",'"+stepType+"')";
		try {
			this.getJtA().update(sql);
			logItem.setMethod("insertNodepropertyuser");
			logItem.setLevel("info");
			logItem.setContent("添加节点授权信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertNodepropertyuser");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存节点授权信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}	
		
		
	}
	 /** 添加聚合节点的配置信息*/
	public void insertJoinpropertcondition(String workId,String stepID,String formula){
		String sql = "insert into t_oswf_nodecondition_config (id,workId,stepID,formula)VALUES('"+Guid.get()+"','"
				+ workId + "'," + stepID + ",'"+formula+"')";
		try {
			this.getJtA().update(sql);
			logItem.setMethod("insertJoinpropertcondition");
			logItem.setLevel("info");
			logItem.setContent("添加聚合节点配置信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertJoinpropertcondition");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存聚合节点配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	 /** 添加表单角色配置信息*/
	public void insertFormConfig(String workId,String stepID,String formId,String tdID,String role,String check){
		String sql = "insert into t_oswf_nodeform_config (id,workId,stepID,formId,tdID,role,ischeck,remark)VALUES('"+Guid.get()+"','"
				+ workId + "'," + stepID + ",'"+formId+"','"+tdID+"','"+role+"','"+check+"','')";
		try {
			this.getJtA().update(sql);
			logItem.setMethod("insertFormConfig");
			logItem.setLevel("info");
			logItem.setContent("添加表单角色配置信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertFormConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存表单角色配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	/** 添加功能按钮配置信息*/
	public void insertFunctionConfig(String workId,String stepID,String funcConfig,String buttonType){
		String sql = "insert into t_oswf_nodecondition_config (id,workId,stepID,funcConfig,buttonType)VALUES('"+Guid.get()+"','"
				+ workId + "'," + stepID + ",'"+funcConfig+"','"+buttonType+"')";
		String update="update t_oswf_nodecondition_config set funcConfig='"+funcConfig+"', buttonType='"+buttonType+"' " +
				"where workId='"+workId+"' and stepID="+stepID+"";
		String select="select id from t_oswf_nodecondition_config where workId='"+workId+"' and stepID="+stepID+"";
		try {
			List<Map> list=this.getJtA().queryForList(select);
			if(list.size()!=0){
				this.getJtA().update(update);
			}else{
				this.getJtA().update(sql);
			}
			logItem.setMethod("insertFunctionConfig");
			logItem.setLevel("info");
			logItem.setContent("添加功能配置信息成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertFunctionConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存功能配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	/**
	 * 添加表单执行函数
	 * @param workid
	 * @param stepid
	 * @param funcname
	 */
	public void insertFormFuncConfig(String workid,String stepid,String funcname){
		StringBuffer sqlStr=new StringBuffer();
		try {
			sqlStr.append("insert into t_oswf_nodeformfunc_config(id,workid,stepid,funcname) ");
			sqlStr.append("values(?,?,?,?)");
			this.getJtN().update(sqlStr.toString(), new Object[]{Guid.get(),workid,stepid,funcname});
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertFormFuncConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("添加表单执行函数时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	/**
	 * 添加子流程配置信息
	 * @param workid
	 * @param stepid
	 * @param subbz
	 * @param docid
	 * @param formbz
	 * @param zwbz
	 * @param fjbz
	 * @param lybz
	 * @param lcjsbz
	 */
	public void insertSubConfig(String workid,String stepid,String subbz,String docid,String formbz,String zwbz,String fjbz,String lybz,String lcjsbz){
		StringBuffer sqlStr=new StringBuffer();
		try {
			sqlStr.append("insert into t_oswf_node_sub_config(id,workid,stepid,subbz_dm,doc_id,formbz_dm,zwbz_dm,fjbz_dm,lybz_dm,lcjsbz_dm) ");
			sqlStr.append("values(?,?,?,?,?,?,?,?,?,?) ");
			this.getJtN().update(sqlStr.toString(),new Object[]{Guid.get(),workid,stepid,subbz,docid,formbz,zwbz,fjbz,lybz,lcjsbz});
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("insertSubConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("添加子流程配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	 /** 读取流程所用模板表名*/
	public String getFormModelName(String businessId,String stepId){
		String modelId="";
		String sql="select c.formId  from t_oswf_nodeform_config c,t_oswf_busi_relation b" +
				" where c.workId=b.workId and b.businessId="+businessId+" and c.stepID="+stepId+" group by c.workId ";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				modelId=String.valueOf(map.get("formId"));
			}
			sql="select tableName from t_form_template_info where id='"+modelId+"'";
			List<Map> list2=this.getJtA().queryForList(sql);
			for(Map map:list2){
				modelId=String.valueOf(map.get("tableName"));
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModelName");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取流程所用模板表名时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return modelId;
		
	}
	/** 读取表单所有字段*/
	public ArrayList getFormModel(String tableId){
		ArrayList list=new ArrayList();
		String sql="select id,tdid,remark from t_form_authority where tableid='"+tableId+"'";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				String[] td=new String[3];
				td[0]=String.valueOf(map.get("id"));
				td[1]=String.valueOf(map.get("tdid"));
				td[2]=String.valueOf(map.get("remark"));
				list.add(td);
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getFormModel");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取表单所有字段时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return list;
		
	}
	 /** 读取当前步骤的id*/
	public ArrayList getCurrentStep(String businessId){
		ArrayList stepIds=new ArrayList();
		String sql="select STEP_ID from os_currentstep where ENTRY_ID="+businessId+"";
		try {
			List<Map> list1=this.getJtA().queryForList(sql);
			for(Map map:list1){
				stepIds.add(Integer.parseInt(String.valueOf(map.get("STEP_ID"))));
			}
			
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("getCurrentStep");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("读取当前步骤的id时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return stepIds;
	}
	
}

package com.coffice.workflow.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.workflow.sync.Sync;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;

public class UnDoItem extends BaseUtil
  implements FunctionProvider
{
  LogItem logItem;

  public void execute(Map transientVars, Map args, PropertySet ps)
  {
    this.logItem = new LogItem();
    String isBack = ps.getString("isBack");
    String stepIdVal = "";
    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    WorkflowContext context = (WorkflowContext)transientVars.get("context");
    String lclb_dm = String.valueOf(transientVars.get("lclb_dm"));
    String mk_dm = String.valueOf(transientVars.get("mk_dm"));
    String mkurllb = String.valueOf(transientVars.get("mkurllb"));
    String ywid = String.valueOf(transientVars.get("ywid"));
    String caller = String.valueOf(transientVars.get("username"));
    String gwbt = String.valueOf(transientVars.get("gwbt"));
    String undo_title = String.valueOf(transientVars.get("undo_title"));
    String workid = String.valueOf(transientVars.get("workid"));
    String stepid = String.valueOf(transientVars.get("stepId"));
    String formid = String.valueOf(transientVars.get("formid"));
    String isEnd = String.valueOf(transientVars.get("isEnd"));
    long entry_id = entry.getId();
    if("1".equals(isEnd)){
    	 //新增加 ，增加判断同步函数，多线程 起
    	Map _syncMap = new HashMap();
    	_syncMap.put("isBack",isBack);
    	_syncMap.put("isEnd",isEnd);
    	_syncMap.put("workid", workid);
    	_syncMap.put("entryid", entry_id);
    	_syncMap.put("stepid", stepid);
    	_syncMap.put("formid", formid);
    	_syncMap.put("curstepid","4");
    	_syncMap.put("yhid",transientVars.get("caller"));
    	_syncMap.put("userList",new ArrayList());
    	Sync sync = new Sync(_syncMap);
    	sync.start();
    	return;
    }
    ArrayList userList = null;
    List<Map> _list = null;
    if (("null".equals(gwbt)) || (gwbt == null)) {
      gwbt = "";
    }
    if (("null".equals(undo_title)) || (undo_title == null)) {
    	undo_title = "";
      }
    String zzid = String.valueOf(transientVars.get("zzid"));
    String bmid = String.valueOf(transientVars.get("bmid"));
    String gwid = String.valueOf(transientVars.get("gwid"));

  
    JDBCWorkflowStore store = (JDBCWorkflowStore)transientVars.get("store");
    List stepList = null;
    WorkFlowMethod method = null;
    try {
      String sql = "";
      String delSql = "";
     // String workid = "";
      String stepType = "";
      String oswf_work_show_fqr = "0";

     // sql = "select workid from t_oswf_busi_relation where businessId='" + Integer.parseInt(new StringBuilder(String.valueOf(entry_id)).toString()) + "'";
      //Map _map = getJtN().queryForMap(sql);
      //workid = (String)_map.get("workid");
      if ((isBack == null) || (!isBack.equals("yes"))) {
        stepList = store.findCurrentSteps(entry_id);
        SimpleStep currentStep = (SimpleStep)stepList.get(stepList.size() - 1);
        stepIdVal = String.valueOf(currentStep.getStepId());
        method = new WorkFlowMethod();

       sql = "select steptype from t_oswf_noderole_config where workid='" + workid + "' and stepid='" + stepIdVal + "'";
        List<Map> list3 = getJtN().queryForList(sql);
        if (list3.size() != 0) {
          for (Map map : list3) {
            stepType = String.valueOf(map.get("steptype"));
            if (stepType.equals("0")){
            	method.insertNodeUser(workid, Integer.parseInt(stepIdVal), (int)entry_id, caller, "", "-1");
            }
          }
        }else {
          stepType = "0";
          method.insertNodeUser(workid, Integer.parseInt(stepIdVal), (int)entry_id, caller, "", "-1");
        }

       /* if (SysPara.compareValue("oswf_work_show_fqr", "1")) {
          oswf_work_show_fqr = "1";
        }*/
       // if (oswf_work_show_fqr.equals("1"))
         // sql = "select distinct userId as userId,remind_id,cuib_id,fqz from t_oswf_work_item_role n,t_oswf_busi_relation b where  b.businessId=" + 
          //  entry_id + " and stepID=" + stepIdVal + 
          //  " and b.businessId=n.businessId";
        sql= "select distinct userId as userId,remind_id,cuib_id,fqz from t_oswf_work_item_role where businessId='"+entry_id+"' and  stepID=" + stepIdVal; 
       /* else {
          sql = "select distinct userId as userId,remind_id,cuib_id from t_oswf_work_item_role n,t_oswf_busi_relation b where  b.businessId=" + 
            entry_id + " and stepID=" + stepIdVal + 
            " and b.businessId=n.businessId";
        }*/

        delSql = "delete from t_oswf_work_item_role where businessId=" + entry_id + " and stepID=" + stepIdVal;

         _list = getJtN().queryForList(sql);
        userList = new ArrayList();
        for (Map map : _list) {
          String[] temp = new String[4];
          temp[0] = String.valueOf(map.get("userId"));
          temp[1] = String.valueOf(map.get("remind_id"));
          temp[2] = String.valueOf(map.get("cuib_id"));
          if (oswf_work_show_fqr.equals("1")) {
            temp[3] = String.valueOf(map.get("fqz"));
          }
          userList.add(temp);
        }

        String sqlInsert = "";
        StringBuffer sqlStr = new StringBuffer();
        String trust = "";
        for (int i = 0; i < userList.size(); ++i) {
          String[] temp = (String[])userList.get(i);
          String userId = temp[0];
          String remind_id = temp[1];
          String cuib_id = temp[2];
          String fqz = "";
          if (oswf_work_show_fqr.equals("1")) {
            fqz = temp[3];
          }
          sqlStr.delete(0, sqlStr.length());
          sqlStr.append("select id from t_oswf_trust_flow_record where trustType='1' and  takeBackDate is null and businessId='").append(workid).append("' and trustUser='").append(userId).append("'");
          List listTrust = getJtN().queryForList(sqlStr.toString());
          if (listTrust.size() == 0)
            trust = "1";
          else {
            trust = "0";
          }
          sqlInsert = "select id from t_oswf_work_item where id='" + ywid + "'";
          List listYwid = getJtN().queryForList(sqlInsert);
          if (listYwid.size() != 0) {
            ywid = Guid.get();
          }
        //  if (oswf_work_show_fqr.equals("1"))
            sqlInsert = "insert into t_oswf_work_item (zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,type,fqz,value,status,trust,formname,doc_id,mk_dm,mkurllb,remind_id,cuib_id,cjsj) VALUES('" + zzid + "','" + bmid + "','" + gwid + "','" + ywid + "','" + workid + "','" + 
              entry_id + "','" + stepIdVal + "','" + stepType + "','" + fqz + "','" + userId + "',1,'" + trust + "','" + undo_title + "','" + lclb_dm + "','" + mk_dm + "','" + mkurllb + "','" + remind_id + "','" + cuib_id + "',"+getDateStr()+")";
        //  else {
        //    sqlInsert = "insert into t_oswf_work_item (zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,type,value,status,trust,formname,doc_id,mk_dm,mkurllb,remind_id,cuib_id,cjsj) VALUES('" + zzid + "','" + bmid + "','" + gwid + "','" + ywid + "','" + workid + "','" + 
        //      entry_id + "','" + stepIdVal + "','" + stepType + "','" + userId + "',1,'" + trust + "','" + undo_title + "','" + lclb_dm + "','" + mk_dm + "','" + mkurllb + "','" + remind_id + "','" + cuib_id + "',"+getDateStr()+")";
        //  }
          ArrayList list = method.findWorkitem(entry_id, Integer.parseInt(stepIdVal), userId);
          if (list.size() <= 0) {
            //sqlInsert = TransSql.trans(sqlInsert);
            getJtN().update(sqlInsert);
          }
        }
        method.updateWorkitemTitle(ywid, undo_title);
        getJtN().update(delSql);
      }else{
        ps.remove("isBack");
      }
    //新增加 ，增加判断同步函数，多线程 起
	Map _syncMap = new HashMap();
	_syncMap.put("isBack",isBack);
	_syncMap.put("workid", workid);
	_syncMap.put("entryid", entry_id);
	_syncMap.put("stepid", stepid);
	_syncMap.put("curstepid", stepIdVal);
	_syncMap.put("formid", formid);
	_syncMap.put("yhid",transientVars.get("caller"));
	_syncMap.put("userList",_list);
	Sync sync = new Sync(_syncMap);
	sync.start();
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("Workitem.execute");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("用户：" + caller + "执行Workitem函数,分配待办任务函数出现异常,实例：" + entry_id);
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }
}
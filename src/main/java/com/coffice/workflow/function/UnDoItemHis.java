package com.coffice.workflow.function;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;
import java.util.List;
import java.util.Map;

public class UnDoItemHis extends BaseUtil
  implements FunctionProvider
{
  LogItem logItem;

  public void execute(Map transientVars, Map args, PropertySet ps)
  {
    this.logItem = new LogItem();
    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    WorkflowDescriptor des = (WorkflowDescriptor)transientVars.get("descriptor");

    JDBCWorkflowStore store = (JDBCWorkflowStore)transientVars.get("store");
    List currentList = (List)(List)transientVars.get("currentSteps");
    WorkflowContext context = (WorkflowContext)transientVars.get("context");
    String currentStepId = args.get("stepId").toString();
    String all = (args.get("all") == null) ? null : args.get("all").toString();

    long entry_id = entry.getId();
    String caller = String.valueOf(transientVars.get("username"));
    String oswf_work_show_fqr = "0";
    if (SysPara.compareValue("oswf_work_show_fqr", "1")) {
      oswf_work_show_fqr = "1";
    }
    String sql = "";
    sql = "insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,"+getDateStr()+" from t_oswf_work_item where Entry_ID=" + 
        entry_id + " and Step_ID=" + currentStepId + " and Value='" + caller + "'";
    if (all != null) {
      if (oswf_work_show_fqr.equals("1"))
        sql = "insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,"+getDateStr()+" from t_oswf_work_item where Entry_ID=" + 
          entry_id + " and Step_ID=" + currentStepId;
      else
        sql = "insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,"+getDateStr()+" from t_oswf_work_item where Entry_ID=" + 
          entry_id + " and Step_ID=" + currentStepId;
    }
    try
    {
		//判断否为多人审核
		String multiShenHeSql = "select distinct status from t_oswf_work_item where Entry_ID="+entry_id+" and Step_ID="+currentStepId+"";	
		List<Map> multiShenHeList = this.getJtN().queryForList(multiShenHeSql);
		boolean isMultiShenHe = false;
		if(multiShenHeList!=null&&multiShenHeList.size()>0){
			for(Map multiShenHeMap : multiShenHeList){
				String status = String.valueOf(multiShenHeMap.get("status"));
				if("0".equals(status)){
					isMultiShenHe=true;
					break;
				}
			}
		}
		//如果不是多人审核，则向已办表中插入数据
		if(!isMultiShenHe){
			//sql=TransSql.trans(sql);
			this.getJtN().update(sql);
		}
      StringBuffer strSql = new StringBuffer();
      strSql.append("select cuib_id from t_oswf_work_item where Entry_ID=").append(entry_id).append(" and Step_ID=").append(currentStepId).append(" and Value='").append(caller).append("'");
      List<Map> mapCuib = getJtN().queryForList(strSql.toString());

      for (Map _map : mapCuib) {
        String cuib = (String)_map.get("cuib_id");
        if ((cuib != null) && (!"".equals(cuib))) {
          strSql.delete(0, strSql.length());
          strSql.append("update t_oswf_work_cuib_his set wctime=").append(getDateStr()).append(" where cuib_id='").append(_map.get("cuib_id")).append("'");
          getJtN().update(strSql.toString());
        }
      }
      sql = "delete from t_oswf_work_item where Entry_ID=" + entry_id + " and Step_ID=" + currentStepId + " and Value='" + caller + "'";
      if (all != null) {
        sql = "delete from t_oswf_work_item where Entry_ID=" + entry_id + " and Step_ID=" + currentStepId;
      }
      getJtN().update(sql);
    }
    catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("WorkitemtoHistory.execute");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("用户：" + caller + "执行WorkitemtoHistory函数,将待办任务转移到历史表出现异常,实例：" + entry_id);
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }
}
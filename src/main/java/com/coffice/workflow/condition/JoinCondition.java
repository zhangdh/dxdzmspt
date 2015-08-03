package com.coffice.workflow.condition;

import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.jdbc.JDBCWorkflowStore;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class JoinCondition extends BaseUtil
  implements Condition
{
  LogItem logItem;
  StringBuffer strSql = new StringBuffer();

  public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
    throws WorkflowException
  {
    System.out.println("执行JoinCondition条件");

    this.logItem = new LogItem();
    boolean result = false;

    WorkflowDescriptor wf = (WorkflowDescriptor)transientVars.get("descriptor");
    JDBCWorkflowStore store = (JDBCWorkflowStore)transientVars.get("store");

    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    String stepId = args.get("stepId").toString();

    WorkFlowMethod method = new WorkFlowMethod();
    String nodecon = method.findNodepropertcondition(Integer.parseInt(stepId), Integer.parseInt(entry.getId()+""));

    int sid = 0;
    Map smap = null;
    ArrayList arry = new ArrayList();
    List _list = store.findHistorySteps(entry.getId());

    this.strSql.append("select stepid from t_oswf_node_info where workid in(select workid from t_oswf_entry_md5 where entryid=?)");
    List list1 = getJtN().queryForList(this.strSql.toString(), new Object[] { Long.valueOf(entry.getId()) });
    if (list1.size() != 0) {
      for (Iterator localIterator = list1.iterator(); localIterator.hasNext(); ) { Map map1 = (Map)localIterator.next();
        sid = Integer.parseInt(map1.get("stepid").toString());
        StepDescriptor stepDesc = wf.getStep(sid);
        if (!(stepDesc.resultsInJoin(Integer.parseInt(stepId))))
        smap = new HashMap();
        smap.put("stepid", Integer.valueOf(sid));
        label295: arry.add(smap);
      }

    }

    int config = arry.size();
    if ((nodecon != null) && (!(nodecon.trim().equals(""))))
      config = Integer.parseInt(nodecon);
    try
    {
 
      int i = 0;

      for ( i = 0; i < arry.size(); ++i) {
        Map _map = (Map)(Map)arry.get(i);
        sid = Integer.parseInt(String.valueOf(_map.get("stepid")));
        System.out.println(sid);
        for (int j = 0; j < _list.size(); ++j) {
          Step step = (Step)_list.get(j);
          if (sid == step.getStepId()) {
            ++i;
            break;
          }
        }
      }
      if (i >= config)
      {
        String sql = "";
        for ( i = 0; i < arry.size(); ++i) {
          Map _map = (Map)(Map)arry.get(i);
          sid = Integer.parseInt(String.valueOf(_map.get("stepid")));
          sql = "insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,now() from t_oswf_work_item where Entry_ID=" + 
            entry.getId() + 
            " and Step_ID=" + 
            sid;

          getJtA().update(sql);

          sql = "delete from t_oswf_work_item where Entry_ID=" + entry.getId() + 
            " and Step_ID=" + sid;
          getJtN().update(sql);
        }

        result = true;
      } else {
        result = false;
      }
      this.logItem.setMethod("JoinCondition.passesCondition");
      this.logItem.setLevel("info");
      this.logItem.setContent("汇聚节点 条件成功");
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("JoinCondition.passesCondition");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("汇聚节点 条件出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
    return result;
  }
}
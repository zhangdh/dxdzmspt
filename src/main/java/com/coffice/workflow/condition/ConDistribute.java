package com.coffice.workflow.condition;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowEntry;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConDistribute extends BaseUtil
  implements Condition
{
  LogItem logItem;

  public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
    throws WorkflowException
  {
    System.out.println("执行ConDistribute条件");
    boolean result = false;
    this.logItem = new LogItem();
    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");

    long entry_id = entry.getId();

    ArrayList list = (ArrayList)(ArrayList)transientVars.get("currentSteps");
    WorkflowContext context = (WorkflowContext)transientVars.get("context");
    String caller = context.getCaller();
    Step step = (Step)list.get(0);
    String stepId = args.get("stepId").toString();
    Object obj = args.get("con");
    String conf = (args.get("con") == null) ? "" : args.get("con").toString();
    String sql = "select workcondition as workcondition from t_oswf_nodecondition_config n ,t_oswf_busi_relation b where n.stepID='" + 
      stepId + "' and b.businessId='" + entry_id + "' and  n.workId=b.workId";
    int condition = -1;
    try {
      List _list = getJtN().queryForList(sql);
      for (Iterator localIterator1 = _list.iterator(); localIterator1.hasNext(); ) { Map map = (Map)localIterator1.next();
        String condit = String.valueOf(map.get("workcondition"));
        if (condit.equals("null"))
          return true;

        condition = Integer.parseInt(condit);
      }

      sql = "select status as status from t_oswf_work_item where step_ID='" + 
        stepId + "' and Entry_ID='" + entry_id + "'";
      List _list1 = getJtN().queryForList(sql);
      ArrayList listStatus = new ArrayList();
      for (Iterator localIterator2 = _list1.iterator(); localIterator2.hasNext(); ) { Map map = (Map)localIterator2.next();
        listStatus.add(Integer.valueOf(Integer.parseInt(String.valueOf(map.get("status")))));
      }

      if (condition == 0)
      {
        result = true;
      }
      else
        result = true;

      this.logItem.setMethod("ConDistribute.passesCondition");
      this.logItem.setLevel("info");
      this.logItem.setContent("处理指定多个审批人条件处理成功");
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("ConDistribute.passesCondition");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("处理指定多个审批人条件处理时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
    return result;
  }
}
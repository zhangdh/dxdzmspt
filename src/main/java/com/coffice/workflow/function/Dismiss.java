package com.coffice.workflow.function;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.WorkflowEntry;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class Dismiss extends BaseUtil
  implements FunctionProvider
{
  LogItem logItem;

  public void execute(Map transientVars, Map args, PropertySet ps)
  {
    System.out.println("执行Dismiss函数");

    this.logItem = new LogItem();

    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    long entry_id = entry.getId();
    try
    {
      String currentStepId = args.get("stepId").toString();
      List currentList = (List)(List)transientVars.get("currentSteps");
      String sql = "";
      for (int i = 0; i < currentList.size(); ++i) {
        SimpleStep step = (SimpleStep)currentList.get(i);
        if (step.getStepId() != Integer.parseInt(currentStepId))
        {
          sql = "delete from os_currentstep where ENTRY_ID=" + entry_id + " and STEP_ID=" + step.getStepId();
          getJtN().update(sql);

          sql = "delete from t_oswf_work_item where Entry_ID=" + entry_id + " and Step_ID=" + step.getStepId();
          getJtN().update(sql);
        }
      }
      this.logItem.setMethod("Dismiss.execute");
      this.logItem.setLevel("info");
      this.logItem.setContent("驳回时删除其他分支信息成功");
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("Dismiss.execute");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("驳回时删除其他分支信息时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }
}
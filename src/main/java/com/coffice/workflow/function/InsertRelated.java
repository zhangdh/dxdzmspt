package com.coffice.workflow.function;

import java.util.HashMap;
import java.util.Map;

import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.workflow.sync.SyncCreate;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowContext;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;

public class InsertRelated
  implements FunctionProvider
{
  LogItem logItem;

  public void execute(Map transientVars, Map args, PropertySet ps)
  {
    this.logItem = new LogItem();
    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    WorkflowDescriptor des = (WorkflowDescriptor)transientVars.get("descriptor");
    WorkflowContext context = (WorkflowContext)transientVars.get("context");
    String workId = args.get("workId").toString();
    String caller = context.getCaller();

    long entry_id = entry.getId();
    this.logItem.setMethod("InsertBusinessShip.execute");
    this.logItem.setLevel("info");
    this.logItem.setDesc("用户：" + caller + "执行InsertBusinessShip函数,开始初始化流程业务表,实例：" + entry_id);
    Log.write(this.logItem);
    try {
        WorkFlowMethod method = new WorkFlowMethod();
        method.insertBusinessShip(entry_id, workId);
    } catch (Exception e) {
      this.logItem.setMethod("InsertBusinessShip.execute");
      this.logItem.setLevel("error");
      this.logItem.setDesc("用户：" + caller + "执行InsertBusinessShip函数,初始化流程业务表发生异常,实例：" + entry_id);
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
    this.logItem.setMethod("InsertBusinessShip.execute");
    this.logItem.setLevel("info");
    this.logItem.setDesc("用户：" + caller + "执行InsertBusinessShip函数,结束初始化流程业务表,实例：" + entry_id);
    Log.write(this.logItem);
  }
}
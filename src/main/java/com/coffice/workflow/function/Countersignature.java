package com.coffice.workflow.function;

import com.coffice.workflow.condition.table.date.WorkitemData;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.spi.WorkflowEntry;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Countersignature
  implements FunctionProvider
{
  public void execute(Map transientVars, Map args, PropertySet ps)
  {
    System.out.println("执行Countersignature函数");

    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
    WorkFlowMethod method = new WorkFlowMethod();
    Object stepIdVal = args.get("stepId");
    ArrayList imtemList = method.findWorkitem(entry.getId(), Integer.parseInt(stepIdVal.toString()));
    int i = 0;
    for (Iterator iterator = imtemList.iterator(); iterator.hasNext(); ) {
      WorkitemData data = (WorkitemData)iterator.next();

      if (!(data.status.equals("1"))){
        ++i;
      }
    }

    if (i > 0) {
      transientVars.put("state", "start");
      System.out.println("执行Countersignature函数方式：start");
    } else {
      System.out.println("执行Countersignature函数方式：Underway");
      transientVars.put("state", "Underway");
    }
  }
}
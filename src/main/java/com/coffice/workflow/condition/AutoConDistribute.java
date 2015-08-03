package com.coffice.workflow.condition;

import bsh.Interpreter;

import com.coffice.workflow.condition.table.NodepropertConditionTable;
import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.spi.SimpleStep;
import com.opensymphony.workflow.spi.WorkflowEntry;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class AutoConDistribute extends BaseUtil
  implements Condition
{
  LogItem logItem;

  public boolean passesCondition(Map transientVars, Map args, PropertySet ps)
    throws WorkflowException
  {
    System.out.println("执行AutoConDistribute条件");
    boolean result = false;
    this.logItem = new LogItem();
    String autoStep = "";
    String formula = "";
    WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");

    long entry_id = entry.getId();

    String stepId = args.get("stepId").toString();
    NodepropertConditionTable nodTable = new NodepropertConditionTable();
    String sql = "select " + nodTable.condition + " as condition,autoStep,formula from t_oswf_nodecondition_config as n ,t_oswf_busi_relation b " + 
      "where n.stepID='" + stepId + "' and b.businessId='" + entry_id + "' and  n.workId=b.workId";

    int condition = -1;
    try {
      List _list = getJtA().queryForList(sql);
      for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
        condition = Integer.parseInt(String.valueOf(map.get("condition")));
        autoStep = String.valueOf(map.get("autoStep"));
        formula = String.valueOf(map.get("formula"));
      }

      if (autoStep.equals("1"))
      {
        System.out.print(" 自动执行条件为 : " + formula);
        String[] f = formula.split(",");
        String op = "";

        WorkFlowMethod method = new WorkFlowMethod();
        ArrayList current = (ArrayList)(ArrayList)transientVars.get("currentSteps");
        SimpleStep s = (SimpleStep)current.get(0);
        long[] preStep = s.getPreviousStepIds();
        String value = method.findFieldValue(f[0].toString()+"", entry_id+"", preStep[0]+"");
        if (f[1].equals("0"))
          op = ">";
        else if (f[1].equals("1"))
          op = "<";
        else if (f[1].equals("2"))
          op = "=";
        else if (f[1].equals("3"))
          op = "!=";

        Interpreter i = new Interpreter();
        String script = "";
        script = script + "a=" + value + ";";
        script = script + "b=" + f[2] + ";";
        script = script + "c=a" + op + "b;";
        result = ((Boolean)i.eval(script)).booleanValue();
      }
      this.logItem.setMethod("AutoConDistribute.passesCondition");
      this.logItem.setLevel("info");
      this.logItem.setContent("处理指定多个审批人条件处理成功");
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("AutoConDistribute.passesCondition");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("处理指定多个审批人条件处理时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
    return result;
  }
}
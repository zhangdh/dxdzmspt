package com.coffice.workflow.condition.table.date;

import java.io.Serializable;
import java.util.ArrayList;

public class NodeRoleData
  implements Serializable
{
  public int stepId;
  public int workId;
  public ArrayList roleList = new ArrayList();
}
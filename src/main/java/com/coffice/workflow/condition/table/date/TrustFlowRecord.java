package com.coffice.workflow.condition.table.date;

import java.io.Serializable;

public class TrustFlowRecord
  implements Serializable
{
  public int id;
  public String workid;
  public String businessId;
  public String trustType;
  public String stepId;
  public String trustUser;
  public String mandatary;
  public String trustDate;
  public String takeBackDate;
  public String username;
}
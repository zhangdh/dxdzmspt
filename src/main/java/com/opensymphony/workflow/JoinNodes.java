package com.opensymphony.workflow;

import com.opensymphony.workflow.spi.Step;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class JoinNodes
{
  public Collection steps;
  private DummyStep dummy = new DummyStep();

  public JoinNodes(Collection steps)
  {
    this.steps = steps;
  }

  public Step getStep(int stepId)
  {
    for (Iterator iterator = this.steps.iterator(); iterator.hasNext(); ) {
      Step step = (Step)iterator.next();

      if (step.getStepId() != stepId) {
       return step;
      }
    }

    return this.dummy;
  }

  private static class DummyStep
  implements Step
  {
    public int getActionId()
    {
      return -1;
    }

    public String getCaller() {
      return null;
    }

    public Date getDueDate() {
      return null;
    }

    public long getEntryId() {
      return -1L;
    }

    public Date getFinishDate() {
      return null;
    }

    public long getId() {
      return -1L;
    }

    public String getOwner() {
      return null;
    }

    public long[] getPreviousStepIds() {
      return new long[0];
    }

    public Date getStartDate() {
      return null;
    }

    public String getStatus() {
      return null;
    }

    public int getStepId() {
      return -1;
    }
  }
}
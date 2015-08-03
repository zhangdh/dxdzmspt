package com.coffice.workflow.util;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class CommonMethod
{
  public Element addpreFunctions(boolean cou, String stepId)
  {
    Element preElement = DocumentHelper.createElement("pre-functions");

    Element functionElement = preElement.addElement("function");
    functionElement.setAttributeValue("type", "class");
    Element arg = functionElement.addElement("arg");
    arg.setAttributeValue("name", "class.name");
    arg.setText("com.opensymphony.workflow.util.Caller");

    if (cou) {
      Element confunctionElement = preElement.addElement("function");
      confunctionElement.setAttributeValue("type", "class");
      Element conarg = confunctionElement.addElement("arg");
      conarg.setAttributeValue("name", "class.name");
      conarg.setText("com.coffice.workflow.function.Countersignature");
      Element steparg = confunctionElement.addElement("arg");
      steparg.setAttributeValue("name", "stepId");
      steparg.setText(stepId);
    }
    return preElement;
  }

  public Element addconditions(boolean cou, String stepId, String state) {
    Element restricttoElement = DocumentHelper.createElement("restrict-to");
    Element conElement = restricttoElement.addElement("conditions");
    conElement.setAttributeValue("type", "AND");

    Element conditionElement = conElement.addElement("condition");
    conditionElement.setAttributeValue("type", "class");
    Element arg = conditionElement.addElement("arg");
    arg.setAttributeValue("name", "class.name");
    arg.setText("com.opensymphony.workflow.util.StatusCondition");
    Element statusarg = conditionElement.addElement("arg");
    statusarg.setAttributeValue("name", "status");
    statusarg.setText(state);

    if (cou) {
      Element hqElement = conElement.addElement("condition");
      hqElement.setAttributeValue("type", "class");
      Element hqarg = hqElement.addElement("arg");
      hqarg.setAttributeValue("name", "class.name");
      hqarg.setText("com.coffice.workflow.condition.ConDistribute");
      Element hqarg2 = hqElement.addElement("arg");
      hqarg2.setAttributeValue("name", "stepId");
      hqarg2.setText(stepId);
    }
    return restricttoElement;
  }

  public Element addprepostFunctions(String stepId)
  {
    Element preElement = DocumentHelper.createElement("post-functions");
    Element functionElement = preElement.addElement("function");
    functionElement.setAttributeValue("type", "class");
    Element arg = functionElement.addElement("arg");
    arg.setAttributeValue("name", "class.name");
    arg.setText("com.coffice.workflow.function.UnDoItemHis");
    Element argstep = functionElement.addElement("arg");
    argstep.setAttributeValue("name", "stepId");
    argstep.setText(stepId);

    Element argsall = functionElement.addElement("arg");
    argsall.setAttributeValue("name", "all");
    argsall.setText("0");
    return preElement;
  }

  public Element addWorkitem()
  {
    Element preElement = DocumentHelper.createElement("pre-functions");
    Element functionElement = preElement.addElement("function");
    functionElement.setAttributeValue("type", "class");
    Element arg = functionElement.addElement("arg");
    arg.setAttributeValue("name", "class.name");
    arg.setText("com.coffice.workflow.function.UnDoItem");
    return preElement;
  }
}
package com.coffice.workflow.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class NodeConfigManager
{
  public WorkFlowMethod method = null;
  LogItem logItem = new LogItem();

  public void nodeConfig(String workId, String parsexml)
  {
    String xml;
    try
    {
      xml = parsexml;

      Document doc = DocumentHelper.parseText(xml);

      List list = doc.selectNodes("/workflow");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("nodeId");

        Attribute att2 = step.attribute("nodeName");

        Attribute att1 = step.attribute("nodeType");
        this.method = new  WorkFlowMethod();
        String stepId = att.getValue();
        String stepType = "1";
        if (att1.getValue().equals("START_NODE")) {
          stepType = "0";
        }

        String stepName = att2.getValue();
        this.method.insertNodeInfo(workId, stepId, stepName);

        String condition = step.element("conditionType").attribute("value").getValue();
        String amount = "";
        if ((condition.equals("0")) || (condition.equals("2"))) {
          amount = step.element("conditionType").attribute("amount").getValue();
        }

        String taskAllocation = step.element("taskAllocation").attribute("value").getValue();

        String isBack = step.element("backNode").attribute("value").getValue();

        String autoNode = step.element("autoNode").attribute("value").getValue();

        String formula = "";
        if (autoNode.equals("1")) {
          formula = step.element("autoNode").attribute("formula").getValue();
        }

        String remindNode = step.element("remind").attribute("value").getValue();

        String isCuibNode = step.element("isCuib").attribute("value").getValue();
        String contime = "";
        if (isCuibNode.equals("0"))
          contime = (step.element("isCuib").attribute("contime").getValue() == null) ? "0" : step.element("isCuib").attribute("contime").getValue();

        if ((taskAllocation == null) || ("null".equals(taskAllocation)) || ("".equals(taskAllocation))) {
          taskAllocation = "";
        }

        this.method.insertNodepropertcondition(workId, stepId, condition, amount, taskAllocation, formula, autoNode, isBack, remindNode, isCuibNode, contime);
        List roleList = step.elements("roletype");

        for (Iterator roleiter = roleList.iterator(); roleiter.hasNext(); ) {
          Element steprole = (Element)roleiter.next();
          Attribute type = steprole.attribute("type");
          Attribute value = steprole.attribute("value");
          Attribute gwrole = steprole.attribute("gwrole");

          String gwvalue = "0";
          if (gwrole != null)
            gwvalue = gwrole.getValue();

          this.method.insertNodepropertyuser(workId, stepId, value.getValue(), type.getValue(), gwvalue, stepType);
        }
      }
    }
    catch (Exception e)
    {
      String guid = Guid.get();
      this.logItem.setMethod("nodeConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存节点的配置信息时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void insertworkflow(String name) {
    ClassLoader classLoader = super.getClass().getClassLoader();
    String path = classLoader.getResource("workflows.xml").getPath();
    SAXReader saxReader = new SAXReader();
    try {
      Document document = saxReader.read(new FileInputStream(path), "UTF-8");
      Element root = document.getRootElement();
      Element workflow = root.addElement("workflow");

      workflow.setAttributeValue("name", name);
      workflow.setAttributeValue("type", "file");
      workflow.setAttributeValue("location", name + ".xml");

      OutputFormat format = OutputFormat.createPrettyPrint();
      OutputStream out = new FileOutputStream(path);
      Writer wr = new OutputStreamWriter(out, "UTF-8");
      XMLWriter output = new XMLWriter(wr, format);
      output.write(document);
      output.close();
    }
    catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("insertworkflow");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("修改配置文件workflows.xml时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void joinConfig(String workId, String parsexml)
  {
    Document doc;
    try
    {
      doc = DocumentHelper.parseText(parsexml);
      List list = doc.selectNodes("/workflow");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("nodeId");

        Attribute att2 = step.attribute("nodeName");

        Attribute att1 = step.attribute("nodeType");
        this.method = new  WorkFlowMethod();
        String stepId = att.getValue();
        String stepType = "1";
        if (att1.getValue().equals("START_NODE")) {
          stepType = "0";
        }

        String stepName = att2.getValue();
        this.method.insertNodeInfo(workId, stepId, stepName);

        String formula = step.element("jionconfig").attributeValue("value");
        this.method.insertJoinpropertcondition(workId, stepId, formula);
      }
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("joinConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存聚合节点配置信息时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void formConfig(String workId, String parsexml)
  {
    String stepId;
    try
    {
      stepId = "";
      String formId = "";
      String tdId = "";
      String role = "";
      String check = "";
      this.method = new  WorkFlowMethod();
      Document doc = DocumentHelper.parseText(parsexml);
      List list = doc.selectNodes("/step");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("stepId");
        stepId = att.getValue();

        Element formElement = step.element("form");
        formId = formElement.attributeValue("formId");

        List tdIdElements = step.elements("tdId");
        for (Iterator iterator = tdIdElements.iterator(); iterator.hasNext(); ) {
          Element tdIdElement = (Element)iterator.next();
          tdId = tdIdElement.attributeValue("value");
          Element roleElement = tdIdElement.element("role");
          role = roleElement.attributeValue("value");

          check = roleElement.attributeValue("check");
          this.method.insertFormConfig(workId, stepId, formId, tdId, role, check);
        }
      }
    }
    catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("formConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单权限设置时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void functionConfig(String workId, String parsexml)
  {
    Document doc;
    try
    {
      doc = DocumentHelper.parseText(parsexml);
      List list = doc.selectNodes("/workflow");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("nodeId");
        this.method = new  WorkFlowMethod();
        String stepId = att.getValue();
        List functionsList = step.elements("functions");
        String buttonType = "";
        String config = "";
        for (int i = 0; i < functionsList.size(); ++i) {
          Element functions = (Element)functionsList.get(i);
          if (i == functionsList.size() - 1)
            buttonType = buttonType + functions.attributeValue("value");
          else
            buttonType = buttonType + functions.attributeValue("value") + ",";

          List funtionList = functions.elements("function");
          for (int j = 0; j < funtionList.size(); ++j) {
            Element function = (Element)funtionList.get(j);
            config = config + function.attribute("value").getValue() + ",";
          }
        }
        if (!(buttonType.equals(""))) {
          if (!(config.equals("")))
            config = config.substring(0, config.length() - 1);

          this.method.insertFunctionConfig(workId, stepId, config, buttonType);
        }
      }
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("functionConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存步骤功能按钮的设置时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void formFuncConfig(String workId, String parsexml)
  {
    Document doc;
    try
    {
      doc = DocumentHelper.parseText(parsexml);
      List list = doc.selectNodes("/workflow");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("nodeId");
        this.method = new WorkFlowMethod();
        String stepId = att.getValue();
        String funcname = step.element("func").attributeValue("value");
        this.method.insertFormFuncConfig(workId, stepId, funcname);
      }
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("formFuncConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单执行函数设置时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }

  public void subConfig(String workId, String parsexml)
  {
    String docid = "";
    try {
      Document doc = DocumentHelper.parseText(parsexml);
      List list = doc.selectNodes("/workflow");
      Iterator iter = list.iterator();
      while (iter.hasNext()) {
        Element step = (Element)iter.next();
        Attribute att = step.attribute("nodeId");
        String stepId = att.getValue();
        String subbz = step.element("subbz").attributeValue("value");
        docid = step.element("docid").attributeValue("value");
        if ((docid == null) || ("".equals(docid)) || ("null".equals(docid)))
          docid = "0";

        String formbz = step.element("formbz").attributeValue("value");
        String zwbz = step.element("zwbz").attributeValue("value");
        String fjbz = step.element("fjbz").attributeValue("value");
        String lybz = step.element("lybz").attributeValue("value");
        String lcjsbz = step.element("lcjsbz").attributeValue("value");
        this.method.insertSubConfig(workId, stepId, subbz, docid, formbz, zwbz, fjbz, lybz, lcjsbz);
      }
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("subConfig");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存子流程配置信息时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
    }
  }
}
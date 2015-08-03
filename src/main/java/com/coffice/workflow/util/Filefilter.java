package com.coffice.workflow.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import name.xio.xiorkflow.domain.logic.ProcessServiceImpl;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.coffice.workflow.condition.table.date.NodepropertconditionData;
import com.coffice.workflow.util.WorkFlowMethod;

public class Filefilter
{
  private String inFile = "";
  private String outFile = "";
  private String workId = "";

  public String getWorkId()
  {
    return this.workId;
  }

  public void setWorkId(String workId) {
    this.workId = workId;
  }

  public String getInFile() {
    return this.inFile;
  }

  public void setInFile(String inFile) {
    this.inFile = inFile;
  }

  public String getOutFile() {
    return this.outFile;
  }

  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public void filter(String fileName) {
    ProcessServiceImpl pro = new ProcessServiceImpl();
    String putInFileName = pro.getOsBaseDir() + "\\" + 
      fileName + "." + "xml";
    String putOutFileName = pro.getOsBaseDir() + "\\" + 
      "tmpXml." + "xml";
    setInFile(putInFileName);
    setOutFile(putOutFileName);
    StringBuffer content = readAndCheckFile(putInFileName);

    String temp = content.toString().replace(">&&", "><![CDATA[");
    temp = temp.toString().replace("getStatus())</arg>", "getStatus())]]></arg>");
    content = new StringBuffer(temp);

    writeFile(putOutFileName, new String(content));
    File oldFile = new File(putInFileName);
    oldFile.delete();
    StringBuffer tmpContent = readFile(putOutFileName);
    writeFile(putInFileName, new String(tmpContent));
    File tepFile = new File(putOutFileName);
    tepFile.delete();
  }

  public void filter(String path, String fileName)
  {
    String putInFileName = path + "\\" + fileName + ".xml";
    String putOutFileName = path + "\\" + "tmpXml.xml";
    setInFile(putInFileName);
    setOutFile(putOutFileName);
    StringBuffer content = readAndCheckFile(putInFileName);

    String temp = content.toString().replace(">&&", "><![CDATA[");
    temp = temp.toString().replace("getStatus())</arg>", "getStatus())]]></arg>");

    temp = temp.replaceAll("com.coffice.workflow.function.InsertRelated", "com.coffice.workflow.function.InsertRelated</arg><arg name=\"workId\">" + getWorkId());
    content = new StringBuffer(temp);

    writeFile(putOutFileName, new String(content));
    File oldFile = new File(putInFileName);
    oldFile.delete();
    StringBuffer tmpContent = readFile(putOutFileName);
    writeFile(putInFileName, new String(tmpContent));
    File tepFile = new File(putOutFileName);
    tepFile.delete();
  }

  public StringBuffer readAndCheckFile(String url) {
    StringBuffer content = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(url);
      InputStreamReader isr = new InputStreamReader(fis, "utf8");
      BufferedReader buff = new BufferedReader(isr);
      String temp = "";
      int i = 0;
      while ((temp = buff.readLine()) != null)
      {
        if ((temp.length() != 0) && 
          (temp.indexOf("&amp;") > 0)) {
          temp = temp.trim().replaceAll("&amp;", "&");
        }

        if (i == 0)
          temp = temp + "<!DOCTYPE workflow PUBLIC '-//OpenSymphony Group//DTD OSWorkflow 2.8//EN' 'workflow_2_8.dtd'>";

        ++i;

        if (temp.indexOf("<splits />") > 0)
          continue;

        if (temp.indexOf("<joins />") > 0)
          continue;

        content.append(temp).append("\n");
      }
      buff.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return content;
  }

  public StringBuffer readFile(String url)
  {
    StringBuffer content = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(url);
      InputStreamReader isr = new InputStreamReader(fis, "utf8");
      BufferedReader buff = new BufferedReader(isr);
      String temp = "";
      while ((temp = buff.readLine()) != null)
        content.append(temp).append("\n");

      buff.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return content;
  }

  public void writeFile(String url, String content) {
    FileOutputStream fos;
    try {
      fos = new FileOutputStream(url);
      Writer out = new OutputStreamWriter(fos, "utf8");
      out.write(content);
      out.flush();
      out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addConfiguration()
  {
    modifyDocument(new File(getInFile()));
    modifyworkflow(getInFile()); }

  public void modifyDocument(File inputXml) {
    try {
      SAXReader saxReader = new SAXReader();
      saxReader.setEntityResolver(
        new EntityResolver() {
        ProcessServiceImpl pro;
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException { 
        	return new InputSource(this.pro.getWebAppRoot() + "WEB-INF\\classes" + "\\workflow_2_8.dtd");
        }

      });
      Document document = saxReader.read(new FileInputStream(inputXml), "UTF-8");
      List list = document.selectNodes("//actions/action");
      Iterator iter = list.iterator();
      List steps = document.selectNodes("//steps/step");
      Iterator stepsiter = steps.iterator();
      while (stepsiter.hasNext()) {
        Element step = (Element)stepsiter.next();
        CommonMethod method = new CommonMethod();
        Element prefunction = method.addWorkitem();
        if (step.elements().size() > 0) {
          Element ref = (Element)step.elements().get(0);
          if (ref == null) {
            step.add(prefunction);
          } else {
            List listEl = step.elements();
            listEl.add(0, prefunction);
          }
        }
      }
      while (iter.hasNext()) {
        CommonMethod method;
        List listEl;
        Element element = (Element)iter.next();

        Element elementstep = element.getParent().getParent();
        Attribute att = elementstep.attribute("id");
        String stepId = att.getText();

        Element parent = element;
        att = parent.attribute("name");
        att.setText("提交");

        Element preElement = null;

        if (parent.selectNodes("pre-functions").size() > 0) {
          method = new CommonMethod();
          preElement = method.addpreFunctions(false, null);
        } else {
          method = new CommonMethod();
          preElement = method.addpreFunctions(true, stepId);
        }
        Element ref = (Element)parent.elements().get(0);
        if (ref == null) {
          parent.add(preElement);
        } else {
          listEl = parent.elements();
          listEl.add(0, preElement);
        }

        if (parent.selectNodes("pre-functions").size() > 0) {
          method = new CommonMethod();
          preElement = method.addconditions(true, stepId, "Underway");
        } else {
          method = new CommonMethod();
          preElement = method.addconditions(true, stepId, "Underway");
        }
        ref = (Element)parent.elements().get(0);
        if (ref == null) {
          parent.add(preElement);
        } else {
        	 listEl = parent.elements();
			listEl.add(0, preElement);
        }

         method = new CommonMethod();
        preElement = method.addprepostFunctions(stepId);

        parent.add(preElement);

        Element actionElement = (Element)element.clone();
        Attribute attribute = actionElement.attribute("id");
        attribute.setText("10" + attribute.getText());
        attribute = actionElement.attribute("name");
        attribute.setText("保存提交");

        List results = actionElement.selectNodes("results");
        Iterator resultsIterator = results.iterator();
        Element resultElement = (Element)resultsIterator.next();
        resultElement = resultElement.element("unconditional-result");
        Attribute stAtt = resultElement.attribute("status");
        stAtt.setText("${state}");
        stAtt = resultElement.attribute("split");
        if (stAtt != null) {
          resultElement.remove(stAtt);
          resultElement.addAttribute("step", stepId);
        }

        stAtt = resultElement.attribute("join");
        if (stAtt != null) {
          resultElement.remove(stAtt);
          resultElement.addAttribute("step", stepId);
        }
        stAtt = resultElement.attribute("step");
        if (stAtt != null)
          stAtt.setText(stepId);

        List restrictTo = actionElement.selectNodes("restrict-to");
        Iterator restrictToIterator = restrictTo.iterator();
        Element conditions = (Element)restrictToIterator.next();
        List conditsionList = conditions.elements("conditions");
        Iterator consIterator = conditsionList.iterator();
        Element conditionElement = (Element)consIterator.next();
        List conditionList = conditionElement.elements("condition");
        Iterator conIterator = conditionList.iterator();
        while (conIterator.hasNext()) {
          Element conElement = (Element)conIterator.next();
          List arglist = conElement.elements("arg");
          Element argElement = (Element)arglist.get(1);

          if (argElement.getText().equals("Underway"))
            argElement.setText("start");

        }

        Element postFunctions = actionElement.element("post-functions");
        actionElement.remove(postFunctions);
        parent.getParent().add(actionElement);
      }
      OutputFormat format = OutputFormat.createPrettyPrint();
      OutputStream out = new FileOutputStream(getInFile());
      Writer wr = new OutputStreamWriter(out, "UTF-8");
      XMLWriter output = new XMLWriter(wr, format);
      output.write(document);
      output.close();
    }
    catch (DocumentException e)
    {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage()); }
  }

  public void modifyworkflow(String filename) {
    SAXReader saxReader;
    try {
      saxReader = new SAXReader();

      saxReader.setEntityResolver(
        new EntityResolver() {
        ProcessServiceImpl pro;

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException { return new InputSource(this.pro.getWebAppRoot() + "WEB-INF\\classes" + "\\workflow_2_8.dtd");
        }

      });
      Document document = saxReader.read(new FileInputStream(filename), "UTF-8");

      List initaction = document.selectNodes("//initial-actions");
      Element action = (Element)initaction.get(0);
      Element postfunction = action.element("action").element("pre-functions");
      Element function = postfunction.element("function");
      Element argElement = function.addElement("arg");
      argElement.setAttributeValue("name", "workId");
      argElement.setText(getWorkId());

      List list = document.selectNodes("//actions");
      Iterator iter = list.iterator();
      WorkFlowMethod method = new WorkFlowMethod();
      ArrayList listcon = method.findAllNodepropertcondition(getWorkId());

      Iterator iterList = list.iterator();
      while (iterList.hasNext()) {
        Element element = (Element)iterList.next();
        Element elementstep = element.getParent();
        Attribute att = elementstep.attribute("id");
        String stepId = att.getText();

        for (int i = 0; i < listcon.size(); ++i) {
          NodepropertconditionData data = (NodepropertconditionData)listcon.get(i);

          if ((stepId.equals(data.stepID)) && 
            (data.autoStep != null) && (data.autoStep.toString().equals("1"))) {
            Element autoNode = (Element)element.element("action").clone();
            Attribute autoAtt = autoNode.attribute("id");
            autoAtt.setValue("11" + autoAtt.getValue());
            autoNode.setAttributeValue("auto", "true");

            Element res = autoNode.element("restrict-to");
            List conditions = res.element("conditions").elements("condition");
            for (Iterator conIte = conditions.iterator(); conIte.hasNext(); ) {
              Element condition = (Element)conIte.next();
              Element arg = condition.element("arg");
              if (!(arg.getText().equals("com.coffice.workflow.condition.ConDistribute"))){
            	  arg.setText("com.coffice.workflow.condition.AutoConDistribute");
              }
            }

            element.add(autoNode);
          }

        }

        Element backNode = null;

        for (int i = 0; i < listcon.size(); ++i) {
          NodepropertconditionData data = (NodepropertconditionData)listcon.get(i);

          if ((stepId.equals(data.stepID)) && 
            (data.isBack != null) && (data.isBack.equals("0"))) {
            backNode = (Element)element.element("action").clone();
            backNode.attribute("id").setText("11" + backNode.attribute("id").getText());
            backNode.attribute("name").setText("驳回");
            Element results = backNode.element("results");
            Element unconditional = results.element("unconditional-result");
            Attribute attr = unconditional.attribute("join");
            if (attr != null) {
              unconditional.remove(attr);
              unconditional.setAttributeValue("step", "1");
            }
            attr = unconditional.attribute("split");
            if (attr != null) {
              unconditional.remove(attr);
              unconditional.setAttributeValue("step", "1");
            }
            attr = unconditional.attribute("step");
            if (attr != null) {
              unconditional.remove(attr);
              unconditional.setAttributeValue("step", "1");
            }

            Element postFunctions = backNode.element("post-functions");
            Element postFunction = postFunctions.addElement("function");
            postFunction.setAttributeValue("type", "class");
            Element arg1 = postFunction.addElement("arg");
            arg1.setText("com.coffice.workflow.function.Dismiss");
            arg1.setAttributeValue("name", "class.name");
            Element arg2 = postFunction.addElement("arg");
            arg2.setAttributeValue("name", "stepId");
            arg2.setText(stepId);
            element.add(backNode);
          }

        }

      }

      List joinList = document.selectNodes("//joins");
      Iterator joiniter = joinList.iterator();
      while (joiniter.hasNext()) {
        Element element = (Element)joiniter.next();
        Element joinNode = element.element("join");
        String joinId = joinNode.attributeValue("id");
        Element conditions = joinNode.element("conditions");
        Element condition = conditions.element("condition");
        condition.attribute("type").setText("class");
        Element arg = condition.element("arg");
        arg.attribute("name").setText("class.name");
        arg.setText("com.coffice.workflow.condition.JoinCondition");

        Element stepArg = condition.addElement("arg");
        stepArg.setAttributeValue("name", "stepId");
        stepArg.setText(joinId);
      }

      OutputFormat format = OutputFormat.createPrettyPrint();
      OutputStream out = new FileOutputStream(getInFile());
      Writer wr = new OutputStreamWriter(out, "UTF-8");
      XMLWriter output = new XMLWriter(wr, format);
      output.write(document);
      output.close();
    } catch (DocumentException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void modifyHuit(String realPath1, String ospath, String name, String yhid)
  {
    String filename = ospath + name + ".xml";
    final String realPath = realPath1;
    try {
      SAXReader saxReader = new SAXReader();

      saxReader.setEntityResolver(
        new EntityResolver() {
        ProcessServiceImpl pro;

        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException { 
        	return new InputSource((new StringBuilder(String.valueOf(realPath))).append("WEB-INF\\classes").append("\\workflow_2_8.dtd").toString());
        }

      });
      Document document = saxReader.read(new FileInputStream(filename), "UTF-8");

      List list = document.selectNodes("//actions");
      Iterator iter = list.iterator();
      WorkFlowMethod method = new WorkFlowMethod();
      ArrayList listcon = method.findAllNodepropertcondition(getWorkId());

      Iterator iterList = list.iterator();
      while (iterList.hasNext()) {
        Element element = (Element)iterList.next();
        Element elementstep = element.getParent();
        Attribute att = elementstep.attribute("id");
        String stepId = att.getText();

        for (int i = 0; i < listcon.size(); ++i) {
          NodepropertconditionData data = (NodepropertconditionData)listcon.get(i);

          if ((stepId.equals(data.stepID)) && 
            (data.autoStep != null) && (data.autoStep.toString().equals("1"))) {
            Element autoNode = (Element)element.element("action").clone();
            Attribute autoAtt = autoNode.attribute("id");
            autoAtt.setValue("11" + autoAtt.getValue());
            autoNode.setAttributeValue("auto", "true");

            Element res = autoNode.element("restrict-to");
            List conditions = res.element("conditions").elements("condition");
            for (Iterator conIte = conditions.iterator(); conIte.hasNext(); ) {
              Element condition = (Element)conIte.next();
              Element arg = condition.element("arg");
              if (!(arg.getText().equals("com.coffice.workflow.condition.ConDistribute"))) {
                arg.setText("com.coffice.workflow.condition.AutoConDistribute");
              }
            }

            element.add(autoNode);
          }

        }

        Element backNode = null;
        System.out.println("action-->stepId：" + stepId);
        for (int i = 0; i < listcon.size(); ++i) {
          NodepropertconditionData data = (NodepropertconditionData)listcon.get(i);
          System.out.println("node-->stepId：" + data.stepID);
          if (stepId.equals(data.stepID)) {
            System.out.println("action==node-->stepId：" + data.stepID);

            if ((data.isBack != null) && (data.isBack.equals("0")))
            {
              Iterator iter1 = element.elementIterator("action");
              while (iter1.hasNext()) {
                Element element1 = (Element)iter1.next();
                Attribute att1 = element1.attribute("name");
                if (att1.getText().equals("回退"))
                  break;
              }

              System.out.println("add huit-->stepId：" + data.stepID);
              backNode = (Element)element.element("action").clone();
              backNode.attribute("id").setText("11" + backNode.attribute("id").getText());
              backNode.attribute("name").setText("回退");
              Element results = backNode.element("results");
              Element unconditional = results.element("unconditional-result");
              Attribute attr = unconditional.attribute("join");
              if (attr != null) {
                unconditional.remove(attr);
                unconditional.setAttributeValue("step", "3");
              }
              attr = unconditional.attribute("split");
              if (attr != null) {
                unconditional.remove(attr);
                unconditional.setAttributeValue("step", "3");
              }
              attr = unconditional.attribute("step");
              if (attr != null) {
                unconditional.remove(attr);
                unconditional.setAttributeValue("step", "3");
              }

              Element postFunctions = backNode.element("post-functions");
              Element postFunction = postFunctions.addElement("function");
              postFunction.setAttributeValue("type", "class");
              Element arg1 = postFunction.addElement("arg");
              arg1.setText("com.coffice.workflow.function.Dismiss");
              arg1.setAttributeValue("name", "class.name");
              Element arg2 = postFunction.addElement("arg");
              arg2.setAttributeValue("name", "stepId");
              arg2.setText(stepId);
              element.add(backNode);

              break;
            }
          }
        }

      }

      OutputFormat format = OutputFormat.createPrettyPrint();
      OutputStream out = new FileOutputStream(filename);
      Writer wr = new OutputStreamWriter(out, "UTF-8");
      XMLWriter output = new XMLWriter(wr, format);
      output.write(document);
      output.close();
    } catch (DocumentException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
package com.coffice.workflow.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import name.xio.xiorkflow.domain.logic.ProcessServiceImpl;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class Transform extends BaseUtil
{
  LogItem logItem;
  private String workid;

  public boolean xmlTxml(String xmlFile)
  {
    this.logItem = new LogItem();
    try {
      ProcessServiceImpl pro = new ProcessServiceImpl();
      Source xmlSource = new StreamSource(pro.getBaseDirName() + "\\" + xmlFile + "." + "xml");
      Source xsltSource = new StreamSource(pro.getBaseDirName() + "\\" + "defaultxsl.xsl");

      TransformerFactory transFact = TransformerFactory.newInstance();
      Transformer trans = transFact.newTransformer(xsltSource);
      Properties properties = trans.getOutputProperties();
      properties.setProperty("encoding", "utf-8");
      trans.setOutputProperties(properties);
      File outFile = new File(pro.getOsBaseDir() + "\\" + xmlFile + "." + "xml");
      trans.transform(xmlSource, new StreamResult(new FileOutputStream(outFile)));

      formatXMLFile(pro.getOsBaseDir() + "\\" + xmlFile + "." + "xml");
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("xmlTxml");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("处理xml转换的类时出现异常");
      this.logItem.setContent(e.getMessage());
      Log.write(this.logItem);
    }

    Filefilter file = new Filefilter();
    file.filter(xmlFile);
    file.setWorkId(getWorkid());
    file.addConfiguration();
    return true;
  }

  public int formatXMLFile(String filename)
  {
    int returnValue = 0;
    try
    {
      SAXBuilder builder = new SAXBuilder();
      Document doc = builder.build(new File(filename));
      XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
      out.output(doc, new FileOutputStream(new File(filename)));

      returnValue = 1;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return returnValue;
  }

  public String getWorkid() {
    return this.workid; }

  public void setWorkid(String workid) {
    this.workid = workid;
  }
}
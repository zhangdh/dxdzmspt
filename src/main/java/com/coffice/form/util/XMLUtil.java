package com.coffice.form.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;

public class XMLUtil
{
  public static String generateXmlString(List<? extends Object> list)
    throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    int length = list.size();
    Document doc = new DOMDocument();
    String className = list.get(0).getClass().toString();
    Element root = new DOMElement("root");
    for (int i = 0; i < length; ++i) {
      Object obj = list.get(i);
      Element e = new DOMElement(
        className.substring(className.lastIndexOf(".") + 1).toLowerCase());
      Class cls = obj.getClass();
      Method[] m = cls.getMethods();
      Method[] arrayOfMethod1 = m;  i = 0; for (int j = arrayOfMethod1.length; i < j; ++i) { Method method = arrayOfMethod1[i];
        String methodName = method.getName();
        if (methodName.indexOf("get") != -1) {
          String attribute = methodName.split("get")[1].toLowerCase();
          String attributeValue = (method.invoke(obj, new Object[0]) != null) ? 
            method.invoke(obj, new Object[0]).toString() : "NULL";
          Element el = new DOMElement(attribute);
          el.addText(attributeValue);
          e.add(el);
        }
      }
      root.add(e);
    }
    doc.add(root);
    return doc.asXML();
  }
}
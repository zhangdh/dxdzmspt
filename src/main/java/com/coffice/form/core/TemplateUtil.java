package com.coffice.form.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coffice.form.bean.FieldInfo;
import com.coffice.form.util.StringUtil;
import com.coffice.util.SysPara;

public class TemplateUtil
{
  private static final String REGEX_INPUT = "<input(.*?)/>";
  private static final String REGEX_TEXTAREA = "<textarea [^//]+/{1}textarea>";
  private static final String REGEX_DROPDOWNLIST = "<select [^//]+/{1}option>[^//]*/{1}select>";
  private static final String REGEX_SPAN = "<span([^>]*?)\\s(datatype)(.*?)>(.*?)</span>";
  private static final String REGEX_TD = "<td([^<]*?)\\s(id)([^<]*?)>";
  private static final String REGEX_INPUT_BUTTON_old = "<input type=\"button\"{1}[^//]+ />{1}";
  private static final String REGEX_INPUT_BUTTON = "<input type=\"button\"(.*?)\\s(mark=\"huiqian\"|mark=\"ziliucheng\")(.*?)/>";
  private static final String REGEX_INPUT_CHECKBOX = "<input([^<]*?)\\s(type=\"checkbox\")(.*?)/>";
  private static final String REGEX_INPUT_USERPICKER = "<input[^//]*(pickertype){1}[^//]+ />{1}";
  public static final String REGEX_REMARK = "remark=\"{1}[^\"]+\"{1}";
  public static final String SYS_INSERT = "0";
  public static final String SYS_INSERT_MANUAL = "3";
  public static final String USER_INSERT = "1";
  public static final String ATTITUDE_INSERT = "2";
  public static final String SUB_PROCESS_INFO = "4";
  public static final String USERPICKER_PREFIX = "_option_";
  private static final String USERPICKER_SEND_TO = "0";
  private static final String USERPICKER_COPY_TO = "1";
  private static final String USERPICKER_AUTHOR = "2";
  private static final String USERPICKER_OTHERS = "3";

  public void createTemplateFile(String path, String sourceName, String targetName, String form)
    throws IOException, DocumentException
  {
    String content = readTemplate(path, sourceName);

    form = replaceButton(form);
    form = replaceUserPicker(form);
    form = replaceSpan(form);
    form = replaceCheckBox(form);
    String t = replaceTags(content, form);
    writeTemplate(t, path, targetName);
  }

  public void createPrintTemplateFile(String path, String sourceName, String targetName, String printTemplateSource)
    throws IOException, DocumentException
  {
    String content = readTemplate(path, sourceName);
    String t = repacePrintTags(content, printTemplateSource);
    writeTemplate(t, path, targetName);
  }

  public List<FieldInfo> getFields(String formContent)
  {
    return getFeilds(formContent);
  }

  private String readTemplate(String path, String sourceName)
    throws IOException
  {
    StringBuffer content = new StringBuffer();
    File oldFile = new File(path + sourceName);
    BufferedReader buff = new BufferedReader(new FileReader(oldFile));
    String temp = "";
    while ((temp = buff.readLine()) != null)
      content.append(temp).append(System.getProperty("line.separator"));

    buff.close();
    return content.toString();
  }

  private String replaceTags(String content, String form)
  {
    form = addFreeMarkerDirectives(form);
    return content.replaceFirst("replaceme", 
      StringUtil.filterDollarStr(form));
  }

  private String repacePrintTags(String content, String form)
  {
    return content.replaceFirst("replaceme", 
      StringUtil.filterDollarStr(form));
  }

  private String addFreeMarkerDirectives(String form) {
    String newForm = form;
    Pattern p = Pattern.compile("<select [^//]+/{1}option>[^//]*/{1}select>", 
      2);
    Matcher m = p.matcher(form);
    boolean rs = m.find();
    while (rs) {
      int start = m.start();
      int end = m.end();
      String temp = form.substring(start, end);
      String newTemp = "";
      try {
        newTemp = addDirectives(temp);
      } catch (DocumentException e) {
        e.printStackTrace();
      }
      newForm = newForm.replace(temp, newTemp);
      rs = m.find();
    }
    return "<form id='_formid' action='/form/default.do?method=saveData&tableName=${tableName}' method='POST'><input type='hidden' id='_id'  name='id' value=${id}>" + 
      newForm + "<input type='hidden' name='viewflow' id='viewflow' value='${viewflow}'></form>";
  }

  public void writeTemplate(String content, String path, String targetName)
    throws IOException
  {
    File newFile = new File(path + targetName);
    BufferedWriter buff = new BufferedWriter(new FileWriter(newFile));
    buff.write(content);
    buff.flush();
    buff.close();
  }

  private List<FieldInfo> getFeilds(String form)
  {
    List list = new ArrayList();
    String[] regEx = { "<input(.*?)/>", "<textarea [^//]+/{1}textarea>", "<select [^//]+/{1}option>[^//]*/{1}select>", 
      "<td([^<]*?)\\s(id)([^<]*?)>", "<span([^>]*?)\\s(datatype)(.*?)>(.*?)</span>" };
    FieldInfo field = null;
    Pattern p = null;
    Matcher m = null;
    boolean rs = false;

    String[] arrayOfString1 = regEx; 
    int i = 0;
    for (int j = arrayOfString1.length; i < j; ++i) { 
      String reX = arrayOfString1[i];
      p = Pattern.compile(reX, 2);
      m = p.matcher(form);
      rs = m.find();
      while (rs) {
        int start = m.start();
        int end = m.end();
        String temp = form.substring(start, end);
        System.out.println(temp);

        field = 
          getFieldInfoByTag(StringUtil.ampersandFilter(temp));
        if (field != null)
          list.add(field);
        rs = m.find();
      }
    }
    return list;
  }

  private FieldInfo getFieldInfoByTag(String tagString)
  {
    Document document;
    FieldInfo info = null;
    if (tagString.indexOf("<td") != -1) {
      tagString = tagString + "</td>";
    }

    try
    {
      document = DocumentHelper.parseText("<root>" + tagString + "</root>");
      Element root = document.getRootElement();
      for (Iterator i = root.elementIterator(); i.hasNext(); ) {
        Element element = (Element)i.next();
        if (element.getName() != null) {
          info = new FieldInfo();
          info.setComponentType(element.getName().toUpperCase());
          if (("input".equalsIgnoreCase(element.getName())) && (element.attribute("pickertype") != null)) {
            String picker = element.attributeValue
              ("pickertype");
            String sid = "0";
            if ("sdcncsi_ict_cs".equalsIgnoreCase(picker))
              sid = "1";
            else if ("sdcncsi_ict_ngr".equalsIgnoreCase(picker))
              sid = "2";
            else if ("other".equalsIgnoreCase(picker))
              sid = "3";
            info.setSequenceTypeId(sid);
            info.setLb("1800");
            info.setFieldLength(1000);
          }

          if (("input".equalsIgnoreCase(element.getName())) && 
            ("checkbox".equalsIgnoreCase(
            element.attributeValue("type"))))
            info.setLb("1802");

          if (("input".equalsIgnoreCase(element.getName())) && 
            ("button".equalsIgnoreCase(
            element.attributeValue("type"))))
            if ("huiqian".equals(element.attributeValue("mark"))) {
              info.setInsertType("2");
              info.setLb("1801");
            } else if ("ziliucheng".equals(element.attributeValue("mark"))) {
              info.setInsertType("4"); } else {
              if ("anniu".equals(element.attributeValue("mark")))
                return null;

              if (!(SysPara.compareValue("form_button_mark", "true"))) {
                info.setInsertType("2");
                info.setLb("1801");
              } else {
                return null;
              }
            }

          if (element.getName().equalsIgnoreCase("select")) {
            info.setSequenceTypeId
              (element.attributeValue
              ("typeid"));
          } else if ("td".equalsIgnoreCase(element.getName())) {
            info.setFieldName(element.attributeValue("id"));
            info.setTdId(element.attributeValue("id"));
          } else if ("span".equalsIgnoreCase(element.getName())) {
            info.setFieldName(element.attributeValue("name"));
            info.setSequenceTypeId(
              element.attributeValue("datatype"));

            String inserttype = element.attributeValue("inserttype");
            if ("3".equals(inserttype))
              info.setInsertType("3");
            else
              info.setInsertType("0");
          }

          if (element.attribute("remark") != null)
            info.setRemark(element.attributeValue("remark"));
        }

        if ((element.attribute("name") != null)) 
        info.setFieldName(element.attributeValue("name"));
        if (element.attribute("datatype") != null) {
          String datatype = element.attributeValue("datatype");
          if ("numeric".equals(datatype))
            info.setFieldType("FLOAT");
          else if ("datetime".equals(datatype))
            info.setFieldType("DATETIME");
          else
            info.setFieldType("VARCHAR");

        }
        else
        {
          info.setFieldType("VARCHAR");
        }
        if (element.attribute("pid") != null)
          info.setTdId(element.attributeValue("pid"));
        if (element.attribute("order") != null) 
        info.setXh(element.attributeValue("order"));

        
      }
    } catch (DocumentException e) {
      e.printStackTrace();
      return null;
    }

    if (("VARCHAR".equals(info.getFieldType())) && (info.getFieldLength() == 0))
     info.setFieldLength(255);
    else if ("FLOAT".equals(info.getFieldType()))
      info.setFieldLength(11);
    return info;
  }

  private String addDirectives(String tagString) throws DocumentException
  {
    String name = "";
    tagString = tagString.replace("&nbsp;", "");
    Document document = DocumentHelper.parseText("<root>" + tagString + 
      "</root>");
    Element root = document.getRootElement();
    for (Iterator i = root.elementIterator(); i.hasNext(); ) {
      Element element = (Element)i.next();
      if (element.attribute("name") == null) break;
      name = element.attributeValue("name");

      break;
    }
    tagString = StringUtil.filterStr(tagString).replaceAll(
      "selected=\"selected\"", "");
    String head = tagString.substring(0, tagString.indexOf(">") + 1);
    String body = tagString.substring(tagString.indexOf(">") + 1, 
      tagString.lastIndexOf("<"));
    String foot = tagString.substring(tagString.lastIndexOf("<"));

    String bodyTemp_1 = body.substring(0, body.indexOf(">"));
    String bodyTemp_2 = body.substring(body.indexOf(">"));
    body = bodyTemp_1 + "<#if t.svalue==" + name + 
      "_value>selected=\"selected\"</#if>" + bodyTemp_2;
    return head + "<#list " + name + " as t>" + body + "</#list>" + foot;
  }

  
  private String replaceButton(String form) throws DocumentException
  {
    Pattern p = null;
    Matcher m = null;
    boolean rs = false;
    String newForm = form;

    if (SysPara.compareValue("form_button_mark", "true"))
      p = Pattern.compile("<input type=\"button\"(.*?)\\s(mark=\"huiqian\"|mark=\"ziliucheng\")(.*?)/>", 2);
    else {
      p = Pattern.compile("<input type=\"button\"{1}[^//]+ />{1}", 2);
    }

    m = p.matcher(form);
    rs = m.find();
    while (rs) {
      StringBuffer sb;
      String name = ""; String pid = "";
      String exp_isshow = "";
      int start = m.start();
      int end = m.end();
      String temp = form.substring(start, end);
      temp = temp.replace("&nbsp;", "");
      Document document = DocumentHelper.parseText("<root>" + temp + 
        "</root>");
      Element root = document.getRootElement();
      Iterator i = root.elementIterator();
      if (i.hasNext()) {
        Element element = (Element)i.next();
        if (element.attribute("name") != null)
          name = element.attributeValue("name");
        if (element.attribute("pid") != null) {
          pid = element.attributeValue("pid");
        }

      }

      if (SysPara.compareValue("form_spyj", "1", "1")) {
        sb = new StringBuffer();
        sb.append("<#if ");
        sb.append(name);
        sb.append("??&&");
        sb.append(name);
        sb.append("?size != 0><span><br><#list ");
        sb.append(name);
        sb.append(" as t>");
        if (SysPara.compareValue("huiqian_exp", "1", "1")) {
          sb.append("${t.content}(<#if signature_enabled??&&(signature_enabled=\"true\")><#if t.loginCode??><img src='${context}/showpic/default.do?method=pic&mk_dm=form&imgzid=${t.loginCode}'/><#else>${t.temp}</#if><#else>${t.loginCode}</#if>)${t.insertDate}");
        } else {
          String exp = "";
          try {
            exp = SysPara.getValue("huiqian_exp");
          } catch (Exception e) {
            exp = "${content}(${username})${insertdate}${inserttime}";
          }
          if (exp.indexOf("${insertdate}") > 0)
            exp = exp.replace("${insertdate}", "${insertDateTime?string('yyyy-MM-dd')}");

          if (exp.indexOf("${inserttime}") > 0)
            exp = exp.replace("${inserttime}", " ${insertDateTime?string('HH:mm:ss')}");

          exp = exp.replace("${", "${t.");
          sb.append("<#if signature_enabled??&&(signature_enabled=\"true\")><#if t.loginCode??>");
          sb.append(exp.replace("${t.username}", "<img src='${context}/showpic/default.do?method=pic&mk_dm=form&imgzid=${t.loginCode}'/>"));
          sb.append("<#else>");
          sb.append(exp.replace("${t.username}", "${t.temp}"));
          sb.append("</#if><#else>");
          sb.append(exp.replace("${t.username}", "${t.loginCode}"));
          sb.append("</#if>");
        }

        sb.append("<br></#list></span></#if><br><TEXTAREA name=\"");
        sb.append(name);
        sb.append("\" id=\"");
        sb.append(name);
        sb.append("\" ");
        if (!("".equals(pid))) {
          sb.append("style=\"display:${").append(pid);
          sb.append("_show_huiqian_inputbox?default(\"''\")}\"");
        }
        sb.append("></TEXTAREA>");
        sb.append("<br><#if form_spyj1223??&&form_spyj1223?size != 0");
        if (!("".equals(pid))) {
          sb.append("&&");
          sb.append(pid);
          sb.append("_tdClass=='DEFAULT'");
        }
        if (SysPara.compareValue("form_spyj_style", "1", "1")) {
          sb.append("><select  name=\"form_spyj1223\" onchange=\"$('#");
          sb.append(name);
          sb.append("').attr('value',this.value);\">");
          sb.append("<option value=\"\" >选择意见常用语</option>");
          sb.append("<#list form_spyj1223 as t>");
          sb.append("<option value=\"${t.yjnr}\">${t.yjlable}</option>");
          sb.append("</#list></select>");
        } else {
          sb.append(">");
          sb.append("<#list form_spyj1223 as t>");
          sb.append("<input type=\"button\"  name=\"form_spyj1223\" onclick=\"$('#");
          sb.append(name);
          sb.append("').attr('value',<#if t.yjlable??&&t.yjlable=='签名'>'${current_logged_on_user}'<#else>this.value</#if>);\"");
          sb.append(" value=\"${t.yjlable}\"/>");
          sb.append("</#list>");
        }

        sb.append("</#if>");
        newForm = newForm.replace
          (temp, sb.toString());
      } else {
        sb = new StringBuffer();
        newForm = newForm.replace
          (
          temp, 
          "<#if " + 
          name + 
          "??&&" + 
          name + 
          "?size != 0><span><br><#list " + 
          name + 
          " as t>${t.content}(<#if signature_enabled??&&(signature_enabled=\"true\")><#if t.loginCode??><img src='${context}/showpic/default.do?method=pic&mk_dm=form&imgzid=${t.loginCode}'/><#else>${t.temp}</#if><#else>${t.loginCode}</#if>)${t.insertDate}<br></#list></span></#if><br><TEXTAREA name=\"" + 
          name + "\"></TEXTAREA>");
        sb.append("<#if ");
        sb.append(name);
        sb.append("??&&").append(name);
        sb.append("?size != 0><span><br><#list ");
        sb.append(name);
        sb.append(" as t>${t.content}(<#if signature_enabled??&&(signature_enabled=\"true\")><#if t.loginCode??><img src='${context}/showpic/default.do?method=pic&mk_dm=form&imgzid=${t.loginCode}'/><#else>${t.temp}</#if><#else>${t.loginCode}</#if>)${t.insertDate}<br></#list></span></#if><br><TEXTAREA name=\"");
        sb.append(name);
        sb.append("\" ");
        if (!("".equals(pid))) {
          sb.append("style=\"display:${").append(pid);
          sb.append("_show_huiqian_inputbox?default(\"''\")}\"");
        }
        sb.append("></TEXTAREA>");
        newForm = newForm.replace(temp, sb.toString());
      }

      rs = m.find();
    }
    return newForm;
  }

  private String replaceCheckBox(String form) throws DocumentException {
    Pattern p = null;
    Matcher m = null;
    boolean rs = false;
    String newForm = form;
    p = Pattern.compile("<input([^<]*?)\\s(type=\"checkbox\")(.*?)/>", 2);
    m = p.matcher(form);
    rs = m.find();
    while (rs) {
      String name = ""; String value = "";
      int start = m.start();
      int end = m.end();
      String temp = form.substring(start, end);
      temp = temp.replace("&nbsp;", "");
      Document document = DocumentHelper.parseText("<root>" + temp + 
        "</root>");
      Element root = document.getRootElement();
      Iterator i = root.elementIterator();
      if (i.hasNext()) {
        Element element = (Element)i.next();
        if (element.attribute("name") != null)
          name = element.attributeValue("name");
        if (element.attribute("value") != null)
          value = element.attributeValue("value");
      }

      StringBuffer sb = new StringBuffer();
      sb.append("<#if id??&&id!=''>");
      sb.append("<input type=checkbox name=").append(name);
      sb.append(" value=").append(value);
      sb.append("<#if ").append(name).append("??&&").append(name).append("=='");
      sb.append(value).append("'> checked</#if>>");
      sb.append("<#else>").append(temp).append("</#if>");
      newForm = newForm.replace
        (temp, sb.toString());

      rs = m.find();
    }
    return newForm;
  }

  private String replaceUserPicker(String form) throws DocumentException {
    Pattern p = null;
    Matcher m = null;
    boolean rs = false;
    String newForm = form;
    p = Pattern.compile("<input[^//]*(pickertype){1}[^//]+ />{1}", 2);
    m = p.matcher(form);
    rs = m.find();
    while (rs) {
      int start = m.start();
      int end = m.end();
      String temp = form.substring(start, end);
      temp = temp.replace("&nbsp;", "");
      Document document = null;
      try {
        document = 
          DocumentHelper.parseText("<root>" + temp + "</root>");
        Element root = document.getRootElement();
        Iterator i = root.elementIterator();
        if (i.hasNext()) {
          Element element = (Element)i.next();
          String name = element.attributeValue("name");
          String view = element.attributeValue("show");
          if ((view == null) || ("".equals(view)))
            view = "1";

          String show = "";

          if ((!(name.equals("sdcncsi_ict_zs"))) && (!(name.equals("sdcncsi_ict_cs"))))
            show = "bm,yh";
          else {
            show = "bm,gw,js,yh";
          }

          StringBuilder builder = new StringBuilder();
          builder.append
            ("<input type='hidden' name='").append
            (name).append
            ("' id='").append
            (name).append
            ("9527' value='${").append
            (name).append
            ("}' show='").append
            (view).append
            ("'>").append
            ("<select id='").append
            (name).append
            ("_lb' pickertype='1'>").append
            ("<#if ").append
            ("_option_").append
            (name).append
            ("??&&(").append
            ("_option_").append
            (name).append
            ("?size>0)><#list ").append
            ("_option_").append
            (name).append
            (" as t>").append
            ("<option value='${t.dm}'>").append
            ("${t.mc}").append
            ("</option></#list></#if></select>").append
            (
            "&nbsp;&nbsp;<input type='button' value='&gt;&gt;'").append
            (" onclick = ").append
            (
            "sys_selStaff_openWin('${context}/org/default.do?method=select_pub&sys_fsfw_lb=").append
            (name).append("_lb&sys_fsfw=").append(name).append
            ("9527&show=").append(show).append("')").append(
            ">");
          newForm = newForm.replace(temp, builder.toString());
        }
        rs = m.find();
      } catch (DocumentException e) {
        e.printStackTrace();
        throw e;
      }
    }
    return newForm;
  }

  private String replaceSpan(String form) throws DocumentException
  {
    Pattern p = null;
    Matcher m = null;
    boolean rs = false;
    String newForm = form;
    p = Pattern.compile("<span([^>]*?)\\s(datatype)(.*?)>(.*?)</span>", 2);
    m = p.matcher(form);
    rs = m.find();
    while (rs) {
      String name = ""; String insertType = "";
      String datatype = "";
      int start = m.start();
      int end = m.end();
      String temp = form.substring(start, end);
      temp = temp.replace("&nbsp;", "");
      Document document = DocumentHelper.parseText("<root>" + temp + 
        "</root>");
      Element root = document.getRootElement();
      Iterator i = root.elementIterator();
      if (i.hasNext()) {
        Element element = (Element)i.next();
        if (element.attribute("name") != null)
          name = element.attributeValue("name");
        insertType = element.attributeValue("inserttype");
        datatype = element.attributeValue("datatype");
      }
      if ("0".equals(insertType)) {
        newForm = newForm.replace(temp, "<input type='text' name='" + name + 
          "' value='${" + name + "}'/>");
      }
      else
      {
        newForm = newForm.replace(temp, "<input type='text' name='" + name + 
          "' id='" + name + "' datatype='" + datatype + "' value='${" + name + "}'/>");
      }
      rs = m.find();
    }
    return newForm;
  }

  private String[] addPrintFreeMarkerDirectives(Map<String, FieldInfo> map, String[] ids)
  {
    String[] outIds = new String[ids.length];
    for (int i = 0; i < ids.length; ++i) {
      String tmp = "${" + ids[i] + "}";
      if (map.containsKey(ids[i])) {
        FieldInfo info = (FieldInfo)map.get(ids[i]);
        if ("SELECT".equalsIgnoreCase(info.getComponentType()))
          tmp = "<select name='" + 
            ids[i] + 
            "'><#list " + 
            ids[i] + 
            " as t><option value='${t.svalue}' <#if " + 
            ids[i] + 
            "_value??&&t.svalue==" + 
            ids[i] + 
            "_value>selected='selected'</#if>>${t.sname}</option></#list></select>";
        else if (("INPUT".equalsIgnoreCase(info.getComponentType())) && 
          ("2".equals(
          info.getInsertType())))
          tmp = "<#if " + 
            ids[i] + 
            "??&&" + 
            ids[i] + 
            "?size != 0><span><br><#list " + 
            ids[i] + 
            " as t>${t.content} (${t.loginCode})${t.insertDate}<br></#list></span></#if><br><TEXTAREA name=\"" + 
            ids[i] + "\"></TEXTAREA>";
      }
      outIds[i] = tmp;
    }
    return outIds;
  }

  public String generateHTMLCode(Map<String, FieldInfo> map, String formId, String[] ids, String[] x, String[] y)
  {
    StringBuilder builder = new StringBuilder("");
    String[] tags = addPrintFreeMarkerDirectives(map, ids);
    if ((tags.length == x.length) && (tags.length == y.length)) {
      int size = tags.length;
      for (int i = 0; i < size; ++i) {
        String id = tags[i];
        String ax = x[i];
        String ay = y[i];
        builder.append("<div style='top:").append(ay).append(";left:").append
          (ax).append("'").append(">").append(id).append(
          "</div>");
      }
    }
    return builder.toString();
  }
}
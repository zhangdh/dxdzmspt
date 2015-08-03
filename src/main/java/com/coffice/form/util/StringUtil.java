package com.coffice.form.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.springframework.jdbc.core.JdbcTemplate;

import com.coffice.form.bean.FieldInfo;
import com.coffice.util.Db;
import com.coffice.util.SysPara;

public class StringUtil
{
  public static StringBuffer addSqlString(StringBuffer sql, FieldInfo info, String str)
  {
    sql.append(str);
    if (info.getFieldType().indexOf("VARCHAR") != -1) {
      if (info.getFieldValue() == null)
        sql.append(info.getFieldValue());
      else
        sql.append("'" + info.getFieldValue() + "'");
    }
    else if (info.getFieldType().indexOf("FLOAT") != -1) {
      sql.append(info.getFieldValue());
    }
    else if (info.getFieldValue() == null) {
      sql.append(info.getFieldValue());
    }
    else if (SysPara.compareValue("db_type", "oracle")) {
      String tmp = info.getFieldValue();
      if (tmp.indexOf(":") > 0)
        sql.append("to_date('" + info.getFieldValue() + "','YYYY-MM-DD hh24:mi:ss')");
      else
        sql.append("to_date('" + info.getFieldValue() + "','YYYY-MM-DD')");
    }
    else {
      sql.append("'" + info.getFieldValue() + "'");
    }

    return sql;
  }

  public static String filterDollarStr(String str)
  {
    String sReturn = "";
    if (!(str.equals(""))) {
      if (str.indexOf(36, 0) > -1) {
        while (str.length() > 0) {
          if (str.indexOf(36, 0) > -1) {
            sReturn = sReturn + str.subSequence(0, str.indexOf(36, 0));
            sReturn = sReturn + "\\$";
            str = str.substring(str.indexOf(36, 0) + 1, 
              str.length());
          } else {
            sReturn = sReturn + str;
            str = "";
          }

        }

      }
      else
        sReturn = str;

    }

    return sReturn;
  }

  public static String filterStr(String str)
  {
    String sReturn = "";
    if (!(str.equals(""))) {
      if (str.indexOf(34, 0) > -1) {
        while (str.length() > 0) {
          if (str.indexOf(34, 0) > -1) {
            sReturn = sReturn + str.subSequence(0, str.indexOf(34, 0));
            sReturn = sReturn + "\"";
            str = str.substring(str.indexOf(34, 0) + 1, 
              str.length());
          } else {
            sReturn = sReturn + str;
            str = "";
          }

        }

      }
      else
        sReturn = str;

    }

    return sReturn;
  }

  public static String ampersandFilter(String sourceString) {
    return sourceString.replaceAll("&", "");
  }

  public static String regFilter(String reg, String target)
  {
    return target.replaceAll(reg, ""); }

  public String readProperties(String filePath, String key) {
    Properties prop = new Properties();
    InputStream in = super.getClass().getResourceAsStream(filePath);
    try {
      prop.load(in);
      return ((String)prop.get(key));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return ""; }

  public static boolean isExistsTable(String tablename) {
    String sql = "";
    if (SysPara.compareValue("db_type", "oracle"))
      sql = "select table_name  from all_tables where table_name ='" + tablename.toUpperCase() + "'";
    else if (SysPara.compareValue("db_type", "sqlserver"))
      sql = "select name from sysobjects where name='" + tablename + "' and type='U'";
    else if (SysPara.compareValue("db_type", "mysql"))
      sql = "select table_name from information_schema.tables where table_name = '" + tablename + "'";
    else
      return false;

    List list = Db.getJtN().queryForList(sql);

    return (list.size() > 0);
  }
}
package com.coffice.solr;

import java.io.InputStream;
import java.util.Properties;

public class ProperUtil
{
  private static InputStream inputStream = ProperUtil.class.getResourceAsStream("solr.properties");
  private static Properties p = new Properties();

  static {
    try { p.load(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getURL() {
    String url = "";
    url = p.getProperty("connetionURL");
    return url;
  }
  public static String getValueByProper(String proper) {
    return p.getProperty(proper);
  }
}
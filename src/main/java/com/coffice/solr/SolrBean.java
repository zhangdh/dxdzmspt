package com.coffice.solr;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolrBean
{
  private String guid;
  private String bt;
  private String lbid;
  private String qxids;
  private String content;
  private Map<String, String> attachments = new HashMap();

  private Map<String, String> fjlj = new HashMap();

  private Map<String, String> dynamicStr = new HashMap();

  private Map<String, String> dynamicContent = new HashMap();
  private Date cjsj;

  public Date getCjsj()
  {
    return this.cjsj;
  }

  public void setCjsj(Date cjsj) {
    this.cjsj = cjsj;
  }

  public String getGuid() {
    return this.guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getBt() {
    return this.bt;
  }

  public void setBt(String bt) {
    this.bt = bt;
  }

  public String getLbid() {
    return this.lbid;
  }

  public void setLbid(String lmid) {
    this.lbid = lmid;
  }

  public String getQxids() {
    return this.qxids;
  }

  public void setQxids(String qxids) {
    this.qxids = qxids;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void addAttachments(String fujianid, String cclj)
  {
    if ((fujianid == null) || (fujianid.trim().equals("")) || (cclj == null) || 
      (cclj.trim().equals(""))) {
      return;
    }
    this.attachments.put(fujianid, cclj);
  }

  public Map<String, String> getFjlj(){
    Iterator fjs = this.attachments.keySet().iterator();
    String fjid = "";
    List<Map> fjList = null;
    while (fjs.hasNext()) {
      fjid = (String)fjs.next();

      for (Map fjMap : fjList) {
        this.fjlj.put((String)fjMap.get("guid"), (String)fjMap.get("pre") + 
          (String)fjMap.get("lj"));
      }
    }
    return this.fjlj;
  }

  public Map<String, String> getDynamicStr() {
    return this.dynamicStr;
  }

  public void setDynamicStr(Map<String, String> dynamicStr) {
    this.dynamicStr = dynamicStr;
  }

  public Map<String, String> getDynamicContent() {
    return this.dynamicContent;
  }

  public void setDynamicContent(Map<String, String> dynamicContent) {
    this.dynamicContent = dynamicContent;
  }
}
package com.coffice.form.bean;

import java.io.Serializable;

public class FormInfo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String formName;
  private String tableName;
  private String templateName;
  private String inUse = "Y";

  public String getTemplateName()
  {
    return this.templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getInUse() {
    return this.inUse;
  }

  public void setInUse(String inUse) {
    this.inUse = inUse;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFormName() {
    return this.formName;
  }

  public void setFormName(String formName) {
    this.formName = formName;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
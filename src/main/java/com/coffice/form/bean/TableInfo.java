package com.coffice.form.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableInfo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private FormInfo form = new FormInfo();
  private List<FieldInfo> fields = new ArrayList();

  public List<FieldInfo> getFields()
  {
    return this.fields;
  }

  public void setFields(List<FieldInfo> fields) {
    this.fields = fields;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public FormInfo getForm() {
    return this.form;
  }

  public void setForm(FormInfo form) {
    this.form = form;
  }
}
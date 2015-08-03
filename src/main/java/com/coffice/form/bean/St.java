package com.coffice.form.bean;

import java.io.Serializable;

public class St
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String desc;

  public String getId()
  {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDesc() {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
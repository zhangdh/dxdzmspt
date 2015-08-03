package com.coffice.form.bean;

import java.io.Serializable;

public class Ss
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int id;
  private String sname;
  private String svalue;
  private St st = new St();

  public int getId()
  {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSname() {
    return this.sname;
  }

  public void setSname(String sname) {
    this.sname = sname;
  }

  public String getSvalue() {
    return this.svalue;
  }

  public void setSvalue(String svalue) {
    this.svalue = svalue;
  }

  public St getSt() {
    return this.st;
  }

  public void setSt(St st) {
    this.st = st;
  }
}
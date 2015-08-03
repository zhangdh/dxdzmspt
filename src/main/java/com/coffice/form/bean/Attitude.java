package com.coffice.form.bean;

import java.io.Serializable;
import java.util.Date;

public class Attitude
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String tableId;
  private String formId;
  private String content;
  private String loginCode;
  private String insertDate;
  private String columnId;
  private String temp;
  private Date insertDateTime;

  public String getColumnId()
  {
    return this.columnId;
  }

  public void setColumnId(String columnId) {
    this.columnId = columnId;
  }

  public String getInsertDate() {
    return this.insertDate;
  }

  public void setInsertDate(String insertDate) {
    this.insertDate = insertDate;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTableId() {
    return this.tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public String getFormId() {
    return this.formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getLoginCode() {
    return this.loginCode;
  }

  public void setLoginCode(String loginCode) {
    this.loginCode = loginCode;
  }

  public String getTemp() {
    return this.temp;
  }

  public void setTemp(String temp) {
    this.temp = temp;
  }

  public Date getInsertDateTime() {
    return this.insertDateTime;
  }

  public void setInsertDateTime(Date insertDateTime) {
    this.insertDateTime = insertDateTime;
  }
}
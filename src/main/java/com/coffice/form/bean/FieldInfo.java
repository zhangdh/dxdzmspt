package com.coffice.form.bean;

import java.io.Serializable;

public class FieldInfo
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String fieldName;
  private String fieldType;
  private int fieldLength;
  private String fieldConstrain;
  private String componentType;
  private String sequenceTypeId = null;
  private String fieldValue = "";
  private String insertType = "1";
  private String tdId;
  private String remark = "";
  private String lb;
  private String xh;

  public String getXh()
  {
    return this.xh;
  }

  public void setXh(String xh) {
    this.xh = xh;
  }

  public String getLb() {
    return this.lb;
  }

  public void setLb(String lb) {
    this.lb = lb;
  }

  public String getRemark() {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getTdId() {
    return this.tdId;
  }

  public void setTdId(String tdId) {
    this.tdId = tdId;
  }

  public String getInsertType() {
    return this.insertType;
  }

  public void setInsertType(String insertType) {
    this.insertType = insertType;
  }

  public String getFieldValue() {
    return this.fieldValue;
  }

  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getSequenceTypeId() {
    return this.sequenceTypeId;
  }

  public void setSequenceTypeId(String sequenceTypeId) {
    this.sequenceTypeId = sequenceTypeId;
  }

  public String getComponentType() {
    return this.componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public String getFieldName() {
    return this.fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldType() {
    return this.fieldType;
  }

  public void setFieldType(String fieldType) {
    this.fieldType = fieldType;
  }

  public int getFieldLength() {
    return this.fieldLength;
  }

  public void setFieldLength(int fieldLength) {
    this.fieldLength = fieldLength;
  }

  public String getFieldConstrain() {
    return this.fieldConstrain;
  }

  public void setFieldConstrain(String fieldConstrain) {
    this.fieldConstrain = fieldConstrain;
  }
}
package com.coffice.form.bean;

public class ValidateInfo
{
  private String componentName;
  private String dataType;
  private String remark;
  private int validateType;
  public static final int VALIDATE_NULL = 0;
  public static final int VALIDATE_NOT_NULL = 1;

  public String getRemark()
  {
    return this.remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getComponentName() {
    return this.componentName;
  }

  public void setComponentName(String componentName) {
    this.componentName = componentName;
  }

  public String getDataType() {
    return this.dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public int getValidateType() {
    return this.validateType;
  }

  public void setValidateType(int validateType) {
    this.validateType = validateType;
  }
}
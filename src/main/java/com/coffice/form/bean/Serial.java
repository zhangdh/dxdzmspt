package com.coffice.form.bean;

public class Serial
{
  private String id;
  private String Serial;
  private SerialType serialType = new SerialType();

  public SerialType getSerialType()
  {
    return this.serialType;
  }

  public void setSerialType(SerialType serialType) {
    this.serialType = serialType;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSerial() {
    return this.Serial;
  }

  public void setSerial(String serial) {
    this.Serial = serial;
  }
}
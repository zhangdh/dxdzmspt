package com.coffice.form.dao;

import java.util.List;

import com.coffice.form.bean.Serial;
import com.coffice.form.bean.SerialType;

public abstract interface ISerialDao
{
  public abstract void insertSerialType(SerialType paramSerialType);

  public abstract void insertSerial(Serial paramSerial);

  public abstract List<SerialType> getAllSerialTypes();

  public abstract Serial getSerialById(String paramString);

  public abstract int getSerialCount(String paramString1, String paramString2);

  public abstract SerialType getTypeById(String paramString);

  public abstract void updateSerialType(SerialType paramSerialType);
}
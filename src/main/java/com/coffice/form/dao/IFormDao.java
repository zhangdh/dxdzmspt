package com.coffice.form.dao;

import java.sql.SQLException;
import java.util.List;

import com.coffice.form.bean.FormInfo;
import com.coffice.form.bean.TableInfo;

public abstract interface IFormDao
{
  public abstract String createFormInfo(FormInfo paramFormInfo);

  public abstract void createFormTable(TableInfo paramTableInfo);

  public abstract void alterFormTable(TableInfo paramTableInfo)
    throws SQLException;

  public abstract void deleteFormTable(String paramString);

  public abstract List<FormInfo> getAllForms();

  public abstract FormInfo getFormById(String paramString);

  public abstract void insertComponentType(TableInfo paramTableInfo)
    throws SQLException;

  public abstract void insertTd(TableInfo paramTableInfo)
    throws SQLException;

  public abstract void dropForm(String paramString);
}
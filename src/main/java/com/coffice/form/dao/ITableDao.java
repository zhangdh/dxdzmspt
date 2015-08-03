package com.coffice.form.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.coffice.form.bean.Attitude;
import com.coffice.form.bean.FieldInfo;

public abstract interface ITableDao
{
  public abstract void insert(String paramString, List<FieldInfo> paramList);

  public abstract void insertHistory(String paramString1, List<FieldInfo> paramList, String paramString2, boolean paramBoolean)
    throws SQLException;

  public abstract void createPrintInfo(String paramString1, String paramString2, String paramString3)
    throws SQLException;

  public abstract void delete(String paramString, int paramInt)
    throws SQLException;

  public abstract void update(String paramString, List<FieldInfo> paramList, int paramInt);

  public abstract void insertAttitude(Attitude paramAttitude);

  public abstract List<Attitude> getAttitudeList(String paramString1, String paramString2)
    throws SQLException;

  public abstract List<String> getComponentByTd(String paramString);

  public abstract List<String> getComponentByTableName(String paramString);

  public abstract List<FieldInfo> select(String paramString, int paramInt, List<FieldInfo> paramList);

  public abstract List<FieldInfo> getColumnNames(String paramString);

  public abstract List<FieldInfo> getColumnDataTypes(String paramString);

  public abstract Map<String, FieldInfo> getColumns(String paramString, String[] paramArrayOfString)
    throws SQLException;

  public abstract List<String> getPrintTemplateFileNames(String paramString)
    throws SQLException;

  public abstract int getLastInsertId()
    throws SQLException;

  public abstract int getIdByTableName(String paramString)
    throws SQLException;

  public abstract int getAttitudeId(String paramString1, String paramString2, String paramString3)
    throws SQLException;

  public abstract String getColumnId(String paramString1, String paramString2)
    throws SQLException;
}
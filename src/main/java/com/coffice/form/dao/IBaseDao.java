package com.coffice.form.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract interface IBaseDao
{
  public abstract Connection getConnection();

  public abstract ResultSet getResultSet();

  public abstract Statement getStatement();
}
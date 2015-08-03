package com.coffice.form.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

import com.coffice.form.dao.IBaseDao;
import com.coffice.util.Db;
import com.coffice.util.SysPara;

public class DBUtil
{
  private static final String url = "jdbc:mysql://127.0.0.1:3306/form";
  private static final String userName = "root";
  private static final String password = "123456";

  public static Connection getConnection()
  {
    Connection conn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/form", "root", "123456");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  public static void closeConnection(ResultSet rs, Statement stm, Connection conn)
  {
    try
    {
      if (rs != null)
        rs.close();

      if (stm != null)
        stm.close();

      if (conn == null) return;
      conn.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void closeConnection(IBaseDao dao)
  {
    try
    {
      if (dao.getResultSet() != null)
        dao.getResultSet().close();

      if (dao.getStatement() != null)
        dao.getStatement().close();

      if (dao.getConnection() == null) return;
      dao.getConnection().close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void beginTransaction(Connection conn) throws SQLException {
    conn.setAutoCommit(false);
  }

  public static void commitTransaction(Connection conn) throws SQLException {
    conn.commit();
  }

  public static void rollBackTransation(Connection conn) throws SQLException {
    conn.rollback(); }

  public static String getDateFun() {
    String dateStr = ""; String dbtype = "";
    try {
      dbtype = SysPara.getValue("db_type");
    } catch (Exception e) {
      dbtype = "mysql";
    }
    if ("mysql".equals(dbtype))
      dateStr = "now()";
    else if ("sqlserver".equals(dbtype))
      dateStr = "getdate()";
    else if ("oracle".equals(dbtype))
      dateStr = "sysdate";

    return dateStr; }

  public static boolean isExistsTable(String tablename) {
    String sql = "";
    if (SysPara.compareValue("db_type", "oracle"))
      sql = "select table_name  from all_tables where table_name ='" + tablename.toUpperCase() + "'";
    else if (SysPara.compareValue("db_type", "sqlserver"))
      sql = "select name from sysobjects where name='" + tablename + "' and type='U'";
    else if (SysPara.compareValue("db_type", "mysql"))
      sql = "select table_name from information_schema.tables where table_name = '" + tablename + "'";
    else
      return false;

    List list = Db.getJtN().queryForList(sql);

    return (list.size() > 0);
  }
}
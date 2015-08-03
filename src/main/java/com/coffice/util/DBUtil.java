// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DBUtil.java

package com.coffice.util;

import java.sql.*;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;


public class DBUtil
{

	private static final String url = "jdbc:mysql://127.0.0.1:3306/form";
	private static final String userName = "root";
	private static final String password = "123456";

	public DBUtil()
	{
	}

	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/form", "root", "123456");
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
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
			if (conn != null)
				conn.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void beginTransaction(Connection conn)
		throws SQLException
	{
		conn.setAutoCommit(false);
	}

	public static void commitTransaction(Connection conn)
		throws SQLException
	{
		conn.commit();
	}

	public static void rollBackTransation(Connection conn)
		throws SQLException
	{
		conn.rollback();
	}

	public static String getDateFun()
	{
		String dateStr = "";
		String dbtype = "";
		try
		{
			dbtype = SysPara.getValue("db_type");
		}
		catch (Exception e)
		{
			dbtype = "mysql";
		}
		if ("mysql".equals(dbtype))
			dateStr = "now()";
		else
		if ("sqlserver".equals(dbtype))
			dateStr = "getdate()";
		else
		if ("oracle".equals(dbtype))
			dateStr = "sysdate";
		return dateStr;
	}

	public static boolean isExistsTable(String tablename)
	{
		String sql = "";
		if (SysPara.compareValue("db_type", "oracle"))
			sql = (new StringBuilder("select table_name  from all_tables where table_name ='")).append(tablename.toUpperCase()).append("'").toString();
		else
		if (SysPara.compareValue("db_type", "sqlserver"))
			sql = (new StringBuilder("select name from sysobjects where name='")).append(tablename).append("' and type='U'").toString();
		else
		if (SysPara.compareValue("db_type", "mysql"))
			sql = (new StringBuilder("select table_name from information_schema.tables where table_name = '")).append(tablename).append("'").toString();
		else
			return false;
		List list = Db.getJtN().queryForList(sql);
		return list.size() > 0;
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   DbWeb.java

package com.coffice.util;

import java.util.*;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.coffice.bean.PageBean;


// Referenced classes of package com.ashburz.util:
//			Guid, SysPara, Str, LogItem, 
//			Log, Db

public class DbWeb
{

	private static DataSource dsWeb;
	private static DataSource dsCownewWeb;
	private static MySQLMaxValueIncrementer mysqlInc;
	private static SqlServerMaxValueIncrementer sqlserverInc;
	private static OracleSequenceMaxValueIncrementer oracleInc;

	public DbWeb()
	{
	}

	public static String getGUID()
	{
		return Guid.get();
	}

	public static int getNextId()
		throws Exception
	{
		if (SysPara.getValue("db_type").equals("mysql"))
		{
			if (mysqlInc == null)
			{
				mysqlInc = new MySQLMaxValueIncrementer(dsWeb, "t_sys_sequence", "id");
				mysqlInc.setCacheSize(10);
			}
			return mysqlInc.nextIntValue();
		}
		if (SysPara.getValue("db_type").equals("sqlserver"))
		{
			if (sqlserverInc == null)
			{
				sqlserverInc = new SqlServerMaxValueIncrementer(dsWeb, "t_sys_sequence", "id");
				sqlserverInc.setCacheSize(10);
			}
			return sqlserverInc.nextIntValue();
		}
		if (SysPara.getValue("db_type").equals("oracle"))
		{
			if (oracleInc == null)
				oracleInc = new OracleSequenceMaxValueIncrementer(dsWeb, "t_sys_sequence");
			return oracleInc.nextIntValue();
		} else
		{
			throw new Exception("t_sys_para.csdm>db_type不匹配");
		}
	}

	public static int getBuzId()
	{
		return 0;
	}

	public static JdbcTemplate getJtN()
	{
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(dsWeb);
		return jt;
	}

	public static JdbcTemplate getJtA()
	{
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(dsCownewWeb);
		return jt;
	}

	public static NamedParameterJdbcTemplate getNpjtA()
	{
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(dsCownewWeb);
		return jt;
	}

	public static NamedParameterJdbcTemplate getNpjtN()
	{
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(dsWeb);
		return jt;
	}

	public static SimpleJdbcInsert getSji()
	{
		SimpleJdbcInsert sji = new SimpleJdbcInsert(dsWeb);
		return sji;
	}

	public static List getPageData(PageBean page)
		throws Exception
	{
		String sql = "";
		List list;
		sql = page.getSql();
		int pageSize = 0;
		pageSize = (new Integer(page.getPageSize())).intValue();
		String sqlCount = "";
		if (Str.filterNull(page.getCountSql()).equals(""))
			sqlCount = sql.replaceAll("SELECT.+FROM", "SELECT COUNT(*) FROM");
		else
			sqlCount = page.getCountSql();
		NamedParameterJdbcTemplate jt = getNpjtN();
		int page_allCount;
		try
		{
			page_allCount = jt.queryForInt(sqlCount, page.getNamedParameters());
		}
		catch (Exception e)
		{
			page_allCount = 0;
		}
		String curPage = page.getPageGoto();
		if (curPage == null || curPage.equals(""))
			curPage = "1";
		int cp;
		try
		{
			cp = (new Integer(curPage)).intValue();
		}
		catch (NumberFormatException e)
		{
			cp = 1;
		}
		int page_allPage = (double)page_allCount / (double)pageSize <= (double)(page_allCount / pageSize) ? page_allCount / pageSize : page_allCount / pageSize + 1;
		if (cp > page_allPage)
			cp = page_allPage;
		if (cp < 1)
			cp = 1;
		int begin = (cp - 1) * pageSize;
		page.setPage_allPage(page_allPage);
		page.setPage_cur(cp);
		page.setPage_allCount(page_allCount);
		String dbtype = SysPara.getValue("db_type");
		StringBuffer sb = new StringBuffer();
		if (dbtype.equals("mysql"))
		{
			sb.append(sql);
			sb.append(" limit ");
			sb.append(begin);
			sb.append(",");
			sb.append(pageSize);
		} else
		if (dbtype.equals("oracle"))
		{
			page.getNamedParameters().put("page_begin", Integer.valueOf(begin));
			page.getNamedParameters().put("page_end", Integer.valueOf(begin + pageSize));
			sb.append("select * from ( select row_.*, rownum rownum_ from ( ");
			sb.append(sql);
			sb.append(" ) row_ where rownum <= :page_end) where rownum_ > :page_begin");
		} else
		if (dbtype.equals("hssql"))
		{
			sb.append("select limit ? ? * from (");
			sb.append(sql);
			sb.append(" )");
		} else
		if (dbtype.equals("postgresql"))
		{
			sb.append(sql);
			sb.append(" limit ? offset ?");
		} else
		if (dbtype.indexOf("db2") != -1)
		{
			sb.append("select * from( select rownumber() over() as rownum_ ,a1.* from ( ");
			sb.append(sql);
			sb.append(" ) a1) a2 where a2.rownum_ between ? and ? ");
		}
		if (!page.getOrderBy().equals(""))
			sb.append("order by ").append(page.getOrderBy());
		list = new ArrayList();
		try{
		if (dbtype.equals("sqlserver"))
		{
			SqlRowSet rs = jt.queryForRowSet(sql, page.getNamedParameters());
			SqlRowSetMetaData md = rs.getMetaData();
			String columnNames[] = md.getColumnNames();
			if (rs.next())
			{
				rs.first();
				for (int j = 0; j < begin; j++)
					rs.next();

				for (int i = 0; i < pageSize; i++)
				{
					if (rs.isAfterLast())
						break;
					list.add(rs2Map(rs, columnNames));
					rs.next();
				}

			}
		} else
		{
			list = jt.queryForList(sb.toString(), page.getNamedParameters());
		}
		return list;
		}catch(Exception e){
		String guid = Guid.get();
		LogItem logItem = new LogItem();
		logItem.setClassName(com.coffice.util.DbWeb.class.getName());
		logItem.setMethod("getPageData");
		logItem.setLogid(guid);
		logItem.setLevel("error");
		logItem.setDesc("[取分页数据][异常]");
		logItem.setSql(sql);
		logItem.setContent(e.toString());
		Log.write(logItem);
		throw new Exception((new StringBuilder("getPageData异常:")).append(e.getMessage()).toString());
		}
	}

	public static List getPageData_SqlServer(PageBean page)
		throws Exception
	{
		String sql = "";
		List list;
		sql = page.getSql();
		int pageSize = 0;
		pageSize = (new Integer(page.getPageSize())).intValue();
		String sqlCount = "";
		if (Str.filterNull(page.getCountSql()).equals(""))
			sqlCount = sql.replaceAll("SELECT.+FROM", "SELECT COUNT(*) FROM");
		else
			sqlCount = page.getCountSql();
		NamedParameterJdbcTemplate jt = getNpjtN();
		int page_allCount;
		try
		{
			page_allCount = jt.queryForInt(sqlCount, page.getNamedParameters());
		}
		catch (Exception e)
		{
			page_allCount = 0;
		}
		String curPage = page.getPageGoto();
		if (curPage == null || curPage.equals(""))
			curPage = "1";
		int cp;
		try
		{
			cp = (new Integer(curPage)).intValue();
		}
		catch (NumberFormatException e)
		{
			cp = 1;
		}
	try{
		int page_allPage = (double)page_allCount / (double)pageSize <= (double)(page_allCount / pageSize) ? page_allCount / pageSize : page_allCount / pageSize + 1;
		if (cp > page_allPage)
			cp = page_allPage;
		if (cp < 1)
			cp = 1;
		int begin = (cp - 1) * pageSize;
		page.setPage_allPage(page_allPage);
		page.setPage_cur(cp);
		page.setPage_allCount(page_allCount);
		sql = sql.replaceAll("@pageSize", (new StringBuilder()).append(begin).append(pageSize).toString());
		sql = sql.replaceAll("@page_begin", (new StringBuilder()).append(begin).toString());
		if (!page.getOrderBy().equals(""))
			sql = (new StringBuilder(String.valueOf(sql))).append(" order by ").append(page.getOrderBy()).toString();
		list = jt.queryForList(sql, page.getNamedParameters());
		return list;
	}catch(Exception e){
		String guid = Guid.get();
		LogItem logItem = new LogItem();
		logItem.setClassName("DbWeb");
		logItem.setMethod("getPageData");
		logItem.setLogid(guid);
		logItem.setLevel("error");
		logItem.setDesc("[取分页数据][异常]");
		logItem.setSql(sql);
		logItem.setContent(e.toString());
		Log.write(logItem);
		throw new Exception((new StringBuilder("getPageData异常:")).append(e.getMessage()).toString());
	}
	}

	public static List getPageDataA(PageBean page)
		throws Exception
	{
		String sql = "";
		List list;
		sql = page.getSql();
		int pageSize = 0;
		pageSize = (new Integer(page.getPageSize())).intValue();
		String sqlCount = "";
		if (Str.filterNull(page.getCountSql()).equals(""))
			sqlCount = sql.replaceAll("SELECT.+FROM", "SELECT COUNT(*) FROM");
		else
			sqlCount = page.getCountSql();
		NamedParameterJdbcTemplate jt = getNpjtA();
		int page_allCount;
		try
		{
			page_allCount = jt.queryForInt(sqlCount, page.getNamedParameters());
		}
		catch (Exception e)
		{
			page_allCount = 0;
		}
		String curPage = page.getPageGoto();
		if (curPage == null || curPage.equals(""))
			curPage = "1";
		int cp;
		try
		{
			cp = (new Integer(curPage)).intValue();
		}
		catch (NumberFormatException e)
		{
			cp = 1;
		}
		int page_allPage = (double)page_allCount / (double)pageSize <= (double)(page_allCount / pageSize) ? page_allCount / pageSize : page_allCount / pageSize + 1;
		if (cp > page_allPage)
			cp = page_allPage;
		if (cp < 1)
			cp = 1;
		int begin = (cp - 1) * pageSize;
		page.setPage_allPage(page_allPage);
		page.setPage_cur(cp);
		page.setPage_allCount(page_allCount);
		String dbtype = SysPara.getValue("db_type");
		StringBuffer sb = new StringBuffer();
		if (dbtype.equals("mysql"))
		{
			sb.append(sql);
			sb.append(" limit ");
			sb.append(begin);
			sb.append(",");
			sb.append(pageSize);
		} else
		if (dbtype.equals("oracle"))
		{
			page.getNamedParameters().put("page_begin", Integer.valueOf(begin));
			page.getNamedParameters().put("page_end", Integer.valueOf(begin + pageSize));
			sb.append("select * from ( select row_.*, rownum rownum_ from ( ");
			sb.append(sql);
			sb.append(" ) row_ where rownum <= :page_end) where rownum_ > :page_begin");
		} else
		if (dbtype.equals("hssql"))
		{
			sb.append("select limit ? ? * from (");
			sb.append(sql);
			sb.append(" )");
		} else
		if (dbtype.equals("postgresql"))
		{
			sb.append(sql);
			sb.append(" limit ? offset ?");
		} else
		if (dbtype.indexOf("db2") != -1)
		{
			sb.append("select * from( select rownumber() over() as rownum_ ,a1.* from ( ");
			sb.append(sql);
			sb.append(" ) a1) a2 where a2.rownum_ between ? and ? ");
		}
		if (!page.getOrderBy().equals(""))
			sb.append("order by ").append(page.getOrderBy());
		list = new ArrayList();
		try{
		if (dbtype.equals("sqlserver"))
		{
			SqlRowSet rs = jt.queryForRowSet(sql, page.getNamedParameters());
			SqlRowSetMetaData md = rs.getMetaData();
			String columnNames[] = md.getColumnNames();
			rs.absolute(begin);
			for (int i = 0; i < pageSize; i++)
			{
				rs.next();
				if (rs.isAfterLast())
					break;
				list.add(rs2Map(rs, columnNames));
			}

		} else
		{
			list = jt.queryForList(sb.toString(), page.getNamedParameters());
		}
		return list;
		}catch(Exception e){
		
		String guid = Guid.get();
		LogItem logItem = new LogItem();
		logItem.setClassName(com.coffice.util.DbWeb.class.getName());
		logItem.setMethod("getPageData");
		logItem.setLogid(guid);
		logItem.setLevel("error");
		logItem.setDesc("[取分页数据][异常]");
		logItem.setSql(sql);
		logItem.setContent(e.toString());
		Log.write(logItem);
		throw new Exception((new StringBuilder("getPageData异常:")).append(e.getMessage()).toString());
		}
	}

	private static Map rs2Map(SqlRowSet rs, String columnNames[])
	{
		Map map = new HashMap();
		for (int i = 0; i < columnNames.length; i++)
			map.put(columnNames[i], rs.getString(columnNames[i]));

		return map;
	}

	public static DataSource getDsWeb()
	{
		return dsWeb;
	}

	public void setDsWeb(DataSource dsWeb)
	{
		dsWeb = dsWeb;
	}

	public void setDsCownewWeb(DataSource dsCownewWeb)
	{
		dsCownewWeb = dsCownewWeb;
	}
}

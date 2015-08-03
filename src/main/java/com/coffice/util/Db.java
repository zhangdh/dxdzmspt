

package com.coffice.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javacommon.xsqlbuilder.XsqlBuilder;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.SqlServerMaxValueIncrementer;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.coffice.bean.PageBean;
import com.coffice.util.cache.Cache;




public class Db
{

	private static DataSource ds;
	private static DataSource dsCownew;
	private static MySQLMaxValueIncrementer mysqlInc;
	private static SqlServerMaxValueIncrementer sqlserverInc;
	private static OracleSequenceMaxValueIncrementer oracleInc;

	public Db()
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
				mysqlInc = new MySQLMaxValueIncrementer(ds, "t_sys_sequence", "id");
				mysqlInc.setCacheSize(10);
			}
			return mysqlInc.nextIntValue();
		}
		if (SysPara.getValue("db_type").equals("sqlserver"))
		{
			if (sqlserverInc == null)
			{
				sqlserverInc = new SqlServerMaxValueIncrementer(ds, "t_sys_sequence", "id");
				sqlserverInc.setCacheSize(10);
			}
			return sqlserverInc.nextIntValue();
		}
		if (SysPara.getValue("db_type").equals("oracle"))
		{
			if (oracleInc == null)
				oracleInc = new OracleSequenceMaxValueIncrementer(ds, "sys_sequence");
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
		jt.setDataSource(ds);
		return jt;
	}

	public static JdbcTemplate getJtA()
	{
		JdbcTemplate jt = new JdbcTemplate();
		jt.setDataSource(dsCownew);
		return jt;
	}

	public static NamedParameterJdbcTemplate getNpjtA()
	{
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(dsCownew);
		return jt;
	}

	public static NamedParameterJdbcTemplate getNpjtN()
	{
		NamedParameterJdbcTemplate jt = new NamedParameterJdbcTemplate(ds);
		return jt;
	}

	public static SimpleJdbcInsert getSji()
	{
		SimpleJdbcInsert sji = new SimpleJdbcInsert(ds);
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
		if (cp < 1){
			cp = 1;
		}
		 
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
			sb.append(" ) row_ where rownum <=:page_end) where rownum_ > :page_begin");
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
		} else
		if (dbtype.equals("sqlserver"))
		{
			page.getNamedParameters().put("page_begin", Integer.valueOf(begin));
			sql = sql.toLowerCase();
			if(sql.indexOf("union")>-1){
				sql = "select sb.* from ("+sql+") sb";
			}
			sql = sql.replaceFirst("select", "").trim();
			if (sql.indexOf("distinct") == 0)
			{
				sql = sql.replaceFirst("distinct", "");
				sql = (new StringBuilder("select distinct top ")).append(begin + pageSize).append(" 0 as TempColmun, ").append(sql).toString();
			} else
			{
				sql = (new StringBuilder("select top ")).append(begin + pageSize).append(" 0 as TempColmun, ").append(sql).toString();
			}
			sb.append("select * from (");
			sb.append("select row_number()over(order by TempColmun) as rownum_, temp.* from (");
			sb.append(sql);
			sb.append(") temp)t where rownum_ >:page_begin");
		}
	try{
		if (!page.getOrderBy().equals(""))
			sb.append(" order by ").append(page.getOrderBy());
		list = new ArrayList();
		list = jt.queryForList(sb.toString(), page.getNamedParameters());
		return list;
	}catch(Exception e){
		String guid = Guid.get();
		LogItem logItem = new LogItem();
		logItem.setClassName(com.coffice.util.Db.class.getName());
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
		int page_allPage = (double)page_allCount / (double)pageSize <= (double)(page_allCount / pageSize) ? page_allCount / pageSize : page_allCount / pageSize + 1;
		if (cp > page_allPage)
			cp = page_allPage;
		if (cp < 1)
			cp = 1;
	try{
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
		logItem.setClassName(com.coffice.util.Db.class.getName());
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
		try{
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
		logItem.setClassName(com.coffice.util.Db.class.getName());
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

	public static int simpleUpdate(String tablename, String sqlWhere, Map map)
		throws Exception
	{
		ResultSet rs = null;
		Map metaData = null;
		int rows;
		if (Cache.getGlobalInfo("metaData", tablename) == null)
		{
			metaData = new HashMap();
			for ( rs = ds.getConnection().getMetaData().getColumns(null, null, tablename.toUpperCase(), null); rs.next(); metaData.put(rs.getString("COLUMN_NAME").toLowerCase(), Integer.valueOf(rs.getInt("DATA_TYPE"))));
			Cache.setGlobalInfo("metaData", tablename, metaData);
		} else
		{
			metaData = (Map)Cache.getGlobalInfo("metaData", tablename);
		}
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("update ").append(tablename).append(" set ");
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();)
		{
			Object obj = iterator.next();
			if (metaData.containsKey(obj))
			{
				int columnType = ((Integer)metaData.get(obj)).intValue();
				if (columnType == 93)
				{
					if ("".equals(String.valueOf(map.get(obj))))
						map.put(obj, null);
					else
					if (String.valueOf(map.get(obj)).length() == 10)
						map.put(obj, Timestamp.valueOf((new StringBuilder(String.valueOf(String.valueOf(map.get(obj))))).append(" 00:00:00").toString()));
					else
						map.put(obj, Timestamp.valueOf(String.valueOf(map.get(obj))));
				} else
				if (columnType == 91)
				{
					if ("".equals(String.valueOf(map.get(obj))))
						map.put(obj, null);
					else
						map.put(obj, Date.valueOf(String.valueOf(map.get(obj))));
				} else
				if ((columnType == 4 || columnType == 3 || columnType == 6 || columnType == 8 || columnType == 7) && "".equals(String.valueOf(map.get(obj))))
					map.put(obj, null);
				sqlBuffer.append(obj).append("=:").append(obj).append(",");
			}
		}
		try{
			sqlBuffer = sqlBuffer.replace(sqlBuffer.length() - 1, sqlBuffer.length(), "");
			sqlBuffer.append(" where ").append(sqlWhere);
			rows = getNpjtN().update(sqlBuffer.toString(), map);
			return rows;
		}catch(Exception e){
		String guid = Guid.get();
		LogItem logItem = new LogItem();
		logItem.setClassName(com.coffice.util.Db.class.getName());
		logItem.setMethod("simpleUpdate");
		logItem.setLogid(guid);
		logItem.setLevel("error");
		logItem.setDesc("更新数据库异常");
		logItem.setContent(e.toString());
		Log.write(logItem);
		throw new Exception((new StringBuilder("simpleUpdate异常:")).append(e.getMessage()).toString());
		}
	}

	public static String getFilterSql(String sql, Map map)
	{
		sql = sql.toLowerCase();
		if (sql.indexOf("and") > 0)
		{
			sql = sql.replace("and", "~//~ and");
			sql = sql.replace("where", "where /~");
		} else
		{
			sql = sql.replace("where", "where /~");
		}
		if (sql.indexOf("order") > 0)
			sql = sql.replace(" order", "~/ order");
		else
			sql = (new StringBuilder(String.valueOf(sql))).append("~/").toString();
		Pattern p = Pattern.compile("/~.+?~/");
		Matcher m = null;
		for (m = p.matcher(sql); m.find();)
		{
			String temp = m.group();
			if (temp.indexOf("[") < 0 && temp.indexOf("{") < 0)
				sql = sql.replace(temp, temp.replace("/~", "").replace("~/", ""));
		}

		javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult result = (new XsqlBuilder()).generateHql(sql, map);
		sql = result.getXsql();
		if (sql.matches(".*where(\\s*order.+||\\s*)"))
			sql = sql.replace("where", "");
		else
		if (sql.matches(".*where\\s+and.+"))
			sql = sql.replaceFirst(" and", "");
		return sql;
	}

	private static Map rs2Map(SqlRowSet rs, String columnNames[])
	{
		Map map = new HashMap();
		for (int i = 0; i < columnNames.length; i++)
			map.put(columnNames[i], rs.getString(columnNames[i]));

		return map;
	}

	public static DataSource getDs()
	{
		return ds;
	}
 
	public void setDs(DataSource ds)
	{
		this.ds = ds;
	}
    
	public void setDsCownew(DataSource dsCownew)
	{
		this.dsCownew = dsCownew;
	}
	public static String getDateStr(){
		   try{
			   if(SysPara.getValue("dbType").equals("mysql")){
				   return "now()";
			   }else if(SysPara.getValue("dbType").equals("sqlsever")){
				   return "getDate()";
			   }else if(SysPara.getValue("dbType").equals("oracle")){
				   return "sysdate";
			   }else {
				   return "now()";
			   }
		   }catch(Exception e){
			    
			   e.printStackTrace();
			   return "now()";
		   }	
	   }	
	   public static String getStr(){
		   String str = "";
		   try{
			   str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return str;
	   }
}

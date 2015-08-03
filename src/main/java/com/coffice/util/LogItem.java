
package com.coffice.util;


public class LogItem
{

	private String yhid;
	private String logid;
	private String level;
	private String className;
	private String method;
	private String desc;
	private String sql;
	private String sqlData;
	private String content;

	public LogItem()
	{
		yhid = "";
		logid = "";
		level = "";
		className = "";
		method = "";
		desc = "";
		sql = "";
		sqlData = "";
		content = "";
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getLogid()
	{
		return logid;
	}

	public void setLogid(String logid)
	{
		this.logid = logid;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getSql()
	{
		return sql;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	public String getSqlData()
	{
		return sqlData;
	}

	public void setSqlData(String sqlData)
	{
		this.sqlData = sqlData;
	}

	public String getYhid()
	{
		return yhid;
	}

	public void setYhid(String yhid)
	{
		this.yhid = yhid;
	}

	public String getLevel()
	{
		return level;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}

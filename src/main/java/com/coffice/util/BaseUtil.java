// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BaseUtil.java

package com.coffice.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

// Referenced classes of package com.ashburz.util:
//			Db

public class BaseUtil
{

	protected static final Logger log = LoggerFactory.getLogger("coffice.log");
	protected static final Logger oplog = LoggerFactory.getLogger("coffice.oplog");

	public BaseUtil()
	{
	}

	protected JdbcTemplate getJtA()
	{
		return Db.getJtA();
	}

	protected JdbcTemplate getJtN()
	{
		return Db.getJtN();
	}

	protected NamedParameterJdbcTemplate getNpjtN()
	{
		return Db.getNpjtN();
	}

	protected NamedParameterJdbcTemplate getNpjtA()
	{
		return Db.getNpjtA();
	}

	protected SimpleJdbcInsert getSji()
	{
		return Db.getSji();
	}

	protected String getGUID()
	{
		return Db.getGUID();
	}

	protected int getNextId()
		throws Exception
	{
		return Db.getNextId();
	}
	public static String getDateStr(){
		String db_type = "";
		try{
			db_type = SysPara.getDbType();
		}catch(Exception e){
			log.error("根据数据库类型获取时间串异常");
			return "CONVERT(varchar(100), GETDATE(), 20)";
		}
		if("oracle".equals(db_type)){
			return "sysdate";
		}else if("mysql".equals(db_type)){
			return "now()";
		}else{
			return "CONVERT(varchar(100), GETDATE(), 20)";
		}
	}
	public static String DateConvertStr(){
		 java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String str = format.format(new Date());
	     return str;
		
	}
}

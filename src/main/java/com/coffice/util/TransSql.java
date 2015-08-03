// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TransSql.java

package com.coffice.util;

import com.cownew.cownewsql.imsql.ISQLTranslator;
import com.cownew.cownewsql.imsql.common.DialectManager;

// Referenced classes of package com.ashburz.util:
//			SysPara

public class TransSql
{

	public TransSql()
	{
	}

	public static String trans(String sql)
		throws Exception
	{
		String dbtype = SysPara.getValue("db_type");
		String tsql[] = (String[])null;
		try
		{
			if (dbtype.equals("mysql"))
			{
				ISQLTranslator txMysql = DialectManager.createTranslator("mysql");
				tsql = txMysql.translateSQL(sql);
			} else
			if (dbtype.equals("sqlserver"))
			{
				ISQLTranslator txMssql = DialectManager.createTranslator("mssqlserver");
				tsql = txMssql.translateSQL(sql);
			} else
			if (dbtype.equals("oracle"))
			{
				ISQLTranslator txOracle = DialectManager.createTranslator("oracle");
				tsql = txOracle.translateSQL(sql);
			}
		}
		catch (Exception e)
		{
			throw new Exception((new StringBuilder("����sql�����쳣��")).append(e.getMessage()).toString());
		}
		return tsql[0];
	}
}


package com.coffice.util;

import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;

// Referenced classes of package com.ashburz.util:
//			Db

public class SysPara
{

	static Map cacheMap = new HashMap();

	public SysPara()
	{
	}

	public void clear()
	{
		cacheMap.clear();
	}

	public static String getValue(String csdm)
		throws Exception
	{
		if (cacheMap.isEmpty())
			getFromDb();
		if (cacheMap.containsKey(csdm))
			return (String)cacheMap.get(csdm);
		else
			throw new Exception((new StringBuilder("系统参数：没有查找到")).append(csdm).append("对应的值").toString());
	}

	public static boolean compareValue(String csdm, String cvalue)
	{
		try{
			return cvalue != null && cvalue.equals(getValue(csdm));
		}catch(Exception e){
			return false;
		}
		
	}

	public static boolean compareValue(String csdm, String cvalue, String dvalue)
	{
		String value = "";
		try
		{
			value = getValue(csdm);
		}
		catch (Exception e)
		{
			value = dvalue;
		}
		return cvalue != null && cvalue.equals(value);
	}

	public static void updateSysPara(String key, String value)
	{
		cacheMap.remove(key);
		cacheMap.put(key, value);
	}

	public static String getDbType()
		throws Exception
	{
		return getValue("db_type").toLowerCase();
	}

	private static void getFromDb()
		throws Exception
	{
		try
		{
			List list = Db.getJtN().queryForList("select csdm,csz from t_sys_para where yxbz=1");
			if (list.size() == 0)
				throw new Exception("ϵͳ系统参数信息没有配置");
			Map map;
			for (Iterator iter = list.iterator(); iter.hasNext(); cacheMap.put(map.get("csdm"), map.get("csz")))
				map = (Map)(Map)iter.next();

		}
		catch (Exception e)
		{
			throw new Exception((new StringBuilder("获取t_sys_para表数据时出现异常:")).append(e.getMessage()).toString());
		}
	}

}

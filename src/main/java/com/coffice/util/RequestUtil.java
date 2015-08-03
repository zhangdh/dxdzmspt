// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   RequestUtil.java

package com.coffice.util;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.coffice.util.cache.Cache;


public class RequestUtil
{

	public RequestUtil()
	{
	}

	public static Map getMap(HttpServletRequest req)
	{
		Map mapReq = new HashMap();
		String paramName;
		String value;
		for (Enumeration enu = req.getParameterNames(); enu.hasMoreElements(); mapReq.put(paramName, value))
		{
			paramName = (String)enu.nextElement();
			String values[] = req.getParameterValues(paramName);
			value = "";
			for (int i = 0; i < values.length; i++){
				value = (new StringBuilder(String.valueOf(value))).append(value.equals("") ? "" : "~").append(values[i]).toString();
			}
			mapReq.put(paramName, value);
		}

		String yhid = (String)req.getAttribute("yhid");
		mapReq.put("yhid", req.getAttribute("yhid"));
		if (yhid != null)
			if (yhid.equals("admin") || yhid.equals("sys"))
			{
				mapReq.put("bmid", "0");
				mapReq.put("gwid", "0");
				mapReq.put("zzid", "0");
			} else
			{
				mapReq.put("zzid", Cache.getUserInfo((String)req.getAttribute("yhid"), "zzid"));
			}
		return mapReq;
	}
}

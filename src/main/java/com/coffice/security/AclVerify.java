// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   AclVerify.java

package com.coffice.security;

import java.util.*;

import com.coffice.exception.MethodNotPermitException;
import com.coffice.exception.UrlNotPermitException;
import com.coffice.util.cache.Cache;


public class AclVerify
{

	public AclVerify()
	{
	}

	public void verify(String yhid, String uri, String method)
		throws UrlNotPermitException, MethodNotPermitException
	{
		boolean returnBool = false;
		if (uri.indexOf(".jsp") > 0)
		{
			List list = (List)Cache.getUserInfo(yhid, "url");
			for (Iterator iterator = list.iterator(); iterator.hasNext();)
			{
				Map map = (Map)iterator.next();
				if (String.valueOf(map.get("url")).indexOf(uri) == 0)
				{
					returnBool = true;
					break;
				}
			}

			if (!returnBool)
			{
				List url_list = (List)Cache.getGlobalInfo("", "url");
				for (Iterator iterator2 = url_list.iterator(); iterator2.hasNext();)
				{
					Map map = (Map)iterator2.next();
					if (String.valueOf(map.get("url")).indexOf(uri) == 0)
					{
						returnBool = true;
						break;
					}
				}

				if (returnBool)
					throw new UrlNotPermitException();
			}
		} else
		if (uri.indexOf(".do") > 0 && method != null && !method.equals(""))
		{
			method = (new StringBuilder(String.valueOf(uri))).append(method).toString();
			List _list = (List)Cache.getUserInfo(yhid, "method");
			for (Iterator iterator1 = _list.iterator(); iterator1.hasNext();)
			{
				Map map = (Map)iterator1.next();
				if (method.equals((String)map.get("method")))
				{
					returnBool = true;
					break;
				}
			}

			if (!returnBool)
			{
				List all_list = (List)Cache.getGlobalInfo("", "method");
				for (Iterator iterator3 = all_list.iterator(); iterator3.hasNext();)
				{
					Map map = (Map)iterator3.next();
					if (String.valueOf(map.get("method")).equals(method))
					{
						returnBool = true;
						break;
					}
				}

				if (returnBool)
					throw new MethodNotPermitException();
			}
		}
	}
}

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Cache.java

package com.coffice.util.cache;


// Referenced classes of package com.ashburz.util.cache:
//			EhCacheImpl, ICache

public class Cache
{

	private static ICache cache = new EhCacheImpl();

	public Cache()
	{
	}

	public static void setUserInfo(String yhid, String key, Object value)
	{
		cache.setUserInfo(yhid, key, value);
	}

	public static Object getUserInfo(String yhid, String key)
	{
		return cache.getUserInfo(yhid, key);
	}

	public static void setGlobalInfo(String module, String key, Object value)
	{
		cache.setGlobalInfo(module, key, value);
	}

	public static Object getGlobalInfo(String module, String key)
	{
		return cache.getGlobalInfo(module, key);
	}

	public static void removeInfo(String prefix, String key)
	{
		cache.removeInfo(prefix, key);
	}

}

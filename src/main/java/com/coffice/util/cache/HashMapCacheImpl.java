// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   HashMapCacheImpl.java

package com.coffice.util.cache;

import java.util.HashMap;

// Referenced classes of package com.ashburz.util.cache:
//			ICache

public class HashMapCacheImpl
	implements ICache
{

	private static HashMap map = new HashMap();

	public HashMapCacheImpl()
	{
	}

	public Object getGlobalInfo(String module, String key)
	{
		return map.get((new StringBuilder(String.valueOf(module))).append(key).toString());
	}

	public Object getUserInfo(String yhid, String key)
	{
		return map.get((new StringBuilder(String.valueOf(yhid))).append(key).toString());
	}

	public void setGlobalInfo(String module, String key, Object value)
	{
		map.put((new StringBuilder(String.valueOf(module))).append(key).toString(), value);
	}

	public void setUserInfo(String yhid, String key, Object value)
	{
		map.put((new StringBuilder(String.valueOf(yhid))).append(key).toString(), value);
	}

	public void removeInfo(String prefix, String key)
	{
		map.remove((new StringBuilder(String.valueOf(prefix))).append(key).toString());
	}

}

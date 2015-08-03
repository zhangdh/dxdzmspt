// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Convert.java

package com.coffice.util;

import java.util.*;

public class Convert
{

	public Convert()
	{
	}

	public static List dbDm2Mc(List _list, Map _map)
	{
		List list = new ArrayList();
		for (int i = 0; i < _list.size(); i++)
		{
			Map map = (Map)(Map)_list.get(i);
			for (Iterator it = map.entrySet().iterator(); it.hasNext();)
			{
				java.util.Map.Entry m = (java.util.Map.Entry)(java.util.Map.Entry)it.next();
				if (_map.containsKey(m.getKey().toString().toLowerCase()))
					try
					{
						map.put(m.getKey(), ((Map)(Map)_map.get(m.getKey().toString().toLowerCase())).get(String.valueOf(m.getValue()).trim()));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
			}

			list.add(map);
		}

		return list;
	}
}

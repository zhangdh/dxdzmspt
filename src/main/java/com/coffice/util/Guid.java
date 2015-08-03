// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Guid.java

package com.coffice.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Guid
{

	public Guid()
	{
	}

	public static String get()
	{
		StringBuffer now = new StringBuffer((new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(new Date()));
		int n = (int)(Math.random() * 90000D + 10000D);
		return now.append(n).toString();
	}
}

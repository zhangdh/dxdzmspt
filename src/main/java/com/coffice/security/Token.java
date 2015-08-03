// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Token.java

package com.coffice.security;

import com.coffice.util.Md5;
import com.coffice.util.SysPara;

public class Token
{

	public Token()
	{
	}

	public static String getToken(String str)
		throws Exception
	{
		String token_key = SysPara.getValue("token_key");
		return Md5.getMd5((new StringBuilder(String.valueOf(str))).append(token_key).toString());
	}

	public static boolean checkToken(String str, String token)
		throws Exception
	{
		String token_key = SysPara.getValue("token_key");
		String md5 = Md5.getMd5((new StringBuilder(String.valueOf(str))).append(token_key).toString());
		return md5.equals(token);
	}
}

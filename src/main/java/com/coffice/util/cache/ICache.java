// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ICache.java

package com.coffice.util.cache;


public interface ICache
{

	public abstract void setUserInfo(String s, String s1, Object obj);

	public abstract Object getUserInfo(String s, String s1);

	public abstract void setGlobalInfo(String s, String s1, Object obj);

	public abstract Object getGlobalInfo(String s, String s1);

	public abstract void removeInfo(String s, String s1);
}

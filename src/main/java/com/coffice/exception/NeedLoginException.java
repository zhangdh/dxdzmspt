// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   NeedLoginException.java

package com.coffice.exception;


public class NeedLoginException extends Exception
{

	private static final long serialVersionUID = 0x2dcfe4b433bc7d99L;

	public NeedLoginException()
	{
	}

	public NeedLoginException(String msg)
	{
		super(msg);
	}
}

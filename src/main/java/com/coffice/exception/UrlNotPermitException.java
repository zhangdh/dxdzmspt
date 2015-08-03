// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UrlNotPermitException.java

package com.coffice.exception;


public class UrlNotPermitException extends Exception
{

	private static final long serialVersionUID = 0xcf2bf7cbd632afc4L;

	public UrlNotPermitException()
	{
	}

	public UrlNotPermitException(String msg)
	{
		super(msg);
	}
}

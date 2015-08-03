// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SidNotPermitException.java

package com.coffice.exception;


public class SidNotPermitException extends Exception
{

	private static final long serialVersionUID = 0x742bb7ca5d78b94eL;

	public SidNotPermitException()
	{
	}

	public SidNotPermitException(String msg)
	{
		super(msg);
	}
}

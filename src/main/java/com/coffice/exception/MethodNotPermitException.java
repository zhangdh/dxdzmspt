// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MethodNotPermitException.java

package com.coffice.exception;


public class MethodNotPermitException extends Exception
{

	private static final long serialVersionUID = 0xb5b352884200003fL;

	public MethodNotPermitException()
	{
	}

	public MethodNotPermitException(String msg)
	{
		super(msg);
	}
}

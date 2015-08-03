// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Pinyin.java

package com.coffice.util;

import java.io.PrintStream;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin
{

	public Pinyin()
	{
	}

	public static String converterToFirstSpell(String chines)
	{
		String pinyinName = "";
		char nameChar[] = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++)
			if (nameChar[i] > '\200')
				try
				{
					pinyinName = (new StringBuilder(String.valueOf(pinyinName))).append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0)).toString();
				}
				catch (BadHanyuPinyinOutputFormatCombination e)
				{
					e.printStackTrace();
				}
			else
				pinyinName = (new StringBuilder(String.valueOf(pinyinName))).append(nameChar[i]).toString();

		return pinyinName;
	}

	public static String converterToSpell(String chines)
	{
		String pinyinName = "";
		char nameChar[] = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++)
			if (nameChar[i] > '\200')
				try
				{
					pinyinName = (new StringBuilder(String.valueOf(pinyinName))).append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]).toString();
				}
				catch (BadHanyuPinyinOutputFormatCombination e)
				{
					e.printStackTrace();
				}
			else
				pinyinName = (new StringBuilder(String.valueOf(pinyinName))).append(nameChar[i]).toString();

		return pinyinName;
	}

	public static void main(String args[])
	{
		System.out.println(converterToSpell("��������!!"));
	}
}

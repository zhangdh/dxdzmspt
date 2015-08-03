// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Log.java

package com.coffice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log
{

	static final Logger log = LoggerFactory.getLogger(com.coffice.util.Log.class);

	public Log()
	{
	}

	public static void write(LogItem logItem)
	{
		StringBuffer sb = new StringBuffer("{");
		if (logItem.getLogid().equals(""))
			logItem.setLogid(Guid.get());
		sb.append(logItem.getLogid()).append("}{").append(logItem.getYhid()).append("}{");
		sb.append(logItem.getClassName()).append("}{").append(logItem.getMethod()).append("}{");
		sb.append(logItem.getDesc()).append("}{").append(logItem.getContent()).append("}{");
		sb.append(logItem.getSql()).append("}{").append(logItem.getSqlData()).append("}");
		String logContent = sb.toString();
		if (logItem.getLevel().equals("") || logItem.getMethod().equals("") || logItem.getDesc().equals(""))
			System.out.println((new StringBuilder("⣺Log.write()")).append(logContent).toString());
		if (logItem.getLevel().toLowerCase().equals("debug")){
			log.debug(logContent);
		}else if (logItem.getLevel().toLowerCase().equals("info")){
			log.info(logContent);
		}else if (logItem.getLevel().toLowerCase().equals("warn")){
			log.info(logContent);
		}else if (logItem.getLevel().toLowerCase().equals("error")){
			log.info(logContent);
		}else if (logItem.getLevel().toLowerCase().equals("fatal")){
			log.info(logContent);
		}else{
			System.out.println(logContent);
		}
	}
	public static void writeLog(String level,String content){
		if (level.toLowerCase().equals("debug")){
			log.debug(content);
		}else if (level.toLowerCase().equals("info")){
			log.info(content);
		}else if (level.toLowerCase().equals("warn")){
			log.info(content);
		}else if (level.toLowerCase().equals("error")){
			log.info(content);
		}else if (level.toLowerCase().equals("fatal")){
			log.info(content);
		}else{
			System.out.println(content);
		}
	}
}

package com.coffice.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Sql {

	/**
	 * 将字符串类型的日期转换为一个Date
	 * @param dateString
	 * @return
	 * @throws java.lang.Exception
	 */
	 public final static java.sql.Date stringToDate(String dateString)
	  throws java.lang.Exception {
	  DateFormat dateFormat;
	  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  dateFormat.setLenient(false);
	  java.util.Date timeDate = dateFormat.parse(dateString);
	  java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());
	  return dateTime;
	 }
	 
	 /**
	  * 将字符串类型的日期转换为一个timestamp
	  * @param dateString
	  * @return
	  * @throws java.lang.Exception
	  */
	 public final static java.sql.Timestamp stringToTimestamp(String dateString)
	  throws java.lang.Exception {
	  DateFormat dateFormat;
	  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	  dateFormat.setLenient(false);
	  java.util.Date timeDate = dateFormat.parse(dateString);
	  java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());
	  return dateTime;
	 }
}

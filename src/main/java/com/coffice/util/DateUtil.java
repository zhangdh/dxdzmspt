package com.coffice.util;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class DateUtil
{
	 public static String getChineseDate(Date date)
	    throws Exception
	  {
	    if (date == null)
	      return null;
	    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", new DateFormatSymbols());
	    String dtrDate = df.format(date);
	    return dtrDate.substring(0, 4) + "年" + Integer.parseInt(dtrDate.substring(4, 6)) + "月" + Integer.parseInt(dtrDate.substring(6, 8)) + "日";
	  }
	 
	 public static String getChineseWeek(Date date)
	  {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int i = cal.get(7);
	    if (i == 1)
	      return "星期天";
	    if (i == 2)
	      return "星期一";
	    if (i == 3)
	      return "星期二";
	    if (i == 4)
	      return "星期三";
	    if (i == 5)
	      return "星期四";
	    if (i == 6)
	      return "星期五";
	    if (i == 7) {
	      return "星期六";
	    }
	    return "";
	  }
	 
	 public static String getDbDateF(){
		 try {
			String db_type= SysPara.getDbType();
			if("oracle".equals(db_type)){
				return "sysdate";
			}else if("mysql".equals(db_type)){
				return "now()";
			}else{
				return "getDate()";
			}
		} catch (Exception e) {
			return "getDate()";
		}
	 }
	 
	 public static String getCurTimeStr(){
		 java.text.DateFormat format1 = new java.text.SimpleDateFormat(  
         "yyyy-MM-dd hh:mm:ss");  
		 return format1.format(new Date());   
	 }

}
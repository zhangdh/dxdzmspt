package com.coffice.util;

import org.apache.commons.lang.StringUtils;

public class Str {
	
	public static String filterNull(String strIn) {
		if (strIn == null) {
			return "";
		} else if (StringUtils.upperCase(strIn).equals("NULL")) {
			return "";
		} else {
			return strIn;
		}
	}

	public static String autoQuotationMark(String strIn) {
		if (strIn == null) {
			return "null";
		} else if (strIn.equals("")) {
			return "null";
		} else if (StringUtils.upperCase(strIn).equals("NULL")) {
			return "null";
		} else {
			return "'" + strIn + "'";
		}
	}

	public static String nullToZero(String strin) {
		if ((strin) == null) {
			return "0.00";
		} else if (strin == "null") {
			return "0.00";

		} else if (strin == "") {
			return "0.00";
		} else {
			return strin;
		}

	}

    public static String filterNullToZero(String strIn) {
		if (strIn == null) {
			return "0";
		} else if (strIn == "null") {
			return "0";
		} else {
			return strIn;
		}
	}
    
	public static String charset(String str,String fromCharset,String toCharset) {
		try {
			String temp_p = str;
			byte[] temp_t = temp_p.getBytes(fromCharset);
			return new String(temp_t,toCharset);
		}
		catch(Exception e) {
			return "null";
		}
	}
}

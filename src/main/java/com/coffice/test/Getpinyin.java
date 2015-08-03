package com.coffice.test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Getpinyin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String name="市委组织部,市委宣传部,市委统战部,市委政法委,市委研究室,市委老干局,市档案局(馆),市直机关工委,市委保密办,市委党校,市委讲师团,市委农工部,市接待办公室,市委办公室,市纪委,定西日报社,市党史研究室,市编办";
		System.out.println(converterToFirstSpell(name));

	}
	
	
	public static String converterToFirstSpell(String chines){    
		String pinyinName = "";      
	    char[] nameChar = chines.toCharArray();      
	    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();      
	    defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);      
	    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
	    
	    for (int i = 0; i < nameChar.length; i++) {      
	        if (nameChar[i] > 128) {      
	            try {      
	                pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);      
	            } catch (BadHanyuPinyinOutputFormatCombination e) {      
	                e.printStackTrace();      
	            }      
	        }else{      
	            pinyinName += nameChar[i];      
	        }      
	    }      
	    return pinyinName;      
		
		
		
	}
		


}

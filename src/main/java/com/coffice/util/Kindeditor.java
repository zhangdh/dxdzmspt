package com.coffice.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coffice.util.Guid;
import com.oreilly.servlet.multipart.FileRenamePolicy;


public class Kindeditor implements FileRenamePolicy {
	public static String filename = "";

    public File rename(File file) {
        // TODO Auto-generated method stub
        String body="";
        String ext="";
        String guid="",fileAllName="";
        String wjmc=file.getName();
        
        boolean zwFlag=false;//名称中是否包含中文
        String regEx = "[\u4e00-\u9fa5]"; 
        Pattern pat = Pattern.compile(regEx); 
        Matcher matcher = pat.matcher(wjmc); 
        if(matcher.find()){ 
        	zwFlag=true;
        } 
        
        int pot=file.getName().lastIndexOf(".");
        ext=file.getName().substring(pot);
        guid=Guid.get();
        if(zwFlag) {
        	fileAllName=guid+ext;  
        } else {
        	fileAllName=guid + wjmc.substring(0,wjmc.lastIndexOf(".") )+ext;  
        }
        filename = fileAllName;
        file=new File(file.getParent(),fileAllName);
        return file;
    }
    //处理手机端显示的图片
    public String ZoomPhonePic(String nr){   	
    	try{
    		String size=SysPara.getValue("kindeditor_wap_pic_size");
	    	nr = nr.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1"); 
			nr = nr.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
			nr = nr.replaceAll("(.*?)src=\"(.*?)\"", "$1src=\"$2?size="+size+"\"");
    	}catch(Exception e){
    		e.printStackTrace();
    		System.out.println("处理手机端显示的图片的宽高属性出错啦！");
    	}
    	return nr;  	
    }

}

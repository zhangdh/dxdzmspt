package com.coffice.util;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


public class SmartPrinter extends Thread{
    private static List<String[]> fileList = new LinkedList<String[]>();
    private static Boolean flag = false;
    LogItem logItem = new LogItem();
    /**
     * 
     * @param src 源文件
     * @param target 目标文件
     */
    public void convertFile(String srcFile, String targetFile){
    	 String[] fileArray = {srcFile,targetFile};
    	 fileList.add(fileArray);
    	 if(!flag){
    	 synchronized (flag) {
			   if(fileList.size() > 0){
				   flag = true;
			       new SmartPrinter().start();
			   }
		    }
    	 }
    }
    /**
     * @param src 源文件
     * @throws SmartPrinterException 
     */
    public void convertFile(String srcFile){
    	if (!isExists(srcFile)){
    		logItem.setMethod("convertFile");
			logItem.setLevel("error");
			logItem.setContent("SmartPrinter找不到源文件!");
			return;
    	}
		String targetFile = srcFile.substring(0,srcFile.lastIndexOf(".")) + ".tif";
		convertFile(srcFile, targetFile);
    }
    
    public void run(){
    	try{
    	    while(true){
    	    	if(fileList.size() > 0){
    	           for(String[] fileArray : fileList){
    	               smartPrint(fileArray[0],fileArray[1]);
    	               Thread.sleep(2000);
    	           }
    	           fileList.clear();   
    	    	}else{
    	    		flag = false;
    	    		break;
    	    	}
    	    }
    	}catch(Exception e){
    		logItem.setMethod("new SmartPrinter().start()!");
			logItem.setLevel("error");
			logItem.setDesc("SmartPrinter转换文件时出现错误!");
			logItem.setContent(e.toString());
    		flag = false;
    	}
    }
    
    /**
     * 转换格式
     * @param src
     * @param target
     * @throws Exception
     */
    private void smartPrint(String src,String target) throws Exception{
    	       ComThread.InitSTA();
    	       Dispatch  ConvertEngine = (Dispatch)new ActiveXComponent("ConvertAgentCOM").getObject();
    	       Dispatch.call(ConvertEngine ,"InitAgent",new Variant("SmartPrinter"),new Variant("60"),new Variant("sdunisi"),new Variant("7DD5FBA90C563BC37DC4D096B61B7DCF"),false);  
               Dispatch.call((Dispatch)ConvertEngine ,"ConvertDoc",new Variant(src),new Variant(target));  
               Dispatch.call((Dispatch)ConvertEngine ,"CleanAgent");
               ComThread.Release();
    }
    
    
    /**
     * 判断文件是否存在?
     * @param src
     * @throws IOException
     */
    public boolean isExists(String src){
		File file = new File(src);
		if(!file.exists()) return false;
		return true;
	}
}

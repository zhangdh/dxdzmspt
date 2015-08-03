package com.coffice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Pdftoswf {
	/**
	 * 将pdf文件转化成swf文件 
	 * @param fileName 源文件绝对路径
	 * @param destPath 目标文件路径
	 * @param destName 目标文件名称 必须为swf文件
	 * @return -1：源文件不存在,-2:格式不正确,-3：发生异常,0:转化成功 
	 * @author fanglm created on Jul 9, 2010 1:13:04 PM
	 */
	public int ConvertPdfToSwf(String fileName,String destPath,String destName){
		String fileExt = "",destExt = "";
		StringBuffer command = new StringBuffer();
		fileExt = fileName.split("\\.")[fileName.split("\\.").length-1].toLowerCase();
		destExt = destName.split("\\.")[destName.split("\\.").length-1].toLowerCase();
		try{
			File file = new File(fileName);
			if(!file.exists()){//判断源文件是否存在
				return -1;
			}else if(!fileExt.equals("pdf") || !destExt.equals("swf")){//判断文件格式
				return -2;
			}
			else{
				String swftoolsPath = SysPara.getValue("swftools_path");//获取pdf转swf工具的路径
				if(!swftoolsPath.substring(swftoolsPath.length()-1, swftoolsPath.length()).equals("\\")){
					swftoolsPath = swftoolsPath+"\\";    //在目录后加 "\"
				}
				if(!destPath.substring(destPath.length()-1, destPath.length()).equals("\\")){
					destPath = destPath+"\\";    //在目录后加 "\"
				}
				File destFile = new File(destPath);
				if(!destFile.exists()){//目标文件路径如果不存在，则创建目录
					destFile.mkdirs();
				}
				command.append(swftoolsPath).append("pdf2swf.exe ").append(fileName).append(" -o ").append(destPath).append(destName);
				Process pro = Runtime.getRuntime().exec(command.toString());
				BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while(buffer.readLine()!=null);
				return pro.exitValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			return -3;
		}
	}
	/**
	 * 将pdf文件转化成swf文件 
	 * @param fileName 源文件绝对路径
	 * @param destPath 目标文件路径
	 * @return -1：源文件不存在,-2:格式不正确,-3：发生异常,0:转化成功 
	 * @author fanglm created on Jul 9, 2010 1:13:04 PM
	 */
	public int ConvertPdfToSwf(String fileName,String destPath){
		String destName = "",fileExt = "";
		StringBuffer command = new StringBuffer();
		fileExt = fileName.split("\\.")[fileName.split("\\.").length-1].toLowerCase();
		try{
			File file = new File(fileName);
			if(!file.exists()){//判断源文件是否存在
				return -1;
			}else if(!fileExt.equals("pdf")){//判断文件是否是pdf格式的文件
				return -2;
			}
			else{
				String swftoolsPath = SysPara.getValue("swftools_path");//获取pdf转swf工具的路径
				if(!swftoolsPath.substring(swftoolsPath.length()-1, swftoolsPath.length()).equals("\\")){
					swftoolsPath = swftoolsPath+"\\";    //在目录后加 "\"
				}
				if(!destPath.substring(destPath.length()-1, destPath.length()).equals("\\")){
					destPath = destPath+"\\";    //在目录后加 "\"
				}
				File destFile = new File(destPath);
				if(!destFile.exists()){//目标文件路径如果不存在，则创建目录
					destFile.mkdirs();
				}
				destName = file.getName().substring(0, file.getName().length()-4)+".swf";//目标文件名称
				command.append(swftoolsPath).append("pdf2swf.exe ").append(fileName).append(" -o ").append(destPath).append(destName);
				Process pro = Runtime.getRuntime().exec(command.toString());
				BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while(buffer.readLine()!=null);
				return pro.exitValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			return -3;
		}
	}
	/**
	 * 将pdf文件转化成同目录下的swf文件 
	 * @param fileName 源文件的绝对路径
	 * @return -1：源文件不存在,-2:格式不正确,-3：发生异常,0:转化成功 
	 * @author fanglm created on Jul 9, 2010 1:12:45 PM
	 */
	public int ConvertPdfToSwf(String fileName){
		String destName = "",fileExt = "";
		StringBuffer command = new StringBuffer();
		destName = fileName.substring(0, fileName.length()-4)+".swf";    //目标文件名称
		fileExt = fileName.split("\\.")[fileName.split("\\.").length-1].toLowerCase();
		try{
			File file = new File(fileName);
			if(!file.exists()){//判断源文件是否存在
				return -1;
			}else if(!fileExt.equals("pdf")){//判断文件是否是pdf格式的文件
				return -2;
			}
			else{
				String swftoolsPath = SysPara.getValue("swftools_path");//获取pdf转swf工具的路径
				if(!swftoolsPath.substring(swftoolsPath.length()-1, swftoolsPath.length()).equals("\\")){
					swftoolsPath = swftoolsPath+"\\";    //在目录后加 "\"
				}
				command.append(swftoolsPath).append("pdf2swf.exe ").append(fileName).append(" -o ").append(destName);
				Process pro = Runtime.getRuntime().exec(command.toString());
				BufferedReader buffer = new BufferedReader(new InputStreamReader(pro.getInputStream()));
				while(buffer.readLine()!=null);
				return pro.exitValue();
			}
		}catch (Exception e){
			e.printStackTrace();
			return -3;
		}
	}
}

package com.coffice.util.excel;

import java.io.File;
import java.util.Map;

import com.coffice.util.cache.Cache;

import net.sf.jxls.transformer.XLSTransformer;


public class ExcelThread extends Thread {
	private String destFileName,templateFilepath,key,path,exportName,pre;
	private Map map,beans;
	private XLSTransformer transformer = null;
	
	public void run() { 
		try {
			File file = new File(destFileName);
			System.out.println("目标文件路径:"+destFileName);
			file.createNewFile();
			transformer.transformXLS(pre + templateFilepath, beans, destFileName);
			System.out.println("模板文件路径:"+pre + templateFilepath);
			System.out.println("web文件路径:"+pre + path+"/excelDestFile/"
					+ exportName + "_" + key + ".xls");
			
			Cache.setUserInfo((String) map.get("yhid"), key, path+"/excelDestFile/"
					+ exportName + "_" + key + ".xls");			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	public String getDestFileName() {
		return destFileName;
	}

	public void setDestFileName(String destFileName) {
		this.destFileName = destFileName;
	}

	public String getTemplateFilepath() {
		return templateFilepath;
	}

	public void setTemplateFilepath(String templateFilepath) {
		this.templateFilepath = templateFilepath;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Map getBeans() {
		return beans;
	}

	public void setBeans(Map beans) {
		this.beans = beans;
	}

	public XLSTransformer getTransformer() {
		return transformer;
	}

	public void setTransformer(XLSTransformer transformer) {
		this.transformer = transformer;
	} 
}

package com.coffice.util.excel;

import java.io.File;
import java.util.Map;

import com.coffice.util.cache.Cache;

import net.sf.jxls.transformer.XLSTransformer;


public class ExcelThreadV2 extends Thread {
	private String webFilepath,templateFilepath,key,exportFilePath,yhid;
	private Map map,beans;
	private XLSTransformer transformer = null;
	
	public void run() { 
		try {
			File file = new File(exportFilePath);
			System.out.println("version 2: 导出文件路径："+exportFilePath+" 模板文件路径："+templateFilepath+" web访问路径："+webFilepath);
			file.createNewFile();
			transformer.transformXLS( templateFilepath, beans, exportFilePath);
			Cache.setUserInfo(yhid, key, webFilepath);
			//System.out.println("yhid:"+yhid+"key:"+key);
			System.out.println("导出文件成功！");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
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


	public String getWebFilepath() {
		return webFilepath;
	}


	public void setWebFilepath(String webFilepath) {
		this.webFilepath = webFilepath;
	}


	public String getExportFilePath() {
		return exportFilePath;
	}


	public void setExportFilePath(String exportFilePath) {
		this.exportFilePath = exportFilePath;
	}


	public String getYhid() {
		return yhid;
	}


	public void setYhid(String yhid) {
		this.yhid = yhid;
	}


	
}

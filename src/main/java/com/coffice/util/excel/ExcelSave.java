package com.coffice.util.excel;

import java.io.File;
import java.util.Map;

import com.coffice.util.BaseUtil;
import com.coffice.util.JspJsonData;
import com.coffice.util.LogItem;
import com.oreilly.servlet.multipart.FileRenamePolicy;


public class ExcelSave extends BaseUtil implements FileRenamePolicy {
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String yhid;
	Map map;
	String sql;
	String fileName;

	public ExcelSave(String fileName) {
		this.fileName = fileName;
		jjd = new JspJsonData();
		logItem = new LogItem();
		logItem.setClassName(this.getClass().getName());
	}

	// 上传文件名
	public File rename(File file) {
		int pot = file.getName().lastIndexOf(".");
		String ext = file.getName().substring(pot);
		String newFileName = fileName + ext;
		file = new File(file.getParent(), newFileName);
		return file;
	}
}

package com.coffice.util.excel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.coffice.util.BaseUtil;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;
import com.coffice.util.SysPara;


public class ExcelTool  {
	private Map beans = new HashMap();
	private XLSTransformer transformer = new XLSTransformer();
	private String templateFilepath = "";
	private String templateFileName="";
	private String exportName = "";
	private int dataSize = 0;

	/**
	 * 构造方法，初始化模板路径和导出文件名称
	 * 
	 * @param templateFilepath
	 *            模板文件路径
	 * @param exportName
	 *            导出到客户端的excel名称
	 */
	public ExcelTool(String templateFilepath, String exportName) {
		this.templateFilepath = templateFilepath;
		this.templateFileName=templateFilepath+".xls";
		this.exportName = exportName;

	}

	/**
	 * 添加模板中的bean
	 * 
	 * @param beanName
	 *            模板bean的名字
	 * @param beanMap
	 *            数据库中查出来的数据
	 */
	public void addBeans(String beanName, Map beanMap) {
		if (beanMap==null||beanMap.isEmpty()) {
			beans.put(beanName, new Object());
		} else {
			beans.put(beanName, getVmBean(beanMap));
		}
	}

	/**
	 * 通过Map获取对应的虚拟Bean
	 * 
	 * @param map
	 * @return
	 */
	private DynaBean getVmBean(Map map) {
		DynaProperty[] properties = new DynaProperty[map.size()];
		Iterator it = map.keySet().iterator();
		int i = 0;
		String key = "";
		Object value = null;
		while (it.hasNext()) {
			key = (String) it.next();
			value = map.get(key);
			if(value==null){
				properties[i] = new DynaProperty(key, Object.class.getClass());	
			}else{
				properties[i] = new DynaProperty(key, value.getClass());
			}
			i++;
		}
		BasicDynaClass dynaClass = new BasicDynaClass("vmBean",
				BasicDynaBean.class, properties);
		DynaBean vmBean = null;
		try {
			vmBean = dynaClass.newInstance();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Iterator it2 = map.keySet().iterator();
		while (it2.hasNext()) {
			key = (String) it2.next();
			value = map.get(key);
			vmBean.set(key, value);
		}
		return vmBean;
	}

	/**
	 * 添加模板中的list
	 * 
	 * @param listName
	 *            模板中list的名字
	 * @param dataList
	 *            数据库中查出来的数据
	 */
	public void addLists(String listName, List<Map> dataList) {
		if(dataList==null||dataList.size()==0){
			return;
		}
		List data = new ArrayList();
		for (Map map : dataList) {
			data.add(getVmBean(map));
		}
		dataSize = (dataSize + data.size());
		beans.put(listName, data);
		transformer.groupCollection(listName);
	}

	/**
	 * 导出excel
	 * 
	 * @param response
	 */
	public void exportExcel(HttpServletResponse response,
			HttpServletRequest request) {
		try {
			if (dataSize > 2000) {
				String key = new Date().getTime() + "";
				String pre = request.getRealPath("/");
				String path = request.getContextPath();
				Map map = RequestUtil.getMap(request);
				String destFileName = request.getRealPath("/excelDestFile/"
						+ exportName + "_" + key );
				response.sendRedirect(path+"/excel/wait.jsp?key="+key);
				//response.sendRedirect(path+"/excel/wait.jsp?key="+key+"&fileName="+exportName);
				ExcelThread excellThread = new ExcelThread();
				excellThread.setBeans(beans);
				excellThread.setExportName(exportName);
				excellThread.setDestFileName(destFileName);
				excellThread.setMap(map);
				excellThread.setKey(key);
				excellThread.setPath(path);
				excellThread.setPre(pre);
				excellThread.setTemplateFilepath(templateFilepath);
				excellThread.setTransformer(transformer);
				excellThread.start();
			} else {
				this.exportName = new String(exportName.getBytes("gb2312"),"iso8859-1");
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + exportName + ".xls");
				InputStream is = new BufferedInputStream(new FileInputStream(
						request.getRealPath("/") + templateFilepath));
				HSSFWorkbook workbook = (HSSFWorkbook) transformer
						.transformXLS(is, beans);
				OutputStream os = response.getOutputStream();
				workbook.write(os);
				is.close();
				os.flush();
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("os close time:" + new Date().toLocaleString());
	}

	/**
	 * 第二个版本的导出功能
	 * @param jjd
	 * @param yhid
	 * 用户ID
	 */

	public void exportExcel(JspJsonData jjd,String yhid) {
		try {
				String key = new Date().getTime() + "";
				String lj =  SysPara.getValue("excel_export_path");
				String destFileName = lj.split(":")[1];
				jjd.setExtend("key", key);
				ExcelThreadV2 excellThread = new ExcelThreadV2();
				excellThread.setBeans(beans);
				excellThread.setKey(key);
				excellThread.setYhid(yhid);
				excellThread.setTemplateFilepath(lj+"/"+templateFileName);
				excellThread.setWebFilepath(destFileName+"/temp/"+ exportName + "_" + key + ".xls");
				excellThread.setExportFilePath(lj+"/temp/"+ exportName + "_" + key + ".xls");
				
				excellThread.setTransformer(transformer);
				excellThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}
	
	
}

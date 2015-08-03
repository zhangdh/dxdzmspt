package com.coffice.util.excel;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;


import com.coffice.bean.PageBean;
import com.coffice.bean.UserBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;
import com.oreilly.servlet.MultipartRequest;

import edu.emory.mathcs.backport.java.util.LinkedList;

public class ExcelImport extends BaseUtil {

	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String yhid;
	Map map;
	protected static String strOralceTime = "to_date({0},'yyyy-mm-dd hh24:mi:ss')";// oracle数据库格式化日期函数(精确到秒)
	private static final SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
	public ExcelImport() {
	}

	public ExcelImport(Map mapin) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapin.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapin;
	}

	public Map list() {
		try {
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql("select * from t_excel_upload where is_del=0");
			page
					.setCountSql("select count(*) from t_excel_upload where is_del=0");
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);
		} catch (Exception e) {
			jjd.setResult(false, "ExcelUpload.list异常:" + e.toString());
			String msg=new StringBuffer().append("处理批量导入显示列表时出现异常:").append(e.toString()).toString();
		    //log.error(msg);
		}
		return jjd.getData();
	}

	public Map query() {
		try {
			String cx_mz = map.get("cx_mc").toString();
			StringBuffer sWhereSQL = new StringBuffer();
			if (cx_mz != null && !"".equals(cx_mz)) {
				sWhereSQL.append(" and mc like '%").append(cx_mz).append("%'");
			}
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql("select * from t_excel_upload where is_del=0"
					+ sWhereSQL);
			page
					.setCountSql("select count(*) from t_excel_upload where is_del=0"
							+ sWhereSQL);
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);

			jjd.setExtend("cjsj", this.getCurrentTime().substring(0, 10));
			// List _selectlist =
			// this.getNpjtN().queryForList("select guid as dm,mz as mc from t_photo_fl where is_del=0",map);
			// jjd.setSelect("fl_guid", _selectlist);
			// long startTime = System.currentTimeMillis();
			// long endTime=System.currentTimeMillis();
			// for (int i = 0; i < 3523; i++) {
			// this.getNpjtN().update("insert into t_dm_xzqh_excel_temp(xzqh_dm,xzqh_mc,sj_xzqh_dm) values('11111','22222','33333')",
			// map);
			// }
			// endTime=System.currentTimeMillis();
			// System.out.println("3523row结束******程序运行时间： "+(endTime-startTime)+"ms");
		} catch (Exception e) {
			jjd.setResult(false, "ExcelUpload.query异常:" + e.toString());
			String msg=new StringBuffer().append("处理批量导入查询时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return jjd.getData();

	}

	// 显示明细
	public Map show() {
		try {
			Map _map = this.getNpjtN().queryForMap(
					"select * from t_excel_upload where guid=:guid", map);
			jjd.setForm(_map);
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
			String msg=new StringBuffer().append("批量导入没有查找到数据出现异常:").append(e.toString()).toString();
	        //log.error(msg);
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
			String msg=new StringBuffer().append("批量导入数据部唯一出现异常:").append(e.toString()).toString();
	        //log.error(msg);
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "查找明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			String msg=new StringBuffer().append("处理批量导入显示明细时出现异常:").append(e.toString()).toString();
	        //log.error(msg);
		}
		return jjd.getData();
	}

	/*
	 * 保存配置
	 */
	public Map save() {
		String id = Guid.get();
		try {
			map.put("guid", Guid.get());
			map.put("cjsj", new Date());
			map.put("is_del", 0);
			if (SysPara.getValue("db_type").equals("oracle")) {
				this.getSji().withTableName("t_excel_upload").execute(map);
			} else {
				this.getSji().withTableName("t_excel_upload").execute(map);
			}
			jjd.setResult(true, "保存成功");
			// 所有增删改操作要写业务日志
			//oplog.error("批量导入保存配置保存成功");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "保存数据时出现错误，错误代码:" + guid);
			String msg=new StringBuffer().append("批量导入保存数据时出现异常:").append(e.toString()).toString();
	        //log.error(msg);
			throw new ServiceException("保存数据时异常");// 抛出此异常以触发回滚
		}
		return jjd.getData();
	}

	/*
	 * 删除配置
	 */
	public Map delete() {
		String delguid = "";
		if (map.get("table_list_checkbox") != null
				&& !map.get("table_list_checkbox").equals("")) {
			delguid = map.get("table_list_checkbox").toString();
			delguid = delguid.replaceAll("~", "','");
		}

		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("update t_excel_upload set is_del=1 where guid in ('"
					+ delguid + "')");
			this.getNpjtN().update(strSQL.toString(), map);
			jjd.setResult(true, "删除成功");
			//oplog.error("批量导入删除配置成功");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "删除数据时出现错误，错误代码：" + guid);
			String msg=new StringBuffer().append("批量导入删除数据时出现异常:").append(e.toString()).toString();
	        //log.error(msg);
			throw new ServiceException("删除数据时异常");// 回滚
		}
		return jjd.getData();
	}

	/*
	 * 更新配置
	 */
	public Map update() {
		try {
			String guid = (String) map.get("guid");
			String strSQL = "update t_excel_upload set mc=:mc,bmc=:bmc,zddy=:zddy,zdyz=:zdyz,zdqz=:zdqz,ffyz=:ffyz,zj=:zj,ksh=:ksh,jsh=:jsh,ksl=:ksl,jsl=:jsl where guid=:guid";
			this.getNpjtN().update(strSQL, map);

			jjd.setResult(true, "修改成功");

			//oplog.error("批量导入修改配置成功");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "修改数据时出现错误，错误代码：" + guid);
			String msg=new StringBuffer().append("批量导入修改数据时出现异常:").append(e.toString()).toString();
	        //log.error(msg);
			throw new ServiceException("修改数据时异常");// 回滚
		}
		return jjd.getData();
	}

	// 保存上传的excel文档
	public Map saveExcelUpload(HttpServletRequest request) {
		String msg = "";
		String guidString = Guid.get();
		int maxPostSize = 10 * 1024 * 1024;
		String fileName = guidString;
		String fileRealName = ""; // 真实的文件名字
		ExcelSave excelSave = new ExcelSave(fileName);
		Map reMap = null;

		try {
			String saveDirectory = SysPara.getValue("excel_upload_path");
			creatDir(saveDirectory);
			MultipartRequest multi = new MultipartRequest(request,
					saveDirectory, maxPostSize, "utf-8", excelSave);
			Enumeration files = multi.getFileNames();
			fileRealName = multi.getParameter("filerealname");
			fileRealName = fileRealName.substring(fileRealName
					.lastIndexOf("\\") + 1, fileRealName.length());
			String extNameString = fileRealName.substring(fileRealName
					.lastIndexOf("."), fileRealName.length());
			String excelUploadGuidString = (String) map
					.get("excel_upload_guid");

			String urlParaString = (String) map.get("urlPara");
			// String urlParaString = multi.getParameter("urlPara");;
			Map urlParaMap = hashUrlPara(urlParaString);
			
			String queryPara = (String) map.get("queryPara");
			
			Map queryParaMap = hashUrlPara(queryPara);

			map.put("guid", guidString);
			map.put("yhid", yhid);
			map.put("wjmc", fileRealName);
			map.put("scsj", new Date());
			map.put("excel_upload_guid", excelUploadGuidString);
			map.put("his_state", 0);
			if (SysPara.getValue("db_type").equals("oracle")) {
				this.getSji().withTableName("t_excel_upload_his").execute(map);
			} else {
				this.getSji().withTableName("t_excel_upload_his").execute(map);
			}

			ExcelConf excelConf = getExcelConf(excelUploadGuidString);

			reMap = initExcel(saveDirectory + "/" + fileName + extNameString,
					fileRealName, guidString, excelUploadGuidString, excelConf,
					urlParaMap,queryParaMap);

			map.put("urlPara", urlParaString);
			map.put("resultMap", reMap);
			map.put("excel_upload_guid", excelUploadGuidString); // 配置的主键
			map.put("upload_lishi_guid", guidString); // 历史的主键

			List vaildateList = (List) reMap.get("vaildateList");
			if (vaildateList == null || vaildateList.size() < 1) { // 验证通过
				clearData(guidString, 1, excelConf);
			}
			//oplog.info("批量导入保存上传Excel成功");
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			String message=new StringBuffer().append("批量导入保存上传Excel时出现异常:").append(e.toString()).toString();
	        //log.error(message);
			msg = "保存失败";
		}
		return map;
	}

	/**
	 * 分析、验证、插入excel上传文档
	 * 
	 * @param filePath
	 *            文件全路径
	 * @param fileRealName
	 *            文件真实名字
	 * @param uploadLishiGuid
	 *            历史表的主键
	 * @param uploadGuid
	 *            配置表的主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> initExcel(String filePath, String fileRealName,
			String uploadLishiGuid, String uploadGuid, ExcelConf excelConf,
			Map<String, String> urlPara,Map<String, String> queryPara) {
		String msg = "";
		StringBuffer strSQL = new StringBuffer();
		Map<String, Object> vaildateMap = new HashMap<String, Object>();
		String tableName = excelConf.bmcTemp;
		String zhujian = excelConf.zj;
		
		String inputSql = "";
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		try {
			File file = new File(filePath);
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			boolean isEmpty = true; // 判断一列是否为空，为空则结束

			Map<String, String> colTitleNameMap = new HashMap<String, String>();
			LinkedList colList = new LinkedList();
			LinkedList colNameList = new LinkedList();

			//add by hanxc at 20101216循环列，保存Excel中的列名到excel_upload_temp表中，键是excel头单元格顺序，值是单元格内容 开始
			if (rows > 0 && cols > 0) {
				for (int i = 0; i < cols; i++) {
					Cell cell = sheet.getCell(i, 0);
					
					String cellString = cell.getContents().trim();
					if(!cellString.equals("")){//add by yan_jwei 防止列名为空
					strSQL.delete(0, strSQL.length());
					strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'colName_"
							+ i
							+ "', '"
							+ cellString
							+ "')");
					this.getNpjtN().update(strSQL.toString(), map);

					colTitleNameMap.put(cellString , cellString);
					colList.add(excelConf.getzddy(cellString));//excel文件列名称对应excel上传配置中定义对应的字段
					colNameList.add(cellString);//对应的excel文件的列
					}
				}
			}
			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'colNum', '" + cols + "')");//保存excel文件的列数
			this.getNpjtN().update(strSQL.toString(), map);
      
			//end add by hanxc at 20101216 循环列，保存Excel中的列名到excel_upload_temp表中，键是excel头单元格顺序，值是单元格内容  结束
			
			Map  insertMap = new HashMap();		
			UserBean userbean = (UserBean) Cache.getUserInfo(yhid, "userbean");
			String yhid = userbean.getYhid();
			String bmid = userbean.getBmid();
			String gwid = userbean.getGwid();
			String zzid = userbean.getZzid();
			insertMap.put("yhid", yhid);
			insertMap.put("bmid", bmid);
			insertMap.put("gwid", gwid);
			insertMap.put("zzid", zzid);
			insertMap.putAll(urlPara);
			insertMap.put("upload_lishi_guid", uploadLishiGuid);
			if (!zhujian.equals("")) {
				String zhujianString = Guid.get();
				map.put(zhujian, zhujianString);
				insertMap.put(zhujian,zhujianString);
				
			}
			
			
			//String insertSql = "insert into "+ excelConf.bmc;
			
			// 拼装sql
			String urlColName = "", urlColValue = "";
			int colNameSqlNum = 0;
			
			String  insertCol = excelConf.zddy;
			String colName = "",colValue="";
	        String[] insert = insertCol.split(";");
	        for(int x=0;x<insert.length;x++){
	        	String[] in = insert[x].split(":");
	        	String col = ":"+in[0]+",";
	        	String col2 = in[1]+",";
	        	colValue = colValue.trim()+col;
	        	colName = colName.trim()+ col2;	        	
	        }
	        if (!zhujian.equals("")) {
	        	colName = colName +zhujian+",";
	        	colValue = colValue+":"+zhujian+",";
	        }
			
			strSQL.delete(0, strSQL.length());
			strSQL.append("insert into " + tableName + "(" + colName
					+ " upload_lishi_guid) values(" + colValue
					+ " :upload_lishi_guid)");
			
			
			String insertSql = strSQL.toString();
			
			inputSql = strSQL.toString();
					
			//得到数据库配置字段取值信息
			Map valueMap=getZdqzValue(excelConf.getzdqzMap());//字段取值，通过；； ：：拆分

			//List<String> vaildateList = new LinkedList();
			List vaildateList = new LinkedList();
			boolean rowVildatePass = true; // 这一行所有列都通过验证
			Map<String, String> vildateTempMap = new HashMap<String, String>();
			List tgList=new ArrayList();
			int  count=0;//add by yan_jwei 记录上传的记录数
			for (int i = 0; i < rows; i++) { // 循环行
				if (excelConf.jsh != 0 && excelConf.jsh < i) { // 结束行生效
					break;
				}
				if (excelConf.ksh != 0 && i < excelConf.ksh) {
					continue;
				}
				boolean ifCall = false;
				HashMap callArgMap = new HashMap<String, Object>();
				rowVildatePass = true;
				vildateTempMap.clear();
				List<String> allVaildateList = new LinkedList();
				Set<String> callPathMethodSet = new LinkedHashSet<String>();
				isEmpty = true;

				int colNum = 0;
				String zhujianString = ""; // 保存主键
				Map m = new HashMap();
				Map errorm=new HashMap();
				for (int j = 0; j < cols; j++) { // 循环列
					if (excelConf.jsl != 0 && excelConf.jsl < j) { // 结束列生效
						break;
					}
					if (excelConf.ksl != 0 && j < excelConf.ksl) {
						continue;
					}
					boolean cellVildatePass = true; // 这一个单元格验证通过
					Cell titleCell = sheet.getCell(j,0);
					Cell cell = sheet.getCell(j, i);
					String cellString="";
					CellType celltype=cell.getType();
					if(celltype==CellType.DATE){
						DateCell dateCell=(DateCell)cell;
						cellString = dateCell.getContents();
						Date date1=dateCell.getDate();
						getLocaltime(date1);
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						cellString=dateFormat.format(date1);
					}else{
						cellString = cell.getContents().trim();
					}
					if (!cellString.equals("")) {
						isEmpty = false;
					}
					String colTitleNameString = colTitleNameMap.get(titleCell.getContents().trim());
					if(colTitleNameString==null||colTitleNameString.trim().equals(""))continue;//如果列名为空就不再加入到数据库中
					String colTypeString = excelConf.getzdyzType(
							colTitleNameString).toLowerCase();
					int colLen = excelConf.getzdyzLen(colTitleNameString);
					// update by yanjiawei 先获取是否从表中取值，如果是则不验证该字段
					String excelColName = (String) colNameList.get(j);
					String excelCol = (String) colList.get(j);
					String sqlString = excelConf.getzdqz(excelColName);//字段取值取出来的sql
					Map midvalueMap=(Map) valueMap.get(excelColName);//add by yan_jwei 2010-11-29根据名称读取该名称对应的map
					if (colLen != 0 && cellString.length() > colLen) {
						rowVildatePass = false;
						cellVildatePass = false;
					}
					// 判断是否从表中取值开始
					if (sqlString.equals("")) {	// update by yanjiawei 先获取是否从表中取值，如果是则不验证该字段
						if (colTypeString.equals("int")) {
							try {
								int tempInt = Integer.parseInt(cellString);
							} catch (Exception e) {
								rowVildatePass = false;
								cellVildatePass = false;
							}
						} else if (colTypeString.equals("datetime")) {
							if(!checkDatetime(cellString)){//时间格式验证不通过
								rowVildatePass = false;
								cellVildatePass = false;
							}
						}else if(colTypeString.equals("float")){
							try{ 
								float   f=new   Float(cellString).floatValue(); 
								} 
								catch(NumberFormatException   e) 
								{ 
									
									rowVildatePass = false;
									cellVildatePass = false;
								} 

						}else if(colTypeString.equals("string")){
							
						}
					}
					
					//System.out.println(cellString);
					
					
					
						//update by yan_jwei 2010-11-03 oracle数据库对于增加时间类型需要赋予Date才能添加
					/*	if(celltype==CellType.DATE){
							DateCell dateCell=(DateCell)cell;
							Date date1=dateCell.getDate();
							getLocaltime(date1);
							
							m.put(titleCell.getContents(), date1);
							errorm.put(titleCell.getContents(), date1);
							//map.put("col" + j, date1);
						}else{*/
							m.put(titleCell.getContents(), cellString.equals("") ? null
									: cellString);
							errorm.put(titleCell.getContents(), cellString.equals("") ? null
									: cellString);
							//map.put("col" + j, cellString.equals("") ? null
								//	: cellString);
						//}
						
					
						
						
					
						
						
						
						// 判断是否从表中取值 结束
						if (!cellVildatePass) { // 本单元格验证没通过
							allVaildateList.add("error_row_" + i + "_col_" + j);
						} else {
							allVaildateList.add("row_" + i + "_col_" + j);
						}
						vildateTempMap.put("row_" + i + "_col_" + j, cellString);
				} // 循环列结束
            
				List zdqzList = null;
				
				String zdqzSql = excelConf.zdqz;
				m.putAll(insertMap);
				String[] zd=zdqzSql.split(";");
				Map zdqzMap = new HashMap();
				for(int y=0;y<zd.length;y++){
					
					String zdSql = zd[y];
					if(!"".equals(zdSql)&&zdSql!=null){
						zdqzList = this.getNpjtN().queryForList(zdSql, m);
						if(zdqzList.size()==1){
							zdqzMap = (Map)zdqzList.get(0);
						    Map zdMap = new HashMap();
							for(Iterator ite = zdqzMap.entrySet().iterator(); ite.hasNext();){ 
								  Map.Entry entry = (Map.Entry) ite.next();   
								  String key = (String)entry.getKey();	
								  key = key.toLowerCase();
								  zdMap.put(key,  entry.getValue());
							}  

							
							m.putAll(zdMap);
						}else if(zdqzList.size()<=0){
							//给字段取值取出来的值赋NULL值
							try{
							String s = zdSql.substring(zdSql.indexOf("select")+"select".length(), zdSql.indexOf("from"));									
							String[] ss = s.split(",");									
							for(int mm=0;mm<ss.length;mm++){
								String sss = ss[mm];										
								//String[] ssss = sss.split(" as ");
								String[] ssss = sss.split("\\s{1,}as\\s{1,}");  
								String s1 = ssss[1].trim();
								m.put(s1, "");												
							}
							}catch(Exception e){
								msg = msg + "字段取值配置错误，";
								throw new RuntimeException("字段取值的sql是否正确配置，包含了AS？");
								
							}
						}
						else{
							msg = msg + "字段取值返回结果条数不能超过1条";
							throw new RuntimeException("字段取值返回结果条数不能超过1条");						
						}
					}
				}					
				
				
				if (isEmpty) { // 如果一行为空，则结束
					break;
				}
				count++;
				if (!rowVildatePass) { // 本行验证没通过
					//vaildateList.add(((HashMap)errorm).clone());
					for (String keyString : allVaildateList) {
						vaildateList.add(keyString);
					}
					vaildateMap.putAll(vildateTempMap);
				} else { // 通过，插入到数据库
					try {
						if (!zhujian.equals("")) {
							zhujianString = Guid.get();
							map.put(zhujian, zhujianString);
							m.put(zhujian,zhujianString);
							callArgMap.put("guid", zhujianString);
						}
											
						//this.getNpjtN().update(inputSql, map); // 插入到数据库
						tgList.add(((HashMap)m).clone());//add by yan_jwei 把通过验证的数据存放到list，验证完后批量插入数据库
										
					} catch (Exception e) {
						for (String keyString : allVaildateList) {
							keyString = keyString.startsWith("error_") ? keyString
									: "error_" + keyString;
							//vaildateList.add(keyString);
						}
						vaildateMap.putAll(vildateTempMap);
					}
				}
				
			}
			
			
			vaildateMap.put("vaildateList", vaildateList);
			vaildateMap.put("colNum", cols);
			/*endTime = System.currentTimeMillis();
			System.out.println("结束插入临时表时间： " + (endTime - startTime)+ "ms");*/
		    String res =	batchInsert(tgList,inputSql);// add by yan_jwei 2010-11-29 验证通过的数据批量导入临时表
			if("1".equals(res)){
				msg = msg+"批量导入SQL异常";
			}
			else if("2".equals(res)){
				msg = msg+"excel中日期填写不正确";
			}else {
				msg = msg +res;
			}
			
			setTotalCount(uploadLishiGuid,count);
			//String fieldstr=getTableFields(excelConf.bmc+"_excel_temp");
			String fieldstr=getTableFields(excelConf.bmc);
			if (vaildateList == null || vaildateList.size() < 1) { // 验证通过
				
				String ywff =excelConf.ffyz;
				if(ywff!=null&&!"".equals(ywff)){
					String[] yw = ywff.split(";");
					for(int x=0;x<yw.length;x++){
						String c = yw[x];
						String cpath = c.split(":")[0];
						String cMehthod = c.split(":")[1];
						callYWMethod(cpath,
								cMehthod, tgList);
					}
				}
				
				
				strSQL.delete(0, strSQL.length());
				strSQL.append("insert into " + excelConf.bmc
						+ " select "+fieldstr +" from " + excelConf.bmcTemp +" where  upload_lishi_guid = '"+uploadLishiGuid+"'" );
				this.getNpjtN().update(strSQL.toString(), map);
				clearData(uploadLishiGuid, 1, excelConf);
			}
		
			//oplog.info("批量导入数据成功");	
			if(msg.equals("")){
			  msg = "保存成功";
			}
			vaildateMap.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			
			String message=new StringBuffer().append("批量导入数据时出现异常:").append(e.toString()).toString();
	        //log.error(message);
			msg = "保存失败" +msg +message;
			vaildateMap.put("msg", msg);
		}
		return vaildateMap;
	}
	/**
	 * 传入一个表名，获得该表的所有列
	 * @param sql
	 * @return
	 */
	public String getTableFields(String tablename){
		String sql="select *  from "+tablename;
		StringBuilder fields=new StringBuilder("");
		SqlRowSet sqlRowSet=this.getJtA().queryForRowSet(sql);
		SqlRowSetMetaData sqlRowSetMetaData=sqlRowSet.getMetaData();
		int columnCount=sqlRowSetMetaData.getColumnCount();
		for(int i=1;i<=columnCount;i++){
			fields.append(sqlRowSetMetaData.getColumnName(i)).append(",");
		}
		String fieldstr=fields.toString();
		if(fieldstr.lastIndexOf(",")!=-1){
			fieldstr=fieldstr.substring(0,fieldstr.lastIndexOf(","));
		}
		return fieldstr;
		
	}
	/**
	 * 设置该次上传上传数据总条数
	 * @param uploadLishiGuid
	 * @param total
	 */
	public void setTotalCount(String uploadLishiGuid,int total){
		String strsql="update t_excel_upload_his set zts=" + total+ " where guid='" + uploadLishiGuid + "'";
		this.getNpjtN().update(strsql, map);
	}
	/**
	 * 把插入sql语句改为查询语句
	 * @param insertsql
	 * @return
	 */
	public String getQuerySql(String insertsql){
		String resql="select ";
		if(insertsql!=null&&!insertsql.equals("")){
			insertsql=insertsql.replace("insert into ", " from ");
			int start=insertsql.indexOf("(");
			int end=insertsql.indexOf(")");
			resql+=insertsql.substring(start+1,end);
			resql+=insertsql.substring(0,start);
			
		}
		return resql;
	}
	/**
	 * 批量导入数据
	 * @param list 数据的list
	 * @param sql sql为插入sql语句
	 */
	public String batchInsert(final List list,final String sql) {
		String res = "";
		String querysql=getQuerySql(sql);//把插入sql语句改为查询语句
		String newsql="";
		SqlRowSet sqlRowSet=this.getJtA().queryForRowSet(querysql);
		SqlRowSetMetaData sqlRowSetMetaData=sqlRowSet.getMetaData();//根据查询sql获得各个字段的类型信息
		final NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(this.getJtN());
		if(list!=null&&list.size()>0){
			Map map=(Map) list.get(0);
			MapSqlParameterSource mapsqlparam=new MapSqlParameterSource(map);
			//namedParameterJdbcTemplate.getPreparedStatementCreator(sql, mapsqlparam);
			newsql=namedParameterJdbcTemplate.sqltoUse(sql, mapsqlparam);
			boolean runflag=false;
			List newlist=new ArrayList();
			for(int m=0;m<list.size();m++){
				newlist.add(list.get(m));
				if(m%500==0&&m!=0){
					try{
					res = batchupdate(newsql,sql,newlist,namedParameterJdbcTemplate,sqlRowSetMetaData);
					}catch(Exception e){
						res = e.getMessage();
					}
					newlist.clear();
				}
			}
			if(newlist.size()>0){
				try{
				res = batchupdate(newsql,sql,newlist,namedParameterJdbcTemplate,sqlRowSetMetaData);
				}catch(Exception e){
					res = e.getMessage();
				}
			}
		}
		return res;
	}
	 /**
	 * 
	 * @param sql
	 * @param cansql
	 * @param list
	 * @param namedParameterJdbcTemplate
	 * @param sqlRowSetMetaData
	 */
	public String batchupdate(final String sql,final String cansql,final List list,final NamedParameterJdbcTemplate namedParameterJdbcTemplate,final SqlRowSetMetaData sqlRowSetMetaData) throws   Exception{
		final StringBuffer res = new StringBuffer("");
		try{
		this.getJtA().batchUpdate(sql, new BatchPreparedStatementSetter()   {
			   public int getBatchSize() {
				    return list.size();    //这个方法设定更新记录数，通常List里面存放的都是我们要更新的，所以返回list.size()；
				   }
				 
				public void setValues (PreparedStatement ps,int i) throws SQLException   {
					Map map=(Map) list.get(i);
					MapSqlParameterSource mapsqlparam=new MapSqlParameterSource(map);
					Object[] objects=namedParameterJdbcTemplate.maptoObject(cansql, mapsqlparam);
					try {
					for(int j=0;j<objects.length;j++){
						Object midobj=objects[j];
						String typename=sqlRowSetMetaData.getColumnTypeName(j+1);
						if(midobj==null){
							ps.setObject(j+1,midobj, java.sql.Types.VARCHAR);
						}else{
						if(typename.toUpperCase().equals("TIMESTAMP")){
							if(midobj!=null){
								String date=midobj.toString();
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								Date date1=sdf.parse(date);
								Long longdate=date1.getTime();
								java.sql.Timestamp dateTime = new java.sql.Timestamp(longdate);
								ps.setTimestamp(j+1, dateTime);
							}
						}else if(typename.toUpperCase().equals("STRING")||typename.toUpperCase().equals("VARCHAR")||typename.toUpperCase().equals("VARCHAR2")){
							ps.setObject(j+1, midobj);
						}else if(typename.toUpperCase().equals("DATETIME")){
							if(midobj!=null){
								String date=midobj.toString();
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								Date date1=sdf.parse(date);
								Long longdate=date1.getTime();
								java.sql.Timestamp dateTime = new java.sql.Timestamp(longdate);
								ps.setTimestamp(j+1, dateTime);
							}
						}else if(typename.toUpperCase().equals("DATE")){	
							if(midobj!=null){
								String date=midobj.toString();
								SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
								Date date1=sdf.parse(date);
								Long longdate=date1.getTime();
								java.sql.Timestamp dateTime = new java.sql.Timestamp(longdate);
								ps.setTimestamp(j+1, dateTime);
							}
							
						}else{
							ps.setObject(j+1, midobj);
						}
						}
						
					}
					} catch (SQLException e) {
						
						res.append("1");
						//throw e;
					}catch(Exception e){
						res.append("2");
						
					}
					
					}
				  });
		
		}catch(Exception e){
			res.append("批量导入出现异常:查看批量导入的excel文件是否在批量导入配置中配置，错误：");
			res.append(e.getMessage());
			return res.toString();
		}
		return res.toString();
	}


	/**
	 * jxl读取excel读取的时间转换为真正时间
	 * @param date
	 */
	public void getLocaltime(Date date){
		String Dtz=TimeZone.getDefault().getID(); 
        //获去格林威治标准时间 
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT")); 
        //获去服务器时区时间 
        Calendar cal2 = new GregorianCalendar(TimeZone.getTimeZone(Dtz));
        int tz=0;
        tz=cal2.get(Calendar.HOUR_OF_DAY)-cal.get(Calendar.HOUR_OF_DAY); 
        long time = (date.getTime()) - Calendar.getInstance().getTimeZone().getRawOffset(); 
        date.setTime(time);

	}
	/**
	 * 验证时间格式 add by yanjiawei at 2010-9-16
	 * @param datetime
	 * @return
	 */
	public boolean checkDatetime(String datetime){
		boolean flag=true;
		datetime=datetime.replace("-", "/");
		datetime=datetime.trim();
		String format="";
		if(datetime.length()<=10){
			format="yyyy/MM/dd";
		}else if(datetime.length()<=13){
			format="yyyy/MM/dd HH";
		}else{
			format="yyyy/MM/dd HH:mm";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
			Date date=dateFormat.parse(datetime);
		} catch (ParseException e) {
			flag=false;
		}
		return flag;
	}
	// 重新提交更改后的excel数据
	public Map reSubExcelData(HttpServletRequest request) {
		Map<String, Object> vaildateMap = new HashMap<String, Object>();
		String msg = "";
		StringBuffer strSQL = new StringBuffer();

		try {
			String excelUploadGuid = (String) map.get("excel_upload_guid");
			String uploadLishiGuid = (String) map.get("upload_lishi_guid");
			String urlParaString = (String) map.get("urlPara");
			Map<String, String> urlParaMap = hashUrlPara(urlParaString);
			String cellNames = (String) map.get("cellnames");
			String[] cellNameStrings = cellNames.split(";");
			ExcelConf excelConf = getExcelConf(excelUploadGuid);
			String tableName = excelConf.bmcTemp;
			String zhujian = excelConf.zj;
			map.put("upload_lishi_guid", uploadLishiGuid);
			String inputSql = "";

			int rows = 0, cols = 0, lastRow = 0, thisRow = 0; // 读取数据源
			List<LinkedList> rowDataList = new LinkedList();
			LinkedList colDataList = new LinkedList();
			for (int i = 0; i < cellNameStrings.length; i++) {
				thisRow = Integer.parseInt(cellNameStrings[i].substring(
						cellNameStrings[i].indexOf("row_") + 4,
						cellNameStrings[i].indexOf("col_") - 1));
				if (thisRow != lastRow) {
					if (lastRow != 0) {
						rowDataList.add(colDataList);
					}
					lastRow = thisRow;
					if (colDataList.size() > cols) {
						cols = colDataList.size();
					}
					colDataList = new LinkedList();
				}
				colDataList.add((String) map.get(cellNameStrings[i]));
			}
			rowDataList.add(colDataList);
			rows = rowDataList.size();
			cols = colDataList.size();
			

			boolean isEmpty = true; // 判断一列是否为空，为空则结束

			Map<String, String> colTitleNameMap = new HashMap<String, String>();
			LinkedList colList = new LinkedList();
			LinkedList colNameList = new LinkedList();

			strSQL.delete(0, strSQL.length()); // 读取表头
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='colNum'");
			List<Map> tempList = this.getNpjtN().queryForList(
					strSQL.toString(), map);
			if (tempList != null) {
				Map tempMap = tempList.get(0);
				int colNum = 0;
				colNum = Integer.parseInt((String) tempMap.get("zhi"));
				for (int i = 0; i < colNum; i++) {
					strSQL.delete(0, strSQL.length());
					strSQL
							.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
									+ uploadLishiGuid
									+ "' and jian='colName_"
									+ i + "'");
					tempList = this.getNpjtN().queryForList(strSQL.toString(),
							map);
					String tempString = "";
					if (tempList != null&&tempList.size()>0) {
						tempMap = tempList.get(0);
						tempString = (String) tempMap.get("zhi");
						colTitleNameMap.put("colName_" + i, tempString);
						colList.add(excelConf.getzddy(tempString));//excel文件头对应的数据库字段LIST
						colNameList.add(tempString);
					}
				}
			}

			  

			Map  insertMap = new HashMap();		
			UserBean userbean = (UserBean) Cache.getUserInfo(yhid, "userbean");
			String yhid = userbean.getYhid();
			String bmid = userbean.getBmid();
			String gwid = userbean.getGwid();
			String zzid = userbean.getZzid();
			insertMap.put("yhid", yhid);
			insertMap.put("bmid", bmid);
			insertMap.put("gwid", gwid);
			insertMap.put("zzid", zzid);
			insertMap.putAll(urlParaMap);
			insertMap.put("upload_lishi_guid", uploadLishiGuid);
			if (!zhujian.equals("")) {
				String zhujianString = Guid.get();
				map.put(zhujian, zhujianString);
				insertMap.put(zhujian,zhujianString);
				
			}
			
			
			// 拼装sql
			// 拼装sql
			String urlColName = "", urlColValue = "";
			int colNameSqlNum = 0;
			
			String  insertCol = excelConf.zddy;
			String colName = "",colValue="";
	        String[] insert = insertCol.split(";");
	        for(int x=0;x<insert.length;x++){
	        	String[] in = insert[x].split(":");
	        	String col = ":"+in[0]+",";
	        	String col2 = in[1]+",";
	        	colValue = colValue.trim()+col;
	        	colName = colName.trim()+ col2;	        	
	        }
	        if (!zhujian.equals("")) {
	        	colName = colName +zhujian+",";
	        	colValue = colValue+":"+zhujian+",";
	        }
			
			strSQL.delete(0, strSQL.length());
			strSQL.append("insert into " + tableName + "(" + colName
					+ " upload_lishi_guid) values(" + colValue
					+ " :upload_lishi_guid)");
			
			
			String insertSql = strSQL.toString();
			
			inputSql = strSQL.toString();
			// 拼装sql结束
			
			
			
			//add by yan_wei 2010-11-29 提高excel导入效率，把从循环里读取数据库取值放到外面读取加载到内存，循环里读取内存map
			Map valueMap=getZdqzValue(excelConf.getzdqzMap());
			List<String> vaildateList = new LinkedList();
			boolean rowVildatePass = true; // 这一行所有列都通过验证
			Map<String, String> vildateTempMap = new HashMap<String, String>();
			List tgList=new ArrayList();
			for (int i = 0; i < rows; i++) { // 循环行
				boolean ifCall = false;
				HashMap callArgMap = new HashMap<String, Object>();
				rowVildatePass = true;
				vildateTempMap.clear();
				List<String> allVaildateList = new LinkedList();
				Set<String> callPathMethodSet = new LinkedHashSet<String>();
				isEmpty = true;
				LinkedList midtempList = rowDataList.get(i);

				int colNum = 0;
				String zhujianString = ""; // 保存主键
				Map m = new HashMap();
				for (int j = 0; j < cols; j++) { // 循环列
					boolean cellVildatePass = true; // 这一个单元格验证通过
					String cellString = (String) midtempList.get(j);
					if (!cellString.equals("")) {
						isEmpty = false;
					}
					String colTitleNameString = colTitleNameMap.get("colName_"
							+ j);
					if(colTitleNameString==null||colTitleNameString.trim().equals(""))continue;//如果列名为空就不再加入到数据库中
					String colTypeString = excelConf.getzdyzType(
							colTitleNameString).toLowerCase();
					int colLen = excelConf.getzdyzLen(colTitleNameString);
					//add by yanjiawei 2010-9-16 修改验证，使有取值的字段不在验证
					String excelColName = (String) colNameList.get(colNum);
					String excelCol = (String) colList.get(colNum);
					String sqlString = excelConf.getzdqz(excelColName);
					Map midvalueMap=(Map) valueMap.get(excelColName);//add by yan_jwei 2010-11-29根据名称读取该名称对应的map
					if (colLen != 0 && cellString.length() > colLen) {
						rowVildatePass = false;
						cellVildatePass = false;
					}
					if (sqlString.equals("")) {	// update by yanjiawei 先获取是否从表中取值，如果是则不验证该字段
						if (colTypeString.equals("int")) {
							try {
								int tempInt = Integer.parseInt(cellString);
							} catch (Exception e) {
								rowVildatePass = false;
								cellVildatePass = false;
							}
						} else if (colTypeString.equals("datetime")) {
							if(!checkDatetime(cellString)){//时间格式验证不通过
								rowVildatePass = false;
								cellVildatePass = false;
							}
						}else if(colTypeString.equals("float")){
							try{ 
								float   f=new   Float(cellString).floatValue(); 
								} 
								catch(NumberFormatException   e) 
								{ 
									rowVildatePass = false;
									cellVildatePass = false;
								} 

						}else if(colTypeString.equals("string")){
							
						}
					}
					m.put(colTitleNameString, cellString);
					m.putAll(insertMap);
					// 判断是否从表中取值
					String zdqzSql = excelConf.zdqz;
					
					List zdqzList = null;
					m.putAll(insertMap);
					Map zdqzMap = new HashMap();
					
					String[] zd=zdqzSql.split(";");
					for(int y=0;y<zd.length;y++){
						
						String zdSql = zd[y];
						if(!"".equals(zdSql)&&zdSql!=null){
							zdqzList = this.getNpjtN().queryForList(zdSql, m);
							if(zdqzList.size()==1){
								zdqzMap = (Map)zdqzList.get(0);
							
								m.putAll(zdqzMap);
							}else if(zdqzList.size()<=0){
								//给字段取值取出来的值赋NULL值
								try{
								String s = zdSql.substring(zdSql.indexOf("select")+"select".length(), zdSql.indexOf("from"));									
								String[] ss = s.split(",");									
								for(int mm=0;mm<ss.length;mm++){
									String sss = ss[mm];										
									String[] ssss = sss.split("as");
									String s1 = ssss[1].trim();
									m.put(s1, null);												
								}
								}catch(Exception e){
									throw new RuntimeException("字段取值的sql是否正确配置，包含了AS？");
								}
							}
							else{
								throw new RuntimeException("字段取值返回结果条数不能超过1条");						
							}
						}
					}	
					// 判断是否从表中取值 结束
					if (!cellVildatePass) { // 本单元格验证没通过
						allVaildateList.add("error_row_" + i + "_col_" + j);
					} else {
						allVaildateList.add("row_" + i + "_col_" + j);
					}
					vildateTempMap.put("row_" + i + "_col_" + j, cellString);
				} // 循环列结束

				if (isEmpty) { // 如果一行为空，则结束
					break;
				}
				if (!rowVildatePass) { // 本行验证没通过
					for (String keyString : allVaildateList) {
						vaildateList.add(keyString);
					}
					vaildateMap.putAll(vildateTempMap);
				} else { // 通过，插入到数据库
					try {
						if (!zhujian.equals("")) {
							zhujianString = Guid.get();
							map.put(zhujian, zhujianString);
							m.put(zhujian,zhujianString);
							callArgMap.put("guid", zhujianString);
						}
						tgList.add(((HashMap)m).clone());//add by yan_jwei 把通过验证的数据存放到list，验证完后批量插入数据库

						/*if (ifCall) {
							for (String keyString : callPathMethodSet) {
								try {
									callMethod(keyString.split(";")[0],
											keyString.split(";")[1], callArgMap);
								} catch (Exception e) {
									callMethodError(keyString.split(";")[0],
											keyString.split(";")[1] + "Error",
											uploadLishiGuid);
								}
							}
						}*/
					} catch (Exception e) {
						for (String keyString : allVaildateList) {
							keyString = keyString.startsWith("error_") ? keyString
									: "error_" + keyString;
							vaildateList.add(keyString);
						}
						vaildateMap.putAll(vildateTempMap);
					}
				}
			}
			
			vaildateMap.put("vaildateList", vaildateList);
			vaildateMap.put("colNum", cols);
			batchInsert(tgList,inputSql);// add by yan_jwei 2010-11-29 验证通过的数据批量导入临时表
			String fieldstr=getTableFields(excelConf.bmc);
			if (vaildateList == null || vaildateList.size() < 1) { // 验证通过
				
				
				//都执行完以后执行业务方法，
				
					String ywff =excelConf.ffyz;
					if(ywff!=null&&!"".equals(ywff)){
						String[] yw = ywff.split(";");
						for(int x=0;x<yw.length;x++){
							String c = yw[x];
							String cpath = c.split(":")[0];
							String cMehthod = c.split(":")[1];
							callYWMethod(cpath,
									cMehthod, tgList);
						}
					}
					
							
				strSQL.delete(0, strSQL.length());
				strSQL.append("insert into " + excelConf.bmc
						+ " select "+fieldstr +" from " + excelConf.bmcTemp +" where upload_lishi_guid='"+uploadLishiGuid+"'");
				this.getNpjtN().update(strSQL.toString(), map);
				clearData(uploadLishiGuid, 1, excelConf);
			}
			map.put("resultMap", vaildateMap);
			map.put("excel_upload_guid", excelUploadGuid); // 配置的主键
			map.put("upload_lishi_guid", uploadLishiGuid); // 历史的主键
			map.put("urlPara", urlParaString);
			
			//oplog.info("批量导入重新提交数据成功");
		
			msg = "保存成功";
			map.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
			String message=new StringBuffer().append("批量导入重新提交数据时出现异常:").append(e.toString()).toString();
	        //log.error(message);
			msg = "保存失败";
			map.put("msg", msg);
		}
		return map;
	}
	
	/*// 重新提交更改后的excel数据
	public Map reSubExcelData(HttpServletRequest request) {
		Map<String, Object> vaildateMap = new HashMap<String, Object>();
		String msg = "";
		StringBuffer strSQL = new StringBuffer();
		String excelUploadGuid = (String) map.get("excel_upload_guid");
		String cellNames = (String) map.get("cellnames");
		String[] cellNameStrings = cellNames.split(";");
		ExcelConf excelConf = getExcelConf(excelUploadGuid);
		String tableName = excelConf.bmcTemp;
		String urlParaString = (String) map.get("urlPara");
		String uploadLishiGuid = (String) map.get("upload_lishi_guid");
		try {
			
			
			
						
			
			Enumeration eun =  request.getParameterNames();
			
			List list = new ArrayList();
			List nlist = new ArrayList();
			List insertList = new ArrayList();
			for (Enumeration e = eun ; e.hasMoreElements() ;) {
				
		         String thisName=e.nextElement().toString();
		         nlist.add(thisName);
		         String[] values = request.getParameterValues(thisName);
		        // list.add(values);
		         for(int x =0;x<values.length;x++){
		        	  list.add(values[x]);
		        	  System.out.println(thisName+"-------"+values[x]);
		         }
		        String thisValue=request.getParameter(thisName);
		        
		        System.out.println(thisName+"-------"+thisValue);
		}
			Map  insertMap = new HashMap();	
			int xx = nlist.size();
			int yy = (list.size())/xx;
			List validateList = new ArrayList();
			List errorList = new ArrayList();
			Map vmap = new HashMap();	
			for(int m=0;m<yy;m++){
				Map map = new HashMap();
							
				Map<String, String> urlParaMap = hashUrlPara(urlParaString);					
				UserBean userbean = (UserBean) Cache.getUserInfo(yhid, "userbean");
				String yhid = userbean.getYhid();
				String bmid = userbean.getBmid();
				String gwid = userbean.getGwid();
				String zzid = userbean.getZzid();
				insertMap.put("yhid", yhid);
				insertMap.put("bmid", bmid);
				insertMap.put("gwid", gwid);
				insertMap.put("zzid", zzid);
				insertMap.putAll(urlParaMap);
				insertMap.put("upload_lishi_guid", uploadLishiGuid);
				String zhujian = excelConf.zj;		
				
				
				String zdqzSql = excelConf.zdqz;
				
				if (!zhujian.equals("")) {
					String zhujianString = Guid.get();				
					insertMap.put(zhujian,zhujianString);					
				}								
				for(int n=0;n<xx;n++){	
					
					
					map.put(nlist.get(n), list.get(n+xx));
				   
				}
				 map.putAll(insertMap);
				
				
				List zdqzList = null;
				
				Map zdqzMap = new HashMap();
				
				String[] zd=zdqzSql.split(";");
				for(int y=0;y<zd.length;y++){
					
					String zdSql = zd[y];
					if(!"".equals(zdSql)&&zdSql!=null){
						zdqzList = this.getNpjtN().queryForList(zdSql, map);
						if(zdqzList.size()==1){
							zdqzMap = (Map)zdqzList.get(0);
						
							map.putAll(zdqzMap);
						}else if(zdqzList.size()<=0){
							//给字段取值取出来的值赋NULL值
							try{
							String s = zdSql.substring(zdSql.indexOf("select")+"select".length(), zdSql.indexOf("from"));									
							String[] ss = s.split(",");									
							for(int mm=0;mm<ss.length;mm++){
								String sss = ss[mm];										
								String[] ssss = sss.split("as");
								String s1 = ssss[1].trim();
								map.put(s1, null);												
							}
							}catch(Exception e){
								throw new RuntimeException("字段取值的sql是否正确配置，包含了AS？");
							}
						}
						else{
							throw new RuntimeException("字段取值返回结果条数不能超过1条");						
						}
					}
				}	
				
				
				
				
				
				
				
			
				insertList.add(map);
				validateList.add(vmap);
			}	
				
			String  insertCol = excelConf.zddy;
			String colName = "",colValue="";
	        String[] insert = insertCol.split(";");
	        for(int x=0;x<insert.length;x++){
	        	String[] in = insert[x].split(":");
	        	String col = ":"+in[0]+",";
	        	String col2 = in[1]+",";
	        	colValue = colValue.trim()+col;
	        	colName = colName.trim()+ col2;	        	
	        }
	        String zhujian = excelConf.zj;			
	        if (!zhujian.equals("")) {
	        	colName = colName +zhujian+",";
	        	colValue = colValue+":"+zhujian+",";
	        }
			
			strSQL.delete(0, strSQL.length());
			strSQL.append("insert into " + tableName + "(" + colName
					+ " upload_lishi_guid) values(" + colValue
					+ " :upload_lishi_guid)");
						
			String insertSql = strSQL.toString();		
			
			
			
			
			
			
			
	
			for(int j=0;j<insertList.size();j++){
				Map<String,String> map2 = (Map)insertList.get(j);
				boolean checkOK= true;
				Map errorMap = new HashMap();
				List collist = this.getJtN().
	            queryForList("select jian,zhi from t_excel_upload_temp  where upload_lishi_guid = '"+uploadLishiGuid+"' and jian !='colNum'");
				if(collist.size()>0){
				    for(int xy=0;xy<collist.size();xy++){
				    	Map colMap = (Map)collist.get(xy);
				    	String key = (String)colMap.get("jian");
				    	String value =  map2.get(key);
				    	errorMap.put(key, value);
				    	String colTypeString = excelConf.getzdyzType(
								key).toLowerCase();
						int colLen = excelConf.getzdyzLen(key);
						if (colLen != 0 && value.length() > colLen) {
							checkOK = false;
						}
							if (colTypeString.equals("int")) {
								try {
									int tempInt = Integer.parseInt(key);
								} catch (Exception e) {
									checkOK = false;
								}
							} else if (colTypeString.equals("datetime")) {
								if(!checkDatetime(key)){//时间格式验证不通过
									checkOK = false;
								}
							}else if(colTypeString.equals("float")){
								try{ 
									float   f=new   Float(key).floatValue(); 
									} 
									catch(NumberFormatException   e) 
									{ 
										checkOK = false;
									} 

							}else if(colTypeString.equals("string")){
								
							}
				    }
				    if(checkOK==false){
				    	  Map newMap = new HashMap();
				    	  newMap.putAll(errorMap);
						  errorList.add(newMap);
						}
					
				}
		
			}
			
			batchInsert(insertList,insertSql);// add by yan_jwei 2010-11-29 验证通过的数据批量导入临时表
			vaildateMap.put("vaildateList", errorList);
			if (errorList == null || errorList.size() < 1) { // 验证通过
				strSQL.delete(0, strSQL.length());
				strSQL.append("insert into " + excelConf.bmc
						+ " select * from " + excelConf.bmcTemp);
				this.getNpjtN().update(strSQL.toString(), map);
				clearData(uploadLishiGuid, 1, excelConf);
			}else{
				List collist = this.getJtN().
				                queryForList("select jian,zhi from t_excel_upload_temp  where upload_lishi_guid = '"+uploadLishiGuid+"' and jian !='colNum'");
				if(collist.size()>0){
					for(int xy=0;xy<collist.size();xy++){
						Map map = (Map)collist.get(xy);
						String name =(String)map.get("jian");
						for(int mn=0;mn<errorList.size();mn++){
							Map<String,String> mm = (Map)errorList.get(mn);
							for(String k:mm.keySet()){
								
							}
						}
					}
				}
			}
			vaildateMap.put("colNum", errorList.size());
			map.put("resultMap", vaildateMap);
			map.put("excel_upload_guid", excelUploadGuid); // 配置的主键
			map.put("upload_lishi_guid", uploadLishiGuid); // 历史的主键
			map.put("urlPara", urlParaString);
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "保存失败";
		}
		return map;
	}*/
/**
 * 	 把通过字段取值的sql，通过查询数据库都加载到内存中
 * @param zdqzMap
 * @return
 */
public Map getZdqzValue(Map zdqzMap){
	Map reMap=new HashMap();
	if(zdqzMap!=null&&!zdqzMap.isEmpty()){
		Iterator it=zdqzMap.keySet().iterator();
		while(it.hasNext()){
			String midkey=(String) it.next();
			String sql=zdqzMap.get(midkey)==null?"":zdqzMap.get(midkey).toString();
			reMap.put(midkey,queryZdqzSql(sql));
		}
		
	}
	return reMap;
	
}
/**
 * 把根据占位符查询sql，去掉该条件，查询所有的条件。最好只有一个@
 * @param sql
 * @return
 */
public Map queryZdqzSql(String sql){
	Map reMap=new HashMap();
	if(sql!=null&&!sql.equals("")){
		int ind=sql.indexOf("@");
		int end=sql.substring(ind).indexOf(" ");
		int start=sql.substring(0,ind).lastIndexOf(" ");
		if(end==-1){
			end=ind+3;
		}else{
			end=ind+end;
		}
		
		String substr=sql.substring(start,end);
		sql=sql.replace(substr, " (1=1) ");
		String key=substr;
		
		if(key.indexOf(".")!=-1) key=key.substring(key.indexOf(".")+1).trim();
		if(key.indexOf("=")!=-1)key=key.substring(0,key.indexOf("=")).trim();
		List list=this.getNpjtN().queryForList(sql, map);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				Map  midobj=(Map)list.get(i);
				if(midobj.get(key)!=null){
					reMap.put(midobj.get(key), midobj);
				}
			}
		}
	}
	return reMap;
}
	// 提交以前的数据
	public Map subAgoData(HttpServletRequest request) {
		String msg = "";
		Map reMap = new HashMap();

		try {
			String excelUploadGuid = (String) map.get("excel_upload_guid");
			String uploadLishiGuid = (String) map.get("upload_lishi_guid");
			String urlParaString = (String) map.get("urlPara");
			Map urlParaMap = hashUrlPara(urlParaString);
			String cellNames = (String) map.get("cellnames");
			String[] cellNameStrings = cellNames.split(";");
			StringBuffer sqlStr = new StringBuffer();

			Map<String, String> skipMap = new HashMap<String, String>();
			for (int i = 0; i < cellNameStrings.length; i++) {
				if (cellNameStrings[i].equals("")) {
					continue;
				}
				skipMap.put(cellNameStrings[i], "");
			}

			ExcelConf excelConf = getExcelConf(excelUploadGuid);

			sqlStr.delete(0, sqlStr.length());
			
			String fieldstr=getTableFields(excelConf.bmc);
			
			sqlStr.append("insert into " + excelConf.bmc + " select "+fieldstr +" from " + excelConf.bmcTemp);
			this.getNpjtN().update(sqlStr.toString(), map);
			String sql = "select *  from " + excelConf.bmcTemp;
			List tgList = this.getJtN().queryForList(sql);
			String ywff =excelConf.ffyz;
			if(ywff!=null&&!"".equals(ywff)){
				String[] yw = ywff.split(";");
				for(int x=0;x<yw.length;x++){
					String c = yw[x];
					String cpath = c.split(":")[0];
					String cMehthod = c.split(":")[1];
					callYWMethod(cpath,
							cMehthod, tgList);
				}
			}

			sqlStr.delete(0, sqlStr.length());
			sqlStr.append("delete from " + excelConf.bmcTemp
					+ " where upload_lishi_guid='" + uploadLishiGuid + "'");
			this.getNpjtN().update(sqlStr.toString(), map);

			reMap.put("vaildateList", new ArrayList());
			reMap.put("colNum", 0);
			map.put("resultMap", reMap);
			map.put("excel_upload_guid", excelUploadGuid); // 配置的主键
			map.put("upload_lishi_guid", uploadLishiGuid); // 历史的主键
			map.put("urlPara", urlParaString);
			
			
			
			

			clearData(uploadLishiGuid, 2, excelConf);
			map.put("cellnames", "");
			//log.info("批量导入提交成功数据成功");
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			String message=new StringBuffer().append("批量导入提交成功数据时出现异常:").append(e.toString()).toString();
	        //log.error(message);
			msg = "保存失败";
		}
		return map;
	}

	// 取消以前提交的数据
	public Map cancelData(HttpServletRequest request) {
		String msg = "";
		Map reMap = new HashMap();

		try {
			String excelUploadGuid = (String) map.get("excel_upload_guid");
			String uploadLishiGuid = (String) map.get("upload_lishi_guid");

			reMap.put("vaildateList", new ArrayList());
			reMap.put("colNum", 0);
			map.put("resultMap", reMap);
			map.put("excel_upload_guid", excelUploadGuid); // 配置的主键
			map.put("upload_lishi_guid", uploadLishiGuid); // 历史的主键

			ExcelConf excelConf = getExcelConf(excelUploadGuid);

			clearData(uploadLishiGuid, 3, excelConf);
			map.put("cellnames", "");
			//log.info("批量导入取消以前提交数据成功");
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			String message=new StringBuffer().append("批量导入取消以前提交成功的数据时出现异常:").append(e.toString()).toString();
	        //log.error(message);
			msg = "保存失败";
		}
		return map;
	}

	/**
	 * 把参数映射成map
	 * 
	 * @param urlPara
	 * @return
	 */
	public Map<String, String> hashUrlPara(String urlPara) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (urlPara == null || urlPara.equals("")) {
			return resultMap;
		}
		String[] zuStrings = urlPara.split(";:;");
		for (int i = 0; i < zuStrings.length; i++) {
			String[] tempStrings = zuStrings[i].split("=");
			if (tempStrings.length < 2) {
				tempStrings = new String[2];
				tempStrings[0] = zuStrings[i].split("=")[0];
				if (tempStrings[0].equals("")) {
					continue;
				}
				tempStrings[1] = "";
			}
			resultMap.put(tempStrings[0], tempStrings[1]);
		}
		return resultMap;
	}

	/**
	 * 调用业务方法
	 * 
	 * @param classPath
	 *            类路径
	 * @param classMethod
	 *            方法名称
	 * @param argMap
	 *            参数（数据库记录）
	 */
	void callMethod(String classPath, String classMethod,
			HashMap<String, Object> argMap) throws Exception {
		Class classType = Class.forName(classPath);
		Object object = classType.newInstance();
		Object[] argsObject = new Object[1];
		argsObject[0] = argMap;
		Class[] argsClass = new Class[argsObject.length];
		for (int i = 0, j = argsObject.length; i < j; i++) {
			argsClass[i] = argsObject[i].getClass();
		}
		Method method = classType.getMethod(classMethod, new Class[] { Object[].class });
		method.invoke(object, argsObject);
	}
	/**
	 * 调用业务方法
	 * 
	 * @param classPath
	 *            类路径
	 * @param classMethod
	 *            方法名称
	 * @param argMap
	 *            参数（数据库记录）
	 */
	void callYWMethod(String classPath, String classMethod,
			List list) throws Exception {
		Class classType = Class.forName(classPath);
		Object object = classType.newInstance();
		/*Object[] argsObject = new Object[1];
		argsObject[0] = list;
		Class[] argsClass = new Class[argsObject.length];
		for (int i = 0, j = argsObject.length; i < j; i++) {
			argsClass[i] = argsObject[i].getClass();
		}*/
		Method method = classType.getMethod(classMethod, new Class[] { List.class });
		method.invoke(object, list);
	}
	/**
	 * 发生异常的时候调用业务方法
	 * 
	 * @param classPath
	 *            类路径
	 * @param classMethod
	 *            方法名称
	 * @param argMap
	 *            参数（数据库记录）
	 */
	void callMethodError(String classPath, String classMethod, String argString)
			throws Exception {
		Class classType = Class.forName(classPath);
		Object object = classType.newInstance();
		Object[] argsObject = new Object[1];
		argsObject[0] = argString;
		Class[] argsClass = new Class[argsObject.length];
		for (int i = 0, j = argsObject.length; i < j; i++) {
			argsClass[i] = argsObject[i].getClass();
		}
		Method method = classType.getMethod(classMethod, argsClass);
		method.invoke(object, argsObject);
	}

	/**
	 * 清空上传的记录
	 * 
	 * @param uploadLishiGuid
	 * @param hisState
	 *            提交状态：1成功插入，2部分插入，3取消提交
	 */
	void clearData(String uploadLishiGuid, int hisState, ExcelConf excelConf) throws Exception {
		//try {
			
			StringBuffer strSql = new StringBuffer();
			strSql.append(" select count(*) as total from  " + excelConf.bmc
					+ " where upload_lishi_guid='" + uploadLishiGuid + "'");
			Map map=this.getNpjtN().queryForMap(strSql.toString(),new HashMap());
			int tgtotal=0;
			if(map!=null){
				tgtotal=Integer.parseInt(map.get("total")==null?"0":map.get("total").toString());
			}
			map=null;
			strSql.delete(0, strSql.length());
			strSql.append(" select *  from  t_excel_upload_his where guid='" + uploadLishiGuid + "'");
			map=this.getNpjtN().queryForMap(strSql.toString(),new HashMap());
			int total=0;
			if(map!=null){
				total=Integer.parseInt(map.get("zts")==null?"0":map.get("zts").toString());
			}
			int sbts=total-tgtotal;
			strSql.delete(0, strSql.length());
			strSql.append("update t_excel_upload_his set his_state=" + hisState
					+ ", cgts="+tgtotal+",sbts="+sbts+" where guid='" + uploadLishiGuid + "'");
			this.getNpjtN().update(strSql.toString(), map);

			strSql.delete(0, strSql.length());
			strSql.append("delete from t_excel_upload_temp where yhid='" + yhid
					+ "'");
			this.getNpjtN().update(strSql.toString(), map);

			strSql.delete(0, strSql.length());
			strSql
					.append("delete from "
							+ excelConf.bmcTemp
							+ " where upload_lishi_guid in (select guid from t_excel_upload_his where yhid='"
							+ yhid + "')");
			this.getNpjtN().update(strSql.toString(), map);

			String saveDirectory = SysPara.getValue("excel_upload_path");
			File excelFile = new File(saveDirectory + "\\" + uploadLishiGuid
					+ ".xls");
			if (excelFile.exists()) {
				excelFile.delete();
			}
		//} 
	}

	// 配置类
	class ExcelConf {
		int ksh = 0; // 开始行
		int jsh = 0; // 结束行
		int ksl = 0; // 开始列
		int jsl = 0; // 结束列
		String bmc = ""; // 表名称
		String bmcTemp = ""; // 临时表名称后缀“_excel_temp”
		String zddy = ""; // 文件名称和数据库字段对应关系 // zimu:dzimu;shuzi:dshuzi
		String zdyz = ""; // 文件字段的验证约束 // zimu:string(10);shuzi:int(11)
		String zdqz = ""; // 文件里面的值从数据库相应的表取值 // 民族::select dm from t_dm where
							// mc='{@}';;
		String ffyz = ""; // 业务方法处理 //业务:com.coffice.util.excel.TestFS.method;
		String zj = ""; // 主键
		// int yxfg = 0; // 允许覆盖

		Map<String, String> zddyMap = null;
		Map<String, String> zdyzTypeMap = null;
		Map<String, Integer> zdyzLenMap = null;
		Map<String, String> zdqzMap = null;
		Map<String, String> zdqzkeyMap = null;
		Map<String, String> ffyzMap = null;

		public String getzddy(String fileColName) { // 文件名称和数据库字段对应关系
			if (zddyMap == null) {
				zddyMap = new HashMap<String, String>();
				String[] temp = zddy.split(";");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split(":");
					if (nameStrings.length > 1) {
						zddyMap.put(nameStrings[0], nameStrings[1]);
					}
				}
			}
			String dy = zddyMap.get(fileColName);
			return dy;
		}

		public String getzdyzType(String fileColName) { // 字段验证类型
			creatYzMap();
			String yzTypeString = "";
			if (zdyzTypeMap.containsKey(fileColName)) {
				yzTypeString = zdyzTypeMap.get(fileColName);
			}
			return yzTypeString;
		}

		public int getzdyzLen(String fileColName) { // 字段验证长度
			creatYzMap();
			int yzLen = 0;
			if (zdyzLenMap.containsKey(fileColName)) {
				yzLen = zdyzLenMap.get(fileColName);
			}
			return yzLen;
		}

		public void creatYzMap() {
			if (zdyzTypeMap == null) {
				zdyzTypeMap = new HashMap<String, String>();
				zdyzLenMap = new HashMap<String, Integer>();
				String[] temp = zdyz.split(";");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split(":");
					if (nameStrings.length > 1) {
						String typeString = nameStrings[1]
								.substring(
										0,
										nameStrings[1].indexOf("(") > -1 ? nameStrings[1]
												.indexOf("(")
												: nameStrings[1].length());
						int len = 0;
						if (nameStrings[1].indexOf("(") > -1
								&& nameStrings[1].indexOf(")") > -1) {
							String lenString = nameStrings[1].substring(
									nameStrings[1].indexOf("(") + 1,
									nameStrings[1].indexOf(")"));
							try {
								len = Integer.parseInt(lenString);
								zdyzLenMap.put(nameStrings[0], len);
							} catch (Exception e) {
							}
							zdyzTypeMap.put(nameStrings[0], nameStrings[1]
									.substring(0, nameStrings[1].indexOf("(")));
						} else {
							zdyzTypeMap.put(nameStrings[0], nameStrings[1]);
						}
					}
				}
			}
		}

		public String getzdqz(String fileColName) { // 文件名和sql对应关系
			if (zdqzMap == null) {
				zdqzMap = new HashMap<String, String>();
				String[] temp = zdqz.split(";;");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split("::");
					if (nameStrings.length > 1) {
						zdqzMap.put(nameStrings[0], nameStrings[1]);
					}
				}
			}
			if (!zdqzMap.containsKey(fileColName)) {
				return "";
			}
			return zdqzMap.get(fileColName);
		}
		
		public Map getzdqzMap(){
			if (zdqzMap == null) {
				zdqzMap = new HashMap<String, String>();
				String[] temp = zdqz.split(";;");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split("::");
					if (nameStrings.length > 1) {
						zdqzMap.put(nameStrings[0].trim(), nameStrings[1]+" ");
					}
				}
			}
			return zdqzMap==null?new HashMap():zdqzMap;
		}
		
		public Map getzdqzkeyMap(){
			if (zdqzkeyMap == null) {
				zdqzkeyMap = new HashMap<String, String>();
				String[] temp = zdqz.split(";;");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split("::");
					if (nameStrings.length > 2) {
						zdqzkeyMap.put(nameStrings[0], nameStrings[2]);
					}
				}
			}
			return zdqzkeyMap==null?new HashMap():zdqzkeyMap;
		}

		public String getffyzType(String fileColName) { // 取得字段相关的类路径
			if (ffyzMap == null) {
				ffyzMap = new HashMap<String, String>();
				String[] temp = ffyz.split(";");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split(":");
					if (nameStrings.length > 1) {
						ffyzMap.put(nameStrings[0], nameStrings[1]);
					}
				}
			}
			if (!ffyzMap.containsKey(fileColName)) {
				return "";
			}
			String classPathString = ffyzMap.get(fileColName);
			try {
				classPathString = classPathString.substring(0, classPathString
						.lastIndexOf("."));
			} catch (Exception e) {
				return "";
			}
			return classPathString;
		}

		public String getffyzMethod(String fileColName) { // 取得字段相关的类方法
			if (ffyzMap == null) {
				ffyzMap = new HashMap<String, String>();
				String[] temp = ffyz.split(";");
				for (int i = 0; i < temp.length; i++) {
					String[] nameStrings = temp[i].split(":");
					if (nameStrings.length > 1) {
						ffyzMap.put(nameStrings[0], nameStrings[1]);
					}
				}
			}
			if (!ffyzMap.containsKey(fileColName)) {
				return "";
			}
			String classPathString = ffyzMap.get(fileColName);
			try {
				classPathString = classPathString.substring(classPathString
						.lastIndexOf(".") + 1, classPathString.length());
			} catch (Exception e) {
				return "";
			}
			return classPathString;
		}

		// 判断是否允许覆盖
		// public boolean allowCover() {
		// return yxfg == 1 ? true : false;
		// }

		// 是否调用业务方法
		public boolean allowCall() {
			return ffyz.equals("") ? false : true;
		}
	}

	/**
	 * 读取配置
	 * 
	 * @param uploadGuid
	 */
	public ExcelConf getExcelConf(String uploadGuid) {
		ExcelConf excelConf = new ExcelConf();

		try {
			StringBuffer strSQL = new StringBuffer();
			strSQL.append("select * from t_excel_upload where guid='"
					+ uploadGuid + "'");
			Map rsMap = this.getNpjtN().queryForMap(strSQL.toString(), map);

			excelConf.bmc = (String) ((String) rsMap.get("bmc")==null?"":rsMap.get("bmc"));
			excelConf.bmcTemp = excelConf.bmc + "_excel_temp";
			excelConf.zddy = (String) ((String) rsMap.get("zddy")==null?"":rsMap.get("zddy"));
			excelConf.zdyz = (String) ((String) rsMap.get("zdyz")==null?"":rsMap.get("zdyz"));
			excelConf.zdqz = (String) ((String) rsMap.get("zdqz")==null?"":rsMap.get("zdqz"));
			excelConf.zj = (String) ((String) rsMap.get("zj")==null?"":rsMap.get("zj"));
			excelConf.ffyz = (String) ((String) rsMap.get("ffyz")==null?"":rsMap.get("ffyz"));
			excelConf.ksh = Integer.parseInt(rsMap.get("ksh").toString());
			excelConf.jsh = Integer.parseInt(rsMap.get("jsh").toString());
			excelConf.ksl = Integer.parseInt(rsMap.get("ksl").toString());
			excelConf.jsl = Integer.parseInt(rsMap.get("jsl").toString());
			// excelConf.yxfg = Integer.parseInt( rsMap.get("yxfg").toString()
			// );

		} catch (Exception e) {
			e.printStackTrace();
			String guid = Guid.get();
			logItem.setMethod("list");
			logItem.setLogid(guid);
			logItem.setLevel("info");
			logItem.setDesc("读取配置时出现异常4");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ExcelUpload.query异常:" + e.toString());
		}
		return excelConf;
	}

	public final static java.sql.Timestamp stringToTimestamp(String dateString)
			throws java.lang.Exception {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());
		return dateTime;
	}

	public final static java.sql.Timestamp stringToTimestampss(String dateString)
			throws java.lang.Exception {
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		dateFormat.setLenient(false);
		java.util.Date timeDate = dateFormat.parse(dateString);
		java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());
		return dateTime;
	}

	/**
	 * 建立每一级的目录
	 */
	private boolean creatDir(String path) {
		String[] dirs = path.split("/");
		String temppath = "";
		File file = null;
		boolean ifCreatDir = false; // 是否建好目录

		try {
			for (int filei = 0; filei < dirs.length; filei++) {
				temppath += dirs[filei] + "/";
				// filePath = filePath.toString();
				file = new java.io.File(temppath);
				if (!file.exists())
					file.mkdir();
			}
			ifCreatDir = true; // 已经建立好目录
		} catch (Exception e) {
			e.printStackTrace();
			ifCreatDir = false; // 建立目录失败
		}
		return ifCreatDir;
	}

	/**
	 * 获取当前时间
	 */
	private String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		return curTime;
	}

	// *************暂时取消的方法

	// 重新提交更改后的excel数据
	public Map reSubExcelDataBak(HttpServletRequest request) {
		String msg = "";
		Map reMap = null;

		try {
			String excelUploadGuid = (String) map.get("excel_upload_guid");
			String uploadLishiGuid = (String) map.get("upload_lishi_guid");
			String urlPara = (String) map.get("urlPara");
			String cellNames = (String) map.get("cellnames");
			String[] cellNameStrings = cellNames.split(";");
			StringBuffer sqlStr = new StringBuffer();

			for (int i = 0; i < cellNameStrings.length; i++) {
				if (cellNameStrings[i].equals("")) {
					continue;
				}
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("update t_excel_upload_temp set zhi=:"
						+ cellNameStrings[i] + " where upload_lishi_guid='"
						+ uploadLishiGuid + "' and jian='" + cellNameStrings[i]
						+ "' and yhid='" + yhid + "'");
				this.getNpjtN().update(sqlStr.toString(), map);
			}

			ExcelConf excelConf = getExcelConf(excelUploadGuid);

			reMap = vaildateData(uploadLishiGuid, excelConf);

			List tempList = (List) reMap.get("vaildateList");
			if (tempList == null || tempList.size() < 1) { // 验证通过
				Map tempMap = hashUrlPara(urlPara);
				putTableData(uploadLishiGuid, new HashMap<String, String>(),
						tempMap, excelConf);
			}

			map.put("resultMap", reMap);
			map.put("excel_upload_guid", excelUploadGuid); // 配置的主键
			map.put("upload_lishi_guid", uploadLishiGuid); // 历史的主键
			map.put("urlPara", urlPara);

			List vaildateList = (List) reMap.get("vaildateList");
			if (vaildateList == null || vaildateList.size() < 1) { // 验证通过
				clearData(uploadLishiGuid, 1, excelConf);
			}
			msg = "保存成功";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "保存失败";
		}
		return map;
	}

	class LishiConf { // 取历史的开始结束行和列
		int startRow = 0;
		int endRow = 0;
		int startCol = 0;
		int endCol = 0;
		int colNum = 0;
	}

	/**
	 * 读取历史配置
	 * 
	 * @param uploadGuid
	 */
	public LishiConf getlishiConf(String uploadLishiGuid) {
		LishiConf lishiConf = new LishiConf();

		try {
			String startRowString = "0";
			String endRowString = "0";
			String startColString = "0";
			String endColString = "0";
			String colNumString = "0";
			int startRow = 0;
			int endRow = 0;
			int startCol = 0;
			int endCol = 0;
			int colNum = 0;

			StringBuffer strSQL = new StringBuffer();
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='startRow'");
			List<Map> tempList = this.getNpjtN().queryForList(
					strSQL.toString(), map);
			Map tempMap = null;
			if (tempList != null) {
				tempMap = tempList.get(0);
				startRowString = (String) tempMap.get("zhi");
				startRow = Integer.parseInt(startRowString);
			}
			strSQL.delete(0, strSQL.length());
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='endRow'");
			tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
			if (tempList != null) {
				tempMap = tempList.get(0);
				endRowString = (String) tempMap.get("zhi");
				endRow = Integer.parseInt(endRowString);
			}
			strSQL.delete(0, strSQL.length());
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='startCol'");
			tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
			if (tempList != null) {
				tempMap = tempList.get(0);
				startColString = (String) tempMap.get("zhi");
				startCol = Integer.parseInt(startColString);
			}
			strSQL.delete(0, strSQL.length());
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='endCol'");
			tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
			if (tempList != null) {
				tempMap = tempList.get(0);
				endColString = (String) tempMap.get("zhi");
				endCol = Integer.parseInt(endColString);
			}

			strSQL.delete(0, strSQL.length());
			strSQL
					.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
							+ uploadLishiGuid + "' and jian='colNum'");
			tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
			if (tempList != null) {
				tempMap = tempList.get(0);
				colNumString = (String) tempMap.get("zhi");
				colNum = Integer.parseInt(colNumString);
			}

			lishiConf.startRow = startRow;
			lishiConf.endRow = endRow;
			lishiConf.startCol = startCol;
			lishiConf.endCol = endCol;
			lishiConf.colNum = colNum;
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("list");
			logItem.setLogid(guid);
			logItem.setLevel("info");
			logItem.setDesc("读取历史配置时出现异常4");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ExcelUpload.query异常:" + e.toString());
		}
		return lishiConf;
	}

	/**
	 * 验证字段约束
	 * 
	 * @param uploadLishiGuid
	 *            历史表的主键
	 * @param excelConf
	 *            配置数据
	 * @return
	 */
	public Map vaildateData(String uploadLishiGuid, ExcelConf excelConf) {
		StringBuffer strSQL = new StringBuffer();
		Map<String, Object> vaildateMap = new HashMap<String, Object>();

		try {
			Map tempMap = null;
			List<Map> tempList = null;

			LishiConf lishiConf = getlishiConf(uploadLishiGuid);

			int startRow = lishiConf.startRow;
			int endRow = lishiConf.endRow;
			int startCol = lishiConf.startCol;
			int endCol = lishiConf.endCol;
			int colNum = lishiConf.colNum;

			String colNumString = "";
			Map<String, String> colTitleNameMap = new HashMap<String, String>();
			for (int i = 0; i < colNum; i++) {
				strSQL.delete(0, strSQL.length());
				strSQL
						.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
								+ uploadLishiGuid
								+ "' and jian='colName_"
								+ i
								+ "'");
				tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
				if (tempList != null) {
					tempMap = tempList.get(0);
					colNumString = (String) tempMap.get("zhi");
					colTitleNameMap.put("colName_" + i, colNumString);
				}
			}
			List<String> vaildateList = new LinkedList();
			boolean rowVildatePass = true; // 这一行所有列都通过验证
			Map<String, String> vildateTempMap = new HashMap<String, String>();
			for (int i = startRow; i <= endRow; i++) {
				rowVildatePass = true;
				vildateTempMap.clear();
				List<String> allVaildateList = new LinkedList();
				for (int j = startCol; j <= endCol; j++) {
					boolean cellVildatePass = true; // 这一个单元格验证通过
					String colTitleNameString = colTitleNameMap.get("colName_"
							+ j);
					String colTypeString = excelConf.getzdyzType(
							colTitleNameString).toLowerCase();
					int colLen = excelConf.getzdyzLen(colTitleNameString);
					String cellContentString = "";
					strSQL.delete(0, strSQL.length());
					strSQL
							.append("select zhi from t_excel_upload_temp where upload_lishi_guid='"
									+ uploadLishiGuid
									+ "' and jian='row_"
									+ i
									+ "_col_" + j + "'");
					tempList = this.getNpjtN().queryForList(strSQL.toString(),
							map);
					if (tempList != null) {
						tempMap = tempList.get(0);
						cellContentString = (String) tempMap.get("zhi");
						if (colLen != 0 && cellContentString.length() > colLen) {
							rowVildatePass = false;
							cellVildatePass = false;
						}
						if (colTypeString.equals("int")) {
							try {
								int tempInt = Integer
										.parseInt(cellContentString);
							} catch (Exception e) {
								rowVildatePass = false;
								cellVildatePass = false;
							}
						} else if (colTypeString.equals("string")) {
						}
						// 判断是否从表中取值
						String sqlString = excelConf
								.getzdqz(colTitleNameString);
						if (!sqlString.equals("")) {
							sqlString = sqlString.replace("{@}",
									cellContentString);
							List<Map> tempqzList = null;
							try {
								tempqzList = this.getNpjtN().queryForList(
										sqlString, map);
								Object object = null;
								if (tempqzList != null && tempqzList.size() > 0) {
									Map tempqzMap = tempqzList.get(0);
									for (Object key : tempqzMap.keySet()) {
										object = tempqzMap.get((String) key);
										break;
									}
								} else {
									rowVildatePass = false;
									cellVildatePass = false;
								}
							} catch (Exception e) {
								rowVildatePass = false;
								cellVildatePass = false;
							}
						}
						// 判断是否从表中取值 结束
						if (!cellVildatePass) { // 本单元格验证没通过
							allVaildateList.add("error_row_" + i + "_col_" + j);
						} else {
							allVaildateList.add("row_" + i + "_col_" + j);
						}
						vildateTempMap.put("row_" + i + "_col_" + j,
								cellContentString);
					}
				}
				if (!rowVildatePass) { // 本行验证没通过
					for (String keyString : allVaildateList) {
						vaildateList.add(keyString);
					}
					vaildateMap.putAll(vildateTempMap);
				}
			}
			vaildateMap.put("vaildateList", vaildateList);
			vaildateMap.put("colNum", colNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vaildateMap;
	}

	/**
	 * 分析excel上传文档
	 * 
	 * @param filePath
	 *            文件全路径
	 * @param fileRealName
	 *            文件真实名字
	 * @param uploadLishiGuid
	 *            历史表的主键
	 * @param uploadGuid
	 *            配置表的主键
	 * @return
	 */
	public String analyseExcel(String filePath, String fileRealName,
			String uploadLishiGuid, String uploadGuid, ExcelConf excelConf) {
		try {
			File file = new File(filePath);
			Workbook book = Workbook.getWorkbook(file);
			Sheet sheet = book.getSheet(0);
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			boolean isEmpty = true; // 判断一列是否为空，为空则结束

			StringBuffer strSQL = new StringBuffer();
			int endRow = 0;
			int endCol = 0;
			Map<String, String> emptyColMap = new HashMap<String, String>();

			if (rows > 0 && cols > 0) {
				for (int i = 0; i < cols; i++) {
					Cell cell = sheet.getCell(i, 0);
					String cellString = cell.getContents().trim();
					strSQL.delete(0, strSQL.length());
					strSQL
							.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
									+ yhid
									+ "', '"
									+ uploadLishiGuid
									+ "', 'colName_"
									+ i
									+ "', '"
									+ cellString
									+ "')");
					this.getNpjtN().update(strSQL.toString(), map);

					strSQL.delete(0, strSQL.length());
					strSQL
							.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
									+ yhid
									+ "', '"
									+ uploadLishiGuid
									+ "', 'colNameData_"
									+ i
									+ "', '"
									+ excelConf.getzddy(cellString) + "')");
					this.getNpjtN().update(strSQL.toString(), map);

					if (cellString.equals("")) {
						emptyColMap.put(Integer.toString(i), "");
					}
				}
			}
			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'colNum', '" + cols + "')");
			this.getNpjtN().update(strSQL.toString(), map);

			for (int i = 0; i < rows; i++) { // 循环行
				if (excelConf.jsh != 0 && excelConf.jsh < i) { // 结束行生效
					break;
				}
				endRow = i;
				if (excelConf.ksh != 0 && i < excelConf.ksh) {
					continue;
				}
				isEmpty = true;
				for (int j = 0; j < cols; j++) { // 循环列
					if (excelConf.jsl != 0 && excelConf.jsl < j) { // 结束列生效
						break;
					}
					endCol = j;
					if (excelConf.ksl != 0 && j < excelConf.ksl) {
						continue;
					}
					Cell cell = sheet.getCell(j, i);
					String cellString = cell.getContents().trim();
					if (!cellString.equals("")) {
						isEmpty = false;
					}
					strSQL.delete(0, strSQL.length());
					strSQL
							.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
									+ yhid
									+ "', '"
									+ uploadLishiGuid
									+ "', 'row_"
									+ i
									+ "_col_"
									+ j
									+ "', '"
									+ cellString + "')");
					this.getNpjtN().update(strSQL.toString(), map);
				}
				if (isEmpty) { // 如果一行为空，则结束
					break;
				}
			}

			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'startRow', '" + excelConf.ksh + "')");
			this.getNpjtN().update(strSQL.toString(), map);

			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'endRow', '" + endRow + "')");
			this.getNpjtN().update(strSQL.toString(), map);

			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'startCol', '" + excelConf.ksl + "')");
			this.getNpjtN().update(strSQL.toString(), map);

			strSQL.delete(0, strSQL.length());
			strSQL
					.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values ('"
							+ yhid
							+ "', '"
							+ uploadLishiGuid
							+ "', 'endCol', '" + endCol + "')");
			this.getNpjtN().update(strSQL.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把临时数据验证过后插入数据库表
	 * 
	 * @param tableName
	 *            表名称
	 * @param uploadLishiGuid
	 *            历史主键
	 * @param zhujian
	 *            主键
	 * @param skipMap
	 *            跳过的行
	 * @param urlPara
	 *            通过url传过来的值
	 * @return
	 */
	public String putTableData(String uploadLishiGuid, Map skipMap,
			Map<String, String> urlPara, ExcelConf excelConf) {
		LishiConf lishiConf = getlishiConf(uploadLishiGuid);
		String tableName = excelConf.bmc;
		String zhujian = excelConf.zj;

		try {
			StringBuffer strSQL = new StringBuffer();

			LinkedList colList = new LinkedList();
			LinkedList colNameList = new LinkedList();
			for (int i = 0; i < lishiConf.colNum; i++) {
				strSQL.delete(0, strSQL.length());
				strSQL
						.append("select * from t_excel_upload_temp where upload_lishi_guid='"
								+ uploadLishiGuid
								+ "' and jian='colNameData_"
								+ i + "'");
				List<Map> tempList = this.getNpjtN().queryForList(
						strSQL.toString(), map);
				if (tempList != null) {
					Map tempMap = tempList.get(0);
					colList.add(tempMap.get("zhi"));
				}
				strSQL.delete(0, strSQL.length());
				strSQL
						.append("select * from t_excel_upload_temp where upload_lishi_guid='"
								+ uploadLishiGuid
								+ "' and jian='colName_"
								+ i
								+ "'");
				tempList = this.getNpjtN().queryForList(strSQL.toString(), map);
				if (tempList != null) {
					Map tempMap = tempList.get(0);
					colNameList.add(tempMap.get("zhi"));
				}
			}

			for (int i = lishiConf.startRow; i <= lishiConf.endRow; i++) {
				boolean ifCall = false;
				HashMap callArgMap = new HashMap<String, Object>();
				Set<String> callPathMethodSet = new LinkedHashSet<String>();

				if (skipMap.containsKey("row_" + i + "_col_0")) {
					continue;
				}
				String urlColName = "", urlColValue = "";
				int num = 0;
				for (String keyString : urlPara.keySet()) {
					urlColName = urlColName + keyString + ",";
					urlColValue = urlColValue + ":url" + num + ",";
					map.put("url" + num, urlPara.get(keyString));
					num++;
				}
				// int colNum = colList.size() - 1;
				int colNum = 0;
				String colName = zhujian + ",", colValue = ":" + zhujian + ",";
				if (zhujian == null || zhujian.equals("")) {
					colName = urlColName;
					colValue = urlColValue;
				} else {
					colName = colName + urlColName;
					colValue = colValue + urlColValue;
				}
				String zhujianString = ""; // 保存主键
				for (int j = lishiConf.startCol; j <= lishiConf.endCol; j++) {
					String tempString = "";
					strSQL.delete(0, strSQL.length());
					strSQL
							.append("select * from t_excel_upload_temp where upload_lishi_guid='"
									+ uploadLishiGuid
									+ "' and jian='row_"
									+ i
									+ "_col_" + j + "'");
					List<Map> tempList = this.getNpjtN().queryForList(
							strSQL.toString(), map);
					Map tempMap = null;
					String insertString = "";
					if (tempList != null) {
						tempMap = tempList.get(0);
						insertString = (String) tempMap.get("zhi");
					}

					// 判断是否从表中取值
					String excelColName = (String) colNameList.get(colNum);
					String sqlString = excelConf.getzdqz(excelColName);
					String ywClassPath = excelConf.getffyzType(excelColName);
					String ywClassMethod = excelConf
							.getffyzMethod(excelColName);
					if (ywClassPath.equals("")) {
						colName = colName + colList.get(colNum) + ",";
						colValue = colValue + ":col" + colNum + ",";
					}
					callArgMap.put(excelColName, insertString);
					if (!sqlString.equals("")) {
						sqlString = sqlString.replace("{@}", insertString);
						Object object = null;
						try {
							List<Map> tempqzList = this.getNpjtN()
									.queryForList(sqlString, map);
							object = null;
							if (tempqzList != null&&tempqzList.size()>0) {
								Map tempqzMap = tempqzList.get(0);
								for (Object key : tempqzMap.keySet()) {
									object = tempqzMap.get((String) key);
									break;
								}
							}
						} catch (Exception e) {
							object = new String("");
						}
						map.put("col" + colNum++, object);
						callArgMap.put(excelColName, object);
					} else if (!ywClassPath.equals("")) {
						ifCall = true;
						callPathMethodSet
								.add(ywClassPath + ";" + ywClassMethod);
					} else {
						map.put("col" + colNum++, insertString);
					}
					// 判断是否从表中取值 结束
				}
				if (!zhujian.equals("")) {
					zhujianString = Guid.get();
					map.put(zhujian, zhujianString);
					callArgMap.put("guid", zhujianString);
				}
				if (!colName.equals("")
						&& (colName.lastIndexOf(",") == colName.length() - 1)) {
					colName = colName.substring(0, colName.length() - 1);
				}
				if (!colValue.equals("")
						&& (colValue.lastIndexOf(",") == colValue.length() - 1)) {
					colValue = colValue.substring(0, colValue.length() - 1);
				}

				strSQL.delete(0, strSQL.length());
				strSQL.append("insert into " + tableName + "(" + colName
						+ ") values(" + colValue + ")");
				this.getNpjtN().update(strSQL.toString(), map);

				if (ifCall) {
					for (String keyString : callPathMethodSet) {
						try {
							callMethod(keyString.split(";")[0], keyString
									.split(";")[1], callArgMap);
						} catch (Exception e) {
							callMethodError(keyString.split(";")[0], keyString
									.split(";")[1]
									+ "Error", uploadLishiGuid);
						}
					}
				}

				if (!zhujianString.equals("")) {
					strSQL.delete(0, strSQL.length());
					// strSQL.append("insert into t_excel_upload_temp(yhid,upload_lishi_guid,jian,zhi) values('"+
					// yhid
					// +"','"+uploadLishiGuid+"','rowGuid_"+i+"','"+zhujianString+"')");
					// this.getNpjtN().update(strSQL.toString(), map);
					strSQL
							.append("insert into t_excel_pici(table_name,guid,upload_lishi_guid) values('"
									+ excelConf.bmc
									+ "','"
									+ zhujianString
									+ "','" + uploadLishiGuid + "')");
					this.getNpjtN().update(strSQL.toString(), map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String guid = Guid.get();
			logItem.setMethod("list");
			logItem.setLogid(guid);
			logItem.setLevel("info");
			logItem.setDesc("把临时数据验证过后插入数据库表出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "ExcelUpload.putTable1Data异常:" + e.toString());
		}
		return null;
	}
}
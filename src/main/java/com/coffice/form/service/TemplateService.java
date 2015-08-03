// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TemplateService.java

package com.coffice.form.service;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.dom4j.DocumentException;

import com.coffice.form.bean.*;
import com.coffice.form.core.TemplateUtil;
import com.coffice.form.dao.ISerialDao;
import com.coffice.form.dao.imp.*;
import com.coffice.form.util.StringUtil;
import com.coffice.util.Yhfw;
import com.coffice.util.Db;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;

public class TemplateService
{

	private MySqlFormDaoImpl formDao;
	private MySqlTableDaoImpl tableDao;
	private MySqlQueryDaoImpl queryDao;
	private ISerialDao serialDao;
	private TemplateUtil tUtil;

	public TemplateService()
	{
		formDao = null;
		tableDao = null;
		queryDao = null;
		serialDao = null;
		tUtil = new TemplateUtil();
	}

	public void createTemplate(String formContent, String path, String sourceName, String formName, Map map)
	{
		FormInfo form = new FormInfo();
		String formid = "";
		try
		{
			formid = (new StringBuilder()).append(Db.getNextId()).toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		form.setId(formid);
		form.setFormName(formName);
		String tableName = (new StringBuilder("t_form")).append(formid).toString();
		form.setTableName(tableName);
		form.setTemplateName((new StringBuilder("t_form")).append(formid).append(".ftl").toString());
		form.setInUse("Y");
		TableInfo table = new TableInfo();
		table.setForm(form);
		table.setFields(tUtil.getFields(formContent));
		try
		{
			tUtil.writeTemplate(formContent, path, (new StringBuilder(String.valueOf(tableName))).append("_source").append(".txt").toString());
			formContent = StringUtil.regFilter("remark=\"{1}[^\"]+\"{1}", formContent);
			tUtil.createTemplateFile(path, sourceName, (new StringBuilder(String.valueOf(tableName))).append(".ftl").toString(), formContent);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		formDao = new MySqlFormDaoImpl();
		String id = formDao.createFormInfo(form, map);
		table.setId((new StringBuilder(String.valueOf(id))).toString());
		formDao.createFormTable(table);
		try
		{
			formDao.insertComponentType(table);
			formDao.insertTd(table);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List loadAllTemplate()
	{
		List list = new ArrayList();
		formDao = new MySqlFormDaoImpl();
		list = formDao.getAllForms();
		return list;
	}

	public Page getPage(String tableName, String id, Map authority, String yhid, int showOPinionsList)
	{
		Page page = new Page();
		List<FieldInfo> list = null;
		List<Attitude> aList = null;
		tableDao = new MySqlTableDaoImpl();
		page.put("tableName", tableName);
		try {
			list = tableDao.getColumnNames(tableName);
			if (id != null) {
				//记录不为空时填充数据
				list = tableDao.select(tableName, id, list);
				//aList = tableDao.getAttitudeList(tableName, id);
			}
			for (FieldInfo fieldInfo : list) {
				//会签
				if (TemplateUtil.ATTITUDE_INSERT.equalsIgnoreCase(fieldInfo
						.getInsertType())||TemplateUtil.SUB_PROCESS_INFO.equalsIgnoreCase(fieldInfo
						.getInsertType())) {
					aList = tableDao.getAttitudeList(tableName, id, fieldInfo
							.getFieldName());
					//aList = tableDao.getAttitudeList2(tableName, id, fieldInfo.getFieldName(),yhid);
					
					String temp = fieldInfo.getFieldName();
					page.put(temp, aList);
					//获取常用的审批意见
					if(SysPara.compareValue("form_spyj", "1")&&showOPinionsList==1){
						if(page.get("form_spyj1223")==null){
						List<Map> _list = tableDao.getOpinionsList(yhid);
						 _list = this.convertMapKey(_list);
						page.put("form_spyj1223", _list);
						}
					}
				} else if (TemplateUtil.SYS_INSERT.equalsIgnoreCase(fieldInfo
						.getInsertType())) {//文号自动生成
					if (id == null||"".equals(id)) {
					String customid = fieldInfo.getSequenceTypeId();  //文号类型id
						serialDao = new MySqlSerialDaoImpl();
						SerialType serialType = serialDao.getTypeById(customid);
						int count = serialDao.getSerialCount(customid,
								serialType.getType());//serialType文号累加形式
						fieldInfo.setFieldValue(serialType
								.generateSerial(count));
					}
					page.put(fieldInfo.getFieldName(), fieldInfo
							.getFieldValue());
				}else if (TemplateUtil.SYS_INSERT_MANUAL.equalsIgnoreCase(fieldInfo
						.getInsertType())) {//文号手动生成
                    String wh = fieldInfo.getFieldValue();
                    if(wh==null)
					page.put(fieldInfo.getFieldName(), "");
                    else
                    page.put(fieldInfo.getFieldName(), fieldInfo.getFieldValue());	
				}else if (!"SELECT".equalsIgnoreCase(fieldInfo
						.getComponentType())){//普通的文本
					String value = fieldInfo.getFieldValue();
					if(value!=null){	
					page.put(fieldInfo.getFieldName(), value);							
					}else{
					page.put(fieldInfo.getFieldName(), null);	
					}
				}else {//列表
					String typeId = fieldInfo.getSequenceTypeId();
					queryDao = new MySqlQueryDaoImpl();
					String zzid = (String)Cache.getUserInfo(yhid, "zzid");
					List<Ss> sList = queryDao.getSsById(typeId);
					page.put(fieldInfo.getFieldName(), sList);
					// add a name_value data field
					page.put(fieldInfo.getFieldName() + "_value", fieldInfo
							.getFieldValue());
				}
		if ("INPUT".equalsIgnoreCase(fieldInfo.getComponentType())
				&& fieldInfo.getSequenceTypeId() != null && !"null".equals(fieldInfo.getSequenceTypeId()) && id != null) {//人员选择
				if(fieldInfo.getFieldValue()!=null&&!"".equals(fieldInfo.getFieldValue())){
				   List<Map> _list= Yhfw.getYhfwList(fieldInfo.getFieldValue());
				   String yh_mc_list="";
				   _list = this.convertMapKey(_list);
				//   for(Map map:_list){
				//	   yh_mc_list = yh_mc_list+","+(String)map.get("mc");
				//   }
				//   yh_mc_list = yh_mc_list.replaceFirst(",", "");
				   //page.put(fieldInfo.getFieldName(), yh_mc_list);
					page.put(TemplateUtil.USERPICKER_PREFIX
							+ fieldInfo.getFieldName(), _list);
					page.put(fieldInfo.getFieldName(), fieldInfo.getFieldValue());
				}
			}
		}

			Iterator<Entry<String, Integer>> it = authority.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> element = it.next();
				String td = (String) element.getKey();
				Integer au = (Integer) authority.get(td);
				String auString = "DEFAULT";
				if (au == FormService.AUTHORITY_HIDDEN) {
					auString = "HIDDEN";
			// get component by td
		//	List<String> components = tableDao.getComponentByTd(td);
			//for (String string : components) {
			//	page.put(string, "");
			//}
					page.put(td + "_show_huiqian_inputbox", "none");
				} else if (au == FormService.AUTHORITY_READONLY){
					auString = "READONLY";
					page.put(td + "_show_huiqian_inputbox", "none");
				}else{//默认
					page.put(td + "_show_huiqian_inputbox", "''");
				}
				page.put(td + "_tdClass", auString);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return page;
	}

	public String generateScript(List list)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("var oform = document.forms[0] ;");
		builder.append("var validate_function = function (){");
		int VALIDATE_NOT_NULL = 0;
		if (SysPara.compareValue("form_check_isempty", "true"))
			VALIDATE_NOT_NULL = 0;
		else
			VALIDATE_NOT_NULL = 1;
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			ValidateInfo validateInfo = (ValidateInfo)iterator.next();
			String temp = validateInfo.getDataType();
			if (validateInfo.getValidateType() == VALIDATE_NOT_NULL)
			{
				String vName = (new StringBuilder("v_")).append(validateInfo.getComponentName()).toString();
				builder.append("var ").append(vName);
				builder.append(" = oform.").append(validateInfo.getComponentName()).append(";");
				builder.append("if(!").append(vName).append(".value){");
				builder.append("alert('").append(validateInfo.getRemark());
				builder.append("不能为空');");
				builder.append(vName).append(".focus();");
				builder.append("return false;");
				builder.append("}");
			}
			if ("FLOAT".equals(validateInfo.getDataType()))
			{
				String vName = (new StringBuilder("_")).append(validateInfo.getComponentName()).toString();
				builder.append("var ").append(vName);
				builder.append(" = oform.").append(validateInfo.getComponentName()).append(";");
				builder.append("if(isNaN(").append(vName).append(".value)){");
				builder.append("alert('").append(validateInfo.getRemark()).append("必须是数字');");
				builder.append(vName).append(".focus();");
				builder.append("return false;");
				builder.append("}");
			}
		}

		builder.append("return true;};");
		builder.append("if( oform ) {");
		builder.append("oform.attachEvent( 'onsubmit' ,  validate_function );");
		builder.append("oform.oldSubmit = oform.submit;");
		builder.append("oform.submit = validate_function;");
		builder.append("}");
		return builder.toString();
	}

	public String getPrintTemplateName(String tableName)
	{
		tableDao = new MySqlTableDaoImpl();
		List list = null;
		list = tableDao.getPrintTemplateFileNames(tableName);
		return (String)list.get(0);
	}

	public List convertMapKey(List list)
	{
		String dbtype = "";
		try
		{
			dbtype = SysPara.getValue("db_type");
		}
		catch (Exception e)
		{
			dbtype = "mysql";
		}
		if ("oracle".equals(dbtype))
		{
			Map _map = null;
			List _list = new ArrayList();
			for (Iterator iterator = list.iterator(); iterator.hasNext(); _list.add(_map))
			{
				Map map = (Map)iterator.next();
				_map = new HashMap();
				Object obj;
				for (Iterator iterator1 = map.keySet().iterator(); iterator1.hasNext(); _map.put(obj.toString().toLowerCase(), map.get(obj)))
					obj = iterator1.next();

			}

			return _list;
		} else
		{
			return list;
		}
	}
}

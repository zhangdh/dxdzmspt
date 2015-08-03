package com.coffice.form.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import com.coffice.form.bean.Attitude;
import com.coffice.form.bean.FieldInfo;
import com.coffice.form.bean.SerialType;
import com.coffice.form.dao.ISerialDao;
import com.coffice.form.dao.imp.MySqlSerialDaoImpl;
import com.coffice.form.dao.imp.MySqlTableDaoImpl;
import com.coffice.exception.ServiceException;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Str;
import com.coffice.util.cache.Cache;

public class FormService
{
  LogItem logItem = new LogItem();
  private MySqlTableDaoImpl tableDao = null;
  private SerialService serialService = null;
  public static final Integer AUTHORITY_DEFAULT = Integer.valueOf(0);
  public static final Integer AUTHORITY_READONLY = Integer.valueOf(1);
  public static final Integer AUTHORITY_HIDDEN = Integer.valueOf(2);

  @Transactional
  public String saveFormData(String tableName, Map formValues, Map<String, Integer> authority, String loginCode, String businessId)
  {
    String result = "";
    List list = null;
    List list2 = new ArrayList();
    this.tableDao = new MySqlTableDaoImpl();
    this.serialService = new SerialService();
    try {
      list = this.tableDao.getColumnDataTypes(tableName);
      list = FieldAuthorityFilter(list, authority);
      if (list == null);
      Attitude att = null;
      List attList = new ArrayList();
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { 
    	String cid;
        String wh;
        FieldInfo fieldInfo = (FieldInfo)localIterator.next();
        if ("1".equalsIgnoreCase(fieldInfo.getInsertType()))
        {
          if (("DATETIME".equals(fieldInfo.getFieldType())) || ("FLOAT".equals(fieldInfo.getFieldType())))
            fieldInfo.setFieldValue(
              ("".equals
              ((String)formValues.get(fieldInfo.getFieldName()))) ? null : 
              (String)formValues.get(fieldInfo.getFieldName()));
          else
            fieldInfo.setFieldValue((String)formValues.get(fieldInfo.getFieldName()));
            list2.add(fieldInfo); 
        }
        if ("0".equalsIgnoreCase
          (fieldInfo.getInsertType()))
        {
          cid = fieldInfo.getFieldValue();
          wh = (String)formValues.get(
            fieldInfo.getFieldName());

          if (!(Str.filterNull(wh).equals(""))) {
            this.serialService.insertSerial(cid, wh);
            fieldInfo.setFieldValue(wh);

            formValues.put("sdcncsi_ict_wh", wh);
          } else {
            String temp = this.serialService.insertSerial(cid);
            fieldInfo.setFieldValue(temp);

            formValues.put("sdcncsi_ict_wh", temp);
          }
          list2.add(fieldInfo); 
        }
        if ("3".equalsIgnoreCase
          (fieldInfo.getInsertType())) {
          cid = fieldInfo.getFieldValue();
          wh = (String)formValues.get(
            fieldInfo.getFieldName());
          if ((wh == null) || ("".equals(wh))) ;
          this.serialService.insertSerial(cid, wh);
          fieldInfo.setFieldValue(wh);

          formValues.put("sdcncsi_ict_wh", wh);
          list2.add(fieldInfo); 
        }
        if ((!("2".equalsIgnoreCase(fieldInfo.getInsertType()))) && 
          (!("4".equalsIgnoreCase(fieldInfo.getInsertType())))) ;
        String content = (String)formValues.get(
          fieldInfo.getFieldName());
        if ((content == null) || (content.equals(""))) ;
        att = new Attitude();
        String xm = (String)Cache.getUserInfo(loginCode, "xm");
        if (content.equals(xm))
          content = "";

        att.setContent(content);
        att.setLoginCode(loginCode);
        att.setTableId
          (this.tableDao.getIdByTableName
          (tableName));
        att.setColumnId(this.tableDao.getColumnId
          (att.getTableId(), fieldInfo.getFieldName()));
        attList.add(att);

        if ((!("4".equalsIgnoreCase(fieldInfo.getInsertType()))) || 
          ("".equals(Str.filterNull((String)formValues.get("ptablename"))))) ;
        Attitude att2 = new Attitude();
        att2.setContent(content);
        att2.setLoginCode(loginCode);
        att2.setTableId(String.valueOf(String.valueOf(formValues.get("ptablename"))));
        att2.setFormId(String.valueOf(formValues.get("pformid")));
        att2.setColumnId(this.tableDao.getColumnId
          (att.getTableId(), fieldInfo.getFieldName()));
        label709: attList.add(att2);
      }

      result = this.tableDao.insert(tableName, list2);

      formValues.put("businessId", businessId);
      if ((businessId != null) && (!(businessId.equals(""))))
      {
        if ("".equals(Str.filterNull(String.valueOf(formValues.get("sdcncsi_ict_gwbt"))))) {
          String gwbt = (String)Db.getJtN().queryForObject("select doc_name from t_document_type where id=(select doc_id from  (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) l where id=?)", 
            new Object[] { businessId }, String.class);
          formValues.put("sdcncsi_ict_gwbt", gwbt);
        }
        Db.getSji().withTableName("t_gw_info").execute(formValues);
      }

      if (attList.size() > 0) {
        for (Iterator localIterator = attList.iterator(); localIterator.hasNext(); ) { Attitude attitude = (Attitude)localIterator.next();
          attitude.setFormId(String.valueOf(result));

          this.tableDao.insertAttitude(attitude);
        }

      }

      this.logItem.setMethod("saveFormData");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单数据成功,操作用户：" + loginCode);
      Log.write(this.logItem);
    }
    catch (SQLException e) {
      String guid = Guid.get();
      this.logItem.setMethod("saveFormData");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单数据时出现异常");
      this.logItem.setContent("表单编号：" + tableName + ";当前登录用户：" + loginCode + 
        e.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存表单数据时出现异常");
    }
    label1060: return result;
  }

  @Transactional
  public boolean updateFormData(String tableName, Map<String, Object> formValues, String id, Map<String, Integer> authority, String loginCode, String businessId)
  {
    boolean result = false;
    List list = null;
    Attitude att = null;
    List attList = new ArrayList();
    this.tableDao = new MySqlTableDaoImpl();
    try {
      list = this.tableDao.getColumnDataTypes(tableName);
      list = FieldAuthorityFilter(list, authority);
      if (list != null) {
        for (int i = list.size() - 1; i >= 0; --i) {
        	FieldInfo t = (FieldInfo)list.get(i);
        	if (("2".equalsIgnoreCase(t.getInsertType())) || ("4".equalsIgnoreCase(t.getInsertType()))) {
        		String content = (String)formValues.get(t.getFieldName());
        		if ((content != null) && (!(content.equals("")))) {
        			att = new Attitude();
        			String xm = (String)Cache.getUserInfo(loginCode, "xm");
        			if (content.equals(xm))content = "";
        			att.setContent(content);
        			att.setLoginCode(loginCode);
        			att.setTableId(String.valueOf(this.tableDao.getIdByTableName(tableName)));
        			att.setFormId(String.valueOf(id));
        			att.setColumnId(this.tableDao.getColumnId(att.getTableId(), t.getFieldName()));
             if (("4".equalsIgnoreCase(t.getInsertType())) && (!("".equals(Str.filterNull((String)formValues.get("ptablename")))))) {
                Attitude att2 = new Attitude();
                att2.setContent(content);
                att2.setLoginCode(loginCode);
                att2.setTableId(String.valueOf(formValues.get("ptablename")));
                att2.setFormId(String.valueOf(formValues.get("pformid")));
                att2.setColumnId(this.tableDao.getColumnId(att.getTableId(), t.getFieldName()));
                attList.add(att2);
              }
              attList.add(att);
            }
          }
          if ((!("1".equals(((FieldInfo)list.get(i)).getInsertType()))) && (!("0".equals(((FieldInfo)list.get(i)).getInsertType()))) && (!("3".equals(((FieldInfo)list.get(i)).getInsertType()))))
          {
            list.remove(i);
          }
        }
        for (Iterator t = list.iterator(); t.hasNext(); ) { FieldInfo fieldInfo = (FieldInfo)t.next();
          if (("DATETIME".equals(fieldInfo.getFieldType())) || ("FLOAT".equals(fieldInfo.getFieldType()))) {
        	  	fieldInfo.setFieldValue(("".equals((String)formValues.get(fieldInfo.getFieldName()))) ? null : (String)formValues.get(fieldInfo.getFieldName()));
          }
          fieldInfo.setFieldValue((String)formValues.get(fieldInfo.getFieldName()));
        }
        this.tableDao.update(tableName, list, id);
        this.tableDao.update_gwInfo(formValues, list, businessId);
        if (attList.size() > 0)
          for (Iterator t = attList.iterator(); t.hasNext(); ) { 
        	  	Attitude attitude = (Attitude)t.next();
        	  	String attId = this.tableDao.getAttitudeId(tableName, String.valueOf(attitude.getFormId()), loginCode, attitude.getColumnId());
            if (!(attId.equals(""))) {
              attitude.setId(String.valueOf(attId));
              this.tableDao.updateAttitude(attitude); 
            }else{
            	this.tableDao.insertAttitude(attitude);
            }
          }
      }
      result = true;
      this.logItem.setMethod("updateFormData");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单数据成功,操作用户：" + loginCode);
      Log.write(this.logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("updateFormData");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单数据时出现异常,表单编号：" + tableName + "；操作用户：" + loginCode);
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存数据时出现异常");
    }
    return result;
  }
  @Transactional
  public boolean updateFormData1(String tableName, Map<String, Object> formValues, String id, Map<String, Integer> authority, String loginCode, String businessId,String stepId)
  {
    boolean result = false;
    List list = null;
    Attitude att = null;
    List attList = new ArrayList();
    this.tableDao = new MySqlTableDaoImpl();
    try {
      list = this.tableDao.getColumnDataTypes(tableName);
      list = FieldAuthorityFilter(list, authority);
      if (list != null) {
        for (int i = list.size() - 1; i >= 0; --i) {
        	FieldInfo t = (FieldInfo)list.get(i);
        	if (("2".equalsIgnoreCase(t.getInsertType())) || ("4".equalsIgnoreCase(t.getInsertType()))) {
        		String content = (String)formValues.get(t.getFieldName());
        		if ((content != null) && (!(content.equals("")))) {
        			att = new Attitude();
        			String xm = (String)Cache.getUserInfo(loginCode, "xm");
        			if (content.equals(xm))content = "";
        			att.setContent(content);
        			att.setLoginCode(loginCode);
        			att.setTableId(String.valueOf(this.tableDao.getIdByTableName(tableName)));
        			att.setFormId(String.valueOf(id));
        			att.setColumnId(this.tableDao.getColumnId(att.getTableId(), t.getFieldName()));
             if (("4".equalsIgnoreCase(t.getInsertType())) && (!("".equals(Str.filterNull((String)formValues.get("ptablename")))))) {
                Attitude att2 = new Attitude();
                att2.setContent(content);
                att2.setLoginCode(loginCode);
                att2.setTableId(String.valueOf(formValues.get("ptablename")));
                att2.setFormId(String.valueOf(formValues.get("pformid")));
                att2.setColumnId(this.tableDao.getColumnId(att.getTableId(), t.getFieldName()));
                attList.add(att2);
              }
              attList.add(att);
            }
          }
          if ((!("1".equals(((FieldInfo)list.get(i)).getInsertType()))) && (!("0".equals(((FieldInfo)list.get(i)).getInsertType()))) && (!("3".equals(((FieldInfo)list.get(i)).getInsertType()))))
          {
            list.remove(i);
          }
        }
        for (Iterator t = list.iterator(); t.hasNext(); ) { FieldInfo fieldInfo = (FieldInfo)t.next();
          if (("DATETIME".equals(fieldInfo.getFieldType())) || ("FLOAT".equals(fieldInfo.getFieldType()))) {
        	  	fieldInfo.setFieldValue(("".equals((String)formValues.get(fieldInfo.getFieldName()))) ? null : (String)formValues.get(fieldInfo.getFieldName()));
          }
          fieldInfo.setFieldValue((String)formValues.get(fieldInfo.getFieldName()));
        }
        this.tableDao.update(tableName, list, id);
        this.tableDao.update_gwInfo(formValues, list, businessId);
        if (attList.size() > 0)
          for (Iterator t = attList.iterator(); t.hasNext(); ) { 
        	  	Attitude attitude = (Attitude)t.next();
        	  	String attId = this.tableDao.getAttitudeId1(tableName, String.valueOf(attitude.getFormId()), loginCode, attitude.getColumnId(),stepId);
            if (!(attId.equals(""))) {
              attitude.setId(String.valueOf(attId));
              this.tableDao.updateAttitude(attitude); 
            }else{
            	this.tableDao.insertAttitude1(attitude,stepId);
            }
          }
      }
      result = true;
      this.logItem.setMethod("updateFormData");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单数据成功,操作用户：" + loginCode);
      Log.write(this.logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("updateFormData");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单数据时出现异常,表单编号：" + tableName + "；操作用户：" + loginCode);
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存数据时出现异常");
    }
    return result;
  }
  
  private List<FieldInfo> FieldAuthorityFilter(List<FieldInfo> list, Map<String, Integer> authority)
  {
    List temp = new ArrayList();
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { FieldInfo info = (FieldInfo)localIterator.next();
      temp.add(info);
    }
    for (int i = 0; i < list.size(); ++i) {
      FieldInfo info = (FieldInfo)list.get(i);
      String fieldName = info.getTdId();
      if ((authority.containsKey(fieldName)) && 
        (authority.get(fieldName) != AUTHORITY_DEFAULT))
        temp.remove(info);
    }

    return temp;
  }

  public Map getWh(Map map)
  {
    ISerialDao serialDao = new MySqlSerialDaoImpl();
    String customid = (String)map.get("customid");
    SerialType serialType = serialDao.getTypeById(customid);
    int count = serialDao.getSerialCount(customid, serialType.getType());
    String wh = serialType.generateSerial(count);
    this.serialService = new SerialService();
    this.serialService.insertSerial(customid, wh);
    map.put("sdcncsi_ict_wh", wh);
    return map;
  }

  @Transactional
  public String saveUserFormData(String tableName, Map<String, Object> formValues, Map<String, Integer> authority, String loginCode)
  {
    String result = "";
    List list = null;
    List list2 = new ArrayList();
    this.tableDao = new MySqlTableDaoImpl();
    this.serialService = new SerialService();
    try {
      list = this.tableDao.getColumnDataTypes(tableName);
      list = FieldAuthorityFilter(list, authority);
      if (list == null)  ;
      Attitude att = null;
      List attList = new ArrayList();
      for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { String cid;
        String wh;
        FieldInfo fieldInfo = (FieldInfo)localIterator.next();
        if ("1".equalsIgnoreCase(
          fieldInfo.getInsertType())) {
          fieldInfo.setFieldValue(
            (String)formValues.get(fieldInfo.getFieldName()));
          list2.add(fieldInfo);  
        }
        if ("0".equalsIgnoreCase
          (fieldInfo.getInsertType())) {
          cid = fieldInfo.getFieldValue();
          wh = (String)formValues.get(
            fieldInfo.getFieldName());
          if (!(Str.filterNull(wh).equals(""))) {
            this.serialService.insertSerial(cid, wh);
            fieldInfo.setFieldValue(wh);
          } else {
            String temp = this.serialService.insertSerial(cid);
            fieldInfo.setFieldValue(temp);
          }
          list2.add(fieldInfo);  
        }
        if ("3".equalsIgnoreCase
          (fieldInfo.getInsertType())) {
          cid = fieldInfo.getFieldValue();
          wh = (String)formValues.get(
            fieldInfo.getFieldName());
          if ((wh == null) || ("".equals(wh))) ;
          this.serialService.insertSerial(cid, wh);
          fieldInfo.setFieldValue(wh);
          list2.add(fieldInfo);  
        }

        if (!("2".equalsIgnoreCase
          (fieldInfo.getInsertType()))) ;
        String content = (String)formValues.get(
          fieldInfo.getFieldName());
        if ((content == null) || (content.equals(""))) ;
        att = new Attitude();

        att.setContent(
          (String)formValues.get
          (fieldInfo.getFieldName()));
        att.setLoginCode(loginCode);
        att.setTableId
          (this.tableDao.getIdByTableName
          (tableName));
        att.setColumnId(this.tableDao.getColumnId
          (att.getTableId(), fieldInfo.getFieldName()));
        label450: attList.add(att);
      }

      if ((formValues.get("sdunisi_ict_form_guid") != null) && (!("".equals(formValues.get("sdunisi_ict_form_guid"))))) {
        result = String.valueOf(formValues.get("sdunisi_ict_form_guid"));
        this.tableDao.insert(tableName, list2, result);
      } else {
        result = this.tableDao.insert(tableName, list2);
      }
      if (attList.size() > 0)
        for (Iterator localIterator = attList.iterator(); localIterator.hasNext(); ) { Attitude attitude = (Attitude)localIterator.next();
          attitude.setFormId(String.valueOf(result));
          if (attitude.getContent().trim().length() <= 0)  
          label597: this.tableDao.insertAttitude(attitude);
        }
      this.logItem.setMethod("saveUserFormData");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单数据成功,操作用户：" + loginCode);
      Log.write(this.logItem);
    }
    catch (SQLException e) {
      String guid = Guid.get();
      this.logItem.setMethod("saveUserFormData");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单数据时出现异常");
      this.logItem.setContent("表单编号：" + tableName + ";当前登录用户：" + loginCode + 
        e.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存表单数据时出现异常");
    }
    label779: return result;
  }

  @Transactional
  public boolean updateUserFormData(String tableName, Map<String, Object> formValues, String id, Map<String, Integer> authority, String loginCode)
  {
    boolean result = false;
    List list = null;
    Attitude att = null;
    List attList = new ArrayList();
    this.tableDao = new MySqlTableDaoImpl();
    try {
      list = this.tableDao.getColumnDataTypes(tableName);
      list = FieldAuthorityFilter(list, authority);
      if (list != null) {
        for (int i = list.size() - 1; i >= 0; --i) {
        	FieldInfo t = (FieldInfo)list.get(i);
          if ("2".equalsIgnoreCase(
            t.getInsertType())) {
            String content = (String)formValues.get(
              t.getFieldName());
            if ((content != null) && (!(content.equals("")))) {
              att = new Attitude();
              att.setContent(
                (String)formValues.get
                (t.getFieldName()));
              att.setLoginCode(loginCode);
              att.setTableId(String.valueOf(
                this.tableDao.getIdByTableName(tableName)));
              att.setFormId(String.valueOf(id));
              att.setColumnId(this.tableDao.getColumnId
                (att.getTableId(), t.getFieldName()));
              attList.add(att);
            }
          }
          if ((!("1".equals(
            ((FieldInfo)list.get(i)).getInsertType()))) && 
            (!("0".equals(
            ((FieldInfo)list.get(i)).getInsertType()))) && 
            (!("3".equals(
            ((FieldInfo)list.get(i)).getInsertType()))))
          {
            list.remove(i);
          }
        }
        for (Iterator t = list.iterator(); t.hasNext(); ) { FieldInfo fieldInfo = (FieldInfo)t.next();
          fieldInfo.setFieldValue(
            (String)formValues.get
            (fieldInfo.getFieldName()));
        }
        this.tableDao.update(tableName, list, id);
        if (attList.size() > 0)
          for (Iterator t = attList.iterator(); t.hasNext(); ) { Attitude attitude = (Attitude)t.next();
            String attId = this.tableDao.getAttitudeId
              (tableName, String.valueOf(id), 
              loginCode, attitude.getColumnId());
            if (!(attId.equals(""))) {
              attitude.setId(String.valueOf(attId));
              if (attitude.getContent().trim().length() <= 0)  ;
              this.tableDao.updateAttitude(attitude);  
            }
            if (attitude.getContent().trim().length() <= 0)  ;
            label493: this.tableDao.insertAttitude(attitude);
          }

      }

      result = true;
      this.logItem.setMethod("updateUserFormData");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("更新表单数据成功,操作用户：" + loginCode);
      Log.write(this.logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("updateUserFormData");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("更新表单数据时出现异常,表单编号：" + tableName + "；操作用户：" + loginCode);
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存数据时出现异常");
    }
    return result;
  }
}
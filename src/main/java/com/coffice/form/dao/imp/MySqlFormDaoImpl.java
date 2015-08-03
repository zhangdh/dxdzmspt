package com.coffice.form.dao.imp;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import com.coffice.form.bean.FieldInfo;
import com.coffice.form.bean.FormInfo;
import com.coffice.form.bean.TableInfo;
import com.coffice.form.util.StringUtil;
import com.coffice.bean.PageBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class MySqlFormDaoImpl extends BaseUtil
{
  LogItem logItem = new LogItem();

  public String createFormInfo(FormInfo form, Map map)
  {
    String id = form.getId();
    try {
      map.put("id", id);

      map.put("tableName", form.getTableName());
      map.put("formName", form.getFormName());
      map.put("templateName", form.getTemplateName());
      map.put("inuse", "0");
      map.put("zt_dm", "1");
      getSji().withTableName("t_form_template_info").execute(map);
      this.logItem.setMethod("createFormInfo");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单信息成功");
      Log.write(this.logItem);
    } catch (Exception ex) {
      this.logItem.setMethod("createFormInfo");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("error");
      this.logItem.setDesc("保存表单信息时异常");
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("保存表单信息时异常");
    }
    return id; }

  public void createFormTable(TableInfo table) {
    StringBuffer sql = new StringBuffer("CREATE TABLE " + 
      table.getForm().getTableName() + "(" + 
      "id VARCHAR(50) NOT NULL ");
    int col_size = table.getFields().size();
    for (int i = 0; i < col_size; ++i) {
      FieldInfo fieldInfo = (FieldInfo)table.getFields().get(i);
      if ((fieldInfo.getFieldName() != null) && 
        (fieldInfo.getFieldType() != null))
      {
        sql.append(",").append(fieldInfo.getFieldName()).append(" ").append
          (fieldInfo.getFieldType());
        if (fieldInfo.getFieldLength() > 0)
          sql.append("(").append(fieldInfo.getFieldLength()).append(") ");

      }

    }

    sql.append(",PRIMARY KEY id)");
    try
    {
      System.out.println(sql.toString());
      getJtA().execute(sql.toString());

      this.logItem.setMethod("createFormTable");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("创建表成功");
      Log.write(this.logItem);
    } catch (Exception ex) {
      this.logItem.setMethod("createFormTable");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("error");
      this.logItem.setDesc("创建表单临时表失败");
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("创建表单临时表失败");
    }
  }

  public void alterFormTable(TableInfo table) throws SQLException {
    throw new SQLException("Not support alter table!");
  }

  public void deleteFormTable(String tableName) {
    String sql = "DROP TABLE " + tableName;
    try {
      getJtA().execute(sql);
      this.logItem.setMethod("deleteFormTable");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("删除表成功");
      Log.write(this.logItem);
    } catch (Exception ex) {
      this.logItem.setMethod("deleteFormTable");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("error");
      this.logItem.setDesc("删除表失败");
      Log.write(this.logItem);
      throw new ServiceException("删除表" + tableName + "时异常");
    }
  }

  public List<FormInfo> getAllForms() {
    String sql = "SELECT * FROM t_form_template_info WHERE inuse ='1' and id not in(select templateid from t_form_template_conf)";
    List list = null;
    FormInfo info = null;
    List list_temp = getNpjtA().getJdbcOperations().queryForList(sql);
    list = new ArrayList();
    for (Iterator localIterator = list_temp.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      info = new FormInfo();
      info.setId(String.valueOf(map.get("id")));
      info.setFormName((String)map.get("formName"));
      info.setTableName((String)map.get("tableName"));
      info.setTemplateName((String)map.get("templateName"));
      list.add(info);
    }
    return list; }

  public Map getAllForms(Map map) {
    JspJsonData jjd = new JspJsonData();
    try {
      PageBean page = new PageBean();
      page.setPageGoto((String)map.get("page_goto"));
      page.setPageSize("5");
      page.setSql("SELECT * FROM t_form_template_info WHERE inuse ='Y'");
      page.setCountSql("SELECT count(*) FROM t_form_template_info WHERE inuse ='Y'");
      page.setNamedParameters(map);
      List _list = Db.getPageData(page);
      jjd.setGrid("table_list", _list, page);
    } catch (Exception e) {
      String guid = Guid.get();
      this.logItem.setMethod("list");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("处理列表数据时出现异常");
      this.logItem.setContent(e.toString());
      Log.write(this.logItem);
      jjd.setResult(false, "demo.list异常:" + e.toString());
    }
    return jjd.getData();
  }

  public FormInfo getFormById(String id)
  {
    String sql = "SELECT * FROM t_form_template_info WHERE zt_dm ='1' and id =:id";
    HashMap map = new HashMap();
    map.put("id", id);
    Map _map = getNpjtA().queryForMap(sql, map);
    FormInfo info = null;
    if (_map != null) {
      info = new FormInfo();
      info.setId((String)_map.get("id"));
      info.setFormName((String)_map.get("formName"));
      info.setTableName((String)_map.get("tableName"));
      info.setTemplateName((String)_map.get("templateName"));
    }
    return info;
  }

  public FormInfo getFormByTemplateName(String templatename)
  {
    String sql = "SELECT * FROM t_form_template_info WHERE zt_dm ='1' and templatename =:templatename";
    HashMap map = new HashMap();
    map.put("templatename", templatename);
    Map _map = getNpjtA().queryForMap(sql, map);
    FormInfo info = null;
    if (_map != null) {
      info = new FormInfo();
      info.setId((String)_map.get("id"));
      info.setFormName((String)_map.get("formName"));
      info.setTableName((String)_map.get("tableName"));
      info.setTemplateName((String)_map.get("templateName"));
    }
    return info;
  }

  public String getTemplatePath(String templateid)
  {
    if (!(StringUtil.isExistsTable("t_form_template_conf")))
      return "";

    String path = "";
    List list = Db.getJtN().queryForList("select templatepath from  t_form_template_conf where templateid=?", 
      new Object[] { templateid });
    if (list.size() > 0)
      path = String.valueOf(((Map)list.get(0)).get("templatepath"));

    return path;
  }

  @Transactional
  public void insertComponentType(TableInfo tableInfo) throws SQLException {
    ArrayList _list = new ArrayList();

    List list = tableInfo.getFields();
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { FieldInfo fieldInfo = (FieldInfo)localIterator.next();
      if (fieldInfo.getFieldType() == null)
      {
       // break label314;
      }

      StringBuilder batchSql = new StringBuilder(
        "INSERT INTO t_form_columntype VALUES ('" + Db.getGUID() + "','");
      batchSql.append(fieldInfo.getFieldName()).append("','").append(
        fieldInfo.getComponentType()).append
        ("','").append(
        tableInfo.getId()).append
        ("','").append(
        fieldInfo.getSequenceTypeId()).append
        ("','").append(
        fieldInfo.getFieldType()).append
        ("','").append(
        fieldInfo.getInsertType()).append
        ("',");
      if (fieldInfo.getTdId() == null)
        batchSql.append("null");
      else
        batchSql.append("'").append(fieldInfo.getTdId()).append
          ("'");
      batchSql.append(",'").append(fieldInfo.getRemark()).append
        ("',").append(("".equals(fieldInfo.getLb())) ? "-1" : fieldInfo.getLb()).append(",").append(fieldInfo.getXh()).append(")");
      System.out.println("batchSql= " + batchSql.toString());
      label314: _list.add(batchSql.toString());
    }

    String[] sqlList = new String[_list.size()];
    try {
      getJtN().batchUpdate((String[])_list.toArray(sqlList));
      this.logItem.setMethod("insertComponentType");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("保存表单标签成功");
      Log.write(this.logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("insertComponentType");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("批量插入数据时出现异常");
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("批量插入数据时出现异常"); }
  }

	
/*
  @Transactional
  public void insertTd(TableInfo tableInfo) throws SQLException {
    List list = tableInfo.getFields();
    ArrayList _list = new ArrayList();
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { FieldInfo fieldInfo = (FieldInfo)localIterator.next();
      String batchSql = "INSERT INTO t_form_authority VALUES ('" + Db.getGUID() + "','";
      if (!("TD".equalsIgnoreCase(fieldInfo.getComponentType()))) break label142;
      batchSql = batchSql + fieldInfo.getFieldName() + "','" + tableInfo.getId() + 
        "','" + fieldInfo.getRemark() + "')";
      label142: _list.add(batchSql);
    }

    if (_list.size() > 0) {
      String[] sqlList = new String[_list.size()];
      try {
        getJtN().batchUpdate((String[])_list.toArray(sqlList));
        this.logItem.setMethod("insertTd");
        this.logItem.setLogid(Guid.get());
        this.logItem.setLevel("info");
        this.logItem.setDesc("保存成功");
        Log.write(this.logItem);
      } catch (Exception ex) {
        String guid = Guid.get();
        this.logItem.setMethod("insertTd");
        this.logItem.setLogid(guid);
        this.logItem.setLevel("error");
        this.logItem.setDesc("批量插入数据时出现异常");
        this.logItem.setContent(ex.toString());
        Log.write(this.logItem);
        throw new ServiceException("批量插入数据时出现异常");
      }
    }
  }
*/
	@Transactional
	public void insertTd(final TableInfo tableInfo) throws SQLException {
		final List<FieldInfo> list = tableInfo.getFields();
		ArrayList<String> _list = new ArrayList<String>();
		for (FieldInfo fieldInfo : list) {
			String batchSql = "INSERT INTO t_form_authority VALUES ('"+Db.getGUID()+"','";
			if ("TD".equalsIgnoreCase(fieldInfo.getComponentType())) {
				batchSql += fieldInfo.getFieldName() + "','" + tableInfo.getId()
						+ "','"+fieldInfo.getRemark() +"')";
				_list.add(batchSql);
			}
		}
		if(_list.size()>0){
		String[] sqlList = new String[_list.size()];
		try{
			this.getJtN().batchUpdate(_list.toArray(sqlList));
			logItem.setMethod("insertTd");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存成功");
			Log.write(logItem);
		}catch(Exception ex){
			String guid = Guid.get();
			logItem.setMethod("insertTd");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("批量插入数据时出现异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("批量插入数据时出现异常");// 抛出此异常以触发回滚	
		}
	}
}
  public void dropForm(String tableName) {
    String sql = "DROP TABLE `" + tableName + "`";
    try {
      getJtA().execute(sql);
      this.logItem.setMethod("dropForm");
      this.logItem.setLogid(Guid.get());
      this.logItem.setLevel("info");
      this.logItem.setDesc("删除表成功");
      Log.write(this.logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("dropForm");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("删除表时异常");
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
      throw new ServiceException("删除表时出现异常"); }
  }
/*
  public int getLastInsertId() {
    String sql = "";
    try {
      if (SysPara.getValue("db_type").equals("mysql")) {
        sql = "SELECT LAST_INSERT_ID() as ID";
      }

      if (SysPara.getValue("db_type").equals("sqlserver"))
        sql = "SELECT @@IDENTITY as ID";

      if (!(SysPara.getValue("db_type").equals("oracle"))) break label69;
      sql = "SELECT sequence.currval as ID from dual";
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    label69: int result = 0;
    List _list = getJtN().queryForList(sql);
    result = Integer.parseInt(String.valueOf(((Map)_list.get(0)).get("ID")));
    return result;
  }
*/
  public int getLastInsertId(){
		String sql = "";
	   try {
	   if (SysPara.getValue("db_type").equals("mysql")) {
			sql = "SELECT LAST_INSERT_ID() as ID";
		}
		
	    if (SysPara.getValue("db_type").equals("sqlserver")) {
	    sql = "SELECT @@IDENTITY as ID";	
	    }
	    if (SysPara.getValue("db_type").equals("oracle")) {
		sql = "SELECT sequence.currval as ID from dual";	
		}
	    }catch (Exception e) {
		e.printStackTrace();
		}
		int result = 0;
		List<Map<String,String>> _list = this.getJtN().queryForList(sql);
		result = Integer.parseInt(String.valueOf(_list.get(0).get("ID")));
		return result;
	}
}
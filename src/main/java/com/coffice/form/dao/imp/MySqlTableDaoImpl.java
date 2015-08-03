// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MySqlTableDaoImpl.java

package com.coffice.form.dao.imp;

import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;

import com.coffice.form.bean.Attitude;
import com.coffice.form.bean.FieldInfo;
import com.coffice.form.util.DBUtil;
import com.coffice.form.util.StringUtil;
import com.coffice.exception.ServiceException;
import com.coffice.util.*;
import com.coffice.util.cache.Cache;

public class MySqlTableDaoImpl extends BaseUtil
{

	LogItem logItem;

	public MySqlTableDaoImpl()
	{
		logItem = new LogItem();
	}

	public void delete(String tableName, int id)
	{
		String sql = (new StringBuilder("DELETE FROM ")).append(tableName).append(" WHERE id = ").append(id).toString();
		try
		{
			getJtN().update(sql);
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].delete");
			logItem.setLogid("删除表数据成功");
			logItem.setLevel("info");
			logItem.setDesc("删除成功");
			Log.write(logItem);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			logItem.setMethod("delete");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("删除表数据时异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("删除表数据时异常");
		}
	}

	public String insert(String tableName, List list)
	{
		String id = Db.getGUID();
		StringBuffer sql = new StringBuffer((new StringBuilder("INSERT INTO ")).append(tableName).append(" (id").toString());
		FieldInfo fieldInfo;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); sql.append(",").append(fieldInfo.getFieldName()))
			fieldInfo = (FieldInfo)iterator.next();

		sql.append((new StringBuilder(")VALUES ('")).append(id).append("'").toString());
		for (int i = 0; i < list.size(); i++)
		{
			FieldInfo o = (FieldInfo)list.get(i);
			if (!o.getFieldName().equalsIgnoreCase("id"))
			{
				sql.append(",");
				sql = StringUtil.addSqlString(sql, o, "");
			}
		}

		sql.append(")");
		try
		{
			getJtN().execute(sql.toString());
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].insert");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存成功");
			Log.write(logItem);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("insert");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存表数据失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			throw new ServiceException("保存表数据失败");
		}
		return id;
	}

	public void insert(String tableName, List list, String formid)
	{
		String id = formid;
		StringBuffer sql = new StringBuffer((new StringBuilder("INSERT INTO ")).append(tableName).append(" (id").toString());
		FieldInfo fieldInfo;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); sql.append(",").append(fieldInfo.getFieldName()))
			fieldInfo = (FieldInfo)iterator.next();

		sql.append((new StringBuilder(")VALUES ('")).append(id).append("'").toString());
		for (int i = 0; i < list.size(); i++)
		{
			FieldInfo o = (FieldInfo)list.get(i);
			if (!o.getFieldName().equalsIgnoreCase("id"))
			{
				sql.append(",");
				sql = StringUtil.addSqlString(sql, o, "");
			}
		}

		sql.append(")");
		try
		{
			getJtN().execute(sql.toString());
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].insert");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存成功");
			Log.write(logItem);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("insert");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存表数据失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			throw new ServiceException("保存表数据失败");
		}
	}

	public List select(String tableName, String id, List infos)
	{
		String sql = (new StringBuilder("SELECT * FROM ")).append(tableName).append(" WHERE id = '").append(id).append("'").toString();
		List list = getJtA().queryForList(sql);
		if (list.size() > 0)
		{
			Map _map = (Map)list.get(0);
			for (Iterator iterator = infos.iterator(); iterator.hasNext();)
			{
				FieldInfo fieldInfo = (FieldInfo)iterator.next();
				if (!"2".equalsIgnoreCase(fieldInfo.getInsertType()))
					if ("DATETIME".equals(fieldInfo.getFieldType()))
					{
						if (String.valueOf(_map.get(fieldInfo.getFieldName())).indexOf("00:00:00") > 0)
							fieldInfo.setFieldValue(String.valueOf(_map.get(fieldInfo.getFieldName())).substring(0, 10));
						else
						if (String.valueOf(_map.get(fieldInfo.getFieldName())).indexOf(".0") > 0)
							fieldInfo.setFieldValue(String.valueOf(_map.get(fieldInfo.getFieldName())).replaceFirst("\\.0", ""));
					} else
					if (_map.get(fieldInfo.getFieldName()) == null)
						fieldInfo.setFieldValue(null);
					else
						fieldInfo.setFieldValue(String.valueOf(_map.get(fieldInfo.getFieldName())));
			}

		}
		return infos;
	}

	public void update(String tableName, List list, String id)
	{
		StringBuffer sql = new StringBuffer((new StringBuilder("UPDATE ")).append(tableName).append(" SET ").toString());
		for (int i = 0; i < list.size(); i++)
		{
			FieldInfo o = (FieldInfo)list.get(i);
			String columName = o.getFieldName();
			sql = StringUtil.addSqlString(sql, o, (new StringBuilder(String.valueOf(columName))).append("=").toString());
			if (i != list.size() - 1)
				sql.append(",");
		}

		sql.append(" WHERE id = '").append(id).append("'");
		if (list.size() > 0)
		{
			System.out.println((new StringBuilder("update:")).append(sql.toString()).toString());
			getJtN().execute(sql.toString());
		}
		try
		{
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].update");
			logItem.setLevel("info");
			logItem.setLogid(Guid.get());
			logItem.setDesc("更新成功");
			Log.write(logItem);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			logItem.setMethod("insert");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("更新表数据失败");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("更新表数据时发生异常");
		}
	}

	public void update_gwInfo(Map formValues, List list, String businessId)
	{
		logItem = new LogItem();
		StringBuffer sb = new StringBuffer();
		String gw_zd[] = {
			"sdcncsi_ict_gwbt", "sdcncsi_ict_mj", "sdcncsi_ict_ngr", "sdcncsi_ict_ztc", "sdcncsi_ict_zs", "sdcncsi_ict_cs"
		};
		String gw_zd_update = "";
		String as[] = gw_zd;
		int j = 0;
		for (int k = as.length; j < k; j++)
		{
			String temp = as[j];
			for (Iterator iterator1 = list.iterator(); iterator1.hasNext();)
			{
				FieldInfo info = (FieldInfo)iterator1.next();
				String fieldname = info.getFieldName();
				if (temp.equals(fieldname))
					gw_zd_update = (new StringBuilder(String.valueOf(gw_zd_update))).append(",").append(temp).toString();
			}

		}

		if (!gw_zd_update.equals(""))
		{
			gw_zd_update = gw_zd_update.replaceFirst(",", "");
			String gw_zd_filter[] = gw_zd_update.split(",");
			sb.append("update t_gw_info set ");
			try
			{
				for (int i = 0; i < gw_zd_filter.length; i++)
					if (i == gw_zd_filter.length - 1)
						sb.append(gw_zd_filter[i]).append("='").append(formValues.get(gw_zd_filter[i])).append("'");
					else
						sb.append(gw_zd_filter[i]).append("='").append(formValues.get(gw_zd_filter[i])).append("',");

				List _list = getJtN().queryForList((new StringBuilder("select id from (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) l where entry_id='")).append(businessId).append("'").toString());
				String id = "";
				for (Iterator iterator = _list.iterator(); iterator.hasNext();)
				{
					Map _map = (Map)iterator.next();
					id = (new StringBuilder(String.valueOf(id))).append(",").append(_map.get("id")).toString();
				}

				id = id.replaceFirst(",", "");
				id = id.replaceAll(",", "','");
				sb.append(" where businessId in ('").append(id).append("')");
				getJtN().update(sb.toString());
				logItem.setLogid(Guid.get());
				logItem.setMethod("update_gwInfo");
				logItem.setLevel("info");
				logItem.setDesc("更新成功");
				Log.write(logItem);
			}
			catch (Exception e)
			{
				String guid = Guid.get();
				logItem.setMethod("update_gwInfo");
				logItem.setLogid(guid);
				logItem.setLevel("error");
				logItem.setDesc("更新失败");
				logItem.setContent(e.toString());
				Log.write(logItem);
				throw new ServiceException("更新失败");
			}
		}
	}

	public List getColumnNames(String tableName)
	{
		String sql = (new StringBuilder("SELECT c.componentname,c.componenttype,c.stypeid,c.remark,c.inserttype,c.datatype FROM t_form_columntype  c INNER JOIN t_form_template_info  f ON c.tableid = f.id WHERE f.tablename = '")).append(tableName).append("'").toString();
		List list = new ArrayList();
		FieldInfo fieldInfo = null;
		List _list = getJtN().queryForList(sql);
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add(fieldInfo))
		{
			Map map = (Map)iterator.next();
			fieldInfo = new FieldInfo();
			fieldInfo.setFieldName((String)map.get("componentname"));
			fieldInfo.setComponentType((String)map.get("componenttype"));
			if (map.get("stypeid") != null)
				fieldInfo.setSequenceTypeId((String)map.get("stypeid"));
			fieldInfo.setRemark((String)map.get("remark"));
			fieldInfo.setInsertType((String)map.get("inserttype"));
			if ("2".equalsIgnoreCase((String)map.get("inserttype")))
				fieldInfo.setFieldValue(null);
			fieldInfo.setFieldType((String)map.get("datatype"));
		}

		fieldInfo = new FieldInfo();
		fieldInfo.setFieldName("id");
		fieldInfo.setComponentType("HIDDEN");
		list.add(fieldInfo);
		return list;
	}

	public List getColumnDataTypes(String tableName)
	{
		String sql = (new StringBuilder("SELECT c.componentname,c.datatype,c.inserttype,c.stypeid,c.tdid FROM t_form_columntype  c INNER JOIN t_form_template_info  f ON f.id = c.tableid WHERE f.tableName='")).append(tableName).append("'").toString();
		List list = new ArrayList();
		FieldInfo info = null;
		List _list = getJtN().queryForList(sql);
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add(info))
		{
			Map map = (Map)iterator.next();
			info = new FieldInfo();
			info.setFieldName((String)map.get("componentname"));
			info.setFieldType((String)map.get("datatype"));
			info.setInsertType((String)map.get("inserttype"));
			info.setFieldValue(String.valueOf(map.get("stypeid")));
			info.setTdId((String)map.get("tdid"));
		}

		return list;
	}

	public List getComponentByTd(String td)
	{
		List list = null;
		String sql = (new StringBuilder("SELECT componentname FROM t_form_columntype WHERE tdid = '")).append(td).append("'").toString();
		List _list = getJtN().queryForList(sql);
		list = new ArrayList();
		Map map;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add((String)map.get("componentname")))
			map = (Map)iterator.next();

		return list;
	}

	public List getComponentByTableName(String tableName)
	{
		String sql = (new StringBuilder("SELECT f.tdid FROM t_form_authority  f INNER JOIN t_form_template_info  i on i.id = f.tableid WHERE tableName = '")).append(tableName).append("'").toString();
		List list = new ArrayList();
		List _list = getJtN().queryForList(sql);
		list = new ArrayList();
		Map map;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add((String)map.get("tdid")))
			map = (Map)iterator.next();

		return list;
	}

	public int getLastInsertId()
	{
		String sql = "";
		try
		{
			if (SysPara.getValue("db_type").equals("mysql"))
				sql = "SELECT LAST_INSERT_ID() as ID";
			if (SysPara.getValue("db_type").equals("sqlserver"))
				sql = "SELECT @@IDENTITY as ID";
			if (SysPara.getValue("db_type").equals("oracle"))
				sql = "SELECT sequence.currval as ID from dual";
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		int result = 0;
		List _list = getJtN().queryForList(sql);
		result = Integer.parseInt(String.valueOf(((Map)_list.get(0)).get("ID")));
		return result;
	}

	public void insertAttitude(Attitude attitude)
	{
		String id = Db.getGUID();
		String sql = (new StringBuilder("INSERT INTO t_form_attitude (id,tableid,formid,content,logincode,insertdate,columnid) VALUES ('")).append(id).append("','").append(attitude.getTableId()).append("','").append(attitude.getFormId()).append("','").append(attitude.getContent()).append("','").append(attitude.getLoginCode()).append("',").append(DBUtil.getDateFun()).append(",'").append(attitude.getColumnId()).append("')").toString();
		try
		{
			getJtN().execute(sql);
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].insertAttitude");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存会签意见成功");
			Log.write(logItem);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			logItem.setMethod("insertAttitude");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存会签意见异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("保存会签意见异常");
		}
	}
	public void insertAttitude1(Attitude attitude,String stepId)
	{
		String id = Db.getGUID();
		String sql = (new StringBuilder("INSERT INTO t_form_attitude (id,tableid,formid,content,logincode,insertdate,columnid,stepid) VALUES ('")).append(id).append("','").append(attitude.getTableId()).append("','").append(attitude.getFormId()).append("','").append(attitude.getContent()).append("','").append(attitude.getLoginCode()).append("',").append(DBUtil.getDateFun()).append(",'").append(attitude.getColumnId()).append("','").append(stepId).append("')").toString();
		try
		{
			getJtN().execute(sql);
			logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].insertAttitude");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存会签意见成功");
			Log.write(logItem);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			logItem.setMethod("insertAttitude");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存会签意见异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("保存会签意见异常");
		}
	}
	public List getAttitudeList(String tableName, String formId, String columnName)
	{
		String signature_enabled = "";
		StringBuffer sb = new StringBuffer();
		try
		{
			signature_enabled = SysPara.getValue("signature_enabled");
		}
		catch (Exception ex)
		{
			signature_enabled = "false";
		}
		String sql = "";
		if ("true".equals(signature_enabled))
		{
			if (SysPara.compareValue("px_huiqian", "1"))
			{
				sb.append("SELECT  a.id,a.formid,a.tableid,a.content,");
				sb.append("(select xm from t_org_yh where yhid=a.logincode) as temp,");
				sb.append("(select kzz from t_form_kz where yhid=a.logincode and kz_dm=2700) as logincode,");
				sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
				sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
				sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
				sb.append(" INNER JOIN t_org_tree o  ON o.lxid = a.logincode ");
				sb.append(" left outer join  t_org_yh_px p on p.yhid=a.logincode ");
				sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
				sb.append(tableName).append("' AND c.componentname = '").append(columnName);
				sb.append("' order by p.pxxh,o.xh");
			} else
			{
				sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
				sb.append("(select xm from t_org_yh where yhid=a.logincode) as temp,");
				sb.append("(select kzz from t_form_kz where yhid=a.logincode and kz_dm=2700) as logincode,");
				sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
				sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
				sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
				sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
				sb.append(tableName).append("' AND c.componentname = '").append(columnName);
				sb.append("' order by a.insertdate");
			}
		} else
		if (SysPara.compareValue("px_huiqian", "1"))
		{
			sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
			sb.append("(select xm from t_org_yh where yhid=a.logincode) as logincode,");
			sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
			sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
			sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
			sb.append(" INNER JOIN t_org_tree o  ON o.lxid = a.logincode ");
			sb.append(" left outer join  t_org_yh_px p on p.yhid=a.logincode ");
			sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
			sb.append(tableName).append("' AND c.componentname = '").append(columnName);
			sb.append("'  order by p.pxxh,o.xh");
		} else
		{
			sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
			sb.append("(select xm from t_org_yh where yhid=a.logincode) as logincode,");
			sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
			sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
			sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
			sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
			sb.append(tableName).append("' AND c.componentname = '").append(columnName);
			sb.append("' order by a.insertdate");
		}
		List _list = getJtN().queryForList(sb.toString());
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = _list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}

		_list.clear();
		_list.addAll(newList);
		List list = new ArrayList();
		Attitude attitude = null;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add(attitude))
		{
			Map map = (Map)iterator.next();
			attitude = new Attitude();
			attitude.setId((String)map.get("id"));
			attitude.setFormId((String)map.get("formid"));
			attitude.setTableId((String)map.get("tableid"));
			String content = (String)map.get("content");
			content = content.replace("\n", "</br>");
			attitude.setContent(content);
			attitude.setLoginCode((String)map.get("logincode"));
			if ("true".equals(signature_enabled) && map.get("logincode") == null)
				attitude.setTemp((String)map.get("temp"));
			String insertdate = map.get("insertdate").toString();
			attitude.setInsertDate(insertdate.substring(0, insertdate.indexOf(".")));
			attitude.setInsertDateTime(new Date(((Timestamp)map.get("insertdate")).getTime()));
			attitude.setColumnId((String)map.get("columnid"));
		}

		return list;
	}
	public List getAttitudeList2(String tableName, String formId, String columnName,String yhid)
	{
		
		String signature_enabled = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
		sb.append("(select xm from t_org_yh where yhid=a.logincode) as temp,");
		sb.append("(select kzz from t_form_kz where yhid=a.logincode and kz_dm=2700) as logincode,");
		sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
		sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
		sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
		sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
		sb.append(tableName).append("' AND c.componentname = '").append(columnName);
		sb.append("' and logincode <> '").append(yhid).append("' order by a.insertdate");
		List _list = getJtN().queryForList(sb.toString());
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = _list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}

		_list.clear();
		_list.addAll(newList);
		List list = new ArrayList();
		Attitude attitude = null;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add(attitude))
		{
			Map map = (Map)iterator.next();
			attitude = new Attitude();
			attitude.setId((String)map.get("id"));
			attitude.setFormId((String)map.get("formid"));
			attitude.setTableId((String)map.get("tableid"));
			String content = (String)map.get("content");
			content = content.replace("\n", "</br>");
			attitude.setContent(content);
			attitude.setLoginCode((String)map.get("logincode"));
			if ("true".equals(signature_enabled) && map.get("logincode") == null)
				attitude.setTemp((String)map.get("temp"));
			String insertdate = map.get("insertdate").toString();
			attitude.setInsertDate(insertdate.substring(0, insertdate.indexOf(".")));
			attitude.setInsertDateTime(new Date(((Timestamp)map.get("insertdate")).getTime()));
			attitude.setColumnId((String)map.get("columnid"));
		}

		return list;
	}
	public List getOpinionsList(String yhid)
	{
		List _list = null;
		List list = getJtN().queryForList("select kzz from t_org_yh_kz where yhid=? and kz_dm=307", new Object[] {
			yhid
		});
		if (list.size() > 0)
		{
			String public_list = (String)((Map)list.get(0)).get("kzz");
			if ("1".equals(public_list))
				_list = getJtN().queryForList("select yjnr,yjnr as yjlable from t_form_spyj where yhid=? or sfgk=1", new Object[] {
					yhid
				});
			else
				_list = getJtN().queryForList("select yjnr,yjnr as yjlable from t_form_spyj where yhid=?", new Object[] {
					yhid
				});
		} else
		{
			_list = getJtN().queryForList("select yjnr,yjnr as yjlable from t_form_spyj where yhid=? or sfgk=1", new Object[] {
				yhid
			});
		}
		if (!SysPara.compareValue("huiqian_exp", "1", "1"))
		{
			Map map = new HashMap();
			map.put("yjlable", "签名");
			String xm = (String)Cache.getUserInfo(yhid, "xm");
			map.put("yjnr", xm);
			_list.add(map);
		}
		return _list;
	}

	public List getAttitudeList_wap(String tableName, String formId, String columnName)
	{
		StringBuffer sb = new StringBuffer();
		if (SysPara.compareValue("px_huiqian", "1"))
		{
			sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
			sb.append("(select xm from t_org_yh where yhid=a.logincode) as logincode,");
			sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
			sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
			sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
			sb.append(" INNER JOIN t_org_tree o  ON o.lxid = a.logincode ");
			sb.append(" left outer join  t_org_yh_px p on p.yhid=a.logincode ");
			sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
			sb.append(tableName).append("' AND c.componentname = '").append(columnName);
			sb.append("'  order by p.pxxh,o.xh");
		} else
		{
			sb.append("SELECT a.id,a.formid,a.tableid,a.content,");
			sb.append("(select xm from t_org_yh where yhid=a.logincode) as logincode,");
			sb.append("a.insertdate,a.columnid FROM t_form_attitude  a");
			sb.append(" INNER JOIN t_form_columntype  c ON c.id = a.columnid ");
			sb.append(" INNER JOIN t_form_template_info  f ON f.id = a.tableid ");
			sb.append("WHERE a.formid = '").append(formId).append("' and f.tablename ='");
			sb.append(tableName).append("' AND c.componentname = '").append(columnName);
			sb.append("' order by a.insertdate");
		}
		List _list = getJtN().queryForList(sb.toString());
		String exp = "";
		try
		{
			exp = SysPara.getValue("huiqian_exp");
		}
		catch (Exception e)
		{
			exp = "${content}(${username})${insertdate}${inserttime}";
		}
		Map map;
		String content;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); map.put("content", content))
		{
			map = (Map)iterator.next();
			content = exp.replace("${content}", Str.filterNull(String.valueOf(map.get("content"))));
			content = content.replace("${username}", Str.filterNull(String.valueOf(map.get("logincode"))));
			if (exp.indexOf("${insertdate}") > 0)
				content = content.replace("${insertdate}", (new SimpleDateFormat("yyyy-MM-dd")).format((Timestamp)map.get("insertdate")));
			if (exp.indexOf("${inserttime}") > 0)
				content = content.replace("${inserttime}", (new SimpleDateFormat(" HH:mm:ss")).format((Timestamp)map.get("insertdate")));
		}

		return _list;
	}

	public void insertHistory(String tableName, List list, String id, boolean isNew)
	{
		StringBuffer sql = new StringBuffer((new StringBuilder("INSERT INTO  form_")).append(tableName).append("(id,ver,pid").toString());
		FieldInfo fieldInfo;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); sql.append(",").append(fieldInfo.getFieldName()))
			fieldInfo = (FieldInfo)iterator.next();

		sql.append(") SELECT id,");
		int ver = 1;
		if (!isNew)
			ver = getMaxVersion(tableName, id) + 1;
		sql.append(ver).append(",").append(id);
		for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); sql.append(",").append(fieldInfo.getFieldName()))
			fieldInfo = (FieldInfo)iterator1.next();

		sql.append(" FROM ").append(tableName).append(" WHERE id='").append(id).append("'");
		getJtA().execute(sql.toString());
		logItem.setMethod("[com.coffice.form.dao.imp.MySqlTableDaoImpl].insertHistory");
		logItem.setLevel("info");
		logItem.setDesc("保存成功");
		Log.write(logItem);
	}

	private int getMaxVersion(String tableName, String pid)
	{
		String sql = (new StringBuilder("SELECT MAX(ver) AS ver FROM  form_")).append(tableName).append(" WHERE pid = ").append(pid).toString();
		int verion = 0;
		String ver_Str = (String)getJtA().queryForObject(sql, String.class);
		if (ver_Str != null)
			verion = Integer.parseInt(ver_Str);
		return verion;
	}

	public String getIdByTableName(String tableName)
		throws SQLException
	{
		String sql = (new StringBuilder("SELECT(id) FROM t_form_template_info  f WHERE tableName='")).append(tableName).append("'").toString();
		String r = "";
		List list = getJtN().queryForList(sql);
		if (list.size() > 0)
			r = (String)((Map)list.get(0)).get("id");
		return r;
	}

	public String getAttitudeId(String tableName, String formId, String loginCode, String columnId)
		throws SQLException
	{
		String r = "";
		String sql = (new StringBuilder("SELECT  a.id AS ids FROM t_form_attitude  a INNER JOIN t_form_template_info  f on f.id = a.tableid WHERE f.tableName = '")).append(tableName).append("' AND a.formid = '").append(formId).append("' AND a.logincode = '").append(loginCode).append("' AND columnid = '").append(columnId).append("'").toString();
		List list = getJtN().queryForList(sql);
		if (list.size() > 0)
			r = (String)((Map)list.get(0)).get("ids");
		return r;
	}
	
	public String getAttitudeId1(String tableName, String formId, String loginCode, String columnId,String stepId)
	throws SQLException
{
	String r = "";
	String sql = (new StringBuilder("SELECT  a.id AS ids FROM t_form_attitude  a INNER JOIN t_form_template_info  f on f.id = a.tableid WHERE f.tableName = '")).append(tableName).append("' AND a.formid = '").append(formId).append("' AND a.logincode = '").append(loginCode).append("' AND columnid = '").append(columnId).append("' and stepid='").append(stepId).append("'").toString();
	List list = getJtN().queryForList(sql);
	if (list.size() > 0)
		r = (String)((Map)list.get(0)).get("ids");
	return r;
}

	public void updateAttitude(Attitude att)
		throws SQLException
	{
		String sql = (new StringBuilder("UPDATE t_form_attitude SET content = '")).append(att.getContent()).append("' WHERE id =  '").append(att.getId()).append("'").toString();
		try
		{
			getJtN().update(sql);
			logItem.setMethod("updateAttitude");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("更新成功");
			Log.write(logItem);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			logItem.setMethod("updateAttitude");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("更新会签意见异常");
			logItem.setContent(ex.toString());
			Log.write(logItem);
			throw new ServiceException("更新会签意见异常");
		}
	}

	public void createPrintInfo(String formId, String templateName, String fileName)
	{
		String sql = (new StringBuilder("INSERT INTO t_form_printinfo VALUES('")).append(Db.getGUID()).append("',").append(formId).append(",'").append(templateName).append("','").append(fileName).append("')").toString();
		logItem = new LogItem();
		try
		{
			getJtN().update(sql);
			logItem.setMethod("createPrintInfo");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("保存成功");
			Log.write(logItem);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("createPrintInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存失败");
			logItem.setContent(e.toString());
			Log.write(logItem);
			throw new ServiceException("保存失败");
		}
	}

	public List getPrintTemplateFileNames(String tableName)
	{
		String sql = (new StringBuilder("SELECT p.templatefilename FROM t_form_printinfo  p INNER JOIN t_form_template_info  f ON f.id = p.formid WHERE f.tableName = '")).append(tableName).append("'").toString();
		List _list = getJtN().queryForList(sql);
		List list = new ArrayList();
		Map map;
		for (Iterator iterator = _list.iterator(); iterator.hasNext(); list.add(String.valueOf(map.get("templatefilename"))))
			map = (Map)iterator.next();

		return list;
	}

	public Map getColumns(String tableId, String ids[])
	{
		StringBuilder sql = new StringBuilder((new StringBuilder("SELECT * FROM t_form_columntype WHERE tableid = ")).append(tableId).append(" AND componentname IN (").toString());
		int l = ids.length;
		for (int i = 0; i < l; i++)
		{
			String id = ids[i];
			sql.append("'").append(id).append("'");
			if (i != l - 1)
				sql.append(",");
		}

		sql.append(")");
		List list = getJtN().queryForList(sql.toString());
		Map map = new HashMap();
		FieldInfo info = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); map.put(info.getFieldName(), info))
		{
			Map _map = (Map)iterator.next();
			info = new FieldInfo();
			info.setFieldName(String.valueOf(_map.get("componentname")));
			info.setComponentType(String.valueOf(_map.get("componenttype")));
			info.setInsertType(String.valueOf(_map.get("inserttype")));
		}

		return map;
	}

	public String getColumnId(String tableId, String columnName)
		throws SQLException
	{
		String sql = (new StringBuilder("select id from t_form_columntype where tableid = '")).append(tableId).append("' and componentname = '").append(columnName).append("'").toString();
		String returnString = "";
		List list = getJtN().queryForList(sql);
		if (list.size() > 0)
			returnString = (String)((Map)list.get(0)).get("id");
		return returnString;
	}
}

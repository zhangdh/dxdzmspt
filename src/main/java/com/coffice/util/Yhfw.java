package com.coffice.util;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
 
import org.springframework.transaction.annotation.Transactional;

import com.coffice.form.util.DBUtil;
 
import com.coffice.bean.UserBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Str;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;

public class Yhfw extends BaseUtil
{
  public static List list(String ywid, Integer fsfw_dm)
  {
    List list = null;
    List _list = new ArrayList();
    if (fsfw_dm == null)
      list = Db.getJtA().queryForList("select xzlx_dm,xzlxz  from t_yhfw where ywid=?", new Object[] { ywid });
    else
      list = Db.getJtA().queryForList("select xzlx_dm,xzlxz  from t_yhfw where ywid=? and fsfw_dm=?", 
        new Object[] { ywid, fsfw_dm });

    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { 
    	Map map = (Map)localIterator.next();
      if (String.valueOf(map.get("xzlx_dm")).equals("201")) {
        Map m = Db.getJtA().queryForMap("select bmid as dm,mc from t_org_bm where bmid=?", 
          new Object[] { map.get("xzlxz") });
        _list.add(m);  }
      if (String.valueOf(map.get("xzlx_dm")).equals("202")) {
    	  Map m = Db.getJtA().queryForMap("select gwid as dm,mc from t_org_gw where gwid=?", 
          new Object[] { map.get("xzlxz") });
        _list.add(m);  }
      if (String.valueOf(map.get("xzlx_dm")).equals("203")) {
    	  Map  m = Db.getJtA().queryForMap("select jsid as dm,mc from t_org_js where jsid=?", 
          new Object[] { map.get("xzlxz") });
        _list.add(m);   }
      if (String.valueOf(map.get("xzlx_dm")).equals("204")) {
    	  Map m = Db.getJtA().queryForMap("select yhid as dm,xm as mc from t_org_yh where yhid=?", 
          new Object[] { map.get("xzlxz") });
    	  _list.add(m); }
      if ((String.valueOf(map.get("xzlx_dm")).equals("205"))){
    	  Map m = Db.getJtA().queryForMap("select guid as dm, mc from t_org_yhz where guid=?", 
        new Object[] { map.get("xzlxz") });
        _list.add(m);
      }
    }

    Map _map = new HashMap();
    _map.put("dm", "-");
    _map.put("mc", "请选择");
    _list.add(0, _map);
    return _list;
  }

  public static String getYhfw(String ywid, String fsfw_dm)
  {
    String xzlxz;
    StringBuffer sql = new StringBuffer();
    StringBuffer fsfw = new StringBuffer();
    if ((fsfw_dm == null) || (fsfw_dm.equals("")))
      sql.append("select xzlx_dm,xzlxz from t_yhfw where ywid='").append(ywid).append("'");
    else
      sql.append("select xzlx_dm,xzlxz from t_yhfw where ywid='").append(ywid).append("' and fsfw_dm='").append(
        fsfw_dm).append
        ("'");

    List list = Db.getJtA().queryForList(sql.toString());
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      int xzlx_dm = Integer.parseInt(map.get("xzlx_dm").toString());
      xzlxz = (String)map.get("xzlxz");
      switch (xzlx_dm)
      {
      case 201:
        fsfw.append("bm").append(xzlxz).append(",");
        break;
      case 202:
        fsfw.append("gw").append(xzlxz).append(",");
        break;
      case 203:
        fsfw.append("js").append(xzlxz).append(",");
        break;
      case 204:
        fsfw.append("yh").append(xzlxz).append(",");
        break;
      case 205:
        fsfw.append("zdy").append(xzlxz).append(",");
      }
    }

    return fsfw.toString();
  }

  public static List getYhfwList(String fsfw)
  {
    String[] fsfw_arr = fsfw.split(",");
    List _list = new ArrayList();
    String[] arrayOfString1 = fsfw_arr; int i = 0; for (int j = arrayOfString1.length; i < j; ++i) { Map m;
      String temp = arrayOfString1[i];
      if (temp.startsWith("bm")) {
        m = Db.getJtA().queryForMap("select bmid as dm,mc from t_org_bm where bmid=?", 
          new Object[] { temp.replaceFirst("bm", "") });
        _list.add(m);
      } else if (temp.startsWith("gw")) {
        m = Db.getJtA().queryForMap("select gwid as dm,mc from t_org_gw where gwid=?", 
          new Object[] { temp.replaceFirst("gw", "") });
        _list.add(m);
      } else if (temp.startsWith("js")) {
        m = Db.getJtA().queryForMap("select jsid as dm,mc from t_org_js where jsid=?", 
          new Object[] { temp.replaceFirst("js", "") });
        _list.add(m);
      } else if (temp.startsWith("yh")) {
        m = Db.getJtA().queryForMap("select yhid as dm,xm as mc from t_org_yh where yhid=?", 
          new Object[] { temp.replaceFirst("yh", "") });
        _list.add(m);
      } else if (temp.startsWith("zdy")) {
        m = Db.getJtA().queryForMap("select guid as dm, mc from t_org_yhz where guid=?", 
          new Object[] { temp.replaceFirst("zdy", "") });
        _list.add(m);
      }
    }
    return _list;
  }

  @Transactional
  public static void deleteYhfw(String ywid)
  {
    LogItem logItem = new LogItem();
    try {
      Db.getJtA().update("delete from t_yhfw where ywid=?", new Object[] { ywid });
      Db.getJtA().update("delete from t_yhfw_yd where ywid=?", new Object[] { ywid });
    } catch (Exception e) {
      String guid = Guid.get();
      logItem.setMethod("deleteYhfw");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("删除用户范围记录时出错");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("删除用户范围记录时出错");
    }
  }

  public static void save(UserBean userbean, String ywid, String mk_dm, String sys_fsfw, String fsfw_dm, int mkurllb, String bt, int ydbz)
	{
		if (sys_fsfw != null && !sys_fsfw.equals(""))
		{
			LogItem logItem = new LogItem();
			Yhfw yhfw = new Yhfw();
			String sys_fsfw_list[] = sys_fsfw.split(",");
			int xzlx_dm = 0;
			String xzlxz = "";
			Set set = new HashSet();
			try
			{
				String as[] = sys_fsfw_list;
				int i = 0;
				for (int j = as.length; i < j; i++)
				{
					String fsfw = as[i];
					if (fsfw.startsWith("bm"))
					{
						xzlx_dm = 201;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("gw"))
					{
						xzlx_dm = 202;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("js"))
					{
						xzlx_dm = 203;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("yh"))
					{
						xzlx_dm = 204;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("zz"))
					{
						xzlx_dm = 200;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("zdy"))
					{
						xzlx_dm = 205;
						xzlxz = fsfw.substring(3, fsfw.length());
					}
					Db.getJtN().update((new StringBuilder("insert into t_yhfw(guid,zzid,bmid,gwid,yhid,ywid,mk_dm,xzlx_dm,xzlxz,fsfw_dm,cjsj)values(?,?,?,?,?,?,?,?,?,?,")).append(DBUtil.getDateFun()).append(")").toString(), new Object[] {
						Db.getGUID(), userbean.getZzid(), userbean.getBmid(), userbean.getGwid(), userbean.getYhid(), ywid, mk_dm, Integer.valueOf(xzlx_dm), xzlxz, fsfw_dm
					});
				}

				if (ydbz == 1)
				{
					int db_dy = 1;
					List list = Db.getJtN().queryForList("select db_dy from t_todo_dm where mk_dm=?", new Object[] {
						mk_dm
					});
					for (Iterator iterator1 = list.iterator(); iterator1.hasNext();)
					{
						Map map = (Map)iterator1.next();
						db_dy = Integer.parseInt(map.get("db_dy").toString());
					}

					if (db_dy == 1 && "103".equals(mk_dm))
					{
						List _list = Db.getJtN().queryForList((new StringBuilder("select id from (select * from t_oswf_work_item union select * from t_oswf_work_item_his) l where entry_id=(select entry_id from t_oswf_work_item  where id='")).append(ywid).append("'  union select entry_id from t_oswf_work_item_his where id='").append(ywid).append("')").toString());
						StringBuffer ids = new StringBuffer();
						Map map;
						for (Iterator iterator2 = _list.iterator(); iterator2.hasNext(); ids.append("','").append(map.get("id")))
							map = (Map)iterator2.next();

						if (_list.size() > 0)
							ids = ids.replace(0, 2, "").append("'");
						else
							ids.append("''");
						Db.getJtN().update((new StringBuilder("delete from t_yhfw_yd where ywid in(")).append(ids.toString()).append(")").toString());
					}
					if (SysPara.compareValue("sms_batchupdate", "true"))
					{
						StringBuffer sql = new StringBuffer();
						if (db_dy == 1)
						{
							if (SysPara.compareValue("db_type", "oracle"))
							{
								sql.append("insert into t_yhfw_yd(zzid,yhid,guid,mk_dm,ywid,mkurllb,bt,cjsj) select '").append(userbean.getZzid()).append("',yhid,YHFW_SEQUENCE.nextval,'");
								sql.append(mk_dm).append("','").append(ywid).append("','").append(mkurllb).append("','");
								sql.append(bt).append("',sysdate from (").append(getSql_YhidList(sys_fsfw));
								sql.append(") a");
							} else
							{
								sql.append("insert into t_yhfw_yd(zzid,yhid,mk_dm,ywid,mkurllb,bt,cjsj) select '").append(userbean.getZzid()).append("',yhid,'");
								sql.append(mk_dm).append("','").append(ywid).append("','").append(mkurllb).append("','");
								if (SysPara.compareValue("db_type", "sqlserver"))
									sql.append(bt).append("',").append(getDateStr()).append(" from (").append(getSql_YhidList(sys_fsfw));
								else
									sql.append(bt).append("',now() from (").append(getSql_YhidList(sys_fsfw));
								sql.append(") a");
							}
						} else
						if (db_dy == 0)
						{
							sql.append("insert into t_oswf_work_item(Entry_ID,Step_ID,value,formname,mk_dm,mkurllb,lclb_dm,cjsj) select '-1','-1',yhid,'").append(bt);
							sql.append("','").append(mk_dm).append("','").append(mkurllb).append("','");
							sql.append(bt).append(",now() from (").append(getSql_YhidList(sys_fsfw));
							sql.append(") a");
						}
						Db.getJtN().update(sql.toString());
					} else
					{
						set = yhfw.getYhList(sys_fsfw);
						if (db_dy == 1)
						{
							Iterator it = set.iterator();
							for (String yhid = ""; it.hasNext() && !"".equals(yhid = (String)it.next()); Db.getJtN().update((new StringBuilder("insert into t_yhfw_yd(guid,zzid,yhid,mk_dm,ywid,mkurllb,bt,cjsj)values(?,?,?,?,?,?,?,")).append(DBUtil.getDateFun()).append(")").toString(), new Object[] {
	Db.getGUID(), userbean.getZzid(), yhid, mk_dm, ywid, Integer.valueOf(mkurllb), bt
}));
						} else
						if (db_dy == 0)
						{
							for (Iterator it = set.iterator(); it.hasNext() && !"".equals(it.next()); Db.getJtN().update((new StringBuilder("insert into t_oswf_work_item(Id,Entry_ID,Step_ID,value,formname,mk_dm,mkurllb,lclb_dm,cjsj)values(?,?,?,?,?,?,?,?,")).append(DBUtil.getDateFun()).append(")").toString(), new Object[] {
	Db.getGUID(), "-1", "-1", it.next(), bt, mk_dm, Integer.valueOf(mkurllb), Integer.valueOf(1602)
}));
						}
					}
				}
			}
			catch (Exception ex)
			{
				System.out.println(ex.toString());
				String guid = Guid.get();
				logItem.setMethod("[com.coffice.util.yhfw].save");
				logItem.setLogid(guid);
				logItem.setLevel("error");
				logItem.setDesc("保存数据时出现异常");
				logItem.setContent(ex.toString());
				System.out.println(ex.toString());
				Log.write(logItem);
				throw new ServiceException("保存数据时异常");
			}
		} else
		if ("103".equals(mk_dm))
		{
			List _list = Db.getJtN().queryForList((new StringBuilder("select id from (select * from t_oswf_work_item union select * from t_oswf_work_item_his) l where entry_id=(select entry_id from t_oswf_work_item  where id='")).append(ywid).append("'  union select entry_id from t_oswf_work_item_his where id='").append(ywid).append("')").toString());
			StringBuffer ids = new StringBuffer();
			Map map;
			for (Iterator iterator = _list.iterator(); iterator.hasNext(); ids.append("','").append(map.get("id")))
				map = (Map)iterator.next();

			if (_list.size() > 0)
				ids = ids.replace(0, 2, "").append("'");
			else
				ids.append("''");
			Db.getJtN().update((new StringBuilder("delete from t_yhfw_yd where ywid in(")).append(ids.toString()).append(")").toString());
		}
	}

	public static void save_wap(UserBean userbean, String ywid, String mk_dm, String sys_fsfw, String fsfw_dm, int mkurllb, String bt, int ydbz)
	{
		if (sys_fsfw != null && !sys_fsfw.equals(""))
		{
			LogItem logItem = new LogItem();
			Yhfw yhfw = new Yhfw();
			String sys_fsfw_list[] = sys_fsfw.split(",");
			String sys_fsfw_list_temp = "";
			HashSet bm_set = new HashSet();
			String as[] = sys_fsfw_list;
			int i = 0;
			for (int j = as.length; i < j; i++)
			{
				String fsfw = as[i];
				if (fsfw.startsWith("bm"))
				{
					List bm_list = Db.getJtA().queryForList((new StringBuilder("select bmid from t_org_bm where bmid like '%")).append(fsfw.replaceFirst("bm", "")).append("%' and zt_dm=1").toString());
					Map map;
					for (Iterator iterator = bm_list.iterator(); iterator.hasNext(); bm_set.add((String)map.get("bmid")))
						map = (Map)iterator.next();

				} else
				if (fsfw.startsWith("yh"))
					sys_fsfw_list_temp = (new StringBuilder(String.valueOf(sys_fsfw_list_temp))).append(fsfw.replaceFirst("bm", "")).append(",").toString();
			}

			for (Iterator iter = bm_set.iterator(); iter.hasNext();)
			{
				String temp = (String)iter.next();
				sys_fsfw_list_temp = (new StringBuilder(String.valueOf(sys_fsfw_list_temp))).append("bm").append(temp).append(",").toString();
			}

			String fsfwArr[] = sys_fsfw_list_temp.split(",");
			int xzlx_dm = 0;
			String xzlxz = "";
			Set set = new HashSet();
			try
			{
				String as1[] = fsfwArr;
				int k = 0;
				for (int l = as1.length; k < l; k++)
				{
					String fsfw = as1[k];
					if (fsfw.startsWith("bm"))
					{
						xzlx_dm = 201;
						xzlxz = fsfw.substring(2, fsfw.length());
					} else
					if (fsfw.startsWith("yh"))
					{
						xzlx_dm = 204;
						xzlxz = fsfw.substring(2, fsfw.length());
					}
					Db.getJtA().update("insert into t_yhfw(guid,zzid,bmid,gwid,yhid,ywid,mk_dm,xzlx_dm,xzlxz,fsfw_dm,cjsj)values(?,?,?,?,?,?,?,?,?,?,now())", new Object[] {
						Db.getGUID(), userbean.getZzid(), userbean.getBmid(), userbean.getGwid(), userbean.getYhid(), ywid, mk_dm, Integer.valueOf(xzlx_dm), xzlxz, fsfw_dm
					});
				}

				if (ydbz == 1)
				{
					int db_dy = 0;
					List list = Db.getJtA().queryForList("select db_dy from t_todo_dm where mk_dm=?", new Object[] {
						mk_dm
					});
					for (Iterator iterator1 = list.iterator(); iterator1.hasNext();)
					{
						Map map = (Map)iterator1.next();
						db_dy = Integer.parseInt(map.get("db_dy").toString());
					}

					if (SysPara.compareValue("sms_batchupdate", "true"))
					{
						StringBuffer sql = new StringBuffer();
						if (db_dy == 1)
						{
							if ("103".equals(mk_dm))
								Db.getJtN().update((new StringBuilder("delete from t_yhfw where ywid in(select a.id from t_oswf_work_item_his a,t_oswf_work_item_his b where a.entry_id=b.entry_id and b.id='")).append(ywid).append("')").toString());
							if (SysPara.compareValue("db_type", "oracle"))
							{
								sql.append("insert into t_yhfw_yd(zzid,yhid,guid,mk_dm,ywid,mkurllb,bt,cjsj) select '").append(userbean.getZzid()).append("',yhid,YHFW_SEQUENCE.nextval,'");
								sql.append(mk_dm).append("','").append(ywid).append("','").append(mkurllb).append("','");
								sql.append(bt).append("',sysdate from (").append(getSql_YhidList(sys_fsfw_list_temp));
								sql.append(") a");
							} else
							{
								sql.append("insert into t_yhfw_yd(zzid,yhid,mk_dm,ywid,mkurllb,bt,cjsj) select '").append(userbean.getZzid()).append("',yhid,'");
								sql.append(mk_dm).append("','").append(ywid).append("','").append(mkurllb).append("','");
								if (SysPara.compareValue("db_type", "sqlserver"))
									sql.append(bt).append("',").append(getDateStr()).append(" from (").append(getSql_YhidList(sys_fsfw_list_temp));
								else
									sql.append(bt).append("',now() from (").append(getSql_YhidList(sys_fsfw_list_temp));
								sql.append(") a");
							}
						} else
						if (db_dy == 0)
						{
							sql.append("insert into t_oswf_work_item(Entry_ID,Step_ID,value,formname,mk_dm,mkurllb,lclb_dm,cjsj) select '-1','-1',yhid,'").append(bt);
							sql.append("','").append(mk_dm).append("','").append(mkurllb).append("','");
							sql.append(bt).append(",now() from (").append(getSql_YhidList(sys_fsfw_list_temp));
							sql.append(") a");
						}
						Db.getJtN().update(sql.toString());
					} else
					if (db_dy == 1)
					{
						set = yhfw.getYhList(sys_fsfw);
						for (Iterator it = set.iterator(); it.hasNext(); Db.getJtA().update("insert into t_yhfw_yd(guid,zzid,yhid,mk_dm,ywid,mkurllb,bt,cjsj)values(?,?,?,?,?,?,?,now())", new Object[] {
	Db.getGUID(), userbean.getZzid(), it.next(), mk_dm, ywid, Integer.valueOf(mkurllb), bt
}));
					} else
					if (db_dy == 0)
					{
						for (Iterator it = set.iterator(); it.hasNext(); Db.getJtA().update("insert into t_oswf_work_item(Id,Entry_ID,Step_ID,value,formname,mk_dm,mkurllb,lclb_dm,cjsj)values(?,?,?,?,?,?,?,?,now())", new Object[] {
	Db.getGUID(), "-1", "-1", it.next(), bt, mk_dm, Integer.valueOf(mkurllb), Integer.valueOf(1602)
}));
					}
				}
			}
			catch (Exception ex)
			{
				String guid = Guid.get();
				logItem.setMethod("[com.coffice.util.yhfw].save");
				logItem.setLogid(guid);
				logItem.setLevel("error");
				logItem.setDesc("保存数据时出现异常");
				logItem.setContent(ex.toString());
				System.out.println(ex.toString());
				Log.write(logItem);
				throw new ServiceException("保存数据时异常");
			}
		}
	}

  public static String getMobileList(String sys_fsfw)
  {
    StringBuffer mobile = new StringBuffer();
    StringBuffer yh_mc = new StringBuffer();
    String result = "";
    Set set = new Yhfw().getYhList(sys_fsfw);
    Iterator it = set.iterator();
    while (it.hasNext()) {
      String temp = (String)it.next();
      if ((temp != null) && (!(temp.equals("")))) {
        Map map = Db.getJtA().queryForMap("select xm,sj from t_org_yh where yhid=? and zt_dm=1", 
          new Object[] { temp });
        String xm = (String)map.get("xm");
        String sj = (String)map.get("sj");
        if ((sj != null) && (!("".equals(sj))))
          mobile.append(",").append(sj);
        else
          yh_mc.append(",").append(xm);
      }
    }

    if (mobile.length() > 0)
      if (yh_mc.length() > 0)
        result = mobile.substring(1, mobile.length()) + ";" + yh_mc.substring(1, yh_mc.length());
      else
        result = mobile.substring(1, mobile.length()) + ";";
    else
      result = ";" + yh_mc.substring(1, yh_mc.length());

    return result;
  }

  private Set getYhList(String sys_fsfw)
  {
    int xzlx_dm = 0;
    String xzlxz = "";
    String[] sys_fsfw_list = (String[])null;
    HashSet set = new HashSet();
    if ((sys_fsfw != null) && (!("".equals(sys_fsfw)))) {
      sys_fsfw_list = sys_fsfw.substring(0, sys_fsfw.length() - 1).split(",");

      Yhfw yhfw = new Yhfw();
      String[] arrayOfString1 = sys_fsfw_list; int i = 0; for (int j = arrayOfString1.length; i < j; ++i) { String yhid;
        int k;
        int l;
        String[] arrayOfString2;
        String fsfw = arrayOfString1[i];
        if (fsfw.startsWith("bm")) {
          xzlx_dm = 201;
          xzlxz = fsfw.substring(2, fsfw.length());
        } else if (fsfw.startsWith("gw")) {
          xzlx_dm = 202;
          xzlxz = fsfw.substring(2, fsfw.length());
        } else if (fsfw.startsWith("js")) {
          xzlx_dm = 203;
          xzlxz = fsfw.substring(2, fsfw.length());
        } else if (fsfw.startsWith("yh")) {
          xzlx_dm = 204;
          xzlxz = fsfw.substring(2, fsfw.length());
        }
        String yhid_list = "";
        String[] yhid_arr = (String[])null;
        switch (xzlx_dm)
        {
        case 201:
          yhid_list = yhfw.getYhListByBmid(xzlxz);
          yhid_arr = yhid_list.split(",");
          arrayOfString2 = yhid_arr; k = 0; for (l = arrayOfString2.length; k < l; ++k) { yhid = arrayOfString2[k];
            set.add(yhid);
          }
          break;
        case 202:
          yhid_list = yhfw.getYhListByGwid(xzlxz);
          yhid_arr = yhid_list.split(",");
          arrayOfString2 = yhid_arr; k = 0; for (l = arrayOfString2.length; k < l; ++k) { yhid = arrayOfString2[k];
            set.add(yhid);
          }
          break;
        case 203:
          yhid_list = yhfw.getYhListByJsid(xzlxz);
          yhid_arr = yhid_list.split(",");
          arrayOfString2 = yhid_arr; k = 0; for (l = arrayOfString2.length; k < l; ++k) { yhid = arrayOfString2[k];
            set.add(yhid);
          }
          break;
        case 204:
          set.add(xzlxz);
        }
      }
    }

    return set;
  }

  public static String getSql_YhidList(String sys_fsfw)
  {
    String bmid_list = ""; String gwid_list = ""; String jsid_list = ""; String yhid_list = ""; String zzid = ""; String yhz_list = "";
    String[] sys_fsfw_list = (String[])null;
    StringBuffer sql = new StringBuffer();
    if (sys_fsfw.endsWith(","))
      sys_fsfw_list = sys_fsfw.substring(0, sys_fsfw.length() - 1).split(",");
    else
      sys_fsfw_list = sys_fsfw.split(",");

    String[] arrayOfString1 = sys_fsfw_list; int i = 0; for (int j = arrayOfString1.length; i < j; ++i) { String fsfw = arrayOfString1[i];
      if (fsfw.startsWith("bm"))
        bmid_list = bmid_list + ",'" + fsfw.substring(2, fsfw.length()) + "'";
      else if (fsfw.startsWith("gw"))
        gwid_list = gwid_list + ",'" + fsfw.substring(2, fsfw.length()) + "'";
      else if (fsfw.startsWith("js"))
        jsid_list = jsid_list + ",'" + fsfw.substring(2, fsfw.length()) + "'";
      else if (fsfw.startsWith("yh"))
        yhid_list = yhid_list + ",'" + fsfw.substring(2, fsfw.length()) + "'";
      else if (fsfw.startsWith("zz"))
        zzid = "'" + fsfw.substring(2, fsfw.length()) + "'";
      else if (fsfw.startsWith("zdy"))
        yhz_list = yhz_list + ",'" + fsfw.substring(3, fsfw.length()) + "'";
    }

    bmid_list = bmid_list.replaceFirst(",", "");
    gwid_list = gwid_list.replaceFirst(",", "");
    jsid_list = jsid_list.replaceFirst(",", "");
    yhid_list = yhid_list.replaceFirst(",", "");
    yhz_list = yhz_list.replaceFirst(",", "");
    if (!("".equals(bmid_list))) {
      sql.append("union select lxid as yhid from t_org_tree where sjbmid in(");
      sql.append(bmid_list);
      sql.append(") and lx_dm=204 and zt_dm=1 ");
    }
    if (!("".equals(gwid_list))) {
      sql.append("union select lxid as yhid from t_org_tree where sjid in(select guid from  t_org_tree where lxid  in(");
      sql.append(gwid_list);
      sql.append(")) ");
    }
    if (!("".equals(jsid_list))) {
      sql.append("union select yhid from t_org_yh_kz a where kz_dm='300' and kzz in(");
      sql.append(jsid_list);
      sql.append(") and exists (select yhid from t_org_yh where yhid=a.yhid and zt_dm=1) ");
    }
    if (!("".equals(yhid_list))) {
      sql.append("union select yhid from t_org_yh where yhid in(");
      sql.append(yhid_list);
      sql.append(") and zt_dm=1 ");
    }
    if (!("".equals(zzid))) {
      sql.append("union select yhid from t_org_yh where zzid ='");
      sql.append(zzid);
      sql.append("' and zt_dm=1 ");
    }
    if (!("".equals(yhz_list))) {
      sql.append("union select distinct yhid from t_org_yhz_fb where yhzid in(");
      sql.append(yhz_list);
      sql.append(") ");
    }

    return sql.toString().replaceFirst("union", "");
  }

  private String getYhListByBmid(String bmid)
  {
    StringBuffer yhid = new StringBuffer();
    List list = Db.getJtA().queryForList(
      "select lxid from t_org_tree where sjbmid like '" + bmid + "%' and lx_dm=204 and zt_dm=1");
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      yhid.append(",").append((String)map.get("lxid"));
    }
    return yhid.toString().replaceFirst(",", "");
  }

  private String getYhListByGwid(String gwid)
  {
    StringBuffer yhid = new StringBuffer();
    List list = Db.getJtA().queryForList(
      "select lxid from t_org_tree where sjid in (select guid from t_org_tree where lxid = '" + gwid + 
      "' ) and lx_dm=204 and zt_dm=1");
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      yhid.append(",").append((String)map.get("lxid"));
    }
    return yhid.toString().replaceFirst(",", "");
  }

  private String getYhListByJsid(String jsid)
  {
    StringBuffer yhid = new StringBuffer();
    List list = 
      Db.getJtA().queryForList
      (
      "select yhid from t_org_yh_kz a where kz_dm='300' and kzz=? and exists (select yhid from t_org_yh where yhid=a.yhid and zt_dm=1)", 
      new Object[] { jsid });
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      yhid.append(",").append((String)map.get("yhid"));
    }
    return yhid.toString().replaceFirst(",", "");
  }

  public static String getYhSQL(String yhid)
  {
    if ((yhid != null) && (yhid.startsWith("yh"))) {
      yhid = yhid.replaceAll(",", "");
      yhid = yhid.substring(2, yhid.length());
    }
    String SQL = " and t_yhfw.xzlx_dm='204' and t_yhfw.xzlxz='" + yhid + "'";
    return SQL;
  }

  public static String getBmSQL(String bmid)
  {
    if ((bmid != null) && (bmid.startsWith("bm"))) {
      bmid = bmid.replaceAll(",", "");
      bmid = bmid.substring(2, bmid.length());
    }
    String SQL = " and t_yhfw.xzlx_dm='201' and t_yhfw.xzlxz='" + bmid + "'";
    return SQL;
  }

  public static String getGwSQL(String gwid)
  {
    if ((gwid != null) && (gwid.startsWith("gw"))) {
      gwid = gwid.replaceAll(",", "");
      gwid = gwid.substring(2, gwid.length());
    }
    String SQL = " and t_yhfw.xzlx_dm='202' and t_yhfw.xzlxz='" + gwid + "'";
    return SQL;
  }

  public static String getJsSQL(String jsid)
  {
    if ((jsid != null) && (jsid.startsWith("js"))) {
      jsid = jsid.replaceAll(",", "");
      jsid = jsid.substring(2, jsid.length());
    }
    String SQL = " and t_yhfw.xzlx_dm='203' and t_yhfw.xzlxz='" + jsid + "'";
    return SQL;
  }

	public static String getAllSQL(String yhid)
	{
		StringBuffer SQL = new StringBuffer();
		if ("admin".equals(yhid))
		{
			SQL.append(" and (t_yhfw.xzlx_dm='204' and t_yhfw.xzlxz='").append(yhid).append("')");
			return SQL.toString();
		}
		if (yhid != null && yhid.startsWith("yh"))
		{
			yhid = yhid.replaceAll(",", "");
			yhid = yhid.substring(2, yhid.length());
		}
		StringBuffer bmid = new StringBuffer();
		StringBuffer gwid = new StringBuffer();
		StringBuffer jsid = new StringBuffer();
		UserBean bean = UserBean.get(yhid);
		List<Map> bm_list = bean.getBmid_list();
		List<Map> gw_list = bean.getGwid_list();
		List<Map> js_list = bean.getJsid_list();
		String yhz = "";
		if(SysPara.compareValue("enabled_yhz", "true")){
			List<Map> yhz_list =Db.getJtN().queryForList("select yhzid from t_org_yhz_fb where yhid=?",
					new Object[]{yhid});
			for(Map map: yhz_list){
				yhz = yhz+",'"+map.get("yhzid")+"'";
			}
			}
			for (Map map : bm_list) {
				bmid.append((String) map.get("dm")).append(",");
			}
			for (Map map : gw_list) {
				gwid.append((String) map.get("dm")).append(",");
			}
			for (Map map : js_list) {
				jsid.append((String) map.get("dm")).append(",");
			}

		SQL.append(" and ((t_yhfw.xzlx_dm='204' and t_yhfw.xzlxz='").append(yhid).append("')");
		SQL.append(" or (t_yhfw.xzlx_dm='203' and t_yhfw.xzlxz in ('").append(jsid.substring(0, jsid.length() - 1).replaceAll(",", "','")).append("'))");
		SQL.append(" or (t_yhfw.xzlx_dm='202' and t_yhfw.xzlxz in ('").append(gwid.substring(0, gwid.length() - 1).replaceAll(",", "','")).append("'))");
		SQL.append(" or (t_yhfw.xzlx_dm='201' and t_yhfw.xzlxz in ('").append(bmid.substring(0, bmid.length() - 1).replaceAll(",", "','")).append("'))");
		SQL.append(" or (t_yhfw.xzlx_dm='200' and t_yhfw.xzlxz ='").append(bean.getZzid()).append("')");
		if (!"".equals(yhz))
		{
			yhz = yhz.replaceFirst(",", "");
			SQL.append(" or (t_yhfw.xzlx_dm='205' and t_yhfw.xzlxz in (").append(yhz).append("))");
		}
		SQL.append(")");
		return SQL.toString();
	}

  public static String getAllSQL_Wap(String yhid)
  {
    return getAllSQL(yhid);
  }

  public static boolean spQx(String url, String yhid)
  {
    int count = Db.getJtA().queryForInt("select count(*) from t_qx_js a,t_qx_mx b where a.qxid = b.qxid and b.url='" + url + 
      "'  and (a.jsid='" + yhid + "' or jsid in(select kzz from t_org_yh_kz where kz_dm=300 and yhid='" + url + "'))");

    return (count > 0);
  }

  public static Map cxfw_list(String yhid, String tagname)
  {
    Map map;
    JspJsonData jjd = new JspJsonData();
    List list = new ArrayList();
    boolean isManager = ((Boolean)Cache.getUserInfo(yhid, "isManager")).booleanValue();
   /* if (Group.isGroup()) {
      if (isManager)
      {
        String zzid = (String)Cache.getUserInfo(yhid, "zzid");
        List list2 = Db.getJtN().queryForList("select a.zzid as dm,a.mc from t_org_bm a,t_org_bm_fb b where a.bmid = b.bmid and b.zzid<>'" + 
          zzid + "' and b.zzid like '" + zzid + "%' and a.zt_dm=1 order by b.zzid");
        if ((list2.size() > 0) || ("admin".equals(yhid))) {
          Map map3 = new HashMap();
          map3.put("dm", "syr");
          map3.put("mc", "所有人");
          list.add(map3);
        }
        if (!("admin".equals(yhid))) {
          Map map2 = new HashMap();
          map2.put("dm", "bdw");
          map2.put("mc", "本单位");
          list.add(map2);
        }

        list.addAll(list2);
      }

      map = new HashMap();
      map.put("dm", "br");
      map.put("mc", "本人");
      list.add(map);
    } else {*/
      if (isManager) {
        Map map3 = new HashMap();
        map3.put("dm", "syr");
        map3.put("mc", "所有人");
        list.add(map3);
      }
      map = new HashMap();
      map.put("dm", "br");
      map.put("mc", "本人");
      list.add(map);
   // }

    Map _map = new HashMap();
    _map.put("dm", "-");
    _map.put("mc", "请选择");
    list.add(0, _map);
    jjd.setSelect(tagname, list);
    return jjd.getData();
  }

	public static String cxfw_sql(String yhid, String fwbz, String tablename)
	{
		if ("admin".equals(yhid) && !"br".equals(fwbz))
			return "";
		StringBuffer sb = new StringBuffer();
		if (!"".equals(tablename))
			sb.append(" and ").append(tablename).append(".");
		else
			sb.append(" and ");
		if ("br".equals(fwbz))
			sb.append("yhid='").append(yhid).append("'");
		else
		if ("bdw".equals(fwbz))
		{
			String zzid = (String)Cache.getUserInfo(yhid, "zzid");
			sb.append("zzid='").append(zzid).append("'");
		} else
		if ("syr".equals(fwbz))
		{
			/*if (Group.isGroup())
			{
				String zzid = (String)Cache.getUserInfo(yhid, "zzid");
				sb.append("zzid like '").append(zzid).append("%'");
			} else
			{*/
				return "";
			//}
		} else
		if (!"".equals(Str.filterNull(fwbz)))
		{
			sb.append("zzid='").append(fwbz).append("'");
		} else
		{
			boolean isManager = ((Boolean)Cache.getUserInfo(yhid, "isManager")).booleanValue();
			if (isManager)
			{
				String zzid = (String)Cache.getUserInfo(yhid, "zzid");
				sb.append("zzid='").append(zzid).append("'");
			} else
			{
				sb.append("yhid='").append(yhid).append("'");
			}
		}
		return sb.toString();
	}

	private List getZzidList(String yhid)
	{
		List _list = Db.getJtN().queryForList("select b.zzid from t_org_tree a , t_org_bm b where a.sjbmid=b.bmid and a.lxid=?", new Object[] {
			yhid
		});
		return _list;
	}
}

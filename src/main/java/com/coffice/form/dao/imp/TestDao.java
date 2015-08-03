package com.coffice.form.dao.imp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.coffice.form.bean.Ss;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class TestDao extends BaseUtil
{
  LogItem logItem = new LogItem();
  private static final String SQL1 = "select * from t_form_sequencetype ";

  public List getAllTypes(Map map)
  {
    StringBuffer sb = new StringBuffer();
    List _list = null;
    if (SysPara.compareValue("form_list_mark", "true")) {
      if ("admin".equals(map.get("yhid")))
      {
        sb.append("select dm as id,zwmc as description from t_dm_lb a ,t_dm_lb_kz b");
        sb.append(" where a.dm =b.lb_dm ");
        sb.append(" and b.zybz='1' and b.zt_dm=1");
        sb.append(" and a.dm>=1000");

        _list = getJtN().queryForList(sb.toString());
      }
      else
      {
        sb.append("select dm as id,zwmc as description from t_dm_lb a ,t_dm_lb_kz b");
        sb.append(" where a.dm =b.lb_dm and (b.zzid='");
        sb.append(map.get("zzid"));
        sb.append("' or b.zzid='0') and b.zybz='1' and b.zt_dm=1");
        sb.append(" and a.dm>=1000");

        _list = getJtN().queryForList(sb.toString());
      }
    } else {
      sb.append("select id ,description  from t_form_sequencetype union ");
      sb.append(" select dm as id,zwmc as description from t_dm_lb where dm>=1000");
      _list = getJtN().queryForList(sb.toString());
    }
    Map _map = new HashMap();
    _map.put("id", Integer.valueOf(0));
    _map.put("description", "是|否");
    _list.add(_map);
    Map _map4 = new HashMap();
    _map4.put("id", Integer.valueOf(2));
    _map4.put("description", "有|无");
    _list.add(_map4);
    Map _map2 = new HashMap();
    _map2.put("id", Integer.valueOf(1));
    _map2.put("description", "车辆");
    _list.add(_map2);
    Map _map3 = new HashMap();
    _map3.put("id", Integer.valueOf(72));
    _map3.put("description", "考勤类别");
    _list.add(_map3);

    return _list;
  }

  public List<Ss> getUserSequencesByTypeId(int id) {
    List list = null;
    List _list = getJtA().queryForList("select * from t_form_sequencetype ");
    list = new ArrayList();
    Ss ss = null;
    for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { Map map = (Map)localIterator.next();
      ss = new Ss();

      ss.setId(((Integer)map.get("id")).intValue());
      ss.setSname((String)map.get("sname"));
      ss.setSvalue((String)map.get("svalue"));
      list.add(ss);
    }
    return list;
  }

  public Map<String, String> getAllFormIds() {
    String sql = "SELECT id,formName FROM t_form_template_info";
    Map map = new HashMap();
    List _list = getJtA().queryForList(sql);
    for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { Map _map = (Map)localIterator.next();
      map.put(String.valueOf(_map.get("id")), 
        (String)_map.get("formName"));
    }
    return map;
  }

  public String getTableName(String formId) {
    String sql = "SELECT tableName FROM t_form_template_info WHERE id = '" + 
      formId + "'";
    String tableName = "";
    try {
      tableName = (String)getJtA().queryForObject
        (sql, String.class);
    } catch (IncorrectResultSizeDataAccessException ex) {
      System.out.println("错误：记录不唯一或为空");
    } catch (Exception ex) {
      String guid = Guid.get();
      this.logItem.setMethod("show");
      this.logItem.setLogid(guid);
      this.logItem.setLevel("error");
      this.logItem.setDesc("查找数据时出现异常");
      this.logItem.setContent(ex.toString());
      Log.write(this.logItem);
    }

    return tableName;
  }

  public List<String> getAllIds(String tableName) {
    String sql = "SELECT id FROM " + tableName;

    List list = new ArrayList();
    List _list = getJtA().queryForList(sql);

    for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { Map _map = (Map)localIterator.next();
      list.add((String)_map.get("id"));
    }
    return list;
  }

  public List getAllSerialType()
  {
    String sql = "SELECT * FROM t_form_serial";

    List _list = getJtA().queryForList(sql);

    return _list;
  }

  public int getMaxCount(String year)
  {
    String sql = "select count(sn.id) cid from t_form_serialnum as sn inner join t_form_serial as s on sn.serialTypeId = s.id where createdate like '" + 
      year + "%'";
    int maxCount = -1;
    List list = getJtA().queryForList(sql);
    if (list != null)
      maxCount = Integer.parseInt(String.valueOf(((Map)(Map)list.get(0)).get
        ("cid")));
    return maxCount;
  }

  public void insertNewSerialNum(String serialTypeId, String serialNum)
    throws SQLException
  {
    String sql = "insert into t_form_serialnum values(null," + serialTypeId + 
      ",'" + serialNum + "',now())";
    getJtA().execute(sql);
    this.logItem.setMethod("insertNewSerialNum");
    this.logItem.setLevel("info");
    this.logItem.setDesc("添加记录成功");
    Log.write(this.logItem);
  }

  public String getSerialTypeById(String id)
  {
    String sql = "select serialType from t_form_serial where id = " + id;
    String serialType = "";
    List list = getJtA().queryForList(sql);
    if ((list != null) && (list.size() > 0))
      serialType = (String)((Map)(Map)list.get(0)).get("serialType");
    return serialType;
  }
}
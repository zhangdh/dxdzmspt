package com.coffice.form.dao.imp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coffice.form.bean.Ss;
import com.coffice.form.dao.IQueryDao;
import com.coffice.util.BaseUtil;
import com.coffice.util.SysPara;

public class MySqlQueryDaoImpl extends BaseUtil
  implements IQueryDao
{
  public List<Ss> getSsById(String id)
  {
    int typeid = Integer.parseInt(id);
    List list = new ArrayList();
    List _list = null;
    String sql = "";
    if (typeid < 1000) {
      if (SysPara.compareValue("form_list_mark", "true"))
        if (typeid == 1) {
          sql = "select guid as id,chehao as sname from t_cl_mx where zt_dm=1";
          _list = getJtA().queryForList(sql);
        } else if (typeid == 72) {
          sql = "select dm as id,mc as sname from t_dm where zt_dm=1 and lb_dm=72";
          _list = getJtA().queryForList(sql);
        }
    }
    else if ((typeid >= 1000) && (typeid < 10000)) {
      sql = "select dm as id,mc as sname from t_dm where zt_dm=1 and lb_dm=" + id;
      _list = getJtA().queryForList(sql);
    } else if (typeid >= 10000)
    {
      String tablename = (String)getJtA().queryForObject("select mc from t_dm_lb where dm=?", 
        new Object[] { Integer.valueOf(typeid) }, String.class);
      sql = "select dm as id,mc as sname from " + tablename + " where yxbz=1 and lb_dm=" + typeid;
      _list = getJtA().queryForList(sql);
    }
    if ((!(SysPara.compareValue("form_list_mark", "true"))) && 
      (typeid < 1000)) {
      sql = "select id, sname from t_form_usersequence where typeid=" + typeid;
      _list = getJtA().queryForList(sql);
    }

    if (_list == null)
      _list = new ArrayList();

    for (Iterator localIterator = _list.iterator(); localIterator.hasNext(); ) { 
      Map _map = (Map)localIterator.next();
      Ss s = new Ss();
      s.setSvalue(String.valueOf(_map.get("id")));
      s.setSname((String)_map.get("sname"));
      list.add(s);
    }
    if (typeid == 0) {
      Ss s = new Ss();
      Ss s2 = new Ss();
      s.setSvalue("1");
      s.setSname("是");
      s2.setSvalue("0");
      s2.setSname("否");
      list.add(s);
      list.add(s2);
    } else if (typeid == 2) {
      Ss s = new Ss();
      Ss s2 = new Ss();
      s.setSvalue("0");
      s.setSname("无");
      s2.setSvalue("1");
      s2.setSname("有");
      list.add(s);
      list.add(s2);
    }
    return list;
  }
}
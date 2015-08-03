package com.coffice.form.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.coffice.form.bean.Serial;
import com.coffice.form.bean.SerialType;
import com.coffice.form.dao.ISerialDao;
import com.coffice.form.util.DBUtil;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class MySqlSerialDaoImpl extends BaseUtil
  implements ISerialDao
{
  public List<SerialType> getAllSerialTypes()
  {
    String sql = "SELECT * FROM t_form_customserial where inuse='1'";
    List list = getJtN().queryForList(sql);
    return list;
  }

  public List<SerialType> getAllSerialTypes(Map map) {
    String sql = "";
    if ("admin".equals(map.get("yhid")))
      sql = "SELECT * FROM t_form_customserial where inuse='1'";
    else
      sql = "SELECT * FROM t_form_customserial where inuse='1' and zzid='" + map.get("zzid") + "'";

    List list = getJtN().queryForList(sql);
    return list; }

  public void insertSerialType(SerialType type) {
    LogItem logItem = new LogItem();
    String id = Db.getGUID();
    try {
      String sql = "INSERT INTO t_form_customserial (id,serialtype,serialreg,currentnumber,places) VALUES ('" + id + "','" + 
        type.getType() + 
        "','" + 
        type.getSerialReg() + 
        "'," + 
        type.getCurrentSerialNo() + "," + type.getPlaces() + ")";
      getJtN().update(sql);
      logItem.setMethod("insertSerialType");
      logItem.setLevel("info");
      Log.write(logItem);
    } catch (Exception e) {
      String guid = Guid.get();
      logItem.setMethod("insertSerialType");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("");
    }
  }

  public void insertSerial(Serial serial)
  {
    String id = Db.getGUID();
    LogItem logItem = new LogItem();
    try {
      String sql = "INSERT INTO t_form_serialno (id,customid,serial,gendate) VALUES ('" + id + "'," + 
        serial.getSerialType().getId() + 
        ",'" + 
        serial.getSerial() + 
        "'," + DBUtil.getDateFun() + ")";
      getJtN().update(sql);
      logItem.setMethod("insertSerial");
      logItem.setLevel("info");
      logItem.setDesc("更新成功");
      Log.write(logItem);
    } catch (Exception ex) {
      String guid = Guid.get();
      logItem.setMethod("insertSerial");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("");
      logItem.setContent(ex.toString());
      Log.write(logItem);
      throw new ServiceException("");
    }
  }

  public Serial getSerialById(String id)
  {
    String sql = "select * from t_form_serialno where id = '" + id + "'";
    List list = getJtN().queryForList(sql);
    Serial serial = null;
    if (list.size() > 0) {
      Map map = (Map)list.get(0);
      serial = new Serial();
      serial.setId((String)map.get("id"));
      serial.setSerial((String)map.get("serial"));
      serial.getSerialType().setId((String)map.get("customid"));
    }
    return serial;
  }

  public int getSerialCount(String tid, String type)
  {
    StringBuilder sql = new StringBuilder(
      "SELECT COUNT(id) AS cid FROM t_form_serialno WHERE customid = '").append
      (tid).append("'");
    Calendar date = Calendar.getInstance();
    String year = String.valueOf(date.get(1));
    String month = 
      "0" + 
      (date.get(2) + 1);
    String day = String.valueOf(date.get(5));
    if ("1".equals(type))
      sql.append(" AND gendate like '").append(year).append("%'");
    else if ("2".equals(type))
      sql.append(" AND gendate like '").append(year).append("-").append(
        month).append
        ("%'");
    else if ("3".equals(type))
      sql.append(" AND gendate like '").append(year).append("-").append(
        month).append
        ("-").append(day).append("%'");
    int resultCount = 0;
    resultCount = getJtN().queryForInt(sql.toString());
    return resultCount;
  }

  public SerialType getTypeById(String id)
  {
    String sql = "SELECT * FROM t_form_customserial WHERE id ='" + id + "'";
    SerialType type = null;
    type = (SerialType)getJtN().queryForObject(sql, new RowMapper() {
      public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        SerialType type = new SerialType();
        type.setType(rs.getString("serialtype"));
        type.setCurrentSerialNo(rs.getDouble("currentnumber"));
        type.setSerialReg(rs.getString("serialreg"));
        type.setPlaces(rs.getInt("places"));
        type.setId(rs.getString("id"));
        return type;
      }

    });
    return type;
  }

  public void updateSerialType(SerialType type)
  {
    LogItem logItem = new LogItem();
    try {
      String sql = "UPDATE t_form_customserial SET serialtype='" + type.getType() + 
        "',serialreg='" + type.getSerialReg() + "',currentnumber=" + 
        type.getCurrentSerialNo() + ",places=" + type.getPlaces() + 
        ",update_bz='0' WHERE id = '" + type.getId() + "'";

      getJtN().update(sql);
      logItem.setMethod("updateSerialType");
      logItem.setLogid(Guid.get());
      logItem.setLevel("info");
      logItem.setDesc("更新成功");
      Log.write(logItem);
    } catch (Exception ex) {
      logItem.setMethod("updateSerialType");
      logItem.setLogid(Guid.get());
      logItem.setLevel("error");
      logItem.setDesc("更新失败");
      logItem.setContent(ex.toString());
      Log.write(logItem);
      throw new ServiceException("更新失败");
    }
  }
}
package com.coffice.form.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.coffice.form.bean.Serial;
import com.coffice.form.bean.SerialType;
import com.coffice.form.dao.ISerialDao;
import com.coffice.form.dao.imp.MySqlSerialDaoImpl;
import com.coffice.exception.ServiceException;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class SerialService
{
  private ISerialDao serialDao = null;

  public ISerialDao getSerialDao()
  {
    return this.serialDao;
  }

  public SerialService() {
    this.serialDao = new MySqlSerialDaoImpl();
  }

  public List<SerialType> getAllSerialType() {
    List list = new ArrayList();
    list = this.serialDao.getAllSerialTypes();

    return list;
  }

  public void saveSerialType(SerialType type) {
    try {
      this.serialDao.insertSerialType(type);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateSerialType(SerialType type) {
    try {
      this.serialDao.updateSerialType(type);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SerialType getSerialTypeById(String id) {
    SerialType type = null;
    try {
      type = this.serialDao.getTypeById(id);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return type;
  }

  @Transactional
  public void insertSerial(String id, String wh)
  {
    String guid;
    LogItem logItem = new LogItem();
    SerialType type = null;
    try
    {
      type = this.serialDao.getTypeById(id);
      Serial serial = new Serial();

      serial.setSerial(wh);
      serial.setSerialType(type);

      if ("0".equalsIgnoreCase(type.getType())) {
        type.setCurrentSerialNo(type.getCurrentSerialNo() + 1D);
      } else {
        String update_bz = "";
        update_bz = (String)Db.getJtN().queryForObject(
          "select update_bz from t_form_customserial where id=?", 
          new Object[] { id }, String.class);

        if ((this.serialDao.getSerialCount(type.getId(), type.getType()) == 0) && 
          (update_bz.equals("0")))
          type.setCurrentSerialNo(2.0D);
        else
          type.setCurrentSerialNo(type.getCurrentSerialNo() + 1D);

      }

      type.setCurrentSerialNo(type.getCurrentSerialNo() + 1D);
      this.serialDao.updateSerialType(type);

      this.serialDao.insertSerial(serial);

      logItem.setMethod("insertSerial");
      logItem.setLogid(Guid.get());
      logItem.setLevel("error");
      logItem.setDesc("文号添加成功");
      Log.write(logItem);
    } catch (NumberFormatException e) {
      guid = Guid.get();
      logItem.setMethod("insertSerial");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("文号处理时出现异常");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("文号处理时出现异常");
    } catch (Exception e) {
      guid = Guid.get();
      logItem.setMethod("insertSerial");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("文号处理时出现异常");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("文号处理时出现异常");
    }
  }

  @Transactional
  public String insertSerial(String id)
  {
    String guid;
    LogItem logItem = new LogItem();
    SerialType type = null;
    String rs = "";
    try {
      type = this.serialDao.getTypeById(id);
      Serial serial = new Serial();
      serial.setSerial(type.generateSerial
        (this.serialDao.getSerialCount
        (type.getId(), type.getType())));
      serial.setSerialType(type);

      if ("0".equalsIgnoreCase(type.getType())) {
        type.setCurrentSerialNo(type.getCurrentSerialNo() + 1D);
      } else {
        String update_bz = "";
        update_bz = (String)Db.getJtA().queryForObject(
          "select update_bz from t_form_customserial where id=?", 
          new Object[] { id }, String.class);

        if ((this.serialDao.getSerialCount(type.getId(), type.getType()) == 0) && 
          (update_bz.equals("0")))
          type.setCurrentSerialNo(2.0D);
        else
          type.setCurrentSerialNo(type.getCurrentSerialNo() + 1D);

      }

      this.serialDao.updateSerialType(type);

      this.serialDao.insertSerial(serial);

      rs = serial.getSerial();
      logItem.setMethod("insertSerial");
      logItem.setLogid(Guid.get());
      logItem.setLevel("error");
      logItem.setDesc("文号添加成功");
      Log.write(logItem);
    } catch (NumberFormatException e) {
      guid = Guid.get();
      logItem.setMethod("insertSerial");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("文号处理时出现异常");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("文号处理时出现异常");
    }
    catch (Exception e) {
      guid = Guid.get();
      logItem.setMethod("insertSerial");
      logItem.setLogid(guid);
      logItem.setLevel("error");
      logItem.setDesc("文号处理时出现异常");
      logItem.setContent(e.toString());
      Log.write(logItem);
      throw new ServiceException("文号处理时出现异常");
    }
    return rs;
  }
}
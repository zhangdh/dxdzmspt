package com.coffice.form.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

import com.coffice.form.core.TemplateUtil;
import com.coffice.form.dao.imp.MySqlFormDaoImpl;
import com.coffice.form.dao.imp.MySqlTableDaoImpl;
import com.coffice.form.util.XMLUtil;

public class AjaxService
{
  private MySqlTableDaoImpl tableDao;
  private MySqlFormDaoImpl formDao;

  public String getColumn(String tableName)
    throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    this.tableDao = new MySqlTableDaoImpl();
    List list = this.tableDao.getColumnNames(tableName);
    list.remove(list.size() - 1);
    return XMLUtil.generateXmlString(list);
  }

  public String getAllForms() throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    this.formDao = new MySqlFormDaoImpl();
    return XMLUtil.generateXmlString(this.formDao.getAllForms());
  }

  @Transactional
  public boolean createPrintTemplate(String formId, String templateName, String fileName, String path, String sourceName, String targetName, String[] ids, String[] x, String[] y)
  {
    TemplateUtil tu = new TemplateUtil();
    boolean rs = false;
    try {
      this.tableDao = new MySqlTableDaoImpl();
      Map map = this.tableDao.getColumns(formId, ids);
      String printTemplateSource = tu.generateHTMLCode(map, formId, ids, 
        x, y);
      tu.createPrintTemplateFile(path, sourceName, targetName, 
        printTemplateSource);

      this.tableDao.createPrintInfo(formId, templateName, fileName);

      rs = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return rs;
  }
}
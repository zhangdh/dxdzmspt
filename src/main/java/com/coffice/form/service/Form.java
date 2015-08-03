package com.coffice.form.service;

import java.util.HashMap;
import java.util.Map;

import com.coffice.workflow.util.WorkFlowMethod;

public class Form
{
  public Map saveFormData(Map map)
  {
    Map _map = new HashMap();
    try {
      FormService formService = new FormService();
      String businessId = String.valueOf(map.get("businessId"));
      String stepId = String.valueOf(map.get("stepId"));
      String loginCode = String.valueOf(map.get("yhid"));
      String tableName = String.valueOf(map.get("tableName"));
      Map authority = null;
      WorkFlowMethod wfMethod = new WorkFlowMethod();
      String id = wfMethod.getFormId(businessId, stepId);
      authority = wfMethod.getFormRole(businessId, stepId);
      if ((id == null) || ("".equals(id)))
        id = String.valueOf(map.get("id"));

      if ((id == null) || (id.equals(""))) {
        id = formService.saveUserFormData(tableName, map, authority, loginCode);
        _map.put("isupdate", "false");
      }
      else {
        formService.updateUserFormData(tableName, map, id, authority, loginCode);
        _map.put("isupdate", "true");
      }

      _map.put("formId", id);
      _map.put("result", "success");
    } catch (Exception ex) {
      _map.put("result", "error");
      ex.printStackTrace();
    }
    return _map;
  }
}
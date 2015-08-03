package com.coffice.form.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Page
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String template = "";
  private Map<String, Object> root = new HashMap();

  public String getTemplate()
  {
    return this.template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Map<String, Object> getRoot() {
    return this.root;
  }

  public void setRoot(Map<String, Object> root) {
    this.root = root;
  }

  public void put(String name, Object value) {
    this.root.put(name, value); }

  public Object get(String name) {
    return this.root.get(name); }

  public void put(String name, boolean value) {
    this.root.put(name, new Boolean(value));
  }

  public void put(String name, int value) {
    this.root.put(name, new Integer(value));
  }

  public void put(String name, double value) {
    this.root.put(name, new Double(value));
  }
}
package com.coffice.form.dao;

import java.util.List;

import com.coffice.form.bean.Ss;

public abstract interface IQueryDao
{
  public abstract List<Ss> getSsById(String paramString);
}
package com.coffice.form.manager;


import java.util.*;

import com.coffice.bean.PageBean;
import com.coffice.util.*;
public class FormManager extends BaseUtil{
	JspJsonData jjd;
	String bmid;
	String gwid;
	String yhid;
	Map _map;
	String str;
	ArrayList list;
	LogItem logItem;// 日志项

	public FormManager()
	{
		str = "";
		list = new ArrayList();
	}

	public FormManager(Map mapIn)
	{
		str = "";
		list = new ArrayList();
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String)mapIn.get("yhid");
		bmid = (String)mapIn.get("bmid");
		gwid = (String)mapIn.get("gwid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		_map = mapIn;
	}
	public Map listForm(){
		String formname = _map.get("cx_mc")==null?"":String.valueOf(_map.get("cx_mc"));
		StringBuffer sqlWhere  = new StringBuffer();
		if(!"".equals(formname)){
			sqlWhere.append(" and formname like '%").append(formname).append("%'");
		}
		try
		{
			PageBean page = new PageBean();
			if(!"".equals(_map.get("page_goto")) && _map.get("page_goto")!=null){
				page.setPageGoto((String)_map.get("page_goto"));
			}else{
				page.setPageGoto("1");
			}
			if(!"".equals(_map.get("page_num")) && _map.get("page_num")!=null){
				page.setPageSize((String)_map.get("page_num"));
			}else{
				page.setPageSize("10");
			}
			page.setSql((new StringBuilder("select id,tablename,formName mc,inuse,case  when inuse='0' then '未启用' when inuse='1' then '已启用'  end as zt  from t_form_template_info  WHERE  zt_dm=1 ")).append(sqlWhere).append(" order by id desc").toString());
			page.setCountSql((new StringBuilder("SELECT count(*) FROM t_form_template_info WHERE  zt_dm=1")).append(sqlWhere).toString());
			page.setNamedParameters(_map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch (Exception e){
			String guid = Guid.get();
			logItem.setMethod("listForm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理表单列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, (new StringBuilder("获取表单列表异常:")).append(e.toString()).toString());
		}	
		return jjd.getData();
	}
	public Map startForm(){
		try{
			String id = _map.get("id")==null?"":String.valueOf(_map.get("id"));
			if(!"".equals(id)){
				this.getJtN().update("update t_form_template_info set inuse =1 where id ='"+id+"'");
			}else{
				
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("startForm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("启用表单时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, (new StringBuilder("启用表单异常:")).append(e.toString()).toString());
		}
		return jjd.getData();
	}
	public Map delForm(){
		try{
			String id = _map.get("id")==null?"":String.valueOf(_map.get("id"));
			if(!"".equals(id)){
				this.getJtN().update("update t_form_template_info set zt_dm =0 where id ='"+id+"'");
			}else{}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("delForm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("表单列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, (new StringBuilder("获取表单列表异常:")).append(e.toString()).toString());
		}
			return jjd.getData();
		}
    protected String getSys_para(String csdm){
		String csz = "";
		List list = getJtA().queryForList("select csz from t_sys_para where csdm=?", new Object[] {
			csdm
		});
		if (list.size() > 0)
			csz = (String)((Map)list.get(0)).get("csz");
		return csz;
	}
}

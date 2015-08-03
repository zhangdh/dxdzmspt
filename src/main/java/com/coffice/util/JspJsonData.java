
package com.coffice.util;

import java.util.*;

import com.coffice.bean.PageBean;


public class JspJsonData
{

	private HashMap dataMap;
	private HashMap selectMap;
	private HashMap gridMap;
	private HashMap formMap;
	private List tableIdList;
	private String dbtype;

	public JspJsonData()
	{
		dataMap = new HashMap();
		selectMap = new HashMap();
		gridMap = new HashMap();
		formMap = new HashMap();
		tableIdList = new ArrayList();
		dbtype = "";
		dataMap.put("result", Boolean.valueOf(true));
		dataMap.put("msg", "");
	}

	public HashMap getData()
	{
		dataMap.put("selectData", selectMap);
		dataMap.put("formData", formMap);
		dataMap.put("gridData", gridMap);
		dataMap.put("tableHover", tableIdList);
		return dataMap;
	}

	public void setSelect(String selectId, List list)
	{
		try
		{
			Map _map = new HashMap();
			_map.put("dm","");
			_map.put("mc","");
			list.add(0,_map);
			selectMap.put(selectId, convertList(list));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void setGrid(String tableId, List list, PageBean page)
	{
		HashMap map = new HashMap();
		try
		{
			map.put("data", convertList(list));
			HashMap mapPageInfo = null;
			if (page != null)
			{
				mapPageInfo = new HashMap();
				mapPageInfo.put("page_cur", Integer.valueOf(page.getPage_cur()));
				mapPageInfo.put("page_allCount", Integer.valueOf(page.getPage_allCount()));
				mapPageInfo.put("page_allPage", Integer.valueOf(page.getPage_allPage()));
			}
			map.put("page", mapPageInfo);
			gridMap.put(tableId, map);
			tableIdList.add(tableId);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public List convertList(List _list)
	{
		List list = new ArrayList();
		Map _map = null;
		for (int i = 0; i < _list.size(); i++)
		{
			Map map = (Map)(Map)_list.get(i);
			Iterator it = map.entrySet().iterator();
			_map = new HashMap();
			java.util.Map.Entry m;
			for (; it.hasNext(); _map.put(m.getKey().toString().toLowerCase(), m.getValue()))
				m = (java.util.Map.Entry)(java.util.Map.Entry)it.next();

			list.add(_map);
		}

		return list;
	}

	public Map convertMap(Map _map)
	{
		Map map1 = new HashMap();
		try
		{
			java.util.Map.Entry m;
			for (Iterator it = _map.entrySet().iterator(); it.hasNext(); map1.put(m.getKey().toString().toLowerCase(), m.getValue()))
				m = (java.util.Map.Entry)(java.util.Map.Entry)it.next();

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return map1;
	}

	public void setForm(Map map)
	{
		try
		{
			formMap.putAll(convertMap(map));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void setResult(boolean result, String msg)
	{
		dataMap.put("result", Boolean.valueOf(result));
		dataMap.put("msg", msg);
	}

	public void setExtend(String extId, Object data)
	{
		if (data instanceof List)
			try
			{
				dataMap.put(extId, convertList((List)data));
			}
			catch (Exception exception) { }
		else
			dataMap.put(extId, data);
	}
}

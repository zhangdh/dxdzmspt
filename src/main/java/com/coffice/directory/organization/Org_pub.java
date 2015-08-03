
package com.coffice.directory.organization;

import java.util.*;

import com.coffice.util.BaseUtil;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;


public class Org_pub extends BaseUtil
{

	public Org_pub()
	{
	}

	public List getSjyh(String yhid)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select lxid as yhid,mc from t_org_tree a where sjid in(select guid from t_org_tree where lxid in ");
		sb.append(" (select sjgwid from t_org_gw where gwid in(select a.lxid from t_org_tree a,t_org_tree b where a.guid =b.sjid and b.lxid='");
		sb.append(yhid).append("') and zt_dm=1)");
		sb.append(" and sjbmid=(select sjbmid from t_org_tree where lxid='").append(yhid).append("')");
		sb.append(" and zt_dm=1) and zt_dm=1");
		List list = getJtA().queryForList(sb.toString());
		return list;
	}

	public List getXjyh(String yhid)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select lxid as yhid,mc from t_org_tree ");
		sb.append("where sjid in(select guid from t_org_tree where lxid in(");
		sb.append("select gwid from t_org_gw where sjgwid in(select lxid from t_org_tree where guid in(");
		sb.append("select sjid from t_org_tree where  lxid='").append(yhid).append("'))))");
		sb.append(" and sjbmid in(select sjbmid from t_org_tree where  lxid='").append(yhid).append("') and zt_dm=1");
		List list = getJtA().queryForList(sb.toString());
		return list;
	}

	public List getYhInfoList(String yhid_list)
	{
		List list = getJtA().queryForList((new StringBuilder("select yhid,xm as mc from t_org_yh where yhid in(")).append(yhid_list).append(") and zt_dm=1").toString());
		return list;
	}

	public List getBmInfoList(String bmid_list)
	{
		List list = getJtA().queryForList((new StringBuilder("select bmid,mc from t_org_bm where bmid in(")).append(bmid_list).append(") and zt_dm=1").toString());
		return list;
	}

	public List getGwInfoList(String gwid_list)
	{
		List list = getJtA().queryForList((new StringBuilder("select gw,mc from t_org_gw where gwid in(")).append(gwid_list).append(") and zt_dm=1").toString());
		return list;
	}

	public List getYhInfoList(String lxz, String lxbz)
	{
		List list = null;
		if (lxz.indexOf("'") < 0)
		{
			lxz = lxz.replaceAll(",", "','");
			lxz = (new StringBuilder("'")).append(lxz).append("'").toString();
		}
		StringBuffer sql = new StringBuffer();
		String px_org = "";
		String db_type = "";
		try
		{
			db_type = SysPara.getDbType();
		}
		catch (Exception e)
		{
			db_type = "mysql";
		}
		try
		{
			px_org = SysPara.getValue("px_org");
		}
		catch (Exception e)
		{
			px_org = "false";
		}
		if ("bm".equals(lxbz))
		{
			if (!"true".equals(px_org))
			{
				sql.append("select distinct lxid as yhid,mc,py from t_org_tree where sjbmid in(");
				sql.append(lxz).append(")");
				sql.append(" and lx_dm=204 and zt_dm=1 order by py");
			} else
			{
				sql.append("select");
				sql.append(" lxid as yhid,mc,xh,isnull(pxxh,10000) as pxxh,py");
				sql.append(" from t_org_tree a left outer join t_org_yh_px b on a.lxid = b.yhid");
				sql.append(" where sjbmid in (").append(lxz);
				sql.append(")  and lx_dm =204 and zt_dm=1 order by pxxh,xh,py");
			}
		} else
		if ("gw".equals(lxbz))
		{
			if (!"true".equals(px_org))
			{
				sql.append("select distinct lxid as yhid,mc,py from t_org_tree where ");
				sql.append("sjid in (select guid from t_org_tree where lxid in (").append(lxz).append("))");
				sql.append(" and lx_dm=204 and zt_dm=1 order by py");
			} else
			{
				sql.append("select");
				sql.append(" lxid as yhid,mc,xh,isnull(pxxh,10000) as pxxh,py");
				sql.append(" from t_org_tree a left outer join t_org_yh_px b on a.lxid = b.yhid");
				sql.append(" where sjid in (select guid from t_org_tree where lxid in (");
				sql.append(lxz).append("))  and lx_dm =204 and zt_dm=1 order by pxxh,xh,py");
			}
		} else
		if ("yh".equals(lxbz))
		{
			if (!"true".equals(px_org))
			{
				sql.append("select lxid as yhid,mc,py from t_org_tree where lxid in(");
				sql.append(lxz).append(") and zt_dm=1 order by py");
			} else
			{
				sql.append("select");
				sql.append(" lxid as yhid,mc,xh,isnull(pxxh,10000) as pxxh,py");
				sql.append(" from t_org_tree a left outer join t_org_yh_px b on a.lxid = b.yhid");
				sql.append(" where lxid in (").append(lxz).append(")  and zt_dm=1 order by pxxh,xh,py");
			}
		} else
		if ("js".equals(lxbz))
			if (!"true".equals(px_org))
			{
				sql.append("select a.yhid,xm as mc from t_org_yh a,t_org_yh_kz b");
				sql.append(" where a.yhid = b.yhid and a.zt_dm=1 and kz_dm=300 and kzz in(");
				sql.append(lxz).append(") order by py");
			} else
			{
				sql.append("select");
				sql.append(" a.lxid as yhid,a.mc,xh,isnull(pxxh,10000) as pxxh,py from t_org_tree a");
				sql.append(" left outer join  t_org_yh_px b on a.lxid=b.yhid,t_org_yh_kz c");
				sql.append(" where a.lxid=c.yhid and kz_dm=300 and kzz in(");
				sql.append(lxz).append(")  and a.zt_dm=1 order by pxxh,xh,py");
			}
		if ("sqlserver".equalsIgnoreCase(db_type))
			list = getJtN().queryForList(sql.toString());
		else
			list = getJtA().queryForList(sql.toString());
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}

		list.clear();
		list.addAll(newList);
		return list;
	}

	public List getBmList()
	{
		List list = getJtA().queryForList("select bmid as dm ,mc from t_org_bm where zt_dm=1 order by bmid");
		Map map;
		String temp_mc;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); map.put("mc", temp_mc))
		{
			map = (Map)iterator.next();
			String dm = (String)map.get("dm");
			temp_mc = (String)map.get("mc");
			String temp[] = dm.split("_");
			for (int i = 0; i < temp.length - 1; i++)
				temp_mc = (new StringBuilder("--")).append(temp_mc).toString();

			map.remove("mc");
		}

		return list;
	}

	public List getBmList(String yhid)
	{
		List list = null;
		if ("admin".equals(yhid))
			list = getJtN().queryForList("select bmid as dm ,mc from t_org_bm where zt_dm=1 order by bmid");
		else
			list = getJtN().queryForList((new StringBuilder("select bmid as dm ,mc from t_org_bm where zzid like '")).append((String)Cache.getUserInfo(yhid, "zzid")).append("%' and zt_dm=1 order by bmid").toString(), new Object[0]);
		Map map;
		String temp_mc;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); map.put("mc", temp_mc))
		{
			map = (Map)iterator.next();
			String dm = (String)map.get("dm");
			temp_mc = (String)map.get("mc");
			String temp[] = dm.split("_");
			for (int i = 0; i < temp.length - 1; i++)
				temp_mc = (new StringBuilder("    ")).append(temp_mc).toString();

			map.remove("mc");
		}

		return list;
	}

	public List getZzList(String yhid)
	{
		List list = null;
		if ("admin".equals(yhid))
			list = getJtN().queryForList("select a.zzid,a.bmid as dm ,a.mc from t_org_bm a,t_org_bm_fb b where a.bmid = b.bmid and a.zt_dm=1 and b.zt_dm=1 order by a.bmid");
		else
			list = getJtN().queryForList((new StringBuilder("select a.zzid,a.bmid as dm ,a.mc from t_org_bm a,t_org_bm_fb b where a.bmid = b.bmid and a.zzid like '")).append((String)Cache.getUserInfo(yhid, "zzid")).append("%' and a.zt_dm=1 and b.zt_dm=1 order by a.bmid").toString(), new Object[0]);
		Map map;
		String temp_mc;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); map.put("mc", temp_mc))
		{
			map = (Map)iterator.next();
			String zzid = (String)map.get("zzid");
			temp_mc = (String)map.get("mc");
			String temp[] = zzid.split("_");
			for (int i = 0; i < temp.length - 1; i++)
				temp_mc = (new StringBuilder("    ")).append(temp_mc).toString();

			map.remove("mc");
		}

		return list;
	}
}

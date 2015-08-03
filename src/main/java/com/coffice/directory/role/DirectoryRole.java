package com.coffice.directory.role;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import com.coffice.bean.PageBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.SysPara;
public class DirectoryRole extends BaseUtil{

	JspJsonData jjd;
	String yhid;
	Map gmap;
	String str;
	ArrayList list;

	public DirectoryRole()
	{
		str = "";
		list = new ArrayList();
	}

	public DirectoryRole(Map mapIn)
	{
		str = "";
		list = new ArrayList();
		jjd = new JspJsonData();
		yhid = (String)mapIn.get("yhid");
		gmap = mapIn;
	}

	public Map saveRole(Map map)
	{
		jjd = new JspJsonData();
		String jsid = "";
		String zzid = "0";
		/*if (((Boolean)Cache.getGlobalInfo("", "isgroup")).booleanValue())
			zzid = (String)map.get("org_zzid");
		else
			zzid = "0";*/
		try
		{
			jsid = (String)map.get("js_id");
			if (jsid == null || jsid.equals(""))
			{
				jsid = Db.getGUID();
				String dbtype = SysPara.getValue("db_type");
				if ("mysql".equals(dbtype) || "sqlserver".equals(dbtype))
					getJtN().update("insert into t_org_js(zzid,jsid,mc,bz,cjsj,lrr_bmid,lrr_gwid,lrr_yhid)values(?,?,?,?,?,?,?,?)", new Object[] {
						zzid, jsid, map.get("mc"), map.get("bz"), getCurrentTime(), map.get("bmid"), map.get("gwid"), map.get("yhid")
					});
				else
				if ("oracle".equals(dbtype))
					getJtN().update("insert into t_org_js(zzid,jsid,mc,bz,cjsj,lrr_bmid,lrr_gwid,lrr_yhid)values(?,?,?,?,sysdate,?,?,?)", new Object[] {
						zzid, jsid, map.get("mc"), map.get("bz"), map.get("bmid"), map.get("gwid"), map.get("yhid")
					});
				oplog.info("新建用户组：{}", map.get("mc"));
			} else
			{
				getJtN().update("update t_org_js set mc=?,bz=? where jsid=?", new Object[] {
					map.get("mc"), map.get("bz"), jsid
				});
				oplog.info("更新用户组：{}", map.get("mc"));
			}
			jjd.setResult(true, "保存成功");
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			String msg = "保存用户组时出现异常[错误代码：" + guid + ",method:saveRole]:" + ex.toString();
			log.error(msg);
			jjd.setResult(false, (new StringBuilder("保存数据时出现错误，错误代码:")).append(guid).toString());
			throw new ServiceException("保存数据时异常");
		}
		return jjd.getData();
	}

	public Map queryRole(Map map)
	{
		jjd = new JspJsonData();
		String sqlWhere = "";
		String sql = "";
		String sqlcount = "";
		String js_mc = map.get("js_mc")==null?"":String.valueOf(map.get("js_mc"));
		//String zz = Str.filterNull(String.valueOf(map.get("zz")));
		String order_sql = "";
		order_sql = " order by jsid ";
		if (!"".equals(js_mc))
			sqlWhere = (new StringBuilder(" where  mc like '%")).append(js_mc).append("%'").toString();
		sql = (new StringBuilder("select * from t_org_js")).append(sqlWhere).append(order_sql).toString();
		sqlcount = (new StringBuilder("select count(*) from t_org_js")).append(sqlWhere).toString();
		PageBean page = new PageBean();
		if(map.get("page_size")!=null){
			page.setPageSize(String.valueOf(map.get("page_size")));
		}else{
			page.setPageSize("10");
		}
		page.setPageGoto((String)map.get("page_goto"));
		page.setSql(sql);
		page.setCountSql(sqlcount);
		page.setNamedParameters(map);
		List _list = null;
		try
		{
			_list = Db.getPageData(page);
		}
		catch (Exception ex)
		{
			String guid = Guid.get();
			jjd.setResult(false, (new StringBuilder("查找数据时出现错误，错误代码：")).append(guid).toString());
			String msg = "用户组查询时出现异常[错误代码：" + guid + ",method:queryRole]:" + ex.toString();
			log.error(msg);
		}
		jjd.setGrid("datalist", _list, page);
		return jjd.getData();
	}
	public Map showRoleInfo(Map map)
	{
		jjd = new JspJsonData();
		try
		{
			Map _map = getNpjtA().queryForMap("select zzid, jsid as js_id,mc,bz from t_org_js where jsid=:jsid", map);
			_map.put("org_bmid", map.get("zzid"));
			jjd.setForm(_map);
		}
		catch (EmptyResultDataAccessException e)
		{
			jjd.setResult(false, "没有查找到数据");
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			jjd.setResult(false, "错误：记录不唯一");
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			jjd.setResult(false, (new StringBuilder("查找用户组数据时出现错误，错误代码：")).append(guid).toString());
			String msg = "查找用户组数据时出现异常[错误代码：" + guid + ",method:showRoleInfo]:" + e.toString();
			log.error(msg);
		}
		return jjd.getData();
	}

	public Map deleteRole(Map map)
	{
		jjd = new JspJsonData();
		String jsidList = (String)map.get("jsid");
		String jsid_arr[] = jsidList.split("~");
		boolean flag = true;
		try
		{
			String as[] = jsid_arr;
			int i = 0;
			for (int j = as.length; i < j; i++)
			{
				String jsid = as[i];
				int count = getJtN().queryForInt("select count(*) from t_org_yh_kz a,t_org_yh b where a.yhid = b.yhid and kz_dm=300 and kzz=? and b.zt_dm=1", new Object[] {
					jsid
				});
				if (count <= 0)
					continue;
				flag = false;
				break;
			}

			if (flag)
			{
				jsidList = jsidList.replace("~", "','");
				getJtN().update((new StringBuilder("delete from t_org_js where jsid in('")).append(jsidList).append("')").toString());
				List list = getJtN().queryForList((new StringBuilder("select mc from t_org_js where jsid in('")).append(jsidList).append("')").toString(), new Object[0]);
				String jsmc = "";
				for (Iterator iterator = list.iterator(); iterator.hasNext();)
				{
					Map js_map = (Map)iterator.next();
					jsmc = (new StringBuilder(String.valueOf(jsmc))).append(",").append(js_map.get("mc")).toString();
				}

				oplog.info("删除用户组：{}", jsmc.replace(",", ""));
			} else
			{
				jjd.setResult(false, "用户组已经启用，您不能删除！");
				oplog.info("删除用户组失败，因为角色已经启用。");
			}
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			jjd.setResult(false, (new StringBuilder("删除用户组时出现异常，错误代码：")).append(guid).toString());
			String msg = "删除用户组时出现异常[错误代码：" + guid + ",method:deleteRole]:" + e.toString();
			log.error(msg);
		}
		return jjd.getData();
	}
	private String getCurrentTime()
	{
		java.util.Date date = new java.util.Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(date);
		return curTime;
	}
}

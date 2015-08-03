package com.coffice.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.coffice.util.BaseUtil;


/**
 * 用户基本信息
 * 
 * @author yanggc
 *
 */
@SuppressWarnings("unchecked")
public class UserBean extends BaseUtil implements Serializable{
	static final long serialVersionUID=-2805284943658356093L;
	private String zzid;
    private String xm; //用户姓名
	private String bmid;

	private String bmmc;
	
	private List<Map> bmid_list;

	private String gwid;

	private List<Map> gwid_list;

	private String yhid;

	private List<Map> jsid_list;

	public String getBmid() {
		return bmid;
	}

	public void setBmid(String bmid) {
		this.bmid = bmid;
	}

	public String getGwid() {
		return gwid;
	}

	public void setGwid(String gwid) {
		this.gwid = gwid;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getZzid() {
		return zzid;
	}

	public void setZzid(String zzid) {
		this.zzid = zzid;
	}

	public List<Map> getBmid_list() {
		return bmid_list;
	}

	public void setBmid_list(List<Map> bmid_list) {
		this.bmid_list = bmid_list;
	}

	public List<Map> getGwid_list() {
		return gwid_list;
	}

	public void setGwid_list(List<Map> gwid_list) {
		this.gwid_list = gwid_list;
	}

	public List<Map> getJsid_list() {
		return jsid_list;
	}

	public void setJsid_list(List<Map> jsid_list) {
		this.jsid_list = jsid_list;
	}

	/**
	 * 根据用户id获取用户信息
	 * 
	 * @param yhid
	 *            用户id
	 * @return
	 */
	public static UserBean get(String yhid) {
		UserBean bean = new UserBean();
		String zzid = "";
		String xm = "";
		// 部门列表
		List<Map> bm_list = bean
				.getJtA()
				.queryForList(
						"select distinct sjbmid as dm,b.mc from t_org_tree a,t_org_bm b where a.sjbmid = b.bmid and a.lx_dm=204 and a.lxid=? and a.zt_dm=1",
						new Object[] { yhid });
		
		// 岗位列表
		List<Map> gw_list = bean.getJtA().queryForList(
				"select distinct lxid as dm,mc from t_org_tree a where lx_dm=202 and "
						+ "exists(select * from t_org_tree b where b.sjid=a.guid and b.lxid=? and b.zt_dm=1)",
				new Object[] { yhid });
		// 角色列表
		List<Map> js_list = bean.getJtA().queryForList(
				"select b.kzz as dm, c.mc from t_org_yh a , t_org_yh_kz b,t_org_js c "
						+ "where a.yhid = b.yhid and b.kzz =  c.jsid and a.yhid=? and a.zt_dm=1 and b.kz_dm=300",
				new Object[] { yhid });
		
        //获取组织id 
		List<Map>  _list=bean.getJtA().queryForList("select zzid,xm from t_org_yh where yhid = ? and zt_dm=1",new Object[]{yhid});
		for(Map _map:_list){
			zzid = _map.get("zzid").toString();
			xm = _map.get("xm").toString();
		}
		bean.setBmid_list(bm_list);
		if(bm_list.size()>0){
			bean.setBmmc(((Map)bm_list.get(0)).get("mc").toString());
		}else{
			bean.setBmmc("管理员");
		}
		if(bm_list.size()>0){
		bean.setBmid((String)bm_list.get(0).get("dm"));
		}
		bean.setGwid_list(gw_list);
		if(bm_list.size()>0){
		bean.setGwid((String)gw_list.get(0).get("dm"));
		}
		bean.setJsid_list(js_list);
		bean.setYhid(yhid);
		bean.setZzid(zzid);
		bean.setXm(xm);
		return bean;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}
}

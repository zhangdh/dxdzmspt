package com.coffice.util;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.coffice.util.Yhfw;
import com.coffice.bean.UserBean;



 
public class Lxqx{

	/**
	 * 权限保存到t_yhfw表中
	 * @param userBean 
	 * @param ywid 业务id
	 * @param mk_dm 模块代码
	 * @param sys_fsfw 发送范围，部门、岗位、角色、用户代码   举例:(bm01,gw01,js01,yh01),以逗号分隔
	 * @param fsfw_dm t_dm中的自定义值
	 */
	/**
	 * 保存用户范围
	 */
	public static void insert(UserBean userBean,String ywid,String mk_dm,String sys_fsfw,String fsfw_dm){
		try {
			Yhfw.save(userBean, ywid, mk_dm, sys_fsfw, fsfw_dm, 0,"",0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取用户范围列表
	 * @param ywid
	 * @param fsfw_dm
	 * @return
	 */
	public static List getYhfwList(String ywid, int fsfw_dm){
		return Yhfw.list(ywid, fsfw_dm);
	}
	/**
	 * 获取用户范围
	 * @param ywid
	 * @param fsfw_dm
	 * @return
	 */
	public static String getYhfw(String ywid, String fsfw_dm){
		return Yhfw.getYhfw(ywid, fsfw_dm);
	}
	
	
	
	
	/**
	 * 删除权限 根据业务ID
	 * @param ywid
	 * @throws Exception 
	 */
	@Transactional
	public static void delete(String ywid) throws Exception{
		 LogItem logItem = new LogItem();
		    try {
		      Db.getJtA().update("delete from t_yhfw where ywid=?", new Object[] { ywid });
		    } catch (Exception e) {
		      String guid = Guid.get();
		      logItem.setMethod("delete");
		      logItem.setLogid(guid);
		      logItem.setLevel("error");
		      logItem.setDesc("删除用户范围记录时出错");
		      logItem.setContent(e.toString());
		      Log.write(logItem);
		      throw new Exception("删除用户范围记录时出错");
		    }
	}
	
	/**
	 * 删除权限 根据业务ID,发送范围
	 * @param ywid
	 * @throws Exception 
	 */
	@Transactional
	public static void deleteByfsfw(String ywid,String fsfw) throws Exception{
		 LogItem logItem = new LogItem();
		    try {
		      Db.getJtA().update("delete from t_yhfw where ywid=? and fsfw_dm=?", new Object[] { ywid,fsfw });
		    } catch (Exception e) {
		      String guid = Guid.get();
		      logItem.setMethod("delete");
		      logItem.setLogid(guid);
		      logItem.setLevel("error");
		      logItem.setDesc("删除用户范围记录时出错");
		      logItem.setContent(e.toString());
		      Log.write(logItem);
		      throw new Exception("删除用户范围记录时出错");
		    }
	}
	
	
}

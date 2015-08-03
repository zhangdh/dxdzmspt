package com.coffice.hjzx.syschronizeInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.coffice.util.BaseUtil;


public class SynchroInfoImpl extends BaseUtil implements SynchroInfo  {
	public List<Map> synchroInfo(String tableName,String vdn){
		List<Map> _list = null;
		try {
			//查询同步信息
			String guidStr = "";
			String updateSQL = "";
			List<Map> tbxx_list = null;
			String tbxxCount = "select count(*) from t_xxtb where zt_dm=1 and bm='"+tableName+"' and vdn='"+vdn+"'";
			int counts = this.getJtA().queryForInt(tbxxCount);
			String xxtbListStr = "";
			if(counts>0) {
				String tbxxStr = "select guid from t_xxtb where zt_dm=1 and bm='"+tableName+"' and vdn='"+vdn+"'";
				tbxx_list = this.getJtA().queryForList(tbxxStr);
				if(tbxx_list!=null&&tbxx_list.size()>0) {
					for(int i=0;i<tbxx_list.size();i++) {
						Map _map = (Map)tbxx_list.get(i);
						guidStr=guidStr+_map.get("guid").toString()+",";
					}
					//查询需要同步的信息集合
					xxtbListStr="select * from "+tableName+" where guid in("+guidStr.substring(0,guidStr.lastIndexOf(","))+")";
//					xxtbListStr="select * from "+tableName;
					_list = this.getJtN().queryForList(xxtbListStr);
					
					//将服务器端的中间表的数据的状态为改为0
					updateSQL="update t_xxtb set zt_dm=0,gxsj=sysdate where bm='"+tableName+"' and vdn='"+vdn+"' and guid in("+guidStr.substring(0,guidStr.lastIndexOf(","))+")";
					this.getJtN().update(updateSQL);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return _list;
	}
}

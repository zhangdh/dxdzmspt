package com.coffice.jsry;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
public class Jsry {
	public static void save(String ywid,String mk_dm,String jsry){
		if("120".equals(mk_dm)){
			//站内邮件
			String[] ryStr = jsry.replaceAll("yh","").split(",");
			StringBuffer sqlStr = new StringBuffer();
			String guid ="";
			for(int i=0;i<ryStr.length;i++){
				sqlStr.delete(0,sqlStr.length());
				guid = Guid.get();
				sqlStr.append("insert into t_email_fsfw(guid,emailid,fsfw_ry,ydcs,cjsj,zt_dm)values('")
					  .append(guid).append("','").append(ywid).append("','").append(ryStr[i])
					  .append("',0,").append(BaseUtil.getDateStr()).append(",1)");
				Db.getJtN().update(sqlStr.toString());
			}
		}
		
	}
}

package com.coffice.sms;

import com.coffice.util.BaseUtil;
import com.coffice.util.LogItem;

public class SysData extends BaseUtil {
	int c = -1;
	LogItem logItem;// 日志项
	public static boolean sp_edit=true;//判断帐号是否有变更（短信通多帐号上行时使用）
	public SysData() {
		logItem = new LogItem();
		logItem.setClassName(this.getClass().getName());
	}
public int getNextId(final String name) {
		try {
			if (this.getJtN().queryForInt("select count(1) from sms..t_dx_sys_id where name='"
										+ name + "'") > 0) {
				c = this.getJtN()
						.queryForInt(
								"select num from sms..t_dx_sys_id where name='"
										+ name + "'");
			}
			if (c < 1) {
				if (this.getJtN().update(
						"insert into sms..t_dx_sys_id(name,num) values(?,1)",
						new Object[] { name }) > 0)
					c = 1;
				else
					c = -1;
			} else {
				if (this.getJtN().update(
						"update sms..t_dx_sys_id set num=num+1 where name=?",
						new Object[] { name }) > 0)
					c += 1;
				else
					c = -1;
			}
		} catch (Exception e) {
			String msg = new StringBuffer("获取下一ID时出现异常:")
					.append(e.toString()).toString();
			log.error(msg);
		}
		return c;
	}
}


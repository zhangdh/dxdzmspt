package com.coffice.hjzx.syschronizeInfo;

import java.util.List;
import java.util.Map;

/**
 * 信息同步接口
 * @author mengfh
 */
public interface SynchroInfo {
	/**
	 * 信息同步方法
	 * @param tableName 需要同步表明
	 * @return
	 * @throws Exception 
	 */
	public List<Map> synchroInfo(String tableName,String vdn);
}

package com.coffice.form.use;

import java.util.Map;

public class FilterData {
	public static Map filter(String stepid,Map dataMap){
		String sfbm = dataMap.get("sfbm")==null?"":String.valueOf(dataMap.get("sfbm"));
		if("200102".equals(sfbm)){
			if("".equals(stepid)){
				
			}
			
		}
		return dataMap;
	}
}

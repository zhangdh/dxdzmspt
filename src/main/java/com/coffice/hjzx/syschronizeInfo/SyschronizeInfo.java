package com.coffice.hjzx.syschronizeInfo;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.caucho.hessian.client.HessianProxyFactory;
import com.coffice.util.BaseUtil;


public class SyschronizeInfo extends BaseUtil{
	/**
	 * 信息同步方法
	 * @param url 验证需要的url
	 * @param tableName 需要同步的表明
	 * @param insertSql 插入语句头部语句
	 */
	public void syschronizeInfo(String url,String tableName,String insertSql,String vdn) {
		
		//		String url = "http://localhost:9000/hessian/hessianService/synchro.rs";  
		        HessianProxyFactory factory = new HessianProxyFactory();
		        SynchroInfo syn;
		        boolean flag=false;//标识服务器端是否验证通过
		        List<Map> _list = null;//同步信息集合
		        
		        //服务器端验证
				try {
					syn = (SynchroInfo)factory.create(SynchroInfo.class, url);
					_list = syn.synchroInfo(tableName,vdn);
					flag=true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//信息同步
				try {
					if(flag) {
						if(_list!=null&&_list.size()>0) {
							for(int i=0;i<_list.size();i++) {
								Object obj = (Object)_list.get(i);
								String str_temp = obj.toString();
								String value_str = "";
								str_temp=str_temp.substring(1,str_temp.indexOf("}"));
								String[] valueArr = str_temp.split(",");
								for(int j=0;j<valueArr.length;j++) {
//									System.out.println(valueArr[j]);
									String temp1 = valueArr[j].substring(valueArr[j].indexOf("=")+1,valueArr[j].length());
									String temp2 = "";
									String regEx="\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d.."; 
									Pattern p=Pattern.compile(regEx);
									Matcher m=p.matcher(temp1);
									if("null".equals(temp1)) {
										temp2=valueArr[j].substring(valueArr[j].indexOf("=")+1,valueArr[j].length())+",";
									} 
//									else if(temp1.length()<=21&&m.find()){
//										temp2="to_date('"+temp1.substring(0,temp1.lastIndexOf("."))+"','yyyy-mm-dd hh24:mi:ss')"+",";
//									}
									else {
										temp2="'"+valueArr[j].substring(valueArr[j].indexOf("=")+1,valueArr[j].length())+"',";
									}
									value_str=value_str+temp2;
								}
								String sql = insertSql+" values("+value_str.substring(0,value_str.lastIndexOf(","))+")";
								this.getJtN().update(sql);
							}
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("客户端信息同步出错！");
				}
			}
}

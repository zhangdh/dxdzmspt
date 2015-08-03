package com.coffice.util;

import java.util.List;
import java.util.Map;



public class IP {
	
	/**
	 * 判断IP是否在某个网段内
	 * @param address
	 * @return
	 */
	public boolean isUnicom(String address){
		boolean flag=false;
		StringBuffer sb = new StringBuffer();
		//山东联通wap网关出公网地址
		sb.append("119.163.122.65~119.163.122.65;");
		sb.append("119.163.122.126~119.163.122.126;");
		sb.append("119.163.122.193~119.163.122.254;");
		//3g web网关出公网地址
		sb.append("119.163.122.161~119.163.122.168;");
		//net公网出口
		sb.append("112.224.3.0~112.224.3.255;");
		sb.append("112.224.2.0~112.224.2.255");
		String ips = sb.toString();
		double[][] ipValue=null;
		try {
			ipValue=convertIp(ips);
			double addValue=calcIp(address);
			for(int i=0;i<ipValue.length;i++){				
				if(addValue>=ipValue[i][0] && addValue<=ipValue[i][1]){
					flag=true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean isKaoqinIp(String ipAdd,String zzid) {
		
		return false;
	}
	/**
	 * 得到所有ip段
	 * @param ips
	 * @return
	 */
	public double[][] convertIp(String ips){
		double[][] ipValue=null;
		String[] ipRow=null;
		String[] ipColum=null;
		try {
			ipRow=ips.split(";");
			ipValue=new double[ipRow.length][2];
			for(int i=0;i<ipRow.length;i++){
				String str=ipRow[i];
				ipColum=str.split("~");
				ipValue[i][0]=calcIp(ipColum[0]);
				ipValue[i][1]=calcIp(ipColum[1]);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipValue;
	}
	
	/**
	 * 计算一个IP数值和
	 * @param address
	 * @return
	 */
	public double calcIp(String address){
		double ipSum=0;
		String[] ipSeg=null;
		try {
			ipSeg=address.split("\\.");
			for(int i=0;i<ipSeg.length;i++){
				ipSum=ipSum+Math.pow(256,(4-i+1))*Integer.parseInt(ipSeg[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipSum;
	}
	
	
}

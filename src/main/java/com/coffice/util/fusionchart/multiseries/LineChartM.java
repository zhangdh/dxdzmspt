
package com.coffice.util.fusionchart.multiseries;


import java.util.List;
import java.util.Map;

public class LineChartM {

	private StringBuffer xmlStr;
	
	/**
	 * 构造方法
	 * @param categoriesList 列名List
	 * @param column 数据表中的列名
	 */
	public LineChartM(List categoriesList,String column){
		xmlStr = new StringBuffer("<chart caption=\"\" lineThickness=\"3\" shownames=\"1\" showvalues=\"1\" showBorder=\"0\"    baseFontSize=\"10\"  bgColor=\"FFFFFF,FFFFFF\" divLineColor=\"CC3300\"  decimals=\"0\">");
		xmlStr.append("<categories>");
		for(int i=0;i<categoriesList.size();i++){
			Map m = (Map) categoriesList.get(i);
			xmlStr.append("<category label=\"").append(m.get(column).toString()).append("\" />");
		}
		xmlStr.append("</categories>");
	}
	
	/**
	 * 构造方法
	 * @param titleListStr 显示列名，固定，以”,“隔开
	 */
	public LineChartM(String titleListStr){
		xmlStr = new StringBuffer("<chart caption=\"\" labelDisplay =\"none\"  shownames=\"1\" showvalues=\"1\"  baseFontSize=\"12\" >");
		xmlStr.append("<categories>");
		for(int i=0;i<titleListStr.split(",").length;i++){
			xmlStr.append("<category label=\"").append(titleListStr.split(",")[i]).append("\" />");
		}
		xmlStr.append("</categories>");
	}
	
	public void dataSet(List dataList,String column,String seriesName,String color,String showValues){
		
	}
	
	//public void dataSet(List dataList,String column,String seriesName,String showValues){
			
	//}

	/**
	 * 设置数据集合
	 * @param dataList 数据集合
	 * @param column 数据集合中要显示的列名
	 * @param seriesName 显示名称
	 */
	public void dataSet(List dataList,String column,String seriesName,String color){
		xmlStr.append("<dataset seriesName=\"").append(seriesName).append("\"");
		xmlStr.append(" color=\"").append(color).append("\"");
		xmlStr.append(" showValues=\"1\">");
		for(int i=0;i<dataList.size();i++){
			xmlStr.append("<set value=\"");
			xmlStr.append(((Map)dataList.get(i)).get(column).toString());
			xmlStr.append("\" />");
		}
		xmlStr.append("</dataset>");
	}
	
	public String getXml(){
		xmlStr.append("</chart>");
		return xmlStr.toString();
	}
}

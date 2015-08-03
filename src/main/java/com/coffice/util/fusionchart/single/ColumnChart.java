package com.coffice.util.fusionchart.single;

import java.util.List;

import com.coffice.util.fusionchart.DataSet;
public class ColumnChart {

	private String caption="";//图表标题
	
	private String xAxisName="";//X轴显示名字
	
	private String yAxisName="";//Y轴显示名字
	
	private String numberPrefix="";//数值单位
	
	private String showValues = "1";//是否显示数值
	
	private String clickURL;//点击超链接URL
	
	private String baseFontSize="12";//字体大小
	
	private String baseFont="宋体";//字体样式
	
	private String useRoundEdges="1";//为圆边
	
	private List dataList;//数据集合

	/**
	 * 构造方法
	 * @param caption 标题
	 * @param xAxisName X轴名字
	 * @param yAxisName Y轴名字
	 */
	public ColumnChart(String caption,String xAxisName,String yAxisName) {
		this.caption = caption;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
	}
	
	/**
	 * 构造方法
	 */
	public ColumnChart() {
	}
	
	/**
	 * 设置标题
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * 设置X轴名字
	 * @param axisName
	 */
	public void setXAxisName(String axisName) {
		xAxisName = axisName;
	}

	/**
	 * 设置Y轴名字
	 * @param axisName
	 */
	public void setYAxisName(String axisName) {
		yAxisName = axisName;
	}

	/**
	 * 设置字体大小
	 * @param baseFontSize
	 */
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}

	/**
	 * 设置字体
	 * @param baseFont
	 */
	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}

	/**
	 * 数值单位，如果输入$，形如 $200
	 * @param numberPrefix
	 */
	public void setNumberPrefix(String numberPrefix) {
		this.numberPrefix = numberPrefix;
	}


	/**
	 * 是否显示数值 
	 * @param showValues 0/1 
	 * @param useRoundEdges 
	 */
	public void setUseRoundEdges(String showValues, String useRoundEdges) {
		this.useRoundEdges = useRoundEdges;
	}
	/**
	 *  
	 * @param showValues 0/1 
	 */
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}


	/**
	 * 点击链接URL
	 * @param clickURL
	 */
	public void setClickURL(String clickURL) {
		this.clickURL = clickURL;
	}

	/**
	 * 数据集合，传入DataSet对象的List集合
	 * @param dataList
	 */
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	/**
	 * 获得图表XML
	 * @return
	 */
	public String dataXml(){
		return createXml();
	}
	
	/**
	 * 内部方法，创建XML字符串。如需扩展属性集，请按照代码自行添加
	 * @return
	 */
	private String createXml(){
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<chart ");
		//开始对图表属性XML进行构建
		xmlBuffer.append("caption='").append(caption).append("' ");
		xmlBuffer.append("xAxisName='").append(xAxisName).append("' ");
		xmlBuffer.append("yAxisName='").append(yAxisName).append("' ");
		xmlBuffer.append("useRoundEdges='").append(useRoundEdges).append("' ");
		xmlBuffer.append("numberPrefix='").append(numberPrefix).append("' ");
		xmlBuffer.append("showValues='").append(showValues).append("' ");
		xmlBuffer.append("baseFontSize='").append(baseFontSize).append("' ");
		if(clickURL!=null){
			xmlBuffer.append("clickURL='").append(clickURL).append("' ");
		}
		xmlBuffer.append(" >\n");
		//开始对数据XML进行构建
		for(int i=0;i<dataList.size();i++){
			DataSet ds = (DataSet) dataList.get(i);
			xmlBuffer.append(ds.dateXml()).append("\n");
		}
		xmlBuffer.append(" </chart>");
		
		return xmlBuffer.toString();
	}
	
}

package com.coffice.util.fusionchart.single;

import java.util.List;

import com.coffice.util.fusionchart.DataSet;

public class PieChart {

	private String caption="";//图表标题
	
	private String palette="5";//颜色样式
	
	private String decimals="2";//小数点位数
	
	private String enableRotation="0";//是否能旋转(可旋转后不能拉出)
	
	private String bgColor="FFFFFF,FFFFFF";//背景颜色
	
	private String bgAlpha = "100";//背景透明度，与颜色都支持渐变
	
	private String bgRatio="0";//背景宽度
	
	private String bgAngle="360";//角度
	
	private String showBorder="0";//显示边框
	
	private String startingAngle="70";//初始角度
	
	private String baseFontSize="12";//字体大小
	
	private String baseFont="宋体";//字体样式

	private List dataList;//数据集合
	
	/**
	 * 设置数据集合
	 * @param dataList
	 */
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	/**
	 * 构造方法
	 */
	public PieChart() {
		
	}
	
	/**
	 * 设置标题
	 * @param caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	/**
	 * 设置字体
	 * @param baseFont
	 */
	public void setBaseFont(String baseFont) {
		this.baseFont = baseFont;
	}
	
	/**
	 * 字体大小
	 * @param baseFontSize
	 */
	public void setBaseFontSize(String baseFontSize) {
		this.baseFontSize = baseFontSize;
	}
	/**
	 * 设置颜色样式
	 * @param palette
	 */
	public void setPalette(String palette) {
		this.palette = palette;
	}

	/**
	 * 设置小数点位数
	 * @param decimals
	 */
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}

	/**
	 * 设置是否能旋转
	 * @param enableRotation
	 */
	public void setEnableRotation(String enableRotation) {
		this.enableRotation = enableRotation;
	}

	/**
	 * 设置背景颜色
	 * @param bgColor
	 */
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	/**
	 * 设置背景透明度
	 * @param bgAlpha
	 */
	public void setBgAlpha(String bgAlpha) {
		this.bgAlpha = bgAlpha;
	}

	/**
	 * 设置背景宽度
	 * @param bgRatio
	 */
	public void setBgRatio(String bgRatio) {
		this.bgRatio = bgRatio;
	}

	/**
	 * 设置角度
	 * @param bgAngle
	 */
	public void setBgAngle(String bgAngle) {
		this.bgAngle = bgAngle;
	}

	/**
	 * 设置是否显示边框
	 * @param showBorder
	 */
	public void setShowBorder(String showBorder) {
		this.showBorder = showBorder;
	}

	/**
	 * 初始角度
	 * @param startingAngle
	 */
	public void setStartingAngle(String startingAngle) {
		this.startingAngle = startingAngle;
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
		xmlBuffer.append("palette='").append(palette).append("' ");
		xmlBuffer.append("decimals='").append(decimals).append("' ");
		xmlBuffer.append("enableRotation='").append(enableRotation).append("' ");
		xmlBuffer.append("bgColor='").append(bgColor).append("' ");
		xmlBuffer.append("bgAlpha='").append(bgAlpha).append("' ");
		xmlBuffer.append("bgRatio='").append(bgRatio).append("' ");
		xmlBuffer.append("bgAngle='").append(bgAngle).append("' ");
		xmlBuffer.append("showBorder='").append(showBorder).append("' ");
		xmlBuffer.append("startingAngle='").append(startingAngle).append("' ");
		xmlBuffer.append("baseFontSize='").append(baseFontSize).append("' ");
		xmlBuffer.append("baseFont='").append(baseFont).append("' ");
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

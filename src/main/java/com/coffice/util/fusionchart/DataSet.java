package com.coffice.util.fusionchart;

public class DataSet {

	private String label;//标题
	
	private String value;//值
	
	private String color;//颜色
	
	private String link;//单条数据超链接
	
	private String showLabel;//是否显示标题
	
	private String showValue;//是否显示值
	
	private String alpha;//透明度 0~100
	
	/**
	 * 构造方法
	 * @param label 标题
	 * @param value 值
	 */
	public DataSet(String label,String value) {
		this.label = label;
		this.value = value;
	}

	
	/**
	 * 设置颜色
	 * @param color 色值
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 单条数据超链接
	 * @param link 链接URL
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 是否显示标题
	 * @param showLabel true false 字符串
	 */
	public void setShowLabel(String showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * 是否显示值
	 * @param showValue true false 字符串
	 */
	public void setShowValue(String showValue) {
		this.showValue = showValue;
	}

	/**
	 * 单条数据透明度
	 * @param alpha 0~100字符串
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	/**
	 * 获取单条数据XML
	 * @return XML字符串
	 */
	public String dateXml(){
		return createXml();
	}
	
	/**
	 * 内部方法，创建XML字符串。如需扩展属性集，请按照代码自行添加
	 * @return
	 */
	private String createXml(){
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<set ");
		if(label!=null){
			xmlBuffer.append("label='").append(label).append("' ");
		}
		if(value!=null){
			xmlBuffer.append("value='").append(value).append("' ");
		}
		if(color!=null){
			xmlBuffer.append("color='").append(color).append("' ");
		}
		if(link!=null){
			xmlBuffer.append("link='").append(link).append("' ");
		}
		if(showLabel!=null){
			xmlBuffer.append("showLabel='").append(showLabel).append("' ");
		}
		if(showValue!=null){
			xmlBuffer.append("showValue='").append(showValue).append("' ");
		}
		if(alpha!=null){
			xmlBuffer.append("alpha='").append(alpha).append("' ");
		}
		xmlBuffer.append(" />");
		return xmlBuffer.toString();
	}
}

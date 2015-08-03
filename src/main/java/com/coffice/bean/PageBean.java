package com.coffice.bean;

import java.util.Map;

/**
 * 数据分页查询
 * 
 * @author lin
 * 
 */
public class PageBean {
	private String sql = "";// 要执行的sql语句

	private String countSql = "";// 复杂sql需要指定计算总记录数的语句

	private String currentPage = "";// 当前页码

	private String pageGoto = "";// 要转到的页数

	private String pageSize = "10";// 每页大小

	private Map namedParameters;// 参数值，供npjt使用

	private int page_allPage = 0;// 返回到页面，总页数

	private int page_cur = 1;// 返回到页面，当前页

	private int page_allCount = 0;// 返回到页面，总记录数

	private String orderBy = "";// 排序字段

	public String getPageGoto() {
		return pageGoto;
	}

	public void setPageGoto(String pageGoto) {
		this.pageGoto = pageGoto;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCountSql() {
		return countSql;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public Map getNamedParameters() {
		return namedParameters;
	}

	public void setNamedParameters(Map namedParameters) {
		this.namedParameters = namedParameters;
	}

	public int getPage_allCount() {
		return page_allCount;
	}

	public void setPage_allCount(int page_allCount) {
		this.page_allCount = page_allCount;
	}

	public int getPage_allPage() {
		return page_allPage;
	}

	public void setPage_allPage(int page_allPage) {
		this.page_allPage = page_allPage;
	}

	public int getPage_cur() {
		return page_cur;
	}

	public void setPage_cur(int page_cur) {
		this.page_cur = page_cur;
	}

	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 正常sql中order by后页面内容<br>
	 * setSql中不能包含排序字段
	 * 
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}

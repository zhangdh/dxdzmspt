package com.coffice.solr;

import java.util.List;

public class SolrPage
{
  private long dataTotal;
  private long pageSize;
  private long currentPage = 1L;
  private long pageTotal;
  private List<SolrBean> pageData;

  public long getDataTotal()
  {
    return this.dataTotal;
  }

  public void setDataTotal(long dataTotal) {
    this.dataTotal = dataTotal;
  }

  public long getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(long pageSize) {
    this.pageSize = pageSize;
  }
  public long getPageTotal() {
    return this.pageTotal;
  }

  public void setPageTotal(long pageTotal) {
    this.pageTotal = pageTotal;
  }

  public long getCurrentPage() {
    return this.currentPage;
  }

  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
  }

  public List<SolrBean> getPageData() {
    return this.pageData;
  }

  public void setPageData(List<SolrBean> pageData) {
    this.pageData = pageData;
  }
}
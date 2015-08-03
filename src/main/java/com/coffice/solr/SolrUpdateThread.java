package com.coffice.solr;

public class SolrUpdateThread extends Thread
{
  private String lmid;
  private String qxids;

  public SolrUpdateThread(String lmid, String qxids)
  {
    this.lmid = lmid;
    this.qxids = qxids;
  }

  public void run() {
    SolrUtil.startUpdateThread(this.lmid, this.qxids);
  }
}
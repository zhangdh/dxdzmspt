package com.coffice.solr;

import java.io.File;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;

public class SolrSample
{
  public void addOrUpdateSample()
  {
    String cjsj = "2012-10-16 10:10:10";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SolrBean solrBean = new SolrBean();
    solrBean.setGuid("2011112413265967191899");
    solrBean.setBt("测试标题法国公共");
    solrBean.setContent("目前有两个版本的 mmseg4j，1.7 版比较耗内存（一个词库目录就要 50M 左右），2011112413265967191899,43,55,22");
    solrBean.setLbid("lmid_2");
    solrBean.setQxids("cpid:8;bmid:12_34_56_78");
    try {
      solrBean.setCjsj(df.parse(cjsj));
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    Map map1 = new HashMap();
    map1.put("ceshi_content", "ddddddddddddd");
    solrBean.setDynamicContent(map1);

    Map map2 = new HashMap();
    map2.put("ceshi_s", "sssssss");
    solrBean.setDynamicStr(map2);

    SolrUtil.addOrUpdate(solrBean);
  }

  public void batchUpdateSample()
  {
    SolrUtil.batchUpdate("lmid_2", "newsqxids");
  }

  public void querySample()
  {
  }

  public void deleteSample()
  {
    SolrUtil.deleteByGuid("guid_2");
    System.out.println("删除guid_2成功！");

    SolrUtil.deleteByQuery("*:*");
    System.out.println("删除全部成功！");
  }

  public static void main(String[] args) throws Exception
  {
    SolrSample test = new SolrSample();
    test.addOrUpdateSample();
  }

  public static CommonsHttpSolrServer getSolrServer()
    throws Exception
  {
    String url = "http://localhost:8080/solr";
    CommonsHttpSolrServer server = new CommonsHttpSolrServer(url);
    server.setSoTimeout(60000);
    server.setConnectionTimeout(100);
    server.setDefaultMaxConnectionsPerHost(100);
    server.setMaxTotalConnections(100);
    server.setFollowRedirects(false);
    server.setAllowCompression(true);
    server.setMaxRetries(1);
    server.setParser(new XMLResponseParser());
    return server;
  }

  public static void solrAttachment()
  {
    try
    {
      String urlString = "http://localhost:8090/solr";
      SolrServer solr = getSolrServer();
      ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
      up.addFile(new File("D:/文档.docx"));

      up.setParam("literal.qxids", "cpid:8,zzid:9_10");
      up.setParam("literal.id", "123456789");
      up.setParam("literal.cjsj", "2012-11-21");
      up.setParam("uprefix", "attr_");
      up.setParam("fmap.content", "content");
      up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
      solr.request(up);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
package com.coffice.solr;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest.ACTION;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

public class SolrUtil
{
  private static void commitFileToSolr(ContentStreamUpdateRequest updateRequest)
  {
    try
    {
      updateRequest.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, 
        true);
      getCommonsHttpSolrServer().request(updateRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List querySolrDocuments(SolrQuery query, SolrPage page)
  {
    List data = new ArrayList();

    Iterator solrDocuments = null;
    try {
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      QueryResponse queryResponse = server.query(query);
      SolrDocumentList list = queryResponse.getResults();
      if (page != null) {
        page.setDataTotal(list.getNumFound());
      }
      solrDocuments = list.iterator();
      data.add(queryResponse);
      data.add(solrDocuments);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return data;
  }

  private static void commitDocsToSolr(Collection<SolrInputDocument> docs)
  {
    try
    {
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      int timeOut = 10000;
      server.setConnectionTimeout(10000);

      server.add(docs);
      server.commit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static CommonsHttpSolrServer getCommonsHttpSolrServer()
  {
    CommonsHttpSolrServer server = null;
    try {
      int timeOut = 5000;
      int maxConnection = 50;
      int maxToalConnections = 1000;
      String url = ProperUtil.getURL();
      server = new CommonsHttpSolrServer(url);
      server.setSoTimeout(5000);
      server.setConnectionTimeout(5000);
      server.setDefaultMaxConnectionsPerHost(50);
      server.setMaxTotalConnections(1000);
      server.setFollowRedirects(false);

      server.setAllowCompression(true);
      server.setMaxRetries(0);
      server.setParser(new XMLResponseParser());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return server;
  }

  public static void addOrUpdate(SolrBean solrBean)
  {
    String v = "";
    String k = "";
    Set keySet = new HashSet();
    String id = solrBean.getGuid();
    String bt = solrBean.getBt();
    String lmid = solrBean.getLbid();
    String content = solrBean.getContent();
    String qxids = solrBean.getQxids();
    Map dynamicStr = solrBean.getDynamicStr();
    Map dynamicContent = solrBean.getDynamicContent();
    Map fjlj = solrBean.getFjlj();
    try {
      Collection docs = new ArrayList();
      SolrInputDocument doc1 = new SolrInputDocument();
      Date cjsj = solrBean.getCjsj();
      if ((cjsj != null) && (!"".equals(cjsj))) {
        Calendar c = Calendar.getInstance();
        c.setTime(cjsj);
        c.add(11, 8);
        doc1.addField("cjsj", c.getTime());
      }
      doc1.addField("id", id);
      doc1.addField("lmid", lmid);
      doc1.addField("bt", bt);
      doc1.addField("content", content);
      doc1.addField("qxids", qxids);

      keySet = dynamicStr.keySet();
      for (Iterator key = keySet.iterator(); key.hasNext(); ) {
        k = (String)key.next();
        if (k.endsWith("_s")) {
          doc1.addField(k, dynamicStr.get(k));
        }
      }

      keySet = dynamicContent.keySet();
      for (Iterator key = keySet.iterator(); key.hasNext(); ) {
        k = (String)key.next();
        if (k.endsWith("_content")) {
          doc1.addField(k, dynamicContent.get(k));
        }
      }

      docs.add(doc1);
      commitDocsToSolr(docs);

      if (fjlj.size() == 0) {
        return;
      }
      Iterator keyIter = fjlj.keySet().iterator();
      String key = "";
      while (keyIter.hasNext()) {
        key = (String)keyIter.next();
        ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
          ProperUtil.getValueByProper("StreamUpdateRequest"));
        up.addFile(new File((String)fjlj.get(key)));

        up.setParam("literal.id", "fj_" + key);
        up.setParam("literal.bt", bt);
        up.setParam("literal.lmid", lmid);
        up.setParam("literal.filepath", (String)fjlj.get(key));
        up.setParam("literal.qxids", qxids);
        up.setParam("uprefix", "attr_");
        up.setParam("fmap.content", "content");
        commitFileToSolr(up);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static void updateAttachmentTypeIndex(SolrDocument doc)
  {
    try
    {
      String id = (String)doc.getFieldValue("id");
      String lmid = (String)doc.getFieldValue("lmid");
      String bt = (String)doc.getFieldValue("bt");
      String qxids = (String)doc.getFieldValue("qxids");
      String filepath = (String)doc.getFieldValue("filepath");
      ContentStreamUpdateRequest up = new ContentStreamUpdateRequest(
        ProperUtil.getValueByProper("StreamUpdateRequest"));
      up.addFile(new File(filepath));
      up.setParam("literal.id", id);
      up.setParam("literal.bt", bt);
      up.setParam("literal.lmid", lmid);
      up.setParam("literal.filepath", filepath);
      up.setParam("literal.qxids", qxids);
      up.setParam("uprefix", "attr_");
      up.setParam("fmap.content", "content");
      commitFileToSolr(up);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void updateSolrDocumentTypeIndex(SolrDocument doc)
  {
    try
    {
      String id = (String)doc.getFieldValue("id");
      String lmid = (String)doc.getFieldValue("lmid");
      String bt = (String)doc.getFieldValue("bt");
      String qxids = (String)doc.getFieldValue("qxids");
      String content = (String)doc.getFieldValue("content");
      Collection docs = new ArrayList();
      SolrInputDocument doc1 = new SolrInputDocument();
      doc1.addField("id", id);
      doc1.addField("lmid", lmid);
      doc1.addField("bt", bt);
      doc1.addField("content", content);
      doc1.addField("qxids", qxids);
      docs.add(doc1);
      commitDocsToSolr(docs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void update(String id, String qxids)
  {
    try
    {
      SolrQuery query = new SolrQuery();
      query.setQuery("id:" + id);
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      QueryResponse queryResponse = server.query(query);
      SolrDocumentList docs = queryResponse.getResults();
      SolrDocument doc = null;
      String docId = "";
      for (int i = 0; i < docs.size(); i++) {
        doc = (SolrDocument)docs.get(i);
        docId = (String)doc.getFieldValue("id");
        doc.setField("qxids", qxids);
        if (id.startsWith("fj_"))
          updateAttachmentTypeIndex(doc);
        else
          updateSolrDocumentTypeIndex(doc);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void startUpdateThread(String lmid, String qxids)
  {
    try
    {
      List<String> ids = queryAllOfId(lmid);
      int zongshu = ids.size();
      int index = 1;
      for (String id : ids) {
        update(id, qxids);

        index++;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void batchUpdate(String lmid, String qxids)
  {
    SolrUpdateThread updateThread = new SolrUpdateThread(lmid, qxids);
    updateThread.start();
  }

  public static SolrPage queryByLm(String lmids, String keyword, int currentPage, int pageSize)
  {
    String queyLastPar = "(" + getQueryLmids(lmids) + " AND " + 
      getQueryKeyword(keyword, "", "content") + ")";
    return query(queyLastPar, "and", currentPage, pageSize);
  }

  public static SolrPage queryQxidsOR(String qxid, String keyword, int currentPage, int pageSize, Date startDate, Date endDate, String operator)
  {
    String queryStr = "";
    if (("*".equals(qxid.trim())) || ("".equals(qxid))) {
      queryStr = getQueryKeyword(keyword, operator, "content");
      return query(queryStr, "and", currentPage, pageSize);
    }
    queryStr = parseQxidsExp(qxid, "OR");
    if (queryStr.trim().endsWith("OR")) {
      queryStr = queryStr.substring(0, queryStr.lastIndexOf("OR")).trim();
    }
    queryStr = "(" + queryStr + ")";
    queryStr = queryStr + " AND " + getQueryKeyword(keyword, operator, "content");
    String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    if ((startDate != null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate == null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[* TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate != null) && (endDate == null)) {
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO *]";
    }

    return query(queryStr, "and", currentPage, pageSize);
  }

  public static SolrPage queryQxidsAnd(String qxid, String keyword, int currentPage, int pageSize, Date startDate, Date endDate, String operator)
  {
    String queryStr = "";
    if (("*".equals(qxid.trim())) || ("".equals(qxid))) {
      queryStr = getQueryKeyword(keyword, operator, "content");
      return query("*:*", "and", currentPage, pageSize);
    }
    queryStr = parseQxidsExp(qxid, "AND");
    queryStr = queryStr + getQueryKeyword(keyword, operator, "content");
    if (queryStr.trim().endsWith("AND")) {
      queryStr = queryStr.substring(0, queryStr.lastIndexOf("AND")).trim();
    }
    String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    if ((startDate != null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate == null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[* TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate != null) && (endDate == null)) {
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO *]";
    }

    return query(queryStr, "and", currentPage, pageSize);
  }

  public static SolrPage queryQxidsExp(String queryStr, int currentPage, int pageSize)
  {
    return query(queryStr, "and", currentPage, pageSize);
  }

  public static SolrPage queryByQx(String qxid, Map keyword, int currentPage, int pageSize, Date startDate, Date endDate, String operator)
  {
    String queryStr = "";
    String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    queryStr = parseQxidsExp(qxid, "AND");
    queryStr = queryStr + parseKeyWord(keyword, operator);

    if (queryStr.trim().endsWith("AND")) {
      queryStr = queryStr.substring(0, queryStr.lastIndexOf("AND")).trim();
    }
    if (startDate != null) {
      Calendar cstart = Calendar.getInstance();
      cstart.setTime(startDate);
      cstart.add(10, -8);
      startDate = cstart.getTime();
    }
    if (endDate != null) {
      Calendar estart = Calendar.getInstance();
      estart.setTime(endDate);
      estart.add(10, -8);
      endDate = estart.getTime();
    }
    if ((startDate != null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate == null) && (endDate != null))
      queryStr = queryStr + " AND cjsj:[* TO " + DateFormatUtils.format(endDate, pattern) + "]";
    else if ((startDate != null) && (endDate == null)) {
      queryStr = queryStr + " AND cjsj:[" + DateFormatUtils.format(startDate, pattern) + " TO *]";
    }

    return query(queryStr, "and", currentPage, pageSize);
  }

  public static String parseKeyWord(Map<String, String> map, String operator)
  {
    if ((map == null) || (map.isEmpty())) {
      return "";
    }
    Set set = map.keySet();
    String k = "";
    String v = "";
    String queryStr = "";
    for (Iterator i = set.iterator(); i.hasNext(); ) {
      k = (String)i.next();
      v = (String)map.get(k);
      queryStr = queryStr + " AND " + getQueryKeyword(v, operator, k);
    }
    queryStr = queryStr.trim().replaceFirst("AND", "").trim();
    return queryStr;
  }

  public static String parseQxidsExp(String qxid, String operator)
  {
    if ((operator == null) || ("".equals(operator))) {
      operator = "and";
    }
    if (("*".equals(qxid.trim())) || ("".equals(qxid))) {
      return " qxids:*  AND ";
    }
    operator = operator.toUpperCase();
    String queryStr = "";

    String[] qxids = qxid.split(",");
    Map map = new HashMap();
    String preStr = "";
    String v = "";
    String[] aqxid = new String[2];
    for (int i = 0; i < qxids.length; i++) {
      aqxid = qxids[i].split(":");
      preStr = aqxid[0];
      v = (String)map.get(preStr);
      if ((v == null) || (v.equals("")))
        v = "\"" + qxids[i] + "\"";
      else {
        v = v + "OR\"" + qxids[i] + "\"";
      }
      map.put(preStr, v);
    }
    Set k = map.keySet();
    for (Iterator i = k.iterator(); i.hasNext(); ) {
      v = (String)map.get(i.next());
      if (v.indexOf("OR") > -1)
        queryStr = queryStr + "qxids:(" + v + ") " + operator + " ";
      else {
        queryStr = queryStr + "qxids:" + v + " " + operator + " ";
      }
    }
    return queryStr;
  }

  private static List<String> queryAllOfId(String lmid)
  {
    List ids = new ArrayList();
    try {
      SolrQuery query = new SolrQuery();
      query.set("fl", new String[] { "id" });
      query.setQuery("lmid:" + lmid);
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      QueryResponse queryResponse = server.query(query);
      SolrDocumentList docs = queryResponse.getResults();
      SolrDocument doc = null;
      String id = "";
      for (int i = 0; i < docs.size(); i++) {
        doc = (SolrDocument)docs.get(i);
        id = (String)doc.getFieldValue("id");
        ids.add(id);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return ids;
  }

  private static SolrPage query(String queyLastPar, String andOr, int currentPage, int pageSize)
  {
    if (queyLastPar.trim().equals("")) {
      queyLastPar = "*:*";
    }
    int contentFragsize = 100;
    SolrPage solrPage = new SolrPage();
    if (currentPage - 1 < 0) {
      return solrPage;
    }
    int start = (currentPage - 1) * pageSize;
    if (start < 0) {
      start = 0;
    }
    SolrQuery query = new SolrQuery();

    query.setQuery(queyLastPar);
    query.addHighlightField("content").setHighlight(true).setHighlightSimplePre("<span class='solr_Highlight'>")
      .setHighlightSimplePost("</span>").set("hl.snippets", 1)
      .set("hl.mergeContinuous", true).set("hl.fragsize", 100)
      .set("hl.requireFieldMatch", true);
    query.addHighlightField("bt").setHighlight(true).setHighlightSimplePre("<span class='solr_Highlight'>")
      .setHighlightSimplePost("</span>").set("hl.requireFieldMatch", true);
    query.setStart(new Integer(start));
    query.setRows(new Integer(pageSize));
    List myRes = querySolrDocuments(query, solrPage);
    initSolrpage(myRes, solrPage, currentPage, pageSize);
    return solrPage;
  }

  private static void initSolrpage(List<Object> myRes, SolrPage solrPage, int currentPage, int pageSize)
  {
    List pageData = new ArrayList();
    QueryResponse queryResponse = (QueryResponse)myRes.get(0);
    Iterator solrDocuments = (Iterator)myRes.get(1);
    long pageTotal = solrPage.getDataTotal() / pageSize;
    if (pageTotal * pageSize != solrPage.getDataTotal())
      solrPage.setPageTotal(pageTotal + 1L);
    else {
      solrPage.setPageTotal(pageTotal);
    }
    solrPage.setCurrentPage(currentPage);
    solrPage.setPageSize(pageSize);
    SolrDocument resultDoc = null;

    String content = "";
    String bt = "";

    List dynamicKey = new ArrayList();
    String dykey = "";
    SolrBean solrBean = null;
    while (solrDocuments.hasNext()) {
      content = ""; bt = "";
      solrBean = new SolrBean();
      resultDoc = (SolrDocument)solrDocuments.next();

      if (dynamicKey.isEmpty()) {
        Set keyset = resultDoc.keySet();
        for (Iterator keyIterator = keyset.iterator(); keyIterator.hasNext(); ) {
          dykey = (String)keyIterator.next();

          if (dykey.endsWith("_s"))
            dynamicKey.add(dykey);
          else if (dykey.endsWith("_content")) {
            dynamicKey.add(dykey);
          }
        }
      }
      Date date = (Date)resultDoc.getFieldValue("cjsj");
      String id = (String)resultDoc.getFieldValue("id");
      String lmid = (String)resultDoc.getFieldValue("lmid");
      String qxids = (String)resultDoc.getFieldValue("qxids");
      if (queryResponse.getHighlighting().get(id) != null) {
        Map hightlightMap = 
          (Map)queryResponse
          .getHighlighting().get(id);
        List<String> highlightSnippets = (List)hightlightMap.get("content");
        content = "";
        if (highlightSnippets != null) {
          for (String string : highlightSnippets) {
            content = content + string;
          }
        }
        highlightSnippets = (List)hightlightMap.get("bt");
        if (highlightSnippets != null) {
          for (String string : highlightSnippets) {
            bt = bt + string;
          }
        }
      }
      if (bt.equals("")) {
        bt = (String)resultDoc.getFieldValue("bt");
      }
      if (content.equals("")) {
        content = String.valueOf(resultDoc.getFieldValue("content"));
        if ((content != null) && (content.length() > 100)) {
          content = content.substring(0, 100) + ".....";
        }
      }
      solrBean.setGuid(id);
      solrBean.setBt(bt);
      solrBean.setContent(content);
      solrBean.setLbid(lmid);
      solrBean.setQxids(qxids);
      solrBean.setCjsj(date);
      Map dynamicStr = new HashMap();
      Map dynamicContent = new HashMap();
      for (Iterator keyIterator = dynamicKey.iterator(); keyIterator.hasNext(); ) {
        dykey = (String)keyIterator.next();
        if (dykey.endsWith("_s"))
          dynamicStr.put(dykey, (String)resultDoc.get(dykey));
        else if (dykey.endsWith("_content")) {
          dynamicContent.put(dykey, (String)resultDoc.get(dykey));
        }
      }
      solrBean.setDynamicContent(dynamicContent);
      solrBean.setDynamicStr(dynamicStr);
      pageData.add(solrBean);
    }
    solrPage.setPageData(pageData);
  }

  public static SolrPage queryByLb(String lmids, String keyword, String andOr, int currentPage, int pageSize)
  {
    String queyLastPar = "(" + getQueryLmids(lmids) + " AND " + 
      getQueryKeyword(keyword, "", "content") + ")";
    return query(queyLastPar, andOr, currentPage, pageSize);
  }

  public static SolrPage queryByKeyword(String keyword, int currentPage, int pageSize)
  {
    String queyLastPar = getQueryKeyword(keyword, "", "content");
    return query(queyLastPar, "and", currentPage, pageSize);
  }

  public static SolrPage queryByKeyword(String keyword, String andOr, int currentPage, int pageSize)
  {
    String queyLastPar = getQueryKeyword(keyword, "", "content");
    return query(queyLastPar, andOr, currentPage, pageSize);
  }

  public static void deleteByGuid(String guid)
  {
    try
    {
      List list = null;
      if ((guid != null) && (!guid.equals(""))) {
        if (guid.indexOf("~") != -1) {
          String[] strings = guid.split("~");
          list = new ArrayList();
          for (String string : strings) {
            list.add(string);
          }
        }
      }
      else {
        return;
      }
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      if (list == null)
        server.deleteById(guid);
      else {
        server.deleteById(list);
      }
      server.commit();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void deleteByQuery(String query)
  {
    try
    {
      CommonsHttpSolrServer server = getCommonsHttpSolrServer();
      server.deleteByQuery(query);
      server.commit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getQueryLmids(String lmids)
  {
    if ((lmids == null) || ("".equals(lmids.trim()))) {
      return "";
    }
    if ("*".equals(lmids.trim())) {
      return "lmid:*";
    }

    String[] ids = lmids.split(",");
    String temp = "";
    String resLmids;
    if (ids.length > 1)
    {
      for (String string : ids) {
        if (temp.equals(""))
          temp = string;
        else {
          temp = temp + " OR lmid:" + string;
        }
      }
      resLmids = "(lmid:" + temp + ")";
    } else {
      resLmids = "(lmid:" + lmids + ")";
    }
    return resLmids;
  }

  private static String getQueryKeyword(String keyword, String operator, String key)
  {
    String nowKeyword = "";
    if ((operator == null) || ("".equals(operator))) {
      operator = "AND";
    }
    if ((keyword == null) || (keyword.trim().equals(""))) {
      return "";
    }
    String[] words = keyword.split(" ");
    for (String string : words) {
      if ("".equals(string)) {
        continue;
      }
      if (nowKeyword.equals(""))
        nowKeyword = "\"" + string + "\"";
      else {
        nowKeyword = nowKeyword + operator.toUpperCase() + "\"" + string + "\"";
      }
    }

    nowKeyword = "(" + nowKeyword + ")";
    if (key.equals("content")) {
      return "(content:" + nowKeyword + " OR bt:" + nowKeyword + ")";
    }
    return key + ":" + nowKeyword;
  }
}
package com.coffice.knowledge;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.zilverline.service.CollectionManager;

import com.coffice.solr.SolrPage;

import com.coffice.solr.SolrBean;
import com.coffice.solr.SolrUtil;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class Knowledge extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	Map map;
	
	private CollectionManager manager=null;
	
	public Knowledge(){
		jjd = new JspJsonData();
		logItem = new LogItem();
	}
	
	public Knowledge(Map mapIn){
		jjd = new JspJsonData();
		logItem = new LogItem();
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		zzid = (String) mapIn.get("zzid");
		bmid = (String) mapIn.get("bmid");
		gwid = (String) mapIn.get("gwid");
		yhid = (String) mapIn.get("yhid");
		this.map = mapIn;
	}
	
	public void setMap(Map _map){
		this.map=_map;
	}
	
	public Map saveSortTree() {
		try{
			
			String sjGuid = map.get("curkid")==null?"":String.valueOf(map.get("curkid"));
			String kName = map.get("kname")==null?"":String.valueOf(map.get("kname"));
			String guid = sjGuid+"_"+Db.getNextId();
			this.getJtN().update("insert into t_knowledge values('"+guid+"','"+sjGuid+"','"+kName+"',1,1,getDate())");
			//重新查询更新后的知识库目录
			List _list = this.getJtN().queryForList("select kid,ksjid,kname from t_knowledge where yxbz = 1 ");
			StringBuffer treeStr = new StringBuffer();
			treeStr.append("[");
			for(int i = 0;i < _list.size(); i ++){
				Map _map = (Map)_list.get(i);
				if("0".equals(_map.get("ksjid"))){
					treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'root.png'},");
				}else{
					treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'close.png',iconOpen:'open.png',iconClose:'close.png'},");
				}
			}
			String treeString = treeStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("kbList",treeString);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.saveSortTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"保存知识库目录时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public Map getSortTree() {
		try{
			List _list = this.getJtN().queryForList("select kid,ksjid,kname from t_knowledge where yxbz = 1 ");
			StringBuffer treeStr = new StringBuffer();
			treeStr.append("[");
			for(int i = 0;i < _list.size(); i ++){
				Map _map = (Map)_list.get(i);
				if("0".equals(_map.get("ksjid"))){
					treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'root.png'},");
				}else{
					treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'close.png',iconOpen:'open.png',iconClose:'close.png'},");
				}
			}
			String treeString = treeStr.toString();
			if(treeString.length()>1){
				treeString = treeString.substring(0,treeString.length()-1);
				treeString = treeString+"]";
			}
			jjd.setExtend("kbList",treeString);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.getSortTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"得到知识库目录时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map delSortTree() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			int num = this.getJtN().queryForInt("select count(*) from t_knowledge where ksjid = '"+kid+"'");
			if(num>0){
				jjd.setExtend("false","有下级目录，无法删除");
			}else{
				int itemnum = this.getJtN().queryForInt("select count(*) from t_knowledge_item where sskid = '"+kid+"'");
				if(itemnum>0){
					jjd.setExtend("false","目录下有知识项，无法删除");
				}else{
					this.getJtN().update("delete from t_knowledge where kid='"+kid+"'");
				}
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.delSortTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"得到知识库目录时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map saveItem() {
		try{
			String sskid = map.get("sskid1")==null?"":String.valueOf(map.get("sskid1"));
			String kbt = map.get("kbt")==null?"":String.valueOf(map.get("kbt"));
			String knr = map.get("knr")==null?"":String.valueOf(map.get("knr"));
			knr = knr.replaceAll("'","");
			String fjid = map.get("fjid")==null?"":String.valueOf(map.get("fjid"));
			String guid = Guid.get();
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("insert into t_knowledge_item values('").append(yhid).append("','").append(guid).append("','").append(sskid)
				  .append("','").append(kbt).append("','").append(knr).append("','").append(fjid)
				  .append("',1,getDate())");
			this.getJtN().update(sqlStr.toString());
			jjd.setResult(true,"新增成功");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.saveItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"保存知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map delItem() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			this.getJtN().update("delete from t_knowledge_item where kid='"+kid+"'");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.delItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map queryItem() {
		try{
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlWhere = new StringBuffer();
			String temp ="";
			temp = map.get("keynr")==null?"":String.valueOf(map.get("keynr"));
			if(!"".equals(temp)){
				sqlWhere.append(" and knr like '%").append(temp).append("%'");
			}
			temp = map.get("sskid")==null?"":String.valueOf(map.get("sskid"));
			if(!"".equals(temp)){
				sqlWhere.append(" and sskid in ('").append(temp).append("')");
			}else{
				sqlWhere.append(" and sskid = '0' ");
			}
			sqlStr.append("select kid,(select xm from t_org_yh where yhid = a.yhid)xm,kbt,cjsj,(select kname from t_knowledge where kid=a.sskid )sskid from t_knowledge_item a where yxbz = 1 ");
			sqlStr.append(sqlWhere);
			sqlCount.append("select count(*) from t_knowledge_item where yxbz = 1 ").append(sqlWhere);
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.queryItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	public Map queryKb() {
		try{
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlWhere = new StringBuffer();
			String ksjid = map.get("ksjid")==null?"":String.valueOf(map.get("ksjid"));
			if("".equals(ksjid)){
				ksjid = "1";
			}
			sqlStr.append("select kid,kname,cjsj from t_knowledge a where yxbz = 1 ")
			      .append(" and ksjid='").append(ksjid).append("'");
			sqlCount.append("select count(*) from t_knowledge where yxbz = 1 ")
					.append(" and ksjid='").append(ksjid).append("'");
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.queryItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	public Map openKb() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			Map _map = Db.getJtN().queryForMap("select * from t_knowledge where kid='"+kid+"'");
			jjd.setForm(_map);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.openKb()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"查看知识目录时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	public Map queryKeyItem() {
		try{
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlWhere = new StringBuffer();
			String temp ="";
			temp = map.get("keyword")==null?"":String.valueOf(map.get("keyword"));
			if(!"".equals(temp)){
				sqlWhere.append(" and (knr like '%").append(temp).append("%' or kbt like '%").append(temp).append("%')");
			}
			temp = map.get("sskid")==null?"":String.valueOf(map.get("sskid"));
			if(!"".equals(temp)){
				sqlWhere.append(" and sskid like '").append(temp).append("%'");
			}
			sqlStr.append("select kid,(select xm from t_org_yh where yhid = a.yhid)xm,kbt,cjsj,(select kname from t_knowledge where kid=a.sskid ) sskid from t_knowledge_item a where yxbz = 1 ");
			sqlStr.append(sqlWhere);
			sqlCount.append("select count(*) from t_knowledge_item where yxbz = 1 ").append(sqlWhere);
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.queryKeyItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"查询知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map querySolrItem(){		
		try{
			StringBuffer sqlWhere = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			StringBuffer sqlStr = new StringBuffer();
			List<Map> _list = null;
			String keyword = map.get("keyword")==null?"":String.valueOf(map.get("keyword"));
			String sskid = map.get("sskid")==null?"":String.valueOf(map.get("sskid"));
			if(!"".equals(sskid)&& !"''".equals(sskid)){
				sqlWhere.append(" and sskid in (").append(sskid).append(")");
			}
			sqlStr.append("select kid,kbt,knr from t_knowledge_item where (kbt like '%").append(keyword)
				  .append("%' or knr like '%").append(keyword).append("%') ").append(sqlWhere.toString());
			sqlCount.append("select count(*) from t_knowledge_item where (kbt like '%").append(keyword)
				  .append("%' or knr like '%").append(keyword).append("%') ").append(sqlWhere.toString());
			PageBean page = new PageBean();
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by cjsj desc ");
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			_list = Db.getPageData(page);
			for(Map _map:_list){
				_map.put("kbt",((String) _map.get("kbt")).replace(keyword,"<font color='red'>"+keyword+"</font>"));
				String nr = (String) _map.get("knr");
				nr = HtmlUtil.getTextFromHtml(nr);
				int i = nr.indexOf(keyword);
				if(i>-1){
					_map.put("knr", (nr.substring(i-100<0?0:i-100,i)+nr.substring(i,i+100>nr.length()?nr.length():i+100)).replace(keyword, "<font color='red'>"+keyword+"</font>"));
				}
			}
			jjd.setGrid("solrlist", _list, page);
		}catch(Exception e){
			logItem.setMethod("Knowledge.querySolrItem()");
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"查询知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();		
	}
	
	public Map showItem() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			List kList = this.getJtN().queryForList("select fjid,(select xm from t_org_yh where yhid = a.yhid)xm,kbt,knr,cjsj from t_knowledge_item a where yxbz = 1 and kid='"+kid+"'");
			if (kList.size()>0){
				Map _map = (Map)kList.get(0);
				jjd.setExtend("xm",_map.get("xm").toString());
				jjd.setExtend("kname",_map.get("kbt").toString());
				jjd.setExtend("knr",_map.get("knr").toString());
				jjd.setExtend("cjsj",_map.get("cjsj").toString());
				if(!"".equals(_map.get("fjid").toString())){
					jjd.setExtend("ifFj",1);
					String knowledge_dir = SysPara.getValue("knowledge_dir");
					jjd.setExtend("knowledge_dir", knowledge_dir);
					String fjStr = "[";
					List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+_map.get("fjid").toString()+"'");
					for(int i=0;i<fjList.size();i++){
						Map fjMap = (Map)fjList.get(i);
						fjStr = fjStr+"{wjmc:'"+fjMap.get("wjmc")+"',wjlj:'"+fjMap.get("wjlj")+"'},";
					}
					fjStr = fjStr.substring(0,fjStr.length()-1);
					fjStr +="]";
					jjd.setExtend("fjStr",fjStr);
				}
			}
			
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.queryItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map getTree() {
		try{
			List _list = this.getJtN().queryForList("select kid dm,('&nbsp;&nbsp;&nbsp;&nbsp;'+kname) mc from t_knowledge where ksjid = '1' and yxbz =1 ");
			Map _map = new HashMap();
			_map.put("dm","1");
			_map.put("mc","&nbsp;知识仓库");
			List reList = new ArrayList();
			reList.add(_map);
			reList.addAll(_list);
			jjd.setSelect("sskid",reList);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.getTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	public Map updateSortTree() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			String kname = map.get("kname")==null?"":String.valueOf(map.get("kname"));
			if(!"".equals(kid) && !"".equals(kname)){
				Db.getJtN().update("update t_knowledge set kname='"+kname+"' where kid='"+kid+"'");
				jjd.setExtend("msg","更新成功");
				//重新查询更新后的知识库目录
				List _list = this.getJtN().queryForList("select kid,ksjid,kname from t_knowledge where yxbz = 1 ");
				StringBuffer treeStr = new StringBuffer();
				treeStr.append("[");
				for(int i = 0;i < _list.size(); i ++){
					Map _map = (Map)_list.get(i);
					if("0".equals(_map.get("ksjid"))){
						treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'root.png'},");
					}else{
						treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'close.png',iconOpen:'open.png',iconClose:'close.png'},");
					}
				}
				String treeString = treeStr.toString();
				if(treeString.length()>1){
					treeString = treeString.substring(0,treeString.length()-1);
					treeString = treeString+"]";
				}
				jjd.setExtend("kbList",treeString);
			}else{
				jjd.setExtend("msg","更新失败");
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.updateSortTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"更新知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map deleteTree() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			int i = Db.getJtN().queryForInt("select count(*) from t_knowledge_item where yxbz = 1 and sskid='"+kid+"'");
			if(i>0){
				jjd.setResult(false, "");
				jjd.setExtend("msg","删除失败,目录下存在知识项,无法删除");
			}else{
				i = Db.getJtN().queryForInt("select count(*) from t_knowledge where yxbz = 1 and ksjid='"+kid+"'");
				if(i>0){
					jjd.setResult(false, "");
					jjd.setExtend("msg","删除失败,目录下存在子目录,无法删除");
				}else{
					Db.getJtN().update("update t_knowledge set yxbz =0 where yxbz = 1 and  kid='"+kid+"'");
					//重新查询更新后的知识库目录
					List _list = this.getJtN().queryForList("select kid,ksjid,kname from t_knowledge where yxbz = 1 ");
					StringBuffer treeStr = new StringBuffer();
					treeStr.append("[");
					for(int j = 0;j < _list.size(); j ++){
						Map _map = (Map)_list.get(j);
						if("0".equals(_map.get("ksjid"))){
							treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'root.png'},");
						}else{
							treeStr.append("{id:'").append(_map.get("kid")).append("',pId:'").append(_map.get("ksjid")).append("',name:'").append(_map.get("kname")).append("',icon:'close.png',iconOpen:'open.png',iconClose:'close.png'},");
						}
					}
					String treeString = treeStr.toString();
					if(treeString.length()>1){
						treeString = treeString.substring(0,treeString.length()-1);
						treeString = treeString+"]";
					}
					jjd.setExtend("kbList",treeString);
				}
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.deleteTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"更新知识目录时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	
	public Map getAllTree() {
		try{
			List _list = this.getJtN().queryForList("select kid dm,('&nbsp;&nbsp;&nbsp;&nbsp;'+kname) mc,ksjid from t_knowledge where ksjid = '1' and yxbz =1 ");
			List _list1 = this.getJtN().queryForList("select kid dm,('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+kname) mc,ksjid from t_knowledge where ksjid not in ('1','0') and yxbz =1 ");
			Map _map = new HashMap();
			_map.put("dm","1");
			_map.put("mc","&nbsp;知识仓库");
			List reList = new ArrayList();
			reList.add(_map);
			//Map kMap = new HashMap();
			for(int i=0;i<_list.size();i++){
				Map fMap = (Map)_list.get(i);
				reList.add(fMap);
				String kid = fMap.get("dm").toString();
				for(int j=0;j<_list1.size();j++){
					if(kid.equals(((Map)_list1.get(j)).get("ksjid").toString())){
						//kMap.clear();
						Map kMap = new HashMap();
						kMap.put("dm",((Map)_list1.get(j)).get("dm").toString());
						kMap.put("mc",((Map)_list1.get(j)).get("mc").toString());
						reList.add(kMap);
					}
				}	
			}
			jjd.setSelect("sskid1",reList);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.getAllTree()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"得到知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public Map deleteItem() {
		try{
			String kid = map.get("kid")==null?"":String.valueOf(map.get("kid"));
			Db.getJtN().update("update t_knowledge_item set yxbz = 0 where kid ='"+kid+"'");
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Knowledge.deleteItem()");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户："+yhid+"删除知识项时异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	public void addOrUpdateSolr(String guid,String cjsj,String q,String a){
		SolrBean solrBean = new SolrBean();
		solrBean.setGuid(guid);
		solrBean.setBt(q);
		solrBean.setContent(HtmlUtil.getTextFromHtml(a));		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			solrBean.setCjsj(df.parse(cjsj));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		SolrUtil.addOrUpdate(solrBean);	
	}
}

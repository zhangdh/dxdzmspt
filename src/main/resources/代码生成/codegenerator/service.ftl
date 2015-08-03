package ${packagename};

import java.util.List;
import java.util.Map;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
<#if attachment??>
import com.coffice.base.attachment.Attachment;
</#if>
<#if yhfw??>
import com.coffice.base.yhfw.Yhfw;
import com.coffice.bean.UserBean;
</#if>
<#if tx??>
import com.coffice.oa.remind.Remind;
</#if>
import com.coffice.bean.PageBean;
import com.coffice.exception.ServiceException;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class ${serviceClass} extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;

	public ${serviceClass}(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	<#if div_show??>
	/**
	 * 保存数据
	 * @return
	 */
	public Map save() {
		try {
		    String guid = Guid.get();
			map.put("guid", guid);//业务id
			this.getSji().withTableName("${tablename}").execute(map);
			<#if yhfw??&&(yhfw?size>0)>
			//人员选择			
			UserBean userbean = new UserBean();
			userbean.setBmid((String)map.get("bmid"));
			userbean.setGwid((String)map.get("gwid"));
			userbean.setZzid((String)map.get("zzid"));
			userbean.setYhid((String)map.get("yhid"));
			<#list yhfw as t>
	        String ${t.sys_fsfw}= String.valueOf(map.get("${t.sys_fsfw}"));
			if(!"null".equals(${t.sys_fsfw})&&!"".equals(${t.sys_fsfw})){
			Yhfw.save(userbean, guid, "[mk_dm]", String.valueOf(map.get("${t.sys_fsfw}")),"${t.fsfw_dm}",0,"[标题]", 0) ;
			}
			</#list>
			</#if>
			<#if tx??>
			//提醒
			Remind remind =new Remind();
			remind.insert(map,"[提醒主题]","",guid,"[提醒人]",0,0); 
			</#if>
			jjd.setResult(true, "保存成功");
			// 所有增删改操作要写业务日志
			logItem.setMethod("save");
			logItem.setLevel("info");
			logItem.setDesc("[保存成功]");
			Log.write(logItem);
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("[保存数据时出现异常]");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "保存数据时出现错误，错误代码:" + guid);
			throw new ServiceException("保存数据时异常");// 抛出此异常以触发回滚
		}
		return jjd.getData();
	}
	/**
	 * 修改数据
	 * @return
	 */
	public Map update(){
		try{	
			Db.simpleUpdate("${tablename}", "guid=:guid",map);
			<#if yhfw??&&(yhfw?size>0)>
			//人员选择
			Yhfw.deleteYhfw(String.valueOf(map.get("guid")));
			UserBean userbean = new UserBean();
			userbean.setBmid((String)map.get("bmid"));
			userbean.setGwid((String)map.get("gwid"));
			userbean.setZzid((String)map.get("zzid"));
			userbean.setYhid((String)map.get("yhid"));
			<#list yhfw as t>
			String ${t.sys_fsfw}= String.valueOf(map.get("${t.sys_fsfw}"));
			if(!"null".equals(${t.sys_fsfw})&&!"".equals(${t.sys_fsfw})){
			Yhfw.save(userbean, String.valueOf(map.get("guid")), "[mk_dm]", String.valueOf(map.get("${t.sys_fsfw}")),"${t.fsfw_dm}",0,"[标题]", 0) ;
			}
			</#list>
			</#if>
			<#if tx??>
			//提醒
			Remind remind =new Remind();
			remind.delete(String.valueOf(map.get("guid")));
			remind.insert(map,"[提醒主题]","",String.valueOf(map.get("guid")),"[提醒人]",0,0);
			</#if>
			jjd.setResult(true, "修改成功");
			logItem.setMethod("update");
			logItem.setLevel("info");
			logItem.setDesc("[修改成功]");
			Log.write(logItem);
		}catch(Exception e){
			String guid = Guid.get();
			jjd.setResult(false, "修改数据时出现错误，错误代码：" + guid);
			logItem.setMethod("update");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("[修改数据时出现错误！]");
			logItem.setContent(e.toString());
			Log.write(logItem);
			throw new ServiceException("修改数据时异常");//回滚			
		}
  	return 	jjd.getData();		
	}
	</#if>
	/**
	 * 删除数据
	 */
	public Map delete() {
		try {
			String guid = String.valueOf(map.get("checkbox_guid"));
            guid = guid.replace("~", "','");
            this.getJtN().update("update ${tablename} set zt_dm=0 where guid in('"+guid+"')");
			logItem.setMethod("delete");
			logItem.setLogid(Guid.get());
			logItem.setLevel("info");
			logItem.setDesc("[删除数据成功]");
			Log.write(logItem);
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "删除数据时出现异常，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("delete");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("[删除数据时出现异常]");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
	/**
	 * 查询
	 * @return
	 */
	public Map query() {
	    StringBuffer sqlWhere = new StringBuffer();
	    sqlWhere.append(" where 1=1");
	    <#if cxtj??&&(cxtj?size>0)> 
	    <#list cxtj as t>
	    <#if t.cxzd_name='rq'>
	    if(SysPara.compareValue("db_type", "oracle")){
		    sqlWhere.append("/~ and cjsj >= TO_DATE('[cx_rqq]','YYYY-MM-DD HH24:MI:SS')~/");
		    sqlWhere.append("/~ and cjsj <= TO_DATE('[cx_rqz]','YYYY-MM-DD HH24:MI:SS')~/");
	    }else{
		    sqlWhere.append("/~ and cjsj >= {cx_rqq}~/");
		    sqlWhere.append("/~ and cjsj <= {cx_rqz}~/");
	    }
	    <#else>
	      <#if t.cxzd_name='cxfw'>
	    sqlWhere.append(Yhfw.cxfw_sql(yhid, (String)map.get("cxfw"), ""));
	      <#else>
	    sqlWhere.append("/~ and ${t.cxzd_name} = {cx_${t.cxzd_name}}~/");
	       </#if>
	    </#if>
	    </#list>
	    </#if> 
	    sqlWhere.append(" and zt_dm=1");
	    XsqlFilterResult result = new XsqlBuilder().generateHql(sqlWhere.toString(),map); 
		try {
			PageBean page = new PageBean();
			page.setPageGoto((String) (map.get("page_goto")));
			page.setPageSize("10");
			page.setSql("select * from ${tablename}"+ result.getXsql());
			page.setCountSql("select count(*) from ${tablename} "+ result.getXsql());
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("table_list", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("list");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "处理列表数据时出现异常:" + e.toString());
		}
		return jjd.getData();
	}
   /**
    * 显示数据明细
    * @return
    */
	public Map show() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtA();
			Map _map = npjt.queryForMap("select * from ${tablename} where guid=:guid", map);
			<#if yhfw??&&(yhfw?size>0)>
			 //人员选择
			 <#list yhfw as t>
			 _map.put("${t.sys_fsfw}", Yhfw.getYhfw((String)_map.get("guid"), "${t.fsfw_dm}"));
			  jjd.setSelect("${t.sys_fsfw}_lb",Yhfw.list((String)map.get("guid"), ${t.fsfw_dm}));
			 </#list>
			 </#if>
			 <#if attachment??>
			//附件
			 Attachment.getFileinfo(jjd, yhid, (String)_map.get("fjid"), "1");
			 </#if>
			 <#if tx??>
			 //提醒
			 Remind remind = new Remind();
			 remind.show(jjd,_map.get("guid").toString(),0);
			 </#if>
			jjd.setForm(_map);	
		} catch (EmptyResultDataAccessException e) {
			jjd.setResult(false, "没有查找到数据");
		} catch (IncorrectResultSizeDataAccessException e) {
			jjd.setResult(false, "错误：记录不唯一");
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "查找明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("show");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查找明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}
}

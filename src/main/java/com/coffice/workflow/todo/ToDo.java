package com.coffice.workflow.todo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;

public class ToDo extends BaseUtil{
	JspJsonData jjd;
	LogItem logItem;
	String zzid;
	String bmid;
	String gwid;
	String jsid;
	String yhid;
	Map map;
	public ToDo(Map mapIn) {
		this.jjd = new JspJsonData();
		this.logItem = new LogItem();
		this.zzid = ((String) mapIn.get("zzid"));
		this.bmid = ((String) mapIn.get("bmid"));
		this.gwid = ((String) mapIn.get("gwid"));
		this.jsid = ((String) mapIn.get("jsid"));	
		this.yhid = ((String) mapIn.get("yhid"));		
		this.logItem.setYhid(this.yhid);
		this.logItem.setClassName(super.getClass().getName());
		this.map = mapIn;
	}
	
	public Map listDesk(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			PageBean page = new PageBean();
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
			String sfcq = "(case when zbsj IS null  or sc>=12  then '<img src=green.jpg>' when sc<0 then '<img src=red.jpg>' else '<img src=yellow.jpg>' end) imgState";
			sqlStr.append("select b.Entry_ID enid,b.step_id curid,left(formname,25) bt,ldr ldrxm,lxdh,(select mc from t_dm where dm=a.nrlb) nrlb,(select mc from t_dm where dm=a.xxly) xxly,")
				  .append("a.cjsj sdsj from t_form1305 a,t_oswf_work_item b where a.slbh= b.step_id ");
			sqlCount.append("select count(*) from t_form1305 a,t_oswf_work_item b where a.slbh= b.step_id ");
			page.setSql(sqlStr.toString()+" order by cjsj desc");
			page.setCountSql(sqlCount.toString());

			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "桌面查询流程待办时：" + guid);
			this.logItem.setMethod("listDesk");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("桌面查询流程待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map init(){
		try{
			List nrlbList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='4017' order by xh desc ");
			jjd.setSelect("cx_nrfl",nrlbList);
			List xzflList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2005' order by xh desc ");
			jjd.setSelect("cx_xzfl",xzflList);
			List xxlyList = this.getJtN().queryForList("select dm,mc from t_dm where zt_dm = 1 and lb_dm='2006' order by xh desc ");
			jjd.setSelect("cx_xxly",xxlyList);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "初始化已办页面：" + guid);
			this.logItem.setMethod("init");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("初始化已办页面时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map queryCurStep(){
		try{
			String entryId = map.get("entryId")==null?"":String.valueOf(map.get("entryId"));
			if(!"".equals(entryId)){
				List _list = this.getJtN().queryForList("select STEP_ID from OS_CURRENTSTEP where ENTRY_ID="+entryId);
				if(_list.size()>0){
					jjd.setExtend("stepId", ((Map)_list.get(0)).get("STEP_ID"));
				}else{
					jjd.setExtend("stepId","0");
				}
				
			}
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "查询当前步骤时：" + guid);
			this.logItem.setMethod("queryCurStep");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("查询当前步骤时时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map query(){
		try{
				StringBuffer sqlStr = new StringBuffer();
				StringBuffer sqlWhere = new StringBuffer();
				StringBuffer sqlWhere1 = new StringBuffer();
				StringBuffer sqlCount = new StringBuffer();
				String dbtype = SysPara.getValue("db_type");
				String temp = "";
				temp = map.get("cx_ldrqq")==null?"":String.valueOf(map.get("cx_ldrqq"));
				if(!"".equals(temp)){
					if(dbtype.equals("oracle")){
						sqlWhere1.append(" and b.cjsj>=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
						//sqlWhere.append(" and cjsj>=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sqlWhere1.append(" and b.cjsj>='").append(temp).append(" 00:00:00'");
						//sqlWhere.append(" and cjsj>='").append(temp).append(" 00:00:00'");
					}
				}
				temp = map.get("cx_ldrqz")==null?"":String.valueOf(map.get("cx_ldrqz"));
				if(!"".equals(temp)){
					if(dbtype.equals("oracle")){
						sqlWhere1.append(" and b.cjsj<=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
						//sqlWhere.append(" and cjsj<=to_date('").append(temp).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
					}else{
						sqlWhere1.append(" and b.cjsj<='").append(temp).append(" 23:59:59'");
						//sqlWhere.append(" and cjsj<='").append(temp).append(" 23:59:59'");
					}
				}
				temp = map.get("cx_nrfl")==null?"":String.valueOf(map.get("cx_nrfl"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.nrfl = '").append(temp).append("'");
				}
				temp = map.get("cx_xzfl")==null?"":String.valueOf(map.get("cx_xzfl"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.xzfl = '").append(temp).append("'");
				}
				temp = map.get("cx_xxly")==null?"":String.valueOf(map.get("cx_xxly"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.xxly = '").append(temp).append("'");
				}
				temp = map.get("cx_slbh")==null?"":String.valueOf(map.get("cx_slbh"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.slbh_day = '").append(temp).append("'");
				}
				temp = map.get("cx_ldhm")==null?"":String.valueOf(map.get("cx_ldhm"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.lxdh = '").append(temp).append("'");
				}
				temp = map.get("cx_ldhxm")==null?"":String.valueOf(map.get("cx_ldhxm"));
				if(!"".equals(temp)){
					sqlWhere.append(" and a.ldr = '").append(temp).append("'");
				}

				PageBean page = new PageBean();
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
				sqlStr.append("select ldr,lxdh,formid id,workid flowid,curstepid stepid,slbh_day,left(undo_title,25) undo_title,ldrq,")
					  .append("DATEDIFF(mi,ISNULL(bjsj,getDate()),ISNULL(clqx,'2099-01-01 00:00:00')) sfcq,")
					  .append("(select mc from t_dm where dm = xxly )xxly,(select mc from t_dm where dm = xzfl )xzfl,")
					  .append("(select stepname from t_oswf_node_info where stepid=a.curstepid) blhj,")
					  .append("(select mc from t_org_bm where bmid =a.bmid) qx, ")
					  .append("(select xm from t_org_yh where yhid =a.clrid) cbdw,clqx,entryid  from t_cbd_sync ")
					  .append(" a where entryid in (select Entry_ID from t_oswf_work_item_his b where b.value='").append(yhid).append("' ").append(sqlWhere1).append(") ")
					  .append(sqlWhere);
				sqlCount.append("select count(*) ")
				  .append("  from t_cbd_sync a where entryid in (select Entry_ID from t_oswf_work_item_his b where b.value='").append(yhid).append("' ").append(sqlWhere1).append(") ")
				  .append(sqlWhere);
				
				String sortBy = String.valueOf(map.get("sortBy"))!=null?String.valueOf(map.get("sortBy")):"";
				String sortType = String.valueOf(map.get("sortType"))!=null?String.valueOf(map.get("sortType")):"";
				if(!"".equals(sortBy) && !"null".equals(sortBy)){
					sqlStr.append(" order by ").append(sortBy).append(" ");
				}else{
					sqlStr.append(" order by ldrq ");
				}
				if(!"".equals(sortType) && !"null".equals(sortType)){
					sqlStr.append(sortType);
				}else{
					sqlStr.append(" desc ");
				}
				
				page.setSql(sqlStr.toString());
				page.setCountSql(sqlCount.toString());

				page.setNamedParameters(map);
				List _list = Db.getPageData(page);
				jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			this.jjd.setResult(false, "查询待办时：" + guid);
			this.logItem.setMethod("query");
			this.logItem.setLogid(guid);
			this.logItem.setLevel("error");
			this.logItem.setDesc("查询待办时出现异常");
			this.logItem.setContent(e.toString());
			Log.write(this.logItem);
		}
		return jjd.getData();
	}
	public Map showDocumentInfo() {
		String username=yhid;
		String workid=map.get("flowid")==null?"":String.valueOf(map.get("flowid"));
		String wkName="";
		String mk_dm="";
		String stepId="";
		try {
			String businessid=String.valueOf(map.get("id"));
			long entryId=Long.parseLong(String.valueOf(map.get("entryid")));
			stepId = String.valueOf(map.get("stepId"));
			
			/*strSql.delete(0, strSql.length());
			strSql.append(" select a.workid,a.mk_dm,a.step_id from t_oswf_work_item_his where id = '")
				  .append(businessid).append("' ");
			List<Map> list1=this.getJtN().queryForList(strSql.toString());
			for(Map map:list1){
				workid=String.valueOf(map.get("workid"));
				stepid = String.valueOf(map.get("step_id"));
				mk_dm=String.valueOf(map.get("mk_dm"));
				dburl=String.valueOf(map.get("dburl"));//url(iframe)
			}*/
			Workflow wf = new BasicWorkflow(username);
			wkName=wf.getWorkflowName(entryId);
			WorkFlowMethod wfMethod=new WorkFlowMethod();
		    String templateName="t_form1305.ftl";//wfMethod.findTemplateById(workid);
		    //表单id
		    String formId=wfMethod.findFormIdByBusiness((int)entryId);
		    //功能列表
		    map.put("id", entryId);
		    map.put("templateName", templateName);//模板名称
		    map.put("formId", formId);//表单id
		    map.put("stepId", stepId);
		    map.put("wkName", wkName);
			map.put("mk_dm", mk_dm);//模块代码
			map.put("businessid", businessid);//业务id
			List _list = this.getJtN().queryForList("select * from t_msg where ywid='"+String.valueOf(entryId)+"'");
			if(_list.size()>0){
				map.put("ifLy","1");
				map.put("Lys", _list.size());
			}
			map.putAll(getFiles(entryId));
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("showDocumentInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示待阅时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}
	public Map getFiles(long id){
		Map _map = new HashMap();
		String zid = String.valueOf(id);
		try{
			String mk_mc = "form";			
			if(!"".equals(zid)){
				String mk_dir = SysPara.getValue(mk_mc+"_dir");
				jjd.setExtend("mk_dir", mk_dir);
				String fjStr = "[";
				List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+zid+"'");
				for(int i=0;i<fjList.size();i++){
					Map fjMap = (Map)fjList.get(i);
					fjStr = fjStr+"{wjmc:'"+fjMap.get("wjmc")+"',wjlj:'"+fjMap.get("wjlj")+"'},";
				}
				fjStr = fjStr.substring(0,fjStr.length()-1);
				fjStr +="]";
				_map.put("fjStr",fjStr);
				if(fjList.size()>0){
					_map.put("ifFj","1");
					_map.put("mk_dir",SysPara.getValue("form_dir"));
				}else{
					_map.put("ifFj","0");
				}
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"查询附件信息");
		}
			return _map;
		}
}

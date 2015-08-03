package com.coffice.sms;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;

public class Sms extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Sms(Map mapIn) {
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
	/**
	 * 转办查询
	 * 
	 * @return
	 */
	public Map queryDx() {
		try {
			NamedParameterJdbcTemplate npjt = this.getNpjtN();
			StringBuffer sqlStr=new StringBuffer();
			StringBuffer sqlWhere=new StringBuffer();
			StringBuffer sqlCount=new StringBuffer();		 
			String tmp="";
			tmp = String.valueOf(map.get("cjsjq"))!=null?String.valueOf(map.get("cjsjq")):"";
			if(!"".equals(tmp)&& tmp!=null & !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(create_time,'YYYY-MM-DD') >='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and create_time >='").append(tmp).append("'");
				}
			}
			tmp = String.valueOf(map.get("cjsjz"))!=null?String.valueOf(map.get("cjsjz")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				if("oracle".equals(SysPara.getDbType())){
					sqlWhere.append(" and to_char(create_time,'YYYY-MM-DD') <='").append(tmp).append("'");
				}else{
					sqlWhere.append(" and create_time<='").append(tmp).append(" 23:59:59'");
				}
			}
			tmp = String.valueOf(map.get("cxhm"))!=null?String.valueOf(map.get("cxhm")):"";
			if(!"".equals(tmp) && tmp!=null && !"null".equals(tmp)){
				sqlWhere.append(" and src_terminal_id like '%").append(tmp).append("%'");
			}
			sqlStr.append("select (case when state = 1 then '已回复办结' when state = 2 then '已转办' else '未处理' end )state,id,src_terminal_id ldhm,left(msg_content,25) content,SUBSTRING(convert(varchar(50),create_time,120),6,11) create_time,create_time create_time1  ")
				  .append(" from sms..t_dx_cmpp_deliver where 1=1 ").append(sqlWhere);
			sqlCount.append("select count(*) from sms..t_dx_cmpp_deliver a where 1=1 ").append(sqlWhere);
			
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
			page.setCountSql(sqlCount.toString());
			page.setSql(sqlStr.toString()+" order by create_time1 desc");
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
  			jjd.setGrid("datalist", _list, page);
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("queryDx");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询短信列表");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "sms.queryDx:" + e.toString());
		}
		return jjd.getData();
	}
	
	/**
	 * 删除转办
	 * 
	 * @return
	 */
	public Map replayDx() {
		try {
			String ldhm = String.valueOf(map.get("dxhm"))!=null?String.valueOf(map.get("dxhm")):"";
			String id = String.valueOf(map.get("id"))!=null?String.valueOf(map.get("id")):"";
			if(!"".equals(ldhm)){
				String content = (map.get("replay_content")==null?"":map.get("replay_content").toString());
				int taskId=new SysData().getNextId("sms_taskid");
				String dx_moi_sp="106573002345";
				String sqlOrder="INSERT INTO sms..T_DX_CMPP_ORDER(TASKID,SERVICE_ID,SP_NUMBER,MOBILES,CONTENT,STATE,Registered_Delivery,Msg_level,Fee_terminal_Id,DestUsr_tl,LINKID,SUBMIT_TIME,CREATE_TIME) VALUES('"+taskId+"','0000','"+dx_moi_sp+"','"+ldhm+"','"+content+"',0,1,1,'',1,0,GETDATE(),GETDATE())";
				this.getJtN().update(sqlOrder);
				this.getJtN().update("update sms..t_dx_cmpp_deliver set state =1 where id = "+id);
			}else{
				
			}
		} catch (Exception e) {
			logItem.setMethod("replayDx");
			logItem.setLevel("error");
			logItem.setDesc("回复短信时");
			logItem.setContent(e.toString());
			logItem.setYhid(yhid);
			Log.write(logItem);
			jjd.setResult(false, "回复短信时发生异常");
		}
		return jjd.getData();
	}
	/*
	 * 显示明细
	 * 
	 */
	public Map show() {
		try {
			String sql="select id,msg_content dxnr,src_terminal_id dxhm from sms..t_dx_cmpp_deliver where id ="+map.get("guid");
			Map _map = this.getNpjtN().queryForMap(sql, map);	
			String dxnr = String.valueOf(_map.get("dxnr"));
			String dxnr_new = "";
			String regx = "[^\u4e00-\u9fa5|a-z|A-Z|0-9|\\p{Punct}|，|；|？|》|《|%|！|。|“|”]?";
			_map.put("dxnr",dxnr.replaceAll(regx,""));
			//System.out.println(_map.get("dxnr").toString().replace('\u00A3',""));
			jjd.setResult(true,"查询成功");
			jjd.setForm(_map);
		} catch (Exception e) {
			String guid = Guid.get();
			jjd.setResult(false, "显示明细数据时出现错误，错误代码：" + guid);// 向最终用户提供日志编号，不提供异常明细
			logItem.setMethod("SMS_show");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示明细数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "SMS_show:" + e.toString());

		}
		return jjd.getData();
	}
	public Map saveFwqq(){
		try{
			StringBuffer sqlStr = new StringBuffer();
			int yxbz = 1;
			int ifEnd = 1;
			if(((String)map.get("end")).equals("0")){
				ifEnd = 0;
			}
			String guid = Guid.get();
			String lxhm = map.get("lxhm")==null?"":String.valueOf(map.get("lxhm"));
			String nr = map.get("nr")==null?"":String.valueOf(map.get("nr"));
			String id = map.get("id")==null?"":String.valueOf(map.get("id"));
			String ldrdz = "";
			sqlStr.append("insert into t_hjzx_fwqq(zzid,bmid,gwid,yhid,guid,ldrxm,ldhm,lxhm,nr,cblb_dm,xxly,cjsj,yxbz,ifEnd)values('")
			      .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','").append(yhid)
			      .append("','").append(guid).append("','短信联系人','").append(lxhm).append("','").append(lxhm).append("','")
			      .append(nr).append("','94063','200603',getDate(),0,0)");
			this.getJtN().update(sqlStr.toString());
			this.getJtN().update("update sms..t_dx_cmpp_deliver set state =2 where id = "+id);
			jjd.setExtend("fwqqid",guid);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveFwqq");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存服务请求数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "sms.saveFwqq异常:" + e.toString());
		}
		return jjd.getData();
	}
}

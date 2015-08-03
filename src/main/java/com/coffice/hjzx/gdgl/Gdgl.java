package com.coffice.hjzx.gdgl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coffice.form.use.FormUse;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.Md5;
import com.coffice.workflow.sync.Sync;
import com.coffice.workflow.use.WKUse;
import com.coffice.workflow.util.WorkFlowMethod;

public class Gdgl extends BaseUtil {
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;// 组织ID
	String bmid;// 部门ID
	String gwid;// 岗位ID
	String yhid;// 用户ID
	Map map;

	public Gdgl(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		zzid = (String) mapIn.get("zzid");
		bmid = (String) mapIn.get("bmid");
		gwid = (String) mapIn.get("gwid");
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}

	/**
	 * 创建工单
	 * 
	 * @return
	 */
	public synchronized Map createGd(WKUse app) {
		String workid = "";
		String entryid = "";
		String ywid = "";
		String mRecordID = "";// 公文id
		StringBuffer strSql = new StringBuffer();
		String fwqqid = (String) map.get("fwqqid");// 服务请求id
		Map fwqqMap=this.getJtN().queryForMap("select ldrxm,xxly,ldrdz,sfbm,xzfl,bt,lxhm,xzfl,nr,lypath,lb_dm from t_hjzx_fwqq where guid='"+fwqqid+"'");
		System.out.println("fwqqMap:"+fwqqMap);
		String xm =  fwqqMap.get("ldrxm")==null?"":String.valueOf(fwqqMap.get("ldrxm"));// 来电姓名
		String lxhm = fwqqMap.get("lxhm")==null?"":String.valueOf(fwqqMap.get("lxhm"));// 来电号码
		String lypath = fwqqMap.get("lypath")==null?"":String.valueOf(fwqqMap.get("lypath"));;// 来电号码
		String clr = (String) fwqqMap.get("clr")==null?"":String.valueOf(fwqqMap.get("clr"));// 处理人
		String bt = (String) fwqqMap.get("bt")==null?"":String.valueOf(fwqqMap.get("bt"));// 处理人
		String lb_dm = fwqqMap.get("lb_dm")==null?"":String.valueOf(fwqqMap.get("lb_dm"));// 类别代码
		String dz = (String) fwqqMap.get("ldrdz")==null?"":String.valueOf(fwqqMap.get("ldrdz"));// 联系地址
		String sfbm = fwqqMap.get("sfbm")==null?"":String.valueOf(fwqqMap.get("sfbm"));
		String xzfl = fwqqMap.get("xzfl")==null?"":String.valueOf(fwqqMap.get("xzfl"));// 服务请求id
		String xxly = fwqqMap.get("xxly")==null?"":String.valueOf(fwqqMap.get("xxly"));
		String docid = "";
		String wkname = "";
		String id = "";
		String thnr = fwqqMap.get("nr").toString();// 内容
		try {
			docid = "0";
			wkname="1343118002906admin";
			String formid = Guid.get();
			Map amap = new HashMap();
			amap.put("wfT", wkname);
			amap.put("lclb_dm", docid);
			amap.put("mk_dm", "8808");
			amap.put("mkurllb", "1");
			amap.put("undo_title", bt);
			amap.put("formid", formid);
			app.setMap(amap);
			ywid = Guid.get();
			app.setYwid(ywid);
			// 创建流程(接口)
			
			map = app.createWfInstance();
			workid = String.valueOf(map.get("workid"));
			entryid = String.valueOf(map.get("entryid"));
			//---------这是什么意思，t_oswf_work_entry_md5 表有什么用 起
			/*mRecordID = Md5.getMd5(String.valueOf(entryid));
			strSql.delete(0, strSql.length());
			strSql.append("insert into t_oswf_entry_md5(id,workid,entryid,entryidmd5) ");
			strSql.append("values('").append(Guid.get()).append("','").append(
					workid).append("','").append(entryid).append("','").append(
					mRecordID).append("')");
			this.getJtN().update(strSql.toString());*/

			StringBuffer slbh_day= new StringBuffer();
			if("200601".equals(xxly)){
				slbh_day.append("DH");
			}else if("200603".equals(xxly)){
				slbh_day.append("DX");
			}else if("200606".equals(xxly)){
				slbh_day.append("WL");
			}else if("200605".equals(xxly)){
				slbh_day.append("XX");
			}
			if("200501".equals(xzfl)){
				slbh_day.append("ZX");
			}else if("200502".equals(xzfl)){
				slbh_day.append("QZ");
			}else if("200503".equals(xzfl)){
				slbh_day.append("JY");
			}else if("200504".equals(xzfl)){
				slbh_day.append("TS");
			}else if("200506".equals(xzfl)){
				slbh_day.append("WX");
			}else if("200507".equals(xzfl)){
				slbh_day.append("QT");
			}
			slbh_day.append(formid.substring(2,8));
			if(entryid.length()==1){
				slbh_day.append("00000").append(entryid);
			}else if(entryid.length()==2){
				slbh_day.append("0000").append(entryid);
			}else if(entryid.length()==3){
				slbh_day.append("000").append(entryid);
			}else if(entryid.length()==4){
				slbh_day.append("00").append(entryid);
			}else if(entryid.length()==5){
				slbh_day.append("0").append(entryid);
			}else if(entryid.length()==6){
				slbh_day.append(entryid);
			}
			WorkFlowMethod wfMethod = new WorkFlowMethod();
			//String tablename = wfMethod.findTableNameById(workid);
			strSql.delete(0, strSql.length());
			strSql.append("insert into t_form1305 ")
					.append(" (slbh_day,ldrq,xxly,sfbm,xzfl,ldr,lxdh,lxdz,nrfl,fywt,slzx,id,slbh,undo_title,lypath) ")
			        .append("values('"+slbh_day.toString()+"','"+DateConvertStr()+"','").append(xxly).append("','").append(sfbm)
			        .append("','").append(xzfl).append("','").append(xm).append("','").append(lxhm).append("','")
			        .append(dz).append("','").append(lb_dm).append("','").append(thnr).append("','")
			        .append(clr).append("','").append(formid).append("','").append(entryid).append("','")
			        .append(bt).append("','").append(lypath).append("')");							
			int i = this.getJtN().update(strSql.toString());
			//app.checkBusinesForm(entryid, "3", formid, "1");
			
			wfMethod.saveBusinessForm(entryid, "3",formid,"1");
			//将t_hjzx_fwqq表置为有效
			this.getJtN().update("update t_hjzx_fwqq set yxbz=1,tablename='t_form1305',formid='"+formid+"',entryid='"+entryid+"' where guid='"+fwqqid+"'");
			
			strSql.delete(0, strSql.length());
			strSql.append("select max(id) id from t_oswf_work_item where entry_id=?");
			List<Map> list1 = this.getJtN().queryForList(strSql.toString(),new Object[] { entryid });
			for (Map map1 : list1) {
				id = (String) map1.get("id");
			} 
			FormUse doc = new FormUse(map);
			app.setMap("id", id);
			map = doc.showDocumentItem(app);
			map.put("dbFlag", "1"); 
			map.put("info", "1");
			map.put("workid",workid);  
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("Gdgl.createGd");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("用户：" + yhid + "创建工单时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			e.printStackTrace();
		}
		return map;
	}
	public Map getFormnameWithTx() {
		String entryid = map.get("entryid").toString();
		Map formnameMap = this.getJtN().queryForMap(
				"select slbh_day,undo_title from t_form1305 where id=(select  top 1 formId from t_oswf_busi_form_relation where businessId='"+entryid+"')");
		jjd.setExtend("title", formnameMap.get("undo_title"));
		jjd.setExtend("slbh_day", formnameMap.get("slbh_day"));
		return jjd.getData();
	}
		
}

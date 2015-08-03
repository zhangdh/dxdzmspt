package com.coffice.attachment;

import java.io.File;
import java.util.List;
import java.util.Map;
import com.coffice.util.BaseUtil;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.cache.Cache;



public class UploadFile extends BaseUtil {
	LogItem logItem;// 日志项

	String yhid;
	String gwid;
	String bmid;
	String zzid;
	JspJsonData jjd;
	Map _map ;

	public UploadFile(String yhid) {
		this.yhid = yhid;
		this.gwid = "";
		this.bmid = (String) Cache.getUserInfo(yhid, "zzid");
		this.zzid = (String) Cache.getUserInfo(yhid, "zzid");
		logItem = new LogItem();
	}

	public void vali(String dir){
		File dirname = new File(dir); 
		if (!dirname.isDirectory()) 
		{ //目录不存在 
		     dirname.mkdir(); //创建目录
		} 
	}
	public UploadFile(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this._map = mapIn;
	}
	public int getFileSize(String filepath){
		 int sFlag = 0;
		    try {
		      File f = new File(filepath);
		      if (f.exists()) {
		        sFlag = (int)f.length();
		      }else{
		    	sFlag=0;
		      }
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		    return sFlag;
	}
	
	public void saveFjInfo(String guidFJ,String mk_dm,String fjId,String wjmc,String wjDir,int wjSize){
		try{
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("insert into t_attachment (zzid,bmid,gwid,yhid,guid,mk_dm,zid,wjmc,wjlj,wjkjxs,wjkj,scsj)values('")
				  .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','")
				  .append(yhid).append("','").append(guidFJ).append("','").append(mk_dm).append("','")
				  .append(fjId).append("','").append(wjmc).append("','").append(wjDir).append("','")
				  .append(wjSize).append("','").append(wjSize).append("',").append(getDateStr()).append(")");
			this.getJtN().update(sqlStr.toString());
			
		}catch(Exception e){
			logItem.setMethod("saveFjInfo");
			logItem.setLevel("error");
			logItem.setDesc("保存附件明细时数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	public Map delFj(){
	try{
		String fj_id = _map.get("wjlj")==null?"":String.valueOf(_map.get("wjlj"));
		if(!"".equals(fj_id)){
			this.getJtN().update("delete from t_attachment where wjlj = '"+fj_id.trim()+"'");
		}
	}catch(Exception e){
		logItem.setContent(e.toString());
		Log.write(logItem);
		jjd.setResult(false,"删除附件异常");
	}
		return jjd.getData();
	}
	public Map show(){
		try{
			String wjlj = _map.get("wjlj")==null?"":String.valueOf(_map.get("wjlj"));
			if(!"".equals(wjlj)){
				this.getJtN().update("delete from t_attachment where wjlj = '"+wjlj+"'");
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"删除附件异常");
		}
			return jjd.getData();
		}
	public Map save(){
		try{
			String fj_id = _map.get("wjlj")==null?"":String.valueOf(_map.get("wjlj"));
			if(!"".equals(fj_id)){
				this.getJtN().update("delete from t_attachment where wjlj = '"+fj_id+"'");
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"删除附件异常");
		}
			return jjd.getData();
		}
	public Map send(){
		try{
			String fj_id = _map.get("wjlj")==null?"":String.valueOf(_map.get("wjlj"));
			if(!"".equals(fj_id)){
				this.getJtN().update("delete from t_attachment where wjlj = '"+fj_id+"'");
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"删除附件异常");
		}
			return jjd.getData();
		}
	public Map getFiles(){
		try{
			String zid = _map.get("zid")==null?"":String.valueOf(_map.get("zid"));
			String mk_mc = _map.get("mk_mc")==null?"":String.valueOf(_map.get("mk_mc"));
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
				jjd.setExtend("fjStr",fjStr);
				if(fjList.size()>0){
					jjd.setExtend("ifFj","1");
				}else{
					jjd.setExtend("ifFj","0");
				}
			}
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"查询附件信息");
		}
			return jjd.getData();
		}
	public Map getFileList(){
		try{
			String zid = _map.get("zid")==null?"":String.valueOf(_map.get("zid"));
			List _list = this.getJtN().queryForList("select wjmc mc,wjlj dm from t_attachment where zid='"+zid+"'");
			jjd.setSelect("attachments", _list);
		}catch(Exception e){
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false,"得到附件列表时异常");
		}
			return jjd.getData();
		}
}

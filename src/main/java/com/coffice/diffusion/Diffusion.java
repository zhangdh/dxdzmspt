package com.coffice.diffusion;
import java.util.HashMap;
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

public class Diffusion extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;
	String zzid;
	String gwid;
	String bmid;

	Map map;

	public Diffusion(Map mapIn) {
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		zzid = (String) mapIn.get("zzid");
		gwid = (String) mapIn.get("gwid");
		bmid = (String) mapIn.get("bmid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;

	}
	public Diffusion() {
		
	}
	public Map manageConfig(){
		try{
			PageBean page = new PageBean();
			page.setSql(new StringBuffer().append("select guid id,glcdmc,ckcdmc,lb_dm,")
										  .append("(case when plbz ='1' then '允许评论' when plbz ='0' then '不允许评论' end) plqk,")
										  .append("(case when fbfwbz ='1' then '全体' when fbfwbz ='0' then '自定义' end) fbfw ")
										  .append(" from t_publish_lb where zt_dm = 1 ").toString());
			page.setCountSql("select count(*) from t_publish_lb where zt_dm = 1");
			if(map.get("query_page")!=null){
				page.setPageGoto((String) (map.get("query_page")));
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize((String) (map.get("page_num")));
			}else{
				page.setPageGoto("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("manageConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("处理列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.manageConfig异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map saveManager(){
		try{
			if(map.get("id")!=null){
				String id = map.get("id").toString();
				if(!"".equals(id)){
					String plbz  = map.get("xxfb_yxpl")==null?"":String.valueOf(map.get("xxfb_yxpl"));
					String fbfwbz  = map.get("xxfb_fbfw")==null?"":String.valueOf(map.get("xxfb_fbfw"));
					String glcdmc = map.get("glcdmc")==null?"":String.valueOf(map.get("glcdmc"));
					String ckcdmc = map.get("ckcdmc")==null?"":String.valueOf(map.get("ckcdmc"));
					this.getJtN().update(new StringBuffer().append("update t_publish_lb set plbz = '")
															.append(plbz).append("',fbfwbz='").append(fbfwbz)
															.append("',glcdmc='").append(glcdmc).append("',")
															.append("ckcdmc='").append(ckcdmc).append("' ")
															.append(" where guid = '").append(id).append("'")
															.toString());
				}
			}else{
				String dbtype = SysPara.getValue("db_type");
				map.put("guid", Guid.get());
				map.put("yxts", 30);
				//由于之前的信息类别去掉了，而t_dm2表中的mc又不能为空字符串，所以将信息类别手动放入一个空格处理
				map.put("lb_dm"," ");
				int lb_dm = Db.getNextId();
				map.put("lb_dm", lb_dm);//类别代码
				map.put("zmbz", "0");//是否在桌面显示
				map.put("plbz", map.get("xxfb_yxpl").toString());//是否允许评论
				map.put("fbfwbz", map.get("xxfb_fbfw").toString());//发布范围
				map.put("fjbz","1");//是否允许上传附件

				//如果允许上传缩略图,那么则向kz_dm字段里面添加8300,表示包含缩略图
				map.put("kz_dm","0");//是否允许上传缩略图
				map.put("yxqbz","1");//有效期设置
				map.put("zt_dm", 1);//有效设置
				this.getSji().withTableName("t_publish_lb").execute(map);
				NamedParameterJdbcTemplate npjt = this.getNpjtA();
				if (dbtype.equals("oracle")) {
					npjt.getJdbcOperations().update("insert into t_dm2 (dm,lb_dm,mc,sm,zt_dm,xh,cjsj,sjdm) values ("+lb_dm+",81,'"+map.get("lb_dm").toString()+"','"+map.get("lb_dm").toString()+"',1,0,sysdate,0);");
				} else if (dbtype.equals("mysql")){
					npjt.getJdbcOperations().update("insert into t_dm2 (dm,lb_dm,mc,sm,zt_dm,xh,cjsj,sjdm) values ("+lb_dm+",81,'"+map.get("lb_dm").toString()+"','"+map.get("lb_dm").toString()+"',1,0,now(),0);");
				} else if (dbtype.equals("sqlserver")){
					npjt.getJdbcOperations().update("insert into t_dm2 (dm,lb_dm,mc,sm,zt_dm,xh,cjsj,sjdm) values ("+lb_dm+",81,'"+map.get("lb_dm").toString()+"','"+map.get("lb_dm").toString()+"',1,0,getdate(),0);");
				}
				//向权限表中插入
				int qxid_gl = npjt.getJdbcOperations().queryForInt("select max(qxid) from t_qx_mx where mk_dm='105'");
				qxid_gl=qxid_gl+1;
				npjt.getJdbcOperations().update("insert into t_qx_mx (qxid,mk_dm,qxlx,mc,sjid,url,id,method,xh,zt_dm) values ("+qxid_gl+",105,'1','"+map.get("glcdmc").toString()+"','10500','/diffusion/MList.jsp?lb_dm="+lb_dm+"',null,null,null,1);");
				int qxid_ck = npjt.getJdbcOperations().queryForInt("select max(qxid) from t_qx_mx where mk_dm='105'");
				qxid_ck=qxid_ck+1;
				//插入管理页面
				npjt.getJdbcOperations().update("insert into t_qx_mx (qxid,mk_dm,qxlx,mc,sjid,url,id,method,xh,zt_dm) values ("+qxid_ck+",105,'1','"+map.get("ckcdmc").toString()+"','10500','/diffusion/List.jsp?lb_dm="+lb_dm+"',null,null,null,1);");
				
				//向表t_todo_fb中插入一条数据
				npjt.getJdbcOperations().update("insert into t_todo_fb (mk_dm,mkurllb,dburl,sfxs,dkfs,bz) values (105,"+lb_dm+",'/diffusion/diffusionMx.jsp?lb_dm="+lb_dm+"&guid=',1,1,'信息发布')");	
				jjd.setResult(true, "保存成功");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("saveManager");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.manageConfig异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map delManager(){
		try{
			if(map.get("id")!=null && map.get("lb_dm")!=null){
				String delId = map.get("id").toString();
				String lb_dm = map.get("lb_dm").toString();
				//逻辑删除配置列表
				this.getJtN().execute("update t_publish_lb set zt_dm='0' where guid in("+delId+")");
				//逻辑删除与删除类别相关的信息内容
				this.getJtN().execute("update t_publish_mx set zt_dm='0' where lb_dm='"+lb_dm+"'");
				//逻辑删除代码表对应的类别
				this.getJtN().execute("update t_dm2 set zt_dm='0' where dm ='"+lb_dm+"'");
				//逻辑删除权限表类别
				this.getJtN().execute("update t_qx_mx set zt_dm='0' where url='/diffusion/MList.jsp?lb_dm="+lb_dm+"' or url='/diffusion/List.jsp?lb_dm="+lb_dm+"'");
				
			}else{
				jjd.setResult(false,"删除有误,id或类别代码为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("delManager");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.delManager异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map showManager(){
		try{
			if(map.get("id")!=null){
				String id = map.get("id").toString();
				Map _map = this.getJtN().queryForMap("select guid id,lb_dm,glcdmc,ckcdmc,fbfwbz xxfb_fbfw,plbz xxfb_yxpl from t_publish_lb where guid ='"+id+"'");
				jjd.setForm(_map);
			}else{
				jjd.setResult(false,"id为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showManager");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.showManager异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map queryLb(){
		try{
			List _list = this.getJtN().queryForList("select mc ,dm  from t_dm where lb_dm ='10501' and zt_dm  = 1");
			Map _map = new HashMap();
			_map.put("mc","--信息类别--");
			_map.put("dm","");
			_list.add(0, _map);
			jjd.setSelect("gglb",_list);
			jjd.setSelect("lb_dm",_list);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("queryLb");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.queryLb异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map send(){
		try{
			String db_type = SysPara.getDbType(); 
			StringBuffer sqlStr = new StringBuffer();
			String guid = Guid.get();
			String fbfwbz = map.get("fsfwbz")==null?"0":String.valueOf(map.get("fsfwbz"));
			String yxsjbz = map.get("yxsjbz")==null?"0":String.valueOf(map.get("yxsjbz"));
			sqlStr.append("insert into t_publish_mx(zzid,bmid,gwid,yhid,guid,lb_dm,lb2_dm,zt,nr,fjid,cjsj,yxqq,yxqz,plbz,zt_dm,fbfwbz)values('")
				  .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','").append(yhid).append("','")
				  .append(guid).append("','").append((String)map.get("gglb")).append("','0','").append((String)map.get("xxzt"))
				  .append("','").append((String)map.get("nr")).append("','").append((String)map.get("fjid"))
				  .append("',").append(getDateStr()).append(",");
			if("oracle".equals(db_type)){
				sqlStr.append("to_date('").append((String)map.get("yxqq")).append("','yyyy-mm-dd hh24:mi:ss'),")
					  .append("to_date('").append((String)map.get("yxqz")).append("','yyyy-mm-dd hh24:mi:ss'),");
			}else{
				sqlStr.append("'").append((String)map.get("yxqq")).append("',")
				      .append("'").append((String)map.get("yxqz")).append("',");
			}
			sqlStr.append("'1','8002','").append(fbfwbz).append("')");
			this.getJtN().update(sqlStr.toString());
			if(!"1".equals(fbfwbz)){
				String fbfw_value = map.get("fbfw_value")==null?"":String.valueOf( map.get("fbfw_value"));
				if(!"".equals(fbfw_value)){
					String[] fbfw_ry = fbfw_value.split(",");
					for(int i=0;i<fbfw_ry.length;i++){
						this.getJtN().update("insert into t_diffusion_yhfw values('"+Guid.get()+"','"+guid+"','"+fbfw_ry[i]+"',0,"+getDateStr()+",1)");
					}
				}
			}
			
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("send");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.send异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map save(){
		try{
			String db_type = SysPara.getDbType(); 
			StringBuffer sqlStr = new StringBuffer();
			String guid = Guid.get();
			String fbfwbz = map.get("fsfwbz")==null?"0":String.valueOf(map.get("fsfwbz"));
			String yxsjbz = map.get("yxsjbz")==null?"0":String.valueOf(map.get("yxsjbz"));
			sqlStr.append("insert into t_publish_mx(zzid,bmid,gwid,yhid,guid,lb_dm,lb2_dm,zt,nr,fjid,cjsj,yxqq,yxqz,plbz,zt_dm,fbfwbz)values('")
				  .append(zzid).append("','").append(bmid).append("','").append(gwid).append("','").append(yhid).append("','")
				  .append(guid).append("','").append((String)map.get("gglb")).append("','0','").append((String)map.get("xxzt"))
				  .append("','").append((String)map.get("nr")).append("','").append((String)map.get("fjid"))
				  .append("',").append(getDateStr()).append(",");
			if("oracle".equals(db_type)){
				sqlStr.append("to_date('").append((String)map.get("yxqq")).append("','yyyy-mm-dd hh24:mi:ss'),")
					  .append("to_date('").append((String)map.get("yxqz")).append("','yyyy-mm-dd hh24:mi:ss'),");
			}else{
				sqlStr.append("'").append((String)map.get("yxqq")).append("',")
				      .append("'").append((String)map.get("yxqz")).append("',");
			}
			sqlStr.append("'1','8001','").append(fbfwbz).append("')");
			this.getJtN().update(sqlStr.toString());
			if(!"1".equals(fbfwbz)){
				String fbfw_value = map.get("fbfw_value")==null?"":String.valueOf( map.get("fbfw_value"));
				if(!"".equals(fbfw_value)){
					String[] fbfw_ry = fbfw_value.split(",");
					for(int i=0;i<fbfw_ry.length;i++){
						this.getJtN().update("insert into t_diffusion_yhfw values('"+Guid.get()+"','"+guid+"','"+fbfw_ry[i]+"',0,"+getDateStr()+",1)");
					}
				}
			}			
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.save异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map del(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("delete from t_publish_mx where guid ='"+guid+"'");
				this.getJtN().update("delete from t_diffusion_yhfw where ywid ='"+guid+"'");
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("del");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.dels异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map sendDiff(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("update  t_publish_mx set zt_dm = '8002' where guid ='"+guid+"'");
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("sendDiff");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.sendDiff异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map show(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				Map _map = this.getJtN().queryForMap("select guid ,lb_dm gglb,zt xxzt,nr xxnr,a.* from t_publish_mx a where guid = '"+guid+"'");
				jjd.setExtend("fbfwbz",_map.get("fbfwbz")==null?"":String.valueOf(_map.get("fbfwbz")));
				jjd.setExtend("xxnr",_map.get("xxnr")==null?"":String.valueOf(_map.get("xxnr")));
				jjd.setExtend("xxzt", _map.get("zt_dm")==null?"":String.valueOf(_map.get("zt_dm")));
				
				List _list = this.getJtN().queryForList("select fsfw_ry dm,(select xm from t_org_yh where yhid=a.fsfw_ry) mc from t_diffusion_yhfw a where ywid ='"+guid+"'");
				jjd.setSelect("fbfw", _list);
				String fjId = _map.get("fjid")==null?"":String.valueOf(_map.get("fjid"));
				jjd.setExtend("ifFj","0");
				if(!"".equals(fjId)){
					String diffusion_dir = SysPara.getValue("diffusion_dir");
					jjd.setExtend("diffusion_dir", diffusion_dir);
					String fjStr = "[";
					List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+_map.get("fjid").toString()+"'");
					for(int i=0;i<fjList.size();i++){
						Map fjMap = (Map)fjList.get(i);
						fjStr = fjStr+"{wjmc:'"+fjMap.get("wjmc")+"',wjlj:'"+fjMap.get("wjlj")+"'},";
					}
					fjStr = fjStr.substring(0,fjStr.length()-1);
					fjStr +="]";
					jjd.setExtend("fjStr",fjStr);
					jjd.setExtend("ifFj","1");
				}
				jjd.setForm(_map);
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("show");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.show异常:" + e.toString());
		}
		return jjd.getData();
	}
	
	public Map queryFs(){
		try{
			StringBuffer sqlWhere = new StringBuffer();
			String db_type = SysPara.getDbType();
			String temp ="";
			temp = map.get("cx_gq")==null?"":String.valueOf(map.get("cx_gq"));
			if(temp != null  && !"".equals(temp)){
				if("0".equals(temp)){
					sqlWhere.append(" and yxqz <").append(getDateStr());
				}else if("1".equals(temp)){
					sqlWhere.append(" and (yxqz >=").append(getDateStr()).append(" or yxqz is NULL) ");
				}else if("2".equals(temp)){
					sqlWhere.append(" and yxqz is NULL");
				}
			}
			temp = map.get("cx_xxzt")==null?"":String.valueOf(map.get("cx_xxzt"));
			if(temp != null  && !"".equals(temp)){
				sqlWhere.append(" and zt like '%").append(temp).append("%'");
			}
			temp = map.get("lb_dm")==null?"":String.valueOf(map.get("lb_dm"));
			if(temp != null  && !"".equals(temp)){
				sqlWhere.append(" and lb_dm = '").append(temp).append("'");
			}
			temp = map.get("cx_rq1")==null?"":String.valueOf(map.get("cx_rq1"));
			if(temp != null  && !"".equals(temp)){
				if("oracle".equals(db_type)){
					sqlWhere.append(" and cjsj > to_date('").append(temp).append("','yyyy-mm-dd')");
				}else{
					sqlWhere.append(" and cjsj > '").append(temp).append("'");
				}
			}
			temp = map.get("cx_rq2")==null?"":String.valueOf(map.get("cx_rq2"));
			if(temp != null  && !"".equals(temp)){
				if("oracle".equals(db_type)){
					sqlWhere.append(" and cjsj < to_date('").append(temp).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				}else{
					sqlWhere.append(" and cjsj < '").append(temp).append(" 23:59:59'");
				}
			}
			PageBean page = new PageBean();
			page.setSql(new StringBuffer().append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(case when zt_dm='8001' then '暂时保存' when zt_dm='8002' then '已发送' end)xxzt,SUBSTRING(convert(varchar(100),cjsj,120) ,6,11) cjsj,cjsj cjsj1,yxqq,yxqz from t_publish_mx a ")
										  .append(" where yhid='").append(yhid).append("'").append(sqlWhere).append(" order by cjsj1 desc ").toString());
			
			page.setCountSql((new StringBuffer().append("select count(*) from t_publish_mx ")
					                            .append(" where yhid='").append(yhid).append("'").append(sqlWhere).toString()));
			if(map.get("query_page")!=null){
				page.setPageGoto(map.get("query_page").toString());
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("queryFs");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.queryFs异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map queryJs(){
		try{
			StringBuffer sqlWhere = new StringBuffer();
			StringBuffer sqlStr= new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			String db_type = SysPara.getDbType();
			String temp ="";
			temp = map.get("cx_gq")==null?"":String.valueOf(map.get("cx_gq"));
			if(temp != null  && !"".equals(temp)){
				if("0".equals(temp)){
					sqlWhere.append(" and yxqz <").append(getDateStr());
				}else if("1".equals(temp)){
					sqlWhere.append(" and (yxqz >=").append(getDateStr()).append(" or yxqz is NULL) ");
				}else if("2".equals(temp)){
					sqlWhere.append(" and yxqz is NULL");
				}
			}
			temp = map.get("lb_dm")==null?"":String.valueOf(map.get("lb_dm"));
			if(temp != null  && !"".equals(temp)){
				sqlWhere.append(" and lb_dm = '").append(temp).append("'");
			}
			temp = map.get("cx_xxzt")==null?"":String.valueOf(map.get("cx_xxzt"));
			if(temp != null  && !"".equals(temp)){
				sqlWhere.append(" and zt like '%").append(temp).append("%'");
			}
			temp = map.get("cx_rq1")==null?"":String.valueOf(map.get("cx_rq1"));
			if(temp != null  && !"".equals(temp)){
				if("oracle".equals(db_type)){
					sqlWhere.append(" and cjsj > to_date('").append(temp).append("','yyyy-mm-dd')");
				}else{
					sqlWhere.append(" and cjsj > '").append(temp).append("'");
				}
			}
			temp = map.get("cx_rq2")==null?"":String.valueOf(map.get("cx_rq2"));
			if(temp != null  && !"".equals(temp)){
				if("oracle".equals(db_type)){
					sqlWhere.append(" and cjsj < to_date('").append(temp).append(" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
				}else{
					sqlWhere.append(" and cjsj < '").append(temp).append(" 23:59:59'");
				}
			}
			sqlStr.append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(select xm from t_org_yh where yhid =a.yhid) fbr, cjsj ,yxqq,yxqz from t_publish_mx a where zt_dm = '8002' and fbfwbz = 1 ").append(sqlWhere.toString());
			sqlStr.append(" union  ");
			sqlStr.append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(select xm from t_org_yh where yhid =a.yhid) fbr, cjsj,yxqq,yxqz from t_publish_mx a where zt_dm = '8002' and fbfwbz = 0 ")
				  .append(" and guid in (").append("select ywid from t_diffusion_yhfw where zt_dm =1 and fsfw_ry='").append(yhid).append("')").append(sqlWhere.toString());
			sqlCount.append("select count(*) from (").append(sqlStr.toString()).append(") a");
			PageBean page = new PageBean();
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			page.setOrderBy(" cjsj desc ");
			if(map.get("query_page")!=null){
				page.setPageGoto(map.get("query_page").toString());
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("queryJs");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.queryJs异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map delJs(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("delete  from t_diffusion_fsfw  where ywid ='"+guid+"' and fsfw_ry='"+yhid+"'");
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("delJs");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.delJs异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map showJs(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				StringBuffer sqlStr = new StringBuffer();
				sqlStr.append("select (select xm from t_org_yh where yhid =a.yhid) yhid,fjid,yxqq,yxqz,zt,nr from t_publish_mx a where guid ='"+guid+"'");
				Map _map = this.getJtN().queryForMap(sqlStr.toString());
				jjd.setExtend("nr",_map.get("nr")==null?"":String.valueOf(_map.get("nr")));
				jjd.setExtend("zt",_map.get("zt")==null?"":String.valueOf(_map.get("zt")));
				jjd.setExtend("fbr",_map.get("yhid")==null?"":String.valueOf(_map.get("yhid")));
				jjd.setExtend("yxqq",_map.get("yxqq")==null?"":String.valueOf(_map.get("yxqq")));
				jjd.setExtend("yxqz",_map.get("yxqz")==null?"":String.valueOf(_map.get("yxqz")));
				String fjId = _map.get("fjid")==null?"":String.valueOf(_map.get("fjid"));
				jjd.setExtend("ifFj","0");
				if(!"".equals(fjId)){
					String diffusion_dir = SysPara.getValue("diffusion_dir");
					jjd.setExtend("diffusion_dir", diffusion_dir);
					String fjStr = "[";
					List fjList  = this.getJtN().queryForList("select wjmc,wjlj from t_attachment where zid = '"+_map.get("fjid").toString()+"'");
					for(int i=0;i<fjList.size();i++){
						Map fjMap = (Map)fjList.get(i);
						fjStr = fjStr+"{wjmc:'"+fjMap.get("wjmc")+"',wjlj:'"+fjMap.get("wjlj")+"'},";
					}
					fjStr = fjStr.substring(0,fjStr.length()-1);
					fjStr +="]";
					jjd.setExtend("fjStr",fjStr);
					jjd.setExtend("ifFj","1");
				}
				this.getJtN().update("update t_diffusion_yhfw set ydcs = ydcs+1 where ywid='"+guid+"' and fsfw_ry='"+yhid+"'");
			}else{
				jjd.setResult(false,"无法查询到信息，guid为空");
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("showJs");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询列表数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.showJs异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map save1(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("delete from t_diffusion_yhfw  where ywid ='"+guid+"'");
				this.getJtN().update("delete from t_publish_mx  where guid ='"+guid+"'");
				save();
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("save1");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.save1异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map send1(){
		try{
			String guid = map.get("guid")==null?"":String.valueOf(map.get("guid"));
			if(!"".equals(guid)){
				this.getJtN().update("delete from t_diffusion_yhfw  where ywid ='"+guid+"'");
				this.getJtN().update("delete from t_publish_mx  where guid ='"+guid+"'");
				send();
			}
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("send1");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("桌面请求发布信息异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.send1异常:" + e.toString());
		}
		return jjd.getData();
	}
	public Map listDesk(){
		try{
			StringBuffer sqlStr= new StringBuffer();
			StringBuffer sqlCount = new StringBuffer();
			sqlStr.append("select guid,zt,(select mc from t_dm where dm = a.lb_dm) xxlb,(select xm from t_org_yh where yhid =a.yhid) fbr,cjsj,yxqq,yxqz from t_publish_mx a where zt_dm = '8002' and yxqz >=").append(getDateStr());
			sqlStr.append(" and (fbfwbz = 1 or guid in(").append("select ywid from t_diffusion_yhfw where zt_dm =1 and fsfw_ry='").append(yhid).append("')");
			sqlStr.append(" ) order by cjsj desc ");
			sqlCount.append("select count(*) from (").append(sqlStr.toString()).append(") a");
			PageBean page = new PageBean();
			page.setSql(sqlStr.toString());
			page.setCountSql(sqlCount.toString());
			if(map.get("query_page")!=null){
				page.setPageGoto(map.get("query_page").toString());
			}else{
				page.setPageGoto("1");
			}
			if(map.get("page_num")!=null){
				page.setPageSize(map.get("page_num").toString());
			}else{
				page.setPageSize("10");
			}
			page.setNamedParameters(map);
			List _list = Db.getPageData(page);
			jjd.setGrid("datalist", _list, page);
		}catch(Exception e){
			String guid = Guid.get();
			logItem.setMethod("listDesk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("桌面请求发布信息异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "Diffusion.listDesk异常:" + e.toString());
		}
		return jjd.getData();
	}
}

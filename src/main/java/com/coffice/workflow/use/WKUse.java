package com.coffice.workflow.use;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coffice.bean.PageBean;
import com.coffice.bean.UserBean;
import com.coffice.directory.organization.Org_pub;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.SysPara;
import com.coffice.util.TransSql;
import com.coffice.util.Yhfw;
import com.coffice.util.cache.Cache;
import com.coffice.workflow.util.WorkFlowMethod;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.JoinDescriptor;
import com.opensymphony.workflow.loader.ResultDescriptor;
import com.opensymphony.workflow.loader.SplitDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.SimpleStep;

public class WKUse extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象
	LogItem logItem;// 日志项
	String zzid;//组织ID
	String bmid;//部门ID
	String gwid;//岗位ID
	String yhid;
	Map map;
	String sql;
	WorkFlowMethod wfMethod=null;
	String ywid="";	
	public WKUse(){
		logItem = new LogItem();
		
	}	
	public WKUse(Map mapIn){
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

	public void setYwid(String ywid){
		this.ywid=ywid;
	}	
	public String getYwid(){
		return this.ywid;
	}
	public void setMap(String key,String value){
		this.map.put(key, value);
	}
	public void setMap(Map cacheMap){
		this.map.putAll(cacheMap);
	}
	/**
	 * 创建工作流实例
	 * @return
	 */
	public  Map createWfInstance(){
		String username=yhid;
		String wfname=String.valueOf(map.get("wfT"));//流程名称
		String ldlb_dm=String.valueOf(map.get("lclb_dm"));//流程类别代码
		String mk_dm=String.valueOf(map.get("mk_dm"));//模块代码
		String mkurllb=String.valueOf(map.get("mkurllb"));
		String undo_title=String.valueOf(map.get("undo_title"));
		String formid=String.valueOf(map.get("formid"));
		String ywid=this.getYwid();//业务id
		String workid="";
		String stepid="";
		long wfId=0;
		StringBuffer strSql=new StringBuffer();
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("开始创建工作流实例：").append(wfname);
			//log.info(sb.toString());
			HashMap inputs = new HashMap();
			inputs.put("lclb_dm", ldlb_dm);
			inputs.put("mk_dm", mk_dm);
			inputs.put("mkurllb", mkurllb);
			inputs.put("ywid", ywid);
			inputs.put("username", username);
			inputs.put("zzid", zzid);
			inputs.put("undo_title", undo_title);
			inputs.put("formid", formid);
			UserBean userbean = (UserBean)Cache.getUserInfo(yhid, "userbean");
			if(userbean!=null){
				bmid = userbean.getBmid();
				gwid = userbean.getGwid();
			}else{
				strSql.append("select lrr_bmid,lrr_gwid from t_org_yh where yhid=?");
				List<Map> _list=this.getJtN().queryForList(strSql.toString(),new Object[]{username});
				for(Map _map:_list){
					bmid=(String)_map.get("lrr_bmid");
					gwid=(String)_map.get("lrr_gwid");
				}
			}
			inputs.put("bmid", bmid);
			inputs.put("gwid", gwid);
			sql = "select id from t_oswf_def where name='"+wfname+"'";
			List<Map> list2=this.getJtN().queryForList(sql);
			for(Map _map:list2){
				workid=String.valueOf(_map.get("id"));
			}
			inputs.put("workid", workid);
			Workflow wf=new BasicWorkflow(username);
			Configuration config = new DefaultConfiguration();
			wf.setConfiguration(config);
			wfId=wf.initialize(wfname, 2127, inputs);
			/*sql="select id,workid,step_id from t_oswf_work_item where entry_id='"+wfId+"' ";
			List<Map> list1=this.getJtN().queryForList(sql);
			for(Map _map:list1){
				map.put("id", String.valueOf(_map.get("id")));
				workid=String.valueOf(_map.get("workid"));
				stepid=String.valueOf(_map.get("step_id"));
			}*/
			map.put("workid", workid);
			map.put("entryid", String.valueOf(wfId));
			map.put("stepid", stepid);
			sb.delete(0, sb.length());
			sb.append("创建工作流实例：[").append(wfId).append("]成功");
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("创建工作流实例:[").append(wfId).append("]时出现异常:").append(e.toString()).toString();
			//log.error(msg);
			jjd.setResult(false, msg);
		}
		return map;
	}
	/**
	 * 保存业务和表单的关系
	 * @param entryid
	 * @param stepid
	 * @param formid
	 * @param verid
	 */
	public void checkBusinesForm(String entryid,String stepid,String formid,String verid){
		try {
			sql="select * from t_oswf_busi_form_relation where businessId='"+entryid+"' and stepID='"+stepid+"' and formId='"+formid+"'";
			List<Map> list1=this.getJtN().queryForList(sql);
			if(list1.size()==0){
				wfMethod=new WorkFlowMethod();
				wfMethod.saveBusinessForm(entryid, stepid+"", formid,verid);
			}
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("checkBusinesForm");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存业务和表单的关系时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}
	/**
	 * 显示流程实例项目
	 * @param reponse
	 */
	public Map showWfInstanceItem() {
		String username=yhid;
		//待办业务id
		String businessid=String.valueOf(map.get("id"));
		long entryId=0;
		int stepId=0;
		String stepName="";
		String wkName="";
		String workid="";
		String steptype="";
		String mk_dm="";
		String dbFlag="";
		Map _map=null;
		ArrayList _list=new ArrayList();
		StringBuffer strSql=new StringBuffer();
		String zwbz="";//正文标记
		String bllx = map.get("bllx")==null?"-1":String.valueOf(map.get("bllx"));
		String tsgd = map.get("tsgd")==null?"":String.valueOf(map.get("tsgd"));
		try{
			sql="select a.workid,a.entry_id,a.step_id,a.doc_id,a.mk_dm,a.mkurllb from t_oswf_work_item a where id='"+businessid+"'";
			List<Map> list1=this.getJtN().queryForList(sql);
			for(Map map:list1){
				workid=String.valueOf(map.get("workid"));
				entryId=Long.parseLong(String.valueOf(map.get("entry_id")));
				stepId=Integer.parseInt(String.valueOf(map.get("step_id")));
				mk_dm=String.valueOf(map.get("mk_dm"));//模块代码
			}
			Workflow wf = new BasicWorkflow(username);
			wkName=wf.getWorkflowName(entryId);
		    WorkflowDescriptor wd = wf.getWorkflowDescriptor(wkName);
			StepDescriptor stepDe = (StepDescriptor) wd.getStep(stepId);
			stepName=stepDe.getName();//步骤名称
			List list=stepDe.getActions();/*本步骤的所有动作*/
		    int[] actions = wf.getAvailableActions(entryId, null);
		    /*查找可以执行的动作*/
		    for (int i = 0; i < actions.length; i++) {
		    	 for (int j = 0; j < list.size(); j++) {
		    		 ActionDescriptor action = (ActionDescriptor) list.get(j);
		    		 int id = action.getId();
		    		 if(actions[i]==action.getId()){
		    			 if("1".equals(bllx) && stepId == 98){
		    				 //重办时如办理类型是市级，则隐藏县级重办
		    				 if(action.getId() == 9803){
		    					 break;
		    				 }
		    			 }
		    			 if("0".equals(bllx) && stepId == 98){
		    				 //重办时如办理类型是县级，则隐藏市级重办
		    				 if(action.getId() == 9802){
		    					 break;
		    				 }
		    			 }
		    			 //对于特殊类工单,单位可以直接结束
		    			 if("".equals(tsgd) || ("600700").equals(tsgd)){
		    				 //非特殊 不显示单位办结按钮
		    				 if(action.getId() == 5704 || action.getId() == 9304){
		    					 break;
		    				 }
		    			 }else{
		    				 //特殊 显示办结按钮，不显示其他按钮
		    				 if(action.getId() == 5701 || action.getId() == 5702 || action.getId() == 5703 ||action.getId() == 9301 || action.getId() == 9302 || action.getId() == 9303	){
		    					 break;
		    				 }
		    			 }
		    			 _map=new HashMap();
		    			 _map.put("actionid", action.getId());
		    			 _map.put("actionname", action.getName());
		    			 _list.add(_map);
		    			 break;
		    		 }
		    	 }
		    }
		    wfMethod=new WorkFlowMethod();
		    String formId=wfMethod.findFormIdByBusiness((int)entryId,stepId);
			//更新签收时间。
		    if(stepId == 57 || stepId == 93){
				//办理部门更新签收时间
				Db.getJtN().update("update t_form1305 set qssj = ISNULL(qssj,CONVERT(varchar(120),GETDATE(),120)) where id='"+formId+"'");
				Db.getJtN().update("update t_cbd_sync set qssj = ISNULL(qssj,CONVERT(varchar(120),GETDATE(),120)) where formid='"+formId+"'");
			}
		    //查询当前步骤可用功能
		    _map=wfMethod.getFuncConfig(workid,stepId,entryId+"");
		    map.putAll(_map);
		    
		    sql="select steptype from t_oswf_noderole_config where workId='"+workid+"' and stepID="+stepId;
		    List<Map> list2=this.getJtN().queryForList(sql);
		    if(list2.size()!=0){
				for(Map map:list2){
					steptype=String.valueOf(map.get("steptype"));
				}
		    }else{
		    	steptype="0";
		    }
		    map.put("id", entryId);
		    map.put("stepId", stepId);
		    map.put("stepName", stepName);
		    //流程动作List
		    map.put("actionList", _list);
		    map.put("formId", formId);
		    map.put("wkName", wkName);
		    //此流程所有的步骤List
			List stepList=wd.getSteps();
			map.put("stepList", stepList);
			map.put("steptype", steptype);
			map.put("mk_dm", mk_dm);//模块代码
			//待办表Id
			map.put("businessid", businessid);//业务id
			map.put("username", yhid);
			map.put("workid", workid);
		}catch(Exception e){
			String msg=new StringBuffer("显示流程实例:[").append(entryId).append("]项目时出现异常:").append(e.toString()).toString();
			jjd.setResult(false, msg);
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 执行流程动作
	 * @return
	 */
	public Map doWfInstanceAction(){
		long id = Long.parseLong(String.valueOf(map.get("id")));//实例id entryid
		String businessid=String.valueOf(map.get("businessid"));//业务id 待办id
		String formId = String.valueOf(map.get("formid"));//表单实例id formid
		String stepId=String.valueOf(map.get("stepid"));//步骤id
		String doString = (String)map.get("doId");//动作id
		String username=yhid;
		String sendtype=(String)map.get("sendtype");//发送方式(是否选人)
		String lclb_dm=String.valueOf(map.get("lclb_dm"));//流程类别代码
		String mk_dm=String.valueOf(map.get("mk_dm"));//模块代码
		String mkurllb=String.valueOf(map.get("mkurllb"));
		String undo_title=String.valueOf(map.get("undo_title"));//标题
		Map wfMap=new HashMap();
		wfMap.put("lclb_dm", lclb_dm);
		//wfMap.put("mk_dm", mk_dm);
		wfMap.put("mk_dm", "8808");
		//wfMap.put("mkurllb", mkurllb);
		wfMap.put("mkurllb", 1);
		wfMap.put("lclb_dm", 0);
		wfMap.put("ywid", businessid);
		wfMap.put("username", username);
		wfMap.put("undo_title", undo_title);
		wfMap.put("formid", formId);
		wfMap.put("stepId", stepId);
		wfMap.put("zzid", zzid);
		//UserBean userbean = (UserBean)Cache.getUserInfo(yhid, "userbean");
		UserBean userbean = UserBean.get(yhid);
		bmid = userbean.getBmid();
		gwid = userbean.getGwid();
		wfMap.put("bmid", bmid);
		wfMap.put("gwid", gwid);
		List ygList=null;
		Map nextStepConfig=null;
		String workid=map.get("workid").toString();
		wfMap.put("workid", workid);
		String taskAllocation="";//分配方式
		String workcondition="";//条件
		String amount="";//个数
		String remind="";//提醒
		String stepnames="";
		Map hismap=new HashMap();
		hismap.put("entryid", id);
		hismap.put("xm", Cache.getUserInfo(yhid, "xm"));
		StringBuffer strSql=new StringBuffer();
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("开始执行流程实例：[").append(id).append("]");
 
			BasicWorkflow wf = new BasicWorkflow(username);
			wfMethod=new WorkFlowMethod();
			jjd.setExtend("entryid", String.valueOf(id));
			//doString不为空时提交下一步
			if (doString != null && !doString.equals("")) {
		    	int action = Integer.parseInt(doString);
		    	String workname = wf.getWorkflowName(id);
				WorkflowDescriptor wd = wf.getWorkflowDescriptor(workname);
				ActionDescriptor actionDesc=wd.getAction(action);
				hismap.put("stepname", wd.getStep(Integer.parseInt(stepId)).getName());
				hismap.put("actionname", actionDesc.getName());
				//判断动作是否为回退
				if(!actionDesc.getName().equals("回退")){
					//根据当前动作得到下一步骤ID
					ResultDescriptor resultDesc=actionDesc.getUnconditionalResult();
					//判断动作下一步是step、split、join
					if(resultDesc.getStep()!=0){
						System.out.println("***********************getStep");
						int nextStep=resultDesc.getStep();
						jjd.setExtend("nextstep", nextStep);
						//代表下一步为尾结点
						if(wd.getStep(nextStep).getActions().size()==0){
							Cache.setUserInfo(yhid, "eflag", "1");//结束标志
							Map endMap = new HashMap();
							endMap.put("isEnd",1);
							endMap.put("stepId",stepId);
							endMap.put("formid",formId);
							endMap.put("workid", workid);
							wf.doAction(id, action, endMap);
							//调用历史步骤
							hismap.put("nextstepname", wd.getStep(nextStep).getName());
							this.saveHisStep(hismap);
							jjd.setExtend("flag", "0");//直接处理
							jjd.setExtend("result", "success");
							map=jjd.getData();
						}else{//非尾节点
							//第一次执行工作流
							if(sendtype==null || "".equals(sendtype)){
								//得到下一步骤配置信息
								nextStepConfig=getNextStepConfig2(Integer.parseInt(id+""),nextStep,username,workid);
								workid=String.valueOf(nextStepConfig.get("workid"));
								taskAllocation=String.valueOf(nextStepConfig.get("taskAllocation"));
								workcondition=String.valueOf(nextStepConfig.get("workcondition"));
								amount=String.valueOf(nextStepConfig.get("amount"));
								remind=String.valueOf(nextStepConfig.get("remind"));
								ygList=(List)nextStepConfig.get("ygList");
								if(taskAllocation.equals("0")){//手工分配
									Cache.setUserInfo(username, "isShowYh", "0");//手工分配是否显示用户列表(0、显示 1、不显示)
									Cache.setUserInfo(username, "ygList", ygList);//用户列表
									Cache.setUserInfo(username, "workcondition", workcondition);//条件
									Cache.setUserInfo(username, "amount", amount);//个数
									Cache.setUserInfo(username, "remind", remind);//提醒
									//Cache.setUserInfo(username, "isCuib", isCuib); 
									Cache.setUserInfo(username, "nextStep", nextStep);//下一步骤id
									//Cache.setUserInfo(username, "select_user_count_flag", select_user_count_flag);
									jjd.setExtend("isShowYh", "0");//wap使用
									jjd.setExtend("remind", remind);//wap使用
									/*jjd.setExtend("isCuib", isCuib);*/ 
									jjd.setExtend("flag", "1");//手工处理
									jjd.setExtend("workcondition", workcondition);//0(多人)1(单人)
									jjd.setExtend("amount", amount);// 
									jjd.setExtend("nextStep", nextStep);//下一步
									Cache.setUserInfo(username, "_map", map);
									jjd.setExtend("result", "1");//一个节点用户列表
									map=jjd.getData();
								}else{//自动分配
									String txFlag=String.valueOf(map.get("txflag"));//提醒标记
									String cbFlag=String.valueOf(map.get("cbflag"));//催办标记
									if(!txFlag.equals("0") && !cbFlag.equals("0")){//没有设置提醒和催办功能(自动分配)
											//验证单人或多人审核
										if(this.validateShenHe(workid, stepId,id+"",yhid,nextStep+"",map)){
											for(int i=0;i<ygList.size();i++){
												Map mapyg=(Map)ygList.get(i);
												String userid=String.valueOf(mapyg.get("yhid"));
												wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(id + ""), userid,"","");
											}
											wf.doAction(id, action, wfMap);
												//调用历史步骤
											hismap.put("nextstepname", wd.getStep(nextStep).getName());
											this.saveHisStep(hismap);
										}else{//非多人，单人
												//调用历史步骤
											hismap.put("nextstepname", wd.getStep(nextStep).getName());
											this.saveHisStep(hismap);
										}
								    	jjd.setExtend("flag", "0");//直接处理
								    	jjd.setExtend("result", "success");
							    		map=jjd.getData();
									}else{//设置提醒或催办功能
										if(txFlag.equals("0")){
											Cache.setUserInfo(username, "remind", remind);//提醒
										}
										Cache.setUserInfo(username, "isShowYh", "1");//手工分配是否显示用户列表(0、显示 1、不显示)
										Cache.setUserInfo(username, "_map", map);
										jjd.setExtend("isShowYh", "1");//wap使用
										jjd.setExtend("remind", remind);//wap使用
										jjd.setExtend("flag", "1");//手工处理
										jjd.setExtend("result", "1");//一个节点用户列表
										map=jjd.getData();
									}
								}
					    	}else{
					    		map.put("username", username);
						    		//验证单人或多人审核
								if(this.validateShenHe(workid, stepId,id+"", yhid,nextStep+"",map)){
										//调用表单执行函数
									Map map1=(Map)Cache.getUserInfo(username, "_map");
									rgIssueTask(Integer.parseInt(id+""),nextStep,map);
									wf.doAction(id, action, wfMap);
										//调用历史步骤
									hismap.put("nextstepname", wd.getStep(nextStep).getName());
									this.saveHisStep(hismap);
								}else{
										//调用历史步骤
									hismap.put("nextstepname", wd.getStep(nextStep).getName());
									this.saveHisStep(hismap);
								}
								jjd.setExtend("flag", "1");
						    	jjd.setExtend("result", "success");
					    		map=jjd.getData();
							}
						}
					}else if(resultDesc.getSplit()!=0){//分支
						System.out.println("***********************split");
						int nextSplit=resultDesc.getSplit();
						SplitDescriptor splitDesc = wd.getSplit(nextSplit);
						List results = splitDesc.getResults();
						ArrayList userList=new ArrayList();
						String steps="";
						//下一步骤分配方式
						if("".equals(sendtype) || sendtype==null){
							//遍历分支每一节点,判断其分配方式
							for (int i=0;i<results.size();i++) {
				                ResultDescriptor resultDescriptor = (ResultDescriptor) results.get(i);
				                int nextStep=resultDescriptor.getStep();
				                String stepName=wd.getStep(nextStep).getName();
				                //得到下一步配置
				                nextStepConfig=getNextStepConfig(Integer.parseInt(id+""),nextStep,username);
								workid=String.valueOf(map.get("workid"));
								taskAllocation=String.valueOf(nextStepConfig.get("taskAllocation"));
								workcondition=String.valueOf(nextStepConfig.get("workcondition"));
								remind=String.valueOf(nextStepConfig.get("remind"));//提醒
								//isCuib=String.valueOf(nextStepConfig.get("isCuib"));//催办
								ygList=(List)nextStepConfig.get("ygList");
								//select_user_count_flag=String.valueOf(nextStepConfig.get("select_user_count_flag"));
								if(taskAllocation.equals("0")){//手工
									steps=steps+nextStep+",";
									Map usersMap=new HashMap();
									usersMap.put("stepid", nextStep);
									usersMap.put("stepName", stepName);
									usersMap.put("workcondition", workcondition);
									usersMap.put("remind", remind);
									//usersMap.put("isCuib", isCuib);
									usersMap.put("userList", ygList);
									//usersMap.put("select_user_count_flag", select_user_count_flag);//人员选择人数
									userList.add(usersMap);
								}
							}
							//全部分支为自动分配
							if(userList.size()==0){
								for (int i=0;i<results.size();i++) {
					                ResultDescriptor resultDescriptor = (ResultDescriptor) results.get(i);
					                int nextStep=resultDescriptor.getStep();
					                if(i==results.size()-1){
					                	stepnames=stepnames+wd.getStep(nextStep).getName();
					                }else{
					                	stepnames=stepnames+wd.getStep(nextStep).getName()+",";
					                }
					                nextStepConfig=getNextStepConfig(Integer.parseInt(id+""),nextStep,username);
					                ygList=(List)nextStepConfig.get("ygList");
									for(int j=0;j<ygList.size();j++){
										Map mapyg=(Map)ygList.get(j);
										String userid=String.valueOf(mapyg.get("yhid"));
										wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(id + ""), userid,"","");
									}
								}
								//调用表单执行函数
								/*if(!"0".equals(callfun)){
									map.put("workid", wfMethod.queryWorkidByEntryid(String.valueOf(id)));
									String rturn=exeCallFun(map);
									if(!"1".equals(rturn)){
										jjd.setExtend("flag", "0");//直接处理
										jjd.setExtend("rturn", rturn);
										jjd.setExtend("result", "rerror");
										return jjd.getData();
									}
								}*/
								wf.doAction(id, action, wfMap);
								//调用历史步骤
								hismap.put("nextstepname", stepnames);
								this.saveHisStep(hismap);
								//执行传阅
								//execChuany(zzid,bmid,gwid,yhid,id+"",isChuany,cyr_value,businessid,undo_title);
								jjd.setExtend("flag", "0");
								jjd.setExtend("result", "success");
					    		map=jjd.getData();
							}else{
								//手工处理
								Cache.setUserInfo(username, "ygsList", userList);
								Cache.setUserInfo(username, "steps", steps);
								Cache.setUserInfo(username, "_map", map);
								//Cache.setUserInfo(username, "select_user_count_flag", select_user_count_flag);//人员选择人数
								jjd.setExtend("flag", "1");//手工处理
								jjd.setExtend("result", "2");//多个节点用户列表
								map=jjd.getData();
							}
						}else{//手工选择下一步处理人
							steps=String.valueOf(map.get("steps"));
							steps=steps.substring(0, steps.length()-1);
							String[] stepsid=steps.split(",");
							//手动
							for(int i=0;i<stepsid.length;i++){
								String userIds=String.valueOf(map.get("userIds"+stepsid[i]));
								stepnames=stepnames+wd.getStep(Integer.parseInt(stepsid[i])).getName()+",";
								rgIssueTask(userIds,Integer.parseInt(id+""),Integer.parseInt(stepsid[i]),map);
							}
							//判断有没有自动分配
							for (int i=0;i<results.size();i++) {
								boolean ifxd=false;
				                ResultDescriptor resultDescriptor = (ResultDescriptor) results.get(i);
				                int nextStep=resultDescriptor.getStep();
				                for(int j=0;j<stepsid.length;j++){
				                	if(nextStep==Integer.parseInt(stepsid[j])){
				                		ifxd=true;
				                		break;
				                	}
				                }
				                if(!ifxd){
				                	nextStepConfig=getNextStepConfig(Integer.parseInt(id+""),nextStep,username);
									workid=String.valueOf(map.get("workid"));
									taskAllocation=String.valueOf(nextStepConfig.get("taskAllocation"));
									workcondition=String.valueOf(nextStepConfig.get("workcondition"));
									ygList=(List)nextStepConfig.get("ygList");
					                stepnames=stepnames+wd.getStep(nextStep).getName()+",";
									for(int k=0;k<ygList.size();k++){
										Map mapyg=(Map)ygList.get(k);
										String userid=String.valueOf(mapyg.get("yhid"));
										wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(id + ""), userid,"","");
									}
				                }
							}
							stepnames=stepnames.substring(0,stepnames.length()-1);
							//调用表单执行函数
							Map map1=(Map)Cache.getUserInfo(username, "_map");
							/*callfun=(String)map1.get("callfun");
							if(!"0".equals(callfun)){
								map1.put("workid", wfMethod.queryWorkidByEntryid(String.valueOf(id)));
								String rturn=exeCallFun(map1);
								if(!"1".equals(rturn)){
									jjd.setExtend("flag", "0");//直接处理
									jjd.setExtend("rturn", rturn);
									jjd.setExtend("result", "rerror");
									return jjd.getData();
								}
							}*/
							wf.doAction(id, action, wfMap);
							//调用历史步骤
							hismap.put("nextstepname", stepnames);
							this.saveHisStep(hismap);
							//执行传阅
							//execChuany(zzid,bmid,gwid,yhid,id+"",isChuany,cyr_value,businessid,undo_title);
							jjd.setExtend("flag", "1");
							jjd.setExtend("result", "success");
				    		map=jjd.getData();
						}
						
					}else if(resultDesc.getJoin()!=0){//聚合
						System.out.println("***********************Join");
						int nextJoin=resultDesc.getJoin();
						resultDesc=actionDesc.getUnconditionalResult();
						JoinDescriptor joinDesc =wd.getJoin(nextJoin);
						ResultDescriptor results=joinDesc.getResult();
						int nextStep=results.getStep();
						//代表下一步为尾结点
						if(wd.getStep(nextStep).getActions().size()==0){
							wf.doAction(id, action, Collections.EMPTY_MAP);
							jjd.setExtend("flag", "0");//直接处理
							jjd.setExtend("result", "success");
							map=jjd.getData();
						}else{
							if("".equals(sendtype) || sendtype==null){
								//得到下一步骤配置信息
								nextStepConfig=getNextStepConfig(Integer.parseInt(id+""),nextStep,username);
								workid=String.valueOf(nextStepConfig.get("workid"));
								taskAllocation=String.valueOf(nextStepConfig.get("taskAllocation"));
								workcondition=String.valueOf(nextStepConfig.get("workcondition"));
								remind=String.valueOf(nextStepConfig.get("remind"));
								//isCuib=String.valueOf(nextStepConfig.get("isCuib"));
								ygList=(List)nextStepConfig.get("ygList");
								//select_user_count_flag=String.valueOf(nextStepConfig.get("select_user_count_flag"));
								if(taskAllocation.equals("0")){
									//手工
									Cache.setUserInfo(username, "isShowYh", "0");//手工分配是否显示用户列表(0、显示 1、不显示)
									Cache.setUserInfo(username, "ygList", ygList);
									Cache.setUserInfo(username, "workcondition", workcondition);
									Cache.setUserInfo(username, "remind", remind);//提醒
									//Cache.setUserInfo(username, "isCuib", isCuib);//催办
									Cache.setUserInfo(username, "_map", map);
									//Cache.setUserInfo(username, "select_user_count_flag", select_user_count_flag);//人员选择人数
									jjd.setExtend("flag", "1");//手工处理
									jjd.setExtend("result", "1");//一个节点用户列表
									map=jjd.getData();
								}else{
						    		//自动
									String txFlag=String.valueOf(map.get("txFlag"));//提醒标记
									String cbFlag=String.valueOf(map.get("cbFlag"));//催办标记
									if(!txFlag.equals("0") && !cbFlag.equals("0")){//没有设置提醒和催办功能(自动分配)
										for(int i=0;i<ygList.size();i++){
											Map mapyg=(Map)ygList.get(i);
											String userid=String.valueOf(mapyg.get("yhid"));
											wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(id + ""), userid,"","");
										}
										//调用表单执行函数
										/*if(!"0".equals(callfun)){
											map.put("workid", wfMethod.queryWorkidByEntryid(String.valueOf(id)));
											String rturn=exeCallFun(map);
											if(!"1".equals(rturn)){
												jjd.setExtend("flag", "0");//直接处理
												jjd.setExtend("rturn", rturn);
												jjd.setExtend("result", "rerror");
												return jjd.getData();
											}
										}*/
							    		wf.doAction(id, action, wfMap);
										//调用历史步骤
										hismap.put("nextstepname", wd.getStep(nextStep).getName());
										this.saveHisStep(hismap);
										//执行传阅
										//execChuany(zzid,bmid,gwid,yhid,id+"",isChuany,cyr_value,businessid,undo_title);
							    		jjd.setExtend("flag", "0");//直接处理
							    		jjd.setExtend("result", "success");
							    		map=jjd.getData();
									}else{//设置提醒或催办功能
										if(txFlag.equals("0")){
											Cache.setUserInfo(username, "remind", remind);//提醒
										}
										/*if(cbFlag.equals("0")){
											Cache.setUserInfo(username, "isCuib", isCuib);//催办
										}*/
										Cache.setUserInfo(username, "isShowYh", "1");//手工分配是否显示用户列表(0、显示 1、不显示)
										Cache.setUserInfo(username, "_map", map);
										jjd.setExtend("flag", "1");//手工处理
										jjd.setExtend("result", "1");//一个节点用户列表
										map=jjd.getData();
									}
								}
					    	}else{//手工选择下一步处理人
								//分配指定待办表
					    		map.put("username", username);
					    		//调用表单执行函数
					    		Map map1=(Map)Cache.getUserInfo(username, "_map");
					    		/*callfun=(String)map1.get("callfun");
					    		if(!"0".equals(callfun)){
					    			map1.put("workid", wfMethod.queryWorkidByEntryid(String.valueOf(id)));
									String rturn=exeCallFun(map1);
									if(!"1".equals(rturn)){
										jjd.setExtend("flag", "0");//直接处理
										jjd.setExtend("rturn", rturn);
										jjd.setExtend("result", "rerror");
										return jjd.getData();
									}
					    		}*/
								rgIssueTask(Integer.parseInt(id+""),nextStep,map);
					    		wf.doAction(id, action, wfMap);
								//调用历史步骤
								hismap.put("nextstepname", wd.getStep(nextStep).getName());
								this.saveHisStep(hismap);
								//执行传阅
								//execChuany(zzid,bmid,gwid,yhid,id+"",isChuany,cyr_value,businessid,undo_title);
					    		jjd.setExtend("flag", "1");
					    		jjd.setExtend("result", "success");
					    		map=jjd.getData();
							}
						}
					}
					sb.delete(0, sb.length());
					sb.append("发送流程实例:[").append(id).append("]成功");
					//log.warn(sb.toString());
				}else{//回退
					boolean isAbleHuiTui = wfMethod.isAbleHuiTui(String.valueOf(id), stepId);
					if(!isAbleHuiTui){
						jjd.setExtend("flag", "disablehuitui");//不能回退
						return jjd.getData();
					}
					List<Map> tempList = this.getJtN().queryForList("select * from t_oswf_work_item where Entry_ID="+id+" and Step_ID="+stepId+" and status='0'");
					if(tempList.size()>0){
						jjd.setExtend("flag", "disablehuitui");//不能回退
						return jjd.getData();
					}
					
					//执行驳回动作
					String oswf_huti_tx=SysPara.getValue("oswf_huit_tx");//获取回退提醒(0、不提醒　1、提醒)
					String stepids="";
					if(oswf_huti_tx!=null && "1".equals(oswf_huti_tx)){
						//下一步骤分配方式
						if("".equals(sendtype) || sendtype==null){
							Cache.setUserInfo(username, "_map", map);
							jjd.setExtend("flag", "1");//手工处理
							jjd.setExtend("result", "3");//回退提醒
							map=jjd.getData();
						}else{
							if(sendHuitTx(String.valueOf(id),stepId,map)){
//								wf.getPropertySet(id).setString("isBack", "yes");
//								wf.doAction(id, action, wfMap);
								stepids=wfMethod.findos_historystep(String.valueOf(id),stepId,null,yhid);
								String[] steps=stepids.split(",");
								for(int i=0;i<steps.length;i++){
									if(i==steps.length-1){
										stepnames=stepnames+wd.getStep(Integer.parseInt(steps[i])).getName();
									}else{
										stepnames=stepnames+wd.getStep(Integer.parseInt(steps[i])).getName()+",";
									}
								}
								//调用历史步骤
								hismap.put("nextstepname", stepnames);
								this.saveHisStep(hismap);
								//执行传阅
								//execChuany(zzid,bmid,gwid,yhid,id+"",isChuany,cyr_value,businessid,undo_title);
							}
							jjd.setExtend("flag", "0");//自动处理
							jjd.setExtend("result", "success");
				    		map=jjd.getData();
							sb.delete(0, sb.length());
							sb.append("发送流程实例:[").append(id).append("]成功");
							//log.warn(sb.toString());
						}
					}else{
						//wf.getPropertySet(id).setString("isBack", "yes");
						//wf.doAction(id, action, wfMap);
						stepids=wfMethod.findos_historystep(String.valueOf(id),stepId,null,yhid);
						String[] steps=stepids.split(",");
						for(int i=0;i<steps.length;i++){
							if(i==steps.length-1){
								stepnames=stepnames+wd.getStep(Integer.parseInt(steps[i])).getName();
							}else{
								stepnames=stepnames+wd.getStep(Integer.parseInt(steps[i])).getName()+",";
							}
						}
						//调用历史步骤
						hismap.put("nextstepname", stepnames);
						this.saveHisStep(hismap);
						jjd.setExtend("flag", "0");//自动处理
						jjd.setExtend("result", "success");
			    		map=jjd.getData();
						sb.delete(0, sb.length());
						sb.append("回退流程实例:[").append(id).append("]成功");
					}
				}
				
		    }else{
		    	jjd.setExtend("result", "success");
		    	map=jjd.getData();
				sb.delete(0, sb.length());
				sb.append("保存流程实例:[").append(id).append("]成功");
		    }
			//保存业务和表单的关系
			List currentStep=wf.getCurrentSteps(id);
		    for(Iterator iterator=currentStep.iterator();iterator.hasNext();){
		    	SimpleStep step=(SimpleStep)iterator.next();
		    	checkBusinesForm(id+"",step.getStepId()+"",formId,"1");	
		    }
		}catch(Exception e){
			String msg=new StringBuffer("执行流程实例：[").append(id).append("]时出现异常:").append(e.toString()).toString();
			log.error(msg);
		}
		return map;
	}
	/**
	 * 执行表单回调函数
	 * @param _map
	 */
	public String exeCallFun(Map _map){
		String callfun=(String)_map.get("callfun");
		String flag="1";
		try {
			if(callfun!=null && !"".equals(callfun)){
				String classname=callfun.substring(0, callfun.lastIndexOf("."));
				String methodname=callfun.substring(callfun.lastIndexOf(".")+1);
				String workid=(String)_map.get("workid");
				String entryid=String.valueOf(_map.get("id"));
				Object obj=Class.forName(classname).newInstance();
				Method method=obj.getClass().getMethod(methodname, new Class[]{String.class,String.class});
			    String tableName=wfMethod.findTableNameById(workid);
			    String formId=wfMethod.findFormIdByBusiness(Integer.parseInt(entryid));
				method.invoke(obj, new String[]{tableName,formId});
			}
		} catch (Exception e) {
			InvocationTargetException targetEx = (InvocationTargetException)e; 
	    	Throwable t = targetEx.getTargetException();
			String guid = Guid.get();
			logItem.setMethod("exeCallFun(Map _map)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("执行表单回调函数时出现异常：");
			logItem.setContent(t.toString());
			Log.write(logItem);
			flag=t.toString();
		}
		return flag;
	}
	/**
	 * 保存历史步骤
	 * @param _map
	 */
	public void saveHisStep(Map _map){
		String entryid=String.valueOf(_map.get("entryid"));//实例id
		String stepname=String.valueOf(_map.get("stepname"));//当前步骤名称
		String actionname=String.valueOf(_map.get("actionname"));//动作名称
		String xm=String.valueOf(_map.get("xm"));//当前处理人
		String nextstepname=String.valueOf(_map.get("nextstepname"));//下一步骤名称
		StringBuffer sqlStr=new StringBuffer();
		try {
			if(nextstepname!=null&& !"".equals(nextstepname)&& !"null".equals(nextstepname)){
				String[] nextsteps=nextstepname.split(",");
				for(int i=0;i<nextsteps.length;i++){
					sqlStr.delete(0, sqlStr.length());
					sqlStr.append("insert into t_oswf_historystep(guid,entryid,stepname,actionname,xm,nextstepname,cjsj) ");
					sqlStr.append("values(?,?,?,?,?,?,").append(getDateStr()).append(")");
					this.getJtN().update(sqlStr.toString(), new Object[]{Guid.get(),entryid,stepname,actionname,xm,nextsteps[i]});
				}
			}else{
				sqlStr.delete(0, sqlStr.length());
				sqlStr.append("insert into t_oswf_historystep(guid,entryid,stepname,actionname,xm,nextstepname,cjsj) ");
				sqlStr.append("values(?,?,?,?,?,?,").append(getDateStr()).append(")");
				this.getJtN().update(sqlStr.toString(), new Object[]{Guid.get(),entryid,stepname,actionname,xm,"流程结束"});
			}
		} catch (Exception e) {
			
			String msg=new StringBuffer("保存历史步骤时：[").append(entryid).append("]时出现异常:").append(e.toString()).toString();
			e.printStackTrace();
			log.error(msg);
		}
	}
	/**
	 * 执行传阅
	 * @param isChuany
	 * @param cyr_value
	 * @return
	 */
	public boolean execChuany(String zzid1,String bmid1,String gwid1,String yhid1,String entryid,String isChuany,String cyr_value,String businessid,String ict_gwbt){
		boolean flag=false;
		try {
			if("1".equals(isChuany)){
				UserBean userBean=new UserBean();
				userBean.setZzid(zzid1);
				userBean.setBmid(bmid1);
				userBean.setGwid(gwid1);
				userBean.setYhid(yhid1);
				Yhfw.save(userBean, businessid, "103", cyr_value, "1", 2, ict_gwbt, 1);
			}
		} catch (Exception e) {
			String msg=new StringBuffer("执行传阅：[").append(entryid).append("]时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return flag;
	}
	
	/**
	 * 子流程结束
	 * @param entryid
	 * @param _map
	 * @param wf
	 * @param wd
	 * @param collections
	 * @return
	 */
	public boolean endSubEntry(String entryid,Map _map,BasicWorkflow wf,Map collections){
		boolean flag=false;
		StringBuffer strSql=new StringBuffer();
		String parentid="";//父流程实例id
		String pstepid="";//父流程步骤id
		String pworkid="";//父流程id
		String lcjsbz="";//子流程结束标记
		String formbz="";//
		int actionid=0;
		Map nextStepConfig=null;
		List ygList=null;
		int nextStep=0;
		try {
		    //判断是否为子流程
		    if(SysPara.compareValue("oswf_usesubwf","1")){
		    	strSql.delete(0, strSql.length());
		    	strSql.append("select pworkid,parentid,stepid from t_oswf_busi_sub_relation where subid=?");
		    	List<Map> list4=this.getJtN().queryForList(strSql.toString(),new Object[]{entryid});
		    	if(list4.size()!=0){
		    		for(Map map4:list4){
		    			parentid=String.valueOf(map4.get("parentid"));
		    			pstepid=String.valueOf(map4.get("stepid"));
		    			pworkid=String.valueOf(map4.get("pworkid"));
		    		}
		    		strSql.delete(0, strSql.length());
		    		strSql.append("select lcjsbz_dm,formbz_dm from t_oswf_node_sub_config where workid=? and stepid=?");
		    		List<Map> list5=this.getJtN().queryForList(strSql.toString(),new Object[]{pworkid,pstepid});
		    		if(list5.size()!=0){
		    			for(Map map5:list5){
		    				lcjsbz=String.valueOf(map5.get("lcjsbz_dm"));
		    				formbz=String.valueOf(map5.get("formbz_dm"));
		    			}
		    		}
		    	}
		    	if(!"".equals(lcjsbz)){
		    		strSql.delete(0, strSql.length());
		    		strSql.append("select * from t_oswf_busi_sub_relation where parentid=? and state='0' ");
		    		List<Map> list6=this.getJtN().queryForList(strSql.toString(),new Object[]{parentid});
		    		if(list6.size()!=0){
	    				strSql.delete(0, strSql.length());
	    				strSql.append("update t_oswf_busi_sub_relation set state=1 where subid=? ");
	    				this.getJtN().update(strSql.toString(),new Object[]{entryid});
		    			if(list6.size()!=1){//父流程发启的子流程不是最后一个结束

		    			}else{//父流程发启的子流程是最后一个结束
		    				if("7809".equals(lcjsbz)){//子流程结束后归并至主流程上
		    					WorkflowDescriptor wd1 = wf.getWorkflowDescriptor(wf.getWorkflowName(new Long(parentid)));
		    					StepDescriptor stepDe = (StepDescriptor) wd1.getStep(Integer.parseInt(pstepid));
		    					List list=stepDe.getActions();
		    					ActionDescriptor action = (ActionDescriptor) list.get(0);
		    					actionid=action.getId();
		    					ActionDescriptor actionDesc=wd1.getAction(actionid);
		    					ResultDescriptor resultDesc=actionDesc.getUnconditionalResult();
		    					nextStep=resultDesc.getStep();
		    					//得到下一步骤配置信息
		    					nextStepConfig=getNextStepConfig(Integer.parseInt(parentid),nextStep,yhid);
		    					ygList=(List)nextStepConfig.get("ygList");
		    					wfMethod=new WorkFlowMethod();
		    					for(int i=0;i<ygList.size();i++){
		    						Map mapyg=(Map)ygList.get(i);
		    						String userid=String.valueOf(mapyg.get("yhid"));
		    						wfMethod.insertNodeUser(pworkid,nextStep, Integer.parseInt(parentid), userid,"","");
		    					}
		    					strSql.delete(0, strSql.length());
		    					strSql.append("select distinct id,doc_id,mk_dm,mkurllb from t_oswf_work_item where entry_id=?");
		    					List<Map> list7=this.getJtN().queryForList(strSql.toString(),new Object[]{parentid});
		    					for(Map map7:list7){
		    						collections.put("ywid", (String)map7.get("id"));
		    						collections.put("lclb_dm", String.valueOf(map7.get("doc_id")));
		    						collections.put("mk_dm", String.valueOf(map7.get("mk_dm")));
		    						collections.put("mkurllb", String.valueOf(map7.get("mkurllb")));
		    						if("7802".equals(formbz)){
		    							strSql.delete(0, strSql.length());
		    							strSql.append("select distinct formname from t_oswf_work_item where entry_id=?");
		    							Map map8=this.getJtN().queryForMap(strSql.toString(),new Object[]{parentid});
		    							collections.put("gwbt", (String)map8.get("formname"));
		    						}
		    					}
		    					wf.doAction(new Long(parentid), actionid, collections);
		    					//调用历史步骤
		    					Map hismap=new HashMap();
		    					hismap.put("entryid", parentid);
		    					hismap.put("xm", Cache.getUserInfo(yhid, "xm"));
		    					hismap.put("stepname", wd1.getStep(Integer.parseInt(pstepid)).getName());
		    					hismap.put("actionname", action.getName());
		    					hismap.put("nextstepname", wd1.getStep(nextStep).getName());
		    					this.saveHisStep(hismap);
		    				}
			    			//保存业务和表单的关系
		    				strSql.delete(0, strSql.length());
	    					strSql.append("select distinct formid from t_oswf_busi_form_relation where businessid=?");
	    					Map map9=this.getJtN().queryForMap(strSql.toString(),new Object[]{parentid});
						    checkBusinesForm(parentid+"",nextStep+"",(String)map9.get("formid"),"1");	
		    			}
		    		}
		    	}
		    }
		    flag=true;
		} catch (Exception e) {
			String msg=new StringBuffer("结束子流程：[").append(entryid).append("]时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return flag;
	}
	/**
	 * 得到下一步骤配置信息(taskAllocation,workcondition,ygList)
	 * @param entryid
	 * @param nextstep
	 * @param username
	 * @return
	 */
	public Map getNextStepConfig(int entryid,int nextstep,String username){
		List ygList=null;
		Map nextStep=new HashMap();
		try {
			wfMethod=new WorkFlowMethod();
			Map  _map=wfMethod.findNodeTaskAllocation(nextstep,entryid);
			//得到下一步骤的分配方式(0、手工 1、自动)
			String taskAllocation=String.valueOf(_map.get("taskAlltocation"));
			//0(多人)1(单人)
			String workcondition=String.valueOf(_map.get("workcondition"));
			//个数
			String amount=String.valueOf(_map.get("amount"));
			//提醒(0、提醒　1、不提醒)
			String remind=String.valueOf(_map.get("remind"));
			//催办(0、催办　1、不催办)
			/*String isCuib=String.valueOf(_map.get("isCuib"));*/
			
			String roleType="";//权限类别
			String roleValue="";//权限id
			String workid="";//流程id
			int stepid=0;//步骤id
			int condm=0;//1(发起者上级岗位)2(上一步执行者上级岗位)3(发起者)
			//ArrayList  nodeUsers=wfMethod.findNodeUser(nextstep, entryid);
			List nodeUsers =  wfMethod.findNodeUser1(nextstep, entryid);
			for(int j=0;j<nodeUsers.size();j++){
				Map data=(Map)nodeUsers.get(j);
				roleType=data.get("doType").toString();
				if(j==nodeUsers.size()-1){
					roleValue=roleValue+data.get("userId").toString();
				}else{
					roleValue=roleValue+data.get("userId").toString()+",";
				}
				workid=data.get("workId").toString();
				stepid=	Integer.parseInt(data.get("stepID").toString());
				condm=Integer.parseInt(data.get("con_dm").toString());
			}
			Org_pub org=new Org_pub();
			switch(Integer.parseInt(roleType)){
				case 0://部门
					ygList=org.getYhInfoList(roleValue,"bm");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
				case 1://员工
					ygList=org.getYhInfoList(roleValue,"yh");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
				case 2://岗位
					if(condm==0){
						ygList=org.getYhInfoList(roleValue,"gw");
						//判断人员选择列表个数
						ifSelectUserCount(ygList,nextStep);
					}else{
						if(condm==1){//岗位发启者直接上级
							sql=" select top 1 value from (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) l where entry_id='"+entryid+"' order by cjsj asc ";
							sql=TransSql.trans(sql);
							List<Map> _list=this.getJtN().queryForList(sql);
							for(Map map1:_list){
								ygList=org.getSjyh(String.valueOf(map1.get("value")));
								//判断人员选择列表个数
								ifSelectUserCount(ygList,nextStep);
							}
						}else if(condm==2){//岗位执行者直接上级
							ygList=org.getSjyh(username);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
						}else if(condm==4){//上一步执行者下级岗位
							ygList=org.getXjyh(username);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
					   	}else{//发启者
							sql="select value as yhid,xm as mc  from (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) a,t_org_yh b where entry_id='"+entryid+"' and type='0' and a.value=b.yhid ";
							ygList=this.getJtN().queryForList(sql);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
						}
					}
					break;
				case 3://角色
					ygList=org.getYhInfoList(roleValue,"js");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
			}
			nextStep.put("workid", workid);
			nextStep.put("taskAllocation", taskAllocation);
			nextStep.put("workcondition", workcondition);
			nextStep.put("amount", amount);
			nextStep.put("remind", remind);
			/*nextStep.put("isCuib", isCuib);*/
			nextStep.put("ygList", ygList);
		} catch (Exception e) {
			String msg=new StringBuffer("得到下一步骤配置信息表时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return nextStep;
	}
	/**
	 * 得到下一步骤配置信息(taskAllocation,workcondition,ygList)
	 * @param entryid
	 * @param nextstep
	 * @param username
	 * @return
	 */
	public Map getNextStepConfig2(int entryid,int nextstep,String username,String workid){
		List ygList=null;
		Map nextStep=new HashMap();
		try {
			wfMethod=new WorkFlowMethod();
			Map  _map=wfMethod.findNodeTaskAllocation2(nextstep,workid);
			//得到下一步骤的分配方式(0、手工 1、自动)
			String taskAllocation=String.valueOf(_map.get("taskAlltocation"));
			//0(多人)1(单人)
			String workcondition=String.valueOf(_map.get("workcondition"));
			//个数
			String amount=String.valueOf(_map.get("amount"));
			//提醒(0、提醒　1、不提醒)
			String remind=String.valueOf(_map.get("remind"));
			//催办(0、催办　1、不催办)
			/*String isCuib=String.valueOf(_map.get("isCuib"));*/
			
			String roleType="";//权限类别
			String roleValue="";//权限id
			int stepid=0;//步骤id
			int condm=0;//1(发起者上级岗位)2(上一步执行者上级岗位)3(发起者)
			//ArrayList  nodeUsers=wfMethod.findNodeUser(nextstep, entryid);
			List nodeUsers =  wfMethod.findNodeUser2(nextstep, workid);
			for(int j=0;j<nodeUsers.size();j++){
				Map data=(Map)nodeUsers.get(j);
				roleType=data.get("doType").toString();
				if(j==nodeUsers.size()-1){
					roleValue=roleValue+data.get("userId").toString();
				}else{
					roleValue=roleValue+data.get("userId").toString()+",";
				}
				workid=data.get("workId").toString();
				stepid=	Integer.parseInt(data.get("stepID").toString());
				condm=Integer.parseInt(data.get("con_dm").toString());
			}
			if(condm == 9){
				//代表某一节点的处理人
				ygList  = getYhInfo_Step(entryid,roleValue);
				nextStep.put("workid", workid);
				nextStep.put("taskAllocation", taskAllocation);
				nextStep.put("workcondition", workcondition);
				nextStep.put("amount", amount);
				nextStep.put("remind", remind);
				nextStep.put("isCuib", "0");
				nextStep.put("ygList", ygList);
				return nextStep;
			}
			if(condm == 8){
				ygList=getYhInfoList(entryid,stepid);
				if(ygList.size() >0){
					ifSelectUserCount(ygList,nextStep);
					nextStep.put("workid", workid);
					nextStep.put("taskAllocation", "1");
					nextStep.put("workcondition", workcondition);
					nextStep.put("amount", amount);
					nextStep.put("remind", remind);
					nextStep.put("isCuib", "0");
					nextStep.put("ygList", ygList);
					return nextStep;
				}else{
					condm = 0;
				}
			}
			Org_pub org=new Org_pub();
			switch(Integer.parseInt(roleType)){
				case 0://部门
					ygList=org.getYhInfoList(roleValue,"bm");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
				case 1://员工
					ygList=org.getYhInfoList(roleValue,"yh");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
				case 2://岗位
					if(condm==0){
						ygList=org.getYhInfoList(roleValue,"gw");
						//判断人员选择列表个数
						ifSelectUserCount(ygList,nextStep);
					}else{
						if(condm==1){//岗位发启者直接上级
							sql=" select top 1 value from (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) l where entry_id='"+entryid+"' order by cjsj asc ";
							sql=TransSql.trans(sql);
							List<Map> _list=this.getJtN().queryForList(sql);
							for(Map map1:_list){
								ygList=org.getSjyh(String.valueOf(map1.get("value")));
								//判断人员选择列表个数
								ifSelectUserCount(ygList,nextStep);
							}
						}else if(condm==2){//岗位执行者直接上级
							ygList=org.getSjyh(username);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
						}else if(condm==4){//上一步执行者下级岗位
							ygList=org.getXjyh(username);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
					   	}else{//发启者
							sql="select value as yhid,xm as mc  from (select * from t_oswf_work_item union select * from t_oswf_work_item_his ) a,t_org_yh b where entry_id='"+entryid+"' and type='0' and a.value=b.yhid ";
							ygList=this.getJtN().queryForList(sql);
							//判断人员选择列表个数
							ifSelectUserCount(ygList,nextStep);
						}
					}
					break;
				case 3://角色
					ygList=org.getYhInfoList(roleValue,"js");
					//判断人员选择列表个数
					ifSelectUserCount(ygList,nextStep);
					break;
			}
			nextStep.put("workid", workid);
			nextStep.put("taskAllocation", taskAllocation);
			nextStep.put("workcondition", workcondition);
			nextStep.put("amount", amount);
			nextStep.put("remind", remind);
			/*nextStep.put("isCuib", isCuib);*/
			nextStep.put("ygList", ygList);
		} catch (Exception e) {
			String msg=new StringBuffer("得到下一步骤配置信息表时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return nextStep;
	}
	/**
	 * 判断人员选择列表个数
	 * @param yhlist
	 * @param nextStep
	 */
	public void ifSelectUserCount(List yhlist,Map nextStep){
		String yhcount="-1";
		int syhcount=0;
		try {
			try{
				yhcount=SysPara.getValue("oswf_select_yh_count");
			}catch(Exception ex ){};
			if(!"-1".equals(yhcount)){
				syhcount=yhlist.size();
				if(syhcount>Integer.parseInt(yhcount)){
					nextStep.put("select_user_count_flag", "1");
				}else{
					nextStep.put("select_user_count_flag", "0");
				}
			}else{
				nextStep.put("select_user_count_flag", "0");
			}
		} catch (Exception e) {
			nextStep.put("select_user_count_flag", "0");
		}
	}
	public boolean validateShenHe(String workid,String stepid,String entryid,String yhid,String nextStep,Map map1){
		boolean flag=true;
		StringBuffer strSql=new StringBuffer();
		try {
			wfMethod=new WorkFlowMethod();
			Map  _map=wfMethod.findNodeTaskAllocation(Integer.parseInt(stepid),Integer.parseInt(entryid));
			//0(多人)1(单人)2(并序)
			String workcondition=String.valueOf(_map.get("workcondition"));
			//个数
			String amount=String.valueOf(_map.get("amount"));
			if(workcondition.equals("0")){
				strSql.append("update t_oswf_work_item set status='0' where Entry_ID='").append(entryid).append("' and workid='").append(workid).append("' and step_id='").append(stepid).append("' and value='").append(yhid).append("'");
				this.getJtN().update(strSql.toString());
				if(amount.equals("-1")){//选择多少人全部处理后才发送
					strSql.delete(0,strSql.length());
					strSql.append("select * from t_oswf_work_item where Entry_ID='").append(entryid).append("' and  workid='").append(workid).append("' and step_id='").append(stepid).append("' and status='1'");
					List<Map> _list=this.getJtN().queryForList(strSql.toString());
					if(_list.size()!=0){
						flag=false;
					}
//					strSql.delete(0,strSql.length());
//					strSql.append("insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
//					strSql.append("select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,getdate() ");
//					strSql.append("from t_oswf_work_item where Entry_ID='").append(entryid).append("' and Step_ID='").append(stepid).append("' and Value='").append(yhid).append("'");
//					this.getJtN().update(strSql.toString());
				}else{
					strSql.delete(0,strSql.length());
					strSql.append("select * from t_oswf_work_item where Entry_ID='").append(entryid).append("' and  workid='").append(workid).append("' and step_id='").append(stepid).append("' and status='0'");
					List<Map> _list=this.getJtN().queryForList(strSql.toString());
					if(_list.size()!=(Integer.parseInt(amount))){
						flag=false;
					}
//					strSql.delete(0,strSql.length());
//					strSql.append("insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
//					strSql.append("select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,getdate() ");
//					strSql.append("from t_oswf_work_item where Entry_ID='").append(entryid).append("' and Step_ID='").append(stepid).append("' and Value='").append(yhid).append("'");
//					this.getJtN().update(strSql.toString());
				}
				strSql.delete(0,strSql.length());
				strSql.append("insert into t_oswf_work_item_his(zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
				strSql.append("select zzid,bmid,gwid,Id,workid,Entry_ID,Step_ID,Type,fqz,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,").append(getDateStr());
				strSql.append("from t_oswf_work_item where Entry_ID='").append(entryid).append("' and Step_ID='").append(stepid).append("' and Value='").append(yhid).append("'");
				this.getJtN().update(strSql.toString());
				
			}else if(workcondition.equals("2")){//并序
				if(amount.equals("-2")){//开始
					strSql.delete(0, strSql.length());
					strSql.append("select * from t_oswf_work_item where ENTRY_ID=? and STEP_ID=? ");
					List list1=this.getJtN().queryForList(strSql.toString(),new Object[]{entryid,stepid});
					if(list1.size()!=1){//不执行流程,将待办表数据手工插入至历史表
						//分配待办
						rgIssueTask(Integer.parseInt(entryid),Integer.parseInt(nextStep),map1);
						strSql.delete(0, strSql.length());
						strSql.append("insert into t_oswf_work_item (Id,workid,Entry_ID,Step_ID,type,value,status,trust,formname,doc_id,mk_dm,mkurllb,remind_id,cuib_id,cjsj) "); 
						strSql.append(" select id,workid,businessid,stepid,'1',userId,'1','1','").append(map.get("sdcncsi_ict_gwbt")).append("','").append(map.get("lclb_dm")).append("','").append(map.get("mk_dm")).append("','");
						strSql.append(map.get("mkurllb")).append("',remind_id,cuib_id, ").append(getDateStr());
						strSql.append(" from t_oswf_work_item_role ");
						strSql.append(" where businessId=? and stepID=? ");
						this.getJtN().update(strSql.toString(),new Object[]{entryid,stepid});
						//将现有待办信息转至历史表，并将待办信息删除
						strSql.delete(0, strSql.length());
						strSql.append("insert into t_oswf_work_item_his(Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id,cjsj) ");
						strSql.append(" select Id,workid,Entry_ID,Step_ID,Type,Value,status,trust,formname,doc_id,lclb_dm,mk_dm,mkurllb,remind_id,cuib_id, ").append(getDateStr());
						strSql.append(" from t_oswf_work_item where Entry_ID=? and Step_ID=? and Value=? ");
						this.getJtN().update(strSql.toString(),new Object[]{entryid,stepid,yhid});
						strSql.delete(0, strSql.length());
						strSql.append(" delete from t_oswf_work_item where Entry_ID=? and Step_ID=? and Value=? ");
						this.getJtN().update(strSql.toString(),new Object[]{entryid,stepid,yhid});
						flag=false;
					}else{
						
					}
				}
				else if(amount.equals("-4")){//普通

				}
				else if(amount.equals("-3")){//结束
					
				}
			}
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("validateShenHe");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("验证单人审核，还是多人审核时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		
		return flag;
	}
	/**
	 * 人工分配任务(分支聚合)
	 * @param userIds
	 * @param entryid
	 * @param nextStep
	 * @param _map
	 * @return
	 */
	public boolean rgIssueTask(String userIds, int entryid,int nextStep,Map _map){
		boolean flag=false;
		String workid="";
		String txFlag=String.valueOf(_map.get("txFlag"+nextStep));//提醒标记
		String cbFlag=String.valueOf(_map.get("cbFlag"+nextStep));//催办标记
		String txbt="";//提醒标题
		String txnr="";//提醒内容
		String cbbt="";//催办标题
		String cbnr="";//催办内容
		String ywid_tx="";//提醒业务id
		String ywid_cb="";//催办业务id
		String fsfw="";//提醒人员编码
		//QuartzApi qa=null;
		StringBuffer strSql=null;
		String txsj="";//催办时间
		String strExpress="";
		String formname="";
		String fqz="";
		try {
			wfMethod=new WorkFlowMethod();
			//提醒
			if(txFlag.equals("0") || txFlag.equals("-1")){
				ywid_tx=Guid.get();
			}
			//催办
			if(cbFlag.equals("0")){
				ywid_cb=Guid.get();
			}
			//得到流程ID
			sql="select workid from t_oswf_busi_relation where businessId='"+entryid+"'";
			List<Map> list4=this.getJtN().queryForList(sql);
			for(Map map1:list4){
				workid=String.valueOf(map1.get("workid"));
			}
			//向待办临时表插入待办信息
			String[] userList=userIds.split("~");
			for(int i=0;i<userList.length;i++){
				fsfw=fsfw+"yh"+userList[i]+",";//发送人员列表
				wfMethod.insertNodeUser(workid,nextStep, entryid, userList[i],ywid_tx,ywid_cb);
			}

			//手工提醒
			if(txFlag.equals("0")){
				txbt=String.valueOf(_map.get("txbt"+nextStep));
				txnr=String.valueOf(_map.get("txnr"+nextStep));
			}
			//自动提醒
			if(txFlag.equals("-1")){
				txnr=SysPara.getValue("oswf_tx_nr");
				strSql=new StringBuffer();
				strExpress="case when a.formname is null or a.formname=''  then  c.doc_name ";
				strExpress=strExpress+" else a.formname end as formname ";
				strSql.append("select distinct ").append(strExpress).append(",fqz from t_oswf_work_item a,t_document_type c where a.entry_id=? and a.doc_id=c.id ");
				List<Map> list=this.getJtN().queryForList(strSql.toString(),new Object[]{entryid});
				for(Map map10:list){
					formname=(String)map10.get("formname");
					fqz=(String)map10.get("fqz");
				}
				txnr=txnr.replaceAll("title", formname);
				txnr=txnr.replaceAll("fqz", fqz);
				//Remind.smsTx(txnr, fsfw,yhid);
			}
			//催办
			if(cbFlag.equals("0")){
				cbbt=String.valueOf(_map.get("cbbt"+nextStep));
				cbnr=String.valueOf(_map.get("cbnr"+nextStep));
				strSql=new StringBuffer();
				String dbtype = SysPara.getValue("db_type");
				if (dbtype.equals("mysql")) {
					strSql.append(" select date_add(sysdate(),interval cuibTime hour) as sendTime ");
				}else if (dbtype.equals("sqlserver")) {
					strSql.append(" select DATEADD(hour,cast(cuibTime as int),").append(getDateStr()).append(") as sendTime ");
				}else if (dbtype.equals("oracle")) {
					strSql.append(" select to_char(sysdate+cuibTime/24,'yyyy-mm-dd HH24:MI:SS') as sendTime ");
				}
				strSql.append(" from t_oswf_nodecondition_config ");
				strSql.append(" where stepid='").append(nextStep).append("' and workid in( ");
				strSql.append(" select workid ");
				strSql.append(" from t_oswf_busi_relation ");
				strSql.append(" where businessId='").append(entryid).append("') ");
				List<Map> list5=this.getJtN().queryForList(strSql.toString());
				for(Map map:list5){
					txsj=String.valueOf(map.get("sendTime"));
				}
				//remind.cuiban(_map, cbbt, cbnr, ywid_cb, fsfw, txsj,nextStep+"");
			}
			flag=true;
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("rgIssueTask");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("人工分配任务时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return true;
	}
	/**
	 * 人工分配任务(步骤)
	 * @param userid
	 * @return
	 */
	public boolean rgIssueTask(int entryid,int nextStep,Map _map){
		boolean flag=false;
		String workid="";
		String txFlag=String.valueOf(_map.get("txflag"));//提醒标记
		String cbFlag=String.valueOf(_map.get("cbflag"));//催办标记
		String txbt="";//提醒标题
		String txnr="";//提醒内容
		String cbbt="";//催办标题
		String cbnr="";//催办内容
		String ywid_tx="";//提醒业务id
		String ywid_cb="";//催办业务id
		String fsfw="";//提醒人员编码
		//QuartzApi qa=null;
		String isShowYh="0";//String.valueOf(_map.get("isshowyh"));//是否显示用户列表(0、显示　1、不显示)
		StringBuffer strSql=null;
		String txsj="";//催办时间
		String subflag="";//子流程标记
		String strExpress="";
		String formname="";
		String fqz="";
		try {
			wfMethod=new WorkFlowMethod();
			subflag=(String)_map.get("subflag");
			//提醒
			if(txFlag.equals("0") || txFlag.equals("-1")){
				ywid_tx=Guid.get();
			}
			//催办
			if(cbFlag.equals("0")){
				ywid_cb=Guid.get();
			}
			//用户列表
			if("7811".equals(subflag)){
				String userIds=String.valueOf(_map.get("userIds"));
				//向待办临时表插入待办信息
				String[] userList=userIds.split("~");
				for(int i=0;i<userList.length;i++){
					fsfw=fsfw+"yh"+userList[i]+",";//发送人员列表
				}
				wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(entryid + ""), "",ywid_tx,ywid_cb);
			}else{
				if(isShowYh.equals("0")){
					String userIds=String.valueOf(_map.get("userIds"));
					//得到流程ID
					sql="select workid from t_oswf_busi_relation where businessId='"+entryid+"'";
					List<Map> list4=this.getJtN().queryForList(sql);
					for(Map map1:list4){
						workid=String.valueOf(map1.get("workid"));
					}
					//向待办临时表插入待办信息
					String[] userList=userIds.split("~");
					for(int i=0;i<userList.length;i++){
						fsfw=fsfw+"yh"+userList[i]+",";//发送人员列表
						wfMethod.insertNodeUser(workid,nextStep, entryid, userList[i],ywid_tx,ywid_cb);
					}
				}else{
					String username=String.valueOf(_map.get("username"));
					Map nextStepConfig=getNextStepConfig(Integer.parseInt(entryid+""),nextStep,username);
					List ygList=(List)nextStepConfig.get("ygList");
					for(int i=0;i<ygList.size();i++){
						Map mapyg=(Map)ygList.get(i);
						String userid=String.valueOf(mapyg.get("yhid"));
						fsfw=fsfw+"yh"+userid+",";//发送人员列表
						wfMethod.insertNodeUser(workid,nextStep, Integer.parseInt(entryid + ""), userid,ywid_tx,ywid_cb);
					}
				}
			}
			flag=true;
		} catch (Exception e) {
			String msg=new StringBuffer("人工分配任务时出现异常:").append(e.toString()).toString();
			//log.error(msg);
		}
		return flag;
	}
	/**
	 * 回退提醒
	 * @param userid
	 * @return
	 */
	public boolean sendHuitTx(String entryid,String stepId,Map _map){
		boolean flag=false;
		String txbt="";//提醒标题
		String txnr="";//提醒内容
		String ywid_tx="";//提醒业务id
		String fsfw="";//提醒人员编码
		//QuartzApi qa=null;
		StringBuffer strSql=null;
		String sql="";
		ArrayList previous=new ArrayList();
		try {
			wfMethod=new WorkFlowMethod();
			sql="select ID from os_currentstep where ENTRY_ID="+entryid+" and STEP_ID="+stepId+"";
		    List<Map> list=this.getJtN().queryForList(sql);
		    int currentId=0;
			for(Map map:list){
				currentId=Integer.parseInt(String.valueOf(map.get("ID")));
			}
			sql="select PREVIOUS_ID from os_currentstep_prev where ID="+currentId;
			List<Map> list1=this.getJtN().queryForList(sql);
			for(Map map:list1){
				previous.add(Integer.parseInt(String.valueOf(map.get("PREVIOUS_ID"))));
			}
			for(int i=0;i<previous.size();i++){
				sql="select CALLER,STEP_ID from os_historystep where ID="+previous.get(i);
				List<Map> list3=this.getJtN().queryForList(sql);
				String oldStepId="";
				for(Map map:list3){
					oldStepId=String.valueOf(map.get("STEP_ID"));
				}
				sql=" select Value ";
				sql=sql+" from t_oswf_work_item_his ";
				sql=sql+" where Entry_ID='"+entryid+"' and Step_ID='"+oldStepId+"'";
				List<Map> list4=this.getJtN().queryForList(sql);
				for(Map map:list4){
					fsfw=fsfw+"yh"+(String)map.get("Value")+",";
				}
			}
			
			//提醒
			ywid_tx=Guid.get();
			//提醒
			txbt=String.valueOf(_map.get("txbt"));
			txnr=String.valueOf(_map.get("txnr"));
			
			flag=true;
		} catch (Exception e) {
			// TODO: handle exception
			String guid = Guid.get();
			logItem.setMethod("sendHuitTx(entryid,nextStep,map)");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("回退提醒时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return flag;
	}
	/**
	 * 显示流程实例用户列表
	 * @return
	 */
	public Map showUserList(){
		String username=yhid;
		try{
			map.put("isShowYh", Cache.getUserInfo(username, "isShowYh"));
			map.put("ygList", Cache.getUserInfo(username, "ygList"));
			map.put("workcondition", Cache.getUserInfo(username, "workcondition"));
			map.put("amount", Cache.getUserInfo(username, "amount"));
			map.put("remind", Cache.getUserInfo(username, "remind"));
			map.put("isCuib", Cache.getUserInfo(username, "isCuib"));
			map.put("select_user_count_flag", Cache.getUserInfo(username, "select_user_count_flag"));//人员选择人数
			map.put("nextStep", Cache.getUserInfo(username, "nextStep"));//下一步骤id
			map.putAll((Map)Cache.getUserInfo(username, "_map"));
		}catch(Exception e){
			String msg=new StringBuffer("显示流程实例用户列表时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
		return map;
	}
	/**
	 * 查看流程实例
	 * @param reponse
	 */
	public Map showWfInstanceInfo() {
		String username=yhid;
		//待办业务id
		String businessid=String.valueOf(map.get("id"));
		long entryId=Long.parseLong(String.valueOf(map.get("entryid")));
		String workid="";
		String stepid="";
		String wkName="";
		String lclb_dm="";
		String dburl="";
		String mk_dm="";
		Map _map=null;
		StringBuffer strSql=new StringBuffer();
		ArrayList arry=new ArrayList();
		ArrayList parry=new ArrayList();
		String zwbz="";//正文标记
		String fjbz="";//附件标记
		String lybz="";//留言标记
		String pzwbz="0";//父正文标记
		String useparentbz="0";
		String parentid="";//父流程实例id
		String pstepid="";
		String pworkid="";
		try{		

			strSql.delete(0, strSql.length());
			strSql.append(" select a.workid,a.mk_dm,a.step_id from t_oswf_work_item_his where id = '")
				  .append(businessid).append("' ");
			List<Map> list1=this.getJtN().queryForList(strSql.toString());
			for(Map map:list1){
				workid=String.valueOf(map.get("workid"));
				stepid = String.valueOf(map.get("step_id"));
				mk_dm=String.valueOf(map.get("mk_dm"));
				dburl=String.valueOf(map.get("dburl"));//url(iframe)
			}
			Workflow wf = new BasicWorkflow(username);
			wkName=wf.getWorkflowName(entryId);
			wfMethod=new WorkFlowMethod();
		    String templateName=wfMethod.findTemplateById(workid);
		    //表单id
		    String formId=wfMethod.findFormIdByBusiness((int)entryId);
		    //功能列表
		    map.put("id", entryId);
		    map.put("stepId", stepid);
		    map.put("username", yhid);
		    map.put("templateName", templateName);//模板名称
		    map.put("formId", formId);//表单id
		    map.put("wkName", wkName);
			map.put("mk_dm", mk_dm);//模块代码
			map.put("businessid", businessid);//业务id
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("查看流程实例：[").append(entryId).append("]时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}	
		return map;
	}
	/**
	 * 查看流程实例
	 * @param reponse
	 */
	public Map queryHis() {
		String id="";
		StringBuffer sqlStr = new StringBuffer();
		try{
			id = map.get("id")==null?"":String.valueOf(map.get("id"));
			sqlStr.append("select * from(");
			sqlStr.append("select guid,xm clr,(stepname+'->'+actionname+'->'+nextstepname)dz,cjsj dzsj,0 xh from t_oswf_historystep where entryid=").append(id);
			sqlStr.append(" union all ");
			sqlStr.append("select id,(select xm from t_org_yh where yhid = a.value) clr,'待处理',cjsj dzsj,1 xh from t_oswf_work_item a where Entry_ID=").append(id);
			sqlStr.append(") a order by xh,dzsj asc");
			List list = getJtN().queryForList(sqlStr.toString());
			PageBean page = new PageBean();
			page.setPage_allCount(10000);
			page.setPageSize("10000");
			jjd.setGrid("datalist", list, page);
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("查看流程实例：[").append(id).append("]历史时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}	
		return jjd.getData();
	}
	//查询时间轴数据
	public Map queryTimeLine() {
		String id="";
		StringBuffer sqlStr = new StringBuffer();
		try{
			id = map.get("id")==null?"":String.valueOf(map.get("id"));
			sqlStr.append("select (select stepname from t_oswf_node_info where stepid = a.STEP_ID) stepname, a.STEP_ID stepid,")
			      .append("convert(varchar(100),START_DATE,120) starttime,convert(varchar(100),FINISH_DATE,120) endtime,")
			      .append("(convert(varchar(20),datediff(day,START_DATE,FINISH_DATE))+'天'+convert(varchar(20),datediff(HOUR,START_DATE,FINISH_DATE))+'小时'+convert(varchar(20),datediff(MINUTE,START_DATE,FINISH_DATE))+'分钟') sjjg,")
			      .append("(select XM from t_org_yh where yhid = a.CALLER) clr ")
			      .append("from OS_HISTORYSTEP a where ENTRY_ID =").append(id)
			      .append(" union all ")
			      .append("select (select stepname from t_oswf_node_info where stepid = a.STEP_ID) stepname,a.STEP_ID stepid,")
			      .append("START_DATE starttime,'未处理' endtime,'' sjjg,'' clr from OS_CURRENTSTEP a where ENTRY_ID=").append(id);
			List list = getJtN().queryForList(sqlStr.toString());
			jjd.setExtend("list",list);
		}catch(Exception e){
			e.printStackTrace();
			String msg=new StringBuffer("查看流程实例：[").append(id).append("]时间轴异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}	
		return jjd.getData();
	}
	//获取步骤历史处理人员
	public List getYhInfoList(int entryid,int stepid){
		List _list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select lxid as yhid,mc,py from t_org_tree a,(select value from t_oswf_work_item_his where Entry_ID= ")
		   .append(entryid).append(" and Step_ID = ").append(stepid).append(") b where a.lxid = b.value ");
		_list = this.getJtN().queryForList(sql.toString());
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = _list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}

		_list.clear();
		_list.addAll(newList);
		return _list;
	
	}
	public List getYhInfo_Step(int entryid,String stepid){
		List _list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select lxid as yhid,mc,py from t_org_tree a,(select value from t_oswf_work_item_his where Entry_ID= ")
		   .append(entryid).append(" and Step_ID = ").append(stepid).append(") b where a.lxid = b.value ");
		_list = this.getJtN().queryForList(sql.toString());
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = _list.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}

		_list.clear();
		_list.addAll(newList);
		return _list;
		
	}
}

package name.xio.xiorkflow.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import name.xio.xiorkflow.domain.Process;
import name.xio.xiorkflow.domain.ProcessResult;
import name.xio.xiorkflow.domain.logic.ProcessService;
import name.xio.xml.SimpleXMLWorkShop;

import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.coffice.workflow.condition.table.date.NodepropertconditionData;
import com.coffice.workflow.condition.table.date.NodepropertyuserData;
import com.coffice.workflow.condition.table.date.config_nodeformData;
import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.workflow.util.NodeConfigManager;
import com.coffice.workflow.util.Transform;
import com.coffice.util.BaseUtil;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;


public class ProcessBusiBean extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	String yhid;

	Map map;
	
	 WorkFlowMethod wfMethod=null;
	
	public ProcessBusiBean(Map mapIn){
		jjd = new JspJsonData();
		logItem = new LogItem();
		yhid = (String) mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(this.getClass().getName());
		this.map = mapIn;
	}
	/**
	 * 保存流程
	 * @param map
	 * @return
	 */
	public Map saveProcess(){
        String userName="yu";
        String name=new Date().getTime()+userName;
        String nameDscribe = (String)map.get("name");
        nameDscribe=nameDscribe.substring(0,nameDscribe.indexOf("undefined"));
        String xml = (String)map.get("xml");
        String joinxml = (String)map.get("join");
        String functionxml = (String)map.get("functions");
        String formConfigxml = (String)map.get("formConfig");
        String pworkId = (String)map.get("pworkId");
        String wkTypeId=(String)map.get("wkTypeId");//流程类别ID
        String length=(String)map.get("length");
        ArrayList nodeConfigs=new ArrayList();
        String configs[]=new String[Integer.parseInt(length)];//request.getParameter("xml0");
        for(int i=0;i<Integer.parseInt(length);i++){
        	configs[i]=(String)map.get("xml"+i);
        }
        ProcessResult processResult = new ProcessResult();
        Document doc;
        ApplicationContext ac = new FileSystemXmlApplicationContext(System.getProperty("xiorkflow.root")+"WEB-INF/xiorkflow-servlet.xml");
        ProcessService processService=(ProcessService)ac.getBean("processService");
        try {
        	doc = SimpleXMLWorkShop.str2Doc(xml);
        	if (doc != null) {
                Process process = new Process();
                process.setName(name);
                process.setDoc(doc);
                processResult = processService.addProcess(process);
            }
//        	response.setContentType("text/xml");
//            response.setHeader("Cache-Control", "no-cache");
//        	SimpleXMLWorkShop.outputXML(ProcessResult.convertXml(processResult),response.getOutputStream());
        	
            //转换
            Transform transform=new Transform();
            //xml文件  xml样式文件
            /**保存流程到数据库*/
            wfMethod=new  WorkFlowMethod();
            String workId="";
            if(pworkId==null||pworkId.trim().equals("")||pworkId.equals("null")){
            	//保存流程定义
//            	workId = wfMethod.insertWorkdef(name,nameDscribe,userName,wkTypeId);
            }else{
//            	workId = wfMethod.insertWorkdef(pworkId,name,nameDscribe,userName,wkTypeId); 
            }
             //保存节点配置
             NodeConfigManager manager=new NodeConfigManager();
             for(int i=0;i<configs.length;i++){
            	 manager.nodeConfig(workId,configs[i].toString());
             }
             //保存聚合节点的配置信息
             String[] joins=joinxml.split(",");
             for(int i=0;i<joins.length;i++){
            	 if(joins[i]!=null&&!joins[i].equals("")){
            		 manager.joinConfig(workId, joins[i]);
            	 }
             }
           //保存功能按钮配置信息
             String[] functions=functionxml.split(",");
             for(int i=0;i<functions.length;i++){
            	 if(functions[i]!=null&&!functions[i].equals("")){
            	   manager.functionConfig(workId, functions[i]);
            	 }
             }
             //保存表单权限设置信息
             String[] fromConfig=formConfigxml.split(",");
             for(int i=0;i<fromConfig.length;i++){
            	 if(fromConfig[i]!=null&&!fromConfig[i].equals("")){
            	   manager.formConfig(workId, fromConfig[i]);
            	 }
             }
             //往配置文件中增加流程配置
             manager.insertworkflow(name);
            //not redirect to other view,it processed on response
             //根据节点配置修改workflow配置文件
             transform.setWorkid(workId);
             transform.xmlTxml(name);
             jjd.setExtend("result", "保存流程成功");
		} catch (Exception e) {
			// 所有异常都要写详细日志
			String guid = Guid.get();
			logItem.setMethod("saveProcess");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "保存流程发生异常");
		}
		return jjd.getData();
	}
	/**
	 * 显示流程
	 *
	 */
	public void showProcess(HttpServletResponse response){
		String name = String.valueOf(map.get("name"));
        String id = String.valueOf(map.get("id"));
        String workId=String.valueOf(map.get("workId"));
		try {
			ApplicationContext ac = new FileSystemXmlApplicationContext(System.getProperty("xiorkflow.root")+"WEB-INF/xiorkflow-servlet.xml");
	        ProcessService processService=(ProcessService)ac.getBean("processService");
	        ProcessResult processResult = processService.getProcess(name);
	        Process process = processResult.getProcess();
	        
	        response.setContentType("text/xml");
	        response.setHeader("Cache-Control", "no-cache");

	        Document doc = null;
	        if (process != null) {
	            doc = process.getDoc();
	        }
	        doc = (doc == null) ? ProcessResult.convertXml(processResult) : doc;
	        Element root=doc.getRootElement();
	        
	        wfMethod=new  WorkFlowMethod();
	        //当前步骤
	        if(id!=null&&!id.equals("undefined")){
		        ArrayList list=wfMethod.getCurrentStep(id);
		        for(Iterator iterator=list.iterator();iterator.hasNext();){
		        	String step=iterator.next().toString();
		        	Element currentStep= new Element("currentStep");
		            currentStep.setAttribute(new Attribute("stepId",step));
		            root.addContent((Content)currentStep);
		        }
	        }
	        //读取执行过的所有步骤
	        if(id!=null&&!id.equals("undefined")){
		        ArrayList list=wfMethod.getSteps(id);
		        for(Iterator iterator=list.iterator();iterator.hasNext();){
		        	String step=iterator.next().toString();
		        	Element currentStep= new Element("historyStep");
		            currentStep.setAttribute(new Attribute("stepId",step));
		            root.addContent((Content)currentStep);
		        }
	        }
	        //读取节点配置文件
	        
	        String formId="";
	        //得到流程类别ID
		        String wkTypeId=wfMethod.getWkTypeId(workId);
		        Element wkType= new Element("wkType");
		        wkType.setAttribute(new Attribute("wkTypeId",wkTypeId));
	    		root.addContent((Content)wkType);
    		
	        ArrayList roleList=wfMethod.getStepConfig(workId);
	        for(Iterator iterator=roleList.iterator();iterator.hasNext();){
	        	NodepropertconditionData data=(NodepropertconditionData)iterator.next();
	        	//聚合节点
	        	if((data.workcondition==null||data.workcondition.equals(""))
	        			&&(data.taskAllocation==null||data.taskAllocation.equals(""))){
	        		//聚合节点
	        		Element joinStep= new Element("workflowJoin");
	        		joinStep.setAttribute(new Attribute("nodeId",data.stepID+""));
	        		Element jionConfig= new Element("jionconfig");
	        		jionConfig.setAttribute("value", data.formula);
	        		joinStep.addContent((Content)jionConfig);
	        		root.addContent((Content)joinStep);
	        	}else{
	        	//普通节点
	        		//表单配置
	        		ArrayList formList=wfMethod.getStepFormConfig(workId,data.stepID+"");
	        		if(formList.size()>0){
	        		Element nodeStep= new Element("stepForm");
	        		nodeStep.setAttribute(new Attribute("stepId",data.stepID+""));
	        		
	        		
	        		root.addContent((Content)nodeStep);
	        		for(Iterator formite=formList.iterator();formite.hasNext();){
	        			config_nodeformData formdata=(config_nodeformData) formite.next();
	        			Element formStep= new Element("form");
	        			formStep.setAttribute(new Attribute("formId", formdata.formId));
	        			nodeStep.addContent((Content)formStep);
	        			formId=formdata.formId;
	        			
	        			Element tdIdStep= new Element("tdId");
	        			tdIdStep.setAttribute(new Attribute("value", formdata.tdID));
	        			
	        			Element roleStep= new Element("role");
	        			roleStep.setAttribute(new Attribute("value", formdata.role));
	        			tdIdStep.addContent((Content)roleStep);
	        			
	        			nodeStep.addContent((Content)tdIdStep);
	        		}
	        		}
	        		//权限配置
	        		Element roleStep= new Element("workflowRole");
	        		roleStep.setAttribute(new Attribute("nodeId",data.stepID+""));
	        		root.addContent((Content)roleStep);
	        		
	        		Element taskStep= new Element("taskAllocation");
	        		taskStep.setAttribute(new Attribute("value",data.taskAllocation));
	        		roleStep.addContent((Content)taskStep);
	        		
	        		Element condStep=new Element("conditionType");
	        		condStep.setAttribute(new Attribute("value", data.workcondition));
	        		condStep.setAttribute(new Attribute("amount", "2"));
	        		roleStep.addContent((Content)condStep);
	        		
	        		Element backStep=new Element("backNode");
	        		backStep.setAttribute(new Attribute("value", data.isBack==null?"":data.isBack));//data.isBack));
	        		roleStep.addContent((Content)backStep);
	        		
	        		Element autoNodeStep=new Element("autoNode");
	        		autoNodeStep.setAttribute(new Attribute("value",data.autoStep==null?"":data.autoStep));//data.autoStep));
	        		if(data.autoStep.equals("1")){
	        			autoNodeStep.setAttribute(new Attribute("formula",data.formula==null?"":data.formula));
	        		}
	        		
	        		roleStep.addContent((Content)autoNodeStep);
	        		
	        		
	        		
	        		Element remindStep=new Element("remind");
	        		remindStep.setAttribute(new Attribute("value", data.remind==null?"":data.remind));// data.remind));
	        		roleStep.addContent((Content)remindStep);
	        		if(data.remind!=null&&data.remind.equals("0")){
	        			remindStep.setAttribute(new Attribute("content", data.remindcontent));
	        			remindStep.setAttribute(new Attribute("contime", data.remindTime==null?" ":data.remindTime));
	        			
	        		}
	        		//读取角色配置
	        		ArrayList roleNodeList=wfMethod.getStepUserConfig(workId,data.stepID+"");
	        		for(Iterator roleite=roleNodeList.iterator();roleite.hasNext();){
	        			NodepropertyuserData roledata=(NodepropertyuserData)roleite.next();
	        			
	        			Element role=new Element("roletype");
	        			role.setAttribute(new Attribute("type", roledata.doType));
	        			role.setAttribute(new Attribute("value", roledata.userId));
	        			roleStep.addContent((Content)role);        			
	        		}
	        		
	        		if(!formId.equals("")){
	        			
	        			Element formStep= new Element("formModel");
	        			formStep.setAttribute(new Attribute("formId",formId));
	            		root.addContent((Content)formStep);
	        		}
	        	}
	        }
	        SimpleXMLWorkShop.outputXML(doc, response.getOutputStream());
		} catch (Exception e) {
			String msg=new StringBuffer("显示流程时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
	}
	
	/**
	 * 显示流程属性
	 * @param map
	 * @return
	 */
	public Map showWkAttr(){
		wfMethod=new  WorkFlowMethod();
		try {
			//显示所有模板
		    map.put("modelList", wfMethod.getFormModelList());
		    //显示流程类别
		    map.put("wkTypeList", wfMethod.getWkTypeList());
		    
		} catch (Exception e) {
			String msg=new StringBuffer("显示流程属性时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
		return map;
	}
	
	/**
	 * 显示节点配置
	 * @param map
	 * @return
	 */
	public Map showNodeConfig(){
		wfMethod=new  WorkFlowMethod();
		try {
			//显示所有模板
		    map.put("modelList", wfMethod.getFormModelList());
		    
		} catch (Exception e) {
			String msg=new StringBuffer("显示节点配置时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
		return map;
	}
	
	/**
	 * 根据模板ID显示表单信息
	 * @return
	 */
	public Map showFormConfig(){
		wfMethod=new  WorkFlowMethod();
		String tableName=String.valueOf(map.get("formId"));
		try {
			//显示表单信息
		    map.put("modelList", wfMethod.getFormModel(tableName));
		    
		} catch (Exception e) {
			String msg=new StringBuffer("显示表单配置时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
		return map;
	}
	
	/**
	 * 显示功能列表
	 * @return
	 */
	public Map showFunctionConfig(){
		try {
			String sql="select * from t_oswf_node_function_dm where func_parent=0 ";
			List<Map> list=this.getJtA().queryForList(sql);
			jjd.setExtend("funcfirst", list);
			sql="select * from t_oswf_node_function_dm where func_parent<>0";
			List<Map> list1=this.getJtA().queryForList(sql);
			jjd.setExtend("funcsecond", list1);
		} catch (Exception e) {
			String msg=new StringBuffer("显示功能列表时出现异常:").append(e.toString()).toString();
			log.error(msg);
			jjd.setResult(false, msg);
		}
		return jjd.getData();
	}
	

}

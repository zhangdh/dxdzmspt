package name.xio.xiorkflow.web;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import name.xio.xiorkflow.domain.Process;
import name.xio.xiorkflow.domain.ProcessResult;
import name.xio.xiorkflow.domain.logic.ProcessService;
import name.xio.xml.SimpleXMLWorkShop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.coffice.workflow.condition.table.date.NodepropertconditionData;
import com.coffice.workflow.condition.table.date.NodepropertyuserData;
import com.coffice.workflow.condition.table.date.config_nodeformData;
import com.coffice.workflow.util.WorkFlowMethod;


public class GetProcess implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String workId=request.getParameter("workId");
       // workId="1";
        log.info("get process:" + name);

        ProcessResult processResult = this.getProcessService().getProcess(name);

        Process process = processResult.getProcess();

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        Document doc = null;
        if (process != null) {
            doc = process.getDoc();
        }
        doc = (doc == null) ? ProcessResult.convertXml(processResult) : doc;
        Element root=doc.getRootElement();
        
         WorkFlowMethod method=new  WorkFlowMethod();
        //当前步骤
        if(id!=null&&!id.equals("undefined")){
	        ArrayList list=method.getCurrentStep(id);
	        for(Iterator iterator=list.iterator();iterator.hasNext();){
	        	String step=iterator.next().toString();
	        	Element currentStep= new Element("currentStep");
	            currentStep.setAttribute(new Attribute("stepId",step));
	            root.addContent((Content)currentStep);
	        }
        }
        //读取执行过的所有步骤
        if(id!=null&&!id.equals("undefined")){
	        ArrayList list=method.getSteps(id);
	        for(Iterator iterator=list.iterator();iterator.hasNext();){
	        	String step=iterator.next().toString();
	        	Element currentStep= new Element("historyStep");
	            currentStep.setAttribute(new Attribute("stepId",step));
	            root.addContent((Content)currentStep);
	        }
        }
        //读取节点配置文件
        
        String formId="";
        ArrayList roleList=method.getStepConfig(workId);
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
        		ArrayList formList=method.getStepFormConfig(workId,data.stepID+"");
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
        		ArrayList roleNodeList=method.getStepUserConfig(workId,data.stepID+"");
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
//    			<workflow nodeId="1">
//    			<roletype type="2" value="role1" />
//    			<taskAllocation value="0" />
//    			<conditionType value="0" amount="" />
//    			<backNode value="0" />
//    			<autoNode value="0" />
//    			<remind value="0" content="nihao" />
//    		</workflow>
        	}
        }
        
        SimpleXMLWorkShop.outputXML(doc, response.getOutputStream());
		
        //not redirect to other view,it processed on response
        return null;
    }

    /**
     * @return Returns the processService.
     */
    public ProcessService getProcessService() {
        return processService;
    }

    /**
     * @param processService The processService to set.
     */
    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    private Log log = LogFactory.getLog(GetProcess.class);

    private ProcessService processService;
}
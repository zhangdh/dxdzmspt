// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Design.java

package com.coffice.workflow.design;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.*;
import org.springframework.jdbc.core.JdbcTemplate;

import com.coffice.workflow.condition.table.date.*;
import com.coffice.workflow.util.WorkFlowMethod;
import com.coffice.workflow.util.Filefilter;
import com.coffice.workflow.util.NodeConfigManager;
import com.coffice.util.*;

public class FlowDesign extends BaseUtil
{

	JspJsonData jjd;
	LogItem logItem;
	String zzid;
	String bmid;
	String gwid;
	String yhid;
	Map map;
	WorkFlowMethod wfMethod;

	public FlowDesign(Map mapIn)
	{
		wfMethod = null;
		jjd = new JspJsonData();
		logItem = new LogItem();
		zzid = (String)mapIn.get("zzid");
		bmid = (String)mapIn.get("bmid");
		gwid = (String)mapIn.get("gwid");
		yhid = (String)mapIn.get("yhid");
		logItem.setYhid(yhid);
		logItem.setClassName(getClass().getName());
		map = mapIn;
	}

	public Map saveProcess(HttpServletRequest request)
	{
		String realPath = request.getRealPath("/");
		String userName = yhid;
		String pworkId = (String)map.get("pworkId") != null ? (String)map.get("pworkId") : "";
		String name = (new StringBuilder(String.valueOf((new Date()).getTime()))).append(userName).toString();
		String nameDscribe = (String)map.get("name") != null ? (String)map.get("name") : "";
		String xml = (String)map.get("data") != null ? (String)map.get("data") : "";
		String joinxml = (String)map.get("join") != null ? (String)map.get("join") : "";
		String functionxml = (String)map.get("functions") != null ? (String)map.get("functions") : "";
		String formConfigxml = (String)map.get("formConfig") != null ? (String)map.get("formConfig") : "";
		String roleConfigxml = (String)map.get("roleConfig") != null ? (String)map.get("roleConfig") : "";
		String formFuncConfigxml = (String)map.get("formFuncConfig") != null ? (String)map.get("formFuncConfig") : "";
		String subConfigxml = (String)map.get("subConfig") != null ? (String)map.get("subConfig") : "";
		String wkTypeId = "0";
		String workId = "";
		String ifNewWk = "";
		if (formConfigxml.equals("undefined"))
			formConfigxml = "";
		Document doc = null;
		try
		{
			wfMethod = new WorkFlowMethod();
			if (pworkId != null && !pworkId.trim().equals("") && !pworkId.equals("null"))
			{
				ifNewWk = String.valueOf(map.get("ifNewWk"));
				if (!ifNewWk.equals("0"))
					name = wfMethod.getWkName(pworkId);
			}
			doc = DocumentHelper.parseText(xml);
			OutputFormat format = OutputFormat.createPrettyPrint();
			String flowpath = SysPara.getValue("oswf_design_xmlpath_save");
			String ospath = SysPara.getValue("oswf_xmlpath");
			OutputStream out = new FileOutputStream((new StringBuilder(String.valueOf(flowpath))).append(name).append(".xml").toString());
			Writer wr = new OutputStreamWriter(out, "UTF-8");
			XMLWriter output = new XMLWriter(wr, format);
			output.write(doc);
			output.close();
			Source xmlSource = new StreamSource((new StringBuilder(String.valueOf(flowpath))).append(name).append(".xml").toString());
			Source xsltSource = new StreamSource((new StringBuilder(String.valueOf(flowpath))).append("/default.xslt").toString());
			TransformerFactory transFact = TransformerFactory.newInstance();
			Transformer trans = transFact.newTransformer(xsltSource);
			Properties properties = trans.getOutputProperties();
			properties.setProperty("encoding", "utf-8");
			trans.setOutputProperties(properties);
			File outFile = new File((new StringBuilder(String.valueOf(ospath))).append(name).append(".xml").toString());
			trans.transform(xmlSource, new StreamResult(new FileOutputStream(outFile)));
			formatXMLFile((new StringBuilder(String.valueOf(ospath))).append(name).append(".xml").toString());
			if (pworkId == null || pworkId.trim().equals("") || pworkId.equals("null"))
				workId = wfMethod.insertWorkdef(zzid, bmid, gwid, name, nameDscribe, userName, wkTypeId);
			else
				workId = wfMethod.insertWorkdef(zzid, bmid, gwid, pworkId, name, nameDscribe, userName, wkTypeId, ifNewWk);
			NodeConfigManager manager = new NodeConfigManager();
			String configs[] = roleConfigxml.split(",");
			for (int i = 0; i < configs.length; i++)
				manager.nodeConfig(workId, configs[i].toString());

			String joins[] = joinxml.split(",");
			for (int i = 0; i < joins.length; i++)
				if (joins[i] != null && !joins[i].equals(""))
					manager.joinConfig(workId, joins[i]);

			String functions[] = functionxml.split(",");
			for (int i = 0; i < functions.length; i++)
				if (functions[i] != null && !functions[i].equals(""))
					manager.functionConfig(workId, functions[i]);

			String fromConfig[] = formConfigxml.split(",");
			for (int i = 0; i < fromConfig.length; i++)
				if (fromConfig[i] != null && !fromConfig[i].equals(""))
					manager.formConfig(workId, fromConfig[i]);

			String formFuncConfig[] = formFuncConfigxml.split(",");
			for (int i = 0; i < formFuncConfig.length; i++)
				if (formFuncConfig[i] != null && !"".equals(formFuncConfig[i]))
					manager.formFuncConfig(workId, formFuncConfig[i]);

			String subConfig[] = subConfigxml.split(",");
			for (int i = 0; i < subConfig.length; i++)
				if (subConfig[i] != null && !"".equals(subConfig[i]))
					manager.subConfig(workId, subConfig[i]);

			manager.insertworkflow(name);
			Filefilter file = new Filefilter();
			file.setWorkId(workId);
			file.filter(ospath, name);
			file.modifyHuit(realPath, ospath, name, yhid);
			jjd.setExtend("result", "保存流程成功");
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("saveProcess");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存数据时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setExtend("result", "保存流程失败");
			jjd.setResult(false, "保存流程发生异常");
		}
		return jjd.getData();
	}

	public Map showProcess()
	{
		String workid = String.valueOf(this.map.get("workid"));
		String xml = "";
		String funxml = "";
		String sql = "";
		try
		{
			jjd.setExtend("design_xmlpath", SysPara.getValue("oswf_design_xmlpath_show"));
			wfMethod = new WorkFlowMethod();
			String formId = "";
			String wkTypeId = wfMethod.getWkTypeId(workid);
			jjd.setExtend("wkType", wkTypeId);
			String wkName = wfMethod.getWkRemark(workid);
			jjd.setExtend("wkName", wkName);
			ArrayList roleList = wfMethod.getStepConfig(workid);
			for (Iterator iterator = roleList.iterator(); iterator.hasNext();)
			{
				NodepropertconditionData data = (NodepropertconditionData)iterator.next();
				if (data.buttonType != null && !"".equals(data.buttonType)&& !"null".equals(data.buttonType))
				{
					funxml = (new StringBuilder(String.valueOf(funxml))).append("<workflow nodeId=\"").append(String.valueOf(data.stepID)).append("\">").toString();
					String funcs[] = data.buttonType.split(",");
					for (int i = 0; i < funcs.length; i++)
					{
						funxml = (new StringBuilder(String.valueOf(funxml))).append("<functions value=\"").append(funcs[i]).append("\" >").toString();
						if (data.funcconfig != null && !"".equals(data.funcconfig) && !"null".equals(data.funcconfig))
						{
							String fun[] = data.funcconfig.split(",");
							sql = (new StringBuilder("select id from t_oswf_node_function_dm where func_parent='")).append(funcs[i]).append("' ").toString();
							List _list = getJtA().queryForList(sql);
							for (int j = 0; j < fun.length; j++)
							{
								if (_list.size() == 0)
									break;
								for (Iterator iterator1 = _list.iterator(); iterator1.hasNext();)
								{
									Map map = (Map)iterator1.next();
									String funId = String.valueOf(map.get("id"));
									if (fun[j].equals(funId))
									{
										funxml = (new StringBuilder(String.valueOf(funxml))).append("<function value=\"").append(fun[j]).append("\" />").toString();
										break;
									}
								}

							}

						}
						funxml = (new StringBuilder(String.valueOf(funxml))).append("</functions >").toString();
					}

					funxml = (new StringBuilder(String.valueOf(funxml))).append("</workflow>,").toString();
				}
				String nodeName = wfMethod.getNodeName(data.workId, String.valueOf(data.stepID));
				if (data.formula != null && !"".equals(data.formula) && !"null".equals(data.formula) && "0".equals(data.amount))
				{
					xml = (new StringBuilder(String.valueOf(xml))).append("<workflow nodeId=\"").append(String.valueOf(data.stepID)).append("\" nodeName=\"").append(nodeName).append("\" nodeType=\"1\">").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<jionconfig value=\"").append(data.formula).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("</workflow>,").toString();
				} else
				{
					String nodeType = wfMethod.getNodeType(data.workId, String.valueOf(data.stepID));
					xml = (new StringBuilder(String.valueOf(xml))).append("<workflow nodeId=\"").append(String.valueOf(data.stepID)).append("\" nodeName=\"").append(nodeName).append("\" nodeType=\"").append(nodeType).append(" \">").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<conditionType value=\"").append(data.workcondition).append("\" amount=\"").append(data.amount).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<taskAllocation value=\"").append(data.taskAllocation).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<backNode value=\"").append(data.isBack).append("\"/>").toString();
					if (data.autoStep.equals("0"))
						xml = (new StringBuilder(String.valueOf(xml))).append("<autoNode value=\"").append(data.autoStep).append("\"/>").toString();
					else
						xml = (new StringBuilder(String.valueOf(xml))).append("<autoNode value=\"").append(data.autoStep).append("\" formula=\"").append(data.formula).append("\"/>").toString();
					if (data.remind != null && data.remind.equals("1"))
						xml = (new StringBuilder(String.valueOf(xml))).append("<remind value=\"").append(data.remind).append("\"/>").toString();
					else
						xml = (new StringBuilder(String.valueOf(xml))).append("<remind value=\"").append(data.remind).append("\"/>").toString();
					if (data.isCuib != null && data.isCuib.equals("1"))
						xml = (new StringBuilder(String.valueOf(xml))).append("<isCuib value=\"").append(data.isCuib).append("\"/>").toString();
					else
						xml = (new StringBuilder(String.valueOf(xml))).append("<isCuib value=\"").append(data.isCuib).append("\" contime=\"").append(data.remindTime).append("\"/>").toString();
					ArrayList roleNodeList = wfMethod.getStepUserConfig(workid, (new StringBuilder(String.valueOf(data.stepID))).toString());
					for (Iterator roleite = roleNodeList.iterator(); roleite.hasNext();)
					{
						NodepropertyuserData roledata = (NodepropertyuserData)roleite.next();
						xml = (new StringBuilder(String.valueOf(xml))).append("<roletype type=\"").append(roledata.doType).append("\" value=\"").append(roledata.userId).append("\" gwrole=\"").append(roledata.conDm).append("\" />").toString();
					}

					xml = (new StringBuilder(String.valueOf(xml))).append("</workflow>,").toString();
				}
			}

			xml = xml.substring(0, xml.length() - 1);
			jjd.setExtend("nodeConfig", xml);
			funxml = funxml.substring(0, funxml.length() - 1);
			jjd.setExtend("funcConfig", funxml);
			xml = "";
			ArrayList stepList = wfMethod.getConfigStep(workid);
			for (Iterator iterator = stepList.iterator(); iterator.hasNext();)
			{
				String arry[] = (String[])iterator.next();
				xml = (new StringBuilder(String.valueOf(xml))).append("<step stepId=\"").append(String.valueOf(arry[0])).append("\">").toString();
				xml = (new StringBuilder(String.valueOf(xml))).append("<form formId=\"").append(arry[1]).append("\" />").toString();
				formId = arry[1];
				ArrayList formList = wfMethod.getStepFormConfig(workid, arry[0]);
				for (Iterator formIterator = formList.iterator(); formIterator.hasNext();)
				{
					config_nodeformData data = (config_nodeformData)formIterator.next();
					xml = (new StringBuilder(String.valueOf(xml))).append("<tdId value=\"").append(data.tdID).append("\">").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<role value=\"").append(data.role).append("\" check=\"").append(data.ischeck).append("\"/></tdId>").toString();
				}

				xml = (new StringBuilder(String.valueOf(xml))).append("</step>,").toString();
			}

			xml = xml.substring(0, xml.length() - 1);
			jjd.setExtend("formConfig", xml);
			jjd.setExtend("formModel", formId);
			xml = "";
			List formFuncList = wfMethod.getConfigFormFunc(workid);
			for (int i = 0; i < formFuncList.size(); i++)
			{
				Map _map = (Map)formFuncList.get(i);
				xml = (new StringBuilder(String.valueOf(xml))).append("<workflow nodeId=\"").append(String.valueOf(_map.get("stepid"))).append("\" >").toString();
				xml = (new StringBuilder(String.valueOf(xml))).append("<func value=\"").append(String.valueOf(_map.get("funcname"))).append("\"/>").toString();
				xml = (new StringBuilder(String.valueOf(xml))).append("</workflow>,").toString();
			}

			jjd.setExtend("formFuncConfig", xml);
			xml = "";
			if (SysPara.compareValue("oswf_usesubwf", "1"))
			{
				List subList = wfMethod.getConfigSub(workid);
				for (int i = 0; i < subList.size(); i++)
				{
					Map _map = (Map)subList.get(i);
					xml = (new StringBuilder(String.valueOf(xml))).append("<workflow nodeId=\"").append(String.valueOf(_map.get("stepid"))).append("\" >").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<subbz value=\"").append(String.valueOf(_map.get("subbz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<docid value=\"").append(String.valueOf(_map.get("doc_id"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<formbz value=\"").append(String.valueOf(_map.get("formbz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<zwbz value=\"").append(String.valueOf(_map.get("zwbz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<fjbz value=\"").append(String.valueOf(_map.get("fjbz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<lybz value=\"").append(String.valueOf(_map.get("lybz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("<lcjsbz value=\"").append(String.valueOf(_map.get("lcjsbz_dm"))).append("\"/>").toString();
					xml = (new StringBuilder(String.valueOf(xml))).append("</workflow>,").toString();
				}

			}
			jjd.setExtend("subConfig", xml);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("showProcess");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示流程时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public Map viewProcess()
	{
		String entryid = String.valueOf(map.get("entryid"));
		String steps = "";
		try
		{
			jjd.setExtend("design_xmlpath", SysPara.getValue("oswf_design_xmlpath_show"));
			wfMethod = new WorkFlowMethod();
			if (!entryid.equals("null"))
			{
				ArrayList list = wfMethod.getSteps(entryid);
				for (Iterator iterator = list.iterator(); iterator.hasNext();)
				{
					String step = iterator.next().toString();
					steps = (new StringBuilder(String.valueOf(steps))).append(step).append(",").toString();
				}

			}
			jjd.setExtend("steps", steps);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("viewProcess");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("浏览流程时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public int formatXMLFile(String filename)
	{
		int returnValue = 0;
		Document document = null;
		try
		{
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(filename));
			XMLWriter output = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			output = new XMLWriter(new FileOutputStream(new File(filename)), format);
			output.write(document);
			output.close();
			returnValue = 1;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return returnValue;
	}

	public Map showWkAttr()
	{
		wfMethod = new WorkFlowMethod();
		try
		{
			if ("admin".equals(yhid) || "sys".equals(yhid) )
				map.put("modelList", wfMethod.getFormModelList());
			else
				map.put("modelList", wfMethod.getFormModelList(zzid));
			map.put("wkTypeList", wfMethod.getWkTypeList());
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("showWkAttr");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("显示流程属性时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}
}

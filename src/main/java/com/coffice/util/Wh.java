// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Wh.java

package com.coffice.util;

import java.io.PrintWriter;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;

import com.coffice.util.*;
import com.coffice.util.cache.Cache;



public class Wh extends BaseUtil
{


	JspJsonData jjd;
	LogItem logItem;
	Map map;

	public Wh()
	{
		logItem = new LogItem();
		logItem.setClassName(getClass().getName());
	}

	public Wh(Map mapIn)
	{
		jjd = new JspJsonData();
		logItem = new LogItem();
		logItem.setClassName(getClass().getName());
		map = mapIn;
	}

	public Map login(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String n = String.valueOf((int)(Math.random() * 90000D + 10000D));
			printLoginJsp(request, response, n, "");
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.login");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("登陆时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return null;
	}

	public void printLoginJsp(HttpServletRequest request, HttpServletResponse response, String n, String error)
		throws Exception
	{
		String str = "";
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		str = (new StringBuilder(String.valueOf(str))).append("<html>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<head>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<title>用户登陆</title>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script src=\"").append(request.getContextPath()).append("/js/jquery-1.3.1.js?t=20090219\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script src=\"").append(request.getContextPath()).append("/js/jquery.tablehover.js?t=20090220\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/jquery.checkboxes.js?t=20090220\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/ict.js?t=20090221\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/validate.js?t=20090111\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <link href=\"").append(request.getContextPath()).append("/css/1/style.css\" type=\"text/css\" id=\"css_id\" rel=\"stylesheet\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script src=\"").append(request.getContextPath()).append("/wh/show.js\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script type=\"text/javascript\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tvar sys_ctx=\"").append(request.getContextPath()).append("\";\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</head>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("$(function(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" if(\"").append(error).append("\"!=\"\"){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("   alert(\"").append(error).append("\");\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("function login(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" if (!Validate.CheckForm($('#formid')[0])) return;//数据校验\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("  document.formid.submit();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<body>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" height=\"100%\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("  <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <td valign=\"middle\" align=\"center\"><form id=\"formid\" name=\"formid\" action=\"").append(request.getContextPath()).append("/wh/default.do?method=validate\" method=\"post\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        <table width=\"652\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("          <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            <td height=\"302\" valign=\"top\" background=\"").append(request.getContextPath()).append("/css/1/oa_login_bg1.gif\"><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                  <td height=\"139\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                  <td height=\"153\" valign=\"top\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td width=\"350\" height=\"30\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td width=\"63\" height=\"30\">随机码：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td colspan=\"2\"><input type=\"text\" name=\"randomcode\" id=\"randomcode\"  size=\"15\" maxlength=\"15\" style=\"width:50%;ime-mode:disabled\" readonly value=\"").append(n).append("\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td height=\"30\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td height=\"28\">密码：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td colspan=\"2\"><input type=\"password\" name=\"mm\" id=\"mm\" value=\"\" size=\"15\" maxlength=\"15\" required=\"true\" showName=\"密码\" onkeydown=\"if(event.keyCode == 13) login();\"  style=\"width:50%;ime-mode:disabled\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        </td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td colspan=\"4\" headers=\"5\" height=\"10\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td height=\"29\">&nbsp;</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td height=\"29\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td height=\"29\" colspan=\"2\"><img id=\"bg_2\" src=\"").append(request.getContextPath()).append("/css/1/oa_loggdl.gif\" onClick=\"login();\" style=\"cursor:hand\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                       </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                        <td colspan=\"4\" headers=\"5\" height=\"5\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                      </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                    </table></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("              </table></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("          </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("          <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            <td valign=\"top\"><img src=\"").append(request.getContextPath()).append("/css/1/oa_login_bg2.gif\" width=\"652\" height=\"28\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("          </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        </table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("      </form>      \n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("  </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</body>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</html>").toString();
		pw.print(str);
	}

	public Map validate(HttpServletRequest request, HttpServletResponse response)
	{
		String randomcode = (String)map.get("randomcode");
		String pass = (String)map.get("mm");
		StringBuffer strSql = new StringBuffer();
		String sys_wh = "";
		try
		{
			Des des = new Des();
			if (randomcode != null)
			{
				String makePass = des.getEncString(randomcode).substring(0, 5);
				if (pass.equals(makePass))
				{
					strSql.append("select * from t_sys_para where csdm='sys_wh' ");
					List list = getJtN().queryForList(strSql.toString());
					if (list.size() != 0)
					{
						for (Iterator iterator = list.iterator(); iterator.hasNext();)
						{
							Map _map = (Map)iterator.next();
							if ((String)_map.get("csz") != null && !"".equals((String)_map.get("csz")))
							{
								sys_wh = des.getDesString((String)_map.get("csz"));
								String cszs[] = sys_wh.split(":");
								map.put("org_flthm", cszs[0]);
								if ("-1".equals(cszs[1]))
									map.put("org_sjhmsl", "");
								else
									map.put("org_sjhmsl", cszs[1]);
								map.put("wap_flthm", cszs[2]);
								map.put("sms_sendflthm", cszs[3]);
								map.put("sms_inputjhrhm", cszs[4]);
							}
						}

					}
					map.put("result", "1");
					printShowJsp(request, response, map);
				} else
				{
					map.put("result", "0");
					map.put("error", "密码错误，请重新输入！");
					printLoginJsp(request, response, randomcode, "密码错误，请重新输入！");
				}
			} else
			{
				map.put("result", "0");
				map.put("error", "非法请求！");
				printLoginJsp(request, response, randomcode, "非法请求！");
			}
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.validate");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("维护页面登陆校验时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}

	public void printShowJsp(HttpServletRequest request, HttpServletResponse response, Map _map)
		throws Exception
	{
		String str = "";
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		String org_flthm = (String)_map.get("org_flthm") == null ? "" : (String)_map.get("org_flthm");
		String org_sjhmsl = (String)_map.get("org_sjhmsl") == null ? "" : (String)_map.get("org_sjhmsl");
		String wap_flthm = (String)_map.get("wap_flthm") == null ? "" : (String)_map.get("wap_flthm");
		String sms_sendflthm = (String)_map.get("sms_sendflthm") == null ? "" : (String)_map.get("sms_sendflthm");
		String sms_inputjhrhm = (String)_map.get("sms_inputjhrhm") == null ? "" : (String)_map.get("sms_inputjhrhm");
		str = (new StringBuilder(String.valueOf(str))).append("<html>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<head>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <title>维护管理</title>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script src=\"").append(request.getContextPath()).append("/js/jquery-1.3.1.js?t=20090219\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script src=\"").append(request.getContextPath()).append("/js/jquery.tablehover.js?t=20090220\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/jquery.checkboxes.js?t=20090220\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/ict.js?t=20090221\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/validate.js?t=20090111\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script src=\"").append(request.getContextPath()).append("/js/datadumper.js?t=2009011\" type=\"text/javascript\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<script type=\"text/javascript\" src=\"").append(request.getContextPath()).append("/js/datePicker/WdatePicker.js\"></script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <link href=\"").append(request.getContextPath()).append("/css/1/style.css\" type=\"text/css\" id=\"css_id\" rel=\"stylesheet\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <script type=\"text/javascript\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tvar sys_ctx=\"").append(request.getContextPath()).append("\";\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</head>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("sys_btn_auth = \"btn_saveYh\";\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("$(function() {\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tsys_showButton(\"\");\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tif('").append(org_flthm).append("'=='1'){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t$('input[type=checkbox][name=org_flthm]').get(0).checked = true;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tif('").append(wap_flthm).append("'=='1'){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t$('input[type=checkbox][name=wap_flthm]').get(0).checked = true;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" var phones='';\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" phones='").append(sms_sendflthm).append("';\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" if(phones!=''){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" var phoneList=phones.split('~');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" var obj=document.getElementsByName('sms_sendflthm');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" for(j=0;j<obj.length;j++){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("  for(k=0;k<phoneList.length;k++){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("   if(obj[j].value==phoneList[k]){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    $('input[type=checkbox][name=sms_sendflthm]').get(j).checked = true;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("   }\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("  }\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" }\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append(" }\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tif('").append(sms_inputjhrhm).append("'=='1'){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t$('input[type=checkbox][name=sms_inputjhrhm]').get(0).checked = true;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t//搜索\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t$(\"#btn_queren\").click(function(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tif($(\"#c_xm\").val()=='' && $(\"#c_sj\").val()==''){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\talert('姓名或手机不允许都为空！');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\treturn false;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tvar querystr=$(\"#form_query\").serialize();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tsys_ajaxGet(\"/wh/default.do?method=query\",querystr);\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t//使用情况监控搜索\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t$(\"#btn_query\").click(function(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tvar querystr=$(\"#form_query1\").serialize();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tsys_ajaxGet(\"/wh/default.do?method=queryJk\",querystr);\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t//保存维护参数设置\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t$(\"#btn_save\").click(function(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tvar queryString=$(\"#form_show1\").serialize();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tsys_ajaxGet(\"/wh/default.do?method=save\",queryString,function(msg){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\tif(msg.result==\"1\"){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\talert('保存成功!');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t}else if(msg.result==\"0\"){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\talert('保存失败，请于系统管理员联系!');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t//保存人员信息\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t$(\"#btn_saveYh\").click(function(){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tif(!Validate.CheckForm($('#form_show')[0]))return;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tvar queryString=$(\"#form_show\").serialize();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tsys_ajaxGet(\"/wh/default.do?method=update\",queryString,function(msg){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\tif(msg.result==\"1\"){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\talert('保存成功!');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t}else if(msg.result==\"0\"){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\talert('保存失败，请于系统管理员联系!');\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tvar querystr=\"&c_xm=\"+$(\"#c_xm\").val()+\"&c_sj=\"+$(\"#c_sj\").val();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tsys_ajaxGet(\"/wh/default.do?method=query\",encodeURI(querystr));\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\tsys_showButton(\"\");\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("//分页查询回调函数\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("function callback_getPageData_table_list(pagenum){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tvar querystr=$(\"#form_query\").serialize();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tquerystr+=\"&page_goto=\"+pagenum;\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tsys_ajaxGet(\"/wh/default.do?method=query\",querystr);\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("//点击列表显示明细数据回调函数\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("function callback_trclick_table_list(guid){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tsys_ajaxGet(\"/wh/default.do?method=show\",{guid:guid},function(json){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\tbind(json);\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t});\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\tsys_showButton(\"btn_saveYh\");\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("function callback_trclick_table_list1(guid){\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("}\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<body id=\"bodyid\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_show1\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <div class=\"c_table_show_btn\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<input type=\"button\" id=\"btn_save\" name=\"btn_save\" value=\"保存\"/>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <form id=\"form_show1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<table class=\"c_table_show\" width=\"100%\" border=\"0\" >\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t            <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t            \t<td width=\"18%\" height=\"30\" align=\"right\" >组织目录：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t                <td width=\"40%\"><input type=\"checkbox\" id=\"org_flthm\" name=\"org_flthm\" value=\"1\">允许录入非联通号码</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t                <td width=\"42%\">允许录入的手机号码数量<input type=\"text\" id=\"org_sjhmsl\" name=\"org_sjhmsl\" value=\"").append(org_sjhmsl).append("\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t         \t</tr>\t\t\t\t\t\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"18%\" height=\"30\" align=\"right\">wap访问：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"82%\" colspan=\"2\"><input type=\"checkbox\" id=\"wap_flthm\" name=\"wap_flthm\" value=\"1\">允许非联通手机</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t            \t<td width=\"18%\" height=\"30\" align=\"right\" >短信模块：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t                <td width=\"40%\"><input type=\"checkbox\" id=\"sms_sendflthm\" name=\"sms_sendflthm\" value=\"0\">联通<input type=\"checkbox\" id=\"sms_sendflthm\" name=\"sms_sendflthm\" value=\"1\">移动<input type=\"checkbox\" id=\"sms_sendflthm\" name=\"sms_sendflthm\" value=\"2\">电信</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t                <td width=\"42%\"><input type=\"checkbox\" id=\"sms_inputjhrhm\" name=\"sms_inputjhrhm\" value=\"1\">允许输入短信接收人号码</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t         \t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        </table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </form>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_query\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <form id=\"form_query\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        <label>姓名：<input type=\"text\" id=\"c_xm\" name=\"c_xm\" ></label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        <label>移动电话：<input type=\"text\" id=\"c_sj\" name=\"c_sj\" ></label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<input type=\"button\" id=\"btn_queren\" value=\"搜索\" >\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </form>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_list\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <div id=\"div_table_list\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        <div >\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            <table id=\"table_list\" class=\"c_table_list\" width=\"100%\" border=\"1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <thead id=\"table_list_thead\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                    <td width=\"11%\" style=\"display:none\">yhid</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                    <td width=\"4%\">&nbsp;</td>\t\t\t\t\t \n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\" height=\"22\" align=\"center\"  class=\"lieb\">姓名</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\" height=\"22\" align=\"center\"  class=\"lieb\" >移动电话</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </thead>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tbody>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr style=\"display:none\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                    <td style=\"display:none\">{yhid}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\"><input type=\"checkbox\" name=\"table_list_checkbox\" value=\"{yhid}\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\" style=\"cursor:hand;\">{xm}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\">{sj}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tbody>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            </table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_show\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <div class=\"c_table_show_btn\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<input type=\"button\" class=\"c_btn_auth\" id=\"btn_saveYh\" name=\"btn_saveYh\" value=\"保存\"/>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <form id=\"form_show\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<input type=\"hidden\" id=\"guid\" name=\"guid\" >\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"18%\" height=\"30\" align=\"right\">姓名：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"82%\"><input class=\"biinput\" maxlength=\"130\" size=\"15\"  style=\"WIDTH: 400px; \" name=\"xm\" id=\"xm\" readonly /></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"18%\" height=\"30\" align=\"right\">移动电话：</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t  <td width=\"82%\"><input class=\"biinput\" maxlength=\"130\" size=\"15\"  style=\"WIDTH: 400px; \" name=\"sj\" id=\"sj\" required=\"true\" showName=\"移动电话\" /></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            </table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </form>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_query1\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <form id=\"form_query1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    \t<label>使用情况监控 </label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("         <label>日期：<input  type=\"text\" id=\"cx_rq1\" name=\"cx_rq1\" style=\"WIDTH: 70px; HEIGHT: 20px; \" onClick=\"WdatePicker({dateFmt:'yyyy-MM'})\"></label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("         <label>&nbsp;至&nbsp;</label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("         <label><input type=\"text\" id=\"cx_rq2\" name=\"cx_rq2\"  style=\"WIDTH: 70px; HEIGHT: 20px; \"  onClick=\"WdatePicker({dateFmt:'yyyy-MM'})\"></label>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("         <input type=\"button\" id=\"btn_query\" value=\"搜索\" >     \n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </form>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<div id=\"div_list1\" class=\"c_table_bar_content\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    <div id=\"div_table_list1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        <div >\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            <table id=\"table_list1\" class=\"c_table_list\" width=\"100%\" border=\"1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <thead id=\"table_list_thead\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                \t<td width=\"11%\" style=\"display:none\">guid</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"20%\" height=\"22\" align=\"center\"  class=\"lieb\">年/月</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"20%\" height=\"22\" align=\"center\"  class=\"lieb\" >账号数量</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"20%\" height=\"22\" align=\"center\"  class=\"lieb\" >月总登录次数</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"20%\" height=\"22\" align=\"center\"  class=\"lieb\" >月账号平均登录次数</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"20%\" height=\"22\" align=\"center\"  class=\"lieb\" >月短信发送数量</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </thead>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tbody>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                <tr style=\"display:none\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                \t<td style=\"display:none\">{guid}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\" >{ny}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\">{zhsl}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\">{yzdlcs}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\">{ypjldcs}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td align=\"center\">{ydxfsl}</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("                </tbody>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("            </table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("        </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("    </div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</div>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_show1')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">参数设置</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_show1_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_show1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_show1').html($('#div_show1').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_show1').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_query')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">查询</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_query_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_query\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_query').html($('#div_query').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_query').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_list')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">查询结果</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_list_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_list\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_list').html($('#div_list').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_list').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_show')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">数据显示</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_show_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_show\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_show').html($('#div_show').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_show').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_query1')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">查询</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_query1_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_query1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_query1').html($('#div_query1').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_query1').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr onClick=\"tableBarClick('div_list1')\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"c_table_bar\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"50%\">查询结果</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"47%\"></td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t<td width=\"3%\" id=\"sys_td_div_list1_arrow\" class=\"c_table_bar_arrow_up\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t<tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t<td id=\"sys_td_div_list1\">\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t<script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#sys_td_div_list1').html($('#div_list1').html());\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t\t$('#div_list1').empty();\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t\t</script>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t\t</td>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("\t</tr>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</table>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</body>\n").toString();
		str = (new StringBuilder(String.valueOf(str))).append("</html>\n").toString();
		pw.print(str);
	}

	public Map save()
	{
		String org_flthm = (String)map.get("org_flthm") == null ? "0" : (String)map.get("org_flthm");
		String org_sjhmsl = (String)map.get("org_sjhmsl") == null || "".equals((String)map.get("org_sjhmsl")) ? "-1" : (String)map.get("org_sjhmsl");
		String wap_flthm = (String)map.get("wap_flthm") == null ? "0" : (String)map.get("wap_flthm");
		String sms_sendflthm = (String)map.get("sms_sendflthm") == null ? "0" : (new StringBuilder("0~")).append((String)map.get("sms_sendflthm")).toString();
		String sms_inputjhrhm = (String)map.get("sms_inputjhrhm") == null ? "0" : (String)map.get("sms_inputjhrhm");
		String sys_wh = "";
		StringBuffer strSql = new StringBuffer();
		try
		{
			sys_wh = (new StringBuilder(String.valueOf(org_flthm))).append(":").append(org_sjhmsl).append(":").append(wap_flthm).append(":").append(sms_sendflthm).append(":").append(sms_inputjhrhm).toString();
			strSql.append("select * from t_sys_para where csdm='sys_wh' ");
			List list = getJtN().queryForList(strSql.toString());
			if (list.size() == 0)
			{
				strSql.delete(0, strSql.length());
				strSql.append("insert into t_sys_para (id, mk_dm, csdm, csz, cssm, yxbz, cjsj) ");
				strSql.append(" values('113','100','sys_wh',?,'系统维护参数','1',now()) ");
			} else
			{
				strSql.delete(0, strSql.length());
				strSql.append("update t_sys_para set csz=? where id='113' ");
			}
			getJtA().update(strSql.toString(), new Object[] {
				(new Des()).getEncString(sys_wh)
			});
			Map cacheMap = new HashMap();
			cacheMap.put("org_flthm", org_flthm);
			cacheMap.put("org_sjhmsl", org_sjhmsl);
			cacheMap.put("wap_flthm", wap_flthm);
			cacheMap.put("sms_sendflthm", sms_sendflthm);
			cacheMap.put("sms_inputjhrhm", sms_inputjhrhm);
			Cache.setGlobalInfo("global", "sys_wh", cacheMap);
			jjd.setExtend("result", "1");
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.save");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("保存维护参数设置时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setExtend("result", "0");
		}
		return jjd.getData();
	}

	public Map query()
	{
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlWhere = new StringBuffer();
		try
		{
			String c_xm = (String)map.get("c_xm");
			String c_sj = (String)map.get("c_sj");
			if (c_xm != null && !"".equals(c_xm))
				sqlWhere.append(" and xm='").append(c_xm).append("' ");
			if (c_sj != null && !"".equals(c_sj))
				sqlWhere.append(" and sj='").append(c_sj).append("' ");
			sqlStr.append("select yhid,dlmc,xm,sj from t_org_yh where  dlmc<>'admin'  ").append(sqlWhere.toString());
			List _list = getJtN().queryForList(sqlStr.toString());
			jjd.setGrid("table_list", _list, null);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询组织目录时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public Map show()
	{
		String guid = (String)map.get("guid");
		StringBuffer strSql = new StringBuffer();
		try
		{
			strSql.append("select yhid as guid,xm,sj from t_org_yh where yhid=?");
			Map _map = getJtN().queryForMap(strSql.toString(), new Object[] {
				guid
			});
			jjd.setForm(_map);
		}
		catch (Exception e)
		{
			String guid1 = Guid.get();
			logItem.setMethod("Wh.show");
			logItem.setLogid(guid1);
			logItem.setLevel("error");
			logItem.setDesc("显示人员信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public Map update()
	{
		String guid = (String)map.get("guid");
		StringBuffer strSql = new StringBuffer();
		try
		{
			strSql.append("update t_org_yh set sj=? where yhid=?");
			getJtN().update(strSql.toString(), new Object[] {
				map.get("sj"), guid
			});
			jjd.setExtend("result", "1");
		}
		catch (Exception e)
		{
			String guid1 = Guid.get();
			logItem.setMethod("Wh.update");
			logItem.setLogid(guid1);
			logItem.setLevel("error");
			logItem.setDesc("保存人员手机号码信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setExtend("result", "0");
		}
		return jjd.getData();
	}

	public void cacheWhInfo()
	{
		String sys_wh = "";
		StringBuffer strSql = new StringBuffer();
		Des des = null;
		Map cacheMap = new HashMap();
		try
		{
			if (Cache.getGlobalInfo("global", "sys_wh") == null)
			{
				strSql.append("select * from t_sys_para where csdm='sys_wh' ");
				List list = getJtN().queryForList(strSql.toString());
				if (list.size() != 0)
				{
					for (Iterator iterator = list.iterator(); iterator.hasNext();)
					{
						Map _map = (Map)iterator.next();
						if ((String)_map.get("csz") != null && !"".equals((String)_map.get("csz")))
						{
							des = new Des();
							sys_wh = des.getDesString((String)_map.get("csz"));
							String cszs[] = sys_wh.split(":");
							cacheMap.put("org_flthm", cszs[0]);
							cacheMap.put("org_sjhmsl", cszs[1]);
							cacheMap.put("wap_flthm", cszs[2]);
							cacheMap.put("sms_sendflthm", cszs[3]);
							cacheMap.put("sms_inputjhrhm", cszs[4]);
						} else
						{
							cacheMap.put("org_flthm", "0");
							cacheMap.put("org_sjhmsl", "-1");
							cacheMap.put("wap_flthm", "0");
							cacheMap.put("sms_sendflthm", "0");
							cacheMap.put("sms_inputjhrhm", "0");
						}
					}

				} else
				{
					cacheMap.put("org_flthm", "0");
					cacheMap.put("org_sjhmsl", "-1");
					cacheMap.put("wap_flthm", "0");
					cacheMap.put("sms_sendflthm", "0");
					cacheMap.put("sms_inputjhrhm", "0");
				}
				Cache.setGlobalInfo("global", "sys_wh", cacheMap);
			}
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.cacheWhInfo");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("将参数表里sys_wh值放入至cache时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}

	public Map checkSysWh(String key, Map smap)
	{
		Map _map = null;
		map = new HashMap();
		try
		{
			cacheWhInfo();
			_map = (Map)Cache.getGlobalInfo("global", "sys_wh");
			if (key.equals("org_flthm"))
			{
				String org_flthm = (String)_map.get(key);
				if ("1".equals(org_flthm))
				{
					map.put("tflag", "1");
				} else
				{
					String sj = (String)smap.get("sj");
					String regex = "(^1(3[0-2]|5[56]|8[56])\\d{8}$)|(^0\\d{9,11}$)";
					if (sj != null && !"".equals(sj))
					{
						if (sj.matches(regex))
						{
							map.put("tflag", "1");
						} else
						{
							map.put("tflag", "0");
							map.put("info", "非联通号码不允许录入！");
						}
					} else
					{
						map.put("tflag", "1");
					}
				}
			} else
			if (key.equals("org_sjhmsl"))
			{
				String org_sjhmsl = (String)_map.get(key);
				if ("-1".equals(org_sjhmsl))
				{
					map.put("tflag", "1");
				} else
				{
					int usercount = getJtN().queryForInt("select count(yhid) from t_org_yh where zt_dm=1");
					if (usercount < Integer.parseInt(org_sjhmsl))
					{
						map.put("tflag", "1");
					} else
					{
						map.put("tflag", "0");
						map.put("info", (new StringBuilder("系统用户数已达到注册用户数[")).append(org_sjhmsl).append("]！").toString());
					}
				}
			} else
			if (key.equals("wap_flthm"))
			{
				String wap_flthm = (String)_map.get(key);
				if ("1".equals(wap_flthm))
				{
					map.put("tflag", "1");
				} else
				{
					String phoneip = (String)smap.get("phoneip");
					if ((new IP()).isUnicom(phoneip))
					{
						map.put("tflag", "1");
					} else
					{
						map.put("tflag", "0");
						map.put("info", "非联通手机不允许访问！");
					}
				}
			} else
			if (key.equals("sms_sendflthm"))
			{
				String sms_sendflthm = (String)_map.get(key);
				if (sms_sendflthm.indexOf("1") != -1)
				{
					map.put("tflag", "1");
				} else
				{
					String sendphone = (String)smap.get("sendphone");
					String regex = "(^1(3[0-2]|5[56]|8[56])\\d{8}$)|(^0\\d{9,11}$)";
					if (sendphone.matches(regex))
						map.put("tflag", "1");
					else
						map.put("tflag", "0");
				}
			} else
			if (key.equals("sms_inputjhrhm"))
			{
				String sms_inputjhrhm = (String)_map.get(key);
				if ("1".equals(sms_inputjhrhm))
					map.put("tflag", "1");
				else
					map.put("tflag", "0");
			}
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.checkSysWh");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("各模块检查系统维护参数时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return map;
	}

	public void updateSysLog(String key, int num)
	{
		StringBuffer sqlStr = new StringBuffer();
		String ny = "";
		int n = 1;
		try
		{
			Date date = new Date();
			int year = date.getYear() + 1900;
			int month = date.getMonth() + 1;
			ny = (new StringBuilder(String.valueOf(String.valueOf(year)))).append("-").append(String.valueOf(month)).toString();
			sqlStr.append("select * from t_sys_log where ny=?");
			List list = getJtN().queryForList(sqlStr.toString(), new Object[] {
				ny
			});
			if (list.size() == 0)
			{
				sqlStr.delete(0, sqlStr.length());
				if (key.equals("login"))
					sqlStr.append("insert into t_sys_log(guid,ny,yzdlcs) values(?,?,?)");
				else
				if (key.equals("org"))
					sqlStr.append("insert into t_sys_log(guid,ny,zhsl) values(?,?,?)");
				else
				if (key.equals("sms"))
				{
					sqlStr.append("insert into t_sys_log(guid,ny,ydxfsl) values(?,?,?)");
					n = num;
				}
				getJtN().update(sqlStr.toString(), new Object[] {
					Guid.get(), ny, new Integer(n)
				});
			} else
			{
				sqlStr.delete(0, sqlStr.length());
				if (key.equals("login"))
					sqlStr.append("update t_sys_log set yzdlcs=yzdlcs+? where ny=?");
				else
				if (key.equals("org"))
					sqlStr.append("update t_sys_log set zhsl=zhsl+? where ny=?");
				else
				if (key.equals("sms"))
				{
					sqlStr.append("update t_sys_log set ydxfsl=ydxfsl+? where ny=?");
					n = num;
				}
				getJtN().update(sqlStr.toString(), new Object[] {
					new Integer(n), ny
				});
			}
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.updateSysLog");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("更新使用情况监控时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
	}

	public Map queryJk()
	{
		StringBuffer sqlStr = new StringBuffer();
		StringBuffer sqlWhere = new StringBuffer();
		try
		{
			String dbtype = SysPara.getValue("db_type");
			String cx_rq1 = (String)map.get("cx_rq1");
			String cx_rq2 = (String)map.get("cx_rq2");
			if (cx_rq1 != null && !"".equals(cx_rq1) && cx_rq2 != null && !"".equals(cx_rq2))
			{
				cx_rq1 = (new StringBuilder(String.valueOf(cx_rq1))).append("-01").toString();
				cx_rq2 = (new StringBuilder(String.valueOf(cx_rq2))).append("-01").toString();
				if (dbtype.equals("mysql"))
					sqlWhere.append(" and DATE_FORMAT(concat(ny,'-01'),'%Y-%m-%d')>='").append(cx_rq1).append("' and DATE_FORMAT(concat(ny,'-01'),'%Y-%m-%d')<=DATE_SUB(DATE_ADD('").append(cx_rq2).append("',INTERVAL 1 month),INTERVAL 1 day)");
				else
				if (dbtype.equals("sqlserver"))
					sqlWhere.append(" and cast((ny+'-01') as datetime)>='").append(cx_rq1).append("' and cast((ny+'-01') as datetime)<=dateadd(day,-1,dateadd(month,1,'").append(cx_rq2).append("'))");
				else
				if (dbtype.equals("oracle"))
					sqlWhere.append(" and to_date(ny+'-01','yyyy-mm-dd')>=to_date('").append(cx_rq1).append("','yyyy-mm-dd'").append("' and to_date(ny+'-01','yyyy-mm-dd')<=last_day(to_date('").append(cx_rq2).append("','yyyy-mm-dd'))");
			}
			sqlStr.append("select guid,ny,zhsl,yzdlcs,case when zhsl=0 then 0 else yzdlcs/zhsl end as ypjldcs,ydxfsl from t_sys_log where  1=1  ").append(sqlWhere.toString());
			List _list = getJtN().queryForList(sqlStr.toString());
			jjd.setGrid("table_list1", _list, null);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.queryJk");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询使用情况监控时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return jjd.getData();
	}

	public Map checkSmsSendPhone(String phone)
	{
		return new HashMap();
	}

	public String getSmsConfig()
	{
		Map _map = null;
		map = new HashMap();
		String sms_sendflthm = "";
		String phoneList[] = (String[])null;
		String rvalue = "";
		String sflag = "";
		try
		{
			cacheWhInfo();
			_map = (Map)Cache.getGlobalInfo("global", "sys_wh");
			sms_sendflthm = (String)_map.get("sms_sendflthm");
			phoneList = sms_sendflthm.split("\\~");
			for (int i = 0; i < phoneList.length; i++)
			{
				sflag = phoneList[i];
				if (sflag.equals("0"))
					rvalue = (new StringBuilder(String.valueOf(rvalue))).append("联通").append(",").toString();
				else
				if (sflag.equals("1"))
					rvalue = (new StringBuilder(String.valueOf(rvalue))).append("移动").append(",").toString();
				else
				if (sflag.equals("2"))
					rvalue = (new StringBuilder(String.valueOf(rvalue))).append("电信").append(",").toString();
			}

			rvalue = rvalue.substring(0, rvalue.length() - 1);
		}
		catch (Exception e)
		{
			String guid = Guid.get();
			logItem.setMethod("Wh.getSmsConfig");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("得到短信配置信息时出现异常");
			logItem.setContent(e.toString());
			Log.write(logItem);
		}
		return rvalue;
	}
}

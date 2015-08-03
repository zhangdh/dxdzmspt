package codegenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class TemplateData {
	public Map getModalData(){ 
		Map map=null;//保存模板数据
		try { 
		//InputStreamReader read = new InputStreamReader(new FileInputStream("src/main/java/codegenerator/template.txt"),"UTF-8");
		InputStreamReader read = new InputStreamReader(new FileInputStream("template.txt"),"UTF-8");
		BufferedReader br = new BufferedReader(read); 
		String line=""; 
		int count=1;//计数器
	     map = new HashMap();//保存模板数据
    	List<Map> init_lb = new ArrayList<Map>();//需要初始化的select列表
    	String input_default="";//控件默认设置
    	List form_zd = new ArrayList();;//保存表单字段
    	List form_fieldname = new ArrayList();//保存表单字段的name属性
    	//StringBuffer sys_btn_auth = new StringBuffer();//授权按钮列表
    	List yhfw = new ArrayList();//保存人员选择控件的name属性和发送范围代码
		while((line=br.readLine())!=null){
          if(count==1){//模块、数据库表等基本信息
        	String[] info = line.split(";"); 
        	String packagename = info[0].substring(info[0].indexOf(":")+1, info[0].length());//包名
        	map.put("packagename", packagename);
        	//业务类名
        	String mk_dm =packagename.substring(packagename.lastIndexOf(".")+1, packagename.length());
        	String serviceClass = mk_dm.substring(0,1).toUpperCase()+mk_dm.substring(1,mk_dm.length());
        	map.put("mk_dm", mk_dm);
        	map.put("serviceClass", serviceClass);
        	map.put("url", "/"+mk_dm.toLowerCase()+"/default.do");
        	//控制器类名
        	String controllerClass=serviceClass+"Controller";
        	map.put("controllerClass", controllerClass);
        	//类存放路径
        	String filepath = packagename.replace(".", "/");
        	map.put("filepath", filepath);
        	//数据库表名
        	String tablename = info[1].substring(info[1].indexOf(":")+1, info[1].length());
        	map.put("tablename", tablename);
        	//控件默认设置
            input_default=info[2].substring(info[2].indexOf(":")+1, info[2].length());
          }else if(count==3){//查询条件
        	List cx_list = new ArrayList();
        	String[] info=line.split("\\|");
        	String name = "";//查询字段的name属性
        	for(String cxzd:info){//循环取得查询字段
        		Map _map = new HashMap();
        		int index = cxzd.indexOf("[");
        		if(index>0){//特殊控件--日期、列表、查询范围、人员选择等
        		 	String zwmc=cxzd.substring(0,index);//查询字段
        		    String kj = cxzd.substring(index+1, cxzd.length()-1);//控件
        		 	 name=TemplateData.converterToFirstSpell(zwmc).toLowerCase();//字段name属性;
    		 		StringBuffer sb = new StringBuffer();
        		 	if("查询范围".equals(kj)){
        		 		map.put("cxfw", "true");
        		 		sb.append("<label>").append(zwmc).append("：");
                        sb.append("<select id=\"cxfw\" name=\"cxfw\"></select>");
                        sb.append("</label>");
                        _map.put("cxzd", sb.toString());
        		 	}else if("人员选择".equals(kj)){
        		 		sb.append("<label>").append(zwmc).append("：");
        		 		sb.append("<input type=\"hidden\" id=\"cx_").append(name).append("\" name=\"cx_");
        		 		sb.append(name).append("\"/>");
                        sb.append("<select  id=\"cx_").append(name).append("_lb\" name=\"cx_").append(name);
                        sb.append("_lb\" style=\"WIDTH: 150px; HEIGHT: 20px; \"></select>");
                        sb.append("<input name=\"depanniu\" type=\"button\" onclick=");
                        sb.append("\"sys_selStaff_openWin('${ctx}/org/default.do?method=select_pub&&sys_fsfw_lb=cx_").append(name);
                        sb.append("_lb&sys_fsfw=cx_").append(name).append("')\" value=\"&gt;&gt;\">");
                        sb.append("</label>");
                        _map.put("cxzd", sb.toString());
        		 	}else if(kj.indexOf("t_dm")==0){//列表框
          		 		map.put("liebiao", "true");
          		 		Map lb_map = new HashMap();//
          		 		lb_map.put("select", "cx_"+name);
          		 		lb_map.put("lb_dm", kj.substring(kj.indexOf(":")+1, kj.length()));
          		 		init_lb.add(lb_map);
          		 		sb.append("<label>").append(zwmc).append("：");
          		 		sb.append("<select name=\"cx_").append(name).append("\"  id=\"cx_").append(name);
          		 		sb.append("\"  style=\"WIDTH: 150px; HEIGHT: 20px; \"></select></label>");
          		 	   _map.put("cxzd", sb.toString());
        		 	}else if("日期".equals(kj)){
        		 		map.put("wdatepicker", "true");
        		 		sb.append("<label>").append(zwmc).append("：");
        		 		sb.append("<input type=\"text\" id=\"cx_rqq\" name=\"cx_rqq\" size=\"15\" onClick=\"WdatePicker()\" ></label>");
        		 		sb.append("<label>&nbsp;至&nbsp;</label>");
        		 		sb.append("<label><input type=\"text\" id=\"cx_rqz\" name=\"cx_rqz\" size=\"15\" onClick=\"WdatePicker()\" ></label>");
        		 		_map.put("cxzd", sb.toString());
        		 	}
        		}else{//一般控件
        		  name=TemplateData.converterToFirstSpell(cxzd).toLowerCase();//字段name属性
        		 String cx="<label>"+cxzd+"：<input type=\"text\" id=\"cx_mc\" name=\"cx_"+name+"\" size=\"15\"></label>";
        		 _map.put("cxzd", cx);
        		}
        		if("true".equals(map.get("wdatepicker"))){//日期控件
       		     _map.put("cxzd_name", "rq");
        		}else{
        		 _map.put("cxzd_name", name);	
        		}
        		cx_list.add(_map);
        	}
        	map.put("cxtj", cx_list);
          }else if(count==5){//查询列表字段
        	  String[] info = line.split("\\|");
        	  List<String> cxjgzd_zw_list = new ArrayList<String>();//查询结果字段（中文）
        	  List<String> cxjdzd_list = new ArrayList<String>();//查询结果字段
        	  for(String str:info){
        		  cxjgzd_zw_list.add(str);
        		  cxjdzd_list.add(TemplateData.converterToFirstSpell(str).toLowerCase());
        	  }
        	  map.put("cxjgzd_zw", cxjgzd_zw_list);  
        	  map.put("cxjgzd", cxjdzd_list); 
          }else if(count==6){//默认添加反选、删除按钮
        	  
          }else if(count==8){//功能按钮
        	  String[] info = line.split("\\|");
        	  List btn_list = new ArrayList();
        	  for(String str:info){
        		  Map _map = new HashMap();
        		 if("新增".equals(str)){
        			 _map.put("id", "btn_add");
        			 _map.put("onClick", "add();");
        			 _map.put("value", "新增");
        		 }else if("保存".equals(str)){
        			 _map.put("id", "btn_save");
        			 _map.put("onClick", "save();");
        			 _map.put("value", "保存");
        		 }else{
        			String btn_pinyin=TemplateData.converterToFirstSpell(str).toLowerCase();
        			btn_pinyin = btn_pinyin.replaceAll("\\{\\!\\}", "");
        			 _map.put("id", "btn_"+btn_pinyin);
        			 _map.put("onClick", btn_pinyin+"();");
        			 _map.put("value", str);
        		 }
        		 if(str.endsWith("{!}")){//按钮是否授权
        			 _map.put("c_btn_auth", "true");
        			 str = str.replaceAll("\\{\\!\\}", "");
        			 _map.put("value", str);
        		 }
        		 btn_list.add(_map);
        	  }
        	  map.put("btn", btn_list);
          }else if(count>8){//表单字段
        	    map.put("div_show", "true");//标识是否显示数据明细区
        		 Map _map = new HashMap();
        		 String lable="";//字段说明
                 if(line.indexOf("{")>0){
                	 lable = line.substring(0,line.indexOf("{"));
                 }else if(line.indexOf("[")>0){
                	 lable = line.substring(0,line.indexOf("["));
                 }else if(line.indexOf("[")<0){
                	 lable=line; 
                 }
/*                 if(lable.indexOf("*")>0){
                	 lable = lable.replace("*", "<font color=\"red\">*</font>");
                 }*/
               	if(lable.indexOf("*")>0){//必填项
                 	_map.put("required", "true");	
                 }
            	 _map.put("lable",lable.replace("*", ""));
                 //添加字段
                 String[] zd = line.split("\\}|\\]");//将字符串进行拆分{*}[***]
                 StringBuffer sb = new StringBuffer();
        		 String required = "";
        		 String required_mark="";//必填项标志
        		 if(lable.indexOf("*")>0){//必填项
        			 required = " required=\"true\" ";
        			 lable= lable.replace("*", "");
        			 required_mark = "<font color=\"red\">*</font>";
        		 }
                 for(String temp: zd){
                	 if(temp.indexOf("{")>0){
                		 String name=TemplateData.converterToFirstSpell(lable).toLowerCase();
                		 form_fieldname.add(name);
                		 temp = temp.replaceFirst(".*\\{", "");
                		 sb.append("<").append(input_default).append(" id=\"").append(name).append("\" name=\"");
         		 		 sb.append(name).append("\" ").append(temp).append(required).append(" />").append(required_mark);
                	 }else if(temp.indexOf("[")>=0){//公用控件
                		 temp = temp.replaceFirst(".*\\[", "");
                		 if("人员选择".equals(temp)){
                    		 String name=TemplateData.converterToFirstSpell(lable.replace("：", "")).toLowerCase();
                    		 form_fieldname.add(name);
                    		 Map yhfw_map = new HashMap();
                    		 yhfw_map.put("sys_fsfw", name);
                    		 yhfw_map.put("fsfw_dm", count);
                    		 yhfw.add(yhfw_map);
             		 		 sb.append("<input type=\"hidden\" id=\"").append(name).append("\" name=\"");
             		 		 sb.append(name).append("\"/>");
                             sb.append("<select  id=\"").append(name).append("_lb\" name=\"").append(name);
                             sb.append("_lb\" style=\"WIDTH: 150px; HEIGHT: 20px; \"></select>");
                             sb.append("<input name=\"depanniu\" type=\"button\" onclick=");
                             sb.append("\"sys_selStaff_openWin('${ctx}/org/default.do?method=select_pub&&sys_fsfw_lb=").append(name);
                             sb.append("_lb&sys_fsfw=").append(name).append("')\" value=\"&gt;&gt;\">");
             		 	}else if(temp.indexOf("t_dm")==0){//列表框
                   		    String name=TemplateData.converterToFirstSpell(lable).toLowerCase();
                   		    form_fieldname.add(name);
               		 		map.put("liebiao", "true");
               		 		Map lb_map = new HashMap();//
               		 		String lb_dm = temp.substring(temp.indexOf(":")+1, temp.length());
               		 		for(Map __map:init_lb){//dm是否已存在，如果存在，则将类别代码的值替换为之前对应lb_dm的select值
               		 			if(lb_dm.equals(__map.get("lb_dm"))){
               		 				lb_dm=":"+String.valueOf(__map.get("select"));
               		 				break;
               		 			}
               		 		} 
               		 		lb_map.put("select", name);             		 		
               		 		lb_map.put("lb_dm", lb_dm);
               		 		init_lb.add(lb_map);
               		 		sb.append("<select name=\"").append(name).append("\"  id=\"").append(name);
               		 		sb.append("\"  style=\"WIDTH: 150px; HEIGHT: 20px; \"></select>");
             		 	}else if("日期".equals(temp)){
                   		    String name=TemplateData.converterToFirstSpell(lable).toLowerCase();
                   		    form_fieldname.add(name);
             		 		map.put("wdatepicker", "true");
             		 		sb.append("<input type=\"text\" id=\"").append(name).append("\" name=\"").append(name);
             		 		sb.append("\" size=\"15\" onClick=\"WdatePicker()\" >");
             		 	}else if("附件".equals(temp)){
             		 		map.put("attachment", "true");
             		 		sb.append("<span id=\"sys_attachment_span\" name=\"sys_attachment_span\" ></span>");
             		 	}else if("提醒".equals(temp)){//只考虑一个提醒的情况
             		 		map.put("tx", "true");
             		 		sb.append("<span id=\"sys_tixing_span_0\" name=\"sys_tixing_span_0\"></span>");
             		 	}else if("KE".equals(temp)){
             		 		map.put("KE", "true");
             		 		form_fieldname.add("content");
             		 		sb.append("<textarea id=\"content\" name=\"content\" style=\"width:760px;height:200px;visibility:hidden;\"></textarea>");
             		 	}
                		 
                	 }else{//默认设置
                		 String name=TemplateData.converterToFirstSpell(lable).toLowerCase();
                		 form_fieldname.add(name);
                		 sb.append("<").append(input_default).append(" id=\"").append(name).append("\" name=\"");
         		 		 sb.append(name).append("\"").append(required).append("/>").append(required_mark);
                	 }
                 }
                 _map.put("zd", sb.toString());
           	      form_zd.add(_map);
        	 // }
          }
          count++;//计数器加1
		} 
        map.put("form_zd", form_zd);
        map.put("init_lb", init_lb);
        map.put("form_fieldname", form_fieldname);
        map.put("yhfw", yhfw);
		}catch (FileNotFoundException e) { 
        e.printStackTrace();
		}catch (IOException e) { 
        e.printStackTrace();
		} 
		return map;
		}
    /**  
	    * 汉字转换为汉语拼音首字母，英文字符不变  
	    * @param chines 汉字  
	    * @return 拼音  
	    */  
	    public static String converterToFirstSpell(String chines){          
	        String pinyinName = "";   
	        char[] nameChar = chines.toCharArray();   
	        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
	        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
	        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
	        for (int i = 0; i < nameChar.length; i++) {   
	            if (nameChar[i] > 128) {   
	                try {   
	                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);   
	                } catch (BadHanyuPinyinOutputFormatCombination e) {   
	                    e.printStackTrace();   
	                }   
	            }else{   
	                pinyinName += nameChar[i];   
	            }   
	        }   
	        return pinyinName;   
	    } 
	    
	    //生成代码
	    public boolean genCode(){
	     Map map = this.getModalData();
	    	 Configuration cfg = new Configuration();
	    	 try{
	    	// cfg.setDirectoryForTemplateLoading(new File("src/main/java/codegenerator"));//模板存放位置
	    	 cfg.setDirectoryForTemplateLoading(new File("codegenerator"));//模板存放位置
	 		 cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
	 		 cfg.setDefaultEncoding("UTF-8");
			 //cfg.setOutputEncoding("UTF-8");
			 cfg.setLocale(Locale.CHINA);
			 //生成jsp文件
			 Template template = cfg.getTemplate("jsp.ftl");//模板名称
			 template.setEncoding("UTF-8");

			 String jspfileDir = "../../../../webapp/"+map.get("mk_dm");
			 //String jspfileDir ="webapp/"+map.get("mk_dm");
			 File fileDir=new File(jspfileDir);   
	         org.apache.commons.io.FileUtils.forceMkdir(fileDir);   
	    	// File output = new File("webapp/"+map.get("mk_dm")+"/default.jsp");
	         File output = new File(jspfileDir+"/default.jsp");  
	         Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output),"UTF-8"));
	    	 template.process(map, writer);   
	    	 writer.close(); 
	    	 //生成js文件
			 Template template_js = cfg.getTemplate("js.ftl");//模板名称
			 template_js.setEncoding("UTF-8");  
			 File output_js = new File(jspfileDir+"/default.js"); 
	         Writer writer_js = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_js),"UTF-8"));
	         template_js.process(map, writer_js);   
	    	 writer_js.close(); 
	    	 //生成控制器类文件
			 Template template_controller = cfg.getTemplate("controller.ftl");//模板名称
			 template_controller.setEncoding("UTF-8"); 
			 //类路径
			 String javaDir_str = "../../java/"+map.get("filepath");
			 // String javaDir_str = "src/main/java/"+map.get("filepath");
			 File javaDir = new File(javaDir_str);
			 org.apache.commons.io.FileUtils.forceMkdir(javaDir);  
			 File output_controller = new File(javaDir_str+"/"+map.get("controllerClass")+".java");  
	         Writer writer_controller = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_controller),"UTF-8"));
	         template_controller.process(map, writer_controller);   
	         writer_controller.close(); 
	    	 //生成业务类文件
			 Template template_service = cfg.getTemplate("service.ftl");//模板名称
			 template_controller.setEncoding("UTF-8"); 
			 org.apache.commons.io.FileUtils.forceMkdir(javaDir); 
			 File output_service = new File(javaDir_str+"/"+map.get("serviceClass")+".java");  
	         Writer writer_service = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_service),"UTF-8"));
	         template_service.process(map, writer_service);   
	         writer_service.close(); 
	         return true;
	    	 }catch (TemplateException e) {   
	    		e.printStackTrace();   
	    		return false;
        
	         } catch (IOException e) {   
	    		 e.printStackTrace(); 
	    		 return false;
	    	 }   
	    }
	    
	public static void main(String args[]){
		TemplateData templateData = new TemplateData();
		if(templateData.genCode()){
			System.out.println("代码生成成功...");
		}
	}

}

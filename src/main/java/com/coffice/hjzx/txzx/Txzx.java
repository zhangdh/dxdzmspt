package com.coffice.hjzx.txzx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.coffice.bean.PageBean;
import com.coffice.util.BaseUtil;
import com.coffice.util.Db;
import com.coffice.util.Guid;
import com.coffice.util.JspJsonData;
import com.coffice.util.Log;
import com.coffice.util.LogItem;
import com.coffice.util.fusionchart.multiseries.ColumnChartM;

public class Txzx extends BaseUtil{
	JspJsonData jjd;// 页面json数据对象

	LogItem logItem;// 日志项

	Map map;
	
	String zzid;//组织ID
	
	String bmid;//部门ID
	
	String gwid;//岗位ID
	
	String yhid;//用户ID
	public Txzx(Map mapIn) {
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
	public Map query() {
		try {
			StringBuffer chart1 = new StringBuffer();
			StringBuffer chart2 = new StringBuffer();
			StringBuffer chart3 = new StringBuffer();
			StringBuffer chart4 = new StringBuffer();
			StringBuffer chart5 = new StringBuffer();
			StringBuffer chart6 = new StringBuffer();
			StringBuffer chart7 = new StringBuffer();
			StringBuffer chart8 = new StringBuffer();
			StringBuffer chart9 = new StringBuffer();
			StringBuffer chart10 = new StringBuffer();
			chart1.append("<chart caption='平台总受理件内容分类统计' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			      .append("<set label='交通出行' value='197'  /><set label='水电汽暖' value='239'  />")
			      .append("<set label='社会保障' value='170'  /><set label='治安消防' value='25'  />")
			      .append("<set label='市政管理' value='34'  /><set label='价格收费' value='37'  />")
			      .append("<set label='民政救济' value='24'  /><set label='环境保护' value='208'  />")
			      .append("<set label='医疗卫生' value='19'  /><set label='劳动社保' value='61'  />")
			      .append("<set label='路线教育' value='6'  /><set label='房产物业' value='127'  />")
			      .append("<set label='其它' value='252'  /><set label='三农问题' value='183'  />")
			      .append("<set label='文教体育' value='23'  /></chart>");
			jjd.setExtend("chart1",chart1.toString());
			chart2.append("<chart caption = '各县区受理件比较饼状图'  showPercentInToolTip= '1'  baseFontSize='12' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1'>")
			      .append("<set label='市级直属' value='244'/><set label='崆峒区' value='486' />")
			      .append("<set label='华亭县' value='62'/><set label='灵台县' value='51' />")
			      .append("<set label='庄浪县' value='42'/><set label='泾川县' value='56' />")
			      .append("<set label='静宁县' value='108'/><set label='崇信县' value='36' />")
			      .append("</chart>");
			jjd.setExtend("chart2",chart2.toString());
			
			chart3.append("<chart caption='市级直属部门受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
		      .append("<set label='交通出行' value='63'  /><set label='水电汽暖' value='16'  />")
		      .append("<set label='社会保障' value='18'  /><set label='治安消防' value='2'  />")
		      .append("<set label='市政管理' value='12'  /><set label='价格收费' value='15'  />")
		      .append("<set label='民政救济' value='3'  /><set label='环境保护' value='27'  />")
		      .append("<set label='医疗卫生' value='1'  /><set label='劳动社保' value='13'  />")
		      .append("<set label='路线教育' value='1'  /><set label='房产物业' value='22'  />")
		      .append("<set label='其它' value='37'  /><set label='三农问题' value='10'  />")
		      .append("<set label='文教体育' value='4'  /></chart>");
		    jjd.setExtend("chart3",chart3.toString());
		
			chart4.append("<chart caption='崆峒区受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
				  .append("<set label='房产物业' value='56'  /><set label='环境保护' value='116'  />")
				  .append("<set label='价格收费' value='4'  /><set label='交通出行' value='43'  />")
				  .append("<set label='劳动社保' value='9'  /><set label='路线教育' value='0'  />" +
				  		  "<set label='民政救济' value='4'  />")
				  .append("<set label='其它' value='44'  /><set label='三农问题' value='53'  />")
				  .append("<set label='社会保障' value='25'  /><set label='市政管理' value='12'  />")
				  .append("<set label='水电汽暖' value='96'  /><set label='文教体育' value='10'  />")
				  .append("<set label='医疗卫生' value='7'  /><set label='治安消防' value='7'  />")
				  .append("</chart>");
		    jjd.setExtend("chart4",chart4.toString());
		    
			chart5.append("<chart caption='泾川县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='4'  /><set label='环境保护' value='2'  />")
			  .append("<set label='价格收费' value='1'  /><set label='交通出行' value='7'  />")
			  .append("<set label='劳动社保' value='0'  /><set label='路线教育' value='0'  />" +
			  		  "<set label='民政救济' value='0'  />")
			  .append("<set label='其它' value='12'  /><set label='三农问题' value='17'  />")
			  .append("<set label='社会保障' value='6'  /><set label='市政管理' value='1'  />")
			  .append("<set label='水电汽暖' value='3'  /><set label='文教体育' value='1'  />")
			  .append("<set label='医疗卫生' value='2'  /><set label='治安消防' value='0'  />")
			  .append("</chart>");
			jjd.setExtend("chart5",chart5.toString());
	    
			chart6.append("<chart caption='灵台县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='3'  /><set label='环境保护' value='1'  />")
			  .append("<set label='价格收费' value='0'  /><set label='交通出行' value='2'  />")
			  .append("<set label='劳动社保' value='3'  /><set label='路线教育' value='1'  />" +
			  		  "<set label='民政救济' value='1'  />")
			  .append("<set label='其它' value='6'  /><set label='三农问题' value='11'  />")
			  .append("<set label='社会保障' value='14'  /><set label='市政管理' value='2'  />")
			  .append("<set label='水电汽暖' value='7'  /><set label='文教体育' value='0'  />")
			  .append("<set label='医疗卫生' value='0'  /><set label='治安消防' value='0'  />")
			  .append("</chart>");
			jjd.setExtend("chart6",chart6.toString());
			
			chart7.append("<chart caption='崇信县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='2'  /><set label='环境保护' value='1'  />")
			  .append("<set label='价格收费' value='4'  /><set label='交通出行' value='4'  />")
			  .append("<set label='劳动社保' value='0'  /><set label='路线教育' value='1'  />" +
			  		  "<set label='民政救济' value='1'  />")
			  .append("<set label='其它' value='4'  /><set label='三农问题' value='8'  />")
			  .append("<set label='社会保障' value='0'  /><set label='市政管理' value='0'  />")
			  .append("<set label='水电汽暖' value='4'  /><set label='文教体育' value='1'  />")
			  .append("<set label='医疗卫生' value='1'  /><set label='治安消防' value='0'  />")
			  .append("</chart>");
			jjd.setExtend("chart7",chart7.toString());
			
			chart8.append("<chart caption='华亭县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='6'  /><set label='环境保护' value='5'  />")
			  .append("<set label='价格收费' value='0'  /><set label='交通出行' value='6'  />")
			  .append("<set label='劳动社保' value='4'  /><set label='路线教育' value='0'  />" +
			  		  "<set label='民政救济' value='3'  />")
			  .append("<set label='其它' value='6'  /><set label='三农问题' value='11'  />")
			  .append("<set label='社会保障' value='10'  /><set label='市政管理' value='0'  />")
			  .append("<set label='水电汽暖' value='8'  /><set label='文教体育' value='1'  />")
			  .append("<set label='医疗卫生' value='0'  /><set label='治安消防' value='2'  />")
			  .append("</chart>");
			jjd.setExtend("chart8",chart8.toString());
	    
			chart9.append("<chart caption='庄浪县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='3'  /><set label='环境保护' value='1'  />")
			  .append("<set label='价格收费' value='0'  /><set label='交通出行' value='1'  />")
			  .append("<set label='劳动社保' value='2'  /><set label='路线教育' value='0'  />" +
			  		  "<set label='民政救济' value='4'  />")
			  .append("<set label='其它' value='6'  /><set label='三农问题' value='11'  />")
			  .append("<set label='社会保障' value='4'  /><set label='市政管理' value='2'  />")
			  .append("<set label='水电汽暖' value='5'  /><set label='文教体育' value='0'  />")
			  .append("<set label='医疗卫生' value='1'  /><set label='治安消防' value='2'  />")
			  .append("</chart>");
			jjd.setExtend("chart9",chart9.toString());
	    
			chart10.append("<chart caption='静宁县受理件各内容分类量' xAxisName='' yAxisName='' useRoundEdges='1' numberPrefix='' showValues='1' baseFontSize='12'  > ")
			  .append("<set label='房产物业' value='9'  /><set label='环境保护' value='3'  />")
			  .append("<set label='价格收费' value='1'  /><set label='交通出行' value='9'  />")
			  .append("<set label='劳动社保' value='7'  /><set label='路线教育' value='1'  />" +
			  		  "<set label='民政救济' value='3'  />")
			  .append("<set label='其它' value='10'  /><set label='三农问题' value='28'  />")
			  .append("<set label='社会保障' value='11'  /><set label='市政管理' value='0'  />")
			  .append("<set label='水电汽暖' value='21'  /><set label='文教体育' value='1'  />")
			  .append("<set label='医疗卫生' value='2'  /><set label='治安消防' value='2'  />")
			  .append("</chart>");
			jjd.setExtend("chart10",chart10.toString());
	    
	    
		} catch (Exception e) {
			String guid = Guid.get();
			logItem.setMethod("query");
			logItem.setLogid(guid);
			logItem.setLevel("error");
			logItem.setDesc("查询图形展现数据");
			logItem.setContent(e.toString());
			Log.write(logItem);
			jjd.setResult(false, "fwqq.query异常:" + e.toString());
		}
		return jjd.getData();
	}
	
}

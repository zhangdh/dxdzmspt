package com.coffice.workflow.use;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.coffice.util.JspJsonData;
import com.coffice.util.RequestUtil;

@Controller
@RequestMapping({"/wfuser.coffice"})
public class WKUserController {
	@RequestMapping(params = "method=showUserTree")
	public ModelAndView showUserTree(HttpServletRequest request) {
		Map<String,String> map=RequestUtil.getMap(request);
		JspJsonData jjd = new JspJsonData();
		String entryid =  map.get("entryid");
		String stepid = map.get("stepid");
		String workid = map.get("workid");
		WKUser su = new WKUser();
		String[] spr=su.getOswfClrInfoKz(workid,stepid);
		List list = su.getYhTree(spr[1],spr[0],spr[2],map.get("yhid"),entryid);
		StringBuffer treeStr = new StringBuffer();
		treeStr.append("[");
		for(int i = 0;i < list.size(); i ++){
			Map _map = (Map)list.get(i);
			//根节点
		//	System.out.println("lx_dm:"+_map.get("lx_dm"));
			if("0".equals(_map.get("sjid"))){
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/1_close.png'},");
				
			}else if(String.valueOf(_map.get("lx_dm")).equals("201")){
				//部门
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/folder.png'},");
			}else if(String.valueOf(_map.get("lx_dm")).equals("202")){
				//岗位
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/7.png'},");
			}else if(String.valueOf(_map.get("lx_dm")).equals("204")){
				//人员
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/man.png'},");
			}else{
				//其它
				treeStr.append("{id:'").append(_map.get("guid")).append("',pId:'").append(_map.get("sjid")).append("',lxid:'").append(_map.get("lxid")).append("',lx_dm:'").append(_map.get("lx_dm")).append("',name:'").append(_map.get("mc")).append("',icon:'/js/ztree/css/zTreeStyle/img/diy/9.png'},");
			}
			
		}
		String treeString = treeStr.toString();
		if(treeString.length()>1){
			treeString = treeString.substring(0,treeString.length()-1);
			treeString = treeString+"]";
		}
		jjd.setExtend("user_tree",treeString);
		return new ModelAndView("jsonView", jjd.getData());	
	}
 
}

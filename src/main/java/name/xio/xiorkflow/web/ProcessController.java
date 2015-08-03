
package name.xio.xiorkflow.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;



/**
 * 功能：添加流程
 */
@Controller
@RequestMapping("/oswf/graphicdesigner/default.do")
public class ProcessController {

	/**
	 * 保存流程
	 */
	@Transactional
	@RequestMapping(params = "method=saveProcess")
	public ModelAndView saveProcess(HttpServletRequest request){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",processBusi.saveProcess());
	}
	
	/**
	 * 显示流程
	 */
	@RequestMapping(params = "method=showProcess")
	public ModelAndView showProcess(HttpServletRequest request,HttpServletResponse response){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		processBusi.showProcess(response);
		return null;
	}
	
	/**
	 * 显示流程属性
	 */
	@RequestMapping(params = "method=showWkAttr")
	public ModelAndView showWkAttr(HttpServletRequest request){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		return new ModelAndView("/oswf/graphicdesigner/config/workflow_attr.jsp",processBusi.showWkAttr());
	}
	
	/**
	 * 显示节点配置
	 */
	@RequestMapping(params = "method=showNodeConfig")
	public ModelAndView showNodeConfig(HttpServletRequest request){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		return new ModelAndView("/oswf/graphicdesigner/config/workflow_config.jsp",processBusi.showNodeConfig());
	}
	
	/**
	 * 根据模块ID显示表单信息
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=showFormConfig")
	public ModelAndView showFormConfig(HttpServletRequest request){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",processBusi.showFormConfig());
	}
	
	/**
	 * 显示功能列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "method=showFunctionConfig")
	public ModelAndView showFunctionConfig(HttpServletRequest request){
		ProcessBusiBean processBusi=new ProcessBusiBean(RequestUtil.getMap(request));
		return new ModelAndView("jsonView",processBusi.showFunctionConfig());
	}
	
}

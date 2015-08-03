package ${packagename};

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coffice.util.RequestUtil;

@Controller
@RequestMapping("${url}")
public class ${controllerClass}{
	//查询
	@RequestMapping(params = "method=query")
	public ModelAndView query(HttpServletRequest request) {
		${serviceClass} ${mk_dm} = new ${serviceClass}(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ${mk_dm}.query());
	}
	<#if div_show??>
	// 保存数据
	@Transactional
	@RequestMapping(params = "method=save")
	public ModelAndView save(HttpServletRequest request) {
	    Map map = RequestUtil.getMap(request);
		${serviceClass} ${mk_dm} = new ${serviceClass}(map);
		if(map.get("guid")!=null&&!"".equals(map.get("guid"))){//更新
			map = ${mk_dm}.update();
		}else{
			map = ${mk_dm}.save();
		}
		return new ModelAndView("jsonView", map);
	}
	</#if>
	//删除数据
	@RequestMapping(params = "method=delete")
	public ModelAndView delete(HttpServletRequest request) {
		${serviceClass} ${mk_dm} = new ${serviceClass}(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ${mk_dm}.delete());
	}
	//显示数据明细
	@RequestMapping(params = "method=show")
	public ModelAndView show(HttpServletRequest request){
		${serviceClass} ${mk_dm} = new ${serviceClass}(RequestUtil.getMap(request));
		return new ModelAndView("jsonView", ${mk_dm}.show());
	}

}

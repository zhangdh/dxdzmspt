package name.xio.xiorkflow.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.workflow.util.WorkFlowMethod;

//读取权限信息
public class GetFormRole extends HttpServlet {
	public GetFormRole() {
		super();
	}
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		  request.setCharacterEncoding("UTF-8");
	      String businessId = request.getParameter("businessId");
	      String stepId= request.getParameter("stepId");
	      //读取表单所有字段
	      WorkFlowMethod method=new  WorkFlowMethod();
	      Map  map=method.getFormRole(businessId,stepId);
	      List validateList=method.getFormValidate(businessId, stepId);
	      String formId=method.getFormModel(businessId, stepId);//"9785848d-4065-43ab-bcd9-ba97fd04a744.ftl";
	      request.getSession().setAttribute("mapRole", map);
	      request.getSession().setAttribute("id", formId);
	      request.getSession().setAttribute("validate", validateList);
		  RequestDispatcher rd=request.getRequestDispatcher("/template");
		  rd.forward(request, response);
	      
	}

	public void init() throws ServletException {
		// Put your code here
	}

}

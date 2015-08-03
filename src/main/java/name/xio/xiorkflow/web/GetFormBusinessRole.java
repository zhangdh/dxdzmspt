package name.xio.xiorkflow.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.workflow.util.WorkFlowMethod;


public class GetFormBusinessRole extends HttpServlet {
	public GetFormBusinessRole() {
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

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		  request.setCharacterEncoding("UTF-8");
	      String businessId = request.getParameter("businessId");
	      String stepId= request.getParameter("stepId");
	      //读取表单所有字段
	      WorkFlowMethod method=new WorkFlowMethod();
	      Map  map=method.getFormRole(businessId,stepId);
	      
	      String formId=method.getFormId(businessId, stepId);
	      String modelName=method.getFormModelName(businessId, stepId);
	      request.getSession().setAttribute("mapRole", map);
	      request.getSession().setAttribute("formId", formId);
	      request.getSession().setAttribute("modelName", modelName);
	      //LoadFormAction
		  RequestDispatcher rd=request.getRequestDispatcher("/loadform?method=data");
		  rd.forward(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

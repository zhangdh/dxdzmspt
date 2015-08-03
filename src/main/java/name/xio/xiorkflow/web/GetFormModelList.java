package name.xio.xiorkflow.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.workflow.util.WorkFlowMethod;



public class GetFormModelList extends HttpServlet {
	public GetFormModelList() {
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
	      String name = request.getParameter("formId");
	      //读取表单所有字段
	      WorkFlowMethod method=new  WorkFlowMethod();
	      List modelList=method.getFormModelList();
	      request.setAttribute("model", modelList);
		  RequestDispatcher rd=request.getRequestDispatcher("/oswf/graphicdesigner/formConfig/form_list.jsp");
		  rd.forward(request, response);
	      
	}

	public void init() throws ServletException {
		// Put your code here
	}

}

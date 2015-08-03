package name.xio.xiorkflow.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coffice.workflow.util.WorkFlowMethod;


public class GetFormModel extends HttpServlet {
	public GetFormModel() {
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
		  String forward="";
		  request.setCharacterEncoding("UTF-8");
	      String name = request.getParameter("formId");
	      String m=request.getParameter("method");
	      //读取表单所有字段
	      WorkFlowMethod method=new WorkFlowMethod();
	      ArrayList modelList=method.getFormModel(name);
	      request.setAttribute("model", modelList);
	      if(m==null||m.equals("")){
	    	  forward="/oswf/graphicdesigner/formConfig/form_config.jsp";
	      }else{
	    	  forward="/oswf/graphicdesigner/node_config.jsp";
	      }
		  RequestDispatcher rd=request.getRequestDispatcher(forward);
		  rd.forward(request, response);
	      
	}

	public void init() throws ServletException {
		// Put your code here
	}

}

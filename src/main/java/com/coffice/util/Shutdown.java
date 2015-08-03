package com.coffice.util;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.logicalcobwebs.proxool.ProxoolFacade;

public class Shutdown extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1837499735814196005L;

	/**
	 * web容器关闭时关闭数据库连接 <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		ProxoolFacade.shutdown(); 
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

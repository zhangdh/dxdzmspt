package com.coffice.util.quartz;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;

public class ActionUtil {   

	private SchedulerFactory schefactory = null;
	private Scheduler schedul = null; 

    public Scheduler getScheduler(HttpServletRequest request){   

    	// Retrieve the ServletContext   
        ServletContext ctx =  request.getSession()
        		.getServletContext();   
 
        // Retrieve the factory from the ServletContext   
        StdSchedulerFactory factory = (StdSchedulerFactory)   
             	ctx.getAttribute( QuartzInitializerServlet.QUARTZ_FACTORY_KEY);  
        
         try {
        	 schedul = factory.getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
		return schedul;
    } 
    
    /**
     * 获得scheduler
     * @return Scheduler
     * @throws 
     */
    public Scheduler getDefaultScheduler() {   

    	schefactory = new StdSchedulerFactory();
    	try {
			schedul = schefactory.getScheduler();

			if (schedul.isShutdown()) {
				schedul.start();             //若关闭,启动
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         return schedul;  
    }
}  

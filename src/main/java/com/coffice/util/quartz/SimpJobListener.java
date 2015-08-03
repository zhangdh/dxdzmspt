package com.coffice.util.quartz;

import org.apache.commons.logging.Log;   
import org.apache.commons.logging.LogFactory;   
import org.quartz.JobExecutionContext;   
import org.quartz.JobExecutionException;   
import org.quartz.JobListener;   
  
public class SimpJobListener implements JobListener {   
     Log logger = LogFactory.getLog(SimpJobListener.class);   
     String name; 
     
     
     /** 
      * 注意name是必须的
      */
     public SimpJobListener(String name){
    	 
    	 this.name = name;
     }
     
     public String getName()
     {
    	 return name; //getClass().getSimpleName();
     }
     
     public void jobToBeExecuted(JobExecutionContext context) {   
          String jobName = context.getJobDetail().getName();   
          //System.out.println(jobName + "  将执行");   
     }   
     public void jobExecutionVetoed(JobExecutionContext context) {   
          String jobName = context.getJobDetail().getName();   
          logger.info(jobName + " was vetoed and not executed()");   
     }   
     public void jobWasExecuted(JobExecutionContext context,   
               JobExecutionException jobException) {   
  
          String jobName = context.getJobDetail().getName();   
         // System.out.println(jobName + "  执行完毕");   
     }   
}
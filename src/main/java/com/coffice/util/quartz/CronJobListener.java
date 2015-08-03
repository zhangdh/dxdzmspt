package com.coffice.util.quartz;

import org.apache.commons.logging.Log;   
import org.apache.commons.logging.LogFactory;   
import org.quartz.JobExecutionContext;   
import org.quartz.JobExecutionException;   
import org.quartz.JobListener;   
  
public class CronJobListener implements JobListener {   
     Log logger = LogFactory.getLog(CronJobListener.class);   
     String name; 
     
     
     /** 
      * 注意name是必须的
      */
     public CronJobListener(String name){
    	 
    	 this.name = name;
     }
     
     public String getName()
     {
    	 return name; //getClass().getSimpleName();
     }
     
     public void jobToBeExecuted(JobExecutionContext context) {   
          String jobName = context.getJobDetail().getName();   
     }   
     public void jobExecutionVetoed(JobExecutionContext context) {   
          String jobName = context.getJobDetail().getName();   
          logger.info(jobName + " was vetoed and not executed()");   
     }   
     public void jobWasExecuted(JobExecutionContext context,   
               JobExecutionException jobException) {   
  
          String jobName = context.getJobDetail().getName();   
     }   
}
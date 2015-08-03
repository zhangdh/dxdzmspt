package com.coffice.util.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;   
import org.apache.commons.logging.LogFactory;   
import org.quartz.JobExecutionContext;   
import org.quartz.Trigger;   
import org.quartz.TriggerListener;   


  
public class CronTriggerListener implements TriggerListener {   
     Log logger = LogFactory.getLog(CronTriggerListener.class);   
    // private SimpleDateFormat sdf = null;
   //  private dbconnect db = null;
     public CronTriggerListener(){
    //	 sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //	 db = new dbconnect();
     }
     
     
     public String getName()
     {
         return getClass().getSimpleName();   
     }

     public void triggerFired(Trigger trigger,   
          JobExecutionContext context) {   
          String triggerName = trigger.getName();   
      
         // Date fireDate = context.getFireTime();
  		 // String dateString = (String) sdf.format(fireDate);
          //db.ScheduState(triggerName, dateString, 1);   //记录状态
         // System.out.println(triggerName + "   触发器开始 ..");   
          logger.info(triggerName + "   触发器开始 ..");
     }   
  
     
     
     public boolean vetoJobExecution(Trigger trigger,   
               JobExecutionContext context) {   
//  
//          String triggerName = trigger.getName();   
//          logger.info(triggerName + " was not vetoed");   
          return false;   
     }   
  
     public void triggerMisfired(Trigger trigger) {   
         String triggerName = trigger.getName();   
         
         logger.info(triggerName + " misfired");   
     }   
  
     public void triggerComplete(Trigger trigger,   
         JobExecutionContext context,   
         int triggerInstructionCode) {   
  
         
         String triggerName1 = trigger.getName();   
         
        // Date fireDate = context.getFireTime();
 		//  String dateString = (String) sdf.format(fireDate);
         //db.ScheduState(triggerName1, dateString, 2);   //记录状态 
         logger.info(triggerName1 + "   触发器结束 ..");
       //  System.out.println(triggerName1 + "   触发器结束 .."); 
         if(context.getNextFireTime()==null){
       	  logger.info(triggerName1 + "   触发器作业已完全结束 ..");
         }
     }   
} 









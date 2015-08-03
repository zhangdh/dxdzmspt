package com.coffice.util.quartz;

import java.util.Iterator;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobDetails implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		// TODO Auto-generated method stub
		JobDataMap  datamap = arg0.getJobDetail().getJobDataMap();
		//System.out.println("执行作业 ........... \n下次触发时间"+arg0.getTrigger().getNextFireTime()
				//+"  作业名称 :"+arg0.getTrigger().getJobName());		
		Iterator iter = datamap.keySet().iterator();   		  
        while (iter.hasNext()) {   
             Object key = iter.next();   
             Object value = datamap.get(key);   
             //System.out.println("Key: " + key + " - Value: " + value);   
        }   

        
        
        
        
	}

}

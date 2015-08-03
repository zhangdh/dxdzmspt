package com.coffice.util.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import com.coffice.util.Log;
import com.coffice.util.LogItem;

public class QuartzApi {

	static ActionUtil actionutil = null;
	private Scheduler sched = null;
	private JobDetail job = null;
	private CronTrigger Ctrigger = null;
	private SimpleTrigger Strigger = null;
	private Log log = null;
	private JobDataMap jobdatamap = null;
	LogItem logItem;// 日志项
	
	public QuartzApi() {
		
		//log = LogFactory.getLog(QuartzApi.class);		
	}

	

	
	/**
	 * 添加Cron类型作业
	 * @param cronExpress
	 * @param className
	 * @param mapArgus
	 * @param doMissfire  是否执行错过作业
	 * @param guid  事件标识--既触发器标识
	 * @return 添加成功返回作业名称 失败返回异常
	 */   
	public boolean addCronJob(String cronExpress, Class className, Map mapArgus ,boolean doMissfire,String guid ) {
		logItem = new LogItem();
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		
		String pass = getRandom();
		String jobNameBydate = "jname" + pass;		// 生成作业名,触发器名
		String triNameBydate = guid;
		
		try {
			sched = actionutil.getDefaultScheduler();
			job = new JobDetail(jobNameBydate, Scheduler.DEFAULT_GROUP,
					className);
			jobdatamap = job.getJobDataMap();
			this.setJDMap(mapArgus);				// 传入参数转换
			try {
				Ctrigger = new CronTrigger(triNameBydate, Scheduler.DEFAULT_GROUP);
				Ctrigger.setCronExpression(cronExpress);
				
				if (doMissfire){                   //是否执行错过作业
					Ctrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
				} else {
					Ctrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
				}

			} catch (ParseException e) {
				logItem.setMethod("addCronJob");
				logItem.setLevel("info");
				logItem.setDesc("添加Cron类型作业失败");
				logItem.setContent(e.toString());
				Log.write(logItem);	
			  //log.info("addCronJob false .");
				e.printStackTrace();				
			}

			CronTriggerListener crontriListener = new CronTriggerListener();
			sched.addTriggerListener(crontriListener);		     	// scheduler 添加非全局监听
			Ctrigger.addTriggerListener(crontriListener.getName());  // trigger 添加触发器监听

			Date adate = sched.scheduleJob(job, Ctrigger);
			logItem.setMethod("addCronJob");
			logItem.setLevel("info");
			logItem.setDesc("添加Cron类型作业成功");
			Log.write(logItem);	
			
		//	log.info("Job has added successful ! Cron Triggername is : "+
		//			triNameBydate+" . This Fire time at:" + adate);
			return true;

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logItem.setMethod("addCronJob");
			logItem.setLevel("info");
			logItem.setDesc("添加Cron类型作业异常");
			logItem.setContent(e.toString());
			Log.write(logItem);				
			//log.info("addCronJob method  has Exception :" + e);
			return false;
		}
	}
	

	/**
	 * 添加Simple类型作业
	 * @param startTime
	 * @param endTime
	 * @param repeatCount
	 * @param repeatInterval
	 * @param className
	 * @param mapArgus
	 * @param doMissfire  是否执行错过作业
	 * @param guid  事件标识 --即触发器标识
	 * @return  添加成功返回作业名称 失败返回异常
	 */
	public boolean addSimpJob(Date startTime,Date endTime,int repeatCount,
					long repeatInterval, Class className, Map mapArgus,boolean doMissfire,String guid) {
		logItem = new LogItem();
		
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		String pass = getRandom();
		String jobNameBydate = "jname" + pass;		// 随机产生作业名,触发器名
		String triNameBydate = guid;
		
		try {
			sched = actionutil.getDefaultScheduler();
			job = new JobDetail(jobNameBydate, Scheduler.DEFAULT_GROUP,
					className);
			jobdatamap = job.getJobDataMap();
			this.setJDMap(mapArgus);					// 传入参数转换
		    Strigger = new SimpleTrigger(triNameBydate, Scheduler.DEFAULT_GROUP);
		    
		    Strigger.setStartTime(startTime);
		    Strigger.setEndTime(endTime);
		    Strigger.setRepeatCount(repeatCount);
		    Strigger.setRepeatInterval(repeatInterval);
		    
		    if (!doMissfire){                   //是否执行错过作业
		    	Strigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
		    } else {
		    	Strigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT);
		    }

		    
			SimpTriggerListener simptriListener = new SimpTriggerListener();
			sched.addTriggerListener(simptriListener); 					// scheduler 添加非全局监听
			Strigger.addTriggerListener(simptriListener.getName());		 // trigger 添加触发器监听

			Date adate = sched.scheduleJob(job, Strigger);
			logItem.setMethod("addSimpJob");
			logItem.setLevel("info");
			logItem.setDesc("添加Simple类型作业成功 ");
			Log.write(logItem);		
			
			//log.info("Job has added successful ! Simple Triggername is : "
			//		+triNameBydate+" . This Fire time at :" + adate);
			return true;

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logItem.setMethod("addSimpJob");
			logItem.setLevel("info");
			logItem.setDesc("添加Simple类型作业失败 ");
			logItem.setContent(e.toString());
			Log.write(logItem);	
			
			
			//log.info("addSimpleJob method  has Exception :" + e);

			return false;
		}
	}
	
	
	
	
	/**
	 * 添加Simple类型作业
	 * @param startTime
	 * @param endTime
	 * @param repeatCount
	 * @param repeatInterval
	 * @param className
	 * @param mapArgus
	 * @param doMissfire  是否执行错过作业
	 * @return  添加成功返回作业名称 失败返回异常
	 */
	public boolean addSimpJobOnce(Date startTime, Class className, Map mapArgus,boolean doMissfire,String guid) {
		logItem = new LogItem();
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		String pass = getRandom();
		String jobNameBydate = "jname" + pass;		// 随机产生作业名,触发器名
		String triNameBydate = guid;
		
		try {
			sched = actionutil.getDefaultScheduler();
			job = new JobDetail(jobNameBydate, Scheduler.DEFAULT_GROUP,
					className);
			jobdatamap = job.getJobDataMap();
			this.setJDMap(mapArgus);					// 传入参数转换
		    Strigger = new SimpleTrigger(triNameBydate, Scheduler.DEFAULT_GROUP);
		    
		    Strigger.setStartTime(startTime);
		    
		    if (!doMissfire){                   //是否执行错过作业
		    	Strigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
		    } else {
		    	Strigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT);
		    }

		    
			SimpTriggerListener simptriListener = new SimpTriggerListener();
			sched.addTriggerListener(simptriListener); 					// scheduler 添加非全局监听
			Strigger.addTriggerListener(simptriListener.getName());		 // trigger 添加触发器监听

			Date adate = sched.scheduleJob(job, Strigger);
			logItem.setMethod("addSimpJobOnce");
			logItem.setLevel("info");
			logItem.setDesc("添加Simple类型作业成功");

			Log.write(logItem);	
			
			//log.info("Job has added successful ! Simple Triggername is : "
			//		+triNameBydate+" . This Fire time at :" + adate);
			return true;

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logItem.setMethod("addSimpJobOnce");
			logItem.setLevel("info");
			logItem.setDesc("添加Simple类型作业失败");
            logItem.setContent(e.toString());
			Log.write(logItem);	
			
			//log.info("addSimpleJob method  has Exception :" + e);
			return false;
		}
	}
	
	
	/**
	 *  暂停作业触发器
	 * @param triggerName
	 * @param triggerGroup
	 */
	public void pauseTrigger(String triggerName,String triggerGroup){
		
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		sched = actionutil.getDefaultScheduler();
		
		try {
			sched.pauseTrigger(triggerName, Scheduler.DEFAULT_GROUP);
			//System.out.println("pause : "+
			//		this.getState(sched.getTriggerState(triggerName, Scheduler.DEFAULT_GROUP)));
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 *  删除某一组监听器
	 * @param triggergroup
	 * @return 成功返回true  失败返回false
	 */
	public boolean deletegroup(String triggergroup){
		String[] triggers  = null;boolean bflag = false;
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		sched = actionutil.getDefaultScheduler();
		try{
			if(triggergroup!=null&&!"".equals(triggergroup)){
				triggers  = sched.getTriggerNames(triggergroup);
				for(int i=0;i<triggers.length;i++){
					Trigger tg = sched.getTrigger(triggers[i], triggergroup);
                    this.deleteJob(tg.toString(), triggergroup);					
				}
				
			}	
			bflag = true;
		}catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return bflag;
		
	}
	
	
	
	
	
	/**
	 *  删除作业触发器
	 * @param triggerName
	 * @param triggerGroup
	 * @return 成功返回true  失败返回false
	 */
	public boolean deleteJob(String triggerName,String triggerGroup){
		logItem = new LogItem();
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		sched = actionutil.getDefaultScheduler();
		
		boolean delcomple = false;
		try {

			String jobName = sched.getTrigger(triggerName, Scheduler.DEFAULT_GROUP).getJobName();
			String jobNameGroup = sched.getTrigger(triggerName, Scheduler.DEFAULT_GROUP).getJobGroup();
			delcomple = sched.deleteJob(jobName, jobNameGroup);
			
			logItem.setMethod("deleteJob");
			logItem.setLevel("info");
			logItem.setDesc("删除作业触发器成功");
			Log.write(logItem);
			//sched.unscheduleJob(jobName, jobNameGroup);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			logItem.setMethod("deleteJob");
			logItem.setLevel("info");
			logItem.setDesc("删除作业触发器失败");
	        logItem.setContent(e.toString());
			Log.write(logItem);
	//		e.printStackTrace();

		}

	//	System.out.println("delcomple : "+delcomple);
		
		return delcomple;
	}
	
	
	/**
	 *   恢复作业触发器
	 * @param triggerName
	 * @param triggerGroup
	 */
	public void resumeTrigger(String triggerName,String triggerGroup){
		
		if (actionutil == null) {
			synchronized (this) {
				actionutil = new ActionUtil();
			}
		}
		sched = actionutil.getDefaultScheduler();
		
		try {
			sched.resumeTrigger(triggerName, triggerGroup);
			//System.out.println("resume : "+
			//		this.getState(sched.getTriggerState(triggerName, triggerGroup)));
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 *   获取触发器状态
	 * @param triggerName
	 * @param triggerGroup
	 * @return  状态常量
	 */
	public String getTriggerState(String triggerName,String triggerGroup){
		
		String state = "";
		try {
			state =  this.getState(
					sched.getTriggerState(triggerName, triggerGroup));
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}
	
	
	

	/**
	 * 获取 命名 随机数
	 * @author cncsi juzhi
	 * @return
	 */
	public String getRandom() {

		Date thisDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = (String) sdf.format(thisDate);
		double rnum = Math.random() * 100000.0;
		long pnum = Math.round(rnum);
		String pass = dateString + String.valueOf(pnum);
		return pass;
	}

	/**
	 * 参数转化为JobDataMap
	 * @author cncsi juzhi
	 * @param argumap
	 */
	public void setJDMap(Map argumap) {
		
		//	jobdatamap = (JobDataMap) argumap;
		
		Iterator iter = argumap.keySet().iterator();  
		while (iter.hasNext()) {   
            Object key = iter.next();   
            Object value = argumap.get(key);   
            jobdatamap.put(key, value);
       } 
	}
	
	public String getState(int returnint){
		
		String state = "";
		switch (returnint){
		case -1 : state ="STATE_NONE" ; break;
		case 0 : state ="STATE_NORMAL" ; break;
		case 1 : state ="STATE_PAUSED" ; break;
		case 2 : state ="STATE_COMPLETE" ; break;
		case 3 : state ="STATE_ERROR" ; break;
		case 4 : state ="STATE_BLOCKED" ; break;
		}
		return state;
	}
	
}

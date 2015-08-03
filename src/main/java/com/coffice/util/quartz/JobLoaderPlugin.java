package com.coffice.util.quartz;


import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerListener;
import org.quartz.spi.SchedulerPlugin;
  
public class JobLoaderPlugin implements SchedulerPlugin {

	//private TriggerListener trgListener = null;
    
  
	/**
	 * quartz 初始化 添加触发器监听
	 * @author cncsi juzhi
	 * @return void
	 */
	
	private String trgListenersList = null; 
	private List<TriggerListener> trgListeners = null; 
	private Log logger = null;
	public void setTrgListenersList(String trgListenersList){
		this.trgListenersList = trgListenersList;
	}
	/**
	 * 初始化触发器监听
	 * @author cncsi juzhi
	 * @since 2008-12-25
	 */
	public void initialize(String pluginName, Scheduler scheduler)
			throws SchedulerException {
		//this.trgListenersList = pluginName;
		logger = LogFactory.getLog(CronTriggerListener.class); 
		trgListeners = parseTrgListeners();
		logger.info("Will load ListenerPlus 's Number is : "+trgListeners.size());  
		for (TriggerListener trgListener : trgListeners) {
			scheduler.addTriggerListener(trgListener);         //加载监听
			logger.info("Has Loaded Class of ListenerPlus is : "+trgListener.getClass());
		}

	}

	private List<TriggerListener> parseTrgListeners() {
		List<TriggerListener> listeners = new LinkedList<TriggerListener>();
		try {
								// 读取 quartz.properties 配置
			StringTokenizer tokenizer = new StringTokenizer(trgListenersList,",");  
													// 分割配置信息
			
			TriggerListener triggerListener = null;
			while (tokenizer.hasMoreTokens()) {
				
				Class<?> trgListenerClass = Class.forName(tokenizer.nextToken());
				triggerListener = (TriggerListener) trgListenerClass.newInstance();
				listeners.add(triggerListener);
			}
			return listeners;
		} catch (Exception ex) {
			
		}
		return listeners;
	}  

	public void shutdown() {
		// TODO Auto-generated method stub
	}

	public void start() {
		// TODO Auto-generated method stub
	}   
  
    
} 
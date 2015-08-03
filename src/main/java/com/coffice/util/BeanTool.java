package com.coffice.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class BeanTool {
	public static Object getBeans(String BeanName){
	    ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:coffice-servlet.xml"); 
	    return ac.getBean(BeanName); 
	}
	
}

package com.example.wsClientDemo.component;

import org.springframework.context.ApplicationContext;
public class SpringHelper {
	private static ApplicationContext applicationContext = null;
	
	public static void setApplicationContext(ApplicationContext appContext) {
		if(applicationContext == null){
            applicationContext  = appContext;
        }
	}
	public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
	
	public static Object getBean(String name){
        return getApplicationContext().getBean(name);

    }
	public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
	//通过name,以及Class返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}

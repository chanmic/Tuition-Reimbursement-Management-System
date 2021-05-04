package com.revature.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BeanFactory {
	private static Logger log = LogManager.getLogger(BeanFactory.class);
	private static BeanFactory bf = null;
	
	private BeanFactory() {
	}
	
	public static synchronized BeanFactory getFactory() {
		if(bf == null) {
			bf = new BeanFactory();
		}
		return bf;
	}
	
	public Object get(Class<?> inter, Class<?> clazz) {
		if(!clazz.isAnnotationPresent(Log.class)) {
			throw new RuntimeException("Class not annotated with @Log");
		}
		Object o = null;
		Constructor<?> c;
		try {
			c = clazz.getConstructor();
			o = Proxy.newProxyInstance(inter.getClassLoader(),
					new Class[]{inter},
					new LogProxy(c.newInstance()));
		} catch (Exception e) {
			log.error("Method threw exception: "+e);
			for(StackTraceElement s : e.getStackTrace()) {
				log.warn(s);
			}
			throw new RuntimeException(e);
		}
		return o;
	}
}

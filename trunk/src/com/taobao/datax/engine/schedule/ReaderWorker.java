/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.engine.schedule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.taobao.datax.common.constants.ExitStatus;
import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.exception.UnRerunableException;
import com.taobao.datax.common.plugin.LineSender;
import com.taobao.datax.common.plugin.Pluginable;
import com.taobao.datax.common.plugin.Reader;
import com.taobao.datax.engine.conf.PluginConf;

/**
 * {@link ReaderWorker} represents executor of {@link Reader} </br>
 * {@link Engine} use {@link ReaderWorker} to read data.</br>
 * 
 * */
public class ReaderWorker extends PluginWorker implements Runnable {
	private LineSender sender;

	private Method connect;

	private Method startRead;

	private Method finish;

	private static int globalIndex = 0;

	private static final Logger logger = Logger.getLogger(ReaderWorker.class);
	
	/**
	 * Constructor for {@link ReaderWorker}.
	 * 
	 * @param	pluginConf
	 * 			{@link PluginConf}
	 * 
	 * @param	myClass
	 * 			class of {@link Pluginable}
	 * 
	 * @throws	UnRerunableException
	 * 			if {@link Engine} loading {@link Pluginable} failed
	 * 
	 * 			{@link RerunableException}
	 * 			for other exceptions
	 * 
	 * */
	public ReaderWorker(PluginConf pluginConf, Class<?> myClass) {
		super(pluginConf, myClass);
		try {
			connect = myClass.getMethod("connect", new Class[] {});
			startRead = myClass
					.getMethod("startRead", new Class[] { Class
							.forName("com.taobao.datax.common.plugin.LineSender") });
			finish = myClass.getMethod("finish", new Class[] {});
		} catch (SecurityException e) {
			logger.error(e.getCause(), e);
			throw new UnRerunableException(e);
		} catch (NoSuchMethodException e) {
			logger.error(e.getCause(), e);
			throw new UnRerunableException(e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getCause(), e);
			throw new UnRerunableException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RerunableException(e);
		}
		this.setMyIndex(globalIndex++);
	}


	/**
	 * Read data, main execute logic code of {@link Reader} <br>
	 * NOTE: When catches exception, {@link ReaderWorker} exit process immediately.
	 * 
	 * */
	@Override
	public void run() {
		try {
			int iRetcode = (Integer) init.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader initialize failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) connect.invoke(myObject,
					new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader connect to DB failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) startRead.invoke(myObject,
					new Object[] { this.sender });
			if (iRetcode != 0) {
				logger.error("Reader startRead failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) finish.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader finish loading failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(ExitStatus.FAILED.value());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(ExitStatus.FAILED.value());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			System.exit(ExitStatus.RERUN.value());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ExitStatus.RERUN.value());
		}
	}
	
	public void setLineSender(LineSender sender) {
		this.sender = sender;
	}

}

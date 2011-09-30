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

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.exception.UnRerunableException;
import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.common.plugin.Writer;
import com.taobao.datax.engine.conf.PluginConf;
import com.taobao.datax.engine.constants.ExitStatus;

/**
 * Represents executor of a {@link Writer}.</br>
 * 
 * <p>{@link Engine} use {@link WriterWorker} to dump data.</p>
 * 
 * @see ReaderWorker
 * 
 * */
public class WriterWorker extends PluginWorker implements Runnable {
	private LineReceiver receiver;

	private Method init;

	private Method connectToDb;

	private Method startWrite;

	private Method commit;

	private Method finish;

	private static int globalIndex = 0;

	private static final Logger logger = Logger.getLogger(WriterWorker.class);

	/**
	 * Construct a {@link WriterWorker}.
	 * 
	 * @param	pluginConf
	 * 			PluginConf of {@link Writer}.
	 * 
	 * @param myClass
	 * 
	 * @throws RerunableException
	 * 
	 * @throws UnRerunableException
	 * 
	 */
	public WriterWorker(PluginConf pluginConf, Class<?> myClass)  {
		super(pluginConf, myClass);
		try {
			init = myClass.getMethod("init", new Class[] {});
			connectToDb = myClass.getMethod("connectToDb", new Class[] {});
			startWrite = myClass
					.getMethod("startWrite", new Class[] { Class
							.forName("com.taobao.datax.common.plugin.LineReceiver") });
			commit = myClass.getMethod("commit", new Class[] {});
			finish = myClass.getMethod("finish", new Class[] {});
		} catch (SecurityException e) {
			logger.error("DataX Initialize writer failed: " + e.getCause());
			throw new UnRerunableException(e);
		} catch (NoSuchMethodException e) {
			logger.error("DataX Initialize writer failed: " + e.getCause());
			throw new UnRerunableException(e);
		} catch (ClassNotFoundException e) {
			logger.error("DataX Initialize writer failed: " + e.getCause());
			throw new UnRerunableException(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RerunableException(e);
		}
		this.setMyIndex(globalIndex++);
	}

	public void setLineReceiver(LineReceiver receiver) {
		this.receiver = receiver;
	}

	/**
	 * Write data, main execute logic code of {@link Writer} <br>
	 * NOTE: When catches exception, {@link WriterWorker} exit process immediately
	 * 
	 * */
	@Override
	public void run() {
		try {
			int iRetcode = (Integer) init.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("DataX Initialize failed.");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) connectToDb.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("DataX connect to DataSink failed.");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) startWrite.invoke(myObject,
					new Object[] { receiver });
			if (iRetcode != 0) {
				logger.error("DataX starts writing data failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) commit.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("DataX commits transaction failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
			iRetcode = (Integer) finish.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("DataX do finish job failed .");
				System.exit(ExitStatus.RERUN.value());
				return;
			}
		} catch (IllegalArgumentException e) {
			logger.error("DataX Run failed, Due to " + e.getCause());
			e.printStackTrace();
			System.exit(ExitStatus.FAILED.value());
		} catch (IllegalAccessException e) {
			logger.error("DataX Run failed, Due to " + e.getCause());
			e.printStackTrace();
			System.exit(ExitStatus.FAILED.value());
		} catch (InvocationTargetException e) {
			logger.error("DataX Run failed, Due to " + e.getTargetException());
			e.printStackTrace();
			System.exit(ExitStatus.RERUN.value());
		}
	}

}

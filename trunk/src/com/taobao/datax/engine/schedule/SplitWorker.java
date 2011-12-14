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
import java.util.List;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.exception.UnRerunableException;
import com.taobao.datax.common.plugin.Pluginable;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.engine.conf.PluginConf;


/**
 * {@link SplitWorker} represents executor of {@link Splitter}
 * {@link Engine} use {@link SplitWorker} to split job.
 * 
 * */
public class SplitWorker extends PluginWorker {
	private Method init;

	private Method split;

	/**
	 * Constructor for {@link SplitWorker}
	 * 
	 * @param	pluginConf	
	 * 			@see {@link PluginConf}
	 * 
	 * @param	myClass
	 * 			class of {@link Splitter}
	 * 
	 * @throws	UnRerunableException
	 * 			if {@link Engine} loading {@link Pluginable} failed
	 * 
	 * 			{@link RerunableException}
	 * 			for other exceptions
	 *  
	 * */
	public SplitWorker(PluginConf pluginConf, Class<?> myClass) {
		super(pluginConf, myClass);
		try {
			init = myClass.getMethod("init", new Class[] {});
			split = myClass.getMethod("split", new Class[] {});
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new UnRerunableException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new UnRerunableException(e);
		} catch (Exception e) {
			throw new RerunableException(e);
		}
	}

	/**
	 * Split job into sub-jobs </br>
	 *
	 * @return
	 * 			a list of sub-jobs params
	 * 
	 * @throws	UnRerunableException
	 * 			if {@link Engine} loading {@link Pluginable} failed
	 * 
	 * 			{@link RerunableException}
	 * 			for other exceptions
	 *  
	 * */
	@SuppressWarnings("unchecked")
	public List<PluginParam> doSplit() {
		try {
			int iRetCode = (Integer) init.invoke(myObject, new Object[] {});
			if (iRetCode != 0)
				return null;
			return (List<PluginParam>) split.invoke(myObject, new Object[] {});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new UnRerunableException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new UnRerunableException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new UnRerunableException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RerunableException(e);
		}
	}
}

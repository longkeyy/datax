/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.plugin;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.exception.UnRerunableException;
import com.taobao.datax.common.plugin.ParamsKey.StreamWriter;
import com.taobao.datax.engine.plugin.DefaultPlugin;


/**
 * {@link Reader} represents a kind of {@link Plugin} to load data from source into DataX, such as mysql, hdfs.
 * 
 * @see {@link StreamWriter}.
 * 
 * */
public abstract class Reader extends DefaultPlugin{
	
	/**
	 * Initialize {@link Reader} before the reader work.
	 * 
	 * @return
	 *			0 for OK, others for failure.
	 * 
	 * @throws	RerunableException
	 * 					UnrerunableException
	 * 					if method init failed.
	 * 
	 * */
	public abstract int init() ;
	
	/**
	 * Connect to source DB, e.g mysql, HDFS.
	 * 
	 * @return
	 *			0 for OK, others for failure.
	 * 
	 * @throws	{@link	RerunableException}
	 * 					Method init failed, rerun DataX may resolve this problem, e.g. connect to database interrupted.
	 *					{@link UnRerunableException}
	 *					Method init failed, rerun DataX may resolve this problem, e.g. job configuration file format error.
	 *
	 * */
	public abstract int connect();
	
	/**
	 * Start to load data into DataX.
	 * 
	 * @param	resultWriter
	 * 			a handler used by {@link Reader} to load data from data source.
	 * 
	 * @return
	 * 			0 for OK, others for failure.
	 * 
	 * @throws	{@link	RerunableException}
	 * 			Method startLoad failed, rerun DataX may resolve this problem, e.g. connect to database interrupted.
	 *			{@link UnRerunableException}
	 *			Method startLoad failed, rerun DataX may resolve this problem, e.g. job configuration file format error.
	 * */
	public abstract int startRead(LineSender sender);
	
	
	/**
	 * Do some finish work, e.g release some resources.
	 * 
	 * @return
	 *			0 for OK, others for failure.
	 * 
	 * @throws	{@link	RerunableException}
	 * 					Method finish failed, rerun DataX may resolve this problem, e.g. connect to database interrupted.
	 *					{@link UnRerunableException}
	 *					Method finish failed, rerun DataX may resolve this problem, e.g. job configuration file format error.
	 * */
	public abstract int finish();
}

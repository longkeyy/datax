/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.writer.hbasewriter;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.common.util.SplitUtils;


public class HbaseWriterSplitter extends Splitter {
	private int splitnum = 1;

	private Logger logger = Logger.getLogger(HbaseWriterSplitter.class);
	@Override
	public int init() {
		splitnum = param.getIntValue(ParamsKey.HbaseWriter.concurrency, splitnum);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split() {
		List<PluginParam> v = new ArrayList<PluginParam>();	
		logger.info("hbase split number: " + splitnum);
		for (int i = 0; i < splitnum; i++) {
			PluginParam oParams = SplitUtils.copyParam(param);
			v.add(oParams);
		}				
		return v;
	}
}

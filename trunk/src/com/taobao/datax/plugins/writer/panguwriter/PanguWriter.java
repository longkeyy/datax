/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.writer.panguwriter;

import org.apache.log4j.Logger;

import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.plugins.writer.hdfswriter.HdfsWriter;


/**
 * Writer for Apsara Pangu
 * 
 */
public class PanguWriter extends HdfsWriter {
    private static final Logger log = Logger.getLogger(PanguWriter.class);

	@Override
	public int init() {
	    try {
            super.init();
        } catch (Exception e) {
            log.error("PanguWriter.init() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }
	
    @Override
    public int connect() {
        try {
            super.connect();
        } catch (Exception e) {
            log.error("PanguWriter.connect() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int startWrite(LineReceiver rr) {
        try {
            super.startWrite(rr);
        } catch (Exception e) {
            log.error("PanguWriter.startDump() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int commit() {
        try {
            super.commit();
        } catch (Exception e) {
            log.error("PanguWriter.commit() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int finish() {
        try {
            super.finish();
        } catch (Exception e) {
            log.error("PanguWriter.finish() ERR: " + e.getMessage(), e);
        }
        return PluginStatus.SUCCESS.value();
    }

}

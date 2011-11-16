/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.writer.streamwriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Writer;


public class StreamWriter extends Writer {
	private char FIELD_SPLIT = '\t';

	private String ENCODING = "UTF-8";

	private String PREFIX = "";
	
	private boolean printable = true;
	
	private String nullString = "";

	private Logger logger = Logger.getLogger(StreamWriter.class
			.getCanonicalName());
	
	@Override
	public int init() {
		this.FIELD_SPLIT = param.getCharValue(
				ParamsKey.StreamWriter.fieldSplit, '\t');
		this.ENCODING = param
				.getValue(ParamsKey.StreamWriter.encoding, "UTF-8");
		this.PREFIX = param.getValue(ParamsKey.StreamWriter.prefix, "");
		this.nullString = param.getValue(ParamsKey.StreamWriter.nullChar,
				this.nullString);
		this.printable = param.getBoolValue(ParamsKey.StreamWriter.print,
				this.printable);

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connect() {
		return PluginStatus.SUCCESS.value();
	}

	private String makeVisual(Line line) {
		if (line == null || line.getFieldNum() == 0) {
			return this.PREFIX + "\n";
		}

		int i = 0;
		String item = null;
		int num = line.getFieldNum();
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.PREFIX);
		for (i = 0; i < num; i++) {
			item = line.getField(i);
			if (null == item) {
				sb.append(nullString);
			} else {
				sb.append(item);
			}
			
			if (i != num - 1) {
				sb.append(FIELD_SPLIT);
			} else {
				sb.append('\n');
			}
		}
		
		return sb.toString();
	}

	@Override
	public int startWrite(LineReceiver linereceiver){
		Line line = null;
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					System.out, this.ENCODING));
			while ((line = linereceiver.getFromReader()) != null) {
				if (this.printable) {
					writer.write(makeVisual(line));
				} else {
					/* do nothing */
				}
			}
			writer.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		}

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int commit() {
		return 0;
	}

	@Override
	public int finish() {
		return 0;
	}

}

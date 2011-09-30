/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.reader.streamreader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineSender;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Reader;



/**
 * @author bazhen.csy
 *
 */
public class StreamReader extends Reader {	
	private char FIELD_SPLIT = '\t';
	
	private String ENCODING = "UTF-8";
	
	private String nullString = "";
		
	private static Logger logger = Logger.getLogger(StreamReader.class.getCanonicalName());
	/* (non-Javadoc)
	 * @see common.plugin.Reader#init()
	 */
	@Override
	public int init() {
		this.FIELD_SPLIT = param.getCharValue(ParamsKey.StreamReader.fieldSplit, '\t');
		this.ENCODING = param.getValue(ParamsKey.StreamReader.encoding, "UTF-8");
		this.nullString = param.getValue(ParamsKey.StreamReader.nullString, "");
		
		return PluginStatus.SUCCESS.value();
	}

	/* (non-Javadoc)
	 * @see common.plugin.Reader#connectToDb()
	 */
	@Override
	public int connectToDb() {
		return 0;
	}

	private String changeNull(final String item) {
		if (nullString != null && nullString.equals(item)) {
			return null;
		}
		return item;
	}
	/* (non-Javadoc)
	 * @see common.plugin.Reader
	 */
	@Override
	public int startRead(LineSender resultWriter){
		int ret = PluginStatus.SUCCESS.value();
		
		int previous = 0;
		String fetch = null;
		String split = String.valueOf(this.FIELD_SPLIT);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in, this.ENCODING));
			while ((fetch = reader.readLine()) != null) {
				previous = 0;
				Line line = resultWriter.createLine();
				for (int i = 0; i < fetch.length(); i++) {
					if (fetch.charAt(i) == this.FIELD_SPLIT) {
						line.addField(changeNull(fetch.substring(previous, i)));
						previous = i + 1;
					}
				}
				line.addField(fetch.substring(previous));
				resultWriter.sendToWriter(line);
			}
			resultWriter.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		}
		
		return ret;
	}

	/* (non-Javadoc)
	 * @see common.plugin.Reader#finish()
	 */
	@Override
	public int finish(){
		return PluginStatus.SUCCESS.value();
	}

}

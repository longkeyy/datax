/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineSender;
import com.taobao.datax.common.plugin.MetaData;
import com.taobao.datax.common.plugin.PluginMonitor;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Reader;
import com.taobao.datax.common.plugin.Writer;
import com.taobao.datax.engine.storage.Storage;


/**
 * A proxy which provides to database {@link Reader} plugin. <br/>
 * Usually this proxy is held by a Reader plugin. The proxy wraps ResultSet to line, then send line to {@link Writer}.
 * 
 * @see Reader
 * @see DbWriterProxy
 * 
 */
public class DbReaderProxy {
	private LineSender sender;

	protected PluginMonitor monitor;

	private int columnCount;

	private Map<String, SimpleDateFormat> dateFormatMap = new HashMap<String, SimpleDateFormat>();

	private SimpleDateFormat[] timeMap = null;

	private static final Logger logger = Logger.getLogger(DbReaderProxy.class);
	
	/**
	 * A static factory method which provides {@link DbReaderProxy}.
	 * 
	 * @param	sender
	 * 			a LineSender.
	 * 
	 * @return
	 * 			a DbReaderProxy instance.
	 * 
	 */
	public static DbReaderProxy newProxy(LineSender sender) {
		return new DbReaderProxy(sender);
	}

	/**
	 * A normal constructor of {@link DbReaderProxy}.
	 * 
	 * @param lineSender
	 * 			a LineSender.
	 * 
	 * @see DbReaderProxy#newProxy(LineSender)
	 * 
	 */
	public DbReaderProxy(LineSender lineSender) {
		this.sender = lineSender;
	}

	/**
	 * Set Monitor.
	 * 
	 * @param	iMonitor
	 * 			a PluginMonitor.
	 * 
	 */
	public void setMonitor(PluginMonitor iMonitor) {
		this.monitor = iMonitor;
	}
	
	/**
	 * Set date format when needs to deal with kinds of time format.
	 * 
	 * @param	dateFormatMap
	 * 			a map which key is String and value is {@link SimpleDateFormat}.
	 * 
	 */
	public void setDateFormatMap(Map<String, SimpleDateFormat> dateFormatMap) {
		this.dateFormatMap = dateFormatMap;
	}

	/**
	 * Send data in type of a {@link ResultSet} to {@link Writer}.
	 * 
	 * @param resultSet
	 * 			a {@link ResultSet}.
	 * 
	 * @throws SQLException
	 * 			if occurs SQLException.
	 * 
	 */
	public void sendToWriter(ResultSet resultSet) throws SQLException {
		String item = null;
		Timestamp timeString = null;
		setColumnCount(resultSet.getMetaData().getColumnCount());
		setColumnTypes(resultSet);
		while (resultSet.next()) {
			Line line = sender.createLine();
			try {
				/* TODO: date format need to handle by transfomer plugin */
				for (int i = 1; i <= columnCount; i++) {
					if (null != timeMap[i]) {
						timeString = resultSet.getTimestamp(i);
						if (null != timeString) {
							item = timeMap[i].format(timeString);
						} else {
							item = null;
						}
					} else {
						item = resultSet.getString(i);
					}

					if (null != item) {
						line.addField(item);
					} else {
						line.addField(null);
					}
				}
				boolean b = sender.sendToWriter(line);
				if (null != monitor) {
					if (b) {
						monitor.lineSuccess();
					} else {
						monitor.lineFail("Send one line failed!");
					}
				}
			} catch (SQLException e) {
				logger.error(e.getMessage() + "| One dirty line : " + line.toString('\t'));
			}
		}
		
	}

	/**
	 * Flush data in buffer (if exists) to {@link Storage}.
	 * 
	 * */
	public void flush() {
		if (sender != null) {
			sender.flush();
		}
	}
	
	private void setColumnTypes(ResultSet resultSet) throws SQLException {
		timeMap = new SimpleDateFormat[columnCount + 1];

		ResultSetMetaData rsmd = resultSet.getMetaData();
		
		for (int i = 1; i <= columnCount; i++) {
			String type = rsmd.getColumnTypeName(i).toLowerCase().trim();
			if (this.dateFormatMap.containsKey(type)) {
				timeMap[i] = this.dateFormatMap.get(type);
			}
		}
	}
	
	private void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
}

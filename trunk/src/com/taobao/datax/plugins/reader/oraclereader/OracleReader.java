/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.reader.oraclereader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.exception.UnRerunableException;
import com.taobao.datax.common.plugin.LineSender;
import com.taobao.datax.common.plugin.MetaData;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Reader;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.common.util.DbReaderProxy;
import com.taobao.datax.common.util.DbSource;
import com.taobao.datax.common.util.DbUtils;
import com.taobao.datax.common.util.OracleTnsAssist;


public class OracleReader extends Reader {
	public static final String ORACLE_READER_DB_POOL_KEY = "ORACE_DB_POOL_KEY";
	
	private Connection connection;

	private String username = "";

	private String password = "";

	private String ip;

	private String port = "1521";

	private String dbname;

	private String sql;

	private String charset = "utf-8";

	private String tnsname;
	
	private int concurrency;
	
	private int splitMode;

	private String connectKey;

	private static final Set<String> supportEncode = new HashSet<String>() {
		{
			add("utf-8");
			add("gbk");
			add("gb2312");
		}
	};

	private Logger logger = Logger.getLogger(OracleReader.class);

	@Override
	public int init() {
		this.sql = param.getValue(ParamsKey.OracleReader.sql, "").trim();
		
		//since oracle jdbc driver must ensure sql cannot end with ';'
		if (this.sql.endsWith(";")) {
			this.sql = this.sql.substring(0, this.sql.lastIndexOf(';'));
			param.putValue(ParamsKey.OracleReader.sql, this.sql);
		}
		
		this.charset = param
				.getValue(ParamsKey.OracleReader.encoding, "utf-8")
				.toLowerCase().trim();
		if (!isSupportEncode(charset)) {
			throw new UnRerunableException("encoding error");
		}

		this.ip = param.getValue(ParamsKey.OracleReader.ip, "");
		this.port = param.getValue(ParamsKey.OracleReader.port, this.port);
		this.username = param.getValue(ParamsKey.OracleReader.username, this.username);
		this.password = param.getValue(ParamsKey.OracleReader.password, this.password);
		this.dbname = param.getValue(ParamsKey.OracleReader.dbname);
		this.tnsname = param.getValue(ParamsKey.OracleReader.tnsFile, "");
		this.splitMode = param.getIntValue(ParamsKey.OracleReader.splitMod, 1);
		this.concurrency = param.getIntValue(ParamsKey.OracleReader.concurrency, 1);
		this.connectKey = param.getValue(this.ORACLE_READER_DB_POOL_KEY, "oracle_pool_key");
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int prepare(PluginParam param) {
		if (!StringUtils.isBlank(this.ip) && !StringUtils.isBlank(this.port)) {
			/* User defined ip & port */
			this.connectKey = DbSource.genKey(this.getClass(), this.ip, this.port, this.dbname);
			DbSource.register(this.connectKey, this.createProperties());
			this.connection = DbSource.getConnection(this.connectKey);
		} else {
			/* Non-user defined ip & port */
			List<Map<String, String>> details = OracleTnsAssist.newInstance(
					tnsname).search(dbname);
			/* try every connect info */
			for (Map<String, String> kv : details) {
				this.ip = kv.get(OracleTnsAssist.HOST_KEY);
				this.port = kv.get(OracleTnsAssist.PORT_KEY);
				this.dbname = kv.get(OracleTnsAssist.SID_KEY);
				this.connectKey = DbSource.genKey(this.getClass(), ip, port, dbname);
				try {
					DbSource.register(this.connectKey, this.createProperties());
					this.connection = DbSource.getConnection(this.connectKey);
				} catch (Exception e) {
					logger.error(String.format(
							"OracleReader try to connect %s:%s failed ",
							this.ip, this.port));
					continue;
				}
				if (null != this.connection) {
					break;
				}
			}
		}
		
		if (null == this.connection) {
			logger.error("OracleReader try to connect to database failed .");
			return PluginStatus.FAILURE.value();
		}
		
		param.putValue(this.ORACLE_READER_DB_POOL_KEY, this.connectKey);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split(PluginParam param) {
		List<PluginParam> params = null;		
		if (!StringUtils.isBlank(this.sql)) {
			/* user-defined sql */
			params = super.split(param);
		} else {
			/* non user-define sql */
			Splitter spliter = null;
			switch (this.splitMode) {
			case 0:
				logger.info("OracleReader use no-rowid split mechanism .");
				spliter = new OracleReaderTableSplitter(param);
				break;
			
			case 1:
				logger.info("OracleReader use rowid split mechanism .");
				spliter = new OracleReaderRowidSplitter(param);
				break;
				
			default:
				logger.info("OracleReader use ntile-rowid split mechanism .");
				spliter = new OracleReaderRowidSplitter(param);
				break;
			}
			spliter.init();
			params = spliter.split();
		}
		
		String sql = params.get(0).getValue(ParamsKey.OracleReader.sql);
		MetaData m = null;
		try {
			m = DbUtils.genMetaData(connection, sql);
			param.setMyMetaData(m);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RerunableException(e);
		}
		
		return params;
	}

	@Override
	public int connect() {
		connection = DbSource.getConnection(this.connectKey);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startRead(LineSender lineSender) {
		DbReaderProxy proxy = DbReaderProxy.newProxy(lineSender);
		proxy.setMonitor(getMonitor());
		proxy.setDateFormatMap(genDateFormatMap());

		String sql = param.getValue(ParamsKey.OracleReader.sql);
		logger.info(String.format("OracleReader start to query %s .", sql));
		ResultSet rs = null;
		try {
			rs = DbUtils.query(connection, sql);
			proxy.sendToWriter(rs);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RerunableException(e);
		}

		DbUtils.closeResultSet(rs);
		proxy.flush();
		getMonitor().setStatus(PluginStatus.READ_OVER);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int finish() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connection = null;
		}
		return PluginStatus.SUCCESS.value();
	}

	private boolean isSupportEncode(String encode) {
		boolean ret = false;
		if (supportEncode.contains(encode.toLowerCase())) {
			ret = true;
		}
		return ret;
	}

	private Map<String, SimpleDateFormat> genDateFormatMap() {
		Map<String, SimpleDateFormat> mapDateFormat;
		mapDateFormat = new HashMap<String, SimpleDateFormat>();
		mapDateFormat.clear();
		mapDateFormat.put("date", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"));
		mapDateFormat.put("timestamp", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"));
		return mapDateFormat;
	}

	private Properties createProperties() {
		Properties p = new Properties();
		String url = "jdbc:oracle:thin:@" + this.ip + ":" + this.port + "/"
				+ this.dbname;

		p.setProperty("driverClassName", "oracle.jdbc.driver.OracleDriver");
		p.setProperty("url", url);
		p.setProperty("username", this.username);
		p.setProperty("password", this.password);

		p.setProperty("maxActive", String.valueOf(concurrency + 2));
		p.setProperty("initialSize", String.valueOf(concurrency + 2));
		p.setProperty("maxIdle", "1");
		p.setProperty("maxWait", "1000");
		p.setProperty("defaultReadOnly", "true");
		p.setProperty("testOnBorrow", "true");
		p.setProperty("validationQuery", "select 1 from dual");

		this.logger.info(String.format("OracleReader try connection: %s .", url));
		return p;
	}
}

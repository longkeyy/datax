/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.reader.oraclereader;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.common.util.SplitUtils;



public class OracleReaderTableSplitter extends Splitter {
	private static final String SQL_WITHOUT_WHERE_PATTERN = "select {0} from {1} a";

	private static final String SQL_WITH_WHERE_PATTERN = "select {0} from {1} a where {2}";

	private String schema;
	
	private String where;
	
	private String columns;

	private List<String> tables;

	private Logger logger = Logger.getLogger(OracleReaderTableSplitter.class);
	
	public OracleReaderTableSplitter(PluginParam iParam){
		param = iParam;
	}
	
	@Override
	public int init() {
		logger.info("OracleReaderTableSpliter initialize begins .");

		tables = SplitUtils.splitTables(param.getValue(ParamsKey.OracleReader.tables));

		schema = param.getValue(ParamsKey.OracleReader.schema);
		
		where = param.getValue(ParamsKey.OracleReader.where, "");
		
		columns = param.getValue(ParamsKey.OracleReader.columns, "*");

		return PluginStatus.SUCCESS.value();
	}

	private String makeSql(String table) {
		String sql = "";

		if (StringUtils.isBlank(where)) {
			sql = format(SQL_WITHOUT_WHERE_PATTERN, this.columns, this.schema
					+ "." + table);
		} else {
			sql = format(SQL_WITH_WHERE_PATTERN, this.columns, this.schema
					+ "." + table, where);
		}
		return sql;
	}

	@Override
	public List<PluginParam> split() {
		List<PluginParam> params = new ArrayList<PluginParam>();

		for (String table : tables) {
			String sql = makeSql(table);
			PluginParam iParam = SplitUtils.copyParam(this.param);
			iParam.putValue(ParamsKey.OracleReader.sql, sql);
			params.add(iParam);
		}

		return (params);
	}

}

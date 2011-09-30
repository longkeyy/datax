/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.writer.oraclewriter;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Writer;
import com.taobao.datax.common.util.StrUtils;


public class OracleWriter extends Writer {

	/* \001 is field split, \002 is line split */
    private static final char[] replaceChars = {'\001', 0, '\002', 0};
    
    private Logger logger = Logger.getLogger(OracleWriter.class);
    
	private String password;

	private String username;

	private String dbname;

	private String table;

	private static final char SEP = '\001';
	
	private static final char BREAK = '\002';

	private String pre;

	private String post;

	private String dtfmt;

	private String encoding;

	private String colorder;

	private String limit;
	
	private long concurrency;

	private String logon;

	private long p;

	private int save2server;

	private int commit2server;

	private long skipindex;

	@Override
	public List<PluginParam> split(PluginParam param) {
		OracleWriterSplitter spliter = new OracleWriterSplitter();
		spliter.setParam(param);
		spliter.init();
		return spliter.split();
	}

	@Override
	public int prepare(PluginParam param) {
		try {
			if (!pre.isEmpty()) {
				p = OracleWriterJni.getInstance().oracle_dumper_init(logon,
						table, String.valueOf(SEP), pre, post, dtfmt, encoding,
						colorder, limit, concurrency, skipindex);
				OracleWriterJni.getInstance().oracle_dumper_connect(p);
				OracleWriterJni.getInstance().oracle_dumper_predump(p, 0);
				OracleWriterJni.getInstance().oracle_dumper_finish(p, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int post(PluginParam param) {
		// TODO Auto-generated method stub
		try {
			if (!post.isEmpty()) {
				p = OracleWriterJni.getInstance().oracle_dumper_init(logon,
						table, String.valueOf(SEP), pre, post, dtfmt, encoding,
						colorder, limit, concurrency, skipindex);
				OracleWriterJni.getInstance().oracle_dumper_connect(p);
				OracleWriterJni.getInstance().oracle_dumper_finish(p, 3); // end
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int init() {
		password = param.getValue(ParamsKey.OracleWriter.password, "");
		username = param.getValue(ParamsKey.OracleWriter.username, "");

		dbname = param.getValue(ParamsKey.OracleWriter.dbname, "");
		table = param.getValue("schema") + "."
				+ param.getValue(ParamsKey.OracleWriter.table, "");
		dtfmt = StrUtils
				.removeSpace(
						param.getValue(ParamsKey.OracleWriter.dtfmt,
								""), ",");
		pre = param.getValue(ParamsKey.OracleWriter.pre, "");
		post = param.getValue(ParamsKey.OracleWriter.post, "");
		encoding = param
				.getValue(ParamsKey.OracleWriter.encoding, "UTF-8");
		colorder = StrUtils.removeSpace(
				param.getValue(ParamsKey.OracleWriter.colorder, ""),
				",");
		limit = param.getValue(ParamsKey.OracleWriter.limit, "");
		concurrency = param.getIntValue(ParamsKey.OracleWriter.concurrency, 1);
		
		commit2server = 50000;
		save2server = 1000;
		skipindex = 0;
		logon = username + "/" + password + "@" + dbname;

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connectToDb() {
		try {
			p = OracleWriterJni.getInstance().oracle_dumper_init(logon, table,
					String.valueOf(SEP), "", post, dtfmt, encoding, colorder,
					limit, concurrency, skipindex);
			OracleWriterJni.getInstance().oracle_dumper_connect(p);
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startWrite(LineReceiver resultHandler) {
		try {
			OracleWriterJni.getInstance().oracle_dumper_predump(p, 1);
			Line line = null;
			String field = null;
			int iCount = 0;
			int iCount1 = 0;

			StringBuilder sb = new StringBuilder(1024000);
			while ((line = resultHandler.getFromReader()) != null) {
				int num = line.getFieldNum();
				for (int i = 0; i < num; i++) {
					field = line.getField(i);
					if (null != field) {
						sb.append(StrUtils.replaceChars(field, replaceChars));
					} /*
						else {
						sb.append("");
					}
					*/
					sb.append(this.SEP);
				}
				sb.delete(sb.length() - 1, sb.length());
				sb.append(this.BREAK);
				
				if (iCount == save2server) {
					OracleWriterJni.getInstance().oracle_dumper_dump(p,
							sb.toString());
					sb.setLength(0);
					iCount = 0;
				}
				iCount++;

				if (iCount1 == commit2server) {
					sb.append(this.BREAK + "1y9i8x7i0a3o2*5" + this.BREAK);
					OracleWriterJni.getInstance().oracle_dumper_dump(p,
							sb.toString());
					sb.setLength(0);
					commit();
					iCount1 = 0;
				}
				iCount1++;
			}
			sb.append(this.BREAK + "1y9i8x7i0a3o2*5" + this.BREAK);
			OracleWriterJni.getInstance().oracle_dumper_dump(p, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}

		return PluginStatus.SUCCESS.value();
	}

	public static byte[] charToByte(char ch) {
		int temp = (int) ch;
		byte[] b = new byte[2];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Integer(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}

	@Override
	public int commit() {
		try {
			int ret = OracleWriterJni.getInstance().oracle_dumper_commit(p);
			if (0 != ret) {
				return PluginStatus.FAILURE.value();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int finish() {
		try {
			int discard = OracleWriterJni.getInstance().oracle_dumper_finish(p, 1);
			this.getMonitor().setFailedLines(discard);
		} catch (Exception e) {
			e.printStackTrace();
			return PluginStatus.FAILURE.value();
		}

		return PluginStatus.SUCCESS.value();
	}
}

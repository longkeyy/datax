/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */

package com.taobao.datax.plugins.writer.hbasewriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;

public class HBaseProxy {
	private Configuration config;

	private HTable htable;

	private HBaseAdmin admin;

	private HTableDescriptor descriptor;

	private Put p;

	private Logger logger = Logger.getLogger(HBaseProxy.class);

	public HBaseProxy(String hbase_conf, String tableName, boolean autoflush)
			throws IOException {
		Configuration conf = new Configuration();
		conf.addResource(new Path(hbase_conf));
		config = new Configuration(conf);
		htable = new HTable(config, tableName);
		admin = new HBaseAdmin(config);
		init(autoflush);
	}

	private void init(boolean autoflush) throws IOException {
		descriptor = htable.getTableDescriptor();
		htable.setAutoFlush(autoflush);
	}

	public void setHTable(String tableName) throws IOException {
		this.htable = new HTable(config, tableName);
	}

	public void setAutoFlush(boolean autoflush) {
		this.htable.setAutoFlush(autoflush);
	}

	public boolean check() throws RerunableException, IOException {
		if (admin.isMasterRunning() == false) {
			throw new RerunableException("hbase master is not running!");
		}
		if (admin.tableExists(htable.getTableName()) == false) {
			throw new RerunableException("hbase table " + htable.getTableName()
					+ " is not existed!");
		}
		if (admin.isTableAvailable(htable.getTableName()) == false) {
			throw new RerunableException("hbase table " + htable.getTableName()
					+ " is not available!");
		}
		if (admin.isTableEnabled(htable.getTableName()) == false) {
			throw new RerunableException("hbase table " + htable.getTableName()
					+ " is disable!");
		}

		return true;
	}

	public void close() throws IOException {
		htable.close();
	}

	public void deleteTable() throws IOException {
		Scan s = new Scan();
		ResultScanner scanner = htable.getScanner(s);
		logger.info("deleting old datas in your table...");
		for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
			htable.delete(new Delete(rr.getRow()));
		}
		scanner.close();
	}

	public void truncateTable() throws IOException {
		admin.disableTable(htable.getTableName());
		admin.deleteTable(htable.getTableName());
		admin.createTable(descriptor);
	}

	public void flushCommits() throws IOException {
		htable.flushCommits();
	}

	public void addPut(String field, String column, String encode)
			throws IOException {
		try {
			p.add(column.split(":")[0].getBytes(),
					column.split(":")[1].getBytes(), field.getBytes(encode));
		} catch (UnsupportedEncodingException e) {
			p.add(column.split(":")[0].getBytes(),
					column.split(":")[1].getBytes(), field.getBytes());
		}

	}

	public void generatePut(String rowkey) {
		p = new Put(rowkey.getBytes());
	}

	public void putLine() throws IOException {
		htable.put(p);
	}
}

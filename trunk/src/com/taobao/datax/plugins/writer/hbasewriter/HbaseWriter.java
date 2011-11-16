/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */

package com.taobao.datax.plugins.writer.hbasewriter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Writer;

public class HbaseWriter extends Writer {
	private String tablename;

	private String encode = "utf-8";

	private Object proxy;

	private String[] keys;

	private int[] values;

	private int rowkey;

	private Logger logger = Logger.getLogger(HbaseWriter.class);

	@Override
	public List<PluginParam> split(PluginParam param) {
		HbaseWriterSplitter spliter = new HbaseWriterSplitter();
		spliter.setParam(param);
		spliter.init();
		return spliter.split();
	}

	@Override
	public int prepare(PluginParam param) {
		try {
			create(param);
			int delete_mode = param.getIntValue(ParamsKey.HbaseWriter.delMode,
					1);
			switch (delete_mode) {
			case 0:
				break;
			case 1:
				truncateTable();
				break;
			case 2:
				deleteTables();
				break;
			default:
				break;
			}
			finish();
		} catch (MasterNotRunningException e) {
			logger.error("not found master: ", e);
			return PluginStatus.FAILURE.value();
		} catch (IOException e) {
			logger.error("IO Exception: ", e);
			return PluginStatus.FAILURE.value();
		} catch (RerunableException e) {
			throw e;
		} catch (Exception e) {
			logger.error("other exception: ", e);
			return PluginStatus.FAILURE.value();
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int post(PluginParam param) {
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int init() {
		encode = getParam().getValue(ParamsKey.HbaseWriter.encoding, "UTF-8");
		rowkey = Integer.valueOf(getParam().getValue(
				ParamsKey.HbaseWriter.rowkey));
		keys = getParam().getValue(ParamsKey.HbaseWriter.columns_key)
				.split(",");
		String[] tmp = getParam().getValue(ParamsKey.HbaseWriter.columns_value)
				.split(",");
		values = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			values[i] = Integer.valueOf(tmp[i]);
		}
		if (values.length != keys.length) {
			logger.error("values.length must equal keys.length");
			return PluginStatus.FAILURE.value();
		}
		try {
			create(getParam());
		} catch (MasterNotRunningException e) {
			logger.error("not found master: ", e);
			return PluginStatus.FAILURE.value();
		} catch (IOException e) {
			logger.error("IO Exception: ", e);
			return PluginStatus.FAILURE.value();
		} catch (Exception e) {
			logger.error("other exception: ", e);
			return PluginStatus.FAILURE.value();
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connect() {
		logger.debug("connect start!");
		logger.debug("connect finish!");
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startWrite(LineReceiver resultHandler) {
		Line line = null;
		try {
			Method generatePut = proxy.getClass().getMethod("generatePut",
					String.class);
			generatePut.setAccessible(true);
			Method addPut = proxy.getClass().getMethod("addPut",
					new Class[] { String.class, String.class, String.class });
			addPut.setAccessible(true);
			Method putLine = proxy.getClass().getMethod("putLine");
			putLine.setAccessible(true);
			while ((line = resultHandler.getFromReader()) != null) {
				int num = line.getFieldNum();
				if (keys.length > num) {
					throw new RerunableException(
							"keys.length > Line.num: keys.length is "
									+ keys.length + " and Line.num is " + num);
				}
				generatePut.invoke(proxy, line.getField(rowkey));
				for (int i = 0; i < keys.length; i++) {
					String field = line.getField(values[i]);
					String column = keys[i];
					addPut.invoke(proxy, new Object[] { field, column, encode });
				}
				putLine.invoke(proxy);
			}
		} catch (Exception e) {
			throw new RerunableException("htable put failed: ", e);
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int commit() {
		try {
			proxy.getClass().getMethod("flushCommits").invoke(proxy);
		} catch (Exception e) {
			throw new RerunableException("htable flush failed: ", e);
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int finish() {
		try {
			proxy.getClass().getMethod("close").invoke(proxy);
		} catch (Exception e) {
			throw new RerunableException(e);
		}
		return PluginStatus.SUCCESS.value();
	}

	private void deleteTables() {
		try {
			proxy.getClass().getMethod("deleteTable").invoke(proxy);
		} catch (Exception e) {
			logger.error("exception: ", e);
			throw new RerunableException(e);
		}
	}

	private void truncateTable() {
		try {
			proxy.getClass().getMethod("truncateTable").invoke(proxy);
		} catch (Exception e) {
			logger.error("exception: ", e);
			throw new RerunableException(e);
		}
	}

	/**
	 * This method create a proxy of our HBaseProxy, which use a new ClassLoader
	 * Because the default hdfs's version yunti-0.19.2, and our hbase's hdfs
	 * version is more than 0.20.2. So we must use a new Class loader to our
	 * HBaseProxy.
	 * 
	 * @param param
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private boolean create(PluginParam param) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InvocationTargetException {
		ClassLoader jarloader = generateJarLoader();
		Class Proxy = Class.forName(
				"com.taobao.datax.plugins.writer.hbasewriter.HBaseProxy", true,
				jarloader);
		tablename = param.getValue(ParamsKey.HbaseWriter.htable);
		String path = param.getValue(ParamsKey.HbaseWriter.hbase_conf);
		Constructor con = Proxy.getConstructor(new Class[] { String.class,
				String.class, boolean.class });
		Boolean autoflush = param.getBoolValue(ParamsKey.HbaseWriter.autoFlush,
				true);
		proxy = con.newInstance(new Object[] { path, tablename, autoflush });
		Method checkadmin = proxy.getClass().getMethod("check");
		return (Boolean) checkadmin.invoke(proxy);
	}

	/**
	 * This method add jars to a new Class loader.
	 * 
	 * @return a new ClassLoader
	 * @throws IOException
	 */
	private ClassLoader generateJarLoader() throws IOException {
		String libPath = System.getProperty("java.ext.dirs");
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().startsWith("hadoop-0.19.2"))
					return false;
				else
					return pathname.getName().endsWith(".jar");
			}
		};
		File[] jars1 = new File(libPath).listFiles(filter);
		File[] jars2 = new File("extend").listFiles(filter);
		File[] jars3 = new File("engine").listFiles(filter);
		File[] jars4 = new File("plugins").listFiles(filter);
		URL[] jarUrls = new URL[jars1.length + jars2.length + jars3.length
				+ jars4.length];

		int k = 0;
		for (int i = 0; i < jars1.length; i++) {
			jarUrls[k++] = jars1[i].toURI().toURL();
		}
		for (int i = 0; i < jars2.length; i++) {
			jarUrls[k++] = jars2[i].toURI().toURL();
		}
		for (int i = 0; i < jars3.length; i++) {
			jarUrls[k++] = jars3[i].toURI().toURL();
		}
		for (int i = 0; i < jars4.length; i++) {
			jarUrls[k++] = jars4[i].toURI().toURL();
		}
		ClassLoader jarloader = new URLClassLoader(jarUrls, null);
		for (URL jar : jarUrls) {
			System.out.println(jar);
		}
		System.out.println("jarloader:" + jarloader);
		return jarloader;
	}
}

/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.taobao.datax.common.plugin.Plugin;
import com.taobao.datax.plugins.reader.mysqlreader.MysqlReader;

/**
 * A tool class which wraps DataSource. It provides methods to create a DataSource, get data source,
 * get database connection so that other plugins can use these methods to work easily.<br>
 * This class is mainly designed to avoid problem while occurs while 
 * data source and destination is the same kind of DataBase. 
 * 
 * for usage, see {@link MysqlReader}
 *
 */
public class DbSource {
	private static Logger logger = Logger.getLogger(DbSource.class);

	
	public static HashMap<String, DataSource> sourceInfoMap = new HashMap<String, DataSource>();

	private DbSource() {
	}
	
	/**
	 * A synchronized static method which create {@link DataSource}.
	 * @param 	clazz
	 * 					{@link Plugin} class 
	 * 
	 * @param		ip
	 * 					database host ip address
	 * 
	 * @param		port
	 * 					database port
	 * 
	 * @param p
	 * 					Properties file.
	 * 
	 * @return
	 * 					true if create DataSource successfully, false if failed.
	 * 
	 * @throws	
	 * 					IllegalStateException
	 * 
	 */
	public static boolean register(Class<? extends Plugin> clazz,
			String ip, String port, String dbname, Properties p) {

		String id = genKey(clazz, ip, port, dbname);
		
		return register(id, p);
	}

	/**
	 * A synchronized static method which create {@link DataSource}.
	 * NOTE: Client must make sure all connections to the same database should share the same value
	 *  a suggestion: use genKey we provide below
	 * 
	 * @param		key
	 * 					key to query database source
	 * 
	 * @param 	p
	 * 					Properties file.
	 * 
	 * @return
	 * 					true if create DataSource successfully, false if failed.
	 * 
	 * @throws	
	 * 					IllegalStateException
	 * 
	 */
	public static synchronized boolean register(String key, Properties p) {
		boolean succeed = false;
		
		if (!sourceInfoMap.containsKey(key)) {
			BasicDataSource dataSource = null;
			try {
				dataSource = (BasicDataSource) BasicDataSourceFactory
						.createDataSource(p);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(String.format("Key [%s] register database pool failed .", key));
				throw new IllegalStateException(e.getCause());
			}
			if (null != dataSource) {
				sourceInfoMap.put(key, dataSource);
				logger.info(String.format("Key [%s] register database pool successfully .", key));
				succeed = true;
			} else {
				logger.error(String.format("Key [%s] register database pool failed .", key));
			}
		} else {
			logger.error(String.format("Key [%s] already in database pool .", key));
		}

		return succeed;
	}
	
	/**
	 * A synchronized static method which gets DataSoucre by the identification.
	 * 
	 * @param 	clazz
	 * 					{@link Plugin} class 
	 * 
	 * @param		ip
	 * 					database host ip address
	 * 
	 * @param		port
	 * 					database port
	 * 
	 * @param		dbname
	 * 					database name
	 * 
	 * @param p
	 * 					Properties file.
	 * 
	 * 
	 * @return
	 * 			a binded DataSource.
	 * 
	 */
	public static DataSource getDataSource(Class<? extends Plugin> clazz,
			String ip, String port, String dbname) {
		return getDataSource(genKey(clazz, ip, port, dbname));
	}
	
	
	/**
	 * A synchronized static method which gets DataSoucre by the identification.
	 * 
	 * @param key
	 * 			unique identification binding with DataSource.
	 * 
	 * @return
	 * 			a binded DataSource.
	 * 
	 */
	public static synchronized DataSource getDataSource(String key) {
		DataSource source = sourceInfoMap.get(key);
		if (null == source){
			throw new IllegalArgumentException(String.format(
					"Cannot get DataSource specified by key [%s] .", key));
		}
		return source;
	}
	
	
	/**
	 * A synchronized static method which gets database connection by the identification.
	 * 
	 * @param 	clazz
	 * 					{@link Plugin} class 
	 * 
	 * @param		ip
	 * 					database host ip address
	 * 
	 * @param		port
	 * 					database port
	 * 
	 * @param		dbname
	 * 					database name
	 * 
	 * @return
	 * 			a database connection.
	 * 
	 * @throws	
	 * 						{@link IllegalStateException}
	 * 						Cannot connect to DataBase
	 * 
	 * 						{@link IllegalArgumentException}
	 * 						Connect id is not registered
	 * 
	 */
	public static Connection getConnection(Class<? extends Plugin> clazz,
			String ip, String port, String dbname ) {
			return getConnection(genKey(clazz, ip, port, dbname));
	}
	
	/**
	 * A synchronized static method which gets database connection by the identification.
	 * 
	 * @param id
	 * 			unique identification binding with DataSource.
	 * 
	 * @return
	 * 			a database connection.
	 * 
	 * @throws	
	 * 						{@link IllegalStateException}
	 * 						Cannot connect to DataBase
	 * 
	 * 						{@link IllegalArgumentException}
	 * 						Connect id is not registered
	 * 
	 */
	public static synchronized Connection getConnection(String id) {
		Connection c = null;
		BasicDataSource dataSource = (BasicDataSource) sourceInfoMap.get(id);
		try {
			c = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(String.format("Key [%s]  connect to database pool failed .", id));
			throw new IllegalArgumentException(e.getCause());
		}
		if (null != c) {
			logger.info(String.format("Key [%s] connect to database pool successfully .", id));
		} else {
			logger.error(String.format("Key [%s]  connect to database pool failed .", id));
			throw new IllegalArgumentException(String.format("Connection key [%d] error .", id));
		}
		return c;
	}

	/**
	 * generate key to get {@link DataSource} 
	 * 
	 * NOTE: Client must make sure all connections to the same database should share the same value
	 *  a suggestion: use genKey we provide below, we used MD5 encryption method.
	 * 
	 * */
	public static String genKey(Class<? extends Plugin> clazz, String ip,
			String port, String dbname) {
		String str =  clazz.getCanonicalName() + "_" + ip + "_" + port + "_" + dbname;
		return md5(str);
	}

	private static String md5(String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(key.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer(32);
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0){
					i += 256;
				}
				if (i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return key;
		}
	}
}

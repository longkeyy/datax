/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.engine.schedule;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.log4j.Logger;

/**
 * A special class to load the jar file.
 * 
 *@see java.lang.ClassLoader
 *
 */
public class JarLoader extends ClassLoader {
	private static final Logger log = Logger.getLogger(JarLoader.class);

	private String jarfile;
	private String fileType = ".class";

	/**
	 * @param 	jarfile
	 * 			jar name.
	 * 
	 */
	public JarLoader(String jarfile) {
		this.jarfile = jarfile;
	}
	
	/**
	 * Find Class Object through the name if exists.
	 * 
	 * @param	name
	 * 			name of class.
	 * 
	 * @return	resultClass
	 * 			the founded Class Object.
	 * 
	 */
	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> resultClass = null;
		String pathName = name.replace('.', '/');
		byte[] data = loadClassData(pathName);
		log.debug("name: "+name);
		log.debug("pathName: "+pathName);
		resultClass = defineClass(name, data, 0, data.length);
		return resultClass;
	}

	/**
	 * LoadClassData from the appointed class to a byte array.
	 * 
	 * @param	classname
	 * 			name of class.
	 * 
	 * @return	byte[]
	 * 			the data which loaded from the class.
	 * 
	 * @throws	ClassNotFoundException
	 * 			if the appointed class is not founded.
	 * 
	 */
	public byte[] loadClassData(String classname) throws ClassNotFoundException {

		InputStream fis = null;
		JarInputStream jar = null;
		ByteArrayOutputStream baos = null;
		byte[] data = null;
		try {
			jar = new JarInputStream(new FileInputStream(jarfile));
			JarEntry entry;
			while ((entry = jar.getNextJarEntry()) != null) {
				if (entry.getName().toLowerCase().endsWith(fileType)) {
					log.debug("entryname:" + entry.getName());
					log.debug("classname:" + classname);

					if (entry.getName().equals(classname + fileType)) 
						data = getResourceData(jar);					
				}
			}

		} catch (IOException e) {
			throw new ClassNotFoundException("Can't find the Class ["+classname+"] in "+ jarfile +".");
		} finally {
			try {
				if (baos != null) {
					baos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
			}
		}
		return data;
	}

	/**
	 * 
	 * @param	jar
	 * 			JarInputStream jar.
	 * 
	 * @return	byte[]
	 * 			byte array which getted from jar.
	 *  
	 * @throws	IOException
	 * 			if error occur when get data from the JarInputStream jar.
	 * 
	 */
	private byte[] getResourceData(JarInputStream jar) throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int size;
		while (jar.available() > 0) {
			size = jar.read(buffer);
			if (size > 0) {
				data.write(buffer, 0, size);
			}
		}
		return data.toByteArray();
	}
//
//	public static void main(String[] args) throws Exception {
//		JarLoader loader = new JarLoader(
//				"D:\\work\\project\\data_exchange\\DataExchangeWorker\\dataexchange\\build\\dist1\\hdfsspliter-1.0.0.jar");
//		Class<?> objClass = loader
//				.loadClass("com.taobao.datax.plugins.spliter.hdfsspliter.HdfsDirSpliter");
//		System.out.println(objClass.getName());
//		System.out.println(objClass.getClassLoader());
//	}
}

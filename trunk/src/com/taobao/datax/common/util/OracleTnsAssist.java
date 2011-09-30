/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 *
 * A assistant class to parse a tnsfile
 * 
 * Usage:
 * OracleTnsAssist assist = OracleTnsAssist.newInstance("/home/hedgehog/tnsnames.ora");
 * List<Map<String, String>> details = assist.search("tnsname");
 * 
 * details:
 * [{'SID'='dwdb', 'HOST'='192.168.0.1', 'PORT'='1521'}, 
 *  {'SID'='dwdb', 'HOST'='192.168.0.2', 'PORT'='1521'}, 
 *  {'SID'='dwdb', 'HOST'='192.168.0.3', 'PORT'='1521'},]
 *  
 * */
public class OracleTnsAssist {
	public static final String HOST_KEY = "HOST";

	public static final String PORT_KEY = "PORT";

	public static final String SID_KEY = "SID";

	private static final Pattern TNS_NAME_PATTERN = Pattern
			.compile("^(\\w+)\\s*=?");

	private static final Pattern TNS_LOCATION_PATTERN = Pattern
			.compile(
					"\\(\\s*HOST\\s*=\\s*([\\w_\\-\\.]+)\\s*\\)\\s*\\(\\s*PORT\\s*=\\s*(\\d+)\\s*\\)",
					Pattern.CASE_INSENSITIVE);

	private static final Pattern TNS_SID_PATTERN = Pattern.compile(
			"(SERVICE_NAME|SID)\\s*=\\s*(\\w+)", Pattern.CASE_INSENSITIVE);

	private String tnsFile;

	private OracleTnsAssist(String filename) {
		this.tnsFile = filename;
	}

	/**
	 * Factory method to create a new {@link OracleTnsAssist}.
	 * 
	 * @param	filename
	 * 			tns file
	 * 
	 * @return	a new instance of {@link OracleTnsAssist}
	 * */
	public static OracleTnsAssist newInstance(String filename) {
		return new OracleTnsAssist(filename);
	}
	
	/**
	 * Search one tns-id in one tns file.
	 * 
	 * @param	tnsId
	 * 			tns id to search 
	 * 
	 * @return
	 * 			a list of tns configuration <br> 
	 * 			{"HOST": "hostname", "PORT": "port", "SID": "sid"}.
	 * 
	 * */
	public List<Map<String, String>> search(String tnsId) {
		List<Map<String, String>> connDetails = new ArrayList<Map<String,String>>();
		
		try {
			connDetails = this.parseConnDetail(this.grepConnDetail(tnsId));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return connDetails;
	}

	private List<String> parseTnsFile(String tnsFile) throws IOException {
		List<String> tns = new ArrayList<String>();
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			reader = new BufferedReader(new FileReader(tnsFile));
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				/* ignore comment line */
				if (isComment(line))
					continue;
				
				/* new line begins 
				 * NOTE:  a non-blank start line indicates new tns item begins
				 * 1 one blank line 
				 * 2 new tns identifier
				 * */
				if (TNS_NAME_PATTERN.matcher(line).find() &&
						sb.length() > 0) {
					tns.add(sb.toString());
					sb.setLength(0);
					sb.append(line);
					continue;
					/* */
				}
				sb.append(line);
			}
			if (sb.length() > 0) {
				tns.add(sb.toString());
			}
			return tns;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	private String grepConnDetail(String dbname) throws IOException {
		List<String> connDetails = this.parseTnsFile(this.tnsFile);
		for (String perConnDetail : connDetails) {
			Matcher matcher = TNS_NAME_PATTERN.matcher(perConnDetail);
			if (matcher.find() && matcher.group(1).equalsIgnoreCase(dbname)) {
				return perConnDetail;
			}
		}
		return "";
	}

	private List<Map<String, String>> parseConnDetail(String content) {
		List<Map<String, String>> connDetails = new ArrayList<Map<String, String>>();

		if (StringUtils.isBlank(content))
			return connDetails;

		Matcher sidMatcher = TNS_SID_PATTERN.matcher(content);
		if (!sidMatcher.find())
			throw new IllegalArgumentException(String.format(
					"Illegal TNS format, options SID missing : %s .", content));
		
		String sid = sidMatcher.group(2);
		Matcher locationMatcher = TNS_LOCATION_PATTERN.matcher(content);
		if (!locationMatcher.find())
			throw new IllegalArgumentException(String.format(
					"Illegal TNS format, options HOST/PORT missing : %s .", content));
		do {
			String host = locationMatcher.group(1);
			String port = locationMatcher.group(2);
			Map<String, String> location = new HashMap<String, String>();
			location.put(HOST_KEY, host);
			location.put(PORT_KEY, port);
			location.put(SID_KEY, sid);
			connDetails.add(location);
		} while (locationMatcher.find());
		
		return connDetails;
	}

	private boolean isBlankLine(String line) {
		if (StringUtils.isBlank(line)) {
			return true;
		}
		return false;
	}

	private boolean isComment(String line) {
		return line.trim().startsWith("#");
	}
//
//	public static void main(String[] args) throws IOException {
//		OracleTnsAssist assist = OracleTnsAssist.newInstance("/home/hedgehog/tnsnames.ora");
//		System.out.println(assist.search("dwdb"));
//		return;
//	}
}

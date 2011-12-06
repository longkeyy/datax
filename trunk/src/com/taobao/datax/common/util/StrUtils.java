/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;

/**
 * A tool class which used to deal with strings. 
 * Usually unified the style of strings.
 * 
 *@see ArrayUtils
 *
 */
public class StrUtils {
	private static final Logger log = Logger.getLogger(StrUtils.class);

	private static final Pattern MAIL_PATTERN = Pattern
			.compile("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");

	private static final Pattern VARIABLE_PATTERN = Pattern
			.compile("(\\$)\\{?(\\w+)\\}?");

	private static String SYSTEM_ENCODING = System.getProperty("file.encoding");

	private static final DateFormat TS_STRING_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.US);

	private static final DateFormat PRINT_DATE_FORMAT = DateFormat
	.getDateInstance(DateFormat.DEFAULT, Locale.US);
	
	static {
		if (SYSTEM_ENCODING == null) {
			SYSTEM_ENCODING = "UTF-8";
		}
	}

	private StrUtils() {
	}
	/**
	 * Convert the string date to a particular format.
	 * 
	 * @param	dateString
	 * 			The String to operate on.
	 *            
	 * @param	srcFormat
	 * 			The date Format contained in the String.
	 *            
	 * @param	destFormat
	 * 			The format to be converted to.
	 *            
	 * @return 
	 * 			Formatted Date String if successful, null otherwise.
	 * 
	 */
	public static String changeDateFormat(String dateString, String srcFormat,
			String destFormat) {
		if (srcFormat == null || destFormat == null || srcFormat.isEmpty()
				|| destFormat.isEmpty()) {
			return "";
		}

		// Set the Date Format for the vaules in the column specified
		SimpleDateFormat dateFormat = new SimpleDateFormat(srcFormat);

		if (dateString == null || dateFormat == null || dateString.isEmpty()) {
			return "";
		}

		// Get date as per the format
		java.util.Date dateVal = dateFormat.parse(dateString,
				new ParsePosition(0));

		dateFormat = new SimpleDateFormat(destFormat);

		return dateFormat.format(dateVal);
	}

	/**
	 * This method converts the string timestamp to a date format by trimming of
	 * the time entry. And also returns back with requried format.
	 * 
	 * @param dateString
	 *            The String to operate on.
	 *            
	 * @param srcFormat
	 *            The date Format contained in the String.
	 *            
	 * @param destFormat
	 *            The format to be converted to.
	 *            
	 * @return 
	 * 			Formatted Date String if successful, null otherwise.
	 * 
	 */
	public static String convertTimeStampToDate(String dateString,
			String srcFormat, String destFormat) {
		if (srcFormat == null || destFormat == null || srcFormat.isEmpty()
				|| destFormat.isEmpty()) {
			return "";
		}

		if (dateString == null || dateString.isEmpty()) {
			return "";
		}
		String[] tmpDate = dateString.split(" ");
		return changeDateFormat(tmpDate[0], srcFormat, destFormat);
	}

	public static String changeDateFormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String conDate = dateFormat.format(date);

		return conDate;
	}
	
	/**
	 * replace some char in {@link String}
	 * 
	 * NOTE: old cannot be null
	 * 
	 * @param	old
	 * 			{@link String} to be replaced
	 * 
	 * @param	rchars
	 * 			replace chars
	 * 
	 * @return	String after being replaced.
	 * 
	 * */
	public static String replaceChars(String old, char[] rchars) {
		if (null == rchars)
			return old;
		
		int oldLen = old.length();
		int rLen = rchars.length;
		
		StringBuilder sb = new StringBuilder(oldLen);
		char[] oldArrays = old.toCharArray();
		boolean found = true;
		char c1;
		
		for (int i = 0; i < oldLen; i++) {
			found = false;
			c1 = oldArrays[i];
			for (int j = 0; j < rLen; j += 2) {
				if (c1 == rchars[j]) {
					if (rchars[j + 1] != 0) {
						sb.append(rchars[j + 1]);
					}
					found = true;
					continue;
				}
			}
			if (!found) {
				sb.append(c1);
			}
		}
		
		return sb.toString();
	}

	public static boolean identical(String master, String comparee) {
		String strMaster = master != null ? master.trim() : "";
		String strCompar = comparee != null ? comparee.trim() : "";
		return strMaster.length() == strCompar.length() ? strMaster
				.equalsIgnoreCase(strCompar) : false;
	}

	public static boolean identicalNumericString(String master, String comparee) {
		String strMaster = master != null ? master.trim() : "";
		String strCompar = comparee != null ? comparee.trim() : "";
		if (strMaster.lastIndexOf('.') > 0) {
			strMaster = strMaster.replaceFirst("0+$", "");
		}
		if (strCompar.lastIndexOf('.') > 0) {
			strCompar = strCompar.replaceFirst("0+$", "");
		}

		return strMaster.length() == strCompar.length() ? strMaster
				.equalsIgnoreCase(strCompar) : false;
	}

	public static char changeChar(String str) {
		char out = '\001';
		if (str != null) {
			if (str.equals("\\t"))
				out = '\t';
			else if (str.equals("\\n"))
				out = '\n';
			else if (str.equals("\\001"))
				out = '\001';
			else if (str.equals("\\009"))
				out = '\t';
			else {
				char[] ch = str.toCharArray();
				if (ch.length == 1)
					out = ch[0];
				else if (ch.length > 1) {
					if (str.indexOf("\\u") > -1 && str.length() == 6) {
						try {
							out = (char) Integer.valueOf(str.substring(2))
									.intValue();
						} catch (NumberFormatException e) {
							throw new IllegalArgumentException(e);
						}
					} else {
						throw new IllegalArgumentException(String.format(
								"Cannot convert literal %s to char type", str));
					}
				}
			}
		}
		return out;
	}

	public static boolean validateMailAddress(String mail) {
		Matcher matcher = MAIL_PATTERN.matcher(mail);
		return matcher.matches();
	}

	public static String[] filterMailAddresses(final String[] addrs) {
		List<String> validated = new ArrayList<String>();
		for (String a : addrs) {
			if (!validateMailAddress(a)) {
				continue;
			}
			validated.add(a);
		}
		String[] strs = new String[] {};
		return validated.toArray(strs);
	}

	/**
	 * rip the numeric characters from the supplied string e.g. w123ac45 -->>
	 * 12345
	 * 
	 * @param raw
	 */
	public static String ripNumber(String raw) {
		return raw.replaceAll("(?i)[a-z\\s]+", "");
	}

	public static char getCharParam(String param, char defaultvalue) {
		if (param != null)
			return changeChar(param);
		else
			return defaultvalue;
	}

	public static String replaceString(String param) {
		Matcher matcher = VARIABLE_PATTERN.matcher(param);
		String param1 = param;
		List<String> re = new ArrayList<String>();
		int i = 0;
		while (matcher.find()) {
			param1 = StringUtils.replace(param1, matcher.group(),
					System.getProperty(matcher.group(2), matcher.group()));
			if (param1.equals(param)) {
				i++;
				param1 = StringUtils.replace(param1, matcher.group(),
						"@replace" + i);
				re.add(matcher.group());
			}
			log.debug(param1);
			param = param1;
			matcher = VARIABLE_PATTERN.matcher(param1);
		}
		for (; i > 0; i--) {
			param1 = StringUtils.replace(param1, "@replace" + i, re.get(i - 1));
		}
		log.debug(param1);
		return param1;
	}

	public static String getStrParam(String param, String defaultvalue) {
		if (param != null)
			return replaceString(param);
		else
			return defaultvalue;
	}

	public static String getStrParam(String param) {
		if (param != null) {
			return replaceString(param);
		} else {
			log.error("can't find key: " + param + " in xmls!");
			throw new RerunableException("can't find key: " + param
					+ " in xmls!");
		}
	}

	public static int getIntParam(String param, int defaultvalue) {
		return getIntParam(param, defaultvalue, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}

	public static int getIntParam(String param, int defaultvalue, int min,
			int max) {
		if (param != null) {
			try {
				Integer value = Integer.valueOf(param);
				if (value < min || value > max) {
					throw new RerunableException(String.format(
							"the [%s]'value is out of range{min=%d,max=%d}",
							param, min, max));
				} else
					return value;
			} catch (NumberFormatException e) {
				log.error(String
						.format("converting [%s]'value to numeric types failed,so use the default value[%d].",
								param, defaultvalue));
				return defaultvalue;
			}
		} else
			return defaultvalue;
	}

	public static double getDoubleParam(String param, double defaultvalue,
			double min, double max) {
		if (param != null) {
			try {
				Double value = Double.valueOf(param);
				if (value < min || value > max) {
					throw new RerunableException(String.format(
							"the [%s]'value is out of range{min=%f,max=%f}",
							param, min, max));
				} else
					return value;
			} catch (NumberFormatException e) {
				log.warn(String
						.format("converting [%s]'value to numeric types failed, so use the default value[%f].",
								param, defaultvalue));
				return defaultvalue;
			}
		} else
			return defaultvalue;
	}

	public static String encodingString(String origin) {
		if (origin == null)
			return "";
		try {
			return new String(origin.getBytes(SYSTEM_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error(e.getCause());
		}
		return origin;
	}

	public static String removeSpace(final String str, final String sep) {
		assert str != null;
		assert sep != null;

		StringBuilder sb = new StringBuilder(str.length());

		String[] items = str.trim().split(sep);
		for (String item : items) {
			sb.append(item.trim()).append(sep);
		}

		return sb.substring(0, sb.lastIndexOf(sep)).toString();
	}

	public static String transformDefault(final String item) {
		if (item == null){
			return null;
		}
		if (item.equalsIgnoreCase("\\space")){
			return " ";
		}
		if (item.equalsIgnoreCase("\\blank")){
			return "";
		}
		return item;
	}
//
//	public static void main(String[] args) {
//		System.setProperty("PATH", "UNIX");
//		System.out.println(replaceString("${PATH}"));
//
//		System.out.println("abcd\001a asdfsd\001");
//		System.out.println(replaceChars("abcd\001a asdfsd\001", new char[] {'\n', ' ', '\001', 0}));
//		return;
//	}

}

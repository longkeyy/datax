/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsParse {

	private static final String CLASS_DECLARATION_PATTERN = "([^}])+\\}";

	private static final String CLASS_DECLARATION_HEADER = "(public\\s+static\\s+class\\s+)(\\w+)";

	private static final String METHOD_DECLARATION = "(/\\*(.+?)\\*/)([^;]+;)";

	private static final String COMMENT_PATTERN = "(\\w+\\s*)(:)([^\\r\\n]+)";

	private static final String MEMBER_PATTERN = "((\\w+\\s+)+)(\\s*=\\s*)(\"[^\"]+\")";

	public static List<ClassNode> parse(String source) {
		List<ClassNode> nodes = new ArrayList<ClassNode>();

		source = source.substring(source.indexOf("public abstract class ParamsKey{") + 
				"public abstract class ParamsKey{".length());
		source = source.substring(0, source.lastIndexOf('}'));

		Pattern classPattern = Pattern.compile(CLASS_DECLARATION_PATTERN);
		Matcher classMatcher = classPattern.matcher(source);

		while (classMatcher.find()) {
			ClassNode node = parseClass(classMatcher.group(0));
			nodes.add(node);
		}

		return nodes;
	}

	public static ClassNode parseClass(String classSource) {

		Pattern pattern = Pattern.compile(CLASS_DECLARATION_HEADER);
		Matcher matcher = pattern.matcher(classSource);
		if (!matcher.find()) {
			throw new IllegalArgumentException(
					"File format error: class declaration missing @" + classSource);
		}

		ClassNode classNode = ClassNode.getInstance();
		classNode.setName(matcher.group(2));

		pattern = Pattern.compile(METHOD_DECLARATION, Pattern.DOTALL);
		matcher = pattern.matcher(classSource);

		while (matcher.find()) {
			/* parse comment */
			//System.out.println("method: " + matcher.group(0));
			Pattern commentPattern = Pattern.compile(COMMENT_PATTERN);
			Matcher commentMatcher = commentPattern.matcher(matcher.group(1));
			if (!commentMatcher.find())
				throw new IllegalArgumentException(
						"File format error: class declaration without comment @"
								+ matcher.group(1));

			Map<String, String> attributes = new HashMap<String, String>();
			do {
				attributes.put(commentMatcher.group(1), commentMatcher.group(3)
						.trim());
			} while (commentMatcher.find());

			/* add key */
			Pattern memberPattern = Pattern.compile(MEMBER_PATTERN);
			Matcher memberMatcher = memberPattern.matcher(matcher.group(3));

			if (!memberMatcher.find())
				throw new IllegalArgumentException(
						"File format error: comment without member declaration @"
								+ matcher.group(3));

			classNode.addMember(classNode.createMember(memberMatcher.group(4),
					attributes));
		}

		return classNode;
	}

}

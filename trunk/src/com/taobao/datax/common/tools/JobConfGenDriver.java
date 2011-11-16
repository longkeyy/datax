/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import java.util.ArrayList;

import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class JobConfGenDriver {

	private static final String SRC_JAVA_FILE = "/home/hedgehog/workspace/dataexchange/conf/ParamsKey.java";

	public static void showCopyRight() {
		System.out.println("Taobao DataX V1.0 ");
		System.out.println("(C) Taobao Inc. All rights reserved .\n");
	}

	public static int genConf(String source) {
		showCopyRight();
		
		/* 1、parse all nodes */
		List<ClassNode> nodes = ParamsParse.parse(source);
		
		ClassNode reader;
		ClassNode writer;
		
		/* 2、interactive with User */
		Scanner scanner = new Scanner(System.in);
		
		List<ClassNode> temp = showChoices(nodes, 1);
		String readerChoice = scanner.nextLine();
		reader = temp.get(Integer.parseInt(readerChoice) - 1);
		temp = showChoices(nodes, 2);
		String writerChoice = scanner.nextLine();
		writer = temp.get(Integer.parseInt(writerChoice) - 1);
		String filename = System.getProperty("user.dir") + "/jobs/" + reader.getName() + "_to_" + writer.getName() + "_"
				+ System.currentTimeMillis() + ".xml";
		
		/* 3、generate Job xml */
		boolean b = createXMLFile(filename, reader, writer, nodes);
		if (b) {
			System.out.println("Create Job XML file successfully: "
					+ filename);
			return 0;
		}
		return -1;
	}

	private static boolean createXMLFile(String filename, ClassNode reader,
			ClassNode writer, List<ClassNode> nodes) {

		boolean result = false;
		Document document = DocumentHelper.createDocument();
		Element jobsElement = document.addElement("jobs");
		Element jobElement = jobsElement.addElement("job");
		String id = reader.getName() + "_to_" + writer.getName() + "_job";
		jobElement.addAttribute("id", id);

		/**
		 * 生成reader部分的xml文件
		 */
		Element readerElement = jobElement.addElement("reader");
		Element plugin_Element = readerElement.addElement("plugin");
		plugin_Element.setText(reader.getName());

		ClassNode readerNode = reader;
		Element tempElement = null;
		
		List<ClassMember> members = readerNode.getAllMembers();
		for (ClassMember member : members) {
			StringBuilder command = new StringBuilder("\n");

			Set<String> set = member.getAllKeys();
			String value = "";
			for (String key : set) {
				value = member.getAttr("default");
				command.append(key).append(":").append(member.getAttr(key))
						.append("\n");
			}
			readerElement.addComment(command.toString());

			String keyName = member.getName();
			keyName = keyName.substring(1, keyName.length() - 1);
			tempElement = readerElement.addElement("param");
			tempElement.addAttribute("key", keyName);

			if (value == null || "".equals(value)) {
				value = "?";
			}
			tempElement.addAttribute("value", value);
		}

		/**
		 * 生成writer部分的xml文件
		 */
		Element writerElement = jobElement.addElement("writer");
		plugin_Element = writerElement.addElement("plugin");
		plugin_Element.setText(writer.getName());

		ClassNode writerNode = writer;
		
		members = writerNode.getAllMembers();
		for (ClassMember member : members) {
			StringBuilder command = new StringBuilder("\n");
			Set<String> set = member.getAllKeys();

			String value = "";
			for (String key : set) {
				value = member.getAttr("default");
				command.append(key).append(":").append(member.getAttr(key))
						.append("\n");
			}
			writerElement.addComment(command.toString());

			String keyName = member.getName();
			keyName = keyName.substring(1, keyName.length() - 1);
			tempElement = writerElement.addElement("param");
			tempElement.addAttribute("key", keyName);

			if (value == null || "".equals(value)) {
				value = "?";
			}
			tempElement.addAttribute("value", value);
		}

		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			XMLWriter writerOfXML = new XMLWriter(new FileWriter(new File(
					filename)), format);
			writerOfXML.write(document);
			writerOfXML.close();
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private static List<ClassNode> showChoices(List<ClassNode> nodes, int type) {
		if (type == 1) {
			List<ClassNode> readerNodes = new ArrayList<ClassNode>();
			int i = 1;
			StringBuilder sb = new StringBuilder();
			for (ClassNode c : nodes) {
				String name = c.getName();
				if (name.endsWith("Reader")) {
					name = name.substring(0, name.length() - "Reader".length());
					sb.append(i).append("\t").append(name).append("\n");
					readerNodes.add(c);
					i++;
				}
			}

			System.out.println("Data source information : ");
			System.out.println(sb.toString());
			System.out.print("Please choose data source : ");
			return readerNodes;
		} else {
			List<ClassNode> writerNodes = new ArrayList<ClassNode>();
			int i = 1;
			StringBuilder sb = new StringBuilder();
			for (ClassNode c : nodes) {
				String name = c.getName();
				if (name.endsWith("Writer")) {
					name = name.substring(0, name.length() - "Writer".length());
					sb.append(i).append("\t").append(name).append("\n");
					writerNodes.add(c);
					i++;
				}
			}

			System.out.println("\nData destination information : ");
			System.out.println(sb.toString());
			System.out.print("Please choose data destination : ");
			return writerNodes;
		}
	}
	
//	public static void main(String[] args) {
//		try {
//			String content = FileUtils.readFileToString(
//					new File(SRC_JAVA_FILE), "UTF-8");
//			
//			genConf(content);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}

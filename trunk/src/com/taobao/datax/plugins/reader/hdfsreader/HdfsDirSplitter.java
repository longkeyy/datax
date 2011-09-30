/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.reader.hdfsreader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.ParamsKey;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.common.util.DfsUtils;
import com.taobao.datax.common.util.SplitUtils;


public class HdfsDirSplitter extends Splitter {
	private Path p = null;

	private FileSystem fs = null;

	@Override
	public int init() {
		String dir = param.getValue(ParamsKey.HdfsReader.dir);
		if (dir.endsWith("*")) {
			dir = dir.substring(0, dir.lastIndexOf("*"));
		}

		String ugi = param.getValue(ParamsKey.HdfsReader.ugi, null);
		if (dir == null) {
			throw new RerunableException("Can't find the param ["
					+ ParamsKey.HdfsReader.dir + "] in hdfs-spliter-param.");
		}
		p = new Path(dir);
		if (p == null) {
			throw new RerunableException("Can't create file system.");
		}
		
		String configure = param.getValue(ParamsKey.HdfsReader.hadoop_conf, "");
		try {
			fs = DfsUtils.createFileSystem(new URI(dir),
					DfsUtils.getConf(dir, ugi, configure));
			if (!fs.exists(p)) {
				IOUtils.closeStream(fs);
				throw new RerunableException("the path[" + dir
						+ "] does not exist.");
			}
		} catch (IOException e) {
			throw new RerunableException("Can't create the HDFS file system:"
					+ e);
		} catch (URISyntaxException e) {
			throw new RerunableException("Can't create the HDFS file system:"
					+ e);
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split() {

		List<PluginParam> v = new ArrayList<PluginParam>();
		try {
			FileStatus[] status = fs.listStatus(p);
			for (FileStatus state : status) {
				if (!state.isDir()) {
					String file = state.getPath().toString();
					PluginParam oParams = SplitUtils.copyParam(param);
					oParams.putValue(ParamsKey.HdfsReader.dir, file);
					v.add(oParams);
				}
			}
		} catch (IOException e) {
			throw new RerunableException(
					"some errors have happened in fetching the file-status:"
							+ e.getCause());
		} finally {
			IOUtils.closeStream(fs);
		}
		return v;
	}

}

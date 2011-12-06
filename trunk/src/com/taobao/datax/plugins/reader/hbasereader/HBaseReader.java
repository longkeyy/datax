package com.taobao.datax.plugins.reader.hbasereader;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.Line;
import com.taobao.datax.common.plugin.LineSender;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Reader;

public class HBaseReader extends Reader {
	private Logger logger = Logger.getLogger(this.getClass());

	private String tableName = null;

	private String columns = null;

	private String hbaseConf = null;

	private String rowkeyRange = null;

	private HBaseProxy proxy = null;

	@Override
	public List<PluginParam> split(PluginParam param) {
		HBaseReaderSplitter splitter = new HBaseReaderSplitter();
		splitter.setParam(param);
		splitter.init();
		return splitter.split();
	}

	@Override
	public int init() {
		// TODO Auto-generated method stub
		this.tableName = this.param.getValue(ParamKey.htable);
		this.columns = this.param.getValue(ParamKey.columns_key);
		this.hbaseConf = this.param.getValue(ParamKey.hbase_conf);
		this.rowkeyRange = this.param.getValue(
				ParamKey.rowkey_range, "");

		try {
			proxy = HBaseProxy.newProxy(hbaseConf, tableName);
		} catch (IOException e) {
			try {
				if (null != proxy) {
					proxy.close();
				}
			} catch (IOException e1) {
			}
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		}

		if (StringUtils.isBlank(rowkeyRange)) {
			proxy.setStartEndRange(null, null);
		} else {
			rowkeyRange = " " + rowkeyRange + " ";
			String[] pair = rowkeyRange.split(",");
			if (null == pair || 0 == pair.length) {
				proxy.setStartEndRange(null, null);
			} else {
				proxy.setStartEndRange(
						StringUtils.isBlank(pair[0].trim()) ? null : pair[0].trim().getBytes(),
						StringUtils.isBlank(pair[1].trim()) ? null : pair[1].trim().getBytes());
			}
		}
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connect() {
		this.logger.info("HBaseReader start to connect to HBase .");
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startRead(LineSender sender) {
		try {
			proxy.prepare(columns.split(","));
			Line line = sender.createLine();
			while (proxy.fetchLine(line)) {
				sender.sendToWriter(line);
				line = sender.createLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RerunableException(e.getCause());
		} finally {
			try {
				proxy.close();
			} catch (IOException e) {
			}
		}

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int finish() {
		return PluginStatus.SUCCESS.value();
	}

}

package com.taobao.datax.plugins.reader.hbasereader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.apache.log4j.Logger;

import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.Splitter;
import com.taobao.datax.common.util.SplitUtils;

public class HBaseReaderSplitter extends Splitter {

	private Logger logger = Logger.getLogger(this.getClass());

	private String tableName = null;

	private String columns = null;

	private String hbaseConf = null;

	private String rowkeyRange = null;

	private HBaseProxy proxy = null;

	@Override
	public int init() {
		// TODO Auto-generated method stub
		this.tableName = this.param.getValue(ParamKey.htable);
		this.columns = this.param.getValue(ParamKey.columns_key);
		this.hbaseConf = this.param.getValue(ParamKey.hbase_conf);
		this.rowkeyRange = this.param.getValue(ParamKey.rowkey_range, "");

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split() {
		// TODO Auto-generated method stub
		List<PluginParam> ret = new ArrayList<PluginParam>();

		try {
			this.proxy = HBaseProxy.newProxy(hbaseConf, tableName);

			Pair<byte[][], byte[][]> ranges = proxy.getStartEndKeys();

			if (null == ranges) {
				ret = super.split(param);
			} else {
				String totalStartKey = null;
				String totalEndKey = null;

				if (!StringUtils.isBlank(rowkeyRange)) {
					if (!rowkeyRange.contains(",")) {
						throw new IllegalArgumentException(
								"rowkeyrange must be like 'startkey,endkey'");
					}

					rowkeyRange = " " + rowkeyRange + " ";
					String[] tmps = rowkeyRange.split(",");
					if (!StringUtils.isBlank(tmps[0])) {
						totalStartKey = tmps[0].trim();
					}

					if (!StringUtils.isBlank(tmps[1])) {
						totalEndKey = tmps[1].trim();
					}
				}

				if (null != totalEndKey
						&& null != totalStartKey
						&& Bytes.compareTo(totalStartKey.getBytes(),
								totalEndKey.getBytes()) > 0) {
					throw new IllegalArgumentException(String.format(
							"startkey %s cannot be larger than endkey %s .",
							totalStartKey, totalEndKey));
				}

				logger.info(String.format(
						"HBaseReader split job into %d sub-jobs .",
						ranges.getFirst().length));

				for (int i = 0; i < ranges.getFirst().length; i++) {
					PluginParam p = SplitUtils.copyParam(param);

					String thisStartKey = null;
					if (null == totalStartKey
							|| Bytes.compareTo(totalStartKey.getBytes(),
									ranges.getFirst()[i]) < 0) {
						thisStartKey = Bytes.toString(ranges.getFirst()[i]);
					} else {
						thisStartKey = totalStartKey;
					}

					String thisEndKey = null;
					if (null == totalEndKey
							|| Bytes.compareTo(totalEndKey.getBytes(),
									ranges.getSecond()[i]) > 0) {
						thisEndKey = Bytes.toString(ranges.getSecond()[i]);
					} else {
						thisEndKey = totalEndKey;
					}

					p.putValue(ParamKey.rowkey_range, thisStartKey + ","
							+ thisEndKey);
					ret.add(p);
				}
			}
		} catch (IOException e) {
			ret = super.split(param);
		} finally {
			try {
				if (null != proxy) {
					proxy.close();
				}
			} catch (IOException e) {
				/* swallow exception */
				e.printStackTrace();
			}
		}

		return ret;
	}
}

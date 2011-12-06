/*
 * PanguDumper.java 2011/04/22
 * shenggong.wang@aliyun-inc.com
 * Last modified: Oct 10,2011
 */
package com.taobao.datax.plugins.writer.hdfswriter;

import org.apache.log4j.Logger;



import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import org.apache.hadoop.conf.Configuration;

import com.taobao.datax.common.plugin.PluginParam;
import com.taobao.datax.common.exception.RerunableException;
import com.taobao.datax.common.plugin.PluginStatus;
import com.taobao.datax.common.plugin.LineReceiver;
import com.taobao.datax.plugins.common.DfsUtils;

/**
 * Dumper for Apsara Pangu
 * 
 * @author shenggong.wang@aliyun-inc.com
 * @version 1.0.0
 */
public class PanguWrapper extends HdfsWriter {

	// Logger
    private static final Logger log = Logger.getLogger(PanguWrapper.class);
    
    // Capability for DataX
    private static final String CAPABILITY_FILE = "/apsara/security/CapabilityForDataX.txt";

    private boolean panguInit = false;

    private boolean prepare = true;
    private boolean init = true;
    private boolean connect = true;
    private boolean dump = true;
    private boolean commit = true;
   
    private void error(String msg, Throwable t) {
        if (log.isDebugEnabled())
            log.debug(msg, t);
        else
            log.error(msg);
    }

    private void panguInit() {
        if (panguInit) return;

        File capabilityFile = new File(CAPABILITY_FILE);
        if (capabilityFile.exists() && capabilityFile.canRead()) {
            BufferedReader capaReader = null;
            try {
                capaReader = new BufferedReader(new FileReader(capabilityFile));
                StringBuffer capaStr = new StringBuffer();
                String line = null;
                while ((line = capaReader.readLine()) != null) {
                    capaStr.append(line).append('\n');
                }

                String hadoop_conf = param.getValue(ParamKey.hadoop_conf, "");
                String dir = param.getValue(ParamKey.dir);
                String ugi = param.getValue(ParamKey.ugi, null);

                Configuration conf = DfsUtils.getConf(dir, ugi, hadoop_conf);
                conf.set("apsara.security.capability", capaStr.toString().trim());

            } catch (Exception e) {
                log.warn("Read capability error: " + e.getMessage());
            } finally {
                if (capaReader != null) {
                    try { capaReader.close(); } catch (Exception e) { log.warn(e.getMessage()); }
                }
            }
        }

        panguInit = true;
    }

	@Override
	public int init() {
	    log.debug("PanguWrapper.init()");
        try {
            // pangu init
            panguInit();

            super.init();
        } catch (Exception e) {
            init = false;
            error("PanguWrapper.init() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }
	
    @Override
    public int connect() {
        log.debug("PanguWrapper.connect()");
        try {
            if (init)
                super.connect();
            else
                connect = false;
        } catch (Exception e) {
            connect = false;
            error("PanguWrapper.connect() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int startWrite(LineReceiver receiver) {
        log.debug("PanguWrapper.startWrite()");
        try {
            if (connect)
                super.startWrite(receiver);
            else
                dump = false;
        } catch (Exception e) {
            dump = false;
            error("PanguWrapper.startWrite() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int commit() {
        log.debug("PanguWrapper.commit()");
        try {
            if (dump)
                super.commit();
            else
                commit = false;
        } catch (Exception e) {
            commit = false;
            error("PanguWrapper.commit() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

	@Override
	public int finish() {
        log.debug("PanguWrapper.finish()");
        try {
            if (commit)
                super.finish();
        } catch (Exception e) {
            error("PanguWrapper.finish() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

    @Override
    public int prepare(PluginParam param) {
        log.debug("PanguWrapper.prepare()");
        try {
            // pangu init
            panguInit();

            super.prepare(param);
            log.debug("PanguWrapper.prepare() done.");
        } catch (Exception e) {
            prepare = false;
            error("PanguWrapper.prepare() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

    @Override
    public int post(PluginParam param) throws RerunableException {
        log.debug("PanguDumper.post()");
        try {
            if (dump)
                super.post(param);
        } catch (Exception e) {
            error("PanguDumper.post() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }

    @Override
    public List<PluginParam> split(PluginParam param) {
        log.debug("PanguWrapper.split()");
        List<PluginParam> ret = null;
        
        try {
            if (init)
                ret = super.split(param);
        } catch (Exception e) {
            error("PanguWrapper.split() ERR: " + e.getMessage(), e);
        }

        return ret;
    }

    @Override
    public int cleanup() {
        log.debug("PanguWrapper.cleanup()");
        try {
            if (init)
                super.cleanup();
        } catch (Exception e) {
            error("PanguWrapper.cleanup() ERR: " + e.getMessage(), e);
        }

        return PluginStatus.SUCCESS.value();
    }
    
}


/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.plugins.writer.oraclewriter;

import com.taobao.datax.engine.constants.Constants;

public class OracleWriterJni{
	
	final static OracleWriterJni instance = new OracleWriterJni();
	
	public static OracleWriterJni getInstance() {
		return instance;
	}

	private OracleWriterJni(){
		try{			
			System.load(Constants.DATAX_LOCATION+"/plugins/liboraclewriter.so");
    	} catch (UnsatisfiedLinkError e){
    		e.printStackTrace();
    		System.out.println("UnsatisfiedLinkError");
    		System.exit(-1);
    	} catch (NullPointerException e){
    		e.printStackTrace();
    		System.out.println("UnsatisfiedLinkError");
    		System.exit(-1);
    	}
	}
	
	public native long oracle_dumper_init(
			String logon, String table, String sep, String pre,
            String post, String dtfmt, String encoding,
            String colorder, String limit, long parallel, long skipindex);
	public native int oracle_dumper_connect(long p);
    public native int oracle_dumper_predump(long p, long flag);
    public native int oracle_dumper_dump(long p, String line);
    public native int oracle_dumper_commit(long p);
    public native int oracle_dumper_finish(long p, long flag);
        
/*    public int init(String logon, String table, String sep, String pre,
            String post, String dtfmt, String encoding,
            String colorder, String limit){
    	p = oracle_dumper_init(logon, table, sep, pre, post, dtfmt, encoding, colorder, limit);
    	return 0;
    }
    public  int connect(){
    	return oracle_dumper_connect(this.p);
    }
    public  int predump(){
    	return oracle_dumper_predump(this.p);
    }
    public  int dump(String line){
    	return oracle_dumper_dump(this.p, line);
    }
    public  int commit(){
    	return oracle_dumper_commit(this.p);
    }
    public  int finish(){
    	return oracle_dumper_finish(this.p);
    }*/
    
}

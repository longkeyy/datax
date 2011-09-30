/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.plugin;

/**
 *  Constant params definition for {@link PluginParam}.
 *  
 * */
public abstract class ParamsKey{
	public static class MysqlReader {
		 /*
	       * @name: ip
	       * @description: Mysql database's ip address
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String ip = "ip";
		/*
	       * @name: port
	       * @description: Mysql database's port
	       * @range:
	       * @mandatory: true
	       * @default:3306
	       */
		public final static String port = "port";
		/*
	       * @name: dbname
	       * @description: Mysql database's name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String dbname = "dbname";
		/*
	       * @name: username
	       * @description: Mysql database's login name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String username = "username";
		/*
	       * @name: password
	       * @description: Mysql database's login password
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String password = "password";
		/*
	       * @name: tables
	       * @description: tables to export data, format can support simple regex, table[0-63]
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public final static String tables = "tables";
		/*
	       * @name: where
	       * @description: where clause, like 'modified_time > sysdate'
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public final static String where = "where";
		/*
	       * @name: sql
	       * @description: self-defined sql statement
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public final static String sql = "sql";
		/*
	       * @name: columns
	       * @description: columns to be selected, default is *
	       * @range: 
	       * @mandatory: false
	       * @default: *
	       */
		public final static String columns = "columns";
		/*
	       * @name: encoding
	       * @description: mysql database's encode
	       * @range: UTF-8|GBK|GB2312
	       * @mandatory: false
	       * @default: UTF-8
	       */
		public final static String encoding = "encoding";
		
        /*
	       * @name: mysql.params
	       * @description: mysql driver params, starts with no &, e.g. loginTimeOut=3000&yearIsDateType=false
	       * @range: 
	       * @mandatory: false
	       * @default:
	       */
		public final static String mysqlParams = "mysql.params";
		
		 /*
	       * @name: concurrency
	       * @description: concurrency of the job
	       * @range: 1-10
	       * @mandatory: false
	       * @default: 1
	       */
		public final static String concurrency = "concurrency";
	}
	public static class SqlServerReader{
		 /*
	       * @name: ip
	       * @description: Sqlserver database's ip address
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String ip = "ip";
		/*
	       * @name: port
	       * @description: Sqlserver database's port
	       * @range:
	       * @mandatory: false
	       * @default:
	       */
		public final static String port = "port";
		/*
	       * @name: dbname
	       * @description: Sqlserver database's name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String dbName = "dbname";
		/*
	       * @name: username
	       * @description: Sqlserver database's login name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String username = "username";
		/*
	       * @name: password
	       * @description: Sqlserver database's login password
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String password = "password";
		/*
	       * @name: tables
	       * @description: tables to export data, format can support simple regex, table[0-63]
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public final static String tables = "tables";
		/*
	       * @name: where
	       * @description: where clause, like 'modified_time > sysdate'
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public final static String where = "where";
		/*
	       * @name: columns
	       * @description: columns to be selected, default is *
	       * @range: 
	       * @mandatory: true
	       * @default: *
	       */
		public final static String columns = "columns";
		
		/*
	       * @name: sql
	       * @description: self-defined sql statement
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public final static String sql = "sql";
		
		/*
	       * @name: ENCODING
	       * @description: Sqlserver database's encode
	       * @range: UTF-8|GBK|GB2312
	       * @mandatory: false
	       * @default: UTF-8
	       */
		public final static String encoding = "encoding";
	    /*
	       * @name: sqlserver.params
	       * @description: Sqlserver driver params
	       * @range: 
	       * @mandatory: false
	       * @default:
	       */
		public final static String sqlServerParams = "sqlserver.params";
		
		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	public static class OracleReader{
		/*
	       * @name: dbname
	       * @description: Oracle database name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String dbname = "dbname";
		/*
	       * @name: username
	       * @description:  Oracle database login username
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String username = "username";
		/*
	       * @name: password
	       * @description: Oracle database login password
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String password = "password";
		/*
	       * @name: schema
	       * @description: Oracle database schema
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String schema = "schema";
		 /*
	       * @name: ip
	       * @description: Oracle database ip address
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String ip = "ip";
		/*
	       * @name: port
	       * @description: Oracle database port
	       * @range:
	       * @mandatory: true
	       * @default: 1521
	       */
		public final static String port = "port";
		/*
	       * @name: tables
	       * @description: tables to be exported
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public final static String tables = "tables";
		
		/*
	       * @name: columns
	       * @description: columns to be selected
	       * @range: 
	       * @mandatory: false
	       * @default: *
	       */
		public final static String columns = "columns";
		
		/*
	       * @name: where
	       * @description: where clause, like 'gmtdate > trunc(sysdate)'
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public final static String where = "where";		
		/*
	       * @name: sql
	       * @description: self-defined sql statement
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public final static String sql = "sql";
		/*
	       * @name: encoding
	       * @description: oracle database encode
	       * @range: UTF-8|GBK|GB2312
	       * @mandatory: false
	       * @default: UTF-8
	       */
		public final static String encoding = "encoding";
		/*
	       * @name: split_mod
	       * @description: how to split job
	       * @range: 0-no split, 1-rowid split, others ntile split 
	       * @mandatory: false
	       * @default: 1
	       */
		public final static String splitMod = "split_mod";
		/*
	       * @name: tnsfile
	       * @description: tns file path
	       * @range: 
	       * @mandatory: true
	       * @default: /home/oracle/product/10g/db/network/admin/tnsnames.ora
	       */
		public final static String tnsFile = "tnsfile";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	
	public static class HdfsReader{
		/*
		 * @name: ugi
		 * @description: HDFS login account, e.g. 'username, groupname(groupname...),#password
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String ugi = "hadoop.job.ugi";
		
		/*
		 * @name: hadoop_conf
		 * @description: hadoop-site.xml path
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String hadoop_conf = "hadoop_conf";
		
		/*
		 * @name: dir
		 * @description: hdfs path, format like: hdfs://ip:port/path, or file:///home/taobao/
		 * @range:
		 * @mandatory: true 
		 * @default:
		 */
		public final static String dir = "dir";
		/*
		 * @name: fieldSplit
		 * @description: how to sperate a line
		 * @range:
		 * @mandatory: false 
		 * @default:\t
		 */
		public final static String fieldSplit = "field_split";
		/*
		 * @name: encoding 
		 * @description: hdfs encode
		 * @range:UTF-8|GBK|GB2312
		 * @mandatory: false 
		 * @default:UTF-8
		 */
		public final static String encoding = "encoding";
		/*
		 * @name: bufferSize
		 * @description: how large the buffer
		 * @range: [1024-4194304]
		 * @mandatory: false 
		 * @default: 4096
		 */
		public final static String bufferSize = "buffer_size";
	
		/*
	       * @name: nullString
	       * @description: replace the nullstring to null
	       * @range: 
	       * @mandatory: false
	       * @default: \N
	       */
		public final static String nullString = "nullstring";
		/*
		 * @name: ignoreKey
		 * @description: ingore key
		 * @range: true|false
		 * @mandatory: false 
		 * @default: true
		 */		
		public final static String ignoreKey = "ignore_key";
		/*
		 * @name: colFilter
		 * @description: how to filter column
		 * @range: 
		 * @mandatory: false 
		 * @default: 
		 */		
		public final static String colFilter = "col_filter";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	
	public static class HttpReader {
	
		/* 
		 * @name: URLDelimiter
		 * @description: how to split url
		 * @range:
		 * @mandatory: false
		 * @default: ;
		 */
		public final static String URLDelimiter = "urldelimiter";
		
		/*
		 * @name: fieldSplit
		 * @description: separator to split urls
		 * @range:
		 * @mandatory: false
		 * @default:\t
		 */
		public final static String fieldSplit = "field_split";
	
		/*
		 * @name: encoding
		 * @description: encode 
		 * @range: UTF-8|GBK|GB2312
		 * @mandatory: false
		 * @default: UTF-8
		 */
		public final static String encoding = "encoding";
	
		/*
		 * @name: nullString
		 * @description: replace this nullString to null
		 * @range:
		 * @mandatory: false
		 * @default: \N
		 */
		public final static String nullString = "null_string";
		
		
		/*
		 * @name: httpURLs
		 * @description: url to fetch data
		 * @range:legal http url
		 * @mandatory: true
		 * @default:
		 */
		public final static String httpURLs = "httpurls";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	public static class StreamReader {
		
		/*
		 * @name: fieldSplit
		 * @description: seperator to seperate field
		 * @range:
		 * @mandatory: false 
		 * @default:\t
		 */
		public final static String fieldSplit = "field_split";
		
		/*
		 * @name: encoding
		 * @description: environment encode 
		 * @range: UTF-8|GBK|GB2312
		 * @mandatory: false
		 * @default: UTF-8
		 */
		public final static String encoding = "encoding";
		
		/*
	       * @name: nullString
	       * @description: replace nullString to null
	       * @range: 
	       * @mandatory: false
	       * @default: \N
	       */
		public final static String nullString = "null_string";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	public static class MysqlWriter {
		 /*
	       * @name: ip
	       * @description: Mysql database ip address
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String ip = "ip";
		/*
	       * @name: port
	       * @description: Mysql database port
	       * @range:
	       * @mandatory: true
	       * @default:3306
	       */
		public final static String port = "port";
		/*
	       * @name: dbname
	       * @description: Mysql database name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String dbname = "dbname";
		/*
	       * @name: username
	       * @description: Mysql database login username
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String username = "username";
		/*
	       * @name: password
	       * @description: Mysql database login password
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String password = "password";
		/*
	       * @name: table
	       * @description: table to be dumped data into
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public final static String table = "table";
		/*
	       * @name: colorder
	       * @description: order of columns
	       * @range: 
	       * @mandatory: false
	       * @default:
	       */
		public final static String colorder = "colorder";
		/*
	       * @name: encoding
	       * @description: 
	       * @range: UTF-8|GBK|GB2312
	       * @mandatory: false
	       * @default: UTF-8
	       */
		public final static String encoding = "encoding";
		/*
		 * @name: pre
		 * @description: execute sql before dumping data
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String pre = "pre";
		/*
		 * @name: post
		 * @description: execute sql after dumping data
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String post = "post";
	
		/*
		 * @name: limit
		 * @description: error limit
		 * @range: [0-65535]
		 * @mandatory: false
		 * @default: 0
		 */
		public final static String limit = "limit";
		/*
		 * @name: set
		 * @description:
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String set = "set";
		/*
		 * @name: replace
		 * @description:
		 * @range: [true/false]
		 * @mandatory: false
		 * @default:false
		 */
		public final static String replace = "replace";
	      /*
	       * @name:mysql.params
	       * @description:mysql driver params
	       * @range:params1|params2|...
	       * @mandatory: false
	       * @default:
	       */
		public final static String mysqlParams = "mysql.params";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	
	}
	public static class OracleWriter{
		/*
		 * @name: dbname
		 * @description: Oracle database dbname
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String dbname = "dbname";
		
		/*
	       * @name: schema
	       * @description: Oracle database schema
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public final static String schema = "schema";
		
		/*
		 * @name: table
		 * @description: table to be dumped data into
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String table = "table";
	    /*
		 * @name: username
		 * @description: oracle database login username
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String username = "username";
		
		/*
		 * @name: password
		 * @description: oracle database login password
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String password = "password";
	
		/*
		 * @name: dtfmt
		 * @description: oracle time format
		 * @range: 
		 * @mandatory: true
		 * @default:yyyyMMddhhmmss
		 */
		public final static String dtfmt = "dtfmt";
	
		/*
		 * @name: pre
		 * @description: execute pre sql before writing data .
		 * @range:
		 * @mandatory: true
		 * @default:
		 */
		public final static String pre = "pre";
		/*
		 * @name: post
		 * @description: execute post sql after writing data .
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String post = "post";
		/*
		 * @name: encoding
		 * @description: oracle encode
		 * @range: UTF-8|GBK|GB2312
		 * @mandatory: false
		 * @default: UTF-8
		 */
		public final static String encoding = "encoding";
		/*
		 * @name: colorder
		 * @description: order of columns
		 * @range: col1,col2...
		 * @mandatory: false
		 * @default:
		 */
		public final static String colorder = "colorder";
		
		/*
		 * @name: limit
		 * @description: limit amount of errors
		 * @range:
		 * @mandatory: false
		 * @default: 0
		 */
		public final static String limit = "limit";
		
		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
	public static class HdfsWriter{
		/*
		 * @name: ugi
		 * @description: HDFS login account, e.g. username,groupname(groupname...),#password
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String ugi = "hadoop.job.ugi";
		
		/*
		 * @name: hadoop_conf
		 * @description: hadoop-site.xml path
		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String hadoop_conf = "hadoop_conf";
		
		/*
		 * @name: dir
		 * @description: hdfs dir，hdfs://ip:port/path, or file:///home/taobao
		 * @range:
		 * @mandatory: true 
		 * @default:
		 */
		public final static String dir = "dir";
		/*
		 * @name: prefixname
		 * @description: hdfs filename
		 * @range:
		 * @mandatory: false 
		 * @default: part
		 */
		public final static String prefixname = "prefix_filename";
	
		/*
		 * @name: fieldSplit
		 * @description: how to seperate fields
		 * @range:\t,\001,",'
		 * @mandatory: false 
		 * @default:\t
		 */
		public final static String fieldSplit = "field_split";
		/*
		 * @name: lineSplit
		 * @description: how to seperate lines
		 * @range:\n
		 * @mandatory: false 
		 * @default:\n
		 */
		public final static String lineSplit = "line_split";
		/*
		 * @name: encoding 
		 * @description: encode
		 * @range: UTF-8|GBK|GB2312
		 * @mandatory: false
		 * @default: UTF-8
		 */
		public final static String encoding = "encoding";
		
		/*
		 * @name: nullChar
		 * @description: how to replace null in hdfs
		 * @range: 
		 * @mandatory: false
		 * @default:
		 */
		public final static String nullChar = "nullchar";
		
		/*
		 * @name: codecClass
		 * @description: compress codecs
		 * @range:org. apache.hadoop.io.compress.BZip2Codec|org.apache.hadoop.io.compress.DefaultCodec|org.apache.hadoop.io.compress.GzipCodec
		 * @mandatory: false 
		 * @default: org.apache.hadoop.io.compress.DefaultCodec
		 */
		public final static String codecClass = "codec_class";
		/*
		 * @name: compressionType
		 * @description: how to compress file
		 * @range: BLOCK|NONE|RECORD   
		 * @mandatory: false
		 * @default: NONE
		 */
		public final static String compressionType = "compression_type";
		/*
		 * @name: keyFieldIndex
		 * @description: 
		 * @range: [-1-255]
		 * @mandatory: false
		 * @default: -1
		 */		
		public final static String keyFieldIndex = "key_field_index";
		/*
		 * @name: bufferSize
		 * @description: how much the buffer size is
		 * @range: [1024-4194304]
		 * @mandatory: false 
		 * @default: 4096
		 */
		public final static String bufferSize = "buffer_size";
		/*
		 * @name: fileType
		 * @description: Filetype TXT->TextFile,SEQ->SequenceFile,TXT_COMP->Compressed TextFile,SEQ_COMP->Compressed SequenceFile
		 * @range: TXT|SEQ|TXT_COMP|SEQ_COMP
		 * @mandatory: true 
		 * @default: TXT
		 */
		public final static String fileType = "file_type";
		/*
		 * @name: keyClass
		 * @description:SequenceFile key class
		 * @range:org.apache.hadoop.io.Text|org.apache.hadoop.io.LongWritable|org.apache.hadoop.io.IntWritable|org.apache.hadoop.io.DoubleWritable|org.apache.hadoop.io.BooleanWritable|org.apache.hadoop.io.ByteWritable|org.apache.hadoop.io.VIntWritable|org.apache.hadoop.io.VLongWritable|org.apache.hadoop.io.NullWritable
		 * @mandatory: false 
		 * @default:org.apache.hadoop.io.Text
		 */
		public final static String keyClass = "key_class";
		/*
		 * @name: valueClass
		 * @description:SequenceFile value class
		 * @range:org.apache.hadoop.io.Text|org.apache.hadoop.io.LongWritable|org.apache.hadoop.io.IntWritable|org.apache.hadoop.io.DoubleWritable|org.apache.hadoop.io.BooleanWritable|org.apache.hadoop.io.ByteWritable|org.apache.hadoop.io.VIntWritable|org.apache.hadoop.io.VLongWritable|org.apache.hadoop.io.NullWritable
		 * @mandatory: false
		 * @default:org.apache.hadoop.io.Text
		 */
		public final static String valueClass = "value_class";
		/* 
		 * @name: delMode
		 * @description: do clean data before loading  0: overwrite file with the same filename  1: report error when exists same filename  2: delete single file  3: delete all files with the same prefix name 	4: delete all files in the directory     
		 * @range:[0-4]   
		 * @mandatory: false
		 * @default:3
		 */
		public final static String delMode = "del_mode";
		/* 
		 * @name: hiveTableSwitch
		 * @description:
  		 * @range:
		 * @mandatory: false
		 * @default: false
		 */
		public final static String hiveTableswitch = "HIVETABLE_SWITCH";
		/* 
		 * @name: tableName
		 * @description:
  		 * @range:
		 * @mandatory: false
		 * @default: 
		 */
		public final static String tableName = "TABLE_NAME";
		/* 
		 * @name: partitionNames
		 * @description: 
  		 * @range:
		 * @mandatory: false
		 * @default: 
		 */
		public final static String partitionNames = "PARTITION_NAMES";	
		/* 
		 * @name: partitionValues
		 * @description: 
  		 * @range:
		 * @mandatory: false
		 * @default: 
		 */
		public final static String partitionValues = "PARTITION_VALUES";
		/* 
		 * @name: hiveServer
		 * @description:
  		 * @range:
		 * @mandatory: false
		 * @default:
		 */
		public final static String hiveServer = "HIVESERVER_IP";	
		/* 
		 * @name: hiveServerPort
		 * @description: 
  		 * @range:
		 * @mandatory: false
		 * @default: 10000
		 */
		public final static String hiveServerPort = "HIVESERVER_PORT";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1-100
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
		
	}
	
	public static class StreamWriter {
		
		/*
		 * @name: fieldSplit
		 * @description: seperator to seperate field
		 * @range:
		 * @mandatory: false 
		 * @default:\t
		 */
		public final static String fieldSplit = "field_split";
		
		/*
		 * @name: encoding
		 * @description: stream encode
		 * @range: UTF-8|GBK|GB2312
		 * @mandatory: false
		 * @default: UTF-8
		 */
		public final static String encoding = "encoding";
		/*
		 * @name: prefix 
		 * @description:  print result with prefix
		 * @range: 
		 * @mandatory: false
		 * @default:
		 */
		public final static String prefix = "prefix";
		
		/*
		 * @name: print
		 * @description: print the result
		 * @range: 
		 * @mandatory: false
		 * @default: true
		 */
		public final static String print = "print";
		
		/*
		 * @name: nullchar
		 * @description:  replace null with the nullchar
		 * @range: 
		 * @mandatory: false
		 * @default: 
		 */
		public final static String nullChar = "nullchar";

		 /*
	       * @name:concurrency
	       * @description:concurrency of the job
	       * @range:1
	       * @mandatory: false
	       * @default:1
	       */
		public final static String concurrency = "concurrency";
	}
}

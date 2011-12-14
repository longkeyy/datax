package com.taobao.datax.plugins.writer.panguwriter;

public final class ParamKey {

	/*
	 * @name: dir
	 * @description: 
	 * @range:
	 * @mandatory: true
	 * @default:
	 */
	public final static String dir = "dir";
	
	/*
	 * @name: gongcao
	 * @description: 
	 * @range: true|false
	 * @mandatory: false
	 * @default:false
	 */
	public final static String gongcao = "apsara.use.gongcao";
	
	/*
	 * @name: apsaraUsername
	 * @description: 
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public final static String apsaraUsername = "apsara.user.name";
	
	/*
	 * @name: apsaraPassword
	 * @description: 
	 * @range: 
	 * @mandatory: false
	 * @default:
	 */
	public final static String apsaraPassword = "apsara.user.password";
	
	/*
	 * @name: apsaraPanguCapability
	 * @description: 
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public final static String apsaraCapability = "apsara.capabilityfile";
	
	/*
	 * @name: prefixname
	 * @description: 
	 * @range:
	 * @mandatory: false
	 * @default: part
	 */
	public final static String prefixname = "prefix_filename";
	/*
	 * @name: postfixname
	 * @description: 
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public final static String postfixname = "postfix_filename";
	/*
	 * @name: splitnum
	 * @description: 
	 * @range: [1-10000]
	 * @mandatory: false
	 * @default: 1
	 */
	public final static String splitnum = "split_num";
	/*
	 * @name: fieldSplit
	 * @description: 
	 * @range:\t,\001,",'
	 * @mandatory: false
	 * @default:\t
	 */
	public final static String fieldSplit = "field_split";
	
	/*
	 * @name: encoding
	 * @description: 
	 * @range: UTF-8|GBK|GB2312
	 * @mandatory: false
	 * @default: UTF-8
	 */
	public final static String encoding = "encoding";

	/*
	 * @name: bufferSize
	 * @description: 
	 * @range: [1024-4194304]
	 * @mandatory: false
	 * @default: 4096
	 */
	public final static String bufferSize = "buffer_size";

	 /*
       * @name:concurrency
       * @description:concurrency of the job
       * @range:1-100
       * @mandatory: false
       * @default:1
       */
	public final static String concurrency = "concurrency";
}


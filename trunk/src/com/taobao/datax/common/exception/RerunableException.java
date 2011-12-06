/**
 * (C) 2010-2011 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 
 * version 2 as published by the Free Software Foundation. 
 * 
 */


package com.taobao.datax.common.exception;

/**
 * A kind of exception DataX throws in condition of bad network, unexpected connect failure, etc.
 * The exception usually indicates it may be recovered soon, so DataX will try to rerun the job if occurs this exception.
 * 
 * @see UnRerunableException
 * 
 * */
public class RerunableException extends RuntimeException {
	
	private static final long serialVersionUID = -6896389644432598060L;
	
	private String msg;

	/**
     * Constructor.
     * 
     * @param message
     * 			exception message.
     * 
     */
    public RerunableException(final String message) {
        super(message);
    }

    /**
     * Constructor : warp a exception to {@link RerunableException}
     * 
     * @param exception
     * 			wrapped exception.
     */
    public RerunableException(final Exception exception) {
    	super();
    	if (null != exception) {
    		msg = exception.getMessage();
    	}
	} 
    
    /**
     * A default constructor.
     * 
     */
    public RerunableException() {
		super();
	}

    /**
     * Constructor : wrap a {@link java.lang.Throwable} param into {@link RerunableException}.
     * 
     * @param exception
     * 			wrapped Throwable param.
     * 
     */
	public RerunableException(Throwable cause) {
		super(cause);
	}

	/**
     * Constructor : wrap a {@link java.lang.Throwable} param into {@link RerunableException} using exception message.
     * 
     * @param msg
     * 			exception message.
     * 
     * @param exception
     * 			wrapped Throwable param.
     * 
     */
	public RerunableException(final String msg,
			final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Get exception message.
	 * 
	 * @return
	 * 			exception message.
	 * 
	 */
	@Override
	public String getMessage() {
		return msg == null ? super.getMessage() : msg;
	}

	/**
	 * Set exception message.
	 * 
	 * @param message
	 *            exception message.
	 *            
	 */
	public void setMessage(final String message) {
		msg = message;
	}

	/**
	 * Get String format of this {@link RerunableException} .
	 * 
	 * @return
	 *				exception message.
	 *
	 */
	@Override
	public String toString() {
		return getMessage();
	}

}

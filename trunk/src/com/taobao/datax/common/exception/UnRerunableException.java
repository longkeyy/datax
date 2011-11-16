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
 * One kind of exception DataX throws in condition of wrong operation, serious problems
 * The exception usually indicates it is not worthy to do again.
 * 
 * @see RerunableException
 * */
public class UnRerunableException extends RuntimeException {

	private static final long serialVersionUID = 7895371373503323805L;
	
	private String msg;

	/**
     * Constructor.
     * 
     * @param message
     * 			exception message.
     * 
     */
    public UnRerunableException(final String message) {
        super(message);
    }

    /**
     * Constructor :  wrap a exception to {@link UnRunableException}.
     * 
     * @param exception
     * 			wrapped exception.
     * 
     */
    public UnRerunableException(final Exception exception) {
    	super();
	    	if (null != exception) {
	    		msg = exception.getMessage();
	    	}
	} 
    
    /**
     * A default constructor.
     * 
     */
    public UnRerunableException() {
		super();
	}

    /**
     * Constructor : wrap a {@link java.lang.Throwable} param to {@link UnRerunableException}.
     * 
     * @param exception
     * 			wrapped Throwable param.
     * 
     */
	public UnRerunableException(Throwable cause) {
		super(cause);
	}

	/**
     * Constructor :  wrap a {@link java.lang.Throwable} param to {@link UnRerunableException} using exception message.
     * 
     * @param msg
     * 			exception message.
     * 
     * @param exception
     * 			wrapped Throwable param.
     * 
     */
	public UnRerunableException(final String msg,
			final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Get exception message.
	 * 
	 * @return
	 * 			exception message.
	 */
	@Override
	public String getMessage() {
		return msg == null ? super.getMessage() : msg;
	}

	/**
	 * Set exception message.
	 * 
	 * @param message
	 * 			exception message.
	 * 
	 */
	public void setMessage(final String message) {
		msg = message;
	}

	/**
	 * Get information of this {@link UnRerunableException}.
	 * 
	 * @return 
	 * 			exception message.
	 * 
	 */
	@Override
	public String toString() {
		return getMessage();
	}

}

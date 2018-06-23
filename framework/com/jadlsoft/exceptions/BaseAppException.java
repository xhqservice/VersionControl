/**
 * <p>Title: BaseAppException</p>
 * <p>Description: Ӧ��Exception����</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: ��������</p>
 * @author ����
 * @version 1.0
 * 2006-9-18
 */

package com.jadlsoft.exceptions;

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseAppException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  An optional nested exception used to provide the ability to encapsulate
	 *  a lower-level exception to provide more detailed context information
	 *  concerning the exact cause of the exception.
	 */
	protected Throwable rootCause = null;

	/**
	 *  Holds a collection of exceptions added to this exception, for use when the
	 *  application desires to return multiple exception conditions at once. For
	 *  instance, if the application must evaluate and validate multiple pieces
	 *  of data before performing an operation, it might be beneficial to
	 *  validate all of the data and collect all resulting exceptions to be
	 *  passed back at once.
	 */
	private List exceptions = new ArrayList();

	/**
	 * The key to the bundle message.
	 */
	private String messageKey = null;
	
	/**
	 * An optional array of arguments to use with the bundle message.
	 */
	private Object[] messageArgs = null;

	/**
	 * Empty Constructor
	 */
	public BaseAppException() {
		super();
	}

	/**
	 *  A Constructor that takes a root cause throwable.
	 */
	public BaseAppException(Throwable cause) {
		this.rootCause = cause;
	}

	/**
	 * A Constructor that takes a message by messageKey.
	 * @param msgKey
	 * @param args
	 */
	public BaseAppException(String msgKey, Object[] args) {
		this.messageKey = msgKey;
		this.messageArgs = args;
	}
	
	/**
	 *  Return the root cause exception, if one exists.
	 */
	public Throwable getRootCause() {
		return rootCause;
	}
	
	/**
	 *  Set a nested, encapsulated exception to provide more low-level detailed
	 *  information to the client.
	 */
	public void setRootCause(Throwable anException) {
		rootCause = anException;
	}
	
	/**
	 *  Retrieve the collection of exceptions.
	 */
	public List getExceptions() {
		return exceptions;
	}

	/**
	 * Add an exception to the exception collection.
	 */
	public void addException(BaseAppException ex) {
		exceptions.add(ex);
	}

	/**
	 * Retrieve the message bundle key.
	 */
	public String getMessageKey() {
		return messageKey;
	}
	
	/**
	 * Set the key to the bundle.
	 */
	public void setMessageKey(String key) {
		this.messageKey = key;
	}

	/**
	 * Retrieve the optional arguments.
	 */
	public Object[] getMessageArgs() {
		return messageArgs;
	}
	
	/**
	 * Set an object array that can be used for parametric replacement.
	 */
	public void setMessageArgs(Object[] args) {
		this.messageArgs = args;
	}
	
	/**
	 *  Print both the normal and rootCause stack traces.
	 */
	public void printStackTrace(PrintWriter writer) {
		super.printStackTrace(writer);
		if (getRootCause() != null) {
			getRootCause().printStackTrace(writer);
		}
		writer.flush();
	}

	/**
	 *  Print both the normal and rootCause stack traces.
	 */
	public void printStackTrace(PrintStream outStream) {
		printStackTrace(new PrintWriter(outStream));
	}

	/**
	 *  Print both the normal and rootCause stack traces.
	 */
	public void printStackTrace() {
		printStackTrace(System.err);
	}
	
	/**
	 * Description: 
	 * @return
	 */
	public static BaseAppException getSimpleBaseAppException(String messagekey, Object[] args) {
		BaseAppException be = new BaseAppException();
		be.addException(new BaseAppException(messagekey, args));
		return be;
	}
	
	/**
	 * Description: 
	 * @return
	 */
	public static BaseAppException getSimpleBaseAppException(String messagekey) {
		return getSimpleBaseAppException(messagekey, null);
	}

}

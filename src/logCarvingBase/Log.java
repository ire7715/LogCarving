package logCarvingBase;

import java.util.Map;

public abstract class Log {
	public static final int ID_COMMENT = 1;
	public static final int ID_WARNING = 2;
	public static final int ID_ERROR = 3;
	
	/**
	 * Close the connection. 
	 * finalize() is called implicitly, which is called by GC.
	 * close() is call explicitly, which is called by user. (recommended)
	 */
	public abstract void finalize();
	
	/**
	 * Close the connection. 
	 * finalize() is called implicitly, which is called by GC.
	 * close() is call explicitly, which is called by user. (recommended)
	 */
	public abstract void close();
	
	/**
	 * Build a connection from here to log system.
	 * @param additional
	 * @throws Exception
	 */
	public abstract void establishConnection(Map<String, String> additional) throws Exception;
	
	/**
	 * send a message to the logging DB.
	 * @param messageLabel : int, label to represent status of this message.
	 * @param message : String, just message.
	 */
	public abstract void send(int messageLabel, String message) throws Exception;
	
	/**
	 * ends an exception information to the logging DB.
	 * @param messageLabel : int, a label which represents the status of this exception. 
	 * @param e : Exception, the raised exception.
	 * @throws Exception
	 */
	public abstract void send(int messageLabel, Exception e) throws Exception;
	
	/**
	 * get the line number of the error code occurred.
	 * @param e : Exception, which exception to fetch.
	 * @return int : line number.
	 */
	public static int lineNumber(Exception e){
		return e.getStackTrace()[0].getLineNumber();
	}
	
	/**
	 * get which method raised this exception.
	 * @param threw : Throwable, raised.
	 * @return String
	 */
	private static String causedBy(Throwable threw){
		return threw.getStackTrace()[0].getMethodName();
	}
	
	/**
	 * get the line number of the calling code occurred.
	 * @param th : Thread, the thread which is line wanted. (often be Thread.currentThread())
	 * @return int : line number.
	 */
	public static int lineNumber(Thread th){
		final int HIDEOUT = 2; // JRE 1.7, magic number for tracking line number
		return th.getStackTrace()[HIDEOUT].getLineNumber();
	}
	private static int lineNumber(Thread th, int hideout){
		return th.getStackTrace()[hideout].getLineNumber();
	}
	
	/**
	 * get the method which caused the exception.
	 * @param th : Thread, the thread which is method wanted.
	 * @return String
	 */
	public static String causedBy(Thread th){
		final int HIDEOUT = 2; // JRE 1.7, magic number for tracking line number
		return th.getStackTrace()[HIDEOUT].getMethodName();
	}
	public static String causedBy(Thread th, int hideout){
		return th.getStackTrace()[hideout].getMethodName();
	}
	
	/**
	 * build an error message for you.
	 * @param instance : Object, current object.
	 * @param message : String, messages you want to enter.
	 * @return String
	 */
	public static String buildMessage(Object instance, String message){
		final int HIDEOUT = 3; // one more layer than lineNumber has.
		return "line(" + Log.lineNumber(Thread.currentThread(), HIDEOUT) + ")@" + Log.causedBy(Thread.currentThread()) + "@" + instance.getClass() + " -- " + message;
	}
	
	/**
	 * build an error message from an exception.
	 * @param instance : Object, the object encountered an error.
	 * @param e : Exception, the exception.
	 * @return String
	 */
	public static String buildMessage(Object instance, Exception e){
		return "line(" + Log.lineNumber(e) + ")@" + Log.causedBy(e) + "@" + instance.getClass() + " -- " + e.getMessage();
	}
}

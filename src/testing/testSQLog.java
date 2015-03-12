package SQLog;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import logCarvingBase.Log;

import org.junit.Test;


public class testSQLog {
	private static final String host = "0.0.0.0";
	private static final String db = "xxx";
	private static final String user = "xxx";
	private static final String pass = "xxx";
	private static final String table = "logging";
	private static final String labelCol = "level";
	private static final String messageCol = "message";

	@Test
	public void testSend() {
		SQLog logger = new SQLog();
		Map<String, String> info = new HashMap<String, String>();
		info.put("host", testSQLog.host);
		info.put("user", testSQLog.user);
		info.put("pass", testSQLog.pass);
		info.put("db", testSQLog.db);
		info.put("table", testSQLog.table);
		info.put("label", testSQLog.labelCol);
		info.put("message", testSQLog.messageCol);
		try{
			logger.establishConnection(info);

			logger.send(0, this.getClass() + ": " + Log.lineNumber(Thread.currentThread()) + " -- " + "testing");
			String test = Log.buildMessage(this, "testing");
			assertEquals("build message from thread", "line(33)@buildMessage@class logCarving.testSQLog -- testing", test);
			//System.out.println(test);
			String violence = null;
			try{
				violence.length();
			}catch(Exception e2){
				test = Log.buildMessage(this, e2);
				assertEquals("line(38)@testSend@class logCarving.testSQLog -- null", test);
				//System.out.println(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

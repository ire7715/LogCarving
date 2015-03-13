package testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import logCarvingBase.Log;

import org.junit.Test;

import ElasticLog.ElasticLog;
import ElasticLog.ElasticLogger;

public class testElasticLog {

	//@Test
	public void test() {
		ElasticLog elaLog = new ElasticLog();
		Map<String, String> info = new HashMap<String, String>();
		info.put("host", "0.0.0.0");
		info.put("apiport", "0");
		info.put("cluster_name", "FacebookDB");
		info.put("index", "logtest");
		info.put("type", "logging");
		
		
		try {
			elaLog.establishConnection(info);
			elaLog.send(0, "test1");
			String s = null;
			try{
				s.length();
			} catch(Exception e1){
				elaLog.send(1, e1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLogger(){
		class tester extends ElasticLogger{
			public tester(){
				super("properties.xml");
			}
			
			public void touch(){
				try {
					this.logger.send(Log.ID_COMMENT, "12321312312312414");
					System.err.println("12321312312312414");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public void finalize(){
				super.finalize();
			}
		}
		
		tester t1 = new tester();
		t1.touch();
		t1.finalize();
		t1 = null;
	}

}

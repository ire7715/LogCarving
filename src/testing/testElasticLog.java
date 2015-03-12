package testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ElasticLog.ElasticLog;

public class testElasticLog {

	@Test
	public void test() {
		ElasticLog elaLog = new ElasticLog();
		Map<String, String> info = new HashMap<String, String>();
		info.put("host", "0.0.0.0");
		info.put("apiport", "0");
		info.put("cluster_name", "TwitterDB");
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

}

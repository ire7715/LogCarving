package logCarving;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class logLoader {
	public static SQLog SQLogger(String properties) throws Exception{
		Properties props = new Properties();
		Map<String, String> info = new HashMap<String, String>();
		props.loadFromXML(new FileInputStream(properties));
		
		info.put("host", props.getProperty("logCarving_host"));
		info.put("user", props.getProperty("logCarving_user"));
		info.put("pass", props.getProperty("logCarving_pass"));
		info.put("db", props.getProperty("logCarving_db"));
		info.put("table", props.getProperty("logCarving_table"));
		info.put("label", props.getProperty("logCarving_label"));
		info.put("message", props.getProperty("logCarving_message"));
		SQLog logger = new SQLog();
		logger.establishConnection(info);
		return logger;
	}
	
	public static ElasticLog ElasticLogger(String properties) throws Exception {
		Properties props = new Properties();
		Map<String, String> info = new HashMap<String, String>();
		props.loadFromXML(new FileInputStream(properties));
		
		info.put("host", props.getProperty("logCarving_host"));
		info.put("apiport", props.getProperty("logCarving_apiport"));
		info.put("cluster_name", props.getProperty("logCarving_cluster_name"));
		info.put("index", props.getProperty("logCarving_index"));
		info.put("type", props.getProperty("logCarving_type"));
		ElasticLog logger = new ElasticLog();
		logger.establishConnection(info);
		return logger;
	}
}

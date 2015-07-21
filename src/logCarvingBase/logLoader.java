package logCarvingBase;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ElasticLog.ElasticLog;
import SQLog.SQLog;

public class logLoader {
	public static SQLog SQLogger(String properties) throws Exception{
		Properties props = new Properties();
		Map<String, String> info = new HashMap<String, String>();
		FileInputStream propsStream = new FileInputStream(properties);
		props.loadFromXML(propsStream);
		propsStream.close();
		
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
		FileInputStream propsStream = new FileInputStream(properties);
		props.loadFromXML(propsStream);
		propsStream.close();
		
		info.put("host", props.getProperty("logCarving_host"));
		info.put("user", props.getProperty("logCarving_user"));
		info.put("password", props.getProperty("logCarving_password"));
		info.put("apiport", props.getProperty("logCarving_apiport"));
		info.put("cluster_name", props.getProperty("logCarving_cluster_name"));
		info.put("index", props.getProperty("logCarving_index"));
		info.put("type", props.getProperty("logCarving_type"));
		ElasticLog logger = new ElasticLog();
		logger.establishConnection(info);
		return logger;
	}
}

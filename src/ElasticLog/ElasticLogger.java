package ElasticLog;

import logCarvingBase.Logger;
import logCarvingBase.logLoader;

public abstract class ElasticLogger extends Logger{
	public ElasticLogger(String propsAt){
		try{
			this.logger = logLoader.ElasticLogger(propsAt);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Logger initialize failed.");
		}
	}
}

package SQLog;

import logCarvingBase.Logger;
import logCarvingBase.logLoader;

public abstract class SQLogger extends Logger {
	public SQLogger(String propsAt){
		try{
			this.logger = logLoader.SQLogger(propsAt);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Logger initialize failed.");
		}
	}
}

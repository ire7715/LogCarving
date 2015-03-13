package logCarvingBase;

public abstract class Logger {
	protected Log logger = null;
	
	public void finalize(){
		this.logger.close();
		this.logger = null;
	}
}

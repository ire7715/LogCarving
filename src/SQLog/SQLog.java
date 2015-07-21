package SQLog;

import java.util.Map;
import java.sql.*;

import logCarvingBase.Log;

public class SQLog extends Log {
	private Connection conn = null;
	private String host = null;
	private String user = null;
	private String pass = null;
	private String db = null;
	private String table = null;
	private String labelCol = null;
	private String messageCol = null;

	/**
	 * establish the connection between logger and logging system.
	 * @param host : String, host of the logging system.
	 * @param user : String, user name in the logging system.
	 * @param pass : String, password corresponds to the user.
	 * @param additional : Map<String, String>, additional information which may need while the connecting phase.
	 * <p>
	 *  additional of SQLog should contain
	 *  <br> 
	 *  	db : for the USE `db`.
	 *  <br>
	 *  	table : for the INSERT INTO `table`.
	 *  <br>
	 *  	label : for the column name of message label.
	 *  <br>
	 *  	message : for the column name of logging message.
	 *  </p>
	 */
	@Override
	public void establishConnection(Map<String, String> additional) throws SQLException{
		host = additional.get("host");
		user = additional.get("user");
		pass = additional.get("pass");
		db = additional.get("db");
		table = additional.get("table");
		labelCol = additional.get("label");
		messageCol= additional.get("message");
		this.conn = this.connect();
	}
	
	public void finalize(){
		if (conn != null){
			try{
				conn.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void close(){
		this.finalize();
	}
	
	public boolean isConnected(){
		try {
			return (this.conn != null && !this.conn.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Connection connect(){
		while(!this.isConnected()){
			try {
				this.conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + db, user, pass);
				Statement stmt = conn.createStatement();
				stmt.execute("SET NAMES utf8;");
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					Thread.sleep(Log.RECONNECT_WAIT);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					return null;
				}
			}
		}
		return this.conn;
	}

	@Override
	public void send(int messageLabel, String message) throws SQLException{
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO `" + this.table + "`(`" + labelCol + "`, `" + messageCol + "`)");
		query.append("VALUES(?, ?)");
		PreparedStatement stmt = this.connect().prepareStatement(query.toString());
		stmt.setInt(1, messageLabel);
		stmt.setString(2, message);
		stmt.executeUpdate();
		stmt.close();
	}

	@Override
	public void send(int messageLabel, Exception e) throws SQLException {
		this.send(messageLabel, Log.buildMessage(this, e));
	}
}

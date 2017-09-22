package cpscorereport;

import java.sql.SQLException;

public interface IServerHelper {
	public void startServer(String dbUrl, String dbName, String username, String password, CPscorereport scorer) throws SQLException;
	public void stopServer();
	public boolean isRunning();
	public String getURL();
	public IDatabaseConnection newDbConn();
}

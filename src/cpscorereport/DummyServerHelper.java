package cpscorereport;

import java.sql.SQLException;

public class DummyServerHelper implements IServerHelper {

	@Override
	public void startServer(String dbUrl, String dbName, String username, String password, CPscorereport scorer)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopServer() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDatabaseConnection newDbConn() {
		// TODO Auto-generated method stub
		return null;
	}

}

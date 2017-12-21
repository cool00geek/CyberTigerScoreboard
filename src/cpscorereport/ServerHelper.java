package cpscorereport;

import java.sql.SQLException;

public class ServerHelper {

    protected boolean myStatus = false;
    protected String connUrl;

    public void startServer(String dbUrl, String dbName, String username, String password, CPscorereport scorer)
            throws SQLException {
        // TODO Auto-generated method stub
    }

    public void stopServer() {
        myStatus = false;
    }

    public boolean isRunning() {
        return myStatus;
    }

    public String getURL() {
        if (connUrl != null && myStatus) {
            return connUrl;
        }
        return "";
    }

    public IDatabaseConnection newDbConn() {
        // TODO Auto-generated method stub
        return null;
    }

}

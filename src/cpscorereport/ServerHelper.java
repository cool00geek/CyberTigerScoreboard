/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 054457
 */
public class ServerHelper {

    private String connUrl;
    private boolean myAzureStatus;

    public ServerHelper() {
        myAzureStatus = false;
    }

    public void startAzureServer(String dbUrl, String dbName, String username, String password, CPscorereport scorer) throws SQLException {
        System.out.println("Azure Server started!");
        try {
            System.out.print("Attempting connection...");
            connUrl = "jdbc:sqlserver://" + dbUrl + ";database=" + dbName + ";user=" + username + "@ctsb;password=" + password + ";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            DriverManager.getConnection(connUrl);
            System.out.println(" Conected!\n");
            myAzureStatus = true;
            scorer.createEverything();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            String error = "";
            error += "An error has occured:\n" + ex.getMessage();
            error += "\nIt is possible that your IP is blacklisted!";
            URL whatismyip = null;
            try {
                whatismyip = new URL("http://checkip.amazonaws.com");
            } catch (MalformedURLException ex1) {
                Logger.getLogger(ServerHelper.class.getName()).log(Level.SEVERE, null, ex1);
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                String ip = in.readLine();
                error += "\nYou are trying to connect from " + ip;
            } catch (IOException ex1) {
                Logger.getLogger(ServerHelper.class.getName()).log(Level.SEVERE, null, ex1);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            error += "\nYour username is " + username + ", and you are trying to connect to " + dbName;
            myAzureStatus = false;
            throw new SQLException(error);
        }
    }

    public void stopAzureServer() {
        myAzureStatus = false;
        System.out.println("Stopping server...");
    }

    public String getURL() {
        if (connUrl != null) {
            return connUrl;
        }
        return "";
    }

    public boolean isAzureRunning() {
        return myAzureStatus;
    }
}

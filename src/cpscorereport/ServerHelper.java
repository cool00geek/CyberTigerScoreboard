/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.text.Text;

/**
 *
 * @author 054457
 */
public class ServerHelper {

    public static Thread startServer(int port, String fileName) {
        final Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    System.out.println("Server started!");
                    try {
                        ServerSocket serverSocket = new ServerSocket(port);
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        while (true) {
                            String cominginText;
                            cominginText = in.readLine();
                            cominginText = cominginText.replaceAll("[^A-Za-z0-9 -]", "").trim();
                            System.out.println(cominginText);
                            FileWriter appender = new FileWriter(fileName, true);
                            appender.write("\r\n" + cominginText);//appends the string to the file
                            //appends the string to the file
                        }
                    } catch (IOException ex) {
                        System.out.println("An issue....");
                        return;
                    }
                }
            }
        }
        );
        server.start();
        return server;
    }

    public static void stopServer(Thread theServer) {
        final Thread stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Stopping server...");
                theServer.interrupt();
            }
        });
        stopper.start();
    }

    public static Thread startJumpServer(String fileName) {
        final Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {

                }
            }
        }
        );
        server.start();
        return server;
    }

    public static void stopJumpServer(Thread theServer) {
        final Thread stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Stopping server...");
                theServer.interrupt();
            }
        });
        stopper.start();
    }

    public static Thread startAzureServer(String fileName, String username, String password) {
        final Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    System.out.println("Azure Server started!");
                    try {
                        System.out.print("Attempting connection...");
                        String url = "jdbc:sqlserver://ctsb.database.windows.net:1433;database=CPscores;user=" + username + "@ctsb;password=" + password + ";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        Connection conn = DriverManager.getConnection(url);
                        System.out.println(" Conected!\n");

                        Statement st = conn.createStatement();
                        ResultSet rs = st.executeQuery("SELECT * FROM TeamScores");
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();

                        FileWriter appender = new FileWriter(fileName, true);

                        while (rs.next()) {
                            appender.write("\r\n");
                            for (int i = 1; i <= columnsNumber; i++) {
                                appender.write(rs.getString(i) + " ");
                            }
                        }
                    } catch (ClassNotFoundException | IOException | SQLException ex) {
                        System.out.println("An issue....\n" + ex);
                        return;
                    }
                }
            }
        }
        );
        server.start();
        return server;
    }

    public static void stopAzureServer(Thread theServer) {
        final Thread stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Stopping server...");
                theServer.interrupt();
            }
        });
        stopper.start();
    }

    public static void exportToExcel(Text infoBox) {
        String filename = "Scoreboard_";
        filename += new SimpleDateFormat("MMddyy_HHmm").format(Calendar.getInstance().getTime());
        infoBox.setText("Saving as " + filename);
        // Do save stuff...
        try {
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            Logger.getLogger(ServerHelper.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        infoBox.setText("Saved as " + filename);
    }
}

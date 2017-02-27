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
                    ServerSocket serverSocket;
                    try {
                        serverSocket = new ServerSocket(port);
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

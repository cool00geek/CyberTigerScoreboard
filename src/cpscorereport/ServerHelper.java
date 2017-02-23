/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpscorereport;

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

    public static String startServer() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            //do Stuff;
            return "Server started for Windows";
        }
        return "Server cannot be started for this OS";
    }

    public static String stopServer() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            //do Stuff;
            return "Server stopped successfully";
        }
        return "Unable to find running server to stop";
    }

    public static void exportToExcel(Text infoBox) {
        String filename = "Scoreboard_";
        filename += new SimpleDateFormat("MMddyy_HHmm").format(Calendar.getInstance().getTime());
        infoBox.setText("Saving as " + filename);
        // Do save stuff...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        infoBox.setText("Saved as " + filename);
    }
}

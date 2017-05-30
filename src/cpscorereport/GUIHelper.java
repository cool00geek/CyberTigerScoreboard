/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

/**
 *
 * @author Vinay
 */
public class GUIHelper {

    private String myAzureDBUser;
    private String myAzureDBPass;
    private final ServerHelper myServerHelp;
    private final CPscorereport myScorer;

    public GUIHelper(CPscorereport scorer) {
        myAzureDBUser = "";
        myAzureDBPass = "";
        myServerHelp = new ServerHelper();
        myScorer = scorer;
    }

    public MenuBar getMenu(Text textbox) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu serverMenu = new Menu("Server");
        Menu helpMenu = new Menu("Help");

        MenuItem startAz = new MenuItem("Start Azure server");
        startAz.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isAzureRunning()){
                textbox.setText("Server already running!");
                System.out.println("Azure server already running! Nothing to start!");
                return;
            }
            textbox.setText("Configuring Azure server...");
            TextInputDialog urlb = new TextInputDialog("xxxx.database.windows.net:1433");
            urlb.setTitle("Azure server");
            urlb.setHeaderText("Enter your Azure server location(URL)");
            urlb.setContentText("Enter your Azure server location(URL):");

            // Traditional way to get the response value.
            Optional<String> result = urlb.showAndWait();
            String DBUrl = "";
            if (result.isPresent()) {
                DBUrl = result.get();
            } else {
                textbox.setText("Azure server not running!");
                return;
            }

            TextInputDialog dbName = new TextInputDialog("CPscores");
            dbName.setTitle("Azure DB");
            dbName.setHeaderText("Enter your Azure Database name");
            dbName.setContentText("Enter your Azure Database name:");

            // Traditional way to get the response value.
            Optional<String> result2 = dbName.showAndWait();
            String DBn = "";
            if (result2.isPresent()) {
                DBn = result2.get();
            } else {
                textbox.setText("Azure server not running!");
                return;
            }

            TextInputDialog userPrompt = new TextInputDialog("Azure username");
            userPrompt.setTitle("Azure login");
            userPrompt.setHeaderText("Enter your Azure DB username");
            userPrompt.setContentText("Enter your Azure DB username:");

            Optional<String> result3 = userPrompt.showAndWait();
            if (result3.isPresent()) {
                myAzureDBUser = result3.get();
            } else {
                textbox.setText("Azure server not running!");
                return;
            }

            PasswordDialog pwPrompt = new PasswordDialog();
            Optional<String> result4 = pwPrompt.showAndWait();
            if (result4.isPresent()) {
                myAzureDBPass = result4.get();
            } else {
                textbox.setText("Azure server not running!");
                return;
            }

            textbox.setText("Starting Azure server...");
            try {
                myServerHelp.startAzureServer(DBUrl, DBn, myAzureDBUser, myAzureDBPass, myScorer);
                textbox.setText("Azure Server started!");
                System.out.println("Azure Server started!");
            } catch (SQLException ex) {
                textbox.setText("Starting azure server failed!");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Server start failure");
                alert.setHeaderText("An error occured while starting the server");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
                System.out.println(ex.getMessage());
            }

        });

        MenuItem refreshItem = new MenuItem("Refresh data");
        refreshItem.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isAzureRunning()) {
                try {
                    myScorer.createEverything();
                } catch (IOException ex) {
                    Logger.getLogger(GUIHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Server not running!");
                alert.setContentText("The server is not running! Try setting up the server before refreshing!");
                alert.showAndWait();
            }
        });

        MenuItem stopAz = new MenuItem("Stop Azure server");
        stopAz.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isAzureRunning()){
            textbox.setText("Stopping Azure server...");
            myServerHelp.stopAzureServer();
            textbox.setText("Azure Server stopped!");
            System.out.println("Azure Server stopped!");
            } else {
                textbox.setText("Azure server not running!");
                System.out.println("Azure server not running!");
            }
        });

        MenuItem export = new MenuItem("Export to xls");
        export.setOnAction((ActionEvent t) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Export to Excel");
            alert.setHeaderText("How to export to Excel");
            alert.setContentText("1) Open a blank Excel sheet\n2) In the 'Data' tab, choose 'Other sources' --> SQL\n3) Enter the DB location, and enter your DB username and password");
            alert.showAndWait();
        });

        MenuItem quit = new MenuItem("Exit");
        quit.setOnAction((ActionEvent t) -> {
            myServerHelp.stopAzureServer();
            System.exit(0);
        });

        MenuItem about = new MenuItem("About...");
        about.setOnAction((ActionEvent t) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About CyberTiger Scoreboard");
            alert.setHeaderText("About the CyberTiger Scoreboard");
            alert.setContentText("Created by @billwi and @hexalellogram for the VHS CyberPatriot team!\nShoutout to Mr. Osborne for running CyberPatriot, Irvin for guiding us, and Mr. Parker for giving us the knowledge to create this scoreboard!\n\nThis is licensed under the GNU/GPL V3 Public license!");
            alert.showAndWait();
        });

        fileMenu.getItems().addAll(export, quit);
        serverMenu.getItems().addAll(startAz, refreshItem, stopAz);
        helpMenu.getItems().addAll(about);
        menuBar.getMenus().addAll(fileMenu, serverMenu, helpMenu);

        return menuBar;
    }

    public void updateScores(ArrayList<Team> teams, String filename) {
        try {
            BufferedReader inFile = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String L1 = inFile.readLine();
            while (L1 != null) {
                // Parse it into Team-OS, SCORE, and TIME
                String[] teamScoreTime = L1.split(" ");
                String teamnameWOS = teamScoreTime[0];
                int score = Integer.parseInt(teamScoreTime[1]);
                String time = teamScoreTime[2];

                String[] names = teamnameWOS.split("-"); // Make it just OS and 
                String teamname = names[0];
                String OS = names[1];

                // Check if it exists, or if we need a new team
                int loc = -1;
                for (int i = 0; i < teams.size(); i++) {
                    if (teams.get(i).getTeamName().equals(teamname)) {
                        loc = i;
                    }
                }

                if (loc == -1) { // New team if it isn't
                    teams.add(new Team(teamname, new Score(time, score), OS));
                } else { // add to existing
                    teams.get(loc).addScore(new Score(time, score), OS);
                }
                L1 = inFile.readLine();
                System.out.println("Scores updated!");
            }
        } catch (IOException ex) {
            System.out.println("An error has occured!\n" + ex + "\n\nIf the file was not found, don't worry, as the file has been created!");
        }
    }

    public String getConnURL() {
        if (myServerHelp != null) {
            return myServerHelp.getURL();
        }
        return "";
    }

    public boolean isAzureRunning() {
        return myServerHelp.isAzureRunning();
    }
}

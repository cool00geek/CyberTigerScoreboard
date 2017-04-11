/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

    private Thread myServerThread; //The thread with the running server
    private Thread myJumpServerThread; //The thread with the running jump server
    private Thread myAzureServerThread; //The thread with the running Azure server
    private int myPort; //The port the server is running on
    private final String myFilename; // The filename of the file to read
    private String myAzureDBUser;
    private String myAzureDBPass;

    public GUIHelper(String filename) {
        myServerThread = null;
        myPort = 1947;
        myFilename = filename;
        myAzureDBUser = "";
        myAzureDBPass = "";
    }

    public MenuBar getMenu(Text textbox) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu serverMenu = new Menu("Server");
        Menu helpMenu = new Menu("Help");

        MenuItem startServer = new MenuItem("Start TCP Server");
        startServer.setOnAction((ActionEvent t) -> {
            textbox.setText("Starting server on port " + myPort + "...");
            myServerThread = ServerHelper.startServer(myPort, myFilename);
            textbox.setText("Server started on port " + myPort + "!");
            System.out.println("TCP Server started on " + myPort);
        });

        MenuItem TCPServerConfig = new MenuItem("Configure TCP Server");
        TCPServerConfig.setOnAction((ActionEvent t) -> {
            TextInputDialog dialog = new TextInputDialog("" + myPort);
            dialog.setTitle("TCP Port");
            dialog.setHeaderText("Choose a custom TCP Port");
            dialog.setContentText("Please enter the port of your choice:");
            
            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    String res = result.get();
                    if (res.contains("-")) {
                        res += "asd";
                    }
                    int newPort = Integer.parseInt(res);
                    if (myPort != newPort) {
                        textbox.setText("Please restart the server to use port: " + newPort);
                        System.out.println("The new port is " + newPort);
                        myPort = newPort;
                    } else {
                        textbox.setText("Port unchanged! Still using " + newPort + ".");
                    }
                } catch (NumberFormatException e) {
                    textbox.setText(result.get() + " is not a valid port! Still using " + myPort);
                }
            }
        });

        MenuItem stopServer = new MenuItem("Stop TCP Server");
        stopServer.setOnAction((ActionEvent t) -> {
            textbox.setText("Stopping TCP server...");
            ServerHelper.stopServer(myServerThread);
            myServerThread = null;
            textbox.setText("Server stopped!");
            System.out.println("Server stopped!");
        });

        MenuItem startJump = new MenuItem("Start Jump Server");
        stopServer.setOnAction((ActionEvent t) -> {
            textbox.setText("Starting Jump server...");
            myJumpServerThread = ServerHelper.startJumpServer(myFilename);
            textbox.setText("Jump server started!");
            System.out.println("Jump server started!");
        });

        MenuItem stopJump = new MenuItem("Stop Jump Server");
        stopServer.setOnAction((ActionEvent t) -> {
            textbox.setText("Stopping Jump server...");
            ServerHelper.stopJumpServer(myJumpServerThread);
            textbox.setText("Jump server stopped!");
            System.out.println("Jump Server stopped!");
        });

        MenuItem startAz = new MenuItem("Start Azure server");
        startAz.setOnAction((ActionEvent t) -> {
            textbox.setText("Configuring Jump server...");
            TextInputDialog urlb = new TextInputDialog("xxxx.database.windows.net:1433");
            urlb.setTitle("Azure server");
            urlb.setHeaderText("Enter your Azure server location(URL)");
            urlb.setContentText("Enter your Azure server location(URL):");
            
            // Traditional way to get the response value.
            Optional<String> result = urlb.showAndWait();
            String DBUrl = "";
            if (result.isPresent()){
                DBUrl = result.toString();
            }
            
            TextInputDialog dbName = new TextInputDialog("CPscores");
            dbName.setTitle("Azure DB");
            dbName.setHeaderText("Enter your Azure Database name");
            dbName.setContentText("Enter your Azure Database name:");
            
            // Traditional way to get the response value.
            Optional<String> result2 = dbName.showAndWait();
            String DBn = "";
            if (result2.isPresent()){
                DBn = result2.toString();
            }
            
            TextInputDialog userPrompt = new TextInputDialog("Azure username");
            userPrompt.setTitle("Azure login");
            userPrompt.setHeaderText("Enter your Azure DB username");
            userPrompt.setContentText("Enter your Azure DB username:");
            
            // Traditional way to get the response value.
            Optional<String> result3 = userPrompt.showAndWait();
            result3.ifPresent(username -> myAzureDBUser = username);
            
            PasswordDialog pwPrompt = new PasswordDialog();
            Optional<String>  result4 = pwPrompt.showAndWait();
            result4.ifPresent(password -> myAzureDBPass = password);
            
            textbox.setText("Starting Azure server...");
            myAzureServerThread = ServerHelper.startAzureServer(myFilename, DBUrl, DBn, myAzureDBUser, myAzureDBPass);
            textbox.setText("Azure Server started!");
            System.out.println("Azure Server started!");
        });

        MenuItem stopAz = new MenuItem("Stop Azure server");
        stopAz.setOnAction((ActionEvent t) -> {
            textbox.setText("Stopping Azure server...");
            ServerHelper.stopAzureServer(myAzureServerThread);
            myAzureServerThread = null;
            textbox.setText("Azure Server stopped!");
            System.out.println("Azure Server stopped!");
        });

        MenuItem export = new MenuItem("Export to xls");
        export.setOnAction((ActionEvent t) -> {
            Dialog aboutDiag = new Dialog();
            aboutDiag.setContentText("1) Open a blank Excel sheet\n2) In the 'Data' tab, choose 'Other sources' --> SQL\n3) Enter the DB location, and enter your DB username and password");
            aboutDiag.setTitle("Export to Excel");
            aboutDiag.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = aboutDiag.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
            aboutDiag.showAndWait();
        });

        MenuItem quit = new MenuItem("Exit");
        quit.setOnAction((ActionEvent t) -> {
            ServerHelper.stopServer(myServerThread);
            System.exit(0);
        });

        MenuItem about = new MenuItem("About...");
        about.setOnAction((ActionEvent t) -> {
            Dialog aboutDiag = new Dialog();
            aboutDiag.setContentText("Created by @billwi and @hexalellogram for the VHS CyberPatriot team!\nShoutout to Mr. Osborne for running CyberPatriot, Irvin for guiding us, and Mr. Parker for giving us the knowledge to create this scoreboard!\n\nThis is licensed under the GNU/GPL V3 Public license!");
            aboutDiag.setTitle("About CyberTiger Scoreboard");
            aboutDiag.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = aboutDiag.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
            aboutDiag.showAndWait();
        });

        fileMenu.getItems().addAll(export, quit);
        serverMenu.getItems().addAll(startAz, stopAz, startServer, TCPServerConfig, stopServer, startJump, stopJump);
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
}

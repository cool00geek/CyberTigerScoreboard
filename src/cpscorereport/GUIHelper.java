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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    Thread myServerThread; //The thread with the running server
    int myPort; //The port the server is running on
    String myFilename; // The filename of the file to read

    public GUIHelper(String filename) {
        myServerThread = null;
        myPort = 1947;
        myFilename = filename;
    }

    public MenuBar getMenu(Text textbox) {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu serverMenu = new Menu("Server");
        Menu helpMenu = new Menu("Help");

        MenuItem startServer = new MenuItem("Start TCP Server");
        startServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                textbox.setText("Starting server on port " + myPort + "...");
                myServerThread = ServerHelper.startServer(myPort, myFilename);
                textbox.setText("Server started on port " + myPort + "!");
                System.out.println("TCP Server started on " + myPort);
            }
        });

        MenuItem TCPServerConfig = new MenuItem("Configure TCP Server");
        TCPServerConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
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
                            System.out.println("The new port is " + newPort );
                            myPort = newPort;
                        } else {
                            textbox.setText("Port unchanged! Still using " + newPort + ".");
                        }
                    } catch (NumberFormatException e) {
                        textbox.setText(result.get() + " is not a valid port! Still using " + myPort);
                    }
                }
            }
        });

        MenuItem stopServer = new MenuItem("Stop TCP Server");
        stopServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                textbox.setText("Stopping server...");
                ServerHelper.stopServer(myServerThread);
                textbox.setText("Server stopped!");
                System.out.println("Server stopped!");
            }
        });

        MenuItem export = new MenuItem("Export to xls");
        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ServerHelper.exportToExcel(textbox);
            }
        });

        MenuItem quit = new MenuItem("Exit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ServerHelper.stopServer(myServerThread);
                System.exit(0);
            }
        });

        MenuItem about = new MenuItem("About...");
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Dialog aboutDiag = new Dialog();
                aboutDiag.setContentText("Created by @billwi and @hexalellogram for the VHS CyberPatriot team!\nShoutout to Mr. Osborne for running CyberPatriot, Irvin for guiding us, and Mr. Parker for giving us the knowledge to create this scoreboard!\n\nThis is licensed under the GNU/GPL V3 Public license!");
                aboutDiag.setTitle("About CyberTiger Scoreboard");
                aboutDiag.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                Node closeButton = aboutDiag.getDialogPane().lookupButton(ButtonType.CLOSE);
                closeButton.managedProperty().bind(closeButton.visibleProperty());
                closeButton.setVisible(false);
                aboutDiag.showAndWait();
            }
        });

        fileMenu.getItems().addAll(export, quit);
        serverMenu.getItems().addAll(startServer, TCPServerConfig, stopServer);
        helpMenu.getItems().addAll(about);
        menuBar.getMenus().addAll(fileMenu, serverMenu, helpMenu);

        return menuBar;
    }

    public void updateScores(ArrayList<Team> teams, String filename) {
        try {
            BufferedReader inFile = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            //BufferedReader inFile = new BufferedReader(new FileReader(filename, "UTF-8"));
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
            }
        } catch (IOException ex) {
            Logger.getLogger(GUIHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Scores updated!");
    }
}

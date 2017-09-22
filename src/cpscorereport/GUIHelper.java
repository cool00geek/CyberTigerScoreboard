/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Vinay
 */
public class GUIHelper {

    private String myAzureDBUser; // The database username
    private String myAzureDBPass; // The DB password
    private IServerHelper myServerHelp; // The server helper for the GUI Helper
    private final CPscorereport myScorer; // The scorer to refresh if required

    public GUIHelper(CPscorereport scorer) {
        myAzureDBUser = ""; // Initialize variables
        myAzureDBPass = "";
        myServerHelp = new DummyServerHelper();
        myScorer = scorer; // Make sure it already exists
    }

    public MenuBar getMenu(Text textbox) {
        MenuBar menuBar = new MenuBar(); // Create the menubar
        Menu fileMenu = new Menu("File"); // Create the file menu tab
        Menu serverMenu = new Menu("Server"); // Create the server tab
        Menu helpMenu = new Menu("Help"); // Add the file tab

        MenuItem startAz = new MenuItem("Start Azure server"); // Have an option to start the Azure server 
        startAz.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isRunning()) { // If it's already running,
                textbox.setText("Server already running!"); // Tell them
                return; // And exit
            }
            textbox.setText("Configuring Azure server..."); // Otherwise, tell them
            TextInputDialog urlb = new TextInputDialog("xxxx.database.windows.net:1433"); // Give them a URL template
            urlb.setTitle("Azure server"); // Set the dialog's title
            urlb.setHeaderText("Enter your Azure server location (URL)"); // Set the URL location
            urlb.setContentText("Enter your Azure server location (URL):"); // Give a prompt

            Optional<String> result = urlb.showAndWait(); // Get the result
            String DBUrl; // Cretae the string to hold onto the URL
            if (result.isPresent()) { // Check if they hit ok
                DBUrl = result.get(); // Then get it
            } else {
                textbox.setText("Azure server not running!"); // Tell them it isn't running still
                return; // And exit since they hit cancel
            }

            TextInputDialog dbName = new TextInputDialog("CPscores"); // The next dialog asks for the DB name: CPscores is default
            dbName.setTitle("Azure DB"); // Set the title of the db
            dbName.setHeaderText("Enter your Azure Database name"); // Ask for their one
            dbName.setContentText("Enter your Azure Database name:"); // Prompt

            Optional<String> result2 = dbName.showAndWait(); // Get the result
            String DBn; // String to hold it
            if (result2.isPresent()) { // Check if they hit ok or cancel
                DBn = result2.get(); // Get it 
            } else {
                textbox.setText("Azure server not running!"); // Tell them it isn't running
                return; // Exit
            }

            TextInputDialog userPrompt = new TextInputDialog("Azure username"); // Ask for the username
            userPrompt.setTitle("Azure login"); // Set the title
            userPrompt.setHeaderText("Enter your Azure DB username"); // Ask for their username
            userPrompt.setContentText("Enter your Azure DB username:"); // Prompt

            Optional<String> result3 = userPrompt.showAndWait(); // Get the result
            if (result3.isPresent()) { // If they hit ok
                myAzureDBUser = result3.get(); // get the value
            } else {
                textbox.setText("Azure server not running!"); // Otherwise tell them it still isn't running
                return; // and exit
            }

            PasswordDialog pwPrompt = new PasswordDialog("Azure"); // Get the password dialog
            Optional<String> result4 = pwPrompt.showAndWait(); // Get the result
            if (result4.isPresent()) { // Check if they entered it
                myAzureDBPass = result4.get(); // Get the password
            } else {
                textbox.setText("Azure server not running!"); // Tell them it still isn't running
                return; //exit
            }

            textbox.setText("Starting Azure server..."); // Let them know we're trying to start
            try {
                myServerHelp = new AzureServerHelper();
            	myServerHelp.startServer(DBUrl, DBn, myAzureDBUser, myAzureDBPass, myScorer); // Start it
                textbox.setText("Azure Server started!"); // Tell them it started
            } catch (SQLException ex) { // We would have an exception otherwise
                textbox.setText("Starting azure server failed!"); // Dialog to tell them it didn't work
                Alert alert = new Alert(AlertType.ERROR); // Give an error alert
                alert.setTitle("Server start failure"); // Tell them it failed
                alert.setHeaderText("An error occured while starting the server"); // Tell them something is wrong
                alert.setContentText(ex.getMessage()); // Give the message
                alert.showAndWait(); // Show the error
            }
        });

        MenuItem startMy = new MenuItem("Start MySQL server"); // Have an option to start the Azure server 
        startMy.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isRunning()) { // If it's already running,
                textbox.setText("Server already running!"); // Tell them
                return; // And exit
            }
            textbox.setText("Configuring MySQL server..."); // Otherwise, tell them
            TextInputDialog urlb = new TextInputDialog("localhost:3306"); // Give them a URL template
            urlb.setTitle("MySQL server"); // Set the dialog's title
            urlb.setHeaderText("Enter your MySQL server location (URL)"); // Set the URL location
            urlb.setContentText("Enter your MySQL server location (URL):"); // Give a prompt

            Optional<String> result = urlb.showAndWait(); // Get the result
            String DBUrl; // Cretae the string to hold onto the URL
            if (result.isPresent()) { // Check if they hit ok
                DBUrl = result.get(); // Then get it
            } else {
                textbox.setText("MySQL server not running!"); // Tell them it isn't running still
                return; // And exit since they hit cancel
            }

            TextInputDialog dbName = new TextInputDialog("CPscores"); // The next dialog asks for the DB name: CPscores is default
            dbName.setTitle("MySQL DB"); // Set the title of the db
            dbName.setHeaderText("Enter your MySQL Database name"); // Ask for their one
            dbName.setContentText("Enter your MySQL Database name:"); // Prompt

            Optional<String> result2 = dbName.showAndWait(); // Get the result
            String DBn; // String to hold it
            if (result2.isPresent()) { // Check if they hit ok or cancel
                DBn = result2.get(); // Get it 
            } else {
                textbox.setText("MySQL server not running!"); // Tell them it isn't running
                return; // Exit
            }

            TextInputDialog userPrompt = new TextInputDialog("MySQL username"); // Ask for the username
            userPrompt.setTitle("MySQL login"); // Set the title
            userPrompt.setHeaderText("Enter your MySQL DB username"); // Ask for their username
            userPrompt.setContentText("Enter your MySQL DB username:"); // Prompt

            Optional<String> result3 = userPrompt.showAndWait(); // Get the result
            if (result3.isPresent()) { // If they hit ok
                myAzureDBUser = result3.get(); // get the value
            } else {
                textbox.setText("MySQL server not running!"); // Otherwise tell them it still isn't running
                return; // and exit
            }

            PasswordDialog pwPrompt = new PasswordDialog("MySQL"); // Get the password dialog
            Optional<String> result4 = pwPrompt.showAndWait(); // Get the result
            if (result4.isPresent()) { // Check if they entered it
                myAzureDBPass = result4.get(); // Get the password
            } else {
                textbox.setText("MySQL server not running!"); // Tell them it still isn't running
                return; //exit
            }

            textbox.setText("Starting MySQL server..."); // Let them know we're trying to start
            try {
                myServerHelp = new MySQLServerHelper();
            	myServerHelp.startServer(DBUrl, DBn, myAzureDBUser, myAzureDBPass, myScorer); // Start it
                textbox.setText("MySQL Server started!"); // Tell them it started
            } catch (SQLException ex) { // We would have an exception otherwise
                textbox.setText("Starting MySQL server failed!"); // Dialog to tell them it didn't work
                Alert alert = new Alert(AlertType.ERROR); // Give an error alert
                alert.setTitle("Server start failure"); // Tell them it failed
                alert.setHeaderText("An error occured while starting the server"); // Tell them something is wrong
                alert.setContentText(ex.getMessage()); // Give the message
                alert.showAndWait(); // Show the error
            }
        });



     
        MenuItem refreshItem = new MenuItem("Refresh data"); // Get the refresh button
        refreshItem.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isRunning()) { // Make sure it's running
                try {
                    myScorer.refreshData(); // Refresh
                } catch (IOException ex) {
                    System.out.println("Something isn't right...\n" + ex); // Tell them it won't work
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING); // Show an alert
                alert.setTitle("Warning"); // Tell them it isn't running
                alert.setHeaderText("Server not running!"); // Set the title
                alert.setContentText("The server is not running! Try setting up the server before refreshing!"); // Show the content
                alert.showAndWait(); // Show the entire alert
            }
        });

        MenuItem stopDB = new MenuItem("Stop server"); // Stop it
        stopDB.setOnAction((ActionEvent t) -> {
            if (myServerHelp.isRunning()) { // Make sure it's running
                textbox.setText("Stopping DB server..."); // Tell them it's stopping
                myServerHelp.stopServer(); // Stop it
                textbox.setText("DB Server stopped!"); // Tell them it stopped
            } else {
                textbox.setText("DB server not running!"); // It already isn't running!
            }
        });
        
        MenuItem export = new MenuItem("Export to xls"); // Export to excel
        export.setOnAction((ActionEvent t) -> {
            Alert alert = new Alert(AlertType.INFORMATION); // Show an alert
            alert.setTitle("Export to Excel"); // Tell them they're trying to export
            alert.setHeaderText("How to export to Excel"); // Tell them how
            alert.setContentText("1) Open a blank Excel sheet\n2) In the 'Data' tab, choose 'Other sources' --> SQL\n3) Enter the DB location, and enter your DB username and password");
            alert.showAndWait(); // Show the alert
        });

        MenuItem quit = new MenuItem("Exit"); // Option to exit
        quit.setOnAction((ActionEvent t) -> {
            myServerHelp.stopServer(); // Stop the server
            System.exit(0); // Close
        });

        MenuItem about = new MenuItem("About..."); // Show them an about box
        about.setOnAction((ActionEvent t) -> {
            Alert alert = new Alert(AlertType.INFORMATION); // Make it info
            alert.setTitle("About CyberTiger Scoreboard"); // About it
            alert.setHeaderText("About the CyberTiger Scoreboard"); // About
            alert.setContentText("Created by @billwi and @hexalellogram for the VHS CyberPatriot team!\nShoutout to Mr. Osborne for running CyberPatriot, Irvin for guiding us, and Mr. Parker for giving us the knowledge to create this scoreboard!\n\nThis is licensed under the GNU/GPL V3 Public license!");
            alert.showAndWait(); // Show them 
        });

        fileMenu.getItems().addAll(export, quit); // Add the items to the about button
        serverMenu.getItems().addAll(startAz, startMy, refreshItem, stopDB); // Add the items to the server option
        helpMenu.getItems().addAll(about); // Add it to the about section
        
        menuBar.getMenus().addAll(fileMenu, serverMenu, helpMenu); // Add all options to the menu
        return menuBar; // Give the bar
    }

    public String getConnURL() {
        if (myServerHelp != null) {
            return myServerHelp.getURL();
        }
        return "";
    }

    public boolean isDBRunning() {
    	if(myServerHelp==null){
    		return false;
    	}
        return myServerHelp.isRunning();
    }

    public Text createTextBox(String defText) {
        Text text = new Text(defText); // Info text box
        text.setFont(new Font(20)); // Make the font sane

        return text; // Give it to them
    }

	public IDatabaseConnection newDbConn() {
		return myServerHelp.newDbConn();
	}
}

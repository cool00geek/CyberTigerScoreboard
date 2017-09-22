/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vinay/@billwi
 */
public class CPscorereport extends Application {

    final private double DEFAULT_WIDTH = 1366; // Default width for 720p
    final private double DEFAULT_HEIGHT = 720; // Default height for 720p
    final private String ICON_LOC = "/resources/icon.png"; // The location of the icon
    final private int REFRESH_TIMEOUT = 60; // Delay in seconds between refreshes
    private GUIHelper setUpHelp; // A helper class to setup GUI components
    private Text info; // The text box on the GUI responsible for giving the end user feedback
    private TabPane teamTabs; // The tabs showing the teams
    private MenuBar menuBar; // The menubar on top with the different categories
    private BorderPane borderPane; // The main pane with stuff
    private StackPane elementSect; // The pane inside ^ to contain the objects
    private IDatabaseConnection dbConn;

    @Override
    public void start(Stage mainWin) throws IOException {
        teamTabs = new TabPane(); // Initialize the pane with for the tabs
        setUpHelp = new GUIHelper(this); // Initialize the GUI helper class
        info = setUpHelp.createTextBox("Server not configured!"); // Initialize the textbox
        menuBar = setUpHelp.getMenu(info); // Initialize the menubar and the menus
        elementSect = new StackPane(); // Initialize the element stackpane
        elementSect.getChildren().add(teamTabs); // Add the tabs from teamtabs there
        borderPane = new BorderPane(); // Add the border pane
        borderPane.setTop(menuBar); // Add stuff to the borders
        borderPane.setCenter(elementSect); // But the elementSect in the middle
        borderPane.setBottom(info); // Put the textpane in the bottom
        Scene scene = new Scene(borderPane, DEFAULT_WIDTH, DEFAULT_HEIGHT); // Create the scene for the height
        mainWin.getIcons().add(new Image(ICON_LOC)); // Set the icon as the CyberTiger icon
        mainWin.setTitle("CyberTiger Scoreboard"); // Get the window name
        mainWin.setScene(scene); // Set the window
        mainWin.show(); // Show the window

        refreshData(); // Refresh the data since this creates the rest of teh GUI

        Timeline scoreboardRefresh = new Timeline(new KeyFrame(Duration.seconds(REFRESH_TIMEOUT), (ActionEvent event) -> {
            try {
                refreshData(); // Put the refresh method in this method to autorefresh every minute
            } catch (IOException ex) { // Catch the exception from the database conn
                info.setText("Error refreshing scores! " + ex); // Show the errors
            }
        }));
        scoreboardRefresh.setCycleCount(Timeline.INDEFINITE); // Set the number of times to run
        scoreboardRefresh.play(); // Run the timer
    }

    public void refreshData() throws IOException {
        ArrayList<Team> teams; // Create an arraylist of the teams
        String url = setUpHelp.getConnURL(); // Get the database connection URL
        if (setUpHelp.isDBRunning()) { // Make sure the server is running first
            info.setText("Refreshing data..."); // Let the user know it's refreshing
            teams = dbConn.loadList(url); // Get the URL to load
        } else {
            info.setText("Server not running!"); // Tell the user the status
            return; // Don't do anything
        }

        Tab[] teamTabList = new Tab[teams.size() + 1]; // Create the list/array for each team and the all teams

        int totalOSes = 0; // Get the total number of OSes
        totalOSes = teams.stream().map((team) -> team.getTotalOS()).reduce(totalOSes, Integer::sum); // Count the OSes using a lamda expression

        XYChart.Series[] scoreSeries = new XYChart.Series[totalOSes]; // Create array for a lot of series
        LineChart[] charts = new LineChart[teamTabList.length]; // Create a chart for each team

        int seriesPos = 0; // Where are we adding? We have some teams with multiple OSes and some without

        for (int i = 1; i < teamTabList.length; i++) {
            teamTabList[i] = new Tab(); // Initialize the tab
            teamTabList[i].setText(teams.get(i - 1).getTeamName()); // Set the name of the tab to the team

            NumberAxis timeAxis = new NumberAxis(); // Create the X axis
            timeAxis.setLabel("Running Time"); // Set the axis label

            NumberAxis pointsAxis = new NumberAxis(); // Create the Y Axis
            pointsAxis.setLabel("Points"); // Set the axis label

            charts[i] = new LineChart(timeAxis, pointsAxis); // Create the chart for this tab
            charts[i].setTitle("Scoreboard: Team " + teams.get(i - 1).getTeamName()); // Set the title to the name of the team

            ArrayList<ArrayList<Score>> allScores = teams.get(i - 1).getScores(); // Get an array of array for all the scores
            ArrayList<String> OSName = teams.get(i - 1).getOSes(); // Get the OS name(s)
            for (int j = 0; j < allScores.size(); j++) { // Go for each OS
                XYChart.Series thisSeries = new XYChart.Series(); // Create a new chart for this team
                ArrayList<Score> scores = allScores.get(j); // Get that particular score set
                scoreSeries[seriesPos] = new XYChart.Series(); // Set the series tehre
                thisSeries.setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(j)); // Set the title
                scoreSeries[seriesPos].setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(j)); // Set the graph for the all teams tab
                for (int k = 0; k < scores.size(); k++) { // Add every score
                    int time = scores.get(k).getTimeInt(); // Get the time
                    int score = scores.get(k).getScore(); // Get the score
                    scoreSeries[seriesPos].getData().add(new XYChart.Data(time, score)); // Add it to the total score
                    thisSeries.getData().add(new XYChart.Data(time, score)); // Add it to the team's chart
                }
                charts[i].getData().add(thisSeries); // Add this partcular series
                seriesPos++; // Increment the tab we're at
                teamTabList[i].setContent(charts[i]); //Add it
                teamTabList[i].setClosable(false); // Make sure the user doesn't close the tab
            }
        }
        teamTabList[0] = new Tab(); // Create the all teams tab
        teamTabList[0].setText("All teams"); // Set the title
        teamTabList[0].setClosable(false); // Make sure the user can't close it
        NumberAxis xAx = new NumberAxis(); // Create the axis for time
        xAx.setLabel("Running Time"); // Set the running time
        NumberAxis yAx = new NumberAxis(); // Set the y axis for points
        yAx.setLabel("Points"); // Set the name 
        LineChart allChart = new LineChart(xAx, yAx); // Create the chart
        allChart.setTitle("Scoreboard: All Teams"); // Set the chart's title
        allChart.getData().addAll(Arrays.asList(scoreSeries)); // Add all the data we need
        teamTabList[0].setContent(allChart); // Set the content

        int loc = teamTabs.getSelectionModel().getSelectedIndex(); // Get the old selected index for seamless updating

        while (!teamTabs.getTabs().isEmpty()) { // Keep adding
            teamTabs.getTabs().remove(0); // Keep adding while freeing memory
        }
        teamTabs.getTabs().addAll(Arrays.asList(teamTabList)); // Add the data/tabs to the tabpane
        if (teamTabList.length >= loc) { // Make sure the existing location is greater than what we have
            teamTabs.getSelectionModel().select(loc); // Select that tab
        } else { // If something happened (DB side data change)
            teamTabs.getSelectionModel().select(0); // Set the location to 0 (all teams)
        }
        info.setText("Data updated at " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "!"); // Tell them we updated the data, and the time

    }

    /**
     * @param args the command line arguments. It should not be used.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vinay/@billwi
 */
public class CPscorereport extends Application {

    int refreshNum = 0;
    double width = 1366;
    double height = 720;
    boolean maximized = true;

    @Override
    public void start(Stage mainWin) throws FileNotFoundException {

        int REFRESH_TIMEOUT = 60; // Delay in seconds between refreshes
        String ICON_LOC = "/resources/icon.png"; // The location of the icon
        String FILENAME = "rawData.txt";

        // Info text box
        Text info = new Text("Server stopped!");
        info.setFont(new Font(20));

        // Get the menubar
        GUIHelper help = new GUIHelper(FILENAME);
        MenuBar menuBar = help.getMenu(info);

        // Get the CT icon
        Image icon = new Image(CPscorereport.class.getResourceAsStream(ICON_LOC));
        ImageView iconView = new ImageView();
        iconView.setImage(icon);
        iconView.setFitWidth(120);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.setCache(true);

        // Set the splash screen
        Text loading1 = new Text("      Opening CyberTiger Scoreboard...\n   It may take over a minute. Please wait...\n   The app is responding! Do not close it!");
        loading1.setFont(new Font(20));
        TilePane tempLoader = new TilePane();
        tempLoader.setPrefRows(8);
        tempLoader.getChildren().add(iconView);
        TilePane.setAlignment(iconView, Pos.CENTER);
        tempLoader.getChildren().add(loading1);
        TilePane.setAlignment(loading1, Pos.CENTER);

        // Set the icon for all windows created
        mainWin.getIcons().add(new Image(CPscorereport.class.getResourceAsStream(ICON_LOC)));
        Scene loader = new Scene(tempLoader, 380, 240);
        mainWin.setTitle("CyberTiger Launcher");

        // Start the loader
        mainWin.setScene(loader);

        // Make it visible
        mainWin.show();

        // Make it refresh
        Timeline scoreboardRefresh = new Timeline(new KeyFrame(Duration.seconds(REFRESH_TIMEOUT), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Make sure dimensions don't change

                //Update the scores
                ArrayList<Team> teams = new ArrayList<>();
                help.updateScores(teams, FILENAME);

                // Get tabs
                TabPane teamTabs = new TabPane(); //Create the tab pane
                Tab[] teamTabList = new Tab[teams.size() + 1];
                // Create some tabs with the total teams and one for all teams

                // Get all the OSes
                int totalOSes = 0;
                totalOSes = teams.stream().map((team) -> team.getTotalOS()).reduce(totalOSes, Integer::sum);

                XYChart.Series[] scoreSeries = new XYChart.Series[totalOSes]; // Create array for a lot of series
                LineChart[] charts = new LineChart[teamTabList.length]; // Create a chart for each team

                int seriesPos = 0; // Where are we adding? We have some teams with multiple OSes and some without

                for (int i = 1; i < teamTabList.length; i++) {
                    // Configure tab
                    teamTabList[i] = new Tab();
                    teamTabList[i].setText(teams.get(i - 1).getTeamName());
                    teamTabList[i].setClosable(false);

                    // Configure tab contents
                    // X Axis
                    NumberAxis timeAxis = new NumberAxis();
                    timeAxis.setLabel("Running Time");

                    // Y Axis
                    NumberAxis pointsAxis = new NumberAxis();
                    pointsAxis.setLabel("Points");

                    // Create the chart
                    LineChart chart1 = new LineChart(timeAxis, pointsAxis);
                    charts[i] = chart1;
                    charts[i].setTitle("Scoreboard: Team " + teams.get(i - 1).getTeamName());

                    // Create series (or more, depending on how many OSes they have
                    ArrayList<ArrayList<Score>> allScores = teams.get(i - 1).getScores();
                    ArrayList<String> OSName = teams.get(i - 1).getOSes();
                    for (int sumenum = 0; sumenum < allScores.size(); sumenum++) {
                        XYChart.Series thisSeries = new XYChart.Series();
                        ArrayList<Score> scores = allScores.get(sumenum);
                        scoreSeries[seriesPos] = new XYChart.Series();
                        thisSeries.setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(sumenum));
                        scoreSeries[seriesPos].setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(sumenum));
                        for (int k = 0; k < scores.size(); k++) {
                            int time = scores.get(k).getTimeInt();
                            int score = scores.get(k).getScore();
                            scoreSeries[seriesPos].getData().add(new XYChart.Data(time, score));
                            thisSeries.getData().add(new XYChart.Data(time, score));
                        }
                        charts[i].getData().add(thisSeries);
                        seriesPos++;
                        teamTabList[i].setContent(charts[i]);
                    }
                }
                // Configure all teams tab
                teamTabList[0] = new Tab();
                teamTabList[0].setText("All teams");
                teamTabList[0].setClosable(false);
                NumberAxis xAx = new NumberAxis();
                xAx.setLabel("Running Time");
                NumberAxis yAx = new NumberAxis();
                yAx.setLabel("Points");
                LineChart allChart = new LineChart(xAx, yAx);
                allChart.setTitle("Scoreboard: All Teams");
                allChart.getData().addAll(Arrays.asList(scoreSeries));
                teamTabList[0].setContent(allChart);

                // Make all tabs visible
                teamTabs.getTabs().addAll(Arrays.asList(teamTabList));

                // Make the menubar visible
                BorderPane borderPane = new BorderPane();
                borderPane.setTop(menuBar);

                // Set the main pane
                StackPane elementSect = new StackPane();
                elementSect.getChildren().add(teamTabs);

                // Add everything to the border pane (even the main pane
                borderPane.setCenter(elementSect);
                borderPane.setBottom(info);

                // Make sure the window has good dimensions
                Scene scene;
                if (refreshNum == 0) {
                    scene = new Scene(borderPane, width, height);
                } else {
                    scene = new Scene(borderPane);
                }
                //Scene scene = new Scene(borderPane);
                mainWin.setTitle("CyberTiger Scoreboard");
                mainWin.setScene(scene);
                mainWin.show();
                mainWin.setMaximized(true);
                refreshNum++;
            }
        }));
        // Make it update every min
        scoreboardRefresh.setCycleCount(Timeline.INDEFINITE);
        scoreboardRefresh.play();
    }

    /**
     * @param args the command line arguments. It should not be used.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

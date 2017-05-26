/*
 * GNU/GPL v3. Check out https://github.com/billwi/CyberTigerScoreboard for more info
 */
package cpscorereport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vinay/@billwi
 */
public class CPscorereport extends Application {

    final private double DEFAULT_WIDTH = 1366;
    final private double DEFAULT_HEIGHT = 720;
    final private String ICON_LOC = "/resources/icon.png"; // The location of the icon
    final private int REFRESH_TIMEOUT = 60; // Delay in seconds between refreshes
    private GUIHelper setUpHelp;
    private Text info;
    private TabPane teamTabs;
    private MenuBar menuBar;
    private BorderPane borderPane;
    private StackPane elementSect;
    boolean maximized = true;

    private Text createTextBox(String defText) {
        Text text = new Text(defText); // Info text box
        text.setFont(new Font(20));

        return text;
    }

    @Override
    public void start(Stage mainWin) throws FileNotFoundException, IOException {
        teamTabs = new TabPane();
        info = createTextBox("Server not configured!");
        setUpHelp = new GUIHelper(this);
        menuBar = setUpHelp.getMenu(info);
        borderPane = new BorderPane();

        borderPane.setTop(menuBar);
        elementSect = new StackPane();
        elementSect.getChildren().add(teamTabs);
        borderPane.setCenter(elementSect);
        borderPane.setBottom(info);
        Scene scene = new Scene(borderPane, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        mainWin.getIcons().add(new Image(ICON_LOC));
        mainWin.setTitle("CyberTiger Scoreboard");
        mainWin.setScene(scene);
        mainWin.show();

        createEverything();

        // Make it refresh
        Timeline scoreboardRefresh = new Timeline(new KeyFrame(Duration.seconds(REFRESH_TIMEOUT), (ActionEvent event) -> {
            try {
                createEverything();
            } catch (IOException ex) {
                Logger.getLogger(CPscorereport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));

        // Make it update every min
        scoreboardRefresh.setCycleCount(Timeline.INDEFINITE);
        scoreboardRefresh.play();
    }

    public void createEverything() throws IOException {
        ArrayList<Team> teams;
        String url = setUpHelp.getConnURL();
        if (!url.equals("")) {
            if (setUpHelp.isAzureRunning()) {
                System.out.println("Refreshing data...");
                info.setText("Refreshing data...");
                teams = DataBaseConnection.loadList(url);
            } else {
                info.setText("Server not running!");
                return;
            }

            Tab[] teamTabList = new Tab[teams.size() + 1];

            int totalOSes = 0;
            totalOSes = teams.stream().map((team) -> team.getTotalOS()).reduce(totalOSes, Integer::sum);

            XYChart.Series[] scoreSeries = new XYChart.Series[totalOSes]; // Create array for a lot of series
            LineChart[] charts = new LineChart[teamTabList.length]; // Create a chart for each team

            int seriesPos = 0; // Where are we adding? We have some teams with multiple OSes and some without

            for (int i = 1; i < teamTabList.length; i++) {
                // Configure tab
                teamTabList[i] = new Tab();
                teamTabList[i].setText(teams.get(i - 1).getTeamName());

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
                for (int j = 0; j < allScores.size(); j++) {
                    XYChart.Series thisSeries = new XYChart.Series();
                    ArrayList<Score> scores = allScores.get(j);
                    scoreSeries[seriesPos] = new XYChart.Series();
                    thisSeries.setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(j));
                    scoreSeries[seriesPos].setName("Scores for team " + teams.get(i - 1).getTeamName() + " " + OSName.get(j));
                    for (int k = 0; k < scores.size(); k++) {
                        int time = scores.get(k).getTimeInt();
                        int score = scores.get(k).getScore();
                        scoreSeries[seriesPos].getData().add(new XYChart.Data(time, score));
                        thisSeries.getData().add(new XYChart.Data(time, score));
                    }
                    charts[i].getData().add(thisSeries);
                    seriesPos++;
                    teamTabList[i].setContent(charts[i]);
                    teamTabList[i].setClosable(false);
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

            int loc = teamTabs.getSelectionModel().getSelectedIndex();

            // Make all tabs visible
            while (!teamTabs.getTabs().isEmpty()) {
                teamTabs.getTabs().remove(0);
            }
            teamTabs.getTabs().addAll(Arrays.asList(teamTabList));
            if (teamTabList.length >= loc) {
                teamTabs.getSelectionModel().select(loc);
            } else {
                teamTabs.getSelectionModel().select(0);
            }
            info.setText("Data updated at " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "!");
        }
    }

    /**
     * @param args the command line arguments. It should not be used.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

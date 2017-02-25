/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpscorereport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vinay
 */
public class CPscorereport extends Application {

    @Override
    public void start(Stage mainWin) throws FileNotFoundException {

        // Info text box
        Text info = new Text("Server stopped!");
        info.setFont(new Font(20));

        // Get the menubar
        GUIHelper help = new GUIHelper();
        MenuBar menuBar = help.getMenu(info);
        ArrayList<Team> teams = new ArrayList<>();

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(60), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ArrayList<Team> teams = new ArrayList<>();
                help.updateScores(teams); //Update the scores

                // Get tabs
                TabPane teamTabs = new TabPane(); //Create the tab pane
                Tab[] teamTabList = new Tab[teams.size() + 1];
                // Create some tabs with the total teams and one for all teams

                // Get all the OSes
                int totalOSes = 0;
                for (Team team : teams) {
                    totalOSes += team.getTotalOS();
                }

                XYChart.Series[] scoreSeries = new XYChart.Series[totalOSes]; // Create array for a lot of series
                LineChart[] charts = new LineChart[teamTabList.length]; // Create a chart for each team

                int seriesPos = 0;

                for (int i = 1; i < teamTabList.length; i++) {

                    // Configure tab
                    teamTabList[i] = new Tab();
                    teamTabList[i].setText(teams.get(i - 1).getTeamName());
                    teamTabList[i].setClosable(false);

                    // COnfigure tab contents
                    // X Axis
                    NumberAxis timeAxis = new NumberAxis();
                    timeAxis.setLabel("Running Time");

                    // Y Axis
                    NumberAxis pointsAxis = new NumberAxis();
                    pointsAxis.setLabel("Points");

                    // Create chart
                    LineChart chart1 = new LineChart(timeAxis, pointsAxis);
                    charts[i] = chart1;
                    charts[i].setTitle("Scoreboard: Team " + teams.get(i - 1).getTeamName());

                    // Create a series
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
                    }
                    //}
                    teamTabList[i].setContent(charts[i]);
                }
                // Get the list of teams
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
                for (XYChart.Series scoreSerie : scoreSeries) {
                    allChart.getData().add(scoreSerie);
                }
                teamTabList[0].setContent(allChart);

                // Make all tabs visible
                for (Tab tab : teamTabList) {
                    teamTabs.getTabs().add(tab);
                }
                BorderPane borderPane = new BorderPane();
                borderPane.setTop(menuBar);

                // Set the main pane
                StackPane elementSect = new StackPane();
                elementSect.getChildren().add(teamTabs);
                // Add everything to the border
                borderPane.setCenter(elementSect);
                borderPane.setBottom(info);

                Scene scene = new Scene(borderPane, 1366, 720);

                mainWin.setTitle("CyberTiger Scoreboard");
                mainWin.setScene(scene);
                mainWin.show();
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

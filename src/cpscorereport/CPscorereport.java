/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpscorereport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.application.Application;
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

        // Get the menu
        MenuBar menuBar = GUIHelper.getMenu(info);

        // Get the list of teams
        ArrayList<Team> teams = new ArrayList<>();
        GUIHelper.updateScores(teams);

        // Get tabs
        TabPane teamTabs = new TabPane();
        Tab[] teamTabList = new Tab[teams.size() + 1];
        XYChart.Series[] scoreSeries = new XYChart.Series[teams.size()];
        LineChart[] charts = new LineChart[scoreSeries.length];
        for (int i = 1; i < teamTabList.length; i++) {
            // Configure tab
            teamTabList[i] = new Tab();
            teamTabList[i].setText(teams.get(i-1).getTeamName());
            teamTabList[i].setClosable(false);
            teamTabs.getTabs().add(teamTabList[i]);

            // COnfigure tab contents
            NumberAxis xAx = new NumberAxis();
            xAx.setLabel("Running Time");
            NumberAxis yAx = new NumberAxis();
            yAx.setLabel("Points");
            
            LineChart chart1 = new LineChart(xAx, yAx);
            charts[i-1] = chart1;
            charts[i-1].setTitle("Scoreboard: Team " + teams.get(i-1).getTeamName());
            
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Scores for team " + teams.get(i-1).getTeamName());
            
            ArrayList<Score> scores = teams.get(i-1).getScores();
            
            for (int j = 0; j < scores.size(); j++) {
                series1.getData().add(new XYChart.Data(scores.get(j).getTimeInt(), scores.get(j).getScore()));
            }
            
            scoreSeries[i-1] = series1;
            
            charts[i-1].getData().add(scoreSeries[i-1]);
            teamTabList[i].setContent(charts[i-1]);
             
        }

        // Configure all teams tab
        teamTabList[0] = new Tab();
        teamTabList[0].setText("All teams");
        teamTabList[0].setClosable(false);
        teamTabs.getTabs().add(teamTabList[0]);
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
        
        // Configure border (menu bar) and add main pane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        // Set the main pane
        StackPane elementSect = new StackPane();
        elementSect.getChildren().add(teamTabs);

        // Add everything to the border
        borderPane.setCenter(elementSect);
        borderPane.setBottom(info);

        // Set the window
        Scene scene = new Scene(borderPane, 1366, 720);

        mainWin.setTitle("CyberPatriot VHS Scoreboard");
        mainWin.setScene(scene);
        mainWin.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

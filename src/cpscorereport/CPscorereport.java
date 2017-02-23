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
        for (int i = 1; i < teamTabList.length; i++) {
            // Configure tab
            teamTabList[i] = new Tab();
            teamTabList[i].setText(teams.get(i).getTeamName());
            teamTabList[i].setClosable(false);

            // COnfigure tab contents
            NumberAxis xAx = new NumberAxis();
            xAx.setLabel("Running Time");
            NumberAxis yAx = new NumberAxis();
            yAx.setLabel("Points");
            LineChart chart1 = new LineChart(xAx, yAx);
            chart1.setTitle("Scoreboard: Team " + teams.get(i).getTeamName());
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Scores for team " + teams.get(i).getTeamName());
            ArrayList<Score> scores = teams.get(i).getScores();
            for (int j = 0; j < scores.size(); j++) {
                series1.getData().add(new XYChart.Data(scores.get(i).getTimeInt(), scores.get(i).getScore()));
            }
            scoreSeries[i-1] = series1;
            
            chart1.getData().add(series1);

            teamTabs.getTabs().add(teamTabList[i]);
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
        allChart.setTitle("Scoreboard: All Team");
        for (int i = 0; i < scoreSeries.length; i++){
            allChart.getData().add(scoreSeries[i]);
        }
        
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

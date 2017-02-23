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
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Vinay
 */
public class CPscorereport extends Application {

    @Override
    public void start(Stage mainWin) throws FileNotFoundException {

        // Get the menu
        MenuBar menuBar = GUIHelper.getMenu();

        // Get the list of teams
        ArrayList<Team> teams = new ArrayList<>();
        GUIHelper.updateScores(teams);

        // Get tabs
        TabPane teamTabs = new TabPane();
        Tab[] teamTabList = new Tab[teams.size() + 1];
        for (int i = 1; i < teamTabList.length; i++) {
            teamTabList[i] = new Tab();
            teamTabList[i].setText(teams.get(i).getTeamName());
            teamTabs.getTabs().add(teamTabList[i]);
        }
        teamTabList[0] = new Tab();
        teamTabList[0].setText("All teams");
        teamTabs.getTabs().add(teamTabList[0]);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);

        StackPane elementSect = new StackPane();
        elementSect.getChildren().add(teamTabs);

        borderPane.setCenter(elementSect);

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

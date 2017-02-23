/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpscorereport;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
    public void start(Stage mainWin) {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        
        MenuItem startServer = new MenuItem("Start Server");
        startServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Server started!");
            }
        });
        
        MenuItem stopServer = new MenuItem("Stop Server");
        stopServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Server stopped!");
            }
        });
        
        MenuItem export = new MenuItem("Export to xls");
        export.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.out.println("Exported!");
            }
        });
        
        MenuItem quit = new MenuItem("Exit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        fileMenu.getItems().addAll(startServer, stopServer, export, quit);
        menuBar.getMenus().addAll(fileMenu);
        
        
        TabPane teamTabs = new TabPane();
        Tab[] teamTabList = new Tab[2];
        for (int i = 0; i < teamTabList.length; i++){
            teamTabList[i] = new Tab();
        }
        for (int i = 0; i < teamTabList.length; i++){
            teamTabList[i].setText("Team" + i);
            teamTabs.getTabs().add(teamTabList[i]);
        }
        
        
        
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

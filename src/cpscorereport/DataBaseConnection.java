/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpscorereport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vinay
 */
public class DataBaseConnection {

    public static ArrayList<Team> loadList(String connectionString) throws MalformedURLException, IOException {
        ArrayList<Team> teams = new ArrayList<>();

        System.out.print("Attempting...");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(connectionString);
            System.out.println(" Conected!");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TeamScores");
            while (rs.next()) {
                String team = rs.getString(1);
                String os = team.substring(team.indexOf("-") + 1);
                team = team.substring(0, team.indexOf("-"));
                int score = Integer.parseInt(rs.getString(2));
                int time = Integer.parseInt(rs.getString(3));
                int loc = doesTeamExist(teams, team);
                if (loc == -1) {
                    teams.add(new Team(team, new Score("" + time, score), os));
                } else {
                    teams.get(loc).addScore(new Score("" + time, score), os);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(" Error! " + e);
            System.out.println("It is possible that your IP is blacklisted!\nYour ip is:");
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            try (BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()))) {
                String ip = in.readLine();
                System.out.println(ip);
            } catch (IOException ex1) {
                Logger.getLogger(ServerHelper.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return teams;
    }

    private static int doesTeamExist(ArrayList<Team> teams, String teamName) {

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getTeamName().equals(teamName)) {
                return i;
            }
        }
        return -1;
    }
}

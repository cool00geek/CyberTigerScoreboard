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

/**
 *
 * @author Vinay
 */
public class DataBaseConnection {

    public static ArrayList<Team> loadList(String connectionString) throws MalformedURLException, IOException {
        ArrayList<Team> teams = new ArrayList<>(); // Create the team arraylist

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Setup the JDBC connection class
            Connection conn = DriverManager.getConnection(connectionString); // Create the connection
            Statement st = conn.createStatement(); // Prepare the statement
            ResultSet rs = st.executeQuery("SELECT * FROM TeamScores"); // Get the query
            while (rs.next()) { // Get all the data until none is left
                String team = rs.getString(1); // Get the string of the total teamname
                String os = team.substring(team.indexOf("-") + 1); // Get just the OS name
                team = team.substring(0, team.indexOf(os) -1); // Set team to only be the team w/o the OS
                int score = Integer.parseInt(rs.getString(2)); // Get the score
                int time = Integer.parseInt(rs.getString(3)); // Get the running time
                int loc = doesTeamExist(teams, team); // Check fi the team already exists
                if (loc == -1) { // If it doesn't, add a new one
                    teams.add(new Team(team, new Score("" + time, score), os)); // Add a new team
                } else {
                    teams.get(loc).addScore(new Score("" + time, score), os); // Add a score set to the existing team
                }
            }
        } catch (ClassNotFoundException | SQLException e) { // If we couldn't connect or we couldn't find the class
            System.out.println(" Error! " + e); // Print the error
            System.out.println("It is possible that your IP is blacklisted!\nYour ip is:"); // Tell them a possible reason
            URL whatismyip = new URL("http://checkip.amazonaws.com"); // Give them the IP
            try (BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()))) {
                String ip = in.readLine(); // Get the IP
                System.out.println(ip); // Print it out
            } catch (IOException ex1) {
                System.out.println("An error occured while printint the error:\n" + ex1);
            }
        }
        return teams;
    }

    private static int doesTeamExist(ArrayList<Team> teams, String teamName) {
        for (int i = 0; i < teams.size(); i++) // Go through all teams
        {
            if (teams.get(i).getTeamName().equals(teamName)) // Check if the teamname exists
            {
                return i; // If it doesn, return the location
            }
        }
        return -1; // It doesn't exist
    }
}

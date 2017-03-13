/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctsbdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Vinay
 */
public class DBman {

    private String myUsername = "";
    private String myPassword = "";
    private String url;

    public DBman(String username, String password) {
        myUsername = username;
        myPassword = password;
        url = "jdbc:sqlserver://ctsb.database.windows.net:1433;database=CPscores;user=" + myUsername + "@ctsb;password=" + myPassword + ";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public void printTable() {
        System.out.print("Attempting connection...");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);
            System.out.println(" Conected!\n");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM TeamScores");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.printf("%-15s", rs.getString(i));
                }
                System.out.println();
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(" Error!");
            System.out.println(e);
        }
    }

    public void clearTable() {
        System.out.print("Attempting connection...");
        try {
            System.out.println(" Connected!");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Conected");
            Statement st = conn.createStatement();
            System.out.println("Clearing table...");
            try {
                st.executeQuery("Truncate table TeamScores");
            } catch (SQLException e) {
            }
            System.out.println("Table cleared");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(" Error!");
            System.out.println(e);
        }
    }

    public void add(String teamname, int score, int time) {
        System.out.print("Attempting connection...");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);
            System.out.println(" Conected");

            String sql = "INSERT INTO TeamScores(TeamName,Score,Time) VALUES(?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.print("Sending data...");
            pstmt.setString(1, teamname);
            pstmt.setInt(2, score);
            pstmt.setInt(3, time);
            pstmt.executeUpdate();
            System.out.println(" Success!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(" Error!");
            System.out.println(e);
        }
    }

    public String connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url);
            return "good";
        } catch (ClassNotFoundException | SQLException e) {
            return e.toString();
        }
    }

    public void setUsername(String username) {
        myUsername = username;
        url = "jdbc:sqlserver://ctsb.database.windows.net:1433;database=CPscores;user=" + myUsername + "@ctsb;password=" + myPassword + ";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public void setPassword(String password) {
        myPassword = password;
        url = "jdbc:sqlserver://ctsb.database.windows.net:1433;database=CPscores;user=" + myUsername + "@ctsb;password=" + myPassword + ";encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    }

    public String info() {
        return url.substring(0, url.indexOf("database="));
    }
}

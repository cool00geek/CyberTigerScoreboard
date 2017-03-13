/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctsbdb;

import java.io.Console;
import java.util.Scanner;

/**
 *
 * @author Vinay
 */
public class CTSBDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the CyberTiger ScoreBoard Databse management console!");
        DBman conn = new DBman("", "");

        System.out.println("Loading...");
        String b;
        do {
            String username = "";
            do {
                System.out.printf("[%s] ", "AzureDB username:");
                username = new Scanner(System.in).nextLine();
            } while (username.contains(" "));

            Console cons = System.console();
            String password = "";
            char[] passwd;
            if (cons != null) {
                passwd = cons.readPassword("[%s] ", "AzureDB Password:");
                password = new String(passwd);
            } else {
                System.out.printf("[%s] ", "AzureDB Password:");
                password = new Scanner(System.in).nextLine();
            }

            conn.setUsername(username);
            conn.setPassword(password);

            System.out.print("Attempting connection...");
            b = conn.connect();
            if ("good".equals(b)) {
                System.out.println(" Success! You have connected!");
            } else {
                System.out.println(" Failure! You have not connected!\nCommon reasons include bad credentials or a blacklisted IP\n" + b);
            }
        } while (!"good".equals(b));

        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1) Clear table");
            System.out.println("2) Print table");
            System.out.println("3) Add team/score/time to db");
            System.out.println("4) View connection info");
            System.out.println("5) Quit");

            boolean ready = false;
            int c = 0;
            while (!ready) {
                System.out.print("Enter a choice --> ");
                String res = "";
                res = new Scanner(System.in).nextLine();

                try {
                    c = Integer.parseInt(res);
                    if (c >= 1 && c <= 5) {
                        ready = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Bad input!");
                }
            }
            switch (c) {
                case 5:
                    keepRunning = false;
                    break;
                case 4:
                    System.out.println("Database connected to: " + conn.info());
                    break;
                case 3:
                    System.out.print("Teamname: ");
                    String team = new Scanner(System.in).nextLine();
                    int s = -1;
                    do {
                        System.out.print("Score: ");
                        try {
                            s = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter a valid int!");
                        }
                    } while (s < 0);
                    int t = -1;
                    do {
                        System.out.print("Time: ");
                        try {
                            t = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter a valid int!");
                        }
                    } while (t < 0);
                    conn.add(team, s, t);
                    break;
                case 2:
                    conn.printTable();
                    break;
                case 1:
                    conn.clearTable();
                    break;
                default:
                    break;
            }
        }
        System.out.println("Have a nice day!");
    }

}

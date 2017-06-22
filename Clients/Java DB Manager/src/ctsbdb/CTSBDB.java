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
        System.out.println();
        System.out.print("Please enter the full database URL, including port: ");
        String server = new Scanner(System.in).nextLine();
        System.out.print("Thanks! Now please enter the database name you want to access, found at" + server +": ");
        String db = new Scanner(System.in).nextLine();
        DBman conn = new DBman(server, db, "", "");

        System.out.println("Cool, thank! Please wait while I load a couple of things up!");
        String b;
        do {
            System.out.println("Alright, I just need a couple more things.\nI'm going to need your credentials so I can connect to the database.");
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
                System.out.println("I have detected that you are not using a system console! Are you using the IDE's console?\nThis means that I have to use the fallback password prompt!\nYou're password will be displayed as plain text."
                        + "\nIf you have any issues with that, please re-run this utility from a native shell, such as Konsole, Command Prompt, Powershell, etc.");
                System.out.printf("[%s] ", "AzureDB Password:");
                password = new Scanner(System.in).nextLine();
            }

            conn.setUsername(username);
            conn.setPassword(password);

            System.out.print("Hold on while I test the credentials you have provided...");
            b = conn.connect();
            if ("good".equals(b)) {
                System.out.println(" Success! You have connected!");
            } else {
                System.out.println(" Failure! You have not connected!\nCommon reasons include bad credentials or a blacklisted IP\n" + b);
            }
        } while (!"good".equals(b));

        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\nWhat would you like me to do for you?");
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
                    System.out.println("I'm sorry, but I can only do one of the specified tasks, so enter an option from 1-5");
                }
            }
            switch (c) {
                case 5:
                    keepRunning = false;
                    break;
                case 4:
                    System.out.println("I am currently connected to: " + conn.info());
                    break;
                case 3:
                    System.out.print("Please enter the teamname you want to add: ");
                    String team = new Scanner(System.in).nextLine();
                    int s = -1;
                    do {
                        System.out.print("Please enter " + team + "'s score: ");
                        try {
                            s = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter a valid score!");
                        }
                    } while (s < 0);
                    int t = -1;
                    do {
                        System.out.print("Please enter " + team + "'s time: ");
                        try {
                            t = new Scanner(System.in).nextInt();
                        } catch (Exception e) {
                            System.out.println("Please enter a valid time, in seconds, so just an int!");
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
        System.out.println("Have a nice day, I will see you soon!!");
    }

}

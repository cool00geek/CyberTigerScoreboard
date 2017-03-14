using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CSDBman
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Welcome to the CyberTiger ScoreBoard Databse management console!");
            DBman conn = new DBman("", "");

            Console.WriteLine("Loading...");
            String b;
            do
            {
                String username = "";
                do
                {
                    Console.Write("[%s] ", "AzureDB username:");
                    username = new Scanner(System.in).nextLine();
                } while (username.Contains(" "));

                Console cons = System.console();
                String password = "";
                char[] passwd;
                if (cons != null)
                {
                    passwd = cons.readPassword("[%s] ", "AzureDB Password:");
                    password = new String(passwd);
                }
                else
                {
                    Console.Writef("[%s] ", "AzureDB Password:");
                    password = new Scanner(System.in).nextLine();
                }

                conn.setUsername(username);
                conn.setPassword(password);

                Console.Write("Attempting connection...");
                b = conn.connect();
                if ("good".equals(b))
                {
                    Console.WriteLine(" Success! You have connected!");
                }
                else
                {
                    Console.WriteLine(" Failure! You have not connected!\nCommon reasons include bad credentials or a blacklisted IP\n" + b);
                }
            } while (!"good".equals(b));

            bool keepRunning = true;

            while (keepRunning)
            {
                Console.WriteLine("\nWhat would you like to do?");
                Console.WriteLine("1) Clear table");
                Console.WriteLine("2) Print table");
                Console.WriteLine("3) Add team/score/time to db");
                Console.WriteLine("4) View connection info");
                Console.WriteLine("5) Quit");

                bool ready = false;
                int c = 0;
                while (!ready)
                {
                    Console.Write("Enter a choice --> ");
                    String res = "";
                    res = new Scanner(System.in).nextLine();

                    try
                    {
                        c = Integer.parseInt(res);
                        if (c >= 1 && c <= 5)
                        {
                            ready = true;
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        Console.WriteLine("Bad input!");
                    }
                }
                switch (c)
                {
                    case 5:
                        keepRunning = false;
                        break;
                    case 4:
                        Console.WriteLine("Database connected to: " + conn.info());
                        break;
                    case 3:
                        Console.Write("Teamname: ");
                        String team = new Scanner(System.in).nextLine();
                        int s = -1;
                        do
                        {
                            Console.Write("Score: ");
                            try
                            {
                                s = new Scanner(System.in).nextInt();
                            }
                            catch (Exception e)
                            {
                                Console.WriteLine("Please enter a valid int!");
                            }
                        } while (s < 0);
                        int t = -1;
                        do
                        {
                            Console.Write("Time: ");
                            try
                            {
                                t = new Scanner(System.in).nextInt();
                            }
                            catch (Exception e)
                            {
                                Console.WriteLine("Please enter a valid int!");
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
            Console.WriteLine("Have a nice day!");
        }
    }
}

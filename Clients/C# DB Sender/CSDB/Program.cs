using System;
using System.Data.Odbc;
using DT = System.Data;
using QC = System.Data.SqlClient;

namespace CSDB
{
    class Program
    {
        static void Main(string[] args)
        {
            String username;
            String password;
            String team;
            int score;
            int time;
            bool quiet;

            try
            {
                username = args[0];
                password = args[1];
                team = args[2];
                score = int.Parse(args[3]);
                time = int.Parse(args[4]);
                quiet = Convert.ToBoolean(args[5]);
            }
            catch (Exception e)
            {
                username = "";
                password = "";
                team = "";
                score = 0;
                time = 0;
                quiet = false;
            }

            string connectionString = "Server = tcp:ctsb.database.windows.net,1433; Initial Catalog = CPscores; Persist Security Info = False; User ID = " + username + "; Password =" + password + "; MultipleActiveResultSets = False; Encrypt = True; TrustServerCertificate = False; Connection Timeout = 30;";
            string cmd = "INSERT INTO TeamScores (TeamName,Score,Time) VALUES (@val1, @val2, @val3)";
            using (QC.SqlConnection conn = new QC.SqlConnection(connectionString))
            {
                using (QC.SqlCommand command = new QC.SqlCommand(cmd))
                {
                    command.Connection = conn;

                    command.Parameters.AddWithValue("@val1", team);
                    command.Parameters.AddWithValue("@val2", score);
                    command.Parameters.AddWithValue("@val3", time);

                    try
                    {
                        if (!quiet)
                        {
                            Console.WriteLine("Opening connection...");
                        }
                        conn.Open();
                        if (!quiet)
                        {
                            Console.WriteLine("Transmitting data!");
                        }
                        command.ExecuteNonQuery();
                        if (!quiet)
                        {
                            Console.WriteLine("Done!");
                        }
                    }
                    catch (QC.SqlException e)
                    {
                        Console.WriteLine("An error has occured");
                        Console.WriteLine(e);
                    }
                }
            }
            if (!quiet)
            {
                Console.Write("Press any key to continue...");
                Console.ReadKey(true);
            }
        }
    }
}
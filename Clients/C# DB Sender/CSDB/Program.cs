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
            String username = "";
            String password = "";
            string connectionString = "Server = tcp:ctsb.database.windows.net,1433; Initial Catalog = CPscores; Persist Security Info = False; User ID = " + username + "; Password =" + password + "; MultipleActiveResultSets = False; Encrypt = True; TrustServerCertificate = False; Connection Timeout = 30;";
            string cmd = "INSERT INTO TeamScores (TeamName,Score,Time) VALUES (@val1, @val2, @val3)";
            using (QC.SqlConnection conn = new QC.SqlConnection(connectionString))
            {
                using (QC.SqlCommand command = new QC.SqlCommand(cmd))
                {
                    command.Connection = conn;
                    //command.CommandString = cmd;

                    command.Parameters.AddWithValue("@val1", "Matrix");
                    command.Parameters.AddWithValue("@val2", 9000);
                    command.Parameters.AddWithValue("@val3", 80);

                    try
                    {
                        Console.WriteLine("Opening connection...");
                        conn.Open();
                        Console.WriteLine("Transmitting data!");
                        command.ExecuteNonQuery();
                        Console.WriteLine("Done!");
                    }
                    catch(QC.SqlException e)
                    {
                        Console.WriteLine("An error has occured");
                        Console.WriteLine(e);
                    }
                }
            }
            Console.Write("Press any key to continue...");
            Console.ReadKey(true);
        }
    }
}
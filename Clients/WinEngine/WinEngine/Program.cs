using System;
using QC = System.Data.SqlClient;

namespace WinEngine
{
    class Program
    {
        static void Main(string[] args)
        {
            string team = "";
            try
            {
                team = System.IO.File.ReadAllText(@"C:\CyberPatriot Score Report\teamname.txt");
            } catch (Exception e)
            {
                Environment.Exit(1);
            }

            string filepath = "C:\\CyberPatriot Score Report\\Score Report.html";
            string timePath = "C:\\CyberPatriot Score Report\\time.txt";
            string username = "";
            string password = "";

            string text = System.IO.File.ReadAllText(filepath);
            int left = text.IndexOf("Current Score: ");
            string dirtyScore = text.Substring(left + 15);
            string cleanScore = dirtyScore.Substring(0, dirtyScore.IndexOf("/"));

            int score = -1;
            try
            {
                score = Int32.Parse(cleanScore);
            } catch (Exception e)
            {
                // Looks like there was an error! Dw, we will send -1 anyway
            }

            
            String sTime = "0";
            int time = 0;
            try
            {
                sTime = System.IO.File.ReadAllText(timePath);
                time = Int32.Parse(sTime);
                time++;
            } catch(Exception e)
            {
                //nothing to worry about, its already init'd'
            }

            System.IO.File.WriteAllText(timePath, "" + time);


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
                        conn.Open();
                        command.ExecuteNonQuery();
                    }
                    catch (QC.SqlException e)
                    {
                    }
                }
            }
        }
    }
}

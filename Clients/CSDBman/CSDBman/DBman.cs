using System;
using System.Data.Odbc;
using DT = System.Data;
using QC = System.Data.SqlClient;

namespace CSDBman
{
    class DBman
    {
        private String myUsername;
        private String myPassword;
        private String myUrl;
        private String myDB;
        private String myServer;

        public DBman(String username, String password, String server, String db)
        {
            myUsername = username;
            myPassword = password;
            myDB = db;
            myServer = server;
            myUrl = "Server = tcp:" + server + ",1433; Initial Catalog = + " + db + "; Persist Security Info = False; User ID = " + username + "; Password =" + password + "; MultipleActiveResultSets = False; Encrypt = True; TrustServerCertificate = False; Connection Timeout = 30;";
        }

        public void printTable()
        {
            string cmd = "SELECT * FROM TeamScores";
            Console.Write("Attempting connection...");
            using (QC.SqlConnection conn = new QC.SqlConnection(myUrl))
            {
                using (QC.SqlCommand command = new QC.SqlCommand(cmd))
                {
                    try
                    {
                        conn.Open();
                        Console.WriteLine(" Conected!\n");
                        DT.DataTable Table = new DT.DataTable(myDB);
                        foreach (DT.DataRow dataRow in Table.Rows)
                        {
                            foreach (var item in dataRow.ItemArray)
                            {
                                Console.WriteLine(item);
                            }
                        }
                    }
                    catch (QC.SqlException e)
                    {
                        Console.WriteLine("An error has occured");
                        Console.WriteLine(e);
                    }

                }
            }
        }

        public void clearTable()
        {
            string cmd = "Truncate table TeamScores";
            Console.WriteLine("Attempting connection...");

            using (QC.SqlConnection conn = new QC.SqlConnection(myUrl))
            {
                using (QC.SqlCommand command = new QC.SqlCommand(cmd))
                {
                    command.Connection = conn;
                    try
                    {
                        Console.WriteLine("Opening connection...");
                        conn.Open();
                        Console.Write("Clearing data... ");
                        command.ExecuteNonQuery();
                        Console.WriteLine(" Done!");
                    }
                    catch (Exception e) { }
                }
            }
        }

    public void setUsername(String username)
        {
            myUsername = username;
            myUrl = "Server = tcp:" + myServer + ",1433; Initial Catalog = + " + myDB + "; Persist Security Info = False; User ID = " + myUsername + "; Password =" + myPassword + "; MultipleActiveResultSets = False; Encrypt = True; TrustServerCertificate = False; Connection Timeout = 30;";
        }

        public void setPassword(String password)
        {
            myPassword = password;
            myUrl = "Server = tcp:" + myServer + ",1433; Initial Catalog = + " + myDB + "; Persist Security Info = False; User ID = " + myUsername + "; Password =" + myPassword + "; MultipleActiveResultSets = False; Encrypt = True; TrustServerCertificate = False; Connection Timeout = 30;";
        }

        public String info()
        {
            return myUrl.Substring(0, myUrl.IndexOf("database="));
        }
    }
}
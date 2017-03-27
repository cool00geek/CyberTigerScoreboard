using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace TeamEntry
{
    public partial class Win : Form
    {
        public Win()
        {
            string TN = @"C:\CyberPatriot Score Report\team.txt";
            if (File.Exists(TN))
            {
                // Nothing to do
            }
            else
            {


                InitializeComponent();

                Label.AutoSize = false;
                Label.TextAlign = ContentAlignment.MiddleCenter;

                ToolTip yourToolTip = new ToolTip();
                yourToolTip.IsBalloon = true;
                yourToolTip.ShowAlways = true;
                yourToolTip.SetToolTip(Label, "Make sure there are no spaces!");

                TeamEntry.KeyDown += (sender, args) =>
                {
                    if (args.KeyCode == Keys.Return)
                    {
                        okButton.PerformClick();
                    }
                };
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void Win_Load(object sender, EventArgs e)
        {

        }

        private async void okButton_Click(object sender, EventArgs e)
        {
            string name = TeamEntry.Text;
            if (name.Equals(""))
            {
                Label.Text = "ENTER A NAME!";
            }
                else if (name.Contains(" "))
            {
                Label.Text = "No spaces!";
            }
            else
            {
                Label.Text = "Name accepted! Saving...";
                Label.Cursor = Cursors.WaitCursor;
                okButton.Cursor = Cursors.WaitCursor;
                TeamEntry.Cursor = Cursors.WaitCursor;
                this.Cursor = Cursors.WaitCursor;
                System.IO.File.WriteAllText(@"C:\CyberPatriot Score Report\team.txt", name);
                await Task.Delay(4000);
                Application.Exit();
            }
        }

        private void Label_Click(object sender, EventArgs e)
        {

        }
    }
}

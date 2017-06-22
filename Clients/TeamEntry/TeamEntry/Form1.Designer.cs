namespace TeamEntry
{
    partial class Win
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.TeamEntry = new System.Windows.Forms.TextBox();
            this.Label = new System.Windows.Forms.Label();
            this.okButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // TeamEntry
            // 
            this.TeamEntry.Font = new System.Drawing.Font("Microsoft Sans Serif", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.TeamEntry.Location = new System.Drawing.Point(8, 62);
            this.TeamEntry.Name = "TeamEntry";
            this.TeamEntry.Size = new System.Drawing.Size(363, 31);
            this.TeamEntry.TabIndex = 0;
            this.TeamEntry.TextChanged += new System.EventHandler(this.textBox1_TextChanged);
            // 
            // Label
            // 
            this.Label.AutoSize = true;
            this.Label.Font = new System.Drawing.Font("Microsoft Sans Serif", 20.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.Label.Location = new System.Drawing.Point(2, 9);
            this.Label.Name = "Label";
            this.Label.Size = new System.Drawing.Size(382, 31);
            this.Label.TabIndex = 3;
            this.Label.Text = "Please enter your team name: ";
            this.Label.TextAlign = System.Drawing.ContentAlignment.TopCenter;
            this.Label.Click += new System.EventHandler(this.Label_Click);
            // 
            // okButton
            // 
            this.okButton.Cursor = System.Windows.Forms.Cursors.Hand;
            this.okButton.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.okButton.Font = new System.Drawing.Font("Microsoft Sans Serif", 15.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.okButton.Location = new System.Drawing.Point(0, 126);
            this.okButton.Name = "okButton";
            this.okButton.Size = new System.Drawing.Size(383, 40);
            this.okButton.TabIndex = 4;
            this.okButton.Text = "Accept";
            this.okButton.UseVisualStyleBackColor = true;
            this.okButton.Click += new System.EventHandler(this.okButton_Click);
            // 
            // Win
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(383, 166);
            this.ControlBox = false;
            this.Controls.Add(this.okButton);
            this.Controls.Add(this.Label);
            this.Controls.Add(this.TeamEntry);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "Win";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.Text = "Team name";
            this.TopMost = true;
            this.Load += new System.EventHandler(this.Win_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox TeamEntry;
        private System.Windows.Forms.Label Label;
        private System.Windows.Forms.Button okButton;
    }
}


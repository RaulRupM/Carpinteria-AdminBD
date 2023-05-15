
namespace Carpinteria_SQLServer
{
    partial class Tipo_Proyecto
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
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.btnElimina = new System.Windows.Forms.Button();
            this.tablaTipo = new System.Windows.Forms.DataGridView();
            this.button1 = new System.Windows.Forms.Button();
            this.btnModificar = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.tablaTipo)).BeginInit();
            this.SuspendLayout();
            // 
            // textBox2
            // 
            this.textBox2.Location = new System.Drawing.Point(79, 47);
            this.textBox2.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.textBox2.Name = "textBox2";
            this.textBox2.Size = new System.Drawing.Size(205, 22);
            this.textBox2.TabIndex = 31;
            this.textBox2.Tag = "tipo";
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(153, 10);
            this.textBox1.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(205, 22);
            this.textBox1.TabIndex = 30;
            this.textBox1.Tag = "nombre";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(8, 53);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(49, 16);
            this.label2.TabIndex = 28;
            this.label2.Text = "Precio:";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(8, 14);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(137, 16);
            this.label1.TabIndex = 27;
            this.label1.Text = "Nombre del proyecto:";
            this.label1.Click += new System.EventHandler(this.label1_Click);
            // 
            // btnElimina
            // 
            this.btnElimina.Location = new System.Drawing.Point(459, 113);
            this.btnElimina.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.btnElimina.Name = "btnElimina";
            this.btnElimina.Size = new System.Drawing.Size(99, 41);
            this.btnElimina.TabIndex = 37;
            this.btnElimina.Text = "Eliminar";
            this.btnElimina.UseVisualStyleBackColor = true;
            this.btnElimina.Click += new System.EventHandler(this.btnElimina_Click);
            // 
            // tablaTipo
            // 
            this.tablaTipo.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.tablaTipo.Location = new System.Drawing.Point(12, 159);
            this.tablaTipo.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.tablaTipo.Name = "tablaTipo";
            this.tablaTipo.RowHeadersWidth = 51;
            this.tablaTipo.RowTemplate.Height = 24;
            this.tablaTipo.Size = new System.Drawing.Size(545, 183);
            this.tablaTipo.TabIndex = 34;
            this.tablaTipo.CellClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.tablaTipo_CellContentClick);
            this.tablaTipo.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.tablaTipo_CellContentClick);
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(249, 113);
            this.button1.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(99, 41);
            this.button1.TabIndex = 38;
            this.button1.Text = "Insertar";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // btnModificar
            // 
            this.btnModificar.Location = new System.Drawing.Point(353, 113);
            this.btnModificar.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.btnModificar.Name = "btnModificar";
            this.btnModificar.Size = new System.Drawing.Size(99, 41);
            this.btnModificar.TabIndex = 39;
            this.btnModificar.Text = "Modificar";
            this.btnModificar.UseVisualStyleBackColor = true;
            this.btnModificar.Click += new System.EventHandler(this.btnModificar_Click);
            // 
            // Tipo_Proyecto
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(575, 356);
            this.Controls.Add(this.btnModificar);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.btnElimina);
            this.Controls.Add(this.tablaTipo);
            this.Controls.Add(this.textBox2);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Margin = new System.Windows.Forms.Padding(4, 4, 4, 4);
            this.Name = "Tipo_Proyecto";
            this.Text = "Tipo Proyecto";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Tipo_Proyecto_FormClosed);
            this.Load += new System.EventHandler(this.Tipo_Proyecto_Load);
            ((System.ComponentModel.ISupportInitialize)(this.tablaTipo)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button btnElimina;
        private System.Windows.Forms.DataGridView tablaTipo;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button btnModificar;
    }
}
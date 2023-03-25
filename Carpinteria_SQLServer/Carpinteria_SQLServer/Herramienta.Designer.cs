﻿namespace Carpinteria_SQLServer
{
    partial class Herramienta
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
			this.btnEliminarHerramienta = new System.Windows.Forms.Button();
			this.btnModificarHerramienta = new System.Windows.Forms.Button();
			this.btnInsertarHerramienta = new System.Windows.Forms.Button();
			this.tablaHerramientas = new System.Windows.Forms.DataGridView();
			this.textBox4 = new System.Windows.Forms.TextBox();
			this.textBox3 = new System.Windows.Forms.TextBox();
			this.textBox2 = new System.Windows.Forms.TextBox();
			this.textBox1 = new System.Windows.Forms.TextBox();
			this.label4 = new System.Windows.Forms.Label();
			this.label3 = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.label1 = new System.Windows.Forms.Label();
			((System.ComponentModel.ISupportInitialize)(this.tablaHerramientas)).BeginInit();
			this.SuspendLayout();
			// 
			// btnEliminarHerramienta
			// 
			this.btnEliminarHerramienta.Location = new System.Drawing.Point(492, 103);
			this.btnEliminarHerramienta.Margin = new System.Windows.Forms.Padding(2);
			this.btnEliminarHerramienta.Name = "btnEliminarHerramienta";
			this.btnEliminarHerramienta.Size = new System.Drawing.Size(74, 33);
			this.btnEliminarHerramienta.TabIndex = 33;
			this.btnEliminarHerramienta.Text = "Eliminar";
			this.btnEliminarHerramienta.UseVisualStyleBackColor = true;
			this.btnEliminarHerramienta.Click += new System.EventHandler(this.btnEliminarHerramienta_Click);
			// 
			// btnModificarHerramienta
			// 
			this.btnModificarHerramienta.Location = new System.Drawing.Point(413, 103);
			this.btnModificarHerramienta.Margin = new System.Windows.Forms.Padding(2);
			this.btnModificarHerramienta.Name = "btnModificarHerramienta";
			this.btnModificarHerramienta.Size = new System.Drawing.Size(74, 33);
			this.btnModificarHerramienta.TabIndex = 32;
			this.btnModificarHerramienta.Text = "Modificar";
			this.btnModificarHerramienta.UseVisualStyleBackColor = true;
			this.btnModificarHerramienta.Click += new System.EventHandler(this.btnModificarHerramienta_Click);
			// 
			// btnInsertarHerramienta
			// 
			this.btnInsertarHerramienta.Location = new System.Drawing.Point(334, 103);
			this.btnInsertarHerramienta.Margin = new System.Windows.Forms.Padding(2);
			this.btnInsertarHerramienta.Name = "btnInsertarHerramienta";
			this.btnInsertarHerramienta.Size = new System.Drawing.Size(74, 33);
			this.btnInsertarHerramienta.TabIndex = 31;
			this.btnInsertarHerramienta.Text = "Insertar";
			this.btnInsertarHerramienta.UseVisualStyleBackColor = true;
			this.btnInsertarHerramienta.Click += new System.EventHandler(this.btnInsertarHerramienta_Click);
			// 
			// tablaHerramientas
			// 
			this.tablaHerramientas.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.tablaHerramientas.Location = new System.Drawing.Point(10, 141);
			this.tablaHerramientas.Margin = new System.Windows.Forms.Padding(2);
			this.tablaHerramientas.Name = "tablaHerramientas";
			this.tablaHerramientas.RowHeadersWidth = 51;
			this.tablaHerramientas.RowTemplate.Height = 24;
			this.tablaHerramientas.Size = new System.Drawing.Size(556, 149);
			this.tablaHerramientas.TabIndex = 30;
			// 
			// textBox4
			// 
			this.textBox4.Location = new System.Drawing.Point(109, 107);
			this.textBox4.Margin = new System.Windows.Forms.Padding(2);
			this.textBox4.Name = "textBox4";
			this.textBox4.Size = new System.Drawing.Size(108, 20);
			this.textBox4.TabIndex = 27;
			this.textBox4.Tag = "cantidad_disponible";
			// 
			// textBox3
			// 
			this.textBox3.Location = new System.Drawing.Point(62, 78);
			this.textBox3.Margin = new System.Windows.Forms.Padding(2);
			this.textBox3.Name = "textBox3";
			this.textBox3.Size = new System.Drawing.Size(155, 20);
			this.textBox3.TabIndex = 26;
			this.textBox3.Tag = "estado";
			// 
			// textBox2
			// 
			this.textBox2.Location = new System.Drawing.Point(62, 47);
			this.textBox2.Margin = new System.Windows.Forms.Padding(2);
			this.textBox2.Name = "textBox2";
			this.textBox2.Size = new System.Drawing.Size(155, 20);
			this.textBox2.TabIndex = 25;
			this.textBox2.Tag = "tipo";
			// 
			// textBox1
			// 
			this.textBox1.Location = new System.Drawing.Point(62, 20);
			this.textBox1.Margin = new System.Windows.Forms.Padding(2);
			this.textBox1.Name = "textBox1";
			this.textBox1.Size = new System.Drawing.Size(155, 20);
			this.textBox1.TabIndex = 24;
			this.textBox1.Tag = "nombre";
			// 
			// label4
			// 
			this.label4.AutoSize = true;
			this.label4.Location = new System.Drawing.Point(9, 107);
			this.label4.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(99, 13);
			this.label4.TabIndex = 21;
			this.label4.Text = "Cantidad disponible";
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(9, 80);
			this.label3.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(40, 13);
			this.label3.TabIndex = 20;
			this.label3.Text = "Estado";
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(9, 52);
			this.label2.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(28, 13);
			this.label2.TabIndex = 19;
			this.label2.Text = "Tipo";
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(9, 20);
			this.label1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(44, 13);
			this.label1.TabIndex = 18;
			this.label1.Text = "Nombre";
			// 
			// Herramienta
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(576, 306);
			this.Controls.Add(this.btnEliminarHerramienta);
			this.Controls.Add(this.btnModificarHerramienta);
			this.Controls.Add(this.btnInsertarHerramienta);
			this.Controls.Add(this.tablaHerramientas);
			this.Controls.Add(this.textBox4);
			this.Controls.Add(this.textBox3);
			this.Controls.Add(this.textBox2);
			this.Controls.Add(this.textBox1);
			this.Controls.Add(this.label4);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.label1);
			this.Margin = new System.Windows.Forms.Padding(2);
			this.Name = "Herramienta";
			this.Text = "Herramienta";
			((System.ComponentModel.ISupportInitialize)(this.tablaHerramientas)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnEliminarHerramienta;
        private System.Windows.Forms.Button btnModificarHerramienta;
        private System.Windows.Forms.Button btnInsertarHerramienta;
        private System.Windows.Forms.DataGridView tablaHerramientas;
        private System.Windows.Forms.TextBox textBox4;
        private System.Windows.Forms.TextBox textBox3;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
    }
}
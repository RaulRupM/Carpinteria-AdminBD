﻿namespace Carpinteria_SQLServer
{
	partial class InsumosProyecto
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
			this.button3 = new System.Windows.Forms.Button();
			this.button2 = new System.Windows.Forms.Button();
			this.button1 = new System.Windows.Forms.Button();
			this.tablaInsumosProyecto = new System.Windows.Forms.DataGridView();
			this.textBox4 = new System.Windows.Forms.TextBox();
			this.textBox3 = new System.Windows.Forms.TextBox();
			this.textBox2 = new System.Windows.Forms.TextBox();
			this.textBox1 = new System.Windows.Forms.TextBox();
			this.label4 = new System.Windows.Forms.Label();
			this.label3 = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.label1 = new System.Windows.Forms.Label();
			this.tablaInsumos = new System.Windows.Forms.DataGridView();
			this.comboProyectos = new System.Windows.Forms.ComboBox();
			this.comboInsumos = new System.Windows.Forms.ComboBox();
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumosProyecto)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumos)).BeginInit();
			this.SuspendLayout();
			// 
			// button3
			// 
			this.button3.Location = new System.Drawing.Point(484, 92);
			this.button3.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.button3.Name = "button3";
			this.button3.Size = new System.Drawing.Size(74, 33);
			this.button3.TabIndex = 45;
			this.button3.Text = "Eliminar";
			this.button3.UseVisualStyleBackColor = true;
			this.button3.Click += new System.EventHandler(this.button3_Click);
			// 
			// button2
			// 
			this.button2.Location = new System.Drawing.Point(405, 92);
			this.button2.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.button2.Name = "button2";
			this.button2.Size = new System.Drawing.Size(74, 33);
			this.button2.TabIndex = 44;
			this.button2.Text = "Modificar";
			this.button2.UseVisualStyleBackColor = true;
			this.button2.Click += new System.EventHandler(this.button2_Click);
			// 
			// button1
			// 
			this.button1.Location = new System.Drawing.Point(327, 92);
			this.button1.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.button1.Name = "button1";
			this.button1.Size = new System.Drawing.Size(74, 33);
			this.button1.TabIndex = 43;
			this.button1.Text = "Insertar";
			this.button1.UseVisualStyleBackColor = true;
			this.button1.Click += new System.EventHandler(this.button1_Click);
			// 
			// tablaInsumosProyecto
			// 
			this.tablaInsumosProyecto.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.tablaInsumosProyecto.Location = new System.Drawing.Point(11, 130);
			this.tablaInsumosProyecto.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.tablaInsumosProyecto.Name = "tablaInsumosProyecto";
			this.tablaInsumosProyecto.RowHeadersWidth = 51;
			this.tablaInsumosProyecto.RowTemplate.Height = 24;
			this.tablaInsumosProyecto.Size = new System.Drawing.Size(547, 149);
			this.tablaInsumosProyecto.TabIndex = 42;
			this.tablaInsumosProyecto.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.tablaInsumosProyecto_CellContentClick);
			this.tablaInsumosProyecto.CellFormatting += new System.Windows.Forms.DataGridViewCellFormattingEventHandler(this.tablaInsumosProyecto_CellFormatting);
			// 
			// textBox4
			// 
			this.textBox4.Enabled = false;
			this.textBox4.Location = new System.Drawing.Point(77, 99);
			this.textBox4.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.textBox4.Name = "textBox4";
			this.textBox4.Size = new System.Drawing.Size(167, 20);
			this.textBox4.TabIndex = 41;
			this.textBox4.Tag = "subtotal";
			this.textBox4.Visible = false;
			// 
			// textBox3
			// 
			this.textBox3.Location = new System.Drawing.Point(77, 70);
			this.textBox3.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.textBox3.Name = "textBox3";
			this.textBox3.Size = new System.Drawing.Size(167, 20);
			this.textBox3.TabIndex = 40;
			this.textBox3.Tag = "cantidad";
			// 
			// textBox2
			// 
			this.textBox2.Location = new System.Drawing.Point(324, 42);
			this.textBox2.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.textBox2.Name = "textBox2";
			this.textBox2.Size = new System.Drawing.Size(155, 20);
			this.textBox2.TabIndex = 39;
			this.textBox2.Tag = "proyecto";
			this.textBox2.Visible = false;
			// 
			// textBox1
			// 
			this.textBox1.Location = new System.Drawing.Point(324, 14);
			this.textBox1.Margin = new System.Windows.Forms.Padding(2, 2, 2, 2);
			this.textBox1.Name = "textBox1";
			this.textBox1.Size = new System.Drawing.Size(155, 20);
			this.textBox1.TabIndex = 38;
			this.textBox1.Tag = "insumo";
			this.textBox1.Visible = false;
			// 
			// label4
			// 
			this.label4.AutoSize = true;
			this.label4.Location = new System.Drawing.Point(11, 99);
			this.label4.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label4.Name = "label4";
			this.label4.Size = new System.Drawing.Size(46, 13);
			this.label4.TabIndex = 37;
			this.label4.Text = "Subtotal";
			this.label4.Visible = false;
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(9, 73);
			this.label3.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(49, 13);
			this.label3.TabIndex = 36;
			this.label3.Text = "Cantidad";
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(9, 44);
			this.label2.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(64, 13);
			this.label2.TabIndex = 35;
			this.label2.Text = "Id Proyecto:";
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(9, 13);
			this.label1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(56, 13);
			this.label1.TabIndex = 34;
			this.label1.Text = "Id Insumo:";
			// 
			// tablaInsumos
			// 
			this.tablaInsumos.AllowUserToAddRows = false;
			this.tablaInsumos.AllowUserToDeleteRows = false;
			this.tablaInsumos.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.tablaInsumos.Location = new System.Drawing.Point(11, 388);
			this.tablaInsumos.Name = "tablaInsumos";
			this.tablaInsumos.ReadOnly = true;
			this.tablaInsumos.RowHeadersWidth = 51;
			this.tablaInsumos.Size = new System.Drawing.Size(547, 150);
			this.tablaInsumos.TabIndex = 46;
			// 
			// comboProyectos
			// 
			this.comboProyectos.FormattingEnabled = true;
			this.comboProyectos.Location = new System.Drawing.Point(77, 43);
			this.comboProyectos.Name = "comboProyectos";
			this.comboProyectos.Size = new System.Drawing.Size(167, 21);
			this.comboProyectos.TabIndex = 47;
			// 
			// comboInsumos
			// 
			this.comboInsumos.FormattingEnabled = true;
			this.comboInsumos.Location = new System.Drawing.Point(77, 13);
			this.comboInsumos.Name = "comboInsumos";
			this.comboInsumos.Size = new System.Drawing.Size(167, 21);
			this.comboInsumos.TabIndex = 48;
			this.comboInsumos.SelectedIndexChanged += new System.EventHandler(this.comboInsumos_SelectedIndexChanged);
			// 
			// InsumosProyecto
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(574, 627);
			this.Controls.Add(this.comboInsumos);
			this.Controls.Add(this.comboProyectos);
			this.Controls.Add(this.tablaInsumos);
			this.Controls.Add(this.button3);
			this.Controls.Add(this.button2);
			this.Controls.Add(this.button1);
			this.Controls.Add(this.tablaInsumosProyecto);
			this.Controls.Add(this.textBox4);
			this.Controls.Add(this.textBox3);
			this.Controls.Add(this.textBox2);
			this.Controls.Add(this.textBox1);
			this.Controls.Add(this.label4);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.label1);
			this.Name = "InsumosProyecto";
			this.Text = "InsumosProyecto";
			this.Load += new System.EventHandler(this.InsumosProyecto_Load);
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumosProyecto)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumos)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.Button button3;
		private System.Windows.Forms.Button button2;
		private System.Windows.Forms.Button button1;
		private System.Windows.Forms.DataGridView tablaInsumosProyecto;
		private System.Windows.Forms.TextBox textBox4;
		private System.Windows.Forms.TextBox textBox3;
		private System.Windows.Forms.TextBox textBox2;
		private System.Windows.Forms.TextBox textBox1;
		private System.Windows.Forms.Label label4;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.DataGridView tablaInsumos;
		private System.Windows.Forms.ComboBox comboProyectos;
		private System.Windows.Forms.ComboBox comboInsumos;
	}
}
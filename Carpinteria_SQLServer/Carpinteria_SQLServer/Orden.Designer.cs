namespace Carpinteria_SQLServer
{
	partial class Orden
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
			this.comboProveedores = new System.Windows.Forms.ComboBox();
			this.button1 = new System.Windows.Forms.Button();
			this.button2 = new System.Windows.Forms.Button();
			this.label1 = new System.Windows.Forms.Label();
			this.tablaOrden = new System.Windows.Forms.DataGridView();
			this.btnEliminar = new System.Windows.Forms.Button();
			this.btnModificar = new System.Windows.Forms.Button();
			this.btnAgregar = new System.Windows.Forms.Button();
			this.dateTimePicker1 = new System.Windows.Forms.DateTimePicker();
			((System.ComponentModel.ISupportInitialize)(this.tablaOrden)).BeginInit();
			this.SuspendLayout();
			// 
			// comboProveedores
			// 
			this.comboProveedores.FormattingEnabled = true;
			this.comboProveedores.Location = new System.Drawing.Point(13, 38);
			this.comboProveedores.Name = "comboProveedores";
			this.comboProveedores.Size = new System.Drawing.Size(231, 21);
			this.comboProveedores.TabIndex = 0;
			this.comboProveedores.SelectedIndexChanged += new System.EventHandler(this.comboProveedores_SelectedIndexChanged);
			// 
			// button1
			// 
			this.button1.Enabled = false;
			this.button1.Location = new System.Drawing.Point(411, 83);
			this.button1.Name = "button1";
			this.button1.Size = new System.Drawing.Size(103, 45);
			this.button1.TabIndex = 1;
			this.button1.Text = "Ordenar herramienta";
			this.button1.UseVisualStyleBackColor = true;
			this.button1.Click += new System.EventHandler(this.button1_Click);
			// 
			// button2
			// 
			this.button2.Enabled = false;
			this.button2.Location = new System.Drawing.Point(539, 83);
			this.button2.Name = "button2";
			this.button2.Size = new System.Drawing.Size(103, 45);
			this.button2.TabIndex = 2;
			this.button2.Text = "Ordenar Insumo";
			this.button2.UseVisualStyleBackColor = true;
			this.button2.Click += new System.EventHandler(this.button2_Click);
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label1.Location = new System.Drawing.Point(89, 15);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(81, 20);
			this.label1.TabIndex = 3;
			this.label1.Text = "Proveedor";
			// 
			// tablaOrden
			// 
			this.tablaOrden.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.tablaOrden.Location = new System.Drawing.Point(13, 204);
			this.tablaOrden.Name = "tablaOrden";
			this.tablaOrden.Size = new System.Drawing.Size(625, 150);
			this.tablaOrden.TabIndex = 4;
			this.tablaOrden.SelectionChanged += new System.EventHandler(this.tablaOrden_SelectionChanged);
			// 
			// btnEliminar
			// 
			this.btnEliminar.Enabled = false;
			this.btnEliminar.Location = new System.Drawing.Point(520, 166);
			this.btnEliminar.Name = "btnEliminar";
			this.btnEliminar.Size = new System.Drawing.Size(118, 32);
			this.btnEliminar.TabIndex = 6;
			this.btnEliminar.Text = "Eliminar orden";
			this.btnEliminar.UseVisualStyleBackColor = true;
			this.btnEliminar.Click += new System.EventHandler(this.btnEliminar_Click);
			// 
			// btnModificar
			// 
			this.btnModificar.Enabled = false;
			this.btnModificar.Location = new System.Drawing.Point(396, 166);
			this.btnModificar.Name = "btnModificar";
			this.btnModificar.Size = new System.Drawing.Size(118, 32);
			this.btnModificar.TabIndex = 7;
			this.btnModificar.Text = "Modificar orden";
			this.btnModificar.UseVisualStyleBackColor = true;
			this.btnModificar.Click += new System.EventHandler(this.btnModificar_Click);
			// 
			// btnAgregar
			// 
			this.btnAgregar.Location = new System.Drawing.Point(251, 38);
			this.btnAgregar.Name = "btnAgregar";
			this.btnAgregar.Size = new System.Drawing.Size(92, 21);
			this.btnAgregar.TabIndex = 9;
			this.btnAgregar.Text = "Nueva orden";
			this.btnAgregar.UseVisualStyleBackColor = true;
			this.btnAgregar.Click += new System.EventHandler(this.btnAgregar_Click);
			// 
			// dateTimePicker1
			// 
			this.dateTimePicker1.Enabled = false;
			this.dateTimePicker1.Location = new System.Drawing.Point(13, 74);
			this.dateTimePicker1.Name = "dateTimePicker1";
			this.dateTimePicker1.Size = new System.Drawing.Size(231, 20);
			this.dateTimePicker1.TabIndex = 10;
			this.dateTimePicker1.Value = new System.DateTime(2023, 4, 26, 15, 42, 38, 0);
			// 
			// Orden
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(650, 431);
			this.Controls.Add(this.dateTimePicker1);
			this.Controls.Add(this.btnAgregar);
			this.Controls.Add(this.btnModificar);
			this.Controls.Add(this.btnEliminar);
			this.Controls.Add(this.tablaOrden);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.button2);
			this.Controls.Add(this.button1);
			this.Controls.Add(this.comboProveedores);
			this.Name = "Orden";
			this.Text = "Orden";
			this.Load += new System.EventHandler(this.Orden_Load);
			((System.ComponentModel.ISupportInitialize)(this.tablaOrden)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.ComboBox comboProveedores;
		private System.Windows.Forms.Button button1;
		private System.Windows.Forms.Button button2;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.DataGridView tablaOrden;
		private System.Windows.Forms.Button btnEliminar;
		private System.Windows.Forms.Button btnModificar;
		private System.Windows.Forms.Button btnAgregar;
		private System.Windows.Forms.DateTimePicker dateTimePicker1;
	}
}
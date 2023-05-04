namespace Carpinteria_SQLServer
{
	partial class NuevaOrden
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
			this.comboProducto = new System.Windows.Forms.ComboBox();
			this.btnAgregar = new System.Windows.Forms.Button();
			this.dataGridView1 = new System.Windows.Forms.DataGridView();
			this.lblCantidad = new System.Windows.Forms.Label();
			this.textBoxCantidad = new System.Windows.Forms.TextBox();
			this.lblProducto = new System.Windows.Forms.Label();
			this.lblPrecio = new System.Windows.Forms.Label();
			this.lblPrecioProd = new System.Windows.Forms.Label();
			this.btnEliminar = new System.Windows.Forms.Button();
			this.btnModificar = new System.Windows.Forms.Button();
			((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
			this.SuspendLayout();
			// 
			// comboProducto
			// 
			this.comboProducto.FormattingEnabled = true;
			this.comboProducto.Location = new System.Drawing.Point(14, 29);
			this.comboProducto.Name = "comboProducto";
			this.comboProducto.Size = new System.Drawing.Size(191, 21);
			this.comboProducto.TabIndex = 0;
			this.comboProducto.SelectedIndexChanged += new System.EventHandler(this.comboProducto_SelectedIndexChanged);
			// 
			// btnAgregar
			// 
			this.btnAgregar.Location = new System.Drawing.Point(14, 129);
			this.btnAgregar.Name = "btnAgregar";
			this.btnAgregar.Size = new System.Drawing.Size(122, 23);
			this.btnAgregar.TabIndex = 1;
			this.btnAgregar.Text = "Agregar a la orden";
			this.btnAgregar.UseVisualStyleBackColor = true;
			// 
			// dataGridView1
			// 
			this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.dataGridView1.Location = new System.Drawing.Point(12, 200);
			this.dataGridView1.Name = "dataGridView1";
			this.dataGridView1.Size = new System.Drawing.Size(551, 165);
			this.dataGridView1.TabIndex = 2;
			this.dataGridView1.CellFormatting += new System.Windows.Forms.DataGridViewCellFormattingEventHandler(this.dataGridView1_CellFormatting);
			this.dataGridView1.SelectionChanged += new System.EventHandler(this.dataGridView1_SelectionChanged);
			// 
			// lblCantidad
			// 
			this.lblCantidad.AutoSize = true;
			this.lblCantidad.Location = new System.Drawing.Point(11, 86);
			this.lblCantidad.Name = "lblCantidad";
			this.lblCantidad.Size = new System.Drawing.Size(49, 13);
			this.lblCantidad.TabIndex = 3;
			this.lblCantidad.Text = "Cantidad";
			// 
			// textBoxCantidad
			// 
			this.textBoxCantidad.Location = new System.Drawing.Point(14, 103);
			this.textBoxCantidad.Name = "textBoxCantidad";
			this.textBoxCantidad.Size = new System.Drawing.Size(94, 20);
			this.textBoxCantidad.TabIndex = 4;
			// 
			// lblProducto
			// 
			this.lblProducto.AutoSize = true;
			this.lblProducto.Location = new System.Drawing.Point(16, 10);
			this.lblProducto.Name = "lblProducto";
			this.lblProducto.Size = new System.Drawing.Size(35, 13);
			this.lblProducto.TabIndex = 5;
			this.lblProducto.Text = "label1";
			// 
			// lblPrecio
			// 
			this.lblPrecio.AutoSize = true;
			this.lblPrecio.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.lblPrecio.Location = new System.Drawing.Point(14, 57);
			this.lblPrecio.Name = "lblPrecio";
			this.lblPrecio.Size = new System.Drawing.Size(57, 20);
			this.lblPrecio.TabIndex = 6;
			this.lblPrecio.Text = "Precio:";
			// 
			// lblPrecioProd
			// 
			this.lblPrecioProd.AutoSize = true;
			this.lblPrecioProd.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.lblPrecioProd.Location = new System.Drawing.Point(77, 57);
			this.lblPrecioProd.Name = "lblPrecioProd";
			this.lblPrecioProd.Size = new System.Drawing.Size(51, 20);
			this.lblPrecioProd.TabIndex = 7;
			this.lblPrecioProd.Text = "label1";
			// 
			// btnEliminar
			// 
			this.btnEliminar.Enabled = false;
			this.btnEliminar.Location = new System.Drawing.Point(487, 171);
			this.btnEliminar.Name = "btnEliminar";
			this.btnEliminar.Size = new System.Drawing.Size(75, 23);
			this.btnEliminar.TabIndex = 8;
			this.btnEliminar.Text = "Eliminar";
			this.btnEliminar.UseVisualStyleBackColor = true;
			// 
			// btnModificar
			// 
			this.btnModificar.Enabled = false;
			this.btnModificar.Location = new System.Drawing.Point(406, 171);
			this.btnModificar.Name = "btnModificar";
			this.btnModificar.Size = new System.Drawing.Size(75, 23);
			this.btnModificar.TabIndex = 9;
			this.btnModificar.Text = "Modificar";
			this.btnModificar.UseVisualStyleBackColor = true;
			this.btnModificar.Click += new System.EventHandler(this.btnModificar_Click);
			// 
			// NuevaOrden
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(587, 475);
			this.Controls.Add(this.btnModificar);
			this.Controls.Add(this.btnEliminar);
			this.Controls.Add(this.lblPrecioProd);
			this.Controls.Add(this.lblPrecio);
			this.Controls.Add(this.lblProducto);
			this.Controls.Add(this.textBoxCantidad);
			this.Controls.Add(this.lblCantidad);
			this.Controls.Add(this.dataGridView1);
			this.Controls.Add(this.btnAgregar);
			this.Controls.Add(this.comboProducto);
			this.Name = "NuevaOrden";
			this.Text = "Pedido";
			((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.ComboBox comboProducto;
		private System.Windows.Forms.Button btnAgregar;
		private System.Windows.Forms.DataGridView dataGridView1;
		private System.Windows.Forms.Label lblCantidad;
		private System.Windows.Forms.TextBox textBoxCantidad;
		private System.Windows.Forms.Label lblProducto;
		private System.Windows.Forms.Label lblPrecio;
		private System.Windows.Forms.Label lblPrecioProd;
		private System.Windows.Forms.Button btnEliminar;
		private System.Windows.Forms.Button btnModificar;
	}
}
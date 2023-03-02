namespace Carpinteria_SQLServer
{
    partial class Insumo
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
			this.btnEliminarInsumo = new System.Windows.Forms.Button();
			this.btnModificaInsumo = new System.Windows.Forms.Button();
			this.btnInsertaInsumo = new System.Windows.Forms.Button();
			this.tablaInsumos = new System.Windows.Forms.DataGridView();
			this.textBox3 = new System.Windows.Forms.TextBox();
			this.textBox2 = new System.Windows.Forms.TextBox();
			this.textBox1 = new System.Windows.Forms.TextBox();
			this.label3 = new System.Windows.Forms.Label();
			this.label2 = new System.Windows.Forms.Label();
			this.label1 = new System.Windows.Forms.Label();
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumos)).BeginInit();
			this.SuspendLayout();
			// 
			// btnEliminarInsumo
			// 
			this.btnEliminarInsumo.Location = new System.Drawing.Point(443, 69);
			this.btnEliminarInsumo.Margin = new System.Windows.Forms.Padding(2);
			this.btnEliminarInsumo.Name = "btnEliminarInsumo";
			this.btnEliminarInsumo.Size = new System.Drawing.Size(74, 33);
			this.btnEliminarInsumo.TabIndex = 33;
			this.btnEliminarInsumo.Text = "Eliminar";
			this.btnEliminarInsumo.UseVisualStyleBackColor = true;
			this.btnEliminarInsumo.Click += new System.EventHandler(this.btnEliminarInsumo_Click);
			// 
			// btnModificaInsumo
			// 
			this.btnModificaInsumo.Location = new System.Drawing.Point(364, 69);
			this.btnModificaInsumo.Margin = new System.Windows.Forms.Padding(2);
			this.btnModificaInsumo.Name = "btnModificaInsumo";
			this.btnModificaInsumo.Size = new System.Drawing.Size(74, 33);
			this.btnModificaInsumo.TabIndex = 32;
			this.btnModificaInsumo.Text = "Modificar";
			this.btnModificaInsumo.UseVisualStyleBackColor = true;
			this.btnModificaInsumo.Click += new System.EventHandler(this.btnModificaInsumo_Click);
			// 
			// btnInsertaInsumo
			// 
			this.btnInsertaInsumo.Location = new System.Drawing.Point(286, 69);
			this.btnInsertaInsumo.Margin = new System.Windows.Forms.Padding(2);
			this.btnInsertaInsumo.Name = "btnInsertaInsumo";
			this.btnInsertaInsumo.Size = new System.Drawing.Size(74, 33);
			this.btnInsertaInsumo.TabIndex = 31;
			this.btnInsertaInsumo.Text = "Insertar";
			this.btnInsertaInsumo.UseVisualStyleBackColor = true;
			this.btnInsertaInsumo.Click += new System.EventHandler(this.btnInsertaInsumo_Click);
			// 
			// tablaInsumos
			// 
			this.tablaInsumos.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
			this.tablaInsumos.Location = new System.Drawing.Point(9, 107);
			this.tablaInsumos.Margin = new System.Windows.Forms.Padding(2);
			this.tablaInsumos.Name = "tablaInsumos";
			this.tablaInsumos.RowHeadersWidth = 51;
			this.tablaInsumos.RowTemplate.Height = 24;
			this.tablaInsumos.Size = new System.Drawing.Size(508, 149);
			this.tablaInsumos.TabIndex = 30;
			// 
			// textBox3
			// 
			this.textBox3.Location = new System.Drawing.Point(59, 69);
			this.textBox3.Margin = new System.Windows.Forms.Padding(2);
			this.textBox3.Name = "textBox3";
			this.textBox3.Size = new System.Drawing.Size(155, 20);
			this.textBox3.TabIndex = 26;
			this.textBox3.Tag = "precio";
			// 
			// textBox2
			// 
			this.textBox2.Location = new System.Drawing.Point(59, 38);
			this.textBox2.Margin = new System.Windows.Forms.Padding(2);
			this.textBox2.Name = "textBox2";
			this.textBox2.Size = new System.Drawing.Size(155, 20);
			this.textBox2.TabIndex = 25;
			this.textBox2.Tag = "cantidad";
			// 
			// textBox1
			// 
			this.textBox1.Location = new System.Drawing.Point(59, 11);
			this.textBox1.Margin = new System.Windows.Forms.Padding(2);
			this.textBox1.Name = "textBox1";
			this.textBox1.Size = new System.Drawing.Size(155, 20);
			this.textBox1.TabIndex = 24;
			this.textBox1.Tag = "nombre";
			// 
			// label3
			// 
			this.label3.AutoSize = true;
			this.label3.Location = new System.Drawing.Point(7, 72);
			this.label3.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(37, 13);
			this.label3.TabIndex = 20;
			this.label3.Text = "Precio";
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(7, 43);
			this.label2.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(49, 13);
			this.label2.TabIndex = 19;
			this.label2.Text = "Cantidad";
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(7, 11);
			this.label1.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(44, 13);
			this.label1.TabIndex = 18;
			this.label1.Text = "Nombre";
			// 
			// Insumo
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(529, 284);
			this.Controls.Add(this.btnEliminarInsumo);
			this.Controls.Add(this.btnModificaInsumo);
			this.Controls.Add(this.btnInsertaInsumo);
			this.Controls.Add(this.tablaInsumos);
			this.Controls.Add(this.textBox3);
			this.Controls.Add(this.textBox2);
			this.Controls.Add(this.textBox1);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.label1);
			this.Margin = new System.Windows.Forms.Padding(2);
			this.Name = "Insumo";
			this.Text = "Insumo";
			((System.ComponentModel.ISupportInitialize)(this.tablaInsumos)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btnEliminarInsumo;
        private System.Windows.Forms.Button btnModificaInsumo;
        private System.Windows.Forms.Button btnInsertaInsumo;
        private System.Windows.Forms.DataGridView tablaInsumos;
        private System.Windows.Forms.TextBox textBox3;
        private System.Windows.Forms.TextBox textBox2;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
    }
}
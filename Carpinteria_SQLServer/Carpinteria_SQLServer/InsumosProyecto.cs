using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace Carpinteria_SQLServer
{
	public partial class InsumosProyecto : Form
	{
		SqlConnection conexion = new SqlConnection("Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		int cantidadActual;
		int idProyectoActual;

		public InsumosProyecto()
		{
			InitializeComponent();
		}


		private void InsumosProyecto_Load(object sender, EventArgs e)
		{
			muestraDatosTabla();
			muestraDatosResto();
			tablaInsumosProyecto.SelectionChanged += llenaTextBox;
		}

		public void llenaTextBox(object sender, EventArgs e)
		{
			
			if (tablaInsumosProyecto.SelectedRows.Count > 0)
			{
				DataGridViewRow filaSeleccionada = tablaInsumosProyecto.SelectedRows[0];
				foreach (DataGridViewCell celda in filaSeleccionada.Cells)
				{
					Control textBox = this.Controls.OfType<Control>().FirstOrDefault(c => c.Tag?.ToString() == celda.OwningColumn.Name);
					textBox.Text = celda.Value.ToString();
				}

				cantidadActual = Int32.Parse(textBox3.Text);
				comboInsumos.SelectedIndex = comboInsumos.FindStringExact(textBox1.Text);
				comboProyectos.SelectedIndex = comboProyectos.FindStringExact(textBox2.Text);
				DataRowView proyectoSel = (DataRowView)comboProyectos.SelectedItem;
				idProyectoActual = (int)proyectoSel["idProyecto"];
			}
		}

		private void muestraDatosTabla()
		{
			try
			{
				conexion.Open();
				//string query = "SELECT * FROM Proyecto.InsumoProyecto";
				string query = "SELECT a.nombre AS 'insumo',b.nombre_proyecto as 'proyecto', c.cantidad, c.subtotal FROM Proyecto.InsumoProyecto c INNER JOIN Proyecto.Insumo a on a.idInsumo = c.idInsumo INNER JOIN Proyecto.Proyecto b on b.idProyecto = c.idProyecto;";
				
				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);

				tablaInsumosProyecto.DataSource = dt;

				tablaInsumosProyecto.AutoSize = true;

				conexion.Close();
			}
			catch (Exception ex) { 
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}

		private void muestraDatosResto()
		{
			conexion.Open();
			//string query = "SELECT a.nombre AS 'insumo',b.nombre_proyecto as 'proyecto', c.cantidad, c.subtotal FROM Proyecto.InsumoProyecto c INNER JOIN Proyecto.Insumo a on a.idInsumo = c.idInsumo INNER JOIN Proyecto.Proyecto b on b.idProyecto = c.idProyecto;";
			string query3 = "SELECT idProyecto, nombre_proyecto as 'nombre' FROM Proyecto.Proyecto";
			string query2 = "SELECT * FROM Proyecto.Insumo";
			string query4 = "SELECT idInsumo, nombre FROM Proyecto.Insumo";

			
			SqlCommand comando = new SqlCommand(query2, conexion);
			SqlDataAdapter adaptador = new SqlDataAdapter(comando);
			DataTable dt = new DataTable();
			adaptador.Fill(dt);
			tablaInsumos.DataSource = dt;
			tablaInsumos.AutoSize = true;
			tablaInsumos.ReadOnly = true;


			comando = new SqlCommand(query3, conexion);
			adaptador = new SqlDataAdapter(comando);
			dt = new DataTable();
			adaptador.Fill(dt);
			comboProyectos.DataSource = dt;

			comboProyectos.DisplayMember = "Nombre";
			comboProyectos.ValueMember = "idProyecto";

			comando = new SqlCommand(query4, conexion);
			adaptador = new SqlDataAdapter(comando);
			dt = new DataTable();
			adaptador.Fill(dt);
			comboInsumos.DataSource = dt;

			comboInsumos.DisplayMember = "nombre";
			comboInsumos.ValueMember = "idInsumo";

			conexion.Close();
		}

		private void insertaProyectoInsumo()
		{
			try
			{
				conexion.Open();
				DataRowView proyectoSel = (DataRowView)comboProyectos.SelectedItem;
				int idProyecto = (int)proyectoSel["idProyecto"];


				DataRowView insumoSel = (DataRowView)comboInsumos.SelectedItem;
				int diInsumo = (int)insumoSel["idInsumo"];

				int cantidad = Int32.Parse(textBox3.Text);

				string query = "INSERT INTO Proyecto.InsumoProyecto (idInsumo,idProyecto,cantidad,subtotal) VALUES (@idInsumo,@idProyecto,@cantidad,"+0+")";
				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idInsumo",(long) diInsumo));
				comando.Parameters.Add(new SqlParameter("idProyecto", (long)idProyecto));
				comando.Parameters.Add(new SqlParameter("cantidad", cantidad));

				comando.ExecuteNonQuery();
				conexion.Close();
			}
			catch (Exception ex) {
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}

		private void modificar(int idInsumo, int idProyecto)
		{
			try
			{
				string texto2 = textBox1.Text;

				conexion.Open();
				string query = "UPDATE Proyecto.InsumoProyecto SET idInsumo = @idInsumo , idProyecto = @idProyecto,cantidad = @cantidad, subtotal = @subtotal WHERE idInsumo = @idInsumo AND idProyecto = "+idProyectoActual+"";
				SqlCommand comando = new SqlCommand(query, conexion);
				//EventosForms.parametrosForm(comando, this);
				comando.Parameters.Add(new SqlParameter("subtotal", textBox4.Text));
				comando.Parameters.Add(new SqlParameter("idInsumo", idInsumo));
				comando.Parameters.Add(new SqlParameter("idProyecto", idProyecto));
				comando.Parameters.Add(new SqlParameter("cantidad", textBox3.Text));
				comando.ExecuteNonQuery();
				conexion.Close();
			}
			catch (Exception ex) { 
				throw new Exception(ex.Message); 
			}
		}


		private void button1_Click(object sender, EventArgs e)
		{
			insertaProyectoInsumo();
			muestraDatosTabla();
			muestraDatosResto();
			EventosForms.limpiaTextbox(this);
		}

		private void restaCantidad(int idInsumo, int idProyecto, int diferencia)
		{
			try
			{
				string texto2 = textBox1.Text;

				conexion.Open();
				string query = "UPDATE Proyecto.Insumo " +
					"SET cantidad = cantidad - @diferencia " +
					
					"WHERE idInsumo = @idInsumo";
				SqlCommand comando = new SqlCommand(query, conexion);

				comando.Parameters.Add(new SqlParameter("idInsumo", idInsumo));
				comando.Parameters.Add(new SqlParameter("diferencia", diferencia));

				comando.ExecuteNonQuery();
				conexion.Close();
			}
			catch (Exception ex)
			{
				throw new Exception(ex.Message);
			}
		}

		private void sumaCantidad(int idInsumo, int idProyecto, int diferencia)
		{
			try
			{
				string texto2 = textBox1.Text;

				conexion.Open();
				string query = "UPDATE Proyecto.Insumo " +
					"SET cantidad = cantidad + @diferencia "  +
					"WHERE idInsumo = @idInsumo";
				SqlCommand comando = new SqlCommand(query, conexion);

				comando.Parameters.Add(new SqlParameter("idInsumo", idInsumo));

				comando.Parameters.Add(new SqlParameter("diferencia", diferencia));
				comando.ExecuteNonQuery();
				conexion.Close();
			}
			catch (Exception ex)
			{
				throw new Exception(ex.Message);
			}
		}

		private void button2_Click(object sender, EventArgs e)
		{
			DataRowView proyectoSel = (DataRowView)comboProyectos.SelectedItem;
			int idProyecto = (int)proyectoSel["idProyecto"];


			DataRowView insumoSel = (DataRowView)comboInsumos.SelectedItem;
			int idInsumo = (int)insumoSel["idInsumo"];

			int cantidad = Int32.Parse(textBox3.Text);


			modificar(idInsumo, idProyecto);
			int diferencia = cantidad - cantidadActual;
			if (diferencia < 0)
			{
				sumaCantidad(idInsumo,idProyecto,diferencia);
			}
			else
			{
				restaCantidad(idInsumo,idProyecto, diferencia);
			}

			conexion.Close();
			muestraDatosTabla();
			muestraDatosResto();
			EventosForms.limpiaTextbox(this);
		}

		private void button3_Click(object sender, EventArgs e)
		{
			try
			{
				DataRowView proyectoSel = (DataRowView)comboProyectos.SelectedItem;
				int idProyecto = (int)proyectoSel["idProyecto"];
				DataRowView insumoSel = (DataRowView)comboInsumos.SelectedItem;
				int idInsumo = (int)insumoSel["idInsumo"];

				conexion.Open();
				string query = "DELETE Proyecto.InsumoProyecto WHERE idInsumo = @id AND idProyecto = @idProyecto";
				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("id", idInsumo));
				comando.Parameters.Add(new SqlParameter("idProyecto", idProyecto));
				comando.ExecuteNonQuery();
				conexion.Close();
				muestraDatosTabla();
				muestraDatosResto();
				EventosForms.limpiaTextbox(this);
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

		private void tablaInsumosProyecto_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
		{
			if (e.ColumnIndex == 0 && e.Value != null && e.Value.GetType() == typeof(decimal))
			{
				decimal value = (decimal)e.Value;
				e.Value = value.ToString("0.00");
				e.FormattingApplied = true;
			}
		}

        private void tablaInsumosProyecto_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

		private void comboInsumos_SelectedIndexChanged(object sender, EventArgs e)
		{

		}
	}
}

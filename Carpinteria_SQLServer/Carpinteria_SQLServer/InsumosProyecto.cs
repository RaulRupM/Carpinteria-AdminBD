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

namespace Carpinteria_SQLServer
{
	public partial class InsumosProyecto : Form
	{
		SqlConnection conexion = new SqlConnection($"Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		public InsumosProyecto()
		{
			InitializeComponent();
		}


		private void InsumosProyecto_Load(object sender, EventArgs e)
		{
			muestraDatosTabla();
			tablaInsumosProyecto.SelectionChanged += EventosForms.llenaTextBox;
		}

		private void muestraDatosTabla()
		{
			try
			{
				conexion.Open();
				string query = "SELECT a.nombre AS 'insumo',b.nombre_proyecto as 'proyecto', c.cantidad, c.subtotal FROM Proyecto.InsumoProyecto c INNER JOIN Proyecto.Insumo a on a.idInsumo = c.idInsumo INNER JOIN Proyecto.Proyecto b on b.idProyecto = c.idProyecto;";

				string query2 = "SELECT * FROM Proyecto.Insumo";

				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				tablaInsumosProyecto.DataSource = dt;
				tablaInsumosProyecto.AutoSize = true;

				comando = new SqlCommand(query2, conexion);
				adaptador = new SqlDataAdapter(comando);
				dt = new DataTable();
				adaptador.Fill(dt);
				tablaInsumos.DataSource = dt;
				tablaInsumos.AutoSize = true;
				conexion.Close();
			}
			catch (Exception ex) { 
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}

		private void insertaProyectoInsumo()
		{
			try
			{
				conexion.Open();
				string query = "INSERT INTO Proyecto.InsumoProyecto (idInsumo,idProyecto,cantidad) VALUES (@idInsumo,@idProyecto,@cantidad)";
				SqlCommand comando = new SqlCommand(query, conexion);
				EventosForms.parametrosForm(comando, this);
				comando.ExecuteNonQuery();
				conexion.Close();
				muestraDatosTabla();
			}
			catch (Exception ex) {
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}

		private void modificar(int id)
		{
			try
			{
				conexion.Open();
				string query = "UPDATE Proyecto.InsumoProyecto SET idInsumo = @idInsumo, idProyecto = @idProyecto,cantidad = @cantidad WHERE idInsumo = @id";
				SqlCommand comando = new SqlCommand(query, conexion);
				EventosForms.parametrosForm(comando, this);
				comando.Parameters.Add(new SqlParameter("id", id));
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
			EventosForms.limpiaTextbox(this);
		}

		private void button2_Click(object sender, EventArgs e)
		{
			int id = (int)tablaInsumosProyecto.SelectedRows[0].Cells["idHerramienta"].Value; ;
			modificar(id);
			muestraDatosTabla();
			EventosForms.limpiaTextbox(this);
		}

		private void button3_Click(object sender, EventArgs e)
		{
			try
			{
				int idInsumo = (int)tablaInsumosProyecto.SelectedRows[0].Cells["idHerramienta"].Value;
				int idProyecto = (int)tablaInsumosProyecto.SelectedRows[0].Cells["idInsumo"].Value;
				conexion.Open();
				string query = "DELETE Proyecto.InsumoProyecto WHERE idInsumo = @id AND idProyecto = @idProyecto";
				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("id", idInsumo));
				comando.Parameters.Add(new SqlParameter("idProyecto", idProyecto));
				comando.ExecuteNonQuery();
				EventosForms.limpiaTextbox(this);
				conexion.Close();
				muestraDatosTabla();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}
	}
}

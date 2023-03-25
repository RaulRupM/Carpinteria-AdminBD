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
    public partial class Herramienta : Form
    {
		SqlConnection conexion = new SqlConnection($"Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		public Herramienta()
        {
            InitializeComponent();
            tablaHerramientas.SelectionChanged += EventosForms.llenaTextBox;
			muestraDatosTabla();
        }

		private void muestraDatosTabla()
		{
			try
			{
				conexion.Open();
				string query = "SELECT * FROM Proyecto.Herramienta";
				SqlCommand comando = new SqlCommand(query,conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				tablaHerramientas.DataSource = dt;
				tablaHerramientas.AutoSize= true;
				conexion.Close();
			}catch(Exception ex) { throw new Exception(ex.Message); }
		}

		private void btnInsertarHerramienta_Click(object sender, EventArgs e)
		{
			try
			{
				conexion.Open();
				string query = "INSERT INTO Proyecto.Herramienta (nombre,tipo,estado,cantidad_disponible) VALUES (@nombre,@tipo,@estado,@cantidad_disponible)";
				SqlCommand comando = new SqlCommand(query,conexion);
				EventosForms.parametrosForm(comando, this);
				comando.ExecuteNonQuery();
				conexion.Close();
				muestraDatosTabla();
				EventosForms.limpiaTextbox(this);
			}catch(Exception ex)
			{
				conexion.Close();
				MessageBox.Show("Error al modificar: el nombre ya existe");
			}
		}

		private void btnModificarHerramienta_Click(object sender, EventArgs e)
		{
			try
			{
				int id = (int)tablaHerramientas.SelectedRows[0].Cells["idHerramienta"].Value;
				conexion.Open();
				string query = "UPDATE Proyecto.Herramienta SET nombre = @nombre, tipo = @tipo, estado = @estado, cantidad_disponible = @cantidad_disponible WHERE idHerramienta = @id";
				SqlCommand comando = new SqlCommand(query,conexion);
				EventosForms.parametrosForm(comando, this);
				comando.Parameters.Add(new SqlParameter("id",id));
				comando.ExecuteNonQuery();
				EventosForms.limpiaTextbox(this);
				conexion.Close();
				muestraDatosTabla();
			}catch(Exception ex)
			{
				conexion.Close();
				MessageBox.Show("Error al modificar: el nombre ya existe");
			}
		}

		private void btnEliminarHerramienta_Click(object sender, EventArgs e)
		{
			try
			{
				int id = (int)tablaHerramientas.SelectedRows[0].Cells["idHerramienta"].Value;
				conexion.Open();
				string query = "DELETE Proyecto.Herramienta WHERE idHerramienta = @id";
				SqlCommand comando = new SqlCommand(query,conexion);
				comando.Parameters.Add(new SqlParameter("id", id));
				comando.ExecuteNonQuery();
				EventosForms.limpiaTextbox(this);
				conexion.Close();
				muestraDatosTabla();
			}catch (Exception ex) { 
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}
	}
}

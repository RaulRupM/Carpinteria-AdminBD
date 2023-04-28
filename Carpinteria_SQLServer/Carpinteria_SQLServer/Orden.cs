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
	public partial class Orden : Form
	{
		SqlConnection conexion = new SqlConnection("Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		public Orden()
		{
			InitializeComponent();
		}
		private void muestraDatosTabla()
		{
			try
			{
				conexion.Open();
				string query = "SELECT o.idOrden, p.nombre as proveedor, o.fechaOrden, o.total FROM Orden.Orden o INNER JOIN Proveedor.Proveedor p ON o.idProveedor = p.idProveedor";
				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				tablaOrden.DataSource = dt;
				tablaOrden.AutoSize = true;
				conexion.Close();
			}
			catch (Exception ex) {
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}

		

		private void obtenerProveedores()
		{
			try
			{
				conexion.Open();
				string query = "SELECT idProveedor, nombre FROM Proveedor.Proveedor";
				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				comboProveedores.DataSource = dt;

				comboProveedores.DisplayMember= "nombre";
				comboProveedores.ValueMember= "idProveedor";
				
				conexion.Close();
			}
			catch (Exception ex) {
				conexion.Close();
				throw new Exception(ex.Message); 
			}
		}



		private void Orden_Load(object sender, EventArgs e)
		{
			actualizaTablas();
			obtenerProveedores();
			dateTimePicker1.Value = DateTime.Now;
		}

		private void actualizaTablas()
		{
			muestraDatosTabla();
		}

		private void btnAgregar_Click(object sender, EventArgs e)
		{
			try
			{
				DataRowView proveedorSel = (DataRowView) comboProveedores.SelectedItem;
				int idProveedor = (int)proveedorSel["idProveedor"];

				conexion.Open();
				string query = "INSERT INTO Orden.Orden(idProveedor,fechaOrden) VALUES (@idProveedor,GETDATE())";

				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idProveedor", idProveedor));
				
				comando.ExecuteNonQuery();
				conexion.Close();

				
				actualizaTablas();
				desactivarBotones();

			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

		private void comboProveedores_SelectedIndexChanged(object sender, EventArgs e)
		{
			btnAgregar.Enabled = true;
		}

		private void tablaOrden_SelectionChanged(object sender, EventArgs e)
		{
			if(tablaOrden.SelectedRows.Count > 0)
			{
				DataGridViewRow filaSeleccionada = tablaOrden.SelectedRows[0];
				string proveedorSel = (string)filaSeleccionada.Cells[1].Value;
				comboProveedores.SelectedIndex = comboProveedores.FindStringExact(proveedorSel);
				btnModificar.Enabled = true;
				btnEliminar.Enabled = true;
				string valorFecha = filaSeleccionada.Cells[2].Value.ToString();
				dateTimePicker1.Value = DateTime.Parse(valorFecha);
				dateTimePicker1.Enabled = true;

				button1.Enabled = true;
				button2.Enabled = true;
			}
		}

		private void desactivarBotones()
		{
			btnEliminar.Enabled = false;
			btnModificar.Enabled = false;
			dateTimePicker1.Value = DateTime.Now;
			dateTimePicker1.Enabled = false;
		}

		private void btnModificar_Click(object sender, EventArgs e)
		{
			try
			{
				string fecha = dateTimePicker1.Value.ToString();
				DataRowView proveedorSel = (DataRowView)comboProveedores.SelectedItem;
				int idProveedor = (int)proveedorSel["idProveedor"];
				DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
				long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

				conexion.Open();
				string query = "UPDATE Orden.Orden SET fechaOrden = @fecha, idProveedor = @idProveedor WHERE idOrden = @idOrden";	
				
				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idProveedor", idProveedor));
				comando.Parameters.Add(new SqlParameter("fecha", fecha));
				comando.Parameters.Add(new SqlParameter("idOrden", idOrdenSeleccionada));

				comando.ExecuteNonQuery();
				conexion.Close();


				actualizaTablas();
				resetearControles();

			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

		private void btnEliminar_Click(object sender, EventArgs e)
		{
			try
			{
				DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
				long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

				conexion.Open();
				string query = "DELETE FROM Orden.Orden WHERE idOrden = @idOrden";

				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idOrden", idOrdenSeleccionada));

				comando.ExecuteNonQuery();
				conexion.Close();


				actualizaTablas();
				resetearControles();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

		private void resetearControles()
		{
			btnEliminar.Enabled = false;
			btnModificar.Enabled = false;
			dateTimePicker1.Value = DateTime.Now;
			dateTimePicker1.Enabled = false;

			button1.Enabled = false;
			button2.Enabled = false;
		}

		private void button1_Click(object sender, EventArgs e)
		{
			DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
			long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

			delegadoActualizaDatosTabla delegado = new delegadoActualizaDatosTabla(muestraDatosTabla);

			NuevaOrden ordenHerramienta = new NuevaOrden("Herramienta",idOrdenSeleccionada,delegado);
			ordenHerramienta.ShowDialog();
		}

		private void button2_Click(object sender, EventArgs e)
		{
			DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
			long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

			delegadoActualizaDatosTabla delegado = new delegadoActualizaDatosTabla(muestraDatosTabla);

			NuevaOrden ordenInsumo = new NuevaOrden("insumo",idOrdenSeleccionada, delegado);
			
			ordenInsumo.ShowDialog();
		}
	}
}

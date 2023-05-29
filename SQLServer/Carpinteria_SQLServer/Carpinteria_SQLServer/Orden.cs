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
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection("Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Orden()
		{
			InitializeComponent();
		}

        //Conexion a la base de datos.
        //Consulta SQL para mostrar información de una tabla de la base de datos.
        private void muestraDatosTabla()
		{
			try
			{
				conexion.Open();
				string query = "SELECT o.idOrden, p.nombre as proveedor, o.fechaOrden, o.total FROM Orden.Orden o INNER JOIN Empresa.Proveedor p ON o.idProveedor = p.idProveedor";
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

        //Función obtenerProveedor, donde hacemos una consulta SQL a la tabla
		//proveedor donde obtenemos los datos de un proveedor específico.
		//Y dichos datos se guardan en una variable para tener mayor control
		//y accesibilidad a ellos.  
        private void obtenerProveedores()
		{
			try
			{
				conexion.Open();
				string query = "SELECT idProveedor, nombre FROM Empresa.Proveedor";
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


		//Llama a las funciones de obtener proveedor y actualizar.
		private void Orden_Load(object sender, EventArgs e)
		{
			actualizaTablas();
			obtenerProveedores();
			dateTimePicker1.Value = DateTime.Now;
		}

		//Funcion de actualizacion de datos.
		private void actualizaTablas()
		{
			muestraDatosTabla();
		}

        //Función del botón Nueva orden, en donde insertamos una nueva orden a un proyecto específico.
        private void btnAgregar_Click(object sender, EventArgs e)
		{
			try
			{
				DataRowView proveedorSel = (DataRowView) comboProveedores.SelectedItem;
				int idProveedor = (int)proveedorSel["idProveedor"];

				conexion.Open();
				string query = "INSERT INTO Orden.Orden(idProveedor,fechaOrden,total) VALUES (@idProveedor,GETDATE(),0)";

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

		//habilita boton agregar.
		private void comboProveedores_SelectedIndexChanged(object sender, EventArgs e)
		{
			btnAgregar.Enabled = true;
		}

		//Funcion para habilitar o deshabilitar botones.
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
			else
			{
				button1.Enabled = false;
				button2.Enabled = false;
			}
		}

		//Llamada a conbio de controles.
		private void desactivarBotones()
		{
			btnEliminar.Enabled = false;
			btnModificar.Enabled = false;
			dateTimePicker1.Value = DateTime.Now;
			dateTimePicker1.Enabled = false;
		}

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
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

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para borrar filas de una tabla de la base de datos.
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
				MessageBox.Show("No se pueden eliminar los elementos","Error al eliminar",MessageBoxButtons.OK, MessageBoxIcon.Error);
				//throw new Exception(ex.Message);
			}
		}

		//Llamada para resetear controles.
		private void resetearControles()
		{
			btnEliminar.Enabled = false;
			btnModificar.Enabled = false;
			dateTimePicker1.Value = DateTime.Now;
			dateTimePicker1.Enabled = false;

			button1.Enabled = false;
			button2.Enabled = false;
		}

        //Función del botón Ordenar Herramientas.
        private void button1_Click(object sender, EventArgs e)
		{
			DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
			long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

			delegadoActualizaDatosTabla delegado = new delegadoActualizaDatosTabla(muestraDatosTabla);

			NuevaOrden ordenHerramienta = new NuevaOrden("Herramienta",idOrdenSeleccionada,delegado);
			ordenHerramienta.ShowDialog();
		}

        //Función del botón Ordenar Insumo.
        private void button2_Click(object sender, EventArgs e)
		{
			DataGridViewRow ordenSeleccionada = tablaOrden.SelectedRows[0];
			long idOrdenSeleccionada = (long)ordenSeleccionada.Cells[0].Value;

			delegadoActualizaDatosTabla delegado = new delegadoActualizaDatosTabla(muestraDatosTabla);

			NuevaOrden ordenInsumo = new NuevaOrden("insumo",idOrdenSeleccionada, delegado);
			
			ordenInsumo.ShowDialog();
		}

		private void tablaOrden_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
		{
			if (e.Value != null && e.Value != DBNull.Value)
			{
				if (e.ColumnIndex == 3)
				{
					e.Value = decimal.Round((decimal)e.Value, 2);
				}
			}
		}
	}
}

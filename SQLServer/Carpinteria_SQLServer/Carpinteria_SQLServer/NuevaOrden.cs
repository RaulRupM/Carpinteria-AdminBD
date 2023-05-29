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
    //Funcion que representa referencias a métodos con una lista de parámetros determinada y un tipo de valor devuelto
    public delegate void delegadoActualizaDatosTabla();

    public partial class NuevaOrden : Form
	{
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection("Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		private string producto;
		private long idOrden;
		private int idProductoSel;
		private decimal subtotalActual = 0;
		//Variable delegada.
		delegadoActualizaDatosTabla delegado;

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public NuevaOrden(string producto, long idOrden, delegadoActualizaDatosTabla delegado)
		{
			InitializeComponent();
			this.producto = producto;
			this.idOrden = idOrden;
			this.delegado = delegado;
			idProductoSel = 0;
			inicializaComponentes();
		}

        //Conexion a la base de datos.
        //Consulta SQL para mostrar información de una tabla de la base de datos.
        private void muestraDatosTablaHerramienta()
		{
			try
			{
				conexion.Open();
				string query = @"SELECT do.idOrden,do.idHerramienta, h.nombre AS herramienta,do.cantidad, do.subtotal 
									FROM Orden.DetalleOrden do
									INNER JOIN Proyecto.Herramienta h
									ON do.idHerramienta = h.idHerramienta
									WHERE do.idOrden = @idOrden";
				
				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idOrden", idOrden));

				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				dataGridView1.DataSource = dt;
				dataGridView1.AutoSize = true;
				conexion.Close();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //Conexion a la base de datos.
        //Consulta SQL para mostrar información de una tabla de la base de datos.
        private void muestraDatosTablaInsumos()
		{
			try
			{
				conexion.Open();
				string query = @"SELECT do.idOrden,do.idInsumo, i.nombre AS insumo, do.cantidad, do.subtotal 
									FROM Orden.DetalleOrden do
									INNER JOIN Proyecto.Insumo i
									ON do.idInsumo = i.idInsumo
									WHERE do.idOrden = @idOrden";

				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idOrden", idOrden));

				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				dataGridView1.DataSource = dt;
				dataGridView1.AutoSize = true;
				conexion.Close();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //Función para inicializar componentes, para obtener la informacion de herramienta y insumo.
        private void inicializaComponentes()
		{
			if (this.producto.Equals("insumo", StringComparison.OrdinalIgnoreCase)){
				lblProducto.Text = "Insumo";
				obtenerComboInsumos();
				btnAgregar.Click += agregarInsumo;
				btnModificar.Click += modificarInsumo;
				btnEliminar.Click += eliminaDetalleInsumo;
				muestraDatosTablaInsumos();
			}
			else
			{
				lblProducto.Text = "Herramienta";
				obtenerComboHerramientas();
				btnAgregar.Click += agregarHerramienta;
				btnModificar.Click += modificarHerramienta;
				btnEliminar.Click += eliminaDetalleHerramienta;
				muestraDatosTablaHerramienta();
			}
			
		}

        //La siguiente función nos ayuda a identificar qué herramienta se está seleccionando en el comboBox y se guarda esa información. 
        private void obtenerComboHerramientas()
		{
			try
			{
				conexion.Open();
				string query = "SELECT idHerramienta, nombre, cantidad_disponible, precio FROM Proyecto.Herramienta";
				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				comboProducto.DataSource = dt;

				comboProducto.DisplayMember = "nombre";
				comboProducto.ValueMember = "idHerramienta";

				conexion.Close();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //La siguiente función nos ayuda a identificar qué insumo se está seleccionando en el comboBox y se guarda esa información. 
        private void obtenerComboInsumos()
		{
			try
			{
				conexion.Open();
				string query = "SELECT * FROM Proyecto.Insumo";
				SqlCommand comando = new SqlCommand(query, conexion);
				SqlDataAdapter adaptador = new SqlDataAdapter(comando);
				DataTable dt = new DataTable();
				adaptador.Fill(dt);
				comboProducto.DataSource = dt;

				comboProducto.DisplayMember = "nombre";
				comboProducto.ValueMember = "idInsumo";

				conexion.Close();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para insertar filas en una tabla de la base de datos.
        private void agregarInsumo(object sender, EventArgs e)
		{
			try
			{
				DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
				int idInsumo = (int)productoSel["idInsumo"];
				int cantidad = Int32.Parse(textBoxCantidad.Text);
				decimal precio = (decimal)productoSel["precio"];

				decimal subtotal = precio * cantidad;


				conexion.Open();
				string query = "INSERT INTO Orden.DetalleOrden (idOrden,cantidad,idInsumo,subtotal) VALUES (@idOrden,@cantidad,@idInsumo,@subtotal)";

				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
				comando.Parameters.Add(new SqlParameter("cantidad", cantidad));
				comando.Parameters.Add(new SqlParameter("idInsumo", idInsumo));
				comando.Parameters.Add(new SqlParameter("subtotal",subtotal));

				comando.ExecuteNonQuery();
				conexion.Close();

				reiniciarControles();
				muestraDatosTablaInsumos();
				actualizarTablaOrden();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        private void modificarInsumo(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			int idInsumo = (int)productoSel["idInsumo"];
			int cantidad = Int32.Parse(textBoxCantidad.Text);
			decimal precio = (decimal)productoSel["precio"];

			decimal subtotal = precio * cantidad;

			restaSubtotalActual();

			conexion.Open();
			string query = @"UPDATE Orden.DetalleOrden 
							SET cantidad = @cantidad, idInsumo = @idInsumo, subtotal = @subtotal
							WHERE idOrden = @idOrden AND idInsumo = @idProducto ";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("cantidad", cantidad));
			comando.Parameters.Add(new SqlParameter("idInsumo", idInsumo));
			comando.Parameters.Add(new SqlParameter("subtotal", subtotal));
			comando.Parameters.Add(new SqlParameter("idProducto", idProductoSel));

			comando.ExecuteNonQuery();


			conexion.Close();

			sumaSubtotalActual(subtotal);

			reiniciarControles();

			muestraDatosTablaInsumos();
			actualizarTablaOrden();
		}

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        private void modificarHerramienta(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			int idInsumo = (int)productoSel["idHerramienta"];
			int cantidad = Int32.Parse(textBoxCantidad.Text);
			decimal precio = (decimal)productoSel["precio"];

			decimal subtotal = precio * cantidad;

			restaSubtotalActual();
			conexion.Open();
			string query = @"UPDATE Orden.DetalleOrden 
							SET cantidad = @cantidad, idHerramienta = @idHerramienta, subtotal = @subtotal
							WHERE idOrden = @idOrden AND idHerramienta = @idProducto ";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("cantidad", cantidad));
			comando.Parameters.Add(new SqlParameter("idHerramienta", idInsumo));
			comando.Parameters.Add(new SqlParameter("subtotal", subtotal));
			comando.Parameters.Add(new SqlParameter("idProducto", idProductoSel));

			comando.ExecuteNonQuery();
			conexion.Close();

			reiniciarControles();
			sumaSubtotalActual(subtotal);

			muestraDatosTablaHerramienta();
			actualizarTablaOrden();
		}

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para insertar filas en una tabla de la base de datos.
        private void agregarHerramienta(object sender, EventArgs e)
		{
			try
			{
				DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
				int idHerramienta = (int)productoSel["idHerramienta"];
				int cantidad = Int32.Parse(textBoxCantidad.Text);
				decimal precio = (decimal)productoSel["precio"];

				decimal subtotal = precio * cantidad;


				conexion.Open();
				string query = "INSERT INTO Orden.DetalleOrden (idOrden,cantidad,idHerramienta,subtotal) VALUES (@idOrden,@cantidad,@idHerramienta,@subtotal)";

				SqlCommand comando = new SqlCommand(query, conexion);
				comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
				comando.Parameters.Add(new SqlParameter("cantidad", cantidad));
				comando.Parameters.Add(new SqlParameter("idHerramienta", idHerramienta));
				comando.Parameters.Add(new SqlParameter("subtotal", subtotal));

				comando.ExecuteNonQuery();
				conexion.Close();

				reiniciarControles();
				muestraDatosTablaHerramienta();
				actualizarTablaOrden();

			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

        //Función delegado, hace referencia a un tipo de valor devuelto y accedemos a la clase Orden..
        public void actualizarTablaOrden()
		{
			delegado();
		}

        //Función DataGridWiew, al hacer clic sobre los datos del datagridview se autocompletará los textbox con dichos datos
        private void dataGridView1_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
		{
			if(e.Value != null && e.Value != DBNull.Value)
			{
				DataGridView tabla = (DataGridView)sender;
				if(e.ColumnIndex == 1 ) {
					tabla.Columns[e.ColumnIndex].Visible = false;
				}
				if(e.ColumnIndex == 4)
				{
					e.Value = decimal.Round((decimal)e.Value, 2);
				}
			}
		}

        //La siguiente función nos ayuda a identificar qué precio se está seleccionando en el comboBox y se guarda esa información. 
        private void comboProducto_SelectedIndexChanged(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			decimal precioProducto = (decimal)productoSel["precio"];
			lblPrecioProd.Text = decimal.Round(precioProducto,2).ToString();
		}

		private void btnModificar_Click(object sender, EventArgs e)
		{

		}

        //Función DataGridWiew, al hacer clic sobre los datos del datagridview se autocompletará los textbox con dichos datos
        private void dataGridView1_SelectionChanged(object sender, EventArgs e)
		{
			if (dataGridView1.SelectedRows.Count > 0)
			{
				DataGridViewRow filaSeleccionada = dataGridView1.SelectedRows[0];
				int productoSel = (int)filaSeleccionada.Cells[1].Value;
				int cantidadSel = (int)filaSeleccionada.Cells[3].Value;
				subtotalActual = (decimal) filaSeleccionada.Cells[4].Value;
				comboProducto.SelectedValue = productoSel;
				idProductoSel = productoSel;
				textBoxCantidad.Text = cantidadSel.ToString();
				btnModificar.Enabled = true;
				btnEliminar.Enabled = true;	
			}
		}

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para borrar filas de una tabla de la base de datos.
        private void eliminaDetalleHerramienta(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			int idInsumo = (int)productoSel["idHerramienta"];
			
			restaSubtotalActual();
			conexion.Open();
			string query = @"DELETE Orden.DetalleOrden
							WHERE idOrden = @idOrden AND idHerramienta = @idProducto ";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("idProducto", idInsumo));

			comando.ExecuteNonQuery();
			conexion.Close();

			reiniciarControles();
			muestraDatosTablaHerramienta();
			actualizarTablaOrden();
		}

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para borrar filas de una tabla de la base de datos.
        private void eliminaDetalleInsumo(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			int idInsumo = (int)productoSel["idInsumo"];

			restaSubtotalActual();
			conexion.Open();
			string query = @"DELETE Orden.DetalleOrden
							WHERE idOrden = @idOrden AND idInsumo = @idProducto ";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("idProducto", idInsumo));

			comando.ExecuteNonQuery();
			conexion.Close();

			reiniciarControles();
			muestraDatosTablaInsumos();
			actualizarTablaOrden();
		}

        // Función, en donde se realiza una consulta SQL para actualizar
		// información sobre la tabla orden de la base de datos, en donde
		// se resta el subtotal.
        private void restaSubtotalActual()
		{
			conexion.Open();
			string query = @"UPDATE Orden.Orden 
							SET total = total - @subtotal
							WHERE idOrden = @idOrden";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("subtotal", subtotalActual));

			comando.ExecuteNonQuery();
			conexion.Close();
		}

        // Función, en donde se realiza una consulta SQL
        // para actualizar información sobre la tabla orden
        // de la base de datos, en donde se suma el subtotal.
        private void sumaSubtotalActual(decimal nuevoSubtotal)
		{
			conexion.Open();
			string query = @"UPDATE Orden.Orden 
							SET total = total + @subtotal
							WHERE idOrden = @idOrden";

			SqlCommand comando = new SqlCommand(query, conexion);
			comando.Parameters.Add(new SqlParameter("idOrden", idOrden));
			comando.Parameters.Add(new SqlParameter("subtotal", nuevoSubtotal));

			comando.ExecuteNonQuery();
			conexion.Close();
		}

        //Función para restablecer controles.
        private void reiniciarControles()
		{
			textBoxCantidad.Clear();
			btnModificar.Enabled = false;
			btnEliminar.Enabled = false;
		}

        private void btnEliminar_Click(object sender, EventArgs e)
        {

        }

        private void NuevaOrden_Load(object sender, EventArgs e)
        {

        }
    }
}

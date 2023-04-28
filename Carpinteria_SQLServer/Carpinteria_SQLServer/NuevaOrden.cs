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
	public delegate void delegadoActualizaDatosTabla();
	public partial class NuevaOrden : Form
	{
		SqlConnection conexion = new SqlConnection("Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		private string producto;
		private long idOrden;
		delegadoActualizaDatosTabla delegado;

		public NuevaOrden(string producto, long idOrden, delegadoActualizaDatosTabla delegado)
		{
			InitializeComponent();
			this.producto = producto;
			this.idOrden = idOrden;
			this.delegado = delegado;
			inicializaComponentes();
		}

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

		private void inicializaComponentes()
		{
			if (this.producto.Equals("insumo", StringComparison.OrdinalIgnoreCase)){
				lblProducto.Text = "Insumo";
				obtenerComboInsumos();
				btnAgregar.Click += agregarInsumo;
				muestraDatosTablaInsumos();
			}
			else
			{
				lblProducto.Text = "Herramienta";
				obtenerComboHerramientas();
				btnAgregar.Click += agregarHerramienta;
				muestraDatosTablaHerramienta();
			}
			
		}

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


				muestraDatosTablaInsumos();
				actualizarTablaOrden();
			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}

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

				muestraDatosTablaHerramienta();
				actualizarTablaOrden();

			}
			catch (Exception ex)
			{
				conexion.Close();
				throw new Exception(ex.Message);
			}
		}
	
		public void actualizarTablaOrden()
		{
			delegado();
		}

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

		private void comboProducto_SelectedIndexChanged(object sender, EventArgs e)
		{
			DataRowView productoSel = (DataRowView)comboProducto.SelectedItem;
			decimal precioProducto = (decimal)productoSel["precio"];
			lblPrecioProd.Text = decimal.Round(precioProducto,2).ToString();
		}
	}
}

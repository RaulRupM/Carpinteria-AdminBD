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
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection($"Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Herramienta()
        {
            InitializeComponent();
            tablaHerramientas.SelectionChanged += EventosForms.llenaTextBox;
			muestraDatosTabla();
        }

        //Conexion a la base de datos.
        //Consulta SQL para mostrar información de una tabla de la base de datos.
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

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para insertar filas en una tabla de la base de datos.
        private void btnInsertarHerramienta_Click(object sender, EventArgs e)
		{
			try
			{
				conexion.Open();
				string query = "INSERT INTO Proyecto.Herramienta (nombre,tipo,estado,cantidad_disponible,precio) VALUES (@nombre,@tipo,@estado,@cantidad_disponible,@precio)";
				SqlCommand comando = new SqlCommand(query,conexion);
				EventosForms.parametrosForm(comando, this);
				comando.ExecuteNonQuery();
				conexion.Close();
				muestraDatosTabla();
				EventosForms.limpiaTextbox(this);
			}catch(Exception ex) { MessageBox.Show("Nombre existente") ; }
		}

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        private void btnModificarHerramienta_Click(object sender, EventArgs e)
		{
			try
			{
				int id = (int)tablaHerramientas.SelectedRows[0].Cells["idHerramienta"].Value;
				conexion.Open();
				string query = "UPDATE Proyecto.Herramienta SET nombre = @nombre, tipo = @tipo, estado = @estado, cantidad_disponible = @cantidad_disponible , precio = @precio WHERE idHerramienta = @id";
				SqlCommand comando = new SqlCommand(query,conexion);
				EventosForms.parametrosForm(comando, this);
				comando.Parameters.Add(new SqlParameter("id",id));
				comando.ExecuteNonQuery();
				EventosForms.limpiaTextbox(this);
				conexion.Close();
				muestraDatosTabla();
			}catch(Exception ex) { throw new Exception(ex.Message); }
		}

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para borrar filas de una tabla de la base de datos.
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
			}catch (Exception ex) { throw new Exception(ex.Message); }
		}

        private void Herramienta_Load(object sender, EventArgs e)
        {

        }

        //Función del botón Préstamo, donde accedemos a la clase préstamo. 
        private void button1_Click(object sender, EventArgs e)
        {
			Prestamo press = new Prestamo();
			press.Show();
			this.Close();
		}
    }
}

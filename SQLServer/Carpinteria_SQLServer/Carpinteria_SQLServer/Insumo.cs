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
using TextBox = System.Windows.Forms.TextBox;

namespace Carpinteria_SQLServer
{
    public partial class Insumo : Form
    {
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection($"Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Insumo()
        {
            InitializeComponent();
            tablaInsumos.SelectionChanged += EventosForms.llenaTextBox;
            muestraDatosTabla();
        }

        //Conexion a la base de datos.
        //Consulta SQL para mostrar información de una tabla de la base de datos.
        private void muestraDatosTabla()
        {
            try
            {
                conexion.Open();
                string query = "SELECT * FROM Proyecto.Insumo";
                SqlCommand comando = new SqlCommand(query,conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(comando);
                DataTable registro = new DataTable();
                adapter.Fill(registro);
                registro.PrimaryKey = new DataColumn[] { registro.Columns["idInsumo"] };
                tablaInsumos.DataSource = registro;
                tablaInsumos.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;
                conexion.Close();
            }catch(Exception ex) {
                throw new Exception(ex.Message);
            }

        }

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para insertar filas en una tabla de la base de datos.
        private void insertaInsumo()
        {
            try
            {
                conexion.Open();
                string query = "INSERT INTO Proyecto.Insumo (nombre,cantidad,precio) VALUES (@nombre,@cantidad,@precio)";
                SqlCommand comando = new SqlCommand(query,conexion);
                EventosForms.parametrosForm(comando,this);
                comando.ExecuteNonQuery();
                conexion.Close();
                muestraDatosTabla();
			}catch(Exception ex) { throw new Exception(ex.Message); }
        }

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        private void modificaInsumo(int id)
        {
            try
            {
                conexion.Open();
                string query = "UPDATE Proyecto.Insumo SET nombre = @nombre, cantidad = @cantidad, precio = @precio WHERE idInsumo = @id";
                SqlCommand comando = new SqlCommand(query,conexion);
                EventosForms.parametrosForm(comando,this);
                comando.Parameters.Add(new SqlParameter("id", id));
                comando.ExecuteNonQuery();
                conexion.Close();
            }catch(Exception ex) { throw new Exception(ex.Message); }
        }

        //Llama a las funciones insertar, mostrar table  y limpia los textbox al finalizar.
        private void btnInsertaInsumo_Click(object sender, EventArgs e)
		{
            insertaInsumo();
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        //Consulta SQL para borrar filas de una tabla de la base de datos.
        private void eliminaInsumo(int id)
        {
            try
            {
                conexion.Open();
                string query = "DELETE Proyecto.Insumo WHERE idInsumo = @id";
                SqlCommand comando = new SqlCommand(query, conexion);
                comando.Parameters.Add(new SqlParameter("id", id));
                comando.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex) { throw new Exception(ex.Message + 1); }
        }

        //Llama las funciones de modificar, mostrar table y limpia los textbox.
        private void btnModificaInsumo_Click(object sender, EventArgs e)
		{
            int id = (int)tablaInsumos.SelectedRows[0].Cells["idInsumo"].Value;
            modificaInsumo(id);
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}

        //Llama las funciones de eliminar, mostrar table y limpia los textbox.
		private void btnEliminarInsumo_Click(object sender, EventArgs e)
		{
			int id = (int)tablaInsumos.SelectedRows[0].Cells["idInsumo"].Value;
            eliminaInsumo(id);
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}

        private void Insumo_Load(object sender, EventArgs e)
        {

        }
    }
}

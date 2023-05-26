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
    public partial class Tipo_Proyecto : Form
    {
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection($"Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Tipo_Proyecto()
        {
            InitializeComponent();
            connectaBD();
        }

        private void Tipo_Proyecto_Load(object sender, EventArgs e)
        {

        }

        //Conexion a la base de datos.
        public int connectaBD()
        {
            try
            {
                conexion.Open();
                muestra();
                conexion.Close();
                return 0;
            }
            catch (Exception ex)
            {
                conexion.Close();
                return -1;
            }
        }

        //Consulta SQL para mostrar información de una tabla de la base de datos.
        public void muestra()
        {
            string query = string.Concat("SELECT * FROM Proyecto.Tipo_Proyecto");

            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            tablaTipo.DataSource = null;
            tablaTipo.DataSource = registro;
            tablaTipo.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }

        //Consulta SQL para insertar filas en una tabla de la base de datos.
        public void insertaRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Tipo_Proyecto" +
                    "(nombre_proyecto,precio)" +
                    " VALUES ('" + textBox1.Text + "'," + textBox2.Text +")";
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Proyecto ya existente, favor de cambiar nombre del proyecto");
            }
        }

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        private void button1_Click(object sender, EventArgs e)
        {
            insertaRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }

        //Consulta SQL para borrar filas de una tabla de la base de datos.
        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Tipo_Proyecto WHERE idTipo_proyecto = " + tablaTipo.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        private void btnElimina_Click(object sender, EventArgs e)
        {
            eliminarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }

        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Tipo_Proyecto " +
                "SET nombre_proyecto = '" + textBox1.Text + "', precio = " + textBox2.Text + " " +
                    "WHERE idTipo_proyecto = " + tablaTipo.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        private void btnModificar_Click(object sender, EventArgs e)
        {
            modificarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }

        //Función DataGridWiew, al hacer clic sobre los datos del datagridview se autocompletará los textbox con dichos datos
        private void tablaTipo_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

            textBox1.Text = tablaTipo.CurrentRow.Cells[1].Value.ToString();
            textBox2.Text = tablaTipo.CurrentRow.Cells[2].Value.ToString();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        //Funcion para regresar al fromulario proyecto.
        private void Tipo_Proyecto_FormClosed(object sender, FormClosedEventArgs e)
        {
            Proyecto p = new Proyecto();
            p.Show();
        }
    }
}

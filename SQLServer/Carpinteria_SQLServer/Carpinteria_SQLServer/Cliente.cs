using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace Carpinteria_SQLServer
{
    public partial class Cliente : Form
    {

        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection("Server=CESARMEDELLIN\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
        string fecha;

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Cliente()
        {
            InitializeComponent();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = false;
            connectaBD();
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
            string query = string.Concat("SELECT * FROM Persona.Cliente");

            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }

        //Consulta SQL para insertar filas en una tabla de la base de datos.
        public void insertaRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Persona.Cliente" +
                    "(nombre,telefono,direccion,correo,fecha_registro,num_pedidos)" +
                    " VALUES ('" + textBox1.Text + "'," + textBox2.Text + ",'" + textBox3.Text + "','" + textBox4.Text + "', '" + fecha + "'," + textBox5.Text + ")";
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch(Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        private void button1_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            insertaRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = false;
        }

        //Consulta SQL para borrar filas de una tabla de la base de datos.
        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Persona.Cliente WHERE idCliente = " + dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand( consulta, conexion);
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
        private void button3_Click(object sender, EventArgs e)
        {
            eliminarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = false;
        }

        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Persona.Cliente " +
                    "SET nombre = '" + textBox1.Text + "', telefono = " + textBox2.Text + ", direccion = '" + textBox3.Text + "', correo = '" + textBox4.Text + "', fecha_registro = '" + fecha + "', num_pedidos = " + textBox5.Text + "" +
                    "WHERE idCliente = " + dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch(Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
        private void button2_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            modificarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            dateTimePicker1.Value= DateTime.Now;
            dateTimePicker1.Enabled= false;
        }


        //Función DataGridWiew, al hacer clic sobre los datos del datagridview se autocompletará los textbox con dichos datos
        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            textBox1.Text = dataGridView1.CurrentRow.Cells[1].Value.ToString();
            textBox2.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();
            textBox3.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
            textBox4.Text = dataGridView1.CurrentRow.Cells[4].Value.ToString();
            textBox5.Text = dataGridView1.CurrentRow.Cells[6].Value.ToString();
            dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[5].Value.ToString();
            dateTimePicker1.Enabled = true;
        }
        private void Cliente_Load(object sender, EventArgs e)
        {
            
        }

        private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
        {

        }
    }
}

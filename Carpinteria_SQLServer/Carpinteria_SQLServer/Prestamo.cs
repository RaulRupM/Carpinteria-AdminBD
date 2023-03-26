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
    public partial class Prestamo : Form
    {
        SqlConnection conexion = new SqlConnection("Server=DESKTOP-P986TH4\\SQLEXPRESS;" + "Database=Carpinteria;" + "Integrated Security=true;");
        string fecha;
        string fecha2;

        String idherr;

        DataTable empleado = new DataTable();
        DataTable herramienta = new DataTable();

        public Prestamo()
        {
            InitializeComponent();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
            dateTimePicker1.Visible = false;
            dateTimePicker2.Visible = false;
            comboBox1.Enabled = false;
            connectaBD();
            HerramientaBusca();
        }

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

        public void muestra()
        {
            string query = string.Concat("SELECT * FROM Proyecto.Prestamo");

            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;

            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }

        public void HerramientaBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Proyecto.Herramienta");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(herramienta);
                conexion.Close();

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Herramienta no encontrada");
            }

            for (int i = 0; i <= herramienta.Rows.Count - 1; i++)
                comboBox1.Items.Add(herramienta.Rows[i]["nombre"].ToString());

        }

        public void insertaRegistro()
        {
            
            string emp = empleado.Rows[0]["idEmpleado"].ToString();
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Prestamo" +
                    "(id_empleado,id_herramienta,fecha_prestamo,fecha_devolucion)" +
                    " VALUES (" + emp+ "," + idherr + ",'" + fecha + "', '" + "Pendiente" + "')";
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

        private void button1_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            insertaRegistro();
            connectaBD();
            textBox4.Clear();
            comboBox1.SelectedIndex = -1;
            comboBox1.Enabled = false;
            dateTimePicker1.Visible = false;
            dateTimePicker1.Value = DateTime.Now;
        }

        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Prestamo " +
                    "WHERE id_empleado = " + dataGridView1.CurrentRow.Cells[0].Value+ " AND " +
                    "id_herramienta = " + dataGridView1.CurrentRow.Cells[1].Value + " AND " +
                    "fecha_prestamo = '" + dataGridView1.CurrentRow.Cells[2].Value + "' AND " +
                    "fecha_devolucion = '"+ dataGridView1.CurrentRow.Cells[3].Value+"'";
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


        private void button3_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow.Cells[3].Value.ToString() != "Pendiente")
            {
                eliminarRegistro();
                connectaBD();
                textBox4.Clear();
                comboBox1.SelectedIndex = -1;
                comboBox1.Enabled = false;
                dateTimePicker1.Visible = false;
                dateTimePicker2.Visible = false;
                dateTimePicker1.Value = DateTime.Now;
                dateTimePicker2.Value = DateTime.Now;
            }
            else
            {
                MessageBox.Show("Herrameinta no se a entregado, Favor de entregar la herramienta faltante");
                textBox4.Clear();
                textBox4.Enabled = true;
                comboBox1.SelectedIndex = -1;
                comboBox1.Enabled = false;
                dateTimePicker1.Visible = false;
                dateTimePicker2.Visible = false;
                dateTimePicker1.Value = DateTime.Now;
                dateTimePicker2.Value = DateTime.Now;

            }
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Prestamo " +
                    "SET fecha_devolucion = '" + fecha2 + "'" +
                    "WHERE id_empleado = " + dataGridView1.CurrentRow.Cells[0].Value + " AND " +
                    "id_herramienta = " + dataGridView1.CurrentRow.Cells[1].Value + " AND " +
                    "fecha_prestamo = '" + dataGridView1.CurrentRow.Cells[2].Value + "' AND " +
                    "fecha_devolucion = '" + dataGridView1.CurrentRow.Cells[3].Value + "'";
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

        private void button2_Click(object sender, EventArgs e)
        {
            fecha2 = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            modificarRegistro();
            connectaBD();
            textBox4.Clear();
            textBox4.Enabled = true;
            comboBox1.Enabled = false;
            dateTimePicker1.Visible = false;
            dateTimePicker2.Visible = false;
            dateTimePicker1.Value = DateTime.Now;
        }

        private void Prestamo_Load(object sender, EventArgs e)
        {

        }

        private void button4_Click(object sender, EventArgs e)
        {
            String nombre = textBox4.Text;
            try
            {
                empleado.Clear();
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Persona.Empleado" +
                " WHERE Empleado.nombre = '" + nombre + "'");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(empleado);
                conexion.Close();
                MessageBox.Show("Se encontro el empleado: \n" + empleado.Rows[0]["nombre"].ToString());
                comboBox1.Enabled = true;
                dateTimePicker1.Visible = true;

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Empleado no encontrado");
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (dataGridView1.CurrentRow.Cells[3].Value.ToString() == "Pendiente")
            {

                textBox4.Enabled = false;
                dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();
                dateTimePicker1.Enabled = false;
                dateTimePicker1.Visible = false;
                dateTimePicker2.Enabled = false;
                dateTimePicker2.Visible = true;
                dateTimePicker2.Value = DateTime.Now;

                comboBox1.Enabled = false;
            }
            else
            {
                MessageBox.Show("Herramienta entregado");
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedIndex != -1)
                idherr = herramienta.Rows[comboBox1.SelectedIndex]["idHerramienta"].ToString();
        }
    }
}

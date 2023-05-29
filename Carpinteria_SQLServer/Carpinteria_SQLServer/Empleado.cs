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
    public partial class Empleado : Form
    {

        SqlConnection conexion = new SqlConnection("Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
        string fecha;
        int antiguedad = 0;

        public Empleado()
        {
            InitializeComponent();
            dateTimePicker1.Value = DateTime.Now;
            comboBox2.Items.Add("Empleado");
            comboBox2.Items.Add("Lider");
            comboBox2.Sorted = true;
            dateTimePicker1.Enabled = false;
            textBox6.Text = antiguedad.ToString();
            textBox6.Enabled = false;
            conectaBD();
        }

        public int conectaBD()
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
            string query = string.Concat("SELECT * FROM Persona.Empleado");
            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }



        private void Empleado_Load(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            insertarRegistro();
            conectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            textBox6.Text = antiguedad.ToString();
            comboBox2.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = false;
            textBox6.Enabled = false;


        }

        public void insertarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Persona.Empleado" +
                    "(tipo_empleado,nombre,telefono,direccion,sueldo,empleado_desde,antiguedad,num_proyectos)"+ 
                    "VALUES ('"+ comboBox2.SelectedItem +"','"+ textBox1.Text +"',"+ textBox2.Text +",'"+ textBox3.Text +"','"+ textBox4.Text +"','"+ fecha +"',"+ antiguedad +","+ textBox5.Text +")";
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                cmd.ExecuteNonQuery();
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
            eliminarRegistro();
            conectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            textBox6.Text = antiguedad.ToString();
            comboBox2.SelectedIndex= -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled=false;
            textBox6.Enabled = false;
        }

        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Persona.Empleado WHERE idEmpleado =" + dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                cmd.ExecuteNonQuery();
                conexion.Close();
            }
            catch(Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            modificaRegistro();
            conectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox3.Clear();
            textBox4.Clear();
            textBox5.Clear();
            textBox6.Text = antiguedad.ToString();
            comboBox2.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = false;
            textBox6.Enabled=false;

        }

        public void modificaRegistro()
        {
            try 
            { 
                conexion.Open();
                string consulta = "UPDATE Persona.Empleado" +
                    " SET tipo_empleado = '"+ comboBox2.SelectedItem +"', nombre='"+ textBox1.Text +"', telefono="+textBox2.Text+", direccion= '"+textBox3.Text+"', sueldo='"+textBox4.Text+"', empleado_desde='"+fecha+"', antiguedad="+textBox6.Text+", num_proyectos="+textBox5.Text+""+
                    " WHERE idEmpleado="+ dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                cmd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            comboBox2.Text = dataGridView1.CurrentRow.Cells[1].Value.ToString();
            textBox1.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();
            textBox2.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
            textBox3.Text = dataGridView1.CurrentRow.Cells[4].Value.ToString();
            textBox4.Text = dataGridView1.CurrentRow.Cells[5].Value.ToString();
            textBox5.Text = dataGridView1.CurrentRow.Cells[8].Value.ToString();
            textBox6.Text = dataGridView1.CurrentRow.Cells[7].Value.ToString();
            dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[6].Value.ToString();
            dateTimePicker1.Enabled = true;
        }

        private void button4_Click(object sender, EventArgs e)
        {
            Empleado_Proyecto empP = new Empleado_Proyecto();
            empP.Show();
            this.Close();
        }
    }
}

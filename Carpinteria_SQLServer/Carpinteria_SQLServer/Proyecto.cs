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
    public partial class Proyecto : Form
    {
        SqlConnection conexion = new SqlConnection("Server=DESKTOP-P986TH4\\SQLEXPRESS;" + "Database=Carpinteria;" + "Integrated Security=true;");
        string fecha;
        String fecha2;
        DataTable cliente = new DataTable();
        DataTable empleado = new DataTable();
        String idmpleado;
        public Proyecto()
        {
            InitializeComponent();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
            connectaBD();
            LiderBusca();
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
            string query = string.Concat("SELECT p.idProyecto,CONCAT(e.nombre,'-',e.antiguedad) AS 'id_emp_supervisor' ,CONCAT(c.nombre,'-',c.correo) AS 'idCliente', p.nombre_proyecto, p.estado, p.fecha_entrega, p.fecha_estimada, p.descuento, p.Total" +
                " FROM Proyecto.Proyecto p " +
                "INNER JOIN Persona.Cliente c ON p.idCliente = c.idCliente " +
                "INNER JOIN Persona.Empleado e ON p.id_emp_supervisor = e.idEmpleado");

            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;
            
            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }

        public void LiderBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Persona.Empleado" +
                " WHERE tipo_empleado = 'Lider'");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(empleado);
                conexion.Close();                

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Empleado no encontrado");
            }

            for(int i = 0; i <= empleado.Rows.Count-1; i++)
            comboBox1.Items.Add(empleado.Rows[i]["nombre"].ToString());

        }

        public void insertaRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Proyecto" +
                    "(id_emp_supervisor,idCliente,nombre_proyecto,estado,fecha_estimada,fecha_entrega,descuento,Total)" +
                    " VALUES ("+ idmpleado+ ","+ cliente.Rows[0]["idCliente"].ToString() +",'" + textBox1.Text + "','" + textBox2.Text + "','" + fecha + "', '" + fecha2 + "'," + textBox4.Text + "," + textBox5.Text + ")";
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
            fecha2 = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            insertaRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox4.Clear();
            textBox5.Clear();
            comboBox1.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
            //insertar_Empleadoproyec();
        }

        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Proyecto WHERE idProyecto = " + dataGridView1.CurrentRow.Cells[0].Value;
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
            eliminarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox4.Clear();
            textBox5.Clear();
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
            //dateTimePicker1.Enabled = false;
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Proyecto " +
                    "SET nombre_proyecto = '" + textBox1.Text + "', estado = '" + textBox2.Text + "', fecha_estimada = '" + fecha + "', fecha_entrega = '" + fecha2 + "', descuento = " + textBox4.Text + ", Total = "+ textBox5.Text +"" +
                    "WHERE idProyecto = " + dataGridView1.CurrentRow.Cells[0].Value;
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
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            fecha2 = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            modificarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
            textBox4.Clear();
            textBox5.Clear();
            comboBox1.Enabled = true;
            textBox3.Enabled = true;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
        }

        private void Proyecto_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            textBox1.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
            textBox2.Text = dataGridView1.CurrentRow.Cells[4].Value.ToString();
            textBox4.Text = dataGridView1.CurrentRow.Cells[7].Value.ToString();
            textBox5.Text = dataGridView1.CurrentRow.Cells[8].Value.ToString();
            dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[5].Value.ToString();
            dateTimePicker2.Text = dataGridView1.CurrentRow.Cells[6].Value.ToString();
            textBox3.Enabled = false;
            comboBox1.Enabled = false;


        }

        private void button4_Click(object sender, EventArgs e)
        {
            String nombre = textBox3.Text;
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Persona.Cliente" +
                " WHERE Cliente.nombre = '"+nombre+"'");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(cliente);
                conexion.Close();
                MessageBox.Show("Se encontro el cliente: \n"+ cliente.Rows[0]["nombre"].ToString());
                textBox3.Enabled = false;

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Cliente no encontrado");
            }

}

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            //idmpleado = comboBox1.SelectedIndex;
            if(comboBox1.SelectedIndex != -1)
            idmpleado = empleado.Rows[comboBox1.SelectedIndex]["idEmpleado"].ToString();
        }
    }
}

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
    public partial class Empleado_Proyecto : Form
    {

        SqlConnection conexion = new SqlConnection("Server=CESARMEDELLIN\\SQLEXPRESS;" + "Database=Carpinteria;" + "Integrated Security=true;");

        DataTable empleados = new DataTable();
        DataTable proyectos = new DataTable();

        String idempl;
        String idproy;
        String idviejoempl;
        String idviejoproy;
        bool bandlider = false;

        public Empleado_Proyecto()
        {
            InitializeComponent();
            conectaBD();
            EmpleadoBusca();
            ProyectoBusca();
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
            string query = string.Concat("SELECT  CONCAT(e.nombre, '-', e.antiguedad) AS 'Empleado', CONCAT(t.nombre_proyecto, '-', p.fecha_estimada) AS 'Proyecto', ep.actividad AS 'Actividad', ep.comision AS 'Comisión'" +
                " FROM Proyecto.Empleado_Proyecto ep " +
                "INNER JOIN Persona.Empleado e ON ep.id_empleado = e.idEmpleado " +
                "INNER JOIN Proyecto.Proyecto p ON ep.id_proyecto = p.idProyecto " +
                "INNER JOIN Proyecto.Tipo_Proyecto t ON t.idTipo_proyecto = p.idTipo_proyecto ");
            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }

        public void EmpleadoBusca()
        {
            try
            {
                conexion.Open();
                if (bandlider == false)
                {
                    string consulta = string.Concat("SELECT *" +
                    " FROM Persona.Empleado WHERE tipo_empleado = 'Empleado'");
                    SqlCommand cmd = new SqlCommand(consulta, conexion);
                    SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                    adapter.Fill(empleados);
                }
                else
                {
                    string consulta = string.Concat("SELECT *" +
                    " FROM Persona.Empleado WHERE tipo_empleado = 'Lider'");
                    SqlCommand cmd = new SqlCommand(consulta, conexion);
                    SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                    adapter.Fill(empleados);
                }
                conexion.Close();

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("No se encuentran empleados");
            }

            for (int i = 0; i <= empleados.Rows.Count - 1; i++)
                comboBox1.Items.Add(empleados.Rows[i]["nombre"].ToString());

        }
        public void ProyectoBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT p.idProyecto, t.nombre_proyecto" +
                " FROM Proyecto.Proyecto p " +
                "INNER JOIN Proyecto.Tipo_Proyecto t ON p.idTipo_proyecto = t.idTipo_proyecto ");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(proyectos);
                conexion.Close();

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("No se encuentran proyectos");
            }

            for (int i = 0; i <= proyectos.Rows.Count - 1; i++)
                comboBox2.Items.Add(proyectos.Rows[i]["nombre_proyecto"].ToString());

        }

        public void insertaRegistro()
        {

            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Empleado_Proyecto" +
                    "(id_empleado,id_proyecto,actividad,comision)" +
                    " VALUES (" + idempl + "," + idproy + ",'" + textBox1.Text + "', '" + textBox2.Text + "')";
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error, informacion repetida");
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            insertaRegistro();
            conectaBD();
            comboBox1.SelectedIndex = -1;
            comboBox2.SelectedIndex = -1;
            comboBox2.Enabled = true;
            textBox1.Clear();
            textBox2.Clear();
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
               
                string consulta = "UPDATE Proyecto.Empleado_Proyecto " +
                "SET id_empleado = " + idempl + ", id_proyecto = " + idproy + ", actividad = '" + textBox1.Text + "', comision = " + textBox2.Text + "" +
                " WHERE id_empleado = " + idviejoempl + " AND " +
                "id_proyecto = " + idviejoproy + "";

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
            modificarRegistro();
            conectaBD();
            textBox1.Clear();
            textBox1.Enabled = true;
            textBox2.Clear();
            comboBox2.Enabled = true;
            comboBox1.Enabled = true;
            comboBox1.SelectedIndex = -1;
            comboBox2.SelectedIndex = -1;

            empleados.Rows.Clear();
            comboBox1.Items.Clear();
            bandlider = false;
            EmpleadoBusca();
        }

        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Empleado_Proyecto " +
                    "WHERE id_empleado = " + idempl + " AND " +
                    "id_proyecto = " + idproy + "";
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
            if (dataGridView1.CurrentRow.Cells[2].Value.ToString() != "Supervisar")
            {
                eliminarRegistro();
                conectaBD();
                textBox1.Clear();
                textBox2.Clear();
                comboBox1.SelectedIndex = -1;
                comboBox2.SelectedIndex = -1;
            }
            else
            {
                textBox1.Clear();
                textBox2.Clear();
                comboBox1.SelectedIndex = -1;
                comboBox2.SelectedIndex = -1;
                MessageBox.Show("No se puede eliminar a un supervisor");
            }
            comboBox1.Enabled = true;
            comboBox2.Enabled = true;
            textBox1.Enabled = true;

            empleados.Rows.Clear();
            comboBox1.Items.Clear();
            bandlider = false;
            EmpleadoBusca();

        }



        private void Form4_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            for (int i = 0; i <= proyectos.Rows.Count - 1; i++)
            {
                string[] id = dataGridView1.CurrentRow.Cells[1].Value.ToString().Split('-');
                if (proyectos.Rows[i]["nombre_proyecto"].ToString() == id[0])
                {
                    comboBox2.SelectedIndex = i;
                    idviejoproy = proyectos.Rows[i]["idProyecto"].ToString();
                }
            }

            if (dataGridView1.CurrentRow.Cells[2].Value.ToString() == "Supervisar")
            {
                //MessageBox.Show("Modificar Supervisor hacerlo desde la tabla Proyecto");
                empleados.Rows.Clear();
                comboBox1.Items.Clear();
                bandlider = true;
                comboBox1.Enabled = false;
                comboBox2.Enabled = false;
                textBox1.Enabled = false;
                EmpleadoBusca();

                for (int i = 0; i <= empleados.Rows.Count - 1; i++)
                {
                    String[] id = dataGridView1.CurrentRow.Cells[0].Value.ToString().Split('-');
                    if (empleados.Rows[i]["nombre"].ToString() == id[0])
                    {
                        comboBox1.SelectedIndex = i;
                        idviejoempl = empleados.Rows[i]["idEmpleado"].ToString();
                    }
                }
            }
            else
            {
                empleados.Rows.Clear();
                comboBox1.Items.Clear();
                bandlider = false;
                comboBox1.Enabled = true;
                comboBox2.Enabled = true;
                textBox1.Enabled = true;
                EmpleadoBusca();

                for (int i = 0; i <= empleados.Rows.Count - 1; i++)
                {
                    String[] id = dataGridView1.CurrentRow.Cells[0].Value.ToString().Split('-');
                    if (empleados.Rows[i]["nombre"].ToString() == id[0])
                    {
                        comboBox1.SelectedIndex = i;
                        idviejoempl = empleados.Rows[i]["idEmpleado"].ToString();
                    }
                }


            }

            textBox2.Enabled = true;

            textBox1.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();
            textBox2.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedIndex != -1)
                idempl = empleados.Rows[comboBox1.SelectedIndex]["idEmpleado"].ToString();
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox2.SelectedIndex != -1)
                idproy = proyectos.Rows[comboBox2.SelectedIndex]["idProyecto"].ToString();
        }

        

        private void Empleado_Proyecto_FormClosed(object sender, FormClosedEventArgs e)
        {
            Empleado emp = new Empleado();
            emp.Show();
        }
    }
}

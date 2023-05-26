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
        //La siguiente variable se utiliza para la conexión con la base de datos, por lo tanto, es utilizada en todas las clases:
        SqlConnection conexion = new SqlConnection("Server=CESARMEDELLIN\\SQLEXPRESS;" + "Database=Carpinteria;" + "Integrated Security=true;");

        //Variables para obtener una copia de nuestra tabla de la base de datos. 
        DataTable empleados = new DataTable();
        DataTable proyectos = new DataTable();

        String idempl;
        String idproy;
        String idviejoempl;
        String idviejoproy;
        bool bandlider = false;

        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Empleado_Proyecto()
        {
            InitializeComponent();
            conectaBD();
            EmpleadoBusca();
            ProyectoBusca();
        }

        //Conexion a la base de datos.
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

        //Consulta SQL para mostrar información de una tabla de la base de datos.
        public void muestra()
        {
            string query = string.Concat("SELECT  CONCAT(e.nombre, '-', e.antiguedad) AS 'Empleado', CONCAT(p.idProyecto,'-' ,t.nombre_proyecto, '-', p.fecha_estimada) AS 'Proyecto', ep.actividad AS 'Actividad', ep.comision AS 'Comisión'" +
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

        //Función EmpleadoBuscar, donde hacemos una consulta SQL a la tabla Empleado donde obtenemos los
        //datos de un empleado líder especifico. Y dichos datos se guardan en una variable para tener mayor
        //control y accesibilidad a ellos.  
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

        //Función ProyectoBuscar, donde hacemos una consulta SQL a la tabla Proyecto y Tipo_Proyecto donde
        //obtenemos los datos de un proyecto específico. Y dichos datos se guardan en una variable para tener
        //mayor control y accesibilidad a ellos.  
        public void ProyectoBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT p.idProyecto, t.nombre_proyecto, p.fecha_estimada"+
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
            {
                string[] fecha = proyectos.Rows[i]["fecha_estimada"].ToString().Split(' ');
                comboBox2.Items.Add(proyectos.Rows[i]["idProyecto"].ToString() + "-" + proyectos.Rows[i]["nombre_proyecto"].ToString() + "-" + fecha[0]);
            }

        }

        //Consulta SQL para insertar filas en una tabla de la base de datos.
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

        //Función para el botón de insertar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
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

        //Consulta SQL para actualizar información sobre una tabla de la base de datos.
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

        //Función para el botón de modificar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
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


        //Consulta SQL para borrar filas de una tabla de la base de datos.
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

        //Función para el botón de eliminar de nuestro formulario, al hacer clic en el botón se llamará la función siguiente:
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

        //Función DataGridWiew, al hacer clic sobre los datos del datagridview se autocompletará los textbox con dichos datos
        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            for (int i = 0; i <= proyectos.Rows.Count - 1; i++)
            {
                string[] id = dataGridView1.CurrentRow.Cells[1].Value.ToString().Split('-');
                if (proyectos.Rows[i]["nombre_proyecto"].ToString() == id[1])
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

        //La siguiente función nos ayuda a identificar que empleado se esta seleccionando en el comboBox y se guarda esa información. 
        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedIndex != -1)
                idempl = empleados.Rows[comboBox1.SelectedIndex]["idEmpleado"].ToString();
        }

        //La siguiente función nos ayuda a identificar que proyecto se está seleccionando en el comboBox y se guarda esa información. 
        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox2.SelectedIndex != -1)
            {
                idproy = proyectos.Rows[comboBox2.SelectedIndex]["idProyecto"].ToString();
            }
        }

        //Función cerrar ventana, se cierra la ventana actual
        private void Empleado_Proyecto_FormClosed(object sender, FormClosedEventArgs e)
        {
            Empleado emp = new Empleado();
            emp.Show();
        }
    }
}

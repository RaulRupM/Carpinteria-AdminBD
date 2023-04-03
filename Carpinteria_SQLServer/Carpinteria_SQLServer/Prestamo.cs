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
        String idant;
        String idempl;

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
            EmpleadoBusca();
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
            string query = string.Concat("SELECT e.nombre AS 'Nombre empleado', h.nombre AS 'Herramienta', p.fecha_prestamo AS 'Fecha préstamo', p.fecha_devolucion  AS 'Fecha devolución'" +
                " FROM Proyecto.Prestamo p " +
                "INNER JOIN Persona.Empleado e ON p.id_empleado = e.idEmpleado " +
                "INNER JOIN Proyecto.Herramienta h ON p.id_herramienta = h.idHerramienta");

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

        public void EmpleadoBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Persona.Empleado");
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

            for (int i = 0; i <= empleado.Rows.Count - 1; i++)
                comboBox2.Items.Add(empleado.Rows[i]["nombre"].ToString());

        }

        public void insertaRegistro()
        {
            
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Prestamo" +
                    "(id_empleado,id_herramienta,fecha_prestamo,fecha_devolucion)" +
                    " VALUES (" + idempl + "," + idherr + ",'" + fecha + "', '" + "Pendiente" + "')";
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error no se encontro empleado");
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            insertaRegistro();
            connectaBD();
            comboBox1.SelectedIndex = -1;
            comboBox1.Enabled = false;
            comboBox2.SelectedIndex = -1;
            dateTimePicker1.Visible = false;
            dateTimePicker1.Value = DateTime.Now;
        }

        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Prestamo " +
                    "WHERE id_empleado = " + idempl+ " AND " +
                    "id_herramienta = " + idherr + " AND " +
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
            if (dataGridView1.CurrentRow != null)
            {
                if (dataGridView1.CurrentRow.Cells[3].Value.ToString() != "Pendiente")
                {
                    eliminarRegistro();
                    connectaBD();
                    comboBox1.SelectedIndex = -1;
                    comboBox1.Enabled = false;
                    comboBox2.SelectedIndex = -1;
                    comboBox2.Enabled = true;
                    dateTimePicker1.Visible = false;
                    dateTimePicker2.Visible = false;
                    dateTimePicker1.Value = DateTime.Now;
                    dateTimePicker2.Value = DateTime.Now;
                }
                else
                {
                    MessageBox.Show("Herrameinta no se a entregado, Favor de entregar la herramienta faltante");
                  
                    comboBox1.SelectedIndex = -1;
                    comboBox1.Enabled = false;
                    comboBox2.SelectedIndex = -1;
                    comboBox2.Enabled = true;
                    dateTimePicker1.Visible = false;
                    dateTimePicker2.Visible = false;
                    dateTimePicker1.Value = DateTime.Now;
                    dateTimePicker2.Value = DateTime.Now;

                }
            }
            else
            {
                MessageBox.Show("No hay datos seleccionados");
            }
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                if (dataGridView1.CurrentRow != null)
                {
                    string consulta = "UPDATE Proyecto.Prestamo " +
                        "SET id_herramienta = "+ idherr + "" +
                        "WHERE id_empleado = " + idempl + " AND " +
                        "id_herramienta = " + idant + " AND " +
                        "fecha_prestamo = '" + dataGridView1.CurrentRow.Cells[2].Value + "' AND " +
                        "fecha_devolucion = '" + dataGridView1.CurrentRow.Cells[3].Value + "'";

                    SqlCommand comd = new SqlCommand(consulta, conexion);
                    comd.ExecuteNonQuery();
                }
                else
                {
                    MessageBox.Show("No hay datos que modificar");
                }
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
            comboBox2.Enabled = true;
            comboBox1.Enabled = false;
            dateTimePicker1.Visible = false;
            dateTimePicker2.Visible = false;
            dateTimePicker1.Value = DateTime.Now;
            comboBox2.SelectedIndex = -1;
            comboBox1.SelectedIndex = -1;
        }

        private void Prestamo_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            for (int i = 0; i <= empleado.Rows.Count - 1; i++)
                if (empleado.Rows[i]["nombre"].ToString() == dataGridView1.CurrentRow.Cells[0].Value.ToString())
                    comboBox2.SelectedIndex = i;

            for (int i = 0; i <= herramienta.Rows.Count - 1; i++)
                if (herramienta.Rows[i]["nombre"].ToString() == dataGridView1.CurrentRow.Cells[1].Value.ToString())
                {
                    comboBox1.SelectedIndex = i;
                    idant = herramienta.Rows[i]["idHerramienta"].ToString();
                }

            if (dataGridView1.CurrentRow.Cells[3].Value.ToString() == "Pendiente")
            {

                dateTimePicker1.Visible = false;
                dateTimePicker2.Enabled = false;
                dateTimePicker2.Visible = true;
                dateTimePicker2.Value = DateTime.Now;


                comboBox1.Enabled = true;
                comboBox2.Enabled = true;
            }
            else
            {
                dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[2].Value.ToString();
                dateTimePicker2.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
                dateTimePicker1.Visible = true;
                dateTimePicker2.Visible = true;
                dateTimePicker1.Enabled = false;
                dateTimePicker2.Enabled = false;
                comboBox1.Enabled = false;
                comboBox2.Enabled= false;

                MessageBox.Show("Herramienta entregado");
            }
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox1.SelectedIndex != -1)
                idherr = herramienta.Rows[comboBox1.SelectedIndex]["idHerramienta"].ToString();
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox2.SelectedIndex != -1)
            {
                idempl = empleado.Rows[comboBox2.SelectedIndex]["idEmpleado"].ToString();
                comboBox1.Enabled = true;
                dateTimePicker1.Visible = true;
                dateTimePicker1.Enabled = false;
            }

        }

        private void Prestamo_FormClosed(object sender, FormClosedEventArgs e)
        {
            Herramienta herr = new Herramienta();
            herr.Show();
        }

        public void EntregaRegistro()
        {
            try
            {
                conexion.Open();
                if (dataGridView1.CurrentRow != null)
                {
                    string consulta = "UPDATE Proyecto.Prestamo " +
                        "SET fecha_devolucion = '" + fecha2 + "'" +
                        "WHERE id_empleado = " + idempl + " AND " +
                        "id_herramienta = " + idant + " AND " +
                        "fecha_prestamo = '" + dataGridView1.CurrentRow.Cells[2].Value + "'";

                    SqlCommand comd = new SqlCommand(consulta, conexion);
                    comd.ExecuteNonQuery();
                }
                else
                {
                    MessageBox.Show("No hay datos que modificar");
                }
                conexion.Close();

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            fecha2 = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            EntregaRegistro();
            connectaBD();
            comboBox2.Enabled = true;
            comboBox1.Enabled = false;
            dateTimePicker1.Visible = false;
            dateTimePicker2.Visible = false;
            dateTimePicker1.Value = DateTime.Now;
            comboBox2.SelectedIndex = -1;
            comboBox1.SelectedIndex = -1;

        }
    }
}

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
        String idcliente;
        String idmpleadoAnt;
        String idclienteAnt;

        public Proyecto()
        {
            InitializeComponent();
            textBox4.Text = "0";
            textBox2.Text = "Comienzo";
            textBox4.Enabled = false;
            textBox2.Enabled = false;
            dateTimePicker1.Value = DateTime.Now;
            //dateTimePicker2.Value = DateTime.Now;
            dateTimePicker2.Enabled = false;
            connectaBD();
            LiderBusca();
            ClienteBusca();
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
            string query = string.Concat("SELECT p.idProyecto,CONCAT(e.nombre,'-',e.antiguedad) AS 'Empleado supervisor',CONCAT(c.nombre,'-',c.correo) AS 'Cliente', p.nombre_proyecto AS 'Nombre Proyecto', p.estado AS 'Estado', p.fecha_estimada AS 'Fecha Estimada', p.fecha_entrega AS 'Fecha de Entrega', p.descuento AS 'Descuento', p.Total AS 'Total Mano de obra'" +
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

        public void ClienteBusca()
        {
            try
            {
                conexion.Open();
                string consulta = string.Concat("SELECT *" +
                " FROM Persona.Cliente");
                SqlCommand cmd = new SqlCommand(consulta, conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(cmd);
                adapter.Fill(cliente);
                conexion.Close();

            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("Cliente no encontrado");
            }

            for (int i = 0; i <= cliente.Rows.Count - 1; i++)
                comboBox2.Items.Add(cliente.Rows[i]["nombre"].ToString());

        }


        public void insertaRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "INSERT INTO Proyecto.Proyecto" +
                    "(id_emp_supervisor,idCliente,nombre_proyecto,estado,fecha_estimada,fecha_entrega,descuento,Total)" +
                    " VALUES ("+ idmpleado + ","+ idcliente +",'" + textBox1.Text + "',' Comienzo ','" + fecha + "', 'Pendiente'," + textBox4.Text + "," + textBox5.Text + ")";
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
            //fecha2 = 0 + "-" + 0 + "-" + 0;
            insertaRegistro();
            connectaBD();
            textBox1.Clear();
            textBox5.Clear();
            comboBox1.SelectedIndex = -1;
            comboBox2.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
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
                MessageBox.Show("Informacion se esta utilizando en otra tabla");
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            eliminarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox5.Clear();
            textBox4.Text = "0";
            textBox2.Text = "Comienzo";
            textBox2.Enabled = false;
            comboBox2.SelectedIndex = -1;
            comboBox1.SelectedIndex= -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker2.Value = DateTime.Now;
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Proyecto " +
                    "SET nombre_proyecto = '" + textBox1.Text + "', estado = '" + textBox2.Text + "', fecha_estimada = '" + fecha + "', fecha_entrega = 'Pendiente', descuento = " + textBox4.Text + ", Total = "+ textBox5.Text +"" +
                    "WHERE idProyecto = " + dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("La informacion se esta utilizando en la tabla Empleado Proyecto");
            }
        }

        public void EntregaRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Proyecto " +
                    "SET fecha_entrega = '" + fecha2 + "' WHERE idProyecto = " + dataGridView1.CurrentRow.Cells[0].Value;
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("La informacion se esta utilizando en la tabla Empleado Proyecto");
            }
        }

        public void TotalProyecto()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Proyecto" +
                    " SET Total = Total + (SELECT ISNULL(SUM(subtotal),0) FROM Proyecto.InsumoProyecto WHERE idProyecto = "+ dataGridView1.CurrentRow.Cells[0].Value.ToString() + ")" +
                    " FROM Proyecto.Proyecto INNER JOIN Proyecto.InsumoProyecto i" +
                    " ON i.idProyecto = Proyecto.idProyecto AND Proyecto.idProyecto = " + dataGridView1.CurrentRow.Cells[0].Value.ToString() +"";
                SqlCommand comd = new SqlCommand(consulta, conexion);
                comd.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex)
            {
                conexion.Close();
                MessageBox.Show("No hay insumos utilizados en este proyecto");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            fecha = dateTimePicker1.Value.Year + "-" + dateTimePicker1.Value.Month + "-" + dateTimePicker1.Value.Day;
            fecha2 = dateTimePicker2.Value.Year + "-" + dateTimePicker2.Value.Month + "-" + dateTimePicker2.Value.Day;
            modificarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox5.Clear();
            textBox4.Text = "0";
            textBox2.Text = "Comienzo";
            textBox2.Enabled = false;
            comboBox1.Enabled = true;
            comboBox2.Enabled = true;
            comboBox2.SelectedIndex = -1;
            comboBox1.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = true;
            dateTimePicker2.Enabled = false;
        }

        private void Proyecto_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (dataGridView1.CurrentRow.Cells[6].Value.ToString() == "Pendiente")
            {
                string client = "";
                string emple;
                for (int i = 0; i <= cliente.Rows.Count - 1; i++)
                {
                    client = cliente.Rows[i]["nombre"].ToString() + "-" + cliente.Rows[i]["correo"].ToString();
                    if (client == dataGridView1.CurrentRow.Cells[2].Value.ToString())
                    {
                        comboBox2.SelectedIndex = i;
                        idclienteAnt = cliente.Rows[i]["idCliente"].ToString();
                    }
                }
                for (int i = 0; i <= empleado.Rows.Count - 1; i++)
                {
                    emple = empleado.Rows[i]["nombre"].ToString() + "-" + empleado.Rows[i]["antiguedad"].ToString();
                    if (emple == dataGridView1.CurrentRow.Cells[1].Value.ToString())
                    {
                        comboBox1.SelectedIndex = i;
                        idmpleadoAnt = empleado.Rows[i]["idEmpleado"].ToString();
                    }
                }

                textBox1.Text = dataGridView1.CurrentRow.Cells[3].Value.ToString();
                textBox2.Text = dataGridView1.CurrentRow.Cells[4].Value.ToString();
                textBox4.Text = dataGridView1.CurrentRow.Cells[7].Value.ToString();
                textBox5.Text = dataGridView1.CurrentRow.Cells[8].Value.ToString();
                dateTimePicker1.Text = dataGridView1.CurrentRow.Cells[5].Value.ToString();
                dateTimePicker2.Value = DateTime.Now;
                comboBox2.Enabled = false;
                comboBox1.Enabled = false;
                textBox2.Enabled = true;
            }
            else
            {
                MessageBox.Show("Proyecto terminado!!!!");
            }
            

        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            //idmpleado = comboBox1.SelectedIndex;
            if(comboBox1.SelectedIndex != -1)
            idmpleado = empleado.Rows[comboBox1.SelectedIndex]["idEmpleado"].ToString();
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (comboBox2.SelectedIndex != -1)
                idcliente = cliente.Rows[comboBox2.SelectedIndex]["idCliente"].ToString();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            fecha2 = dateTimePicker2.Value.Year + "-" + dateTimePicker2.Value.Month + "-" + dateTimePicker2.Value.Day;
            EntregaRegistro();
            TotalProyecto();
            connectaBD();
            textBox1.Clear();
            textBox5.Clear();
            textBox4.Text = "0";
            textBox2.Text = "Comienzo";
            textBox2.Enabled = false;
            comboBox1.Enabled = true;
            comboBox2.Enabled = true;
            comboBox2.SelectedIndex = -1;
            dateTimePicker1.Value = DateTime.Now;
            dateTimePicker1.Enabled = true;
            dateTimePicker2.Enabled = false;
        }

        private void button5_Click(object sender, EventArgs e)
        {
            InsumosProyecto insproy = new InsumosProyecto();
            insproy.Show();
        }

        private void dataGridView1_CellContentClick_1(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}

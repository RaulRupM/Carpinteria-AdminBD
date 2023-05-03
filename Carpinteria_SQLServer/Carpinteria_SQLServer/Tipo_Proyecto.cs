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
        SqlConnection conexion = new SqlConnection($"Server=CESARMEDELLIN\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
        public Tipo_Proyecto()
        {
            InitializeComponent();
            connectaBD();
        }

        private void Tipo_Proyecto_Load(object sender, EventArgs e)
        {

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
            string query = string.Concat("SELECT * FROM Proyecto.Tipo_Proyecto");

            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            tablaTipo.DataSource = null;
            tablaTipo.DataSource = registro;
            tablaTipo.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }


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
                MessageBox.Show("Error de conexion, " + ex.Message);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            insertaRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }


        public void eliminarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "DELETE FROM Proyecto.Tipo_Proyecto WHERE id_Tipo_Proyecto = " + tablaTipo.CurrentRow.Cells[0].Value;
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

        private void btnElimina_Click(object sender, EventArgs e)
        {
            eliminarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }

        public void modificarRegistro()
        {
            try
            {
                conexion.Open();
                string consulta = "UPDATE Proyecto.Tipo_Proyecto " +
                "SET nombre_proyecto = '" + textBox1.Text + "', precio = " + textBox2.Text + " " +
                    "WHERE id_Tipo_Proyecto = " + tablaTipo.CurrentRow.Cells[0].Value;
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

        private void btnModificar_Click(object sender, EventArgs e)
        {
            modificarRegistro();
            connectaBD();
            textBox1.Clear();
            textBox2.Clear();
        }

        private void tablaTipo_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

            textBox1.Text = tablaTipo.CurrentRow.Cells[1].Value.ToString();
            textBox2.Text = tablaTipo.CurrentRow.Cells[2].Value.ToString();
        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}

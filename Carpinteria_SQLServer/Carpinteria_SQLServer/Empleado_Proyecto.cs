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

        SqlConnection conexion = new SqlConnection("Server=DESKTOP-P986TH4\\SQLEXPRESS;" + "Database=Carpinteria;" + "Integrated Security=true;");

        public Empleado_Proyecto()
        {
            InitializeComponent();
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
            string query = string.Concat("SELECT * FROM Proyecto.Empleado_Proyecto");
            SqlCommand cmd = new SqlCommand(query, conexion);
            SqlDataAdapter adapter = new SqlDataAdapter(cmd);
            DataTable registro = new DataTable();
            adapter.Fill(registro);
            dataGridView1.DataSource = null;
            dataGridView1.DataSource = registro;
            dataGridView1.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;

        }


        private void Form4_Load(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}

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
using TextBox = System.Windows.Forms.TextBox;

namespace Carpinteria_SQLServer
{
    public partial class Insumo : Form
    {
		SqlConnection conexion = new SqlConnection($"Server=FNTSMN3\\SQLEXPRESS;Database=Carpinteria;Integrated Security=true");
		public Insumo()
        {
            InitializeComponent();
            tablaInsumos.SelectionChanged += EventosForms.llenaTextBox;
            muestraDatosTabla();
        }

        private void muestraDatosTabla()
        {
            try
            {
                conexion.Open();
                string query = "SELECT * FROM Proyecto.Insumo";
                SqlCommand comando = new SqlCommand(query,conexion);
                SqlDataAdapter adapter = new SqlDataAdapter(comando);
                DataTable registro = new DataTable();
                adapter.Fill(registro);
                registro.PrimaryKey = new DataColumn[] { registro.Columns["idInsumo"] };
                tablaInsumos.DataSource = registro;
                tablaInsumos.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.AllCells;
                conexion.Close();
            }catch(Exception ex) {
                throw new Exception(ex.Message);
            }
        }

        
        private void insertaInsumo()
        {
            try
            {
                conexion.Open();
                string query = "INSERT INTO Proyecto.Insumo (nombre,cantidad,precio) VALUES (@nombre,@cantidad,@precio)";
                SqlCommand comando = new SqlCommand(query,conexion);
                EventosForms.parametrosForm(comando,this);
                comando.ExecuteNonQuery();
                conexion.Close();
                muestraDatosTabla();
			}catch(Exception ex) { throw new Exception(ex.Message); }
        }

        private void modificaInsumo(int id)
        {
            try
            {
                conexion.Open();
                string query = "UPDATE Proyecto.Insumo SET nombre = @nombre, cantidad = @cantidad, precio = @precio WHERE idInsumo = @id";
                SqlCommand comando = new SqlCommand(query,conexion);
                EventosForms.parametrosForm(comando,this);
                comando.Parameters.Add(new SqlParameter("id", id));
                comando.ExecuteNonQuery();
                conexion.Close();
            }catch(Exception ex) { throw new Exception(ex.Message); }
        }

		private void btnInsertaInsumo_Click(object sender, EventArgs e)
		{
            insertaInsumo();
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}

		private void eliminaInsumo(int id)
        {
            try
            {
                conexion.Open();
                string query = "DELETE Proyecto.Insumo WHERE idInsumo = @id";
                SqlCommand comando = new SqlCommand(query, conexion);
                comando.Parameters.Add(new SqlParameter("id", id));
                comando.ExecuteNonQuery();
                conexion.Close();
            }
            catch (Exception ex) { throw new Exception(ex.Message + 1); }
        }
		private void btnModificaInsumo_Click(object sender, EventArgs e)
		{
            int id = (int)tablaInsumos.SelectedRows[0].Cells["idInsumo"].Value;
            modificaInsumo(id);
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}

		private void btnEliminarInsumo_Click(object sender, EventArgs e)
		{
			int id = (int)tablaInsumos.SelectedRows[0].Cells["idInsumo"].Value;
            eliminaInsumo(id);
            muestraDatosTabla();
            EventosForms.limpiaTextbox(this);
		}
	}
}

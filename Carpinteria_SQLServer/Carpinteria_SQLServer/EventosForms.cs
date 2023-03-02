using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Carpinteria_SQLServer
{
	public static class EventosForms
	{
		
		public static void llenaTextBox(object sender, EventArgs e)
		{
			DataGridView tabla = (DataGridView)sender;
			Form pantalla = tabla.Parent as Form;
			if (tabla.SelectedRows.Count > 0)
			{
				DataGridViewRow filaSeleccionada = tabla.SelectedRows[0];
				foreach (DataGridViewCell celda in filaSeleccionada.Cells)
				{
					if (celda.ColumnIndex != 0)
					{
						Control textBox = pantalla.Controls.OfType<Control>().FirstOrDefault(c => c.Tag?.ToString() == celda.OwningColumn.Name);
						textBox.Text = celda.Value.ToString();
					}
				}
			}
		}

		public  static void parametrosForm(SqlCommand comando, Form pantalla)
		{
			foreach (Control control in pantalla.Controls)
			{
				if (control is TextBox)
				{
					comando.Parameters.Add(new SqlParameter(control.Tag.ToString(), control.Text));
				}
			}
		}

		public static void limpiaTextbox(Form pantalla)
		{
			foreach (Control control  in pantalla.Controls)
			{
				if (control is TextBox)
				{
					TextBox textBox = control as TextBox;
					textBox.Clear();
				}
			}
		}
	}
}

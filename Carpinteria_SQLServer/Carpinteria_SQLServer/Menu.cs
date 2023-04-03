using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Carpinteria_SQLServer
{
    public partial class Menu : Form
    {
        public Menu()
        {
            InitializeComponent();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            Herramienta herr = new Herramienta();
            herr.Show();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Cliente client = new Cliente();
            client.Show();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Empleado empl = new Empleado();
            empl.Show();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Insumo insumo = new Insumo();
            insumo.Show();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            Proyecto proyecto = new Proyecto();
            proyecto.Show();
        }
    }
}

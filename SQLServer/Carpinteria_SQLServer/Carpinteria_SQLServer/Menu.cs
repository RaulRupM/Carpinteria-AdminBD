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
        //Función para inicializar componentes de nuestra clase, al momento de ejecutar nuestra clase esta función es la primera en ser llamada.
        public Menu()
        {
            InitializeComponent();
        }

        //Función del botón Menú, donde accedemos a la clase Herramienta. 
        private void button4_Click(object sender, EventArgs e)
        {
            Herramienta herr = new Herramienta();
            herr.Show();
        }

        //Función del botón Menú, donde accedemos a la clase Cliente. 
        private void button1_Click(object sender, EventArgs e)
        {
            Cliente client = new Cliente();
            client.Show();
        }

        //Función del botón Menú, donde accedemos a la clase Empleado.
        private void button2_Click(object sender, EventArgs e)
        {
            Empleado empl = new Empleado();
            empl.Show();
        }

        //Función del botón Menú, donde accedemos a la clase Insumo.
        private void button3_Click(object sender, EventArgs e)
        {
            Insumo insumo = new Insumo();
            insumo.Show();
        }

        //Función del botón Menú, donde accedemos a la clase Proyecto.
        private void button5_Click(object sender, EventArgs e)
        {
            Proyecto proyecto = new Proyecto();
            proyecto.Show();
        }

        //Función del botón Menú, donde accedemos a la clase Proveedor.
        private void button6_Click(object sender, EventArgs e)
		{
            Proveedor proveedor = new Proveedor();
            proveedor.Show();
		}

        //Función del botón Menú, donde accedemos a la clase Orden.
        private void button7_Click(object sender, EventArgs e)
        {
            Orden orden = new Orden();
            orden.Show();
        }
    }
}

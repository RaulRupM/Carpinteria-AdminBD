package carpinteria;

import java.awt.Component;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

import carpinteria.models.BD;
import carpinteria.models.enums.Privilegio;

public class Controles {
    private JFrame pantalla;

    public Controles(JFrame pantalla) {
        this.pantalla = pantalla;
    }

    public void verificarPrivilegiosYDesactivarControles(BD baseDatos){
        ArrayList<String> privilegios = baseDatos.obtenerPrivilegiosUsuario();
        if(!privilegios.contains(Privilegio.INSERT.toString())){
            desactivarTextBox();
            desactivarTablas();
            desactivarDateChooser();
            desactivarComboBox();
            desactivarBotonPorTexto("(?i).*\\b(Insertar|Inserta|Agrega|Nueva|Agregar|Nuevo)\\b.*");
        }
        if(!privilegios.contains(Privilegio.UPDATE.toString())){
            desactivarTextBox();
            desactivarTablas();
            desactivarComboBox();
            desactivarDateChooser();
            desactivarBotonPorTexto("(?i).*\\b(Actualizar|Actualiza|Modifica|Modificar|Entregar)\\b.*");
        }
        if(!privilegios.contains(Privilegio.DELETE.toString())){
            desactivarBotonPorTexto("(?i).*\\b(Eliminar|Elimina|Borrar|Borra)\\b.*");
        }
    }

    public void desactivarBotonPorTexto(String patron) {
        Pattern pattern = Pattern.compile(patron);

        // Obtener todos los componentes del formulario
        Component[] components = pantalla.getContentPane().getComponents();

        // Iterar sobre los componentes
        for (Component component : components) {
            // Verificar si es un JButton
            if (component instanceof JButton) {
                JButton boton = (JButton) component;
                String textoBoton = boton.getText();

                // Aplicar la expresión regular al texto del botón
                Matcher matcher = pattern.matcher(textoBoton);

                // Si el texto del botón coincide con el patrón, desactivar el botón
                if (matcher.matches()) {
                    boton.setEnabled(false);
                    break;
                }
            }
        }
    }

    public void desactivarTextBox() {
        Component[] componentes = pantalla.getContentPane().getComponents();
        for (Component componente : componentes) {
            if (componente instanceof JTextField) {
                JTextField textBox = (JTextField) componente;
                textBox.setEnabled(false);
            }
        }
    }

    public void desactivarTablas(){
        Component[] componentes = pantalla.getContentPane().getComponents();
        for (Component componente : componentes) {
            if (componente instanceof JTable) {
                JTable tabla = (JTable) componente;
                tabla.setEnabled(false);
            }
        }
    }

    private void desactivarComboBox(){
        Component[] componentes = pantalla.getContentPane().getComponents();
        for (Component componente : componentes) {
            if (componente instanceof JComboBox) {
                JComboBox tabla = (JComboBox) componente;
                tabla.setEnabled(false);
            }
        }
    }

    private void desactivarDateChooser(){
        Component[] componentes = pantalla.getContentPane().getComponents();
        for (Component componente : componentes) {
            if (componente instanceof JDateChooser) {
                JDateChooser tabla = (JDateChooser) componente;
                tabla.setEnabled(false);
            }
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carpinteria;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

import carpinteria.models.BD;

/**
 *
 * @author raul
 */
public class Herramienta extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion;
    public String id;
    public String fecha;

    public Herramienta(BD bd) {
        initComponents();
        setTitle("Herramienta");
        asociarEventosBotones();
        asociarEventosTabla();
        llenarTablaHerramienta();

        Controles controles = new Controles(this);
        controles.verificarPrivilegiosYDesactivarControles(bd);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexion" + e);
        }
        return con;
    }

    private void llenarTablaHerramienta() {
        try {
            conexion = getConexion();
            Statement st = conexion.createStatement();
            String sql = "SELECT * FROM Proyecto.Herramienta ORDER BY idHerramienta ASC";
            String[] columnas = { "ID", "Herramienta", "Tipo", "Estado", "Cantidad Disponible", "Precio" };
            String[] datos = new String[columnas.length];
            ResultSet rs = st.executeQuery(sql);
            DefaultTableModel modelo = new DefaultTableModel(null, columnas);
            while (rs.next()) {
                datos[0] = rs.getString("idHerramienta");
                datos[1] = rs.getString("nombre");
                datos[2] = rs.getString("tipo");
                datos[3] = rs.getString("estado");
                datos[4] = rs.getString("cantidad_disponible");
                datos[5] = rs.getString("precio");
                modelo.addRow(datos);
            }
            jTable1.setModel(modelo);
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            inputPrecio.setText("");
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la tabla herramienta\n" + mensaje,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al obtener los datos de la tabla herramienta\n" + mensaje,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociarEventosBotones() {
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarHerramienta();
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarHerramienta();
            }
        });

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarHerramienta();
            }
        });

    }

    private void asociarEventosTabla() {
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTable1.getSelectedRow();
                id = jTable1.getValueAt(fila, 0).toString();
                jTextField1.setText(jTable1.getValueAt(fila, 1).toString());
                jTextField2.setText(jTable1.getValueAt(fila, 2).toString());
                jTextField3.setText(jTable1.getValueAt(fila, 3).toString());
                inputPrecio.setText(jTable1.getValueAt(fila, 5).toString());
            }
        });
    }

    private void insertarHerramienta() {
        try {
            String dinero = inputPrecio.getText();
            dinero = dinero.replace("$", "").replace(",", "");
            conexion = getConexion();
            String sql = """
                        INSERT INTO Proyecto.Herramienta (nombre, tipo, estado, precio)
                        VALUES (?,?,?,?)
                    """;

            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setString(1, jTextField1.getText());
            pst.setString(2, jTextField2.getText());
            pst.setString(3, jTextField3.getText());
            pst.setBigDecimal(4, new BigDecimal(dinero));
            pst.executeUpdate();
            llenarTablaHerramienta();

        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarHerramienta() {
        try {
            String dinero = inputPrecio.getText();
            dinero = dinero.replace("$", "").replace(",", "");
            conexion = getConexion();
            String sql = """
                    UPDATE Proyecto.Herramienta
                    SET nombre = ?, tipo = ?, estado = ?, precio = ?
                    WHERE idHerramienta = ?
                    """;
            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setString(1, jTextField1.getText());
            pst.setString(2, jTextField2.getText());
            pst.setString(3, jTextField3.getText());
            pst.setBigDecimal(4, new BigDecimal(dinero));
            pst.setBigDecimal(5, new BigDecimal(id));
            pst.executeUpdate();

            llenarTablaHerramienta();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarHerramienta() {
        try {
            conexion = getConexion();
            Statement st = conexion.createStatement();
            String sql = "DELETE FROM Proyecto.Herramienta WHERE idHerramienta = " + id;
            st.executeUpdate(sql);
            llenarTablaHerramienta();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar la herramienta\n" + mensaje, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        inputPrecio = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("Modificar");
        jButton2.setName("btnModificar"); // NOI18N

        jButton3.setText("Insertar");
        jButton3.setName("btnInsertar"); // NOI18N

        jLabel1.setText("Nombre");

        jTextField1.setName("txtBoxNombre"); // NOI18N

        jLabel2.setText("Tipo");

        jTextField2.setName("txtBoxCantidad"); // NOI18N

        jLabel3.setText("Estado");

        jTextField3.setName("textBoxPrecio"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Eliminar");
        jButton1.setName("btnEliminar"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Precio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 572,
                                                Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(43, 43, 43)
                                                                .addComponent(jLabel4,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                102, Short.MAX_VALUE)
                                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                102, Short.MAX_VALUE)
                                                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                102, Short.MAX_VALUE)
                                                        .addComponent(inputPrecio))
                                                .addGap(346, 346, 346))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                .createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 94,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel5)
                                                .addComponent(inputPrecio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35,
                                        Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jButton1ActionPerformed

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField inputPrecio;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}

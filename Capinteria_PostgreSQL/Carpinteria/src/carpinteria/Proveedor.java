/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carpinteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

import carpinteria.models.BD;

/**
 *
 * @author raul
 */
public class Proveedor extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    public String id;

    /**
     * Creates new form Proveedor
     */
    public Proveedor(BD bd) {
        initComponents();
        setTitle("Proveedor");
        asociarEventosBotones();
        asociarEventosTabla();
        llenarTablaProveedor();

        Controles controles = new Controles(this);
        controles.verificarPrivilegiosYDesactivarControles(bd);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = (Connection) DriverManager.getConnection(this.url, this.USER, this.PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
        }
        return con;
    }

    private void llenarTablaProveedor() {
        try {
            conexion = getConexion();
            String[] titulos = { "ID", "Nombre", "Dirección", "Teléfono", "Correo" };
            Statement st = conexion.createStatement();
            String sql = "SELECT * FROM Empresa.Proveedor order by idProveedor asc";
            ResultSet rs = st.executeQuery(sql);
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            tablaProveedor.setModel(model);
            String[] fila = new String[5];
            while (rs.next()) {
                fila[0] = rs.getString("idProveedor");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("direccion");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getString("correo");
                model.addRow(fila);
            }
            limpiar();
            conexion.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al llenar tabla proveedor " + e.getMessage());
        }
    }

    private void insertarProveedor() {
        try {
            conexion = getConexion();
            String nombre = inputNombre.getText();
            String direccion = inputDireccion.getText();
            String telefono = inputTelefono.getText();
            String correo = inputCorreo.getText();
            String sql = "INSERT INTO Empresa.Proveedor (nombre, direccion, telefono, correo) VALUES ('" + nombre
                    + "', '" + direccion + "', '" + telefono + "', '" + correo + "')";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaProveedor();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar proveedor " + mensaje);
        } catch(SQLException ex){
            String mensaje = Excepciones.manejarExcepcionSQL(ex);
            JOptionPane.showMessageDialog(null, "Error al insertar proveedor " + mensaje);
        }
    }

    private void modificarProveedor() {
        try {
            conexion = getConexion();
            String nombre = inputNombre.getText();
            String direccion = inputDireccion.getText();
            String telefono = inputTelefono.getText();
            String correo = inputCorreo.getText();
            String sql = "UPDATE Empresa.Proveedor SET nombre = '" + nombre + "', direccion = '" + direccion
                    + "', telefono = '" + telefono + "', correo = '" + correo + "' WHERE idProveedor = " + id;
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            llenarTablaProveedor();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar proveedor \n" + mensaje);
        } catch(SQLException ex){
            String mensaje = Excepciones.manejarExcepcionSQL(ex);
            JOptionPane.showMessageDialog(null, "Error al modificar proveedor \n" + mensaje);
        }
    }

    private void eliminarProveedor() {
        try {
            conexion = getConexion();
            String sql = "DELETE FROM Empresa.Proveedor WHERE idProveedor = " + id;
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            llenarTablaProveedor();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor " + mensaje);
        } catch(SQLException ex){
            String mensaje = Excepciones.manejarExcepcionSQL(ex);
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor " + mensaje);
        }
    }

    private void asociarEventosBotones() {
        
        btnInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarProveedor();
            }
        });

        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarProveedor();
            }
        });

        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarProveedor();
            }
        });
        
    }

    private void asociarEventosTabla() {
        tablaProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaProveedor.getSelectedRow();
                id = tablaProveedor.getValueAt(fila, 0).toString();
                inputNombre.setText(tablaProveedor.getValueAt(fila, 1).toString());
                inputDireccion.setText(tablaProveedor.getValueAt(fila, 2).toString());
                inputTelefono.setText(tablaProveedor.getValueAt(fila, 3).toString());
                inputCorreo.setText(tablaProveedor.getValueAt(fila, 4).toString());
            }
        });
    }

    private void limpiar() {
        inputNombre.setText("");
        inputDireccion.setText("");
        inputTelefono.setText("");
        inputCorreo.setText("");
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inputNombre = new javax.swing.JTextField();
        inputDireccion = new javax.swing.JTextField();
        inputTelefono = new javax.swing.JTextField();
        inputCorreo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProveedor = new javax.swing.JTable();
        btnModificar = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombre");

        jLabel2.setText("Dirección");

        jLabel3.setText("Teléfono");

        jLabel4.setText("Correo");

        inputNombre.setName("inputNombre"); // NOI18N

        inputDireccion.setName("inputDireccion"); // NOI18N

        inputTelefono.setName("inputTelefono"); // NOI18N

        inputCorreo.setName("inputCorreo"); // NOI18N

        tablaProveedor.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        tablaProveedor.setName("tablaProveedor"); // NOI18N
        jScrollPane1.setViewportView(tablaProveedor);

        btnModificar.setText("Modificar");
        btnModificar.setName("btnModificar"); // NOI18N

        btnInsertar.setText("Insertar");
        btnInsertar.setName("btnInsertar"); // NOI18N

        btnEliminar.setText("Eliminar");
        btnEliminar.setName("btnEliminar"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 121,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(inputNombre)
                                        .addComponent(inputDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 126,
                                                Short.MAX_VALUE)
                                        .addComponent(inputTelefono)
                                        .addComponent(inputCorreo))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 93,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 93,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 93,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(inputNombre, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(inputDireccion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(inputTelefono, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(inputCorreo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 376,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JTextField inputCorreo;
    private javax.swing.JTextField inputDireccion;
    private javax.swing.JTextField inputNombre;
    private javax.swing.JTextField inputTelefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaProveedor;
    // End of variables declaration//GEN-END:variables
}

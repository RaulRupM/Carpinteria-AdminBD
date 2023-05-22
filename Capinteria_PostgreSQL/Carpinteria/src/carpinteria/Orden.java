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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

/**
 *
 * @author raul
 */
public class Orden extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    public String id;
    private Map<String, String> mapaOrdenes = new LinkedHashMap<String, String>();

    private Orden referencia = this;
    /**
     * Creates new form Orden
     */
    public Orden() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        asociarEventosControles();
        llenarTablaOrden();
        llenarComboProveedores();
        btnOrdenarHerramienta.enable(false);
        btnOrdenarInsumo.enable(false);
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = (Connection) DriverManager.getConnection(url, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en la conexion" + e.getMessage());
            // TODO: handle exception
        }
        return con;
    }

    public void insertarOrden () {
        try {
            conexion = getConexion();
            String idProveedor = mapaOrdenes.get(comboProveedores.getSelectedItem().toString()); 
            String fechaOrden = dateChooserOrden.getDate().toString();
            String total = "0";

            String sql = "INSERT INTO Orden.Orden (idProveedor, fechaOrden, total) VALUES ('" + idProveedor + "', '" + fechaOrden + "', '" + total + "')";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar los datos \n" + mensaje);
        }catch (SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar los datos \n" + mensaje);
        }
    }

    public void modificarOrden (){
        try {
            conexion = getConexion();
            String idProveedor = mapaOrdenes.get(comboProveedores.getSelectedItem().toString()); 
            String fechaOrden = dateChooserOrden.getDate().toString();
            String total = "0";

            String sql = "UPDATE Orden.Orden SET idProveedor = '" + idProveedor + "', fechaOrden = '" + fechaOrden + "', total = '" + total + "' WHERE idOrden = '" + id + "'";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar los datos\n" + mensaje);
        }catch (SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar los datos\n" + mensaje);
        }
    }


    public void eliminarOrden (){
        try {
            conexion = getConexion();
            String sql = "DELETE FROM Orden.Orden WHERE idOrden = '" + id + "'";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar los datos\n" + mensaje);
        } catch(SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar los datos\n" + mensaje);
        }
    }

    private void asociarEventosControles (){
        btnNuevaOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertarOrden();
            }
        });

        btnModificarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarOrden();
            }
        });

        btnEliminarOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarOrden();
            }
        });

        btnOrdenarInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Pedido pedido = new Pedido("Insumo", id, referencia);
                pedido.setVisible(true);
            }
        });

        btnOrdenarHerramienta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Pedido pedido = new Pedido("Herramienta", id, referencia);
                pedido.setVisible(true);
            }
        });

        tablaOrdenes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tablaOrdenes.getSelectedRow();
                id = tablaOrdenes.getValueAt(fila, 0).toString();
                btnOrdenarHerramienta.setEnabled(true);
                btnOrdenarInsumo.setEnabled(true);
                comboProveedores.setSelectedItem(tablaOrdenes.getValueAt(fila, 1).toString());
                dateChooserOrden.setDate(java.sql.Date.valueOf(tablaOrdenes.getValueAt(fila, 2).toString()));
            }
        });
    }

    public void limpiar(){
        comboProveedores.setSelectedIndex(0);
        dateChooserOrden.setDate(null);
    }

    public void llenarTablaOrden(){
        try {
            conexion = getConexion();
            String[] titulos = {"ID", "Proveedor", "Fecha", "Total"};
            Statement st = conexion.createStatement();
            String sql = """
                SELECT o.idOrden, p.nombre as Proveedor , o.fechaorden, o.total 
                FROM Orden.Orden o 
                INNER JOIN Empresa.Proveedor p 
                ON o.idproveedor = p.idproveedor 
                ORDER BY idorden ASC
            """;
            ResultSet rs = st.executeQuery(sql);
            DefaultTableModel model = new DefaultTableModel(null, titulos);
            tablaOrdenes.setModel(model);
            String[] fila = new String[4];
            while (rs.next()) {
                fila[0] = rs.getString("idOrden");
                fila[1] = rs.getString("Proveedor");
                fila[2] = rs.getString("fechaOrden");
                fila[3] = rs.getString("total");
                model.addRow(fila);
            }
            conexion.close();
            limpiar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al extraer los datos de la tabla" + e.getMessage());
        }
    }

    private void llenarComboProveedores(){
        try {
            conexion = getConexion();
            String sql = "SELECT * FROM Empresa.Proveedor";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            comboProveedores.removeAllItems();
            mapaOrdenes.clear();
            while (rs.next()) {
                mapaOrdenes.put(rs.getString("nombre"), rs.getString("idProveedor"));                
                comboProveedores.addItem(rs.getString("nombre"));
            }
            conexion.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al extraer los datos de la tabla" + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboProveedores = new javax.swing.JComboBox<>();
        btnNuevaOrden = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaOrdenes = new javax.swing.JTable();
        btnOrdenarHerramienta = new javax.swing.JButton();
        btnOrdenarInsumo = new javax.swing.JButton();
        btnModificarOrden = new javax.swing.JButton();
        btnEliminarOrden = new javax.swing.JButton();
        dateChooserOrden = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Proveedor");

        comboProveedores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnNuevaOrden.setText("Nueva orden");

        tablaOrdenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaOrdenes);

        btnOrdenarHerramienta.setText("Ordenar herramienta");

        btnOrdenarInsumo.setText("Ordenar Insumo");

        btnModificarOrden.setText("Modificar orden");

        btnEliminarOrden.setText("Eliminar orden");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnNuevaOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnModificarOrden)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dateChooserOrden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboProveedores, 0, 179, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnOrdenarHerramienta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnOrdenarInsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboProveedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateChooserOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnOrdenarHerramienta, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnOrdenarInsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(btnNuevaOrden)
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Orden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Orden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Orden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Orden.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Orden().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminarOrden;
    private javax.swing.JButton btnModificarOrden;
    private javax.swing.JButton btnNuevaOrden;
    private javax.swing.JButton btnOrdenarHerramienta;
    private javax.swing.JButton btnOrdenarInsumo;
    private javax.swing.JComboBox<String> comboProveedores;
    private com.toedter.calendar.JDateChooser dateChooserOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaOrdenes;
    // End of variables declaration//GEN-END:variables
}

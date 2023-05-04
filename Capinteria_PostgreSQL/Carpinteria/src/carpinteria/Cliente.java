package carpinteria;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex_
 */
public class Cliente extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    public String id;
    public String fecha;
    
    
    public Cliente() {
        
        initComponents();
        
       
        Date Fecha = new Date();
        
        fechaCliente.setDate(Fecha);
        fechaCliente.enable(false);
        
        muestra();
        
        tablaCliente.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    id=tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 0).toString();
                    txtNombre.setText(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 1).toString());
                    txtTelefono.setText(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 2).toString());
                    txtDireccion.setText(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 3).toString());
                    txtCorreo.setText(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 4).toString());
                    txtPedidos.setText(tablaCliente.getValueAt(tablaCliente.getSelectedRow(), 6).toString());
                }
            }
        
        });
    }
    
    public Connection conectaDB() {
        Connection cone = null;
        try {
            Class.forName("org.postgresql.Driver");
            cone = DriverManager.getConnection(url, USER, PASS);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return cone;
    }
    
    public void inserta() throws SQLException {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "INSERT INTO Persona.Cliente "
                    + "(Nombre_Cliente,Telefono_Cliente,Direccion_Cliente,Correo_Cliente,fecha_registro,num_pedidos)"
                    + "VALUES('" + txtNombre.getText() + "', "
                    + "'" + txtTelefono.getText() + "',"
                    + "'" + txtDireccion.getText() + "', "
                    + "'" + txtCorreo.getText()+ "', " 
                    + "'" + fechaCliente.getDate() + "', "
                    + "'" + txtPedidos.getText() + "'" + ");";
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null , ex);
        }
    }
    
    public void modifica() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
          
             String Query= "UPDATE Persona.Cliente "
                            + "SET Nombre_Cliente = '" + txtNombre.getText()+ "',"
                            + "Telefono_Cliente = '" +txtTelefono.getText() + "',"
                            + "Direccion_Cliente = '" +txtDireccion.getText() + "',"
                            + "Correo_Cliente = '" + txtCorreo.getText() + "',"
                            + "fecha_registro = '" + fechaCliente.getDate() + "',"
                            + "num_pedidos = '" + txtPedidos.getText() + "'"  + " "
                            +"WHERE id_Cliente = " + id ;
              
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
          
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void elimina(){
        
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "DELETE FROM Persona.Cliente WHERE id_Cliente = " + id + "";
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void muestra() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "SELECT * FROM Persona.Cliente ORDER BY id_Cliente ASC";
            String[] datos = new String[8];
            ResultSet columnas = corrida.executeQuery(Query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("id_Cliente");
            model.addColumn("Nombre");
            model.addColumn("Telefono");
            model.addColumn("Dirección");
            model.addColumn("Correo");
            model.addColumn("Fecha De Registro");
            model.addColumn("Número De Pedidos");
            tablaCliente.setModel(model);
            while (columnas.next()) {
                datos[0] = columnas.getString(1);
                datos[1] = columnas.getString(2);
                datos[2] = columnas.getString(3);
                datos[3] = columnas.getString(4);
                datos[4] = columnas.getString(5);
                datos[5] = columnas.getString(6);
                datos[6] = columnas.getString(7);
                model.addRow(datos);
            }
            
            txtNombre.setText("");
            txtTelefono.setText("");
            txtDireccion.setText("");
            txtCorreo.setText("");
            txtPedidos.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnInserta = new javax.swing.JButton();
        btnModifica = new javax.swing.JButton();
        btnElimina = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPedidos = new javax.swing.JTextField();
        fechaCliente = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente");

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaCliente);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Nombre");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Telefono");

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Dirección");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Correo");

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Cliente");

        btnInserta.setText("Inserta");
        btnInserta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertaActionPerformed(evt);
            }
        });

        btnModifica.setText("Modifica");
        btnModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificaActionPerformed(evt);
            }
        });

        btnElimina.setText("Elimina");
        btnElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminaActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Fecha de registro");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Número de pedido");

        txtPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPedidosActionPerformed(evt);
            }
        });

        fechaCliente.setEnabled(false);
        fechaCliente.setName("fechaRegistro"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(379, 379, 379)
                                .addComponent(btnInserta, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnModifica, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnElimina, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fechaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(399, 399, 399)
                        .addComponent(jLabel5)))
                .addContainerGap(228, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(fechaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserta)
                    .addComponent(btnModifica)
                    .addComponent(btnElimina))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void btnInsertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertaActionPerformed
        try {
            inserta();
        } catch (SQLException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        muestra();
    }//GEN-LAST:event_btnInsertaActionPerformed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void txtPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPedidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPedidosActionPerformed

    private void btnModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificaActionPerformed
        // TODO add your handling code here:
        modifica();
        muestra();
    }//GEN-LAST:event_btnModificaActionPerformed

    private void btnEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminaActionPerformed
        // TODO add your handling code here:
        elimina();
        muestra();
    }//GEN-LAST:event_btnEliminaActionPerformed

  
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElimina;
    private javax.swing.JButton btnInserta;
    private javax.swing.JButton btnModifica;
    private com.toedter.calendar.JDateChooser fechaCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPedidos;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carpinteria;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import carpinteria.models.BD;

/**
 *
 * @author alex_
 */
public class Empleado extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
   
    public String id;
    public String fecha;
    public int antiguedad = 0;
    
    public Empleado() {
        initComponents();
        inicializarControles();
    }

    private void inicializarControles(){
        txtNumProyecto.setEnabled(false);
        Date Fecha = new Date();
        
        EmpleadoDesde.setDate(Fecha);
        EmpleadoDesde.enable(false);
        
        Calendar startCal = Calendar.getInstance();
        startCal.set(1995,Calendar.JANUARY,1);
        Date startDate = startCal.getTime();
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = endCal.getTime();
        
        EmpleadoDesde.setDate(Fecha);
        this.EmpleadoDesde.setSelectableDateRange(this.EmpleadoDesde.getDate(),
        this.EmpleadoDesde.getDate());
        
        txtNumProyecto.setEnabled(false);
        muestra();
        
        tablaEmpleado.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    id=tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 0).toString();
                    String puesto = tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 1).toString();
                    ComboBoxPuesto.setSelectedItem(puesto);
                    txtNombre.setText(tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 2).toString());
                    txtTelefono.setText(tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 3).toString());
                    txtDireccion.setText(tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 4).toString());
                    txtSueldo.setText(tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 5).toString());
                    txtNumProyecto.setText(tablaEmpleado.getValueAt(tablaEmpleado.getSelectedRow(), 8).toString());
                    
                     EmpleadoDesde.setSelectableDateRange(startDate,endDate);
                }
            }
        
        });
        
    }
    
    public Empleado(BD bd){
        initComponents();
        inicializarControles();
        Controles controles = new Controles(this);
        controles.verificarPrivilegiosYDesactivarControles(bd);
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
    
    
    public void muestra() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "SELECT * FROM Persona.Empleado ORDER BY id_Empleado ASC";
            String[] datos = new String[10];
            ResultSet columnas = corrida.executeQuery(Query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("id_Empleado");
            model.addColumn("Tipo Empleado");
            model.addColumn("Nombre");
            model.addColumn("Teléfono");
            model.addColumn("Dirección");
            model.addColumn("Sueldo");
            model.addColumn("Empleado Desde");
            model.addColumn("Antigüedad");
            model.addColumn("Número Proyectos");
            tablaEmpleado.setModel(model);
            while (columnas.next()) {
                datos[0] = columnas.getString(1);
                datos[1] = columnas.getString(2);
                datos[2] = columnas.getString(3);
                datos[3] = columnas.getString(4);
                datos[4] = columnas.getString(5);
                datos[5] = columnas.getString(6);
                datos[6] = columnas.getString(7);
                datos[7] = columnas.getString(8);
                datos[8] = columnas.getString(9);
                model.addRow(datos);
            }
            
            txtNombre.setText("");
            txtTelefono.setText("");
            txtDireccion.setText("");
            txtSueldo.setText("");
            txtAntiguedad.setText("0");
            txtNumProyecto.setText("0");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
    
    
    public void inserta() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "INSERT INTO Persona.Empleado "
                    + "(Tipo_Empleado,Nombre_Empleado,Telefono_Empleado,Direccion_Empleado,Sueldo,Empleado_desde,Antiguedad,num_proyectos)"
                    + "VALUES('" + ComboBoxPuesto.getSelectedItem() + "', "
                    + "'" + txtNombre.getText() + "',"
                    + "'" + txtTelefono.getText() + "', "
                    + "'" + txtDireccion.getText()+ "', " 
                    + "" + txtSueldo.getText() + ", "
                    + "'" + EmpleadoDesde.getDate() + "'," 
                    + "" + txtAntiguedad.getText() + ","
                    + "" + txtNumProyecto.getText() + "" + ");";
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null , ex);
        }
    }
    
    public void modifica(){
        
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "UPDATE Persona.Empleado "
                            + "SET Tipo_Empleado = '" + ComboBoxPuesto.getSelectedItem() +"',"
                            + "Nombre_Empleado = '" + txtNombre.getText() + "',"
                            + "Telefono_Empleado = '" +txtTelefono.getText() + "',"
                            + "Direccion_Empleado = '" + txtDireccion.getText() + "',"
                            + "Sueldo = '" + txtSueldo.getText() + "',"
                            + "Empleado_desde = '" + EmpleadoDesde.getDate() + "',"
                            + "Antiguedad = " + antiguedad + ","
                            + "num_proyectos = " + txtNumProyecto.getText() + ""
                            + " WHERE id_Empleado = " + id;
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null , ex);
        }
    }
    
    public void elimina(){
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "DELETE FROM Persona.Empleado WHERE id_Empleado = " + id + "";
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labael = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtSueldo = new javax.swing.JTextField();
        txtAntiguedad = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtNumProyecto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleado = new javax.swing.JTable();
        EmpleadoDesde = new com.toedter.calendar.JDateChooser();
        btnInsertar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        ComboBoxPuesto = new javax.swing.JComboBox();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        setTitle("Empleado");

        jLabel1.setText("Empleado");

        jLabel2.setText("Puesto:");

        jLabel3.setText("Nombre:");

        labael.setText("Teléfono:");

        jLabel5.setText("Dirección:");

        jLabel6.setText("Sueldo:");

        jLabel7.setText("Fecha de registro:");

        jLabel8.setText("Antigüedad:");

        jLabel9.setText("Número de proyecto:");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });

        txtAntiguedad.setText("0");
        txtAntiguedad.setEnabled(false);

        jLabel4.setText("años.");

        txtNumProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumProyectoActionPerformed(evt);
            }
        });

        tablaEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title5", "Title6", "Title7", "Title8", "Title9"
            }
        ));
        jScrollPane1.setViewportView(tablaEmpleado);

        btnInsertar.setText("Insertar");
        btnInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        ComboBoxPuesto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Líder", "Empleado" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSueldo, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
                                    .addComponent(txtDireccion)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(labael)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTelefono))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(ComboBoxPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 363, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnInsertar)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtAntiguedad))
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNumProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(EmpleadoDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(321, 321, 321)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(ComboBoxPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labael)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtSueldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(67, 67, 67))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(EmpleadoDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtAntiguedad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtNumProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInsertar)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar))
                        .addGap(29, 29, 29)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtNumProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumProyectoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumProyectoActionPerformed

    private void btnInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarActionPerformed
       inserta();
       muestra();
    }//GEN-LAST:event_btnInsertarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modifica();
        muestra();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
       elimina();
       muestra();
    }//GEN-LAST:event_btnEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox ComboBoxPuesto;
    private com.toedter.calendar.JDateChooser EmpleadoDesde;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labael;
    private javax.swing.JTable tablaEmpleado;
    private javax.swing.JTextField txtAntiguedad;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumProyecto;
    private javax.swing.JTextField txtSueldo;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}

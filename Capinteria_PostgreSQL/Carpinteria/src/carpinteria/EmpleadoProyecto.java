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
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex_
 */




public class EmpleadoProyecto extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    
    public String id;
    
    public String proyectoNuevo;
    
    public String empleadoNuevo;
    
    String idempleado;
    String idproyecto;
    String[] empleadoLider;
    
    public EmpleadoProyecto() {
        initComponents();
    
        muestra();
        
        comboProyecto.setModel(new DefaultComboBoxModel(obtenNomProyectos()));
        comboEmpleado.setModel(new DefaultComboBoxModel(obtenNomEmpleado()));
        
        
        tablaEmpPro.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                  
                    String nombreL[] = tablaEmpPro.getValueAt(tablaEmpPro.getSelectedRow(),0 ).toString().split("-");
                    empleadoLider = tablaEmpPro.getValueAt(tablaEmpPro.getSelectedRow(),0 ).toString().split("-");
                    comboEmpleado.setSelectedItem(nombreL[0]);
                    
                    
                    
                    String nombreP= tablaEmpPro.getValueAt(tablaEmpPro.getSelectedRow(),1 ).toString();
                    
                    comboProyecto.setSelectedItem(nombreP);
                    
                    txtActividad.setText(tablaEmpPro.getValueAt(tablaEmpPro.getSelectedRow(), 2).toString());
                    txtComision.setText(tablaEmpPro.getValueAt(tablaEmpPro.getSelectedRow(), 3).toString());
                    
                    obtenIdEmpleadoNuevo();
                    obtenIdProyectoNuevo();
                    
                    if("Supervisión".equals(txtActividad.getText())){
                        comboEmpleado.setEnabled(false);
                        comboProyecto.setEnabled(false);
                        txtActividad.setEnabled(false);
                    }else{
                        comboEmpleado.setEnabled(true);
                        comboProyecto.setEnabled(true);
                        txtActividad.setEnabled(true);
                    }
                    
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
        
   public String[] obtenNomProyectos(){
    ArrayList<String> nomProyectos = new ArrayList<>();
    Connection conexion = null;
    ResultSet rs = null;
    
    try {
        conexion = conectaDB();
        Statement corrida = conexion.createStatement();
        String query = ("SELECT CONCAT(p.idProyecto, '-' ,t.nombre_proyecto,'-',p.fecha_estimada) AS NombreProyecto\n" +
"FROM Proyecto.Proyecto p\n" +
"INNER JOIN Proyecto.Tipo_Proyecto t ON p.idTipo_proyecto = t.idTipo_proyecto\n" +
"GROUP BY NombreProyecto ");
       
        
        rs = corrida.executeQuery(query);
        
        while (rs.next()) {
            String nombre = rs.getString("NombreProyecto");
            nomProyectos.add(nombre);
        }
        
        corrida.close();
        conexion.close();  
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return nomProyectos.toArray(new String[nomProyectos.size()]);
}
   
   
   
   public String[] obtenNomEmpleado(){
    ArrayList<String> nomEmpleados = new ArrayList<>();
    Connection conexion = null;
    ResultSet rs = null;
    
    try {
        conexion = conectaDB();
        Statement corrida = conexion.createStatement();
        String query = "SELECT Nombre_Empleado FROM Persona.Empleado WHERE tipo_empleado= 'Empleado' ";
        rs = corrida.executeQuery(query);
        
        while (rs.next()) {
            String nombre = rs.getString("Nombre_Empleado");
            nomEmpleados.add(nombre);
        }
        
        corrida.close();
        conexion.close();  
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return nomEmpleados.toArray(new String[nomEmpleados.size()]);
}
    
    public String obtenerIdEmpleadoPorNombre(String nombreEmpleado) {
        String idEmpleado = null;
        Connection conexion = null;
        ResultSet rs = null;
        
        try {
            conexion = conectaDB();
            Statement statement = conexion.createStatement();
            String query = "SELECT id_Empleado FROM Persona.Empleado WHERE Nombre_Empleado = '" + nombreEmpleado + "'";
            rs = statement.executeQuery(query);

            if (rs.next()) {
                idEmpleado = rs.getString("id_Empleado");
            }

            statement.close();
            conexion.close();  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return idEmpleado;
    }
    
    public void obtenIdEmpleado() {
      if (comboEmpleado.getSelectedIndex() != -1) {
          Object seleccionado = comboEmpleado.getSelectedItem();
          String nombreLider = seleccionado.toString();
          String idLider = obtenerIdEmpleadoPorNombre(nombreLider);
          idempleado = idLider;
      }
    }
    
    public void obtenIdEmpleadoLider(){
          String idLider = obtenerIdEmpleadoPorNombre(empleadoLider[0]);
          idempleado = idLider;
    }
    
    public void obtenIdEmpleadoNuevo() {
      if (comboEmpleado.getSelectedIndex() != -1) {
          Object seleccionado = comboEmpleado.getSelectedItem();
          String nombreLider = seleccionado.toString();
          String idLider = obtenerIdEmpleadoPorNombre(nombreLider);
          empleadoNuevo = idLider;
      }
    }
    
    public String obtenerIdProyectoPorNombre(String nombreTipoProyecto, String nombreEmpleado) {
        String idProyecto = null;
        Connection conexion = null;
        ResultSet rs = null;

        try {
            String idEmpleado = obtenerIdEmpleadoPorNombre(nombreEmpleado);

            conexion = conectaDB();
            Statement statement = conexion.createStatement();
           String query = "SElECT idProyecto FROM Proyecto.Proyecto WHERE idProyecto =" + nombreTipoProyecto;
           
           rs = statement.executeQuery(query);

            if (rs.next()) {
                idProyecto = rs.getString("idProyecto");
            }

            statement.close();
            conexion.close();  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return idProyecto;
    }

    public void obtenIdProyecto() {
        if (comboProyecto.getSelectedIndex() != -1 && comboEmpleado.getSelectedIndex() != -1) {
            String[] proyectoSeleccionado = comboProyecto.getSelectedItem().toString().split("-");
            Object empleadoSeleccionado = comboEmpleado.getSelectedItem();
            String idNProyecto = proyectoSeleccionado[0];
            String nombreEmpleado = empleadoSeleccionado.toString();
            String idProyecto = obtenerIdProyectoPorNombre(idNProyecto, nombreEmpleado);
            idproyecto =idProyecto;
            
        }
    }
    
    
    public void obtenIdProyectoNuevo() {
        if (comboProyecto.getSelectedIndex() != -1 && comboEmpleado.getSelectedIndex() != -1) {
            String[] proyectoSeleccionado = comboProyecto.getSelectedItem().toString().split("-");
            Object empleadoSeleccionado = comboEmpleado.getSelectedItem();
            String nombreProyecto = proyectoSeleccionado[0];
            String nombreEmpleado = empleadoSeleccionado.toString();
            String idProyecto = obtenerIdProyectoPorNombre(nombreProyecto, nombreEmpleado);
            proyectoNuevo =idProyecto;
        }
    }
    
    public void inserta() {
       
        
       obtenIdEmpleado();
       obtenIdProyecto();
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            if("Supervisión".equals(txtActividad.getText())){
                String Mensa = "No se puede inserta a un Empleado Lider";
                JOptionPane.showMessageDialog(null , Mensa);
            } 
            else {
                
                String Query = "INSERT INTO Proyecto.Empleado_Proyecto" +
                    "(id_empleado,id_proyecto,actividad,comision)" +
                    " VALUES (" + idempleado + "," + idproyecto + ",'" + txtActividad.getText() + "', '" + txtComision.getText() + "')"; 
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
                
            }
            
        } catch (Exception ex) {
            String Mensaje = "Error, Informacion repetida";
            JOptionPane.showMessageDialog(null , Mensaje);
        }
    }
    
    public void elimina(){
        
        obtenIdEmpleado();
        obtenIdProyecto();
        
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            if("Supervisión".equals(txtActividad.getText())){
                String Mensa = "No se puede Eliminar a un Empleado Lider";
                JOptionPane.showMessageDialog(null , Mensa);
            } 
            else {
            
                String Query = "DELETE FROM Proyecto.Empleado_Proyecto " +
                    "WHERE id_empleado = " + idempleado + " AND " +
                    "id_proyecto = " + idproyecto + "";
            
                corrida.executeUpdate(Query);
                corrida.close();
                conexion.close();
            }
        }catch(Exception ex){
            String Mensa = "Error al eliminar";
            JOptionPane.showMessageDialog(null , Mensa);
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    
    
    public void modifica(){
        
        obtenIdProyecto();
        obtenIdEmpleado();
        
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            if("Supervisión".equals(txtActividad.getText())){
                
                obtenIdEmpleadoLider();
                 String Query = "UPDATE Proyecto.Empleado_Proyecto "
                    + "SET comision = ' " +txtComision.getText()+" '"
                    + " WHERE id_proyecto = " + proyectoNuevo +" AND id_Empleado = " + idempleado;
                 
                    corrida.executeUpdate(Query);
                    corrida.close();
                    conexion.close();
                
            }else{
                String Query2 = "UPDATE Proyecto.Empleado_Proyecto "
                    + "SET id_proyecto = '" + idproyecto +"',"
                    + "id_empleado = '" + idempleado + "',"
                    + "actividad = ' "+txtActividad.getText()+"',"
                    + "comision = ' " +txtComision.getText()+" '"
                    + " WHERE id_proyecto = " + proyectoNuevo +" AND id_Empleado = " + empleadoNuevo;
            
                    corrida.executeUpdate(Query2);
                    corrida.close();
                    conexion.close();
            }
            
        }catch(Exception ex){
            String Mensaje = "Error, Informacion repetida";
            JOptionPane.showMessageDialog(null , Mensaje);
        }
    }
    
    public void muestra() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = ("SELECT CONCAT(e.Nombre_Empleado,'-',e.antiguedad) AS Nombre_Empleado, CONCAT(p.idProyecto, '-' ,t.nombre_proyecto,'-',p.fecha_estimada) AS NombreProyecto, ep.actividad AS Actividad, ep.comision AS Comisión\n" +
"                 FROM Proyecto.Empleado_Proyecto ep \n" +
"                INNER JOIN Persona.Empleado e ON ep.id_empleado = e.id_Empleado \n" +
"                INNER JOIN Proyecto.Proyecto p ON ep.id_proyecto = p.idProyecto\n" +
"                INNER JOIN Proyecto.Tipo_Proyecto t ON t.idTipo_proyecto = p.idTipo_proyecto");
            String[] datos = new String[4];
            ResultSet columnas = corrida.executeQuery(Query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Empleado");
            model.addColumn("Proyecto");
            model.addColumn("Actividad");
            model.addColumn("Comisión");
            tablaEmpPro.setModel(model);
            while (columnas.next()) {
                datos[0] = columnas.getString(1);
                datos[1] = columnas.getString(2);
                datos[2] = columnas.getString(3);
                datos[3] = columnas.getString(4);
                model.addRow(datos);
            }
            
            txtActividad.setText("");
            txtComision.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        comboEmpleado = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboProyecto = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtActividad = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtComision = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEmpPro = new javax.swing.JTable();
        btnInserta = new javax.swing.JButton();
        btnModifica = new javax.swing.JButton();
        btnElimina = new javax.swing.JButton();

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
        jScrollPane1.setViewportView(jTable1);

        setTitle("EmpleadoProyecto");

        comboEmpleado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("EmpleadoProyecto");

        jLabel2.setText("Empleado");

        jLabel3.setText("Proyecto");

        comboProyecto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Actividad");

        jLabel5.setText("Comisión");

        tablaEmpPro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tablaEmpPro);
        if (tablaEmpPro.getColumnModel().getColumnCount() > 0) {
            tablaEmpPro.getColumnModel().getColumn(1).setMinWidth(100);
        }

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtComision, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtActividad, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnInserta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModifica)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnElimina))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(254, 254, 254)))))
                .addContainerGap(90, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtComision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserta)
                    .addComponent(btnModifica)
                    .addComponent(btnElimina))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertaActionPerformed
        inserta();
        muestra();
    }//GEN-LAST:event_btnInsertaActionPerformed

    private void btnEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminaActionPerformed
        elimina();
        muestra();
    }//GEN-LAST:event_btnEliminaActionPerformed

    private void btnModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificaActionPerformed
       modifica();
       muestra();
    }//GEN-LAST:event_btnModificaActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElimina;
    private javax.swing.JButton btnInserta;
    private javax.swing.JButton btnModifica;
    private javax.swing.JComboBox comboEmpleado;
    private javax.swing.JComboBox comboProyecto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaEmpPro;
    private javax.swing.JTextField txtActividad;
    private javax.swing.JTextField txtComision;
    // End of variables declaration//GEN-END:variables
}

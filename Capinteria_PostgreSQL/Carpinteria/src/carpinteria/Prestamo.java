/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carpinteria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

/**
 *
 * @author raul
 */
public class Prestamo extends javax.swing.JFrame {
    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    private Map<String, String> datosEmpleado = new LinkedHashMap<String, String>();
    private Map<String, String> datosHerramienta = new LinkedHashMap<String, String>();

    private ResultSet tablaPrestamoSQL;

    private BigDecimal idEmpleadoSel;
    private BigDecimal idHerramientaSel;

    /**
     * Creates new form Prestamo
     */
    public Prestamo() {
        initComponents();
        setTitle("Préstamo");
        llenarComboEmpleado();
        llenarComboHerramienta();
        asociarEventosControles();
        llenarTablaPrestamo();
    }

    private void llenarTablaPrestamo() {
        try {
            conexion = getConnection();
            Statement sentencia = conexion.createStatement();
            String[] titulos = {"Empleado", "Herramienta", "Fecha de préstamo", "Fecha de devolución"};
            String query = """
                        SELECT
                        (emp.nombre_empleado || '-' || emp.antiguedad) as concatEmp,
                        emp.nombre_empleado as nombreE,
                        her.nombre as nombreH,
                        pr.fecha_prestamo as fechaPrestamo,
                        pr.fecha_devolucion as fechaDevolucion
                    FROM
                        proyecto.prestamo pr
                        INNER JOIN persona.empleado emp ON pr.id_empleado = emp.id_empleado
                        INNER JOIN proyecto.herramienta her ON pr.id_herramienta = her.idHerramienta
                            """;
            
            ResultSet resultado = sentencia.executeQuery(query);
            tablaPrestamoSQL = resultado;
            DefaultTableModel modelo = new DefaultTableModel(null,titulos);
            jTable1.setModel(modelo);
            String[] fila = new String[titulos.length];
            while (resultado.next()) {
                fila[0] = resultado.getString("concatEmp");
                fila[1] = resultado.getString("nombreH");
                fila[2] = resultado.getString("fechaPrestamo");
                fila[3] = resultado.getString("fechaDevolucion");
                modelo.addRow(fila);
            }
            conexion.close();
            limpiar();
            
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
            // TODO: handle exception
        }
    }

    private void limpiar(){
        comboEmpleado.setSelectedIndex(0);
        comboHerramienta.setSelectedIndex(0);
        lblFechaDevolucion.setText("");
        lblFechaPrestamo.setText("");
    }

    private void asociarEventosControles() {
        comboEmpleado.addActionListener(e -> {
            String idEmpleado = datosEmpleado.get(comboEmpleado.getSelectedItem().toString());
            idEmpleadoSel = new BigDecimal(idEmpleado);
        });
        comboHerramienta.addActionListener(e -> {
            String idHerramienta = datosHerramienta.get(comboHerramienta.getSelectedItem().toString());
            idHerramientaSel = new BigDecimal(idHerramienta);
        });
        btnInsertar.addActionListener(e -> {
            insertarPrestamo();
        });
        btnModificar.addActionListener(e -> {
            modificarPrestamo();
        });
        btnEliminar.addActionListener(e -> {
            eliminarPrestamo();
        });
        btnDevolver.addActionListener(e -> {
            devolverPrestamo();
        });
        //Llenar los combos a partir de la fila seleccionada en el jTable1
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTable1.rowAtPoint(evt.getPoint());
                if (fila > -1) {
                    String nombreEmpleado = jTable1.getValueAt(fila, 0).toString().split("-")[0];
                    comboEmpleado.setSelectedItem(nombreEmpleado);
                    comboHerramienta.setSelectedItem(jTable1.getValueAt(fila, 1).toString());
                    lblFechaPrestamo.setText(jTable1.getValueAt(fila, 2).toString());
                    lblFechaDevolucion.setText(jTable1.getValueAt(fila, 3).toString());
                    //Bloquear los botones de insertar, modificar y eliminar si la fecha de devolución es diferente de "PENDIENTE"
                    if (!lblFechaDevolucion.getText().equals("PENDIENTE")) {
                        btnInsertar.setEnabled(false);
                        btnModificar.setEnabled(false);
                        btnEliminar.setEnabled(false);
                        btnDevolver.setEnabled(false);
                    } else {
                        btnInsertar.setEnabled(true);
                        btnModificar.setEnabled(true);
                        btnEliminar.setEnabled(true);
                        btnDevolver.setEnabled(true);
                    }
                }
            }
        });
    }

    private void insertarPrestamo() {
        try {
            conexion = getConnection();
            String query = "INSERT INTO proyecto.prestamo(id_empleado, id_herramienta) VALUES (" + idEmpleadoSel + ", "
                    + idHerramientaSel + ")";
            Statement sentencia = conexion.createStatement();
            sentencia.executeUpdate(query);

            conexion.close();
            llenarTablaPrestamo();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }

    private void modificarPrestamo(){
        try {
            conexion = getConnection();
            String strIdEmpSel = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString().split("-")[0];
            BigDecimal idEmpleadoSel = new BigDecimal(datosEmpleado.get(strIdEmpSel));
            String strIdHerSel = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
            BigDecimal idHerramientaSel = new BigDecimal(datosHerramienta.get(strIdHerSel));

            BigDecimal idNuevoEmpleado = new BigDecimal(datosEmpleado.get(comboEmpleado.getSelectedItem().toString()));
            BigDecimal idNuevaHerramienta = new BigDecimal(datosHerramienta.get(comboHerramienta.getSelectedItem().toString()));

            String query = """
                    UPDATE proyecto.prestamo
                    SET id_empleado = ?, id_herramienta = ?
                    WHERE id_empleado = ? AND id_herramienta = ?
                    """;
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setBigDecimal(1, idNuevoEmpleado);
            sentencia.setBigDecimal(2, idNuevaHerramienta);
            sentencia.setBigDecimal(3, idEmpleadoSel);
            sentencia.setBigDecimal(4, idHerramientaSel);
            sentencia.executeUpdate();

            conexion.close();
            llenarTablaPrestamo();
            limpiar();

        }catch(PSQLException e){
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }catch(SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }

    private void eliminarPrestamo(){
        try {
            conexion = getConnection();
            String strIdEmpSel = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString().split("-")[0];
            BigDecimal idEmpleadoSel = new BigDecimal(datosEmpleado.get(strIdEmpSel));
            String strIdHerSel = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
            BigDecimal idHerramientaSel = new BigDecimal(datosHerramienta.get(strIdHerSel));

            String query = """
                    DELETE FROM proyecto.prestamo
                    WHERE id_empleado = ? AND id_herramienta = ?
                    """;
            PreparedStatement sentencia = conexion.prepareStatement(query);
            sentencia.setBigDecimal(1, idEmpleadoSel);
            sentencia.setBigDecimal(2, idHerramientaSel);
            sentencia.executeUpdate();

            conexion.close();
            llenarTablaPrestamo();
            limpiar();

        }catch(PSQLException e){
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }catch(SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }

    private void devolverPrestamo(){
        try {
            conexion = getConnection();
            String strIdEmpSel = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString().split("-")[0];
            BigDecimal idEmpleadoSel = new BigDecimal(datosEmpleado.get(strIdEmpSel));
            String strIdHerSel = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
            BigDecimal idHerramientaSel = new BigDecimal(datosHerramienta.get(strIdHerSel));

            String query = """
                    UPDATE proyecto.prestamo
                    SET fecha_devolucion = ?
                    WHERE id_empleado = ? AND id_herramienta = ?
                    """;
            PreparedStatement sentencia = conexion.prepareStatement(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = LocalDate.now().format(formatter);

            sentencia.setString(1,formattedDate);
            sentencia.setBigDecimal(2, idEmpleadoSel);
            sentencia.setBigDecimal(3, idHerramientaSel);
            sentencia.executeUpdate();

            conexion.close();
            llenarTablaPrestamo();
            limpiar();

        }catch(PSQLException e){
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }catch(SQLException e){
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }

    private void llenarComboEmpleado() {
        comboEmpleado.removeAllItems();
        ResultSet resultado = datosEmpleados();
        try {
            while (resultado.next()) {
                comboEmpleado.addItem(resultado.getString("nombre"));
                datosEmpleado.put(resultado.getString("nombre"), resultado.getString("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error al llenar el combo empleados");
        }
    }

    private void llenarComboHerramienta() {
        comboHerramienta.removeAllItems();
        ResultSet resultado = datosHerramienta();
        try {
            while (resultado.next()) {
                comboHerramienta.addItem(resultado.getString("nombre"));
                datosHerramienta.put(resultado.getString("nombre"), resultado.getString("idh"));
            }
        } catch (SQLException e) {
            System.out.println("Error al llenar el combo herramientas");
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, USER, PASS);
            System.out.println("Conexion exitosa");
        } catch (Exception e) {
            System.out.println("Error de conexion");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return conexion;
    }

    private ResultSet datosEmpleados() {
        try {
            conexion = getConnection();
            String query = "SELECT id_empleado as id, nombre_empleado as nombre FROM persona.empleado";
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(query);
            return resultado;
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
            // TODO: handle exception
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
        return null;
    }

    private ResultSet datosHerramienta() {
        try {
            conexion = getConnection();
            String query = "SELECT idHerramienta as idh, nombre FROM proyecto.herramienta";
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(query);
            return resultado;
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje);
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        comboEmpleado = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        comboHerramienta = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();
        btnDevolver = new javax.swing.JButton();
        lblFechaPrestamo = new javax.swing.JLabel();
        lblFechaDevolucion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombre del empleado");

        comboEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Herramienta");

        comboHerramienta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Fecha de préstamo");

        jLabel4.setText("Fecha de devolución");

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

        btnEliminar.setText("Eliminar");

        btnModificar.setText("Modificar");

        btnInsertar.setText("Insertar");

        btnDevolver.setText("Devolver");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 203, Short.MAX_VALUE)
                                .addComponent(btnInsertar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboEmpleado, 0, 156, Short.MAX_VALUE)
                            .addComponent(comboHerramienta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFechaPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFechaDevolucion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDevolver, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboHerramienta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblFechaPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblFechaDevolucion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInsertar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Prestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Prestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Prestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Prestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Prestamo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDevolver;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> comboEmpleado;
    private javax.swing.JComboBox<String> comboHerramienta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFechaDevolucion;
    private javax.swing.JLabel lblFechaPrestamo;
    // End of variables declaration//GEN-END:variables
}

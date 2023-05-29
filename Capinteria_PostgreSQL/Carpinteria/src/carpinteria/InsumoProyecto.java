/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carpinteria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

import carpinteria.models.BD;
import carpinteria.models.InsumoModel;
import carpinteria.models.ProyectoModel;

/**
 *
 * @author raul
 */
public class InsumoProyecto extends javax.swing.JFrame {
    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    private String tipoProducto;
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    private Map<String, InsumoModel> mapaInsumos = new LinkedHashMap<String, InsumoModel>();
    private Map<String, ProyectoModel> mapaProyectos = new LinkedHashMap<String, ProyectoModel>();
    private Proyecto frame;
    /**
     * Creates new form InsumoProyecto
     */
    public InsumoProyecto(Proyecto frame, BD bd) {
        this.frame = frame;
        initComponents();

        setTitle("Insumo Proyecto");
        llenarTabla();
        llenarComboInsumos();
        llenarComboProyectos();
        asociarEventosControles();

        Controles controles = new Controles(this);
        controles.verificarPrivilegiosYDesactivarControles(bd);
    }

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection(url, USER, PASS);

        } catch (Exception e) {
            System.out.println("Error de conexion");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        return conexion;
    }

    private void llenarTabla() {
        try {
            conexion = getConnection();
            // idInsumo, idProyecto, cantidad, subtotal
            String[] titulos = { "Insumo", "Proyecto", "Cantidad", "Subtotal" };
            String sql = """
                    SELECT
                        i.nombre as insumo,
                        p.idproyecto ||'-'|| tp.nombre_proyecto || '-' || p.fecha_estimada as concatenacion,
                        tp.nombre_proyecto as proyecto,
                         ip.cantidad,
                         ip.subtotal
                    FROM
                        proyecto.insumoproyecto ip
                    INNER JOIN proyecto.insumo i ON ip.idinsumo = i.idinsumo
                    INNER JOIN proyecto.proyecto p ON ip.idproyecto = p.idproyecto
                    INNER JOIN proyecto.tipo_proyecto tp ON p.idtipo_proyecto = tp.idtipo_proyecto
                                    """;

            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            DefaultTableModel model = new DefaultTableModel(null, titulos);
            String[] fila = new String[titulos.length];
            while (rs.next()) {
                fila[0] = rs.getString("insumo");
                fila[1] = rs.getString("concatenacion");
                fila[2] = rs.getString("cantidad");
                fila[3] = rs.getString("subtotal");
                model.addRow(fila);
            }
            jTable1.setModel(model);
            conexion.close();
            limpiar();

            frame.muestra();

        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asociarEventosControles() {
        // Asociar el evento de click en una fila de la tabla para llenar los campos
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = jTable1.rowAtPoint(evt.getPoint());
                int columna = jTable1.columnAtPoint(evt.getPoint());
                if ((fila > -1) && (columna > -1)) {
                    inputCantidad.setText(jTable1.getValueAt(fila, 2).toString());
                    comboInsumos.setSelectedItem(jTable1.getValueAt(fila, 0).toString());
                    comboProyectos.setSelectedItem(jTable1.getValueAt(fila, 1).toString());
                }
            }
        });

        btnInsertar.addActionListener(e -> {
            if (inputCantidad.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                insertarTupla();
            }
        });
        // Asociar el evento de modificar tupla al botón correspondiente
        btnModificar.addActionListener(e -> {
            if (inputCantidad.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                modificarTupla();
            }
        });

        // Asociar el evento de eliminar tupla al botón correspondiente
        btnEliminar.addActionListener(e -> {
            eliminarTupla();
        });
    }

    private void limpiar() {
        inputCantidad.setText("");
        comboInsumos.setSelectedIndex(0);
        comboProyectos.setSelectedIndex(0);
    }

    private void llenarComboInsumos() {
        comboInsumos.removeAllItems();
        ResultSet rs = obtenerDatosInsumos();
        try {
            while (rs.next()) {
                BigDecimal id = rs.getBigDecimal("idInsumo");
                String nombre = rs.getString("nombre");
                int cantidadDisp = rs.getInt("cantidad_disponible");
                BigDecimal precio = rs.getBigDecimal("precio");
                InsumoModel insumo = new InsumoModel(id, nombre, cantidadDisp, precio);
                mapaInsumos.put(nombre, insumo);
                comboInsumos.addItem(nombre);
            }
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ResultSet obtenerDatosInsumos() {
        try {
            conexion = getConnection();
            String sql = "SELECT * FROM proyecto.insumo ORDER BY nombre ASC";
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            // TODO: handle exception
        }
        return null;
    }

    private void llenarComboProyectos() {
        comboProyectos.removeAllItems();
        ResultSet rs = obtenerDatosProyectos();
        try {
            while (rs.next()) {
                int id = rs.getInt("idproyecto");
                String nombre = rs.getString("nombre_proyecto");
                String fecha = rs.getString("fecha");
                String totalStr = rs.getString("total");
                totalStr = totalStr.replaceAll("\\$|,", "");
                BigDecimal total = new BigDecimal(totalStr);
                ProyectoModel proyecto = new ProyectoModel(id, nombre, total);
                mapaProyectos.put(id + "-"+nombre+"-"+fecha, proyecto);
                comboProyectos.addItem(id + "-"+nombre+"-"+fecha);
            }
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ResultSet obtenerDatosProyectos() {
        try {
            conexion = getConnection();
            String sql = """
                            SELECT
                                idproyecto, nombre_proyecto, total, fecha_estimada as fecha
                            FROM
                                proyecto.proyecto p
                                INNER JOIN proyecto.tipo_proyecto dp ON p.idtipo_proyecto = dp.idtipo_proyecto
                            ORDER BY nombre_proyecto ASC
                    """;
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void insertarTupla() {
        try {

            sumarAlTotalProyecto();

            conexion = getConnection();
            String sql = "INSERT INTO proyecto.insumoProyecto (idInsumo, idProyecto, cantidad, subtotal) VALUES (?, ?, ?, ?)";

            BigDecimal subtotal = mapaInsumos.get(comboInsumos.getSelectedItem().toString()).getPrecio()
                    .multiply(new BigDecimal(inputCantidad.getText()));
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setBigDecimal(1, mapaInsumos.get(comboInsumos.getSelectedItem().toString()).getId());
            ps.setInt(2, mapaProyectos.get(comboProyectos.getSelectedItem().toString()).getIdProyecto());
            ps.setInt(3, Integer.parseInt(inputCantidad.getText()));
            ps.setBigDecimal(4, subtotal);
            ps.executeUpdate();
            conexion.close();

            limpiar();
            llenarTabla();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarTupla() {
        try {
            restarAlTotalProyecto();
            sumarAlTotalProyecto();
            conexion = getConnection();
            String sql = """
                    UPDATE proyecto.insumoProyecto
                    SET
                        idInsumo = ?,
                        idProyecto = ?,
                        cantidad = ?,
                        subtotal = ?
                        WHERE idProyecto = ? AND idInsumo = ?
                        """;

            BigDecimal subtotal = mapaInsumos.get(comboInsumos.getSelectedItem().toString()).getPrecio()
                    .multiply(new BigDecimal(inputCantidad.getText()));
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setBigDecimal(1, mapaInsumos.get(comboInsumos.getSelectedItem().toString()).getId());
            ps.setInt(2, mapaProyectos.get(comboProyectos.getSelectedItem().toString()).getIdProyecto());
            ps.setInt(3, Integer.parseInt(inputCantidad.getText()));
            ps.setBigDecimal(4, subtotal);
            // Obtener el id del proyecto seleccionado en la tabla
            int idProyecto = mapaProyectos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString())
                    .getIdProyecto();
            // Obtener el id del insumo seleccionado en la tabla
            BigDecimal idInsumo = mapaInsumos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()).getId();
            ps.setInt(5, idProyecto);
            ps.setBigDecimal(6, idInsumo);

            ps.executeUpdate();
            conexion.close();

            limpiar();
            llenarTabla();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTupla() {
        try {
            restarAlTotalProyecto();
            conexion = getConnection();
            String sql = """
                    DELETE FROM proyecto.insumoProyecto
                    WHERE idProyecto = ? AND idInsumo = ?
                        """;

            PreparedStatement ps = conexion.prepareStatement(sql);

            // Obtener el id del proyecto seleccionado en la tabla
            int idProyecto = mapaProyectos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString())
                    .getIdProyecto();
            // Obtener el id del insumo seleccionado en la tabla
            BigDecimal idInsumo = mapaInsumos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()).getId();
            ps.setInt(1, idProyecto);
            ps.setBigDecimal(2, idInsumo);

            ps.executeUpdate();
            conexion.close();

            limpiar();
            llenarTabla();
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sumarAlTotalProyecto() {
        try {
            conexion = getConnection();
            String sql = """
                    UPDATE proyecto.proyecto
                    SET
                        total = total + CAST(? AS money)
                        WHERE idProyecto = ?
                        """;

            BigDecimal subtotal = mapaInsumos.get(comboInsumos.getSelectedItem().toString()).getPrecio()
                    .multiply(new BigDecimal(inputCantidad.getText()));
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setBigDecimal(1, subtotal);
            ps.setInt(2, mapaProyectos.get(comboProyectos.getSelectedItem().toString()).getIdProyecto());

            ps.executeUpdate();
            conexion.close();

        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restarAlTotalProyecto() {
        try {
            conexion = getConnection();
            String sql = """
                    UPDATE proyecto.proyecto
                    SET
                        total = total - CAST(? AS money)
                        WHERE idProyecto = ?
                        """;

            String strSubtotal = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString().replaceAll("\\$|,", "");
            BigDecimal subtotal = new BigDecimal(strSubtotal);
            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setBigDecimal(1, subtotal);
            ps.setInt(2, mapaProyectos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString()).getIdProyecto());

            ps.executeUpdate();
            conexion.close();

        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void llenarTablaProyecto() {
        try {
            conexion = getConnection();
            String[] titulos = { "ID", "Nombre", "Total" };
            String sql = """
                            SELECT
                                idproyecto, nombre_proyecto, total
                            FROM
                                proyecto.proyecto p
                                INNER JOIN proyecto.tipo_proyecto dp ON p.idtipo_proyecto = dp.idtipo_proyecto
                            ORDER BY nombre_proyecto ASC
                    """;
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            DefaultTableModel modelo = new DefaultTableModel(null, titulos);
            String[] tupla = new String[titulos.length];

            while (rs.next()) {
                tupla[0] = rs.getString("idproyecto");
                tupla[1] = rs.getString("nombre_proyecto");
                tupla[2] = rs.getString("total");
                modelo.addRow(tupla);
            }
            //tablaProyecto.setModel(modelo);
            conexion.close();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboInsumos = new javax.swing.JComboBox<>();
        comboProyectos = new javax.swing.JComboBox<>();
        inputCantidad = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnInsertar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Insumo:");

        jLabel2.setText("Proyecto:");

        jLabel3.setText("Cantidad:");

        comboInsumos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboProyectos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane1.setViewportView(jTable1);

        btnEliminar.setText("Eliminar");

        btnModificar.setText("Modificar");

        btnInsertar.setText("Insertar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboInsumos, 0, 150, Short.MAX_VALUE)
                            .addComponent(comboProyectos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(inputCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 185, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnInsertar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboProyectos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(inputCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInsertar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> comboInsumos;
    private javax.swing.JComboBox<String> comboProyectos;
    private javax.swing.JTextField inputCantidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

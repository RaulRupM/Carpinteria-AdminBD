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

import carpinteria.models.BD;

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
    private BD bd;

    private Orden referencia = this;

    /**
     * Constructor de la clase Orden que inicializa la interfaz de usuario y llama a
     * los métodos para asociar eventos a los controles,
     * llenar la tabla de órdenes y llenar el cuadro combinado de proveedores.
     * También desactiva los botones de ordenar y establece el cierre de la ventana
     * como DISPOSE_ON_CLOSE.
     * Finalmente, crea un objeto Controles para verificar los privilegios del
     * usuario y desactivar los controles correspondientes.
     *
     * @param bd objeto de la clase BD que proporciona acceso a la base de datos
     */
    public Orden(BD bd) {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Orden");
        asociarEventosControles();
        llenarTablaOrden();
        llenarComboProveedores();
        btnOrdenarHerramienta.enable(false);
        btnOrdenarInsumo.enable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Controles controles = new Controles(this);
        controles.verificarPrivilegiosYDesactivarControles(bd);
        this.bd = bd;
    }

    /**
     * Establece una conexión con la base de datos PostgreSQL utilizando la URL, el
     * usuario y la contraseña especificados.
     * Carga el controlador de la base de datos y devuelve un objeto Connection que
     * representa la conexión establecida.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     *
     * @return objeto Connection que representa la conexión establecida con la base
     *         de datos
     */
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

    /**
     * 
     * Inserta un nuevo pedido en la tabla de la base de datos "Orden" con el
     * proveedor seleccionado, la fecha del pedido y un total establecido en 0.
     * Utiliza un mapa para recuperar el ID del proveedor seleccionado de un cuadro
     * de selección (combo box) y la fecha de un selector de fecha (date chooser).
     * Construye una cadena de consulta SQL y la ejecuta utilizando un objeto
     * Statement.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     * Finalmente, el método llama a llenarTablaOrden() para actualizar la tabla con
     * los datos actualizados.
     */
    public void insertarOrden() {
        try {
            conexion = getConexion();
            String idProveedor = mapaOrdenes.get(comboProveedores.getSelectedItem().toString());
            String fechaOrden = dateChooserOrden.getDate().toString();
            String total = "0";

            String sql = "INSERT INTO Orden.Orden (idProveedor, fechaOrden, total) VALUES ('" + idProveedor + "', '"
                    + fechaOrden + "', '" + total + "')";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar los datos \n" + mensaje);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al insertar los datos \n" + mensaje);
        }
    }

    /**
     * Actualiza una orden existente en la tabla de base de datos Orden con el
     * proveedor seleccionado, la fecha de la orden y el total establecido en 0.
     * Utiliza un mapa para recuperar el ID del proveedor seleccionado de un cuadro
     * combinado y la fecha de un selector de fecha.
     * Luego, construye una cadena de consulta SQL y la ejecuta utilizando un objeto
     * Statement.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     * Finalmente, el método llama a llenarTablaOrden() para actualizar la tabla con
     * los datos actualizados.
     */
    public void modificarOrden() {
        try {
            conexion = getConexion();
            String idProveedor = mapaOrdenes.get(comboProveedores.getSelectedItem().toString());
            String fechaOrden = dateChooserOrden.getDate().toString();
            String total = "0";

            String sql = "UPDATE Orden.Orden SET idProveedor = '" + idProveedor + "', fechaOrden = '" + fechaOrden
                    + "', total = '" + total + "' WHERE idOrden = '" + id + "'";
            Statement st = conexion.createStatement();
            st.executeUpdate(sql);
            conexion.close();
            llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar los datos\n" + mensaje);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al modificar los datos\n" + mensaje);
        }
    }

    /**
     * Elimina una orden de la tabla de base de datos Orden con el ID de orden
     * actual.
     * Construye una cadena de consulta SQL y la ejecuta utilizando un objeto
     * Statement.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     * Finalmente, el método llama a llenarTablaOrden() para actualizar la tabla con
     * los datos actualizados.
     */
    public void eliminarOrden() {
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
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, "Error al eliminar los datos\n" + mensaje);
        }
    }

    /**
     * Asocia eventos de acción a los controles de la interfaz de usuario.
     * Los eventos de acción incluyen insertar una nueva orden, modificar una orden
     * existente, eliminar una orden existente,
     * ordenar un insumo y ordenar una herramienta.
     * También asocia un evento de clic de ratón a la tabla de órdenes para
     * seleccionar una fila y mostrar los detalles de la orden.
     */
    private void asociarEventosControles() {
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
                Pedido pedido = new Pedido("Insumo", id, referencia, bd);
                pedido.setVisible(true);
            }
        });

        btnOrdenarHerramienta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Pedido pedido = new Pedido("Herramienta", id, referencia, bd);
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

    /**
     * Limpia los campos de entrada de la interfaz de usuario para una nueva orden.
     * Establece el proveedor seleccionado en el primer elemento del cuadro
     * combinado, la fecha de la orden en nulo y deshabilita los botones de ordenar.
     */
    public void limpiar() {
        comboProveedores.setSelectedIndex(0);
        dateChooserOrden.setDate(null);
        btnOrdenarHerramienta.setEnabled(false);
        btnOrdenarInsumo.setEnabled(false);
    }

    /**
     * Llena la tabla de órdenes con los datos de la tabla de base de datos Orden.
     * Utiliza una consulta SQL para recuperar los datos de la tabla y los agrega a
     * un modelo de tabla.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     * Finalmente, el método llama a limpiar() para restablecer los campos de
     * entrada de la interfaz de usuario.
     */
    public void llenarTablaOrden() {
        try {
            conexion = getConexion();
            String[] titulos = { "ID", "Proveedor", "Fecha", "Total" };
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

    /**
     * Llena el cuadro combinado de proveedores con los datos de la tabla de base de
     * datos Proveedor.
     * Utiliza una consulta SQL para recuperar los datos de la tabla y los agrega al
     * cuadro combinado y al mapa de órdenes.
     * Si se produce una excepción, se captura y maneja mostrando un mensaje de
     * error con los detalles de la excepción.
     */
    private void llenarComboProveedores() {
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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

        comboProveedores.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnNuevaOrden.setText("Nueva orden");

        tablaOrdenes.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
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
                                                .addComponent(btnNuevaOrden, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525,
                                                Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnModificarOrden)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnEliminarOrden, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(86, 86, 86)
                                                                .addComponent(jLabel1,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 98,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addGroup(layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(dateChooserOrden,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(comboProveedores, 0, 179,
                                                                                Short.MAX_VALUE))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnOrdenarHerramienta,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnOrdenarInsumo,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 140,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(24, 24, 24)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboProveedores, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(dateChooserOrden, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnOrdenarHerramienta,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnOrdenarInsumo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23)
                                .addComponent(btnNuevaOrden)
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnModificarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEliminarOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

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

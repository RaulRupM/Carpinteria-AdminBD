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
import javax.swing.plaf.nimbus.State;
import javax.swing.table.DefaultTableModel;

import org.postgresql.util.PSQLException;

import carpinteria.models.HerramientaModel;
import carpinteria.models.InsumoModel;
import carpinteria.models.ItemProyecto;

/**
 *
 * @author raul
 */
public class Pedido extends javax.swing.JFrame {
    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    private String tipoProducto;
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    public BigDecimal id;
    private String idOrden;
    private Map<String, ItemProyecto> mapaProductos = new LinkedHashMap<String, ItemProyecto>();
    private Orden pantallaOrden;

    /**
     * Creates new form Pedido
     */
    public Pedido(String tipoProducto, String idOrden, Orden pantallaOrden) {
        this.tipoProducto = tipoProducto;
        this.idOrden = idOrden;
        this.pantallaOrden = pantallaOrden;
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Pedido");
        llenarComboProducto();
        asociarEventosControles();
        llenarTablaPedido(tipoProducto.equals("Insumo"));
    }

    private void asociarEventosControles() {
        // Cambiar el lblPrecio al seleccionar un producto del comboProducto
        comboProducto.addActionListener(e -> {
            String nombreProducto = comboProducto.getSelectedItem().toString();
            ItemProyecto item = mapaProductos.get(nombreProducto);
            lblPrecio.setText(item.getPrecio().toString());
            this.id = item.getId();
        });

        //Llenar los controles al seleccionar un pedido de la tabla
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = jTable1.getSelectedRow();
                if (filaSeleccionada != -1) {
                    String cantidad = jTable1.getValueAt(filaSeleccionada, 2).toString();
                    String nombreProducto = jTable1.getValueAt(filaSeleccionada, 1).toString();
                    inputCantidad.setText(cantidad);
                    comboProducto.setSelectedItem(nombreProducto);
                }
            }
        });
        // Insertar pedido al hacer click en el botón
        btnAgregar.addActionListener(e -> {
            if (inputCantidad.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                insertarPedido(tipoProducto);
            }
        });
        btnModificar.addActionListener(e -> {
            if (inputCantidad.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                modificarPedido(tipoProducto);
            }
        });
        btnEliminar.addActionListener(e -> {
            eliminarPedido(tipoProducto);
        });
    }

    
    private void llenarTablaPedido(boolean esInsumo) {
        try {
            conexion = getConnection();
            String[] titulos = {"Orden",  esInsumo ? "Insumo" : "Herramienta", "Cantidad", "Subtotal"};
            String sql = """
                    SELECT
                        (o.idorden || '-' || prov.nombre) as Orden, %s.nombre, detO.cantidad, detO.subtotal
                    FROM
                        orden.detalleorden detO
                        INNER JOIN orden.orden o ON detO.idOrden = o.idOrden
                        INNER JOIN %s ON detO.%s = %s.%s
                        INNER JOIN empresa.proveedor prov ON o.idProveedor = prov.idProveedor
                    WHERE detO.idOrden = ?
                    ORDER BY %s.nombre ASC
                    """;
            String tabla = esInsumo ? "proyecto.insumo i" : "proyecto.herramienta h";
            String idColumna = esInsumo ? "idInsumo" : "idHerramienta";
            String nombreColumna = esInsumo ? "i" : "h";
            sql = String.format(sql, nombreColumna, tabla, idColumna, nombreColumna, idColumna, nombreColumna);
    
            PreparedStatement pst = conexion.prepareStatement(sql);
            pst.setBigDecimal(1, new BigDecimal(idOrden));
            ResultSet rs = pst.executeQuery();
    
            DefaultTableModel modelo = new DefaultTableModel(null, titulos);
            jTable1.setModel(modelo);
            String[] fila = new String[titulos.length];
    
            while (rs.next()) {
                fila[0] = rs.getString("Orden");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("cantidad");
                fila[3] = rs.getString("subtotal");
                modelo.addRow(fila);
            }
            conexion.close();
            this.pantallaOrden.llenarTablaOrden();
            limpiar();
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void limpiar(){
        inputCantidad.setText("");
        lblPrecio.setText("");
        comboProducto.setSelectedIndex(0);
    }

    public void insertarPedido(String tipoProducto) {
        try {
            conexion = getConnection();
            BigDecimal subtotal = new BigDecimal(lblPrecio.getText()).multiply(new BigDecimal(inputCantidad.getText()));
            String query = """
                        INSERT INTO Orden.DetalleOrden (idOrden, %s, cantidad, subtotal)
                        VALUES (?,?,?,?)
                    """.formatted(tipoProducto.equals("Insumo") ? "idInsumo" : "idHerramienta");
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(idOrden));
            pst.setInt(2,
                    Integer.parseInt(mapaProductos.get(comboProducto.getSelectedItem().toString()).getId().toString()));
            pst.setInt(3, Integer.parseInt(inputCantidad.getText()));
            pst.setBigDecimal(4, subtotal);
            pst.executeUpdate();
            conexion.close();

            llenarTablaPedido(tipoProducto.equals("Insumo"));
            this.pantallaOrden.llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modificarPedido(String tipoProducto){
        try {
            String strSubtotalViejo = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString().replaceAll("\\$|,", "");
            BigDecimal subtotalViejo = new BigDecimal(strSubtotalViejo);

            BigDecimal subtotal = new BigDecimal(lblPrecio.getText()).multiply(new BigDecimal(inputCantidad.getText()));
            
            BigDecimal idProducto = mapaProductos.get(comboProducto.getSelectedItem().toString()).getId();
            String tipoProductoColumna = tipoProducto.equals("Insumo") ? "idInsumo" : "idHerramienta";
            BigDecimal idProductoTabla = mapaProductos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 1)).getId();
            restarSubtotal(subtotalViejo);
            
            conexion = getConnection();
            String query = """
                        UPDATE Orden.DetalleOrden
                        SET %s = ?, cantidad = ?, subtotal = ?
                        WHERE idOrden = ? AND %s = ?
                    """.formatted(tipoProductoColumna, tipoProductoColumna);
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setBigDecimal(1, idProducto);
            pst.setInt(2, Integer.parseInt(inputCantidad.getText()));
            pst.setBigDecimal(3, subtotal);
            pst.setInt(4, Integer.parseInt(idOrden));
            pst.setBigDecimal(5,idProductoTabla);
            pst.executeUpdate();
            conexion.close();

            llenarTablaPedido(tipoProducto.equals("Insumo"));
            pantallaOrden.llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarPedido(String tipoProducto){
        try {
            String strSubtotal = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString().replaceAll("\\$|,", "");
            BigDecimal subtotal = new BigDecimal(strSubtotal);
            BigDecimal idProducto = mapaProductos.get(jTable1.getValueAt(jTable1.getSelectedRow(), 1)).getId();
            restarSubtotal(subtotal);
            
            conexion = getConnection();
            String query = """
                        DELETE FROM Orden.DetalleOrden
                        WHERE idOrden = ? AND %s = ?
                    """.formatted(tipoProducto.equals("Insumo") ? "idInsumo" : "idHerramienta");
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(idOrden));
            pst.setBigDecimal(2, idProducto);
            pst.executeUpdate();
            conexion.close();

            llenarTablaPedido(tipoProducto.equals("Insumo"));
            pantallaOrden.llenarTablaOrden();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void restarSubtotal(BigDecimal subtotal){
        try {
            conexion = getConnection();
            String query = """
                        UPDATE Orden.Orden
                        SET total = total - CAST(? AS money)
                        WHERE idOrden = ?
                    """;
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setBigDecimal(1, subtotal);
            pst.setInt(2, Integer.parseInt(idOrden));
            pst.executeUpdate();
            conexion.close();
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);            
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

    public void llenarComboProducto() {
        lblProducto.setText(tipoProducto);
        comboProducto.removeAllItems();
        mapaProductos.clear();
        ResultSet rs = null;
        String nombreId = "";
        switch (tipoProducto) {
            case "Insumo":
                rs = datosComboInsumo();
                nombreId = "idInsumo";
                break;
            case "Herramienta":
                nombreId = "idHerramienta";
                rs = datosComboHerramienta();
                break;
            default:
                break;
        }
        if (rs != null) {
            try {

                while (rs.next()) {

                    BigDecimal id = rs.getBigDecimal(nombreId);
                    String nombre = rs.getString("nombre");
                    int cantidadDisponible = rs.getInt("cantidad_disponible");
                    BigDecimal precio = rs.getBigDecimal("precio");
                    // Crear instancia de Insumo o Herramienta según el tipoProducto
                    ItemProyecto item;
                    if (tipoProducto.equals("Insumo")) {
                        item = new InsumoModel(id, nombre, cantidadDisponible, precio);
                    } else {
                        String tipo = rs.getString("tipo");
                        String estado = rs.getString("estado");
                        item = new HerramientaModel(id, nombre, tipo, estado, cantidadDisponible, precio);
                    }

                    comboProducto.addItem(nombre);
                    mapaProductos.put(nombre, item);
                }

            } catch (SQLException e) {
                String mensaje = Excepciones.manejarExcepcionSQL(e);
                JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                // TODO: handle exception
            }

        }
    }

    // Llena la tabla con los datos de la base de datos

    private ResultSet datosComboInsumo() {
        try {
            conexion = getConnection();
            String query = """
                    SELECT * FROM Proyecto.Insumo
                    ORDER BY nombre ASC
                    """;
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private ResultSet datosComboHerramienta() {
        try {
            conexion = getConnection();
            String query = """
                    SELECT * FROM Proyecto.Herramienta
                    ORDER BY nombre ASC
                    """;
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs;
        } catch (PSQLException e) {
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
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
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblProducto = new javax.swing.JLabel();
        comboProducto = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        inputCantidad = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        comboProducto.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Precio:");

        jLabel4.setText("Cantidad");

        btnAgregar.setText("Agregar a la orden");

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

        btnEliminar.setText("Eliminar");

        btnModificar.setText("Modificar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 75,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(comboProducto, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(jLabel4,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 63,
                                                                Short.MAX_VALUE)
                                                        .addComponent(jLabel2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 76,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(inputCantidad, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnAgregar, javax.swing.GroupLayout.Alignment.LEADING,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnModificar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblProducto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboProducto, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(lblPrecio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputCantidad, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAgregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29,
                                        Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEliminar)
                                        .addComponent(btnModificar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> comboProducto;
    private javax.swing.JTextField inputCantidad;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblProducto;
    // End of variables declaration//GEN-END:variables
}

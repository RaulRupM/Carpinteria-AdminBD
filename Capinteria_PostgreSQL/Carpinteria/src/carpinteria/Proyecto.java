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
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author alex_
 */
public class Proyecto extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    
    public String id;
    
    
    public Proyecto() {
        initComponents();
        
        //Establecer Fecha con el rango de 01/01/95 hasta la fecha actual
        Calendar startCal = Calendar.getInstance();
        startCal.set(1995,Calendar.JANUARY,1);
        Date startDate = startCal.getTime();
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = endCal.getTime();
        
        //Establecer la fecha con el rango del dia actual hasta el ultimo dia del año actual.
        Calendar startCal2 = Calendar.getInstance();
        Date startDate2 = startCal2.getTime();
        
        Calendar endCal2 = Calendar.getInstance();
        endCal2.set(Calendar.MONTH,Calendar.DECEMBER);
        endCal2.set(Calendar.DAY_OF_MONTH, 31);
        Date endDate2 = endCal2.getTime();
        
        Date Fecha = new Date();
        
        dateEstimada.setSelectableDateRange( startDate2, endDate2);
        
        dateEntrega.setDate(Fecha);
        this.dateEntrega.setSelectableDateRange(this.dateEntrega.getDate(),
        this.dateEntrega.getDate());
        
        txtDesc.setEnabled(false);
        txtTotal.setEnabled(false);
        
        dateEntrega.setEnabled(false);
        
        comboNombreP.setModel(new DefaultComboBoxModel(obtenNomProyectos()));
        
        tablaProyecto.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    id=tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 0).toString();
                    
                    //txtNombre.setText(tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 1).toString());
                    //txtPrecio.setText(tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 2).toString());
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
        String query = "SELECT Nombre_Proyecto FROM Proyecto.Tipo_Proyecto";
        rs = corrida.executeQuery(query);
        
        while (rs.next()) {
            String nombre = rs.getString("Nombre_Proyecto");
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
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        comboNombreC = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        comboLider = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaProyecto = new javax.swing.JTable();
        comboNombreP = new javax.swing.JComboBox();
        comboEstado = new javax.swing.JComboBox();
        dateEstimada = new com.toedter.calendar.JDateChooser();
        dateEntrega = new com.toedter.calendar.JDateChooser();
        txtDesc = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        btnInserta = new javax.swing.JButton();
        btnModifica = new javax.swing.JButton();
        btnElimina = new javax.swing.JButton();

        setTitle("Proyecto");

        jLabel1.setText("Proyecto");

        jLabel2.setText("Nombre");

        jLabel3.setText("Estado");

        jLabel4.setText("Fecha estimada de entrega");

        jLabel5.setText("Fecha de entrega");

        jLabel6.setText("Descuento");

        jLabel7.setText("Total mano de obra");

        comboNombreC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel8.setText("Nombre del cliente");

        jLabel9.setText("Empleado Líder");

        comboLider.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablaProyecto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaProyecto);

        comboNombreP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Comienzo", "Pendiente" }));

        btnInserta.setText("Insertar");
        btnInserta.setToolTipText("");

        btnModifica.setText("Modiciar");

        btnElimina.setText("Eliminar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboNombreP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboEstado, 0, 140, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(34, 34, 34)
                                .addComponent(comboLider, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(comboNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(dateEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(dateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(190, 190, 190)
                                .addComponent(btnInserta)
                                .addGap(18, 18, 18)
                                .addComponent(btnModifica)
                                .addGap(18, 18, 18)
                                .addComponent(btnElimina)))
                        .addGap(0, 73, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
            .addGroup(layout.createSequentialGroup()
                .addGap(334, 334, 334)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(comboNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(comboLider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(dateEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(dateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInserta)
                    .addComponent(btnModifica)
                    .addComponent(btnElimina))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElimina;
    private javax.swing.JButton btnInserta;
    private javax.swing.JButton btnModifica;
    private javax.swing.JComboBox comboEstado;
    private javax.swing.JComboBox comboLider;
    private javax.swing.JComboBox comboNombreC;
    private javax.swing.JComboBox comboNombreP;
    private com.toedter.calendar.JDateChooser dateEntrega;
    private com.toedter.calendar.JDateChooser dateEstimada;
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
    private javax.swing.JTable tablaProyecto;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}

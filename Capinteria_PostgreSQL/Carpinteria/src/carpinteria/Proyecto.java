package carpinteria;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    
    public String []id;
    
    String idempleado;
    String idcliente;
    String idtipo_proyecto;
    
    
    
    public Proyecto() {
        initComponents();
        
        //Establecer Fecha con el rango de 01/01/95 hasta la fecha actual
        
        //Establecer la fecha con el rango del dia actual hasta el ultimo dia del año actual.
        Calendar startCal2 = Calendar.getInstance();
        Date startDate2 = startCal2.getTime();
        
        Calendar endCal2 = Calendar.getInstance();
        endCal2.set(Calendar.MONTH,Calendar.DECEMBER);
        endCal2.set(Calendar.DAY_OF_MONTH, 31);
        Date endDate2 = endCal2.getTime();
        
        Date Fecha = new Date();
        
        dateEstimada.setDate(Fecha);
        dateEstimada.setSelectableDateRange(startDate2,endDate2);
        
        dateEntrega.setDate(Fecha);
        dateEntrega.setSelectableDateRange(dateEntrega.getDate(),dateEntrega.getDate());
        
        txtDesc.setEnabled(false);
        txtTotal.setEnabled(false);
        
        dateEntrega.setEnabled(false);
        
        comboNombreP.setModel(new DefaultComboBoxModel(obtenNomProyectos()));
        comboLider.setModel(new DefaultComboBoxModel(obtenNomEmpleado()));
        comboNombreC.setModel(new DefaultComboBoxModel(obtenNomCliente()));
        
        muestra();
        
        tablaProyecto.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    
                    id=tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 0).toString().split("-");
                    comboNombreP.setSelectedItem(id[1]);
                    
                    String nombreL[] = tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(),1 ).toString().split("-");
                    comboLider.setSelectedItem(nombreL[0]);
                    
                    String nombreC[] = tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(),2 ).toString().split("-");
                    comboNombreC.setSelectedItem(nombreC[0]);
                    
                    String estado = tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 3).toString();
                    comboEstado.setSelectedItem(estado);
                    
                    txtDesc.setText(tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 6).toString());
                    txtTotal.setText(tablaProyecto.getValueAt(tablaProyecto.getSelectedRow(), 7).toString());
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
   public void actualizaCombo(){
       comboNombreP.setModel(new DefaultComboBoxModel(obtenNomProyectos()));
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
   
   
   
   public String[] obtenNomEmpleado(){
    ArrayList<String> nomEmpleados = new ArrayList<>();
    Connection conexion = null;
    ResultSet rs = null;
    
    try {
        conexion = conectaDB();
        Statement corrida = conexion.createStatement();
        String query = "SELECT Nombre_Empleado FROM Persona.Empleado WHERE tipo_empleado= 'Líder' ";
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
   
   public String[] obtenNomCliente(){
    ArrayList<String> nomCliente = new ArrayList<>();
    Connection conexion = null;
    ResultSet rs = null;
    
    try {
        conexion = conectaDB();
        Statement corrida = conexion.createStatement();
        String query = "SELECT Nombre_Cliente FROM Persona.Cliente ";
        rs = corrida.executeQuery(query);
        
        while (rs.next()) {
            String nombre = rs.getString("Nombre_Cliente");
            nomCliente.add(nombre);
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
    return nomCliente.toArray(new String[nomCliente.size()]);
}
   
   public void muestra() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = ("SELECT CONCAT(p.idProyecto, '-' ,t.nombre_proyecto) AS NombreProyecto,CONCAT(e.Nombre_Empleado,'-',e.antiguedad) AS EmpleadoSupervisor," +
            "CONCAT(c.Nombre_Cliente,'-',c.Correo_Cliente) AS Cliente , p.estado AS Estado, p.fecha_estimada AS FechaEstimada, p.fecha_entrega AS FechadeEntrega, p.descuento AS Descuento, p.Total AS TotalManodeobra " +
            " FROM Proyecto.Proyecto p" +
            " INNER JOIN Persona.Cliente c ON p.idCliente = c.id_Cliente" +
            " INNER JOIN Proyecto.Tipo_Proyecto t ON p.idTipo_proyecto = t.idTipo_proyecto" +
            " INNER JOIN Persona.Empleado e ON p.id_emp_supervisor = e.id_Empleado"
            + " ORDER BY idProyecto ASC");
            String[] datos = new String[8];
            ResultSet columnas = corrida.executeQuery(Query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Nombre Proyecto");
            model.addColumn("Empleado Supervisor");
            model.addColumn("Cliente");
            model.addColumn("Estado");
            model.addColumn("Fecha Estimada");
            model.addColumn("Fecha de Entrega");
            model.addColumn("Descuento");
            model.addColumn("Total mano de obra");
            tablaProyecto.setModel(model);
            while (columnas.next()) {
                datos[0] = columnas.getString(1);
                datos[1] = columnas.getString(2);
                datos[2] = columnas.getString(3);
                datos[3] = columnas.getString(4);
                datos[4] = columnas.getString(5);
                datos[5] = columnas.getString(6);
                datos[6] = columnas.getString(7);
                datos[7] = columnas.getString(8);
                model.addRow(datos);
            }
            
            txtDesc.setText("0");
            txtTotal.setText("0");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
        }
    }
   
   
    public String obtenerIdClientePorNombre(String nombreCliente) {
        String idCliente = null;
        Connection conexion = null;
        ResultSet rs = null;
    
        try {
            conexion = conectaDB();
            Statement statement = conexion.createStatement();
            String query = "SELECT id_Cliente FROM Persona.Cliente WHERE Nombre_Cliente = '" + nombreCliente + "'";
            rs = statement.executeQuery(query);

            if (rs.next()) {
                idCliente = rs.getString("id_Cliente");
            }

            statement.close();
            conexion.close();  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return idCliente;
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
    
    public String obtenerIdTipoProyectoPorNombre(String nombreTipoProyecto) {
        String idTipoProyecto = null;
        Connection conexion = null;
        ResultSet rs = null;
    
        try {
            conexion = conectaDB();
            Statement statement = conexion.createStatement();
            String query = "SELECT idTipo_Proyecto FROM Proyecto.Tipo_Proyecto WHERE Nombre_Proyecto = '" + nombreTipoProyecto + "'";
            rs = statement.executeQuery(query);

            if (rs.next()) {
                idTipoProyecto = rs.getString("idTipo_Proyecto");
            }

            statement.close();
            conexion.close();  
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return idTipoProyecto;
    }
    
    
   
    public void obtenIdCliente() {
      if (comboNombreC.getSelectedIndex() != -1) {
          Object seleccionado = comboNombreC.getSelectedItem();
          String nombreCliente = seleccionado.toString();
          String idCliente = obtenerIdClientePorNombre(nombreCliente);
          idcliente = idCliente;
      }
    }
    
    public void obtenIdEmpleado() {
      if (comboLider.getSelectedIndex() != -1) {
          Object seleccionado = comboLider.getSelectedItem();
          String nombreLider = seleccionado.toString();
          String idLider = obtenerIdEmpleadoPorNombre(nombreLider);
          idempleado = idLider;
      }
    }
    
    public void obtenIdTipoProyecto() {
      if (comboNombreP.getSelectedIndex() != -1) {
          Object seleccionado = comboNombreP.getSelectedItem();
          String nombreProyecto = seleccionado.toString();
          String idProyecto = obtenerIdTipoProyectoPorNombre(nombreProyecto);
          idtipo_proyecto = idProyecto;
      }
    }
    
   public void inserta() {
       
       obtenIdEmpleado();
       obtenIdCliente();
       obtenIdTipoProyecto();
       
       try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "INSERT INTO Proyecto.Proyecto" +
                    "(id_emp_supervisor,idCliente,idTipo_Proyecto,estado,fecha_estimada,fecha_entrega,descuento,Total)" +
                    " VALUES ("+ idempleado + ","+ idcliente +"," + idtipo_proyecto + ",'" + comboEstado.getSelectedItem() + "','" + dateEstimada.getDate() + "', 'Pendiente'," + txtDesc.getText() + "," + txtTotal.getText() + ")";
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null , ex);
        }
    }
   
   public void elimina(){
      
       
       try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "DELETE FROM Proyecto.Proyecto  WHERE idProyecto= " + id[0] + "";
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
   
   public void modifica(){
        
       obtenIdEmpleado();
       obtenIdCliente();
       obtenIdTipoProyecto();
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query ="UPDATE Proyecto.Proyecto " +
                    "SET id_emp_supervisor = "+ idempleado + " ,idCliente = " + idcliente + ",idTipo_proyecto = " + idtipo_proyecto +" ,estado = '" + comboEstado.getSelectedItem()+ "', fecha_estimada = '" + dateEstimada.getDate() + "', fecha_entrega = 'Pendiente', descuento = " + txtDesc.getText() + ", Total = '"+txtTotal.getText() +"' " +
                    " WHERE idProyecto = " + id[0];
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null , ex);
        }
    }
   
   public void entregaProyecto(){
       
       try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            dateEntrega.getDate();
            
            SimpleDateFormat formatofech = new SimpleDateFormat("dd/MM/YYYY");
            String fec = formatofech.format(dateEntrega.getDate());
            
            String Query = "UPDATE Proyecto.Proyecto " +
                    "SET fecha_entrega = '" + fec + "', estado = 'terminado' WHERE idProyecto = " + id[0];
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
           
            String Mensaje = "Proyecto ya entregado";
            JOptionPane.showMessageDialog(null , Mensaje);
           
        }
       
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
        btnNuevoProyecto = new javax.swing.JButton();
        btnEntregaProye = new javax.swing.JButton();
        btnInsumo = new javax.swing.JButton();

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
        comboNombreP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                comboNombrePFocusGained(evt);
            }
        });

        comboEstado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Comienzo", "Pendiente" }));

        btnInserta.setText("Insertar");
        btnInserta.setToolTipText("");
        btnInserta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertaActionPerformed(evt);
            }
        });

        btnModifica.setText("Modificar");
        btnModifica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificaActionPerformed(evt);
            }
        });

        btnElimina.setText("Eliminar");
        btnElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminaActionPerformed(evt);
            }
        });

        btnNuevoProyecto.setText("Nuevo Proyecto");
        btnNuevoProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProyectoActionPerformed(evt);
            }
        });

        btnEntregaProye.setText("Entregar Proyecto");
        btnEntregaProye.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregaProyeActionPerformed(evt);
            }
        });

        btnInsumo.setText("Agregar Insumo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInserta)
                        .addGap(18, 18, 18)
                        .addComponent(btnModifica)
                        .addGap(18, 18, 18)
                        .addComponent(btnElimina))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(dateEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(comboNombreP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(comboEstado, 0, 140, Short.MAX_VALUE))
                                .addGap(42, 42, 42)
                                .addComponent(btnNuevoProyecto))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(dateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnEntregaProye, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnInsumo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9)))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboLider, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(406, 406, 406))
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
                            .addComponent(comboNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoProyecto))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(dateEstimada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(dateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
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
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnInsumo)
                                .addGap(18, 18, 18)
                                .addComponent(btnEntregaProye))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboNombreC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(comboLider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertaActionPerformed
       inserta();
       muestra();
    }//GEN-LAST:event_btnInsertaActionPerformed

    private void btnNuevoProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProyectoActionPerformed
        Tipo_Proyecto TP = new Tipo_Proyecto();
        TP.show();
    }//GEN-LAST:event_btnNuevoProyectoActionPerformed

    private void btnEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminaActionPerformed
        elimina();
        muestra();
    }//GEN-LAST:event_btnEliminaActionPerformed

    private void btnModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificaActionPerformed
        modifica();
        muestra();
    }//GEN-LAST:event_btnModificaActionPerformed

    private void comboNombrePFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboNombrePFocusGained
        // TODO add your handling code here:
        actualizaCombo();
    }//GEN-LAST:event_comboNombrePFocusGained

    private void btnEntregaProyeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregaProyeActionPerformed
        entregaProyecto();
        muestra();
    }//GEN-LAST:event_btnEntregaProyeActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElimina;
    private javax.swing.JButton btnEntregaProye;
    private javax.swing.JButton btnInserta;
    private javax.swing.JButton btnInsumo;
    private javax.swing.JButton btnModifica;
    private javax.swing.JButton btnNuevoProyecto;
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


package carpinteria;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import carpinteria.models.BD;

/**
 *
 * @author alex_
 */
public class Tipo_Proyecto extends javax.swing.JFrame {

    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER = "postgres";
    private String PASS = "postgres";
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;
    private Connection conexion = null;
    
    public String id;
    
    public Tipo_Proyecto(BD bd) {
        initComponents();
        
        muestra();
        
        tablaTipoP.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    id=tablaTipoP.getValueAt(tablaTipoP.getSelectedRow(), 0).toString();
                    txtNombre.setText(tablaTipoP.getValueAt(tablaTipoP.getSelectedRow(), 1).toString());
                    txtPrecio.setText(tablaTipoP.getValueAt(tablaTipoP.getSelectedRow(), 2).toString());
                }
            }
        });

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
            String Query = "SELECT * FROM Proyecto.Tipo_Proyecto ORDER BY idTipo_Proyecto ASC";
            String[] datos = new String[3];
            ResultSet columnas = corrida.executeQuery(Query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("idTipo_Proyecto");
            model.addColumn("Nombre");
            model.addColumn("Precio");
            tablaTipoP.setModel(model);
            while (columnas.next()) {
                datos[0] = columnas.getString(1);
                datos[1] = columnas.getString(2);
                datos[2] = columnas.getString(3);
                model.addRow(datos);
            }
            
            txtNombre.setText("");
            txtPrecio.setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
    
    public void inserta() {
        try {
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            String Query = "INSERT INTO Proyecto.Tipo_Proyecto (Nombre_Proyecto,Precio) VALUES('" + txtNombre.getText()+ "', '" + txtPrecio.getText()+ "')" ;
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        } catch (Exception ex) {
            String Mensaje = "El proyecto ya existe";
            JOptionPane.showMessageDialog(null , Mensaje);
        }
    }
    
    public void modifica(){
        
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "UPDATE Proyecto.Tipo_Proyecto "
                            + "SET Nombre_Proyecto = '" + txtNombre.getText() +"',"
                            + "Precio = '" + txtPrecio.getText() + "'"
                            + " WHERE idTipo_Proyecto = " + id;
            
            corrida.executeUpdate(Query);
            corrida.close();
            conexion.close();
            
        }catch(Exception ex){
            String Mensaje = "El proyecto ya existe";
            JOptionPane.showMessageDialog(null , Mensaje);
        }
    }
    
    public void elimina(){
        try{
            conexion = conectaDB();
            java.sql.Statement corrida = conexion.createStatement();
            
            String Query = "DELETE FROM Proyecto.Tipo_Proyecto  WHERE idTipo_Proyecto= " + id + "";
            
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnInserta = new javax.swing.JButton();
        btnModifica = new javax.swing.JButton();
        btnElimina = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaTipoP = new javax.swing.JTable();
        txtNombre = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();

        setTitle("Tipo_Proyecto");
        setName("Tipo_Proyecto"); // NOI18N

        jLabel1.setText("Tipo Proyecto");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Precio:");

        btnInserta.setText("Insertar");
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

        tablaTipoP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaTipoP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnInserta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModifica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnElimina)))
                .addGap(22, 22, 22))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(237, 237, 237))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInserta)
                    .addComponent(btnModifica)
                    .addComponent(btnElimina))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertaActionPerformed
       inserta();
       muestra();
       
       
    }//GEN-LAST:event_btnInsertaActionPerformed

    private void btnModificaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificaActionPerformed
       modifica();
       muestra();
    }//GEN-LAST:event_btnModificaActionPerformed

    private void btnEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminaActionPerformed
       elimina();
       muestra();
    }//GEN-LAST:event_btnEliminaActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnElimina;
    private javax.swing.JButton btnInserta;
    private javax.swing.JButton btnModifica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaTipoP;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}

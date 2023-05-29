package carpinteria.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.JOptionPane;

import org.postgresql.util.PSQLException;

import carpinteria.Excepciones;

public class BD {
    private String HOST = "localhost";
    private String PUERTO = "5432";
    private String DB = "Carpinteria02";
    private String USER;
    private String PASS;
    public String url = "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + DB;

    public BD(String usuario, String password) {
        this.USER = usuario;
        this.PASS = password;
    }

    public Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = java.sql.DriverManager.getConnection(url, USER, PASS);
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    // Getter de los privilegios del usuario
    public ArrayList<String> obtenerPrivilegiosUsuario() {
        ArrayList<String> privilegios = new ArrayList<String>();
        try {
            Connection conexion = conectar();
            String sql = """
                    SELECT privilege_type as privilegios
                    FROM information_schema.table_privileges
                    WHERE grantee = ?
                    GROUP BY privilege_type
                        """;
            PreparedStatement st = conexion.prepareStatement(sql);
            st.setString(1, USER);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                privilegios.add(rs.getString("privilegios"));
            }
        } catch (PSQLException e) {
            // TODO: handle exception
            String mensaje = Excepciones.manejarExcepcionPSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            String mensaje = Excepciones.manejarExcepcionSQL(e);
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return privilegios;
    }

}

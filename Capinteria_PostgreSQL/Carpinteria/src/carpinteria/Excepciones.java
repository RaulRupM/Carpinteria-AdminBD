package carpinteria;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.postgresql.util.PSQLException;

public class Excepciones {
    public static String manejarExcepcionSQL(SQLException e) {
        String errorMessage;
        switch (e.getErrorCode()) {
            case 1062: // Violación de restricción única (duplicidad de clave primaria)
                errorMessage = "Error: Duplicidad de clave primaria.";
                break;
            case 1451: // Violación de restricción de integridad referencial
                errorMessage = "Error: El registro ya existe en otra tabla.";
                break;
            case 1146: // Tabla no encontrada
                errorMessage = "Error: La tabla especificada no existe.";
                break;
            default:
                errorMessage = "Error SQL desconocido.";
                break;
        }
        return errorMessage;
    }

    public static String manejarExcepcionPSQL(PSQLException e) {
        String sqlState = e.getSQLState();
        String sqlMessage = e.getMessage();
        Pattern pattern = Pattern.compile("\\(([-\\w.]+)\\)");
        Matcher matcher = pattern.matcher(sqlMessage);
        String valorDuplicado = "";
        if (matcher.find()) {
            valorDuplicado = matcher.group(1);
        } 
        String errorMessage;

        switch (sqlState) {
            case "23505": // Violación de restricción única (duplicidad de clave primaria)
                errorMessage = (valorDuplicado.isEmpty() ? "":valorDuplicado) + " ya existe.";
                break;
            case "23503": // Violación de restricción de integridad referencial
                errorMessage = "Error: el registro ya existe en otra tabla.";
                break;
            case "42P01": // Tabla no encontrada
                errorMessage = "Error: La tabla especificada no existe.";
                break;
            default:
                errorMessage = "Error PSQL desconocido.";
                break;
        }
        return errorMessage;
    }
}

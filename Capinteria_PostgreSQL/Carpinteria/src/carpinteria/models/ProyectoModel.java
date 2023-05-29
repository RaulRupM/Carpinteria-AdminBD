package carpinteria.models;

import java.math.BigDecimal;

public class ProyectoModel {
    private int idProyecto;
    private String nombreProyecto;
    private BigDecimal total;

    //Constructor con parámetros de idProyecto, nombreProyecto, total
    public ProyectoModel(int idProyecto, String nombreProyecto, BigDecimal total) {
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
        this.total = total;
    }

    //Getters y setters de idProyecto, nombreProyecto, total
    public int getIdProyecto() {
        return idProyecto;
    }
    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }
    public String getNombreProyecto() {
        return nombreProyecto;
    }
    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    

}

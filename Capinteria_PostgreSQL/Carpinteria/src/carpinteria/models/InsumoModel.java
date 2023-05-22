package carpinteria.models;

import java.math.BigDecimal;

public class InsumoModel implements ItemProyecto{
    private String nombre;
    private int cantidadDisponible;
    private BigDecimal precio;
    private BigDecimal id;
    
    // Constructor y métodos getters y setters
    
    public InsumoModel(BigDecimal id,String nombre, int cantidadDisponible, BigDecimal precio) {
        this.nombre = nombre;
        this.cantidadDisponible = cantidadDisponible;
        this.precio = precio;
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    @Override
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    @Override
    public BigDecimal getPrecio() {
        return precio;
    }
    @Override
    public BigDecimal getId() {
        return id;
    }
}

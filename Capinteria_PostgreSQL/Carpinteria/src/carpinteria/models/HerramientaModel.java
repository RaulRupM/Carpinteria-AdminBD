package carpinteria.models;
import java.math.BigDecimal;

public class HerramientaModel implements ItemProyecto {
    private String nombre;
    private String tipo;
    private String estado;
    private int cantidadDisponible;
    private BigDecimal precio;
    private BigDecimal id;

    public HerramientaModel(BigDecimal id,String nombre, String tipo, String estado, int cantidadDisponible, BigDecimal precio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.cantidadDisponible = cantidadDisponible;
        this.precio = precio;
        this.id = id;
    }
    
    // Constructor y métodos getters y setters
    public String getTipo() {
        return tipo;
    }
    public String setTipo(String tipo) {
        return this.tipo = tipo;
    }
    
    public String getEstado() {
        return estado;
    }

    public String setEstado(String estado) {
        return this.estado = estado;
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


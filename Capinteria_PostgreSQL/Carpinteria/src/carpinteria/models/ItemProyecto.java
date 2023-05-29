package carpinteria.models;

import java.math.BigDecimal;

public interface ItemProyecto {
    String getNombre();

    BigDecimal getId();

    int getCantidadDisponible();

    void setCantidadDisponible(int cantidadDisponible);

    BigDecimal getPrecio();

}

package utn.saborcito.El_saborcito_back.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreticket {
    private String nombreUsuario;
    private String descripcionProducto;
    private int cantidad;
    private int precioUnitario;
}
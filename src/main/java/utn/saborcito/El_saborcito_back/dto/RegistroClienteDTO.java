package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;

@Data
public class RegistroClienteDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;
    private String confirmPassword;

    // Dirección opcional
    private String calle;
    private Integer numero;
    private String cp;
    private Long localidadId; // FK a localidad
}

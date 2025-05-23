package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private UsuarioDTO usuario; // Asumiendo que UsuarioDTO ya existe y está en el mismo paquete o se importa
    // No se incluye historialPedidos para evitar data excesiva en el DTO de Pedido
}

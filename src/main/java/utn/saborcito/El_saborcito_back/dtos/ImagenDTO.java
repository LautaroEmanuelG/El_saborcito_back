package utn.saborcito.El_saborcito_back.dtos; // Corregido a dtos

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenDTO {
    private Long id;
    private String url;
    // No se incluye usuarioId aquí para mantener el DTO simple
}

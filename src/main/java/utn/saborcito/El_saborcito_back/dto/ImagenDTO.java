package utn.saborcito.El_saborcito_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImagenDTO {
    private Long id;
    private String url;
    // No incluimos UsuarioDTO aquí para evitar ciclos y mantenerlo simple,
    // a menos que sea estrictamente necesario para el contexto de PromocionDTO
}

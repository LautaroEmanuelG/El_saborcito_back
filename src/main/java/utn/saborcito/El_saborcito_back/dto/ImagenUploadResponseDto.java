package utn.saborcito.El_saborcito_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenUploadResponseDto {
    private Long imagenId;
    private String url;
    private String publicId;
    private String message;
    private Boolean success;
}

package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.models.Imagen;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class ImagenMapperImpl implements ImagenMapper {

    @Override
    public ImagenDTO toDTO(Imagen imagen) {
        if ( imagen == null ) {
            return null;
        }

        ImagenDTO imagenDTO = new ImagenDTO();

        imagenDTO.setId( imagen.getId() );
        imagenDTO.setUrl( imagen.getUrl() );

        return imagenDTO;
    }

    @Override
    public Imagen toEntity(ImagenDTO imagenDTO) {
        if ( imagenDTO == null ) {
            return null;
        }

        Imagen.ImagenBuilder imagen = Imagen.builder();

        imagen.id( imagenDTO.getId() );
        imagen.url( imagenDTO.getUrl() );

        return imagen.build();
    }
}

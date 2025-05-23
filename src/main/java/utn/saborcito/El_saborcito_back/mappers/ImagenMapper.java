package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.ImagenDTO;
import utn.saborcito.El_saborcito_back.models.Imagen;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    ImagenMapper INSTANCE = Mappers.getMapper(ImagenMapper.class);

    ImagenDTO toDTO(Imagen imagen);

    Imagen toEntity(ImagenDTO imagenDTO);
}

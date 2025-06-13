package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.models.Domicilio;

import java.util.List;

@Mapper(componentModel = "spring", uses = LocalidadMapper.class)
public interface DomicilioMapper {
    DomicilioMapper INSTANCE = Mappers.getMapper(DomicilioMapper.class);

    DomicilioDTO toDTO(Domicilio domicilio);

    @Mappings({
            @Mapping(target = "usuario", ignore = true),
            @Mapping(target = "principal", source = "principal"), // 👈 aseguramos este campo
            @Mapping(target = "localidad", source = "localidad")
    })
    Domicilio toEntity(DomicilioDTO domicilioDTO);

    List<DomicilioDTO> toDTOList(List<Domicilio> domicilios);


    List<Domicilio> toEntityList(List<DomicilioDTO> domiciliosDTOs);
}

package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.EmpresaDTO;
import utn.saborcito.El_saborcito_back.models.Empresa;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:29-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EmpresaMapperImpl implements EmpresaMapper {

    @Override
    public EmpresaDTO toDTO(Empresa empresa) {
        if ( empresa == null ) {
            return null;
        }

        EmpresaDTO empresaDTO = new EmpresaDTO();

        empresaDTO.setCuil( empresa.getCuil() );
        empresaDTO.setId( empresa.getId() );
        empresaDTO.setNombre( empresa.getNombre() );
        empresaDTO.setRazonSocial( empresa.getRazonSocial() );

        return empresaDTO;
    }

    @Override
    public Empresa toEntity(EmpresaDTO empresaDTO) {
        if ( empresaDTO == null ) {
            return null;
        }

        Empresa.EmpresaBuilder empresa = Empresa.builder();

        empresa.cuil( empresaDTO.getCuil() );
        empresa.id( empresaDTO.getId() );
        empresa.nombre( empresaDTO.getNombre() );
        empresa.razonSocial( empresaDTO.getRazonSocial() );

        return empresa.build();
    }
}

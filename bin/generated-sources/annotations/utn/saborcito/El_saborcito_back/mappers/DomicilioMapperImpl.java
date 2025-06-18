package utn.saborcito.El_saborcito_back.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.DomicilioDTO;
import utn.saborcito.El_saborcito_back.models.Domicilio;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class DomicilioMapperImpl implements DomicilioMapper {

    @Autowired
    private LocalidadMapper localidadMapper;

    @Override
    public DomicilioDTO toDTO(Domicilio domicilio) {
        if ( domicilio == null ) {
            return null;
        }

        DomicilioDTO domicilioDTO = new DomicilioDTO();

        domicilioDTO.setCalle( domicilio.getCalle() );
        domicilioDTO.setCp( domicilio.getCp() );
        domicilioDTO.setId( domicilio.getId() );
        domicilioDTO.setLatitud( domicilio.getLatitud() );
        domicilioDTO.setLocalidad( localidadMapper.toDTO( domicilio.getLocalidad() ) );
        domicilioDTO.setLongitud( domicilio.getLongitud() );
        domicilioDTO.setNumero( domicilio.getNumero() );

        return domicilioDTO;
    }

    @Override
    public Domicilio toEntity(DomicilioDTO domicilioDTO) {
        if ( domicilioDTO == null ) {
            return null;
        }

        Domicilio.DomicilioBuilder domicilio = Domicilio.builder();

        domicilio.localidad( localidadMapper.toEntity( domicilioDTO.getLocalidad() ) );
        domicilio.calle( domicilioDTO.getCalle() );
        domicilio.cp( domicilioDTO.getCp() );
        domicilio.id( domicilioDTO.getId() );
        domicilio.latitud( domicilioDTO.getLatitud() );
        domicilio.longitud( domicilioDTO.getLongitud() );
        domicilio.numero( domicilioDTO.getNumero() );

        return domicilio.build();
    }

    @Override
    public List<DomicilioDTO> toDTOList(List<Domicilio> domicilios) {
        if ( domicilios == null ) {
            return null;
        }

        List<DomicilioDTO> list = new ArrayList<DomicilioDTO>( domicilios.size() );
        for ( Domicilio domicilio : domicilios ) {
            list.add( toDTO( domicilio ) );
        }

        return list;
    }

    @Override
    public List<Domicilio> toEntityList(List<DomicilioDTO> domiciliosDTOs) {
        if ( domiciliosDTOs == null ) {
            return null;
        }

        List<Domicilio> list = new ArrayList<Domicilio>( domiciliosDTOs.size() );
        for ( DomicilioDTO domicilioDTO : domiciliosDTOs ) {
            list.add( toEntity( domicilioDTO ) );
        }

        return list;
    }
}

package utn.saborcito.El_saborcito_back.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.HorarioAtencionDTO;
import utn.saborcito.El_saborcito_back.dto.SucursalDTO;
import utn.saborcito.El_saborcito_back.models.HorarioAtencion;
import utn.saborcito.El_saborcito_back.models.Sucursal;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:36-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class SucursalMapperImpl implements SucursalMapper {

    @Autowired
    private DomicilioMapper domicilioMapper;
    @Autowired
    private EmpresaMapper empresaMapper;
    @Autowired
    private HorarioAtencionMapper horarioAtencionMapper;

    @Override
    public SucursalDTO toDTO(Sucursal sucursal) {
        if ( sucursal == null ) {
            return null;
        }

        SucursalDTO sucursalDTO = new SucursalDTO();

        sucursalDTO.setDomicilio( domicilioMapper.toDTO( sucursal.getDomicilio() ) );
        sucursalDTO.setEmpresa( empresaMapper.toDTO( sucursal.getEmpresa() ) );
        sucursalDTO.setHorarios( horarioAtencionListToHorarioAtencionDTOList( sucursal.getHorarios() ) );
        sucursalDTO.setId( sucursal.getId() );
        sucursalDTO.setNombre( sucursal.getNombre() );

        return sucursalDTO;
    }

    @Override
    public Sucursal toEntity(SucursalDTO sucursalDTO) {
        if ( sucursalDTO == null ) {
            return null;
        }

        Sucursal.SucursalBuilder sucursal = Sucursal.builder();

        sucursal.domicilio( domicilioMapper.toEntity( sucursalDTO.getDomicilio() ) );
        sucursal.empresa( empresaMapper.toEntity( sucursalDTO.getEmpresa() ) );
        sucursal.horarios( horarioAtencionDTOListToHorarioAtencionList( sucursalDTO.getHorarios() ) );
        sucursal.id( sucursalDTO.getId() );
        sucursal.nombre( sucursalDTO.getNombre() );

        return sucursal.build();
    }

    @Override
    public List<SucursalDTO> toDTOList(List<Sucursal> sucursales) {
        if ( sucursales == null ) {
            return null;
        }

        List<SucursalDTO> list = new ArrayList<SucursalDTO>( sucursales.size() );
        for ( Sucursal sucursal : sucursales ) {
            list.add( toDTO( sucursal ) );
        }

        return list;
    }

    protected List<HorarioAtencionDTO> horarioAtencionListToHorarioAtencionDTOList(List<HorarioAtencion> list) {
        if ( list == null ) {
            return null;
        }

        List<HorarioAtencionDTO> list1 = new ArrayList<HorarioAtencionDTO>( list.size() );
        for ( HorarioAtencion horarioAtencion : list ) {
            list1.add( horarioAtencionMapper.toDTO( horarioAtencion ) );
        }

        return list1;
    }

    protected List<HorarioAtencion> horarioAtencionDTOListToHorarioAtencionList(List<HorarioAtencionDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<HorarioAtencion> list1 = new ArrayList<HorarioAtencion>( list.size() );
        for ( HorarioAtencionDTO horarioAtencionDTO : list ) {
            list1.add( horarioAtencionMapper.toEntity( horarioAtencionDTO ) );
        }

        return list1;
    }
}

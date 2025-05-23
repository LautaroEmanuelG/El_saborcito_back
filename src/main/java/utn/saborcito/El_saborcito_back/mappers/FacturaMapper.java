package utn.saborcito.El_saborcito_back.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.models.Factura;

@Mapper(componentModel = "spring", uses = { FormaPagoMapper.class, PedidoMapper.class })
public interface FacturaMapper extends BaseMapper<Factura, FacturaDTO> {

    @Override
    @Mapping(source = "formaPago", target = "formaPago")
    @Mapping(source = "pedido", target = "pedido")
    FacturaDTO toDTO(Factura source);

    @Override
    // Los objetos complejos como Pedido y FormaPago se cargarán en el servicio
    // utilizando los IDs proporcionados en sus respectivos DTOs.
    // MapStruct intentará mapear los campos de PedidoDTO a Pedido, pero como
    // PedidoMapper
    // probablemente no tenga un toEntity completo (o no debería para este
    // contexto),
    // es mejor dejar que el servicio maneje la asignación de la entidad Pedido.
    // Lo mismo aplica para FormaPago.
    // Las advertencias sobre propiedades no mapeadas dentro de Pedido (ej.
    // cliente.usuario.password)
    // se deben a que PedidoMapper.toEntity no está (o no debería estar) mapeando
    // esas profundidades.
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "formaPago", ignore = true)
    Factura toEntity(FacturaDTO source);
}

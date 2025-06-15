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
    @Mapping(source = "pedido.cliente.email", target = "clienteEmail")
    FacturaDTO toDTO(Factura source);

    @Override
    @Mapping(target = "pedido", ignore = true)
    @Mapping(target = "formaPago", ignore = true)
    Factura toEntity(FacturaDTO source);
}

package utn.saborcito.El_saborcito_back.mappers;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utn.saborcito.El_saborcito_back.dto.FacturaDTO;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Factura;
import utn.saborcito.El_saborcito_back.models.Pedido;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T22:00:35-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class FacturaMapperImpl implements FacturaMapper {

    @Autowired
    private FormaPagoMapper formaPagoMapper;
    @Autowired
    private PedidoMapper pedidoMapper;

    @Override
    public FacturaDTO toDTO(Factura source) {
        if ( source == null ) {
            return null;
        }

        FacturaDTO.FacturaDTOBuilder facturaDTO = FacturaDTO.builder();

        facturaDTO.formaPago( formaPagoMapper.toDTO( source.getFormaPago() ) );
        facturaDTO.pedido( pedidoMapper.toDTO( source.getPedido() ) );
        facturaDTO.clienteEmail( sourcePedidoClienteEmail( source ) );
        facturaDTO.ajustada( source.getAjustada() );
        facturaDTO.fechaFacturacion( source.getFechaFacturacion() );
        facturaDTO.id( source.getId() );
        facturaDTO.montoTotalAcreditado( source.getMontoTotalAcreditado() );
        facturaDTO.mpMerchantOrderId( source.getMpMerchantOrderId() );
        facturaDTO.mpPaymentId( source.getMpPaymentId() );
        facturaDTO.mpPaymentType( source.getMpPaymentType() );
        facturaDTO.mpPreferenceId( source.getMpPreferenceId() );
        facturaDTO.totalVenta( source.getTotalVenta() );

        return facturaDTO.build();
    }

    @Override
    public Factura toEntity(FacturaDTO source) {
        if ( source == null ) {
            return null;
        }

        Factura.FacturaBuilder factura = Factura.builder();

        factura.ajustada( source.getAjustada() );
        factura.fechaFacturacion( source.getFechaFacturacion() );
        factura.id( source.getId() );
        factura.montoTotalAcreditado( source.getMontoTotalAcreditado() );
        factura.mpMerchantOrderId( source.getMpMerchantOrderId() );
        factura.mpPaymentId( source.getMpPaymentId() );
        factura.mpPaymentType( source.getMpPaymentType() );
        factura.mpPreferenceId( source.getMpPreferenceId() );
        factura.totalVenta( source.getTotalVenta() );

        return factura.build();
    }

    private String sourcePedidoClienteEmail(Factura factura) {
        if ( factura == null ) {
            return null;
        }
        Pedido pedido = factura.getPedido();
        if ( pedido == null ) {
            return null;
        }
        Cliente cliente = pedido.getCliente();
        if ( cliente == null ) {
            return null;
        }
        String email = cliente.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }
}

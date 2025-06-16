package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.models.ArticuloManufacturado;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import utn.saborcito.El_saborcito_back.mappers.PedidoMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistorialService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    // ✅ IDs de estados fijos
    private static final Long ESTADO_LISTO_ID = 4L;
    private static final Long ESTADO_DELIVERY_ID = 5L;
    private static final Long ESTADO_ENTREGADO_ID = 6L;
    private static final Long ESTADO_FACTURADO_ID = 7L; // Si existe

    public List<PedidoDTO> obtenerPedidosFinalizados() {
        // 🔧 Usar IDs en lugar de nombres
        List<Long> estadosFinalizados = List.of(
                ESTADO_LISTO_ID,
                ESTADO_DELIVERY_ID,
                ESTADO_ENTREGADO_ID
                // ESTADO_FACTURADO_ID // Agregar si existe
        );

        return pedidoRepository.findByEstado_IdIn(estadosFinalizados)
                .stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoConRecetasDTO obtenerDetalleCompletoPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        PedidoConRecetasDTO dto = new PedidoConRecetasDTO();
        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFechaPedido());
        dto.setCliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());

        List<DetalleConRecetaDTO> detalles = pedido.getDetalles().stream()
                .map(det -> {
                    DetalleConRecetaDTO detalleDTO = new DetalleConRecetaDTO();
                    detalleDTO.setArticuloNombre(det.getArticulo().getDenominacion());
                    detalleDTO.setCantidad(det.getCantidad());

                    if (det.getArticulo() instanceof ArticuloManufacturado manufacturado) {
                        detalleDTO.setEsManufacturado(true);

                        List<IngredienteDTO> receta = manufacturado.getArticuloManufacturadoDetalles().stream()
                                .map(ing -> new IngredienteDTO(
                                        ing.getArticuloInsumo().getDenominacion(),
                                        ing.getCantidad(),
                                        ing.getArticuloInsumo().getUnidadMedida().getDenominacion()
                                ))
                                .collect(Collectors.toList());

                        detalleDTO.setReceta(receta);
                    } else {
                        detalleDTO.setEsManufacturado(false);
                        detalleDTO.setReceta(List.of());
                    }

                    return detalleDTO;
                })
                .collect(Collectors.toList());

        dto.setDetalles(detalles);
        return dto;
    }
    public byte[] generarPdfPedido(Long id) {
        PedidoConRecetasDTO dto = obtenerDetalleCompletoPedido(id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font subFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font textFont = new Font(Font.HELVETICA, 10);

        document.add(new Paragraph("Detalle de Pedido #" + dto.getId(), titleFont));
        document.add(new Paragraph("Cliente: " + dto.getCliente(), textFont));
        document.add(new Paragraph("Fecha: " + dto.getFecha(), textFont));
        document.add(new Paragraph(" "));

        for (DetalleConRecetaDTO det : dto.getDetalles()) {
            document.add(new Paragraph("Producto: " + det.getArticuloNombre(), subFont));
            document.add(new Paragraph("Cantidad: " + det.getCantidad(), textFont));
            if (Boolean.TRUE.equals(det.getEsManufacturado())) {
                document.add(new Paragraph("Receta:", textFont));
                for (IngredienteDTO ing : det.getReceta()) {
                    document.add(new Paragraph(
                            "- " + ing.getNombre() + " (" + ing.getCantidad() + " " + ing.getUnidadMedida() + ")",
                            textFont
                    ));
                }
            }
            document.add(new Paragraph(" "));
        }

        document.close();
        return baos.toByteArray();
    }
}

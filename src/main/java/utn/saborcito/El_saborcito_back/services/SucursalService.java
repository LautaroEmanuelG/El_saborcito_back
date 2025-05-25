package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import utn.saborcito.El_saborcito_back.dto.ClienteRankingDTO;
import utn.saborcito.El_saborcito_back.dto.MovimientoMonetarioDTO;
import utn.saborcito.El_saborcito_back.dto.ProductoRankingDTO;
import utn.saborcito.El_saborcito_back.dto.SucursalDTO;
import utn.saborcito.El_saborcito_back.mappers.SucursalMapper;
import utn.saborcito.El_saborcito_back.models.Articulo;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.DetallePedido;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Pedido;
import utn.saborcito.El_saborcito_back.models.Sucursal;
import utn.saborcito.El_saborcito_back.repositories.DetallePedidoRepository;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.PedidoRepository;
import utn.saborcito.El_saborcito_back.repositories.SucursalRepository;
import java.util.Map;
import java.util.Comparator;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SucursalService {
    private final SucursalRepository repo;
    private final DomicilioRepository domicilioRepository;
    private final SucursalMapper sucursalMapper;
    private final DetallePedidoRepository detallePedidoRepo;
    private final PedidoRepository pedidoRepository;


    public MovimientoMonetarioDTO getMovimientos(LocalDate desde, LocalDate hasta) {
        List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);

        double ingresos = pedidos.stream()
                .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                .sum();

        double costos = pedidos.stream()
                .mapToDouble(p -> p.calcularCostoTotal())
                .sum();

        double ganancias = ingresos - costos;

        return new MovimientoMonetarioDTO(ingresos, costos, ganancias);
    }



    public List<ClienteRankingDTO> getRankingClientes(LocalDate desde, LocalDate hasta, String ordenarPor) {
    List<Pedido> pedidos = pedidoRepository.findAllByFechaPedidoBetween(desde, hasta);

    Map<Cliente, List<Pedido>> pedidosPorCliente = pedidos.stream()
        .collect(Collectors.groupingBy(Pedido::getCliente));

        List<ClienteRankingDTO> ranking = pedidosPorCliente.entrySet().stream()
        .map(entry -> {
            Cliente cliente = entry.getKey();
            List<Pedido> pedidosCliente = entry.getValue();
            long cantidad = pedidosCliente.size();
            double total = pedidosCliente.stream().mapToDouble(Pedido::getTotal).sum();
            return new ClienteRankingDTO(
                cliente.getId(),
                cliente.getUsuario().getNombre() + " " + cliente.getUsuario().getApellido(),
                cantidad,
                total
            );
        })
        .sorted((a, b) -> {
            if ("importe".equalsIgnoreCase(ordenarPor)) {
                return Double.compare(b.getTotalImporte(), a.getTotalImporte());
            } else {
                return Long.compare(b.getCantidadPedidos(), a.getCantidadPedidos());
            }
        })
        .collect(Collectors.toList());

        return ranking;
    }


    public List<ProductoRankingDTO> getRankingProductos(LocalDate desde, LocalDate hasta) {
        List<DetallePedido> detalles = detallePedidoRepo.findAllByPedido_FechaPedidoBetween(desde, hasta);

        Map<Articulo, Long> conteo = new HashMap<>();
        for (DetallePedido detalle : detalles) {
            conteo.merge(detalle.getArticulo(), detalle.getCantidad().longValue(), Long::sum);
        }

        return conteo.entrySet().stream()
                .map(entry -> {
                    Articulo articulo = entry.getKey();
                    String tipo = articulo.getClass().getSimpleName().equals("ArticuloManufacturado") ? "MANUFACTURADO" : "INSUMO";
                    return new ProductoRankingDTO(
                            articulo.getId(),
                            articulo.getDenominacion(),
                            entry.getValue(),
                            tipo
                    );
                })
                .sorted(Comparator.comparingLong(ProductoRankingDTO::getCantidadVendida).reversed())
                .collect(Collectors.toList());
    }

    public List<SucursalDTO> findAll() {
        return repo.findAll().stream()
                .map(sucursalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public SucursalDTO findById(Long id) {
        Sucursal sucursal = repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));
        return sucursalMapper.toDTO(sucursal);
    }

    public SucursalDTO create(SucursalDTO dto) {
        Sucursal suc = sucursalMapper.toEntity(dto);

        // Validaciones existentes (adaptadas si es necesario para trabajar con la
        // entidad 'suc' antes de guardar)
        if (suc.getDomicilio() != null && suc.getDomicilio().getId() != null) {
            Domicilio domicilioExistente = domicilioRepository.findById(suc.getDomicilio().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Domicilio proporcionado para la nueva sucursal no encontrado con ID: "
                                    + suc.getDomicilio().getId()));
            suc.setDomicilio(domicilioExistente);
        } else if (suc.getDomicilio() != null) {
            // Si el domicilio es nuevo (sin ID), se guardará por cascada.
            // Asegurarse que el usuario dentro de domicilio no intente crearse si ya existe
            // o si no se desea.
            // Por simplicidad, asumimos que si viene un domicilio nuevo, se crea.
            // Considerar la lógica para el usuario dentro del domicilio si es necesario.
            if (suc.getDomicilio().getUsuario() != null && suc.getDomicilio().getUsuario().getId() != null) {
                // Lógica para asociar usuario existente si es necesario, o validar.
            }
        }

        if (suc.getEmpresa() != null && suc.getEmpresa().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La empresa asociada a la sucursal debe tener un ID válido o ser nula.");
        }
        // Aquí se podrían añadir más validaciones, como verificar que la empresa exista
        // en la BD si se proporciona un ID.

        Sucursal savedSucursal = repo.save(suc);
        return sucursalMapper.toDTO(savedSucursal);
    }

    public SucursalDTO update(Long id, SucursalDTO dto) {
        Sucursal existingSucursal = repo.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id));

        // Mapear los campos actualizables del DTO a la entidad existente
        // Esto podría ser más granular si no todos los campos del DTO son actualizables
        // o si se necesita lógica especial.

        existingSucursal.setNombre(dto.getNombre());

        // Manejo del domicilio
        if (dto.getDomicilio() != null) {
            if (dto.getDomicilio().getId() != null) {
                Domicilio domicilioAActualizar = domicilioRepository.findById(dto.getDomicilio().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Domicilio no encontrado con ID: " + dto.getDomicilio().getId()));
                // Actualizar campos del domicilio existente desde el DTO
                domicilioAActualizar.setCalle(dto.getDomicilio().getCalle());
                domicilioAActualizar.setNumero(dto.getDomicilio().getNumero());
                domicilioAActualizar.setCp(dto.getDomicilio().getCp());
                // Manejar localidad si es necesario (asumimos que la localidad se asigna por ID
                // y ya existe)
                // Si la localidad también puede cambiar, se necesitaría lógica similar.
                existingSucursal.setDomicilio(domicilioAActualizar);
            } else {
                // Si se envía un domicilio sin ID en una actualización, podría significar crear
                // uno nuevo
                // o podría ser un error. Aquí se asume que se reemplaza/crea.
                // Esta lógica puede necesitar ser más robusta dependiendo de los requisitos.
                Domicilio nuevoDomicilio = new Domicilio();
                nuevoDomicilio.setCalle(dto.getDomicilio().getCalle());
                nuevoDomicilio.setNumero(dto.getDomicilio().getNumero());
                nuevoDomicilio.setCp(dto.getDomicilio().getCp());
                // Asignar localidad (requiere que LocalidadDTO tenga ID y que exista)
                // Esta parte necesitaría el LocalidadMapper y LocalidadRepository si se
                // crea/actualiza profundamente
                existingSucursal.setDomicilio(nuevoDomicilio); // Esto podría requerir guardar el domicilio primero si
                                                               // no hay cascada completa
            }
        } else {
            existingSucursal.setDomicilio(null); // Si el DTO no trae domicilio, se elimina la asociación
        }

        // Manejo de la empresa (asumimos que se asigna por ID y ya existe)
        if (dto.getEmpresa() != null && dto.getEmpresa().getId() != null) {
            // Aquí se buscaría la empresa por ID y se asignaría.
            // Por simplicidad, y dado que el mapper lo haría si la entidad Empresa está en
            // el DTO:
            // Sucursal entidadActualizadaDesdeDto = sucursalMapper.toEntity(dto);
            // existingSucursal.setEmpresa(entidadActualizadaDesdeDto.getEmpresa());
            // O más directamente si solo se actualiza la referencia por ID:
            // Empresa empresa =
            // empresaRepository.findById(dto.getEmpresa().getId()).orElseThrow(...);
            // existingSucursal.setEmpresa(empresa);
            // Por ahora, si el mapper está configurado, esto podría funcionar, pero es
            // delicado.
            // Para ser más explícito, se debería cargar la entidad Empresa.
        } else {
            existingSucursal.setEmpresa(null);
        }

        // Horarios: Esto es más complejo. Si la lista de horarios puede cambiar
        // (añadir, quitar, modificar),
        // se necesita una lógica para sincronizar la colección.
        // MapStruct puede mapear colecciones, pero la actualización de colecciones JPA
        // a menudo requiere un manejo cuidadoso (borrar y recrear, o comparar y
        // actualizar elementos).
        // Por simplicidad, si el DTO trae horarios, se reemplazan.
        // Esto podría no ser lo ideal si los horarios tienen su propio ciclo de vida
        // complejo.
        // existingSucursal.setHorarios(sucursalMapper.toEntity(dto).getHorarios()); //
        // Esto requiere que HorarioMapper esté bien configurado

        Sucursal updatedSucursal = repo.save(existingSucursal);
        return sucursalMapper.toDTO(updatedSucursal);
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sucursal no encontrada con id " + id);
        repo.deleteById(id);
    }
}
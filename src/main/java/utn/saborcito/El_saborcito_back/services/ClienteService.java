package utn.saborcito.El_saborcito_back.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import utn.saborcito.El_saborcito_back.dto.*;
import utn.saborcito.El_saborcito_back.enums.Rol;
import utn.saborcito.El_saborcito_back.mappers.ClienteMapper;
import utn.saborcito.El_saborcito_back.models.Cliente;
import utn.saborcito.El_saborcito_back.models.Domicilio;
import utn.saborcito.El_saborcito_back.models.Localidad;
import utn.saborcito.El_saborcito_back.models.Usuario;
import utn.saborcito.El_saborcito_back.repositories.ClienteRepository;
import utn.saborcito.El_saborcito_back.repositories.DomicilioRepository;
import utn.saborcito.El_saborcito_back.repositories.LocalidadRepository;
import utn.saborcito.El_saborcito_back.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repo;
    private final ClienteMapper clienteMapper;
    private final UsuarioRepository usuarioRepository;
    private final DomicilioRepository domicilioRepository;
    private final LocalidadRepository localidadRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService usuarioService;

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Cliente findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id));
    }

    // ✅ NUEVO: Buscar por Auth0 ID - para sincronización (HU1, HU2)
    public Optional<ClienteDTO> findByAuth0Id(String auth0Id) {
        return usuarioService.findByAuth0Id(auth0Id)
                .map(usuarioDTO -> {
                    Cliente cliente = repo.findById(usuarioDTO.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Cliente no encontrado"));
                    return clienteMapper.toDTO(cliente);
                });
    }

    // ✅ MEJORADO: Registro que maneja Auth0 y manual (HU1)
    public ClienteDTO registrarCliente(RegistroClienteDTO dto) {
        // Usar validación centralizada del UsuarioService
        usuarioService.validarEmailUnico(dto.getEmail());
        if (dto.getEsRegistroAuth0() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo 'esRegistroAuth0' es requerido");
        } else if (dto.getEsRegistroAuth0()) {
            return registrarClienteAuth0(dto);
        } else {
            return registrarClienteManual(dto);
        }
    }

    // ✅ NUEVO: Registro manual tradicional (HU1)
    private ClienteDTO registrarClienteManual(RegistroClienteDTO dto) {
        // Validaciones coincidencia de contraseña para HU1
        if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }
        //validacion de password HU1
        if (!isValidPassword(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }
        return crearCliente(dto, false);
    }

    // ✅ NUEVO: Registro con Auth0 (HU1)
    private ClienteDTO registrarClienteAuth0(RegistroClienteDTO dto) {
        // Para Auth0, no necesitamos validar contraseña
        return crearCliente(dto, true);
    }

    // ✅ REFACTORIZADO: Método común para crear cliente
    private ClienteDTO crearCliente(RegistroClienteDTO dto, boolean esAuth0) {
        if (!isValidEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
        }
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        // Asignar rol y no modificable HU1
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        if (esAuth0) {
            usuario.setAuth0Id(dto.getAuth0Id());
        } else {
            // Encriptar contraseña HU1
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

// Guardar usuario primero
        usuario = usuarioRepository.save(usuario);

        List<Domicilio> domicilios = new ArrayList<>();

        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            for (DomicilioDTO domicilioDTO : dto.getDomicilios()) {
                if (domicilioDTO.getCalle() != null && domicilioDTO.getLocalidad() != null &&
                        domicilioDTO.getLocalidad().getId() != null) {

                    Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

                    Domicilio domicilio = new Domicilio();
                    domicilio.setCalle(domicilioDTO.getCalle());
                    domicilio.setNumero(domicilioDTO.getNumero());
                    domicilio.setCp(domicilioDTO.getCp());
                    domicilio.setLocalidad(localidad);
                    domicilio.setUsuario(usuario);
                    domicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());

                    domicilios.add(domicilio);
                }
            }
        }

        usuarioService.validarUnSoloDomicilioPrincipal(domicilios);
        usuarioService.validarAlMenosUnPrincipal(domicilios);

// ✅ CORRECTO: modificar la lista existente, no reemplazarla moverlo a usuario
        if (usuario.getDomicilios() == null) {
            usuario.setDomicilios(new ArrayList<>());
        }
        usuario.getDomicilios().clear();
        usuario.getDomicilios().addAll(domicilios);

        usuario.setFechaUltimaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);

        Cliente cliente = new Cliente(); //devolver DTO
        cliente.setId(usuario.getId());
        cliente.setDomicilios(usuario.getDomicilios());
        cliente.setHistorialPedidos(new ArrayList<>());
        if (cliente.getHistorialPedidos() != null && !cliente.getHistorialPedidos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El historial de pedidos no se puede asignar manualmente al crear un cliente.");
        }
        return clienteMapper.toDTO(repo.save(cliente)); //devoler DTO cliente + token
    }

    // ✅ MEJORADO: Actualización usando métodos de UsuarioService (HU3)
    public ClienteDTO updateCliente(Long id, ActualizarDatosClienteDTO dto) {
        Cliente clienteExistente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        Usuario usuario = clienteExistente;

        // Validar y actualizar datos básicos del usuario
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());

        // ✅ HU3: Validar y actualizar email si corresponde
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (!isValidEmail(dto.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de email inválido");
            }
            usuarioService.validarEmailUnico(dto.getEmail());
            usuario.setEmail(dto.getEmail());
        }
        // ✅ HU3: Actualizar dirección de entrega
        if (dto.getDomicilios() != null && !dto.getDomicilios().isEmpty()) {
            List<Domicilio> domiciliosExistentes = usuario.getDomicilios();

            for (DomicilioDTO domicilioDTO : dto.getDomicilios()) {
                if (domicilioDTO.getCalle() == null || domicilioDTO.getLocalidad() == null ||
                        domicilioDTO.getLocalidad().getId() == null) {
                    continue;
                }

                Localidad localidad = localidadRepository.findById(domicilioDTO.getLocalidad().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localidad no encontrada"));

                Optional<Domicilio> domicilioExistenteOpt = domiciliosExistentes.stream()
                        .filter(d -> d.getId() != null && d.getId().equals(domicilioDTO.getId()))
                        .findFirst();

                if (domicilioExistenteOpt.isPresent()) {
                    Domicilio domicilio = domicilioExistenteOpt.get();
                    domicilio.setCalle(domicilioDTO.getCalle());
                    domicilio.setNumero(domicilioDTO.getNumero());
                    domicilio.setCp(domicilioDTO.getCp());
                    domicilio.setLocalidad(localidad);
                    domicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domicilioRepository.save(domicilio);
                } else {
                    Domicilio nuevoDomicilio = new Domicilio();
                    nuevoDomicilio.setCalle(domicilioDTO.getCalle());
                    nuevoDomicilio.setNumero(domicilioDTO.getNumero());
                    nuevoDomicilio.setCp(domicilioDTO.getCp());
                    nuevoDomicilio.setLocalidad(localidad);
                    nuevoDomicilio.setUsuario(usuario);
                    nuevoDomicilio.setPrincipal(domicilioDTO.getPrincipal() != null && domicilioDTO.getPrincipal());
                    domiciliosExistentes.add(nuevoDomicilio);
                }
            }

            // ✅ CORRECTO: modificar la lista original, no reemplazarla
            if (usuario.getDomicilios() == null) {
                usuario.setDomicilios(new ArrayList<>());
            }
            usuario.getDomicilios().clear();
            usuario.getDomicilios().addAll(domiciliosExistentes);

            usuarioService.validarUnSoloDomicilioPrincipal(usuario.getDomicilios());
            usuarioService.validarAlMenosUnPrincipal(usuario.getDomicilios());
        }

// ✅ HU3: Cambio de contraseña
        if (dto.getNuevaContraseña() != null && !dto.getNuevaContraseña().isBlank()) {
            validarCambioContrasena(usuario.getId(), dto);
            usuarioService.cambiarContrasena(usuario.getId(), dto.getNuevaContraseña());
        } else {
            usuario.setFechaUltimaModificacion(LocalDateTime.now());
            usuarioRepository.save(usuario);
        }

        return clienteMapper.toDTO(clienteExistente);
    }

    // ✅ NUEVO: Validación de cambio de contraseña (HU3)
    private void validarCambioContrasena(Long usuarioId, ActualizarDatosClienteDTO dto) {
        if (!dto.getNuevaContraseña().equals(dto.getConfirmarContraseña())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
        }

        if (!isValidPassword(dto.getNuevaContraseña())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, minúscula y símbolo.");
        }

        // Usar validación centralizada del UsuarioService
        if (!usuarioService.validarContrasenaActual(usuarioId, dto.getContraseñaActual())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La contraseña actual es incorrecta");
        }
    }

    // ✅ MEJORADO: Login delegado a UsuarioService (HU2)
    public UsuarioDTO loginCliente(LoginRequest loginRequest) {
        UsuarioDTO usuario = usuarioService.login(loginRequest);

        // ✅ HU2: Validar que sea cliente
        if (!usuario.getRol().equals(Rol.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo los clientes pueden iniciar sesión aquí");
        }

        // ✅ HU2: Verificar estado activo (ya manejado en UsuarioService)
        return usuario;
    }

    // ✅ MEJORADO: Usar método centralizado de UsuarioService (HU7)
    public void bajaLogicaCliente(Long id) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id));

        usuarioService.bajaLogicaUsuario(cliente.getId());
    }

    // ✅ MEJORADO: Usar método centralizado de UsuarioService (HU7)
    public void altaCliente(Long id) {
        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con ID: " + id));

        usuarioService.altaUsuario(cliente.getId());
    }

    // ✅ NUEVO: Sincronizar usuario de Auth0 con BD local
    public ClienteDTO sincronizarAuth0Usuario(Auth0UserDTO auth0User) {
        Optional<ClienteDTO> clienteExistente = findByAuth0Id(auth0User.getSub());

        if (clienteExistente.isPresent()) {
            // Usuario ya existe, actualizar datos si es necesario
            return actualizarDatosAuth0(clienteExistente.get(), auth0User);
        } else {
            // Usuario nuevo de Auth0, crear registro local
            return crearClienteDesdeAuth0(auth0User);
        }
    }

    // ✅ NUEVO: Crear cliente desde datos de Auth0
    private ClienteDTO crearClienteDesdeAuth0(Auth0UserDTO auth0User) {
        RegistroClienteDTO dto = new RegistroClienteDTO();
        dto.setAuth0Id(auth0User.getSub());
        dto.setEmail(auth0User.getEmail());
        dto.setNombre(auth0User.getGivenName());
        dto.setApellido(auth0User.getFamilyName());
        dto.setEsRegistroAuth0(true);

        return registrarClienteAuth0(dto);
    }

    // ✅ NUEVO: Actualizar datos desde Auth0
    private ClienteDTO actualizarDatosAuth0(ClienteDTO cliente, Auth0UserDTO auth0User) {
        boolean datosActualizados = false;

        // Validar y actualizar nombre
        if (!cliente.getNombre().equals(auth0User.getGivenName())) {
            cliente.setNombre(auth0User.getGivenName());
            datosActualizados = true;
        }

        // Validar y actualizar apellido
        if (!cliente.getApellido().equals(auth0User.getFamilyName())) {
            cliente.setApellido(auth0User.getFamilyName());
            datosActualizados = true;
        }

        // Validar y actualizar email
        if (!cliente.getEmail().equals(auth0User.getEmail())) {
            cliente.setEmail(auth0User.getEmail());
            datosActualizados = true;
        }

        // Persistir cambios si hubo actualizaciones
        if (datosActualizados) {
            Cliente clienteEntity = clienteMapper.toEntity(cliente);
            clienteEntity = repo.save(clienteEntity);
            return clienteMapper.toDTO(clienteEntity);
        }

        return cliente; // No hubo cambios
    }

    // --- Métodos privados de validación (sin cambios) ---
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
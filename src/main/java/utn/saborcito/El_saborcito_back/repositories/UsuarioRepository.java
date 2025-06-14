package utn.saborcito.El_saborcito_back.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.saborcito.El_saborcito_back.models.Usuario;

import java.util.List;
import java.util.Optional;

@Repository

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByAuth0Id(String auth0Id); // ✅ AGREGAR
    List<Usuario> findByEstado(Boolean estado); // ✅ ÚTIL para admin
    boolean existsByAuth0Id(String auth0Id);
} //aun no se usa findByEstado

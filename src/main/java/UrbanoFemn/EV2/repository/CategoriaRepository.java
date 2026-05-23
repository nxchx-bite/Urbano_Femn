package UrbanoFemn.EV2.repository;

import UrbanoFemn.EV2.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


// JpaRepository entrega CRUD completo automáticamente.

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByActivaTrue();

    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
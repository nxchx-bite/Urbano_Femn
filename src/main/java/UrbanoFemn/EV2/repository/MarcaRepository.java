package UrbanoFemn.EV2.repository;

import UrbanoFemn.EV2.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// Permite acceder a los datos de la entidad Marca.


public interface MarcaRepository extends JpaRepository<Marca, Long> {

    List<Marca> findByActivaTrue();

    List<Marca> findByNombreContainingIgnoreCase(String nombre);
}

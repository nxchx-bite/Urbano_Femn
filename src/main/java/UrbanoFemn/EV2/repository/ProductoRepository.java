package UrbanoFemn.EV2.repository;

import UrbanoFemn.EV2.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Buscar productos por nombre.
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos activos.
    List<Producto> findByActivoTrue();

    // Buscar productos por categoría.
    List<Producto> findByCategoriaId(Long categoriaId);

    // Buscar productos por marca.
    List<Producto> findByMarcaId(Long marcaId);

    // Buscar productos por precio máximo.
    List<Producto> findByPrecioLessThanEqual(BigDecimal precioMax);

    // Buscar productos por color.
    List<Producto> findByColorContainingIgnoreCase(String color);

    // Buscar productos por talla.
    List<Producto> findByTallaContainingIgnoreCase(String talla);
}

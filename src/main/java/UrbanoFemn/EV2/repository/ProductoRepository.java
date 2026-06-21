package UrbanoFemn.EV2.repository;

import UrbanoFemn.EV2.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // 1. QUERY METHODS

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByMarcaId(Long marcaId);

    List<Producto> findByPrecioLessThanEqual(BigDecimal precioMax);

    List<Producto> findByColorContainingIgnoreCase(String color);

    List<Producto> findByTallaContainingIgnoreCase(String talla);


    // 2. JPQL

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio <= :precioMax")
    List<Producto> buscarProductosActivosPorPrecioJPQL(@Param("precioMax") BigDecimal precioMax);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorNombreODescripcionJPQL(@Param("texto") String texto);


    // 3. SQL NATIVO

    @Query(value = "SELECT * FROM productos WHERE stock > :stockMinimo", nativeQuery = true)
    List<Producto> buscarProductosConStockMayorSQL(@Param("stockMinimo") Integer stockMinimo);

    @Query(value = "SELECT * FROM productos WHERE categoria_id = :categoriaId AND activo = true", nativeQuery = true)
    List<Producto> buscarProductosActivosPorCategoriaSQL(@Param("categoriaId") Long categoriaId);
}

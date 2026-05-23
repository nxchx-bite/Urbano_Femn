package UrbanoFemn.EV2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// ═══════════════════════════════════════════════════
// ENTIDAD · Producto.java
// Representa un producto del catálogo de Graffity Glam.
// Ejemplos: Polera Oversize Negra, Jeans Tiro Alto,
// Chaqueta Denim, Vestido Floral.
// 
// Producto se relaciona con Categoria y Marca.
// ═══════════════════════════════════════════════════

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre visible del producto.
    @Column(nullable = false, length = 150)
    private String nombre;

    // Descripción del producto.
    @Column(length = 500)
    private String descripcion;

    // Precio del producto.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    // Stock disponible.
    @Column(nullable = false)
    private Integer stock;

    // Talla del producto.
    // Ejemplo: S, M, L, XL, 38, 40.
    @Column(nullable = false, length = 50)
    private String talla;

    // Color del producto.
    @Column(nullable = false, length = 50)
    private String color;

    // URL de imagen referencial.
    @Column(length = 500)
    private String imagenUrl;

    // Permite ocultar productos sin eliminarlos.
    @Column(nullable = false)
    private Boolean activo;

    // Muchos productos pertenecen a una categoría.
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Muchos productos pertenecen a una marca.
    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;
}

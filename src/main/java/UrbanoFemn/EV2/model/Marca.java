package UrbanoFemn.EV2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ═══════════════════════════════════════════════════
// ENTIDAD · Marca.java
// Representa una marca asociada a los productos.
// Ejemplos: Shein, Zara, Urban Style, Fashion Chic.
// Una marca puede estar asociada a muchos productos.
// ═══════════════════════════════════════════════════

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la marca.
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    // Descripción o información breve de la marca.
    @Column(length = 255)
    private String descripcion;

    // País o procedencia de la marca.
    @Column(length = 100)
    private String paisOrigen;

    // Permite activar o desactivar la marca
    @Column(nullable = false)
    private Boolean activa;
}

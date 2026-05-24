package UrbanoFemn.EV2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la categoría.
    // Ejemplo: "Poleras"
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    // Descripción breve de la categoría.
    @Column(length = 255)
    private String descripcion;

    // Permite activar o desactivar la categoría sin eliminarla.
    @Column(nullable = false)
    private Boolean activa;
}

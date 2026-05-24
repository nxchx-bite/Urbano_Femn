package UrbanoFemn.EV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String talla;
    private String color;
    private Boolean activo;
    private String categoriaNombre;
    private String marcaNombre;
}

package UrbanoFemn.EV2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MarcaResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String paisOrigen;
    private Boolean activa;
}

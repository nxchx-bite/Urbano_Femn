package UrbanoFemn.EV2.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class MarcaRequestDTO {
    @NotBlank(message = "El nombre de la marca no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @Size(max = 100, message = "El país de origen no puede superar los 100 caracteres")
    private String paisOrigen;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activa;
}

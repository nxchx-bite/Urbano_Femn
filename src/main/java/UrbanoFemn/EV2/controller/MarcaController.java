package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.service.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "Operaciones relacionadas con las marcas de productos")
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    @Operation(
            summary = "Listar todas las marcas",
            description = "Obtiene una lista con todas las marcas registradas en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Marcas listadas correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MarcaResponseDTO.class))
            )
    )
    public ResponseEntity<List<MarcaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(marcaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar marca por ID",
            description = "Obtiene una marca específica según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Marca encontrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<MarcaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la marca", example = "1")
            @PathVariable Long id) {

        return marcaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva marca",
            description = "Registra una nueva marca de productos en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Marca creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<MarcaResponseDTO> crear(
            @Valid @RequestBody MarcaRequestDTO dto) {

        MarcaResponseDTO nueva = marcaService.guardar(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar marca",
            description = "Actualiza los datos de una marca existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Marca actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MarcaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<MarcaResponseDTO> actualizar(
            @Parameter(description = "ID de la marca", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO dto) {

        return marcaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar marca",
            description = "Elimina una marca existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Marca eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marca no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la marca", example = "1")
            @PathVariable Long id) {

        if (marcaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    @Operation(
            summary = "Listar marcas activas",
            description = "Obtiene solo las marcas que se encuentran activas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Marcas activas listadas correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MarcaResponseDTO.class))
            )
    )
    public ResponseEntity<List<MarcaResponseDTO>> buscarActivas() {
        return ResponseEntity.ok(marcaService.buscarActivas());
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar marcas por nombre",
            description = "Busca marcas cuyo nombre contenga el texto enviado como parámetro."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MarcaResponseDTO.class))
            )
    )
    public ResponseEntity<List<MarcaResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre de la marca", example = "zara")
            @RequestParam String nombre) {

        return ResponseEntity.ok(marcaService.buscarPorNombre(nombre));
    }
}

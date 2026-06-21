package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @Operation(
            summary = "Listar todas las categorías",
            description = "Obtiene una lista con todas las categorías registradas en el sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categorías listadas correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar categoría por ID",
            description = "Obtiene una categoría específica según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría encontrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long id) {

        return categoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva categoría",
            description = "Registra una nueva categoría de productos en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Categoría creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO> crear(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        CategoriaResponseDTO nueva = categoriaService.guardar(dto);
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza los datos de una categoría existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoría actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriaResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        return categoriaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Categoría eliminada correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoría no encontrada",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long id) {

        if (categoriaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/activas")
    @Operation(
            summary = "Listar categorías activas",
            description = "Obtiene solo las categorías que se encuentran activas."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Categorías activas listadas correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoriaResponseDTO>> buscarActivas() {
        return ResponseEntity.ok(categoriaService.buscarActivas());
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar categorías por nombre",
            description = "Busca categorías cuyo nombre contenga el texto enviado como parámetro."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDTO.class))
            )
    )
    public ResponseEntity<List<CategoriaResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre de la categoría", example = "poleras")
            @RequestParam String nombre) {

        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombre));
    }
}


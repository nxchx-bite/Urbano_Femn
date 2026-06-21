package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.service.ProductoService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones relacionadas con el catálogo de productos")
public class ProdcutoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(
            summary = "Listar todos los productos",
            description = "Obtiene una lista con todos los productos registrados en el catálogo."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Productos listados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene un producto específico según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {

        return productoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo producto",
            description = "Registra un nuevo producto en el catálogo, asociándolo a una categoría y una marca mediante sus IDs."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<ProductoResponseDTO> crear(
            @Valid @RequestBody ProductoRequestDTO dto) {

        ProductoResponseDTO nuevo = productoService.guardar(dto);
        return ResponseEntity.status(201).body(nuevo);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud",
                    content = @Content
            )
    })
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {

        return productoService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto existente según su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {

        if (productoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar productos por nombre",
            description = "Busca productos cuyo nombre contenga el texto enviado como parámetro."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre del producto", example = "polera")
            @RequestParam String nombre) {

        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(
            summary = "Buscar productos por categoría",
            description = "Obtiene los productos asociados a una categoría específica."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Productos encontrados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorCategoria(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long categoriaId) {

        return ResponseEntity.ok(productoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/marca/{marcaId}")
    @Operation(
            summary = "Buscar productos por marca",
            description = "Obtiene los productos asociados a una marca específica."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Productos encontrados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorMarca(
            @Parameter(description = "ID de la marca", example = "1")
            @PathVariable Long marcaId) {

        return ResponseEntity.ok(productoService.buscarPorMarca(marcaId));
    }

    @GetMapping("/precio")
    @Operation(
            summary = "Buscar productos por precio máximo",
            description = "Obtiene los productos cuyo precio sea menor o igual al valor enviado."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Productos encontrados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDTO.class))
            )
    )
    public ResponseEntity<List<ProductoResponseDTO>> buscarPorPrecioMaximo(
            @Parameter(description = "Precio máximo permitido", example = "20000")
            @RequestParam BigDecimal precioMax) {

        return ResponseEntity.ok(productoService.buscarPorPrecioMaximo(precioMax));
    }
}

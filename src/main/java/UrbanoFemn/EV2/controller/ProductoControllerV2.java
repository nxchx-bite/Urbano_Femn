package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.assemblers.ProductoModelAssembler;
import UrbanoFemn.EV2.dto.ProductoRequestDTO;
import UrbanoFemn.EV2.dto.ProductoResponseDTO;
import UrbanoFemn.EV2.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/productos")
@RequiredArgsConstructor
@Tag(name = "Productos HATEOAS", description = "Versión 2 de productos con enlaces HATEOAS")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar productos con HATEOAS")
    public CollectionModel<EntityModel<ProductoResponseDTO>> obtenerTodos() {

        List<EntityModel<ProductoResponseDTO>> productos = productoService.obtenerTodos()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar producto por ID con HATEOAS")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> obtenerPorId(@PathVariable Long id) {

        return productoService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear producto con HATEOAS")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> crear(
            @Valid @RequestBody ProductoRequestDTO dto) {

        ProductoResponseDTO nuevoProducto = productoService.guardar(dto);

        return ResponseEntity
                .created(linkTo(methodOn(ProductoControllerV2.class).obtenerPorId(nuevoProducto.getId())).toUri())
                .body(assembler.toModel(nuevoProducto));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar producto con HATEOAS")
    public ResponseEntity<EntityModel<ProductoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {

        return productoService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (productoService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar productos por nombre con HATEOAS")
    public CollectionModel<EntityModel<ProductoResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        List<EntityModel<ProductoResponseDTO>> productos = productoService.buscarPorNombre(nombre)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"));
    }

    @GetMapping(value = "/categoria/{categoriaId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar productos por categoría con HATEOAS")
    public CollectionModel<EntityModel<ProductoResponseDTO>> buscarPorCategoria(
            @PathVariable Long categoriaId) {

        List<EntityModel<ProductoResponseDTO>> productos = productoService.buscarPorCategoria(categoriaId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).buscarPorCategoria(categoriaId)).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"));
    }

    @GetMapping(value = "/marca/{marcaId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar productos por marca con HATEOAS")
    public CollectionModel<EntityModel<ProductoResponseDTO>> buscarPorMarca(
            @PathVariable Long marcaId) {

        List<EntityModel<ProductoResponseDTO>> productos = productoService.buscarPorMarca(marcaId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).buscarPorMarca(marcaId)).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"));
    }

    @GetMapping(value = "/precio", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar productos por precio máximo con HATEOAS")
    public CollectionModel<EntityModel<ProductoResponseDTO>> buscarPorPrecioMaximo(
            @RequestParam BigDecimal precioMax) {

        List<EntityModel<ProductoResponseDTO>> productos = productoService.buscarPorPrecioMaximo(precioMax)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoControllerV2.class).buscarPorPrecioMaximo(precioMax)).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"));
    }
}
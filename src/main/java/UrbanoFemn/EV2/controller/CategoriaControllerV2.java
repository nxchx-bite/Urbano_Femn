package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.assemblers.CategoriaModelAssembler;
import UrbanoFemn.EV2.dto.CategoriaRequestDTO;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import UrbanoFemn.EV2.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorías HATEOAS", description = "Versión 2 de categorías con enlaces HATEOAS")
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar categorías con HATEOAS")
    public CollectionModel<EntityModel<CategoriaResponseDTO>> obtenerTodas() {

        List<EntityModel<CategoriaResponseDTO>> categorias = categoriaService.obtenerTodas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodas()).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).buscarActivas()).withRel("categorias-activas"));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar categoría por ID con HATEOAS")
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> obtenerPorId(@PathVariable Long id) {

        return categoriaService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear categoría con HATEOAS")
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> crear(
            @Valid @RequestBody CategoriaRequestDTO dto) {

        CategoriaResponseDTO nuevaCategoria = categoriaService.guardar(dto);

        return ResponseEntity
                .created(linkTo(methodOn(CategoriaControllerV2.class).obtenerPorId(nuevaCategoria.getId())).toUri())
                .body(assembler.toModel(nuevaCategoria));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar categoría con HATEOAS")
    public ResponseEntity<EntityModel<CategoriaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO dto) {

        return categoriaService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (categoriaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/activas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar categorías activas con HATEOAS")
    public CollectionModel<EntityModel<CategoriaResponseDTO>> buscarActivas() {

        List<EntityModel<CategoriaResponseDTO>> categorias = categoriaService.buscarActivas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerV2.class).buscarActivas()).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodas()).withRel("categorias"));
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar categorías por nombre con HATEOAS")
    public CollectionModel<EntityModel<CategoriaResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        List<EntityModel<CategoriaResponseDTO>> categorias = categoriaService.buscarPorNombre(nombre)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categorias,
                linkTo(methodOn(CategoriaControllerV2.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodas()).withRel("categorias"));
    }
}
package UrbanoFemn.EV2.controller;

import UrbanoFemn.EV2.assemblers.MarcaModelAssembler;
import UrbanoFemn.EV2.dto.MarcaRequestDTO;
import UrbanoFemn.EV2.dto.MarcaResponseDTO;
import UrbanoFemn.EV2.service.MarcaService;
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
@RequestMapping("/api/v2/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas HATEOAS", description = "Versión 2 de marcas con enlaces HATEOAS")
public class MarcaControllerV2 {

    private final MarcaService marcaService;
    private final MarcaModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar marcas con HATEOAS")
    public CollectionModel<EntityModel<MarcaResponseDTO>> obtenerTodas() {

        List<EntityModel<MarcaResponseDTO>> marcas = marcaService.obtenerTodas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(marcas,
                linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withSelfRel(),
                linkTo(methodOn(MarcaControllerV2.class).buscarActivas()).withRel("marcas-activas"));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar marca por ID con HATEOAS")
    public ResponseEntity<EntityModel<MarcaResponseDTO>> obtenerPorId(@PathVariable Long id) {

        return marcaService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear marca con HATEOAS")
    public ResponseEntity<EntityModel<MarcaResponseDTO>> crear(
            @Valid @RequestBody MarcaRequestDTO dto) {

        MarcaResponseDTO nuevaMarca = marcaService.guardar(dto);

        return ResponseEntity
                .created(linkTo(methodOn(MarcaControllerV2.class).obtenerPorId(nuevaMarca.getId())).toUri())
                .body(assembler.toModel(nuevaMarca));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar marca con HATEOAS")
    public ResponseEntity<EntityModel<MarcaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MarcaRequestDTO dto) {

        return marcaService.actualizar(id, dto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        if (marcaService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/activas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar marcas activas con HATEOAS")
    public CollectionModel<EntityModel<MarcaResponseDTO>> buscarActivas() {

        List<EntityModel<MarcaResponseDTO>> marcas = marcaService.buscarActivas()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(marcas,
                linkTo(methodOn(MarcaControllerV2.class).buscarActivas()).withSelfRel(),
                linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withRel("marcas"));
    }

    @GetMapping(value = "/buscar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar marcas por nombre con HATEOAS")
    public CollectionModel<EntityModel<MarcaResponseDTO>> buscarPorNombre(
            @RequestParam String nombre) {

        List<EntityModel<MarcaResponseDTO>> marcas = marcaService.buscarPorNombre(nombre)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(marcas,
                linkTo(methodOn(MarcaControllerV2.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(MarcaControllerV2.class).obtenerTodas()).withRel("marcas"));
    }
}
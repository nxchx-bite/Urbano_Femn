package UrbanoFemn.EV2.assemblers;

import UrbanoFemn.EV2.controller.CategoriaControllerV2;
import UrbanoFemn.EV2.dto.CategoriaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaResponseDTO, EntityModel<CategoriaResponseDTO>> {

    @Override
    public EntityModel<CategoriaResponseDTO> toModel(CategoriaResponseDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerPorId(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).obtenerTodas()).withRel("categorias"),
                linkTo(methodOn(CategoriaControllerV2.class).buscarActivas()).withRel("categorias-activas"),
                linkTo(methodOn(CategoriaControllerV2.class).buscarPorNombre(categoria.getNombre())).withRel("buscar-por-nombre")
        );
    }
}